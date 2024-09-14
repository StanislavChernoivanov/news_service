package com.example.newsService.web.model.toResponse.newsCategoryResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsCategoryListResponse {
    private List<NewsCategoryResponse> newsCategories = new ArrayList<>();
}
