package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.world.World;

public class BlockEndPortalFrame
  extends Block
{
  public static final PropertyDirection field_176508_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static final PropertyBool field_176507_b = PropertyBool.create("eye");
  private static final String __OBFID = "CL_00000237";
  
  public BlockEndPortalFrame()
  {
    super(Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176508_a, EnumFacing.NORTH).withProperty(field_176507_b, Boolean.valueOf(false)));
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public void setBlockBoundsForItemRender()
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
  }
  
  public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    if (((Boolean)worldIn.getBlockState(pos).getValue(field_176507_b)).booleanValue())
    {
      setBlockBounds(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
      super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }
    setBlockBoundsForItemRender();
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
    return null;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    return getDefaultState().withProperty(field_176508_a, placer.func_174811_aO().getOpposite()).withProperty(field_176507_b, Boolean.valueOf(false));
  }
  
  public boolean hasComparatorInputOverride()
  {
    return true;
  }
  
  public int getComparatorInputOverride(World worldIn, BlockPos pos)
  {
    return ((Boolean)worldIn.getBlockState(pos).getValue(field_176507_b)).booleanValue() ? 15 : 0;
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(field_176507_b, Boolean.valueOf((meta & 0x4) != 0)).withProperty(field_176508_a, EnumFacing.getHorizontal(meta & 0x3));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    byte var2 = 0;
    int var3 = var2 | ((EnumFacing)state.getValue(field_176508_a)).getHorizontalIndex();
    if (((Boolean)state.getValue(field_176507_b)).booleanValue()) {
      var3 |= 0x4;
    }
    return var3;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176508_a, field_176507_b });
  }
}
