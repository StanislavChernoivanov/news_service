package com.example.newsService.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(value = EnumType.STRING)
    private RoleType authority;

    @ManyToOne()
    @JoinColumn(name = "username")
    @ToString.Exclude
    private User user;


    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(authority.name());
    }


    public static Role toRole(RoleType roleType) {
        return Role.builder().authority(roleType).build();
    }
}
