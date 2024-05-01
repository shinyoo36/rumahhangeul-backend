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
    String username;
    @Column(name = "nama_depan")
    String namaDepan;
    @Column(name = "nama_belakang")
    String namaBelakang;
    @Column(name = "password")
    String password;
    @Column(name = "email")
    String email;
    @Column(name = "score")
    Long score;
    @Column(name = "point")
    Long point;
    @Column(name = "border_used")
    String borderUsed;
    @Column(name = "profile_used")
    String profileUsed;

}
