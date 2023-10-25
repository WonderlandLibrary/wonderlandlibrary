/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.security.MessageDigest;
import org.apache.logging.log4j.util.Strings;

public final class NameUtil {
    private static final int MASK = 255;

    private NameUtil() {
    }

    public static String getSubName(String name) {
        if (Strings.isEmpty(name)) {
            return null;
        }
        int i = name.lastIndexOf(46);
        return i > 0 ? name.substring(0, i) : "";
    }

    public static String md5(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte[] bytes = digest.digest();
            StringBuilder md5 = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    md5.append('0');
                }
                md5.append(hex);
            }
            return md5.toString();
        }
        catch (Exception ex) {
            return string;
        }
    }
}

