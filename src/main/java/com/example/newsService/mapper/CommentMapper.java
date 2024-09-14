package com.example.newsService.mapper;

import com.example.newsService.model.entities.Comment;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.toResponse.commentResponse.CommentListResponse;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment requestToComment(UpsertCommentRequest upsertCommentRequest);
    @Mapping(source = "commentId", target = "id")
    Comment requestToComment(Long commentId, UpsertCommentRequest upsertCommentRequest);

    CommentResponse commentToResponse(Comment comment);

    List<CommentResponse> commentListToResponseList(List<Comment> comments);

    default CommentListResponse commentListToCommentResponseList(List<Comment> comments){
        CommentListResponse commentListResponse = new CommentListResponse();
        commentListResponse.setCommentList(commentListToResponseList(comments));
        return commentListResponse;
    }
}
