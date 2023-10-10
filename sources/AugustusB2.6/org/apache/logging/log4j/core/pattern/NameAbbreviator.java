// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public abstract class NameAbbreviator
{
    private static final NameAbbreviator DEFAULT;
    
    public static NameAbbreviator getAbbreviator(final String pattern) {
        if (pattern.length() <= 0) {
            return NameAbbreviator.DEFAULT;
        }
        final String trimmed = pattern.trim();
        if (trimmed.isEmpty()) {
            return NameAbbreviator.DEFAULT;
        }
        boolean isNegativeNumber;
        String number;
        if (trimmed.length() > 1 && trimmed.charAt(0) == '-') {
            isNegativeNumber = true;
            number = trimmed.substring(1);
        }
        else {
            isNegativeNumber = false;
            number = trimmed;
        }
        int i;
        for (i = 0; i < number.length() && number.charAt(i) >= '0' && number.charAt(i) <= '9'; ++i) {}
        if (i == number.length()) {
            return new MaxElementAbbreviator(Integer.parseInt(number), isNegativeNumber ? MaxElementAbbreviator.Strategy.DROP : MaxElementAbbreviator.Strategy.RETAIN);
        }
        final ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<PatternAbbreviatorFragment>(5);
        for (int pos = 0; pos < trimmed.length() && pos >= 0; ++pos) {
            int ellipsisPos = pos;
            int charCount;
            if (trimmed.charAt(pos) == '*') {
                charCount = Integer.MAX_VALUE;
                ++ellipsisPos;
            }
            else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
                charCount = trimmed.charAt(pos) - '0';
                ++ellipsisPos;
            }
            else {
                charCount = 0;
            }
            char ellipsis = '\0';
            if (ellipsisPos < trimmed.length()) {
                ellipsis = trimmed.charAt(ellipsisPos);
                if (ellipsis == '.') {
                    ellipsis = '\0';
                }
            }
            fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
            pos = trimmed.indexOf(46, pos);
            if (pos == -1) {
                break;
            }
        }
        return new PatternAbbreviator(fragments);
    }
    
    public static NameAbbreviator getDefaultAbbreviator() {
        return NameAbbreviator.DEFAULT;
    }
    
    public abstract void abbreviate(final String original, final StringBuilder destination);
    
    static {
        DEFAULT = new NOPAbbreviator();
    }
    
    private static class NOPAbbreviator extends NameAbbreviator
    {
        public NOPAbbreviator() {
        }
        
        @Override
        public void abbreviate(final String original, final StringBuilder destination) {
            destination.append(original);
        }
    }
    
    private static class MaxElementAbbreviator extends NameAbbreviator
    {
        private final int count;
        private final Strategy strategy;
        
        public MaxElementAbbreviator(final int count, final Strategy strategy) {
            this.count = Math.max(count, strategy.minCount);
            this.strategy = strategy;
        }
        
        @Override
        public void abbreviate(final String original, final StringBuilder destination) {
            this.strategy.abbreviate(this.count, original, destination);
        }
        
        private enum Strategy
        {
            DROP(0) {
                @Override
                void abbreviate(final int count, final String original, final StringBuilder destination) {
                    int start = 0;
                    for (int i = 0; i < count; ++i) {
                        final int nextStart = original.indexOf(46, start);
                        if (nextStart == -1) {
                            destination.append(original);
                            return;
                        }
                        start = nextStart + 1;
                    }
                    destination.append(original, start, original.length());
                }
            }, 
            RETAIN(1) {
                @Override
                void abbreviate(final int count, final String original, final StringBuilder destination) {
                    int end = original.length() - 1;
                    for (int i = count; i > 0; --i) {
                        end = original.lastIndexOf(46, end - 1);
                        if (end == -1) {
                            destination.append(original);
                            return;
                        }
                    }
                    destination.append(original, end + 1, original.length());
                }
            };
            
            final int minCount;
            
            private Strategy(final int minCount) {
                this.minCount = minCount;
            }
            
            abstract void abbreviate(final int count, final String original, final StringBuilder destination);
        }
    }
    
    private static final class PatternAbbreviatorFragment
    {
        private final int charCount;
        private final char ellipsis;
        
        PatternAbbreviatorFragment(final int charCount, final char ellipsis) {
            this.charCount = charCount;
            this.ellipsis = ellipsis;
        }
        
        int abbreviate(final String input, final int inputIndex, final StringBuilder buf) {
            final int nextDot = input.indexOf(46, inputIndex);
            if (nextDot < 0) {
                buf.append(input, inputIndex, input.length());
                return nextDot;
            }
            if (nextDot - inputIndex > this.charCount) {
                buf.append(input, inputIndex, inputIndex + this.charCount);
                if (this.ellipsis != '\0') {
                    buf.append(this.ellipsis);
                }
                buf.append('.');
            }
            else {
                buf.append(input, inputIndex, nextDot + 1);
            }
            return nextDot + 1;
        }
    }
    
    private static final class PatternAbbreviator extends NameAbbreviator
    {
        private final PatternAbbreviatorFragment[] fragments;
        
        PatternAbbreviator(final List<PatternAbbreviatorFragment> fragments) {
            if (fragments.isEmpty()) {
                throw new IllegalArgumentException("fragments must have at least one element");
            }
            this.fragments = fragments.toArray(new PatternAbbreviatorFragment[0]);
        }
        
        @Override
        public void abbreviate(final String original, final StringBuilder destination) {
            for (int originalIndex = 0, iteration = 0, originalLength = original.length(); originalIndex >= 0 && originalIndex < originalLength; originalIndex = this.fragment(iteration++).abbreviate(original, originalIndex, destination)) {}
        }
        
        PatternAbbreviatorFragment fragment(final int index) {
            return this.fragments[Math.min(index, this.fragments.length - 1)];
        }
    }
}
