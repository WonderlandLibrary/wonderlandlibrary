// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import com.badlogic.gdx.utils.GdxRuntimeException;
import kotlin.coroutines.ContinuationInterceptor;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlin.jvm.functions.Function2;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.Continuation;

public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope, Job
{
    private final CoroutineContext context;
    private CoroutineContext parentContext;
    
    @Override
    public final CoroutineContext getContext() {
        return this.context;
    }
    
    @Override
    public final CoroutineContext getCoroutineContext() {
        return this.context;
    }
    
    @Override
    public final boolean isActive() {
        return super.isActive();
    }
    
    protected void onStart() {
    }
    
    @Override
    public final void onStartInternal$kotlinx_coroutines_core() {
        this.onStart();
    }
    
    @Override
    protected final void onCompletionInternal(final Object state) {
        if (state instanceof CompletedExceptionally) {
            final Throwable cause = ((CompletedExceptionally)state).cause;
            ((CompletedExceptionally)state).getHandled();
            Intrinsics.checkParameterIsNotNull(cause, "cause");
        }
    }
    
    @Override
    public final void resumeWith(final Object result) {
        this.makeCompletingOnce$kotlinx_coroutines_core(Pixmap.toState(result), 0);
    }
    
    @Override
    public final void handleOnCompletionException$kotlinx_coroutines_core(final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        AwaitKt.handleCoroutineException(this.context, exception);
    }
    
    @Override
    public final String nameString$kotlinx_coroutines_core() {
        final String coroutineName2 = CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName2 == null) {
            return super.nameString$kotlinx_coroutines_core();
        }
        final String coroutineName = coroutineName2;
        return "\"" + coroutineName + "\":" + super.nameString$kotlinx_coroutines_core();
    }
    
    public final <R> void start(final CoroutineStart start, final R receiver, final Function2<? super R, ? super Continuation<? super T>, ?> block) {
        Intrinsics.checkParameterIsNotNull(start, "start");
        Intrinsics.checkParameterIsNotNull(block, "block");
        final Job job = this.parentContext.get((CoroutineContext.Key<Job>)Job.Key);
        if (DebugKt.getASSERTIONS_ENABLED() && super.parentHandle != null) {
            throw new AssertionError();
        }
        if (job == null) {
            super.parentHandle = NonDisposableHandle.INSTANCE;
        }
        else {
            job.start();
            final ChildHandle attachChild = job.attachChild(this);
            super.parentHandle = attachChild;
            if (this.isCompleted()) {
                attachChild.dispose();
                super.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
        final AbstractCoroutine abstractCoroutine = this;
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(abstractCoroutine, "completion");
        switch (CoroutineStart$WhenMappings.$EnumSwitchMapping$1[start.ordinal()]) {
            case 1: {
                CancellableKt.startCoroutineCancellable((Function2<? super R, ? super Continuation<? super Object>, ?>)block, receiver, (Continuation<? super Object>)abstractCoroutine);
            }
            case 2: {
                ContinuationInterceptor.DefaultImpls.startCoroutine((Function2<? super R, ? super Continuation<? super Object>, ?>)block, receiver, (Continuation<? super Object>)abstractCoroutine);
            }
            case 3: {
                CancellableKt.startCoroutineUndispatched((Function2<? super R, ? super Continuation<? super Object>, ?>)block, receiver, (Continuation<? super Object>)abstractCoroutine);
            }
            case 4: {}
            default: {
                throw new GdxRuntimeException();
            }
        }
    }
    
    public AbstractCoroutine(final CoroutineContext parentContext, final boolean active) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        super(active);
        this.parentContext = parentContext;
        this.context = this.parentContext.plus(this);
    }
}
