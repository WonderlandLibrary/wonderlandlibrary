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
    public SoundList deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_deserialize_1_, (String)"entry");
        SoundList soundlist = new SoundList();
        soundlist.setReplaceExisting(JsonUtils.getBoolean((JsonObject)jsonobject, (String)"replace", (boolean)false));
        SoundCategory soundcategory = SoundCategory.getCategory((String)JsonUtils.getString((JsonObject)jsonobject, (String)"category", (String)SoundCategory.MASTER.getCategoryName()));
        soundlist.setSoundCategory(soundcategory);
        Validate.notNull((Object)soundcategory, (String)"Invalid category", (Object[])new Object[0]);
        if (jsonobject.has("sounds")) {
            JsonArray jsonarray = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"sounds");
            for (int i = 0; i < jsonarray.size(); ++i) {
                JsonElement jsonelement = jsonarray.get(i);
                SoundList.SoundEntry soundlist$soundentry = new SoundList.SoundEntry();
                if (JsonUtils.isString((JsonElement)jsonelement)) {
                    soundlist$soundentry.setSoundEntryName(JsonUtils.getString((JsonElement)jsonelement, (String)"sound"));
                } else {
                    JsonObject jsonobject1 = JsonUtils.getJsonObject((JsonElement)jsonelement, (String)"sound");
                    soundlist$soundentry.setSoundEntryName(JsonUtils.getString((JsonObject)jsonobject1, (String)"name"));
                    if (jsonobject1.has("type")) {
                        SoundList.SoundEntry.Type soundlist$soundentry$type = SoundList.SoundEntry.Type.getType((String)JsonUtils.getString((JsonObject)jsonobject1, (String)"type"));
                        Validate.notNull((Object)soundlist$soundentry$type, (String)"Invalid type", (Object[])new Object[0]);
                        soundlist$soundentry.setSoundEntryType(soundlist$soundentry$type);
                    }
                    if (jsonobject1.has("volume")) {
                        float f = JsonUtils.getFloat((JsonObject)jsonobject1, (String)"volume");
                        Validate.isTrue((f > 0.0f ? 1 : 0) != 0, (String)"Invalid volume", (Object[])new Object[0]);
                        soundlist$soundentry.setSoundEntryVolume(f);
                    }
                    if (jsonobject1.has("pitch")) {
                        float f1 = JsonUtils.getFloat((JsonObject)jsonobject1, (String)"pitch");
                        Validate.isTrue((f1 > 0.0f ? 1 : 0) != 0, (String)"Invalid pitch", (Object[])new Object[0]);
                        soundlist$soundentry.setSoundEntryPitch(f1);
                    }
                    if (jsonobject1.has("weight")) {
                        int j = JsonUtils.getInt((JsonObject)jsonobject1, (String)"weight");
                        Validate.isTrue((j > 0 ? 1 : 0) != 0, (String)"Invalid weight", (Object[])new Object[0]);
                        soundlist$soundentry.setSoundEntryWeight(j);
                    }
                    if (jsonobject1.has("stream")) {
                        soundlist$soundentry.setStreaming(JsonUtils.getBoolean((JsonObject)jsonobject1, (String)"stream"));
                    }
                }
                soundlist.getSoundList().add((Object)soundlist$soundentry);
            }
        }
        return soundlist;
    }
}
