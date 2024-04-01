package com.example.messagingapp.service;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl{

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public Message sendMessage(User sender, User receiver, String messageContent) {
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Cannot send a message to yourself");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(messageContent);
        return messageRepository.save(message);
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
