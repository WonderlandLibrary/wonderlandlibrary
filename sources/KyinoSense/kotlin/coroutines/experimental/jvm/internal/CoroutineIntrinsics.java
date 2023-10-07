/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.coroutines.experimental.jvm.internal;

import kotlin.Metadata;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a \u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\u00a8\u0006\u0007"}, d2={"interceptContinuationIfNeeded", "Lkotlin/coroutines/experimental/Continuation;", "T", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "continuation", "normalizeContinuation", "kotlin-stdlib-coroutines"})
@JvmName(name="CoroutineIntrinsics")
public final class CoroutineIntrinsics {
    @NotNull
    public static final <T> Continuation<T> normalizeContinuation(@NotNull Continuation<? super T> continuation2) {
        Continuation<Object> continuation3;
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        Continuation<? super T> continuation4 = continuation2;
        if (!(continuation4 instanceof CoroutineImpl)) {
            continuation4 = null;
        }
        if ((continuation3 = (CoroutineImpl)continuation4) == null || (continuation3 = continuation3.getFacade()) == null) {
            continuation3 = continuation2;
        }
        return continuation3;
    }

    @NotNull
    public static final <T> Continuation<T> interceptContinuationIfNeeded(@NotNull CoroutineContext context, @NotNull Continuation<? super T> continuation2) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(continuation2, "continuation");
        Object object = (ContinuationInterceptor)context.get(ContinuationInterceptor.Key);
        if (object == null || (object = object.interceptContinuation(continuation2)) == null) {
            object = continuation2;
        }
        return object;
    }
}

