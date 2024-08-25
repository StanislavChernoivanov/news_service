package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertCommentRequest {
    @NotNull(message = "ID пользователя должно быть указано")
    @Positive(message = "ID пользователя должно быть не менее 1")
    private Long userId;

    @NotNull(message = "ID новости должно быть указано")
    @Positive(message = "ID новости должно быть не менее 1")
    private Long newsId;

    @NotBlank(message = "Строка комментария должна быть заполнена")
    @Size(min = 1, max = 1000, message = "Комментарий должнен быть не менее {min} символов и не более {max} символов")
    private String comment;
}
