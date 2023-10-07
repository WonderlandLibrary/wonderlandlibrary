/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.coroutines.experimental;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u0001\u0018\u0000 \u0015*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0002\u0015\u0016B\u0015\b\u0011\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u00a2\u0006\u0002\u0010\u0004B\u001f\b\u0000\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007J\n\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0001J\u0015\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0011J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lkotlin/coroutines/experimental/SafeContinuation;", "T", "Lkotlin/coroutines/experimental/Continuation;", "delegate", "(Lkotlin/coroutines/experimental/Continuation;)V", "initialResult", "", "(Lkotlin/coroutines/experimental/Continuation;Ljava/lang/Object;)V", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "getContext", "()Lkotlin/coroutines/experimental/CoroutineContext;", "result", "getResult", "resume", "", "value", "(Ljava/lang/Object;)V", "resumeWithException", "exception", "", "Companion", "Fail", "kotlin-stdlib-coroutines"})
@PublishedApi
public final class SafeContinuation<T>
implements Continuation<T> {
    private volatile Object result;
    private final Continuation<T> delegate;
    private static final Object UNDECIDED;
    private static final Object RESUMED;
    private static final AtomicReferenceFieldUpdater<SafeContinuation<?>, Object> RESULT;
    public static final Companion Companion;

    @Override
    @NotNull
    public CoroutineContext getContext() {
        return this.delegate.getContext();
    }

    @Override
    public void resume(T value) {
        block2: {
            while (true) {
                Object result = this.result;
                if (result == UNDECIDED) {
                    if (!RESULT.compareAndSet(this, UNDECIDED, value)) continue;
                    return;
                }
                if (result != IntrinsicsKt.getCOROUTINE_SUSPENDED()) break block2;
                if (RESULT.compareAndSet(this, IntrinsicsKt.getCOROUTINE_SUSPENDED(), RESUMED)) break;
            }
            this.delegate.resume(value);
            return;
        }
        throw (Throwable)new IllegalStateException("Already resumed");
    }

    @Override
    public void resumeWithException(@NotNull Throwable exception) {
        block2: {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            while (true) {
                Object result = this.result;
                if (result == UNDECIDED) {
                    if (!RESULT.compareAndSet(this, UNDECIDED, new Fail(exception))) continue;
                    return;
                }
                if (result != IntrinsicsKt.getCOROUTINE_SUSPENDED()) break block2;
                if (RESULT.compareAndSet(this, IntrinsicsKt.getCOROUTINE_SUSPENDED(), RESUMED)) break;
            }
            this.delegate.resumeWithException(exception);
            return;
        }
        throw (Throwable)new IllegalStateException("Already resumed");
    }

    @PublishedApi
    @Nullable
    public final Object getResult() {
        Object result = this.result;
        if (result == UNDECIDED) {
            if (RESULT.compareAndSet(this, UNDECIDED, IntrinsicsKt.getCOROUTINE_SUSPENDED())) {
                return IntrinsicsKt.getCOROUTINE_SUSPENDED();
            }
            result = this.result;
        }
        if (result == RESUMED) {
            return IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (result instanceof Fail) {
            throw ((Fail)result).getException();
        }
        return result;
    }

    public SafeContinuation(@NotNull Continuation<? super T> delegate, @Nullable Object initialResult) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this.delegate = delegate;
        this.result = initialResult;
    }

    @PublishedApi
    public SafeContinuation(@NotNull Continuation<? super T> delegate) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        this(delegate, UNDECIDED);
    }

    static {
        Companion = new Companion(null);
        UNDECIDED = new Object();
        RESUMED = new Object();
        RESULT = AtomicReferenceFieldUpdater.newUpdater(SafeContinuation.class, Object.class, "result");
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/coroutines/experimental/SafeContinuation$Fail;", "", "exception", "", "(Ljava/lang/Throwable;)V", "getException", "()Ljava/lang/Throwable;", "kotlin-stdlib-coroutines"})
    private static final class Fail {
        @NotNull
        private final Throwable exception;

        @NotNull
        public final Throwable getException() {
            return this.exception;
        }

        public Fail(@NotNull Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            this.exception = exception;
        }
    }

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002RZ\u0010\u0003\u001aF\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0001 \u0006*\"\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u00040\u00048\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0002R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lkotlin/coroutines/experimental/SafeContinuation$Companion;", "", "()V", "RESULT", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlin/coroutines/experimental/SafeContinuation;", "kotlin.jvm.PlatformType", "RESULT$annotations", "RESUMED", "UNDECIDED", "kotlin-stdlib-coroutines"})
    public static final class Companion {
        @JvmStatic
        private static /* synthetic */ void RESULT$annotations() {
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

