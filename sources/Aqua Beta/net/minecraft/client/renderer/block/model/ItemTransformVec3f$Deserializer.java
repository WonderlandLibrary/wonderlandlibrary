package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

static class ItemTransformVec3f.Deserializer
implements JsonDeserializer<ItemTransformVec3f> {
    private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0f, 1.0f, 1.0f);

    ItemTransformVec3f.Deserializer() {
    }

    public ItemTransformVec3f deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
        Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
        vector3f1.scale(0.0625f);
        vector3f1.x = MathHelper.clamp_float((float)vector3f1.x, (float)-1.5f, (float)1.5f);
        vector3f1.y = MathHelper.clamp_float((float)vector3f1.y, (float)-1.5f, (float)1.5f);
        vector3f1.z = MathHelper.clamp_float((float)vector3f1.z, (float)-1.5f, (float)1.5f);
        Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
        vector3f2.x = MathHelper.clamp_float((float)vector3f2.x, (float)-4.0f, (float)4.0f);
        vector3f2.y = MathHelper.clamp_float((float)vector3f2.y, (float)-4.0f, (float)4.0f);
        vector3f2.z = MathHelper.clamp_float((float)vector3f2.z, (float)-4.0f, (float)4.0f);
        return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
    }

    private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue) {
        if (!jsonObject.has(key)) {
            return defaultValue;
        }
        JsonArray jsonarray = JsonUtils.getJsonArray((JsonObject)jsonObject, (String)key);
        if (jsonarray.size() != 3) {
            throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
        }
        float[] afloat = new float[3];
        for (int i = 0; i < afloat.length; ++i) {
            afloat[i] = JsonUtils.getFloat((JsonElement)jsonarray.get(i), (String)(key + "[" + i + "]"));
        }
        return new Vector3f(afloat[0], afloat[1], afloat[2]);
    }
}
