package com.example.tasktracker.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class Task {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private String authorId;
    private User author;
    private String assignedId;
    private User assignee;
    private Set<String> observerIds;
    private Set<User> observers;

    public enum TaskStatus {
        TODO,
        IN_PROGRESS,
        DONE
    }
}
