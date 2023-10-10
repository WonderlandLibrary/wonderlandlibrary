// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;

public final class NameUtil
{
    private NameUtil() {
    }
    
    public static String getSubName(final String name) {
        if (Strings.isEmpty(name)) {
            return null;
        }
        final int i = name.lastIndexOf(46);
        return (i > 0) ? name.substring(0, i) : "";
    }
    
    public static String md5(final String input) {
        Objects.requireNonNull(input, "input");
        try {
            final byte[] inputBytes = input.getBytes();
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] bytes = digest.digest(inputBytes);
            final StringBuilder md5 = new StringBuilder(bytes.length * 2);
            for (final byte b : bytes) {
                final String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    md5.append('0');
                }
                md5.append(hex);
            }
            return md5.toString();
        }
        catch (NoSuchAlgorithmException error) {
            throw new RuntimeException(error);
        }
    }
}
