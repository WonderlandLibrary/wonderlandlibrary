// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class HoverEventActionSerializer
{
    static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;
    
    private HoverEventActionSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.of("hover action", HoverEvent.Action.NAMES);
    }
}
