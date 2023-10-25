package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item {
  public ItemGlassBottle() {
    setCreativeTab(CreativeTabs.tabBrewing);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
    if (movingobjectposition == null)
      return itemStackIn; 
    if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      BlockPos blockpos = movingobjectposition.getBlockPos();
      if (!worldIn.isBlockModifiable(playerIn, blockpos))
        return itemStackIn; 
      if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
        return itemStackIn; 
      if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.water) {
        itemStackIn.stackSize--;
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        if (itemStackIn.stackSize <= 0)
          return new ItemStack(Items.potionitem); 
        if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.potionitem)))
          playerIn.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false); 
      } 
    } 
    return itemStackIn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemGlassBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */