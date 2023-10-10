// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;
import java.util.function.IntConsumer;
import com.google.j2objc.annotations.Weak;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CollectSpliterators
{
    private CollectSpliterators() {
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function) {
        return indexed(size, extraCharacteristics, function, null);
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function, @CheckForNull final Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((extraCharacteristics & 0x4) != 0x0);
        }
        class WithCharacteristics implements Spliterator<T>
        {
            private final OfInt delegate = IntStream.range(0, size).spliterator();
            final /* synthetic */ Comparator val$comparator;
            
            WithCharacteristics(final OfInt delegate, final OfInt val$comparator) {
                this.val$comparator = (Comparator)val$comparator;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                return this.delegate.tryAdvance(i -> action.accept((Object)function.apply(i)));
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> action) {
                this.delegate.forEachRemaining(i -> action.accept((Object)function.apply(i)));
            }
            
            @CheckForNull
            @Override
            public Spliterator<T> trySplit() {
                final OfInt split = this.delegate.trySplit();
                return (split == null) ? null : new WithCharacteristics(split, function, extraCharacteristics, this.val$comparator);
            }
            
            @Override
            public long estimateSize() {
                return this.delegate.estimateSize();
            }
            
            @Override
            public int characteristics() {
                return 0x4050 | extraCharacteristics;
            }
            
            @CheckForNull
            @Override
            public Comparator<? super T> getComparator() {
                if (this.hasCharacteristics(4)) {
                    return (Comparator<? super T>)this.val$comparator;
                }
                throw new IllegalStateException();
            }
        }
        return new WithCharacteristics();
    }
    
    static <InElementT, OutElementT> Spliterator<OutElementT> map(final Spliterator<InElementT> fromSpliterator, final Function<? super InElementT, ? extends OutElementT> function) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new Spliterator<OutElementT>() {
            @Override
            public boolean tryAdvance(final Consumer<? super OutElementT> action) {
                return fromSpliterator.tryAdvance(fromElement -> action.accept((Object)function.apply(fromElement)));
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super OutElementT> action) {
                fromSpliterator.forEachRemaining(fromElement -> action.accept((Object)function.apply(fromElement)));
            }
            
            @CheckForNull
            @Override
            public Spliterator<OutElementT> trySplit() {
                final Spliterator<InElementT> fromSplit = fromSpliterator.trySplit();
                return (Spliterator<OutElementT>)((fromSplit != null) ? CollectSpliterators.map((Spliterator<Object>)fromSplit, (Function<? super Object, ?>)function) : null);
            }
            
            @Override
            public long estimateSize() {
                return fromSpliterator.estimateSize();
            }
            
            @Override
            public int characteristics() {
                return fromSpliterator.characteristics() & 0xFFFFFEFA;
            }
        };
    }
    
    static <T> Spliterator<T> filter(final Spliterator<T> fromSpliterator, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(predicate);
        class Splitr implements Spliterator<T>, Consumer<T>
        {
            @CheckForNull
            T holder;
            
            Splitr() {
                this.holder = null;
            }
            
            @Override
            public void accept(@ParametricNullness final T t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                while (fromSpliterator.tryAdvance(this)) {
                    try {
                        final T next = NullnessCasts.uncheckedCastNullableTToT(this.holder);
                        if (predicate.test(next)) {
                            action.accept((Object)next);
                            return true;
                        }
                        continue;
                    }
                    finally {
                        this.holder = null;
                    }
                }
                return false;
            }
            
            @CheckForNull
            @Override
            public Spliterator<T> trySplit() {
                final Spliterator<T> fromSplit = fromSpliterator.trySplit();
                return (Spliterator<T>)((fromSplit == null) ? null : CollectSpliterators.filter((Spliterator<Object>)fromSplit, predicate));
            }
            
            @Override
            public long estimateSize() {
                return fromSpliterator.estimateSize() / 2L;
            }
            
            @CheckForNull
            @Override
            public Comparator<? super T> getComparator() {
                return (Comparator<? super T>)fromSpliterator.getComparator();
            }
            
            @Override
            public int characteristics() {
                return fromSpliterator.characteristics() & 0x115;
            }
        }
        return new Splitr();
    }
    
    static <InElementT, OutElementT> Spliterator<OutElementT> flatMap(final Spliterator<InElementT> fromSpliterator, final Function<? super InElementT, Spliterator<OutElementT>> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, (Object)"flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, (Object)"flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return (Spliterator<OutElementT>)new FlatMapSpliteratorOfObject(null, (Spliterator<Object>)fromSpliterator, (Function<? super Object, Spliterator<Object>>)function, topCharacteristics, topSize);
    }
    
    static <InElementT> Spliterator.OfInt flatMapToInt(final Spliterator<InElementT> fromSpliterator, final Function<? super InElementT, Spliterator.OfInt> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, (Object)"flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, (Object)"flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfInt<Object>(null, fromSpliterator, function, topCharacteristics, topSize);
    }
    
    static <InElementT> Spliterator.OfLong flatMapToLong(final Spliterator<InElementT> fromSpliterator, final Function<? super InElementT, Spliterator.OfLong> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, (Object)"flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, (Object)"flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfLong<Object>(null, fromSpliterator, function, topCharacteristics, topSize);
    }
    
    static <InElementT> Spliterator.OfDouble flatMapToDouble(final Spliterator<InElementT> fromSpliterator, final Function<? super InElementT, Spliterator.OfDouble> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, (Object)"flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, (Object)"flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new FlatMapSpliteratorOfDouble<Object>(null, fromSpliterator, function, topCharacteristics, topSize);
    }
    
    abstract static class FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT extends Spliterator<OutElementT>> implements Spliterator<OutElementT>
    {
        @CheckForNull
        @Weak
        OutSpliteratorT prefix;
        final Spliterator<InElementT> from;
        final Function<? super InElementT, OutSpliteratorT> function;
        final Factory<InElementT, OutSpliteratorT> factory;
        int characteristics;
        long estimatedSize;
        
        FlatMapSpliterator(@CheckForNull final OutSpliteratorT prefix, final Spliterator<InElementT> from, final Function<? super InElementT, OutSpliteratorT> function, final Factory<InElementT, OutSpliteratorT> factory, final int characteristics, final long estimatedSize) {
            this.prefix = prefix;
            this.from = from;
            this.function = function;
            this.factory = factory;
            this.characteristics = characteristics;
            this.estimatedSize = estimatedSize;
        }
        
        @Override
        public final boolean tryAdvance(final Consumer<? super OutElementT> action) {
            while (this.prefix == null || !this.prefix.tryAdvance(action)) {
                this.prefix = null;
                if (!this.from.tryAdvance(fromElement -> this.prefix = this.function.apply((Object)fromElement))) {
                    return false;
                }
            }
            if (this.estimatedSize != Long.MAX_VALUE) {
                --this.estimatedSize;
            }
            return true;
        }
        
        @Override
        public final void forEachRemaining(final Consumer<? super OutElementT> action) {
            if (this.prefix != null) {
                this.prefix.forEachRemaining(action);
                this.prefix = null;
            }
            final Spliterator<OutElementT> elements;
            this.from.forEachRemaining(fromElement -> {
                elements = this.function.apply((Object)fromElement);
                if (elements != null) {
                    elements.forEachRemaining(action);
                }
                return;
            });
            this.estimatedSize = 0L;
        }
        
        @CheckForNull
        @Override
        public final OutSpliteratorT trySplit() {
            final Spliterator<InElementT> fromSplit = this.from.trySplit();
            if (fromSplit != null) {
                final int splitCharacteristics = this.characteristics & 0xFFFFFFBF;
                long estSplitSize = this.estimateSize();
                if (estSplitSize < Long.MAX_VALUE) {
                    estSplitSize /= 2L;
                    this.estimatedSize -= estSplitSize;
                    this.characteristics = splitCharacteristics;
                }
                final OutSpliteratorT result = this.factory.newFlatMapSpliterator(this.prefix, fromSplit, this.function, splitCharacteristics, estSplitSize);
                this.prefix = null;
                return result;
            }
            if (this.prefix != null) {
                final OutSpliteratorT result2 = this.prefix;
                this.prefix = null;
                return result2;
            }
            return null;
        }
        
        @Override
        public final long estimateSize() {
            if (this.prefix != null) {
                this.estimatedSize = Math.max(this.estimatedSize, this.prefix.estimateSize());
            }
            return Math.max(this.estimatedSize, 0L);
        }
        
        @Override
        public final int characteristics() {
            return this.characteristics;
        }
        
        @FunctionalInterface
        interface Factory<InElementT, OutSpliteratorT extends Spliterator<?>>
        {
            OutSpliteratorT newFlatMapSpliterator(@CheckForNull final OutSpliteratorT p0, final Spliterator<InElementT> p1, final Function<? super InElementT, OutSpliteratorT> p2, final int p3, final long p4);
        }
    }
    
    static final class FlatMapSpliteratorOfObject<InElementT, OutElementT> extends FlatMapSpliterator<InElementT, OutElementT, Spliterator<OutElementT>>
    {
        FlatMapSpliteratorOfObject(@CheckForNull final Spliterator<OutElementT> prefix, final Spliterator<InElementT> from, final Function<? super InElementT, Spliterator<OutElementT>> function, final int characteristics, final long estimatedSize) {
            super(prefix, (Spliterator<Object>)from, (Function<? super Object, Spliterator<OutElementT>>)function, FlatMapSpliteratorOfObject::new, characteristics, estimatedSize);
        }
    }
    
    abstract static class FlatMapSpliteratorOfPrimitive<InElementT, OutElementT, OutConsumerT, OutSpliteratorT extends Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT>> extends FlatMapSpliterator<InElementT, OutElementT, OutSpliteratorT> implements Spliterator.OfPrimitive<OutElementT, OutConsumerT, OutSpliteratorT>
    {
        FlatMapSpliteratorOfPrimitive(@CheckForNull final OutSpliteratorT prefix, final Spliterator<InElementT> from, final Function<? super InElementT, OutSpliteratorT> function, final Factory<InElementT, OutSpliteratorT> factory, final int characteristics, final long estimatedSize) {
            super(prefix, from, function, factory, characteristics, estimatedSize);
        }
        
        @Override
        public final boolean tryAdvance(final OutConsumerT action) {
            while (this.prefix == null || !((Spliterator.OfPrimitive)this.prefix).tryAdvance(action)) {
                this.prefix = null;
                if (!this.from.tryAdvance(fromElement -> this.prefix = this.function.apply((Object)fromElement))) {
                    return false;
                }
            }
            if (this.estimatedSize != Long.MAX_VALUE) {
                --this.estimatedSize;
            }
            return true;
        }
        
        @Override
        public final void forEachRemaining(final OutConsumerT action) {
            if (this.prefix != null) {
                ((Spliterator.OfPrimitive)this.prefix).forEachRemaining(action);
                this.prefix = null;
            }
            final OutSpliteratorT elements;
            this.from.forEachRemaining(fromElement -> {
                elements = (OutSpliteratorT)this.function.apply((Object)fromElement);
                if (elements != null) {
                    ((Spliterator.OfPrimitive<T, OutConsumerT, T_SPLITR>)elements).forEachRemaining(action);
                }
                return;
            });
            this.estimatedSize = 0L;
        }
    }
    
    static final class FlatMapSpliteratorOfInt<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt
    {
        FlatMapSpliteratorOfInt(@CheckForNull final Spliterator.OfInt prefix, final Spliterator<InElementT> from, final Function<? super InElementT, Spliterator.OfInt> function, final int characteristics, final long estimatedSize) {
            super(prefix, (Spliterator<Object>)from, (Function<? super Object, Spliterator.OfInt>)function, FlatMapSpliteratorOfInt::new, characteristics, estimatedSize);
        }
    }
    
    static final class FlatMapSpliteratorOfLong<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong
    {
        FlatMapSpliteratorOfLong(@CheckForNull final Spliterator.OfLong prefix, final Spliterator<InElementT> from, final Function<? super InElementT, Spliterator.OfLong> function, final int characteristics, final long estimatedSize) {
            super(prefix, (Spliterator<Object>)from, (Function<? super Object, Spliterator.OfLong>)function, FlatMapSpliteratorOfLong::new, characteristics, estimatedSize);
        }
    }
    
    static final class FlatMapSpliteratorOfDouble<InElementT> extends FlatMapSpliteratorOfPrimitive<InElementT, Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble
    {
        FlatMapSpliteratorOfDouble(@CheckForNull final Spliterator.OfDouble prefix, final Spliterator<InElementT> from, final Function<? super InElementT, Spliterator.OfDouble> function, final int characteristics, final long estimatedSize) {
            super(prefix, (Spliterator<Object>)from, (Function<? super Object, Spliterator.OfDouble>)function, FlatMapSpliteratorOfDouble::new, characteristics, estimatedSize);
        }
    }
}
