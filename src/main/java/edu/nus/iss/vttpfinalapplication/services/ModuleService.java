package edu.nus.iss.vttpfinalapplication.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ModuleService {

    private static final String NUS_API = "https://api.nusmods.com/v2/2021-2022/moduleInfo.json";

    RestTemplate template = new RestTemplate();

    public List<String> getISSModule() {

        List<String> issModule = new LinkedList<>();

        ResponseEntity<String> resp = template.getForEntity(NUS_API, String.class);

        if(resp.getStatusCodeValue() == 200) {
            try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
                
                JsonReader reader = Json.createReader(is);
                JsonArray data = reader.readArray();

                data.stream()
                    .map(v -> (JsonObject) v)
                    .forEach( o -> {
                        if(o.getString("faculty").contentEquals("Institute of Systems Science")) {
                            issModule.add(o.getString("title"));
                        }
                    });

                // for(String module : issModule) {
                //     System.out.println("module: " + module);
                // }
                
            } catch(Exception e) {
                System.out.println("Cannot get information from NUS Module API");
            }
        } 
        return issModule;
    }
    
}
