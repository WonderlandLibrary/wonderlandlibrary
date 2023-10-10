package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator {
   private Block baseBlockRequired;

   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (var1.isAirBlock(var3) && var1.getBlockState(var3.down()).getBlock() == this.baseBlockRequired) {
         int var4 = var2.nextInt(32) + 6;
         int var5 = var2.nextInt(4) + 1;
         BlockPos.MutableBlockPos var6 = new BlockPos.MutableBlockPos();

         int var7;
         int var8;
         int var9;
         int var10;
         for(var7 = var3.getX() - var5; var7 <= var3.getX() + var5; ++var7) {
            for(var8 = var3.getZ() - var5; var8 <= var3.getZ() + var5; ++var8) {
               var9 = var7 - var3.getX();
               var10 = var8 - var3.getZ();
               if (var9 * var9 + var10 * var10 <= var5 * var5 + 1 && var1.getBlockState(var6.func_181079_c(var7, var3.getY() - 1, var8)).getBlock() != this.baseBlockRequired) {
                  return false;
               }
            }
         }

         for(var7 = var3.getY(); var7 < var3.getY() + var4 && var7 < 256; ++var7) {
            for(var8 = var3.getX() - var5; var8 <= var3.getX() + var5; ++var8) {
               for(var9 = var3.getZ() - var5; var9 <= var3.getZ() + var5; ++var9) {
                  var10 = var8 - var3.getX();
                  int var11 = var9 - var3.getZ();
                  if (var10 * var10 + var11 * var11 <= var5 * var5 + 1) {
                     var1.setBlockState(new BlockPos(var8, var7, var9), Blocks.obsidian.getDefaultState(), 2);
                  }
               }
            }
         }

         EntityEnderCrystal var12 = new EntityEnderCrystal(var1);
         var12.setLocationAndAngles((double)((float)var3.getX() + 0.5F), (double)(var3.getY() + var4), (double)((float)var3.getZ() + 0.5F), var2.nextFloat() * 360.0F, 0.0F);
         var1.spawnEntityInWorld(var12);
         var1.setBlockState(var3.up(var4), Blocks.bedrock.getDefaultState(), 2);
         return true;
      } else {
         return false;
      }
   }

   public WorldGenSpikes(Block var1) {
      this.baseBlockRequired = var1;
   }
}
