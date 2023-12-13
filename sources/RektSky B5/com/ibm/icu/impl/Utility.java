/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Utility {
    private static final char APOSTROPHE = '\'';
    private static final char BACKSLASH = '\\';
    private static final int MAGIC_UNSIGNED = Integer.MIN_VALUE;
    private static final char ESCAPE = '\ua5a5';
    static final byte ESCAPE_BYTE = -91;
    public static String LINE_SEPARATOR = System.getProperty("line.separator");
    static final char[] HEX_DIGIT = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] UNESCAPE_MAP = new char[]{'a', '\u0007', 'b', '\b', 'e', '\u001b', 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', '\u000b'};
    static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final boolean arrayEquals(Object[] source, Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof Object[])) {
            return false;
        }
        Object[] targ = (Object[])target;
        return source.length == targ.length && Utility.arrayRegionMatches(source, 0, targ, 0, source.length);
    }

    public static final boolean arrayEquals(int[] source, Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof int[])) {
            return false;
        }
        int[] targ = (int[])target;
        return source.length == targ.length && Utility.arrayRegionMatches(source, 0, targ, 0, source.length);
    }

    public static final boolean arrayEquals(double[] source, Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof double[])) {
            return false;
        }
        double[] targ = (double[])target;
        return source.length == targ.length && Utility.arrayRegionMatches(source, 0, targ, 0, source.length);
    }

    public static final boolean arrayEquals(byte[] source, Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof byte[])) {
            return false;
        }
        byte[] targ = (byte[])target;
        return source.length == targ.length && Utility.arrayRegionMatches(source, 0, targ, 0, source.length);
    }

    public static final boolean arrayEquals(Object source, Object target) {
        if (source == null) {
            return target == null;
        }
        if (source instanceof Object[]) {
            return Utility.arrayEquals((Object[])source, target);
        }
        if (source instanceof int[]) {
            return Utility.arrayEquals((int[])source, target);
        }
        if (source instanceof double[]) {
            return Utility.arrayEquals((double[])source, target);
        }
        if (source instanceof byte[]) {
            return Utility.arrayEquals((byte[])source, target);
        }
        return source.equals(target);
    }

    public static final boolean arrayRegionMatches(Object[] source, int sourceStart, Object[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (Utility.arrayEquals(source[i2], target[i2 + delta])) continue;
            return false;
        }
        return true;
    }

    public static final boolean arrayRegionMatches(char[] source, int sourceStart, char[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (source[i2] == target[i2 + delta]) continue;
            return false;
        }
        return true;
    }

    public static final boolean arrayRegionMatches(int[] source, int sourceStart, int[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (source[i2] == target[i2 + delta]) continue;
            return false;
        }
        return true;
    }

    public static final boolean arrayRegionMatches(double[] source, int sourceStart, double[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (source[i2] == target[i2 + delta]) continue;
            return false;
        }
        return true;
    }

    public static final boolean arrayRegionMatches(byte[] source, int sourceStart, byte[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (source[i2] == target[i2 + delta]) continue;
            return false;
        }
        return true;
    }

    public static final boolean sameObjects(Object a2, Object b2) {
        return a2 == b2;
    }

    public static final boolean objectEquals(Object a2, Object b2) {
        return a2 == null ? b2 == null : (b2 == null ? false : a2.equals(b2));
    }

    public static <T extends Comparable<T>> int checkCompare(T a2, T b2) {
        return a2 == null ? (b2 == null ? 0 : -1) : (b2 == null ? 1 : a2.compareTo(b2));
    }

    public static int checkHash(Object a2) {
        return a2 == null ? 0 : a2.hashCode();
    }

    public static final String arrayToRLEString(int[] a2) {
        StringBuilder buffer = new StringBuilder();
        Utility.appendInt(buffer, a2.length);
        int runValue = a2[0];
        int runLength = 1;
        for (int i2 = 1; i2 < a2.length; ++i2) {
            int s2 = a2[i2];
            if (s2 == runValue && runLength < 65535) {
                ++runLength;
                continue;
            }
            Utility.encodeRun(buffer, runValue, runLength);
            runValue = s2;
            runLength = 1;
        }
        Utility.encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }

    public static final String arrayToRLEString(short[] a2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a2.length >> 16));
        buffer.append((char)a2.length);
        short runValue = a2[0];
        int runLength = 1;
        for (int i2 = 1; i2 < a2.length; ++i2) {
            short s2 = a2[i2];
            if (s2 == runValue && runLength < 65535) {
                ++runLength;
                continue;
            }
            Utility.encodeRun(buffer, runValue, runLength);
            runValue = s2;
            runLength = 1;
        }
        Utility.encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }

    public static final String arrayToRLEString(char[] a2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a2.length >> 16));
        buffer.append((char)a2.length);
        char runValue = a2[0];
        int runLength = 1;
        for (int i2 = 1; i2 < a2.length; ++i2) {
            char s2 = a2[i2];
            if (s2 == runValue && runLength < 65535) {
                ++runLength;
                continue;
            }
            Utility.encodeRun(buffer, (short)runValue, runLength);
            runValue = s2;
            runLength = 1;
        }
        Utility.encodeRun(buffer, (short)runValue, runLength);
        return buffer.toString();
    }

    public static final String arrayToRLEString(byte[] a2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a2.length >> 16));
        buffer.append((char)a2.length);
        byte runValue = a2[0];
        int runLength = 1;
        byte[] state = new byte[2];
        for (int i2 = 1; i2 < a2.length; ++i2) {
            byte b2 = a2[i2];
            if (b2 == runValue && runLength < 255) {
                ++runLength;
                continue;
            }
            Utility.encodeRun(buffer, runValue, runLength, state);
            runValue = b2;
            runLength = 1;
        }
        Utility.encodeRun(buffer, runValue, runLength, state);
        if (state[0] != 0) {
            Utility.appendEncodedByte(buffer, (byte)0, state);
        }
        return buffer.toString();
    }

    private static final <T extends Appendable> void encodeRun(T buffer, int value, int length) {
        if (length < 4) {
            for (int j2 = 0; j2 < length; ++j2) {
                if (value == 42405) {
                    Utility.appendInt(buffer, value);
                }
                Utility.appendInt(buffer, value);
            }
        } else {
            if (length == 42405) {
                if (value == 42405) {
                    Utility.appendInt(buffer, 42405);
                }
                Utility.appendInt(buffer, value);
                --length;
            }
            Utility.appendInt(buffer, 42405);
            Utility.appendInt(buffer, length);
            Utility.appendInt(buffer, value);
        }
    }

    private static final <T extends Appendable> void appendInt(T buffer, int value) {
        try {
            buffer.append((char)(value >>> 16));
            buffer.append((char)(value & 0xFFFF));
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    private static final <T extends Appendable> void encodeRun(T buffer, short value, int length) {
        try {
            char valueChar = (char)value;
            if (length < 4) {
                for (int j2 = 0; j2 < length; ++j2) {
                    if (valueChar == '\ua5a5') {
                        buffer.append('\ua5a5');
                    }
                    buffer.append(valueChar);
                }
            } else {
                if (length == 42405) {
                    if (valueChar == '\ua5a5') {
                        buffer.append('\ua5a5');
                    }
                    buffer.append(valueChar);
                    --length;
                }
                buffer.append('\ua5a5');
                buffer.append((char)length);
                buffer.append(valueChar);
            }
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    private static final <T extends Appendable> void encodeRun(T buffer, byte value, int length, byte[] state) {
        if (length < 4) {
            for (int j2 = 0; j2 < length; ++j2) {
                if (value == -91) {
                    Utility.appendEncodedByte(buffer, (byte)-91, state);
                }
                Utility.appendEncodedByte(buffer, value, state);
            }
        } else {
            if ((byte)length == -91) {
                if (value == -91) {
                    Utility.appendEncodedByte(buffer, (byte)-91, state);
                }
                Utility.appendEncodedByte(buffer, value, state);
                --length;
            }
            Utility.appendEncodedByte(buffer, (byte)-91, state);
            Utility.appendEncodedByte(buffer, (byte)length, state);
            Utility.appendEncodedByte(buffer, value, state);
        }
    }

    private static final <T extends Appendable> void appendEncodedByte(T buffer, byte value, byte[] state) {
        try {
            if (state[0] != 0) {
                char c2 = (char)(state[1] << 8 | value & 0xFF);
                buffer.append(c2);
                state[0] = 0;
            } else {
                state[0] = 1;
                state[1] = value;
            }
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    public static final int[] RLEStringToIntArray(String s2) {
        int length = Utility.getInt(s2, 0);
        int[] array = new int[length];
        int ai = 0;
        int i2 = 1;
        int maxI = s2.length() / 2;
        while (ai < length && i2 < maxI) {
            int c2;
            if ((c2 = Utility.getInt(s2, i2++)) == 42405) {
                if ((c2 = Utility.getInt(s2, i2++)) == 42405) {
                    array[ai++] = c2;
                    continue;
                }
                int runLength = c2;
                int runValue = Utility.getInt(s2, i2++);
                for (int j2 = 0; j2 < runLength; ++j2) {
                    array[ai++] = runValue;
                }
                continue;
            }
            array[ai++] = c2;
        }
        if (ai != length || i2 != maxI) {
            throw new IllegalStateException("Bad run-length encoded int array");
        }
        return array;
    }

    static final int getInt(String s2, int i2) {
        return s2.charAt(2 * i2) << 16 | s2.charAt(2 * i2 + 1);
    }

    public static final short[] RLEStringToShortArray(String s2) {
        int length = s2.charAt(0) << 16 | s2.charAt(1);
        short[] array = new short[length];
        int ai = 0;
        for (int i2 = 2; i2 < s2.length(); ++i2) {
            int c2 = s2.charAt(i2);
            if (c2 == 42405) {
                if ((c2 = s2.charAt(++i2)) == 42405) {
                    array[ai++] = (short)c2;
                    continue;
                }
                int runLength = c2;
                short runValue = (short)s2.charAt(++i2);
                for (int j2 = 0; j2 < runLength; ++j2) {
                    array[ai++] = runValue;
                }
                continue;
            }
            array[ai++] = (short)c2;
        }
        if (ai != length) {
            throw new IllegalStateException("Bad run-length encoded short array");
        }
        return array;
    }

    public static final char[] RLEStringToCharArray(String s2) {
        int length = s2.charAt(0) << 16 | s2.charAt(1);
        char[] array = new char[length];
        int ai = 0;
        for (int i2 = 2; i2 < s2.length(); ++i2) {
            int c2 = s2.charAt(i2);
            if (c2 == 42405) {
                if ((c2 = s2.charAt(++i2)) == 42405) {
                    array[ai++] = c2;
                    continue;
                }
                int runLength = c2;
                char runValue = s2.charAt(++i2);
                for (int j2 = 0; j2 < runLength; ++j2) {
                    array[ai++] = runValue;
                }
                continue;
            }
            array[ai++] = c2;
        }
        if (ai != length) {
            throw new IllegalStateException("Bad run-length encoded short array");
        }
        return array;
    }

    public static final byte[] RLEStringToByteArray(String s2) {
        int length = s2.charAt(0) << 16 | s2.charAt(1);
        byte[] array = new byte[length];
        boolean nextChar = true;
        int c2 = 0;
        int node = 0;
        int runLength = 0;
        int i2 = 2;
        int ai = 0;
        while (ai < length) {
            int b2;
            if (nextChar) {
                c2 = s2.charAt(i2++);
                b2 = (byte)(c2 >> 8);
                nextChar = false;
            } else {
                b2 = c2 & 0xFF;
                nextChar = true;
            }
            switch (node) {
                case 0: {
                    if (b2 == -91) {
                        node = 1;
                        break;
                    }
                    array[ai++] = b2;
                    break;
                }
                case 1: {
                    if (b2 == -91) {
                        array[ai++] = -91;
                        node = 0;
                        break;
                    }
                    runLength = b2;
                    if (runLength < 0) {
                        runLength += 256;
                    }
                    node = 2;
                    break;
                }
                case 2: {
                    for (int j2 = 0; j2 < runLength; ++j2) {
                        array[ai++] = b2;
                    }
                    node = 0;
                }
            }
        }
        if (node != 0) {
            throw new IllegalStateException("Bad run-length encoded byte array");
        }
        if (i2 != s2.length()) {
            throw new IllegalStateException("Excess data in RLE byte array string");
        }
        return array;
    }

    public static final String formatForSource(String s2) {
        StringBuilder buffer = new StringBuilder();
        int i2 = 0;
        while (i2 < s2.length()) {
            if (i2 > 0) {
                buffer.append('+').append(LINE_SEPARATOR);
            }
            buffer.append("        \"");
            int count = 11;
            while (i2 < s2.length() && count < 80) {
                char c2;
                if ((c2 = s2.charAt(i2++)) < ' ' || c2 == '\"' || c2 == '\\') {
                    if (c2 == '\n') {
                        buffer.append("\\n");
                        count += 2;
                        continue;
                    }
                    if (c2 == '\t') {
                        buffer.append("\\t");
                        count += 2;
                        continue;
                    }
                    if (c2 == '\r') {
                        buffer.append("\\r");
                        count += 2;
                        continue;
                    }
                    buffer.append('\\');
                    buffer.append(HEX_DIGIT[(c2 & 0x1C0) >> 6]);
                    buffer.append(HEX_DIGIT[(c2 & 0x38) >> 3]);
                    buffer.append(HEX_DIGIT[c2 & 7]);
                    count += 4;
                    continue;
                }
                if (c2 <= '~') {
                    buffer.append(c2);
                    ++count;
                    continue;
                }
                buffer.append("\\u");
                buffer.append(HEX_DIGIT[(c2 & 0xF000) >> 12]);
                buffer.append(HEX_DIGIT[(c2 & 0xF00) >> 8]);
                buffer.append(HEX_DIGIT[(c2 & 0xF0) >> 4]);
                buffer.append(HEX_DIGIT[c2 & 0xF]);
                count += 6;
            }
            buffer.append('\"');
        }
        return buffer.toString();
    }

    public static final String format1ForSource(String s2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\"");
        int i2 = 0;
        while (i2 < s2.length()) {
            char c2;
            if ((c2 = s2.charAt(i2++)) < ' ' || c2 == '\"' || c2 == '\\') {
                if (c2 == '\n') {
                    buffer.append("\\n");
                    continue;
                }
                if (c2 == '\t') {
                    buffer.append("\\t");
                    continue;
                }
                if (c2 == '\r') {
                    buffer.append("\\r");
                    continue;
                }
                buffer.append('\\');
                buffer.append(HEX_DIGIT[(c2 & 0x1C0) >> 6]);
                buffer.append(HEX_DIGIT[(c2 & 0x38) >> 3]);
                buffer.append(HEX_DIGIT[c2 & 7]);
                continue;
            }
            if (c2 <= '~') {
                buffer.append(c2);
                continue;
            }
            buffer.append("\\u");
            buffer.append(HEX_DIGIT[(c2 & 0xF000) >> 12]);
            buffer.append(HEX_DIGIT[(c2 & 0xF00) >> 8]);
            buffer.append(HEX_DIGIT[(c2 & 0xF0) >> 4]);
            buffer.append(HEX_DIGIT[c2 & 0xF]);
        }
        buffer.append('\"');
        return buffer.toString();
    }

    public static final String escape(String s2) {
        int c2;
        StringBuilder buf = new StringBuilder();
        for (int i2 = 0; i2 < s2.length(); i2 += UTF16.getCharCount(c2)) {
            c2 = Character.codePointAt(s2, i2);
            if (c2 >= 32 && c2 <= 127) {
                if (c2 == 92) {
                    buf.append("\\\\");
                    continue;
                }
                buf.append((char)c2);
                continue;
            }
            boolean four = c2 <= 65535;
            buf.append(four ? "\\u" : "\\U");
            buf.append(Utility.hex(c2, four ? 4 : 8));
        }
        return buf.toString();
    }

    public static int unescapeAt(String s2, int[] offset16) {
        int dig;
        int result = 0;
        int n2 = 0;
        int minDig = 0;
        int maxDig = 0;
        int bitsPerDigit = 4;
        boolean braces = false;
        int offset = offset16[0];
        int length = s2.length();
        if (offset < 0 || offset >= length) {
            return -1;
        }
        int c2 = Character.codePointAt(s2, offset);
        offset += UTF16.getCharCount(c2);
        switch (c2) {
            case 117: {
                maxDig = 4;
                minDig = 4;
                break;
            }
            case 85: {
                maxDig = 8;
                minDig = 8;
                break;
            }
            case 120: {
                minDig = 1;
                if (offset < length && UTF16.charAt(s2, offset) == 123) {
                    ++offset;
                    braces = true;
                    maxDig = 8;
                    break;
                }
                maxDig = 2;
                break;
            }
            default: {
                dig = UCharacter.digit(c2, 8);
                if (dig < 0) break;
                minDig = 1;
                maxDig = 3;
                n2 = 1;
                bitsPerDigit = 3;
                result = dig;
            }
        }
        if (minDig != 0) {
            while (offset < length && n2 < maxDig && (dig = UCharacter.digit(c2 = UTF16.charAt(s2, offset), bitsPerDigit == 3 ? 8 : 16)) >= 0) {
                result = result << bitsPerDigit | dig;
                offset += UTF16.getCharCount(c2);
                ++n2;
            }
            if (n2 < minDig) {
                return -1;
            }
            if (braces) {
                if (c2 != 125) {
                    return -1;
                }
                ++offset;
            }
            if (result < 0 || result >= 0x110000) {
                return -1;
            }
            if (offset < length && UTF16.isLeadSurrogate((char)result)) {
                int ahead = offset + 1;
                c2 = s2.charAt(offset);
                if (c2 == 92 && ahead < length) {
                    int[] o2 = new int[]{ahead};
                    c2 = Utility.unescapeAt(s2, o2);
                    ahead = o2[0];
                }
                if (UTF16.isTrailSurrogate((char)c2)) {
                    offset = ahead;
                    result = Character.toCodePoint((char)result, (char)c2);
                }
            }
            offset16[0] = offset;
            return result;
        }
        for (int i2 = 0; i2 < UNESCAPE_MAP.length; i2 += 2) {
            if (c2 == UNESCAPE_MAP[i2]) {
                offset16[0] = offset;
                return UNESCAPE_MAP[i2 + 1];
            }
            if (c2 < UNESCAPE_MAP[i2]) break;
        }
        if (c2 == 99 && offset < length) {
            c2 = UTF16.charAt(s2, offset);
            offset16[0] = offset + UTF16.getCharCount(c2);
            return 0x1F & c2;
        }
        offset16[0] = offset;
        return c2;
    }

    public static String unescape(String s2) {
        StringBuilder buf = new StringBuilder();
        int[] pos = new int[1];
        int i2 = 0;
        while (i2 < s2.length()) {
            char c2;
            if ((c2 = s2.charAt(i2++)) == '\\') {
                pos[0] = i2;
                int e2 = Utility.unescapeAt(s2, pos);
                if (e2 < 0) {
                    throw new IllegalArgumentException("Invalid escape sequence " + s2.substring(i2 - 1, Math.min(i2 + 8, s2.length())));
                }
                buf.appendCodePoint(e2);
                i2 = pos[0];
                continue;
            }
            buf.append(c2);
        }
        return buf.toString();
    }

    public static String unescapeLeniently(String s2) {
        StringBuilder buf = new StringBuilder();
        int[] pos = new int[1];
        int i2 = 0;
        while (i2 < s2.length()) {
            char c2;
            if ((c2 = s2.charAt(i2++)) == '\\') {
                pos[0] = i2;
                int e2 = Utility.unescapeAt(s2, pos);
                if (e2 < 0) {
                    buf.append(c2);
                    continue;
                }
                buf.appendCodePoint(e2);
                i2 = pos[0];
                continue;
            }
            buf.append(c2);
        }
        return buf.toString();
    }

    public static String hex(long ch) {
        return Utility.hex(ch, 4);
    }

    public static String hex(long i2, int places) {
        String result;
        boolean negative;
        if (i2 == Long.MIN_VALUE) {
            return "-8000000000000000";
        }
        boolean bl = negative = i2 < 0L;
        if (negative) {
            i2 = -i2;
        }
        if ((result = Long.toString(i2, 16).toUpperCase(Locale.ENGLISH)).length() < places) {
            result = "0000000000000000".substring(result.length(), places) + result;
        }
        if (negative) {
            return '-' + result;
        }
        return result;
    }

    public static String hex(CharSequence s2) {
        return Utility.hex(s2, 4, ",", true, new StringBuilder()).toString();
    }

    public static <S extends CharSequence, U extends CharSequence, T extends Appendable> T hex(S s2, int width, U separator, boolean useCodePoints, T result) {
        try {
            if (useCodePoints) {
                int cp;
                for (int i2 = 0; i2 < s2.length(); i2 += UTF16.getCharCount(cp)) {
                    cp = Character.codePointAt(s2, i2);
                    if (i2 != 0) {
                        result.append(separator);
                    }
                    result.append(Utility.hex(cp, width));
                }
            } else {
                for (int i3 = 0; i3 < s2.length(); ++i3) {
                    if (i3 != 0) {
                        result.append(separator);
                    }
                    result.append(Utility.hex(s2.charAt(i3), width));
                }
            }
            return result;
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    public static String hex(byte[] o2, int start, int end, String separator) {
        StringBuilder result = new StringBuilder();
        for (int i2 = start; i2 < end; ++i2) {
            if (i2 != 0) {
                result.append(separator);
            }
            result.append(Utility.hex(o2[i2]));
        }
        return result.toString();
    }

    public static <S extends CharSequence> String hex(S s2, int width, S separator) {
        return Utility.hex(s2, width, separator, true, new StringBuilder()).toString();
    }

    public static void split(String s2, char divider, String[] output) {
        int i2;
        int last = 0;
        int current = 0;
        for (i2 = 0; i2 < s2.length(); ++i2) {
            if (s2.charAt(i2) != divider) continue;
            output[current++] = s2.substring(last, i2);
            last = i2 + 1;
        }
        output[current++] = s2.substring(last, i2);
        while (current < output.length) {
            output[current++] = "";
        }
    }

    public static String[] split(String s2, char divider) {
        int i2;
        int last = 0;
        ArrayList<String> output = new ArrayList<String>();
        for (i2 = 0; i2 < s2.length(); ++i2) {
            if (s2.charAt(i2) != divider) continue;
            output.add(s2.substring(last, i2));
            last = i2 + 1;
        }
        output.add(s2.substring(last, i2));
        return output.toArray(new String[output.size()]);
    }

    public static int lookup(String source, String[] target) {
        for (int i2 = 0; i2 < target.length; ++i2) {
            if (!source.equals(target[i2])) continue;
            return i2;
        }
        return -1;
    }

    public static boolean parseChar(String id, int[] pos, char ch) {
        int start = pos[0];
        pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
        if (pos[0] == id.length() || id.charAt(pos[0]) != ch) {
            pos[0] = start;
            return false;
        }
        pos[0] = pos[0] + 1;
        return true;
    }

    public static int parsePattern(String rule, int pos, int limit, String pattern, int[] parsedInts) {
        int[] p2 = new int[1];
        int intCount = 0;
        block5: for (int i2 = 0; i2 < pattern.length(); ++i2) {
            char cpat = pattern.charAt(i2);
            switch (cpat) {
                case ' ': {
                    char c2;
                    if (pos >= limit) {
                        return -1;
                    }
                    if (!PatternProps.isWhiteSpace(c2 = rule.charAt(pos++))) {
                        return -1;
                    }
                }
                case '~': {
                    pos = PatternProps.skipWhiteSpace(rule, pos);
                    continue block5;
                }
                case '#': {
                    p2[0] = pos;
                    parsedInts[intCount++] = Utility.parseInteger(rule, p2, limit);
                    if (p2[0] == pos) {
                        return -1;
                    }
                    pos = p2[0];
                    continue block5;
                }
                default: {
                    char c2;
                    if (pos >= limit) {
                        return -1;
                    }
                    if ((c2 = (char)UCharacter.toLowerCase(rule.charAt(pos++))) == cpat) continue block5;
                    return -1;
                }
            }
        }
        return pos;
    }

    public static int parsePattern(String pat, Replaceable text, int index, int limit) {
        int ipat = 0;
        if (ipat == pat.length()) {
            return index;
        }
        int cpat = Character.codePointAt(pat, ipat);
        while (index < limit) {
            int c2 = text.char32At(index);
            if (cpat == 126) {
                if (PatternProps.isWhiteSpace(c2)) {
                    index += UTF16.getCharCount(c2);
                    continue;
                }
                if (++ipat == pat.length()) {
                    return index;
                }
            } else if (c2 == cpat) {
                int n2 = UTF16.getCharCount(c2);
                index += n2;
                if ((ipat += n2) == pat.length()) {
                    return index;
                }
            } else {
                return -1;
            }
            cpat = UTF16.charAt(pat, ipat);
        }
        return -1;
    }

    public static int parseInteger(String rule, int[] pos, int limit) {
        int count = 0;
        int value = 0;
        int p2 = pos[0];
        int radix = 10;
        if (rule.regionMatches(true, p2, "0x", 0, 2)) {
            p2 += 2;
            radix = 16;
        } else if (p2 < limit && rule.charAt(p2) == '0') {
            ++p2;
            count = 1;
            radix = 8;
        }
        while (p2 < limit) {
            int d2;
            if ((d2 = UCharacter.digit(rule.charAt(p2++), radix)) < 0) {
                --p2;
                break;
            }
            ++count;
            int v2 = value * radix + d2;
            if (v2 <= value) {
                return 0;
            }
            value = v2;
        }
        if (count > 0) {
            pos[0] = p2;
        }
        return value;
    }

    public static String parseUnicodeIdentifier(String str, int[] pos) {
        int p2;
        int ch;
        StringBuilder buf = new StringBuilder();
        for (p2 = pos[0]; p2 < str.length(); p2 += UTF16.getCharCount(ch)) {
            ch = Character.codePointAt(str, p2);
            if (buf.length() == 0) {
                if (UCharacter.isUnicodeIdentifierStart(ch)) {
                    buf.appendCodePoint(ch);
                    continue;
                }
                return null;
            }
            if (!UCharacter.isUnicodeIdentifierPart(ch)) break;
            buf.appendCodePoint(ch);
        }
        pos[0] = p2;
        return buf.toString();
    }

    private static <T extends Appendable> void recursiveAppendNumber(T result, int n2, int radix, int minDigits) {
        try {
            int digit = n2 % radix;
            if (n2 >= radix || minDigits > 1) {
                Utility.recursiveAppendNumber(result, n2 / radix, radix, minDigits - 1);
            }
            result.append(DIGITS[digit]);
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    public static <T extends Appendable> T appendNumber(T result, int n2, int radix, int minDigits) {
        try {
            if (radix < 2 || radix > 36) {
                throw new IllegalArgumentException("Illegal radix " + radix);
            }
            int abs = n2;
            if (n2 < 0) {
                abs = -n2;
                result.append("-");
            }
            Utility.recursiveAppendNumber(result, abs, radix, minDigits);
            return result;
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    public static int parseNumber(String text, int[] pos, int radix) {
        int ch;
        int d2;
        int p2;
        int n2 = 0;
        for (p2 = pos[0]; p2 < text.length() && (d2 = UCharacter.digit(ch = Character.codePointAt(text, p2), radix)) >= 0; ++p2) {
            if ((n2 = radix * n2 + d2) >= 0) continue;
            return -1;
        }
        if (p2 == pos[0]) {
            return -1;
        }
        pos[0] = p2;
        return n2;
    }

    public static boolean isUnprintable(int c2) {
        return c2 < 32 || c2 > 126;
    }

    public static <T extends Appendable> boolean escapeUnprintable(T result, int c2) {
        try {
            if (Utility.isUnprintable(c2)) {
                result.append('\\');
                if ((c2 & 0xFFFF0000) != 0) {
                    result.append('U');
                    result.append(DIGITS[0xF & c2 >> 28]);
                    result.append(DIGITS[0xF & c2 >> 24]);
                    result.append(DIGITS[0xF & c2 >> 20]);
                    result.append(DIGITS[0xF & c2 >> 16]);
                } else {
                    result.append('u');
                }
                result.append(DIGITS[0xF & c2 >> 12]);
                result.append(DIGITS[0xF & c2 >> 8]);
                result.append(DIGITS[0xF & c2 >> 4]);
                result.append(DIGITS[0xF & c2]);
                return true;
            }
            return false;
        }
        catch (IOException e2) {
            throw new IllegalIcuArgumentException(e2);
        }
    }

    public static int quotedIndexOf(String text, int start, int limit, String setOfChars) {
        for (int i2 = start; i2 < limit; ++i2) {
            char c2 = text.charAt(i2);
            if (c2 == '\\') {
                ++i2;
                continue;
            }
            if (c2 == '\'') {
                while (++i2 < limit && text.charAt(i2) != '\'') {
                }
                continue;
            }
            if (setOfChars.indexOf(c2) < 0) continue;
            return i2;
        }
        return -1;
    }

    public static void appendToRule(StringBuffer rule, int c2, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf) {
        if (isLiteral || escapeUnprintable && Utility.isUnprintable(c2)) {
            if (quoteBuf.length() > 0) {
                while (quoteBuf.length() >= 2 && quoteBuf.charAt(0) == '\'' && quoteBuf.charAt(1) == '\'') {
                    rule.append('\\').append('\'');
                    quoteBuf.delete(0, 2);
                }
                int trailingCount = 0;
                while (quoteBuf.length() >= 2 && quoteBuf.charAt(quoteBuf.length() - 2) == '\'' && quoteBuf.charAt(quoteBuf.length() - 1) == '\'') {
                    quoteBuf.setLength(quoteBuf.length() - 2);
                    ++trailingCount;
                }
                if (quoteBuf.length() > 0) {
                    rule.append('\'');
                    rule.append(quoteBuf);
                    rule.append('\'');
                    quoteBuf.setLength(0);
                }
                while (trailingCount-- > 0) {
                    rule.append('\\').append('\'');
                }
            }
            if (c2 != -1) {
                if (c2 == 32) {
                    int len = rule.length();
                    if (len > 0 && rule.charAt(len - 1) != ' ') {
                        rule.append(' ');
                    }
                } else if (!escapeUnprintable || !Utility.escapeUnprintable(rule, c2)) {
                    rule.appendCodePoint(c2);
                }
            }
        } else if (quoteBuf.length() == 0 && (c2 == 39 || c2 == 92)) {
            rule.append('\\').append((char)c2);
        } else if (!(quoteBuf.length() <= 0 && (c2 < 33 || c2 > 126 || c2 >= 48 && c2 <= 57 || c2 >= 65 && c2 <= 90 || c2 >= 97 && c2 <= 122) && !PatternProps.isWhiteSpace(c2))) {
            quoteBuf.appendCodePoint(c2);
            if (c2 == 39) {
                quoteBuf.append((char)c2);
            }
        } else {
            rule.appendCodePoint(c2);
        }
    }

    public static void appendToRule(StringBuffer rule, String text, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf) {
        for (int i2 = 0; i2 < text.length(); ++i2) {
            Utility.appendToRule(rule, text.charAt(i2), isLiteral, escapeUnprintable, quoteBuf);
        }
    }

    public static void appendToRule(StringBuffer rule, UnicodeMatcher matcher, boolean escapeUnprintable, StringBuffer quoteBuf) {
        if (matcher != null) {
            Utility.appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
        }
    }

    public static final int compareUnsigned(int source, int target) {
        if ((source -= Integer.MIN_VALUE) < (target -= Integer.MIN_VALUE)) {
            return -1;
        }
        if (source > target) {
            return 1;
        }
        return 0;
    }

    public static final byte highBit(int n2) {
        if (n2 <= 0) {
            return -1;
        }
        byte bit = 0;
        if (n2 >= 65536) {
            n2 >>= 16;
            bit = (byte)(bit + 16);
        }
        if (n2 >= 256) {
            n2 >>= 8;
            bit = (byte)(bit + 8);
        }
        if (n2 >= 16) {
            n2 >>= 4;
            bit = (byte)(bit + 4);
        }
        if (n2 >= 4) {
            n2 >>= 2;
            bit = (byte)(bit + 2);
        }
        if (n2 >= 2) {
            n2 >>= 1;
            bit = (byte)(bit + 1);
        }
        return bit;
    }

    public static String valueOf(int[] source) {
        StringBuilder result = new StringBuilder(source.length);
        for (int i2 = 0; i2 < source.length; ++i2) {
            result.appendCodePoint(source[i2]);
        }
        return result.toString();
    }

    public static String repeat(String s2, int count) {
        if (count <= 0) {
            return "";
        }
        if (count == 1) {
            return s2;
        }
        StringBuilder result = new StringBuilder();
        for (int i2 = 0; i2 < count; ++i2) {
            result.append(s2);
        }
        return result.toString();
    }

    public static String[] splitString(String src, String target) {
        return src.split("\\Q" + target + "\\E");
    }

    public static String[] splitWhitespace(String src) {
        return src.split("\\s+");
    }

    public static String fromHex(String string, int minLength, String separator) {
        return Utility.fromHex(string, minLength, Pattern.compile(separator != null ? separator : "\\s+"));
    }

    public static String fromHex(String string, int minLength, Pattern separator) {
        String[] parts;
        StringBuilder buffer = new StringBuilder();
        for (String part : parts = separator.split(string)) {
            if (part.length() < minLength) {
                throw new IllegalArgumentException("code point too short: " + part);
            }
            int cp = Integer.parseInt(part, 16);
            buffer.appendCodePoint(cp);
        }
        return buffer.toString();
    }

    public static boolean equals(Object a2, Object b2) {
        return a2 == b2 || a2 != null && b2 != null && a2.equals(b2);
    }

    public static int hash(Object ... values) {
        return Arrays.hashCode(values);
    }

    public static int hashCode(Object o2) {
        return o2 == null ? 0 : o2.hashCode();
    }

    public static String toString(Object o2) {
        return o2 == null ? "null" : o2.toString();
    }

    public static int addExact(int x2, int y2) {
        int r2 = x2 + y2;
        if (((x2 ^ r2) & (y2 ^ r2)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return r2;
    }

    public static boolean charSequenceEquals(CharSequence a2, CharSequence b2) {
        if (a2 == b2) {
            return true;
        }
        if (a2 == null || b2 == null) {
            return false;
        }
        if (a2.length() != b2.length()) {
            return false;
        }
        for (int i2 = 0; i2 < a2.length(); ++i2) {
            if (a2.charAt(i2) == b2.charAt(i2)) continue;
            return false;
        }
        return true;
    }

    public static int charSequenceHashCode(CharSequence value) {
        int hash = 0;
        for (int i2 = 0; i2 < value.length(); ++i2) {
            hash = hash * 31 + value.charAt(i2);
        }
        return hash;
    }
}

