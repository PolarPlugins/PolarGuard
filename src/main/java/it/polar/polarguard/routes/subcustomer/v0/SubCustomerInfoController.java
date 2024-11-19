package it.polar.polarguard.routes.subcustomer.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/subcustomer/{uuidString}/info")
public class SubCustomerInfoController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @PatchMapping("/setName/{name}")
    public CompletableFuture<ResponseEntity<SubCustomer>> setName(
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

            Optional<SubCustomer> optionalSubCustomer = Optional.ofNullable(subCustomerService.getSubCustomerByUUID(uuid));
            if (optionalSubCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            SubCustomer subCustomer = optionalSubCustomer.get();
            subCustomer.setName(name);

            return ResponseEntity.ofNullable(subCustomerService.saveSubCustomer(subCustomer));
        });
    }

    @PatchMapping("/setDiscordId/{discordId}")
    public CompletableFuture<ResponseEntity<SubCustomer>> setDiscordId(
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

            Optional<SubCustomer> optionalSubCustomer = Optional.ofNullable(subCustomerService.getSubCustomerByUUID(uuid));
            if (optionalSubCustomer.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            SubCustomer subCustomer = optionalSubCustomer.get();
            subCustomer.setDiscordId(discordId);

            return ResponseEntity.ofNullable(subCustomerService.saveSubCustomer(subCustomer));
        });
    }

    @PatchMapping("/setMaster/{masterUuidString}")
    public CompletableFuture<ResponseEntity<SubCustomer>> setMaster(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("masterUuidString") String masterUuidString,
            @RequestHeader("Access-Token") String accessToken
    ) {
        return CompletableFuture.supplyAsync(() -> {
            if (adminService.getAllAdminsByToken(accessToken).isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            UUID uuid;
            UUID masterUuid;

            try {
                uuid = UUID.fromString(uuidString);
                masterUuid = UUID.fromString(masterUuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<SubCustomer> optionalSubCustomer = Optional.ofNullable(subCustomerService.getSubCustomerByUUID(uuid));
            if (optionalSubCustomer.isEmpty() || customerService.getCustomerByUUID(masterUuid) == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            SubCustomer subCustomer = optionalSubCustomer.get();
            subCustomer.setMaster(masterUuid);

            return ResponseEntity.ofNullable(subCustomerService.saveSubCustomer(subCustomer));
        });
    }
}
