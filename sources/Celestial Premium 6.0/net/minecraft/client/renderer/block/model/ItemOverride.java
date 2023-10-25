/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemOverride {
    private final ResourceLocation location;
    private final Map<ResourceLocation, Float> mapResourceValues;

    public ItemOverride(ResourceLocation locationIn, Map<ResourceLocation, Float> propertyValues) {
        this.location = locationIn;
        this.mapResourceValues = propertyValues;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    boolean matchesItemStack(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase livingEntity) {
        Item item = stack.getItem();
        for (Map.Entry<ResourceLocation, Float> entry : this.mapResourceValues.entrySet()) {
            IItemPropertyGetter iitempropertygetter = item.getPropertyGetter(entry.getKey());
            if (iitempropertygetter != null && !(iitempropertygetter.apply(stack, worldIn, livingEntity) < entry.getValue().floatValue())) continue;
            return false;
        }
        return true;
    }

    static class Deserializer
    implements JsonDeserializer<ItemOverride> {
        Deserializer() {
        }

        @Override
        public ItemOverride deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "model"));
            Map<ResourceLocation, Float> map = this.makeMapResourceValues(jsonobject);
            return new ItemOverride(resourcelocation, map);
        }

        protected Map<ResourceLocation, Float> makeMapResourceValues(JsonObject p_188025_1_) {
            LinkedHashMap<ResourceLocation, Float> map = Maps.newLinkedHashMap();
            JsonObject jsonobject = JsonUtils.getJsonObject(p_188025_1_, "predicate");
            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                map.put(new ResourceLocation(entry.getKey()), Float.valueOf(JsonUtils.getFloat(entry.getValue(), entry.getKey())));
            }
            return map;
        }
    }
}

