// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentApplicable
{
    @NotNull
    Component componentApply(@NotNull final Component component);
}
