package com.example.newsService.web.model.filters;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter {

    private Long userId;

    private Long newsCategoryId;

}
