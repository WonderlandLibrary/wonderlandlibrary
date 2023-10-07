/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.RestrictsSuspension;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0003J\u0019\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u001f\u0010\b\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u001f\u0010\b\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rH\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\b\u001a\u00020\u00052\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u0082\u0002\u0004\n\u0002\b\t\u00a8\u0006\u0012"}, d2={"Lkotlin/coroutines/experimental/SequenceBuilder;", "T", "", "()V", "yield", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "yieldAll", "elements", "", "(Ljava/lang/Iterable;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "iterator", "", "(Ljava/util/Iterator;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "sequence", "Lkotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-coroutines"})
@RestrictsSuspension
@SinceKotlin(version="1.1")
public abstract class SequenceBuilder<T> {
    @Nullable
    public abstract Object yield(T var1, @NotNull Continuation<? super Unit> var2);

    @Nullable
    public abstract Object yieldAll(@NotNull Iterator<? extends T> var1, @NotNull Continuation<? super Unit> var2);

    @Nullable
    public final Object yieldAll(@NotNull Iterable<? extends T> elements, @NotNull Continuation<? super Unit> $completion) {
        if (elements instanceof Collection && ((Collection)elements).isEmpty()) {
            return Unit.INSTANCE;
        }
        return this.yieldAll(elements.iterator(), $completion);
    }

    @Nullable
    public final Object yieldAll(@NotNull Sequence<? extends T> sequence, @NotNull Continuation<? super Unit> $completion) {
        return this.yieldAll(sequence.iterator(), $completion);
    }
}

