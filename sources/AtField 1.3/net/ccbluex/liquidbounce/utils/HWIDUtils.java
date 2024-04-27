/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HWIDUtils {
    public static String getHWID() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String string = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
            byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            byte[] byArray2 = messageDigest.digest(byArray);
            int n = 0;
            for (byte by : byArray2) {
                stringBuilder.append(Integer.toHexString(by & 0xFF | 0x300), 0, 3);
                if (n != byArray2.length - 1) {
                    stringBuilder.append("-");
                }
                ++n;
            }
            return stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }
}

