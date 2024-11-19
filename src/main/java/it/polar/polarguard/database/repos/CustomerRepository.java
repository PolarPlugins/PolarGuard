package it.polar.polarguard.database.repos;

import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.enums.CustomerPriority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Query("{ 'uuid': ?0 }") Customer findByUUID(UUID uuid);
    List<Customer> findAllByDiscordId(String discordId);
    List<Customer> findAllByPriority(CustomerPriority customerPriority);
    List<Customer> findAllByBanned(boolean banned);
}
