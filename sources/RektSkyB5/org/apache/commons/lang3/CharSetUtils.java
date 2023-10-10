/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3;

import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;

public class CharSetUtils {
    public static String squeeze(String str, String ... set) {
        if (StringUtils.isEmpty(str) || CharSetUtils.deepEmpty(set)) {
            return str;
        }
        CharSet chars = CharSet.getInstance(set);
        StringBuilder buffer = new StringBuilder(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        char lastChar = chrs[0];
        char ch = ' ';
        Character inChars = null;
        Character notInChars = null;
        buffer.append(lastChar);
        for (int i2 = 1; i2 < sz; ++i2) {
            ch = chrs[i2];
            if (ch == lastChar) {
                if (inChars != null && ch == inChars.charValue()) continue;
                if (notInChars == null || ch != notInChars.charValue()) {
                    if (chars.contains(ch)) {
                        inChars = Character.valueOf(ch);
                        continue;
                    }
                    notInChars = Character.valueOf(ch);
                }
            }
            buffer.append(ch);
            lastChar = ch;
        }
        return buffer.toString();
    }

    public static boolean containsAny(String str, String ... set) {
        if (StringUtils.isEmpty(str) || CharSetUtils.deepEmpty(set)) {
            return false;
        }
        CharSet chars = CharSet.getInstance(set);
        for (char c2 : str.toCharArray()) {
            if (!chars.contains(c2)) continue;
            return true;
        }
        return false;
    }

    public static int count(String str, String ... set) {
        if (StringUtils.isEmpty(str) || CharSetUtils.deepEmpty(set)) {
            return 0;
        }
        CharSet chars = CharSet.getInstance(set);
        int count = 0;
        for (char c2 : str.toCharArray()) {
            if (!chars.contains(c2)) continue;
            ++count;
        }
        return count;
    }

    public static String keep(String str, String ... set) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty() || CharSetUtils.deepEmpty(set)) {
            return "";
        }
        return CharSetUtils.modify(str, set, true);
    }

    public static String delete(String str, String ... set) {
        if (StringUtils.isEmpty(str) || CharSetUtils.deepEmpty(set)) {
            return str;
        }
        return CharSetUtils.modify(str, set, false);
    }

    private static String modify(String str, String[] set, boolean expect) {
        char[] chrs;
        CharSet chars = CharSet.getInstance(set);
        StringBuilder buffer = new StringBuilder(str.length());
        for (char chr : chrs = str.toCharArray()) {
            if (chars.contains(chr) != expect) continue;
            buffer.append(chr);
        }
        return buffer.toString();
    }

    private static boolean deepEmpty(String[] strings) {
        if (strings != null) {
            for (String s2 : strings) {
                if (!StringUtils.isNotEmpty(s2)) continue;
                return false;
            }
        }
        return true;
    }
}

