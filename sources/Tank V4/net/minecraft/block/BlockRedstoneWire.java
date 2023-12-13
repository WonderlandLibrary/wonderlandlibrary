package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block {
   public static final PropertyEnum NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
   private final Set blocksNeedingUpdate = Sets.newHashSet();
   public static final PropertyEnum WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
   public static final PropertyEnum EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
   private boolean canProvidePower = true;
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

   public int getMetaFromState(IBlockState var1) {
      return (Integer)var1.getValue(POWER);
   }

   protected static boolean canConnectUpwardsTo(IBlockAccess var0, BlockPos var1) {
      return canConnectUpwardsTo(var0.getBlockState(var1));
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.redstone;
   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public BlockRedstoneWire() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, 0));
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
   }

   protected static boolean canConnectUpwardsTo(IBlockState var0) {
      return canConnectTo(var0, (EnumFacing)null);
   }

   private int getMaxCurrentStrength(World var1, BlockPos var2, int var3) {
      if (var1.getBlockState(var2).getBlock() != this) {
         return var3;
      } else {
         int var4 = (Integer)var1.getBlockState(var2).getValue(POWER);
         return var4 > var3 ? var4 : var3;
      }
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      if (!this.canProvidePower) {
         return 0;
      } else {
         int var5 = (Integer)var3.getValue(POWER);
         if (var5 == 0) {
            return 0;
         } else if (var4 == EnumFacing.UP) {
            return var5;
         } else {
            EnumSet var6 = EnumSet.noneOf(EnumFacing.class);
            Iterator var8 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var8.hasNext()) {
               Object var7 = var8.next();
               if ((EnumFacing)var7 != false) {
                  var6.add((EnumFacing)var7);
               }
            }

            if (var4.getAxis().isHorizontal() && var6.isEmpty()) {
               return var5;
            } else if (var6.contains(var4) && !var6.contains(var4.rotateYCCW()) && !var6.contains(var4.rotateY())) {
               return var5;
            } else {
               return 0;
            }
         }
      }
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote) {
         if (var2 != false) {
            this.updateSurroundingRedstone(var1, var2, var3);
         } else {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
         }
      }

   }

   private IBlockState updateSurroundingRedstone(World var1, BlockPos var2, IBlockState var3) {
      var3 = this.calculateCurrentChanges(var1, var2, var2, var3);
      ArrayList var4 = Lists.newArrayList((Iterable)this.blocksNeedingUpdate);
      this.blocksNeedingUpdate.clear();
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         BlockPos var5 = (BlockPos)var6.next();
         var1.notifyNeighborsOfStateChange(var5, this);
      }

      return var3;
   }

   public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
      IBlockState var4 = var1.getBlockState(var2);
      return var4.getBlock() != this ? super.colorMultiplier(var1, var2, var3) : this.colorMultiplier((Integer)var4.getValue(POWER));
   }

   private IBlockState calculateCurrentChanges(World var1, BlockPos var2, BlockPos var3, IBlockState var4) {
      IBlockState var5 = var4;
      int var6 = (Integer)var4.getValue(POWER);
      byte var7 = 0;
      int var15 = this.getMaxCurrentStrength(var1, var3, var7);
      this.canProvidePower = false;
      int var8 = var1.isBlockIndirectlyGettingPowered(var2);
      this.canProvidePower = true;
      if (var8 > 0 && var8 > var15 - 1) {
         var15 = var8;
      }

      int var9 = 0;
      Iterator var11 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         while(var11.hasNext()) {
            Object var10 = var11.next();
            BlockPos var12 = var2.offset((EnumFacing)var10);
            boolean var13 = var12.getX() != var3.getX() || var12.getZ() != var3.getZ();
            if (var13) {
               var9 = this.getMaxCurrentStrength(var1, var12, var9);
            }

            if (var1.getBlockState(var12).getBlock().isNormalCube() && !var1.getBlockState(var2.up()).getBlock().isNormalCube()) {
               if (var13 && var2.getY() >= var3.getY()) {
                  var9 = this.getMaxCurrentStrength(var1, var12.up(), var9);
               }
            } else if (!var1.getBlockState(var12).getBlock().isNormalCube() && var13 && var2.getY() <= var3.getY()) {
               var9 = this.getMaxCurrentStrength(var1, var12.down(), var9);
            }
         }

         if (var9 > var15) {
            var15 = var9 - 1;
         } else if (var15 > 0) {
            --var15;
         } else {
            var15 = 0;
         }

         if (var8 > var15 - 1) {
            var15 = var8;
         }

         if (var6 != var15) {
            var4 = var4.withProperty(POWER, var15);
            if (var1.getBlockState(var2) == var5) {
               var1.setBlockState(var2, var4, 2);
            }

            this.blocksNeedingUpdate.add(var2);
            EnumFacing[] var19;
            int var18 = (var19 = EnumFacing.values()).length;

            for(int var17 = 0; var17 < var18; ++var17) {
               EnumFacing var16 = var19[var17];
               this.blocksNeedingUpdate.add(var2.offset(var16));
            }
         }

         return var4;
      }
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !this.canProvidePower ? 0 : this.getWeakPower(var1, var2, var3, var4);
   }

   public Item getItem(World var1, BlockPos var2) {
      return Items.redstone;
   }

   public boolean canProvidePower() {
      return this.canProvidePower;
   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      int var5 = (Integer)var3.getValue(POWER);
      if (var5 != 0) {
         double var6 = (double)var2.getX() + 0.5D + ((double)var4.nextFloat() - 0.5D) * 0.2D;
         double var8 = (double)((float)var2.getY() + 0.0625F);
         double var10 = (double)var2.getZ() + 0.5D + ((double)var4.nextFloat() - 0.5D) * 0.2D;
         float var12 = (float)var5 / 15.0F;
         float var13 = var12 * 0.6F + 0.4F;
         float var14 = Math.max(0.0F, var12 * var12 * 0.7F - 0.5F);
         float var15 = Math.max(0.0F, var12 * var12 * 0.6F - 0.7F);
         var1.spawnParticle(EnumParticleTypes.REDSTONE, var6, var8, var10, (double)var13, (double)var14, (double)var15);
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         this.updateSurroundingRedstone(var1, var2, var3);
         Iterator var5 = EnumFacing.Plane.VERTICAL.iterator();

         Object var4;
         while(var5.hasNext()) {
            var4 = var5.next();
            var1.notifyNeighborsOfStateChange(var2.offset((EnumFacing)var4), this);
         }

         var5 = EnumFacing.Plane.HORIZONTAL.iterator();

         EnumFacing var6;
         while(var5.hasNext()) {
            var4 = var5.next();
            var6 = (EnumFacing)var4;
            this.notifyWireNeighborsOfStateChange(var1, var2.offset(var6));
         }

         var5 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var5.hasNext()) {
            var4 = var5.next();
            var6 = (EnumFacing)var4;
            BlockPos var7 = var2.offset(var6);
            if (var1.getBlockState(var7).getBlock().isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(var1, var7.up());
            } else {
               this.notifyWireNeighborsOfStateChange(var1, var7.down());
            }
         }
      }

   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{NORTH, EAST, SOUTH, WEST, POWER});
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   private int colorMultiplier(int var1) {
      float var2 = (float)var1 / 15.0F;
      float var3 = var2 * 0.6F + 0.4F;
      if (var1 == 0) {
         var3 = 0.3F;
      }

      float var4 = var2 * var2 * 0.7F - 0.5F;
      float var5 = var2 * var2 * 0.6F - 0.7F;
      if (var4 < 0.0F) {
         var4 = 0.0F;
      }

      if (var5 < 0.0F) {
         var5 = 0.0F;
      }

      int var6 = MathHelper.clamp_int((int)(var3 * 255.0F), 0, 255);
      int var7 = MathHelper.clamp_int((int)(var4 * 255.0F), 0, 255);
      int var8 = MathHelper.clamp_int((int)(var5 * 255.0F), 0, 255);
      return -16777216 | var6 << 16 | var7 << 8 | var8;
   }

   private void notifyWireNeighborsOfStateChange(World var1, BlockPos var2) {
      if (var1.getBlockState(var2).getBlock() == this) {
         var1.notifyNeighborsOfStateChange(var2, this);
         EnumFacing[] var6;
         int var5 = (var6 = EnumFacing.values()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            EnumFacing var3 = var6[var4];
            var1.notifyNeighborsOfStateChange(var2.offset(var3), this);
         }
      }

   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(var1, var2, var3);
      if (!var1.isRemote) {
         EnumFacing[] var7;
         int var6 = (var7 = EnumFacing.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            EnumFacing var4 = var7[var5];
            var1.notifyNeighborsOfStateChange(var2.offset(var4), this);
         }

         this.updateSurroundingRedstone(var1, var2, var3);
         Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();

         Object var8;
         while(var9.hasNext()) {
            var8 = var9.next();
            this.notifyWireNeighborsOfStateChange(var1, var2.offset((EnumFacing)var8));
         }

         var9 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var9.hasNext()) {
            var8 = var9.next();
            BlockPos var10 = var2.offset((EnumFacing)var8);
            if (var1.getBlockState(var10).getBlock().isNormalCube()) {
               this.notifyWireNeighborsOfStateChange(var1, var10.up());
            } else {
               this.notifyWireNeighborsOfStateChange(var1, var10.down());
            }
         }
      }

   }

   private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      BlockPos var4 = var2.offset(var3);
      Block var5 = var1.getBlockState(var2.offset(var3)).getBlock();
      if (var1.getBlockState(var4) != var3 || !var5.isBlockNormalCube() && canConnectUpwardsTo(var1.getBlockState(var4.down()))) {
         return BlockRedstoneWire.EnumAttachPosition.SIDE;
      } else {
         Block var6 = var1.getBlockState(var2.up()).getBlock();
         return !var6.isBlockNormalCube() && var5.isBlockNormalCube() && canConnectUpwardsTo(var1.getBlockState(var4.up())) ? BlockRedstoneWire.EnumAttachPosition.UP : BlockRedstoneWire.EnumAttachPosition.NONE;
      }
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, var1);
   }

   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      var1 = var1.withProperty(WEST, this.getAttachPosition(var2, var3, EnumFacing.WEST));
      var1 = var1.withProperty(EAST, this.getAttachPosition(var2, var3, EnumFacing.EAST));
      var1 = var1.withProperty(NORTH, this.getAttachPosition(var2, var3, EnumFacing.NORTH));
      var1 = var1.withProperty(SOUTH, this.getAttachPosition(var2, var3, EnumFacing.SOUTH));
      return var1;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean isFullCube() {
      return false;
   }

   static enum EnumAttachPosition implements IStringSerializable {
      private final String name;
      private static final BlockRedstoneWire.EnumAttachPosition[] ENUM$VALUES = new BlockRedstoneWire.EnumAttachPosition[]{UP, SIDE, NONE};
      SIDE("side"),
      NONE("none"),
      UP("up");

      private EnumAttachPosition(String var3) {
         this.name = var3;
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this.name;
      }
   }
}
