// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;

public class UninitializedPropertyAccessException extends RuntimeException
{
    public UninitializedPropertyAccessException() {
    }
    
    public UninitializedPropertyAccessException(final String message) {
        super(message);
    }
    
    public UninitializedPropertyAccessException(final String message, final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        super(message, cause);
    }
}
