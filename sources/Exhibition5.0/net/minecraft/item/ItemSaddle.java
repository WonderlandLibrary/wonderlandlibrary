// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSaddle extends Item
{
    private static final String __OBFID = "CL_00000059";
    
    public ItemSaddle() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (target instanceof EntityPig) {
            final EntityPig var4 = (EntityPig)target;
            if (!var4.getSaddled() && !var4.isChild()) {
                var4.setSaddled(true);
                var4.worldObj.playSoundAtEntity(var4, "mob.horse.leather", 0.5f, 1.0f);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        this.itemInteractionForEntity(stack, null, target);
        return true;
    }
}
