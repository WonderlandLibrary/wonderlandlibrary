// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;

public class RandomChanceWithLooting implements LootCondition
{
    private final float chance;
    private final float lootingMultiplier;
    
    public RandomChanceWithLooting(final float chanceIn, final float lootingMultiplierIn) {
        this.chance = chanceIn;
        this.lootingMultiplier = lootingMultiplierIn;
    }
    
    @Override
    public boolean testCondition(final Random rand, final LootContext context) {
        int i = 0;
        if (context.getKiller() instanceof EntityLivingBase) {
            i = EnchantmentHelper.getLootingModifier((EntityLivingBase)context.getKiller());
        }
        return rand.nextFloat() < this.chance + i * this.lootingMultiplier;
    }
    
    public static class Serializer extends LootCondition.Serializer<RandomChanceWithLooting>
    {
        protected Serializer() {
            super(new ResourceLocation("random_chance_with_looting"), RandomChanceWithLooting.class);
        }
        
        @Override
        public void serialize(final JsonObject json, final RandomChanceWithLooting value, final JsonSerializationContext context) {
            json.addProperty("chance", (Number)value.chance);
            json.addProperty("looting_multiplier", (Number)value.lootingMultiplier);
        }
        
        @Override
        public RandomChanceWithLooting deserialize(final JsonObject json, final JsonDeserializationContext context) {
            return new RandomChanceWithLooting(JsonUtils.getFloat(json, "chance"), JsonUtils.getFloat(json, "looting_multiplier"));
        }
    }
}
