// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Equivalence;
import java.util.concurrent.ConcurrentMap;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.base.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class Interners
{
    private Interners() {
    }
    
    public static InternerBuilder newBuilder() {
        return new InternerBuilder();
    }
    
    public static <E> Interner<E> newStrongInterner() {
        return newBuilder().strong().build();
    }
    
    @GwtIncompatible("java.lang.ref.WeakReference")
    public static <E> Interner<E> newWeakInterner() {
        return newBuilder().weak().build();
    }
    
    public static <E> Function<E, E> asFunction(final Interner<E> interner) {
        return new InternerFunction<E>(Preconditions.checkNotNull(interner));
    }
    
    public static class InternerBuilder
    {
        private final MapMaker mapMaker;
        private boolean strong;
        
        private InternerBuilder() {
            this.mapMaker = new MapMaker();
            this.strong = true;
        }
        
        public InternerBuilder strong() {
            this.strong = true;
            return this;
        }
        
        @GwtIncompatible("java.lang.ref.WeakReference")
        public InternerBuilder weak() {
            this.strong = false;
            return this;
        }
        
        public InternerBuilder concurrencyLevel(final int concurrencyLevel) {
            this.mapMaker.concurrencyLevel(concurrencyLevel);
            return this;
        }
        
        public <E> Interner<E> build() {
            return (Interner<E>)(this.strong ? new StrongInterner<E>(this.mapMaker) : new WeakInterner<E>(this.mapMaker));
        }
    }
    
    @VisibleForTesting
    static final class StrongInterner<E> implements Interner<E>
    {
        @VisibleForTesting
        final ConcurrentMap<E, E> map;
        
        private StrongInterner(final MapMaker mapMaker) {
            this.map = mapMaker.makeMap();
        }
        
        @Override
        public E intern(final E sample) {
            final E canonical = this.map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
            return (canonical == null) ? sample : canonical;
        }
    }
    
    @VisibleForTesting
    static final class WeakInterner<E> implements Interner<E>
    {
        @VisibleForTesting
        final MapMakerInternalMap<E, Dummy, ?, ?> map;
        
        private WeakInterner(final MapMaker mapMaker) {
            this.map = mapMaker.weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
        }
        
        @Override
        public E intern(final E sample) {
            while (true) {
                final MapMakerInternalMap.InternalEntry<E, Dummy, ?> entry = (MapMakerInternalMap.InternalEntry<E, Dummy, ?>)this.map.getEntry(sample);
                if (entry != null) {
                    final E canonical = entry.getKey();
                    if (canonical != null) {
                        return canonical;
                    }
                }
                final Dummy sneaky = this.map.putIfAbsent(sample, Dummy.VALUE);
                if (sneaky == null) {
                    return sample;
                }
            }
        }
        
        private enum Dummy
        {
            VALUE;
        }
    }
    
    private static class InternerFunction<E> implements Function<E, E>
    {
        private final Interner<E> interner;
        
        public InternerFunction(final Interner<E> interner) {
            this.interner = interner;
        }
        
        @Override
        public E apply(final E input) {
            return this.interner.intern(input);
        }
        
        @Override
        public int hashCode() {
            return this.interner.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (other instanceof InternerFunction) {
                final InternerFunction<?> that = (InternerFunction<?>)other;
                return this.interner.equals(that.interner);
            }
            return false;
        }
    }
}
