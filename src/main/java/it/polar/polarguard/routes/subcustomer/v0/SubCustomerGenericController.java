package it.polar.polarguard.routes.subcustomer.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.docs.Product;
import it.polar.polarguard.database.docs.SubCustomer;
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
@RequestMapping("/api/v0/subcustomer")
public class SubCustomerGenericController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<SubCustomer>>> getAll(
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            return ResponseEntity.ofNullable(subCustomerService.getAllSubCustomers());
        });
    }

    @GetMapping("/{uuidString}")
    public CompletableFuture<ResponseEntity<SubCustomer>> get(
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

            Optional<SubCustomer> optionalSubCustomer = Optional.ofNullable(subCustomerService.getSubCustomerByUUID(uuid));
            return optionalSubCustomer.map(ResponseEntity::ofNullable).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        });
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<SubCustomer>> create(
            @RequestParam("name") String name,
            @RequestParam("discordId") String discordId,
            @RequestParam("master") String masterUuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID masterUuid;

            try {
                masterUuid = UUID.fromString(masterUuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Customer> optionalMaster = Optional.ofNullable(customerService.getCustomerByUUID(masterUuid));
            if (optionalMaster.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            SubCustomer subCustomer = new SubCustomer(
                    UUID.randomUUID(),
                    masterUuid,
                    name,
                    discordId
            );

            return ResponseEntity.ofNullable(subCustomerService.saveSubCustomer(subCustomer));
        });
    }

    @DeleteMapping("/{uuidString}/delete")
    public CompletableFuture<ResponseEntity<SubCustomer>> delete(
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

            Optional<SubCustomer> optionalSubCustomer = Optional.ofNullable(subCustomerService.getSubCustomerByUUID(uuid));
            if (optionalSubCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            subCustomerService.deleteSubCustomer(uuid);

            return ResponseEntity.status(HttpStatus.OK).build();
        });
    }
}
