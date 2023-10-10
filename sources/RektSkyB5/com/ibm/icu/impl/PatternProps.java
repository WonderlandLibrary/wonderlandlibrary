/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

public final class PatternProps {
    private static final byte[] latin1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 3, 0, 3, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final byte[] index2000 = new byte[]{2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 9};
    private static final int[] syntax2000 = new int[]{0, -1, -65536, 0x7FFF00FF, 0x7FEFFFFE, -65536, 0x3FFFFF, -1048576, -242, 65537};
    private static final int[] syntaxOrWhiteSpace2000 = new int[]{0, -1, -16384, 2147419135, 0x7FEFFFFE, -65536, 0x3FFFFF, -1048576, -242, 65537};

    public static boolean isSyntax(int c2) {
        if (c2 < 0) {
            return false;
        }
        if (c2 <= 255) {
            return latin1[c2] == 3;
        }
        if (c2 < 8208) {
            return false;
        }
        if (c2 <= 12336) {
            int bits = syntax2000[index2000[c2 - 8192 >> 5]];
            return (bits >> (c2 & 0x1F) & 1) != 0;
        }
        if (64830 <= c2 && c2 <= 65094) {
            return c2 <= 64831 || 65093 <= c2;
        }
        return false;
    }

    public static boolean isSyntaxOrWhiteSpace(int c2) {
        if (c2 < 0) {
            return false;
        }
        if (c2 <= 255) {
            return latin1[c2] != 0;
        }
        if (c2 < 8206) {
            return false;
        }
        if (c2 <= 12336) {
            int bits = syntaxOrWhiteSpace2000[index2000[c2 - 8192 >> 5]];
            return (bits >> (c2 & 0x1F) & 1) != 0;
        }
        if (64830 <= c2 && c2 <= 65094) {
            return c2 <= 64831 || 65093 <= c2;
        }
        return false;
    }

    public static boolean isWhiteSpace(int c2) {
        if (c2 < 0) {
            return false;
        }
        if (c2 <= 255) {
            return latin1[c2] == 5;
        }
        if (8206 <= c2 && c2 <= 8233) {
            return c2 <= 8207 || 8232 <= c2;
        }
        return false;
    }

    public static int skipWhiteSpace(CharSequence s2, int i2) {
        while (i2 < s2.length() && PatternProps.isWhiteSpace(s2.charAt(i2))) {
            ++i2;
        }
        return i2;
    }

    public static String trimWhiteSpace(String s2) {
        int start;
        if (s2.length() == 0 || !PatternProps.isWhiteSpace(s2.charAt(0)) && !PatternProps.isWhiteSpace(s2.charAt(s2.length() - 1))) {
            return s2;
        }
        int limit = s2.length();
        for (start = 0; start < limit && PatternProps.isWhiteSpace(s2.charAt(start)); ++start) {
        }
        if (start < limit) {
            while (PatternProps.isWhiteSpace(s2.charAt(limit - 1))) {
                --limit;
            }
        }
        return s2.substring(start, limit);
    }

    public static boolean isIdentifier(CharSequence s2) {
        int limit = s2.length();
        if (limit == 0) {
            return false;
        }
        int start = 0;
        do {
            if (!PatternProps.isSyntaxOrWhiteSpace(s2.charAt(start++))) continue;
            return false;
        } while (start < limit);
        return true;
    }

    public static boolean isIdentifier(CharSequence s2, int start, int limit) {
        if (start >= limit) {
            return false;
        }
        do {
            if (!PatternProps.isSyntaxOrWhiteSpace(s2.charAt(start++))) continue;
            return false;
        } while (start < limit);
        return true;
    }

    public static int skipIdentifier(CharSequence s2, int i2) {
        while (i2 < s2.length() && !PatternProps.isSyntaxOrWhiteSpace(s2.charAt(i2))) {
            ++i2;
        }
        return i2;
    }
}

