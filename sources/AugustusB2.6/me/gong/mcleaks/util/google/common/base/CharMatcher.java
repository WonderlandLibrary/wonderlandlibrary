// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.util.BitSet;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class CharMatcher implements Predicate<Character>
{
    @Deprecated
    public static final CharMatcher WHITESPACE;
    @Deprecated
    public static final CharMatcher BREAKING_WHITESPACE;
    @Deprecated
    public static final CharMatcher ASCII;
    @Deprecated
    public static final CharMatcher DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_LETTER;
    @Deprecated
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_UPPER_CASE;
    @Deprecated
    public static final CharMatcher JAVA_LOWER_CASE;
    @Deprecated
    public static final CharMatcher JAVA_ISO_CONTROL;
    @Deprecated
    public static final CharMatcher INVISIBLE;
    @Deprecated
    public static final CharMatcher SINGLE_WIDTH;
    @Deprecated
    public static final CharMatcher ANY;
    @Deprecated
    public static final CharMatcher NONE;
    private static final int DISTINCT_CHARS = 65536;
    
    public static CharMatcher any() {
        return Any.INSTANCE;
    }
    
    public static CharMatcher none() {
        return None.INSTANCE;
    }
    
    public static CharMatcher whitespace() {
        return Whitespace.INSTANCE;
    }
    
    public static CharMatcher breakingWhitespace() {
        return BreakingWhitespace.INSTANCE;
    }
    
    public static CharMatcher ascii() {
        return Ascii.INSTANCE;
    }
    
    public static CharMatcher digit() {
        return Digit.INSTANCE;
    }
    
    public static CharMatcher javaDigit() {
        return JavaDigit.INSTANCE;
    }
    
    public static CharMatcher javaLetter() {
        return JavaLetter.INSTANCE;
    }
    
    public static CharMatcher javaLetterOrDigit() {
        return JavaLetterOrDigit.INSTANCE;
    }
    
    public static CharMatcher javaUpperCase() {
        return JavaUpperCase.INSTANCE;
    }
    
    public static CharMatcher javaLowerCase() {
        return JavaLowerCase.INSTANCE;
    }
    
    public static CharMatcher javaIsoControl() {
        return JavaIsoControl.INSTANCE;
    }
    
    public static CharMatcher invisible() {
        return Invisible.INSTANCE;
    }
    
    public static CharMatcher singleWidth() {
        return SingleWidth.INSTANCE;
    }
    
    public static CharMatcher is(final char match) {
        return new Is(match);
    }
    
    public static CharMatcher isNot(final char match) {
        return new IsNot(match);
    }
    
    public static CharMatcher anyOf(final CharSequence sequence) {
        switch (sequence.length()) {
            case 0: {
                return none();
            }
            case 1: {
                return is(sequence.charAt(0));
            }
            case 2: {
                return isEither(sequence.charAt(0), sequence.charAt(1));
            }
            default: {
                return new AnyOf(sequence);
            }
        }
    }
    
    public static CharMatcher noneOf(final CharSequence sequence) {
        return anyOf(sequence).negate();
    }
    
    public static CharMatcher inRange(final char startInclusive, final char endInclusive) {
        return new InRange(startInclusive, endInclusive);
    }
    
    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        return (predicate instanceof CharMatcher) ? ((CharMatcher)predicate) : new ForPredicate(predicate);
    }
    
    protected CharMatcher() {
    }
    
    public abstract boolean matches(final char p0);
    
    @Override
    public CharMatcher negate() {
        return new Negated(this);
    }
    
    public CharMatcher and(final CharMatcher other) {
        return new And(this, other);
    }
    
    public CharMatcher or(final CharMatcher other) {
        return new Or(this, other);
    }
    
    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }
    
    @GwtIncompatible
    CharMatcher precomputedInternal() {
        final BitSet table = new BitSet();
        this.setBits(table);
        final int totalCharacters = table.cardinality();
        if (totalCharacters * 2 <= 65536) {
            return precomputedPositive(totalCharacters, table, this.toString());
        }
        table.flip(0, 65536);
        final int negatedCharacters = 65536 - totalCharacters;
        final String suffix = ".negate()";
        final String description = this.toString();
        final String negatedDescription = description.endsWith(suffix) ? description.substring(0, description.length() - suffix.length()) : (description + suffix);
        return new NegatedFastMatcher(precomputedPositive(negatedCharacters, table, negatedDescription)) {
            @Override
            public String toString() {
                return description;
            }
        };
    }
    
    @GwtIncompatible
    private static CharMatcher precomputedPositive(final int totalCharacters, final BitSet table, final String description) {
        switch (totalCharacters) {
            case 0: {
                return none();
            }
            case 1: {
                return is((char)table.nextSetBit(0));
            }
            case 2: {
                final char c1 = (char)table.nextSetBit(0);
                final char c2 = (char)table.nextSetBit(c1 + '\u0001');
                return isEither(c1, c2);
            }
            default: {
                return isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
            }
        }
    }
    
    @GwtIncompatible
    private static boolean isSmall(final int totalCharacters, final int tableLength) {
        return totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16;
    }
    
    @GwtIncompatible
    void setBits(final BitSet table) {
        for (int c = 65535; c >= 0; --c) {
            if (this.matches((char)c)) {
                table.set(c);
            }
        }
    }
    
    public boolean matchesAnyOf(final CharSequence sequence) {
        return !this.matchesNoneOf(sequence);
    }
    
    public boolean matchesAllOf(final CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; --i) {
            if (!this.matches(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matchesNoneOf(final CharSequence sequence) {
        return this.indexIn(sequence) == -1;
    }
    
    public int indexIn(final CharSequence sequence) {
        return this.indexIn(sequence, 0);
    }
    
    public int indexIn(final CharSequence sequence, final int start) {
        final int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        for (int i = start; i < length; ++i) {
            if (this.matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexIn(final CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; --i) {
            if (this.matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int countIn(final CharSequence sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); ++i) {
            if (this.matches(sequence.charAt(i))) {
                ++count;
            }
        }
        return count;
    }
    
    public String removeFrom(final CharSequence sequence) {
        final String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final char[] chars = string.toCharArray();
        int spread = 1;
    Label_0029:
        while (true) {
            ++pos;
            while (pos != chars.length) {
                if (this.matches(chars[pos])) {
                    ++spread;
                    continue Label_0029;
                }
                chars[pos - spread] = chars[pos];
                ++pos;
            }
            break;
        }
        return new String(chars, 0, pos - spread);
    }
    
    public String retainFrom(final CharSequence sequence) {
        return this.negate().removeFrom(sequence);
    }
    
    public String replaceFrom(final CharSequence sequence, final char replacement) {
        final String string = sequence.toString();
        final int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final char[] chars = string.toCharArray();
        chars[pos] = replacement;
        for (int i = pos + 1; i < chars.length; ++i) {
            if (this.matches(chars[i])) {
                chars[i] = replacement;
            }
        }
        return new String(chars);
    }
    
    public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
        final int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return this.removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return this.replaceFrom(sequence, replacement.charAt(0));
        }
        final String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final int len = string.length();
        final StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
        int oldpos = 0;
        do {
            buf.append(string, oldpos, pos);
            buf.append(replacement);
            oldpos = pos + 1;
            pos = this.indexIn(string, oldpos);
        } while (pos != -1);
        buf.append(string, oldpos, len);
        return buf.toString();
    }
    
    public String trimFrom(final CharSequence sequence) {
        int len;
        int first;
        for (len = sequence.length(), first = 0; first < len && this.matches(sequence.charAt(first)); ++first) {}
        int last;
        for (last = len - 1; last > first && this.matches(sequence.charAt(last)); --last) {}
        return sequence.subSequence(first, last + 1).toString();
    }
    
    public String trimLeadingFrom(final CharSequence sequence) {
        for (int len = sequence.length(), first = 0; first < len; ++first) {
            if (!this.matches(sequence.charAt(first))) {
                return sequence.subSequence(first, len).toString();
            }
        }
        return "";
    }
    
    public String trimTrailingFrom(final CharSequence sequence) {
        final int len = sequence.length();
        for (int last = len - 1; last >= 0; --last) {
            if (!this.matches(sequence.charAt(last))) {
                return sequence.subSequence(0, last + 1).toString();
            }
        }
        return "";
    }
    
    public String collapseFrom(final CharSequence sequence, final char replacement) {
        for (int len = sequence.length(), i = 0; i < len; ++i) {
            final char c = sequence.charAt(i);
            if (this.matches(c)) {
                if (c != replacement || (i != len - 1 && this.matches(sequence.charAt(i + 1)))) {
                    final StringBuilder builder = new StringBuilder(len).append(sequence, 0, i).append(replacement);
                    return this.finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
                }
                ++i;
            }
        }
        return sequence.toString();
    }
    
    public String trimAndCollapseFrom(final CharSequence sequence, final char replacement) {
        final int len = sequence.length();
        int first = 0;
        int last = len - 1;
        while (first < len && this.matches(sequence.charAt(first))) {
            ++first;
        }
        while (last > first && this.matches(sequence.charAt(last))) {
            --last;
        }
        return (first == 0 && last == len - 1) ? this.collapseFrom(sequence, replacement) : this.finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
    }
    
    private String finishCollapseFrom(final CharSequence sequence, final int start, final int end, final char replacement, final StringBuilder builder, boolean inMatchingGroup) {
        for (int i = start; i < end; ++i) {
            final char c = sequence.charAt(i);
            if (this.matches(c)) {
                if (!inMatchingGroup) {
                    builder.append(replacement);
                    inMatchingGroup = true;
                }
            }
            else {
                builder.append(c);
                inMatchingGroup = false;
            }
        }
        return builder.toString();
    }
    
    @Deprecated
    @Override
    public boolean apply(final Character character) {
        return this.matches(character);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
    private static String showCharacter(char c) {
        final String hex = "0123456789ABCDEF";
        final char[] tmp = { '\\', 'u', '\0', '\0', '\0', '\0' };
        for (int i = 0; i < 4; ++i) {
            tmp[5 - i] = hex.charAt(c & '\u000f');
            c >>= 4;
        }
        return String.copyValueOf(tmp);
    }
    
    private static IsEither isEither(final char c1, final char c2) {
        return new IsEither(c1, c2);
    }
    
    static {
        WHITESPACE = whitespace();
        BREAKING_WHITESPACE = breakingWhitespace();
        ASCII = ascii();
        DIGIT = digit();
        JAVA_DIGIT = javaDigit();
        JAVA_LETTER = javaLetter();
        JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
        JAVA_UPPER_CASE = javaUpperCase();
        JAVA_LOWER_CASE = javaLowerCase();
        JAVA_ISO_CONTROL = javaIsoControl();
        INVISIBLE = invisible();
        SINGLE_WIDTH = singleWidth();
        ANY = any();
        NONE = none();
    }
    
    abstract static class FastMatcher extends CharMatcher
    {
        @Override
        public final CharMatcher precomputed() {
            return this;
        }
        
        @Override
        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }
    
    abstract static class NamedFastMatcher extends FastMatcher
    {
        private final String description;
        
        NamedFastMatcher(final String description) {
            this.description = Preconditions.checkNotNull(description);
        }
        
        @Override
        public final String toString() {
            return this.description;
        }
    }
    
    static class NegatedFastMatcher extends Negated
    {
        NegatedFastMatcher(final CharMatcher original) {
            super(original);
        }
        
        @Override
        public final CharMatcher precomputed() {
            return this;
        }
    }
    
    @GwtIncompatible
    private static final class BitSetMatcher extends NamedFastMatcher
    {
        private final BitSet table;
        
        private BitSetMatcher(BitSet table, final String description) {
            super(description);
            if (table.length() + 64 < table.size()) {
                table = (BitSet)table.clone();
            }
            this.table = table;
        }
        
        @Override
        public boolean matches(final char c) {
            return this.table.get(c);
        }
        
        @Override
        void setBits(final BitSet bitSet) {
            bitSet.or(this.table);
        }
    }
    
    private static final class Any extends NamedFastMatcher
    {
        static final Any INSTANCE;
        
        private Any() {
            super("CharMatcher.any()");
        }
        
        @Override
        public boolean matches(final char c) {
            return true;
        }
        
        @Override
        public int indexIn(final CharSequence sequence) {
            return (sequence.length() == 0) ? -1 : 0;
        }
        
        @Override
        public int indexIn(final CharSequence sequence, final int start) {
            final int length = sequence.length();
            Preconditions.checkPositionIndex(start, length);
            return (start == length) ? -1 : start;
        }
        
        @Override
        public int lastIndexIn(final CharSequence sequence) {
            return sequence.length() - 1;
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return true;
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence sequence) {
            return sequence.length() == 0;
        }
        
        @Override
        public String removeFrom(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return "";
        }
        
        @Override
        public String replaceFrom(final CharSequence sequence, final char replacement) {
            final char[] array = new char[sequence.length()];
            Arrays.fill(array, replacement);
            return new String(array);
        }
        
        @Override
        public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
            final StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
            for (int i = 0; i < sequence.length(); ++i) {
                result.append(replacement);
            }
            return result.toString();
        }
        
        @Override
        public String collapseFrom(final CharSequence sequence, final char replacement) {
            return (sequence.length() == 0) ? "" : String.valueOf(replacement);
        }
        
        @Override
        public String trimFrom(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return "";
        }
        
        @Override
        public int countIn(final CharSequence sequence) {
            return sequence.length();
        }
        
        @Override
        public CharMatcher and(final CharMatcher other) {
            return Preconditions.checkNotNull(other);
        }
        
        @Override
        public CharMatcher or(final CharMatcher other) {
            Preconditions.checkNotNull(other);
            return this;
        }
        
        @Override
        public CharMatcher negate() {
            return CharMatcher.none();
        }
        
        static {
            INSTANCE = new Any();
        }
    }
    
    private static final class None extends NamedFastMatcher
    {
        static final None INSTANCE;
        
        private None() {
            super("CharMatcher.none()");
        }
        
        @Override
        public boolean matches(final char c) {
            return false;
        }
        
        @Override
        public int indexIn(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return -1;
        }
        
        @Override
        public int indexIn(final CharSequence sequence, final int start) {
            final int length = sequence.length();
            Preconditions.checkPositionIndex(start, length);
            return -1;
        }
        
        @Override
        public int lastIndexIn(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return -1;
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence sequence) {
            return sequence.length() == 0;
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return true;
        }
        
        @Override
        public String removeFrom(final CharSequence sequence) {
            return sequence.toString();
        }
        
        @Override
        public String replaceFrom(final CharSequence sequence, final char replacement) {
            return sequence.toString();
        }
        
        @Override
        public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
            Preconditions.checkNotNull(replacement);
            return sequence.toString();
        }
        
        @Override
        public String collapseFrom(final CharSequence sequence, final char replacement) {
            return sequence.toString();
        }
        
        @Override
        public String trimFrom(final CharSequence sequence) {
            return sequence.toString();
        }
        
        @Override
        public String trimLeadingFrom(final CharSequence sequence) {
            return sequence.toString();
        }
        
        @Override
        public String trimTrailingFrom(final CharSequence sequence) {
            return sequence.toString();
        }
        
        @Override
        public int countIn(final CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return 0;
        }
        
        @Override
        public CharMatcher and(final CharMatcher other) {
            Preconditions.checkNotNull(other);
            return this;
        }
        
        @Override
        public CharMatcher or(final CharMatcher other) {
            return Preconditions.checkNotNull(other);
        }
        
        @Override
        public CharMatcher negate() {
            return CharMatcher.any();
        }
        
        static {
            INSTANCE = new None();
        }
    }
    
    @VisibleForTesting
    static final class Whitespace extends NamedFastMatcher
    {
        static final String TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000";
        static final int MULTIPLIER = 1682554634;
        static final int SHIFT;
        static final Whitespace INSTANCE;
        
        Whitespace() {
            super("CharMatcher.whitespace()");
        }
        
        @Override
        public boolean matches(final char c) {
            return "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(1682554634 * c >>> Whitespace.SHIFT) == c;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            for (int i = 0; i < "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length(); ++i) {
                table.set("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(i));
            }
        }
        
        static {
            SHIFT = Integer.numberOfLeadingZeros("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length() - 1);
            INSTANCE = new Whitespace();
        }
    }
    
    private static final class BreakingWhitespace extends CharMatcher
    {
        static final CharMatcher INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            switch (c) {
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
                default: {
                    return c >= '\u2000' && c <= '\u200a';
                }
            }
        }
        
        @Override
        public String toString() {
            return "CharMatcher.breakingWhitespace()";
        }
        
        static {
            INSTANCE = new BreakingWhitespace();
        }
    }
    
    private static final class Ascii extends NamedFastMatcher
    {
        static final Ascii INSTANCE;
        
        Ascii() {
            super("CharMatcher.ascii()");
        }
        
        @Override
        public boolean matches(final char c) {
            return c <= '\u007f';
        }
        
        static {
            INSTANCE = new Ascii();
        }
    }
    
    private static class RangesMatcher extends CharMatcher
    {
        private final String description;
        private final char[] rangeStarts;
        private final char[] rangeEnds;
        
        RangesMatcher(final String description, final char[] rangeStarts, final char[] rangeEnds) {
            this.description = description;
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
            for (int i = 0; i < rangeStarts.length; ++i) {
                Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
                if (i + 1 < rangeStarts.length) {
                    Preconditions.checkArgument(rangeEnds[i] < rangeStarts[i + 1]);
                }
            }
        }
        
        @Override
        public boolean matches(final char c) {
            int index = Arrays.binarySearch(this.rangeStarts, c);
            if (index >= 0) {
                return true;
            }
            index = ~index - 1;
            return index >= 0 && c <= this.rangeEnds[index];
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
    
    private static final class Digit extends RangesMatcher
    {
        private static final String ZEROES = "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10";
        static final Digit INSTANCE;
        
        private static char[] zeroes() {
            return "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".toCharArray();
        }
        
        private static char[] nines() {
            final char[] nines = new char["0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length()];
            for (int i = 0; i < "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length(); ++i) {
                nines[i] = (char)("0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".charAt(i) + '\t');
            }
            return nines;
        }
        
        private Digit() {
            super("CharMatcher.digit()", zeroes(), nines());
        }
        
        static {
            INSTANCE = new Digit();
        }
    }
    
    private static final class JavaDigit extends CharMatcher
    {
        static final JavaDigit INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            return Character.isDigit(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.javaDigit()";
        }
        
        static {
            INSTANCE = new JavaDigit();
        }
    }
    
    private static final class JavaLetter extends CharMatcher
    {
        static final JavaLetter INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            return Character.isLetter(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.javaLetter()";
        }
        
        static {
            INSTANCE = new JavaLetter();
        }
    }
    
    private static final class JavaLetterOrDigit extends CharMatcher
    {
        static final JavaLetterOrDigit INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            return Character.isLetterOrDigit(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.javaLetterOrDigit()";
        }
        
        static {
            INSTANCE = new JavaLetterOrDigit();
        }
    }
    
    private static final class JavaUpperCase extends CharMatcher
    {
        static final JavaUpperCase INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            return Character.isUpperCase(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.javaUpperCase()";
        }
        
        static {
            INSTANCE = new JavaUpperCase();
        }
    }
    
    private static final class JavaLowerCase extends CharMatcher
    {
        static final JavaLowerCase INSTANCE;
        
        @Override
        public boolean matches(final char c) {
            return Character.isLowerCase(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.javaLowerCase()";
        }
        
        static {
            INSTANCE = new JavaLowerCase();
        }
    }
    
    private static final class JavaIsoControl extends NamedFastMatcher
    {
        static final JavaIsoControl INSTANCE;
        
        private JavaIsoControl() {
            super("CharMatcher.javaIsoControl()");
        }
        
        @Override
        public boolean matches(final char c) {
            return c <= '\u001f' || (c >= '\u007f' && c <= '\u009f');
        }
        
        static {
            INSTANCE = new JavaIsoControl();
        }
    }
    
    private static final class Invisible extends RangesMatcher
    {
        private static final String RANGE_STARTS = "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa";
        private static final String RANGE_ENDS = "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb";
        static final Invisible INSTANCE;
        
        private Invisible() {
            super("CharMatcher.invisible()", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa".toCharArray(), "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb".toCharArray());
        }
        
        static {
            INSTANCE = new Invisible();
        }
    }
    
    private static final class SingleWidth extends RangesMatcher
    {
        static final SingleWidth INSTANCE;
        
        private SingleWidth() {
            super("CharMatcher.singleWidth()", "\u0000\u05be\u05d0\u05f3\u0600\u0750\u0e00\u1e00\u2100\ufb50\ufe70\uff61".toCharArray(), "\u04f9\u05be\u05ea\u05f4\u06ff\u077f\u0e7f\u20af\u213a\ufdff\ufeff\uffdc".toCharArray());
        }
        
        static {
            INSTANCE = new SingleWidth();
        }
    }
    
    private static class Negated extends CharMatcher
    {
        final CharMatcher original;
        
        Negated(final CharMatcher original) {
            this.original = Preconditions.checkNotNull(original);
        }
        
        @Override
        public boolean matches(final char c) {
            return !this.original.matches(c);
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence sequence) {
            return this.original.matchesNoneOf(sequence);
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence sequence) {
            return this.original.matchesAllOf(sequence);
        }
        
        @Override
        public int countIn(final CharSequence sequence) {
            return sequence.length() - this.original.countIn(sequence);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            final BitSet tmp = new BitSet();
            this.original.setBits(tmp);
            tmp.flip(0, 65536);
            table.or(tmp);
        }
        
        @Override
        public CharMatcher negate() {
            return this.original;
        }
        
        @Override
        public String toString() {
            return this.original + ".negate()";
        }
    }
    
    private static final class And extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        And(final CharMatcher a, final CharMatcher b) {
            this.first = Preconditions.checkNotNull(a);
            this.second = Preconditions.checkNotNull(b);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.first.matches(c) && this.second.matches(c);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            final BitSet tmp1 = new BitSet();
            this.first.setBits(tmp1);
            final BitSet tmp2 = new BitSet();
            this.second.setBits(tmp2);
            tmp1.and(tmp2);
            table.or(tmp1);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.and(" + this.first + ", " + this.second + ")";
        }
    }
    
    private static final class Or extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        Or(final CharMatcher a, final CharMatcher b) {
            this.first = Preconditions.checkNotNull(a);
            this.second = Preconditions.checkNotNull(b);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            this.first.setBits(table);
            this.second.setBits(table);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.first.matches(c) || this.second.matches(c);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.or(" + this.first + ", " + this.second + ")";
        }
    }
    
    private static final class Is extends FastMatcher
    {
        private final char match;
        
        Is(final char match) {
            this.match = match;
        }
        
        @Override
        public boolean matches(final char c) {
            return c == this.match;
        }
        
        @Override
        public String replaceFrom(final CharSequence sequence, final char replacement) {
            return sequence.toString().replace(this.match, replacement);
        }
        
        @Override
        public CharMatcher and(final CharMatcher other) {
            return other.matches(this.match) ? this : CharMatcher.none();
        }
        
        @Override
        public CharMatcher or(final CharMatcher other) {
            return other.matches(this.match) ? other : super.or(other);
        }
        
        @Override
        public CharMatcher negate() {
            return CharMatcher.isNot(this.match);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            table.set(this.match);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.is('" + showCharacter(this.match) + "')";
        }
    }
    
    private static final class IsNot extends FastMatcher
    {
        private final char match;
        
        IsNot(final char match) {
            this.match = match;
        }
        
        @Override
        public boolean matches(final char c) {
            return c != this.match;
        }
        
        @Override
        public CharMatcher and(final CharMatcher other) {
            return other.matches(this.match) ? super.and(other) : other;
        }
        
        @Override
        public CharMatcher or(final CharMatcher other) {
            return other.matches(this.match) ? CharMatcher.any() : this;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            table.set(0, this.match);
            table.set(this.match + '\u0001', 65536);
        }
        
        @Override
        public CharMatcher negate() {
            return CharMatcher.is(this.match);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.isNot('" + showCharacter(this.match) + "')";
        }
    }
    
    private static final class IsEither extends FastMatcher
    {
        private final char match1;
        private final char match2;
        
        IsEither(final char match1, final char match2) {
            this.match1 = match1;
            this.match2 = match2;
        }
        
        @Override
        public boolean matches(final char c) {
            return c == this.match1 || c == this.match2;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            table.set(this.match1);
            table.set(this.match2);
        }
        
        @Override
        public String toString() {
            return "CharMatcher.anyOf(\"" + showCharacter(this.match1) + showCharacter(this.match2) + "\")";
        }
    }
    
    private static final class AnyOf extends CharMatcher
    {
        private final char[] chars;
        
        public AnyOf(final CharSequence chars) {
            Arrays.sort(this.chars = chars.toString().toCharArray());
        }
        
        @Override
        public boolean matches(final char c) {
            return Arrays.binarySearch(this.chars, c) >= 0;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            for (final char c : this.chars) {
                table.set(c);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
            for (final char c : this.chars) {
                description.append(showCharacter(c));
            }
            description.append("\")");
            return description.toString();
        }
    }
    
    private static final class InRange extends FastMatcher
    {
        private final char startInclusive;
        private final char endInclusive;
        
        InRange(final char startInclusive, final char endInclusive) {
            Preconditions.checkArgument(endInclusive >= startInclusive);
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }
        
        @Override
        public boolean matches(final char c) {
            return this.startInclusive <= c && c <= this.endInclusive;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet table) {
            table.set(this.startInclusive, this.endInclusive + '\u0001');
        }
        
        @Override
        public String toString() {
            return "CharMatcher.inRange('" + showCharacter(this.startInclusive) + "', '" + showCharacter(this.endInclusive) + "')";
        }
    }
    
    private static final class ForPredicate extends CharMatcher
    {
        private final Predicate<? super Character> predicate;
        
        ForPredicate(final Predicate<? super Character> predicate) {
            this.predicate = Preconditions.checkNotNull(predicate);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.predicate.apply(c);
        }
        
        @Override
        public boolean apply(final Character character) {
            return this.predicate.apply(Preconditions.checkNotNull(character));
        }
        
        @Override
        public String toString() {
            return "CharMatcher.forPredicate(" + this.predicate + ")";
        }
    }
}
