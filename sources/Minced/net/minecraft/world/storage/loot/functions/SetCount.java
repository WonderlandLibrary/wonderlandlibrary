// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;

public class SetCount extends LootFunction
{
    private final RandomValueRange countRange;
    
    public SetCount(final LootCondition[] conditionsIn, final RandomValueRange countRangeIn) {
        super(conditionsIn);
        this.countRange = countRangeIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        stack.setCount(this.countRange.generateInt(rand));
        return stack;
    }
    
    public static class Serializer extends LootFunction.Serializer<SetCount>
    {
        protected Serializer() {
            super(new ResourceLocation("set_count"), SetCount.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final SetCount functionClazz, final JsonSerializationContext serializationContext) {
            object.add("count", serializationContext.serialize((Object)functionClazz.countRange));
        }
        
        @Override
        public SetCount deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            return new SetCount(conditionsIn, JsonUtils.deserializeClass(object, "count", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class));
        }
    }
}
