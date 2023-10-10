// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import org.jetbrains.annotations.Contract;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilderApplicable;

@FunctionalInterface
public interface StyleBuilderApplicable extends ComponentBuilderApplicable
{
    @Contract(mutates = "param")
    void styleApply(final Style.Builder style);
    
    default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
        component.style(this::styleApply);
    }
}
