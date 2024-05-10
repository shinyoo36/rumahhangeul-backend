package com.stiven.rumahhangeul.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Challenge> challenges = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
    @Column(name = "username")
    private String username;
    @Column(name = "nama_depan")
    private String namaDepan;
    @Column(name = "nama_belakang")
    private String namaBelakang;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "score")
    private Long score;
    @Column(name = "point")
    private Long point;
    @Column(name = "border_used")
    private String borderUsed;
    @Column(name = "profile_used")
    private String profileUsed;

}
