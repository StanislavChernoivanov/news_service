package com.example.newsService.web.model.toResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private List<CommentResponse> comments = new ArrayList<>();
    private List<NewsResponse> newsList = new ArrayList<>();
}
