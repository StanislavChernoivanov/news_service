package com.example.newsService.web.model.toResponse.newsResponse;

import com.example.newsService.web.model.toResponse.newsResponse.NewsResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class NewsListResponse {
    private List<NewsResponseWithCommentsAmount> newsResponseList;
}
