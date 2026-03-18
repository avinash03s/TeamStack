package com.example.teamStack.services;
import com.example.teamStack.entity.Admin;

public interface AdminService {
    public void registerAdmin(Admin admin);
    public Admin validateAdmin(String email, String password);
    public Admin updateAdmin(Admin updateAdmin);

    public Admin findByEmail(String email);
    public void updatePassword(String email, String newPassword);
}
