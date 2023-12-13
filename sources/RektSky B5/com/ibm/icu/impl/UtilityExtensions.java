/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.ReplaceableString;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeMatcher;

public class UtilityExtensions {
    public static void appendToRule(StringBuffer rule, String text, boolean isLiteral, boolean escapeUnprintable, StringBuffer quoteBuf) {
        for (int i2 = 0; i2 < text.length(); ++i2) {
            Utility.appendToRule(rule, text.charAt(i2), isLiteral, escapeUnprintable, quoteBuf);
        }
    }

    public static void appendToRule(StringBuffer rule, UnicodeMatcher matcher, boolean escapeUnprintable, StringBuffer quoteBuf) {
        if (matcher != null) {
            UtilityExtensions.appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
        }
    }

    public static String formatInput(ReplaceableString input, Transliterator.Position pos) {
        StringBuffer appendTo = new StringBuffer();
        UtilityExtensions.formatInput(appendTo, input, pos);
        return Utility.escape(appendTo.toString());
    }

    public static StringBuffer formatInput(StringBuffer appendTo, ReplaceableString input, Transliterator.Position pos) {
        if (0 <= pos.contextStart && pos.contextStart <= pos.start && pos.start <= pos.limit && pos.limit <= pos.contextLimit && pos.contextLimit <= input.length()) {
            String b2 = input.substring(pos.contextStart, pos.start);
            String c2 = input.substring(pos.start, pos.limit);
            String d2 = input.substring(pos.limit, pos.contextLimit);
            appendTo.append('{').append(b2).append('|').append(c2).append('|').append(d2).append('}');
        } else {
            appendTo.append("INVALID Position {cs=" + pos.contextStart + ", s=" + pos.start + ", l=" + pos.limit + ", cl=" + pos.contextLimit + "} on " + input);
        }
        return appendTo;
    }

    public static String formatInput(Replaceable input, Transliterator.Position pos) {
        return UtilityExtensions.formatInput((ReplaceableString)input, pos);
    }

    public static StringBuffer formatInput(StringBuffer appendTo, Replaceable input, Transliterator.Position pos) {
        return UtilityExtensions.formatInput(appendTo, (ReplaceableString)input, pos);
    }
}

