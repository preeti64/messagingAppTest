package com.example.messagingapp.service.tests;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.MessageRepository;
import com.example.messagingapp.service.MessageServiceImpl;
import com.example.messagingapp.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void testGetReceivedMessages() {
        User receiver = new User();
        receiver.setId(1L);
        Message message = new Message();
        message.setMessageId(1L);
        message.setMessageContent("Test message content");
        message.setSender(new User());
        message.setReceiver(receiver);

        when(messageRepository.findByReceiver(receiver)).thenReturn(Collections.singletonList(message));

        List<Message> receivedMessages = messageService.getReceivedMessages(receiver);

        assertNotNull(receivedMessages);
        assertFalse(receivedMessages.isEmpty());
        assertEquals(1, receivedMessages.size());
        assertEquals(message.getMessageId(), receivedMessages.get(0).getMessageId());
        assertEquals(message.getMessageContent(), receivedMessages.get(0).getMessageContent());
        assertEquals(message.getReceiver(), receivedMessages.get(0).getReceiver());
        assertEquals(message.getSender(), receivedMessages.get(0).getSender());
    }

}

