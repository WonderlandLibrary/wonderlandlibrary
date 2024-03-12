// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.matcher;

import org.apache.commons.lang3.CharSequenceUtils;

public interface StringMatcher
{
    default StringMatcher andThen(final StringMatcher stringMatcher) {
        return StringMatcherFactory.INSTANCE.andMatcher(this, stringMatcher);
    }
    
    default int isMatch(final char[] buffer, final int pos) {
        return this.isMatch(buffer, pos, 0, buffer.length);
    }
    
    int isMatch(final char[] p0, final int p1, final int p2, final int p3);
    
    default int isMatch(final CharSequence buffer, final int pos) {
        return this.isMatch(buffer, pos, 0, buffer.length());
    }
    
    default int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
        return this.isMatch(CharSequenceUtils.toCharArray(buffer), start, bufferEnd, bufferEnd);
    }
    
    default int size() {
        return 0;
    }
}
