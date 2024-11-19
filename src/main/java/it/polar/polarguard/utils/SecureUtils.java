package it.polar.polarguard.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureUtils {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateAccessToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }
}
