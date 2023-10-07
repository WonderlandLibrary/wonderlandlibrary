// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources.data;

import java.util.Iterator;
import java.util.HashSet;
import com.google.gson.JsonObject;
import java.util.Collection;
import net.minecraft.client.resources.Language;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import java.util.Map;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    private static final String __OBFID = "CL_00001111";
    
    public LanguageMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
        final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        final HashSet var5 = Sets.newHashSet();
        for (final Map.Entry var7 : var4.entrySet()) {
            final String var8 = var7.getKey();
            final JsonObject var9 = JsonUtils.getElementAsJsonObject(var7.getValue(), "language");
            final String var10 = JsonUtils.getJsonObjectStringFieldValue(var9, "region");
            final String var11 = JsonUtils.getJsonObjectStringFieldValue(var9, "name");
            final boolean var12 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var9, "bidirectional", false);
            if (var10.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + var8 + "'->region: empty value");
            }
            if (var11.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + var8 + "'->name: empty value");
            }
            if (!var5.add(new Language(var8, var10, var11, var12))) {
                throw new JsonParseException("Duplicate language->'" + var8 + "' defined");
            }
        }
        return new LanguageMetadataSection(var5);
    }
    
    @Override
    public String getSectionName() {
        return "language";
    }
}
