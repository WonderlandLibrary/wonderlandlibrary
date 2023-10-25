package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer
extends BaseMetadataSectionSerializer<LanguageMetadataSection> {
    public LanguageMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        HashSet set = Sets.newHashSet();
        for (Map.Entry entry : jsonobject.entrySet()) {
            String s = (String)entry.getKey();
            JsonObject jsonobject1 = JsonUtils.getJsonObject((JsonElement)((JsonElement)entry.getValue()), (String)"language");
            String s1 = JsonUtils.getString((JsonObject)jsonobject1, (String)"region");
            String s2 = JsonUtils.getString((JsonObject)jsonobject1, (String)"name");
            boolean flag = JsonUtils.getBoolean((JsonObject)jsonobject1, (String)"bidirectional", (boolean)false);
            if (s1.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + s + "'->region: empty value");
            }
            if (s2.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + s + "'->name: empty value");
            }
            if (set.add((Object)new Language(s, s1, s2, flag))) continue;
            throw new JsonParseException("Duplicate language->'" + s + "' defined");
        }
        return new LanguageMetadataSection((Collection)set);
    }

    public String getSectionName() {
        return "language";
    }
}
