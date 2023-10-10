package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManager {
   private GenLayer biomeIndexLayer;
   private BiomeCache biomeCache;
   private List biomesToSpawnIn;
   private GenLayer genBiomes;
   private String field_180301_f;

   public float[] getRainfall(float[] var1, int var2, int var3, int var4, int var5) {
      IntCache.resetIntCache();
      if (var1 == null || var1.length < var4 * var5) {
         var1 = new float[var4 * var5];
      }

      int[] var6 = this.biomeIndexLayer.getInts(var2, var3, var4, var5);

      for(int var7 = 0; var7 < var4 * var5; ++var7) {
         try {
            float var8 = (float)BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;
            if (var8 > 1.0F) {
               var8 = 1.0F;
            }

            var1[var7] = var8;
         } catch (Throwable var11) {
            CrashReport var9 = CrashReport.makeCrashReport(var11, "Invalid Biome id");
            CrashReportCategory var10 = var9.makeCategory("DownfallBlock");
            var10.addCrashSection("biome id", var7);
            var10.addCrashSection("downfalls[] size", var1.length);
            var10.addCrashSection("x", var2);
            var10.addCrashSection("z", var3);
            var10.addCrashSection("w", var4);
            var10.addCrashSection("h", var5);
            throw new ReportedException(var9);
         }
      }

      return var1;
   }

   public BlockPos findBiomePosition(int var1, int var2, int var3, List var4, Random var5) {
      IntCache.resetIntCache();
      int var6 = var1 - var3 >> 2;
      int var7 = var2 - var3 >> 2;
      int var8 = var1 + var3 >> 2;
      int var9 = var2 + var3 >> 2;
      int var10 = var8 - var6 + 1;
      int var11 = var9 - var7 + 1;
      int[] var12 = this.genBiomes.getInts(var6, var7, var10, var11);
      BlockPos var13 = null;
      int var14 = 0;

      for(int var15 = 0; var15 < var10 * var11; ++var15) {
         int var16 = var6 + var15 % var10 << 2;
         int var17 = var7 + var15 / var10 << 2;
         BiomeGenBase var18 = BiomeGenBase.getBiome(var12[var15]);
         if (var4.contains(var18) && (var13 == null || var5.nextInt(var14 + 1) == 0)) {
            var13 = new BlockPos(var16, 0, var17);
            ++var14;
         }
      }

      return var13;
   }

   public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] var1, int var2, int var3, int var4, int var5) {
      IntCache.resetIntCache();
      if (var1 == null || var1.length < var4 * var5) {
         var1 = new BiomeGenBase[var4 * var5];
      }

      int[] var6 = this.genBiomes.getInts(var2, var3, var4, var5);

      try {
         for(int var7 = 0; var7 < var4 * var5; ++var7) {
            var1[var7] = BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad);
         }

         return var1;
      } catch (Throwable var10) {
         CrashReport var8 = CrashReport.makeCrashReport(var10, "Invalid Biome id");
         CrashReportCategory var9 = var8.makeCategory("RawBiomeBlock");
         var9.addCrashSection("biomes[] size", var1.length);
         var9.addCrashSection("x", var2);
         var9.addCrashSection("z", var3);
         var9.addCrashSection("w", var4);
         var9.addCrashSection("h", var5);
         throw new ReportedException(var8);
      }
   }

   public List getBiomesToSpawnIn() {
      return this.biomesToSpawnIn;
   }

   public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] var1, int var2, int var3, int var4, int var5) {
      return this.getBiomeGenAt(var1, var2, var3, var4, var5, true);
   }

   public WorldChunkManager(long var1, WorldType var3, String var4) {
      this();
      this.field_180301_f = var4;
      GenLayer[] var5 = GenLayer.initializeAllBiomeGenerators(var1, var3, var4);
      this.genBiomes = var5[0];
      this.biomeIndexLayer = var5[1];
   }

   public BiomeGenBase getBiomeGenerator(BlockPos var1, BiomeGenBase var2) {
      return this.biomeCache.func_180284_a(var1.getX(), var1.getZ(), var2);
   }

   public BiomeGenBase getBiomeGenerator(BlockPos var1) {
      return this.getBiomeGenerator(var1, (BiomeGenBase)null);
   }

   public float getTemperatureAtHeight(float var1, int var2) {
      return var1;
   }

   public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] var1, int var2, int var3, int var4, int var5, boolean var6) {
      IntCache.resetIntCache();
      if (var1 == null || var1.length < var4 * var5) {
         var1 = new BiomeGenBase[var4 * var5];
      }

      if (var6 && var4 == 16 && var5 == 16 && (var2 & 15) == 0 && (var3 & 15) == 0) {
         BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(var2, var3);
         System.arraycopy(var9, 0, var1, 0, var4 * var5);
         return var1;
      } else {
         int[] var7 = this.biomeIndexLayer.getInts(var2, var3, var4, var5);

         for(int var8 = 0; var8 < var4 * var5; ++var8) {
            var1[var8] = BiomeGenBase.getBiomeFromBiomeList(var7[var8], BiomeGenBase.field_180279_ad);
         }

         return var1;
      }
   }

   public WorldChunkManager(World var1) {
      this(var1.getSeed(), var1.getWorldInfo().getTerrainType(), var1.getWorldInfo().getGeneratorOptions());
   }

   public void cleanupCache() {
      this.biomeCache.cleanupCache();
   }

   public boolean areBiomesViable(int var1, int var2, int var3, List var4) {
      IntCache.resetIntCache();
      int var5 = var1 - var3 >> 2;
      int var6 = var2 - var3 >> 2;
      int var7 = var1 + var3 >> 2;
      int var8 = var2 + var3 >> 2;
      int var9 = var7 - var5 + 1;
      int var10 = var8 - var6 + 1;
      int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

      try {
         for(int var12 = 0; var12 < var9 * var10; ++var12) {
            BiomeGenBase var16 = BiomeGenBase.getBiome(var11[var12]);
            if (!var4.contains(var16)) {
               return false;
            }
         }

         return true;
      } catch (Throwable var15) {
         CrashReport var13 = CrashReport.makeCrashReport(var15, "Invalid Biome id");
         CrashReportCategory var14 = var13.makeCategory("Layer");
         var14.addCrashSection("Layer", this.genBiomes.toString());
         var14.addCrashSection("x", var1);
         var14.addCrashSection("z", var2);
         var14.addCrashSection("radius", var3);
         var14.addCrashSection("allowed", var4);
         throw new ReportedException(var13);
      }
   }

   protected WorldChunkManager() {
      this.biomeCache = new BiomeCache(this);
      this.field_180301_f = "";
      this.biomesToSpawnIn = Lists.newArrayList();
      this.biomesToSpawnIn.add(BiomeGenBase.forest);
      this.biomesToSpawnIn.add(BiomeGenBase.plains);
      this.biomesToSpawnIn.add(BiomeGenBase.taiga);
      this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
      this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
      this.biomesToSpawnIn.add(BiomeGenBase.jungle);
      this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
   }
}
