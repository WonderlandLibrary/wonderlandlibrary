// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import java.util.EnumMap;
import com.google.common.collect.Maps;
import net.minecraft.util.MathHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import java.util.Map;
import javax.vecmath.Vector3f;

public class BlockPart
{
    public final Vector3f field_178241_a;
    public final Vector3f field_178239_b;
    public final Map field_178240_c;
    public final BlockPartRotation field_178237_d;
    public final boolean field_178238_e;
    private static final String __OBFID = "CL_00002511";
    
    public BlockPart(final Vector3f p_i46231_1_, final Vector3f p_i46231_2_, final Map p_i46231_3_, final BlockPartRotation p_i46231_4_, final boolean p_i46231_5_) {
        this.field_178241_a = p_i46231_1_;
        this.field_178239_b = p_i46231_2_;
        this.field_178240_c = p_i46231_3_;
        this.field_178237_d = p_i46231_4_;
        this.field_178238_e = p_i46231_5_;
        this.func_178235_a();
    }
    
    private void func_178235_a() {
        for (final Map.Entry var2 : this.field_178240_c.entrySet()) {
            final float[] var3 = this.func_178236_a(var2.getKey());
            var2.getValue().field_178243_e.func_178349_a(var3);
        }
    }
    
    private float[] func_178236_a(final EnumFacing p_178236_1_) {
        float[] var2 = null;
        switch (SwitchEnumFacing.field_178234_a[p_178236_1_.ordinal()]) {
            case 1:
            case 2: {
                var2 = new float[] { this.field_178241_a.x, this.field_178241_a.z, this.field_178239_b.x, this.field_178239_b.z };
                break;
            }
            case 3:
            case 4: {
                var2 = new float[] { this.field_178241_a.x, 16.0f - this.field_178239_b.y, this.field_178239_b.x, 16.0f - this.field_178241_a.y };
                break;
            }
            case 5:
            case 6: {
                var2 = new float[] { this.field_178241_a.z, 16.0f - this.field_178239_b.y, this.field_178239_b.z, 16.0f - this.field_178241_a.y };
                break;
            }
            default: {
                throw new NullPointerException();
            }
        }
        return var2;
    }
    
    static class Deserializer implements JsonDeserializer
    {
        private static final String __OBFID = "CL_00002509";
        
        public BlockPart func_178254_a(final JsonElement p_178254_1_, final Type p_178254_2_, final JsonDeserializationContext p_178254_3_) {
            final JsonObject var4 = p_178254_1_.getAsJsonObject();
            final Vector3f var5 = this.func_178249_e(var4);
            final Vector3f var6 = this.func_178247_d(var4);
            final BlockPartRotation var7 = this.func_178256_a(var4);
            final Map var8 = this.func_178250_a(p_178254_3_, var4);
            if (var4.has("shade") && !JsonUtils.func_180199_c(var4, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            final boolean var9 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "shade", true);
            return new BlockPart(var5, var6, var8, var7, var9);
        }
        
        private BlockPartRotation func_178256_a(final JsonObject p_178256_1_) {
            BlockPartRotation var2 = null;
            if (p_178256_1_.has("rotation")) {
                final JsonObject var3 = JsonUtils.getJsonObject(p_178256_1_, "rotation");
                final Vector3f var4 = this.func_178251_a(var3, "origin");
                var4.scale(0.0625f);
                final EnumFacing.Axis var5 = this.func_178252_c(var3);
                final float var6 = this.func_178255_b(var3);
                final boolean var7 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var3, "rescale", false);
                var2 = new BlockPartRotation(var4, var5, var6, var7);
            }
            return var2;
        }
        
        private float func_178255_b(final JsonObject p_178255_1_) {
            final float var2 = JsonUtils.getJsonObjectFloatFieldValue(p_178255_1_, "angle");
            if (var2 != 0.0f && MathHelper.abs(var2) != 22.5f && MathHelper.abs(var2) != 45.0f) {
                throw new JsonParseException("Invalid rotation " + var2 + " found, only -45/-22.5/0/22.5/45 allowed");
            }
            return var2;
        }
        
        private EnumFacing.Axis func_178252_c(final JsonObject p_178252_1_) {
            final String var2 = JsonUtils.getJsonObjectStringFieldValue(p_178252_1_, "axis");
            final EnumFacing.Axis var3 = EnumFacing.Axis.byName(var2.toLowerCase());
            if (var3 == null) {
                throw new JsonParseException("Invalid rotation axis: " + var2);
            }
            return var3;
        }
        
        private Map func_178250_a(final JsonDeserializationContext p_178250_1_, final JsonObject p_178250_2_) {
            final Map var3 = this.func_178253_b(p_178250_1_, p_178250_2_);
            if (var3.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            }
            return var3;
        }
        
        private Map func_178253_b(final JsonDeserializationContext p_178253_1_, final JsonObject p_178253_2_) {
            final EnumMap var3 = Maps.newEnumMap((Class)EnumFacing.class);
            final JsonObject var4 = JsonUtils.getJsonObject(p_178253_2_, "faces");
            for (final Map.Entry var6 : var4.entrySet()) {
                final EnumFacing var7 = this.func_178248_a(var6.getKey());
                var3.put(var7, p_178253_1_.deserialize((JsonElement)var6.getValue(), (Type)BlockPartFace.class));
            }
            return var3;
        }
        
        private EnumFacing func_178248_a(final String p_178248_1_) {
            final EnumFacing var2 = EnumFacing.byName(p_178248_1_);
            if (var2 == null) {
                throw new JsonParseException("Unknown facing: " + p_178248_1_);
            }
            return var2;
        }
        
        private Vector3f func_178247_d(final JsonObject p_178247_1_) {
            final Vector3f var2 = this.func_178251_a(p_178247_1_, "to");
            if (var2.x >= -16.0f && var2.y >= -16.0f && var2.z >= -16.0f && var2.x <= 32.0f && var2.y <= 32.0f && var2.z <= 32.0f) {
                return var2;
            }
            throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + var2);
        }
        
        private Vector3f func_178249_e(final JsonObject p_178249_1_) {
            final Vector3f var2 = this.func_178251_a(p_178249_1_, "from");
            if (var2.x >= -16.0f && var2.y >= -16.0f && var2.z >= -16.0f && var2.x <= 32.0f && var2.y <= 32.0f && var2.z <= 32.0f) {
                return var2;
            }
            throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + var2);
        }
        
        private Vector3f func_178251_a(final JsonObject p_178251_1_, final String p_178251_2_) {
            final JsonArray var3 = JsonUtils.getJsonObjectJsonArrayField(p_178251_1_, p_178251_2_);
            if (var3.size() != 3) {
                throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + var3.size());
            }
            final float[] var4 = new float[3];
            for (int var5 = 0; var5 < var4.length; ++var5) {
                var4[var5] = JsonUtils.getJsonElementFloatValue(var3.get(var5), p_178251_2_ + "[" + var5 + "]");
            }
            return new Vector3f(var4);
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.func_178254_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_178234_a;
        private static final String __OBFID = "CL_00002510";
        
        static {
            field_178234_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_178234_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
