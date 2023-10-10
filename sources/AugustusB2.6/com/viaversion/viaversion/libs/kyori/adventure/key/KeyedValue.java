// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public interface KeyedValue<T> extends Keyed
{
    @NotNull
    default <T> KeyedValue<T> of(@NotNull final Key key, @NotNull final T value) {
        return new KeyedValueImpl<T>(key, Objects.requireNonNull(value, "value"));
    }
    
    @NotNull
    T value();
}
