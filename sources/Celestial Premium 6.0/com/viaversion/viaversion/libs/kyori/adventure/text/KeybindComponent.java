/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.text.BuildableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScopedComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent
extends BuildableComponent<KeybindComponent, Builder>,
ScopedComponent<KeybindComponent> {
    @NotNull
    public String keybind();

    @Override
    @Contract(pure=true)
    @NotNull
    public KeybindComponent keybind(@NotNull String var1);

    public static interface Builder
    extends ComponentBuilder<KeybindComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder keybind(@NotNull String var1);
    }
}

