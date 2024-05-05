package net.minecraft.item;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.world.World;

public class ItemFishingRod
  extends Item
{
  private static final String __OBFID = "CL_00000034";
  
  public ItemFishingRod()
  {
    setMaxDamage(64);
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean isFull3D()
  {
    return true;
  }
  
  public boolean shouldRotateAroundWhenRendering()
  {
    return true;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    if (playerIn.fishEntity != null)
    {
      int var4 = playerIn.fishEntity.handleHookRetraction();
      itemStackIn.damageItem(var4, playerIn);
      playerIn.swingItem();
    }
    else
    {
      worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!worldIn.isRemote) {
        worldIn.spawnEntityInWorld(new EntityFishHook(worldIn, playerIn));
      }
      playerIn.swingItem();
      playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
    }
    return itemStackIn;
  }
  
  public boolean isItemTool(ItemStack stack)
  {
    return super.isItemTool(stack);
  }
  
  public int getItemEnchantability()
  {
    return 1;
  }
}
