// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface StringBinaryTag extends BinaryTag
{
    @NotNull
    default StringBinaryTag of(@NotNull final String value) {
        return new StringBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<StringBinaryTag> type() {
        return BinaryTagTypes.STRING;
    }
    
    @NotNull
    String value();
}
