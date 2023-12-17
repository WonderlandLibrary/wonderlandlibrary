// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.primitives.Floats;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import net.optifine.reflect.Reflector;
import net.minecraft.client.renderer.block.model.ItemOverride;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.CompoundKey;
import java.util.Map;

public class ItemOverrideCache
{
    private ItemOverrideProperty[] itemOverrideProperties;
    private Map<CompoundKey, ResourceLocation> mapValueModels;
    public static final ResourceLocation LOCATION_NULL;
    
    public ItemOverrideCache(final ItemOverrideProperty[] itemOverrideProperties) {
        this.mapValueModels = new HashMap<CompoundKey, ResourceLocation>();
        this.itemOverrideProperties = itemOverrideProperties;
    }
    
    public ResourceLocation getModelLocation(final ItemStack stack, final World world, final EntityLivingBase entity) {
        final CompoundKey compoundkey = this.getValueKey(stack, world, entity);
        return (compoundkey == null) ? null : this.mapValueModels.get(compoundkey);
    }
    
    public void putModelLocation(final ItemStack stack, final World world, final EntityLivingBase entity, final ResourceLocation location) {
        final CompoundKey compoundkey = this.getValueKey(stack, world, entity);
        if (compoundkey != null) {
            this.mapValueModels.put(compoundkey, location);
        }
    }
    
    private CompoundKey getValueKey(final ItemStack stack, final World world, final EntityLivingBase entity) {
        final Integer[] ainteger = new Integer[this.itemOverrideProperties.length];
        for (int i = 0; i < ainteger.length; ++i) {
            final Integer integer = this.itemOverrideProperties[i].getValueIndex(stack, world, entity);
            if (integer == null) {
                return null;
            }
            ainteger[i] = integer;
        }
        return new CompoundKey(ainteger);
    }
    
    public static ItemOverrideCache make(final List<ItemOverride> overrides) {
        if (overrides.isEmpty()) {
            return null;
        }
        if (!Reflector.ItemOverride_mapResourceValues.exists()) {
            return null;
        }
        final Map<ResourceLocation, Set<Float>> map = new LinkedHashMap<ResourceLocation, Set<Float>>();
        for (final ItemOverride itemoverride : overrides) {
            final Map<ResourceLocation, Float> map2 = (Map<ResourceLocation, Float>)Reflector.getFieldValue(itemoverride, Reflector.ItemOverride_mapResourceValues);
            for (final ResourceLocation resourcelocation : map2.keySet()) {
                final Float f = map2.get(resourcelocation);
                if (f != null) {
                    Set<Float> set = map.get(resourcelocation);
                    if (set == null) {
                        set = new HashSet<Float>();
                        map.put(resourcelocation, set);
                    }
                    set.add(f);
                }
            }
        }
        final List<ItemOverrideProperty> list = new ArrayList<ItemOverrideProperty>();
        for (final ResourceLocation resourcelocation2 : map.keySet()) {
            final Set<Float> set2 = map.get(resourcelocation2);
            final float[] afloat = Floats.toArray((Collection)set2);
            final ItemOverrideProperty itemoverrideproperty = new ItemOverrideProperty(resourcelocation2, afloat);
            list.add(itemoverrideproperty);
        }
        final ItemOverrideProperty[] aitemoverrideproperty = list.toArray(new ItemOverrideProperty[list.size()]);
        final ItemOverrideCache itemoverridecache = new ItemOverrideCache(aitemoverrideproperty);
        logCache(aitemoverrideproperty, overrides);
        return itemoverridecache;
    }
    
    private static void logCache(final ItemOverrideProperty[] props, final List<ItemOverride> overrides) {
        final StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < props.length; ++i) {
            final ItemOverrideProperty itemoverrideproperty = props[i];
            if (stringbuffer.length() > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append("" + itemoverrideproperty.getLocation() + "=" + itemoverrideproperty.getValues().length);
        }
        if (overrides.size() > 0) {
            stringbuffer.append(" -> " + overrides.get(0).getLocation() + " ...");
        }
        Config.dbg("ItemOverrideCache: " + stringbuffer.toString());
    }
    
    @Override
    public String toString() {
        return "properties: " + this.itemOverrideProperties.length + ", models: " + this.mapValueModels.size();
    }
    
    static {
        LOCATION_NULL = new ResourceLocation("");
    }
}
