// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.ExceptionsKt__ExceptionsKt;
import kotlinx.coroutines.scheduling.TaskContext;
import kotlin.Unit;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlin.Result;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;
import com.badlogic.gdx.graphics.Pixmap;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.scheduling.Task;

public abstract class DispatchedTask<T> extends Task
{
    public int resumeMode;
    
    public abstract Continuation<T> getDelegate$kotlinx_coroutines_core();
    
    public abstract Object takeState$kotlinx_coroutines_core();
    
    public void cancelResult$kotlinx_coroutines_core(final Object state, final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
    }
    
    public <T> T getSuccessfulResult$kotlinx_coroutines_core(final Object state) {
        return (T)state;
    }
    
    public static Throwable getExceptionalResult$kotlinx_coroutines_core(final Object state) {
        Object o = state;
        if (!(state instanceof CompletedExceptionally)) {
            o = null;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }
    
    @Override
    public final void run() {
        final TaskContext taskContext = this.taskContext;
        try {
            final Continuation<T> delegate$kotlinx_coroutines_core = this.getDelegate$kotlinx_coroutines_core();
            if (delegate$kotlinx_coroutines_core == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>");
            }
            final DispatchedContinuation delegate;
            final Object continuation;
            final CoroutineContext context = ((Continuation)(continuation = (delegate = (DispatchedContinuation)delegate$kotlinx_coroutines_core).continuation)).getContext();
            Object state = this.takeState$kotlinx_coroutines_core();
            final Object countOrElement$iv = delegate.countOrElement;
            final Object oldValue$iv = ThreadContextKt.updateThreadContext(context, countOrElement$iv);
            try {
                final Throwable exception = getExceptionalResult$kotlinx_coroutines_core(state);
                final Job job = Pixmap.isCancellableMode(this.resumeMode) ? context.get((CoroutineContext.Key<Job>)Job.Key) : null;
                if (exception == null && job != null && !job.isActive()) {
                    final CancellationException cause = job.getCancellationException();
                    this.cancelResult$kotlinx_coroutines_core(state, cause);
                    final Continuation $this$resumeWithStackTrace$iv = (Continuation)continuation;
                    final Result.Companion companion = Result.Companion;
                    state = StackTraceRecoveryKt.recoverStackTrace(cause, $this$resumeWithStackTrace$iv);
                    final Continuation continuation2 = $this$resumeWithStackTrace$iv;
                    state = Result.constructor-impl(Pixmap.createFailure((Throwable)state));
                    continuation2.resumeWith(state);
                }
                else if (exception != null) {
                    final Continuation $this$resumeWithStackTrace$iv2 = (Continuation)continuation;
                    final Result.Companion companion2 = Result.Companion;
                    final Throwable recoverStackTrace = StackTraceRecoveryKt.recoverStackTrace(exception, $this$resumeWithStackTrace$iv2);
                    state = $this$resumeWithStackTrace$iv2;
                    ((Continuation)state).resumeWith(Result.constructor-impl(Pixmap.createFailure(recoverStackTrace)));
                }
                else {
                    final Continuation continuation3 = (Continuation)continuation;
                    final Object successfulResult$kotlinx_coroutines_core = this.getSuccessfulResult$kotlinx_coroutines_core(state);
                    final Continuation continuation4 = continuation3;
                    final Result.Companion companion3 = Result.Companion;
                    continuation4.resumeWith(Result.constructor-impl(successfulResult$kotlinx_coroutines_core));
                }
                final Unit instance = Unit.INSTANCE;
            }
            finally {
                ThreadContextKt.restoreThreadContext(context, oldValue$iv);
            }
        }
        catch (Throwable fatalException) {
            Object o;
            try {
                final Result.Companion companion4 = Result.Companion;
                taskContext.afterTask();
                o = Result.constructor-impl(Unit.INSTANCE);
            }
            catch (Throwable exception2) {
                final Result.Companion companion5 = Result.Companion;
                o = Result.constructor-impl(Pixmap.createFailure(exception2));
            }
            final Object result = o;
            this.handleFatalException$kotlinx_coroutines_core(fatalException, Result.exceptionOrNull-impl(result));
        }
        finally {
            Object o2;
            try {
                final Result.Companion companion6 = Result.Companion;
                taskContext.afterTask();
                o2 = Result.constructor-impl(Unit.INSTANCE);
            }
            catch (Throwable exception3) {
                final Result.Companion companion7 = Result.Companion;
                o2 = Result.constructor-impl(Pixmap.createFailure(exception3));
            }
            final Object result2 = o2;
            this.handleFatalException$kotlinx_coroutines_core(null, Result.exceptionOrNull-impl(result2));
        }
    }
    
    public final void handleFatalException$kotlinx_coroutines_core(final Throwable exception, final Throwable finallyException) {
        if (exception == null && finallyException == null) {
            return;
        }
        if (exception != null && finallyException != null) {
            ExceptionsKt__ExceptionsKt.addSuppressed(exception, finallyException);
        }
        Throwable t;
        if ((t = exception) == null) {
            t = finallyException;
        }
        final Throwable cause = t;
        final String string = "Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers";
        final Throwable cause2 = cause;
        if (cause2 == null) {
            Intrinsics.throwNpe();
        }
        final CoroutinesInternalError reason = new CoroutinesInternalError(string, cause2);
        AwaitKt.handleCoroutineException(this.getDelegate$kotlinx_coroutines_core().getContext(), reason);
    }
    
    public DispatchedTask(final int resumeMode) {
        this.resumeMode = resumeMode;
    }
}
