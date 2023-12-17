// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEmptyMap extends ItemMapBase
{
    protected ItemEmptyMap() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = ItemMap.setupNewMap(worldIn, playerIn.posX, playerIn.posZ, (byte)0, true, false);
        final ItemStack itemstack2 = playerIn.getHeldItem(handIn);
        itemstack2.shrink(1);
        if (itemstack2.isEmpty()) {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        if (!playerIn.inventory.addItemStackToInventory(itemstack.copy())) {
            playerIn.dropItem(itemstack, false);
        }
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack2);
    }
}
