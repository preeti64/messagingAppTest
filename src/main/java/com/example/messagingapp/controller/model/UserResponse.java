package com.example.messagingapp.controller.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String message;
    private boolean success;
    private HttpStatus httpStatus;

}
