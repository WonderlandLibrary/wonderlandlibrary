/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentMap;

@Beta
@GwtIncompatible
public final class Interners {
    private Interners() {
    }

    public static InternerBuilder newBuilder() {
        return new InternerBuilder();
    }

    public static <E> Interner<E> newStrongInterner() {
        return Interners.newBuilder().strong().build();
    }

    @GwtIncompatible(value="java.lang.ref.WeakReference")
    public static <E> Interner<E> newWeakInterner() {
        return Interners.newBuilder().weak().build();
    }

    public static <E> Function<E, E> asFunction(Interner<E> interner) {
        return new InternerFunction<E>(Preconditions.checkNotNull(interner));
    }

    private static class InternerFunction<E>
    implements Function<E, E> {
        private final Interner<E> interner;

        public InternerFunction(Interner<E> interner) {
            this.interner = interner;
        }

        @Override
        public E apply(E input) {
            return this.interner.intern(input);
        }

        public int hashCode() {
            return this.interner.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof InternerFunction) {
                InternerFunction that = (InternerFunction)other;
                return this.interner.equals(that.interner);
            }
            return false;
        }
    }

    @VisibleForTesting
    static final class WeakInterner<E>
    implements Interner<E> {
        @VisibleForTesting
        final MapMakerInternalMap<E, Dummy, ?, ?> map;

        private WeakInterner(MapMaker mapMaker) {
            this.map = mapMaker.weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
        }

        @Override
        public E intern(E sample) {
            Dummy sneaky;
            do {
                Object canonical;
                Object entry;
                if ((entry = this.map.getEntry(sample)) == null || (canonical = entry.getKey()) == null) continue;
                return (E)canonical;
            } while ((sneaky = this.map.putIfAbsent(sample, Dummy.VALUE)) != null);
            return sample;
        }

        private static enum Dummy {
            VALUE;

        }
    }

    @VisibleForTesting
    static final class StrongInterner<E>
    implements Interner<E> {
        @VisibleForTesting
        final ConcurrentMap<E, E> map;

        private StrongInterner(MapMaker mapMaker) {
            this.map = mapMaker.makeMap();
        }

        @Override
        public E intern(E sample) {
            E canonical = this.map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
            return canonical == null ? sample : canonical;
        }
    }

    public static class InternerBuilder {
        private final MapMaker mapMaker = new MapMaker();
        private boolean strong = true;

        private InternerBuilder() {
        }

        public InternerBuilder strong() {
            this.strong = true;
            return this;
        }

        @GwtIncompatible(value="java.lang.ref.WeakReference")
        public InternerBuilder weak() {
            this.strong = false;
            return this;
        }

        public InternerBuilder concurrencyLevel(int concurrencyLevel) {
            this.mapMaker.concurrencyLevel(concurrencyLevel);
            return this;
        }

        public <E> Interner<E> build() {
            return this.strong ? new StrongInterner(this.mapMaker) : new WeakInterner(this.mapMaker);
        }
    }
}

