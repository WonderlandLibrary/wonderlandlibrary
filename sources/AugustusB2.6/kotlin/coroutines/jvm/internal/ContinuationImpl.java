// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.Continuation;

public abstract class ContinuationImpl extends BaseContinuationImpl
{
    private transient Continuation<Object> intercepted;
    private final CoroutineContext _context;
    
    @Override
    public final CoroutineContext getContext() {
        final CoroutineContext context = this._context;
        if (context == null) {
            Intrinsics.throwNpe();
        }
        return context;
    }
    
    public final Continuation<Object> intercepted() {
        Continuation<Object> intercepted;
        if ((intercepted = this.intercepted) == null) {
            final ContinuationInterceptor continuationInterceptor = this.getContext().get((CoroutineContext.Key<ContinuationInterceptor>)ContinuationInterceptor.Key);
            final Continuation it;
            final Continuation<Object> continuation = (Continuation<Object>)(it = ((continuationInterceptor != null) ? continuationInterceptor.interceptContinuation(this) : ((ContinuationImpl)this)));
            this.intercepted = (Continuation<Object>)it;
            intercepted = continuation;
        }
        return intercepted;
    }
    
    @Override
    protected final void releaseIntercepted() {
        final Continuation intercepted;
        if ((intercepted = this.intercepted) != null && intercepted != this) {
            final ContinuationInterceptor value = this.getContext().get((CoroutineContext.Key<ContinuationInterceptor>)ContinuationInterceptor.Key);
            if (value == null) {
                Intrinsics.throwNpe();
            }
            value.releaseInterceptedContinuation(intercepted);
        }
        this.intercepted = CompletedContinuation.INSTANCE;
    }
    
    public ContinuationImpl(final Continuation<Object> completion, final CoroutineContext _context) {
        super(completion);
        this._context = _context;
    }
    
    public ContinuationImpl(final Continuation<Object> completion) {
        this(completion, (completion != null) ? completion.getContext() : null);
    }
}
