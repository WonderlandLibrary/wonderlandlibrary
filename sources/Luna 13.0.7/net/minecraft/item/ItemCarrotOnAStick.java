package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemCarrotOnAStick
  extends Item
{
  private static final String __OBFID = "CL_00000001";
  
  public ItemCarrotOnAStick()
  {
    setCreativeTab(CreativeTabs.tabTransport);
    setMaxStackSize(1);
    setMaxDamage(25);
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
    if ((playerIn.isRiding()) && ((playerIn.ridingEntity instanceof EntityPig)))
    {
      EntityPig var4 = (EntityPig)playerIn.ridingEntity;
      if ((var4.getAIControlledByPlayer().isControlledByPlayer()) && (itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7))
      {
        var4.getAIControlledByPlayer().boostSpeed();
        itemStackIn.damageItem(7, playerIn);
        if (itemStackIn.stackSize == 0)
        {
          ItemStack var5 = new ItemStack(Items.fishing_rod);
          var5.setTagCompound(itemStackIn.getTagCompound());
          return var5;
        }
      }
    }
    playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}
