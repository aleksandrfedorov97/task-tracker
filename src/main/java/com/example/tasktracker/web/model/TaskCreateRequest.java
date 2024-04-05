package com.example.tasktracker.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateRequest {
    @NotBlank(message = "Task name cannot be empty!")
    private String name;
    private String description;
    private String status;
    @NotBlank(message = "Task author cannot be empty!")
    private String authorId;
    private String assignedId;
}
