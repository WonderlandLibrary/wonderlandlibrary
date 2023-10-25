/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.apache.commons.lang3.Validate
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
    private AnimationFrame parseAnimationFrame(int n, JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new AnimationFrame(JsonUtils.getInt(jsonElement, "frames[" + n + "]"));
        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "frames[" + n + "]");
            int n2 = JsonUtils.getInt(jsonObject, "time", -1);
            if (jsonObject.has("time")) {
                Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)n2, (String)"Invalid frame time");
            }
            int n3 = JsonUtils.getInt(jsonObject, "index");
            Validate.inclusiveBetween((long)0L, (long)Integer.MAX_VALUE, (long)n3, (String)"Invalid frame index");
            return new AnimationFrame(n3, n2);
        }
        return null;
    }

    public AnimationMetadataSection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        int n;
        ArrayList arrayList = Lists.newArrayList();
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "metadata section");
        int n2 = JsonUtils.getInt(jsonObject, "frametime", 1);
        if (n2 != 1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)n2, (String)"Invalid default frame time");
        }
        if (jsonObject.has("frames")) {
            try {
                JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "frames");
                n = 0;
                while (n < jsonArray.size()) {
                    JsonElement jsonElement2 = jsonArray.get(n);
                    AnimationFrame animationFrame = this.parseAnimationFrame(n, jsonElement2);
                    if (animationFrame != null) {
                        arrayList.add(animationFrame);
                    }
                    ++n;
                }
            }
            catch (ClassCastException classCastException) {
                throw new JsonParseException("Invalid animation->frames: expected array, was " + jsonObject.get("frames"), (Throwable)classCastException);
            }
        }
        int n3 = JsonUtils.getInt(jsonObject, "width", -1);
        n = JsonUtils.getInt(jsonObject, "height", -1);
        if (n3 != -1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)n3, (String)"Invalid width");
        }
        if (n != -1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)n, (String)"Invalid height");
        }
        boolean bl = JsonUtils.getBoolean(jsonObject, "interpolate", false);
        return new AnimationMetadataSection(arrayList, n3, n, n2, bl);
    }

    public JsonElement serialize(AnimationMetadataSection animationMetadataSection, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("frametime", (Number)animationMetadataSection.getFrameTime());
        if (animationMetadataSection.getFrameWidth() != -1) {
            jsonObject.addProperty("width", (Number)animationMetadataSection.getFrameWidth());
        }
        if (animationMetadataSection.getFrameHeight() != -1) {
            jsonObject.addProperty("height", (Number)animationMetadataSection.getFrameHeight());
        }
        if (animationMetadataSection.getFrameCount() > 0) {
            JsonArray jsonArray = new JsonArray();
            int n = 0;
            while (n < animationMetadataSection.getFrameCount()) {
                if (animationMetadataSection.frameHasTime(n)) {
                    JsonObject jsonObject2 = new JsonObject();
                    jsonObject2.addProperty("index", (Number)animationMetadataSection.getFrameIndex(n));
                    jsonObject2.addProperty("time", (Number)animationMetadataSection.getFrameTimeSingle(n));
                    jsonArray.add((JsonElement)jsonObject2);
                } else {
                    jsonArray.add((JsonElement)new JsonPrimitive((Number)animationMetadataSection.getFrameIndex(n)));
                }
                ++n;
            }
            jsonObject.add("frames", (JsonElement)jsonArray);
        }
        return jsonObject;
    }

    @Override
    public String getSectionName() {
        return "animation";
    }
}

