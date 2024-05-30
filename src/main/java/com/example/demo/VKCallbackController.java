package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VKCallbackController {

    @Autowired
    ChatBot chatBot;
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody String requestBody){
        System.out.println("Request body: " + requestBody);

        //TODO
        JsonNode requestNodes = null;
        try {
            requestNodes = mapper.readTree(requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(requestNodes != null){
            String type = requestNodes.get("type").asText();
            if(type.contains("confirmation")){
                String confirmationCode = chatBot.getConfirmationCode(requestNodes);
                return ResponseEntity.ok(confirmationCode);
            }

            if(type.contains("message_new")) chatBot.duplicateUserMessage(requestNodes);
        }
        return ResponseEntity.ok("ok");
    }
}
