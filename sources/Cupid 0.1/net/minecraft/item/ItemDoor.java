package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDoor extends Item {
  private Block block;
  
  public ItemDoor(Block block) {
    this.block = block;
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side != EnumFacing.UP)
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (!block.isReplaceable(worldIn, pos))
      pos = pos.offset(side); 
    if (!playerIn.canPlayerEdit(pos, side, stack))
      return false; 
    if (!this.block.canPlaceBlockAt(worldIn, pos))
      return false; 
    placeDoor(worldIn, pos, EnumFacing.fromAngle(playerIn.rotationYaw), this.block);
    stack.stackSize--;
    return true;
  }
  
  public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door) {
    BlockPos blockpos = pos.offset(facing.rotateY());
    BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
    int i = (worldIn.getBlockState(blockpos1).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).getBlock().isNormalCube() ? 1 : 0);
    int j = (worldIn.getBlockState(blockpos).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube() ? 1 : 0);
    boolean flag = (worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.up()).getBlock() == door);
    boolean flag1 = (worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door);
    boolean flag2 = false;
    if ((flag && !flag1) || j > i)
      flag2 = true; 
    BlockPos blockpos2 = pos.up();
    IBlockState iblockstate = door.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)facing).withProperty((IProperty)BlockDoor.HINGE, flag2 ? (Comparable)BlockDoor.EnumHingePosition.RIGHT : (Comparable)BlockDoor.EnumHingePosition.LEFT);
    worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.LOWER), 2);
    worldIn.setBlockState(blockpos2, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.UPPER), 2);
    worldIn.notifyNeighborsOfStateChange(pos, door);
    worldIn.notifyNeighborsOfStateChange(blockpos2, door);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */