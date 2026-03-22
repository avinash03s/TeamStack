package com.example.teamStack.controller;
import com.example.teamStack.entity.Admin;
import com.example.teamStack.entity.Employee;
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
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/register")
    public String showRegister() {
        log.info("Accessing admin registration page");
        return "register";
    }

    // Handle registration
    @PostMapping("/regForm")
    public String registerAdmin(@Valid @ModelAttribute Admin admin) {
        log.info("Admin registration request received for email: {}", admin.getEmail());
        adminService.registerAdmin(admin);
        emailService.registerSuccessful(admin.getEmail(),admin.getName());
        log.info("Admin registered successfully with email: {}", admin.getEmail());
        return "redirect:/login";
    }

    //show login
    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        log.info("Accessing login page");
        if (session.getAttribute("admin") != null) {
            log.warn("Admin already logged in, redirecting to home");
            return "redirect:/home";
        }
        return "login";
    }

    // Handle login
    @PostMapping("/loginForm")
    public String loginUser(@Valid @RequestParam String email,
                            @RequestParam String password,
                            HttpSession session, Model model) {
        log.info("Login attempt for email: {}", email);
        Admin admin = adminService.validateAdmin(email, password);
        if (admin != null) {
            log.info("Login successful for admin: {}", email);
            session.setAttribute("admin", admin);
            return "redirect:/home";
        } else {
            log.warn("Login failed for email: {}", email);
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        log.info("Accessing home dashboard");
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            log.warn("Unauthorized access attempt to home page");
            return "redirect:/login";
        }
        model.addAttribute("admin", admin);
        List<Employee> empList = employeeService.getEmployeeByAdmin(admin.getId());
        log.debug("Total employees fetched for admin {}: {}", admin.getEmail(), empList.size());
        model.addAttribute("empList", empList);
        model.addAttribute("totalEmployees", empList.size());

        long uniqueRoles = empList.stream()
                .map(Employee::getRole)
                .distinct()
                .count();
        model.addAttribute("uniqueRoles", uniqueRoles);
        return "home";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    //handling form
    @PostMapping("/forgotPasswordForm")
    public String forgotPassword(@RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        Admin admin = adminService.findByEmail(email);
        if(admin == null || !admin.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password");
            return "forgot-password";
        }
        model.addAttribute("email", email);
        return "reset-password";
    }

    //handling form
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String conformPassword,
                                RedirectAttributes redirectAttributes,
                                Model model){
        if(!password.equals(conformPassword)){
            model.addAttribute("error","Passwords do not match");
            model.addAttribute("email",email);
            return "reset-password";
        }
        Admin admin = adminService.findByEmail(email);
        adminService.updatePassword(email,password);
        emailService.passwordSuccessfullyUpdated(email, admin.getName());
        redirectAttributes.addFlashAttribute("success",
                "Password updated successfully. Please login.");
        return "redirect:/login";
    }

    @GetMapping("/delete-account")
    public String showDeleteAccountPage(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/login";
        }
        return "delete-account";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes ra) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/login";
        }
        if (!admin.getEmail().equals(email) || !admin.getPassword().equals(password)) {
            ra.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/settings";
        }
        employeeService.deleteByAdminId(admin.getId());
        adminService.deleteAdmin(admin.getId());
        session.invalidate();
        ra.addFlashAttribute("success", "Account deleted successfully");
        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String settingsPage(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/login";
        }
        return "settings";
    }

    //logout controller
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Admin logged out");
        session.invalidate();
        return "redirect:/login";
    }

}
