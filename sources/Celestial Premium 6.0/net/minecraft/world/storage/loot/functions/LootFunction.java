/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction {
    private final LootCondition[] conditions;

    protected LootFunction(LootCondition[] conditionsIn) {
        this.conditions = conditionsIn;
    }

    public abstract ItemStack apply(ItemStack var1, Random var2, LootContext var3);

    public LootCondition[] getConditions() {
        return this.conditions;
    }

    public static abstract class Serializer<T extends LootFunction> {
        private final ResourceLocation lootTableLocation;
        private final Class<T> functionClass;

        protected Serializer(ResourceLocation location, Class<T> clazz) {
            this.lootTableLocation = location;
            this.functionClass = clazz;
        }

        public ResourceLocation getFunctionName() {
            return this.lootTableLocation;
        }

        public Class<T> getFunctionClass() {
            return this.functionClass;
        }

        public abstract void serialize(JsonObject var1, T var2, JsonSerializationContext var3);

        public abstract T deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3);
    }
}

