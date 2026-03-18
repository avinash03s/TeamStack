package com.example.teamStack.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AdminNotFound.class)
    public String handleAdminNotFound(AdminNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(EmployeeNotFound.class)
    public String handleEmployeeNotFound(EmployeeNotFound ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DuplicateEmailFound.class)
    public String handleDuplicateEmail(DuplicateEmailFound ex, Model model){
        model.addAttribute("errorMessage",ex.getMessage());
        return "error";
    }
    @ExceptionHandler(Exception.class)
    public String handleAllException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Something went wrong!");
        return "error";
    }
}