package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;

public class ItemSaddle extends Item {
  public ItemSaddle() {
    this.maxStackSize = 1;
    setCreativeTab(CreativeTabs.tabTransport);
  }
  
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
    if (target instanceof EntityPig) {
      EntityPig entitypig = (EntityPig)target;
      if (!entitypig.getSaddled() && !entitypig.isChild()) {
        entitypig.setSaddled(true);
        entitypig.worldObj.playSoundAtEntity((Entity)entitypig, "mob.horse.leather", 0.5F, 1.0F);
        stack.stackSize--;
      } 
      return true;
    } 
    return false;
  }
  
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    itemInteractionForEntity(stack, (EntityPlayer)null, target);
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSaddle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */