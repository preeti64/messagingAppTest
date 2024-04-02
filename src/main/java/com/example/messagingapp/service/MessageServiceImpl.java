package com.example.messagingapp.service;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageServiceImpl {

    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final UserServiceImpl userService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, KafkaTemplate<String, Message> kafkaTemplate, UserServiceImpl userService) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
    }

    @Async
    public CompletableFuture<Message> sendMessage(User sender, User receiver, String messageContent) {
        if (!userService.isValidUserId(sender.getId()) || !userService.isValidUserId(receiver.getId())) {
            throw new IllegalArgumentException("Invalid user ID(s) provided.");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(messageContent);

        kafkaTemplate.send("received-messages", message);
        Message savedMessage = messageRepository.save(message);

        return CompletableFuture.completedFuture(savedMessage);
    }

    public List<Message> getReceivedMessages(User receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    public List<Message> getSentMessages(User sender) {
        return messageRepository.findBySender(sender);
    }

    public List<Message> getMessagesFromUser(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }
    public boolean hasReceivedMessages(User receiver) {
        List<Message> receivedMessages = getReceivedMessages(receiver);
        return !receivedMessages.isEmpty();
    }
    public boolean hasSentMessages(User sender) {
        List<Message> sentMessages = getSentMessages(sender);
        return !sentMessages.isEmpty();
    }
    public boolean hasSentMessagesToReceiver(User sender, User receiver) {
        List<Message> sentMessagesToReceiver = getMessagesFromUser(sender, receiver);
        return !sentMessagesToReceiver.isEmpty();
    }

}
