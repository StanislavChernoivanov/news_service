package com.example.newsService.web.model.toResponse.userResponse;

import com.example.newsService.model.entities.Role;
import com.example.newsService.model.entities.RoleType;
import com.example.newsService.model.entities.User;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    protected Long id;
    protected String username;
    private List<RoleType> roleTypes;
    protected List<CommentResponse> commentsList;
    private List<NewsResponseWithCommentsAmount> newsList;


    public static UserResponse setRoleTypes(UserResponse userResponse, List<Role> roles) {

        userResponse.setRoleTypes(roles.stream().map(Role::getAuthority).toList());
        return userResponse;
    }

}
