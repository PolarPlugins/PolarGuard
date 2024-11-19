package it.polar.polarguard.routes.product.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.docs.Product;
import it.polar.polarguard.database.services.AdminService;
import it.polar.polarguard.database.services.CustomerService;
import it.polar.polarguard.database.services.ProductService;
import it.polar.polarguard.database.services.SubCustomerService;
import it.polar.polarguard.enums.CustomerPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/product")
public class ProductGenericController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Product>>> getAll(
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            return ResponseEntity.ofNullable(productService.getAllProducts());
        });
    }

    @GetMapping("/{uuidString}")
    public CompletableFuture<ResponseEntity<Product>> get(
            @PathVariable("uuidString") String uuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;

            try {
                uuid = UUID.fromString(uuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(uuid));
            return optionalProduct.map(ResponseEntity::ofNullable).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        });
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Product>> create(
            @RequestParam("name") String name,
            @RequestParam("version") String version,
            @RequestParam("price") String priceString,
            @RequestParam("stock") String stockQuantityString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            double price;
            int stockQuantity;

            try {
                price = Double.parseDouble(priceString);
                stockQuantity = Integer.parseInt(stockQuantityString);
            } catch (NumberFormatException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Product product = new Product(
                    UUID.randomUUID(),
                    name,
                    version,
                    new HashMap<>(),
                    price,
                    stockQuantity,
                    false
            );

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @DeleteMapping("/{uuidString}/delete")
    public CompletableFuture<ResponseEntity<Product>> delete(
            @PathVariable("uuidString") String uuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;

            try {
                uuid = UUID.fromString(uuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(uuid));
            if (optionalProduct.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            productService.deleteProduct(uuid);
            customerService.removeProductFromAllCustomers(uuid);

            return ResponseEntity.status(HttpStatus.OK).build();
        });
    }
}
