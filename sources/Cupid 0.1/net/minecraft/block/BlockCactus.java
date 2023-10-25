package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockCactus extends Block {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
  
  protected BlockCactus() {
    super(Material.cactus);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    BlockPos blockpos = pos.up();
    if (worldIn.isAirBlock(blockpos)) {
      int i;
      for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; i++);
      if (i < 3) {
        int j = ((Integer)state.getValue((IProperty)AGE)).intValue();
        if (j == 15) {
          worldIn.setBlockState(blockpos, getDefaultState());
          IBlockState iblockstate = state.withProperty((IProperty)AGE, Integer.valueOf(0));
          worldIn.setBlockState(pos, iblockstate, 4);
          onNeighborBlockChange(worldIn, blockpos, iblockstate, this);
        } else {
          worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(j + 1)), 4);
        } 
      } 
    } 
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    float f = 0.0625F;
    return new AxisAlignedBB((pos.getX() + f), pos.getY(), (pos.getZ() + f), ((pos.getX() + 1) - f), ((pos.getY() + 1) - f), ((pos.getZ() + 1) - f));
  }
  
  public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    float f = 0.0625F;
    return new AxisAlignedBB((pos.getX() + f), pos.getY(), (pos.getZ() + f), ((pos.getX() + 1) - f), (pos.getY() + 1), ((pos.getZ() + 1) - f));
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return super.canPlaceBlockAt(worldIn, pos) ? canBlockStay(worldIn, pos) : false;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!canBlockStay(worldIn, pos))
      worldIn.destroyBlock(pos, true); 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos) {
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock().getMaterial().isSolid())
        return false; 
    } 
    Block block = worldIn.getBlockState(pos.down()).getBlock();
    return (block == Blocks.cactus || block == Blocks.sand);
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    entityIn.attackEntityFrom(DamageSource.cactus, 1.0F);
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)AGE)).intValue();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)AGE });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockCactus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */