package com.example.newsService.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity()
@Table(name = "news_categories", indexes = @Index(name = "NEWS_CATEGORY_INDEX"
        , unique = true
        , columnList = "category"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String category;

    @OneToMany(mappedBy = "newsCategory", cascade = CascadeType.MERGE)
    @ToString.Exclude
    @Builder.Default
    private List<News> newsList = new ArrayList<>();


//    public void addNews(News news) {
//        newsList.add(news);
//    }
//
//    public void removeNews(Long newsId) {
//        newsList = newsList.stream().filter(n -> !n.getId().equals(newsId)).collect(Collectors.toList());
//    }


}
