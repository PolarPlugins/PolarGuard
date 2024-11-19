package it.polar.polarguard.routes.customer.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/customer")
public class CustomerGenericController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Customer>>> getAll(
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            return ResponseEntity.ofNullable(customerService.getAllCustomers());
        });
    }

    @GetMapping("/{uuidString}")
    public CompletableFuture<ResponseEntity<Customer>> get(
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
            return optionalCustomer.map(ResponseEntity::ofNullable).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        });
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Customer>> create(
            @RequestParam("name") String name,
            @RequestParam("discordId") String discordId,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            Customer customer = new Customer(
                    UUID.randomUUID(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    name,
                    discordId,
                    CustomerPriority.DEFAULT,
                    new HashSet<>(),
                    new HashSet<>(),
                    false
            );

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @DeleteMapping("/{uuidString}/delete")
    public CompletableFuture<ResponseEntity<Customer>> delete(
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

            customerService.deleteCustomer(uuid);
            subCustomerService.deleteAllSubCustomersByMaster(uuid);

            return ResponseEntity.status(HttpStatus.OK).build();
        });
    }
}
