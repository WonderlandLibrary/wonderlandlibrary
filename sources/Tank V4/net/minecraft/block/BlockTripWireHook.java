package net.minecraft.block;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block {
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public static final PropertyBool POWERED;
   public static final PropertyDirection FACING;
   public static final PropertyBool ATTACHED;
   public static final PropertyBool SUSPENDED;

   static {
      FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
      POWERED = PropertyBool.create("powered");
      ATTACHED = PropertyBool.create("attached");
      SUSPENDED = PropertyBool.create("suspended");
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.func_176260_a(var1, var2, var3, false, true, -1, (IBlockState)null);
   }

   public boolean isFullCube() {
      return false;
   }

   private void func_180694_a(World var1, BlockPos var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      if (var4 && !var6) {
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.1D, (double)var2.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
      } else if (!var4 && var6) {
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.1D, (double)var2.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
      } else if (var3 && !var5) {
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.1D, (double)var2.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
      } else if (!var3 && var5) {
         var1.playSoundEffect((double)var2.getX() + 0.5D, (double)var2.getY() + 0.1D, (double)var2.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (var1.rand.nextFloat() * 0.2F + 0.9F));
      }

   }

   public int getMetaFromState(IBlockState var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumFacing)var1.getValue(FACING)).getHorizontalIndex();
      if ((Boolean)var1.getValue(POWERED)) {
         var3 |= 8;
      }

      if ((Boolean)var1.getValue(ATTACHED)) {
         var3 |= 4;
      }

      return var3;
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return (Boolean)var3.getValue(POWERED) ? 15 : 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED, ATTACHED, SUSPENDED});
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return var1.withProperty(SUSPENDED, !World.doesBlockHaveSolidTopSurface(var2, var3.down()));
   }

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      this.func_176260_a(var1, var2, var3, false, false, -1, (IBlockState)null);
   }

   public BlockTripWireHook() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false).withProperty(ATTACHED, false).withProperty(SUSPENDED, false));
      this.setCreativeTab(CreativeTabs.tabRedstone);
      this.setTickRandomly(true);
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(var1 & 3)).withProperty(POWERED, (var1 & 8) > 0).withProperty(ATTACHED, (var1 & 4) > 0);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (var4 != this && var3 <= 0) {
         EnumFacing var5 = (EnumFacing)var3.getValue(FACING);
         if (!var1.getBlockState(var2.offset(var5.getOpposite())).getBlock().isNormalCube()) {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
         }
      }

   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return var3.getAxis().isHorizontal() && var1.getBlockState(var2.offset(var3.getOpposite())).getBlock().isNormalCube();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT_MIPPED;
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      boolean var4 = (Boolean)var3.getValue(ATTACHED);
      boolean var5 = (Boolean)var3.getValue(POWERED);
      if (var4 || var5) {
         this.func_176260_a(var1, var2, var3, true, false, -1, (IBlockState)null);
      }

      if (var5) {
         var1.notifyNeighborsOfStateChange(var2, this);
         var1.notifyNeighborsOfStateChange(var2.offset(((EnumFacing)var3.getValue(FACING)).getOpposite()), this);
      }

      super.breakBlock(var1, var2, var3);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      float var3 = 0.1875F;
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[((EnumFacing)var1.getBlockState(var2).getValue(FACING)).ordinal()]) {
      case 3:
         this.setBlockBounds(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
         break;
      case 4:
         this.setBlockBounds(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
         break;
      case 5:
         this.setBlockBounds(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
         break;
      case 6:
         this.setBlockBounds(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
      }

   }

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState var9 = this.getDefaultState().withProperty(POWERED, false).withProperty(ATTACHED, false).withProperty(SUSPENDED, false);
      if (var3.getAxis().isHorizontal()) {
         var9 = var9.withProperty(FACING, var3);
      }

      return var9;
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !(Boolean)var3.getValue(POWERED) ? 0 : (var3.getValue(FACING) == var4 ? 15 : 0);
   }

   public void func_176260_a(World var1, BlockPos var2, IBlockState var3, boolean var4, boolean var5, int var6, IBlockState var7) {
      EnumFacing var8 = (EnumFacing)var3.getValue(FACING);
      boolean var9 = (Boolean)var3.getValue(ATTACHED);
      boolean var10 = (Boolean)var3.getValue(POWERED);
      boolean var11 = !World.doesBlockHaveSolidTopSurface(var1, var2.down());
      boolean var12 = !var4;
      boolean var13 = false;
      int var14 = 0;
      IBlockState[] var15 = new IBlockState[42];

      BlockPos var17;
      for(int var16 = 1; var16 < 42; ++var16) {
         var17 = var2.offset(var8, var16);
         IBlockState var18 = var1.getBlockState(var17);
         if (var18.getBlock() == Blocks.tripwire_hook) {
            if (var18.getValue(FACING) == var8.getOpposite()) {
               var14 = var16;
            }
            break;
         }

         if (var18.getBlock() != Blocks.tripwire && var16 != var6) {
            var15[var16] = null;
            var12 = false;
         } else {
            if (var16 == var6) {
               var18 = (IBlockState)Objects.firstNonNull(var7, var18);
            }

            boolean var19 = !(Boolean)var18.getValue(BlockTripWire.DISARMED);
            boolean var20 = (Boolean)var18.getValue(BlockTripWire.POWERED);
            boolean var21 = (Boolean)var18.getValue(BlockTripWire.SUSPENDED);
            var12 &= var21 == var11;
            var13 |= var19 && var20;
            var15[var16] = var18;
            if (var16 == var6) {
               var1.scheduleUpdate(var2, this, this.tickRate(var1));
               var12 &= var19;
            }
         }
      }

      var12 &= var14 > 1;
      var13 &= var12;
      IBlockState var22 = this.getDefaultState().withProperty(ATTACHED, var12).withProperty(POWERED, var13);
      if (var14 > 0) {
         var17 = var2.offset(var8, var14);
         EnumFacing var24 = var8.getOpposite();
         var1.setBlockState(var17, var22.withProperty(FACING, var24), 3);
         this.func_176262_b(var1, var17, var24);
         this.func_180694_a(var1, var17, var12, var13, var9, var10);
      }

      this.func_180694_a(var1, var2, var12, var13, var9, var10);
      if (!var4) {
         var1.setBlockState(var2, var22.withProperty(FACING, var8), 3);
         if (var5) {
            this.func_176262_b(var1, var2, var8);
         }
      }

      if (var9 != var12) {
         for(int var23 = 1; var23 < var14; ++var23) {
            BlockPos var25 = var2.offset(var8, var23);
            IBlockState var26 = var15[var23];
            if (var26 != null && var1.getBlockState(var25).getBlock() != Blocks.air) {
               var1.setBlockState(var25, var26.withProperty(ATTACHED, var12), 3);
            }
         }
      }

   }

   private void func_176262_b(World var1, BlockPos var2, EnumFacing var3) {
      var1.notifyNeighborsOfStateChange(var2, this);
      var1.notifyNeighborsOfStateChange(var2.offset(var3.getOpposite()), this);
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
}
