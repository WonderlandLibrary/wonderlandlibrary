// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public enum TriState
{
    NOT_SET, 
    FALSE, 
    TRUE;
    
    @NotNull
    public static TriState byBoolean(final boolean value) {
        return value ? TriState.TRUE : TriState.FALSE;
    }
    
    @NotNull
    public static TriState byBoolean(@Nullable final Boolean value) {
        return (value == null) ? TriState.NOT_SET : byBoolean((boolean)value);
    }
}
