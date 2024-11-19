package it.polar.polarguard.database.docs;

import it.polar.polarguard.enums.CustomerPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "customers")
@AllArgsConstructor
public class Customer {
    @Id private final UUID uuid;                   // User unique ID. Cannot be changed

    private long firstSeen;                        // The timestamp of the first API call to check the HWID
    private long lastSeen;                         // The timestamp of the last API call to check the HWID

    private String name;                           // Username. Is encoded in UTF8
    private String discordId;                      // Discord user ID
    private CustomerPriority priority;             // Customer priority. For more infos, read the comments in the enum class
    private final Set<UUID> purchases;             // UUID of all the products that a user purchased

    private final Set<String> machines;            // HWIDs that are bound to this customer

    private boolean banned;                        // Is the customer banned?
}