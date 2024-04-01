package com.example.messagingapp.model;

import jakarta.persistence.*;
import org.apache.kafka.common.protocol.types.Field;

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
