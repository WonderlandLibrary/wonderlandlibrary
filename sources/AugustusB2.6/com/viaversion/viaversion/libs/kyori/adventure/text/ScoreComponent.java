// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ScoreComponent extends BuildableComponent<ScoreComponent, Builder>, ScopedComponent<ScoreComponent>
{
    @NotNull
    String name();
    
    @Contract(pure = true)
    @NotNull
    ScoreComponent name(@NotNull final String name);
    
    @NotNull
    String objective();
    
    @Contract(pure = true)
    @NotNull
    ScoreComponent objective(@NotNull final String objective);
    
    @Deprecated
    @Nullable
    String value();
    
    @Deprecated
    @Contract(pure = true)
    @NotNull
    ScoreComponent value(@Nullable final String value);
    
    public interface Builder extends ComponentBuilder<ScoreComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder name(@NotNull final String name);
        
        @Contract("_ -> this")
        @NotNull
        Builder objective(@NotNull final String objective);
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        Builder value(@Nullable final String value);
    }
}
