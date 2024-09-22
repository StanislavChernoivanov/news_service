package com.example.newsService.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @ToString.Exclude
    private User user;

    @JoinColumn(name = "news_id")
    @ManyToOne
    @ToString.Exclude
    private News news;
}
