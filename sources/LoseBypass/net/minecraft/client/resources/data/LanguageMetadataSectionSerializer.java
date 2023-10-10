/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer
extends BaseMetadataSectionSerializer<LanguageMetadataSection> {
    @Override
    public LanguageMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        boolean flag;
        String s2;
        String s1;
        String s;
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        HashSet<Language> set = Sets.newHashSet();
        Iterator<Map.Entry<String, JsonElement>> iterator = jsonobject.entrySet().iterator();
        do {
            if (!iterator.hasNext()) return new LanguageMetadataSection(set);
            Map.Entry<String, JsonElement> entry = iterator.next();
            s = entry.getKey();
            JsonObject jsonobject1 = JsonUtils.getJsonObject(entry.getValue(), "language");
            s1 = JsonUtils.getString(jsonobject1, "region");
            s2 = JsonUtils.getString(jsonobject1, "name");
            flag = JsonUtils.getBoolean(jsonobject1, "bidirectional", false);
            if (s1.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + s + "'->region: empty value");
            }
            if (!s2.isEmpty()) continue;
            throw new JsonParseException("Invalid language->'" + s + "'->name: empty value");
        } while (set.add(new Language(s, s1, s2, flag)));
        throw new JsonParseException("Duplicate language->'" + s + "' defined");
    }

    @Override
    public String getSectionName() {
        return "language";
    }
}

