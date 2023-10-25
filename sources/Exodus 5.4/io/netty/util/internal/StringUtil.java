/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

public final class StringUtil {
    private static final String[] BYTE2HEX_NOPAD;
    public static final String NEWLINE;
    private static final String[] BYTE2HEX_PAD;
    private static final String EMPTY_STRING = "";

    public static String toHexStringPadded(byte[] byArray, int n, int n2) {
        return StringUtil.toHexStringPadded(new StringBuilder(n2 << 1), byArray, n, n2).toString();
    }

    public static String toHexStringPadded(byte[] byArray) {
        return StringUtil.toHexStringPadded(byArray, 0, byArray.length);
    }

    public static <T extends Appendable> T toHexString(T t, byte[] byArray, int n, int n2) {
        int n3;
        assert (n2 >= 0);
        if (n2 == 0) {
            return t;
        }
        int n4 = n + n2;
        int n5 = n4 - 1;
        for (n3 = n; n3 < n5 && byArray[n3] == 0; ++n3) {
        }
        StringUtil.byteToHexString(t, byArray[n3++]);
        int n6 = n4 - n3;
        StringUtil.toHexStringPadded(t, byArray, n3, n6);
        return t;
    }

    public static <T extends Appendable> T toHexStringPadded(T t, byte[] byArray, int n, int n2) {
        int n3 = n + n2;
        for (int i = n; i < n3; ++i) {
            StringUtil.byteToHexStringPadded(t, byArray[i]);
        }
        return t;
    }

    public static String simpleClassName(Object object) {
        if (object == null) {
            return "null_object";
        }
        return StringUtil.simpleClassName(object.getClass());
    }

    public static String byteToHexString(int n) {
        return BYTE2HEX_NOPAD[n & 0xFF];
    }

    public static <T extends Appendable> T byteToHexStringPadded(T t, int n) {
        try {
            t.append(StringUtil.byteToHexStringPadded(n));
        }
        catch (IOException iOException) {
            PlatformDependent.throwException(iOException);
        }
        return t;
    }

    public static String[] split(String string, char c) {
        int n;
        int n2 = string.length();
        ArrayList<String> arrayList = new ArrayList<String>();
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            if (string.charAt(n) != c) continue;
            if (n3 == n) {
                arrayList.add(EMPTY_STRING);
            } else {
                arrayList.add(string.substring(n3, n));
            }
            n3 = n + 1;
        }
        if (n3 == 0) {
            arrayList.add(string);
        } else if (n3 != n2) {
            arrayList.add(string.substring(n3, n2));
        } else {
            for (n = arrayList.size() - 1; n >= 0 && ((String)arrayList.get(n)).isEmpty(); --n) {
                arrayList.remove(n);
            }
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public static String simpleClassName(Class<?> clazz) {
        if (clazz == null) {
            return "null_class";
        }
        Package package_ = clazz.getPackage();
        if (package_ != null) {
            return clazz.getName().substring(package_.getName().length() + 1);
        }
        return clazz.getName();
    }

    public static String toHexString(byte[] byArray) {
        return StringUtil.toHexString(byArray, 0, byArray.length);
    }

    public static <T extends Appendable> T byteToHexString(T t, int n) {
        try {
            t.append(StringUtil.byteToHexString(n));
        }
        catch (IOException iOException) {
            PlatformDependent.throwException(iOException);
        }
        return t;
    }

    public static <T extends Appendable> T toHexStringPadded(T t, byte[] byArray) {
        return StringUtil.toHexStringPadded(t, byArray, 0, byArray.length);
    }

    public static String toHexString(byte[] byArray, int n, int n2) {
        return StringUtil.toHexString(new StringBuilder(n2 << 1), byArray, n, n2).toString();
    }

    private StringUtil() {
    }

    static {
        StringBuilder stringBuilder;
        int n;
        String string;
        BYTE2HEX_PAD = new String[256];
        BYTE2HEX_NOPAD = new String[256];
        try {
            string = new Formatter().format("%n", new Object[0]).toString();
        }
        catch (Exception exception) {
            string = "\n";
        }
        NEWLINE = string;
        for (n = 0; n < 10; ++n) {
            stringBuilder = new StringBuilder(2);
            stringBuilder.append('0');
            stringBuilder.append(n);
            StringUtil.BYTE2HEX_PAD[n] = stringBuilder.toString();
            StringUtil.BYTE2HEX_NOPAD[n] = String.valueOf(n);
        }
        while (n < 16) {
            stringBuilder = new StringBuilder(2);
            char c = (char)(97 + n - 10);
            stringBuilder.append('0');
            stringBuilder.append(c);
            StringUtil.BYTE2HEX_PAD[n] = stringBuilder.toString();
            StringUtil.BYTE2HEX_NOPAD[n] = String.valueOf(c);
            ++n;
        }
        while (n < BYTE2HEX_PAD.length) {
            String string2;
            stringBuilder = new StringBuilder(2);
            stringBuilder.append(Integer.toHexString(n));
            StringUtil.BYTE2HEX_PAD[n] = string2 = stringBuilder.toString();
            StringUtil.BYTE2HEX_NOPAD[n] = string2;
            ++n;
        }
    }

    public static String byteToHexStringPadded(int n) {
        return BYTE2HEX_PAD[n & 0xFF];
    }

    public static <T extends Appendable> T toHexString(T t, byte[] byArray) {
        return StringUtil.toHexString(t, byArray, 0, byArray.length);
    }
}

