/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class FontMetadataSectionSerializer
extends BaseMetadataSectionSerializer {
    private static final String __OBFID = "CL_00001109";

    public FontMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
        JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        float[] var5 = new float[256];
        float[] var6 = new float[256];
        float[] var7 = new float[256];
        float var8 = 1.0f;
        float var9 = 0.0f;
        float var10 = 0.0f;
        if (var4.has("characters")) {
            if (!var4.get("characters").isJsonObject()) {
                throw new JsonParseException("Invalid font->characters: expected object, was " + (Object)var4.get("characters"));
            }
            JsonObject var11 = var4.getAsJsonObject("characters");
            if (var11.has("default")) {
                if (!var11.get("default").isJsonObject()) {
                    throw new JsonParseException("Invalid font->characters->default: expected object, was " + (Object)var11.get("default"));
                }
                JsonObject var12 = var11.getAsJsonObject("default");
                var8 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "width", var8);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var8, (String)"Invalid default width");
                var9 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "spacing", var9);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var9, (String)"Invalid default spacing");
                var10 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "left", var9);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var10, (String)"Invalid default left");
            }
            for (int var18 = 0; var18 < 256; ++var18) {
                JsonElement var13 = var11.get(Integer.toString(var18));
                float var14 = var8;
                float var15 = var9;
                float var16 = var10;
                if (var13 != null) {
                    JsonObject var17 = JsonUtils.getElementAsJsonObject(var13, "characters[" + var18 + "]");
                    var14 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "width", var8);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var14, (String)"Invalid width");
                    var15 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "spacing", var9);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var15, (String)"Invalid spacing");
                    var16 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var17, "left", var10);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)var16, (String)"Invalid left");
                }
                var5[var18] = var14;
                var6[var18] = var15;
                var7[var18] = var16;
            }
        }
        return new FontMetadataSection(var5, var7, var6);
    }

    @Override
    public String getSectionName() {
        return "font";
    }
}

