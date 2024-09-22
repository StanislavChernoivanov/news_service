package com.example.newsService.web.model.toResponse.newsCategoryResponse;

import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategoryResponse {

    private Long id;

    private String category;

    private List<NewsResponseWithCommentsAmount> newsList;
}
