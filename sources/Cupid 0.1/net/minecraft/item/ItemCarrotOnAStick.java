package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {
  public ItemCarrotOnAStick() {
    setCreativeTab(CreativeTabs.tabTransport);
    setMaxStackSize(1);
    setMaxDamage(25);
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public boolean shouldRotateAroundWhenRendering() {
    return true;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (playerIn.isRiding() && playerIn.ridingEntity instanceof EntityPig) {
      EntityPig entitypig = (EntityPig)playerIn.ridingEntity;
      if (entitypig.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7) {
        entitypig.getAIControlledByPlayer().boostSpeed();
        itemStackIn.damageItem(7, (EntityLivingBase)playerIn);
        if (itemStackIn.stackSize == 0) {
          ItemStack itemstack = new ItemStack(Items.fishing_rod);
          itemstack.setTagCompound(itemStackIn.getTagCompound());
          return itemstack;
        } 
      } 
    } 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemCarrotOnAStick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */