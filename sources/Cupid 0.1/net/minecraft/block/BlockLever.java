package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
  public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  protected BlockLever() {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, EnumOrientation.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabRedstone);
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
    return func_181090_a(worldIn, pos, side.getOpposite());
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (EnumFacing enumfacing : EnumFacing.values()) {
      if (func_181090_a(worldIn, pos, enumfacing))
        return true; 
    } 
    return false;
  }
  
  protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
    return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false));
    if (func_181090_a(worldIn, pos, facing.getOpposite()))
      return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing())); 
    for (Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (enumfacing != facing && func_181090_a(worldIn, pos, ((EnumFacing)enumfacing).getOpposite()))
        return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings((EnumFacing)enumfacing, placer.getHorizontalFacing())); 
    } 
    if (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()))
      return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing())); 
    return iblockstate;
  }
  
  public static int getMetadataForFacing(EnumFacing facing) {
    switch (facing) {
      case X:
        return 0;
      case Z:
        return 5;
      case null:
        return 4;
      case null:
        return 3;
      case null:
        return 2;
      case null:
        return 1;
    } 
    return -1;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    if (func_181091_e(worldIn, pos, state) && !func_181090_a(worldIn, pos, ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing().getOpposite())) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
    if (canPlaceBlockAt(p_181091_1_, p_181091_2_))
      return true; 
    dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
    p_181091_1_.setBlockToAir(p_181091_2_);
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    float f = 0.1875F;
    switch ((EnumOrientation)worldIn.getBlockState(pos).getValue((IProperty)FACING)) {
      case X:
        setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        break;
      case Z:
        setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        break;
      case null:
        setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        break;
      case null:
        setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        break;
      case null:
      case null:
        f = 0.25F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        break;
      case null:
      case null:
        f = 0.25F;
        setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        break;
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (worldIn.isRemote)
      return true; 
    state = state.cycleProperty((IProperty)POWERED);
    worldIn.setBlockState(pos, state, 3);
    worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0.6F : 0.5F);
    worldIn.notifyNeighborsOfStateChange(pos, this);
    EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
    worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
      worldIn.notifyNeighborsOfStateChange(pos, this);
      EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
      worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
    } 
    super.breakBlock(worldIn, pos, state);
  }
  
  public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
    return !((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0 : ((((EnumOrientation)state.getValue((IProperty)FACING)).getFacing() == side) ? 15 : 0);
  }
  
  public boolean canProvidePower() {
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumOrientation)state.getValue((IProperty)FACING)).getMetadata();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED });
  }
  
  public enum EnumOrientation implements IStringSerializable {
    DOWN_X(0, "down_x", EnumFacing.DOWN),
    EAST(1, "east", EnumFacing.EAST),
    WEST(2, "west", EnumFacing.WEST),
    SOUTH(3, "south", EnumFacing.SOUTH),
    NORTH(4, "north", EnumFacing.NORTH),
    UP_Z(5, "up_z", EnumFacing.UP),
    UP_X(6, "up_x", EnumFacing.UP),
    DOWN_Z(7, "down_z", EnumFacing.DOWN);
    
    private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final EnumFacing facing;
    
    static {
      for (EnumOrientation blocklever$enumorientation : values())
        META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation; 
    }
    
    EnumOrientation(int meta, String name, EnumFacing facing) {
      this.meta = meta;
      this.name = name;
      this.facing = facing;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public EnumFacing getFacing() {
      return this.facing;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumOrientation byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
      switch (clickedSide) {
        case X:
          switch (entityFacing.getAxis()) {
            case X:
              return DOWN_X;
            case Z:
              return DOWN_Z;
          } 
          throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
        case Z:
          switch (entityFacing.getAxis()) {
            case X:
              return UP_X;
            case Z:
              return UP_Z;
          } 
          throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
        case null:
          return NORTH;
        case null:
          return SOUTH;
        case null:
          return WEST;
        case null:
          return EAST;
      } 
      throw new IllegalArgumentException("Invalid facing: " + clickedSide);
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockLever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */