package com.example.teamStack.services.serviceImpli;
import com.example.teamStack.entity.Admin;
import com.example.teamStack.exceptions.AdminNotFound;
import com.example.teamStack.repository.AdminRepository;
import com.example.teamStack.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImplements implements AdminService {

    @Autowired
    private AdminRepository repository;

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImplements.class);

    @Override
    public void registerAdmin(Admin admin) {
        log.info("Registering new admin with email: {}", admin.getEmail());
        repository.save(admin);
        log.info("Admin registered successfully with email: {}", admin.getEmail());
    }
    @Override
    public Admin validateAdmin(String email, String password) {
        log.info("Validating admin login for email: {}", email);
        Admin admin = repository.findByEmail(email);
        if(admin != null && admin.getPassword().equals(password)){
            log.info("Admin login validation successful for email: {}", email);
            return admin;
        }
        log.warn("Login failed for email: {}", email);
        return null;
    }

    @Override
    public Admin updateAdmin(Admin updateAdmin) {
        log.info("Updating admin profile with id: {}", updateAdmin.getId());
        Admin existingAdmin = repository.findById(updateAdmin.getId())
                .orElseThrow(() -> {
                    log.error("Admin not found with id: {}", updateAdmin.getId());
                    return new AdminNotFound("Admin Not Found..!");
                });
        existingAdmin.setName(updateAdmin.getName());
        existingAdmin.setEmail(updateAdmin.getEmail());
        existingAdmin.setPassword(updateAdmin.getPassword());
        existingAdmin.setCity(updateAdmin.getCity());
        repository.save(existingAdmin);
        log.info("Admin updated successfully with id: {}", existingAdmin.getId());
        return updateAdmin;
    }

    @Override
    public Admin findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    @Override
    public void updatePassword(String email, String newPassword) {
        Admin admin = repository.findByEmail(email);
        if(admin != null){
            log.info("Old Password: {}", admin.getPassword());
            admin.setPassword(newPassword);
            log.info("New Password: {}", admin.getPassword());
            repository.save(admin);   // important
        }
    }

    @Transactional
    @Override
    public void deleteAdmin(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }
}
