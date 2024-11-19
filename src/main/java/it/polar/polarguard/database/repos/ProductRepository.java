package it.polar.polarguard.database.repos;

import it.polar.polarguard.database.docs.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'uuid': ?0 }") Product findByUUID(UUID uuid);
    List<Product> findAllByLockdown(boolean lockdown);
}
