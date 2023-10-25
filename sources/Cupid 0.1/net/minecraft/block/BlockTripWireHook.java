package net.minecraft.block;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool ATTACHED = PropertyBool.create("attached");
  
  public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
  
  public BlockTripWireHook() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabRedstone);
    setTickRandomly(true);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.withProperty((IProperty)SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
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
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return (side.getAxis().isHorizontal() && worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube());
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock().isNormalCube())
        return true; 
    } 
    return false;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)).withProperty((IProperty)SUSPENDED, Boolean.valueOf(false));
    if (facing.getAxis().isHorizontal())
      iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing); 
    return iblockstate;
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    func_176260_a(worldIn, pos, state, false, false, -1, (IBlockState)null);
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (neighborBlock != this)
      if (checkForDrop(worldIn, pos, state)) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().isNormalCube()) {
          dropBlockAsItem(worldIn, pos, state, 0);
          worldIn.setBlockToAir(pos);
        } 
      }  
  }
  
  public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_) {
    int k, m;
    EnumFacing enumfacing = (EnumFacing)hookState.getValue((IProperty)FACING);
    int flag = ((Boolean)hookState.getValue((IProperty)ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)hookState.getValue((IProperty)POWERED)).booleanValue();
    boolean flag2 = !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
    boolean flag3 = !p_176260_4_;
    boolean flag4 = false;
    int i = 0;
    IBlockState[] aiblockstate = new IBlockState[42];
    for (int j = 1; j < 42; j++) {
      BlockPos blockpos = pos.offset(enumfacing, j);
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      if (iblockstate.getBlock() == Blocks.tripwire_hook) {
        if (iblockstate.getValue((IProperty)FACING) == enumfacing.getOpposite())
          i = j; 
        break;
      } 
      if (iblockstate.getBlock() != Blocks.tripwire && j != p_176260_6_) {
        aiblockstate[j] = null;
        flag3 = false;
      } else {
        if (j == p_176260_6_)
          iblockstate = (IBlockState)Objects.firstNonNull(p_176260_7_, iblockstate); 
        int flag5 = !((Boolean)iblockstate.getValue((IProperty)BlockTripWire.DISARMED)).booleanValue() ? 1 : 0;
        boolean flag6 = ((Boolean)iblockstate.getValue((IProperty)BlockTripWire.POWERED)).booleanValue();
        boolean flag7 = ((Boolean)iblockstate.getValue((IProperty)BlockTripWire.SUSPENDED)).booleanValue();
        int n = flag3 & ((flag7 == flag2) ? 1 : 0);
        m = flag4 | ((flag5 && flag6) ? 1 : 0);
        aiblockstate[j] = iblockstate;
        if (j == p_176260_6_) {
          worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
          k = n & flag5;
        } 
      } 
    } 
    k &= (i > 1) ? 1 : 0;
    m &= k;
    IBlockState iblockstate1 = getDefaultState().withProperty((IProperty)ATTACHED, Boolean.valueOf(k)).withProperty((IProperty)POWERED, Boolean.valueOf(m));
    if (i > 0) {
      BlockPos blockpos1 = pos.offset(enumfacing, i);
      EnumFacing enumfacing1 = enumfacing.getOpposite();
      worldIn.setBlockState(blockpos1, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing1), 3);
      func_176262_b(worldIn, blockpos1, enumfacing1);
      func_180694_a(worldIn, blockpos1, k, m, flag, flag1);
    } 
    func_180694_a(worldIn, pos, k, m, flag, flag1);
    if (!p_176260_4_) {
      worldIn.setBlockState(pos, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing), 3);
      if (p_176260_5_)
        func_176262_b(worldIn, pos, enumfacing); 
    } 
    if (flag != k)
      for (int n = 1; n < i; n++) {
        BlockPos blockpos2 = pos.offset(enumfacing, n);
        IBlockState iblockstate2 = aiblockstate[n];
        if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getBlock() != Blocks.air)
          worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty)ATTACHED, Boolean.valueOf(k)), 3); 
      }  
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    func_176260_a(worldIn, pos, state, false, true, -1, (IBlockState)null);
  }
  
  private void func_180694_a(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
    if (p_180694_4_ && !p_180694_6_) {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
    } else if (!p_180694_4_ && p_180694_6_) {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
    } else if (p_180694_3_ && !p_180694_5_) {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
    } else if (!p_180694_3_ && p_180694_5_) {
      worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
    } 
  }
  
  private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_) {
    worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
    worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
  }
  
  private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (!canPlaceBlockAt(worldIn, pos)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      return false;
    } 
    return true;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    float f = 0.1875F;
    switch ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)) {
      case EAST:
        setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        break;
      case WEST:
        setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        break;
      case SOUTH:
        setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        break;
      case NORTH:
        setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        break;
    } 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    boolean flag = ((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
    if (flag || flag1)
      func_176260_a(worldIn, pos, state, true, false, -1, (IBlockState)null); 
    if (flag1) {
      worldIn.notifyNeighborsOfStateChange(pos, this);
      worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite()), this);
    } 
    super.breakBlock(worldIn, pos, state);
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((state.getValue((IProperty)FACING) == side) ? 15 : 0);
  }
  
  public boolean canProvidePower() {
    return true;
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT_MIPPED;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
      i |= 0x4; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED, (IProperty)ATTACHED, (IProperty)SUSPENDED });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockTripWireHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */