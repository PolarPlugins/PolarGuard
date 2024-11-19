package it.polar.polarguard.routes.customer.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.services.AdminService;
import it.polar.polarguard.database.services.CustomerService;
import it.polar.polarguard.database.services.ProductService;
import it.polar.polarguard.database.services.SubCustomerService;
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
@RequestMapping("/api/v0/customer/{uuidString}/purchases")
public class CustomerPurchasesController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @PatchMapping("/add/{purchase}")
    public CompletableFuture<ResponseEntity<Customer>> add(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("purchase") String purchaseUuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            UUID purchaseUuid;

            try {
                uuid = UUID.fromString(uuidString);
                purchaseUuid = UUID.fromString(purchaseUuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.getPurchases().add(purchaseUuid);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @PatchMapping("/remove/{purchase}")
    public CompletableFuture<ResponseEntity<Customer>> remove(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("purchase") String purchaseUuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            UUID purchaseUuid;

            try {
                uuid = UUID.fromString(uuidString);
                purchaseUuid = UUID.fromString(purchaseUuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.getPurchases().remove(purchaseUuid);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @PatchMapping("/clear")
    public CompletableFuture<ResponseEntity<Customer>> clear(
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

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.getPurchases().clear();

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }
}
