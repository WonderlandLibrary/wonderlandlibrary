// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootPool
{
    private final LootEntry[] lootEntries;
    private final LootCondition[] poolConditions;
    private final RandomValueRange rolls;
    private final RandomValueRange bonusRolls;
    
    public LootPool(final LootEntry[] lootEntriesIn, final LootCondition[] poolConditionsIn, final RandomValueRange rollsIn, final RandomValueRange bonusRollsIn) {
        this.lootEntries = lootEntriesIn;
        this.poolConditions = poolConditionsIn;
        this.rolls = rollsIn;
        this.bonusRolls = bonusRollsIn;
    }
    
    protected void createLootRoll(final Collection<ItemStack> stacks, final Random rand, final LootContext context) {
        final List<LootEntry> list = (List<LootEntry>)Lists.newArrayList();
        int i = 0;
        for (final LootEntry lootentry : this.lootEntries) {
            if (LootConditionManager.testAllConditions(lootentry.conditions, rand, context)) {
                final int j = lootentry.getEffectiveWeight(context.getLuck());
                if (j > 0) {
                    list.add(lootentry);
                    i += j;
                }
            }
        }
        if (i != 0 && !list.isEmpty()) {
            int k = rand.nextInt(i);
            for (final LootEntry lootentry2 : list) {
                k -= lootentry2.getEffectiveWeight(context.getLuck());
                if (k < 0) {
                    lootentry2.addLoot(stacks, rand, context);
                }
            }
        }
    }
    
    public void generateLoot(final Collection<ItemStack> stacks, final Random rand, final LootContext context) {
        if (LootConditionManager.testAllConditions(this.poolConditions, rand, context)) {
            for (int i = this.rolls.generateInt(rand) + MathHelper.floor(this.bonusRolls.generateFloat(rand) * context.getLuck()), j = 0; j < i; ++j) {
                this.createLootRoll(stacks, rand, context);
            }
        }
    }
    
    public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool>
    {
        public LootPool deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot pool");
            final LootEntry[] alootentry = JsonUtils.deserializeClass(jsonobject, "entries", p_deserialize_3_, (Class<? extends LootEntry[]>)LootEntry[].class);
            final LootCondition[] alootcondition = JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], p_deserialize_3_, LootCondition[].class);
            final RandomValueRange randomvaluerange = JsonUtils.deserializeClass(jsonobject, "rolls", p_deserialize_3_, (Class<? extends RandomValueRange>)RandomValueRange.class);
            final RandomValueRange randomvaluerange2 = JsonUtils.deserializeClass(jsonobject, "bonus_rolls", new RandomValueRange(0.0f, 0.0f), p_deserialize_3_, RandomValueRange.class);
            return new LootPool(alootentry, alootcondition, randomvaluerange, randomvaluerange2);
        }
        
        public JsonElement serialize(final LootPool p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.add("entries", p_serialize_3_.serialize((Object)p_serialize_1_.lootEntries));
            jsonobject.add("rolls", p_serialize_3_.serialize((Object)p_serialize_1_.rolls));
            if (p_serialize_1_.bonusRolls.getMin() != 0.0f && p_serialize_1_.bonusRolls.getMax() != 0.0f) {
                jsonobject.add("bonus_rolls", p_serialize_3_.serialize((Object)p_serialize_1_.bonusRolls));
            }
            if (!ArrayUtils.isEmpty((Object[])p_serialize_1_.poolConditions)) {
                jsonobject.add("conditions", p_serialize_3_.serialize((Object)p_serialize_1_.poolConditions));
            }
            return (JsonElement)jsonobject;
        }
    }
}
