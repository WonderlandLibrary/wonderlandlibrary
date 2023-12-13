/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

public final class StringUtil {
    public static final String NEWLINE;
    private static final String[] BYTE2HEX_PAD;
    private static final String[] BYTE2HEX_NOPAD;
    private static final String EMPTY_STRING = "";

    public static String[] split(String value, char delim) {
        int i2;
        int end = value.length();
        ArrayList<String> res = new ArrayList<String>();
        int start = 0;
        for (i2 = 0; i2 < end; ++i2) {
            if (value.charAt(i2) != delim) continue;
            if (start == i2) {
                res.add(EMPTY_STRING);
            } else {
                res.add(value.substring(start, i2));
            }
            start = i2 + 1;
        }
        if (start == 0) {
            res.add(value);
        } else if (start != end) {
            res.add(value.substring(start, end));
        } else {
            for (i2 = res.size() - 1; i2 >= 0 && ((String)res.get(i2)).isEmpty(); --i2) {
                res.remove(i2);
            }
        }
        return res.toArray(new String[res.size()]);
    }

    public static String byteToHexStringPadded(int value) {
        return BYTE2HEX_PAD[value & 0xFF];
    }

    public static <T extends Appendable> T byteToHexStringPadded(T buf, int value) {
        try {
            buf.append(StringUtil.byteToHexStringPadded(value));
        }
        catch (IOException e2) {
            PlatformDependent.throwException(e2);
        }
        return buf;
    }

    public static String toHexStringPadded(byte[] src) {
        return StringUtil.toHexStringPadded(src, 0, src.length);
    }

    public static String toHexStringPadded(byte[] src, int offset, int length) {
        return StringUtil.toHexStringPadded(new StringBuilder(length << 1), src, offset, length).toString();
    }

    public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src) {
        return StringUtil.toHexStringPadded(dst, src, 0, src.length);
    }

    public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src, int offset, int length) {
        int end = offset + length;
        for (int i2 = offset; i2 < end; ++i2) {
            StringUtil.byteToHexStringPadded(dst, src[i2]);
        }
        return dst;
    }

    public static String byteToHexString(int value) {
        return BYTE2HEX_NOPAD[value & 0xFF];
    }

    public static <T extends Appendable> T byteToHexString(T buf, int value) {
        try {
            buf.append(StringUtil.byteToHexString(value));
        }
        catch (IOException e2) {
            PlatformDependent.throwException(e2);
        }
        return buf;
    }

    public static String toHexString(byte[] src) {
        return StringUtil.toHexString(src, 0, src.length);
    }

    public static String toHexString(byte[] src, int offset, int length) {
        return StringUtil.toHexString(new StringBuilder(length << 1), src, offset, length).toString();
    }

    public static <T extends Appendable> T toHexString(T dst, byte[] src) {
        return StringUtil.toHexString(dst, src, 0, src.length);
    }

    public static <T extends Appendable> T toHexString(T dst, byte[] src, int offset, int length) {
        int i2;
        assert (length >= 0);
        if (length == 0) {
            return dst;
        }
        int end = offset + length;
        int endMinusOne = end - 1;
        for (i2 = offset; i2 < endMinusOne && src[i2] == 0; ++i2) {
        }
        StringUtil.byteToHexString(dst, src[i2++]);
        int remaining = end - i2;
        StringUtil.toHexStringPadded(dst, src, i2, remaining);
        return dst;
    }

    public static String simpleClassName(Object o2) {
        if (o2 == null) {
            return "null_object";
        }
        return StringUtil.simpleClassName(o2.getClass());
    }

    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            return "null_class";
        }
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return clazz.getName().substring(pkg.getName().length() + 1);
        }
        return clazz.getName();
    }

    private StringUtil() {
    }

    static {
        StringBuilder buf;
        int i2;
        String newLine;
        BYTE2HEX_PAD = new String[256];
        BYTE2HEX_NOPAD = new String[256];
        try {
            newLine = new Formatter().format("%n", new Object[0]).toString();
        }
        catch (Exception e2) {
            newLine = "\n";
        }
        NEWLINE = newLine;
        for (i2 = 0; i2 < 10; ++i2) {
            buf = new StringBuilder(2);
            buf.append('0');
            buf.append(i2);
            StringUtil.BYTE2HEX_PAD[i2] = buf.toString();
            StringUtil.BYTE2HEX_NOPAD[i2] = String.valueOf(i2);
        }
        while (i2 < 16) {
            buf = new StringBuilder(2);
            char c2 = (char)(97 + i2 - 10);
            buf.append('0');
            buf.append(c2);
            StringUtil.BYTE2HEX_PAD[i2] = buf.toString();
            StringUtil.BYTE2HEX_NOPAD[i2] = String.valueOf(c2);
            ++i2;
        }
        while (i2 < BYTE2HEX_PAD.length) {
            String str;
            buf = new StringBuilder(2);
            buf.append(Integer.toHexString(i2));
            StringUtil.BYTE2HEX_PAD[i2] = str = buf.toString();
            StringUtil.BYTE2HEX_NOPAD[i2] = str;
            ++i2;
        }
    }
}

