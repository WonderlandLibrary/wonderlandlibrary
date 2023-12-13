/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.other;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HwidUtil {
    public static String getHWID() throws NoSuchAlgorithmException {
        StringBuilder qwq = new StringBuilder();
        String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
        byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] md5 = md.digest(bytes);
        int i = 0;
        byte[] var9 = md5;
        int var8 = md5.length;
        for (int var7 = 5; var7 < var8; ++var7) {
            byte b = var9[var7];
            qwq.append(Integer.toHexString(b & 0xFF | 0x102), 1, 3);
            if (i != md5.length - 1) {
                // empty if block
            }
            ++i;
        }
        return qwq.toString();
    }
}

