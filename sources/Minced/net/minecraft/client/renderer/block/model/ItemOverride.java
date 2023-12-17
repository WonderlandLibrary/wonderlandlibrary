// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import net.minecraft.item.IItemPropertyGetter;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class ItemOverride
{
    private final ResourceLocation location;
    private final Map<ResourceLocation, Float> mapResourceValues;
    
    public ItemOverride(final ResourceLocation locationIn, final Map<ResourceLocation, Float> propertyValues) {
        this.location = locationIn;
        this.mapResourceValues = propertyValues;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    boolean matchesItemStack(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase livingEntity) {
        final Item item = stack.getItem();
        for (final Map.Entry<ResourceLocation, Float> entry : this.mapResourceValues.entrySet()) {
            final IItemPropertyGetter iitempropertygetter = item.getPropertyGetter(entry.getKey());
            if (iitempropertygetter == null || iitempropertygetter.apply(stack, worldIn, livingEntity) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
    static class Deserializer implements JsonDeserializer<ItemOverride>
    {
        public ItemOverride deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "model"));
            final Map<ResourceLocation, Float> map = this.makeMapResourceValues(jsonobject);
            return new ItemOverride(resourcelocation, map);
        }
        
        protected Map<ResourceLocation, Float> makeMapResourceValues(final JsonObject p_188025_1_) {
            final Map<ResourceLocation, Float> map = (Map<ResourceLocation, Float>)Maps.newLinkedHashMap();
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_188025_1_, "predicate");
            for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                map.put(new ResourceLocation(entry.getKey()), JsonUtils.getFloat(entry.getValue(), entry.getKey()));
            }
            return map;
        }
    }
}
