/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.util.Random;

public class RektSkyUtil {
    public static String genRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder output = new StringBuilder();
        for (int i2 = 0; i2 != len; ++i2) {
            output.append(chars.charAt(random.nextInt(chars.length())));
        }
        return output.toString();
    }
}

