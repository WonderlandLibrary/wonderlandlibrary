package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ItemNameTag extends Item {
  public ItemNameTag() {
    setCreativeTab(CreativeTabs.tabTools);
  }
  
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
    if (!stack.hasDisplayName())
      return false; 
    if (target instanceof EntityLiving) {
      EntityLiving entityliving = (EntityLiving)target;
      entityliving.setCustomNameTag(stack.getDisplayName());
      entityliving.enablePersistence();
      stack.stackSize--;
      return true;
    } 
    return super.itemInteractionForEntity(stack, playerIn, target);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemNameTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */