package com.example.messagingapp.service.model;

import jakarta.persistence.*;

@Entity
@Table(schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nickName;

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
