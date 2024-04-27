/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.sequences.ConstrainedOnceSequence;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.FlatteningSequence;
import kotlin.sequences.GeneratorSequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesJVMKt;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.TransformingSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u001c\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u00f8\u0001\u0000\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001ab\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f2\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0000\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000e\u001a=\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000eH\u0007\u00a2\u0006\u0002\u0010\u0014\u001a+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0017\"\u0002H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0002\u00a2\u0006\u0002\b\u001c\u001a)\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001d0\u0001H\u0007\u00a2\u0006\u0002\b\u001e\u001a\"\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a&\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010#\u001a\u00020$H\u0007\u001a@\u0010%\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020'\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0'0&\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0&0\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006("}, d2={"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "flatMapIndexed", "R", "C", "source", "transform", "Lkotlin/Function2;", "", "Lkotlin/Function1;", "generateSequence", "", "nextFunction", "seedFunction", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "shuffled", "random", "Lkotlin/random/Random;", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, xs="kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt
extends SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> Sequence(Function0<? extends Iterator<? extends T>> iterator2) {
        Intrinsics.checkNotNullParameter(iterator2, "iterator");
        return new Sequence<T>(iterator2){
            final /* synthetic */ Function0<Iterator<T>> $iterator;
            {
                this.$iterator = $iterator;
            }

            @NotNull
            public Iterator<T> iterator() {
                return this.$iterator.invoke();
            }
        };
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> $this$asSequence) {
        Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        return SequencesKt.constrainOnce(new Sequence<T>($this$asSequence){
            final /* synthetic */ Iterator $this_asSequence$inlined;
            {
                this.$this_asSequence$inlined = iterator2;
            }

            @NotNull
            public Iterator<T> iterator() {
                boolean bl = false;
                return this.$this_asSequence$inlined;
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> sequenceOf(T ... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return elements.length == 0 ? SequencesKt.emptySequence() : ArraysKt.asSequence(elements);
    }

    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> Sequence<T> orEmpty(Sequence<? extends T> $this$orEmpty) {
        Sequence<? extends T> sequence = $this$orEmpty;
        return sequence == null ? SequencesKt.emptySequence() : sequence;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Sequence<T> ifEmpty(@NotNull Sequence<? extends T> $this$ifEmpty, @NotNull Function0<? extends Sequence<? extends T>> defaultValue) {
        Intrinsics.checkNotNullParameter($this$ifEmpty, "<this>");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>($this$ifEmpty, defaultValue, null){
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $this_ifEmpty;
            final /* synthetic */ Function0<Sequence<T>> $defaultValue;
            {
                this.$this_ifEmpty = $receiver;
                this.$defaultValue = $defaultValue;
                super(2, $completion);
            }

            /*
             * WARNING - void declaration
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object object) {
                void $result;
                SequenceScope $this$sequence;
                Object object2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        $this$sequence = (SequenceScope)this.L$0;
                        Iterator<T> iterator2 = this.$this_ifEmpty.iterator();
                        if (!iterator2.hasNext()) break;
                        this.label = 1;
                        Object object3 = $this$sequence.yieldAll(iterator2, (Continuation<Unit>)this);
                        if (object3 != object2) return Unit.INSTANCE;
                        return object2;
                    }
                    case 1: {
                        ResultKt.throwOnFailure($result);
                        Object object3 = $result;
                        return Unit.INSTANCE;
                    }
                }
                this.label = 2;
                Object object4 = $this$sequence.yieldAll(this.$defaultValue.invoke(), (Continuation<Unit>)this);
                if (object4 != object2) return Unit.INSTANCE;
                return object2;
                {
                    case 2: {
                        ResultKt.throwOnFailure($result);
                        object4 = $result;
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = value;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super T> p1, @Nullable Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> $this$flatten) {
        Intrinsics.checkNotNullParameter($this$flatten, "<this>");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt($this$flatten, flatten.1.INSTANCE);
    }

    @JvmName(name="flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull Sequence<? extends Iterable<? extends T>> $this$flatten) {
        Intrinsics.checkNotNullParameter($this$flatten, "<this>");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt($this$flatten, flatten.2.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(Sequence<? extends T> $this$flatten, Function1<? super T, ? extends Iterator<? extends R>> iterator2) {
        if ($this$flatten instanceof TransformingSequence) {
            return ((TransformingSequence)$this$flatten).flatten$kotlin_stdlib(iterator2);
        }
        return new FlatteningSequence($this$flatten, flatten.3.INSTANCE, iterator2);
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Sequence<? extends Pair<? extends T, ? extends R>> $this$unzip) {
        Intrinsics.checkNotNullParameter($this$unzip, "<this>");
        ArrayList<T> listT = new ArrayList<T>();
        ArrayList<R> listR = new ArrayList<R>();
        Iterator<Pair<T, R>> iterator2 = $this$unzip.iterator();
        while (iterator2.hasNext()) {
            Pair<T, R> pair = iterator2.next();
            listT.add(pair.getFirst());
            listR.add(pair.getSecond());
        }
        return TuplesKt.to(listT, listR);
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T> Sequence<T> shuffled(@NotNull Sequence<? extends T> $this$shuffled) {
        Intrinsics.checkNotNullParameter($this$shuffled, "<this>");
        return SequencesKt.shuffled($this$shuffled, Random.Default);
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T> Sequence<T> shuffled(@NotNull Sequence<? extends T> $this$shuffled, @NotNull Random random) {
        Intrinsics.checkNotNullParameter($this$shuffled, "<this>");
        Intrinsics.checkNotNullParameter(random, "random");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>($this$shuffled, random, null){
            Object L$1;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $this_shuffled;
            final /* synthetic */ Random $random;
            {
                this.$this_shuffled = $receiver;
                this.$random = $random;
                super(2, $completion);
            }

            /*
             * Unable to fully structure code
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object var1_1) {
                var7_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var1_1);
                        $this$sequence = (SequenceScope)this.L$0;
                        buffer = SequencesKt.toMutableList(this.$this_shuffled);
lbl7:
                        // 3 sources

                        while (((Collection)buffer).isEmpty() == false) {
                            j = this.$random.nextInt(buffer.size());
                            last = CollectionsKt.removeLast(buffer);
                            value = j < buffer.size() ? buffer.set(j, last) : last;
                            this.L$0 = $this$sequence;
                            this.L$1 = buffer;
                            this.label = 1;
                            v0 = $this$sequence.yield(value, this);
                            if (v0 != var7_2) continue;
                            return var7_2;
                        }
                        break;
                    }
                    case 1: {
                        buffer = (List<T>)this.L$1;
                        $this$sequence = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure($result);
                        v0 = $result;
                        ** GOTO lbl7
                    }
                }
                return Unit.INSTANCE;
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = value;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super T> p1, @Nullable Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T, C, R> Sequence<R> flatMapIndexed(@NotNull Sequence<? extends T> source, @NotNull Function2<? super Integer, ? super T, ? extends C> transform, @NotNull Function1<? super C, ? extends Iterator<? extends R>> iterator2) {
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(transform, "transform");
        Intrinsics.checkNotNullParameter(iterator2, "iterator");
        return SequencesKt.sequence(new Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object>(source, transform, iterator2, null){
            Object L$1;
            int I$0;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $source;
            final /* synthetic */ Function2<Integer, T, C> $transform;
            final /* synthetic */ Function1<C, Iterator<R>> $iterator;
            {
                this.$source = $source;
                this.$transform = $transform;
                this.$iterator = $iterator;
                super(2, $completion);
            }

            /*
             * Unable to fully structure code
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object var1_1) {
                var8_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var1_1);
                        $this$sequence = (SequenceScope)this.L$0;
                        index = 0;
                        var4_5 = this.$source.iterator();
lbl8:
                        // 3 sources

                        while (var4_5.hasNext()) {
                            element = var4_5.next();
                            var7_8 = index;
                            index = var7_8 + 1;
                            if (var7_8 < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            result = this.$transform.invoke(Boxing.boxInt(var7_8), element);
                            this.L$0 = $this$sequence;
                            this.L$1 = var4_5;
                            this.I$0 = index;
                            this.label = 1;
                            v0 = $this$sequence.yieldAll(this.$iterator.invoke(result), (Continuation<Unit>)this);
                            if (v0 != var8_2) continue;
                            return var8_2;
                        }
                        break;
                    }
                    case 1: {
                        index = this.I$0;
                        var4_5 = (Iterator<T>)this.L$1;
                        $this$sequence = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure($result);
                        v0 = $result;
                        ** GOTO lbl8
                    }
                }
                return Unit.INSTANCE;
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> function2 = new /* invalid duplicate definition of identical inner class */;
                function2.L$0 = value;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super R> p1, @Nullable Continuation<? super Unit> p2) {
                return (this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> $this$constrainOnce) {
        Intrinsics.checkNotNullParameter($this$constrainOnce, "<this>");
        return $this$constrainOnce instanceof ConstrainedOnceSequence ? $this$constrainOnce : (Sequence)new ConstrainedOnceSequence<T>($this$constrainOnce);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> nextFunction) {
        Intrinsics.checkNotNullParameter(nextFunction, "nextFunction");
        return SequencesKt.constrainOnce((Sequence)new GeneratorSequence<T>(nextFunction, new Function1<T, T>(nextFunction){
            final /* synthetic */ Function0<T> $nextFunction;
            {
                this.$nextFunction = $nextFunction;
                super(1);
            }

            @Nullable
            public final T invoke(@NotNull T it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return this.$nextFunction.invoke();
            }
        }));
    }

    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable T seed, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkNotNullParameter(nextFunction, "nextFunction");
        return seed == null ? (Sequence)EmptySequence.INSTANCE : (Sequence)new GeneratorSequence<T>(new Function0<T>(seed){
            final /* synthetic */ T $seed;
            {
                this.$seed = $seed;
                super(0);
            }

            @Nullable
            public final T invoke() {
                return this.$seed;
            }
        }, nextFunction);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> seedFunction, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkNotNullParameter(seedFunction, "seedFunction");
        Intrinsics.checkNotNullParameter(nextFunction, "nextFunction");
        return new GeneratorSequence<T>(seedFunction, nextFunction);
    }
}

