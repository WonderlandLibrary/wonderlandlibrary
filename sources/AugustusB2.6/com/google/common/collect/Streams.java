// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import javax.annotation.CheckForNull;
import java.util.function.BiConsumer;
import java.util.Objects;
import java.util.function.Consumer;
import com.google.common.base.Preconditions;
import java.util.function.BiFunction;
import com.google.common.math.LongMath;
import java.util.Spliterator;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.OptionalDouble;
import java.util.stream.LongStream;
import java.util.OptionalLong;
import java.util.stream.IntStream;
import java.util.OptionalInt;
import com.google.errorprone.annotations.InlineMeValidationDisabled;
import com.google.common.base.Optional;
import java.util.Spliterators;
import java.util.Iterator;
import com.google.errorprone.annotations.InlineMe;
import com.google.common.annotations.Beta;
import java.util.stream.StreamSupport;
import java.util.Collection;
import java.util.stream.Stream;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Streams
{
    public static <T> Stream<T> stream(final Iterable<T> iterable) {
        return (iterable instanceof Collection) ? ((Collection)iterable).stream() : StreamSupport.stream(iterable.spliterator(), false);
    }
    
    @Deprecated
    @Beta
    @InlineMe(replacement = "collection.stream()")
    public static <T> Stream<T> stream(final Collection<T> collection) {
        return collection.stream();
    }
    
    @Beta
    public static <T> Stream<T> stream(final Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator<? extends T>)iterator, 0), false);
    }
    
    @Beta
    public static <T> Stream<T> stream(final Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }
    
    @Beta
    @InlineMe(replacement = "optional.stream()")
    @InlineMeValidationDisabled("Java 9+ API only")
    public static <T> Stream<T> stream(final java.util.Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }
    
    @Beta
    @InlineMe(replacement = "optional.stream()")
    @InlineMeValidationDisabled("Java 9+ API only")
    public static IntStream stream(final OptionalInt optional) {
        return optional.isPresent() ? IntStream.of(optional.getAsInt()) : IntStream.empty();
    }
    
    @Beta
    @InlineMe(replacement = "optional.stream()")
    @InlineMeValidationDisabled("Java 9+ API only")
    public static LongStream stream(final OptionalLong optional) {
        return optional.isPresent() ? LongStream.of(optional.getAsLong()) : LongStream.empty();
    }
    
    @Beta
    @InlineMe(replacement = "optional.stream()")
    @InlineMeValidationDisabled("Java 9+ API only")
    public static DoubleStream stream(final OptionalDouble optional) {
        return optional.isPresent() ? DoubleStream.of(optional.getAsDouble()) : DoubleStream.empty();
    }
    
    private static void closeAll(final BaseStream<?, ?>[] toClose) {
        for (final BaseStream<?, ?> stream : toClose) {
            stream.close();
        }
    }
    
    @SafeVarargs
    public static <T> Stream<T> concat(final Stream<? extends T>... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0L;
        final ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder = new ImmutableList.Builder<Spliterator<? extends T>>(streams.length);
        for (final Stream<? extends T> stream : streams) {
            isParallel |= stream.isParallel();
            final Spliterator<? extends T> splitr2 = stream.spliterator();
            splitrsBuilder.add(splitr2);
            characteristics &= splitr2.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr2.estimateSize());
        }
        return (Stream<T>)StreamSupport.stream((Spliterator<Object>)CollectSpliterators.flatMap(splitrsBuilder.build().spliterator(), splitr -> splitr, characteristics, estimatedSize), isParallel).onClose(() -> closeAll(streams));
    }
    
    public static IntStream concat(final IntStream... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0L;
        final ImmutableList.Builder<Spliterator.OfInt> splitrsBuilder = new ImmutableList.Builder<Spliterator.OfInt>(streams.length);
        for (final IntStream stream : streams) {
            isParallel |= stream.isParallel();
            final Spliterator.OfInt splitr2 = stream.spliterator();
            splitrsBuilder.add(splitr2);
            characteristics &= splitr2.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr2.estimateSize());
        }
        return ((BaseStream<T, IntStream>)StreamSupport.intStream(CollectSpliterators.flatMapToInt(splitrsBuilder.build().spliterator(), splitr -> splitr, characteristics, estimatedSize), isParallel)).onClose(() -> closeAll(streams));
    }
    
    public static LongStream concat(final LongStream... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0L;
        final ImmutableList.Builder<Spliterator.OfLong> splitrsBuilder = new ImmutableList.Builder<Spliterator.OfLong>(streams.length);
        for (final LongStream stream : streams) {
            isParallel |= stream.isParallel();
            final Spliterator.OfLong splitr2 = stream.spliterator();
            splitrsBuilder.add(splitr2);
            characteristics &= splitr2.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr2.estimateSize());
        }
        return ((BaseStream<T, LongStream>)StreamSupport.longStream(CollectSpliterators.flatMapToLong(splitrsBuilder.build().spliterator(), splitr -> splitr, characteristics, estimatedSize), isParallel)).onClose(() -> closeAll(streams));
    }
    
    public static DoubleStream concat(final DoubleStream... streams) {
        boolean isParallel = false;
        int characteristics = 336;
        long estimatedSize = 0L;
        final ImmutableList.Builder<Spliterator.OfDouble> splitrsBuilder = new ImmutableList.Builder<Spliterator.OfDouble>(streams.length);
        for (final DoubleStream stream : streams) {
            isParallel |= stream.isParallel();
            final Spliterator.OfDouble splitr2 = stream.spliterator();
            splitrsBuilder.add(splitr2);
            characteristics &= splitr2.characteristics();
            estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr2.estimateSize());
        }
        return ((BaseStream<T, DoubleStream>)StreamSupport.doubleStream(CollectSpliterators.flatMapToDouble(splitrsBuilder.build().spliterator(), splitr -> splitr, characteristics, estimatedSize), isParallel)).onClose(() -> closeAll(streams));
    }
    
    @Beta
    public static <A, B, R> Stream<R> zip(final Stream<A> streamA, final Stream<B> streamB, final BiFunction<? super A, ? super B, R> function) {
        Preconditions.checkNotNull(streamA);
        Preconditions.checkNotNull(streamB);
        Preconditions.checkNotNull(function);
        final boolean isParallel = streamA.isParallel() || streamB.isParallel();
        final Spliterator<A> splitrA = streamA.spliterator();
        final Spliterator<B> splitrB = streamB.spliterator();
        final int characteristics = splitrA.characteristics() & splitrB.characteristics() & 0x50;
        final Iterator<A> itrA = Spliterators.iterator((Spliterator<? extends A>)splitrA);
        final Iterator<B> itrB = Spliterators.iterator((Spliterator<? extends B>)splitrB);
        final Stream<Object> stream = StreamSupport.stream((Spliterator<Object>)new Spliterators.AbstractSpliterator<R>(Math.min(splitrA.estimateSize(), splitrB.estimateSize()), characteristics) {
            @Override
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (itrA.hasNext() && itrB.hasNext()) {
                    action.accept(function.apply(itrA.next(), itrB.next()));
                    return true;
                }
                return false;
            }
        }, isParallel);
        Objects.requireNonNull(streamA);
        final Stream stream2 = stream.onClose(streamA::close);
        Objects.requireNonNull(streamB);
        return stream2.onClose(streamB::close);
    }
    
    @Beta
    public static <A, B> void forEachPair(final Stream<A> streamA, final Stream<B> streamB, final BiConsumer<? super A, ? super B> consumer) {
        Preconditions.checkNotNull(consumer);
        if (streamA.isParallel() || streamB.isParallel()) {
            zip(streamA, streamB, TemporaryPair::new).forEach(pair -> consumer.accept((Object)pair.a, (Object)pair.b));
        }
        else {
            final Iterator<A> iterA = streamA.iterator();
            final Iterator<B> iterB = streamB.iterator();
            while (iterA.hasNext() && iterB.hasNext()) {
                consumer.accept((Object)iterA.next(), (Object)iterB.next());
            }
        }
    }
    
    @Beta
    public static <T, R> Stream<R> mapWithIndex(final Stream<T> stream, final FunctionWithIndex<? super T, ? extends R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator<T> fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final Iterator<T> fromIterator = Spliterators.iterator((Spliterator<? extends T>)fromSpliterator);
            final Stream<Object> stream2 = StreamSupport.stream((Spliterator<Object>)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                @Override
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.next(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
            Objects.requireNonNull(stream);
            return (Stream<R>)stream2.onClose(stream::close);
        }
        class Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T>
        {
            @CheckForNull
            T holder;
            final /* synthetic */ FunctionWithIndex val$function;
            
            Splitr(final Spliterator splitr, final Spliterator<T> index, final long p3) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$1Splitr.val$function:Lcom/google/common/collect/Streams$FunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator<TT;>;J)V
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public void accept(@ParametricNullness final T t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (this.fromSpliterator.tryAdvance(this)) {
                    try {
                        action.accept(this.val$function.apply(NullnessCasts.uncheckedCastNullableTToT(this.holder), this.index++));
                        return true;
                    }
                    finally {
                        this.holder = null;
                    }
                }
                return false;
            }
            
            @Override
            Splitr createSplit(final Spliterator<T> from, final long i) {
                return new Splitr(from, i, this.val$function);
            }
        }
        final Stream<Object> stream3 = StreamSupport.stream((Spliterator<Object>)new Splitr(fromSpliterator, 0L, function), isParallel);
        Objects.requireNonNull(stream);
        return (Stream<R>)stream3.onClose(stream::close);
    }
    
    @Beta
    public static <R> Stream<R> mapWithIndex(final IntStream stream, final IntFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfInt fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfInt fromIterator = Spliterators.iterator(fromSpliterator);
            final Stream<Object> stream2 = StreamSupport.stream((Spliterator<Object>)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                @Override
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextInt(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
            Objects.requireNonNull(stream);
            return (Stream<R>)stream2.onClose(stream::close);
        }
        class Splitr extends MapWithIndexSpliterator<OfInt, R, Splitr> implements IntConsumer, Spliterator<R>
        {
            int holder;
            final /* synthetic */ IntFunctionWithIndex val$function;
            
            Splitr(final OfInt splitr, final OfInt index, final long p3) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$2Splitr.val$function:Lcom/google/common/collect/Streams$IntFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfInt;J)V
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public void accept(final int t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((OfInt)this.fromSpliterator).tryAdvance((IntConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            Splitr createSplit(final OfInt from, final long i) {
                return new Splitr(from, i, this.val$function);
            }
        }
        final Stream<Object> stream3 = StreamSupport.stream((Spliterator<Object>)new Splitr(fromSpliterator, 0L, function), isParallel);
        Objects.requireNonNull(stream);
        return (Stream<R>)stream3.onClose(stream::close);
    }
    
    @Beta
    public static <R> Stream<R> mapWithIndex(final LongStream stream, final LongFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfLong fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfLong fromIterator = Spliterators.iterator(fromSpliterator);
            final Stream<Object> stream2 = StreamSupport.stream((Spliterator<Object>)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                @Override
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextLong(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
            Objects.requireNonNull(stream);
            return (Stream<R>)stream2.onClose(stream::close);
        }
        class Splitr extends MapWithIndexSpliterator<OfLong, R, Splitr> implements LongConsumer, Spliterator<R>
        {
            long holder;
            final /* synthetic */ LongFunctionWithIndex val$function;
            
            Splitr(final OfLong splitr, final OfLong index, final long p3) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$3Splitr.val$function:Lcom/google/common/collect/Streams$LongFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfLong;J)V
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public void accept(final long t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((OfLong)this.fromSpliterator).tryAdvance((LongConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            Splitr createSplit(final OfLong from, final long i) {
                return new Splitr(from, i, this.val$function);
            }
        }
        final Stream<Object> stream3 = StreamSupport.stream((Spliterator<Object>)new Splitr(fromSpliterator, 0L, function), isParallel);
        Objects.requireNonNull(stream);
        return (Stream<R>)stream3.onClose(stream::close);
    }
    
    @Beta
    public static <R> Stream<R> mapWithIndex(final DoubleStream stream, final DoubleFunctionWithIndex<R> function) {
        Preconditions.checkNotNull(stream);
        Preconditions.checkNotNull(function);
        final boolean isParallel = stream.isParallel();
        final Spliterator.OfDouble fromSpliterator = stream.spliterator();
        if (!fromSpliterator.hasCharacteristics(16384)) {
            final PrimitiveIterator.OfDouble fromIterator = Spliterators.iterator(fromSpliterator);
            final Stream<Object> stream2 = StreamSupport.stream((Spliterator<Object>)new Spliterators.AbstractSpliterator<R>(fromSpliterator.estimateSize(), fromSpliterator.characteristics() & 0x50) {
                long index = 0L;
                
                @Override
                public boolean tryAdvance(final Consumer<? super R> action) {
                    if (fromIterator.hasNext()) {
                        action.accept(function.apply(fromIterator.nextDouble(), this.index++));
                        return true;
                    }
                    return false;
                }
            }, isParallel);
            Objects.requireNonNull(stream);
            return (Stream<R>)stream2.onClose(stream::close);
        }
        class Splitr extends MapWithIndexSpliterator<OfDouble, R, Splitr> implements DoubleConsumer, Spliterator<R>
        {
            double holder;
            final /* synthetic */ DoubleFunctionWithIndex val$function;
            
            Splitr(final OfDouble splitr, final OfDouble index, final long p3) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        com/google/common/collect/Streams$4Splitr.val$function:Lcom/google/common/collect/Streams$DoubleFunctionWithIndex;
                //     6: aload_0         /* this */
                //     7: aload_1         /* splitr */
                //     8: lload_2         /* index */
                //     9: invokespecial   com/google/common/collect/Streams$MapWithIndexSpliterator.<init>:(Ljava/util/Spliterator;J)V
                //    12: return         
                //    Signature:
                //  (Ljava/util/Spliterator$OfDouble;J)V
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public void accept(final double t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super R> action) {
                if (((OfDouble)this.fromSpliterator).tryAdvance((DoubleConsumer)this)) {
                    action.accept(this.val$function.apply(this.holder, this.index++));
                    return true;
                }
                return false;
            }
            
            @Override
            Splitr createSplit(final OfDouble from, final long i) {
                return new Splitr(from, i, this.val$function);
            }
        }
        final Stream<Object> stream3 = StreamSupport.stream((Spliterator<Object>)new Splitr(fromSpliterator, 0L, function), isParallel);
        Objects.requireNonNull(stream);
        return (Stream<R>)stream3.onClose(stream::close);
    }
    
    @Beta
    public static <T> java.util.Optional<T> findLast(final Stream<T> stream) {
        class OptionalState
        {
            boolean set;
            @CheckForNull
            T value;
            
            OptionalState() {
                this.set = false;
                this.value = null;
            }
            
            void set(final T value) {
                this.set = true;
                this.value = value;
            }
            
            T get() {
                return Objects.requireNonNull(this.value);
            }
        }
        final OptionalState state = new OptionalState();
        final Deque<Spliterator<T>> splits = new ArrayDeque<Spliterator<T>>();
        splits.addLast(stream.spliterator());
        while (!splits.isEmpty()) {
            Spliterator<T> spliterator = splits.removeLast();
            if (spliterator.getExactSizeIfKnown() == 0L) {
                continue;
            }
            if (spliterator.hasCharacteristics(16384)) {
                while (true) {
                    final Spliterator<T> prefix = spliterator.trySplit();
                    if (prefix == null) {
                        break;
                    }
                    if (prefix.getExactSizeIfKnown() == 0L) {
                        break;
                    }
                    if (spliterator.getExactSizeIfKnown() == 0L) {
                        spliterator = prefix;
                        break;
                    }
                }
                final Spliterator<T> spliterator2 = spliterator;
                final OptionalState obj = state;
                Objects.requireNonNull(obj);
                spliterator2.forEachRemaining(obj::set);
                return java.util.Optional.of(state.get());
            }
            final Spliterator<T> prefix = spliterator.trySplit();
            if (prefix == null || prefix.getExactSizeIfKnown() == 0L) {
                final Spliterator<T> spliterator3 = spliterator;
                final OptionalState obj2 = state;
                Objects.requireNonNull(obj2);
                spliterator3.forEachRemaining(obj2::set);
                if (state.set) {
                    return java.util.Optional.of(state.get());
                }
                continue;
            }
            else {
                splits.addLast(prefix);
                splits.addLast(spliterator);
            }
        }
        return java.util.Optional.empty();
    }
    
    @Beta
    public static OptionalInt findLast(final IntStream stream) {
        final java.util.Optional<Integer> boxedLast = findLast(stream.boxed());
        return boxedLast.map((Function<? super Integer, ? extends OptionalInt>)OptionalInt::of).orElseGet(OptionalInt::empty);
    }
    
    @Beta
    public static OptionalLong findLast(final LongStream stream) {
        final java.util.Optional<Long> boxedLast = findLast(stream.boxed());
        return boxedLast.map((Function<? super Long, ? extends OptionalLong>)OptionalLong::of).orElseGet(OptionalLong::empty);
    }
    
    @Beta
    public static OptionalDouble findLast(final DoubleStream stream) {
        final java.util.Optional<Double> boxedLast = findLast(stream.boxed());
        return boxedLast.map((Function<? super Double, ? extends OptionalDouble>)OptionalDouble::of).orElseGet(OptionalDouble::empty);
    }
    
    private Streams() {
    }
    
    private static class TemporaryPair<A, B>
    {
        @ParametricNullness
        final A a;
        @ParametricNullness
        final B b;
        
        TemporaryPair(@ParametricNullness final A a, @ParametricNullness final B b) {
            this.a = a;
            this.b = b;
        }
    }
    
    private abstract static class MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R>
    {
        final F fromSpliterator;
        long index;
        
        MapWithIndexSpliterator(final F fromSpliterator, final long index) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }
        
        abstract S createSplit(final F p0, final long p1);
        
        @CheckForNull
        @Override
        public S trySplit() {
            final Spliterator<?> splitOrNull = this.fromSpliterator.trySplit();
            if (splitOrNull == null) {
                return null;
            }
            final F split = (F)splitOrNull;
            final S result = this.createSplit(split, this.index);
            this.index += split.getExactSizeIfKnown();
            return result;
        }
        
        @Override
        public long estimateSize() {
            return this.fromSpliterator.estimateSize();
        }
        
        @Override
        public int characteristics() {
            return this.fromSpliterator.characteristics() & 0x4050;
        }
    }
    
    @Beta
    public interface DoubleFunctionWithIndex<R>
    {
        @ParametricNullness
        R apply(final double p0, final long p1);
    }
    
    @Beta
    public interface LongFunctionWithIndex<R>
    {
        @ParametricNullness
        R apply(final long p0, final long p1);
    }
    
    @Beta
    public interface IntFunctionWithIndex<R>
    {
        @ParametricNullness
        R apply(final int p0, final long p1);
    }
    
    @Beta
    public interface FunctionWithIndex<T, R>
    {
        @ParametricNullness
        R apply(@ParametricNullness final T p0, final long p1);
    }
}
