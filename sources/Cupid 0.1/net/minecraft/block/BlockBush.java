package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBush extends Block {
  protected BlockBush() {
    this(Material.plants);
  }
  
  protected BlockBush(Material materialIn) {
    this(materialIn, materialIn.getMaterialMapColor());
  }
  
  protected BlockBush(Material p_i46452_1_, MapColor p_i46452_2_) {
    super(p_i46452_1_, p_i46452_2_);
    setTickRandomly(true);
    float f = 0.2F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock()));
  }
  
  protected boolean canPlaceBlockOn(Block ground) {
    return (ground == Blocks.grass || ground == Blocks.dirt || ground == Blocks.farmland);
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    checkAndDropBlock(worldIn, pos, state);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    checkAndDropBlock(worldIn, pos, state);
  }
  
  protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!canBlockStay(worldIn, pos, state)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    } 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    return null;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockBush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */