package com.example.messagingapp.repository;

import com.example.messagingapp.service.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByNickName(String nickName);
    List<User> findByNickNameStartingWith(String nickName);
     //List<User> findByNickNameStartingWith(String nickName, Sort sort);
}
