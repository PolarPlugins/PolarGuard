package it.polar.polarguard.database.services;

import it.polar.polarguard.database.docs.SubCustomer;
import it.polar.polarguard.database.repos.SubCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubCustomerService {
    @Autowired
    private SubCustomerRepository subCustomerRepository;

    public List<SubCustomer> getAllSubCustomers() {
        return subCustomerRepository.findAll();
    }

    public SubCustomer getSubCustomerByUUID(UUID uuid) {
        return subCustomerRepository.findByUUID(uuid);
    }

    public List<SubCustomer> getAllSubCustomersByDiscordId(String discordId) {
        return subCustomerRepository.findAllByDiscordId(discordId);
    }

    public List<SubCustomer> getAllSubCustomersByMaster(UUID master) {
        return subCustomerRepository.findAllByMaster(master);
    }

    public SubCustomer saveSubCustomer(SubCustomer subCustomer) {
        return subCustomerRepository.save(subCustomer);
    }

    public void deleteSubCustomer(UUID uuid) {
        subCustomerRepository.deleteById(uuid.toString());
    }

    public void deleteAllSubCustomersByMaster(UUID master) {
        subCustomerRepository.deleteAllByMaster(master);
    }
}
