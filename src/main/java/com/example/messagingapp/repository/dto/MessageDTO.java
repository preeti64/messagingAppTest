package com.example.messagingapp.repository.dto;

public class MessageDTO {
    private Long messageId;
    private String messageContent;

    public MessageDTO(Long messageId, String messageContent) {
        this.messageId = messageId;
        this.messageContent = messageContent;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
