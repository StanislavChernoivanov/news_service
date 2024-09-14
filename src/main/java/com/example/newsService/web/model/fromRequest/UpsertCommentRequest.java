package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertCommentRequest {

    @NotBlank(message = "Строка комментария должна быть заполнена")
    @Size(min = 1, max = 1000, message = "Комментарий должнен быть не менее {min} символов и не более {max} символов")
    private String comment;
}
