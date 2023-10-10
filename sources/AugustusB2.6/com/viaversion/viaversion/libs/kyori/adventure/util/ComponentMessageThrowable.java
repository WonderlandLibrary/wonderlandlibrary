// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface ComponentMessageThrowable
{
    @Nullable
    default Component getMessage(@Nullable final Throwable throwable) {
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable)throwable).componentMessage();
        }
        return null;
    }
    
    @Nullable
    default Component getOrConvertMessage(@Nullable final Throwable throwable) {
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable)throwable).componentMessage();
        }
        if (throwable != null) {
            final String message = throwable.getMessage();
            if (message != null) {
                return Component.text(message);
            }
        }
        return null;
    }
    
    @Nullable
    Component componentMessage();
}
