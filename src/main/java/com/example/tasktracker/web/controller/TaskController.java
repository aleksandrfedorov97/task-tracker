package com.example.tasktracker.web.controller;

import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.web.mapper.TaskMapper;
import com.example.tasktracker.web.model.TaskAddObserverRequest;
import com.example.tasktracker.web.model.TaskCreateRequest;
import com.example.tasktracker.web.model.TaskResponse;
import com.example.tasktracker.web.model.TaskUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public Flux<TaskResponse> findAll() {
        return taskService.findAll().map(taskMapper::taskToTaskResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody @Valid TaskCreateRequest request) {
        return taskService.create(taskMapper.taskCreateRequestToTask(request))
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> update(@RequestBody @Valid TaskUpdateRequest request, @PathVariable String id) {
        return taskService.update(taskMapper.taskUpdateRequestToTask(id, request))
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/observer/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@RequestBody @Valid TaskAddObserverRequest request, @PathVariable String id) {
        return taskService.addObserver(id, request.getObserverId())
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return taskService.delete(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
