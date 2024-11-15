package com.example.newsService.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;

    private String description;

    @CreationTimestamp
    private Instant createAt;

    @JoinColumn(name = "news_category_id")
    @ManyToOne
    @ToString.Exclude
    private NewsCategory newsCategory;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> commentsList = new ArrayList<>();

    @Transient
    private Integer commentsAmount;

    @Transient
    private String category;

    @Transient
    private Long categoryId;


}
