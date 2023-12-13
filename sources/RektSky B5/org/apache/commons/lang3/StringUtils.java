/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.RegExUtils;

public class StringUtils {
    private static final int STRING_BUILDER_SIZE = 256;
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    public static boolean isAnyEmpty(CharSequence ... css) {
        if (ArrayUtils.isEmpty(css)) {
            return false;
        }
        for (CharSequence cs : css) {
            if (!StringUtils.isEmpty(cs)) continue;
            return true;
        }
        return false;
    }

    public static boolean isNoneEmpty(CharSequence ... css) {
        return !StringUtils.isAnyEmpty(css);
    }

    public static boolean isAllEmpty(CharSequence ... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        }
        for (CharSequence cs : css) {
            if (!StringUtils.isNotEmpty(cs)) continue;
            return false;
        }
        return true;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i2 = 0; i2 < strLen; ++i2) {
            if (Character.isWhitespace(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    public static boolean isAnyBlank(CharSequence ... css) {
        if (ArrayUtils.isEmpty(css)) {
            return false;
        }
        for (CharSequence cs : css) {
            if (!StringUtils.isBlank(cs)) continue;
            return true;
        }
        return false;
    }

    public static boolean isNoneBlank(CharSequence ... css) {
        return !StringUtils.isAnyBlank(css);
    }

    public static boolean isAllBlank(CharSequence ... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        }
        for (CharSequence cs : css) {
            if (!StringUtils.isNotBlank(cs)) continue;
            return false;
        }
        return true;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimToNull(String str) {
        String ts = StringUtils.trim(str);
        return StringUtils.isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String truncate(String str, int maxWidth) {
        return StringUtils.truncate(str, 0, maxWidth);
    }

    public static String truncate(String str, int offset, int maxWidth) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (maxWidth < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        }
        if (str == null) {
            return null;
        }
        if (offset > str.length()) {
            return EMPTY;
        }
        if (str.length() > maxWidth) {
            int ix = offset + maxWidth > str.length() ? str.length() : offset + maxWidth;
            return str.substring(offset, ix);
        }
        return str.substring(offset);
    }

    public static String strip(String str) {
        return StringUtils.strip(str, null);
    }

    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        return (str = StringUtils.strip(str, null)).isEmpty() ? null : str;
    }

    public static String stripToEmpty(String str) {
        return str == null ? EMPTY : StringUtils.strip(str, null);
    }

    public static String strip(String str, String stripChars) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = StringUtils.stripStart(str, stripChars);
        return StringUtils.stripEnd(str, stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        int start;
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        if (stripChars == null) {
            for (start = 0; start != strLen && Character.isWhitespace(str.charAt(start)); ++start) {
            }
        } else {
            if (stripChars.isEmpty()) {
                return str;
            }
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                ++start;
            }
        }
        return str.substring(start);
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }
        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                --end;
            }
        } else {
            if (stripChars.isEmpty()) {
                return str;
            }
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                --end;
            }
        }
        return str.substring(0, end);
    }

    public static String[] stripAll(String ... strs) {
        return StringUtils.stripAll(strs, null);
    }

    public static String[] stripAll(String[] strs, String stripChars) {
        int strsLen;
        if (strs == null || (strsLen = strs.length) == 0) {
            return strs;
        }
        String[] newArr = new String[strsLen];
        for (int i2 = 0; i2 < strsLen; ++i2) {
            newArr[i2] = StringUtils.strip(strs[i2], stripChars);
        }
        return newArr;
    }

    public static String stripAccents(String input) {
        if (input == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
        StringUtils.convertRemainingAccentCharacters(decomposed);
        return pattern.matcher(decomposed).replaceAll(EMPTY);
    }

    private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
        for (int i2 = 0; i2 < decomposed.length(); ++i2) {
            if (decomposed.charAt(i2) == '\u0141') {
                decomposed.deleteCharAt(i2);
                decomposed.insert(i2, 'L');
                continue;
            }
            if (decomposed.charAt(i2) != '\u0142') continue;
            decomposed.deleteCharAt(i2);
            decomposed.insert(i2, 'l');
        }
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, cs1.length());
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        if (str1 == str2) {
            return true;
        }
        if (str1.length() != str2.length()) {
            return false;
        }
        return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
    }

    public static int compare(String str1, String str2) {
        return StringUtils.compare(str1, str2, true);
    }

    public static int compare(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareTo(str2);
    }

    public static int compareIgnoreCase(String str1, String str2) {
        return StringUtils.compareIgnoreCase(str1, str2, true);
    }

    public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return nullIsLess ? -1 : 1;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareToIgnoreCase(str2);
    }

    public static boolean equalsAny(CharSequence string, CharSequence ... searchStrings) {
        if (ArrayUtils.isNotEmpty(searchStrings)) {
            for (CharSequence next : searchStrings) {
                if (!StringUtils.equals(string, next)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence ... searchStrings) {
        if (ArrayUtils.isNotEmpty(searchStrings)) {
            for (CharSequence next : searchStrings) {
                if (!StringUtils.equalsIgnoreCase(string, next)) continue;
                return true;
            }
        }
        return false;
    }

    public static int indexOf(CharSequence seq, int searchChar) {
        if (StringUtils.isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, 0);
    }

    public static int indexOf(CharSequence seq, int searchChar, int startPos) {
        if (StringUtils.isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, startPos);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtils.ordinalIndexOf(str, searchStr, ordinal, false);
    }

    private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if ((index = lastIndex ? CharSequenceUtils.lastIndexOf(str, searchStr, index - 1) : CharSequenceUtils.indexOf(str, searchStr, index + 1)) >= 0) continue;
            return index;
        } while (++found < ordinal);
        return index;
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtils.indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        int endLimit;
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        if (startPos > (endLimit = str.length() - searchStr.length() + 1)) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i2 = startPos; i2 < endLimit; ++i2) {
            if (!CharSequenceUtils.regionMatches(str, true, i2, searchStr, 0, searchStr.length())) continue;
            return i2;
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        if (StringUtils.isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
    }

    public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        if (StringUtils.isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
    }

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtils.ordinalIndexOf(str, searchStr, ordinal, true);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return StringUtils.lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
        }
        if (startPos < 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i2 = startPos; i2 >= 0; --i2) {
            if (!CharSequenceUtils.regionMatches(str, true, i2, searchStr, 0, searchStr.length())) continue;
            return i2;
        }
        return -1;
    }

    public static boolean contains(CharSequence seq, int searchChar) {
        if (StringUtils.isEmpty(seq)) {
            return false;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return false;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i2 = 0; i2 <= max; ++i2) {
            if (!CharSequenceUtils.regionMatches(str, true, i2, searchStr, 0, len)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsWhitespace(CharSequence seq) {
        if (StringUtils.isEmpty(seq)) {
            return false;
        }
        int strLen = seq.length();
        for (int i2 = 0; i2 < strLen; ++i2) {
            if (!Character.isWhitespace(seq.charAt(i2))) continue;
            return true;
        }
        return false;
    }

    public static int indexOfAny(CharSequence cs, char ... searchChars) {
        if (StringUtils.isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        for (int i2 = 0; i2 < csLen; ++i2) {
            char ch = cs.charAt(i2);
            for (int j2 = 0; j2 < searchLen; ++j2) {
                if (searchChars[j2] != ch) continue;
                if (i2 < csLast && j2 < searchLast && Character.isHighSurrogate(ch)) {
                    if (searchChars[j2 + 1] != cs.charAt(i2 + 1)) continue;
                    return i2;
                }
                return i2;
            }
        }
        return -1;
    }

    public static int indexOfAny(CharSequence cs, String searchChars) {
        if (StringUtils.isEmpty(cs) || StringUtils.isEmpty(searchChars)) {
            return -1;
        }
        return StringUtils.indexOfAny(cs, searchChars.toCharArray());
    }

    public static boolean containsAny(CharSequence cs, char ... searchChars) {
        if (StringUtils.isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return false;
        }
        int csLength = cs.length();
        int searchLength = searchChars.length;
        int csLast = csLength - 1;
        int searchLast = searchLength - 1;
        for (int i2 = 0; i2 < csLength; ++i2) {
            char ch = cs.charAt(i2);
            for (int j2 = 0; j2 < searchLength; ++j2) {
                if (searchChars[j2] != ch) continue;
                if (Character.isHighSurrogate(ch)) {
                    if (j2 == searchLast) {
                        return true;
                    }
                    if (i2 >= csLast || searchChars[j2 + 1] != cs.charAt(i2 + 1)) continue;
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        if (searchChars == null) {
            return false;
        }
        return StringUtils.containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
    }

    public static boolean containsAny(CharSequence cs, CharSequence ... searchCharSequences) {
        if (StringUtils.isEmpty(cs) || ArrayUtils.isEmpty(searchCharSequences)) {
            return false;
        }
        for (CharSequence searchCharSequence : searchCharSequences) {
            if (!StringUtils.contains(cs, searchCharSequence)) continue;
            return true;
        }
        return false;
    }

    public static int indexOfAnyBut(CharSequence cs, char ... searchChars) {
        if (StringUtils.isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        block0: for (int i2 = 0; i2 < csLen; ++i2) {
            char ch = cs.charAt(i2);
            for (int j2 = 0; j2 < searchLen; ++j2) {
                if (searchChars[j2] == ch && (i2 >= csLast || j2 >= searchLast || !Character.isHighSurrogate(ch) || searchChars[j2 + 1] == cs.charAt(i2 + 1))) continue block0;
            }
            return i2;
        }
        return -1;
    }

    public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        if (StringUtils.isEmpty(seq) || StringUtils.isEmpty(searchChars)) {
            return -1;
        }
        int strLen = seq.length();
        for (int i2 = 0; i2 < strLen; ++i2) {
            boolean chFound;
            char ch = seq.charAt(i2);
            boolean bl = chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
            if (i2 + 1 < strLen && Character.isHighSurrogate(ch)) {
                char ch2 = seq.charAt(i2 + 1);
                if (!chFound || CharSequenceUtils.indexOf(searchChars, ch2, 0) >= 0) continue;
                return i2;
            }
            if (chFound) continue;
            return i2;
        }
        return -1;
    }

    public static boolean containsOnly(CharSequence cs, char ... valid) {
        if (valid == null || cs == null) {
            return false;
        }
        if (cs.length() == 0) {
            return true;
        }
        if (valid.length == 0) {
            return false;
        }
        return StringUtils.indexOfAnyBut(cs, valid) == -1;
    }

    public static boolean containsOnly(CharSequence cs, String validChars) {
        if (cs == null || validChars == null) {
            return false;
        }
        return StringUtils.containsOnly(cs, validChars.toCharArray());
    }

    public static boolean containsNone(CharSequence cs, char ... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        int csLen = cs.length();
        int csLast = csLen - 1;
        int searchLen = searchChars.length;
        int searchLast = searchLen - 1;
        for (int i2 = 0; i2 < csLen; ++i2) {
            char ch = cs.charAt(i2);
            for (int j2 = 0; j2 < searchLen; ++j2) {
                if (searchChars[j2] != ch) continue;
                if (Character.isHighSurrogate(ch)) {
                    if (j2 == searchLast) {
                        return false;
                    }
                    if (i2 >= csLast || searchChars[j2 + 1] != cs.charAt(i2 + 1)) continue;
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    public static boolean containsNone(CharSequence cs, String invalidChars) {
        if (cs == null || invalidChars == null) {
            return true;
        }
        return StringUtils.containsNone(cs, invalidChars.toCharArray());
    }

    public static int indexOfAny(CharSequence str, CharSequence ... searchStrs) {
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = Integer.MAX_VALUE;
        int tmp = 0;
        for (CharSequence search : searchStrs) {
            if (search == null || (tmp = CharSequenceUtils.indexOf(str, search, 0)) == -1 || tmp >= ret) continue;
            ret = tmp;
        }
        return ret == Integer.MAX_VALUE ? -1 : ret;
    }

    public static int lastIndexOfAny(CharSequence str, CharSequence ... searchStrs) {
        if (str == null || searchStrs == null) {
            return -1;
        }
        int ret = -1;
        int tmp = 0;
        for (CharSequence search : searchStrs) {
            if (search == null || (tmp = CharSequenceUtils.lastIndexOf(str, search, str.length())) <= ret) continue;
            ret = tmp;
        }
        return ret;
    }

    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }
        return str.substring(start);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return EMPTY;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }

    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return EMPTY;
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    private static StringBuilder newStringBuilder(int noOfItems) {
        return new StringBuilder(noOfItems * 16);
    }

    public static String substringBefore(String str, String separator) {
        if (StringUtils.isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfter(String str, String separator) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBeforeLast(String str, String separator) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringAfterLast(String str, String separator) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (StringUtils.isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == str.length() - separator.length()) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBetween(String str, String tag) {
        return StringUtils.substringBetween(str, tag, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        int end;
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1 && (end = str.indexOf(close, start + open.length())) != -1) {
            return str.substring(start + open.length(), end);
        }
        return null;
    }

    public static String[] substringsBetween(String str, String open, String close) {
        int end;
        int start;
        if (str == null || StringUtils.isEmpty(open) || StringUtils.isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int closeLen = close.length();
        int openLen = open.length();
        ArrayList<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen && (start = str.indexOf(open, pos)) >= 0 && (end = str.indexOf(close, start += openLen)) >= 0) {
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] split(String str) {
        return StringUtils.split(str, null, -1);
    }

    public static String[] split(String str, char separatorChar) {
        return StringUtils.splitWorker(str, separatorChar, false);
    }

    public static String[] split(String str, String separatorChars) {
        return StringUtils.splitWorker(str, separatorChars, -1, false);
    }

    public static String[] split(String str, String separatorChars, int max) {
        return StringUtils.splitWorker(str, separatorChars, max, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return StringUtils.splitByWholeSeparatorWorker(str, separator, -1, false);
    }

    public static String[] splitByWholeSeparator(String str, String separator, int max) {
        return StringUtils.splitByWholeSeparatorWorker(str, separator, max, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return StringUtils.splitByWholeSeparatorWorker(str, separator, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return StringUtils.splitByWholeSeparatorWorker(str, separator, max, true);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (separator == null || EMPTY.equals(separator)) {
            return StringUtils.splitWorker(str, null, max, preserveAllTokens);
        }
        int separatorLength = separator.length();
        ArrayList<String> substrings = new ArrayList<String>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);
            if (end > -1) {
                if (end > beg) {
                    if (++numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                        continue;
                    }
                    substrings.add(str.substring(beg, end));
                    beg = end + separatorLength;
                    continue;
                }
                if (preserveAllTokens) {
                    if (++numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                    } else {
                        substrings.add(EMPTY);
                    }
                }
                beg = end + separatorLength;
                continue;
            }
            substrings.add(str.substring(beg));
            end = len;
        }
        return substrings.toArray(new String[substrings.size()]);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return StringUtils.splitWorker(str, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, char separatorChar) {
        return StringUtils.splitWorker(str, separatorChar, true);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> list = new ArrayList<String>();
        int i2 = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i2 < len) {
            if (str.charAt(i2) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i2));
                    match = false;
                    lastMatch = true;
                }
                start = ++i2;
                continue;
            }
            lastMatch = false;
            match = true;
            ++i2;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i2));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars) {
        return StringUtils.splitWorker(str, separatorChars, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return StringUtils.splitWorker(str, separatorChars, max, true);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i2 = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            while (i2 < len) {
                if (Character.isWhitespace(str.charAt(i2))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i2 = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i2));
                        match = false;
                    }
                    start = ++i2;
                    continue;
                }
                lastMatch = false;
                match = true;
                ++i2;
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            while (i2 < len) {
                if (str.charAt(i2) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i2 = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i2));
                        match = false;
                    }
                    start = ++i2;
                    continue;
                }
                lastMatch = false;
                match = true;
                ++i2;
            }
        } else {
            while (i2 < len) {
                if (separatorChars.indexOf(str.charAt(i2)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i2 = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i2));
                        match = false;
                    }
                    start = ++i2;
                    continue;
                }
                lastMatch = false;
                match = true;
                ++i2;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i2));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] splitByCharacterType(String str) {
        return StringUtils.splitByCharacterType(str, false);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return StringUtils.splitByCharacterType(str, true);
    }

    private static String[] splitByCharacterType(String str, boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        char[] c2 = str.toCharArray();
        ArrayList<String> list = new ArrayList<String>();
        int tokenStart = 0;
        int currentType = Character.getType(c2[tokenStart]);
        for (int pos = tokenStart + 1; pos < c2.length; ++pos) {
            int type = Character.getType(c2[pos]);
            if (type == currentType) continue;
            if (camelCase && type == 2 && currentType == 1) {
                int newTokenStart = pos - 1;
                if (newTokenStart != tokenStart) {
                    list.add(new String(c2, tokenStart, newTokenStart - tokenStart));
                    tokenStart = newTokenStart;
                }
            } else {
                list.add(new String(c2, tokenStart, pos - tokenStart));
                tokenStart = pos;
            }
            currentType = type;
        }
        list.add(new String(c2, tokenStart, c2.length - tokenStart));
        return list.toArray(new String[list.size()]);
    }

    @SafeVarargs
    public static <T> String join(T ... elements) {
        return StringUtils.join((Object[])elements, null);
    }

    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(long[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(int[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(short[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(byte[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(char[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(float[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(double[] array, char separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            if (array[i2] == null) continue;
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(long[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(int[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(byte[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(short[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(char[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(double[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(float[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return StringUtils.join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        int noOfItems;
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        if ((noOfItems = endIndex - startIndex) <= 0) {
            return EMPTY;
        }
        StringBuilder buf = StringUtils.newStringBuilder(noOfItems);
        for (int i2 = startIndex; i2 < endIndex; ++i2) {
            if (i2 > startIndex) {
                buf.append(separator);
            }
            if (array[i2] == null) continue;
            buf.append(array[i2]);
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, EMPTY);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj == null) continue;
            buf.append(obj);
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, EMPTY);
        }
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            Object obj;
            if (separator != null) {
                buf.append(separator);
            }
            if ((obj = iterator.next()) == null) continue;
            buf.append(obj);
        }
        return buf.toString();
    }

    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return StringUtils.join(iterable.iterator(), separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return StringUtils.join(iterable.iterator(), separator);
    }

    public static String join(List<?> list, char separator, int startIndex, int endIndex) {
        if (list == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        List<?> subList = list.subList(startIndex, endIndex);
        return StringUtils.join(subList.iterator(), separator);
    }

    public static String join(List<?> list, String separator, int startIndex, int endIndex) {
        if (list == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }
        List<?> subList = list.subList(startIndex, endIndex);
        return StringUtils.join(subList.iterator(), separator);
    }

    public static String joinWith(String separator, Object ... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        }
        String sanitizedSeparator = StringUtils.defaultString(separator);
        StringBuilder result = new StringBuilder();
        Iterator<Object> iterator = Arrays.asList(objects).iterator();
        while (iterator.hasNext()) {
            String value = Objects.toString(iterator.next(), EMPTY);
            result.append(value);
            if (!iterator.hasNext()) continue;
            result.append(sanitizedSeparator);
        }
        return result.toString();
    }

    public static String deleteWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isWhitespace(str.charAt(i2))) continue;
            chs[count++] = str.charAt(i2);
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    public static String removeStart(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeStartIgnoreCase(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        if (StringUtils.startsWithIgnoreCase(str, remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String removeEnd(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String removeEndIgnoreCase(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        if (StringUtils.endsWithIgnoreCase(str, remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String remove(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        return StringUtils.replace(str, remove, EMPTY, -1);
    }

    public static String removeIgnoreCase(String str, String remove) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
            return str;
        }
        return StringUtils.replaceIgnoreCase(str, remove, EMPTY, -1);
    }

    public static String remove(String str, char remove) {
        if (StringUtils.isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i2 = 0; i2 < chars.length; ++i2) {
            if (chars[i2] == remove) continue;
            chars[pos++] = chars[i2];
        }
        return new String(chars, 0, pos);
    }

    @Deprecated
    public static String removeAll(String text, String regex) {
        return RegExUtils.removeAll(text, regex);
    }

    @Deprecated
    public static String removeFirst(String text, String regex) {
        return StringUtils.replaceFirst(text, regex, EMPTY);
    }

    public static String replaceOnce(String text, String searchString, String replacement) {
        return StringUtils.replace(text, searchString, replacement, 1);
    }

    public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtils.replaceIgnoreCase(text, searchString, replacement, 1);
    }

    @Deprecated
    public static String replacePattern(String source, String regex, String replacement) {
        return RegExUtils.replacePattern(source, regex, replacement);
    }

    @Deprecated
    public static String removePattern(String source, String regex) {
        return RegExUtils.removePattern(source, regex);
    }

    @Deprecated
    public static String replaceAll(String text, String regex, String replacement) {
        return RegExUtils.replaceAll(text, regex, replacement);
    }

    @Deprecated
    public static String replaceFirst(String text, String regex, String replacement) {
        return RegExUtils.replaceFirst(text, regex, replacement);
    }

    public static String replace(String text, String searchString, String replacement) {
        return StringUtils.replace(text, searchString, replacement, -1);
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtils.replaceIgnoreCase(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        return StringUtils.replace(text, searchString, replacement, max, false);
    }

    private static String replace(String text, String searchString, String replacement, int max, boolean ignoreCase) {
        int start;
        int end;
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        String searchText = text;
        if (ignoreCase) {
            searchText = text.toLowerCase();
            searchString = searchString.toLowerCase();
        }
        if ((end = searchText.indexOf(searchString, start = 0)) == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        int n2 = increase = increase < 0 ? 0 : increase;
        StringBuilder buf = new StringBuilder(text.length() + (increase *= max < 0 ? 16 : (max > 64 ? 64 : max)));
        while (end != -1) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) break;
            end = searchText.indexOf(searchString, start);
        }
        buf.append(text, start, text.length());
        return buf.toString();
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
        return StringUtils.replace(text, searchString, replacement, max, true);
    }

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return StringUtils.replaceEach(text, searchList, replacementList, false, 0);
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        int timeToLive = searchList == null ? 0 : searchList.length;
        return StringUtils.replaceEach(text, searchList, replacementList, true, timeToLive);
    }

    private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
        if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
        }
        int searchLength = searchList.length;
        int replacementLength = replacementList.length;
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
        }
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;
        for (int i2 = 0; i2 < searchLength; ++i2) {
            if (noMoreMatchesForReplIndex[i2] || searchList[i2] == null || searchList[i2].isEmpty() || replacementList[i2] == null) continue;
            tempIndex = text.indexOf(searchList[i2]);
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i2] = true;
                continue;
            }
            if (textIndex != -1 && tempIndex >= textIndex) continue;
            textIndex = tempIndex;
            replaceIndex = i2;
        }
        if (textIndex == -1) {
            return text;
        }
        int start = 0;
        int increase = 0;
        for (int i3 = 0; i3 < searchList.length; ++i3) {
            int greater;
            if (searchList[i3] == null || replacementList[i3] == null || (greater = replacementList[i3].length() - searchList[i3].length()) <= 0) continue;
            increase += 3 * greater;
        }
        increase = Math.min(increase, text.length() / 5);
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (textIndex != -1) {
            int i4;
            for (i4 = start; i4 < textIndex; ++i4) {
                buf.append(text.charAt(i4));
            }
            buf.append(replacementList[replaceIndex]);
            start = textIndex + searchList[replaceIndex].length();
            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            for (i4 = 0; i4 < searchLength; ++i4) {
                if (noMoreMatchesForReplIndex[i4] || searchList[i4] == null || searchList[i4].isEmpty() || replacementList[i4] == null) continue;
                tempIndex = text.indexOf(searchList[i4], start);
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i4] = true;
                    continue;
                }
                if (textIndex != -1 && tempIndex >= textIndex) continue;
                textIndex = tempIndex;
                replaceIndex = i4;
            }
        }
        int textLength = text.length();
        for (int i5 = start; i5 < textLength; ++i5) {
            buf.append(text.charAt(i5));
        }
        String result = buf.toString();
        if (!repeat) {
            return result;
        }
        return StringUtils.replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = EMPTY;
        }
        boolean modified = false;
        int replaceCharsLength = replaceChars.length();
        int strLength = str.length();
        StringBuilder buf = new StringBuilder(strLength);
        for (int i2 = 0; i2 < strLength; ++i2) {
            char ch = str.charAt(i2);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index >= replaceCharsLength) continue;
                buf.append(replaceChars.charAt(index));
                continue;
            }
            buf.append(ch);
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }

    public static String overlay(String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = EMPTY;
        }
        int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        return str.substring(0, start) + overlay + str.substring(end);
    }

    public static String chomp(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == '\r' || ch == '\n') {
                return EMPTY;
            }
            return str;
        }
        int lastIdx = str.length() - 1;
        char last = str.charAt(lastIdx);
        if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
                --lastIdx;
            }
        } else if (last != '\r') {
            ++lastIdx;
        }
        return str.substring(0, lastIdx);
    }

    @Deprecated
    public static String chomp(String str, String separator) {
        return StringUtils.removeEnd(str, separator);
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen < 2) {
            return EMPTY;
        }
        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);
        if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
            return ret.substring(0, lastIdx - 1);
        }
        return ret;
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= 8192) {
            return StringUtils.repeat(str.charAt(0), repeat);
        }
        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1: {
                return StringUtils.repeat(str.charAt(0), repeat);
            }
            case 2: {
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i2 = repeat * 2 - 2; i2 >= 0; --i2) {
                    output2[i2] = ch0;
                    output2[i2 + 1] = ch1;
                    --i2;
                }
                return new String(output2);
            }
        }
        StringBuilder buf = new StringBuilder(outputLength);
        for (int i3 = 0; i3 < repeat; ++i3) {
            buf.append(str);
        }
        return buf.toString();
    }

    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return StringUtils.repeat(str, repeat);
        }
        String result = StringUtils.repeat(str + separator, repeat);
        return StringUtils.removeEnd(result, separator);
    }

    public static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return EMPTY;
        }
        char[] buf = new char[repeat];
        for (int i2 = repeat - 1; i2 >= 0; --i2) {
            buf[i2] = ch;
        }
        return new String(buf);
    }

    public static String rightPad(String str, int size) {
        return StringUtils.rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return StringUtils.rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(StringUtils.repeat(padChar, pads));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isEmpty(padStr)) {
            padStr = SPACE;
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return StringUtils.rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        }
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i2 = 0; i2 < pads; ++i2) {
            padding[i2] = padChars[i2 % padLen];
        }
        return str.concat(new String(padding));
    }

    public static String leftPad(String str, int size) {
        return StringUtils.leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return StringUtils.leftPad(str, size, String.valueOf(padChar));
        }
        return StringUtils.repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isEmpty(padStr)) {
            padStr = SPACE;
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return StringUtils.leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        }
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i2 = 0; i2 < pads; ++i2) {
            padding[i2] = padChars[i2 % padLen];
        }
        return new String(padding).concat(str);
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static String center(String str, int size) {
        return StringUtils.center(str, size, ' ');
    }

    public static String center(String str, int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = StringUtils.leftPad(str, strLen + pads / 2, padChar);
        str = StringUtils.rightPad(str, size, padChar);
        return str;
    }

    public static String center(String str, int size, String padStr) {
        int strLen;
        int pads;
        if (str == null || size <= 0) {
            return str;
        }
        if (StringUtils.isEmpty(padStr)) {
            padStr = SPACE;
        }
        if ((pads = size - (strLen = str.length())) <= 0) {
            return str;
        }
        str = StringUtils.leftPad(str, strLen + pads / 2, padStr);
        str = StringUtils.rightPad(str, size, padStr);
        return str;
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(locale);
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }

    public static String capitalize(String str) {
        int codepoint;
        int newCodePoint;
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int firstCodepoint = str.codePointAt(0);
        if (firstCodepoint == (newCodePoint = Character.toTitleCase(firstCodepoint))) {
            return str;
        }
        int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; inOffset += Character.charCount(codepoint)) {
            codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String uncapitalize(String str) {
        int codepoint;
        int newCodePoint;
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int firstCodepoint = str.codePointAt(0);
        if (firstCodepoint == (newCodePoint = Character.toLowerCase(firstCodepoint))) {
            return str;
        }
        int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; inOffset += Character.charCount(codepoint)) {
            codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String swapCase(String str) {
        int newCodePoint;
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        int strLen = str.length();
        int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        for (int i2 = 0; i2 < strLen; i2 += Character.charCount(newCodePoint)) {
            int oldCodepoint = str.codePointAt(i2);
            newCodePoint = Character.isUpperCase(oldCodepoint) ? Character.toLowerCase(oldCodepoint) : (Character.isTitleCase(oldCodepoint) ? Character.toLowerCase(oldCodepoint) : (Character.isLowerCase(oldCodepoint) ? Character.toUpperCase(oldCodepoint) : oldCodepoint));
            newCodePoints[outOffset++] = newCodePoint;
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1) {
            ++count;
            idx += sub.length();
        }
        return count;
    }

    public static int countMatches(CharSequence str, char ch) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i2 = 0; i2 < str.length(); ++i2) {
            if (ch != str.charAt(i2)) continue;
            ++count;
        }
        return count;
    }

    public static boolean isAlpha(CharSequence cs) {
        if (StringUtils.isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isLetter(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphaSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isLetter(cs.charAt(i2)) || cs.charAt(i2) == ' ') continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        if (StringUtils.isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isLetterOrDigit(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAlphanumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isLetterOrDigit(cs.charAt(i2)) || cs.charAt(i2) == ' ') continue;
            return false;
        }
        return true;
    }

    public static boolean isAsciiPrintable(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (CharUtils.isAsciiPrintable(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNumeric(CharSequence cs) {
        if (StringUtils.isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isDigit(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isNumericSpace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isDigit(cs.charAt(i2)) || cs.charAt(i2) == ' ') continue;
            return false;
        }
        return true;
    }

    public static String getDigits(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        StringBuilder strDigits = new StringBuilder(sz);
        for (int i2 = 0; i2 < sz; ++i2) {
            char tempChar = str.charAt(i2);
            if (!Character.isDigit(tempChar)) continue;
            strDigits.append(tempChar);
        }
        return strDigits.toString();
    }

    public static boolean isWhitespace(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isWhitespace(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAllLowerCase(CharSequence cs) {
        if (cs == null || StringUtils.isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isLowerCase(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isAllUpperCase(CharSequence cs) {
        if (cs == null || StringUtils.isEmpty(cs)) {
            return false;
        }
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (Character.isUpperCase(cs.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean isMixedCase(CharSequence cs) {
        if (StringUtils.isEmpty(cs) || cs.length() == 1) {
            return false;
        }
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        int sz = cs.length();
        for (int i2 = 0; i2 < sz; ++i2) {
            if (containsUppercase && containsLowercase) {
                return true;
            }
            if (Character.isUpperCase(cs.charAt(i2))) {
                containsUppercase = true;
                continue;
            }
            if (!Character.isLowerCase(cs.charAt(i2))) continue;
            containsLowercase = true;
        }
        return containsUppercase && containsLowercase;
    }

    public static String defaultString(String str) {
        return StringUtils.defaultString(str, EMPTY);
    }

    public static String defaultString(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonBlank(T ... values) {
        if (values != null) {
            for (T val2 : values) {
                if (!StringUtils.isNotBlank(val2)) continue;
                return val2;
            }
        }
        return null;
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(T ... values) {
        if (values != null) {
            for (T val2 : values) {
                if (!StringUtils.isNotEmpty(val2)) continue;
                return val2;
            }
        }
        return null;
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return StringUtils.isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return StringUtils.isEmpty(str) ? defaultStr : str;
    }

    public static String rotate(String str, int shift) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (shift == 0 || strLen == 0 || shift % strLen == 0) {
            return str;
        }
        StringBuilder builder = new StringBuilder(strLen);
        int offset = -(shift % strLen);
        builder.append(StringUtils.substring(str, offset));
        builder.append(StringUtils.substring(str, 0, offset));
        return builder.toString();
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    public static String reverseDelimited(String str, char separatorChar) {
        if (str == null) {
            return null;
        }
        Object[] strs = StringUtils.split(str, separatorChar);
        ArrayUtils.reverse(strs);
        return StringUtils.join(strs, separatorChar);
    }

    public static String abbreviate(String str, int maxWidth) {
        String defaultAbbrevMarker = "...";
        return StringUtils.abbreviate(str, "...", 0, maxWidth);
    }

    public static String abbreviate(String str, int offset, int maxWidth) {
        String defaultAbbrevMarker = "...";
        return StringUtils.abbreviate(str, "...", offset, maxWidth);
    }

    public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
        return StringUtils.abbreviate(str, abbrevMarker, 0, maxWidth);
    }

    public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(abbrevMarker)) {
            return str;
        }
        int abbrevMarkerLength = abbrevMarker.length();
        int minAbbrevWidth = abbrevMarkerLength + 1;
        int minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1;
        if (maxWidth < minAbbrevWidth) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - abbrevMarkerLength) {
            offset = str.length() - (maxWidth - abbrevMarkerLength);
        }
        if (offset <= abbrevMarkerLength + 1) {
            return str.substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker;
        }
        if (maxWidth < minAbbrevWidthOffset) {
            throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", minAbbrevWidthOffset));
        }
        if (offset + maxWidth - abbrevMarkerLength < str.length()) {
            return abbrevMarker + StringUtils.abbreviate(str.substring(offset), abbrevMarker, maxWidth - abbrevMarkerLength);
        }
        return abbrevMarker + str.substring(str.length() - (maxWidth - abbrevMarkerLength));
    }

    public static String abbreviateMiddle(String str, String middle, int length) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(middle)) {
            return str;
        }
        if (length >= str.length() || length < middle.length() + 2) {
            return str;
        }
        int targetSting = length - middle.length();
        int startOffset = targetSting / 2 + targetSting % 2;
        int endOffset = str.length() - targetSting / 2;
        return str.substring(0, startOffset) + middle + str.substring(endOffset);
    }

    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = StringUtils.indexOfDifference((CharSequence)str1, (CharSequence)str2);
        if (at == -1) {
            return EMPTY;
        }
        return str2.substring(at);
    }

    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        int i2;
        if (cs1 == cs2) {
            return -1;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        for (i2 = 0; i2 < cs1.length() && i2 < cs2.length() && cs1.charAt(i2) == cs2.charAt(i2); ++i2) {
        }
        if (i2 < cs2.length() || i2 < cs1.length()) {
            return i2;
        }
        return -1;
    }

    public static int indexOfDifference(CharSequence ... css) {
        if (css == null || css.length <= 1) {
            return -1;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        int arrayLen = css.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;
        for (CharSequence cs : css) {
            if (cs == null) {
                anyStringNull = true;
                shortestStrLen = 0;
                continue;
            }
            allStringsNull = false;
            shortestStrLen = Math.min(cs.length(), shortestStrLen);
            longestStrLen = Math.max(cs.length(), longestStrLen);
        }
        if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
            return -1;
        }
        if (shortestStrLen == 0) {
            return 0;
        }
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; ++stringPos) {
            char comparisonChar = css[0].charAt(stringPos);
            for (int arrayPos = 1; arrayPos < arrayLen; ++arrayPos) {
                if (css[arrayPos].charAt(stringPos) == comparisonChar) continue;
                firstDiff = stringPos;
                break;
            }
            if (firstDiff != -1) break;
        }
        if (firstDiff == -1 && shortestStrLen != longestStrLen) {
            return shortestStrLen;
        }
        return firstDiff;
    }

    public static String getCommonPrefix(String ... strs) {
        if (strs == null || strs.length == 0) {
            return EMPTY;
        }
        int smallestIndexOfDiff = StringUtils.indexOfDifference(strs);
        if (smallestIndexOfDiff == -1) {
            if (strs[0] == null) {
                return EMPTY;
            }
            return strs[0];
        }
        if (smallestIndexOfDiff == 0) {
            return EMPTY;
        }
        return strs[0].substring(0, smallestIndexOfDiff);
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s2, CharSequence t2) {
        int i2;
        if (s2 == null || t2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n2 = s2.length();
        int m2 = t2.length();
        if (n2 == 0) {
            return m2;
        }
        if (m2 == 0) {
            return n2;
        }
        if (n2 > m2) {
            CharSequence tmp = s2;
            s2 = t2;
            t2 = tmp;
            n2 = m2;
            m2 = t2.length();
        }
        int[] p2 = new int[n2 + 1];
        for (i2 = 0; i2 <= n2; ++i2) {
            p2[i2] = i2;
        }
        for (int j2 = 1; j2 <= m2; ++j2) {
            int upper_left = p2[0];
            char t_j = t2.charAt(j2 - 1);
            p2[0] = j2;
            for (i2 = 1; i2 <= n2; ++i2) {
                int upper = p2[i2];
                int cost = s2.charAt(i2 - 1) == t_j ? 0 : 1;
                p2[i2] = Math.min(Math.min(p2[i2 - 1] + 1, p2[i2] + 1), upper_left + cost);
                upper_left = upper;
            }
        }
        return p2[n2];
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s2, CharSequence t2, int threshold) {
        if (s2 == null || t2 == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        int n2 = s2.length();
        int m2 = t2.length();
        if (n2 == 0) {
            return m2 <= threshold ? m2 : -1;
        }
        if (m2 == 0) {
            return n2 <= threshold ? n2 : -1;
        }
        if (Math.abs(n2 - m2) > threshold) {
            return -1;
        }
        if (n2 > m2) {
            CharSequence tmp = s2;
            s2 = t2;
            t2 = tmp;
            n2 = m2;
            m2 = t2.length();
        }
        int[] p2 = new int[n2 + 1];
        int[] d2 = new int[n2 + 1];
        int boundary = Math.min(n2, threshold) + 1;
        for (int i2 = 0; i2 < boundary; ++i2) {
            p2[i2] = i2;
        }
        Arrays.fill(p2, boundary, p2.length, Integer.MAX_VALUE);
        Arrays.fill(d2, Integer.MAX_VALUE);
        for (int j2 = 1; j2 <= m2; ++j2) {
            int max;
            char t_j = t2.charAt(j2 - 1);
            d2[0] = j2;
            int min = Math.max(1, j2 - threshold);
            int n3 = max = j2 > Integer.MAX_VALUE - threshold ? n2 : Math.min(n2, j2 + threshold);
            if (min > max) {
                return -1;
            }
            if (min > 1) {
                d2[min - 1] = Integer.MAX_VALUE;
            }
            for (int i3 = min; i3 <= max; ++i3) {
                d2[i3] = s2.charAt(i3 - 1) == t_j ? p2[i3 - 1] : 1 + Math.min(Math.min(d2[i3 - 1], p2[i3]), p2[i3 - 1]);
            }
            int[] _d = p2;
            p2 = d2;
            d2 = _d;
        }
        if (p2[n2] <= threshold) {
            return p2[n2];
        }
        return -1;
    }

    @Deprecated
    public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
        double DEFAULT_SCALING_FACTOR = 0.1;
        if (first == null || second == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int[] mtp = StringUtils.matches(first, second);
        double m2 = mtp[0];
        if (m2 == 0.0) {
            return 0.0;
        }
        double j2 = (m2 / (double)first.length() + m2 / (double)second.length() + (m2 - (double)mtp[1]) / m2) / 3.0;
        double jw = j2 < 0.7 ? j2 : j2 + Math.min(0.1, 1.0 / (double)mtp[3]) * (double)mtp[2] * (1.0 - j2);
        return (double)Math.round(jw * 100.0) / 100.0;
    }

    private static int[] matches(CharSequence first, CharSequence second) {
        int i2;
        CharSequence min;
        CharSequence max;
        if (first.length() > second.length()) {
            max = first;
            min = second;
        } else {
            max = second;
            min = first;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        block0: for (int mi = 0; mi < min.length(); ++mi) {
            char c1 = min.charAt(mi);
            int xn = Math.min(mi + range + 1, max.length());
            for (int xi = Math.max(mi - range, 0); xi < xn; ++xi) {
                if (matchFlags[xi] || c1 != max.charAt(xi)) continue;
                matchIndexes[mi] = xi;
                matchFlags[xi] = true;
                ++matches;
                continue block0;
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        int si = 0;
        for (i2 = 0; i2 < min.length(); ++i2) {
            if (matchIndexes[i2] == -1) continue;
            ms1[si] = min.charAt(i2);
            ++si;
        }
        si = 0;
        for (i2 = 0; i2 < max.length(); ++i2) {
            if (!matchFlags[i2]) continue;
            ms2[si] = max.charAt(i2);
            ++si;
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; ++mi) {
            if (ms1[mi] == ms2[mi]) continue;
            ++transpositions;
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length() && first.charAt(mi) == second.charAt(mi); ++mi) {
            ++prefix;
        }
        return new int[]{matches, transpositions / 2, prefix, max.length()};
    }

    @Deprecated
    public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
        if (term == null || query == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale must not be null");
        }
        String termLowerCase = term.toString().toLowerCase(locale);
        String queryLowerCase = query.toString().toLowerCase(locale);
        int score = 0;
        int termIndex = 0;
        int previousMatchingCharacterIndex = Integer.MIN_VALUE;
        for (int queryIndex = 0; queryIndex < queryLowerCase.length(); ++queryIndex) {
            char queryChar = queryLowerCase.charAt(queryIndex);
            boolean termCharacterMatchFound = false;
            while (termIndex < termLowerCase.length() && !termCharacterMatchFound) {
                char termChar = termLowerCase.charAt(termIndex);
                if (queryChar == termChar) {
                    ++score;
                    if (previousMatchingCharacterIndex + 1 == termIndex) {
                        score += 2;
                    }
                    previousMatchingCharacterIndex = termIndex;
                    termCharacterMatchFound = true;
                }
                ++termIndex;
            }
        }
        return score;
    }

    public static boolean startsWith(CharSequence str, CharSequence prefix) {
        return StringUtils.startsWith(str, prefix, false);
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return StringUtils.startsWith(str, prefix, true);
    }

    private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == prefix;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
    }

    public static boolean startsWithAny(CharSequence sequence, CharSequence ... searchStrings) {
        if (StringUtils.isEmpty(sequence) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (!StringUtils.startsWith(sequence, searchString)) continue;
            return true;
        }
        return false;
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return StringUtils.endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return StringUtils.endsWith(str, suffix, true);
    }

    private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == suffix;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    public static String normalizeSpace(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        int size = str.length();
        char[] newChars = new char[size];
        int count = 0;
        int whitespacesCount = 0;
        boolean startWhitespaces = true;
        for (int i2 = 0; i2 < size; ++i2) {
            char actualChar = str.charAt(i2);
            boolean isWhitespace = Character.isWhitespace(actualChar);
            if (isWhitespace) {
                if (whitespacesCount == 0 && !startWhitespaces) {
                    newChars[count++] = SPACE.charAt(0);
                }
                ++whitespacesCount;
                continue;
            }
            startWhitespaces = false;
            newChars[count++] = actualChar == '\u00a0' ? 32 : (int)actualChar;
            whitespacesCount = 0;
        }
        if (startWhitespaces) {
            return EMPTY;
        }
        return new String(newChars, 0, count - (whitespacesCount > 0 ? 1 : 0)).trim();
    }

    public static boolean endsWithAny(CharSequence sequence, CharSequence ... searchStrings) {
        if (StringUtils.isEmpty(sequence) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (CharSequence searchString : searchStrings) {
            if (!StringUtils.endsWith(sequence, searchString)) continue;
            return true;
        }
        return false;
    }

    private static String appendIfMissing(String str, CharSequence suffix, boolean ignoreCase, CharSequence ... suffixes) {
        if (str == null || StringUtils.isEmpty(suffix) || StringUtils.endsWith(str, suffix, ignoreCase)) {
            return str;
        }
        if (suffixes != null && suffixes.length > 0) {
            for (CharSequence s2 : suffixes) {
                if (!StringUtils.endsWith(str, s2, ignoreCase)) continue;
                return str;
            }
        }
        return str + suffix.toString();
    }

    public static String appendIfMissing(String str, CharSequence suffix, CharSequence ... suffixes) {
        return StringUtils.appendIfMissing(str, suffix, false, suffixes);
    }

    public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence ... suffixes) {
        return StringUtils.appendIfMissing(str, suffix, true, suffixes);
    }

    private static String prependIfMissing(String str, CharSequence prefix, boolean ignoreCase, CharSequence ... prefixes) {
        if (str == null || StringUtils.isEmpty(prefix) || StringUtils.startsWith(str, prefix, ignoreCase)) {
            return str;
        }
        if (prefixes != null && prefixes.length > 0) {
            for (CharSequence p2 : prefixes) {
                if (!StringUtils.startsWith(str, p2, ignoreCase)) continue;
                return str;
            }
        }
        return prefix.toString() + str;
    }

    public static String prependIfMissing(String str, CharSequence prefix, CharSequence ... prefixes) {
        return StringUtils.prependIfMissing(str, prefix, false, prefixes);
    }

    public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence ... prefixes) {
        return StringUtils.prependIfMissing(str, prefix, true, prefixes);
    }

    @Deprecated
    public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return charsetName != null ? new String(bytes, charsetName) : new String(bytes, Charset.defaultCharset());
    }

    public static String toEncodedString(byte[] bytes, Charset charset) {
        return new String(bytes, charset != null ? charset : Charset.defaultCharset());
    }

    public static String wrap(String str, char wrapWith) {
        if (StringUtils.isEmpty(str) || wrapWith == '\u0000') {
            return str;
        }
        return wrapWith + str + wrapWith;
    }

    public static String wrap(String str, String wrapWith) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(wrapWith)) {
            return str;
        }
        return wrapWith.concat(str).concat(wrapWith);
    }

    public static String wrapIfMissing(String str, char wrapWith) {
        if (StringUtils.isEmpty(str) || wrapWith == '\u0000') {
            return str;
        }
        StringBuilder builder = new StringBuilder(str.length() + 2);
        if (str.charAt(0) != wrapWith) {
            builder.append(wrapWith);
        }
        builder.append(str);
        if (str.charAt(str.length() - 1) != wrapWith) {
            builder.append(wrapWith);
        }
        return builder.toString();
    }

    public static String wrapIfMissing(String str, String wrapWith) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(wrapWith)) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str.length() + wrapWith.length() + wrapWith.length());
        if (!str.startsWith(wrapWith)) {
            builder.append(wrapWith);
        }
        builder.append(str);
        if (!str.endsWith(wrapWith)) {
            builder.append(wrapWith);
        }
        return builder.toString();
    }

    public static String unwrap(String str, String wrapToken) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(wrapToken)) {
            return str;
        }
        if (StringUtils.startsWith(str, wrapToken) && StringUtils.endsWith(str, wrapToken)) {
            int startIndex = str.indexOf(wrapToken);
            int endIndex = str.lastIndexOf(wrapToken);
            int wrapLength = wrapToken.length();
            if (startIndex != -1 && endIndex != -1) {
                return str.substring(startIndex + wrapLength, endIndex);
            }
        }
        return str;
    }

    public static String unwrap(String str, char wrapChar) {
        if (StringUtils.isEmpty(str) || wrapChar == '\u0000') {
            return str;
        }
        if (str.charAt(0) == wrapChar && str.charAt(str.length() - 1) == wrapChar) {
            boolean startIndex = false;
            int endIndex = str.length() - 1;
            if (endIndex != -1) {
                return str.substring(1, endIndex);
            }
        }
        return str;
    }

    public static int[] toCodePoints(CharSequence str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }
        String s2 = str.toString();
        int[] result = new int[s2.codePointCount(0, s2.length())];
        int index = 0;
        for (int i2 = 0; i2 < result.length; ++i2) {
            result[i2] = s2.codePointAt(index);
            index += Character.charCount(result[i2]);
        }
        return result;
    }
}

