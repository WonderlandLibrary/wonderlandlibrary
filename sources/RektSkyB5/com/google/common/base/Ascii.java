/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckReturnValue
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import javax.annotation.CheckReturnValue;

@GwtCompatible
public final class Ascii {
    public static final byte NUL = 0;
    public static final byte SOH = 1;
    public static final byte STX = 2;
    public static final byte ETX = 3;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte ACK = 6;
    public static final byte BEL = 7;
    public static final byte BS = 8;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte NL = 10;
    public static final byte VT = 11;
    public static final byte FF = 12;
    public static final byte CR = 13;
    public static final byte SO = 14;
    public static final byte SI = 15;
    public static final byte DLE = 16;
    public static final byte DC1 = 17;
    public static final byte XON = 17;
    public static final byte DC2 = 18;
    public static final byte DC3 = 19;
    public static final byte XOFF = 19;
    public static final byte DC4 = 20;
    public static final byte NAK = 21;
    public static final byte SYN = 22;
    public static final byte ETB = 23;
    public static final byte CAN = 24;
    public static final byte EM = 25;
    public static final byte SUB = 26;
    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte RS = 30;
    public static final byte US = 31;
    public static final byte SP = 32;
    public static final byte SPACE = 32;
    public static final byte DEL = 127;
    public static final char MIN = '\u0000';
    public static final char MAX = '\u007f';

    private Ascii() {
    }

    public static String toLowerCase(String string) {
        int length = string.length();
        for (int i2 = 0; i2 < length; ++i2) {
            if (!Ascii.isUpperCase(string.charAt(i2))) continue;
            char[] chars = string.toCharArray();
            while (i2 < length) {
                char c2 = chars[i2];
                if (Ascii.isUpperCase(c2)) {
                    chars[i2] = (char)(c2 ^ 0x20);
                }
                ++i2;
            }
            return String.valueOf(chars);
        }
        return string;
    }

    public static String toLowerCase(CharSequence chars) {
        if (chars instanceof String) {
            return Ascii.toLowerCase((String)chars);
        }
        int length = chars.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i2 = 0; i2 < length; ++i2) {
            builder.append(Ascii.toLowerCase(chars.charAt(i2)));
        }
        return builder.toString();
    }

    public static char toLowerCase(char c2) {
        return Ascii.isUpperCase(c2) ? (char)(c2 ^ 0x20) : c2;
    }

    public static String toUpperCase(String string) {
        int length = string.length();
        for (int i2 = 0; i2 < length; ++i2) {
            if (!Ascii.isLowerCase(string.charAt(i2))) continue;
            char[] chars = string.toCharArray();
            while (i2 < length) {
                char c2 = chars[i2];
                if (Ascii.isLowerCase(c2)) {
                    chars[i2] = (char)(c2 & 0x5F);
                }
                ++i2;
            }
            return String.valueOf(chars);
        }
        return string;
    }

    public static String toUpperCase(CharSequence chars) {
        if (chars instanceof String) {
            return Ascii.toUpperCase((String)chars);
        }
        int length = chars.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i2 = 0; i2 < length; ++i2) {
            builder.append(Ascii.toUpperCase(chars.charAt(i2)));
        }
        return builder.toString();
    }

    public static char toUpperCase(char c2) {
        return Ascii.isLowerCase(c2) ? (char)(c2 & 0x5F) : c2;
    }

    public static boolean isLowerCase(char c2) {
        return c2 >= 'a' && c2 <= 'z';
    }

    public static boolean isUpperCase(char c2) {
        return c2 >= 'A' && c2 <= 'Z';
    }

    @CheckReturnValue
    @Beta
    public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
        Preconditions.checkNotNull(seq);
        int truncationLength = maxLength - truncationIndicator.length();
        Preconditions.checkArgument(truncationLength >= 0, "maxLength (%s) must be >= length of the truncation indicator (%s)", maxLength, truncationIndicator.length());
        if (seq.length() <= maxLength) {
            String string = seq.toString();
            if (string.length() <= maxLength) {
                return string;
            }
            seq = string;
        }
        return new StringBuilder(maxLength).append(seq, 0, truncationLength).append(truncationIndicator).toString();
    }

    @Beta
    public static boolean equalsIgnoreCase(CharSequence s1, CharSequence s2) {
        int length = s1.length();
        if (s1 == s2) {
            return true;
        }
        if (length != s2.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; ++i2) {
            int alphaIndex;
            char c2;
            char c1 = s1.charAt(i2);
            if (c1 == (c2 = s2.charAt(i2)) || (alphaIndex = Ascii.getAlphaIndex(c1)) < 26 && alphaIndex == Ascii.getAlphaIndex(c2)) continue;
            return false;
        }
        return true;
    }

    private static int getAlphaIndex(char c2) {
        return (char)((c2 | 0x20) - 97);
    }
}

