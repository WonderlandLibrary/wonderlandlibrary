// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemNameTag extends Item
{
    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.TOOLS);
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target, final EnumHand hand) {
        if (stack.hasDisplayName() && !(target instanceof EntityPlayer)) {
            target.setCustomNameTag(stack.getDisplayName());
            if (target instanceof EntityLiving) {
                ((EntityLiving)target).enablePersistence();
            }
            stack.shrink(1);
            return true;
        }
        return false;
    }
}
