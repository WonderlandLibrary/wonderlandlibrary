// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface Merger
{
    void mergeColor(final StyleImpl.BuilderImpl target, @Nullable final TextColor color);
    
    void mergeDecoration(final StyleImpl.BuilderImpl target, @NotNull final TextDecoration decoration, final TextDecoration.State state);
    
    void mergeClickEvent(final StyleImpl.BuilderImpl target, @Nullable final ClickEvent event);
    
    void mergeHoverEvent(final StyleImpl.BuilderImpl target, @Nullable final HoverEvent<?> event);
    
    void mergeInsertion(final StyleImpl.BuilderImpl target, @Nullable final String insertion);
    
    void mergeFont(final StyleImpl.BuilderImpl target, @Nullable final Key font);
}
