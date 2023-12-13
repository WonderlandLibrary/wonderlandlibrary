package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block {
   public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      float var4 = 0.0625F;
      float var5 = (float)(1 + (Integer)var3.getValue(BITES) * 2) / 16.0F;
      float var6 = 0.5F;
      return new AxisAlignedBB((double)((float)var2.getX() + var5), (double)var2.getY(), (double)((float)var2.getZ() + var4), (double)((float)(var2.getX() + 1) - var4), (double)((float)var2.getY() + var6), (double)((float)(var2.getZ() + 1) - var4));
   }

   private void eatCake(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (var4.canEat(false)) {
         var4.triggerAchievement(StatList.field_181724_H);
         var4.getFoodStats().addStats(2, 0.1F);
         int var5 = (Integer)var3.getValue(BITES);
         if (var5 < 6) {
            var1.setBlockState(var2, var3.withProperty(BITES, var5 + 1), 3);
         } else {
            var1.setBlockToAir(var2);
         }
      }

   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!this.canBlockStay(var1, var2)) {
         var1.setBlockToAir(var2);
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{BITES});
   }

   protected BlockCake() {
      super(Material.cake);
      this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, 0));
      this.setTickRandomly(true);
   }

   public void setBlockBoundsForItemRender() {
      float var1 = 0.0625F;
      float var2 = 0.5F;
      this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
   }

   public Item getItem(World var1, BlockPos var2) {
      return Items.cake;
   }

   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      this.eatCake(var1, var2, var1.getBlockState(var2), var3);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(BITES, var1);
   }

   public int getComparatorInputOverride(World var1, BlockPos var2) {
      return (7 - (Integer)var1.getBlockState(var2).getValue(BITES)) * 2;
   }

   public int quantityDropped(Random var1) {
      return 0;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.0625F;
      float var4 = (float)(1 + (Integer)var1.getBlockState(var2).getValue(BITES) * 2) / 16.0F;
      float var5 = 0.5F;
      this.setBlockBounds(var4, 0.0F, var3, 1.0F - var3, var5, 1.0F - var3);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(var1, var2) ? this.canBlockStay(var1, var2) : false;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(BITES);
   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      this.eatCake(var1, var2, var3, var4);
      return true;
   }

   private boolean canBlockStay(World var1, BlockPos var2) {
      return var1.getBlockState(var2.down()).getBlock().getMaterial().isSolid();
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return null;
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      return this.getCollisionBoundingBox(var1, var2, var1.getBlockState(var2));
   }
}
