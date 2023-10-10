package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper {
   private static final BiomeColorHelper.ColorResolver field_180291_a = new BiomeColorHelper.ColorResolver() {
      public int getColorAtPos(BiomeGenBase var1, BlockPos var2) {
         return var1.getGrassColorAtPos(var2);
      }
   };
   private static final BiomeColorHelper.ColorResolver field_180290_c = new BiomeColorHelper.ColorResolver() {
      public int getColorAtPos(BiomeGenBase var1, BlockPos var2) {
         return var1.waterColorMultiplier;
      }
   };
   private static final BiomeColorHelper.ColorResolver field_180289_b = new BiomeColorHelper.ColorResolver() {
      public int getColorAtPos(BiomeGenBase var1, BlockPos var2) {
         return var1.getFoliageColorAtPos(var2);
      }
   };

   private static int func_180285_a(IBlockAccess var0, BlockPos var1, BiomeColorHelper.ColorResolver var2) {
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;

      int var8;
      for(Iterator var7 = BlockPos.getAllInBoxMutable(var1.add(-1, 0, -1), var1.add(1, 0, 1)).iterator(); var7.hasNext(); var5 += var8 & 255) {
         BlockPos.MutableBlockPos var6 = (BlockPos.MutableBlockPos)var7.next();
         var8 = var2.getColorAtPos(var0.getBiomeGenForCoords(var6), var6);
         var3 += (var8 & 16711680) >> 16;
         var4 += (var8 & '\uff00') >> 8;
      }

      return (var3 / 9 & 255) << 16 | (var4 / 9 & 255) << 8 | var5 / 9 & 255;
   }

   public static int getWaterColorAtPos(IBlockAccess var0, BlockPos var1) {
      return func_180285_a(var0, var1, field_180290_c);
   }

   public static int getGrassColorAtPos(IBlockAccess var0, BlockPos var1) {
      return func_180285_a(var0, var1, field_180291_a);
   }

   public static int getFoliageColorAtPos(IBlockAccess var0, BlockPos var1) {
      return func_180285_a(var0, var1, field_180289_b);
   }

   interface ColorResolver {
      int getColorAtPos(BiomeGenBase var1, BlockPos var2);
   }
}
