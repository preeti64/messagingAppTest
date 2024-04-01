package com.example.messagingapp.repository;

import com.example.messagingapp.model.Message;
import com.example.messagingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver(User receiver);
    List<Message> findBySender(User sender);
    List<Message> findBySenderAndReceiver(User sender, User receiver);

}
