package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStaticLiquid extends BlockLiquid {
   private void updateLiquid(World var1, BlockPos var2, IBlockState var3) {
      BlockDynamicLiquid var4 = getFlowingBlock(this.blockMaterial);
      var1.setBlockState(var2, var4.getDefaultState().withProperty(LEVEL, (Integer)var3.getValue(LEVEL)), 2);
      var1.scheduleUpdate(var2, var4, this.tickRate(var1));
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!this.checkForMixing(var1, var2, var3)) {
         this.updateLiquid(var1, var2, var3);
      }

   }

   protected BlockStaticLiquid(Material var1) {
      super(var1);
      this.setTickRandomly(false);
      if (var1 == Material.lava) {
         this.setTickRandomly(true);
      }

   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.blockMaterial == Material.lava && var1.getGameRules().getBoolean("doFireTick")) {
         int var5 = var4.nextInt(3);
         if (var5 > 0) {
            BlockPos var6 = var2;

            for(int var7 = 0; var7 < var5; ++var7) {
               var6 = var6.add(var4.nextInt(3) - 1, 1, var4.nextInt(3) - 1);
               Block var8 = var1.getBlockState(var6).getBlock();
               if (var8.blockMaterial == Material.air) {
                  if (var6 != false) {
                     var1.setBlockState(var6, Blocks.fire.getDefaultState());
                     return;
                  }
               } else if (var8.blockMaterial.blocksMovement()) {
                  return;
               }
            }
         } else {
            for(int var9 = 0; var9 < 3; ++var9) {
               BlockPos var10 = var2.add(var4.nextInt(3) - 1, 0, var4.nextInt(3) - 1);
               if (var1.isAirBlock(var10.up()) && this.getCanBlockBurn(var1, var10)) {
                  var1.setBlockState(var10.up(), Blocks.fire.getDefaultState());
               }
            }
         }
      }

   }

   private boolean getCanBlockBurn(World var1, BlockPos var2) {
      return var1.getBlockState(var2).getBlock().getMaterial().getCanBurn();
   }
}
