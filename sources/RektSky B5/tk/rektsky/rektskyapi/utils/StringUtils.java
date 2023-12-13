/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.rektskyapi.utils;

import java.util.Random;

public class StringUtils {
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder output = new StringBuilder();
        for (int i2 = 0; i2 != length; ++i2) {
            output.append(chars.charAt(random.nextInt(chars.length())));
        }
        return output.toString();
    }
}

