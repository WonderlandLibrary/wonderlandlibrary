// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;

public interface LootCondition
{
    boolean testCondition(final Random p0, final LootContext p1);
    
    public abstract static class Serializer<T extends LootCondition>
    {
        private final ResourceLocation lootTableLocation;
        private final Class<T> conditionClass;
        
        protected Serializer(final ResourceLocation location, final Class<T> clazz) {
            this.lootTableLocation = location;
            this.conditionClass = clazz;
        }
        
        public ResourceLocation getLootTableLocation() {
            return this.lootTableLocation;
        }
        
        public Class<T> getConditionClass() {
            return this.conditionClass;
        }
        
        public abstract void serialize(final JsonObject p0, final T p1, final JsonSerializationContext p2);
        
        public abstract T deserialize(final JsonObject p0, final JsonDeserializationContext p1);
    }
}
