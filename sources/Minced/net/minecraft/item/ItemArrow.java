// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemArrow extends Item
{
    public ItemArrow() {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }
    
    public EntityArrow createArrow(final World worldIn, final ItemStack stack, final EntityLivingBase shooter) {
        final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
        entitytippedarrow.setPotionEffect(stack);
        return entitytippedarrow;
    }
}
