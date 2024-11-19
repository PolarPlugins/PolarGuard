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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/product/{uuidString}/info")
public class ProductInfoController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @PatchMapping("/setName/{name}")
    public CompletableFuture<ResponseEntity<Product>> setName(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("name") String name,
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

            Product product = optionalProduct.get();
            product.setName(name);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/setLatestVersion/{latestVersion}")
    public CompletableFuture<ResponseEntity<Product>> setLatestVersion(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("latestVersion") String latestVersion,
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

            Product product = optionalProduct.get();
            product.setLatestVersion(latestVersion);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/setPrice/{priceString}")
    public CompletableFuture<ResponseEntity<Product>> setPrice(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("priceString") String priceString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            double price;

            try {
                uuid = UUID.fromString(uuidString);
                price = Double.parseDouble(priceString);
            } catch (Exception ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(uuid));
            if (optionalProduct.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Product product = optionalProduct.get();
            product.setPrice(price);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/setStock/{stockString}")
    public CompletableFuture<ResponseEntity<Product>> setStock(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("stockString") String stockString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            int stock;

            try {
                uuid = UUID.fromString(uuidString);
                stock = Integer.parseInt(stockString);
            } catch (Exception ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(uuid));
            if (optionalProduct.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Product product = optionalProduct.get();
            product.setStockQuantity(stock);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/setLockdown/{lockdownString}")
    public CompletableFuture<ResponseEntity<Product>> setLockdown(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("lockdownString") String lockdownString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            boolean lockdown;

            try {
                uuid = UUID.fromString(uuidString);
                lockdown = Boolean.parseBoolean(lockdownString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(uuid));
            if (optionalProduct.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Product product = optionalProduct.get();
            product.setLockdown(lockdown);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }
}
