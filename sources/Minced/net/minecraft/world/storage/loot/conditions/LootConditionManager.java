// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.common.collect.Maps;
import net.minecraft.world.storage.loot.LootContext;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class LootConditionManager
{
    private static final Map<ResourceLocation, LootCondition.Serializer<?>> NAME_TO_SERIALIZER_MAP;
    private static final Map<Class<? extends LootCondition>, LootCondition.Serializer<?>> CLASS_TO_SERIALIZER_MAP;
    
    public static <T extends LootCondition> void registerCondition(final LootCondition.Serializer<? extends T> condition) {
        final ResourceLocation resourcelocation = condition.getLootTableLocation();
        final Class<T> oclass = (Class<T>)condition.getConditionClass();
        if (LootConditionManager.NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
            throw new IllegalArgumentException("Can't re-register item condition name " + resourcelocation);
        }
        if (LootConditionManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can't re-register item condition class " + oclass.getName());
        }
        LootConditionManager.NAME_TO_SERIALIZER_MAP.put(resourcelocation, condition);
        LootConditionManager.CLASS_TO_SERIALIZER_MAP.put(oclass, condition);
    }
    
    public static boolean testAllConditions(@Nullable final LootCondition[] conditions, final Random rand, final LootContext context) {
        if (conditions == null) {
            return true;
        }
        for (final LootCondition lootcondition : conditions) {
            if (!lootcondition.testCondition(rand, context)) {
                return false;
            }
        }
        return true;
    }
    
    public static LootCondition.Serializer<?> getSerializerForName(final ResourceLocation location) {
        final LootCondition.Serializer<?> serializer = LootConditionManager.NAME_TO_SERIALIZER_MAP.get(location);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item condition '" + location + "'");
        }
        return serializer;
    }
    
    public static <T extends LootCondition> LootCondition.Serializer<T> getSerializerFor(final T conditionClass) {
        final LootCondition.Serializer<T> serializer = (LootCondition.Serializer<T>)LootConditionManager.CLASS_TO_SERIALIZER_MAP.get(conditionClass.getClass());
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot item condition " + conditionClass);
        }
        return serializer;
    }
    
    static {
        NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
        CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();
        registerCondition((LootCondition.Serializer<? extends LootCondition>)new RandomChance.Serializer());
        registerCondition((LootCondition.Serializer<? extends LootCondition>)new RandomChanceWithLooting.Serializer());
        registerCondition((LootCondition.Serializer<? extends LootCondition>)new EntityHasProperty.Serializer());
        registerCondition((LootCondition.Serializer<? extends LootCondition>)new KilledByPlayer.Serializer());
        registerCondition((LootCondition.Serializer<? extends LootCondition>)new EntityHasScore.Serializer());
    }
    
    public static class Serializer implements JsonDeserializer<LootCondition>, JsonSerializer<LootCondition>
    {
        public LootCondition deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "condition");
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "condition"));
            LootCondition.Serializer<?> serializer;
            try {
                serializer = LootConditionManager.getSerializerForName(resourcelocation);
            }
            catch (IllegalArgumentException var8) {
                throw new JsonSyntaxException("Unknown condition '" + resourcelocation + "'");
            }
            return (LootCondition)serializer.deserialize(jsonobject, p_deserialize_3_);
        }
        
        public JsonElement serialize(final LootCondition p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final LootCondition.Serializer<LootCondition> serializer = LootConditionManager.getSerializerFor(p_serialize_1_);
            final JsonObject jsonobject = new JsonObject();
            serializer.serialize(jsonobject, p_serialize_1_, p_serialize_3_);
            jsonobject.addProperty("condition", serializer.getLootTableLocation().toString());
            return (JsonElement)jsonobject;
        }
    }
}
