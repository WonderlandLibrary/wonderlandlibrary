package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockDirectional implements IGrowable {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
  
  public BlockCocoa() {
    super(Material.plants);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)AGE, Integer.valueOf(0)));
    setTickRandomly(true);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!canBlockStay(worldIn, pos, state)) {
      dropBlock(worldIn, pos, state);
    } else if (worldIn.rand.nextInt(5) == 0) {
      int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
      if (i < 2)
        worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(i + 1)), 2); 
    } 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    pos = pos.offset((EnumFacing)state.getValue((IProperty)FACING));
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return (iblockstate.getBlock() == Blocks.log && iblockstate.getValue((IProperty)BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE);
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isOpaqueCube() {
    return false;
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
    EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
    int i = ((Integer)iblockstate.getValue((IProperty)AGE)).intValue();
    int j = 4 + i * 2;
    int k = 5 + i * 2;
    float f = j / 2.0F;
    switch (enumfacing) {
      case SOUTH:
        setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, (15.0F - j) / 16.0F, (8.0F + f) / 16.0F, 0.75F, 0.9375F);
        break;
      case NORTH:
        setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F, (1.0F + j) / 16.0F);
        break;
      case WEST:
        setBlockBounds(0.0625F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, (1.0F + j) / 16.0F, 0.75F, (8.0F + f) / 16.0F);
        break;
      case EAST:
        setBlockBounds((15.0F - j) / 16.0F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F, (8.0F + f) / 16.0F);
        break;
    } 
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
    worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)enumfacing), 2);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    if (!facing.getAxis().isHorizontal())
      facing = EnumFacing.NORTH; 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing.getOpposite()).withProperty((IProperty)AGE, Integer.valueOf(0));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!canBlockStay(worldIn, pos, state))
      dropBlock(worldIn, pos, state); 
  }
  
  private void dropBlock(World worldIn, BlockPos pos, IBlockState state) {
    worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    dropBlockAsItem(worldIn, pos, state, 0);
  }
  
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
    int j = 1;
    if (i >= 2)
      j = 3; 
    for (int k = 0; k < j; k++)
      spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage())); 
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.dye;
  }
  
  public int getDamageValue(World worldIn, BlockPos pos) {
    return EnumDyeColor.BROWN.getDyeDamage();
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return (((Integer)state.getValue((IProperty)AGE)).intValue() < 2);
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(((Integer)state.getValue((IProperty)AGE)).intValue() + 1)), 2);
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)AGE, Integer.valueOf((meta & 0xF) >> 2));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    i |= ((Integer)state.getValue((IProperty)AGE)).intValue() << 2;
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)AGE });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockCocoa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */