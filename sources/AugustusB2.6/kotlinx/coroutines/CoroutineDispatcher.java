// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.EmptyCoroutineContext;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.AbstractCoroutineContextElement;

public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor
{
    public boolean isDispatchNeeded(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return true;
    }
    
    public abstract void dispatch(final CoroutineContext p0, final Runnable p1);
    
    @Override
    public final <T> Continuation<T> interceptContinuation(final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return new DispatchedContinuation<T>(this, continuation);
    }
    
    @Override
    public String toString() {
        return Pixmap.getClassSimpleName(this) + '@' + Pixmap.getHexAddress(this);
    }
    
    public CoroutineDispatcher() {
        super(ContinuationInterceptor.Key);
    }
    
    @Override
    public final <E extends Element> E get(final CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (key == ContinuationInterceptor.Key) {
            return (E)this;
        }
        return null;
    }
    
    @Override
    public final CoroutineContext minusKey(final CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (key == ContinuationInterceptor.Key) {
            return EmptyCoroutineContext.INSTANCE;
        }
        return this;
    }
    
    @Override
    public final void releaseInterceptedContinuation(Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        continuation = continuation;
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
    }
}
