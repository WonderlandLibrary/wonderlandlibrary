package net.minecraft.item;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.World;

public class ItemExpBottle
  extends Item
{
  private static final String __OBFID = "CL_00000028";
  
  public ItemExpBottle()
  {
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public boolean hasEffect(ItemStack stack)
  {
    return true;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    if (!playerIn.capabilities.isCreativeMode) {
      itemStackIn.stackSize -= 1;
    }
    worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote) {
      worldIn.spawnEntityInWorld(new EntityExpBottle(worldIn, playerIn));
    }
    playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}
