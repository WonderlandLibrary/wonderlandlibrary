package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees {
   private void func_175930_c(World var1, BlockPos var2, int var3) {
      byte var4 = 2;

      for(int var5 = -var4; var5 <= 0; ++var5) {
         this.func_175925_a(var1, var2.up(var5), var3 + 1 - var5);
      }

   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = this.func_150533_a(var2);
      if (!this.func_175929_a(var1, var2, var3, var4)) {
         return false;
      } else {
         this.func_175930_c(var1, var3.up(var4), 2);

         int var5;
         for(var5 = var3.getY() + var4 - 2 - var2.nextInt(4); var5 > var3.getY() + var4 / 2; var5 -= 2 + var2.nextInt(4)) {
            float var6 = var2.nextFloat() * 3.1415927F * 2.0F;
            int var7 = var3.getX() + (int)(0.5F + MathHelper.cos(var6) * 4.0F);
            int var8 = var3.getZ() + (int)(0.5F + MathHelper.sin(var6) * 4.0F);

            int var9;
            for(var9 = 0; var9 < 5; ++var9) {
               var7 = var3.getX() + (int)(1.5F + MathHelper.cos(var6) * (float)var9);
               var8 = var3.getZ() + (int)(1.5F + MathHelper.sin(var6) * (float)var9);
               this.setBlockAndNotifyAdequately(var1, new BlockPos(var7, var5 - 3 + var9 / 2, var8), this.woodMetadata);
            }

            var9 = 1 + var2.nextInt(2);
            int var10 = var5;

            for(int var11 = var5 - var9; var11 <= var10; ++var11) {
               int var12 = var11 - var10;
               this.func_175928_b(var1, new BlockPos(var7, var11, var8), 1 - var12);
            }
         }

         for(var5 = 0; var5 < var4; ++var5) {
            BlockPos var13 = var3.up(var5);
            if (this.func_150523_a(var1.getBlockState(var13).getBlock())) {
               this.setBlockAndNotifyAdequately(var1, var13, this.woodMetadata);
               if (var5 > 0) {
                  this.func_181632_a(var1, var2, var13.west(), BlockVine.EAST);
                  this.func_181632_a(var1, var2, var13.north(), BlockVine.SOUTH);
               }
            }

            if (var5 < var4 - 1) {
               BlockPos var14 = var13.east();
               if (this.func_150523_a(var1.getBlockState(var14).getBlock())) {
                  this.setBlockAndNotifyAdequately(var1, var14, this.woodMetadata);
                  if (var5 > 0) {
                     this.func_181632_a(var1, var2, var14.east(), BlockVine.WEST);
                     this.func_181632_a(var1, var2, var14.north(), BlockVine.SOUTH);
                  }
               }

               BlockPos var15 = var13.south().east();
               if (this.func_150523_a(var1.getBlockState(var15).getBlock())) {
                  this.setBlockAndNotifyAdequately(var1, var15, this.woodMetadata);
                  if (var5 > 0) {
                     this.func_181632_a(var1, var2, var15.east(), BlockVine.WEST);
                     this.func_181632_a(var1, var2, var15.south(), BlockVine.NORTH);
                  }
               }

               BlockPos var16 = var13.south();
               if (this.func_150523_a(var1.getBlockState(var16).getBlock())) {
                  this.setBlockAndNotifyAdequately(var1, var16, this.woodMetadata);
                  if (var5 > 0) {
                     this.func_181632_a(var1, var2, var16.west(), BlockVine.EAST);
                     this.func_181632_a(var1, var2, var16.south(), BlockVine.NORTH);
                  }
               }
            }
         }

         return true;
      }
   }

   public WorldGenMegaJungle(boolean var1, int var2, int var3, IBlockState var4, IBlockState var5) {
      super(var1, var2, var3, var4, var5);
   }

   private void func_181632_a(World var1, Random var2, BlockPos var3, PropertyBool var4) {
      if (var2.nextInt(3) > 0 && var1.isAirBlock(var3)) {
         this.setBlockAndNotifyAdequately(var1, var3, Blocks.vine.getDefaultState().withProperty(var4, true));
      }

   }
}
