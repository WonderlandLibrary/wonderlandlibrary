// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;

final class LazyDeferredCoroutine<T> extends DeferredCoroutine<T>
{
    private Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block;
    
    @Override
    protected final void onStart() {
        final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block2;
        if ((block2 = this.block) == null) {
            throw new IllegalStateException("Already started".toString());
        }
        final Function2 block = block2;
        this.block = null;
        CancellableKt.startCoroutineCancellable((Function2<? super LazyDeferredCoroutine, ? super Continuation<? super Object>, ?>)block, this, (Continuation<? super Object>)this);
    }
    
    public LazyDeferredCoroutine(final CoroutineContext parentContext, final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(block, "block");
        super(parentContext, false);
        this.block = block;
    }
}
