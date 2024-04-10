package info.sigmaclient.sigma.gui.clickgui.sorter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import top.fl0wowp4rty.phantomshield.annotations.Native;


public class JelloSorter {
    private JelloSorter() {
        throw new AssertionError("no instances");
    }
    public enum NaturalOrderComparator implements Comparator<Comparable<Object>> {
        INSTANCE;

        @Override
        public int compare(Comparable<Object> c1, Comparable<Object> c2) {
            return c1.compareTo(c2);
        }

        @Override
        public Comparator<Comparable<Object>> reversed() {
            return Comparator.reverseOrder();
        }
    }

    /**
     * Null-friendly comparators
     */
    final static class NullComparator<T> implements Comparator<T>, Serializable {
        private static final long serialVersionUID = -7569533591570686392L;
        private final boolean nullFirst;
        // if null, non-null Ts are considered equal
        private final Comparator<T> real;

        @SuppressWarnings("unchecked")
        NullComparator(boolean nullFirst, Comparator<? super T> real) {
            this.nullFirst = nullFirst;
            this.real = (Comparator<T>) real;
        }

        @Override
        public int compare(T a, T b) {
            if (a == null) {
                return (b == null) ? 0 : (nullFirst ? -1 : 1);
            } else if (b == null) {
                return nullFirst ? 1 : -1;
            } else {
                return (real == null) ? 0 : real.compare(a, b);
            }
        }

        @Override
        public Comparator<T> thenComparing(Comparator<? super T> other) {
            Objects.requireNonNull(other);
            return new Comparators.NullComparator<>(nullFirst, real == null ? other : real.thenComparing(other));
        }

        @Override
        public Comparator<T> reversed() {
            return new Comparators.NullComparator<>(!nullFirst, real == null ? null : real.reversed());
        }
    }

}
