// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectSpliterators
{
    private CollectSpliterators() {
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function) {
        return indexed(size, extraCharacteristics, function, null);
    }
    
    static <T> Spliterator<T> indexed(final int size, final int extraCharacteristics, final IntFunction<T> function, final Comparator<? super T> comparator) {
        if (comparator != null) {
            Preconditions.checkArgument((extraCharacteristics & 0x4) != 0x0);
        }
        class WithCharacteristics implements Spliterator<T>
        {
            private final Spliterator<T> delegate = IntStream.range(0, size).mapToObj((IntFunction<?>)function).spliterator();
            final /* synthetic */ Comparator val$comparator;
            
            WithCharacteristics(final Spliterator delegate, final Spliterator<T> val$comparator) {
                this.val$comparator = (Comparator)val$comparator;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                return this.delegate.tryAdvance(action);
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> action) {
                this.delegate.forEachRemaining(action);
            }
            
            @Nullable
            @Override
            public Spliterator<T> trySplit() {
                final Spliterator<T> split = this.delegate.trySplit();
                return (split == null) ? null : new WithCharacteristics(split, extraCharacteristics, this.val$comparator);
            }
            
            @Override
            public long estimateSize() {
                return this.delegate.estimateSize();
            }
            
            @Override
            public int characteristics() {
                return this.delegate.characteristics() | extraCharacteristics;
            }
            
            @Override
            public Comparator<? super T> getComparator() {
                if (this.hasCharacteristics(4)) {
                    return (Comparator<? super T>)this.val$comparator;
                }
                throw new IllegalStateException();
            }
        }
        return new WithCharacteristics(comparator);
    }
    
    static <F, T> Spliterator<T> map(final Spliterator<F> fromSpliterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        return new Spliterator<T>() {
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                return fromSpliterator.tryAdvance(fromElement -> action.accept((Object)function.apply(fromElement)));
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> action) {
                fromSpliterator.forEachRemaining(fromElement -> action.accept((Object)function.apply(fromElement)));
            }
            
            @Override
            public Spliterator<T> trySplit() {
                final Spliterator<F> fromSplit = fromSpliterator.trySplit();
                return (Spliterator<T>)((fromSplit != null) ? CollectSpliterators.map((Spliterator<Object>)fromSplit, (Function<? super Object, ?>)function) : null);
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
            T holder;
            
            Splitr() {
                this.holder = null;
            }
            
            @Override
            public void accept(final T t) {
                this.holder = t;
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                while (fromSpliterator.tryAdvance(this)) {
                    try {
                        if (predicate.test(this.holder)) {
                            action.accept((Object)this.holder);
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
            
            @Override
            public Spliterator<T> trySplit() {
                final Spliterator<T> fromSplit = fromSpliterator.trySplit();
                return (Spliterator<T>)((fromSplit == null) ? null : CollectSpliterators.filter((Spliterator<Object>)fromSplit, predicate));
            }
            
            @Override
            public long estimateSize() {
                return fromSpliterator.estimateSize() / 2L;
            }
            
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
    
    static <F, T> Spliterator<T> flatMap(final Spliterator<F> fromSpliterator, final Function<? super F, Spliterator<T>> function, final int topCharacteristics, final long topSize) {
        Preconditions.checkArgument((topCharacteristics & 0x4000) == 0x0, (Object)"flatMap does not support SUBSIZED characteristic");
        Preconditions.checkArgument((topCharacteristics & 0x4) == 0x0, (Object)"flatMap does not support SORTED characteristic");
        Preconditions.checkNotNull(fromSpliterator);
        Preconditions.checkNotNull(function);
        class FlatMapSpliterator implements Spliterator<T>
        {
            @Nullable
            Spliterator<T> prefix;
            final Spliterator<F> from;
            final int characteristics;
            long estimatedSize;
            final /* synthetic */ Function val$function;
            
            FlatMapSpliterator(final Spliterator prefix, final Spliterator<T> from, final Spliterator<F> characteristics, final int estimatedSize, final long p5) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: putfield        me/gong/mcleaks/util/google/common/collect/CollectSpliterators$1FlatMapSpliterator.val$function:Ljava/util/function/Function;
                //     6: aload_0         /* this */
                //     7: invokespecial   java/lang/Object.<init>:()V
                //    10: aload_0         /* this */
                //    11: aload_1         /* prefix */
                //    12: putfield        me/gong/mcleaks/util/google/common/collect/CollectSpliterators$1FlatMapSpliterator.prefix:Ljava/util/Spliterator;
                //    15: aload_0         /* this */
                //    16: aload_2         /* from */
                //    17: putfield        me/gong/mcleaks/util/google/common/collect/CollectSpliterators$1FlatMapSpliterator.from:Ljava/util/Spliterator;
                //    20: aload_0         /* this */
                //    21: iload_3         /* characteristics */
                //    22: putfield        me/gong/mcleaks/util/google/common/collect/CollectSpliterators$1FlatMapSpliterator.characteristics:I
                //    25: aload_0         /* this */
                //    26: lload           estimatedSize
                //    28: putfield        me/gong/mcleaks/util/google/common/collect/CollectSpliterators$1FlatMapSpliterator.estimatedSize:J
                //    31: return         
                //    Signature:
                //  (Ljava/util/Spliterator<TT;>;Ljava/util/Spliterator<TF;>;IJ)V
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
                //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:670)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
                //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
                //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public boolean tryAdvance(final Consumer<? super T> action) {
                while (this.prefix == null || !this.prefix.tryAdvance(action)) {
                    this.prefix = null;
                    if (!this.from.tryAdvance(fromElement -> this.prefix = (Spliterator<T>)this.val$function.apply(fromElement))) {
                        return false;
                    }
                }
                if (this.estimatedSize != Long.MAX_VALUE) {
                    --this.estimatedSize;
                }
                return true;
            }
            
            @Override
            public void forEachRemaining(final Consumer<? super T> action) {
                if (this.prefix != null) {
                    this.prefix.forEachRemaining(action);
                    this.prefix = null;
                }
                this.from.forEachRemaining(fromElement -> ((Spliterator)this.val$function.apply(fromElement)).forEachRemaining(action));
                this.estimatedSize = 0L;
            }
            
            @Override
            public Spliterator<T> trySplit() {
                final Spliterator<F> fromSplit = this.from.trySplit();
                if (fromSplit != null) {
                    final int splitCharacteristics = this.characteristics & 0xFFFFFFBF;
                    long estSplitSize = this.estimateSize();
                    if (estSplitSize < Long.MAX_VALUE) {
                        estSplitSize /= 2L;
                        this.estimatedSize -= estSplitSize;
                    }
                    final Spliterator<T> result = new FlatMapSpliterator(this.prefix, fromSplit, splitCharacteristics, estSplitSize, this.val$function);
                    this.prefix = null;
                    return result;
                }
                if (this.prefix != null) {
                    final Spliterator<T> result2 = this.prefix;
                    this.prefix = null;
                    return result2;
                }
                return null;
            }
            
            @Override
            public long estimateSize() {
                if (this.prefix != null) {
                    this.estimatedSize = Math.max(this.estimatedSize, this.prefix.estimateSize());
                }
                return Math.max(this.estimatedSize, 0L);
            }
            
            @Override
            public int characteristics() {
                return this.characteristics;
            }
        }
        return new FlatMapSpliterator(null, fromSpliterator, topCharacteristics, topSize, function);
    }
}
