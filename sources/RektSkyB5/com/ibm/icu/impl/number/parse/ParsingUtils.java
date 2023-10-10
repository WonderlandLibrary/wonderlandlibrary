/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.text.UnicodeSet;

public class ParsingUtils {
    public static final int PARSE_FLAG_IGNORE_CASE = 1;
    public static final int PARSE_FLAG_MONETARY_SEPARATORS = 2;
    public static final int PARSE_FLAG_STRICT_SEPARATORS = 4;
    public static final int PARSE_FLAG_STRICT_GROUPING_SIZE = 8;
    public static final int PARSE_FLAG_INTEGER_ONLY = 16;
    public static final int PARSE_FLAG_GROUPING_DISABLED = 32;
    public static final int PARSE_FLAG_INCLUDE_UNPAIRED_AFFIXES = 128;
    public static final int PARSE_FLAG_USE_FULL_AFFIXES = 256;
    public static final int PARSE_FLAG_EXACT_AFFIX = 512;
    public static final int PARSE_FLAG_PLUS_SIGN_ALLOWED = 1024;
    public static final int PARSE_FLAG_FORCE_BIG_DECIMAL = 4096;
    public static final int PARSE_FLAG_NO_FOREIGN_CURRENCIES = 8192;

    public static void putLeadCodePoints(UnicodeSet input, UnicodeSet output) {
        for (UnicodeSet.EntryRange range : input.ranges()) {
            output.add(range.codepoint, range.codepointEnd);
        }
        for (String str : input.strings()) {
            output.add(str.codePointAt(0));
        }
    }

    public static void putLeadCodePoint(String input, UnicodeSet output) {
        if (!input.isEmpty()) {
            output.add(input.codePointAt(0));
        }
    }

    public static boolean safeContains(UnicodeSet uniset, CharSequence str) {
        return str.length() != 0 && uniset.contains(str);
    }
}

