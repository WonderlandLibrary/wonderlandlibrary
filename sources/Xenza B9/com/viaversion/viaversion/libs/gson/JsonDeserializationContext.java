// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext
{
     <T> T deserialize(final JsonElement p0, final Type p1) throws JsonParseException;
}
