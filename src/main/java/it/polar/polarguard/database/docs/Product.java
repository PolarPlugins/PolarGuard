package it.polar.polarguard.database.docs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "products")
@AllArgsConstructor
public class Product {
    @Id private final UUID uuid;                   // Product unique ID. Cannot be changed

    private String name;                           // Product name. Is encoded in UTF8
    private String latestVersion;                  // Latest released version ID (e.g. 0.0.1-SNAPSHOT)
    private final HashMap<String, byte[]> files;   // Version, File Binary

    private double price;                          // Price in USD
    private int stockQuantity;                     // How many items are available

    private boolean lockdown;                      // Is the product in lockdown?
}
