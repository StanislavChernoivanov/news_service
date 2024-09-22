package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertCommentRequest {

    @NotBlank(message = "Строка комментария должна быть заполнена")
    @Size(min = 1, max = 1000, message = "Комментарий должнен быть не менее {min} символов и не более {max} символов")
    private String comment;
}
