package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  protected BlockLadder() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
    setCreativeTab(CreativeTabs.tabDecorations);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }
  
  public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.getSelectedBoundingBox(worldIn, pos);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() == this) {
      float f = 0.125F;
      switch ((EnumFacing)iblockstate.getValue((IProperty)FACING)) {
        case NORTH:
          setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
          return;
        case SOUTH:
          setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
          return;
        case WEST:
          setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
          return;
      } 
      setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
    } 
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.west()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.east()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.north()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.south()).getBlock().isNormalCube()));
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    if (facing.getAxis().isHorizontal() && canBlockStay(worldIn, pos, facing))
      return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing); 
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (canBlockStay(worldIn, pos, (EnumFacing)enumfacing))
        return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing); 
    } 
    return getDefaultState();
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    if (!canBlockStay(worldIn, pos, enumfacing)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
    super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
  }
  
  protected boolean canBlockStay(World worldIn, BlockPos pos, EnumFacing facing) {
    return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube();
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing = EnumFacing.getFront(meta);
    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
      enumfacing = EnumFacing.NORTH; 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockLadder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */