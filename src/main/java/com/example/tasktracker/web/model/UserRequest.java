package com.example.tasktracker.web.model;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
}
