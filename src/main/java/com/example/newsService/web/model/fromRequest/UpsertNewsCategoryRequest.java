package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class UpsertNewsCategoryRequest {

    @NotBlank(message = "Строка категории новостей должна быть заполнена")
    @Size(min = 3, max = 50, message = "Категория должна содержать не менее {min} символов и не более {max} символов")
    private String category;
}
