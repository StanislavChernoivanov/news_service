package com.example.newsService.controllers;

import com.example.newsService.aop.CheckAccess;
import com.example.newsService.mapper.UserMapper;
import com.example.newsService.model.entities.RoleType;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody @Valid RequestPageableModel model) {

        List<User> users = service.findAll(model);

        return ResponseEntity.ok(
                UserListResponse.setRoleTypes(mapper.userListToUserResponseList(
                        service.findAll(model))
                , users.stream().map(User::getRoles).toList())
        );
    }

    @GetMapping("/{id}")
    @CheckAccess
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
    public ResponseEntity<UserResponse> findById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long userId) {

        User user = service.findById(userId);
        return ResponseEntity.ok(
                UserResponse.setRoleTypes(mapper.userToResponse(user), user.getRoles())
        );
    }




    @PostMapping
    @Operation(
            summary = "Create new account",
            description = "Create new account, return new user",
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
            @RequestBody @Valid UpsertUserRequest upsertUserRequest,
            @RequestParam RoleType roleType) {
        User user = service.createNewAccount(
                mapper.requestToUser(upsertUserRequest), roleType);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(UserResponse.setRoleTypes(mapper.userToResponse(user), user.getRoles()));
    }




    @PutMapping("/{id}")
    @Operation(
            summary = "Update user's username and password by id",
            description = "Update user's username and password by id, return updated user",
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
    @CheckAccess
    public ResponseEntity<UserResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long userId,
            @RequestBody @Valid UpsertUserRequest request) {

        User user = service.update(userId,
                mapper.requestToUser(userId, request));
        return ResponseEntity.ok(mapper.userToResponse(user));
    }




    @DeleteMapping("/{id}")
    @CheckAccess
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
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable("id") Long userId) {

        service.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
