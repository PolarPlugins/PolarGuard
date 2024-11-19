package it.polar.polarguard.database.services;

import it.polar.polarguard.database.docs.Customer;
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
public class CustomerService {
    @Autowired private CustomerRepository customerRepository;
    @Autowired private MongoTemplate mongoTemplate;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByUUID(UUID uuid) {
        return customerRepository.findByUUID(uuid);
    }

    public List<Customer> getAllCustomersByDiscordId(String discordId) {
        return customerRepository.findAllByDiscordId(discordId);
    }

    public List<Customer> getAllCustomersByPriority(CustomerPriority customerPriority) {
        return customerRepository.findAllByPriority(customerPriority);
    }

    public List<Customer> getAllCustomersByBanned(boolean banned) {
        return customerRepository.findAllByBanned(banned);
    }

    public List<Customer> getAllCustomersByMachineId(String machineId) {
        Query query = new Query(Criteria.where("machines").in(machineId));

        return mongoTemplate.find(query, Customer.class);
    }

    public void removeProductFromAllCustomers(UUID productId) {
        Query query = new Query(Criteria.where("purchases").in(productId));
        Update update = new Update().pull("purchases", productId);

        mongoTemplate.updateMulti(query, update, Customer.class);
    }

    public void removeMachineFromAllCustomers(String hwid) {
        Query query = new Query(Criteria.where("machines").in(hwid));
        Update update = new Update().pull("machines", hwid);

        mongoTemplate.updateMulti(query, update, Customer.class);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(UUID uuid) {
        customerRepository.deleteById(uuid.toString());
    }
}
