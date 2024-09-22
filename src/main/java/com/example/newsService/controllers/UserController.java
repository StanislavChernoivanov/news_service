package com.example.newsService.controllers;

import com.example.newsService.mapper.UserMapper;
import com.example.newsService.model.entities.User;
import com.example.newsService.services.UserService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertUserRequest;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import com.example.newsService.web.model.toResponse.userResponse.UserListResponse;
import com.example.newsService.web.model.toResponse.userResponse.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Tag(name = "User V1", description = "User API V1")
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping
    @Operation(
            summary = "Get users",
            description = "Get all users, using user id for access check, return users with news lists",
            tags = {"user"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation =  UserListResponse.class)
                    )
            )
    })
    public ResponseEntity<UserListResponse> findAll
            (@RequestBody @Valid RequestPageableModel model) {

        return ResponseEntity.ok(
                mapper.userListToUserResponseList(
                        service.findAll(model)
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            description = "Get user by id, return user with news list",
            tags = {"user", "user id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(
                mapper.userToResponse(service.findById(userId))
        );
    }




    @PostMapping
    @Operation(
            summary = "Create user",
            description = "Create user, return user",
            tags = {"user"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UpsertUserRequest upsertUserRequest) {
        User user = service.save(
                mapper.requestToUser(upsertUserRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.userToResponse(user));
    }




    @PutMapping("/{id}")
    @Operation(
            summary = "Update user by id",
            description = "Update user by id, return user with news list",
            tags = {"user", "user id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<UserResponse> update(
            @PathVariable("id") Long userId,
            @RequestBody @Valid UpsertUserRequest request) {
        User user = service.update(userId,
                mapper.requestToUser(userId, request));
        return ResponseEntity.ok(mapper.userToResponse(user));
    }




    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user by id",
            description = "Delete user by id",
            tags = {"user", "user id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Long userId) {
        service.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
