// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import org.apache.commons.lang3.Validate;
import com.google.gson.JsonArray;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import java.util.List;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class SoundListSerializer implements JsonDeserializer<SoundList>
{
    public SoundList deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "entry");
        final boolean flag = JsonUtils.getBoolean(jsonobject, "replace", false);
        final String s = JsonUtils.getString(jsonobject, "subtitle", null);
        final List<Sound> list = this.deserializeSounds(jsonobject);
        return new SoundList(list, flag, s);
    }
    
    private List<Sound> deserializeSounds(final JsonObject object) {
        final List<Sound> list = (List<Sound>)Lists.newArrayList();
        if (object.has("sounds")) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(object, "sounds");
            for (int i = 0; i < jsonarray.size(); ++i) {
                final JsonElement jsonelement = jsonarray.get(i);
                if (JsonUtils.isString(jsonelement)) {
                    final String s = JsonUtils.getString(jsonelement, "sound");
                    list.add(new Sound(s, 1.0f, 1.0f, 1, Sound.Type.FILE, false));
                }
                else {
                    list.add(this.deserializeSound(JsonUtils.getJsonObject(jsonelement, "sound")));
                }
            }
        }
        return list;
    }
    
    private Sound deserializeSound(final JsonObject object) {
        final String s = JsonUtils.getString(object, "name");
        final Sound.Type sound$type = this.deserializeType(object, Sound.Type.FILE);
        final float f = JsonUtils.getFloat(object, "volume", 1.0f);
        Validate.isTrue(f > 0.0f, "Invalid volume", new Object[0]);
        final float f2 = JsonUtils.getFloat(object, "pitch", 1.0f);
        Validate.isTrue(f2 > 0.0f, "Invalid pitch", new Object[0]);
        final int i = JsonUtils.getInt(object, "weight", 1);
        Validate.isTrue(i > 0, "Invalid weight", new Object[0]);
        final boolean flag = JsonUtils.getBoolean(object, "stream", false);
        return new Sound(s, f, f2, i, sound$type, flag);
    }
    
    private Sound.Type deserializeType(final JsonObject object, final Sound.Type defaultValue) {
        Sound.Type sound$type = defaultValue;
        if (object.has("type")) {
            sound$type = Sound.Type.getByName(JsonUtils.getString(object, "type"));
            Validate.notNull((Object)sound$type, "Invalid type", new Object[0]);
        }
        return sound$type;
    }
}
