package com.example.teamStack.services;

import com.example.teamStack.entity.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(Employee saveEmp);
    void updateEmployee(Employee update);
    void deleteEmployee(Long id);
    Employee getEmployeeById(Long id);
    List<Employee> getEmployeeByAdmin(Long id);
    boolean emailExists(String email);
    void deleteByAdminId(Long adminId);
}
