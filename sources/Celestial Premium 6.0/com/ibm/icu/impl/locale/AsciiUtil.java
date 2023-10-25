/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.locale;

public final class AsciiUtil {
    public static boolean caseIgnoreMatch(String s1, String s2) {
        char c2;
        char c1;
        int i;
        if (s1 == s2) {
            return true;
        }
        int len = s1.length();
        if (len != s2.length()) {
            return false;
        }
        for (i = 0; i < len && ((c1 = s1.charAt(i)) == (c2 = s2.charAt(i)) || AsciiUtil.toLower(c1) == AsciiUtil.toLower(c2)); ++i) {
        }
        return i == len;
    }

    public static int caseIgnoreCompare(String s1, String s2) {
        if (s1 == s2) {
            return 0;
        }
        return AsciiUtil.toLowerString(s1).compareTo(AsciiUtil.toLowerString(s2));
    }

    public static char toUpper(char c) {
        if (c >= 'a' && c <= 'z') {
            c = (char)(c - 32);
        }
        return c;
    }

    public static char toLower(char c) {
        if (c >= 'A' && c <= 'Z') {
            c = (char)(c + 32);
        }
        return c;
    }

    public static String toLowerString(String s) {
        char c;
        int idx;
        for (idx = 0; idx < s.length() && ((c = s.charAt(idx)) < 'A' || c > 'Z'); ++idx) {
        }
        if (idx == s.length()) {
            return s;
        }
        StringBuilder buf = new StringBuilder(s.substring(0, idx));
        while (idx < s.length()) {
            buf.append(AsciiUtil.toLower(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static String toUpperString(String s) {
        char c;
        int idx;
        for (idx = 0; idx < s.length() && ((c = s.charAt(idx)) < 'a' || c > 'z'); ++idx) {
        }
        if (idx == s.length()) {
            return s;
        }
        StringBuilder buf = new StringBuilder(s.substring(0, idx));
        while (idx < s.length()) {
            buf.append(AsciiUtil.toUpper(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static String toTitleString(String s) {
        if (s.length() == 0) {
            return s;
        }
        int idx = 0;
        char c = s.charAt(idx);
        if (c < 'a' || c > 'z') {
            for (idx = 1; idx < s.length() && (c < 'A' || c > 'Z'); ++idx) {
            }
        }
        if (idx == s.length()) {
            return s;
        }
        StringBuilder buf = new StringBuilder(s.substring(0, idx));
        if (idx == 0) {
            buf.append(AsciiUtil.toUpper(s.charAt(idx)));
            ++idx;
        }
        while (idx < s.length()) {
            buf.append(AsciiUtil.toLower(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static boolean isAlpha(char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    public static boolean isAlphaString(String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (AsciiUtil.isAlpha(s.charAt(i))) continue;
            b = false;
            break;
        }
        return b;
    }

    public static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isNumericString(String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (AsciiUtil.isNumeric(s.charAt(i))) continue;
            b = false;
            break;
        }
        return b;
    }

    public static boolean isAlphaNumeric(char c) {
        return AsciiUtil.isAlpha(c) || AsciiUtil.isNumeric(c);
    }

    public static boolean isAlphaNumericString(String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (AsciiUtil.isAlphaNumeric(s.charAt(i))) continue;
            b = false;
            break;
        }
        return b;
    }

    public static class CaseInsensitiveKey {
        private String _key;
        private int _hash;

        public CaseInsensitiveKey(String key) {
            this._key = key;
            this._hash = AsciiUtil.toLowerString(key).hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof CaseInsensitiveKey) {
                return AsciiUtil.caseIgnoreMatch(this._key, ((CaseInsensitiveKey)o)._key);
            }
            return false;
        }

        public int hashCode() {
            return this._hash;
        }
    }
}

