package it.polar.polarguard;

import it.polar.polarguard.database.docs.Admin;
import it.polar.polarguard.database.services.AdminService;
import it.polar.polarguard.utils.SecureUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@Getter
@SpringBootApplication
public class PolarGuardApplication implements CommandLineRunner {
    @Getter
    private static PolarGuardApplication instance;

    private final String discordWebhookUri = "https://discord.com/api/webhooks/1308101276571144272/vfhcPggmIDduG9bBlX1xUZSXnGjfTrFQlPCQjcBqcsngp2kx5Vyuc70ftTFX9QF0Dx0g";
    private final String mongoConnectionUri = "mongodb+srv://polarguard:gz2zqQvILluqJKvI@mainclusterfree.vqhorvs.mongodb.net/polarguard?retryWrites=true&w=majority&appName=MainClusterFree";
    private final Set<String> allowedClientVersions = new HashSet<>(List.of(    // If the version is not in here, the plugin won't work
            "0.0.1"                                                             // Version 0.0.1 of the Checker Agent
    ));

    @Autowired private AdminService adminService;

    public PolarGuardApplication() {
        instance = this;
    }

    public static void main(String[] args) {
        SpringApplication.run(PolarGuardApplication.class, args);
        new PolarGuardApplication();
    }

    @Override
    public void run(String... args) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("root@polarguard:~# ");

                handleCommand(scanner.nextLine());
            }
        }).start();
    }

    private void handleCommand(String command) {
        String[] args = command.split(" ");

        if (args.length < 1) {
            System.out.println("[!] Invalid command! Available root commands: admin");
            return;
        }

        if (args[0].equalsIgnoreCase("admin")) {
            if (args.length < 2) {
                System.out.println("[!] Invalid command! Usage: admin <add|remove|list> [Name|UUID] [DiscordID]");
                return;
            }

            switch (args[1].toLowerCase()) {
                case "add": {
                    if (args.length < 4) {
                        System.out.println("[!] Invalid command! Usage: admin add <Name> <DiscordID>");
                        return;
                    }

                    Admin admin = new Admin(
                            UUID.randomUUID(),
                            args[2],
                            args[3],
                            SecureUtils.generateAccessToken()
                    );

                    adminService.saveAdmin(admin);

                    System.out.println("[+] Admin added! Here's are his details:");
                    System.out.println("  UUID:    " + admin.getUuid());
                    System.out.println("  Name:    " + admin.getName());
                    System.out.println("  Discord: " + admin.getDiscordId());
                    System.out.println("  Token:   " + admin.getToken());
                    System.out.println();

                    return;
                }

                case "remove": {
                    if (args.length < 3) {
                        System.out.println("[!] Invalid command! Usage: admin remove <UUID>");
                        return;
                    }

                    UUID uuid;

                    try {
                        uuid = UUID.fromString(args[2]);
                    } catch (IllegalArgumentException ignored) {
                        System.out.println("[!] You should input a valid UUID!");
                        return;
                    }

                    Optional<Admin> adminOptional = Optional.ofNullable(adminService.getAdminByUUID(uuid));
                    if (adminOptional.isEmpty()) {
                        System.out.println("[!] This admin does not exist!");
                        return;
                    }

                    adminService.deleteAdmin(uuid);

                    System.out.println("[-] Admin removed successfully!");
                    System.out.println();

                    return;
                }

                case "list": {
                    List<Admin> admins = adminService.getAllAdmins();
                    if (admins.isEmpty()) {
                        System.out.println("[!] There are no admins in the database!");
                        return;
                    }

                    int i = 1;

                    System.out.println("[@] Showing " + admins.size() + " admin(s):");

                    for (Admin admin : admins) {
                        System.out.println("[?] Admin #" + i + ":");
                        System.out.println("  UUID:    " + admin.getUuid());
                        System.out.println("  Name:    " + admin.getName());
                        System.out.println("  Discord: " + admin.getDiscordId());
                        System.out.println("  Token:   " + admin.getToken());

                        i++;
                    }

                    System.out.println();

                    return;
                }

                default: {
                    System.out.println("[!] Invalid command! Usage: admin <add|remove|list> [Name] [DiscordID]");
                    return;
                }
            }
        }
        System.out.println("[!] Invalid command! Available root commands: admin");
    }
}
