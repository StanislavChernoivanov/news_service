package com.example.newsService.web.model.toResponse.userResponse;

import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {

    protected Long id;
    protected String name;
    protected String surname;
    protected List<CommentResponse> commentsList;
    private List<NewsResponseWithCommentsAmount> newsList;

}
