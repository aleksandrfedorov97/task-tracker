package com.example.tasktracker.web.model;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String status;
    private UserResponse author;
    private UserResponse assignee;
    private Set<UserResponse> observers;
}
