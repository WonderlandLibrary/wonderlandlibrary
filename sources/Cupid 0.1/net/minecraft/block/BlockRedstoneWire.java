package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
  public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
  
  public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
  
  public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
  
  public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
  
  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
  
  private boolean canProvidePower = true;
  
  private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
  
  public BlockRedstoneWire() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, EnumAttachPosition.NONE).withProperty((IProperty)EAST, EnumAttachPosition.NONE).withProperty((IProperty)SOUTH, EnumAttachPosition.NONE).withProperty((IProperty)WEST, EnumAttachPosition.NONE).withProperty((IProperty)POWER, Integer.valueOf(0)));
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    state = state.withProperty((IProperty)WEST, getAttachPosition(worldIn, pos, EnumFacing.WEST));
    state = state.withProperty((IProperty)EAST, getAttachPosition(worldIn, pos, EnumFacing.EAST));
    state = state.withProperty((IProperty)NORTH, getAttachPosition(worldIn, pos, EnumFacing.NORTH));
    state = state.withProperty((IProperty)SOUTH, getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
    return state;
  }
  
  private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
    BlockPos blockpos = pos.offset(direction);
    Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
    if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (block.isBlockNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
      Block block1 = worldIn.getBlockState(pos.up()).getBlock();
      return (!block1.isBlockNormalCube() && block.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
    } 
    return EnumAttachPosition.SIDE;
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
  
  public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    return (iblockstate.getBlock() != this) ? super.colorMultiplier(worldIn, pos, renderPass) : colorMultiplier(((Integer)iblockstate.getValue((IProperty)POWER)).intValue());
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone);
  }
  
  private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
    state = calculateCurrentChanges(worldIn, pos, pos, state);
    List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
    this.blocksNeedingUpdate.clear();
    for (BlockPos blockpos : list)
      worldIn.notifyNeighborsOfStateChange(blockpos, this); 
    return state;
  }
  
  private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
    IBlockState iblockstate = state;
    int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
    int j = 0;
    j = getMaxCurrentStrength(worldIn, pos2, j);
    this.canProvidePower = false;
    int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
    this.canProvidePower = true;
    if (k > 0 && k > j - 1)
      j = k; 
    int l = 0;
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      BlockPos blockpos = pos1.offset((EnumFacing)enumfacing);
      boolean flag = (blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ());
      if (flag)
        l = getMaxCurrentStrength(worldIn, blockpos, l); 
      if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
        if (flag && pos1.getY() >= pos2.getY())
          l = getMaxCurrentStrength(worldIn, blockpos.up(), l); 
        continue;
      } 
      if (!worldIn.getBlockState(blockpos).getBlock().isNormalCube() && flag && pos1.getY() <= pos2.getY())
        l = getMaxCurrentStrength(worldIn, blockpos.down(), l); 
    } 
    if (l > j) {
      j = l - 1;
    } else if (j > 0) {
      j--;
    } else {
      j = 0;
    } 
    if (k > j - 1)
      j = k; 
    if (i != j) {
      state = state.withProperty((IProperty)POWER, Integer.valueOf(j));
      if (worldIn.getBlockState(pos1) == iblockstate)
        worldIn.setBlockState(pos1, state, 2); 
      this.blocksNeedingUpdate.add(pos1);
      for (EnumFacing enumfacing1 : EnumFacing.values())
        this.blocksNeedingUpdate.add(pos1.offset(enumfacing1)); 
    } 
    return state;
  }
  
  private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
    if (worldIn.getBlockState(pos).getBlock() == this) {
      worldIn.notifyNeighborsOfStateChange(pos, this);
      for (EnumFacing enumfacing : EnumFacing.values())
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this); 
    } 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote) {
      updateSurroundingRedstone(worldIn, pos, state);
      for (Object enumfacing : EnumFacing.Plane.VERTICAL)
        worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)enumfacing), this); 
      for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL) {
        EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
        notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
      } 
      for (Object enumfacing20 : EnumFacing.Plane.HORIZONTAL) {
        EnumFacing enumfacing2 = (EnumFacing)enumfacing20;
        BlockPos blockpos = pos.offset(enumfacing2);
        if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
          notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
          continue;
        } 
        notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
      } 
    } 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    super.breakBlock(worldIn, pos, state);
    if (!worldIn.isRemote) {
      for (EnumFacing enumfacing : EnumFacing.values())
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this); 
      updateSurroundingRedstone(worldIn, pos, state);
      for (Object enumfacing1 : EnumFacing.Plane.HORIZONTAL)
        notifyWireNeighborsOfStateChange(worldIn, pos.offset((EnumFacing)enumfacing1)); 
      for (Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
        BlockPos blockpos = pos.offset((EnumFacing)enumfacing2);
        if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
          notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
          continue;
        } 
        notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
      } 
    } 
  }
  
  private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
    if (worldIn.getBlockState(pos).getBlock() != this)
      return strength; 
    int i = ((Integer)worldIn.getBlockState(pos).getValue((IProperty)POWER)).intValue();
    return (i > strength) ? i : strength;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote)
      if (canPlaceBlockAt(worldIn, pos)) {
        updateSurroundingRedstone(worldIn, pos, state);
      } else {
        dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      }  
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.redstone;
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return !this.canProvidePower ? 0 : getWeakPower(worldIn, pos, state, side);
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    if (!this.canProvidePower)
      return 0; 
    int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
    if (i == 0)
      return 0; 
    if (side == EnumFacing.UP)
      return i; 
    EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (func_176339_d(worldIn, pos, (EnumFacing)enumfacing))
        enumset.add((EnumFacing)enumfacing); 
    } 
    if (side.getAxis().isHorizontal() && enumset.isEmpty())
      return i; 
    if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
      return i; 
    return 0;
  }
  
  private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    BlockPos blockpos = pos.offset(side);
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    boolean flag = block.isNormalCube();
    boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
    return (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up())) ? true : (canConnectTo(iblockstate, side) ? true : ((block == Blocks.powered_repeater && iblockstate.getValue((IProperty)BlockRedstoneDiode.FACING) == side) ? true : ((!flag && canConnectUpwardsTo(worldIn, blockpos.down())))));
  }
  
  protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
    return canConnectUpwardsTo(worldIn.getBlockState(pos));
  }
  
  protected static boolean canConnectUpwardsTo(IBlockState state) {
    return canConnectTo(state, (EnumFacing)null);
  }
  
  protected static boolean canConnectTo(IBlockState blockState, EnumFacing side) {
    Block block = blockState.getBlock();
    if (block == Blocks.redstone_wire)
      return true; 
    if (Blocks.unpowered_repeater.isAssociated(block)) {
      EnumFacing enumfacing = (EnumFacing)blockState.getValue((IProperty)BlockRedstoneRepeater.FACING);
      return (enumfacing == side || enumfacing.getOpposite() == side);
    } 
    return (block.canProvidePower() && side != null);
  }
  
  public boolean canProvidePower() {
    return this.canProvidePower;
  }
  
  private int colorMultiplier(int powerLevel) {
    float f = powerLevel / 15.0F;
    float f1 = f * 0.6F + 0.4F;
    if (powerLevel == 0)
      f1 = 0.3F; 
    float f2 = f * f * 0.7F - 0.5F;
    float f3 = f * f * 0.6F - 0.7F;
    if (f2 < 0.0F)
      f2 = 0.0F; 
    if (f3 < 0.0F)
      f3 = 0.0F; 
    int i = MathHelper.clamp_int((int)(f1 * 255.0F), 0, 255);
    int j = MathHelper.clamp_int((int)(f2 * 255.0F), 0, 255);
    int k = MathHelper.clamp_int((int)(f3 * 255.0F), 0, 255);
    return 0xFF000000 | i << 16 | j << 8 | k;
  }
  
  public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
    if (i != 0) {
      double d0 = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
      double d1 = (pos.getY() + 0.0625F);
      double d2 = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
      float f = i / 15.0F;
      float f1 = f * 0.6F + 0.4F;
      float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
      float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
      worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, f1, f2, f3, new int[0]);
    } 
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.redstone;
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWER, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)POWER)).intValue();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST, (IProperty)POWER });
  }
  
  enum EnumAttachPosition implements IStringSerializable {
    UP("up"),
    SIDE("side"),
    NONE("none");
    
    private final String name;
    
    EnumAttachPosition(String name) {
      this.name = name;
    }
    
    public String toString() {
      return getName();
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockRedstoneWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */