/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.client.audio;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundList;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class SoundListSerializer
implements JsonDeserializer<SoundList> {
    public SoundList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "entry");
        SoundList soundList = new SoundList();
        soundList.setReplaceExisting(JsonUtils.getBoolean(jsonObject, "replace", false));
        SoundCategory soundCategory = SoundCategory.getCategory(JsonUtils.getString(jsonObject, "category", SoundCategory.MASTER.getCategoryName()));
        soundList.setSoundCategory(soundCategory);
        Validate.notNull((Object)((Object)soundCategory), (String)"Invalid category", (Object[])new Object[0]);
        if (jsonObject.has("sounds")) {
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "sounds");
            int n = 0;
            while (n < jsonArray.size()) {
                JsonElement jsonElement2 = jsonArray.get(n);
                SoundList.SoundEntry soundEntry = new SoundList.SoundEntry();
                if (JsonUtils.isString(jsonElement2)) {
                    soundEntry.setSoundEntryName(JsonUtils.getString(jsonElement2, "sound"));
                } else {
                    JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonElement2, "sound");
                    soundEntry.setSoundEntryName(JsonUtils.getString(jsonObject2, "name"));
                    if (jsonObject2.has("type")) {
                        SoundList.SoundEntry.Type type2 = SoundList.SoundEntry.Type.getType(JsonUtils.getString(jsonObject2, "type"));
                        Validate.notNull((Object)((Object)type2), (String)"Invalid type", (Object[])new Object[0]);
                        soundEntry.setSoundEntryType(type2);
                    }
                    if (jsonObject2.has("volume")) {
                        float f = JsonUtils.getFloat(jsonObject2, "volume");
                        Validate.isTrue((f > 0.0f ? 1 : 0) != 0, (String)"Invalid volume", (Object[])new Object[0]);
                        soundEntry.setSoundEntryVolume(f);
                    }
                    if (jsonObject2.has("pitch")) {
                        float f = JsonUtils.getFloat(jsonObject2, "pitch");
                        Validate.isTrue((f > 0.0f ? 1 : 0) != 0, (String)"Invalid pitch", (Object[])new Object[0]);
                        soundEntry.setSoundEntryPitch(f);
                    }
                    if (jsonObject2.has("weight")) {
                        int n2 = JsonUtils.getInt(jsonObject2, "weight");
                        Validate.isTrue((n2 > 0 ? 1 : 0) != 0, (String)"Invalid weight", (Object[])new Object[0]);
                        soundEntry.setSoundEntryWeight(n2);
                    }
                    if (jsonObject2.has("stream")) {
                        soundEntry.setStreaming(JsonUtils.getBoolean(jsonObject2, "stream"));
                    }
                }
                soundList.getSoundList().add(soundEntry);
                ++n;
            }
        }
        return soundList;
    }
}

