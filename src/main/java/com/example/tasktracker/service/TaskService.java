package com.example.tasktracker.service;

import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(this::loadRelatedEntities);
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id).flatMap(this::loadRelatedEntities);
    }

    public Mono<Task> create(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(task.getCreatedAt());

        if (task.getStatus() == null) {
            task.setStatus(Task.TaskStatus.TODO);
        }

        Mono<Boolean> assignee = Mono.just(true);

        if(task.getAssignedId() != null) {
            assignee = userService.existsById(task.getAssignedId());
        }

        return Mono.zip(
                userService.existsById(task.getAuthorId()),
                assignee
        ).flatMap(
                data -> {
                    if (!data.getT1()) {
                        throw new EntityNotFoundException(MessageFormat.format("User with Id: {0} not found!", task.getAuthorId()));
                    }

                    if (!data.getT2()) {
                        throw new EntityNotFoundException(MessageFormat.format("User with Id: {0} not found!", task.getAssignedId()));
                    }

                    return taskRepository.save(task).flatMap(this::loadRelatedEntities);
                }
        );
    }

    public Mono<Task> update(Task task) {
        return findById(task.getId())
                .flatMap(taskForUpdate -> {
                    if(StringUtils.hasText(task.getName())) {
                        taskForUpdate.setName(task.getName());
                    }

                    if(StringUtils.hasText(task.getDescription())) {
                        taskForUpdate.setDescription(task.getDescription());
                    }

                    if(task.getStatus() != null) {
                        taskForUpdate.setStatus(task.getStatus());
                    }

                    Mono<Boolean> assignee = Mono.just(true);

                    if(StringUtils.hasText(task.getAssignedId())) {
                        assignee = userService.existsById(task.getAssignedId());
                        taskForUpdate.setAssignedId(task.getAssignedId());
                    }

                    return assignee.flatMap(exist -> {
                        if (!exist) {
                            throw new EntityNotFoundException(MessageFormat.format("User with Id: {0} not found!", task.getAssignedId()));
                        }

                        taskForUpdate.setUpdatedAt(Instant.now());
                        return taskRepository.save(taskForUpdate).flatMap(this::loadRelatedEntities);
                    });
                });
    }

    public Mono<Task> addObserver(String taskId, String observerId) {
        return findById(taskId).flatMap(
                task -> {
                    return userService.findById(observerId).flatMap(user -> {
                        task.getObserverIds().add(user.getId());
                        return taskRepository.save(task).flatMap(this::loadRelatedEntities);
                    }).switchIfEmpty(
                            Mono.error(new EntityNotFoundException(MessageFormat.format("User with Id: {0} not found!", observerId)))
                    );
                }
        );
    }

    public Mono<Void> delete(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> loadRelatedEntities(Task task) {

        Mono<User> user = Mono.just(new User());
        if (task.getAuthorId() != null) {
            user = userService.findById(task.getAuthorId())
                    .defaultIfEmpty(new User());;
        }

        Mono<User> assignee = Mono.just(new User());
        if (task.getAssignedId() != null) {
            assignee = userService.findById(task.getAssignedId())
                    .defaultIfEmpty(new User());
        }

        Flux<User> observers = Flux.just(new User());
        if (task.getObserverIds() != null) {
            observers = userService.findAllById(task.getObserverIds())
                    .defaultIfEmpty(new User());
        }

        return Mono.zip(
                user,
                assignee,
                observers.collectList()
        ).flatMap(data -> {
            if (data.getT1().getId() != null) {
                task.setAuthor(data.getT1());
            }

            if (data.getT2().getId() != null) {
                task.setAssignee(data.getT2());
            }

            if (data.getT3().get(0).getId() != null) {
                task.setObservers(new HashSet<User>(data.getT3()));
            } else {
                task.setObservers(new HashSet<>());
            }

            return Mono.just(task);
        });
    }
}
