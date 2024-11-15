package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertUserRequest {


    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Size(min = 2, max = 20, message = "Имя должно содержать не менее {min} символов и не более {max} символов")
    private String username;


    @NotBlank()
    @Size(min = 5, max = 20, message = "Пароль должен содержать не менее {min} символов и не более {max} символов")
    private String password;
}
