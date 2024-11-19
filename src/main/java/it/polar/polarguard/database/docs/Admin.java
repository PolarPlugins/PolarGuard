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
@Document(collection = "admins")
@AllArgsConstructor
public class Admin {
    @Id private final UUID uuid;                   // Admin unique ID. Cannot be changed

    private String name;                           // Username. Is encoded in UTF8
    private String discordId;                      // Discord user ID
    private String token;                          // Access Token
}