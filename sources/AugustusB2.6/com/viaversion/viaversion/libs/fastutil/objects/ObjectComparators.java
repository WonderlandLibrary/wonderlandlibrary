// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.Serializable;
import java.util.Comparator;

public final class ObjectComparators
{
    public static final Comparator NATURAL_COMPARATOR;
    public static final Comparator OPPOSITE_COMPARATOR;
    
    private ObjectComparators() {
    }
    
    public static <K> Comparator<K> oppositeComparator(final Comparator<K> c) {
        if (c instanceof OppositeComparator) {
            return (Comparator<K>)((OppositeComparator)c).comparator;
        }
        return new OppositeComparator<K>(c);
    }
    
    public static <K> Comparator<K> asObjectComparator(final Comparator<K> c) {
        return c;
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements Comparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final Object a, final Object b) {
            return ((Comparable)a).compareTo(b);
        }
        
        @Override
        public Comparator reversed() {
            return ObjectComparators.OPPOSITE_COMPARATOR;
        }
        
        private Object readResolve() {
            return ObjectComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements Comparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final Object a, final Object b) {
            return ((Comparable)b).compareTo(a);
        }
        
        @Override
        public Comparator reversed() {
            return ObjectComparators.NATURAL_COMPARATOR;
        }
        
        private Object readResolve() {
            return ObjectComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator<K> implements Comparator<K>, Serializable
    {
        private static final long serialVersionUID = 1L;
        final Comparator<K> comparator;
        
        protected OppositeComparator(final Comparator<K> c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final K a, final K b) {
            return this.comparator.compare(b, a);
        }
        
        @Override
        public final Comparator<K> reversed() {
            return this.comparator;
        }
    }
}
