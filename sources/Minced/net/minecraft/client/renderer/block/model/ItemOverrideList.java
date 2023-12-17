// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import net.optifine.ItemOverrideCache;
import java.util.List;

public class ItemOverrideList
{
    public static final ItemOverrideList NONE;
    private final List<ItemOverride> overrides;
    private ItemOverrideCache itemOverrideCache;
    
    private ItemOverrideList() {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
    }
    
    public ItemOverrideList(final List<ItemOverride> overridesIn) {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
        for (int i = overridesIn.size() - 1; i >= 0; --i) {
            this.overrides.add(overridesIn.get(i));
        }
        if (this.overrides.size() > 65) {
            this.itemOverrideCache = ItemOverrideCache.make(this.overrides);
        }
    }
    
    @Nullable
    public ResourceLocation applyOverride(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
        if (!this.overrides.isEmpty()) {
            if (this.itemOverrideCache != null) {
                final ResourceLocation resourcelocation = this.itemOverrideCache.getModelLocation(stack, worldIn, entityIn);
                if (resourcelocation != null) {
                    return (resourcelocation == ItemOverrideCache.LOCATION_NULL) ? null : resourcelocation;
                }
            }
            for (final ItemOverride itemoverride : this.overrides) {
                if (itemoverride.matchesItemStack(stack, worldIn, entityIn)) {
                    if (this.itemOverrideCache != null) {
                        this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, itemoverride.getLocation());
                    }
                    return itemoverride.getLocation();
                }
            }
            if (this.itemOverrideCache != null) {
                this.itemOverrideCache.putModelLocation(stack, worldIn, entityIn, ItemOverrideCache.LOCATION_NULL);
            }
        }
        return null;
    }
    
    public IBakedModel handleItemState(final IBakedModel p_handleItemState_1_, final ItemStack p_handleItemState_2_, @Nullable final World p_handleItemState_3_, @Nullable final EntityLivingBase p_handleItemState_4_) {
        if (!p_handleItemState_2_.isEmpty() && p_handleItemState_2_.getItem().hasCustomProperties()) {
            final ResourceLocation resourcelocation = this.applyOverride(p_handleItemState_2_, p_handleItemState_3_, p_handleItemState_4_);
            if (resourcelocation != null && Reflector.ModelLoader_getInventoryVariant.exists()) {
                final ModelResourceLocation modelresourcelocation = (ModelResourceLocation)Reflector.call(Reflector.ModelLoader_getInventoryVariant, resourcelocation.toString());
                return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(modelresourcelocation);
            }
        }
        return p_handleItemState_1_;
    }
    
    public ImmutableList<ItemOverride> getOverrides() {
        return (ImmutableList<ItemOverride>)ImmutableList.copyOf((Collection)this.overrides);
    }
    
    static {
        NONE = new ItemOverrideList();
    }
}
