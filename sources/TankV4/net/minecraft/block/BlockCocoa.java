package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockDirectional implements IGrowable {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)var1.getValue(FACING)).getHorizontalIndex();
      var3 |= (Integer)var1.getValue(AGE) << 2;
      return var3;
   }

   private void dropBlock(World var1, BlockPos var2, IBlockState var3) {
      var1.setBlockState(var2, Blocks.air.getDefaultState(), 3);
      this.dropBlockAsItem(var1, var2, var3, 0);
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
      int var5 = (Integer)var3.getValue(AGE);
      int var6 = 4 + var5 * 2;
      int var7 = 5 + var5 * 2;
      float var8 = (float)var6 / 2.0F;
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var4.ordinal()]) {
      case 3:
         this.setBlockBounds((8.0F - var8) / 16.0F, (12.0F - (float)var7) / 16.0F, 0.0625F, (8.0F + var8) / 16.0F, 0.75F, (1.0F + (float)var6) / 16.0F);
         break;
      case 4:
         this.setBlockBounds((8.0F - var8) / 16.0F, (12.0F - (float)var7) / 16.0F, (15.0F - (float)var6) / 16.0F, (8.0F + var8) / 16.0F, 0.75F, 0.9375F);
         break;
      case 5:
         this.setBlockBounds(0.0625F, (12.0F - (float)var7) / 16.0F, (8.0F - var8) / 16.0F, (1.0F + (float)var6) / 16.0F, 0.75F, (8.0F + var8) / 16.0F);
         break;
      case 6:
         this.setBlockBounds((15.0F - (float)var6) / 16.0F, (12.0F - (float)var7) / 16.0F, (8.0F - var8) / 16.0F, 0.9375F, 0.75F, (8.0F + var8) / 16.0F);
      }

   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (var2 == var3) {
         this.dropBlock(var1, var2, var3);
      } else if (var1.rand.nextInt(5) == 0) {
         int var5 = (Integer)var3.getValue(AGE);
         if (var5 < 2) {
            var1.setBlockState(var2, var3.withProperty(AGE, var5 + 1), 2);
         }
      }

   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(var1)).withProperty(AGE, (var1 & 15) >> 2);
   }

   public int getDamageValue(World var1, BlockPos var2) {
      return EnumDyeColor.BROWN.getDyeDamage();
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      int var6 = (Integer)var3.getValue(AGE);
      byte var7 = 1;
      if (var6 >= 2) {
         var7 = 3;
      }

      for(int var8 = 0; var8 < var7; ++var8) {
         spawnAsEntity(var1, var2, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
      }

   }

   public BlockCocoa() {
      super(Material.plants);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AGE, 0));
      this.setTickRandomly(true);
   }

   public boolean isFullCube() {
      return false;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, AGE});
   }

   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return (Integer)var3.getValue(AGE) < 2;
   }

   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      var1.setBlockState(var3, var4.withProperty(AGE, (Integer)var4.getValue(AGE) + 1), 2);
   }

   public Item getItem(World var1, BlockPos var2) {
      return Items.dye;
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (!var3.getAxis().isHorizontal()) {
         var3 = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, var3.getOpposite()).withProperty(AGE, 0);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var2 == var3) {
         this.dropBlock(var1, var2, var3);
      }

   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getCollisionBoundingBox(var1, var2, var3);
   }

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getSelectedBoundingBox(var1, var2);
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      EnumFacing var6 = EnumFacing.fromAngle((double)var4.rotationYaw);
      var1.setBlockState(var2, var3.withProperty(FACING, var6), 2);
   }
}
