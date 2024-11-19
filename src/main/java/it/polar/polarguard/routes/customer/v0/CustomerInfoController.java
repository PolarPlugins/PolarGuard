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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/customer/{uuidString}/info")
public class CustomerInfoController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @PatchMapping("/setName/{name}")
    public CompletableFuture<ResponseEntity<Customer>> setName(
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

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.setName(name);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @PatchMapping("/setDiscordId/{discordId}")
    public CompletableFuture<ResponseEntity<Customer>> setDiscordId(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("discordId") String discordId,
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
            customer.setDiscordId(discordId);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @PatchMapping("/setPriority/{priority}")
    public CompletableFuture<ResponseEntity<Customer>> setPriority(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("priority") String priorityString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            CustomerPriority customerPriority;

            try {
                uuid = UUID.fromString(uuidString);
                customerPriority = CustomerPriority.valueOf(priorityString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.setPriority(customerPriority);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }

    @PatchMapping("/setBanned/{banned}")
    public CompletableFuture<ResponseEntity<Customer>> setBanned(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("banned") String bannedString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            boolean banned;

            try {
                uuid = UUID.fromString(uuidString);
                banned = Boolean.parseBoolean(bannedString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByUUID(uuid));
            if (optionalCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            Customer customer = optionalCustomer.get();
            customer.setBanned(banned);

            return ResponseEntity.ofNullable(customerService.saveCustomer(customer));
        });
    }
}
