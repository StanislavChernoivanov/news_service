package com.example.newsService.web.model.toResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsCategoryResponse {

    private Long id;

    private String category;

    private List<NewsResponse> newsList = new ArrayList<>();
}
