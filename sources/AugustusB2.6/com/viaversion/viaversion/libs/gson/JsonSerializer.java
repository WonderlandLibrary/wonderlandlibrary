// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T>
{
    JsonElement serialize(final T p0, final Type p1, final JsonSerializationContext p2);
}
