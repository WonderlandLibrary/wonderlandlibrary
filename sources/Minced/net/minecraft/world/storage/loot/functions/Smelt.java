// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.Logger;

public class Smelt extends LootFunction
{
    private static final Logger LOGGER;
    
    public Smelt(final LootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        if (stack.isEmpty()) {
            return stack;
        }
        final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stack);
        if (itemstack.isEmpty()) {
            Smelt.LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", (Object)stack);
            return stack;
        }
        final ItemStack itemstack2 = itemstack.copy();
        itemstack2.setCount(stack.getCount());
        return itemstack2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootFunction.Serializer<Smelt>
    {
        protected Serializer() {
            super(new ResourceLocation("furnace_smelt"), Smelt.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final Smelt functionClazz, final JsonSerializationContext serializationContext) {
        }
        
        @Override
        public Smelt deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            return new Smelt(conditionsIn);
        }
    }
}
