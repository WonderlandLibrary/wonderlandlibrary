package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch {
   private static Map toggles = Maps.newHashMap();
   private final boolean isOn;

   public boolean canProvidePower() {
      return true;
   }

   public void randomDisplayTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.isOn) {
         double var5 = (double)var2.getX() + 0.5D + (var4.nextDouble() - 0.5D) * 0.2D;
         double var7 = (double)var2.getY() + 0.7D + (var4.nextDouble() - 0.5D) * 0.2D;
         double var9 = (double)var2.getZ() + 0.5D + (var4.nextDouble() - 0.5D) * 0.2D;
         EnumFacing var11 = (EnumFacing)var3.getValue(FACING);
         if (var11.getAxis().isHorizontal()) {
            EnumFacing var12 = var11.getOpposite();
            double var13 = 0.27D;
            var5 += 0.27D * (double)var12.getFrontOffsetX();
            var7 += 0.22D;
            var9 += 0.27D * (double)var12.getFrontOffsetZ();
         }

         var1.spawnParticle(EnumParticleTypes.REDSTONE, var5, var7, var9, 0.0D, 0.0D, 0.0D);
      }

   }

   public boolean isAssociatedBlock(Block var1) {
      return var1 == Blocks.unlit_redstone_torch || var1 == Blocks.redstone_torch;
   }

   public Item getItem(World var1, BlockPos var2) {
      return Item.getItemFromBlock(Blocks.redstone_torch);
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!this.onNeighborChangeInternal(var1, var2, var3) && this.isOn == this.shouldBeOff(var1, var2, var3)) {
         var1.scheduleUpdate(var2, this, this.tickRate(var1));
      }

   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      boolean var5 = this.shouldBeOff(var1, var2, var3);
      List var6 = (List)toggles.get(var1);

      while(var6 != null && !var6.isEmpty() && var1.getTotalWorldTime() - ((BlockRedstoneTorch.Toggle)var6.get(0)).time > 60L) {
         var6.remove(0);
      }

      if (this.isOn) {
         if (var5) {
            var1.setBlockState(var2, Blocks.unlit_redstone_torch.getDefaultState().withProperty(FACING, (EnumFacing)var3.getValue(FACING)), 3);
            var1.playSoundEffect((double)((float)var2.getX() + 0.5F), (double)((float)var2.getY() + 0.5F), (double)((float)var2.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (var1.rand.nextFloat() - var1.rand.nextFloat()) * 0.8F);

            for(int var7 = 0; var7 < 5; ++var7) {
               double var8 = (double)var2.getX() + var4.nextDouble() * 0.6D + 0.2D;
               double var10 = (double)var2.getY() + var4.nextDouble() * 0.6D + 0.2D;
               double var12 = (double)var2.getZ() + var4.nextDouble() * 0.6D + 0.2D;
               var1.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var8, var10, var12, 0.0D, 0.0D, 0.0D);
            }

            var1.scheduleUpdate(var2, var1.getBlockState(var2).getBlock(), 160);
         }
      } else if (!var5) {
         ;
      }

   }

   private boolean shouldBeOff(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing var4 = ((EnumFacing)var3.getValue(FACING)).getOpposite();
      return var1.isSidePowered(var2.offset(var4), var4);
   }

   public int getStrongPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return var4 == EnumFacing.DOWN ? this.getWeakPower(var1, var2, var3, var4) : 0;
   }

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   public int getWeakPower(IBlockAccess var1, BlockPos var2, IBlockState var3, EnumFacing var4) {
      return this.isOn && var3.getValue(FACING) != var4 ? 15 : 0;
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (this.isOn) {
         EnumFacing[] var7;
         int var6 = (var7 = EnumFacing.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            EnumFacing var4 = var7[var5];
            var1.notifyNeighborsOfStateChange(var2.offset(var4), this);
         }
      }

   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (this.isOn) {
         EnumFacing[] var7;
         int var6 = (var7 = EnumFacing.values()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            EnumFacing var4 = var7[var5];
            var1.notifyNeighborsOfStateChange(var2.offset(var4), this);
         }
      }

   }

   protected BlockRedstoneTorch(boolean var1) {
      this.isOn = var1;
      this.setTickRandomly(true);
      this.setCreativeTab((CreativeTabs)null);
   }

   public int tickRate(World var1) {
      return 2;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.redstone_torch);
   }

   static class Toggle {
      BlockPos pos;
      long time;

      public Toggle(BlockPos var1, long var2) {
         this.pos = var1;
         this.time = var2;
      }
   }
}
