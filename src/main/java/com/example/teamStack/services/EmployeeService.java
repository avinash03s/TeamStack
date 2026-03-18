package com.example.teamStack.services;

import com.example.teamStack.entity.Employee;

import java.util.List;

public interface EmployeeService {
    public void saveEmployee(Employee saveEmp);
    public void updateEmployee(Employee update);
    public List<Employee> getAllEmployees();
    public void deleteEmployee(Long id);
    public Employee getEmployeeById(Long id);
    public List<Employee> getEmployeeByAdmin(Long id);
    boolean emailExists(String email);
}
