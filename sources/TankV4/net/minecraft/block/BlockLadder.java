package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
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
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
   public static final PropertyDirection FACING;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

   public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getSelectedBoundingBox(var1, var2);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
      if (!this.canBlockStay(var1, var2, var5)) {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
      }

      super.onNeighborBlockChange(var1, var2, var3, var4);
   }

   public IBlockState getStateFromMeta(int var1) {
      EnumFacing var2 = EnumFacing.getFront(var1);
      if (var2.getAxis() == EnumFacing.Axis.Y) {
         var2 = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, var2);
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.getCollisionBoundingBox(var1, var2, var3);
   }

   public boolean isFullCube() {
      return false;
   }

   public int getMetaFromState(IBlockState var1) {
      return ((EnumFacing)var1.getValue(FACING)).getIndex();
   }

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }

   protected boolean canBlockStay(World var1, BlockPos var2, EnumFacing var3) {
      return var1.getBlockState(var2.offset(var3.getOpposite())).getBlock().isNormalCube();
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return var1.getBlockState(var2.west()).getBlock().isNormalCube() ? true : (var1.getBlockState(var2.east()).getBlock().isNormalCube() ? true : (var1.getBlockState(var2.north()).getBlock().isNormalCube() ? true : var1.getBlockState(var2.south()).getBlock().isNormalCube()));
   }

   protected BlockLadder() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (var3.getAxis().isHorizontal() && this.canBlockStay(var1, var2, var3)) {
         return this.getDefaultState().withProperty(FACING, var3);
      } else {
         Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var10.hasNext()) {
            Object var9 = var10.next();
            if (this.canBlockStay(var1, var2, (EnumFacing)var9)) {
               return this.getDefaultState().withProperty(FACING, (EnumFacing)var9);
            }
         }

         return this.getDefaultState();
      }
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

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      if (var3.getBlock() == this) {
         float var4 = 0.125F;
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[((EnumFacing)var3.getValue(FACING)).ordinal()]) {
         case 3:
            this.setBlockBounds(0.0F, 0.0F, 1.0F - var4, 1.0F, 1.0F, 1.0F);
            break;
         case 4:
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var4);
            break;
         case 5:
            this.setBlockBounds(1.0F - var4, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 6:
         default:
            this.setBlockBounds(0.0F, 0.0F, 0.0F, var4, 1.0F, 1.0F);
         }
      }

   }
}
