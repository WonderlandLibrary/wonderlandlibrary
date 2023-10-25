/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 */
package net.minecraft.client.resources.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.JsonUtils;

public class TextureMetadataSectionSerializer
extends BaseMetadataSectionSerializer<TextureMetadataSection> {
    @Override
    public String getSectionName() {
        return "texture";
    }

    public TextureMetadataSection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        boolean bl = JsonUtils.getBoolean(jsonObject, "blur", false);
        boolean bl2 = JsonUtils.getBoolean(jsonObject, "clamp", false);
        ArrayList arrayList = Lists.newArrayList();
        if (jsonObject.has("mipmaps")) {
            try {
                JsonArray jsonArray = jsonObject.getAsJsonArray("mipmaps");
                int n = 0;
                while (n < jsonArray.size()) {
                    JsonElement jsonElement2 = jsonArray.get(n);
                    if (jsonElement2.isJsonPrimitive()) {
                        try {
                            arrayList.add(jsonElement2.getAsInt());
                        }
                        catch (NumberFormatException numberFormatException) {
                            throw new JsonParseException("Invalid texture->mipmap->" + n + ": expected number, was " + jsonElement2, (Throwable)numberFormatException);
                        }
                    } else if (jsonElement2.isJsonObject()) {
                        throw new JsonParseException("Invalid texture->mipmap->" + n + ": expected number, was " + jsonElement2);
                    }
                    ++n;
                }
            }
            catch (ClassCastException classCastException) {
                throw new JsonParseException("Invalid texture->mipmaps: expected array, was " + jsonObject.get("mipmaps"), (Throwable)classCastException);
            }
        }
        return new TextureMetadataSection(bl, bl2, arrayList);
    }
}

