package com.example.messagingapp.service;

import com.example.messagingapp.service.model.Message;
import com.example.messagingapp.service.model.User;
import com.example.messagingapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Long senderId = sender.getId();
        Long receiverId = receiver.getId();

        if (userService.ifValidUserIdExists(sender.getId()) || userService.ifValidUserIdExists(receiver.getId())) {
            throw new IllegalArgumentException("Invalid user ID(s) provided.");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send a message to yourself");
        }

            User userSender = userService.findUserById(senderId);
            User userReceiver = userService.findUserById(receiverId);

            Message message = new Message();
            message.setSender(userSender);
            message.setReceiver(userReceiver);
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


}
