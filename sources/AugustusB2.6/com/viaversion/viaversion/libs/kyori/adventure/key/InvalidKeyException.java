// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public final class InvalidKeyException extends RuntimeException
{
    private static final long serialVersionUID = -5413304087321449434L;
    private final String keyNamespace;
    private final String keyValue;
    
    InvalidKeyException(@NotNull final String keyNamespace, @NotNull final String keyValue, @Nullable final String message) {
        super(message);
        this.keyNamespace = keyNamespace;
        this.keyValue = keyValue;
    }
    
    @NotNull
    public final String keyNamespace() {
        return this.keyNamespace;
    }
    
    @NotNull
    public final String keyValue() {
        return this.keyValue;
    }
}
