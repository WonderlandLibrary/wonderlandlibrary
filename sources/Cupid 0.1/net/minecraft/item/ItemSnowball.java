package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemSnowball extends Item {
  public ItemSnowball() {
    this.maxStackSize = 16;
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (!playerIn.capabilities.isCreativeMode)
      itemStackIn.stackSize--; 
    worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote)
      worldIn.spawnEntityInWorld((Entity)new EntitySnowball(worldIn, (EntityLivingBase)playerIn)); 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSnowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */