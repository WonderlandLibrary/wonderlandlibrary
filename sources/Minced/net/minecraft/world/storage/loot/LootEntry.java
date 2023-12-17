// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootEntry
{
    protected final int weight;
    protected final int quality;
    protected final LootCondition[] conditions;
    
    protected LootEntry(final int weightIn, final int qualityIn, final LootCondition[] conditionsIn) {
        this.weight = weightIn;
        this.quality = qualityIn;
        this.conditions = conditionsIn;
    }
    
    public int getEffectiveWeight(final float luck) {
        return Math.max(MathHelper.floor(this.weight + this.quality * luck), 0);
    }
    
    public abstract void addLoot(final Collection<ItemStack> p0, final Random p1, final LootContext p2);
    
    protected abstract void serialize(final JsonObject p0, final JsonSerializationContext p1);
    
    public static class Serializer implements JsonDeserializer<LootEntry>, JsonSerializer<LootEntry>
    {
        public LootEntry deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot item");
            final String s = JsonUtils.getString(jsonobject, "type");
            final int i = JsonUtils.getInt(jsonobject, "weight", 1);
            final int j = JsonUtils.getInt(jsonobject, "quality", 0);
            LootCondition[] alootcondition;
            if (jsonobject.has("conditions")) {
                alootcondition = JsonUtils.deserializeClass(jsonobject, "conditions", p_deserialize_3_, (Class<? extends LootCondition[]>)LootCondition[].class);
            }
            else {
                alootcondition = new LootCondition[0];
            }
            if ("item".equals(s)) {
                return LootEntryItem.deserialize(jsonobject, p_deserialize_3_, i, j, alootcondition);
            }
            if ("loot_table".equals(s)) {
                return LootEntryTable.deserialize(jsonobject, p_deserialize_3_, i, j, alootcondition);
            }
            if ("empty".equals(s)) {
                return LootEntryEmpty.deserialize(jsonobject, p_deserialize_3_, i, j, alootcondition);
            }
            throw new JsonSyntaxException("Unknown loot entry type '" + s + "'");
        }
        
        public JsonElement serialize(final LootEntry p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("weight", (Number)p_serialize_1_.weight);
            jsonobject.addProperty("quality", (Number)p_serialize_1_.quality);
            if (p_serialize_1_.conditions.length > 0) {
                jsonobject.add("conditions", p_serialize_3_.serialize((Object)p_serialize_1_.conditions));
            }
            if (p_serialize_1_ instanceof LootEntryItem) {
                jsonobject.addProperty("type", "item");
            }
            else if (p_serialize_1_ instanceof LootEntryTable) {
                jsonobject.addProperty("type", "loot_table");
            }
            else {
                if (!(p_serialize_1_ instanceof LootEntryEmpty)) {
                    throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_);
                }
                jsonobject.addProperty("type", "empty");
            }
            p_serialize_1_.serialize(jsonobject, p_serialize_3_);
            return (JsonElement)jsonobject;
        }
    }
}
