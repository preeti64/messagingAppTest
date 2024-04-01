package com.example.messagingapp.service;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private KafkaTemplate<String, Message> kafkaTemplate;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void testSendMessageToSelf() {
        User user = new User();
        user.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(user, user, "Hello");
        });

        verifyNoInteractions(messageRepository);
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void testGetReceivedMessages() {
        // Mock receiver user
        User receiver = new User();
        receiver.setId(1L);

        // Mock repository to return messages
        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageRepository.findByReceiver(receiver)).thenReturn(mockMessages);

        // Call getReceivedMessages method
        List<Message> result = messageService.getReceivedMessages(receiver);

        // Verify that messages are returned
        assertNotNull(result);
        assertEquals(mockMessages.size(), result.size());
    }

    @Test
    void testGetSentMessages() {
        // Mock sender user
        User sender = new User();
        sender.setId(1L);

        // Mock repository to return messages
        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageRepository.findBySender(sender)).thenReturn(mockMessages);

        // Call getSentMessages method
        List<Message> result = messageService.getSentMessages(sender);

        // Verify that messages are returned
        assertNotNull(result);
        assertEquals(mockMessages.size(), result.size());
    }

    @Test
    void testGetMessagesFromUser() {
        // Mock sender and receiver users
        User sender = new User();
        sender.setId(1L);
        User receiver = new User();
        receiver.setId(2L);

        // Mock repository to return messages
        List<Message> mockMessages = List.of(new Message(), new Message());
        when(messageRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(mockMessages);

        // Call getMessagesFromUser method
        List<Message> result = messageService.getMessagesFromUser(sender, receiver);

        // Verify that messages are returned
        assertNotNull(result);
        assertEquals(mockMessages.size(), result.size());
    }
}

