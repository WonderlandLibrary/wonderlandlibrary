/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class AnimationMetadataSectionSerializer
extends BaseMetadataSectionSerializer<AnimationMetadataSection>
implements JsonSerializer<AnimationMetadataSection> {
    @Override
    public AnimationMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        ArrayList<AnimationFrame> list = Lists.newArrayList();
        JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "metadata section");
        int i2 = JsonUtils.getInt(jsonobject, "frametime", 1);
        if (i2 != 1) {
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, i2, "Invalid default frame time");
        }
        if (jsonobject.has("frames")) {
            try {
                JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "frames");
                for (int j2 = 0; j2 < jsonarray.size(); ++j2) {
                    JsonElement jsonelement = jsonarray.get(j2);
                    AnimationFrame animationframe = this.parseAnimationFrame(j2, jsonelement);
                    if (animationframe == null) continue;
                    list.add(animationframe);
                }
            }
            catch (ClassCastException classcastexception) {
                throw new JsonParseException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"), classcastexception);
            }
        }
        int k2 = JsonUtils.getInt(jsonobject, "width", -1);
        int l2 = JsonUtils.getInt(jsonobject, "height", -1);
        if (k2 != -1) {
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, k2, "Invalid width");
        }
        if (l2 != -1) {
            Validate.inclusiveBetween(1L, Integer.MAX_VALUE, l2, "Invalid height");
        }
        boolean flag = JsonUtils.getBoolean(jsonobject, "interpolate", false);
        return new AnimationMetadataSection(list, k2, l2, i2, flag);
    }

    private AnimationFrame parseAnimationFrame(int p_110492_1_, JsonElement p_110492_2_) {
        if (p_110492_2_.isJsonPrimitive()) {
            return new AnimationFrame(JsonUtils.getInt(p_110492_2_, "frames[" + p_110492_1_ + "]"));
        }
        if (p_110492_2_.isJsonObject()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_110492_2_, "frames[" + p_110492_1_ + "]");
            int i2 = JsonUtils.getInt(jsonobject, "time", -1);
            if (jsonobject.has("time")) {
                Validate.inclusiveBetween(1L, Integer.MAX_VALUE, i2, "Invalid frame time");
            }
            int j2 = JsonUtils.getInt(jsonobject, "index");
            Validate.inclusiveBetween(0L, Integer.MAX_VALUE, j2, "Invalid frame index");
            return new AnimationFrame(j2, i2);
        }
        return null;
    }

    @Override
    public JsonElement serialize(AnimationMetadataSection p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("frametime", p_serialize_1_.getFrameTime());
        if (p_serialize_1_.getFrameWidth() != -1) {
            jsonobject.addProperty("width", p_serialize_1_.getFrameWidth());
        }
        if (p_serialize_1_.getFrameHeight() != -1) {
            jsonobject.addProperty("height", p_serialize_1_.getFrameHeight());
        }
        if (p_serialize_1_.getFrameCount() > 0) {
            JsonArray jsonarray = new JsonArray();
            for (int i2 = 0; i2 < p_serialize_1_.getFrameCount(); ++i2) {
                if (p_serialize_1_.frameHasTime(i2)) {
                    JsonObject jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("index", p_serialize_1_.getFrameIndex(i2));
                    jsonobject1.addProperty("time", p_serialize_1_.getFrameTimeSingle(i2));
                    jsonarray.add(jsonobject1);
                    continue;
                }
                jsonarray.add(new JsonPrimitive(p_serialize_1_.getFrameIndex(i2)));
            }
            jsonobject.add("frames", jsonarray);
        }
        return jsonobject;
    }

    @Override
    public String getSectionName() {
        return "animation";
    }
}

