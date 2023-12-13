/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentMap;

@Beta
public final class Interners {
    private Interners() {
    }

    public static <E> Interner<E> newStrongInterner() {
        final ConcurrentMap map = new MapMaker().makeMap();
        return new Interner<E>(){

            @Override
            public E intern(E sample) {
                Object canonical = map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
                return canonical == null ? sample : canonical;
            }
        };
    }

    @GwtIncompatible(value="java.lang.ref.WeakReference")
    public static <E> Interner<E> newWeakInterner() {
        return new WeakInterner();
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

    private static class WeakInterner<E>
    implements Interner<E> {
        private final MapMakerInternalMap<E, Dummy> map = ((MapMaker)new MapMaker().weakKeys().keyEquivalence((Equivalence)Equivalence.equals())).makeCustomMap();

        private WeakInterner() {
        }

        @Override
        public E intern(E sample) {
            Dummy sneaky;
            do {
                E canonical;
                MapMakerInternalMap.ReferenceEntry<E, Dummy> entry;
                if ((entry = this.map.getEntry(sample)) == null || (canonical = entry.getKey()) == null) continue;
                return canonical;
            } while ((sneaky = this.map.putIfAbsent(sample, Dummy.VALUE)) != null);
            return sample;
        }

        private static enum Dummy {
            VALUE;

        }
    }
}

