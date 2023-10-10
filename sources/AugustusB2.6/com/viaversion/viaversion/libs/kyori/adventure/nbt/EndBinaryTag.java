// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface EndBinaryTag extends BinaryTag
{
    @NotNull
    default EndBinaryTag get() {
        return EndBinaryTagImpl.INSTANCE;
    }
    
    @NotNull
    default BinaryTagType<EndBinaryTag> type() {
        return BinaryTagTypes.END;
    }
}
