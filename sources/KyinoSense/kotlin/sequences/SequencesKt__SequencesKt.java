/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
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

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\u001c\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a+\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001a&\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000b\u001a=\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\b2\b\u0010\f\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000bH\u0007\u00a2\u0006\u0002\u0010\r\u001a+\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u000f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010\"\u0002H\u0002\u00a2\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u00050\u000bH\u0002\u00a2\u0006\u0002\b\u0016\u001a)\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00170\u0001H\u0007\u00a2\u0006\u0002\b\u0018\u001a\"\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a@\u0010\u001c\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001e0\u001d\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0015*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00150\u001d0\u0001\u00a8\u0006\u001f"}, d2={"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "generateSequence", "", "nextFunction", "seedFunction", "Lkotlin/Function1;", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "R", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, xs="kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt
extends SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> Sequence(Function0<? extends Iterator<? extends T>> iterator2) {
        int $i$f$Sequence = 0;
        return new Sequence<T>(iterator2){
            final /* synthetic */ Function0 $iterator;

            @NotNull
            public Iterator<T> iterator() {
                return (Iterator)this.$iterator.invoke();
            }
            {
                this.$iterator = $captured_local_variable$0;
            }
        };
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        boolean bl = false;
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
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        T[] TArray = elements;
        boolean bl = false;
        return TArray.length == 0 ? SequencesKt.emptySequence() : ArraysKt.asSequence(elements);
    }

    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> Sequence<T> orEmpty(@Nullable Sequence<? extends T> $this$orEmpty) {
        int $i$f$orEmpty = 0;
        Sequence<Object> sequence = $this$orEmpty;
        if (sequence == null) {
            sequence = SequencesKt.emptySequence();
        }
        return sequence;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Sequence<T> ifEmpty(@NotNull Sequence<? extends T> $this$ifEmpty, @NotNull Function0<? extends Sequence<? extends T>> defaultValue) {
        Intrinsics.checkParameterIsNotNull($this$ifEmpty, "$this$ifEmpty");
        Intrinsics.checkParameterIsNotNull(defaultValue, "defaultValue");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>($this$ifEmpty, defaultValue, null){
            private SequenceScope p$;
            Object L$0;
            Object L$1;
            int label;
            final /* synthetic */ Sequence $this_ifEmpty;
            final /* synthetic */ Function0 $defaultValue;

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
                Iterator<T> iterator22;
                SequenceScope $this$sequence2;
                Object object = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        $this$sequence2 = this.p$;
                        iterator22 = this.$this_ifEmpty.iterator();
                        if (!iterator22.hasNext()) break;
                        this.L$0 = $this$sequence2;
                        this.L$1 = iterator22;
                        this.label = 1;
                        Object object2 = $this$sequence2.yieldAll(iterator22, (Continuation<Unit>)this);
                        if (object2 != object) return Unit.INSTANCE;
                        return object;
                    }
                    case 1: {
                        Iterator iterator22 = (Iterator)this.L$1;
                        SequenceScope $this$sequence2 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure($result);
                        Object object2 = $result;
                        return Unit.INSTANCE;
                    }
                }
                this.L$0 = $this$sequence2;
                this.L$1 = iterator22;
                this.label = 2;
                Object object3 = $this$sequence2.yieldAll((Sequence)this.$defaultValue.invoke(), (Continuation<Unit>)this);
                if (object3 != object) return Unit.INSTANCE;
                return object;
                {
                    case 2: {
                        Iterator iterator22 = (Iterator)this.L$1;
                        $this$sequence2 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure($result);
                        object3 = $result;
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            {
                this.$this_ifEmpty = sequence;
                this.$defaultValue = function0;
                super(2, continuation2);
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> completion) {
                Intrinsics.checkParameterIsNotNull(completion, "completion");
                Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> function2 = new /* invalid duplicate definition of identical inner class */;
                SequenceScope sequenceScope = function2.p$ = (SequenceScope)value;
                return function2;
            }

            public final Object invoke(Object object, Object object2) {
                return (this.create(object, (Continuation)object2)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> $this$flatten) {
        Intrinsics.checkParameterIsNotNull($this$flatten, "$this$flatten");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt($this$flatten, flatten.1.INSTANCE);
    }

    @JvmName(name="flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull Sequence<? extends Iterable<? extends T>> $this$flatten) {
        Intrinsics.checkParameterIsNotNull($this$flatten, "$this$flatten");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt($this$flatten, flatten.2.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(@NotNull Sequence<? extends T> $this$flatten, Function1<? super T, ? extends Iterator<? extends R>> iterator2) {
        if ($this$flatten instanceof TransformingSequence) {
            return ((TransformingSequence)$this$flatten).flatten$kotlin_stdlib(iterator2);
        }
        return new FlatteningSequence($this$flatten, flatten.3.INSTANCE, iterator2);
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Sequence<? extends Pair<? extends T, ? extends R>> $this$unzip) {
        Intrinsics.checkParameterIsNotNull($this$unzip, "$this$unzip");
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

    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> $this$constrainOnce) {
        Intrinsics.checkParameterIsNotNull($this$constrainOnce, "$this$constrainOnce");
        return $this$constrainOnce instanceof ConstrainedOnceSequence ? $this$constrainOnce : (Sequence)new ConstrainedOnceSequence<T>($this$constrainOnce);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return SequencesKt.constrainOnce((Sequence)new GeneratorSequence<T>(nextFunction, new Function1<T, T>(nextFunction){
            final /* synthetic */ Function0 $nextFunction;

            @Nullable
            public final T invoke(@NotNull T it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return (T)this.$nextFunction.invoke();
            }
            {
                this.$nextFunction = function0;
                super(1);
            }
        }));
    }

    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable T seed, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return seed == null ? (Sequence)EmptySequence.INSTANCE : (Sequence)new GeneratorSequence<T>(new Function0<T>(seed){
            final /* synthetic */ Object $seed;

            @Nullable
            public final T invoke() {
                return (T)this.$seed;
            }
            {
                this.$seed = object;
                super(0);
            }
        }, nextFunction);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> seedFunction, @NotNull Function1<? super T, ? extends T> nextFunction) {
        Intrinsics.checkParameterIsNotNull(seedFunction, "seedFunction");
        Intrinsics.checkParameterIsNotNull(nextFunction, "nextFunction");
        return new GeneratorSequence<T>(seedFunction, nextFunction);
    }
}

