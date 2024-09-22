package com.example.newsService.web.model.toResponse.newsResponse;

import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseWithCommentsList extends NewsResponse {


    private List<CommentResponse> commentsList;
}
