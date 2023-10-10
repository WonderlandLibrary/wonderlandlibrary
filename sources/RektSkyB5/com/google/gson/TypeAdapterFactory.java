/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2);
}

