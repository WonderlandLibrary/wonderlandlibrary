// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import com.badlogic.gdx.graphics.Pixmap;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.CancellationException;
import kotlin.UninitializedPropertyAccessException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;

public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CoroutineStackFrame, CancellableContinuation<T>
{
    private final CoroutineContext context;
    private volatile int _decision;
    private static final AtomicIntegerFieldUpdater _decision$FU;
    private volatile Object _state;
    private static final AtomicReferenceFieldUpdater _state$FU;
    private volatile DisposableHandle parentHandle;
    private final Continuation<T> delegate;
    
    @Override
    public final CoroutineContext getContext() {
        return this.context;
    }
    
    @Override
    public final boolean isCompleted() {
        return !(this._state instanceof NotCompleted);
    }
    
    @Override
    public final CoroutineStackFrame getCallerFrame() {
        Continuation<T> delegate;
        if (!((delegate = this.delegate) instanceof CoroutineStackFrame)) {
            delegate = null;
        }
        return (CoroutineStackFrame)delegate;
    }
    
    @Override
    public final StackTraceElement getStackTraceElement() {
        return null;
    }
    
    @Override
    public final Object takeState$kotlinx_coroutines_core() {
        return this._state;
    }
    
    @Override
    public final void cancelResult$kotlinx_coroutines_core(final Object state, final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        if (state instanceof CompletedWithCancellation) {
            final CancellableContinuationImpl this_$iv = this;
            try {
                ((CompletedWithCancellation)state).onCancellation.invoke(cause);
            }
            catch (Throwable ex$iv) {
                AwaitKt.handleCoroutineException(this_$iv.context, new UninitializedPropertyAccessException("Exception in cancellation handler for " + this_$iv, ex$iv));
            }
        }
    }
    
    public final boolean cancel(final Throwable ex$iv) {
        Object state;
        while ((state = this._state) instanceof NotCompleted) {
            final CancelledContinuation update = new CancelledContinuation(this, cause, state instanceof CancelHandler);
            if (CancellableContinuationImpl._state$FU.compareAndSet(this, state, update)) {
                if (state instanceof CancelHandler) {
                    final CancellableContinuationImpl this_$iv = this;
                    try {
                        ((CancelHandler)state).invoke(cause);
                    }
                    catch (Throwable ex$iv) {
                        AwaitKt.handleCoroutineException(this_$iv.context, new UninitializedPropertyAccessException("Exception in cancellation handler for " + this_$iv, ex$iv));
                    }
                }
                this.disposeParentHandle();
                this.dispatchResume(0);
                return true;
            }
        }
        return false;
    }
    
    public static Throwable getContinuationCancellationCause(final Job parent) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        return parent.getCancellationException();
    }
    
    private final boolean tryResume() {
        CancellableContinuationImpl $this$loop$iv = this;
        while (true) {
            switch ($this$loop$iv._decision) {
                case 0: {
                    if (CancellableContinuationImpl._decision$FU.compareAndSet(this, 0, 2)) {
                        return true;
                    }
                    continue;
                }
                case 1: {
                    return false;
                }
                default: {
                    $this$loop$iv = (CancellableContinuationImpl)"Already resumed";
                    throw new IllegalStateException($this$loop$iv.toString());
                }
            }
        }
    }
    
    public final Object getResult() {
        if (!this.isCompleted()) {
            final Job job2 = this.delegate.getContext().get((CoroutineContext.Key<Job>)Job.Key);
            if (job2 != null) {
                final Job parent = job2;
                job2.start();
                final DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(parent, true, false, new ChildContinuation(parent, this), 2, null);
                this.parentHandle = invokeOnCompletion$default;
                if (this.isCompleted()) {
                    invokeOnCompletion$default.dispose();
                    this.parentHandle = NonDisposableHandle.INSTANCE;
                }
            }
        }
        boolean b = false;
    Label_0186:
        while (true) {
            switch (this._decision) {
                case 0: {
                    if (CancellableContinuationImpl._decision$FU.compareAndSet(this, 0, 1)) {
                        b = true;
                        break Label_0186;
                    }
                    continue;
                }
                case 2: {
                    b = false;
                    break Label_0186;
                }
                default: {
                    throw new IllegalStateException("Already suspended".toString());
                }
            }
        }
        if (b) {
            return CoroutineSingletons.COROUTINE_SUSPENDED;
        }
        final Object state;
        if ((state = this._state) instanceof CompletedExceptionally) {
            throw StackTraceRecoveryKt.recoverStackTrace(((CompletedExceptionally)state).cause, this);
        }
        final Job job;
        if (this.resumeMode == 1 && (job = this.context.get((CoroutineContext.Key<Job>)Job.Key)) != null && !job.isActive()) {
            final CancellationException cause = job.getCancellationException();
            this.cancelResult$kotlinx_coroutines_core(state, cause);
            throw StackTraceRecoveryKt.recoverStackTrace(cause, this);
        }
        return this.getSuccessfulResult$kotlinx_coroutines_core(state);
    }
    
    @Override
    public final void resumeWith(Object result) {
        final Object state = Pixmap.toState(result);
        final int resumeMode = this.resumeMode;
        final Object o = state;
        CancelledContinuation state2;
        CancelledContinuation cancelledContinuation;
        while ((cancelledContinuation = (state2 = (CancelledContinuation)this._state)) instanceof NotCompleted) {
            if (CancellableContinuationImpl._state$FU.compareAndSet(this, state2, o)) {
                this.disposeParentHandle();
                this.dispatchResume(resumeMode);
                return;
            }
        }
        if (cancelledContinuation instanceof CancelledContinuation && state2.makeResumed()) {
            final CancelledContinuation cancelledContinuation2 = state2;
            return;
        }
        result = o;
        throw new IllegalStateException(("Already resumed, but proposed with update " + result).toString());
    }
    
    @Override
    public final void invokeOnCancellation(Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        CancelHandler cancelHandler = null;
        final CancellableContinuationImpl $this$loop$iv = this;
        while (true) {
            final Object state;
            final Object o;
            if ((o = (state = $this$loop$iv._state)) instanceof Active) {
                CancelHandler cancelHandler2;
                if ((cancelHandler2 = cancelHandler) == null) {
                    final CancelHandler handler2;
                    CancelHandler cancelHandler3 = null;
                    InvokeOnCancel invokeOnCancel = null;
                    if ((handler2 = handler) instanceof CancelHandler) {
                        cancelHandler3 = handler2;
                    }
                    else {
                        invokeOnCancel = new InvokeOnCancel(handler2);
                    }
                    final InvokeOnCancel invokeOnCancel2 = invokeOnCancel;
                    cancelHandler = cancelHandler3;
                    cancelHandler2 = invokeOnCancel2;
                }
                final CancelHandler node = cancelHandler2;
                if (CancellableContinuationImpl._state$FU.compareAndSet(this, state, node)) {
                    return;
                }
                continue;
            }
            else {
                if (!(o instanceof CancelHandler)) {
                    if (o instanceof CancelledContinuation) {
                        if (!((CancelledContinuation)state).makeHandled()) {
                            multipleHandlersError(handler, state);
                        }
                        final CancellableContinuationImpl this_$iv = this;
                        try {
                            handler = handler;
                            Object o2;
                            if (!((o2 = state) instanceof CompletedExceptionally)) {
                                o2 = null;
                            }
                            final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o2;
                            final Throwable cause$iv = (completedExceptionally != null) ? completedExceptionally.cause : null;
                            handler.invoke(cause$iv);
                        }
                        catch (Throwable ex$iv) {
                            AwaitKt.handleCoroutineException(this_$iv.context, new UninitializedPropertyAccessException("Exception in cancellation handler for " + this_$iv, ex$iv));
                        }
                    }
                    return;
                }
                multipleHandlersError(handler, state);
            }
        }
    }
    
    private static void multipleHandlersError(final Function1<? super Throwable, Unit> handler, final Object state) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + handler + ", already has " + state).toString());
    }
    
    private final void dispatchResume(final int mode) {
        if (this.tryResume()) {
            return;
        }
        DispatchedKt.dispatch((DispatchedTask<? super Object>)this, mode);
    }
    
    private final void disposeParentHandle() {
        final DisposableHandle parentHandle = this.parentHandle;
        if (parentHandle != null) {
            parentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }
    
    @Override
    public final Object tryResumeWithException(final Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        final CancellableContinuationImpl $this$loop$iv = this;
        Object state;
        while ((state = $this$loop$iv._state) instanceof NotCompleted) {
            final CompletedExceptionally update = new CompletedExceptionally(exception, false, 2);
            if (CancellableContinuationImpl._state$FU.compareAndSet(this, state, update)) {
                this.disposeParentHandle();
                return state;
            }
        }
        return null;
    }
    
    @Override
    public final void completeResume(final Object token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        this.dispatchResume(this.resumeMode);
    }
    
    @Override
    public final <T> T getSuccessfulResult$kotlinx_coroutines_core(final Object state) {
        if (state instanceof CompletedIdempotentResult) {
            return (T)((CompletedIdempotentResult)state).result;
        }
        if (state instanceof CompletedWithCancellation) {
            return (T)((CompletedWithCancellation)state).result;
        }
        return (T)state;
    }
    
    @Override
    public String toString() {
        return "CancellableContinuation" + '(' + Pixmap.toDebugString(this.delegate) + "){" + this._state + "}@" + Pixmap.getHexAddress(this);
    }
    
    @Override
    public final Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this.delegate;
    }
    
    public CancellableContinuationImpl(final Continuation<? super T> delegate, final int resumeMode) {
        Intrinsics.checkParameterIsNotNull(delegate, "delegate");
        super(1);
        this.delegate = (Continuation<T>)delegate;
        this.context = this.delegate.getContext();
        this._decision = 0;
        this._state = Active.INSTANCE;
    }
    
    static {
        _decision$FU = AtomicIntegerFieldUpdater.newUpdater(CancellableContinuationImpl.class, "_decision");
        _state$FU = AtomicReferenceFieldUpdater.newUpdater(CancellableContinuationImpl.class, Object.class, "_state");
    }
}
