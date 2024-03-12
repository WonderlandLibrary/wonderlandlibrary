// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.matcher;

import java.util.Arrays;

abstract class AbstractStringMatcher implements StringMatcher
{
    protected AbstractStringMatcher() {
    }
    
    static final class AndStringMatcher extends AbstractStringMatcher
    {
        private final StringMatcher[] stringMatchers;
        
        AndStringMatcher(final StringMatcher... stringMatchers) {
            this.stringMatchers = stringMatchers.clone();
        }
        
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            int curStart = start;
            for (final StringMatcher stringMatcher : this.stringMatchers) {
                if (stringMatcher != null) {
                    final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                    if (len == 0) {
                        return 0;
                    }
                    total += len;
                    curStart += len;
                }
            }
            return total;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            int curStart = start;
            for (final StringMatcher stringMatcher : this.stringMatchers) {
                if (stringMatcher != null) {
                    final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                    if (len == 0) {
                        return 0;
                    }
                    total += len;
                    curStart += len;
                }
            }
            return total;
        }
        
        @Override
        public int size() {
            int total = 0;
            for (final StringMatcher stringMatcher : this.stringMatchers) {
                if (stringMatcher != null) {
                    total += stringMatcher.size();
                }
            }
            return total;
        }
    }
    
    static final class CharArrayMatcher extends AbstractStringMatcher
    {
        private final char[] chars;
        private final String string;
        
        CharArrayMatcher(final char... chars) {
            this.string = String.valueOf(chars);
            this.chars = chars.clone();
        }
        
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = this.size();
            if (start + len > bufferEnd) {
                return 0;
            }
            for (int j = start, i = 0; i < len; ++i, ++j) {
                if (this.chars[i] != buffer[j]) {
                    return 0;
                }
            }
            return len;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = this.size();
            if (start + len > bufferEnd) {
                return 0;
            }
            for (int j = start, i = 0; i < len; ++i, ++j) {
                if (this.chars[i] != buffer.charAt(j)) {
                    return 0;
                }
            }
            return len;
        }
        
        @Override
        public int size() {
            return this.chars.length;
        }
        
        @Override
        public String toString() {
            return super.toString() + "[\"" + this.string + "\"]";
        }
    }
    
    static final class CharMatcher extends AbstractStringMatcher
    {
        private final char ch;
        
        CharMatcher(final char ch) {
            this.ch = ch;
        }
        
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (this.ch == buffer[start]) ? 1 : 0;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (this.ch == buffer.charAt(start)) ? 1 : 0;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public String toString() {
            return super.toString() + "['" + this.ch + "']";
        }
    }
    
    static final class CharSetMatcher extends AbstractStringMatcher
    {
        private final char[] chars;
        
        CharSetMatcher(final char[] chars) {
            Arrays.sort(this.chars = chars.clone());
        }
        
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (Arrays.binarySearch(this.chars, buffer[start]) >= 0) ? 1 : 0;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (Arrays.binarySearch(this.chars, buffer.charAt(start)) >= 0) ? 1 : 0;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public String toString() {
            return super.toString() + Arrays.toString(this.chars);
        }
    }
    
    static final class NoneMatcher extends AbstractStringMatcher
    {
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return 0;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return 0;
        }
        
        @Override
        public int size() {
            return 0;
        }
    }
    
    static final class TrimMatcher extends AbstractStringMatcher
    {
        private static final int SPACE_INT = 32;
        
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (buffer[start] <= ' ') ? 1 : 0;
        }
        
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return (buffer.charAt(start) <= ' ') ? 1 : 0;
        }
        
        @Override
        public int size() {
            return 1;
        }
    }
}
