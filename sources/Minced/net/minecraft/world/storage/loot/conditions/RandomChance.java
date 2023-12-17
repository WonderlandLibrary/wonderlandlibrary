// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;

public class RandomChance implements LootCondition
{
    private final float chance;
    
    public RandomChance(final float chanceIn) {
        this.chance = chanceIn;
    }
    
    @Override
    public boolean testCondition(final Random rand, final LootContext context) {
        return rand.nextFloat() < this.chance;
    }
    
    public static class Serializer extends LootCondition.Serializer<RandomChance>
    {
        protected Serializer() {
            super(new ResourceLocation("random_chance"), RandomChance.class);
        }
        
        @Override
        public void serialize(final JsonObject json, final RandomChance value, final JsonSerializationContext context) {
            json.addProperty("chance", (Number)value.chance);
        }
        
        @Override
        public RandomChance deserialize(final JsonObject json, final JsonDeserializationContext context) {
            return new RandomChance(JsonUtils.getFloat(json, "chance"));
        }
    }
}
