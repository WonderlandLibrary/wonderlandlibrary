// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public class UncheckedTimeoutException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    public UncheckedTimeoutException() {
    }
    
    public UncheckedTimeoutException(@Nullable final String message) {
        super(message);
    }
    
    public UncheckedTimeoutException(@Nullable final Throwable cause) {
        super(cause);
    }
    
    public UncheckedTimeoutException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
}
