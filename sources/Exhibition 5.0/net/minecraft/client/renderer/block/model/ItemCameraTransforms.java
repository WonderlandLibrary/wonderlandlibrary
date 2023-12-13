// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class ItemCameraTransforms
{
    public static final ItemCameraTransforms field_178357_a;
    public final ItemTransformVec3f field_178355_b;
    public final ItemTransformVec3f field_178356_c;
    public final ItemTransformVec3f field_178353_d;
    public final ItemTransformVec3f field_178354_e;
    private static final String __OBFID = "CL_00002482";
    
    public ItemCameraTransforms(final ItemTransformVec3f p_i46213_1_, final ItemTransformVec3f p_i46213_2_, final ItemTransformVec3f p_i46213_3_, final ItemTransformVec3f p_i46213_4_) {
        this.field_178355_b = p_i46213_1_;
        this.field_178356_c = p_i46213_2_;
        this.field_178353_d = p_i46213_3_;
        this.field_178354_e = p_i46213_4_;
    }
    
    static {
        field_178357_a = new ItemCameraTransforms(ItemTransformVec3f.field_178366_a, ItemTransformVec3f.field_178366_a, ItemTransformVec3f.field_178366_a, ItemTransformVec3f.field_178366_a);
    }
    
    static class Deserializer implements JsonDeserializer
    {
        private static final String __OBFID = "CL_00002481";
        
        public ItemCameraTransforms func_178352_a(final JsonElement p_178352_1_, final Type p_178352_2_, final JsonDeserializationContext p_178352_3_) {
            final JsonObject var4 = p_178352_1_.getAsJsonObject();
            ItemTransformVec3f var5 = ItemTransformVec3f.field_178366_a;
            ItemTransformVec3f var6 = ItemTransformVec3f.field_178366_a;
            ItemTransformVec3f var7 = ItemTransformVec3f.field_178366_a;
            ItemTransformVec3f var8 = ItemTransformVec3f.field_178366_a;
            if (var4.has("thirdperson")) {
                var5 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("thirdperson"), (Type)ItemTransformVec3f.class);
            }
            if (var4.has("firstperson")) {
                var6 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("firstperson"), (Type)ItemTransformVec3f.class);
            }
            if (var4.has("head")) {
                var7 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("head"), (Type)ItemTransformVec3f.class);
            }
            if (var4.has("gui")) {
                var8 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("gui"), (Type)ItemTransformVec3f.class);
            }
            return new ItemCameraTransforms(var5, var6, var7, var8);
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.func_178352_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
    
    public enum TransformType
    {
        NONE("NONE", 0), 
        THIRD_PERSON("THIRD_PERSON", 1), 
        FIRST_PERSON("FIRST_PERSON", 2), 
        HEAD("HEAD", 3), 
        GUI("GUI", 4);
        
        private static final TransformType[] $VALUES;
        private static final String __OBFID = "CL_00002480";
        
        private TransformType(final String p_i46212_1_, final int p_i46212_2_) {
        }
        
        static {
            $VALUES = new TransformType[] { TransformType.NONE, TransformType.THIRD_PERSON, TransformType.FIRST_PERSON, TransformType.HEAD, TransformType.GUI };
        }
    }
}
