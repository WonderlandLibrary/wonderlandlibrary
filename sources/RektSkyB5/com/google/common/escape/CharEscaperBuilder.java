/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.escape.CharEscaper;
import com.google.common.escape.Escaper;
import java.util.HashMap;
import java.util.Map;

@Beta
@GwtCompatible
public final class CharEscaperBuilder {
    private final Map<Character, String> map = new HashMap<Character, String>();
    private int max = -1;

    public CharEscaperBuilder addEscape(char c2, String r2) {
        this.map.put(Character.valueOf(c2), Preconditions.checkNotNull(r2));
        if (c2 > this.max) {
            this.max = c2;
        }
        return this;
    }

    public CharEscaperBuilder addEscapes(char[] cs, String r2) {
        Preconditions.checkNotNull(r2);
        for (char c2 : cs) {
            this.addEscape(c2, r2);
        }
        return this;
    }

    public char[][] toArray() {
        char[][] result = new char[this.max + 1][];
        for (Map.Entry<Character, String> entry : this.map.entrySet()) {
            result[entry.getKey().charValue()] = entry.getValue().toCharArray();
        }
        return result;
    }

    public Escaper toEscaper() {
        return new CharArrayDecorator(this.toArray());
    }

    private static class CharArrayDecorator
    extends CharEscaper {
        private final char[][] replacements;
        private final int replaceLength;

        CharArrayDecorator(char[][] replacements) {
            this.replacements = replacements;
            this.replaceLength = replacements.length;
        }

        @Override
        public String escape(String s2) {
            int slen = s2.length();
            for (int index = 0; index < slen; ++index) {
                char c2 = s2.charAt(index);
                if (c2 >= this.replacements.length || this.replacements[c2] == null) continue;
                return this.escapeSlow(s2, index);
            }
            return s2;
        }

        @Override
        protected char[] escape(char c2) {
            return c2 < this.replaceLength ? this.replacements[c2] : null;
        }
    }
}

