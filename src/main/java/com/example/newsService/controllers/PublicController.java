package com.example.newsService.controllers;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/account")
@RequiredArgsConstructor
@Tag(name = "Public V1", description = "Public API V1")
public class PublicController {


    private final UserService service;

    private final UserMapper mapper;


    @PostMapping
    @Operation(
            summary = "Create new account",
            description = "Create new account, return user's data",
            tags = {"account"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
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
                body(mapper.userToResponse(user));
    }
}
