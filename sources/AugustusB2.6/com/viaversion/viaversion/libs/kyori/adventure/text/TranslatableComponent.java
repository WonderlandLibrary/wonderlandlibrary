// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.List;
import org.jetbrains.annotations.Contract;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.NotNull;

public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, Builder>, ScopedComponent<TranslatableComponent>
{
    @NotNull
    String key();
    
    @Contract(pure = true)
    @NotNull
    default TranslatableComponent key(@NotNull final Translatable translatable) {
        return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
    }
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent key(@NotNull final String key);
    
    @NotNull
    List<Component> args();
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent args(@NotNull final ComponentLike... args);
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent args(@NotNull final List<? extends ComponentLike> args);
    
    public interface Builder extends ComponentBuilder<TranslatableComponent, Builder>
    {
        @Contract(pure = true)
        @NotNull
        default Builder key(@NotNull final Translatable translatable) {
            return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder key(@NotNull final String key);
        
        @Contract("_ -> this")
        @NotNull
        Builder args(@NotNull final ComponentBuilder<?, ?> arg);
        
        @Contract("_ -> this")
        @NotNull
        Builder args(@NotNull final ComponentBuilder<?, ?>... args);
        
        @Contract("_ -> this")
        @NotNull
        Builder args(@NotNull final Component arg);
        
        @Contract("_ -> this")
        @NotNull
        Builder args(@NotNull final ComponentLike... args);
        
        @Contract("_ -> this")
        @NotNull
        Builder args(@NotNull final List<? extends ComponentLike> args);
    }
}
