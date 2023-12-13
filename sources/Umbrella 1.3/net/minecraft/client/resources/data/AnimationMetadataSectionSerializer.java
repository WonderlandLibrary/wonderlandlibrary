/*
 * Decompiled with CFR 0.150.
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
extends BaseMetadataSectionSerializer
implements JsonSerializer {
    private static final String __OBFID = "CL_00001107";

    public AnimationMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
        int var8;
        ArrayList var4 = Lists.newArrayList();
        JsonObject var5 = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "metadata section");
        int var6 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var5, "frametime", 1);
        if (var6 != 1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)var6, (String)"Invalid default frame time");
        }
        if (var5.has("frames")) {
            try {
                JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var5, "frames");
                for (var8 = 0; var8 < var7.size(); ++var8) {
                    JsonElement var9 = var7.get(var8);
                    AnimationFrame var10 = this.parseAnimationFrame(var8, var9);
                    if (var10 == null) continue;
                    var4.add(var10);
                }
            }
            catch (ClassCastException var11) {
                throw new JsonParseException("Invalid animation->frames: expected array, was " + (Object)var5.get("frames"), (Throwable)var11);
            }
        }
        int var12 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var5, "width", -1);
        var8 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var5, "height", -1);
        if (var12 != -1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)var12, (String)"Invalid width");
        }
        if (var8 != -1) {
            Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)var8, (String)"Invalid height");
        }
        boolean var13 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var5, "interpolate", false);
        return new AnimationMetadataSection(var4, var12, var8, var6, var13);
    }

    private AnimationFrame parseAnimationFrame(int p_110492_1_, JsonElement p_110492_2_) {
        if (p_110492_2_.isJsonPrimitive()) {
            return new AnimationFrame(JsonUtils.getJsonElementIntegerValue(p_110492_2_, "frames[" + p_110492_1_ + "]"));
        }
        if (p_110492_2_.isJsonObject()) {
            JsonObject var3 = JsonUtils.getElementAsJsonObject(p_110492_2_, "frames[" + p_110492_1_ + "]");
            int var4 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var3, "time", -1);
            if (var3.has("time")) {
                Validate.inclusiveBetween((long)1L, (long)Integer.MAX_VALUE, (long)var4, (String)"Invalid frame time");
            }
            int var5 = JsonUtils.getJsonObjectIntegerFieldValue(var3, "index");
            Validate.inclusiveBetween((long)0L, (long)Integer.MAX_VALUE, (long)var5, (String)"Invalid frame index");
            return new AnimationFrame(var5, var4);
        }
        return null;
    }

    public JsonElement serialize(AnimationMetadataSection p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        JsonObject var4 = new JsonObject();
        var4.addProperty("frametime", (Number)p_serialize_1_.getFrameTime());
        if (p_serialize_1_.getFrameWidth() != -1) {
            var4.addProperty("width", (Number)p_serialize_1_.getFrameWidth());
        }
        if (p_serialize_1_.getFrameHeight() != -1) {
            var4.addProperty("height", (Number)p_serialize_1_.getFrameHeight());
        }
        if (p_serialize_1_.getFrameCount() > 0) {
            JsonArray var5 = new JsonArray();
            for (int var6 = 0; var6 < p_serialize_1_.getFrameCount(); ++var6) {
                if (p_serialize_1_.frameHasTime(var6)) {
                    JsonObject var7 = new JsonObject();
                    var7.addProperty("index", (Number)p_serialize_1_.getFrameIndex(var6));
                    var7.addProperty("time", (Number)p_serialize_1_.getFrameTimeSingle(var6));
                    var5.add((JsonElement)var7);
                    continue;
                }
                var5.add((JsonElement)new JsonPrimitive((Number)p_serialize_1_.getFrameIndex(var6)));
            }
            var4.add("frames", (JsonElement)var5);
        }
        return var4;
    }

    @Override
    public String getSectionName() {
        return "animation";
    }

    public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
        return this.serialize((AnimationMetadataSection)p_serialize_1_, p_serialize_2_, p_serialize_3_);
    }
}

