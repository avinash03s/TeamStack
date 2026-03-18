package com.example.teamStack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "emp_info",
        indexes = {
                @Index(name = "idx_emp_email", columnList = "email"),
                @Index(name = "idx_emp_admin", columnList = "admin_id")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    private String role;

    @Pattern(regexp = "^(\\+91)?[6-9]\\d{9}$")
    private String phone;

    @Pattern(regexp = "^[A-Za-z0-9+.-]+@[A-Za-z0-9+.-]+\\.(com|co\\.in|org)$")
    private String email;

    private double salary;

    private LocalDate joiningDate;

    private String city;

    @ManyToOne
    @JoinColumn(name = "admin_id") // FK column in emp_info table
    private Admin admin;

}
