package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush {
  protected BlockLilyPad() {
    float f = 0.5F;
    float f1 = 0.015625F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    if (collidingEntity == null || !(collidingEntity instanceof net.minecraft.entity.item.EntityBoat))
      super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity); 
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
  }
  
  public int getBlockColor() {
    return 7455580;
  }
  
  public int getRenderColor(IBlockState state) {
    return 7455580;
  }
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    return 2129968;
  }
  
  protected boolean canPlaceBlockOn(Block ground) {
    return (ground == Blocks.water);
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    if (pos.getY() >= 0 && pos.getY() < 256) {
      IBlockState iblockstate = worldIn.getBlockState(pos.down());
      return (iblockstate.getBlock().getMaterial() == Material.water && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0);
    } 
    return false;
  }
  
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockLilyPad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */