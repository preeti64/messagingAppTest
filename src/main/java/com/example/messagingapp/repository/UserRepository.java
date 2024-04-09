package com.example.messagingapp.repository;

import com.example.messagingapp.service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {
    User findByNickName(String nickName);

    List<User> findByNickNameStartingWith(String nickName, Sort sort);

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
}
