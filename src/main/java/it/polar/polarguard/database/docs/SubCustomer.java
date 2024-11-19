package it.polar.polarguard.database.docs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "subcustomers")
@AllArgsConstructor
public class SubCustomer {
    @Id private final UUID uuid;                   // User unique ID. Cannot be changed

    private UUID master;                           // The master customer bound to the sub. Used to retrieve purchases and more...

    private String name;                           // Username. Is encoded in UTF8
    private String discordId;                      // Discord user ID
}