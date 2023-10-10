package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockDirectional {
   protected final boolean isRepeaterPowered;

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   protected boolean isPowered(IBlockState var1) {
      return this.isRepeaterPowered;
   }

   protected abstract IBlockState getPoweredState(IBlockState var1);

   public boolean isAssociatedBlock(Block var1) {
      return this.isAssociated(var1);
   }

   public boolean isFullCube() {
      return false;
   }

   protected abstract IBlockState getUnpoweredState(IBlockState var1);

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, var8.getHorizontalFacing().getOpposite());
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.notifyNeighbors(var1, var2, var3);
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return this.getWeakPower(var1, var2, var3, var4);
   }

   protected BlockRedstoneDiode(boolean var1) {
      super(Material.circuits);
      this.isRepeaterPowered = var1;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return !this.isPowered(var3) ? 0 : (var3.getValue(FACING) == var4 ? this.getActiveSignal(var1, var2, var3) : 0);
   }

   protected int calculateInputStrength(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
      BlockPos var5 = var2.offset(var4);
      int var6 = var1.getRedstonePower(var5, var4);
      if (var6 >= 15) {
         return var6;
      } else {
         IBlockState var7 = var1.getBlockState(var5);
         return Math.max(var6, var7.getBlock() == Blocks.redstone_wire ? (Integer)var7.getValue(BlockRedstoneWire.POWER) : 0);
      }
   }

   public boolean canBlockStay(World var1, BlockPos var2) {
      return World.doesBlockHaveSolidTopSurface(var1, var2.down());
   }

   public boolean isOpaqueCube() {
      return false;
   }

   protected void notifyNeighbors(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
      BlockPos var5 = var2.offset(var4.getOpposite());
      var1.notifyBlockOfStateChange(var5, this);
      var1.notifyNeighborsOfStateExcept(var5, this, var4);
   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return var3.getAxis() != EnumFacing.Axis.Y;
   }

   protected boolean canPowerSide(Block var1) {
      return var1.canProvidePower();
   }

   public boolean isAssociated(Block var1) {
      return var1 == this.getPoweredState(this.getDefaultState()).getBlock() || var1 == this.getUnpoweredState(this.getDefaultState()).getBlock();
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return World.doesBlockHaveSolidTopSurface(var1, var2.down()) ? super.canPlaceBlockAt(var1, var2) : false;
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      if (var3 > 0) {
         var1.scheduleUpdate(var2, this, 1);
      }

   }

   protected int getTickDelay(IBlockState var1) {
      return this.getDelay(var1);
   }

   protected void updateState(World var1, BlockPos var2, IBlockState var3) {
      if (!this.isLocked(var1, var2, var3)) {
         boolean var4 = this.shouldBePowered(var1, var2, var3);
         if ((this.isRepeaterPowered && !var4 || !this.isRepeaterPowered && var4) && !var1.isBlockTickPending(var2, this)) {
            byte var5 = -1;
            if (var3 == false) {
               var5 = -3;
            } else if (this.isRepeaterPowered) {
               var5 = -2;
            }

            var1.updateBlockTick(var2, this, this.getDelay(var3), var5);
         }
      }

   }

   protected int getPowerOnSides(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = (EnumFacing)var3.getValue(FACING);
      EnumFacing var5 = var4.rotateY();
      EnumFacing var6 = var4.rotateYCCW();
      return Math.max(this.getPowerOnSide(var1, var2.offset(var5), var5), this.getPowerOnSide(var1, var2.offset(var6), var6));
   }

   protected int getPowerOnSide(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState var4 = var1.getBlockState(var2);
      Block var5 = var4.getBlock();
      return this.canPowerSide(var5) ? (var5 == Blocks.redstone_wire ? (Integer)var4.getValue(BlockRedstoneWire.POWER) : var1.getStrongPower(var2, var3)) : 0;
   }

   public void onBlockDestroyedByPlayer(World var1, BlockPos var2, IBlockState var3) {
      if (this.isRepeaterPowered) {
         EnumFacing[] var7;
         int var6 = (var7 = EnumFacing.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            EnumFacing var4 = var7[var5];
            var1.notifyNeighborsOfStateChange(var2.offset(var4), this);
         }
      }

      super.onBlockDestroyedByPlayer(var1, var2, var3);
   }

   protected abstract int getDelay(IBlockState var1);

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!this.isLocked(var1, var2, var3)) {
         boolean var5 = this.shouldBePowered(var1, var2, var3);
         if (this.isRepeaterPowered && !var5) {
            var1.setBlockState(var2, this.getUnpoweredState(var3), 2);
         } else if (!this.isRepeaterPowered) {
            var1.setBlockState(var2, this.getPoweredState(var3), 2);
            if (!var5) {
               var1.updateBlockTick(var2, this.getPoweredState(var3).getBlock(), this.getTickDelay(var3), -1);
            }
         }
      }

   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (this.canBlockStay(var1, var2)) {
         this.updateState(var1, var2, var3);
      } else {
         this.dropBlockAsItem(var1, var2, var3, 0);
         var1.setBlockToAir(var2);
         EnumFacing[] var8;
         int var7 = (var8 = EnumFacing.values()).length;

         for(int var6 = 0; var6 < var7; ++var6) {
            EnumFacing var5 = var8[var6];
            var1.notifyNeighborsOfStateChange(var2.offset(var5), this);
         }
      }

   }

   public boolean isLocked(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      return false;
   }

   protected int getActiveSignal(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      return 15;
   }

   public boolean canProvidePower() {
      return true;
   }
}
