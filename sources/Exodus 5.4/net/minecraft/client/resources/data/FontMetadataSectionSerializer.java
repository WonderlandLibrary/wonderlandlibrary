/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class FontMetadataSectionSerializer
extends BaseMetadataSectionSerializer<FontMetadataSection> {
    public FontMetadataSection deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        float[] fArray = new float[256];
        float[] fArray2 = new float[256];
        float[] fArray3 = new float[256];
        float f = 1.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (jsonObject.has("characters")) {
            if (!jsonObject.get("characters").isJsonObject()) {
                throw new JsonParseException("Invalid font->characters: expected object, was " + jsonObject.get("characters"));
            }
            JsonObject jsonObject2 = jsonObject.getAsJsonObject("characters");
            if (jsonObject2.has("default")) {
                if (!jsonObject2.get("default").isJsonObject()) {
                    throw new JsonParseException("Invalid font->characters->default: expected object, was " + jsonObject2.get("default"));
                }
                JsonObject jsonObject3 = jsonObject2.getAsJsonObject("default");
                f = JsonUtils.getFloat(jsonObject3, "width", f);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f, (String)"Invalid default width");
                f2 = JsonUtils.getFloat(jsonObject3, "spacing", f2);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f2, (String)"Invalid default spacing");
                f3 = JsonUtils.getFloat(jsonObject3, "left", f2);
                Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f3, (String)"Invalid default left");
            }
            int n = 0;
            while (n < 256) {
                JsonElement jsonElement2 = jsonObject2.get(Integer.toString(n));
                float f4 = f;
                float f5 = f2;
                float f6 = f3;
                if (jsonElement2 != null) {
                    JsonObject jsonObject4 = JsonUtils.getJsonObject(jsonElement2, "characters[" + n + "]");
                    f4 = JsonUtils.getFloat(jsonObject4, "width", f);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f4, (String)"Invalid width");
                    f5 = JsonUtils.getFloat(jsonObject4, "spacing", f2);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f5, (String)"Invalid spacing");
                    f6 = JsonUtils.getFloat(jsonObject4, "left", f3);
                    Validate.inclusiveBetween((double)0.0, (double)3.4028234663852886E38, (double)f6, (String)"Invalid left");
                }
                fArray[n] = f4;
                fArray2[n] = f5;
                fArray3[n] = f6;
                ++n;
            }
        }
        return new FontMetadataSection(fArray, fArray3, fArray2);
    }

    @Override
    public String getSectionName() {
        return "font";
    }
}

