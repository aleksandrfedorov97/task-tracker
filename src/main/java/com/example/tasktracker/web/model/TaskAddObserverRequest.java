package com.example.tasktracker.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskAddObserverRequest {
    @NotBlank
    private String observerId;
}
