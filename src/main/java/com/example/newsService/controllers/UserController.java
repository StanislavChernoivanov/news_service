package com.example.newsService.controllers;

import com.example.newsService.mapper.NewsMapper;
import com.example.newsService.mapper.UserMapper;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.fromRequest.UpsertUserRequest;
import com.example.newsService.web.model.toResponse.NewsListResponse;
import com.example.newsService.web.model.toResponse.NewsResponse;
import com.example.newsService.web.model.toResponse.UserListResponse;
import com.example.newsService.web.model.toResponse.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.userListToUserResponseList(
                        service.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(
                mapper.userToResponse(service.findById(userId))
        );
    }


    @PostMapping
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UpsertUserRequest upsertUserRequest) {
        User user= service.save(
                mapper.requestToUser(upsertUserRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.userToResponse(user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable("id") Long userId,
            @RequestBody @Valid UpsertUserRequest request) {
        User user = service.update(userId,
                mapper.requestToUser(userId, request));
        return ResponseEntity.ok(mapper.userToResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("{id}") Long userId) {
        service.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
