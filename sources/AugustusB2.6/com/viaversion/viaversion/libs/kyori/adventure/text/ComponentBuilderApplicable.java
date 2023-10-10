// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentBuilderApplicable
{
    @Contract(mutates = "param")
    void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component);
}
