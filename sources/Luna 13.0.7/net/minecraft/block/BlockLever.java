package net.minecraft.block;

import java.util.Iterator;
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
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever
  extends Block
{
  public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumOrientation.class);
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  private static final String __OBFID = "CL_00000264";
  
  protected BlockLever()
  {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.tabRedstone);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
  {
    return null;
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean isFullCube()
  {
    return false;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
  {
    return (side == EnumFacing.UP) && (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) ? true : func_176358_d(worldIn, pos.offset(side.getOpposite()));
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    return World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) ? true : func_176358_d(worldIn, pos.offsetSouth()) ? true : func_176358_d(worldIn, pos.offsetNorth()) ? true : func_176358_d(worldIn, pos.offsetEast()) ? true : func_176358_d(worldIn, pos.offsetWest()) ? true : func_176358_d(worldIn, pos.offsetUp());
  }
  
  protected boolean func_176358_d(World worldIn, BlockPos p_176358_2_)
  {
    return worldIn.getBlockState(p_176358_2_).getBlock().isNormalCube();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    IBlockState var9 = getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
    if (func_176358_d(worldIn, pos.offset(facing.getOpposite()))) {
      return var9.withProperty(FACING, EnumOrientation.func_176856_a(facing, placer.func_174811_aO()));
    }
    Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();
    EnumFacing var11;
    do
    {
      if (!var10.hasNext())
      {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
          return var9.withProperty(FACING, EnumOrientation.func_176856_a(EnumFacing.UP, placer.func_174811_aO()));
        }
        return var9;
      }
      var11 = (EnumFacing)var10.next();
    } while ((var11 == facing) || (!func_176358_d(worldIn, pos.offset(var11.getOpposite()))));
    return var9.withProperty(FACING, EnumOrientation.func_176856_a(var11, placer.func_174811_aO()));
  }
  
  public static int func_176357_a(EnumFacing p_176357_0_)
  {
    switch (SwitchEnumFacing.FACING_LOOKUP[p_176357_0_.ordinal()])
    {
    case 1: 
      return 0;
    case 2: 
      return 5;
    case 3: 
      return 4;
    case 4: 
      return 3;
    case 5: 
      return 2;
    case 6: 
      return 1;
    }
    return -1;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    if ((func_176356_e(worldIn, pos)) && (!func_176358_d(worldIn, pos.offset(((EnumOrientation)state.getValue(FACING)).func_176852_c().getOpposite()))))
    {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    }
  }
  
  private boolean func_176356_e(World worldIn, BlockPos p_176356_2_)
  {
    if (canPlaceBlockAt(worldIn, p_176356_2_)) {
      return true;
    }
    dropBlockAsItem(worldIn, p_176356_2_, worldIn.getBlockState(p_176356_2_), 0);
    worldIn.setBlockToAir(p_176356_2_);
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    float var3 = 0.1875F;
    switch (SwitchEnumFacing.ORIENTATION_LOOKUP[((EnumOrientation)access.getBlockState(pos).getValue(FACING)).ordinal()])
    {
    case 1: 
      setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
      break;
    case 2: 
      setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
      break;
    case 3: 
      setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
      break;
    case 4: 
      setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
      break;
    case 5: 
    case 6: 
      var3 = 0.25F;
      setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
      break;
    case 7: 
    case 8: 
      var3 = 0.25F;
      setBlockBounds(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
    }
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if (worldIn.isRemote) {
      return true;
    }
    state = state.cycleProperty(POWERED);
    worldIn.setBlockState(pos, state, 3);
    worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F);
    worldIn.notifyNeighborsOfStateChange(pos, this);
    EnumFacing var9 = ((EnumOrientation)state.getValue(FACING)).func_176852_c();
    worldIn.notifyNeighborsOfStateChange(pos.offset(var9.getOpposite()), this);
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    if (((Boolean)state.getValue(POWERED)).booleanValue())
    {
      worldIn.notifyNeighborsOfStateChange(pos, this);
      EnumFacing var4 = ((EnumOrientation)state.getValue(FACING)).func_176852_c();
      worldIn.notifyNeighborsOfStateChange(pos.offset(var4.getOpposite()), this);
    }
    super.breakBlock(worldIn, pos, state);
  }
  
  public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return ((EnumOrientation)state.getValue(FACING)).func_176852_c() == side ? 15 : !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : 0;
  }
  
  public boolean canProvidePower()
  {
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(FACING, EnumOrientation.func_176853_a(meta & 0x7)).withProperty(POWERED, Boolean.valueOf((meta & 0x8) > 0));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    byte var2 = 0;
    int var3 = var2 | ((EnumOrientation)state.getValue(FACING)).func_176855_a();
    if (((Boolean)state.getValue(POWERED)).booleanValue()) {
      var3 |= 0x8;
    }
    return var3;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { FACING, POWERED });
  }
  
  public static enum EnumOrientation
    implements IStringSerializable
  {
    private static final EnumOrientation[] field_176869_i;
    private final int field_176866_j;
    private final String field_176867_k;
    private final EnumFacing field_176864_l;
    private static final EnumOrientation[] $VALUES;
    private static final String __OBFID = "CL_00002102";
    
    private EnumOrientation(String p_i45709_1_, int p_i45709_2_, int p_i45709_3_, String p_i45709_4_, EnumFacing p_i45709_5_)
    {
      this.field_176866_j = p_i45709_3_;
      this.field_176867_k = p_i45709_4_;
      this.field_176864_l = p_i45709_5_;
    }
    
    public int func_176855_a()
    {
      return this.field_176866_j;
    }
    
    public EnumFacing func_176852_c()
    {
      return this.field_176864_l;
    }
    
    public String toString()
    {
      return this.field_176867_k;
    }
    
    public static EnumOrientation func_176853_a(int p_176853_0_)
    {
      if ((p_176853_0_ < 0) || (p_176853_0_ >= field_176869_i.length)) {
        p_176853_0_ = 0;
      }
      return field_176869_i[p_176853_0_];
    }
    
    public static EnumOrientation func_176856_a(EnumFacing p_176856_0_, EnumFacing p_176856_1_)
    {
      switch (BlockLever.SwitchEnumFacing.FACING_LOOKUP[p_176856_0_.ordinal()])
      {
      case 1: 
        switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()])
        {
        case 1: 
          return DOWN_X;
        case 2: 
          return DOWN_Z;
        }
        throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
      case 2: 
        switch (BlockLever.SwitchEnumFacing.AXIS_LOOKUP[p_176856_1_.getAxis().ordinal()])
        {
        case 1: 
          return UP_X;
        case 2: 
          return UP_Z;
        }
        throw new IllegalArgumentException("Invalid entityFacing " + p_176856_1_ + " for facing " + p_176856_0_);
      case 3: 
        return NORTH;
      case 4: 
        return SOUTH;
      case 5: 
        return WEST;
      case 6: 
        return EAST;
      }
      throw new IllegalArgumentException("Invalid facing: " + p_176856_0_);
    }
    
    public String getName()
    {
      return this.field_176867_k;
    }
    
    static
    {
      field_176869_i = new EnumOrientation[values().length];
      
      $VALUES = new EnumOrientation[] { DOWN_X, EAST, WEST, SOUTH, NORTH, UP_Z, UP_X, DOWN_Z };
      
      EnumOrientation[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumOrientation var3 = var0[var2];
        field_176869_i[var3.func_176855_a()] = var3;
      }
    }
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] FACING_LOOKUP;
    static final int[] ORIENTATION_LOOKUP;
    static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];
    private static final String __OBFID = "CL_00002103";
    
    SwitchEnumFacing() {}
    
    static
    {
      try
      {
        AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      ORIENTATION_LOOKUP = new int[BlockLever.EnumOrientation.values().length];
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.EAST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.NORTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_Z.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_X.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      FACING_LOOKUP = new int[EnumFacing.values().length];
      try
      {
        FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
      try
      {
        FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError16) {}
    }
  }
}
