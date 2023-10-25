package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
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

public class BlockCarpet extends Block {
  public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
  
  protected BlockCarpet() {
    super(Material.carpet);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.WHITE));
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabDecorations);
    setBlockBoundsFromMeta(0);
  }
  
  public MapColor getMapColor(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMapColor();
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public void setBlockBoundsForItemRender() {
    setBlockBoundsFromMeta(0);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    setBlockBoundsFromMeta(0);
  }
  
  protected void setBlockBoundsFromMeta(int meta) {
    int i = 0;
    float f = (1 * (1 + i)) / 16.0F;
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos));
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    checkForDrop(worldIn, pos, state);
  }
  
  private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (!canBlockStay(worldIn, pos)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      return false;
    } 
    return true;
  }
  
  private boolean canBlockStay(World worldIn, BlockPos pos) {
    return !worldIn.isAirBlock(pos.down());
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    return (side == EnumFacing.UP) ? true : super.shouldSideBeRendered(worldIn, pos, side);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (int i = 0; i < 16; i++)
      list.add(new ItemStack(itemIn, 1, i)); 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)COLOR });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockCarpet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */