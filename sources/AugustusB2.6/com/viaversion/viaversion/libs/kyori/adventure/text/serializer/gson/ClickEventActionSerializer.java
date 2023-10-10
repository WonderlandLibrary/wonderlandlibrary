// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class ClickEventActionSerializer
{
    static final TypeAdapter<ClickEvent.Action> INSTANCE;
    
    private ClickEventActionSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.of("click action", ClickEvent.Action.NAMES);
    }
}
