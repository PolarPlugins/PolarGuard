package it.polar.polarguard.routes;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.docs.Product;
import it.polar.polarguard.database.services.CustomerService;
import it.polar.polarguard.database.services.ProductService;
import it.polar.polarguard.database.services.SubCustomerService;
import it.polar.polarguard.enums.CustomerPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping
public class MainController {
    private final PolarGuardApplication application;

    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;
    @Autowired
    private PolarGuardApplication polarGuardApplication;

    @GetMapping(value = "/api/v0/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<?>> check(
            @RequestParam("hwid") String hwid,
            @RequestParam("product") String productUuidString,
            @RequestHeader("User-Agent") String userAgent
    ) {
        return CompletableFuture.supplyAsync(() -> {
            String[] userAgentParts = userAgent.split("/");
            if (userAgentParts.length != 2 ||
                    !userAgentParts[0].equals("PolarGuard") ||
                    !application.getAllowedClientVersions()
                            .contains(userAgentParts[1])
            ) return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();

            UUID productUuid;

            try {
                productUuid = UUID.fromString(productUuidString);
            } catch (IllegalArgumentException ignored) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Optional<Product> optionalProduct = Optional.ofNullable(productService.getProductByUUID(productUuid));
            if (optionalProduct.isEmpty() || optionalProduct.get().isLockdown()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            List<Customer> customers = customerService.getAllCustomersByMachineId(hwid);

            for (Customer customer : customers) {
                if (!customer.getPurchases().contains(productUuid) || customer.isBanned()) continue;

                customer.setLastSeen(System.currentTimeMillis());
                customerService.saveCustomer(customer);

                return ResponseEntity.ok("{\"allowed\": \"true\", \"name\": \"" + customer.getName() + "\"}");
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        });
    }

    @GetMapping
    public ResponseEntity<?> catchAll() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
