package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
  
  public static final PropertyBool OPEN = PropertyBool.create("open");
  
  public static final PropertyEnum<DoorHalf> HALF = PropertyEnum.create("half", DoorHalf.class);
  
  protected BlockTrapDoor(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)HALF, DoorHalf.BOTTOM));
    float f = 0.5F;
    float f1 = 1.0F;
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return !((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
  }
  
  public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.getSelectedBoundingBox(worldIn, pos);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.getCollisionBoundingBox(worldIn, pos, state);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    setBounds(worldIn.getBlockState(pos));
  }
  
  public void setBlockBoundsForItemRender() {
    float f = 0.1875F;
    setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
  }
  
  public void setBounds(IBlockState state) {
    if (state.getBlock() == this) {
      boolean flag = (state.getValue((IProperty)HALF) == DoorHalf.TOP);
      Boolean obool = (Boolean)state.getValue((IProperty)OPEN);
      EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
      float f = 0.1875F;
      if (flag) {
        setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
      } 
      if (obool.booleanValue()) {
        if (enumfacing == EnumFacing.NORTH)
          setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F); 
        if (enumfacing == EnumFacing.SOUTH)
          setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F); 
        if (enumfacing == EnumFacing.WEST)
          setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); 
        if (enumfacing == EnumFacing.EAST)
          setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F); 
      } 
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (this.blockMaterial == Material.iron)
      return true; 
    state = state.cycleProperty((IProperty)OPEN);
    worldIn.setBlockState(pos, state, 2);
    worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
    return true;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (!worldIn.isRemote) {
      BlockPos blockpos = pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite());
      if (!isValidSupportBlock(worldIn.getBlockState(blockpos).getBlock())) {
        worldIn.setBlockToAir(pos);
        dropBlockAsItem(worldIn, pos, state, 0);
      } else {
        boolean flag = worldIn.isBlockPowered(pos);
        if (flag || neighborBlock.canProvidePower()) {
          boolean flag1 = ((Boolean)state.getValue((IProperty)OPEN)).booleanValue();
          if (flag1 != flag) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(flag)), 2);
            worldIn.playAuxSFXAtEntity((EntityPlayer)null, flag ? 1003 : 1006, pos, 0);
          } 
        } 
      } 
    } 
  }
  
  public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
    setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
    return super.collisionRayTrace(worldIn, pos, start, end);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState();
    if (facing.getAxis().isHorizontal()) {
      iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)OPEN, Boolean.valueOf(false));
      iblockstate = iblockstate.withProperty((IProperty)HALF, (hitY > 0.5F) ? DoorHalf.TOP : DoorHalf.BOTTOM);
    } 
    return iblockstate;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return (!side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock()));
  }
  
  protected static EnumFacing getFacing(int meta) {
    switch (meta & 0x3) {
      case 0:
        return EnumFacing.NORTH;
      case 1:
        return EnumFacing.SOUTH;
      case 2:
        return EnumFacing.WEST;
    } 
    return EnumFacing.EAST;
  }
  
  protected static int getMetaForFacing(EnumFacing facing) {
    switch (facing) {
      case NORTH:
        return 0;
      case SOUTH:
        return 1;
      case WEST:
        return 2;
    } 
    return 3;
  }
  
  private static boolean isValidSupportBlock(Block blockIn) {
    return ((blockIn.blockMaterial.isOpaque() && blockIn.isFullCube()) || blockIn == Blocks.glowstone || blockIn instanceof BlockSlab || blockIn instanceof BlockStairs);
  }
  
  public EnumWorldBlockLayer getBlockLayer() {
    return EnumWorldBlockLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= getMetaForFacing((EnumFacing)state.getValue((IProperty)FACING));
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
      i |= 0x4; 
    if (state.getValue((IProperty)HALF) == DoorHalf.TOP)
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)HALF });
  }
  
  public enum DoorHalf implements IStringSerializable {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String name;
    
    DoorHalf(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockTrapDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */