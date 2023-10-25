package net.optifine;

import com.google.common.primitives.Floats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;
import net.optifine.util.CompoundKey;

public class ItemOverrideCache
{
    private ItemOverrideProperty[] itemOverrideProperties;
    private Map<CompoundKey, ResourceLocation> mapValueModels = new HashMap<CompoundKey, ResourceLocation>();
    public static final ResourceLocation LOCATION_NULL = new ResourceLocation("");

    public ItemOverrideCache(ItemOverrideProperty[] itemOverrideProperties)
    {
        this.itemOverrideProperties = itemOverrideProperties;
    }

    public ResourceLocation getModelLocation(ItemStack stack, World world, EntityLivingBase entity)
    {
        CompoundKey compoundkey = this.getValueKey(stack, world, entity);
        return compoundkey == null ? null : (ResourceLocation)this.mapValueModels.get(compoundkey);
    }

    public void putModelLocation(ItemStack stack, World world, EntityLivingBase entity, ResourceLocation location)
    {
        CompoundKey compoundkey = this.getValueKey(stack, world, entity);

        if (compoundkey != null)
        {
            this.mapValueModels.put(compoundkey, location);
        }
    }

    private CompoundKey getValueKey(ItemStack stack, World world, EntityLivingBase entity)
    {
        Integer[] ainteger = new Integer[this.itemOverrideProperties.length];

        for (int i = 0; i < ainteger.length; ++i)
        {
            Integer integer = this.itemOverrideProperties[i].getValueIndex(stack, world, entity);

            if (integer == null)
            {
                return null;
            }

            ainteger[i] = integer;
        }

        return new CompoundKey(ainteger);
    }

    public static ItemOverrideCache make(List<ItemOverride> overrides)
    {
        if (overrides.isEmpty())
        {
            return null;
        }
        else if (!Reflector.ItemOverride_mapResourceValues.exists())
        {
            return null;
        }
        else
        {
            Map<ResourceLocation, Set<Float>> map = new LinkedHashMap<ResourceLocation, Set<Float>>();

            for (ItemOverride itemoverride : overrides)
            {
                Map<ResourceLocation, Float> map1 = (Map)Reflector.getFieldValue(itemoverride, Reflector.ItemOverride_mapResourceValues);

                for (ResourceLocation resourcelocation : map1.keySet())
                {
                    Float f = map1.get(resourcelocation);

                    if (f != null)
                    {
                        Set<Float> set = (Set)map.get(resourcelocation);

                        if (set == null)
                        {
                            set = new HashSet<Float>();
                            map.put(resourcelocation, set);
                        }

                        set.add(f);
                    }
                }
            }

            List<ItemOverrideProperty> list = new ArrayList<ItemOverrideProperty>();

            for (ResourceLocation resourcelocation1 : map.keySet())
            {
                Set<Float> set1 = (Set)map.get(resourcelocation1);
                float[] afloat = Floats.toArray(set1);
                ItemOverrideProperty itemoverrideproperty = new ItemOverrideProperty(resourcelocation1, afloat);
                list.add(itemoverrideproperty);
            }

            ItemOverrideProperty[] aitemoverrideproperty = (ItemOverrideProperty[])list.toArray(new ItemOverrideProperty[list.size()]);
            ItemOverrideCache itemoverridecache = new ItemOverrideCache(aitemoverrideproperty);
            logCache(aitemoverrideproperty, overrides);
            return itemoverridecache;
        }
    }

    private static void logCache(ItemOverrideProperty[] props, List<ItemOverride> overrides)
    {
        StringBuffer stringbuffer = new StringBuffer();

        for (int i = 0; i < props.length; ++i)
        {
            ItemOverrideProperty itemoverrideproperty = props[i];

            if (stringbuffer.length() > 0)
            {
                stringbuffer.append(", ");
            }

            stringbuffer.append("" + itemoverrideproperty.getLocation() + "=" + itemoverrideproperty.getValues().length);
        }

        if (overrides.size() > 0)
        {
            stringbuffer.append(" -> " + ((ItemOverride)overrides.get(0)).getLocation() + " ...");
        }

        Config.dbg("ItemOverrideCache: " + stringbuffer.toString());
    }

    public String toString()
    {
        return "properties: " + this.itemOverrideProperties.length + ", models: " + this.mapValueModels.size();
    }
}
