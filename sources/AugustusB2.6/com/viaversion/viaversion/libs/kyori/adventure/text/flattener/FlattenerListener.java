// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.flattener;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;

@FunctionalInterface
public interface FlattenerListener
{
    default void pushStyle(@NotNull final Style style) {
    }
    
    void component(@NotNull final String text);
    
    default void popStyle(@NotNull final Style style) {
    }
}
