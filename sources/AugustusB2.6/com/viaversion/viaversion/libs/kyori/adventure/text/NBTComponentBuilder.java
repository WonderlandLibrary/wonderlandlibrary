// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface NBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends ComponentBuilder<C, B>
{
    @Contract("_ -> this")
    @NotNull
    B nbtPath(@NotNull final String nbtPath);
    
    @Contract("_ -> this")
    @NotNull
    B interpret(final boolean interpret);
    
    @Contract("_ -> this")
    @NotNull
    B separator(@Nullable final ComponentLike separator);
}
