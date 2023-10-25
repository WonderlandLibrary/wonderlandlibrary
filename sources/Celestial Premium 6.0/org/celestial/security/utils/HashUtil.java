/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.security.utils;

import java.security.MessageDigest;

public class HashUtil {
    public static String hashInput(String hash, String input) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes;
            MessageDigest messageDigest = MessageDigest.getInstance(hash);
            for (byte dig : bytes = messageDigest.digest(input.getBytes())) {
                sb.append(Integer.toString((dig & 0xFF) + 256, 16).substring(1));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return sb.toString();
    }
}

