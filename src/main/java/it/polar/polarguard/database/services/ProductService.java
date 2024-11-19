package it.polar.polarguard.database.services;

import it.polar.polarguard.database.docs.Product;
import it.polar.polarguard.database.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByUUID(UUID uuid) {
        return productRepository.findByUUID(uuid);
    }

    public List<Product> getAllProductsByLockdown(boolean banned) {
        return productRepository.findAllByLockdown(banned);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(UUID uuid) {
        productRepository.deleteById(uuid.toString());
    }
}
