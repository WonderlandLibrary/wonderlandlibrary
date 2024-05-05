package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet
  extends Block
{
  public static final PropertyEnum field_176330_a = PropertyEnum.create("color", EnumDyeColor.class);
  private static final String __OBFID = "CL_00000338";
  
  protected BlockCarpet()
  {
    super(Material.carpet);
    setDefaultState(this.blockState.getBaseState().withProperty(field_176330_a, EnumDyeColor.WHITE));
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabDecorations);
    setBlockBoundsFromMeta(0);
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean isFullCube()
  {
    return false;
  }
  
  public void setBlockBoundsForItemRender()
  {
    setBlockBoundsFromMeta(0);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
  {
    setBlockBoundsFromMeta(0);
  }
  
  protected void setBlockBoundsFromMeta(int meta)
  {
    byte var2 = 0;
    float var3 = 1 * (1 + var2) / 16.0F;
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    return (super.canPlaceBlockAt(worldIn, pos)) && (canBlockStay(worldIn, pos));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
  {
    checkAndDropBlock(worldIn, pos, state);
  }
  
  private boolean checkAndDropBlock(World worldIn, BlockPos p_176328_2_, IBlockState p_176328_3_)
  {
    if (!canBlockStay(worldIn, p_176328_2_))
    {
      dropBlockAsItem(worldIn, p_176328_2_, p_176328_3_, 0);
      worldIn.setBlockToAir(p_176328_2_);
      return false;
    }
    return true;
  }
  
  private boolean canBlockStay(World worldIn, BlockPos p_176329_2_)
  {
    return !worldIn.isAirBlock(p_176329_2_.offsetDown());
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
  }
  
  public int damageDropped(IBlockState state)
  {
    return ((EnumDyeColor)state.getValue(field_176330_a)).func_176765_a();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
  {
    for (int var4 = 0; var4 < 16; var4++) {
      list.add(new ItemStack(itemIn, 1, var4));
    }
  }
  
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(field_176330_a, EnumDyeColor.func_176764_b(meta));
  }
  
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumDyeColor)state.getValue(field_176330_a)).func_176765_a();
  }
  
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { field_176330_a });
  }
}
