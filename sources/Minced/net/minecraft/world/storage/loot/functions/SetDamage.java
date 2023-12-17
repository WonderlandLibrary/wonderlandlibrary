// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;
import org.apache.logging.log4j.Logger;

public class SetDamage extends LootFunction
{
    private static final Logger LOGGER;
    private final RandomValueRange damageRange;
    
    public SetDamage(final LootCondition[] conditionsIn, final RandomValueRange damageRangeIn) {
        super(conditionsIn);
        this.damageRange = damageRangeIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        if (stack.isItemStackDamageable()) {
            final float f = 1.0f - this.damageRange.generateFloat(rand);
            stack.setItemDamage(MathHelper.floor(f * stack.getMaxDamage()));
        }
        else {
            SetDamage.LOGGER.warn("Couldn't set damage of loot item {}", (Object)stack);
        }
        return stack;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootFunction.Serializer<SetDamage>
    {
        protected Serializer() {
            super(new ResourceLocation("set_damage"), SetDamage.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final SetDamage functionClazz, final JsonSerializationContext serializationContext) {
            object.add("damage", serializationContext.serialize((Object)functionClazz.damageRange));
        }
        
        @Override
        public SetDamage deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            return new SetDamage(conditionsIn, JsonUtils.deserializeClass(object, "damage", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class));
        }
    }
}
