/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T> {
    private final T value;

    boolean hasBanExpired() {
        return false;
    }

    protected UserListEntry(T t, JsonObject jsonObject) {
        this.value = t;
    }

    T getValue() {
        return this.value;
    }

    protected void onSerialization(JsonObject jsonObject) {
    }

    public UserListEntry(T t) {
        this.value = t;
    }
}

