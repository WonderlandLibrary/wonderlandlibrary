/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.Maps
 *  com.google.gson.Gson
 */
package net.minecraft.client.stream;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.util.Map;

public class Metadata {
    private static final Gson field_152811_a = new Gson();
    private Map<String, String> payload;
    private final String name;
    private String description;

    public void func_152807_a(String string) {
        this.description = string;
    }

    public String func_152806_b() {
        return this.payload != null && !this.payload.isEmpty() ? field_152811_a.toJson(this.payload) : null;
    }

    public void func_152808_a(String string, String string2) {
        if (this.payload == null) {
            this.payload = Maps.newHashMap();
        }
        if (this.payload.size() > 50) {
            throw new IllegalArgumentException("Metadata payload is full, cannot add more to it!");
        }
        if (string == null) {
            throw new IllegalArgumentException("Metadata payload key cannot be null!");
        }
        if (string.length() > 255) {
            throw new IllegalArgumentException("Metadata payload key is too long!");
        }
        if (string2 == null) {
            throw new IllegalArgumentException("Metadata payload value cannot be null!");
        }
        if (string2.length() > 255) {
            throw new IllegalArgumentException("Metadata payload value is too long!");
        }
        this.payload.put(string, string2);
    }

    public Metadata(String string, String string2) {
        this.name = string;
        this.description = string2;
    }

    public String func_152809_a() {
        return this.description == null ? this.name : this.description;
    }

    public Metadata(String string) {
        this(string, null);
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("name", (Object)this.name).add("description", (Object)this.description).add("data", (Object)this.func_152806_b()).toString();
    }

    public String func_152810_c() {
        return this.name;
    }
}

