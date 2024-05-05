package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab
  extends Block
{
  public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", EnumBlockHalf.class);
  private static final String __OBFID = "CL_00000253";
  
  public BlockSlab(Material p_i45714_1_)
  {
    super(p_i45714_1_);
    if (isDouble()) {
      this.fullBlock = true;
    } else {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
    setLightOpacity(255);
  }
  
  protected boolean canSilkHarvest()
  {
    return false;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    if (isDouble())
    {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    else
    {
      IBlockState var3 = access.getBlockState(pos);
      if (var3.getBlock() == this) {
        if (var3.getValue(HALF_PROP) == EnumBlockHalf.TOP) {
          setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
          setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
      }
    }
  }
  
  public void setBlockBoundsForItemRender()
  {
    if (isDouble()) {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else {
      setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
  }
  
  public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
  {
    setBlockBoundsBasedOnState(worldIn, pos);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
  }
  
  public boolean isOpaqueCube()
  {
    return isDouble();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    IBlockState var9 = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF_PROP, EnumBlockHalf.BOTTOM);
    return (facing != EnumFacing.DOWN) && ((facing == EnumFacing.UP) || (hitY <= 0.5D)) ? var9 : isDouble() ? var9 : var9.withProperty(HALF_PROP, EnumBlockHalf.TOP);
  }
  
  public int quantityDropped(Random random)
  {
    return isDouble() ? 2 : 1;
  }
  
  public boolean isFullCube()
  {
    return isDouble();
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    if (isDouble()) {
      return super.shouldSideBeRendered(worldIn, pos, side);
    }
    if ((side != EnumFacing.UP) && (side != EnumFacing.DOWN) && (!super.shouldSideBeRendered(worldIn, pos, side))) {
      return false;
    }
    BlockPos var4 = pos.offset(side.getOpposite());
    IBlockState var5 = worldIn.getBlockState(pos);
    IBlockState var6 = worldIn.getBlockState(var4);
    boolean var7 = (func_150003_a(var5.getBlock())) && (var5.getValue(HALF_PROP) == EnumBlockHalf.TOP);
    boolean var8 = (func_150003_a(var6.getBlock())) && (var6.getValue(HALF_PROP) == EnumBlockHalf.TOP);
    return side == EnumFacing.DOWN;
  }
  
  protected static boolean func_150003_a(Block p_150003_0_)
  {
    return (p_150003_0_ == Blocks.stone_slab) || (p_150003_0_ == Blocks.wooden_slab) || (p_150003_0_ == Blocks.stone_slab2);
  }
  
  public abstract String getFullSlabName(int paramInt);
  
  public int getDamageValue(World worldIn, BlockPos pos)
  {
    return super.getDamageValue(worldIn, pos) & 0x7;
  }
  
  public abstract boolean isDouble();
  
  public abstract IProperty func_176551_l();
  
  public abstract Object func_176553_a(ItemStack paramItemStack);
  
  public static enum EnumBlockHalf
    implements IStringSerializable
  {
    private final String halfName;
    private static final EnumBlockHalf[] $VALUES = { TOP, BOTTOM };
    private static final String __OBFID = "CL_00002109";
    
    private EnumBlockHalf(String p_i45713_1_, int p_i45713_2_, String p_i45713_3_)
    {
      this.halfName = p_i45713_3_;
    }
    
    public String toString()
    {
      return this.halfName;
    }
    
    public String getName()
    {
      return this.halfName;
    }
  }
}
