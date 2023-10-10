// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import java.io.IOException;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;

public interface LegacyHoverEventSerializer
{
    HoverEvent.ShowItem deserializeShowItem(@NotNull final Component input) throws IOException;
    
    HoverEvent.ShowEntity deserializeShowEntity(@NotNull final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentDecoder) throws IOException;
    
    @NotNull
    Component serializeShowItem(final HoverEvent.ShowItem input) throws IOException;
    
    @NotNull
    Component serializeShowEntity(final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException;
}
