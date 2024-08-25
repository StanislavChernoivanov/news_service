package com.example.newsService.web.model.toResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserListResponse {

    List<UserResponse> users = new ArrayList<>();
}
