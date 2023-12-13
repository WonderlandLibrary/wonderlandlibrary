/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import java.io.Serializable;
import java.util.Comparator;

public final class IntComparators {
    public static final IntComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final IntComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private IntComparators() {
    }

    public static IntComparator oppositeComparator(IntComparator c2) {
        if (c2 instanceof OppositeComparator) {
            return ((OppositeComparator)c2).comparator;
        }
        return new OppositeComparator(c2);
    }

    public static IntComparator asIntComparator(final Comparator<? super Integer> c2) {
        if (c2 == null || c2 instanceof IntComparator) {
            return (IntComparator)c2;
        }
        return new IntComparator(){

            @Override
            public int compare(int x2, int y2) {
                return c2.compare(x2, y2);
            }

            @Override
            public int compare(Integer x2, Integer y2) {
                return c2.compare(x2, y2);
            }
        };
    }

    protected static class OppositeComparator
    implements IntComparator,
    Serializable {
        private static final long serialVersionUID = 1L;
        final IntComparator comparator;

        protected OppositeComparator(IntComparator c2) {
            this.comparator = c2;
        }

        @Override
        public final int compare(int a2, int b2) {
            return this.comparator.compare(b2, a2);
        }

        @Override
        public final IntComparator reversed() {
            return this.comparator;
        }
    }

    protected static class NaturalImplicitComparator
    implements IntComparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected NaturalImplicitComparator() {
        }

        @Override
        public final int compare(int a2, int b2) {
            return Integer.compare(a2, b2);
        }

        @Override
        public IntComparator reversed() {
            return OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return NATURAL_COMPARATOR;
        }
    }

    protected static class OppositeImplicitComparator
    implements IntComparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected OppositeImplicitComparator() {
        }

        @Override
        public final int compare(int a2, int b2) {
            return -Integer.compare(a2, b2);
        }

        @Override
        public IntComparator reversed() {
            return NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return OPPOSITE_COMPARATOR;
        }
    }
}

