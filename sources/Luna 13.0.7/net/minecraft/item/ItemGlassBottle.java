package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemGlassBottle
  extends Item
{
  private static final String __OBFID = "CL_00001776";
  
  public ItemGlassBottle()
  {
    setCreativeTab(CreativeTabs.tabBrewing);
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
  {
    MovingObjectPosition var4 = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
    if (var4 == null) {
      return itemStackIn;
    }
    if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
    {
      BlockPos var5 = var4.func_178782_a();
      if (!worldIn.isBlockModifiable(playerIn, var5)) {
        return itemStackIn;
      }
      if (!playerIn.func_175151_a(var5.offset(var4.field_178784_b), var4.field_178784_b, itemStackIn)) {
        return itemStackIn;
      }
      if (worldIn.getBlockState(var5).getBlock().getMaterial() == Material.water)
      {
        itemStackIn.stackSize -= 1;
        playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
        if (itemStackIn.stackSize <= 0) {
          return new ItemStack(Items.potionitem);
        }
        if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.potionitem))) {
          playerIn.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
        }
      }
    }
    return itemStackIn;
  }
}
