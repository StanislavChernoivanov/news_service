package com.example.newsService.web.model.toResponse.userResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserListResponse {

    List<UserResponse> users;
}
