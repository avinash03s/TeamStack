package com.example.teamStack.services.serviceImpli;

import com.example.teamStack.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplements implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public void sendEmployeeWelcomeEmail(String toEmail, String name) {
        try {
            System.out.println("EMAIL SERVICE CALLED for: " + toEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to TeamStack");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "Welcome to TeamStack!\n" +
                            "Your employee account has been created successfully.\n\n" +
                            "Best Regards,\nTeamStack Admin"
            );
            mailSender.send(message);
            System.out.println("EMAIL SENT SUCCESSFULLY");
        } catch (Exception e) {
            System.out.println("EMAIL FAILED: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void passwordSuccessfullyUpdated(String toEmail, String name) {
        try {
            System.out.println("EMAIL SERVICE CALLED for: " + toEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Password Updated Successfully..!");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "From TeamStack..!\n" +
                            "Your TeamStack account password has been Successfully updated.\n\n" +
                            "Thank You"
            );
            mailSender.send(message);
        }catch (Exception e){
            System.out.println("EMAIL FAILED: " + e.getMessage());
        }
    }

    @Override
    public void registerSuccessful(String toEmail, String name) {
        try {
            System.out.println("EMAIL SERVICE CALLED for: " + toEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Registration Successful..!");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "From TeamStack..!\n" +
                            "Thank you for Register to TeamStack please Login and continue Your Journey..!\n\n" +
                            "Best Regards,\nTeamStack Family"
            );
            mailSender.send(message);
        }catch (Exception e){
            System.out.println("EMAIL FAILED: " + e.getMessage());
        }
    }
}
