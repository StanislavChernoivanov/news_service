package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertNewsRequest {

    @NotNull(message = "ID категории должно быть указано")
    @Positive(message = "ID категории должно быть не менее 1")
    private Long categoryId;

    @NotBlank(message = "Строка заголовка должна быть заполнена")
    @Size(min = 3, max = 30, message = "Заголовок должен содержать не менее {min} символов и не более {max} символов")
    private String header;

    @NotBlank(message = "Описание должно быть заполнено")
    @Size(min = 3, max = 500, message = "Описание должно содержать не менее {min} символов и не более {max} символов")
    private String description;
}
