package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {
  public static final PropertyBool LOCKED = PropertyBool.create("locked");
  
  public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);
  
  protected BlockRedstoneRepeater(boolean powered) {
    super(powered);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)DELAY, Integer.valueOf(1)).withProperty((IProperty)LOCKED, Boolean.valueOf(false)));
  }
  
  public String getLocalizedName() {
    return StatCollector.translateToLocal("item.diode.name");
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)LOCKED, Boolean.valueOf(isLocked(worldIn, pos, state)));
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (!playerIn.capabilities.allowEdit)
      return false; 
    worldIn.setBlockState(pos, state.cycleProperty((IProperty)DELAY), 3);
    return true;
  }
  
  protected int getDelay(IBlockState state) {
    return ((Integer)state.getValue((IProperty)DELAY)).intValue() * 2;
  }
  
  protected IBlockState getPoweredState(IBlockState unpoweredState) {
    Integer integer = (Integer)unpoweredState.getValue((IProperty)DELAY);
    Boolean obool = (Boolean)unpoweredState.getValue((IProperty)LOCKED);
    EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue((IProperty)FACING);
    return Blocks.powered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, integer).withProperty((IProperty)LOCKED, obool);
  }
  
  protected IBlockState getUnpoweredState(IBlockState poweredState) {
    Integer integer = (Integer)poweredState.getValue((IProperty)DELAY);
    Boolean obool = (Boolean)poweredState.getValue((IProperty)LOCKED);
    EnumFacing enumfacing = (EnumFacing)poweredState.getValue((IProperty)FACING);
    return Blocks.unpowered_repeater.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)DELAY, integer).withProperty((IProperty)LOCKED, obool);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.repeater;
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.repeater;
  }
  
  public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
    return (getPowerOnSides(worldIn, pos, state) > 0);
  }
  
  protected boolean canPowerSide(Block blockIn) {
    return isRedstoneRepeaterBlockID(blockIn);
  }
  
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (this.isRepeaterPowered) {
      EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
      double d0 = (pos.getX() + 0.5F) + (rand.nextFloat() - 0.5F) * 0.2D;
      double d1 = (pos.getY() + 0.4F) + (rand.nextFloat() - 0.5F) * 0.2D;
      double d2 = (pos.getZ() + 0.5F) + (rand.nextFloat() - 0.5F) * 0.2D;
      float f = -5.0F;
      if (rand.nextBoolean())
        f = (((Integer)state.getValue((IProperty)DELAY)).intValue() * 2 - 1); 
      f /= 16.0F;
      double d3 = (f * enumfacing.getFrontOffsetX());
      double d4 = (f * enumfacing.getFrontOffsetZ());
      worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    super.breakBlock(worldIn, pos, state);
    notifyNeighbors(worldIn, pos, state);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)LOCKED, Boolean.valueOf(false)).withProperty((IProperty)DELAY, Integer.valueOf(1 + (meta >> 2)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    i |= ((Integer)state.getValue((IProperty)DELAY)).intValue() - 1 << 2;
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)DELAY, (IProperty)LOCKED });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRedstoneRepeater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */