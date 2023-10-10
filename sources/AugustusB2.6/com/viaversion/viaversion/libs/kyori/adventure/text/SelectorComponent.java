// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface SelectorComponent extends BuildableComponent<SelectorComponent, Builder>, ScopedComponent<SelectorComponent>
{
    @NotNull
    String pattern();
    
    @Contract(pure = true)
    @NotNull
    SelectorComponent pattern(@NotNull final String pattern);
    
    @Nullable
    Component separator();
    
    @NotNull
    SelectorComponent separator(@Nullable final ComponentLike separator);
    
    public interface Builder extends ComponentBuilder<SelectorComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder pattern(@NotNull final String pattern);
        
        @Contract("_ -> this")
        @NotNull
        Builder separator(@Nullable final ComponentLike separator);
    }
}
