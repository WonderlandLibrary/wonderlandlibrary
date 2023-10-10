/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckReturnValue
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Platform;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.SmallCharMatcher;
import java.util.Arrays;
import java.util.BitSet;
import javax.annotation.CheckReturnValue;

@Beta
@GwtCompatible(emulated=true)
public abstract class CharMatcher
implements Predicate<Character> {
    public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher(){

        @Override
        public boolean matches(char c2) {
            switch (c2) {
                case '\t': 
                case '\n': 
                case '\u000b': 
                case '\f': 
                case '\r': 
                case ' ': 
                case '\u0085': 
                case '\u1680': 
                case '\u2028': 
                case '\u2029': 
                case '\u205f': 
                case '\u3000': {
                    return true;
                }
                case '\u2007': {
                    return false;
                }
            }
            return c2 >= '\u2000' && c2 <= '\u200a';
        }

        @Override
        public String toString() {
            return "CharMatcher.BREAKING_WHITESPACE";
        }
    };
    public static final CharMatcher ASCII = CharMatcher.inRange('\u0000', '\u007f', "CharMatcher.ASCII");
    private static final String ZEROES = "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10";
    private static final String NINES;
    public static final CharMatcher DIGIT;
    public static final CharMatcher JAVA_DIGIT;
    public static final CharMatcher JAVA_LETTER;
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    public static final CharMatcher JAVA_UPPER_CASE;
    public static final CharMatcher JAVA_LOWER_CASE;
    public static final CharMatcher JAVA_ISO_CONTROL;
    public static final CharMatcher INVISIBLE;
    public static final CharMatcher SINGLE_WIDTH;
    public static final CharMatcher ANY;
    public static final CharMatcher NONE;
    final String description;
    private static final int DISTINCT_CHARS = 65536;
    static final String WHITESPACE_TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f\u00a0\f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000";
    static final int WHITESPACE_MULTIPLIER = 1682554634;
    static final int WHITESPACE_SHIFT;
    public static final CharMatcher WHITESPACE;

    private static String showCharacter(char c2) {
        String hex = "0123456789ABCDEF";
        char[] tmp = new char[]{'\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000'};
        for (int i2 = 0; i2 < 4; ++i2) {
            tmp[5 - i2] = hex.charAt(c2 & 0xF);
            c2 = (char)(c2 >> 4);
        }
        return String.copyValueOf(tmp);
    }

    public static CharMatcher is(final char match) {
        String description = "CharMatcher.is('" + CharMatcher.showCharacter(match) + "')";
        return new FastMatcher(description){

            @Override
            public boolean matches(char c2) {
                return c2 == match;
            }

            @Override
            public String replaceFrom(CharSequence sequence, char replacement) {
                return sequence.toString().replace(match, replacement);
            }

            @Override
            public CharMatcher and(CharMatcher other) {
                return other.matches(match) ? this : NONE;
            }

            @Override
            public CharMatcher or(CharMatcher other) {
                return other.matches(match) ? other : super.or(other);
            }

            @Override
            public CharMatcher negate() {
                return 9.isNot(match);
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                table.set(match);
            }
        };
    }

    public static CharMatcher isNot(final char match) {
        String description = "CharMatcher.isNot('" + CharMatcher.showCharacter(match) + "')";
        return new FastMatcher(description){

            @Override
            public boolean matches(char c2) {
                return c2 != match;
            }

            @Override
            public CharMatcher and(CharMatcher other) {
                return other.matches(match) ? super.and(other) : other;
            }

            @Override
            public CharMatcher or(CharMatcher other) {
                return other.matches(match) ? ANY : this;
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                table.set(0, match);
                table.set(match + '\u0001', 65536);
            }

            @Override
            public CharMatcher negate() {
                return 10.is(match);
            }
        };
    }

    public static CharMatcher anyOf(CharSequence sequence) {
        switch (sequence.length()) {
            case 0: {
                return NONE;
            }
            case 1: {
                return CharMatcher.is(sequence.charAt(0));
            }
            case 2: {
                return CharMatcher.isEither(sequence.charAt(0), sequence.charAt(1));
            }
        }
        final char[] chars = sequence.toString().toCharArray();
        Arrays.sort(chars);
        StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
        for (char c2 : chars) {
            description.append(CharMatcher.showCharacter(c2));
        }
        description.append("\")");
        return new CharMatcher(description.toString()){

            @Override
            public boolean matches(char c2) {
                return Arrays.binarySearch(chars, c2) >= 0;
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                for (char c2 : chars) {
                    table.set(c2);
                }
            }
        };
    }

    private static CharMatcher isEither(final char match1, final char match2) {
        String description = "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(match1) + CharMatcher.showCharacter(match2) + "\")";
        return new FastMatcher(description){

            @Override
            public boolean matches(char c2) {
                return c2 == match1 || c2 == match2;
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                table.set(match1);
                table.set(match2);
            }
        };
    }

    public static CharMatcher noneOf(CharSequence sequence) {
        return CharMatcher.anyOf(sequence).negate();
    }

    public static CharMatcher inRange(char startInclusive, char endInclusive) {
        Preconditions.checkArgument(endInclusive >= startInclusive);
        String description = "CharMatcher.inRange('" + CharMatcher.showCharacter(startInclusive) + "', '" + CharMatcher.showCharacter(endInclusive) + "')";
        return CharMatcher.inRange(startInclusive, endInclusive, description);
    }

    static CharMatcher inRange(final char startInclusive, final char endInclusive, String description) {
        return new FastMatcher(description){

            @Override
            public boolean matches(char c2) {
                return startInclusive <= c2 && c2 <= endInclusive;
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                table.set((int)startInclusive, endInclusive + '\u0001');
            }
        };
    }

    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        if (predicate instanceof CharMatcher) {
            return (CharMatcher)predicate;
        }
        String description = "CharMatcher.forPredicate(" + predicate + ")";
        return new CharMatcher(description){

            @Override
            public boolean matches(char c2) {
                return predicate.apply(Character.valueOf(c2));
            }

            @Override
            public boolean apply(Character character) {
                return predicate.apply(Preconditions.checkNotNull(character));
            }
        };
    }

    CharMatcher(String description) {
        this.description = description;
    }

    protected CharMatcher() {
        this.description = super.toString();
    }

    public abstract boolean matches(char var1);

    public CharMatcher negate() {
        return new NegatedMatcher(this);
    }

    public CharMatcher and(CharMatcher other) {
        return new And(this, Preconditions.checkNotNull(other));
    }

    public CharMatcher or(CharMatcher other) {
        return new Or(this, Preconditions.checkNotNull(other));
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    CharMatcher withToString(String description) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible(value="java.util.BitSet")
    CharMatcher precomputedInternal() {
        BitSet table = new BitSet();
        this.setBits(table);
        int totalCharacters = table.cardinality();
        if (totalCharacters * 2 <= 65536) {
            return CharMatcher.precomputedPositive(totalCharacters, table, this.description);
        }
        table.flip(0, 65536);
        int negatedCharacters = 65536 - totalCharacters;
        String suffix = ".negate()";
        String negatedDescription = this.description.endsWith(suffix) ? this.description.substring(0, this.description.length() - suffix.length()) : this.description + suffix;
        return new NegatedFastMatcher(this.toString(), CharMatcher.precomputedPositive(negatedCharacters, table, negatedDescription));
    }

    @GwtIncompatible(value="java.util.BitSet")
    private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
        switch (totalCharacters) {
            case 0: {
                return NONE;
            }
            case 1: {
                return CharMatcher.is((char)table.nextSetBit(0));
            }
            case 2: {
                char c1 = (char)table.nextSetBit(0);
                char c2 = (char)table.nextSetBit(c1 + '\u0001');
                return CharMatcher.isEither(c1, c2);
            }
        }
        return CharMatcher.isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
    }

    @GwtIncompatible(value="SmallCharMatcher")
    private static boolean isSmall(int totalCharacters, int tableLength) {
        return totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16;
    }

    @GwtIncompatible(value="java.util.BitSet")
    void setBits(BitSet table) {
        for (int c2 = 65535; c2 >= 0; --c2) {
            if (!this.matches((char)c2)) continue;
            table.set(c2);
        }
    }

    public boolean matchesAnyOf(CharSequence sequence) {
        return !this.matchesNoneOf(sequence);
    }

    public boolean matchesAllOf(CharSequence sequence) {
        for (int i2 = sequence.length() - 1; i2 >= 0; --i2) {
            if (this.matches(sequence.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public boolean matchesNoneOf(CharSequence sequence) {
        return this.indexIn(sequence) == -1;
    }

    public int indexIn(CharSequence sequence) {
        int length = sequence.length();
        for (int i2 = 0; i2 < length; ++i2) {
            if (!this.matches(sequence.charAt(i2))) continue;
            return i2;
        }
        return -1;
    }

    public int indexIn(CharSequence sequence, int start) {
        int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        for (int i2 = start; i2 < length; ++i2) {
            if (!this.matches(sequence.charAt(i2))) continue;
            return i2;
        }
        return -1;
    }

    public int lastIndexIn(CharSequence sequence) {
        for (int i2 = sequence.length() - 1; i2 >= 0; --i2) {
            if (!this.matches(sequence.charAt(i2))) continue;
            return i2;
        }
        return -1;
    }

    public int countIn(CharSequence sequence) {
        int count = 0;
        for (int i2 = 0; i2 < sequence.length(); ++i2) {
            if (!this.matches(sequence.charAt(i2))) continue;
            ++count;
        }
        return count;
    }

    @CheckReturnValue
    public String removeFrom(CharSequence sequence) {
        String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        int spread = 1;
        block0: while (true) {
            ++pos;
            while (pos != chars.length) {
                if (!this.matches(chars[pos])) {
                    chars[pos - spread] = chars[pos];
                    ++pos;
                    continue;
                }
                ++spread;
                continue block0;
            }
            break;
        }
        return new String(chars, 0, pos - spread);
    }

    @CheckReturnValue
    public String retainFrom(CharSequence sequence) {
        return this.negate().removeFrom(sequence);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence sequence, char replacement) {
        String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        chars[pos] = replacement;
        for (int i2 = pos + 1; i2 < chars.length; ++i2) {
            if (!this.matches(chars[i2])) continue;
            chars[i2] = replacement;
        }
        return new String(chars);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence sequence, CharSequence replacement) {
        int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return this.removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return this.replaceFrom(sequence, replacement.charAt(0));
        }
        String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        int len = string.length();
        StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
        int oldpos = 0;
        do {
            buf.append(string, oldpos, pos);
            buf.append(replacement);
        } while ((pos = this.indexIn(string, oldpos = pos + 1)) != -1);
        buf.append(string, oldpos, len);
        return buf.toString();
    }

    @CheckReturnValue
    public String trimFrom(CharSequence sequence) {
        int last;
        int first;
        int len = sequence.length();
        for (first = 0; first < len && this.matches(sequence.charAt(first)); ++first) {
        }
        for (last = len - 1; last > first && this.matches(sequence.charAt(last)); --last) {
        }
        return sequence.subSequence(first, last + 1).toString();
    }

    @CheckReturnValue
    public String trimLeadingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int first = 0; first < len; ++first) {
            if (this.matches(sequence.charAt(first))) continue;
            return sequence.subSequence(first, len).toString();
        }
        return "";
    }

    @CheckReturnValue
    public String trimTrailingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int last = len - 1; last >= 0; --last) {
            if (this.matches(sequence.charAt(last))) continue;
            return sequence.subSequence(0, last + 1).toString();
        }
        return "";
    }

    @CheckReturnValue
    public String collapseFrom(CharSequence sequence, char replacement) {
        int len = sequence.length();
        for (int i2 = 0; i2 < len; ++i2) {
            char c2 = sequence.charAt(i2);
            if (!this.matches(c2)) continue;
            if (!(c2 != replacement || i2 != len - 1 && this.matches(sequence.charAt(i2 + 1)))) {
                ++i2;
                continue;
            }
            StringBuilder builder = new StringBuilder(len).append(sequence.subSequence(0, i2)).append(replacement);
            return this.finishCollapseFrom(sequence, i2 + 1, len, replacement, builder, true);
        }
        return sequence.toString();
    }

    @CheckReturnValue
    public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
        int last;
        int first;
        int len = sequence.length();
        for (first = 0; first < len && this.matches(sequence.charAt(first)); ++first) {
        }
        for (last = len - 1; last > first && this.matches(sequence.charAt(last)); --last) {
        }
        return first == 0 && last == len - 1 ? this.collapseFrom(sequence, replacement) : this.finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
    }

    private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
        for (int i2 = start; i2 < end; ++i2) {
            char c2 = sequence.charAt(i2);
            if (this.matches(c2)) {
                if (inMatchingGroup) continue;
                builder.append(replacement);
                inMatchingGroup = true;
                continue;
            }
            builder.append(c2);
            inMatchingGroup = false;
        }
        return builder.toString();
    }

    @Override
    @Deprecated
    public boolean apply(Character character) {
        return this.matches(character.charValue());
    }

    public String toString() {
        return this.description;
    }

    static {
        StringBuilder builder = new StringBuilder(ZEROES.length());
        for (int i2 = 0; i2 < ZEROES.length(); ++i2) {
            builder.append((char)(ZEROES.charAt(i2) + 9));
        }
        NINES = builder.toString();
        DIGIT = new RangesMatcher("CharMatcher.DIGIT", ZEROES.toCharArray(), NINES.toCharArray());
        JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT"){

            @Override
            public boolean matches(char c2) {
                return Character.isDigit(c2);
            }
        };
        JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER"){

            @Override
            public boolean matches(char c2) {
                return Character.isLetter(c2);
            }
        };
        JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT"){

            @Override
            public boolean matches(char c2) {
                return Character.isLetterOrDigit(c2);
            }
        };
        JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE"){

            @Override
            public boolean matches(char c2) {
                return Character.isUpperCase(c2);
            }
        };
        JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE"){

            @Override
            public boolean matches(char c2) {
                return Character.isLowerCase(c2);
            }
        };
        JAVA_ISO_CONTROL = CharMatcher.inRange('\u0000', '\u001f').or(CharMatcher.inRange('\u007f', '\u009f')).withToString("CharMatcher.JAVA_ISO_CONTROL");
        INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa".toCharArray(), " \u00a0\u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb".toCharArray());
        SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\u0000\u05be\u05d0\u05f3\u0600\u0750\u0e00\u1e00\u2100\ufb50\ufe70\uff61".toCharArray(), "\u04f9\u05be\u05ea\u05f4\u06ff\u077f\u0e7f\u20af\u213a\ufdff\ufeff\uffdc".toCharArray());
        ANY = new FastMatcher("CharMatcher.ANY"){

            @Override
            public boolean matches(char c2) {
                return true;
            }

            @Override
            public int indexIn(CharSequence sequence) {
                return sequence.length() == 0 ? -1 : 0;
            }

            @Override
            public int indexIn(CharSequence sequence, int start) {
                int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                return start == length ? -1 : start;
            }

            @Override
            public int lastIndexIn(CharSequence sequence) {
                return sequence.length() - 1;
            }

            @Override
            public boolean matchesAllOf(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }

            @Override
            public boolean matchesNoneOf(CharSequence sequence) {
                return sequence.length() == 0;
            }

            @Override
            public String removeFrom(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }

            @Override
            public String replaceFrom(CharSequence sequence, char replacement) {
                char[] array = new char[sequence.length()];
                Arrays.fill(array, replacement);
                return new String(array);
            }

            @Override
            public String replaceFrom(CharSequence sequence, CharSequence replacement) {
                StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
                for (int i2 = 0; i2 < sequence.length(); ++i2) {
                    retval.append(replacement);
                }
                return retval.toString();
            }

            @Override
            public String collapseFrom(CharSequence sequence, char replacement) {
                return sequence.length() == 0 ? "" : String.valueOf(replacement);
            }

            @Override
            public String trimFrom(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }

            @Override
            public int countIn(CharSequence sequence) {
                return sequence.length();
            }

            @Override
            public CharMatcher and(CharMatcher other) {
                return Preconditions.checkNotNull(other);
            }

            @Override
            public CharMatcher or(CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }

            @Override
            public CharMatcher negate() {
                return NONE;
            }
        };
        NONE = new FastMatcher("CharMatcher.NONE"){

            @Override
            public boolean matches(char c2) {
                return false;
            }

            @Override
            public int indexIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }

            @Override
            public int indexIn(CharSequence sequence, int start) {
                int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                return -1;
            }

            @Override
            public int lastIndexIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }

            @Override
            public boolean matchesAllOf(CharSequence sequence) {
                return sequence.length() == 0;
            }

            @Override
            public boolean matchesNoneOf(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }

            @Override
            public String removeFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override
            public String replaceFrom(CharSequence sequence, char replacement) {
                return sequence.toString();
            }

            @Override
            public String replaceFrom(CharSequence sequence, CharSequence replacement) {
                Preconditions.checkNotNull(replacement);
                return sequence.toString();
            }

            @Override
            public String collapseFrom(CharSequence sequence, char replacement) {
                return sequence.toString();
            }

            @Override
            public String trimFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override
            public String trimLeadingFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override
            public String trimTrailingFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override
            public int countIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return 0;
            }

            @Override
            public CharMatcher and(CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }

            @Override
            public CharMatcher or(CharMatcher other) {
                return Preconditions.checkNotNull(other);
            }

            @Override
            public CharMatcher negate() {
                return ANY;
            }
        };
        WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(WHITESPACE_TABLE.length() - 1);
        WHITESPACE = new FastMatcher("WHITESPACE"){

            @Override
            public boolean matches(char c2) {
                return CharMatcher.WHITESPACE_TABLE.charAt(1682554634 * c2 >>> WHITESPACE_SHIFT) == c2;
            }

            @Override
            @GwtIncompatible(value="java.util.BitSet")
            void setBits(BitSet table) {
                for (int i2 = 0; i2 < CharMatcher.WHITESPACE_TABLE.length(); ++i2) {
                    table.set(CharMatcher.WHITESPACE_TABLE.charAt(i2));
                }
            }
        };
    }

    @GwtIncompatible(value="java.util.BitSet")
    private static class BitSetMatcher
    extends FastMatcher {
        private final BitSet table;

        private BitSetMatcher(BitSet table, String description) {
            super(description);
            if (table.length() + 64 < table.size()) {
                table = (BitSet)table.clone();
            }
            this.table = table;
        }

        @Override
        public boolean matches(char c2) {
            return this.table.get(c2);
        }

        @Override
        void setBits(BitSet bitSet) {
            bitSet.or(this.table);
        }
    }

    static final class NegatedFastMatcher
    extends NegatedMatcher {
        NegatedFastMatcher(CharMatcher original) {
            super(original);
        }

        NegatedFastMatcher(String toString, CharMatcher original) {
            super(toString, original);
        }

        @Override
        public final CharMatcher precomputed() {
            return this;
        }

        @Override
        CharMatcher withToString(String description) {
            return new NegatedFastMatcher(description, this.original);
        }
    }

    static abstract class FastMatcher
    extends CharMatcher {
        FastMatcher() {
        }

        FastMatcher(String description) {
            super(description);
        }

        @Override
        public final CharMatcher precomputed() {
            return this;
        }

        @Override
        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }

    private static class Or
    extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        Or(CharMatcher a2, CharMatcher b2, String description) {
            super(description);
            this.first = Preconditions.checkNotNull(a2);
            this.second = Preconditions.checkNotNull(b2);
        }

        Or(CharMatcher a2, CharMatcher b2) {
            this(a2, b2, "CharMatcher.or(" + a2 + ", " + b2 + ")");
        }

        @Override
        @GwtIncompatible(value="java.util.BitSet")
        void setBits(BitSet table) {
            this.first.setBits(table);
            this.second.setBits(table);
        }

        @Override
        public boolean matches(char c2) {
            return this.first.matches(c2) || this.second.matches(c2);
        }

        @Override
        CharMatcher withToString(String description) {
            return new Or(this.first, this.second, description);
        }
    }

    private static class And
    extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        And(CharMatcher a2, CharMatcher b2) {
            this(a2, b2, "CharMatcher.and(" + a2 + ", " + b2 + ")");
        }

        And(CharMatcher a2, CharMatcher b2, String description) {
            super(description);
            this.first = Preconditions.checkNotNull(a2);
            this.second = Preconditions.checkNotNull(b2);
        }

        @Override
        public boolean matches(char c2) {
            return this.first.matches(c2) && this.second.matches(c2);
        }

        @Override
        @GwtIncompatible(value="java.util.BitSet")
        void setBits(BitSet table) {
            BitSet tmp1 = new BitSet();
            this.first.setBits(tmp1);
            BitSet tmp2 = new BitSet();
            this.second.setBits(tmp2);
            tmp1.and(tmp2);
            table.or(tmp1);
        }

        @Override
        CharMatcher withToString(String description) {
            return new And(this.first, this.second, description);
        }
    }

    private static class NegatedMatcher
    extends CharMatcher {
        final CharMatcher original;

        NegatedMatcher(String toString, CharMatcher original) {
            super(toString);
            this.original = original;
        }

        NegatedMatcher(CharMatcher original) {
            this(original + ".negate()", original);
        }

        @Override
        public boolean matches(char c2) {
            return !this.original.matches(c2);
        }

        @Override
        public boolean matchesAllOf(CharSequence sequence) {
            return this.original.matchesNoneOf(sequence);
        }

        @Override
        public boolean matchesNoneOf(CharSequence sequence) {
            return this.original.matchesAllOf(sequence);
        }

        @Override
        public int countIn(CharSequence sequence) {
            return sequence.length() - this.original.countIn(sequence);
        }

        @Override
        @GwtIncompatible(value="java.util.BitSet")
        void setBits(BitSet table) {
            BitSet tmp = new BitSet();
            this.original.setBits(tmp);
            tmp.flip(0, 65536);
            table.or(tmp);
        }

        @Override
        public CharMatcher negate() {
            return this.original;
        }

        @Override
        CharMatcher withToString(String description) {
            return new NegatedMatcher(description, this.original);
        }
    }

    private static class RangesMatcher
    extends CharMatcher {
        private final char[] rangeStarts;
        private final char[] rangeEnds;

        RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
            super(description);
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
            for (int i2 = 0; i2 < rangeStarts.length; ++i2) {
                Preconditions.checkArgument(rangeStarts[i2] <= rangeEnds[i2]);
                if (i2 + 1 >= rangeStarts.length) continue;
                Preconditions.checkArgument(rangeEnds[i2] < rangeStarts[i2 + 1]);
            }
        }

        @Override
        public boolean matches(char c2) {
            int index = Arrays.binarySearch(this.rangeStarts, c2);
            if (index >= 0) {
                return true;
            }
            return (index = ~index - 1) >= 0 && c2 <= this.rangeEnds[index];
        }
    }
}

