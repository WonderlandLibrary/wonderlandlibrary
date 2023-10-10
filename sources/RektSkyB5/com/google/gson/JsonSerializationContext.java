/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import java.lang.reflect.Type;

public interface JsonSerializationContext {
    public JsonElement serialize(Object var1);

    public JsonElement serialize(Object var1, Type var2);
}

