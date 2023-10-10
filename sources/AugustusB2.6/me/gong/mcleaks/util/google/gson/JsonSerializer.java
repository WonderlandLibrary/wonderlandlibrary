// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T>
{
    JsonElement serialize(final T p0, final Type p1, final JsonSerializationContext p2);
}
