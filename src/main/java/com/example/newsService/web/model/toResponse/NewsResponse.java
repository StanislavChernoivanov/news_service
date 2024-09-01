package com.example.newsService.web.model.toResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    private Long id;
    private String header;
    private String description;

    private List<CommentResponse> comment;
    private Integer commentsAmount;
}
