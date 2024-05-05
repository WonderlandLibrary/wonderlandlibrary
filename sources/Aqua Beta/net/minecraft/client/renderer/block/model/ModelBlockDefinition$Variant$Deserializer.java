package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public static class ModelBlockDefinition.Variant.Deserializer
implements JsonDeserializer<ModelBlockDefinition.Variant> {
    public ModelBlockDefinition.Variant deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        String s = this.parseModel(jsonobject);
        ModelRotation modelrotation = this.parseRotation(jsonobject);
        boolean flag = this.parseUvLock(jsonobject);
        int i = this.parseWeight(jsonobject);
        return new ModelBlockDefinition.Variant(this.makeModelLocation(s), modelrotation, flag, i);
    }

    private ResourceLocation makeModelLocation(String p_178426_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_178426_1_);
        resourcelocation = new ResourceLocation(resourcelocation.getResourceDomain(), "block/" + resourcelocation.getResourcePath());
        return resourcelocation;
    }

    private boolean parseUvLock(JsonObject p_178429_1_) {
        return JsonUtils.getBoolean((JsonObject)p_178429_1_, (String)"uvlock", (boolean)false);
    }

    protected ModelRotation parseRotation(JsonObject p_178428_1_) {
        int j;
        int i = JsonUtils.getInt((JsonObject)p_178428_1_, (String)"x", (int)0);
        ModelRotation modelrotation = ModelRotation.getModelRotation((int)i, (int)(j = JsonUtils.getInt((JsonObject)p_178428_1_, (String)"y", (int)0)));
        if (modelrotation == null) {
            throw new JsonParseException("Invalid BlockModelRotation x: " + i + ", y: " + j);
        }
        return modelrotation;
    }

    protected String parseModel(JsonObject p_178424_1_) {
        return JsonUtils.getString((JsonObject)p_178424_1_, (String)"model");
    }

    protected int parseWeight(JsonObject p_178427_1_) {
        return JsonUtils.getInt((JsonObject)p_178427_1_, (String)"weight", (int)1);
    }
}
