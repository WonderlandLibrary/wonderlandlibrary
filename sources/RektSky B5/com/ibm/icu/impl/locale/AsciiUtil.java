/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.Utility;

public final class AsciiUtil {
    public static boolean caseIgnoreMatch(String s1, String s2) {
        char c2;
        char c1;
        int i2;
        if (Utility.sameObjects(s1, s2)) {
            return true;
        }
        int len = s1.length();
        if (len != s2.length()) {
            return false;
        }
        for (i2 = 0; i2 < len && ((c1 = s1.charAt(i2)) == (c2 = s2.charAt(i2)) || AsciiUtil.toLower(c1) == AsciiUtil.toLower(c2)); ++i2) {
        }
        return i2 == len;
    }

    public static int caseIgnoreCompare(String s1, String s2) {
        if (Utility.sameObjects(s1, s2)) {
            return 0;
        }
        return AsciiUtil.toLowerString(s1).compareTo(AsciiUtil.toLowerString(s2));
    }

    public static char toUpper(char c2) {
        if (c2 >= 'a' && c2 <= 'z') {
            c2 = (char)(c2 - 32);
        }
        return c2;
    }

    public static char toLower(char c2) {
        if (c2 >= 'A' && c2 <= 'Z') {
            c2 = (char)(c2 + 32);
        }
        return c2;
    }

    public static String toLowerString(String s2) {
        char c2;
        int idx;
        for (idx = 0; idx < s2.length() && ((c2 = s2.charAt(idx)) < 'A' || c2 > 'Z'); ++idx) {
        }
        if (idx == s2.length()) {
            return s2;
        }
        StringBuilder buf = new StringBuilder(s2.substring(0, idx));
        while (idx < s2.length()) {
            buf.append(AsciiUtil.toLower(s2.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static String toUpperString(String s2) {
        char c2;
        int idx;
        for (idx = 0; idx < s2.length() && ((c2 = s2.charAt(idx)) < 'a' || c2 > 'z'); ++idx) {
        }
        if (idx == s2.length()) {
            return s2;
        }
        StringBuilder buf = new StringBuilder(s2.substring(0, idx));
        while (idx < s2.length()) {
            buf.append(AsciiUtil.toUpper(s2.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static String toTitleString(String s2) {
        if (s2.length() == 0) {
            return s2;
        }
        int idx = 0;
        char c2 = s2.charAt(idx);
        if (c2 < 'a' || c2 > 'z') {
            for (idx = 1; idx < s2.length() && (c2 < 'A' || c2 > 'Z'); ++idx) {
            }
        }
        if (idx == s2.length()) {
            return s2;
        }
        StringBuilder buf = new StringBuilder(s2.substring(0, idx));
        if (idx == 0) {
            buf.append(AsciiUtil.toUpper(s2.charAt(idx)));
            ++idx;
        }
        while (idx < s2.length()) {
            buf.append(AsciiUtil.toLower(s2.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }

    public static boolean isAlpha(char c2) {
        return c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z';
    }

    public static boolean isAlphaString(String s2) {
        boolean b2 = true;
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            if (AsciiUtil.isAlpha(s2.charAt(i2))) continue;
            b2 = false;
            break;
        }
        return b2;
    }

    public static boolean isNumeric(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    public static boolean isNumericString(String s2) {
        boolean b2 = true;
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            if (AsciiUtil.isNumeric(s2.charAt(i2))) continue;
            b2 = false;
            break;
        }
        return b2;
    }

    public static boolean isAlphaNumeric(char c2) {
        return AsciiUtil.isAlpha(c2) || AsciiUtil.isNumeric(c2);
    }

    public static boolean isAlphaNumericString(String s2) {
        boolean b2 = true;
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            if (AsciiUtil.isAlphaNumeric(s2.charAt(i2))) continue;
            b2 = false;
            break;
        }
        return b2;
    }

    public static class CaseInsensitiveKey {
        private String _key;
        private int _hash;

        public CaseInsensitiveKey(String key) {
            this._key = key;
            this._hash = AsciiUtil.toLowerString(key).hashCode();
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 instanceof CaseInsensitiveKey) {
                return AsciiUtil.caseIgnoreMatch(this._key, ((CaseInsensitiveKey)o2)._key);
            }
            return false;
        }

        public int hashCode() {
            return this._hash;
        }
    }
}

