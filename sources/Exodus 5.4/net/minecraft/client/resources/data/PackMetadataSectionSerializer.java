/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 */
package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class PackMetadataSectionSerializer
extends BaseMetadataSectionSerializer<PackMetadataSection>
implements JsonSerializer<PackMetadataSection> {
    public PackMetadataSection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        IChatComponent iChatComponent = (IChatComponent)jsonDeserializationContext.deserialize(jsonObject.get("description"), IChatComponent.class);
        if (iChatComponent == null) {
            throw new JsonParseException("Invalid/missing description!");
        }
        int n = JsonUtils.getInt(jsonObject, "pack_format");
        return new PackMetadataSection(iChatComponent, n);
    }

    @Override
    public String getSectionName() {
        return "pack";
    }

    public JsonElement serialize(PackMetadataSection packMetadataSection, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pack_format", (Number)packMetadataSection.getPackFormat());
        jsonObject.add("description", jsonSerializationContext.serialize((Object)packMetadataSection.getPackDescription()));
        return jsonObject;
    }
}

