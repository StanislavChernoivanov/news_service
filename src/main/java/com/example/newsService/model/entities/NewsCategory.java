package com.example.newsService.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "news_categories")
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "newsCategory", cascade = CascadeType.ALL)
    @Column(name = "news_list")
    @ToString.Exclude
    private List<News> newsList;


    public void addNews(News news) {
        newsList.add(news);
    }

    public void removeNews(Long newsId) {
        newsList = newsList.stream().filter(n -> !n.getId().equals(newsId)).collect(Collectors.toList());
    }


}
