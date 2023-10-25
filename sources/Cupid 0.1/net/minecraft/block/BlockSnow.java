package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block {
  public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
  
  protected BlockSnow() {
    super(Material.snow);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LAYERS, Integer.valueOf(1)));
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.tabDecorations);
    setBlockBoundsForItemRender();
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return (((Integer)worldIn.getBlockState(pos).getValue((IProperty)LAYERS)).intValue() < 5);
  }
  
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
    int i = ((Integer)state.getValue((IProperty)LAYERS)).intValue() - 1;
    float f = 0.125F;
    return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, (pos.getY() + i * f), pos.getZ() + this.maxZ);
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public boolean isFullCube() {
    return false;
  }
  
  public void setBlockBoundsForItemRender() {
    getBoundsForLayers(0);
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    getBoundsForLayers(((Integer)iblockstate.getValue((IProperty)LAYERS)).intValue());
  }
  
  protected void getBoundsForLayers(int p_150154_1_) {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, p_150154_1_ / 8.0F, 1.0F);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos.down());
    Block block = iblockstate.getBlock();
    return (block != Blocks.ice && block != Blocks.packed_ice) ? ((block.getMaterial() == Material.leaves) ? true : ((block == this && ((Integer)iblockstate.getValue((IProperty)LAYERS)).intValue() >= 7) ? true : ((block.isOpaqueCube() && block.blockMaterial.blocksMovement())))) : false;
  }
  
  public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    checkAndDropBlock(worldIn, pos, state);
  }
  
  private boolean checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!canPlaceBlockAt(worldIn, pos)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      return false;
    } 
    return true;
  }
  
  public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
    spawnAsEntity(worldIn, pos, new ItemStack(Items.snowball, ((Integer)state.getValue((IProperty)LAYERS)).intValue() + 1, 0));
    worldIn.setBlockToAir(pos);
    player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.snowball;
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
      dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    return (side == EnumFacing.UP) ? true : super.shouldSideBeRendered(worldIn, pos, side);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)LAYERS, Integer.valueOf((meta & 0x7) + 1));
  }
  
  public boolean isReplaceable(World worldIn, BlockPos pos) {
    return (((Integer)worldIn.getBlockState(pos).getValue((IProperty)LAYERS)).intValue() == 1);
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)LAYERS)).intValue() - 1;
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { (IProperty)LAYERS });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */