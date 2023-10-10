// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible
public class VerifyException extends RuntimeException
{
    public VerifyException() {
    }
    
    public VerifyException(@Nullable final String message) {
        super(message);
    }
    
    public VerifyException(@Nullable final Throwable cause) {
        super(cause);
    }
    
    public VerifyException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
}
