/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.Serializable;
import java.util.Comparator;

public final class ObjectComparators {
    public static final Comparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final Comparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private ObjectComparators() {
    }

    public static <K> Comparator<K> oppositeComparator(Comparator<K> c2) {
        if (c2 instanceof OppositeComparator) {
            return ((OppositeComparator)c2).comparator;
        }
        return new OppositeComparator<K>(c2);
    }

    public static <K> Comparator<K> asObjectComparator(Comparator<K> c2) {
        return c2;
    }

    protected static class OppositeComparator<K>
    implements Comparator<K>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final Comparator<K> comparator;

        protected OppositeComparator(Comparator<K> c2) {
            this.comparator = c2;
        }

        @Override
        public final int compare(K a2, K b2) {
            return this.comparator.compare(b2, a2);
        }

        @Override
        public final Comparator<K> reversed() {
            return this.comparator;
        }
    }

    protected static class NaturalImplicitComparator
    implements Comparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected NaturalImplicitComparator() {
        }

        public final int compare(Object a2, Object b2) {
            return ((Comparable)a2).compareTo(b2);
        }

        public Comparator reversed() {
            return OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return NATURAL_COMPARATOR;
        }
    }

    protected static class OppositeImplicitComparator
    implements Comparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected OppositeImplicitComparator() {
        }

        public final int compare(Object a2, Object b2) {
            return ((Comparable)b2).compareTo(a2);
        }

        public Comparator reversed() {
            return NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return OPPOSITE_COMPARATOR;
        }
    }
}

