package com.example.messagingapp.controller;

import com.example.messagingapp.repository.dto.MessageDTO;
import com.example.messagingapp.service.model.Message;
import com.example.messagingapp.service.model.User;
import com.example.messagingapp.service.MessageServiceImpl;
import com.example.messagingapp.controller.model.SendMessageRequest;
import com.example.messagingapp.service.UserServiceImpl;
import io.swagger.annotations.ApiResponses;
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
        this.userService = userService;
    }

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Message sent successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            Long senderId = request.getSenderId();
            Long receiverId = request.getReceiverId();

            if (senderId == null || receiverId == null) {
                throw new IllegalArgumentException("Sender ID or Receiver ID cannot be null");
            }

            User sender = userService.findUserById(senderId);
            User receiver = userService.findUserById(receiverId);

            messageServiceImpl.sendMessage(sender, receiver, request.getContent()).join();
            return ResponseEntity.ok("Message sent successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Received messages found successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/received", method = RequestMethod.GET)
    public ResponseEntity<List<MessageDTO>> getReceivedMessages(@RequestParam Long userId) {
        try {
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Sent messages found successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/sent", method = RequestMethod.GET)
    public ResponseEntity<List<MessageDTO>> getSentMessages(@RequestParam Long userId) {
        try {
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Messages found successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/getMessagesFromUser", method = RequestMethod.GET)
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
