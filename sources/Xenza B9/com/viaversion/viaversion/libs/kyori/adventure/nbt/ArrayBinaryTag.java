// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface ArrayBinaryTag extends BinaryTag
{
    @NotNull
    BinaryTagType<? extends ArrayBinaryTag> type();
}
