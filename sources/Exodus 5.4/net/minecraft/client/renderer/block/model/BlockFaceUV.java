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
 */
package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;

public class BlockFaceUV {
    public final int rotation;
    public float[] uvs;

    public BlockFaceUV(float[] fArray, int n) {
        this.uvs = fArray;
        this.rotation = n;
    }

    public float func_178346_b(int n) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        int n2 = this.func_178347_d(n);
        return n2 != 0 && n2 != 3 ? this.uvs[3] : this.uvs[1];
    }

    public int func_178345_c(int n) {
        return (n + (4 - this.rotation / 90)) % 4;
    }

    public float func_178348_a(int n) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        int n2 = this.func_178347_d(n);
        return n2 != 0 && n2 != 1 ? this.uvs[2] : this.uvs[0];
    }

    private int func_178347_d(int n) {
        return (n + this.rotation / 90) % 4;
    }

    public void setUvs(float[] fArray) {
        if (this.uvs == null) {
            this.uvs = fArray;
        }
    }

    static class Deserializer
    implements JsonDeserializer<BlockFaceUV> {
        Deserializer() {
        }

        private float[] parseUV(JsonObject jsonObject) {
            if (!jsonObject.has("uv")) {
                return null;
            }
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "uv");
            if (jsonArray.size() != 4) {
                throw new JsonParseException("Expected 4 uv values, found: " + jsonArray.size());
            }
            float[] fArray = new float[4];
            int n = 0;
            while (n < fArray.length) {
                fArray[n] = JsonUtils.getFloat(jsonArray.get(n), "uv[" + n + "]");
                ++n;
            }
            return fArray;
        }

        public BlockFaceUV deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            float[] fArray = this.parseUV(jsonObject);
            int n = this.parseRotation(jsonObject);
            return new BlockFaceUV(fArray, n);
        }

        protected int parseRotation(JsonObject jsonObject) {
            int n = JsonUtils.getInt(jsonObject, "rotation", 0);
            if (n >= 0 && n % 90 == 0 && n / 90 <= 3) {
                return n;
            }
            throw new JsonParseException("Invalid rotation " + n + " found, only 0/90/180/270 allowed");
        }
    }
}

