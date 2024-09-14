package com.example.newsService.web.model.toResponse.newsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    protected Long id;
    protected String header;
    protected String description;
    protected String category;

}
