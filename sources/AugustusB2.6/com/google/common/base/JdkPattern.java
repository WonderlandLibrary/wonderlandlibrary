// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class JdkPattern extends CommonPattern implements Serializable
{
    private final Pattern pattern;
    private static final long serialVersionUID = 0L;
    
    JdkPattern(final Pattern pattern) {
        this.pattern = Preconditions.checkNotNull(pattern);
    }
    
    @Override
    public CommonMatcher matcher(final CharSequence t) {
        return new JdkMatcher(this.pattern.matcher(t));
    }
    
    @Override
    public String pattern() {
        return this.pattern.pattern();
    }
    
    @Override
    public int flags() {
        return this.pattern.flags();
    }
    
    @Override
    public String toString() {
        return this.pattern.toString();
    }
    
    private static final class JdkMatcher extends CommonMatcher
    {
        final Matcher matcher;
        
        JdkMatcher(final Matcher matcher) {
            this.matcher = Preconditions.checkNotNull(matcher);
        }
        
        @Override
        public boolean matches() {
            return this.matcher.matches();
        }
        
        @Override
        public boolean find() {
            return this.matcher.find();
        }
        
        @Override
        public boolean find(final int index) {
            return this.matcher.find(index);
        }
        
        @Override
        public String replaceAll(final String replacement) {
            return this.matcher.replaceAll(replacement);
        }
        
        @Override
        public int end() {
            return this.matcher.end();
        }
        
        @Override
        public int start() {
            return this.matcher.start();
        }
    }
}
