/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import org.apache.http.Consts;
import org.apache.http.util.Args;

public final class EncodingUtils {
    public static String getString(byte[] data, int offset, int length, String charset) {
        Args.notNull(data, "Input");
        Args.notEmpty(charset, "Charset");
        try {
            return new String(data, offset, length, charset);
        }
        catch (UnsupportedEncodingException e) {
            return new String(data, offset, length);
        }
    }

    public static String getString(byte[] data, String charset) {
        Args.notNull(data, "Input");
        return EncodingUtils.getString(data, 0, data.length, charset);
    }

    public static byte[] getBytes(String data, String charset) {
        Args.notNull(data, "Input");
        Args.notEmpty(charset, "Charset");
        try {
            return data.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

    public static byte[] getAsciiBytes(String data) {
        Args.notNull(data, "Input");
        try {
            return data.getBytes(Consts.ASCII.name());
        }
        catch (UnsupportedEncodingException e) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data, int offset, int length) {
        Args.notNull(data, "Input");
        try {
            return new String(data, offset, length, Consts.ASCII.name());
        }
        catch (UnsupportedEncodingException e) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data) {
        Args.notNull(data, "Input");
        return EncodingUtils.getAsciiString(data, 0, data.length);
    }

    private EncodingUtils() {
    }
}

