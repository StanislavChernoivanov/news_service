package com.example.newsService.web.model.toResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsResponse {

    private Long id;
    private String header;
    private String description;
    private List<CommentResponse> comment = new ArrayList<>();
}
