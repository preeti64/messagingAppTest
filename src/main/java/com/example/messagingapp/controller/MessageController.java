package com.example.messagingapp.controller;

import com.example.messagingapp.dto.MessageDTO;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.MessageServiceImpl;
import com.example.messagingapp.bean.SendMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageServiceImpl messageServiceImpl;

    private final UserRepository userRepository;

    public MessageController(MessageServiceImpl messageServiceImpl, UserRepository userRepository) {
        this.messageServiceImpl = messageServiceImpl;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            User sender = userRepository.findById(request.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            User receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
            if (sender.getId().equals(receiver.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot send a message to yourself");
            }

            Message message = messageServiceImpl.sendMessage(sender, receiver, request.getContent()).join();

            System.out.println("hi");
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getReceivedMessages(@RequestParam Long userId) {
        try {
            User receiver = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            boolean hasReceivedMessages = messageServiceImpl.hasReceivedMessages(receiver);
            if (!hasReceivedMessages) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user " + userId +"is not receiving any messages from any sender");
            }
            List<Message> receivedMessages = messageServiceImpl.getReceivedMessages(receiver);
            List<MessageDTO> messageDTOs = receivedMessages.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getSentMessages(@RequestParam Long userId) {
        try {
            User sender = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            boolean hasSentMessages = messageServiceImpl.hasSentMessages(sender);
            if (!hasSentMessages) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User" + userId +"is not sending any messages to any receiver");
            }
            List<Message> sentMessages = messageServiceImpl.getSentMessages(sender);
            List<MessageDTO> messageDTOs = sentMessages.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/from")
    public ResponseEntity<?> getMessagesFromUser(@RequestParam Long senderId, @RequestParam Long receiverId) {
        try {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new IllegalArgumentException("receiver not found"));
            boolean hasSentMessagesToReceiver = messageServiceImpl.hasSentMessagesToReceiver(sender, receiver);
            if (!hasSentMessagesToReceiver) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages sent from " +senderId +" to receiver " +receiverId);
            }
            List<Message> messagesFromUser = messageServiceImpl.getMessagesFromUser(sender, receiver);
            List<MessageDTO> messageDTOs = messagesFromUser.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
