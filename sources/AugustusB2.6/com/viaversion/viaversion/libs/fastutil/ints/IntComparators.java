// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;
import java.util.Comparator;

public final class IntComparators
{
    public static final IntComparator NATURAL_COMPARATOR;
    public static final IntComparator OPPOSITE_COMPARATOR;
    
    private IntComparators() {
    }
    
    public static IntComparator oppositeComparator(final IntComparator c) {
        if (c instanceof OppositeComparator) {
            return ((OppositeComparator)c).comparator;
        }
        return new OppositeComparator(c);
    }
    
    public static IntComparator asIntComparator(final Comparator<? super Integer> c) {
        if (c == null || c instanceof IntComparator) {
            return (IntComparator)c;
        }
        return new IntComparator() {
            @Override
            public int compare(final int x, final int y) {
                return c.compare(x, y);
            }
            
            @Override
            public int compare(final Integer x, final Integer y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements IntComparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final int a, final int b) {
            return Integer.compare(a, b);
        }
        
        @Override
        public IntComparator reversed() {
            return IntComparators.OPPOSITE_COMPARATOR;
        }
        
        private Object readResolve() {
            return IntComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements IntComparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final int a, final int b) {
            return -Integer.compare(a, b);
        }
        
        @Override
        public IntComparator reversed() {
            return IntComparators.NATURAL_COMPARATOR;
        }
        
        private Object readResolve() {
            return IntComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements IntComparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        final IntComparator comparator;
        
        protected OppositeComparator(final IntComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final int a, final int b) {
            return this.comparator.compare(b, a);
        }
        
        @Override
        public final IntComparator reversed() {
            return this.comparator;
        }
    }
}
