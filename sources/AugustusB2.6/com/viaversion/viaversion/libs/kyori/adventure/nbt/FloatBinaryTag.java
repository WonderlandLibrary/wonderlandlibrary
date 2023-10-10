// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface FloatBinaryTag extends NumberBinaryTag
{
    @NotNull
    default FloatBinaryTag of(final float value) {
        return new FloatBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<FloatBinaryTag> type() {
        return BinaryTagTypes.FLOAT;
    }
    
    float value();
}
