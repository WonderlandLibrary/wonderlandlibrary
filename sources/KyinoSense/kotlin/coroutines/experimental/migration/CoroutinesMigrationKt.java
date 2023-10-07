/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.coroutines.experimental.migration;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.experimental.EmptyCoroutineContext;
import kotlin.coroutines.experimental.migration.ContextMigration;
import kotlin.coroutines.experimental.migration.ContinuationInterceptorMigration;
import kotlin.coroutines.experimental.migration.ContinuationMigration;
import kotlin.coroutines.experimental.migration.ExperimentalContextMigration;
import kotlin.coroutines.experimental.migration.ExperimentalContinuationInterceptorMigration;
import kotlin.coroutines.experimental.migration.ExperimentalContinuationMigration;
import kotlin.coroutines.experimental.migration.ExperimentalSuspendFunction0Migration;
import kotlin.coroutines.experimental.migration.ExperimentalSuspendFunction1Migration;
import kotlin.coroutines.experimental.migration.ExperimentalSuspendFunction2Migration;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a\f\u0010\u0004\u001a\u00020\u0005*\u00020\u0006H\u0007\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\tH\u0007\u001a\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a\f\u0010\u000b\u001a\u00020\u0006*\u00020\u0005H\u0007\u001a\f\u0010\f\u001a\u00020\t*\u00020\bH\u0007\u001a^\u0010\r\u001a\"\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000e\"\u0004\b\u0000\u0010\u000f\"\u0004\b\u0001\u0010\u0010\"\u0004\b\u0002\u0010\u0011*\"\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000eH\u0000\u001aL\u0010\r\u001a\u001c\u0012\u0004\u0012\u0002H\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0013\"\u0004\b\u0000\u0010\u000f\"\u0004\b\u0001\u0010\u0011*\u001c\u0012\u0004\u0012\u0002H\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0013H\u0000\u001a:\u0010\r\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0014\"\u0004\b\u0000\u0010\u0011*\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0014H\u0000\u00a8\u0006\u0015"}, d2={"toContinuation", "Lkotlin/coroutines/Continuation;", "T", "Lkotlin/coroutines/experimental/Continuation;", "toContinuationInterceptor", "Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "toCoroutineContext", "Lkotlin/coroutines/CoroutineContext;", "Lkotlin/coroutines/experimental/CoroutineContext;", "toExperimentalContinuation", "toExperimentalContinuationInterceptor", "toExperimentalCoroutineContext", "toExperimentalSuspendFunction", "Lkotlin/Function3;", "T1", "T2", "R", "", "Lkotlin/Function2;", "Lkotlin/Function1;", "kotlin-stdlib-coroutines"})
public final class CoroutinesMigrationKt {
    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> kotlin.coroutines.experimental.Continuation<T> toExperimentalContinuation(@NotNull Continuation<? super T> $this$toExperimentalContinuation) {
        Object object;
        Intrinsics.checkParameterIsNotNull($this$toExperimentalContinuation, "$this$toExperimentalContinuation");
        Continuation<? super T> continuation2 = $this$toExperimentalContinuation;
        if (!(continuation2 instanceof ContinuationMigration)) {
            continuation2 = null;
        }
        if ((object = (ContinuationMigration)continuation2) == null || (object = ((ContinuationMigration)object).getContinuation()) == null) {
            object = new ExperimentalContinuationMigration<T>($this$toExperimentalContinuation);
        }
        return object;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Continuation<T> toContinuation(@NotNull kotlin.coroutines.experimental.Continuation<? super T> $this$toContinuation) {
        Object object;
        Intrinsics.checkParameterIsNotNull($this$toContinuation, "$this$toContinuation");
        kotlin.coroutines.experimental.Continuation<? super T> continuation2 = $this$toContinuation;
        if (!(continuation2 instanceof ExperimentalContinuationMigration)) {
            continuation2 = null;
        }
        if ((object = (ExperimentalContinuationMigration)continuation2) == null || (object = ((ExperimentalContinuationMigration)object).getContinuation()) == null) {
            object = new ContinuationMigration<T>($this$toContinuation);
        }
        return object;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final kotlin.coroutines.experimental.CoroutineContext toExperimentalCoroutineContext(@NotNull CoroutineContext $this$toExperimentalCoroutineContext) {
        Intrinsics.checkParameterIsNotNull($this$toExperimentalCoroutineContext, "$this$toExperimentalCoroutineContext");
        ContinuationInterceptor interceptor = (ContinuationInterceptor)$this$toExperimentalCoroutineContext.get(ContinuationInterceptor.Key);
        ContextMigration migration = (ContextMigration)$this$toExperimentalCoroutineContext.get(ContextMigration.Key);
        CoroutineContext remainder = $this$toExperimentalCoroutineContext.minusKey(ContinuationInterceptor.Key).minusKey(ContextMigration.Key);
        Object object = migration;
        if (object == null || (object = ((ContextMigration)object).getContext()) == null) {
            object = EmptyCoroutineContext.INSTANCE;
        }
        Object original = object;
        Object result = remainder == kotlin.coroutines.EmptyCoroutineContext.INSTANCE ? original : original.plus(new ExperimentalContextMigration(remainder));
        return interceptor == null ? result : result.plus(CoroutinesMigrationKt.toExperimentalContinuationInterceptor(interceptor));
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final CoroutineContext toCoroutineContext(@NotNull kotlin.coroutines.experimental.CoroutineContext $this$toCoroutineContext) {
        Intrinsics.checkParameterIsNotNull($this$toCoroutineContext, "$this$toCoroutineContext");
        kotlin.coroutines.experimental.ContinuationInterceptor interceptor = (kotlin.coroutines.experimental.ContinuationInterceptor)$this$toCoroutineContext.get(kotlin.coroutines.experimental.ContinuationInterceptor.Key);
        ExperimentalContextMigration migration = (ExperimentalContextMigration)$this$toCoroutineContext.get(ExperimentalContextMigration.Key);
        kotlin.coroutines.experimental.CoroutineContext remainder = $this$toCoroutineContext.minusKey(kotlin.coroutines.experimental.ContinuationInterceptor.Key).minusKey(ExperimentalContextMigration.Key);
        Object object = migration;
        if (object == null || (object = ((ExperimentalContextMigration)object).getContext()) == null) {
            object = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        }
        Object original = object;
        Object result = remainder == EmptyCoroutineContext.INSTANCE ? original : original.plus(new ContextMigration(remainder));
        return interceptor == null ? result : result.plus(CoroutinesMigrationKt.toContinuationInterceptor(interceptor));
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final kotlin.coroutines.experimental.ContinuationInterceptor toExperimentalContinuationInterceptor(@NotNull ContinuationInterceptor $this$toExperimentalContinuationInterceptor) {
        Object object;
        Intrinsics.checkParameterIsNotNull($this$toExperimentalContinuationInterceptor, "$this$toExperimentalContinuationInterceptor");
        ContinuationInterceptor continuationInterceptor = $this$toExperimentalContinuationInterceptor;
        if (!(continuationInterceptor instanceof ContinuationInterceptorMigration)) {
            continuationInterceptor = null;
        }
        if ((object = (ContinuationInterceptorMigration)continuationInterceptor) == null || (object = ((ContinuationInterceptorMigration)object).getInterceptor()) == null) {
            object = new ExperimentalContinuationInterceptorMigration($this$toExperimentalContinuationInterceptor);
        }
        return object;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final ContinuationInterceptor toContinuationInterceptor(@NotNull kotlin.coroutines.experimental.ContinuationInterceptor $this$toContinuationInterceptor) {
        Object object;
        Intrinsics.checkParameterIsNotNull($this$toContinuationInterceptor, "$this$toContinuationInterceptor");
        kotlin.coroutines.experimental.ContinuationInterceptor continuationInterceptor = $this$toContinuationInterceptor;
        if (!(continuationInterceptor instanceof ExperimentalContinuationInterceptorMigration)) {
            continuationInterceptor = null;
        }
        if ((object = (ExperimentalContinuationInterceptorMigration)continuationInterceptor) == null || (object = ((ExperimentalContinuationInterceptorMigration)object).getInterceptor()) == null) {
            object = new ContinuationInterceptorMigration($this$toContinuationInterceptor);
        }
        return object;
    }

    @NotNull
    public static final <R> Function1<kotlin.coroutines.experimental.Continuation<? super R>, Object> toExperimentalSuspendFunction(@NotNull Function1<? super Continuation<? super R>, ? extends Object> $this$toExperimentalSuspendFunction) {
        Intrinsics.checkParameterIsNotNull($this$toExperimentalSuspendFunction, "$this$toExperimentalSuspendFunction");
        return new ExperimentalSuspendFunction0Migration($this$toExperimentalSuspendFunction);
    }

    @NotNull
    public static final <T1, R> Function2<T1, kotlin.coroutines.experimental.Continuation<? super R>, Object> toExperimentalSuspendFunction(@NotNull Function2<? super T1, ? super Continuation<? super R>, ? extends Object> $this$toExperimentalSuspendFunction) {
        Intrinsics.checkParameterIsNotNull($this$toExperimentalSuspendFunction, "$this$toExperimentalSuspendFunction");
        return new ExperimentalSuspendFunction1Migration($this$toExperimentalSuspendFunction);
    }

    @NotNull
    public static final <T1, T2, R> Function3<T1, T2, kotlin.coroutines.experimental.Continuation<? super R>, Object> toExperimentalSuspendFunction(@NotNull Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> $this$toExperimentalSuspendFunction) {
        Intrinsics.checkParameterIsNotNull($this$toExperimentalSuspendFunction, "$this$toExperimentalSuspendFunction");
        return new ExperimentalSuspendFunction2Migration($this$toExperimentalSuspendFunction);
    }
}

