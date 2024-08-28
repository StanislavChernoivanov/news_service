package com.example.newsService.web.model.toResponse;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewsResponse {

    private Long id;
    private String header;
    private String description;
    private List<CommentResponse> comment;
    private Integer commentsAmount;
}
