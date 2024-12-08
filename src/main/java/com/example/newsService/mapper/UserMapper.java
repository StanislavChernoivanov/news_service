package com.example.newsService.mapper;

import com.example.newsService.model.entities.User;
import com.example.newsService.web.model.fromRequest.UpsertUserRequest;
import com.example.newsService.web.model.toResponse.userResponse.UserListResponse;
import com.example.newsService.web.model.toResponse.userResponse.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest upsertUserRequest);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UpsertUserRequest upsertUserRequest);


    @Mapping(source = "roleTypes", target = "roles")
    UserResponse userToResponse(User user);

    List<UserResponse> userListToResponseList(List<User> users);

    default UserListResponse userListToUserResponseList(List<User> users) {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setUsers(userListToResponseList(users));
        return userListResponse;

    }
}
