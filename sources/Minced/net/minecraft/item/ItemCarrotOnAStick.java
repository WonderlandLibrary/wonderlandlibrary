// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCarrotOnAStick extends Item
{
    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (playerIn.isRiding() && playerIn.getRidingEntity() instanceof EntityPig) {
            final EntityPig entitypig = (EntityPig)playerIn.getRidingEntity();
            if (itemstack.getMaxDamage() - itemstack.getMetadata() >= 7 && entitypig.boost()) {
                itemstack.damageItem(7, playerIn);
                if (itemstack.isEmpty()) {
                    final ItemStack itemstack2 = new ItemStack(Items.FISHING_ROD);
                    itemstack2.setTagCompound(itemstack.getTagCompound());
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack2);
                }
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
}
