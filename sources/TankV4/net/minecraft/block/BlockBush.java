package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBush extends Block {
   protected BlockBush(Material var1) {
      this(var1, var1.getMaterialMapColor());
   }

   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return this.canPlaceBlockOn(var1.getBlockState(var2.down()).getBlock());
   }

   public boolean isFullCube() {
      return false;
   }

   protected BlockBush() {
      this(Material.plants);
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.checkAndDropBlock(var1, var2, var3);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      super.onNeighborBlockChange(var1, var2, var3, var4);
      this.checkAndDropBlock(var1, var2, var3);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   protected void checkAndDropBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canBlockStay(var1, var2, var3)) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockState(var2, Blocks.air.getDefaultState(), 3);
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   protected BlockBush(Material var1, MapColor var2) {
      super(var1, var2);
      this.setTickRandomly(true);
      float var3 = 0.2F;
      this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(var1, var2) && this != var1.getBlockState(var2.down()).getBlock();
   }
}
