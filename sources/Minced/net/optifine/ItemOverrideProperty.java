// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.Arrays;
import net.minecraft.util.ResourceLocation;

public class ItemOverrideProperty
{
    private ResourceLocation location;
    private float[] values;
    
    public ItemOverrideProperty(final ResourceLocation location, final float[] values) {
        this.location = location;
        Arrays.sort(this.values = values.clone());
    }
    
    public Integer getValueIndex(final ItemStack stack, final World world, final EntityLivingBase entity) {
        final Item item = stack.getItem();
        final IItemPropertyGetter iitempropertygetter = item.getPropertyGetter(this.location);
        if (iitempropertygetter == null) {
            return null;
        }
        final float f = iitempropertygetter.apply(stack, world, entity);
        final int i = Arrays.binarySearch(this.values, f);
        return i;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public float[] getValues() {
        return this.values;
    }
    
    @Override
    public String toString() {
        return "location: " + this.location + ", values: [" + Config.arrayToString(this.values) + "]";
    }
}
