// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public abstract class UnicodeEscaper extends Escaper
{
    private static final int DEST_PAD = 32;
    
    protected UnicodeEscaper() {
    }
    
    @CheckForNull
    protected abstract char[] escape(final int p0);
    
    @Override
    public String escape(final String string) {
        Preconditions.checkNotNull(string);
        final int end = string.length();
        final int index = this.nextEscapeIndex(string, 0, end);
        return (index == end) ? string : this.escapeSlow(string, index);
    }
    
    protected int nextEscapeIndex(final CharSequence csq, final int start, final int end) {
        int index;
        int cp;
        for (index = start; index < end; index += (Character.isSupplementaryCodePoint(cp) ? 2 : 1)) {
            cp = codePointAt(csq, index, end);
            if (cp < 0) {
                break;
            }
            if (this.escape(cp) != null) {
                break;
            }
        }
        return index;
    }
    
    protected final String escapeSlow(final String s, int index) {
        final int end = s.length();
        char[] dest = Platform.charBufferFromThreadLocal();
        int destIndex = 0;
        int unescapedChunkStart = 0;
        while (index < end) {
            final int cp = codePointAt(s, index, end);
            if (cp < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            final char[] escaped = this.escape(cp);
            final int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
            if (escaped != null) {
                final int charsSkipped = index - unescapedChunkStart;
                final int sizeNeeded = destIndex + charsSkipped + escaped.length;
                if (dest.length < sizeNeeded) {
                    final int destLength = sizeNeeded + (end - index) + 32;
                    dest = growBuffer(dest, destIndex, destLength);
                }
                if (charsSkipped > 0) {
                    s.getChars(unescapedChunkStart, index, dest, destIndex);
                    destIndex += charsSkipped;
                }
                if (escaped.length > 0) {
                    System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
                    destIndex += escaped.length;
                }
                unescapedChunkStart = nextIndex;
            }
            index = this.nextEscapeIndex(s, nextIndex, end);
        }
        final int charsSkipped2 = end - unescapedChunkStart;
        if (charsSkipped2 > 0) {
            final int endIndex = destIndex + charsSkipped2;
            if (dest.length < endIndex) {
                dest = growBuffer(dest, destIndex, endIndex);
            }
            s.getChars(unescapedChunkStart, end, dest, destIndex);
            destIndex = endIndex;
        }
        return new String(dest, 0, destIndex);
    }
    
    protected static int codePointAt(final CharSequence seq, int index, final int end) {
        Preconditions.checkNotNull(seq);
        if (index >= end) {
            throw new IndexOutOfBoundsException("Index exceeds specified range");
        }
        final char c1 = seq.charAt(index++);
        if (c1 < '\ud800' || c1 > '\udfff') {
            return c1;
        }
        if (c1 > '\udbff') {
            final char i = c1;
            final int j = index - 1;
            final String value = String.valueOf(seq);
            throw new IllegalArgumentException(new StringBuilder(88 + String.valueOf(value).length()).append("Unexpected low surrogate character '").append(c1).append("' with value ").append((int)i).append(" at index ").append(j).append(" in '").append(value).append("'").toString());
        }
        if (index == end) {
            return -c1;
        }
        final char c2 = seq.charAt(index);
        if (Character.isLowSurrogate(c2)) {
            return Character.toCodePoint(c1, c2);
        }
        final char k = c2;
        final int l = index;
        final String value2 = String.valueOf(seq);
        throw new IllegalArgumentException(new StringBuilder(89 + String.valueOf(value2).length()).append("Expected low surrogate but got char '").append(c2).append("' with value ").append((int)k).append(" at index ").append(l).append(" in '").append(value2).append("'").toString());
    }
    
    private static char[] growBuffer(final char[] dest, final int index, final int size) {
        if (size < 0) {
            throw new AssertionError((Object)"Cannot increase internal buffer any further");
        }
        final char[] copy = new char[size];
        if (index > 0) {
            System.arraycopy(dest, 0, copy, 0, index);
        }
        return copy;
    }
}
