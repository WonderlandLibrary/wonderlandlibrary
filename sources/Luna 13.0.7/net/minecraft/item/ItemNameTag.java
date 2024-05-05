package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ItemNameTag
  extends Item
{
  private static final String __OBFID = "CL_00000052";
  
  public ItemNameTag()
  {
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
  {
    if (!stack.hasDisplayName()) {
      return false;
    }
    if ((target instanceof EntityLiving))
    {
      EntityLiving var4 = (EntityLiving)target;
      var4.setCustomNameTag(stack.getDisplayName());
      var4.enablePersistence();
      stack.stackSize -= 1;
      return true;
    }
    return super.itemInteractionForEntity(stack, playerIn, target);
  }
}
