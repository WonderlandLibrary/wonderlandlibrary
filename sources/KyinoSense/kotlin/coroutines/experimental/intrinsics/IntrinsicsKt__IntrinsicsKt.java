/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.coroutines.experimental.intrinsics;

import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.SinceKotlin;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000\u0014\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001c\b\u0004\u0010\u0002\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0087H\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u001a5\u0010\u0007\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001c\b\u0004\u0010\u0002\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0087H\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u001a\u001f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004\"\u0004\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b\u0082\u0002\u0004\n\u0002\b\t\u00a8\u0006\t"}, d2={"suspendCoroutineOrReturn", "T", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "suspendCoroutineUninterceptedOrReturn", "intercepted", "kotlin-stdlib-coroutines"}, xs="kotlin/coroutines/experimental/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsKt
extends IntrinsicsKt__IntrinsicsJvmKt {
    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <T> Object suspendCoroutineOrReturn(Function1<? super Continuation<? super T>, ? extends Object> block, Continuation<? super T> continuation2) {
        int $i$f$suspendCoroutineOrReturn = 0;
        InlineMarker.mark(0);
        Continuation<? super T> cont = continuation2;
        boolean bl = false;
        Continuation<? super T> continuation3 = cont;
        Function1<Continuation<T>, Object> function1 = block;
        Continuation<? super T> continuation4 = CoroutineIntrinsics.normalizeContinuation(continuation3);
        Object object = function1.invoke(continuation4);
        InlineMarker.mark(1);
        return object;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final <T> Object suspendCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> block, Continuation<? super T> continuation2) {
        int $i$f$suspendCoroutineUninterceptedOrReturn = 0;
        throw (Throwable)new NotImplementedError("Implementation of suspendCoroutineUninterceptedOrReturn is intrinsic");
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final <T> Continuation<T> intercepted(@NotNull Continuation<? super T> $this$intercepted) {
        int $i$f$intercepted = 0;
        throw (Throwable)new NotImplementedError("Implementation of intercepted is intrinsic");
    }
}

