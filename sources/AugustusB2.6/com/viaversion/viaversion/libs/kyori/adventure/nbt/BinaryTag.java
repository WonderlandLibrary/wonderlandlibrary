// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface BinaryTag extends BinaryTagLike, Examinable
{
    @NotNull
    BinaryTagType<? extends BinaryTag> type();
    
    @NotNull
    default BinaryTag asBinaryTag() {
        return this;
    }
}
