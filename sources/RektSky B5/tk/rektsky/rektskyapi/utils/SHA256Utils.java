/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Utils {
    public static String toSHA256(byte[] input) {
        try {
            return SHA256Utils.bytesToHex(MessageDigest.getInstance("SHA-256").digest(input));
        }
        catch (NoSuchAlgorithmException e2) {
            return null;
        }
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i2 = 0; i2 < hash.length; ++i2) {
            String hex = Integer.toHexString(0xFF & hash[i2]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

