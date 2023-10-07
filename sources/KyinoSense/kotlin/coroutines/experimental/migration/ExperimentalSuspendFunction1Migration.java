/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental.migration;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.experimental.migration.CoroutinesMigrationKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u001c\u0012\u0004\u0012\u0002H\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003B'\u0012 \u0010\u0006\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003\u00a2\u0006\u0002\u0010\bJ&\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00028\u00002\f\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004H\u0096\u0002\u00a2\u0006\u0002\u0010\u000eR+\u0010\u0006\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lkotlin/coroutines/experimental/migration/ExperimentalSuspendFunction1Migration;", "T1", "R", "Lkotlin/Function2;", "Lkotlin/coroutines/experimental/Continuation;", "", "function", "Lkotlin/coroutines/Continuation;", "(Lkotlin/jvm/functions/Function2;)V", "getFunction", "()Lkotlin/jvm/functions/Function2;", "invoke", "t1", "continuation", "(Ljava/lang/Object;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-coroutines"})
final class ExperimentalSuspendFunction1Migration<T1, R>
implements Function2<T1, kotlin.coroutines.experimental.Continuation<? super R>, Object> {
    @NotNull
    private final Function2<T1, Continuation<? super R>, Object> function;

    @Override
    @Nullable
    public Object invoke(T1 t1, @NotNull kotlin.coroutines.experimental.Continuation<? super R> continuation2) {
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        return this.function.invoke(t1, CoroutinesMigrationKt.toContinuation(continuation2));
    }

    @NotNull
    public final Function2<T1, Continuation<? super R>, Object> getFunction() {
        return this.function;
    }

    public ExperimentalSuspendFunction1Migration(@NotNull Function2<? super T1, ? super Continuation<? super R>, ? extends Object> function) {
        Intrinsics.checkParameterIsNotNull(function, "function");
        this.function = function;
    }
}

