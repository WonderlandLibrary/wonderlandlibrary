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
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.RandomValueRange;
import org.apache.logging.log4j.Logger;

public class SetMetadata extends LootFunction
{
    private static final Logger LOGGER;
    private final RandomValueRange metaRange;
    
    public SetMetadata(final LootCondition[] conditionsIn, final RandomValueRange metaRangeIn) {
        super(conditionsIn);
        this.metaRange = metaRangeIn;
    }
    
    @Override
    public ItemStack apply(final ItemStack stack, final Random rand, final LootContext context) {
        if (stack.isItemStackDamageable()) {
            SetMetadata.LOGGER.warn("Couldn't set data of loot item {}", (Object)stack);
        }
        else {
            stack.setItemDamage(this.metaRange.generateInt(rand));
        }
        return stack;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootFunction.Serializer<SetMetadata>
    {
        protected Serializer() {
            super(new ResourceLocation("set_data"), SetMetadata.class);
        }
        
        @Override
        public void serialize(final JsonObject object, final SetMetadata functionClazz, final JsonSerializationContext serializationContext) {
            object.add("data", serializationContext.serialize((Object)functionClazz.metaRange));
        }
        
        @Override
        public SetMetadata deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootCondition[] conditionsIn) {
            return new SetMetadata(conditionsIn, JsonUtils.deserializeClass(object, "data", deserializationContext, (Class<? extends RandomValueRange>)RandomValueRange.class));
        }
    }
}
