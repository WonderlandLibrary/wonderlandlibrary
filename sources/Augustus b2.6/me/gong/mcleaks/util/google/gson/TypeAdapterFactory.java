// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.gson;

import me.gong.mcleaks.util.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory
{
     <T> TypeAdapter<T> create(final Gson p0, final TypeToken<T> p1);
}
