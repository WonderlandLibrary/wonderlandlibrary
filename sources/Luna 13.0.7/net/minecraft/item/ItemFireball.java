package net.minecraft.item;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFireball
  extends Item
{
  private static final String __OBFID = "CL_00000029";
  
  public ItemFireball()
  {
    setCreativeTab(CreativeTabs.tabMisc);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if (worldIn.isRemote) {
      return true;
    }
    pos = pos.offset(side);
    if (!playerIn.func_175151_a(pos, side, stack)) {
      return false;
    }
    if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air)
    {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
      worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
    }
    if (!playerIn.capabilities.isCreativeMode) {
      stack.stackSize -= 1;
    }
    return true;
  }
}
