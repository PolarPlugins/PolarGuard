package it.polar.polarguard.routes.product.v0;

import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.docs.Customer;
import it.polar.polarguard.database.docs.Product;
import it.polar.polarguard.database.services.AdminService;
import it.polar.polarguard.database.services.CustomerService;
import it.polar.polarguard.database.services.ProductService;
import it.polar.polarguard.database.services.SubCustomerService;
import it.polar.polarguard.utils.ByteUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v0/product/{uuidString}/files")
public class ProductFilesController {
    private final PolarGuardApplication application;

    @Autowired private AdminService adminService;
    @Autowired private ProductService productService;
    @Autowired private CustomerService customerService;
    @Autowired private SubCustomerService subCustomerService;

    @GetMapping("/get/{name}")
    public CompletableFuture<ResponseEntity<byte[]>> get(
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
            if (!product.getFiles().containsKey(name.replace(".", "~"))) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(ByteUtils.decompressBytes(product.getFiles().get(name.replace(".", "~"))));
        });
    }

    @PatchMapping("/add/{name}")
    public CompletableFuture<ResponseEntity<Product>> add(
            @PathVariable("uuidString") String uuidString,
            @PathVariable("name") String name,
            @RequestBody byte[] fileBytes,
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
            product.getFiles().put(name.replace(".", "~"), ByteUtils.compressBytes(fileBytes));

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/remove/{name}")
    public CompletableFuture<ResponseEntity<Product>> remove(
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
            product.getFiles().remove(name);

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }

    @PatchMapping("/clear")
    public CompletableFuture<ResponseEntity<Product>> clear(
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

            Product product = optionalProduct.get();
            product.getFiles().clear();

            return ResponseEntity.ofNullable(productService.saveProduct(product));
        });
    }
}
