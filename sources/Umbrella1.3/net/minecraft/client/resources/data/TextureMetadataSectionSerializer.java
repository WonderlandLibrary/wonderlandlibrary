/*
 * Decompiled with CFR 0.150.
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
extends BaseMetadataSectionSerializer {
    private static final String __OBFID = "CL_00001115";

    public TextureMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
        JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        boolean var5 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "blur", false);
        boolean var6 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "clamp", false);
        ArrayList var7 = Lists.newArrayList();
        if (var4.has("mipmaps")) {
            try {
                JsonArray var8 = var4.getAsJsonArray("mipmaps");
                for (int var9 = 0; var9 < var8.size(); ++var9) {
                    JsonElement var10 = var8.get(var9);
                    if (var10.isJsonPrimitive()) {
                        try {
                            var7.add(var10.getAsInt());
                            continue;
                        }
                        catch (NumberFormatException var12) {
                            throw new JsonParseException("Invalid texture->mipmap->" + var9 + ": expected number, was " + (Object)var10, (Throwable)var12);
                        }
                    }
                    if (!var10.isJsonObject()) continue;
                    throw new JsonParseException("Invalid texture->mipmap->" + var9 + ": expected number, was " + (Object)var10);
                }
            }
            catch (ClassCastException var13) {
                throw new JsonParseException("Invalid texture->mipmaps: expected array, was " + (Object)var4.get("mipmaps"), (Throwable)var13);
            }
        }
        return new TextureMetadataSection(var5, var6, var7);
    }

    @Override
    public String getSectionName() {
        return "texture";
    }
}

