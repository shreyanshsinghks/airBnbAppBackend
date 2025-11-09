package com.shreyanshsinghks.airbnbapp.entity;

import com.shreyanshsinghks.airbnbapp.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // encode it

    private String name;

    @ElementCollection(fetch = FetchType.EAGER) // will create a new table with roles
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
