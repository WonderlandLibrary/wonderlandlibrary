// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryEmpty extends LootEntry
{
    public LootEntryEmpty(final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        super(weightIn, qualityIn, conditionsIn);
    }
    
    @Override
    public void addLoot(final Collection<ItemStack> stacks, final Random rand, final LootContext context) {
    }
    
    @Override
    protected void serialize(final JsonObject json, final JsonSerializationContext context) {
    }
    
    public static LootEntryEmpty deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        return new LootEntryEmpty(weightIn, qualityIn, conditionsIn);
    }
}
