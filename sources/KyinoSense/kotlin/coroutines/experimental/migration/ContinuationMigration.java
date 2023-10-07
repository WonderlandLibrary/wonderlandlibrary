/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.coroutines.experimental.migration;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.experimental.migration.CoroutinesMigrationKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2={"Lkotlin/coroutines/experimental/migration/ContinuationMigration;", "T", "Lkotlin/coroutines/Continuation;", "continuation", "Lkotlin/coroutines/experimental/Continuation;", "(Lkotlin/coroutines/experimental/Continuation;)V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getContinuation", "()Lkotlin/coroutines/experimental/Continuation;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "kotlin-stdlib-coroutines"})
final class ContinuationMigration<T>
implements Continuation<T> {
    @NotNull
    private final CoroutineContext context;
    @NotNull
    private final kotlin.coroutines.experimental.Continuation<T> continuation;

    @Override
    @NotNull
    public CoroutineContext getContext() {
        return this.context;
    }

    @Override
    public void resumeWith(@NotNull Object result) {
        block1: {
            Object object = result;
            boolean bl = false;
            boolean bl2 = false;
            if (Result.isSuccess-impl(object)) {
                Object it = object;
                boolean bl3 = false;
                this.continuation.resume(it);
            }
            bl = false;
            bl2 = false;
            Throwable throwable = Result.exceptionOrNull-impl(object);
            if (throwable == null) break block1;
            Throwable throwable2 = throwable;
            boolean bl4 = false;
            boolean bl5 = false;
            Throwable throwable3 = throwable2;
            boolean bl6 = false;
            Throwable it = throwable3;
            boolean bl7 = false;
            this.continuation.resumeWithException(it);
        }
    }

    @NotNull
    public final kotlin.coroutines.experimental.Continuation<T> getContinuation() {
        return this.continuation;
    }

    public ContinuationMigration(@NotNull kotlin.coroutines.experimental.Continuation<? super T> continuation2) {
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        this.continuation = continuation2;
        this.context = CoroutinesMigrationKt.toCoroutineContext(this.continuation.getContext());
    }
}

