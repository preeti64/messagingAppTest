package com.example.messagingapp.controller.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageRequest {
    private Long senderId;
    private Long receiverId;
    private String content;

}
