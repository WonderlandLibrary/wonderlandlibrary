// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, Builder>, ScopedComponent<KeybindComponent>
{
    @NotNull
    String keybind();
    
    @Contract(pure = true)
    @NotNull
    KeybindComponent keybind(@NotNull final String keybind);
    
    @Contract(pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final KeybindLike keybind) {
        return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
    }
    
    public interface Builder extends ComponentBuilder<KeybindComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder keybind(@NotNull final String keybind);
        
        @Contract(pure = true)
        @NotNull
        default Builder keybind(@NotNull final KeybindLike keybind) {
            return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
        }
    }
    
    public interface KeybindLike
    {
        @NotNull
        String asKeybind();
    }
}
