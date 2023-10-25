/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Replaceable;
import java.util.Comparator;

public final class UTF16 {
    public static final int SINGLE_CHAR_BOUNDARY = 1;
    public static final int LEAD_SURROGATE_BOUNDARY = 2;
    public static final int TRAIL_SURROGATE_BOUNDARY = 5;
    public static final int CODEPOINT_MIN_VALUE = 0;
    public static final int CODEPOINT_MAX_VALUE = 0x10FFFF;
    public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
    public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
    public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
    public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
    public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
    public static final int SURROGATE_MIN_VALUE = 55296;
    public static final int SURROGATE_MAX_VALUE = 57343;
    private static final int LEAD_SURROGATE_BITMASK = -1024;
    private static final int TRAIL_SURROGATE_BITMASK = -1024;
    private static final int SURROGATE_BITMASK = -2048;
    private static final int LEAD_SURROGATE_BITS = 55296;
    private static final int TRAIL_SURROGATE_BITS = 56320;
    private static final int SURROGATE_BITS = 55296;
    private static final int LEAD_SURROGATE_SHIFT_ = 10;
    private static final int TRAIL_SURROGATE_MASK_ = 1023;
    private static final int LEAD_SURROGATE_OFFSET_ = 55232;

    private UTF16() {
    }

    public static int charAt(String source, int offset16) {
        char single = source.charAt(offset16);
        if (single < '\ud800') {
            return single;
        }
        return UTF16._charAt(source, offset16, single);
    }

    private static int _charAt(String source, int offset16, char single) {
        char lead;
        if (single > '\udfff') {
            return single;
        }
        if (single <= '\udbff') {
            char trail;
            if (source.length() != ++offset16 && (trail = source.charAt(offset16)) >= '\udc00' && trail <= '\udfff') {
                return UCharacterProperty.getRawSupplementary(single, trail);
            }
        } else if (--offset16 >= 0 && (lead = source.charAt(offset16)) >= '\ud800' && lead <= '\udbff') {
            return UCharacterProperty.getRawSupplementary(lead, single);
        }
        return single;
    }

    public static int charAt(CharSequence source, int offset16) {
        char single = source.charAt(offset16);
        if (single < '\ud800') {
            return single;
        }
        return UTF16._charAt(source, offset16, single);
    }

    private static int _charAt(CharSequence source, int offset16, char single) {
        char lead;
        if (single > '\udfff') {
            return single;
        }
        if (single <= '\udbff') {
            char trail;
            if (source.length() != ++offset16 && (trail = source.charAt(offset16)) >= '\udc00' && trail <= '\udfff') {
                return UCharacterProperty.getRawSupplementary(single, trail);
            }
        } else if (--offset16 >= 0 && (lead = source.charAt(offset16)) >= '\ud800' && lead <= '\udbff') {
            return UCharacterProperty.getRawSupplementary(lead, single);
        }
        return single;
    }

    public static int charAt(StringBuffer source, int offset16) {
        char lead;
        if (offset16 < 0 || offset16 >= source.length()) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        char single = source.charAt(offset16);
        if (!UTF16.isSurrogate(single)) {
            return single;
        }
        if (single <= '\udbff') {
            char trail;
            if (source.length() != ++offset16 && UTF16.isTrailSurrogate(trail = source.charAt(offset16))) {
                return UCharacterProperty.getRawSupplementary(single, trail);
            }
        } else if (--offset16 >= 0 && UTF16.isLeadSurrogate(lead = source.charAt(offset16))) {
            return UCharacterProperty.getRawSupplementary(lead, single);
        }
        return single;
    }

    public static int charAt(char[] source, int start, int limit, int offset16) {
        if ((offset16 += start) < start || offset16 >= limit) {
            throw new ArrayIndexOutOfBoundsException(offset16);
        }
        char single = source[offset16];
        if (!UTF16.isSurrogate(single)) {
            return single;
        }
        if (single <= '\udbff') {
            if (++offset16 >= limit) {
                return single;
            }
            char trail = source[offset16];
            if (UTF16.isTrailSurrogate(trail)) {
                return UCharacterProperty.getRawSupplementary(single, trail);
            }
        } else {
            char lead;
            if (offset16 == start) {
                return single;
            }
            if (UTF16.isLeadSurrogate(lead = source[--offset16])) {
                return UCharacterProperty.getRawSupplementary(lead, single);
            }
        }
        return single;
    }

    public static int charAt(Replaceable source, int offset16) {
        char lead;
        if (offset16 < 0 || offset16 >= source.length()) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        char single = source.charAt(offset16);
        if (!UTF16.isSurrogate(single)) {
            return single;
        }
        if (single <= '\udbff') {
            char trail;
            if (source.length() != ++offset16 && UTF16.isTrailSurrogate(trail = source.charAt(offset16))) {
                return UCharacterProperty.getRawSupplementary(single, trail);
            }
        } else if (--offset16 >= 0 && UTF16.isLeadSurrogate(lead = source.charAt(offset16))) {
            return UCharacterProperty.getRawSupplementary(lead, single);
        }
        return single;
    }

    public static int getCharCount(int char32) {
        if (char32 < 65536) {
            return 1;
        }
        return 2;
    }

    public static int bounds(String source, int offset16) {
        char ch = source.charAt(offset16);
        if (UTF16.isSurrogate(ch)) {
            if (UTF16.isLeadSurrogate(ch)) {
                if (++offset16 < source.length() && UTF16.isTrailSurrogate(source.charAt(offset16))) {
                    return 2;
                }
            } else if (--offset16 >= 0 && UTF16.isLeadSurrogate(source.charAt(offset16))) {
                return 5;
            }
        }
        return 1;
    }

    public static int bounds(StringBuffer source, int offset16) {
        char ch = source.charAt(offset16);
        if (UTF16.isSurrogate(ch)) {
            if (UTF16.isLeadSurrogate(ch)) {
                if (++offset16 < source.length() && UTF16.isTrailSurrogate(source.charAt(offset16))) {
                    return 2;
                }
            } else if (--offset16 >= 0 && UTF16.isLeadSurrogate(source.charAt(offset16))) {
                return 5;
            }
        }
        return 1;
    }

    public static int bounds(char[] source, int start, int limit, int offset16) {
        if ((offset16 += start) < start || offset16 >= limit) {
            throw new ArrayIndexOutOfBoundsException(offset16);
        }
        char ch = source[offset16];
        if (UTF16.isSurrogate(ch)) {
            if (UTF16.isLeadSurrogate(ch)) {
                if (++offset16 < limit && UTF16.isTrailSurrogate(source[offset16])) {
                    return 2;
                }
            } else if (--offset16 >= start && UTF16.isLeadSurrogate(source[offset16])) {
                return 5;
            }
        }
        return 1;
    }

    public static boolean isSurrogate(char char16) {
        return (char16 & 0xFFFFF800) == 55296;
    }

    public static boolean isTrailSurrogate(char char16) {
        return (char16 & 0xFFFFFC00) == 56320;
    }

    public static boolean isLeadSurrogate(char char16) {
        return (char16 & 0xFFFFFC00) == 55296;
    }

    public static char getLeadSurrogate(int char32) {
        if (char32 >= 65536) {
            return (char)(55232 + (char32 >> 10));
        }
        return '\u0000';
    }

    public static char getTrailSurrogate(int char32) {
        if (char32 >= 65536) {
            return (char)(56320 + (char32 & 0x3FF));
        }
        return (char)char32;
    }

    public static String valueOf(int char32) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Illegal codepoint");
        }
        return UTF16.toString(char32);
    }

    public static String valueOf(String source, int offset16) {
        switch (UTF16.bounds(source, offset16)) {
            case 2: {
                return source.substring(offset16, offset16 + 2);
            }
            case 5: {
                return source.substring(offset16 - 1, offset16 + 1);
            }
        }
        return source.substring(offset16, offset16 + 1);
    }

    public static String valueOf(StringBuffer source, int offset16) {
        switch (UTF16.bounds(source, offset16)) {
            case 2: {
                return source.substring(offset16, offset16 + 2);
            }
            case 5: {
                return source.substring(offset16 - 1, offset16 + 1);
            }
        }
        return source.substring(offset16, offset16 + 1);
    }

    public static String valueOf(char[] source, int start, int limit, int offset16) {
        switch (UTF16.bounds(source, start, limit, offset16)) {
            case 2: {
                return new String(source, start + offset16, 2);
            }
            case 5: {
                return new String(source, start + offset16 - 1, 2);
            }
        }
        return new String(source, start + offset16, 1);
    }

    public static int findOffsetFromCodePoint(String source, int offset32) {
        int count;
        int size = source.length();
        int result = 0;
        if (offset32 < 0 || offset32 > size) {
            throw new StringIndexOutOfBoundsException(offset32);
        }
        for (count = offset32; result < size && count > 0; --count, ++result) {
            char ch = source.charAt(result);
            if (!UTF16.isLeadSurrogate(ch) || result + 1 >= size || !UTF16.isTrailSurrogate(source.charAt(result + 1))) continue;
            ++result;
        }
        if (count != 0) {
            throw new StringIndexOutOfBoundsException(offset32);
        }
        return result;
    }

    public static int findOffsetFromCodePoint(StringBuffer source, int offset32) {
        int count;
        int size = source.length();
        int result = 0;
        if (offset32 < 0 || offset32 > size) {
            throw new StringIndexOutOfBoundsException(offset32);
        }
        for (count = offset32; result < size && count > 0; --count, ++result) {
            char ch = source.charAt(result);
            if (!UTF16.isLeadSurrogate(ch) || result + 1 >= size || !UTF16.isTrailSurrogate(source.charAt(result + 1))) continue;
            ++result;
        }
        if (count != 0) {
            throw new StringIndexOutOfBoundsException(offset32);
        }
        return result;
    }

    public static int findOffsetFromCodePoint(char[] source, int start, int limit, int offset32) {
        int count;
        int result = start;
        if (offset32 > limit - start) {
            throw new ArrayIndexOutOfBoundsException(offset32);
        }
        for (count = offset32; result < limit && count > 0; --count, ++result) {
            char ch = source[result];
            if (!UTF16.isLeadSurrogate(ch) || result + 1 >= limit || !UTF16.isTrailSurrogate(source[result + 1])) continue;
            ++result;
        }
        if (count != 0) {
            throw new ArrayIndexOutOfBoundsException(offset32);
        }
        return result - start;
    }

    public static int findCodePointOffset(String source, int offset16) {
        if (offset16 < 0 || offset16 > source.length()) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        int result = 0;
        boolean hadLeadSurrogate = false;
        for (int i = 0; i < offset16; ++i) {
            char ch = source.charAt(i);
            if (hadLeadSurrogate && UTF16.isTrailSurrogate(ch)) {
                hadLeadSurrogate = false;
                continue;
            }
            hadLeadSurrogate = UTF16.isLeadSurrogate(ch);
            ++result;
        }
        if (offset16 == source.length()) {
            return result;
        }
        if (hadLeadSurrogate && UTF16.isTrailSurrogate(source.charAt(offset16))) {
            --result;
        }
        return result;
    }

    public static int findCodePointOffset(StringBuffer source, int offset16) {
        if (offset16 < 0 || offset16 > source.length()) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        int result = 0;
        boolean hadLeadSurrogate = false;
        for (int i = 0; i < offset16; ++i) {
            char ch = source.charAt(i);
            if (hadLeadSurrogate && UTF16.isTrailSurrogate(ch)) {
                hadLeadSurrogate = false;
                continue;
            }
            hadLeadSurrogate = UTF16.isLeadSurrogate(ch);
            ++result;
        }
        if (offset16 == source.length()) {
            return result;
        }
        if (hadLeadSurrogate && UTF16.isTrailSurrogate(source.charAt(offset16))) {
            --result;
        }
        return result;
    }

    public static int findCodePointOffset(char[] source, int start, int limit, int offset16) {
        if ((offset16 += start) > limit) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        int result = 0;
        boolean hadLeadSurrogate = false;
        for (int i = start; i < offset16; ++i) {
            char ch = source[i];
            if (hadLeadSurrogate && UTF16.isTrailSurrogate(ch)) {
                hadLeadSurrogate = false;
                continue;
            }
            hadLeadSurrogate = UTF16.isLeadSurrogate(ch);
            ++result;
        }
        if (offset16 == limit) {
            return result;
        }
        if (hadLeadSurrogate && UTF16.isTrailSurrogate(source[offset16])) {
            --result;
        }
        return result;
    }

    public static StringBuffer append(StringBuffer target, int char32) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(char32));
        }
        if (char32 >= 65536) {
            target.append(UTF16.getLeadSurrogate(char32));
            target.append(UTF16.getTrailSurrogate(char32));
        } else {
            target.append((char)char32);
        }
        return target;
    }

    public static StringBuffer appendCodePoint(StringBuffer target, int cp) {
        return UTF16.append(target, cp);
    }

    public static int append(char[] target, int limit, int char32) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Illegal codepoint");
        }
        if (char32 >= 65536) {
            target[limit++] = UTF16.getLeadSurrogate(char32);
            target[limit++] = UTF16.getTrailSurrogate(char32);
        } else {
            target[limit++] = (char)char32;
        }
        return limit;
    }

    public static int countCodePoint(String source) {
        if (source == null || source.length() == 0) {
            return 0;
        }
        return UTF16.findCodePointOffset(source, source.length());
    }

    public static int countCodePoint(StringBuffer source) {
        if (source == null || source.length() == 0) {
            return 0;
        }
        return UTF16.findCodePointOffset(source, source.length());
    }

    public static int countCodePoint(char[] source, int start, int limit) {
        if (source == null || source.length == 0) {
            return 0;
        }
        return UTF16.findCodePointOffset(source, start, limit, limit - start);
    }

    public static void setCharAt(StringBuffer target, int offset16, int char32) {
        int count = 1;
        char single = target.charAt(offset16);
        if (UTF16.isSurrogate(single)) {
            if (UTF16.isLeadSurrogate(single) && target.length() > offset16 + 1 && UTF16.isTrailSurrogate(target.charAt(offset16 + 1))) {
                ++count;
            } else if (UTF16.isTrailSurrogate(single) && offset16 > 0 && UTF16.isLeadSurrogate(target.charAt(offset16 - 1))) {
                --offset16;
                ++count;
            }
        }
        target.replace(offset16, offset16 + count, UTF16.valueOf(char32));
    }

    public static int setCharAt(char[] target, int limit, int offset16, int char32) {
        if (offset16 >= limit) {
            throw new ArrayIndexOutOfBoundsException(offset16);
        }
        int count = 1;
        char single = target[offset16];
        if (UTF16.isSurrogate(single)) {
            if (UTF16.isLeadSurrogate(single) && target.length > offset16 + 1 && UTF16.isTrailSurrogate(target[offset16 + 1])) {
                ++count;
            } else if (UTF16.isTrailSurrogate(single) && offset16 > 0 && UTF16.isLeadSurrogate(target[offset16 - 1])) {
                --offset16;
                ++count;
            }
        }
        String str = UTF16.valueOf(char32);
        int result = limit;
        int strlength = str.length();
        target[offset16] = str.charAt(0);
        if (count == strlength) {
            if (count == 2) {
                target[offset16 + 1] = str.charAt(1);
            }
        } else {
            System.arraycopy(target, offset16 + count, target, offset16 + strlength, limit - (offset16 + count));
            if (count < strlength) {
                target[offset16 + 1] = str.charAt(1);
                if (++result < target.length) {
                    target[result] = '\u0000';
                }
            } else {
                target[--result] = '\u0000';
            }
        }
        return result;
    }

    public static int moveCodePointOffset(String source, int offset16, int shift32) {
        int count;
        int result = offset16;
        int size = source.length();
        if (offset16 < 0 || offset16 > size) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        if (shift32 > 0) {
            if (shift32 + offset16 > size) {
                throw new StringIndexOutOfBoundsException(offset16);
            }
            for (count = shift32; result < size && count > 0; --count, ++result) {
                char ch = source.charAt(result);
                if (!UTF16.isLeadSurrogate(ch) || result + 1 >= size || !UTF16.isTrailSurrogate(source.charAt(result + 1))) continue;
                ++result;
            }
        } else {
            if (offset16 + shift32 < 0) {
                throw new StringIndexOutOfBoundsException(offset16);
            }
            for (count = -shift32; count > 0 && --result >= 0; --count) {
                char ch = source.charAt(result);
                if (!UTF16.isTrailSurrogate(ch) || result <= 0 || !UTF16.isLeadSurrogate(source.charAt(result - 1))) continue;
                --result;
            }
        }
        if (count != 0) {
            throw new StringIndexOutOfBoundsException(shift32);
        }
        return result;
    }

    public static int moveCodePointOffset(StringBuffer source, int offset16, int shift32) {
        int count;
        int result = offset16;
        int size = source.length();
        if (offset16 < 0 || offset16 > size) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        if (shift32 > 0) {
            if (shift32 + offset16 > size) {
                throw new StringIndexOutOfBoundsException(offset16);
            }
            for (count = shift32; result < size && count > 0; --count, ++result) {
                char ch = source.charAt(result);
                if (!UTF16.isLeadSurrogate(ch) || result + 1 >= size || !UTF16.isTrailSurrogate(source.charAt(result + 1))) continue;
                ++result;
            }
        } else {
            if (offset16 + shift32 < 0) {
                throw new StringIndexOutOfBoundsException(offset16);
            }
            for (count = -shift32; count > 0 && --result >= 0; --count) {
                char ch = source.charAt(result);
                if (!UTF16.isTrailSurrogate(ch) || result <= 0 || !UTF16.isLeadSurrogate(source.charAt(result - 1))) continue;
                --result;
            }
        }
        if (count != 0) {
            throw new StringIndexOutOfBoundsException(shift32);
        }
        return result;
    }

    public static int moveCodePointOffset(char[] source, int start, int limit, int offset16, int shift32) {
        int count;
        int size = source.length;
        int result = offset16 + start;
        if (start < 0 || limit < start) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (limit > size) {
            throw new StringIndexOutOfBoundsException(limit);
        }
        if (offset16 < 0 || result > limit) {
            throw new StringIndexOutOfBoundsException(offset16);
        }
        if (shift32 > 0) {
            if (shift32 + result > size) {
                throw new StringIndexOutOfBoundsException(result);
            }
            for (count = shift32; result < limit && count > 0; --count, ++result) {
                char ch = source[result];
                if (!UTF16.isLeadSurrogate(ch) || result + 1 >= limit || !UTF16.isTrailSurrogate(source[result + 1])) continue;
                ++result;
            }
        } else {
            if (result + shift32 < start) {
                throw new StringIndexOutOfBoundsException(result);
            }
            for (count = -shift32; count > 0 && --result >= start; --count) {
                char ch = source[result];
                if (!UTF16.isTrailSurrogate(ch) || result <= start || !UTF16.isLeadSurrogate(source[result - 1])) continue;
                --result;
            }
        }
        if (count != 0) {
            throw new StringIndexOutOfBoundsException(shift32);
        }
        return result -= start;
    }

    public static StringBuffer insert(StringBuffer target, int offset16, int char32) {
        String str = UTF16.valueOf(char32);
        if (offset16 != target.length() && UTF16.bounds(target, offset16) == 5) {
            ++offset16;
        }
        target.insert(offset16, str);
        return target;
    }

    public static int insert(char[] target, int limit, int offset16, int char32) {
        int size;
        String str = UTF16.valueOf(char32);
        if (offset16 != limit && UTF16.bounds(target, 0, limit, offset16) == 5) {
            ++offset16;
        }
        if (limit + (size = str.length()) > target.length) {
            throw new ArrayIndexOutOfBoundsException(offset16 + size);
        }
        System.arraycopy(target, offset16, target, offset16 + size, limit - offset16);
        target[offset16] = str.charAt(0);
        if (size == 2) {
            target[offset16 + 1] = str.charAt(1);
        }
        return limit + size;
    }

    public static StringBuffer delete(StringBuffer target, int offset16) {
        int count = 1;
        switch (UTF16.bounds(target, offset16)) {
            case 2: {
                ++count;
                break;
            }
            case 5: {
                ++count;
                --offset16;
            }
        }
        target.delete(offset16, offset16 + count);
        return target;
    }

    public static int delete(char[] target, int limit, int offset16) {
        int count = 1;
        switch (UTF16.bounds(target, 0, limit, offset16)) {
            case 2: {
                ++count;
                break;
            }
            case 5: {
                ++count;
                --offset16;
            }
        }
        System.arraycopy(target, offset16 + count, target, offset16, limit - (offset16 + count));
        target[limit - count] = '\u0000';
        return limit - count;
    }

    public static int indexOf(String source, int char32) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
        }
        if (char32 < 55296 || char32 > 57343 && char32 < 65536) {
            return source.indexOf((char)char32);
        }
        if (char32 < 65536) {
            int result = source.indexOf((char)char32);
            if (result >= 0) {
                if (UTF16.isLeadSurrogate((char)char32) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + 1))) {
                    return UTF16.indexOf(source, char32, result + 1);
                }
                if (result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                    return UTF16.indexOf(source, char32, result + 1);
                }
            }
            return result;
        }
        String char32str = UTF16.toString(char32);
        return source.indexOf(char32str);
    }

    public static int indexOf(String source, String str) {
        int strLength = str.length();
        if (!UTF16.isTrailSurrogate(str.charAt(0)) && !UTF16.isLeadSurrogate(str.charAt(strLength - 1))) {
            return source.indexOf(str);
        }
        int result = source.indexOf(str);
        int resultEnd = result + strLength;
        if (result >= 0) {
            if (UTF16.isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(resultEnd + 1))) {
                return UTF16.indexOf(source, str, resultEnd + 1);
            }
            if (UTF16.isTrailSurrogate(str.charAt(0)) && result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                return UTF16.indexOf(source, str, resultEnd + 1);
            }
        }
        return result;
    }

    public static int indexOf(String source, int char32, int fromIndex) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
        }
        if (char32 < 55296 || char32 > 57343 && char32 < 65536) {
            return source.indexOf((char)char32, fromIndex);
        }
        if (char32 < 65536) {
            int result = source.indexOf((char)char32, fromIndex);
            if (result >= 0) {
                if (UTF16.isLeadSurrogate((char)char32) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + 1))) {
                    return UTF16.indexOf(source, char32, result + 1);
                }
                if (result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                    return UTF16.indexOf(source, char32, result + 1);
                }
            }
            return result;
        }
        String char32str = UTF16.toString(char32);
        return source.indexOf(char32str, fromIndex);
    }

    public static int indexOf(String source, String str, int fromIndex) {
        int strLength = str.length();
        if (!UTF16.isTrailSurrogate(str.charAt(0)) && !UTF16.isLeadSurrogate(str.charAt(strLength - 1))) {
            return source.indexOf(str, fromIndex);
        }
        int result = source.indexOf(str, fromIndex);
        int resultEnd = result + strLength;
        if (result >= 0) {
            if (UTF16.isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(resultEnd))) {
                return UTF16.indexOf(source, str, resultEnd + 1);
            }
            if (UTF16.isTrailSurrogate(str.charAt(0)) && result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                return UTF16.indexOf(source, str, resultEnd + 1);
            }
        }
        return result;
    }

    public static int lastIndexOf(String source, int char32) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
        }
        if (char32 < 55296 || char32 > 57343 && char32 < 65536) {
            return source.lastIndexOf((char)char32);
        }
        if (char32 < 65536) {
            int result = source.lastIndexOf((char)char32);
            if (result >= 0) {
                if (UTF16.isLeadSurrogate((char)char32) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + 1))) {
                    return UTF16.lastIndexOf(source, char32, result - 1);
                }
                if (result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                    return UTF16.lastIndexOf(source, char32, result - 1);
                }
            }
            return result;
        }
        String char32str = UTF16.toString(char32);
        return source.lastIndexOf(char32str);
    }

    public static int lastIndexOf(String source, String str) {
        int strLength = str.length();
        if (!UTF16.isTrailSurrogate(str.charAt(0)) && !UTF16.isLeadSurrogate(str.charAt(strLength - 1))) {
            return source.lastIndexOf(str);
        }
        int result = source.lastIndexOf(str);
        if (result >= 0) {
            if (UTF16.isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + strLength + 1))) {
                return UTF16.lastIndexOf(source, str, result - 1);
            }
            if (UTF16.isTrailSurrogate(str.charAt(0)) && result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                return UTF16.lastIndexOf(source, str, result - 1);
            }
        }
        return result;
    }

    public static int lastIndexOf(String source, int char32, int fromIndex) {
        if (char32 < 0 || char32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument char32 is not a valid codepoint");
        }
        if (char32 < 55296 || char32 > 57343 && char32 < 65536) {
            return source.lastIndexOf((char)char32, fromIndex);
        }
        if (char32 < 65536) {
            int result = source.lastIndexOf((char)char32, fromIndex);
            if (result >= 0) {
                if (UTF16.isLeadSurrogate((char)char32) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + 1))) {
                    return UTF16.lastIndexOf(source, char32, result - 1);
                }
                if (result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                    return UTF16.lastIndexOf(source, char32, result - 1);
                }
            }
            return result;
        }
        String char32str = UTF16.toString(char32);
        return source.lastIndexOf(char32str, fromIndex);
    }

    public static int lastIndexOf(String source, String str, int fromIndex) {
        int strLength = str.length();
        if (!UTF16.isTrailSurrogate(str.charAt(0)) && !UTF16.isLeadSurrogate(str.charAt(strLength - 1))) {
            return source.lastIndexOf(str, fromIndex);
        }
        int result = source.lastIndexOf(str, fromIndex);
        if (result >= 0) {
            if (UTF16.isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && UTF16.isTrailSurrogate(source.charAt(result + strLength))) {
                return UTF16.lastIndexOf(source, str, result - 1);
            }
            if (UTF16.isTrailSurrogate(str.charAt(0)) && result > 0 && UTF16.isLeadSurrogate(source.charAt(result - 1))) {
                return UTF16.lastIndexOf(source, str, result - 1);
            }
        }
        return result;
    }

    public static String replace(String source, int oldChar32, int newChar32) {
        if (oldChar32 <= 0 || oldChar32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument oldChar32 is not a valid codepoint");
        }
        if (newChar32 <= 0 || newChar32 > 0x10FFFF) {
            throw new IllegalArgumentException("Argument newChar32 is not a valid codepoint");
        }
        int index = UTF16.indexOf(source, oldChar32);
        if (index == -1) {
            return source;
        }
        String newChar32Str = UTF16.toString(newChar32);
        int oldChar32Size = 1;
        int newChar32Size = newChar32Str.length();
        StringBuffer result = new StringBuffer(source);
        int resultIndex = index;
        if (oldChar32 >= 65536) {
            oldChar32Size = 2;
        }
        while (index != -1) {
            int endResultIndex = resultIndex + oldChar32Size;
            result.replace(resultIndex, endResultIndex, newChar32Str);
            int lastEndIndex = index + oldChar32Size;
            index = UTF16.indexOf(source, oldChar32, lastEndIndex);
            resultIndex += newChar32Size + index - lastEndIndex;
        }
        return result.toString();
    }

    public static String replace(String source, String oldStr, String newStr) {
        int index = UTF16.indexOf(source, oldStr);
        if (index == -1) {
            return source;
        }
        int oldStrSize = oldStr.length();
        int newStrSize = newStr.length();
        StringBuffer result = new StringBuffer(source);
        int resultIndex = index;
        while (index != -1) {
            int endResultIndex = resultIndex + oldStrSize;
            result.replace(resultIndex, endResultIndex, newStr);
            int lastEndIndex = index + oldStrSize;
            index = UTF16.indexOf(source, oldStr, lastEndIndex);
            resultIndex += newStrSize + index - lastEndIndex;
        }
        return result.toString();
    }

    public static StringBuffer reverse(StringBuffer source) {
        int length = source.length();
        StringBuffer result = new StringBuffer(length);
        int i = length;
        while (i-- > 0) {
            char ch2;
            char ch = source.charAt(i);
            if (UTF16.isTrailSurrogate(ch) && i > 0 && UTF16.isLeadSurrogate(ch2 = source.charAt(i - 1))) {
                result.append(ch2);
                result.append(ch);
                --i;
                continue;
            }
            result.append(ch);
        }
        return result;
    }

    public static boolean hasMoreCodePointsThan(String source, int number) {
        if (number < 0) {
            return true;
        }
        if (source == null) {
            return false;
        }
        int length = source.length();
        if (length + 1 >> 1 > number) {
            return true;
        }
        int maxsupplementary = length - number;
        if (maxsupplementary <= 0) {
            return false;
        }
        int start = 0;
        while (length != 0) {
            if (number == 0) {
                return true;
            }
            if (UTF16.isLeadSurrogate(source.charAt(start++)) && start != length && UTF16.isTrailSurrogate(source.charAt(start))) {
                ++start;
                if (--maxsupplementary <= 0) {
                    return false;
                }
            }
            --number;
        }
        return false;
    }

    public static boolean hasMoreCodePointsThan(char[] source, int start, int limit, int number) {
        int length = limit - start;
        if (length < 0 || start < 0 || limit < 0) {
            throw new IndexOutOfBoundsException("Start and limit indexes should be non-negative and start <= limit");
        }
        if (number < 0) {
            return true;
        }
        if (source == null) {
            return false;
        }
        if (length + 1 >> 1 > number) {
            return true;
        }
        int maxsupplementary = length - number;
        if (maxsupplementary <= 0) {
            return false;
        }
        while (length != 0) {
            if (number == 0) {
                return true;
            }
            if (UTF16.isLeadSurrogate(source[start++]) && start != limit && UTF16.isTrailSurrogate(source[start])) {
                ++start;
                if (--maxsupplementary <= 0) {
                    return false;
                }
            }
            --number;
        }
        return false;
    }

    public static boolean hasMoreCodePointsThan(StringBuffer source, int number) {
        if (number < 0) {
            return true;
        }
        if (source == null) {
            return false;
        }
        int length = source.length();
        if (length + 1 >> 1 > number) {
            return true;
        }
        int maxsupplementary = length - number;
        if (maxsupplementary <= 0) {
            return false;
        }
        int start = 0;
        while (length != 0) {
            if (number == 0) {
                return true;
            }
            if (UTF16.isLeadSurrogate(source.charAt(start++)) && start != length && UTF16.isTrailSurrogate(source.charAt(start))) {
                ++start;
                if (--maxsupplementary <= 0) {
                    return false;
                }
            }
            --number;
        }
        return false;
    }

    public static String newString(int[] codePoints, int offset, int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        char[] chars = new char[count];
        int w = 0;
        int e = offset + count;
        block2: for (int r = offset; r < e; ++r) {
            int cp = codePoints[r];
            if (cp < 0 || cp > 0x10FFFF) {
                throw new IllegalArgumentException();
            }
            while (true) {
                try {
                    if (cp < 65536) {
                        chars[w] = (char)cp;
                        ++w;
                        continue block2;
                    }
                    chars[w] = (char)(55232 + (cp >> 10));
                    chars[w + 1] = (char)(56320 + (cp & 0x3FF));
                    w += 2;
                    continue block2;
                }
                catch (IndexOutOfBoundsException ex) {
                    int newlen = (int)Math.ceil((double)codePoints.length * (double)(w + 2) / (double)(r - offset + 1));
                    char[] temp = new char[newlen];
                    System.arraycopy(chars, 0, temp, 0, w);
                    chars = temp;
                    continue;
                }
                break;
            }
        }
        return new String(chars, 0, w);
    }

    private static String toString(int ch) {
        if (ch < 65536) {
            return String.valueOf((char)ch);
        }
        StringBuilder result = new StringBuilder();
        result.append(UTF16.getLeadSurrogate(ch));
        result.append(UTF16.getTrailSurrogate(ch));
        return result.toString();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static final class StringComparator
    implements Comparator<String> {
        public static final int FOLD_CASE_DEFAULT = 0;
        public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
        private int m_codePointCompare_;
        private int m_foldCase_;
        private boolean m_ignoreCase_;
        private static final int CODE_POINT_COMPARE_SURROGATE_OFFSET_ = 10240;

        public StringComparator() {
            this(false, false, 0);
        }

        public StringComparator(boolean codepointcompare, boolean ignorecase, int foldcaseoption) {
            this.setCodePointCompare(codepointcompare);
            this.m_ignoreCase_ = ignorecase;
            if (foldcaseoption < 0 || foldcaseoption > 1) {
                throw new IllegalArgumentException("Invalid fold case option");
            }
            this.m_foldCase_ = foldcaseoption;
        }

        public void setCodePointCompare(boolean flag) {
            this.m_codePointCompare_ = flag ? 32768 : 0;
        }

        public void setIgnoreCase(boolean ignorecase, int foldcaseoption) {
            this.m_ignoreCase_ = ignorecase;
            if (foldcaseoption < 0 || foldcaseoption > 1) {
                throw new IllegalArgumentException("Invalid fold case option");
            }
            this.m_foldCase_ = foldcaseoption;
        }

        public boolean getCodePointCompare() {
            return this.m_codePointCompare_ == 32768;
        }

        public boolean getIgnoreCase() {
            return this.m_ignoreCase_;
        }

        public int getIgnoreCaseOption() {
            return this.m_foldCase_;
        }

        @Override
        public int compare(String a, String b) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }
            if (this.m_ignoreCase_) {
                return this.compareCaseInsensitive(a, b);
            }
            return this.compareCaseSensitive(a, b);
        }

        private int compareCaseInsensitive(String s1, String s2) {
            return Normalizer.cmpEquivFold(s1, s2, this.m_foldCase_ | this.m_codePointCompare_ | 0x10000);
        }

        private int compareCaseSensitive(String s1, String s2) {
            boolean codepointcompare;
            int index;
            int length1 = s1.length();
            int length2 = s2.length();
            int minlength = length1;
            int result = 0;
            if (length1 < length2) {
                result = -1;
            } else if (length1 > length2) {
                result = 1;
                minlength = length2;
            }
            char c1 = '\u0000';
            char c2 = '\u0000';
            for (index = 0; index < minlength && (c1 = s1.charAt(index)) == (c2 = s2.charAt(index)); ++index) {
            }
            if (index == minlength) {
                return result;
            }
            boolean bl = codepointcompare = this.m_codePointCompare_ == 32768;
            if (c1 >= '\ud800' && c2 >= '\ud800' && codepointcompare) {
                if (!(c1 <= '\udbff' && index + 1 != length1 && UTF16.isTrailSurrogate(s1.charAt(index + 1)) || UTF16.isTrailSurrogate(c1) && index != 0 && UTF16.isLeadSurrogate(s1.charAt(index - 1)))) {
                    c1 = (char)(c1 - 10240);
                }
                if (!(c2 <= '\udbff' && index + 1 != length2 && UTF16.isTrailSurrogate(s2.charAt(index + 1)) || UTF16.isTrailSurrogate(c2) && index != 0 && UTF16.isLeadSurrogate(s2.charAt(index - 1)))) {
                    c2 = (char)(c2 - 10240);
                }
            }
            return c1 - c2;
        }
    }
}

