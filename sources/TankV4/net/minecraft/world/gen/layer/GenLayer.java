package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

public abstract class GenLayer {
   protected GenLayer parent;
   protected long baseSeed;
   private long worldGenSeed;
   private long chunkSeed;

   protected int selectRandom(int... var1) {
      return var1[this.nextInt(var1.length)];
   }

   public GenLayer(long var1) {
      this.baseSeed = var1;
      this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
      this.baseSeed += var1;
      this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
      this.baseSeed += var1;
      this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
      this.baseSeed += var1;
   }

   protected int selectModeOrRandom(int var1, int var2, int var3, int var4) {
      return var2 == var3 && var3 == var4 ? var2 : (var1 == var2 && var1 == var3 ? var1 : (var1 == var2 && var1 == var4 ? var1 : (var1 == var3 && var1 == var4 ? var1 : (var1 == var2 && var3 != var4 ? var1 : (var1 == var3 && var2 != var4 ? var1 : (var1 == var4 && var2 != var3 ? var1 : (var2 == var3 && var1 != var4 ? var2 : (var2 == var4 && var1 != var3 ? var2 : (var3 == var4 && var1 != var2 ? var3 : this.selectRandom(var1, var2, var3, var4))))))))));
   }

   protected static boolean isBiomeOceanic(int var0) {
      return var0 == BiomeGenBase.ocean.biomeID || var0 == BiomeGenBase.deepOcean.biomeID || var0 == BiomeGenBase.frozenOcean.biomeID;
   }

   protected int nextInt(int var1) {
      int var2 = (int)((this.chunkSeed >> 24) % (long)var1);
      if (var2 < 0) {
         var2 += var1;
      }

      this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
      this.chunkSeed += this.worldGenSeed;
      return var2;
   }

   protected static boolean biomesEqualOrMesaPlateau(int var0, int var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != BiomeGenBase.mesaPlateau_F.biomeID && var0 != BiomeGenBase.mesaPlateau.biomeID) {
         BiomeGenBase var2 = BiomeGenBase.getBiome(var0);
         BiomeGenBase var3 = BiomeGenBase.getBiome(var1);

         try {
            return var2 != null && var3 != null ? var2.isEqualTo(var3) : false;
         } catch (Throwable var8) {
            CrashReport var5 = CrashReport.makeCrashReport(var8, "Comparing biomes");
            CrashReportCategory var6 = var5.makeCategory("Biomes being compared");
            var6.addCrashSection("Biome A ID", var0);
            var6.addCrashSection("Biome B ID", var1);
            var6.addCrashSectionCallable("Biome A", new Callable(var2) {
               private final BiomeGenBase val$biomegenbase;

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  return String.valueOf(this.val$biomegenbase);
               }

               {
                  this.val$biomegenbase = var1;
               }
            });
            var6.addCrashSectionCallable("Biome B", new Callable(var3) {
               private final BiomeGenBase val$biomegenbase1;

               {
                  this.val$biomegenbase1 = var1;
               }

               public String call() throws Exception {
                  return String.valueOf(this.val$biomegenbase1);
               }

               public Object call() throws Exception {
                  return this.call();
               }
            });
            throw new ReportedException(var5);
         }
      } else {
         return var1 == BiomeGenBase.mesaPlateau_F.biomeID || var1 == BiomeGenBase.mesaPlateau.biomeID;
      }
   }

   public void initWorldGenSeed(long var1) {
      this.worldGenSeed = var1;
      if (this.parent != null) {
         this.parent.initWorldGenSeed(var1);
      }

      this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
      this.worldGenSeed += this.baseSeed;
      this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
      this.worldGenSeed += this.baseSeed;
      this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
      this.worldGenSeed += this.baseSeed;
   }

   public abstract int[] getInts(int var1, int var2, int var3, int var4);

   public static GenLayer[] initializeAllBiomeGenerators(long var0, WorldType var2, String var3) {
      GenLayerIsland var4 = new GenLayerIsland(1L);
      GenLayerFuzzyZoom var34 = new GenLayerFuzzyZoom(2000L, var4);
      GenLayerAddIsland var5 = new GenLayerAddIsland(1L, var34);
      GenLayerZoom var6 = new GenLayerZoom(2001L, var5);
      GenLayerAddIsland var7 = new GenLayerAddIsland(2L, var6);
      var7 = new GenLayerAddIsland(50L, var7);
      var7 = new GenLayerAddIsland(70L, var7);
      GenLayerRemoveTooMuchOcean var8 = new GenLayerRemoveTooMuchOcean(2L, var7);
      GenLayerAddSnow var9 = new GenLayerAddSnow(2L, var8);
      GenLayerAddIsland var10 = new GenLayerAddIsland(3L, var9);
      GenLayerEdge var11 = new GenLayerEdge(2L, var10, GenLayerEdge.Mode.COOL_WARM);
      var11 = new GenLayerEdge(2L, var11, GenLayerEdge.Mode.HEAT_ICE);
      var11 = new GenLayerEdge(3L, var11, GenLayerEdge.Mode.SPECIAL);
      GenLayerZoom var12 = new GenLayerZoom(2002L, var11);
      var12 = new GenLayerZoom(2003L, var12);
      GenLayerAddIsland var13 = new GenLayerAddIsland(4L, var12);
      GenLayerAddMushroomIsland var14 = new GenLayerAddMushroomIsland(5L, var13);
      GenLayerDeepOcean var15 = new GenLayerDeepOcean(4L, var14);
      GenLayer var16 = GenLayerZoom.magnify(1000L, var15, 0);
      ChunkProviderSettings var17 = null;
      int var18 = 4;
      int var19 = var18;
      if (var2 == WorldType.CUSTOMIZED && var3.length() > 0) {
         var17 = ChunkProviderSettings.Factory.jsonToFactory(var3).func_177864_b();
         var18 = var17.biomeSize;
         var19 = var17.riverSize;
      }

      if (var2 == WorldType.LARGE_BIOMES) {
         var18 = 6;
      }

      GenLayer var20 = GenLayerZoom.magnify(1000L, var16, 0);
      GenLayerRiverInit var21 = new GenLayerRiverInit(100L, var20);
      GenLayerBiome var22 = new GenLayerBiome(200L, var16, var2, var3);
      GenLayer var23 = GenLayerZoom.magnify(1000L, var22, 2);
      GenLayerBiomeEdge var24 = new GenLayerBiomeEdge(1000L, var23);
      GenLayer var25 = GenLayerZoom.magnify(1000L, var21, 2);
      GenLayerHills var26 = new GenLayerHills(1000L, var24, var25);
      GenLayer var27 = GenLayerZoom.magnify(1000L, var21, 2);
      var27 = GenLayerZoom.magnify(1000L, var27, var19);
      GenLayerRiver var28 = new GenLayerRiver(1L, var27);
      GenLayerSmooth var29 = new GenLayerSmooth(1000L, var28);
      Object var35 = new GenLayerRareBiome(1001L, var26);

      for(int var30 = 0; var30 < var18; ++var30) {
         var35 = new GenLayerZoom((long)(1000 + var30), (GenLayer)var35);
         if (var30 == 0) {
            var35 = new GenLayerAddIsland(3L, (GenLayer)var35);
         }

         if (var30 == 1 || var18 == 1) {
            var35 = new GenLayerShore(1000L, (GenLayer)var35);
         }
      }

      GenLayerSmooth var36 = new GenLayerSmooth(1000L, (GenLayer)var35);
      GenLayerRiverMix var31 = new GenLayerRiverMix(100L, var36, var29);
      GenLayerVoronoiZoom var32 = new GenLayerVoronoiZoom(10L, var31);
      var31.initWorldGenSeed(var0);
      var32.initWorldGenSeed(var0);
      return new GenLayer[]{var31, var32, var31};
   }

   public void initChunkSeed(long var1, long var3) {
      this.chunkSeed = this.worldGenSeed;
      this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
      this.chunkSeed += var1;
      this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
      this.chunkSeed += var3;
      this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
      this.chunkSeed += var1;
      this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
      this.chunkSeed += var3;
   }
}
