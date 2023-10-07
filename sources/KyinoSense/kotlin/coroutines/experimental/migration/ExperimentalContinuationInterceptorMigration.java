/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental.migration;

import kotlin.Metadata;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.migration.CoroutinesMigrationKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\"\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\r0\f\"\u0004\b\u0000\u0010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\r0\fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0018\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lkotlin/coroutines/experimental/migration/ExperimentalContinuationInterceptorMigration;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "interceptor", "Lkotlin/coroutines/ContinuationInterceptor;", "(Lkotlin/coroutines/ContinuationInterceptor;)V", "getInterceptor", "()Lkotlin/coroutines/ContinuationInterceptor;", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/experimental/CoroutineContext$Key;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "kotlin-stdlib-coroutines"})
final class ExperimentalContinuationInterceptorMigration
implements kotlin.coroutines.experimental.ContinuationInterceptor {
    @NotNull
    private final ContinuationInterceptor interceptor;

    @Override
    @NotNull
    public CoroutineContext.Key<?> getKey() {
        return kotlin.coroutines.experimental.ContinuationInterceptor.Key;
    }

    @Override
    @NotNull
    public <T> Continuation<T> interceptContinuation(@NotNull Continuation<? super T> continuation2) {
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        return CoroutinesMigrationKt.toExperimentalContinuation(this.interceptor.interceptContinuation(CoroutinesMigrationKt.toContinuation(continuation2)));
    }

    @NotNull
    public final ContinuationInterceptor getInterceptor() {
        return this.interceptor;
    }

    public ExperimentalContinuationInterceptorMigration(@NotNull ContinuationInterceptor interceptor) {
        Intrinsics.checkParameterIsNotNull(interceptor, "interceptor");
        this.interceptor = interceptor;
    }

    @Override
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return ContinuationInterceptor.DefaultImpls.get(this, key);
    }

    @Override
    public <R> R fold(R initial, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return ContinuationInterceptor.DefaultImpls.fold(this, initial, operation);
    }

    @Override
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return ContinuationInterceptor.DefaultImpls.minusKey(this, key);
    }

    @Override
    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return ContinuationInterceptor.DefaultImpls.plus(this, context);
    }
}

