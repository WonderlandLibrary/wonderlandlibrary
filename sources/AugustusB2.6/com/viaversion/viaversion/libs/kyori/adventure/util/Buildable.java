// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public interface Buildable<R, B extends Builder<R>>
{
    @Contract(mutates = "param1")
    @NotNull
    default <R extends Buildable<R, B>, B extends Builder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
        if (consumer != null) {
            consumer.accept((Object)builder);
        }
        return builder.build();
    }
    
    @Contract(value = "-> new", pure = true)
    @NotNull
    B toBuilder();
    
    public interface Builder<R>
    {
        @Contract(value = "-> new", pure = true)
        @NotNull
        R build();
    }
}
