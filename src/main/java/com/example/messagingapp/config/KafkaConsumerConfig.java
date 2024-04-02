
package com.example.messagingapp.config;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.service.MessageServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
class KafkaConsumerConfig {

    private final MessageServiceImpl messageService;

    public KafkaConsumerConfig(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "received-messages", groupId = "group-id")
    public void consumeNewMessage(Message message) {
        try {
            messageService.sendMessage(message.getSender(), message.getReceiver(), message.getMessageContent());
        }catch(Exception e){
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}

