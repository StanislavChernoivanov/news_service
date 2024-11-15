package com.example.newsService.web.model.toResponse.userResponse;

import com.example.newsService.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserListResponse {

    List<UserResponse> users;

    public static UserListResponse setRoleTypes(UserListResponse userResponses, List<List<Role>> roles) {

        for(int i = 0; i < userResponses.getUsers().size(); i++) {
            userResponses.getUsers().get(i).setRoleTypes(roles.get(i).stream().map(Role::getAuthority).toList());
        }

        return userResponses;
    }
}
