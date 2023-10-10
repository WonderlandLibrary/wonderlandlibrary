// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.event;

import org.jetbrains.annotations.NotNull;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Nullable;

public interface HoverEventSource<V>
{
    @Nullable
    default <V> HoverEvent<V> unbox(@Nullable final HoverEventSource<V> source) {
        return (source != null) ? source.asHoverEvent() : null;
    }
    
    @NotNull
    default HoverEvent<V> asHoverEvent() {
        return this.asHoverEvent(UnaryOperator.identity());
    }
    
    @NotNull
    HoverEvent<V> asHoverEvent(@NotNull final UnaryOperator<V> op);
}
