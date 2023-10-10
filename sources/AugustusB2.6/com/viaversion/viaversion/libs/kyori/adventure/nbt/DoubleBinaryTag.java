// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface DoubleBinaryTag extends NumberBinaryTag
{
    @NotNull
    default DoubleBinaryTag of(final double value) {
        return new DoubleBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<DoubleBinaryTag> type() {
        return BinaryTagTypes.DOUBLE;
    }
    
    double value();
}
