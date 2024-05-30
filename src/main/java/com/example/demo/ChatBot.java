package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
@Component
public class ChatBot {

    private final String access_token;

    @Autowired
    public ChatBot(@Value("${access_token}") String access_token) {
        this.access_token = access_token;
    }

    private String confirmationCode = null;
    ObjectMapper mapper = new ObjectMapper();

    public String getConfirmationCode(JsonNode requestNodes){

        if(this.confirmationCode != null) return this.confirmationCode;

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("access_token", access_token);
        formData.add("group_id", requestNodes.get("group_id").asText());
        formData.add("v", requestNodes.get("v").asText());

        ResponseEntity<String> response = send(formData, VkAPI.GET_CALLBACK_CONFIRMATION_CODE.getUri());

        JsonNode responseNodes = null;
        try {
            responseNodes = mapper.readTree(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(responseNodes.get("error") != null) return "";

        this.confirmationCode = responseNodes.get("response").get("code").asText();

        return this.confirmationCode;
    }

    public void duplicateUserMessage(JsonNode requestNodes){

        JsonNode objectNodes = requestNodes.get("object");
        JsonNode messageNodes = objectNodes.get("message");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("access_token", access_token);
        formData.add("random_id", "0");
        formData.add("group_id", requestNodes.get("group_id").asText());
        formData.add("v", requestNodes.get("v").asText());
        formData.add("peer_id", messageNodes.get("peer_id").asText());
        formData.add("message", "Вы сказали: " + messageNodes.get("text").asText());

        //TODO
        ResponseEntity<String> response = send(formData, VkAPI.MESSAGES_SEND.getUri());
    }

    private ResponseEntity<String> send(MultiValueMap<String, String> formData, String api){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        return restTemplate.postForEntity(api, requestEntity, String.class);
    }


}
