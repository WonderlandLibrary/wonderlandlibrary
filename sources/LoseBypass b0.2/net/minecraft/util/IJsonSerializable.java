/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.gson.JsonElement;

public interface IJsonSerializable {
    public void fromJson(JsonElement var1);

    public JsonElement getSerializableElement();
}

