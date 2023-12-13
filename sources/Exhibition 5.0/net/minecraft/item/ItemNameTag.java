// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemNameTag extends Item
{
    private static final String __OBFID = "CL_00000052";
    
    public ItemNameTag() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (!stack.hasDisplayName()) {
            return false;
        }
        if (target instanceof EntityLiving) {
            final EntityLiving var4 = (EntityLiving)target;
            var4.setCustomNameTag(stack.getDisplayName());
            var4.enablePersistence();
            --stack.stackSize;
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target);
    }
}
