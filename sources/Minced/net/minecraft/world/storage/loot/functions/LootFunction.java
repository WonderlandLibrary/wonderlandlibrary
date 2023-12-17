// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction
{
    private final LootCondition[] conditions;
    
    protected LootFunction(final LootCondition[] conditionsIn) {
        this.conditions = conditionsIn;
    }
    
    public abstract ItemStack apply(final ItemStack p0, final Random p1, final LootContext p2);
    
    public LootCondition[] getConditions() {
        return this.conditions;
    }
    
    public abstract static class Serializer<T extends LootFunction>
    {
        private final ResourceLocation lootTableLocation;
        private final Class<T> functionClass;
        
        protected Serializer(final ResourceLocation location, final Class<T> clazz) {
            this.lootTableLocation = location;
            this.functionClass = clazz;
        }
        
        public ResourceLocation getFunctionName() {
            return this.lootTableLocation;
        }
        
        public Class<T> getFunctionClass() {
            return this.functionClass;
        }
        
        public abstract void serialize(final JsonObject p0, final T p1, final JsonSerializationContext p2);
        
        public abstract T deserialize(final JsonObject p0, final JsonDeserializationContext p1, final LootCondition[] p2);
    }
}
