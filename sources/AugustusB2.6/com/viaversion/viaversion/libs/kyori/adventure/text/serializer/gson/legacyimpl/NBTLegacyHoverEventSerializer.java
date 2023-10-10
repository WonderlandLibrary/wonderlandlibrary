// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl;

import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;

public interface NBTLegacyHoverEventSerializer extends LegacyHoverEventSerializer
{
    @NotNull
    default LegacyHoverEventSerializer get() {
        return NBTLegacyHoverEventSerializerImpl.INSTANCE;
    }
}
