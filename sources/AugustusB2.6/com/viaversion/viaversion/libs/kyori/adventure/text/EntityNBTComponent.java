// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface EntityNBTComponent extends NBTComponent<EntityNBTComponent, Builder>, ScopedComponent<EntityNBTComponent>
{
    @NotNull
    String selector();
    
    @Contract(pure = true)
    @NotNull
    EntityNBTComponent selector(@NotNull final String selector);
    
    public interface Builder extends NBTComponentBuilder<EntityNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder selector(@NotNull final String selector);
    }
}
