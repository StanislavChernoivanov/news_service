package com.example.newsService.controllers;

import com.example.newsService.StringTestUtil;
import com.example.newsService.mapper.CommentMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.services.CommentService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.toResponse.commentResponse.CommentListResponse;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CommentControllerTest extends AbstractTestController {
    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentMapper commentMapper;
    @Test
    public void whenFindAllByNewsId_thenReturnAllCommentsByNewsId() throws Exception {
        List<Comment> comments = new ArrayList<>();
        Comment comment = createComment(1L, null, null);
        comments.add(comment);
        User user = createUser(1L);
        News news = createNews(1L, null, null);
        Comment comment1 = createComment(2L, user, news);
        comments.add(comment1);

        List<CommentResponse> commentResponseList;
        CommentResponse commentResponse = createCommentResponse(1L, "Comment " + 1);
        commentResponseList = List.of(commentResponse);

        CommentListResponse commentListResponse =
                new CommentListResponse(commentResponseList);

        Mockito.when(commentService.findAllByNewsId(1L)).thenReturn(List.of(comment));
        Mockito.when(commentMapper.commentListToCommentResponseList(
                        List.of(comment)
                ))
                .thenReturn(commentListResponse);

        String actualResponse = mockMvc.perform(get("/api/comment?newsId=1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/commentResponses/find_all_by_news_id_comments_response.json");

        Mockito.verify(commentService
                , Mockito.times(1)).findAllByNewsId(1L);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentListToCommentResponseList(List.of(comments.get(0)));

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }


    @Test
    public void whenFindById_thenReturnCommentById() throws Exception {
        Comment comment = createComment(1L, null, null);

        CommentResponse commentResponse = createCommentResponse(1L, "Comment " + 1);

        Mockito.when(commentService.findById(1L)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(comment))
                .thenReturn(commentResponse);

        String actualResponse = mockMvc.perform(get("/api/comment/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/commentResponses/find_by_id_comment_response.json");

        Mockito.verify(commentService
                , Mockito.times(1)).findById(1L);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentToResponse(comment);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }

    @Test
    public void whenCreate_thenReturnCreatedComment() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Comment");
        News news = News.builder().id(1L).header("News").description("News").build();
        User user = User.builder().name("User").surname("User").build();

        Comment createdComment = new Comment(1L, "Comment", user, news);
        CommentResponse commentResponse = new CommentResponse(1L, "Comment");
        UpsertCommentRequest request = new UpsertCommentRequest("Comment");

        Mockito.when(commentService.save(1L, 1L, comment)).thenReturn(createdComment);
        Mockito.when(commentMapper.requestToComment(request)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(createdComment)).thenReturn(commentResponse);

        String actualResponse = mockMvc.perform(post("/api/comment?newsId=1&userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/commentResponses/create_comment_response.json");

        Mockito.verify(commentService, Mockito.times(1))
                .save(1L, 1L, comment);
        Mockito.verify(commentMapper, Mockito.times(1))
                .requestToComment(request);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentToResponse(createdComment);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);

    }

    @Test
    public void whenUpdate_thenReturnUpdatedComment() throws Exception {
        Comment comment = new Comment();
        comment.setComment("Comment");
        Comment updatedComment = new Comment(1L, "Comment", new User(), new News());
        CommentResponse commentResponse = new CommentResponse(1L, "Comment");
        UpsertCommentRequest request = new UpsertCommentRequest("Comment");

        Mockito.when(commentService.update(1L, comment)).thenReturn(updatedComment);
        Mockito.when(commentMapper.requestToComment(1L, request)).thenReturn(comment);
        Mockito.when(commentMapper.commentToResponse(updatedComment)).thenReturn(commentResponse);

        String actualResponse = mockMvc.perform(put("/api/comment/1?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/commentResponses/update_comment_response.json");

        Mockito.verify(commentService, Mockito.times(1))
                .update(1L, comment);
        Mockito.verify(commentMapper, Mockito.times(1))
                .requestToComment(1L, request);
        Mockito.verify(commentMapper, Mockito.times(1))
                .commentToResponse(updatedComment);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);

    }

    @Test
    public void whenDelete_ThenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/comment/1?userId=1"))
                .andExpect(status().isNoContent());
        Mockito.verify(commentService, Mockito.times(1))
                .delete(1L, 1L);
    }
}
