package com.example.teamStack.services.serviceImpli;

import com.example.teamStack.entity.Employee;
import com.example.teamStack.exceptions.EmployeeNotFound;
import com.example.teamStack.repository.EmployeeRepository;
import com.example.teamStack.services.EmailService;
import com.example.teamStack.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImplements implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImplements.class);

    @Override
    public void saveEmployee(Employee saveEmp) {
        log.info("Saving new employee with email: {}", saveEmp.getEmail());
        employeeRepository.save(saveEmp);
        log.debug("Employee saved successfully with ID: {}", saveEmp.getId());
    }

    @Override
    public void updateEmployee(Employee update) {
        log.info("Updating employee with id: {}", update.getId());
        Employee employee = employeeRepository.findById(update.getId())
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", update.getId());
                    return new EmployeeNotFound("Employee Not-Found");
                });
        employee.setName(update.getName());
        employee.setEmail(update.getEmail());
        employee.setPhone(update.getPhone());
        employee.setRole(update.getRole());
        employee.setSalary(update.getSalary());
        employee.setJoiningDate(update.getJoiningDate());
        employee.setCity(update.getCity());
        employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", employee.getId());
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        log.info("Attempting to delete employee with id: {}", id);
        employeeRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Employee not found for deletion with id: {}", id);
                    return new EmployeeNotFound("Employee Not Found: " + id);
                });
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        log.debug("Fetching employee by id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Employee Not-Found with id: {}", id);
                    return new EmployeeNotFound("Employee Not Found" + id);
                });
    }

    @Override
    public List<Employee> getEmployeeByAdmin(Long id) {
        log.debug("Fetching employees for admin id: {}", id);
        List<Employee> employees = employeeRepository.findByAdminId(id);
        log.info("Total employees found for admin {}: {}", id, employees.size());
        return employees;
    }

    @Override
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public void deleteByAdminId(Long adminId) {
        employeeRepository.deleteByAdminId(adminId);
    }
}
