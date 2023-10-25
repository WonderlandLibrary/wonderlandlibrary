package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockDirectional {
  public static final PropertyBool OPEN = PropertyBool.create("open");
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
  
  public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
    super(Material.wood, p_i46394_1_.func_181070_c());
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
    if ((enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall)) || (enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall || worldIn.getBlockState(pos.south()).getBlock() == Blocks.cobblestone_wall)))
      state = state.withProperty((IProperty)IN_WALL, Boolean.valueOf(true)); 
    return state;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
      return null; 
    EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
    return (enumfacing$axis == EnumFacing.Axis.Z) ? new AxisAlignedBB(pos.getX(), pos.getY(), (pos.getZ() + 0.375F), (pos.getX() + 1), (pos.getY() + 1.5F), (pos.getZ() + 0.625F)) : new AxisAlignedBB((pos.getX() + 0.375F), pos.getY(), pos.getZ(), (pos.getX() + 0.625F), (pos.getY() + 1.5F), (pos.getZ() + 1));
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    EnumFacing.Axis enumfacing$axis = ((EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING)).getAxis();
    if (enumfacing$axis == EnumFacing.Axis.Z) {
      setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
    } else {
      setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
    } 
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return ((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false));
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
      state = state.withProperty((IProperty)OPEN, Boolean.valueOf(false));
      worldIn.setBlockState(pos, state, 2);
    } else {
      EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
      if (state.getValue((IProperty)FACING) == enumfacing.getOpposite())
        state = state.withProperty((IProperty)FACING, (Comparable)enumfacing); 
      state = state.withProperty((IProperty)OPEN, Boolean.valueOf(true));
      worldIn.setBlockState(pos, state, 2);
    } 
    worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
    return true;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote) {
      boolean flag = worldIn.isBlockPowered(pos);
      if (flag || neighborBlock.canProvidePower())
        if (flag && !((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && !((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
          worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(true)).withProperty((IProperty)POWERED, Boolean.valueOf(true)), 2);
          worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1003, pos, 0);
        } else if (!flag && ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() && ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
          worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)), 2);
          worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1006, pos, 0);
        } else if (flag != ((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
          worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(flag)), 2);
        }  
    } 
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) != 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
      i |= 0x4; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)POWERED, (IProperty)IN_WALL });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockFenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */