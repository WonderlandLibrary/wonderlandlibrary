/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public interface LootCondition {
    public boolean testCondition(Random var1, LootContext var2);

    public static abstract class Serializer<T extends LootCondition> {
        private final ResourceLocation lootTableLocation;
        private final Class<T> conditionClass;

        protected Serializer(ResourceLocation location, Class<T> clazz) {
            this.lootTableLocation = location;
            this.conditionClass = clazz;
        }

        public ResourceLocation getLootTableLocation() {
            return this.lootTableLocation;
        }

        public Class<T> getConditionClass() {
            return this.conditionClass;
        }

        public abstract void serialize(JsonObject var1, T var2, JsonSerializationContext var3);

        public abstract T deserialize(JsonObject var1, JsonDeserializationContext var2);
    }
}

