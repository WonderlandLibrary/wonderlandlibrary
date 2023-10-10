/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public interface JsonDeserializationContext {
    public <T> T deserialize(JsonElement var1, Type var2) throws JsonParseException;
}

