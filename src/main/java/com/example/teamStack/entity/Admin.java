package com.example.teamStack.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin", indexes = {@Index(name = "idx_admin_email", columnList = "email"),
@Index(name = "idx_admin_password",columnList = "password")})//indexing
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+.-]+@[A-Za-z0-9+.-]+\\.(com|co\\.in|org)$")
    private String email;

//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")
    private String password;

    private String city;

    private String photoPath;
}

