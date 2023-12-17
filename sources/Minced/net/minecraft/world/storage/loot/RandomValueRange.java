// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.math.MathHelper;
import java.util.Random;

public class RandomValueRange
{
    private final float min;
    private final float max;
    
    public RandomValueRange(final float minIn, final float maxIn) {
        this.min = minIn;
        this.max = maxIn;
    }
    
    public RandomValueRange(final float value) {
        this.min = value;
        this.max = value;
    }
    
    public float getMin() {
        return this.min;
    }
    
    public float getMax() {
        return this.max;
    }
    
    public int generateInt(final Random rand) {
        return MathHelper.getInt(rand, MathHelper.floor(this.min), MathHelper.floor(this.max));
    }
    
    public float generateFloat(final Random rand) {
        return MathHelper.nextFloat(rand, this.min, this.max);
    }
    
    public boolean isInRange(final int value) {
        return value <= this.max && value >= this.min;
    }
    
    public static class Serializer implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange>
    {
        public RandomValueRange deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (JsonUtils.isNumber(p_deserialize_1_)) {
                return new RandomValueRange(JsonUtils.getFloat(p_deserialize_1_, "value"));
            }
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "value");
            final float f = JsonUtils.getFloat(jsonobject, "min");
            final float f2 = JsonUtils.getFloat(jsonobject, "max");
            return new RandomValueRange(f, f2);
        }
        
        public JsonElement serialize(final RandomValueRange p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.min == p_serialize_1_.max) {
                return (JsonElement)new JsonPrimitive((Number)p_serialize_1_.min);
            }
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("min", (Number)p_serialize_1_.min);
            jsonobject.addProperty("max", (Number)p_serialize_1_.max);
            return (JsonElement)jsonobject;
        }
    }
}
