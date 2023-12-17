// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.ResourceLocation;

public class Variant
{
    private final ResourceLocation modelLocation;
    private final ModelRotation rotation;
    private final boolean uvLock;
    private final int weight;
    
    public Variant(final ResourceLocation modelLocationIn, final ModelRotation rotationIn, final boolean uvLockIn, final int weightIn) {
        this.modelLocation = modelLocationIn;
        this.rotation = rotationIn;
        this.uvLock = uvLockIn;
        this.weight = weightIn;
    }
    
    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }
    
    public ModelRotation getRotation() {
        return this.rotation;
    }
    
    public boolean isUvLock() {
        return this.uvLock;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    @Override
    public String toString() {
        return "Variant{modelLocation=" + this.modelLocation + ", rotation=" + this.rotation + ", uvLock=" + this.uvLock + ", weight=" + this.weight + '}';
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Variant)) {
            return false;
        }
        final Variant variant = (Variant)p_equals_1_;
        return this.modelLocation.equals(variant.modelLocation) && this.rotation == variant.rotation && this.uvLock == variant.uvLock && this.weight == variant.weight;
    }
    
    @Override
    public int hashCode() {
        int i = this.modelLocation.hashCode();
        i = 31 * i + this.rotation.hashCode();
        i = 31 * i + Boolean.valueOf(this.uvLock).hashCode();
        i = 31 * i + this.weight;
        return i;
    }
    
    public static class Deserializer implements JsonDeserializer<Variant>
    {
        public Variant deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final String s = this.getStringModel(jsonobject);
            final ModelRotation modelrotation = this.parseModelRotation(jsonobject);
            final boolean flag = this.parseUvLock(jsonobject);
            final int i = this.parseWeight(jsonobject);
            return new Variant(this.getResourceLocationBlock(s), modelrotation, flag, i);
        }
        
        private ResourceLocation getResourceLocationBlock(final String p_188041_1_) {
            ResourceLocation resourcelocation = new ResourceLocation(p_188041_1_);
            resourcelocation = new ResourceLocation(resourcelocation.getNamespace(), "block/" + resourcelocation.getPath());
            return resourcelocation;
        }
        
        private boolean parseUvLock(final JsonObject json) {
            return JsonUtils.getBoolean(json, "uvlock", false);
        }
        
        protected ModelRotation parseModelRotation(final JsonObject json) {
            final int i = JsonUtils.getInt(json, "x", 0);
            final int j = JsonUtils.getInt(json, "y", 0);
            final ModelRotation modelrotation = ModelRotation.getModelRotation(i, j);
            if (modelrotation == null) {
                throw new JsonParseException("Invalid BlockModelRotation x: " + i + ", y: " + j);
            }
            return modelrotation;
        }
        
        protected String getStringModel(final JsonObject json) {
            return JsonUtils.getString(json, "model");
        }
        
        protected int parseWeight(final JsonObject json) {
            final int i = JsonUtils.getInt(json, "weight", 1);
            if (i < 1) {
                throw new JsonParseException("Invalid weight " + i + " found, expected integer >= 1");
            }
            return i;
        }
    }
}
