package com.example.newsService.model.entities;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Comment> commentList;


    public void addComment(Comment comment) {
        commentList.add(comment);
    }


    public void removeComment(Long commentId) {
        commentList = commentList.stream().filter(c -> !c.getId().equals(commentId)).collect(Collectors.toList());
    }


}
