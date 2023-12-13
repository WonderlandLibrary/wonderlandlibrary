package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;

public class BiomeCache {
   private LongHashMap cacheMap = new LongHashMap();
   private List cache = Lists.newArrayList();
   private final WorldChunkManager chunkManager;
   private long lastCleanupTime;

   public BiomeGenBase[] getCachedBiomes(int var1, int var2) {
      return this.getBiomeCacheBlock(var1, var2).biomes;
   }

   public BiomeGenBase func_180284_a(int var1, int var2, BiomeGenBase var3) {
      BiomeGenBase var4 = this.getBiomeCacheBlock(var1, var2).getBiomeGenAt(var1, var2);
      return var4 == null ? var3 : var4;
   }

   static WorldChunkManager access$0(BiomeCache var0) {
      return var0.chunkManager;
   }

   public BiomeCache(WorldChunkManager var1) {
      this.chunkManager = var1;
   }

   public BiomeCache.Block getBiomeCacheBlock(int var1, int var2) {
      var1 >>= 4;
      var2 >>= 4;
      long var3 = (long)var1 & 4294967295L | ((long)var2 & 4294967295L) << 32;
      BiomeCache.Block var5 = (BiomeCache.Block)this.cacheMap.getValueByKey(var3);
      if (var5 == null) {
         var5 = new BiomeCache.Block(this, var1, var2);
         this.cacheMap.add(var3, var5);
         this.cache.add(var5);
      }

      var5.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
      return var5;
   }

   public void cleanupCache() {
      long var1 = MinecraftServer.getCurrentTimeMillis();
      long var3 = var1 - this.lastCleanupTime;
      if (var3 > 7500L || var3 < 0L) {
         this.lastCleanupTime = var1;

         for(int var5 = 0; var5 < this.cache.size(); ++var5) {
            BiomeCache.Block var6 = (BiomeCache.Block)this.cache.get(var5);
            long var7 = var1 - var6.lastAccessTime;
            if (var7 > 30000L || var7 < 0L) {
               this.cache.remove(var5--);
               long var9 = (long)var6.xPosition & 4294967295L | ((long)var6.zPosition & 4294967295L) << 32;
               this.cacheMap.remove(var9);
            }
         }
      }

   }

   public class Block {
      public long lastAccessTime;
      public int xPosition;
      public BiomeGenBase[] biomes;
      final BiomeCache this$0;
      public int zPosition;
      public float[] rainfallValues;

      public BiomeGenBase getBiomeGenAt(int var1, int var2) {
         return this.biomes[var1 & 15 | (var2 & 15) << 4];
      }

      public Block(BiomeCache var1, int var2, int var3) {
         this.this$0 = var1;
         this.rainfallValues = new float[256];
         this.biomes = new BiomeGenBase[256];
         this.xPosition = var2;
         this.zPosition = var3;
         BiomeCache.access$0(var1).getRainfall(this.rainfallValues, var2 << 4, var3 << 4, 16, 16);
         BiomeCache.access$0(var1).getBiomeGenAt(this.biomes, var2 << 4, var3 << 4, 16, 16, false);
      }
   }
}
