package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBed extends Item {
  public ItemBed() {
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (worldIn.isRemote)
      return true; 
    if (side != EnumFacing.UP)
      return false; 
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    boolean flag = block.isReplaceable(worldIn, pos);
    if (!flag)
      pos = pos.up(); 
    int i = MathHelper.floor_double((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
    EnumFacing enumfacing = EnumFacing.getHorizontal(i);
    BlockPos blockpos = pos.offset(enumfacing);
    if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos, side, stack)) {
      boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
      boolean flag2 = (flag || worldIn.isAirBlock(pos));
      boolean flag3 = (flag1 || worldIn.isAirBlock(blockpos));
      if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos.down())) {
        IBlockState iblockstate1 = Blocks.bed.getDefaultState().withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty((IProperty)BlockBed.FACING, (Comparable)enumfacing).withProperty((IProperty)BlockBed.PART, (Comparable)BlockBed.EnumPartType.FOOT);
        if (worldIn.setBlockState(pos, iblockstate1, 3)) {
          IBlockState iblockstate2 = iblockstate1.withProperty((IProperty)BlockBed.PART, (Comparable)BlockBed.EnumPartType.HEAD);
          worldIn.setBlockState(blockpos, iblockstate2, 3);
        } 
        stack.stackSize--;
        return true;
      } 
      return false;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */