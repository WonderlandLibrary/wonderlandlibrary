package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
  
  public static final PropertyBool ATTACHED = PropertyBool.create("attached");
  
  public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public BlockTripWire() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)DISARMED, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)));
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
    setTickRandomly(true);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)NORTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.NORTH))).withProperty((IProperty)EAST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.EAST))).withProperty((IProperty)SOUTH, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH))).withProperty((IProperty)WEST, Boolean.valueOf(isConnectedTo(worldIn, pos, state, EnumFacing.WEST)));
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
    return EnumWorldBlockLayer.TRANSLUCENT;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.string;
  }
  
  public Item getItem(World worldIn, BlockPos pos) {
    return Items.string;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    boolean flag = ((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue();
    boolean flag1 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
    if (flag != flag1) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    boolean flag = ((Boolean)iblockstate.getValue((IProperty)ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)iblockstate.getValue((IProperty)SUSPENDED)).booleanValue();
    if (!flag1) {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
    } else if (!flag) {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    } else {
      setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
    } 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    state = state.withProperty((IProperty)SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())));
    worldIn.setBlockState(pos, state, 3);
    notifyHook(worldIn, pos, state);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    notifyHook(worldIn, pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)));
  }
  
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!worldIn.isRemote)
      if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
        worldIn.setBlockState(pos, state.withProperty((IProperty)DISARMED, Boolean.valueOf(true)), 4);  
  }
  
  private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
    for (EnumFacing enumfacing : new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }) {
      for (int i = 1; i < 42; i++) {
        BlockPos blockpos = pos.offset(enumfacing, i);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock() == Blocks.tripwire_hook) {
          if (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing.getOpposite())
            Blocks.tripwire_hook.func_176260_a(worldIn, blockpos, iblockstate, false, true, i, state); 
          break;
        } 
        if (iblockstate.getBlock() != Blocks.tripwire)
          break; 
      } 
    } 
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote)
      if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
        updateState(worldIn, pos);  
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)POWERED)).booleanValue())
        updateState(worldIn, pos);  
  }
  
  private void updateState(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    boolean flag = ((Boolean)iblockstate.getValue((IProperty)POWERED)).booleanValue();
    boolean flag1 = false;
    List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
    if (!list.isEmpty())
      for (Entity entity : list) {
        if (!entity.doesEntityNotTriggerPressurePlate()) {
          flag1 = true;
          break;
        } 
      }  
    if (flag1 != flag) {
      iblockstate = iblockstate.withProperty((IProperty)POWERED, Boolean.valueOf(flag1));
      worldIn.setBlockState(pos, iblockstate, 3);
      notifyHook(worldIn, pos, iblockstate);
    } 
    if (flag1)
      worldIn.scheduleUpdate(pos, this, tickRate(worldIn)); 
  }
  
  public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
    BlockPos blockpos = pos.offset(direction);
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (block == Blocks.tripwire_hook) {
      EnumFacing enumfacing = direction.getOpposite();
      return (iblockstate.getValue((IProperty)BlockTripWireHook.FACING) == enumfacing);
    } 
    if (block == Blocks.tripwire) {
      boolean flag = ((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue();
      boolean flag1 = ((Boolean)iblockstate.getValue((IProperty)SUSPENDED)).booleanValue();
      return (flag == flag1);
    } 
    return false;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x1) > 0))).withProperty((IProperty)SUSPENDED, Boolean.valueOf(((meta & 0x2) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0))).withProperty((IProperty)DISARMED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x1; 
    if (((Boolean)state.getValue((IProperty)SUSPENDED)).booleanValue())
      i |= 0x2; 
    if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
      i |= 0x4; 
    if (((Boolean)state.getValue((IProperty)DISARMED)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)POWERED, (IProperty)SUSPENDED, (IProperty)ATTACHED, (IProperty)DISARMED, (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockTripWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */