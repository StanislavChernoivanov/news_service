package com.example.newsService.web.model.toResponse.commentResponse;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentListResponse {

    private List<CommentResponse> commentList = new ArrayList<>();
}
