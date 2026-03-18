package com.example.teamStack.repository;

import com.example.teamStack.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee> findByAdminId(Long userId);
    boolean existsByEmail(String email);
}
