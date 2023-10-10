// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

public final class JsonNull extends JsonElement
{
    public static final JsonNull INSTANCE;
    
    @Deprecated
    public JsonNull() {
    }
    
    @Override
    public JsonNull deepCopy() {
        return JsonNull.INSTANCE;
    }
    
    @Override
    public int hashCode() {
        return JsonNull.class.hashCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof JsonNull;
    }
    
    static {
        INSTANCE = new JsonNull();
    }
}
