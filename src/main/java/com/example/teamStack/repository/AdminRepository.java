package com.example.teamStack.repository;

import com.example.teamStack.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByEmail(String email);
}

