// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class LootFunctionManager
{
    private static final Map<ResourceLocation, LootFunction.Serializer<?>> NAME_TO_SERIALIZER_MAP;
    private static final Map<Class<? extends LootFunction>, LootFunction.Serializer<?>> CLASS_TO_SERIALIZER_MAP;
    
    public static <T extends LootFunction> void registerFunction(final LootFunction.Serializer<? extends T> serializer) {
        final ResourceLocation resourcelocation = serializer.getFunctionName();
        final Class<T> oclass = (Class<T>)serializer.getFunctionClass();
        if (LootFunctionManager.NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
            throw new IllegalArgumentException("Can't re-register item function name " + resourcelocation);
        }
        if (LootFunctionManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can't re-register item function class " + oclass.getName());
        }
        LootFunctionManager.NAME_TO_SERIALIZER_MAP.put(resourcelocation, serializer);
        LootFunctionManager.CLASS_TO_SERIALIZER_MAP.put(oclass, serializer);
    }
    
    public static LootFunction.Serializer<?> getSerializerForName(final ResourceLocation location) {
        final LootFunction.Serializer<?> serializer = LootFunctionManager.NAME_TO_SERIALIZER_MAP.get(location);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item function '" + location + "'");
        }
        return serializer;
    }
    
    public static <T extends LootFunction> LootFunction.Serializer<T> getSerializerFor(final T functionClass) {
        final LootFunction.Serializer<T> serializer = (LootFunction.Serializer<T>)LootFunctionManager.CLASS_TO_SERIALIZER_MAP.get(functionClass.getClass());
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item function " + functionClass);
        }
        return serializer;
    }
    
    static {
        NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
        CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new SetCount.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new SetMetadata.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new EnchantWithLevels.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new EnchantRandomly.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new SetNBT.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new Smelt.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new LootingEnchantBonus.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new SetDamage.Serializer());
        registerFunction((LootFunction.Serializer<? extends LootFunction>)new SetAttributes.Serializer());
    }
    
    public static class Serializer implements JsonDeserializer<LootFunction>, JsonSerializer<LootFunction>
    {
        public LootFunction deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "function");
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "function"));
            LootFunction.Serializer<?> serializer;
            try {
                serializer = LootFunctionManager.getSerializerForName(resourcelocation);
            }
            catch (IllegalArgumentException var8) {
                throw new JsonSyntaxException("Unknown function '" + resourcelocation + "'");
            }
            return (LootFunction)serializer.deserialize(jsonobject, p_deserialize_3_, JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], p_deserialize_3_, LootCondition[].class));
        }
        
        public JsonElement serialize(final LootFunction p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final LootFunction.Serializer<LootFunction> serializer = LootFunctionManager.getSerializerFor(p_serialize_1_);
            final JsonObject jsonobject = new JsonObject();
            serializer.serialize(jsonobject, p_serialize_1_, p_serialize_3_);
            jsonobject.addProperty("function", serializer.getFunctionName().toString());
            if (p_serialize_1_.getConditions() != null && p_serialize_1_.getConditions().length > 0) {
                jsonobject.add("conditions", p_serialize_3_.serialize((Object)p_serialize_1_.getConditions()));
            }
            return (JsonElement)jsonobject;
        }
    }
}
