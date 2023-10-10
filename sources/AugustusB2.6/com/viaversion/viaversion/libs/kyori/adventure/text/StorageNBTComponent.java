// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, Builder>, ScopedComponent<StorageNBTComponent>
{
    @NotNull
    Key storage();
    
    @Contract(pure = true)
    @NotNull
    StorageNBTComponent storage(@NotNull final Key storage);
    
    public interface Builder extends NBTComponentBuilder<StorageNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder storage(@NotNull final Key storage);
    }
}
