package com.example.messagingapp.tests;

import com.example.messagingapp.bean.SendMessageRequest;
import com.example.messagingapp.controller.MessageController;
import com.example.messagingapp.dto.MessageDTO;
import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTests {

    @Mock
    private MessageServiceImpl messageService;

    @InjectMocks
    private MessageController messageController;

    @Mock
    private UserRepository userRepository;

    @Test
    void testSendMessageValidRequest() {

        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        when(messageService.sendMessage(sender, receiver, "Hello"))
                .thenThrow(new IllegalArgumentException("Cannot send a message to yourself"));

        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setContent("Hello");

        ResponseEntity<?> response = messageController.sendMessage(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSendMessageSelfMessage() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        request.setReceiverId(1L);
        request.setContent("Hello");

        ResponseEntity<?> response = messageController.sendMessage(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetReceivedMessagesValidRequest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getReceivedMessages(user)).thenReturn(mockMessages);

        ResponseEntity<?> response = messageController.getReceivedMessages(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetSentMessagesValidRequest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getSentMessages(user)).thenReturn(mockMessages);

        ResponseEntity<List<MessageDTO>> response = messageController.getSentMessages(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetMessagesFromUserValidRequest() {
        User sender = new User();
        sender.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));

        User receiver = new User();
        receiver.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageService.getMessagesFromUser(sender, receiver)).thenReturn(mockMessages);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesFromUser(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages.size(), Objects.requireNonNull(response.getBody()).size());
    }
}

