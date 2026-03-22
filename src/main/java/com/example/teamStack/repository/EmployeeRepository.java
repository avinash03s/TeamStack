package com.example.teamStack.repository;

import com.example.teamStack.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee> findByAdminId(Long userId);
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Employee e WHERE e.admin.id = :adminId")
    void deleteByAdminId(@Param("adminId") Long adminId);
}
