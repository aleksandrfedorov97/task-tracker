package com.example.tasktracker.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskAddAssigneeRequest {
    @NotBlank
    private String assigneeId;
}
