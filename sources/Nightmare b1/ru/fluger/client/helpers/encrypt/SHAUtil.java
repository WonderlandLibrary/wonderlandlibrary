// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SHAUtil
{
    public static String SHA1(final String sha1) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA1");
            final byte[] array = md.digest(sha1.getBytes());
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
