/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.example.dto.DocumentDTO;
import com.example.dto.MainDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@SpringBootApplication
public class Main {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/form")
  String form() {
    return "formulario.html";
  }

  @RequestMapping("/send")
  String send(@RequestParam("comment") String comment) {
    HttpClient httpclient = HttpClients.createDefault();

    try
    {
        URIBuilder builder = new URIBuilder("https://westus.dev.cognitive.microsoft.com/docs/services/TextAnalytics.V2.0/operations/56f30ceeeda5650db055a3c9");


        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", "9958672d1ab346b2ac4c61fb3be6b36c");


        // Request body
        String body = "{\"documents\": [{\"language\": \"pt\",\"id\": \"1\",\"text\": \"#comment\"},]}";
        StringEntity reqEntity = new StringEntity(body.Replace("#comment", comment));
        request.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) 
        {
            String json = EntityUtils.toString(entity);

            ObjectMapper mapper = new ObjectMapper();
            MainDTO obj = mapper.readValue(json, MainDTO.class);

            if (obj.getDocuments() != null && obj.getDocuments().size() > 0) {
                DocumentDTO d = obj.getDocuments().get(0);

                if (d.getScore() != null) {
                    Double score = d.getScore();

                    System.out.println(score);

                    //TODO: Implementar decisão por scores
                }

            }
        }
    }
    catch (Exception e)
    {
        System.out.println(e.getMessage());
    }

    return "formulario.html";
  }

  @RequestMapping("/otimo")
  String otimo() {
    return "otimo.html";
  }

  @RequestMapping("/medio")
  String medio() {
    return "medio.html";
  }

  @RequestMapping("/ruim")
  String ruim() {
    return "ruim.html";
  }

}
