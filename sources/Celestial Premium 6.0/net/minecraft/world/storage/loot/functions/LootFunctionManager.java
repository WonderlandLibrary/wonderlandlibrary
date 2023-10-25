/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage.loot.functions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Map;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetAttributes;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraft.world.storage.loot.functions.Smelt;

public class LootFunctionManager {
    private static final Map<ResourceLocation, LootFunction.Serializer<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
    private static final Map<Class<? extends LootFunction>, LootFunction.Serializer<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

    public static <T extends LootFunction> void registerFunction(LootFunction.Serializer<? extends T> p_186582_0_) {
        ResourceLocation resourcelocation = p_186582_0_.getFunctionName();
        Class<T> oclass = p_186582_0_.getFunctionClass();
        if (NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
            throw new IllegalArgumentException("Can't re-register item function name " + resourcelocation);
        }
        if (CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can't re-register item function class " + oclass.getName());
        }
        NAME_TO_SERIALIZER_MAP.put(resourcelocation, p_186582_0_);
        CLASS_TO_SERIALIZER_MAP.put(oclass, p_186582_0_);
    }

    public static LootFunction.Serializer<?> getSerializerForName(ResourceLocation location) {
        LootFunction.Serializer<?> serializer = NAME_TO_SERIALIZER_MAP.get(location);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item function '" + location + "'");
        }
        return serializer;
    }

    public static <T extends LootFunction> LootFunction.Serializer<T> getSerializerFor(T functionClass) {
        LootFunction.Serializer<?> serializer = CLASS_TO_SERIALIZER_MAP.get(functionClass.getClass());
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item function " + functionClass);
        }
        return serializer;
    }

    static {
        LootFunctionManager.registerFunction(new SetCount.Serializer());
        LootFunctionManager.registerFunction(new SetMetadata.Serializer());
        LootFunctionManager.registerFunction(new EnchantWithLevels.Serializer());
        LootFunctionManager.registerFunction(new EnchantRandomly.Serializer());
        LootFunctionManager.registerFunction(new SetNBT.Serializer());
        LootFunctionManager.registerFunction(new Smelt.Serializer());
        LootFunctionManager.registerFunction(new LootingEnchantBonus.Serializer());
        LootFunctionManager.registerFunction(new SetDamage.Serializer());
        LootFunctionManager.registerFunction(new SetAttributes.Serializer());
    }

    public static class Serializer
    implements JsonDeserializer<LootFunction>,
    JsonSerializer<LootFunction> {
        @Override
        public LootFunction deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            LootFunction.Serializer<?> serializer;
            JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "function");
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "function"));
            try {
                serializer = LootFunctionManager.getSerializerForName(resourcelocation);
            }
            catch (IllegalArgumentException var8) {
                throw new JsonSyntaxException("Unknown function '" + resourcelocation + "'");
            }
            return serializer.deserialize(jsonobject, p_deserialize_3_, JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], p_deserialize_3_, LootCondition[].class));
        }

        @Override
        public JsonElement serialize(LootFunction p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            LootFunction.Serializer<LootFunction> serializer = LootFunctionManager.getSerializerFor(p_serialize_1_);
            JsonObject jsonobject = new JsonObject();
            serializer.serialize(jsonobject, p_serialize_1_, p_serialize_3_);
            jsonobject.addProperty("function", serializer.getFunctionName().toString());
            if (p_serialize_1_.getConditions() != null && p_serialize_1_.getConditions().length > 0) {
                jsonobject.add("conditions", p_serialize_3_.serialize(p_serialize_1_.getConditions()));
            }
            return jsonobject;
        }
    }
}

