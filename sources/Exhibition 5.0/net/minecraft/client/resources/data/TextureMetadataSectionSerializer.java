// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonArray;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import java.util.List;
import com.google.gson.JsonParseException;
import com.google.common.collect.Lists;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    private static final String __OBFID = "CL_00001115";
    
    public TextureMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
        final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        final boolean var5 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "blur", false);
        final boolean var6 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "clamp", false);
        final ArrayList var7 = Lists.newArrayList();
        if (var4.has("mipmaps")) {
            try {
                final JsonArray var8 = var4.getAsJsonArray("mipmaps");
                for (int var9 = 0; var9 < var8.size(); ++var9) {
                    final JsonElement var10 = var8.get(var9);
                    if (var10.isJsonPrimitive()) {
                        try {
                            var7.add(var10.getAsInt());
                            continue;
                        }
                        catch (NumberFormatException var11) {
                            throw new JsonParseException("Invalid texture->mipmap->" + var9 + ": expected number, was " + var10, (Throwable)var11);
                        }
                    }
                    if (var10.isJsonObject()) {
                        throw new JsonParseException("Invalid texture->mipmap->" + var9 + ": expected number, was " + var10);
                    }
                }
            }
            catch (ClassCastException var12) {
                throw new JsonParseException("Invalid texture->mipmaps: expected array, was " + var4.get("mipmaps"), (Throwable)var12);
            }
        }
        return new TextureMetadataSection(var5, var6, var7);
    }
    
    @Override
    public String getSectionName() {
        return "texture";
    }
}
