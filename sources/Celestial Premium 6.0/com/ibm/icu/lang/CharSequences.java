/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.lang;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CharSequences {
    public static int matchAfter(CharSequence a, CharSequence b, int aIndex, int bIndex) {
        char cb;
        char ca;
        int j;
        int i = aIndex;
        int alen = a.length();
        int blen = b.length();
        for (j = bIndex; i < alen && j < blen && (ca = a.charAt(i)) == (cb = b.charAt(j)); ++i, ++j) {
        }
        int result = i - aIndex;
        if (result != 0 && !CharSequences.onCharacterBoundary(a, i) && !CharSequences.onCharacterBoundary(b, j)) {
            --result;
        }
        return result;
    }

    public int codePointLength(CharSequence s) {
        return Character.codePointCount(s, 0, s.length());
    }

    public static final boolean equals(int codepoint, CharSequence other) {
        if (other == null) {
            return false;
        }
        switch (other.length()) {
            case 1: {
                return codepoint == other.charAt(0);
            }
            case 2: {
                return codepoint > 65535 && codepoint == Character.codePointAt(other, 0);
            }
        }
        return false;
    }

    public static final boolean equals(CharSequence other, int codepoint) {
        return CharSequences.equals(codepoint, other);
    }

    public static int compare(CharSequence string, int codePoint) {
        if (codePoint < 0 || codePoint > 0x10FFFF) {
            throw new IllegalArgumentException();
        }
        int stringLength = string.length();
        if (stringLength == 0) {
            return -1;
        }
        char firstChar = string.charAt(0);
        int offset = codePoint - 65536;
        if (offset < 0) {
            int result = firstChar - codePoint;
            if (result != 0) {
                return result;
            }
            return stringLength - 1;
        }
        char lead = (char)((offset >>> 10) + 55296);
        int result = firstChar - lead;
        if (result != 0) {
            return result;
        }
        if (stringLength > 1) {
            char trail = (char)((offset & 0x3FF) + 56320);
            result = string.charAt(1) - trail;
            if (result != 0) {
                return result;
            }
        }
        return stringLength - 2;
    }

    public static int compare(int codepoint, CharSequence a) {
        return -CharSequences.compare(a, codepoint);
    }

    public static int getSingleCodePoint(CharSequence s) {
        int length = s.length();
        if (length < 1 || length > 2) {
            return Integer.MAX_VALUE;
        }
        int result = Character.codePointAt(s, 0);
        return result < 65536 == (length == 1) ? result : Integer.MAX_VALUE;
    }

    public static final <T> boolean equals(T a, T b) {
        return a == null ? b == null : (b == null ? false : a.equals(b));
    }

    public static int compare(CharSequence a, CharSequence b) {
        int blength;
        int alength = a.length();
        int min = alength <= (blength = b.length()) ? alength : blength;
        for (int i = 0; i < min; ++i) {
            int diff = a.charAt(i) - b.charAt(i);
            if (diff == 0) continue;
            return diff;
        }
        return alength - blength;
    }

    public static boolean equalsChars(CharSequence a, CharSequence b) {
        return a.length() == b.length() && CharSequences.compare(a, b) == 0;
    }

    public static boolean onCharacterBoundary(CharSequence s, int i) {
        return i <= 0 || i >= s.length() || !Character.isHighSurrogate(s.charAt(i - 1)) || !Character.isLowSurrogate(s.charAt(i));
    }

    public static int indexOf(CharSequence s, int codePoint) {
        int cp;
        for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
            cp = Character.codePointAt(s, i);
            if (cp != codePoint) continue;
            return i;
        }
        return -1;
    }

    public static int[] codePoints(CharSequence s) {
        int[] result = new int[s.length()];
        int j = 0;
        for (int i = 0; i < s.length(); ++i) {
            char last;
            char cp = s.charAt(i);
            if (cp >= '\udc00' && cp <= '\udfff' && i != 0 && (last = (char)result[j - 1]) >= '\ud800' && last <= '\udbff') {
                result[j - 1] = Character.toCodePoint(last, cp);
                continue;
            }
            result[j++] = cp;
        }
        if (j == result.length) {
            return result;
        }
        int[] shortResult = new int[j];
        System.arraycopy(result, 0, shortResult, 0, j);
        return shortResult;
    }

    private CharSequences() {
    }
}

