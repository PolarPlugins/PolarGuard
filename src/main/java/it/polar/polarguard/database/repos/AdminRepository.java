package it.polar.polarguard.database.repos;

import it.polar.polarguard.database.docs.Admin;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.enums.CustomerPriority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AdminRepository extends MongoRepository<Admin, String> {
    @Query("{ 'uuid': ?0 }") Admin findByUUID(UUID uuid);
    List<Admin> findAllByDiscordId(String discordId);
    List<Admin> findAllByToken(String token);
}
