package com.example.demo;

public enum VkAPI {
    MESSAGES_SEND("https://api.vk.com/method/messages.send"),
    GET_CALLBACK_CONFIRMATION_CODE("https://api.vk.com/method/groups.getCallbackConfirmationCode");

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    VkAPI(String uri){
        this.uri = uri;
    }
}
