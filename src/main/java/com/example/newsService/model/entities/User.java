package com.example.newsService.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<News> newsList;


    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void removeComment(Long commentId) {
        commentList = commentList.stream().filter(c -> !c.getId().equals(commentId)).collect(Collectors.toList());
    }

    public void addNews(News news) {
        newsList.add(news);
    }

    public void removeNews(Long newsId) {
        newsList = newsList.stream().filter(n -> !n.getId().equals(newsId)).collect(Collectors.toList());
    }
}
