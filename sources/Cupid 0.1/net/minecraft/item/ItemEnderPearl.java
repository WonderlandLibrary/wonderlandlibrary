package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemEnderPearl extends Item {
  public ItemEnderPearl() {
    this.maxStackSize = 16;
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (playerIn.capabilities.isCreativeMode)
      return itemStackIn; 
    itemStackIn.stackSize--;
    worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote)
      worldIn.spawnEntityInWorld((Entity)new EntityEnderPearl(worldIn, (EntityLivingBase)playerIn)); 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemEnderPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */