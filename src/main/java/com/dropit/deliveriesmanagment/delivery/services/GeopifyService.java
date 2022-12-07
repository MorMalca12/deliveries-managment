package com.dropit.deliveriesmanagment.delivery.services;


import com.dropit.deliveriesmanagment.delivery.dto.AddressDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Address;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeopifyService {


    private static String key;

    private static String geoapifyUrl;

    private static  ObjectMapper objectMapper ;

    @Autowired
    public GeopifyService(Environment env){
        key = env.getProperty("geopify.key");
        geoapifyUrl = env.getProperty("geopify.search.url");
        objectMapper = new ObjectMapper();
    }

    public AddressDto resolveAddress(String searchTerm)  {
        //Decode search Term
        searchTerm = searchTerm.replace(" ","%20").replace(",","%2C");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(geoapifyUrl + searchTerm + "&format=json&apiKey=" + key)
                .method("GET", null)
                .build();

        JsonNode rootNode = null;
        try {
            Response response = client.newCall(request).execute();
            rootNode = objectMapper.readTree(response.body().string()).path("results").path(0);
        } catch (IOException e) {
            //TODO:print detailed log error

        }

       if(rootNode != null){
           return new AddressDto(
                   rootNode.path("country").asText(),
                   rootNode.path("street").asText(),
                   rootNode.path("postcode").asText(),
                   rootNode.path("address_line1").asText(),
                   rootNode.path("address_line1").asText()
           );
       }else {
           return null;
       }
    }

}
