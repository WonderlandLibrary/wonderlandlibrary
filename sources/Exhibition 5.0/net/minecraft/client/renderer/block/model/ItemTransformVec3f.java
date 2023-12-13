// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.util.MathHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import javax.vecmath.Vector3f;

public class ItemTransformVec3f
{
    public static final ItemTransformVec3f field_178366_a;
    public final Vector3f field_178364_b;
    public final Vector3f field_178365_c;
    public final Vector3f field_178363_d;
    private static final String __OBFID = "CL_00002484";
    
    public ItemTransformVec3f(final Vector3f p_i46214_1_, final Vector3f p_i46214_2_, final Vector3f p_i46214_3_) {
        this.field_178364_b = new Vector3f(p_i46214_1_);
        this.field_178365_c = new Vector3f(p_i46214_2_);
        this.field_178363_d = new Vector3f(p_i46214_3_);
    }
    
    static {
        field_178366_a = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    static class Deserializer implements JsonDeserializer
    {
        private static final Vector3f field_178362_a;
        private static final Vector3f field_178360_b;
        private static final Vector3f field_178361_c;
        private static final String __OBFID = "CL_00002483";
        
        public ItemTransformVec3f func_178359_a(final JsonElement p_178359_1_, final Type p_178359_2_, final JsonDeserializationContext p_178359_3_) {
            final JsonObject var4 = p_178359_1_.getAsJsonObject();
            final Vector3f var5 = this.func_178358_a(var4, "rotation", Deserializer.field_178362_a);
            final Vector3f var6 = this.func_178358_a(var4, "translation", Deserializer.field_178360_b);
            var6.scale(0.0625f);
            MathHelper.clamp_double(var6.x, -1.5, 1.5);
            MathHelper.clamp_double(var6.y, -1.5, 1.5);
            MathHelper.clamp_double(var6.z, -1.5, 1.5);
            final Vector3f var7 = this.func_178358_a(var4, "scale", Deserializer.field_178361_c);
            MathHelper.clamp_double(var7.x, -1.5, 1.5);
            MathHelper.clamp_double(var7.y, -1.5, 1.5);
            MathHelper.clamp_double(var7.z, -1.5, 1.5);
            return new ItemTransformVec3f(var5, var6, var7);
        }
        
        private Vector3f func_178358_a(final JsonObject p_178358_1_, final String p_178358_2_, final Vector3f p_178358_3_) {
            if (!p_178358_1_.has(p_178358_2_)) {
                return p_178358_3_;
            }
            final JsonArray var4 = JsonUtils.getJsonObjectJsonArrayField(p_178358_1_, p_178358_2_);
            if (var4.size() != 3) {
                throw new JsonParseException("Expected 3 " + p_178358_2_ + " values, found: " + var4.size());
            }
            final float[] var5 = new float[3];
            for (int var6 = 0; var6 < var5.length; ++var6) {
                var5[var6] = JsonUtils.getJsonElementFloatValue(var4.get(var6), p_178358_2_ + "[" + var6 + "]");
            }
            return new Vector3f(var5);
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.func_178359_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
        
        static {
            field_178362_a = new Vector3f(0.0f, 0.0f, 0.0f);
            field_178360_b = new Vector3f(0.0f, 0.0f, 0.0f);
            field_178361_c = new Vector3f(1.0f, 1.0f, 1.0f);
        }
    }
}
