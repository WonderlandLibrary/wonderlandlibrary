// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public final class StringMatcherFactory
{
    private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER;
    private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER;
    public static final StringMatcherFactory INSTANCE;
    private static final AbstractStringMatcher.NoneMatcher NONE_MATCHER;
    private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER;
    private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER;
    private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER;
    private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER;
    private static final AbstractStringMatcher.CharMatcher TAB_MATCHER;
    private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER;
    
    private StringMatcherFactory() {
    }
    
    public StringMatcher andMatcher(final StringMatcher... stringMatchers) {
        final int len = ArrayUtils.getLength((Object)stringMatchers);
        if (len == 0) {
            return StringMatcherFactory.NONE_MATCHER;
        }
        if (len == 1) {
            return stringMatchers[0];
        }
        return new AbstractStringMatcher.AndStringMatcher(stringMatchers);
    }
    
    public StringMatcher charMatcher(final char ch) {
        return new AbstractStringMatcher.CharMatcher(ch);
    }
    
    public StringMatcher charSetMatcher(final char... chars) {
        final int len = ArrayUtils.getLength((Object)chars);
        if (len == 0) {
            return StringMatcherFactory.NONE_MATCHER;
        }
        if (len == 1) {
            return new AbstractStringMatcher.CharMatcher(chars[0]);
        }
        return new AbstractStringMatcher.CharSetMatcher(chars);
    }
    
    public StringMatcher charSetMatcher(final String chars) {
        final int len = StringUtils.length((CharSequence)chars);
        if (len == 0) {
            return StringMatcherFactory.NONE_MATCHER;
        }
        if (len == 1) {
            return new AbstractStringMatcher.CharMatcher(chars.charAt(0));
        }
        return new AbstractStringMatcher.CharSetMatcher(chars.toCharArray());
    }
    
    public StringMatcher commaMatcher() {
        return StringMatcherFactory.COMMA_MATCHER;
    }
    
    public StringMatcher doubleQuoteMatcher() {
        return StringMatcherFactory.DOUBLE_QUOTE_MATCHER;
    }
    
    public StringMatcher noneMatcher() {
        return StringMatcherFactory.NONE_MATCHER;
    }
    
    public StringMatcher quoteMatcher() {
        return StringMatcherFactory.QUOTE_MATCHER;
    }
    
    public StringMatcher singleQuoteMatcher() {
        return StringMatcherFactory.SINGLE_QUOTE_MATCHER;
    }
    
    public StringMatcher spaceMatcher() {
        return StringMatcherFactory.SPACE_MATCHER;
    }
    
    public StringMatcher splitMatcher() {
        return StringMatcherFactory.SPLIT_MATCHER;
    }
    
    public StringMatcher stringMatcher(final char... chars) {
        final int length = ArrayUtils.getLength((Object)chars);
        return (length == 0) ? StringMatcherFactory.NONE_MATCHER : ((length == 1) ? new AbstractStringMatcher.CharMatcher(chars[0]) : new AbstractStringMatcher.CharArrayMatcher(chars));
    }
    
    public StringMatcher stringMatcher(final String str) {
        return StringUtils.isEmpty((CharSequence)str) ? StringMatcherFactory.NONE_MATCHER : this.stringMatcher(str.toCharArray());
    }
    
    public StringMatcher tabMatcher() {
        return StringMatcherFactory.TAB_MATCHER;
    }
    
    public StringMatcher trimMatcher() {
        return StringMatcherFactory.TRIM_MATCHER;
    }
    
    static {
        COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');
        DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('\"');
        INSTANCE = new StringMatcherFactory();
        NONE_MATCHER = new AbstractStringMatcher.NoneMatcher();
        QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher("'\"".toCharArray());
        SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher('\'');
        SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');
        SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(" \t\n\r\f".toCharArray());
        TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');
        TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();
    }
}
