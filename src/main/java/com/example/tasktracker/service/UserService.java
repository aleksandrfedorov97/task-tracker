package com.example.tasktracker.service;

import com.example.tasktracker.model.RoleType;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        User user = User.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426655440000").toString())
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@mail.com")
                .roles(Set.of(RoleType.ROLE_MANAGER))
                .build();

        findById(user.getId()).defaultIfEmpty(userRepository.save(user).block());
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Flux<User> findAllById(Set<String> ids) {
        return userRepository.findAllById(ids);
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Boolean> existsById(String id) {
        return userRepository.existsById(id);
    }

    public Mono<User> create(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> update(User user) {
        return findById(user.getId()).flatMap(userForUpdate -> {
            if (StringUtils.hasText(user.getUsername())) {
                userForUpdate.setUsername(user.getUsername());
            }

            if (StringUtils.hasText(user.getEmail())) {
                userForUpdate.setEmail(user.getEmail());
            }

            return userRepository.save(userForUpdate);
        });
    }

    public Mono<Void> delete(String id) {
        return userRepository.deleteById(id);
    }
}
