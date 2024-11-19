package it.polar.polarguard.database.services;

import it.polar.polarguard.database.docs.Admin;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.repos.AdminRepository;
import it.polar.polarguard.database.repos.CustomerRepository;
import it.polar.polarguard.enums.CustomerPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    @Autowired private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminByUUID(UUID uuid) {
        return adminRepository.findByUUID(uuid);
    }

    public List<Admin> getAllAdminsByDiscordId(String discordId) {
        return adminRepository.findAllByDiscordId(discordId);
    }

    public List<Admin> getAllAdminsByToken(String token) {
        return adminRepository.findAllByToken(token);
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdmin(UUID uuid) {
        adminRepository.deleteById(uuid.toString());
    }
}
