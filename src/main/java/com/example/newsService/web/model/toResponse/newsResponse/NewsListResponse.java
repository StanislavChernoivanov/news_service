package com.example.newsService.web.model.toResponse.newsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewsListResponse {
    private List<NewsResponseWithCommentsAmount> news;
}
