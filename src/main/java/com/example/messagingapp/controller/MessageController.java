package com.example.messagingapp.controller;

import com.example.messagingapp.dto.MessageDTO;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.service.MessageServiceImpl;
import com.example.messagingapp.bean.SendMessageRequest;
import com.example.messagingapp.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageServiceImpl messageServiceImpl;
    private final UserServiceImpl userService;

    public MessageController(MessageServiceImpl messageServiceImpl, UserServiceImpl userService) {
        this.messageServiceImpl = messageServiceImpl;
        this.userService =  userService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();

        if (senderId == null || receiverId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender ID or Receiver ID cannot be null");
        }
        try {
            User sender = userService.findUserById(senderId);
            User receiver = userService.findUserById(receiverId);

            if (sender.getId().equals(receiver.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot send a message to yourself");
            }
            Message message = messageServiceImpl.sendMessage(sender, receiver, request.getContent()).join();
            return ResponseEntity.ok(message);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageDTO>> getReceivedMessages(@RequestParam Long userId) {
        try {
                if (userId == null) {
                    return ResponseEntity.badRequest().build();
                }
            User sender = userService.findUserById(userId);

            List<Message> sentMessages = messageServiceImpl.getReceivedMessages(sender);
            List<MessageDTO> messageDTOs = sentMessages.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<List<MessageDTO>> getSentMessages(@RequestParam Long userId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().build();
            }
            User sender = userService.findUserById(userId);
            List<Message> sentMessages = messageServiceImpl.getSentMessages(sender);
            List<MessageDTO> messageDTOs = sentMessages.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/from")
    public ResponseEntity<List<MessageDTO>> getMessagesFromUser(@RequestParam Long senderId, @RequestParam Long receiverId) {
        try {
            User sender = userService.findUserById(senderId);
            User receiver = userService.findUserById(receiverId);
            List<Message> messagesFromUser = messageServiceImpl.getMessagesFromUser(sender, receiver);
            List<MessageDTO> messageDTOs = messagesFromUser.stream()
                    .map(message -> new MessageDTO(message.getMessageId(), message.getMessageContent()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
