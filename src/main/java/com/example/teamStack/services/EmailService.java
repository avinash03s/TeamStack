package com.example.teamStack.services;

public interface EmailService {
    void sendEmployeeWelcomeEmail(String toEmail, String name);
    void passwordSuccessfullyUpdated(String toEmail, String name);
    void registerSuccessful(String toEmail, String name);
}
