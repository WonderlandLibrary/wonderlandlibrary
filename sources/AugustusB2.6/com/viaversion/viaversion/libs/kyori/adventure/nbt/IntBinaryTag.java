// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface IntBinaryTag extends NumberBinaryTag
{
    @NotNull
    default IntBinaryTag of(final int value) {
        return new IntBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<IntBinaryTag> type() {
        return BinaryTagTypes.INT;
    }
    
    int value();
}
