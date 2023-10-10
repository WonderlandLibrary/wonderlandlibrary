// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.CancellationException;

public final class JobCancellationException extends CancellationException implements CopyableThrowable<JobCancellationException>
{
    private Job job;
    
    @Override
    public final Throwable fillInStackTrace() {
        if (DebugKt.getDEBUG()) {
            final Throwable fillInStackTrace = super.fillInStackTrace();
            Intrinsics.checkExpressionValueIsNotNull(fillInStackTrace, "super.fillInStackTrace()");
            return fillInStackTrace;
        }
        return this;
    }
    
    @Override
    public final String toString() {
        return super.toString() + "; job=" + this.job;
    }
    
    @Override
    public final boolean equals(final Object other) {
        return other == this || (other instanceof JobCancellationException && Intrinsics.areEqual(((JobCancellationException)other).getMessage(), this.getMessage()) && Intrinsics.areEqual(((JobCancellationException)other).job, this.job) && Intrinsics.areEqual(((JobCancellationException)other).getCause(), this.getCause()));
    }
    
    @Override
    public final int hashCode() {
        final String message = this.getMessage();
        if (message == null) {
            Intrinsics.throwNpe();
        }
        final int n = (message.hashCode() * 31 + this.job.hashCode()) * 31;
        final Throwable cause = this.getCause();
        return n + ((cause != null) ? cause.hashCode() : 0);
    }
    
    public JobCancellationException(final String message, final Throwable cause, final Job job) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(job, "job");
        super(message);
        this.job = job;
        if (cause != null) {
            this.initCause(cause);
        }
    }
}
