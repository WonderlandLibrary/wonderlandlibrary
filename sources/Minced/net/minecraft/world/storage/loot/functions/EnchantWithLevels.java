// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;

public class EnchantWithLevels extends LootFunction
{
    private final RandomValueRange randomLevel;
    private final boolean isTreasure;
    
    public EnchantWithLevels(final LootCondition[] conditionsIn, final RandomValueRange randomRange, final boolean isTreasureIn) {
        super(conditionsIn);
        this.randomLevel = randomRange;
        this.isTreasure = isTreasureIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        return EnchantmentHelper.addRandomEnchantment(rand, stack, this.randomLevel.generateInt(rand), this.isTreasure);
    }
    
    public static class Serializer extends LootFunction.Serializer<EnchantWithLevels>
    {
        public Serializer() {
            super(new ResourceLocation("enchant_with_levels"), EnchantWithLevels.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final EnchantWithLevels functionClazz, final JsonSerializationContext serializationContext) {
            object.add("levels", serializationContext.serialize((Object)functionClazz.randomLevel));
            object.addProperty("treasure", Boolean.valueOf(functionClazz.isTreasure));
        }
        
        @Override
        public EnchantWithLevels deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            final RandomValueRange randomvaluerange = JsonUtils.deserializeClass(object, "levels", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class);
            final boolean flag = JsonUtils.getBoolean(object, "treasure", false);
            return new EnchantWithLevels(conditionsIn, randomvaluerange, flag);
        }
    }
}
