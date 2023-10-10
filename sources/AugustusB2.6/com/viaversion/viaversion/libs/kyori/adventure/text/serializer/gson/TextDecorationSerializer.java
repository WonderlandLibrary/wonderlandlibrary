// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class TextDecorationSerializer
{
    static final TypeAdapter<TextDecoration> INSTANCE;
    
    private TextDecorationSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.of("text decoration", TextDecoration.NAMES);
    }
}
