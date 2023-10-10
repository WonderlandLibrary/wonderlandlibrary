// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

public enum LongSerializationPolicy
{
    DEFAULT(0) {
        @Override
        public JsonElement serialize(final Long value) {
            return new JsonPrimitive(value);
        }
    }, 
    STRING(1) {
        @Override
        public JsonElement serialize(final Long value) {
            return new JsonPrimitive(String.valueOf(value));
        }
    };
    
    public abstract JsonElement serialize(final Long p0);
}
