package com.example.messagingapp.repository;

import com.example.messagingapp.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickName(String nickName);
}
