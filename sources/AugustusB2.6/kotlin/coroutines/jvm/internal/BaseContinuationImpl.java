// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.Unit;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.Result;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import java.io.Serializable;

public abstract class BaseContinuationImpl implements Serializable, Continuation<Object>, CoroutineStackFrame
{
    private final Continuation<Object> completion;
    
    @Override
    public final void resumeWith(Object result) {
        BaseContinuationImpl baseContinuationImpl = this;
        result = result;
        Continuation completion;
        while (true) {
            Intrinsics.checkParameterIsNotNull(baseContinuationImpl, "frame");
            final BaseContinuationImpl $this$with;
            final Continuation<Object> completion2 = ($this$with = baseContinuationImpl).completion;
            if (completion2 == null) {
                Intrinsics.throwNpe();
            }
            completion = completion2;
            try {
                if ((result = $this$with.invokeSuspend(result)) == CoroutineSingletons.COROUTINE_SUSPENDED) {
                    return;
                }
                final Result.Companion companion = Result.Companion;
                result = Result.constructor-impl(result);
            }
            catch (Throwable exception) {
                final Result.Companion companion2 = Result.Companion;
                result = Result.constructor-impl(Pixmap.createFailure(exception));
            }
            result = result;
            $this$with.releaseIntercepted();
            if (!(completion instanceof BaseContinuationImpl)) {
                break;
            }
            baseContinuationImpl = (BaseContinuationImpl)completion;
            result = result;
        }
        completion.resumeWith(result);
    }
    
    protected abstract Object invokeSuspend(final Object p0);
    
    protected void releaseIntercepted() {
    }
    
    public Continuation<Unit> create(final Object value, final Continuation<?> completion) {
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Continuation at ");
        final StackTraceElement stackTraceElement = ModuleNameRetriever.Cache.getStackTraceElement(this);
        return sb.append((stackTraceElement != null) ? ((StackTraceElement)stackTraceElement) : ((String)this.getClass().getName())).toString();
    }
    
    @Override
    public final CoroutineStackFrame getCallerFrame() {
        Continuation<Object> completion;
        if (!((completion = this.completion) instanceof CoroutineStackFrame)) {
            completion = null;
        }
        return (CoroutineStackFrame)completion;
    }
    
    @Override
    public final StackTraceElement getStackTraceElement() {
        return ModuleNameRetriever.Cache.getStackTraceElement(this);
    }
    
    public final Continuation<Object> getCompletion() {
        return this.completion;
    }
    
    public BaseContinuationImpl(final Continuation<Object> completion) {
        this.completion = completion;
    }
}
