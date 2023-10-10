/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public enum LongSerializationPolicy {
    DEFAULT{

        @Override
        public JsonElement serialize(Long value) {
            return new JsonPrimitive(value);
        }
    }
    ,
    STRING{

        @Override
        public JsonElement serialize(Long value) {
            return new JsonPrimitive(String.valueOf(value));
        }
    };


    public abstract JsonElement serialize(Long var1);
}

