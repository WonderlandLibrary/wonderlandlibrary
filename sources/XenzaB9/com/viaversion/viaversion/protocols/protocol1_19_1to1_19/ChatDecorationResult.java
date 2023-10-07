// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_1to1_19;

import com.viaversion.viaversion.libs.gson.JsonElement;

public final class ChatDecorationResult
{
    private final JsonElement content;
    private final boolean overlay;
    
    public ChatDecorationResult(final JsonElement content, final boolean overlay) {
        this.content = content;
        this.overlay = overlay;
    }
    
    public JsonElement content() {
        return this.content;
    }
    
    public boolean overlay() {
        return this.overlay;
    }
}
