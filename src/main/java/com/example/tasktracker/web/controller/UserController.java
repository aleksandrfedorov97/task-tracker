package com.example.tasktracker.web.controller;

import com.example.tasktracker.service.UserService;
import com.example.tasktracker.web.mapper.UserMapper;
import com.example.tasktracker.web.model.UserRequest;
import com.example.tasktracker.web.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserResponse> findAll() {
        return userService.findAll().map(userMapper::userToUserResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody UserRequest request) {
        return userService.create(userMapper.userRequestToUser(request))
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id, @RequestBody UserRequest request) {
        return userService.update(userMapper.userRequestToUser(id, request))
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userService.delete(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
