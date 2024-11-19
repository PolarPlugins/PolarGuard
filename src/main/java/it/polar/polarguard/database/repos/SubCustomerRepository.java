package it.polar.polarguard.database.repos;

import it.polar.polarguard.database.docs.SubCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SubCustomerRepository extends MongoRepository<SubCustomer, String> {
    @Query("{ 'uuid': ?0 }") SubCustomer findByUUID(UUID uuid);
    List<SubCustomer> findAllByDiscordId(String discordId);
    List<SubCustomer> findAllByMaster(UUID master);
    void deleteAllByMaster(UUID master);
}
