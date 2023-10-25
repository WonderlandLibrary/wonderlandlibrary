package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFishingRod extends Item {
  public ItemFishingRod() {
    setMaxDamage(64);
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public boolean shouldRotateAroundWhenRendering() {
    return true;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (playerIn.fishEntity != null) {
      int i = playerIn.fishEntity.handleHookRetraction();
      itemStackIn.damageItem(i, (EntityLivingBase)playerIn);
      playerIn.swingItem();
    } else {
      worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!worldIn.isRemote)
        worldIn.spawnEntityInWorld((Entity)new EntityFishHook(worldIn, playerIn)); 
      playerIn.swingItem();
      playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    } 
    return itemStackIn;
  }
  
  public boolean isItemTool(ItemStack stack) {
    return super.isItemTool(stack);
  }
  
  public int getItemEnchantability() {
    return 1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemFishingRod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */