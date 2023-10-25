package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBucketMilk extends Item {
  public ItemBucketMilk() {
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    if (!playerIn.capabilities.isCreativeMode)
      stack.stackSize--; 
    if (!worldIn.isRemote)
      playerIn.clearActivePotions(); 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return (stack.stackSize <= 0) ? new ItemStack(Items.bucket) : stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 32;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.DRINK;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemBucketMilk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */