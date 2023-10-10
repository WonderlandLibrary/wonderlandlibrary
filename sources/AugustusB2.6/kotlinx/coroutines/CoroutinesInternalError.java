// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public final class CoroutinesInternalError extends Error
{
    public CoroutinesInternalError(final String message, final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        super(message, cause);
    }
}
