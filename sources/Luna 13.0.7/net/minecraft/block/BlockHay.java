package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class BlockHay
  extends BlockRotatedPillar
{
  private static final String __OBFID = "CL_00000256";
  
  public BlockHay()
  {
    super(Material.grass);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176298_M, EnumFacing.Axis.Y));
    setCreativeTab(CreativeTabs.tabBlock);
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    EnumFacing.Axis var2 = EnumFacing.Axis.Y;
    int var3 = meta & 0xC;
    if (var3 == 4) {
      var2 = EnumFacing.Axis.X;
    } else if (var3 == 8) {
      var2 = EnumFacing.Axis.Z;
    }
    return getDefaultState().withProperty(field_176298_M, var2);
  }
  
  public int getMetaFromState(IBlockState state)
  {
    int var2 = 0;
    EnumFacing.Axis var3 = (EnumFacing.Axis)state.getValue(field_176298_M);
    if (var3 == EnumFacing.Axis.X) {
      var2 |= 0x4;
    } else if (var3 == EnumFacing.Axis.Z) {
      var2 |= 0x8;
    }
    return var2;
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176298_M });
  }
  
  protected ItemStack createStackedBlock(IBlockState state)
  {
    return new ItemStack(Item.getItemFromBlock(this), 1, 0);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(field_176298_M, facing.getAxis());
  }
}
