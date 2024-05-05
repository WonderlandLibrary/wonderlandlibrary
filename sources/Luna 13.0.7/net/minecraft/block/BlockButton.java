package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton
  extends Block
{
  public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing");
  public static final PropertyBool POWERED_PROP = PropertyBool.create("powered");
  private final boolean wooden;
  private static final String __OBFID = "CL_00000209";
  
  protected BlockButton(boolean wooden)
  {
    super(Material.circuits);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, EnumFacing.NORTH).withProperty(POWERED_PROP, Boolean.valueOf(false)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabRedstone);
    this.wooden = wooden;
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
  {
    return null;
  }
  
  public int tickRate(World worldIn)
  {
    return this.wooden ? 30 : 20;
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
    return worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock().isNormalCube();
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    EnumFacing[] var3 = EnumFacing.values();
    int var4 = var3.length;
    for (int var5 = 0; var5 < var4; var5++)
    {
      EnumFacing var6 = var3[var5];
      if (worldIn.getBlockState(pos.offset(var6)).getBlock().isNormalCube()) {
        return true;
      }
    }
    return false;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube() ? getDefaultState().withProperty(FACING_PROP, facing).withProperty(POWERED_PROP, Boolean.valueOf(false)) : getDefaultState().withProperty(FACING_PROP, EnumFacing.DOWN).withProperty(POWERED_PROP, Boolean.valueOf(false));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    if (func_176583_e(worldIn, pos, state))
    {
      EnumFacing var5 = (EnumFacing)state.getValue(FACING_PROP);
      if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().isNormalCube())
      {
        dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
      }
    }
  }
  
  private boolean func_176583_e(World worldIn, BlockPos p_176583_2_, IBlockState p_176583_3_)
  {
    if (!canPlaceBlockAt(worldIn, p_176583_2_))
    {
      dropBlockAsItem(worldIn, p_176583_2_, p_176583_3_, 0);
      worldIn.setBlockToAir(p_176583_2_);
      return false;
    }
    return true;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    func_180681_d(access.getBlockState(pos));
  }
  
  private void func_180681_d(IBlockState p_180681_1_)
  {
    EnumFacing var2 = (EnumFacing)p_180681_1_.getValue(FACING_PROP);
    boolean var3 = ((Boolean)p_180681_1_.getValue(POWERED_PROP)).booleanValue();
    float var4 = 0.25F;
    float var5 = 0.375F;
    float var6 = (var3 ? 1 : 2) / 16.0F;
    float var7 = 0.125F;
    float var8 = 0.1875F;
    switch (SwitchEnumFacing.field_180420_a[var2.ordinal()])
    {
    case 1: 
      setBlockBounds(0.0F, 0.375F, 0.3125F, var6, 0.625F, 0.6875F);
      break;
    case 2: 
      setBlockBounds(1.0F - var6, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
      break;
    case 3: 
      setBlockBounds(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, var6);
      break;
    case 4: 
      setBlockBounds(0.3125F, 0.375F, 1.0F - var6, 0.6875F, 0.625F, 1.0F);
      break;
    case 5: 
      setBlockBounds(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0F + var6, 0.625F);
      break;
    case 6: 
      setBlockBounds(0.3125F, 1.0F - var6, 0.375F, 0.6875F, 1.0F, 0.625F);
    }
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
      return true;
    }
    worldIn.setBlockState(pos, state.withProperty(POWERED_PROP, Boolean.valueOf(true)), 3);
    worldIn.markBlockRangeForRenderUpdate(pos, pos);
    worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
    func_176582_b(worldIn, pos, (EnumFacing)state.getValue(FACING_PROP));
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
    if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
      func_176582_b(worldIn, pos, (EnumFacing)state.getValue(FACING_PROP));
    }
    super.breakBlock(worldIn, pos, state);
  }
  
  public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return ((Boolean)state.getValue(POWERED_PROP)).booleanValue() ? 15 : 0;
  }
  
  public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
  {
    return state.getValue(FACING_PROP) == side ? 15 : !((Boolean)state.getValue(POWERED_PROP)).booleanValue() ? 0 : 0;
  }
  
  public boolean canProvidePower()
  {
    return true;
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
  {
    if (!worldIn.isRemote) {
      if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
        if (this.wooden)
        {
          func_180680_f(worldIn, pos, state);
        }
        else
        {
          worldIn.setBlockState(pos, state.withProperty(POWERED_PROP, Boolean.valueOf(false)));
          func_176582_b(worldIn, pos, (EnumFacing)state.getValue(FACING_PROP));
          worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
          worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
      }
    }
  }
  
  public void setBlockBoundsForItemRender()
  {
    float var1 = 0.1875F;
    float var2 = 0.125F;
    float var3 = 0.125F;
    setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
  {
    if (!worldIn.isRemote) {
      if (this.wooden) {
        if (!((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
          func_180680_f(worldIn, pos, state);
        }
      }
    }
  }
  
  private void func_180680_f(World worldIn, BlockPos p_180680_2_, IBlockState p_180680_3_)
  {
    func_180681_d(p_180680_3_);
    List var4 = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(p_180680_2_.getX() + this.minX, p_180680_2_.getY() + this.minY, p_180680_2_.getZ() + this.minZ, p_180680_2_.getX() + this.maxX, p_180680_2_.getY() + this.maxY, p_180680_2_.getZ() + this.maxZ));
    boolean var5 = !var4.isEmpty();
    boolean var6 = ((Boolean)p_180680_3_.getValue(POWERED_PROP)).booleanValue();
    if ((var5) && (!var6))
    {
      worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(POWERED_PROP, Boolean.valueOf(true)));
      func_176582_b(worldIn, p_180680_2_, (EnumFacing)p_180680_3_.getValue(FACING_PROP));
      worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
      worldIn.playSoundEffect(p_180680_2_.getX() + 0.5D, p_180680_2_.getY() + 0.5D, p_180680_2_.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
    }
    if ((!var5) && (var6))
    {
      worldIn.setBlockState(p_180680_2_, p_180680_3_.withProperty(POWERED_PROP, Boolean.valueOf(false)));
      func_176582_b(worldIn, p_180680_2_, (EnumFacing)p_180680_3_.getValue(FACING_PROP));
      worldIn.markBlockRangeForRenderUpdate(p_180680_2_, p_180680_2_);
      worldIn.playSoundEffect(p_180680_2_.getX() + 0.5D, p_180680_2_.getY() + 0.5D, p_180680_2_.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
    }
    if (var5) {
      worldIn.scheduleUpdate(p_180680_2_, this, tickRate(worldIn));
    }
  }
  
  private void func_176582_b(World worldIn, BlockPos p_176582_2_, EnumFacing p_176582_3_)
  {
    worldIn.notifyNeighborsOfStateChange(p_176582_2_, this);
    worldIn.notifyNeighborsOfStateChange(p_176582_2_.offset(p_176582_3_.getOpposite()), this);
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    EnumFacing var2;
    EnumFacing var2;
    EnumFacing var2;
    EnumFacing var2;
    EnumFacing var2;
    EnumFacing var2;
    switch (meta & 0x7)
    {
    case 0: 
      var2 = EnumFacing.DOWN;
      break;
    case 1: 
      var2 = EnumFacing.EAST;
      break;
    case 2: 
      var2 = EnumFacing.WEST;
      break;
    case 3: 
      var2 = EnumFacing.SOUTH;
      break;
    case 4: 
      var2 = EnumFacing.NORTH;
      break;
    case 5: 
    default: 
      var2 = EnumFacing.UP;
    }
    return getDefaultState().withProperty(FACING_PROP, var2).withProperty(POWERED_PROP, Boolean.valueOf((meta & 0x8) > 0));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    int var2;
    int var2;
    int var2;
    int var2;
    int var2;
    int var2;
    switch (SwitchEnumFacing.field_180420_a[((EnumFacing)state.getValue(FACING_PROP)).ordinal()])
    {
    case 1: 
      var2 = 1;
      break;
    case 2: 
      var2 = 2;
      break;
    case 3: 
      var2 = 3;
      break;
    case 4: 
      var2 = 4;
      break;
    case 5: 
    default: 
      var2 = 5;
      break;
    case 6: 
      var2 = 0;
    }
    if (((Boolean)state.getValue(POWERED_PROP)).booleanValue()) {
      var2 |= 0x8;
    }
    return var2;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { FACING_PROP, POWERED_PROP });
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] field_180420_a = new int[EnumFacing.values().length];
    private static final String __OBFID = "CL_00002131";
    
    SwitchEnumFacing() {}
    
    static
    {
      try
      {
        field_180420_a[EnumFacing.EAST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_180420_a[EnumFacing.WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_180420_a[EnumFacing.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        field_180420_a[EnumFacing.NORTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        field_180420_a[EnumFacing.UP.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        field_180420_a[EnumFacing.DOWN.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
    }
  }
}
