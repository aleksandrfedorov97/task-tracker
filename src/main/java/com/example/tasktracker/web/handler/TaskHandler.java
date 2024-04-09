package com.example.tasktracker.web.handler;

import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.web.mapper.TaskMapper;
import com.example.tasktracker.web.model.TaskAddObserverRequest;
import com.example.tasktracker.web.model.TaskCreateRequest;
import com.example.tasktracker.web.model.TaskResponse;
import com.example.tasktracker.web.model.TaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskHandler {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    public Mono<ServerResponse> getAllTask(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        taskService.findAll().map(taskMapper::taskToTaskResponse),
                        TaskResponse.class
                );
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return taskService.findById(serverRequest.pathVariable("id"))
                .map(taskMapper::taskToTaskResponse)
                .flatMap(response -> {
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                    taskService.findById(serverRequest.pathVariable("id")).map(taskMapper::taskToTaskResponse),
                                    TaskResponse.class
                            );
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskCreateRequest.class)
                .flatMap(request -> {
                    return taskService.create(taskMapper.taskCreateRequestToTask(request))
                            .map(taskMapper::taskToTaskResponse)
                            .flatMap(response -> {
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response);
                            });
                });
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskUpdateRequest.class)
                .flatMap(request -> {
                    return taskService.update(
                            taskMapper.taskUpdateRequestToTask(serverRequest.pathVariable("id"), request)
                    ).map(taskMapper::taskToTaskResponse);
                }).flatMap(response -> {
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addObserver(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TaskAddObserverRequest.class)
                .flatMap(request -> {
                    return taskService.addObserver(serverRequest.pathVariable("id"), request.getObserverId())
                            .map(taskMapper::taskToTaskResponse);
                }).flatMap(response -> {
                    return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response, TaskResponse.class);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return taskService.delete(serverRequest.pathVariable("id")).then(
                ServerResponse.noContent().build()
        ).switchIfEmpty(ServerResponse.notFound().build());
    }
}
