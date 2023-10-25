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
 *  org.lwjgl.util.vector.ReadableVector3f
 *  org.lwjgl.util.vector.Vector3f
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
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class ItemTransformVec3f {
    public final Vector3f scale;
    public final Vector3f rotation;
    public final Vector3f translation;
    public static final ItemTransformVec3f DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));

    public int hashCode() {
        int n = this.rotation.hashCode();
        n = 31 * n + this.translation.hashCode();
        n = 31 * n + this.scale.hashCode();
        return n;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        ItemTransformVec3f itemTransformVec3f = (ItemTransformVec3f)object;
        return !this.rotation.equals((Object)itemTransformVec3f.rotation) ? false : (!this.scale.equals((Object)itemTransformVec3f.scale) ? false : this.translation.equals((Object)itemTransformVec3f.translation));
    }

    public ItemTransformVec3f(Vector3f vector3f, Vector3f vector3f2, Vector3f vector3f3) {
        this.rotation = new Vector3f((ReadableVector3f)vector3f);
        this.translation = new Vector3f((ReadableVector3f)vector3f2);
        this.scale = new Vector3f((ReadableVector3f)vector3f3);
    }

    static class Deserializer
    implements JsonDeserializer<ItemTransformVec3f> {
        private static final Vector3f SCALE_DEFAULT;
        private static final Vector3f TRANSLATION_DEFAULT;
        private static final Vector3f ROTATION_DEFAULT;

        Deserializer() {
        }

        static {
            ROTATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
            TRANSLATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
            SCALE_DEFAULT = new Vector3f(1.0f, 1.0f, 1.0f);
        }

        public ItemTransformVec3f deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vector3f vector3f = this.parseVector3f(jsonObject, "rotation", ROTATION_DEFAULT);
            Vector3f vector3f2 = this.parseVector3f(jsonObject, "translation", TRANSLATION_DEFAULT);
            vector3f2.scale(0.0625f);
            vector3f2.x = MathHelper.clamp_float(vector3f2.x, -1.5f, 1.5f);
            vector3f2.y = MathHelper.clamp_float(vector3f2.y, -1.5f, 1.5f);
            vector3f2.z = MathHelper.clamp_float(vector3f2.z, -1.5f, 1.5f);
            Vector3f vector3f3 = this.parseVector3f(jsonObject, "scale", SCALE_DEFAULT);
            vector3f3.x = MathHelper.clamp_float(vector3f3.x, -4.0f, 4.0f);
            vector3f3.y = MathHelper.clamp_float(vector3f3.y, -4.0f, 4.0f);
            vector3f3.z = MathHelper.clamp_float(vector3f3.z, -4.0f, 4.0f);
            return new ItemTransformVec3f(vector3f, vector3f2, vector3f3);
        }

        private Vector3f parseVector3f(JsonObject jsonObject, String string, Vector3f vector3f) {
            if (!jsonObject.has(string)) {
                return vector3f;
            }
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, string);
            if (jsonArray.size() != 3) {
                throw new JsonParseException("Expected 3 " + string + " values, found: " + jsonArray.size());
            }
            float[] fArray = new float[3];
            int n = 0;
            while (n < fArray.length) {
                fArray[n] = JsonUtils.getFloat(jsonArray.get(n), String.valueOf(string) + "[" + n + "]");
                ++n;
            }
            return new Vector3f(fArray[0], fArray[1], fArray[2]);
        }
    }
}

