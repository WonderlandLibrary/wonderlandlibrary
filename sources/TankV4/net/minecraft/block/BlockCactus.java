package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockCactus extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AGE, var1);
   }

   public boolean isFullCube() {
      return false;
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      float var3 = 0.0625F;
      return new AxisAlignedBB((double)((float)var2.getX() + var3), (double)var2.getY(), (double)((float)var2.getZ() + var3), (double)((float)(var2.getX() + 1) - var3), (double)(var2.getY() + 1), (double)((float)(var2.getZ() + 1) - var3));
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      float var4 = 0.0625F;
      return new AxisAlignedBB((double)((float)var2.getX() + var4), (double)var2.getY(), (double)((float)var2.getZ() + var4), (double)((float)(var2.getX() + 1) - var4), (double)((float)(var2.getY() + 1) - var4), (double)((float)(var2.getZ() + 1) - var4));
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var2 != null) {
         var1.destroyBlock(var2, true);
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void onEntityCollidedWithBlock(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      var4.attackEntityFrom(DamageSource.cactus, 1.0F);
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   protected BlockCactus() {
      super(Material.cactus);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(var1, var2) ? this.canBlockStay(var1, var2) : false;
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      BlockPos var5 = var2.up();
      if (var1.isAirBlock(var5)) {
         int var6;
         for(var6 = 1; var1.getBlockState(var2.down(var6)).getBlock() == this; ++var6) {
         }

         if (var6 < 3) {
            int var7 = (Integer)var3.getValue(AGE);
            if (var7 == 15) {
               var1.setBlockState(var5, this.getDefaultState());
               IBlockState var8 = var3.withProperty(AGE, 0);
               var1.setBlockState(var2, var8, 4);
               this.onNeighborBlockChange(var1, var5, var8, this);
            } else {
               var1.setBlockState(var2, var3.withProperty(AGE, var7 + 1), 4);
            }
         }
      }

   }

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(AGE);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{AGE});
   }
}
