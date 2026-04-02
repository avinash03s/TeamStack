package com.example.teamStack.services;
import com.example.teamStack.entity.Admin;

public interface AdminService {
    void registerAdmin(Admin admin);
    Admin validateAdmin(String email, String password);
    Admin updateAdmin(Admin updateAdmin);
    Admin findByEmail(String email);
    void updatePassword(String email, String newPassword);
    void deleteAdmin(Long id);
    boolean emailExists(String email);
}
