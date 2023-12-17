// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;

public class LootingEnchantBonus extends LootFunction
{
    private final RandomValueRange count;
    private final int limit;
    
    public LootingEnchantBonus(final LootCondition[] conditions, final RandomValueRange countIn, final int limitIn) {
        super(conditions);
        this.count = countIn;
        this.limit = limitIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        final Entity entity = context.getKiller();
        if (entity instanceof EntityLivingBase) {
            final int i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
            if (i == 0) {
                return stack;
            }
            final float f = i * this.count.generateFloat(rand);
            stack.grow(Math.round(f));
            if (this.limit != 0 && stack.getCount() > this.limit) {
                stack.setCount(this.limit);
            }
        }
        return stack;
    }
    
    public static class Serializer extends LootFunction.Serializer<LootingEnchantBonus>
    {
        protected Serializer() {
            super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final LootingEnchantBonus functionClazz, final JsonSerializationContext serializationContext) {
            object.add("count", serializationContext.serialize((Object)functionClazz.count));
            if (functionClazz.limit > 0) {
                object.add("limit", serializationContext.serialize((Object)functionClazz.limit));
            }
        }
        
        @Override
        public LootingEnchantBonus deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            final int i = JsonUtils.getInt(object, "limit", 0);
            return new LootingEnchantBonus(conditionsIn, JsonUtils.deserializeClass(object, "count", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class), i);
        }
    }
}
