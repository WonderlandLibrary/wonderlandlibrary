package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTorch extends Block {
   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate() {
      public boolean apply(EnumFacing var1) {
         return var1 != EnumFacing.DOWN;
      }

      public boolean apply(Object var1) {
         return this.apply((EnumFacing)var1);
      }
   });
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
      double var6 = (double)var2.getX() + 0.5D;
      double var8 = (double)var2.getY() + 0.7D;
      double var10 = (double)var2.getZ() + 0.5D;
      double var12 = 0.22D;
      double var14 = 0.27D;
      if (var5.getAxis().isHorizontal()) {
         EnumFacing var16 = var5.getOpposite();
         var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14 * (double)var16.getFrontOffsetX(), var8 + var12, var10 + var14 * (double)var16.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
         var1.spawnParticle(EnumParticleTypes.FLAME, var6 + var14 * (double)var16.getFrontOffsetX(), var8 + var12, var10 + var14 * (double)var16.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D);
      } else {
         var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6, var8, var10, 0.0D, 0.0D, 0.0D);
         var1.spawnParticle(EnumParticleTypes.FLAME, var6, var8, var10, 0.0D, 0.0D, 0.0D);
      }

   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3;
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[((EnumFacing)var1.getValue(FACING)).ordinal()]) {
      case 1:
      case 2:
      default:
         var3 = var2 | 5;
         break;
      case 3:
         var3 = var2 | 4;
         break;
      case 4:
         var3 = var2 | 3;
         break;
      case 5:
         var3 = var2 | 2;
         break;
      case 6:
         var3 = var2 | 1;
      }

      return var3;
   }

   protected boolean onNeighborChangeInternal(World var1, BlockPos var2, IBlockState var3) {
      if (var3 != false) {
         return true;
      } else {
         EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
         EnumFacing.Axis var5 = var4.getAxis();
         EnumFacing var6 = var4.getOpposite();
         boolean var7 = false;
         if (var5.isHorizontal() && !var1.isBlockNormalCube(var2.offset(var6), true)) {
            var7 = true;
         } else if (var5.isVertical() && var2.offset(var6) != false) {
            var7 = true;
         }

         if (var7) {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
            return true;
         } else {
            return false;
         }
      }
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public MovingObjectPosition collisionRayTrace(World var1, BlockPos var2, Vec3 var3, Vec3 var4) {
      EnumFacing var5 = (EnumFacing)var1.getBlockState(var2).getValue(FACING);
      float var6 = 0.15F;
      if (var5 == EnumFacing.EAST) {
         this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
      } else if (var5 == EnumFacing.WEST) {
         this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
      } else if (var5 == EnumFacing.SOUTH) {
         this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
      } else if (var5 == EnumFacing.NORTH) {
         this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
      } else {
         var6 = 0.1F;
         this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
      }

      return super.collisionRayTrace(var1, var2, var3, var4);
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.checkForDrop(var1, var2, var3);
   }

   protected BlockTorch() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (var3 != false) {
         return this.getDefaultState().withProperty(FACING, var3);
      } else {
         Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var10.hasNext()) {
            Object var9 = var10.next();
            if (var1.isBlockNormalCube(var2.offset(((EnumFacing)var9).getOpposite()), true)) {
               return this.getDefaultState().withProperty(FACING, (EnumFacing)var9);
            }
         }

         return this.getDefaultState();
      }
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      Iterator var4 = FACING.getAllowedValues().iterator();

      while(var4.hasNext()) {
         EnumFacing var3 = (EnumFacing)var4.next();
         if (var3 != false) {
            return true;
         }
      }

      return false;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      this.onNeighborChangeInternal(var1, var2, var3);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   public IBlockState getStateFromMeta(int var1) {
      IBlockState var2 = this.getDefaultState();
      switch(var1) {
      case 1:
         var2 = var2.withProperty(FACING, EnumFacing.EAST);
         break;
      case 2:
         var2 = var2.withProperty(FACING, EnumFacing.WEST);
         break;
      case 3:
         var2 = var2.withProperty(FACING, EnumFacing.SOUTH);
         break;
      case 4:
         var2 = var2.withProperty(FACING, EnumFacing.NORTH);
         break;
      case 5:
      default:
         var2 = var2.withProperty(FACING, EnumFacing.UP);
      }

      return var2;
   }
}
