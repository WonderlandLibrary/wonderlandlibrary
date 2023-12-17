// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<TextureMetadataSection>
{
    public TextureMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        final boolean flag = JsonUtils.getBoolean(jsonobject, "blur", false);
        final boolean flag2 = JsonUtils.getBoolean(jsonobject, "clamp", false);
        return new TextureMetadataSection(flag, flag2);
    }
    
    @Override
    public String getSectionName() {
        return "texture";
    }
}
