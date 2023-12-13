// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    private static final String __OBFID = "CL_00001109";
    
    public FontMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
        final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        final float[] var5 = new float[256];
        final float[] var6 = new float[256];
        final float[] var7 = new float[256];
        float var8 = 1.0f;
        float var9 = 0.0f;
        float var10 = 0.0f;
        if (var4.has("characters")) {
            if (!var4.get("characters").isJsonObject()) {
                throw new JsonParseException("Invalid font->characters: expected object, was " + var4.get("characters"));
            }
            final JsonObject var11 = var4.getAsJsonObject("characters");
            if (var11.has("default")) {
                if (!var11.get("default").isJsonObject()) {
                    throw new JsonParseException("Invalid font->characters->default: expected object, was " + var11.get("default"));
                }
                final JsonObject var12 = var11.getAsJsonObject("default");
                var8 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "width", var8);
                Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var8, "Invalid default width");
                var9 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "spacing", var9);
                Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var9, "Invalid default spacing");
                var10 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var12, "left", var9);
                Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var10, "Invalid default left");
            }
            for (int var13 = 0; var13 < 256; ++var13) {
                final JsonElement var14 = var11.get(Integer.toString(var13));
                float var15 = var8;
                float var16 = var9;
                float var17 = var10;
                if (var14 != null) {
                    final JsonObject var18 = JsonUtils.getElementAsJsonObject(var14, "characters[" + var13 + "]");
                    var15 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var18, "width", var8);
                    Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var15, "Invalid width");
                    var16 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var18, "spacing", var9);
                    Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var16, "Invalid spacing");
                    var17 = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var18, "left", var10);
                    Validate.inclusiveBetween(0.0, 3.4028234663852886E38, (double)var17, "Invalid left");
                }
                var5[var13] = var15;
                var6[var13] = var16;
                var7[var13] = var17;
            }
        }
        return new FontMetadataSection(var5, var7, var6);
    }
    
    @Override
    public String getSectionName() {
        return "font";
    }
}
