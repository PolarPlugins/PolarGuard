package it.polar.polarguard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDUtils {
    public static String getHWID() throws NoSuchAlgorithmException {
        String toEncrypt = System.getenv("COMPUTERNAME") +
                System.getProperty("user.name") +
                System.getenv("PROCESSOR_IDENTIFIER") +
                System.getenv("PROCESSOR_LEVEL");

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(toEncrypt.getBytes());

        StringBuilder hexString = new StringBuilder();
        byte[] byteData = messageDigest.digest();

        for (byte forByteData : byteData) {
            String hex = Integer.toHexString(0xFF & forByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
