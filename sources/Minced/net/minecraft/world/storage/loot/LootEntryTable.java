// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.util.ResourceLocation;

public class LootEntryTable extends LootEntry
{
    protected final ResourceLocation table;
    
    public LootEntryTable(final ResourceLocation tableIn, final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        super(weightIn, qualityIn, conditionsIn);
        this.table = tableIn;
    }
    
    @Override
    public void addLoot(final Collection<ItemStack> stacks, final Random rand, final LootContext context) {
        final LootTable loottable = context.getLootTableManager().getLootTableFromLocation(this.table);
        final Collection<ItemStack> collection = loottable.generateLootForPools(rand, context);
        stacks.addAll(collection);
    }
    
    @Override
    protected void serialize(final JsonObject json, final JsonSerializationContext context) {
        json.addProperty("name", this.table.toString());
    }
    
    public static LootEntryTable deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(object, "name"));
        return new LootEntryTable(resourcelocation, weightIn, qualityIn, conditionsIn);
    }
}
