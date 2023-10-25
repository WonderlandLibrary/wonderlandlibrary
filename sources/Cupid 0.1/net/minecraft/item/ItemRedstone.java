package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstone extends Item {
  public ItemRedstone() {
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    BlockPos blockpos = flag ? pos : pos.offset(side);
    if (!playerIn.canPlayerEdit(blockpos, side, stack))
      return false; 
    Block block = worldIn.getBlockState(blockpos).getBlock();
    if (!worldIn.canBlockBePlaced(block, blockpos, false, side, (Entity)null, stack))
      return false; 
    if (Blocks.redstone_wire.canPlaceBlockAt(worldIn, blockpos)) {
      stack.stackSize--;
      worldIn.setBlockState(blockpos, Blocks.redstone_wire.getDefaultState());
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemRedstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */