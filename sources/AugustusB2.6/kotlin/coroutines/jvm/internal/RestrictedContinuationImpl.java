// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.CoroutineContext;

public abstract class RestrictedContinuationImpl extends BaseContinuationImpl
{
    @Override
    public final CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
    
    public RestrictedContinuationImpl(Continuation<Object> completion) {
        super(completion);
        if (completion == null) {
            return;
        }
        completion = completion;
        completion = completion;
        final boolean b;
        if (!(b = (completion.getContext() == EmptyCoroutineContext.INSTANCE))) {
            throw new IllegalArgumentException("Coroutines with restricted suspension must have EmptyCoroutineContext".toString());
        }
    }
}
