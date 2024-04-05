package com.example.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private String authorId;

    @ReadOnlyProperty
    private User author;
    private String assignedId;

    @ReadOnlyProperty
    private User assignee;
    private Set<String> observerIds = new HashSet<>();

    @ReadOnlyProperty
    private Set<User> observers;

    public enum TaskStatus {
        TODO,
        IN_PROGRESS,
        DONE
    }
}
