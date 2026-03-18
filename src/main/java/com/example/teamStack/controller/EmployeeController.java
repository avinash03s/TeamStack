package com.example.teamStack.controller;

import com.example.teamStack.entity.Admin;
import com.example.teamStack.entity.Employee;
import com.example.teamStack.exceptions.DuplicateEmailFound;
import com.example.teamStack.exceptions.EmployeeNotFound;
import com.example.teamStack.services.AdminService;
import com.example.teamStack.services.EmailService;
import com.example.teamStack.services.EmployeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/saveEmployee")
    public String showForm(Model model, HttpSession session) {
        log.info("Accessing Add Employee Form");
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized access attempt to Add Employee page");
            return "redirect:/login";
        }
        model.addAttribute("admin", admin);
        if (!model.containsAttribute("emp")) {
            log.debug("Initializing empty Employee object for form");
            model.addAttribute("emp", new Employee());
        }
        return "addEmployee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@Valid @ModelAttribute Employee employee,
                               RedirectAttributes redirectAttrs,
                               HttpSession session) {
        log.info("Request received to save new employee");
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized employee save attempt");
            return "redirect:/login";
        }
        employee.setAdmin(admin);/// IMP Line
        log.debug("Saving employee with name: {}", employee.getName());
       try {
           boolean isEmailExist = employeeService.emailExists(employee.getEmail());
           if (isEmailExist) {
               redirectAttrs.addFlashAttribute("errorMessage",
                       "Employee Email Id Already Present..!");
               log.error("Employee Email Id Already Present..!");
               return "redirect:/saveEmployee";
           }
           employeeService.saveEmployee(employee);
           log.info("Employee saved successfully");
           emailService.sendEmployeeWelcomeEmail(
                   employee.getEmail(),
                   employee.getName()
           );
           log.info("Email Message Send to Employee");
           redirectAttrs.addFlashAttribute("message", "Employee added successfully!");
       }catch (DuplicateEmailFound e){
           redirectAttrs.addFlashAttribute("errorMessage", "Employee Email Id Already Present..!");
       }
        return "redirect:/saveEmployee";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id,
                                 Model model,
                                 RedirectAttributes ra,
                                 HttpSession session) {
        log.info("Request received to update employee with id: {}", id);
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized access to update employee");
            return "redirect:/login";
        }
        Employee emp = employeeService.getEmployeeById(id);
        if (emp == null) {
            log.warn("Employee not found with id: {}", id);
            ra.addFlashAttribute("message", "Employee not found!");
            return "redirect:/home";
        }
        log.debug("Employee found for update: {}", emp.getName());
        model.addAttribute("emp", emp);
        model.addAttribute("admin", admin);
        model.addAttribute("editMode", true);
        return "addEmployee";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployee(@Valid @ModelAttribute Employee employee,
                                 HttpSession session,
                                 RedirectAttributes ra) {
        log.info("Updating employee with id: {}", employee.getId());
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized employee update attempt");
            return "redirect:/login";
        }
        employee.setAdmin(admin);
        employeeService.updateEmployee(employee);
        log.info("Employee updated successfully with id: {}", employee.getId());
        ra.addFlashAttribute("message", "Employee updated successfully!");
        return "redirect:/home";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id,
                                 RedirectAttributes redirectAttrs,
                                 HttpSession session) {
        log.info("Request received to delete employee with id: {}", id);
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized employee delete attempt");
            return "redirect:/login";
        }
        try {
            employeeService.deleteEmployee(id);
            log.info("Employee deleted successfully with id: {}", id);
            redirectAttrs.addFlashAttribute("message", "Employee deleted successfully!");
        } catch (EmployeeNotFound e) {
            log.error("Employee deletion failed. Employee not found with id: {}", id);
            redirectAttrs.addFlashAttribute("error", "Employee not found!");
        }
        return "redirect:/home";
    }

    @GetMapping("/adminProfile")
    public String showAdminProfile(HttpSession session, Model model) {
        log.info("Accessing admin profile page");
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized access to admin profile");
            return "redirect:/login";
        }
        List<Employee> empList = employeeService.getEmployeeByAdmin(admin.getId());
        long uniqueRoles = empList.stream()
                .map(Employee::getRole)
                .distinct()
                .count();
        log.debug("Admin {} has {} employees", admin.getName(), empList.size());
        model.addAttribute("admin", admin);
        model.addAttribute("totalEmployees", empList.size());
        model.addAttribute("uniqueRoles", uniqueRoles);
        return "adminProfile";
    }

    @PostMapping("/updateAdmin")
    public String updateAdminProfile(@Valid @ModelAttribute Admin admin, HttpSession session,
                                     RedirectAttributes re) {
        log.info("Admin profile update request received");
        Admin sessionAdmin = (Admin) session.getAttribute("admin");
        if (sessionAdmin == null) {
            log.warn("Unauthorized admin update attempt");
            return "redirect:/login";
        }
        admin.setId(sessionAdmin.getId());
        Admin updatedAdmin = adminService.updateAdmin(admin);
        session.setAttribute("admin", updatedAdmin);
        log.info("Admin profile updated successfully for admin id: {}", updatedAdmin.getId());
        re.addFlashAttribute("message", "Admin Update Successful..!");
        return "redirect:/adminProfile";
    }
}