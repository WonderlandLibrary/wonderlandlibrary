// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.LongHashMap;

public class BiomeCache
{
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap cacheMap;
    private List cache;
    private static final String __OBFID = "CL_00000162";
    
    public BiomeCache(final WorldChunkManager p_i1973_1_) {
        this.cacheMap = new LongHashMap();
        this.cache = Lists.newArrayList();
        this.chunkManager = p_i1973_1_;
    }
    
    public Block getBiomeCacheBlock(int p_76840_1_, int p_76840_2_) {
        p_76840_1_ >>= 4;
        p_76840_2_ >>= 4;
        final long var3 = (p_76840_1_ & 0xFFFFFFFFL) | (p_76840_2_ & 0xFFFFFFFFL) << 32;
        Block var4 = (Block)this.cacheMap.getValueByKey(var3);
        if (var4 == null) {
            var4 = new Block(p_76840_1_, p_76840_2_);
            this.cacheMap.add(var3, var4);
            this.cache.add(var4);
        }
        var4.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return var4;
    }
    
    public BiomeGenBase func_180284_a(final int p_180284_1_, final int p_180284_2_, final BiomeGenBase p_180284_3_) {
        final BiomeGenBase var4 = this.getBiomeCacheBlock(p_180284_1_, p_180284_2_).getBiomeGenAt(p_180284_1_, p_180284_2_);
        return (var4 == null) ? p_180284_3_ : var4;
    }
    
    public void cleanupCache() {
        final long var1 = MinecraftServer.getCurrentTimeMillis();
        final long var2 = var1 - this.lastCleanupTime;
        if (var2 > 7500L || var2 < 0L) {
            this.lastCleanupTime = var1;
            for (int var3 = 0; var3 < this.cache.size(); ++var3) {
                final Block var4 = this.cache.get(var3);
                final long var5 = var1 - var4.lastAccessTime;
                if (var5 > 30000L || var5 < 0L) {
                    this.cache.remove(var3--);
                    final long var6 = (var4.xPosition & 0xFFFFFFFFL) | (var4.zPosition & 0xFFFFFFFFL) << 32;
                    this.cacheMap.remove(var6);
                }
            }
        }
    }
    
    public BiomeGenBase[] getCachedBiomes(final int p_76839_1_, final int p_76839_2_) {
        return this.getBiomeCacheBlock(p_76839_1_, p_76839_2_).biomes;
    }
    
    public class Block
    {
        public float[] rainfallValues;
        public BiomeGenBase[] biomes;
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;
        private static final String __OBFID = "CL_00000163";
        
        public Block(final int p_i1972_2_, final int p_i1972_3_) {
            this.rainfallValues = new float[256];
            this.biomes = new BiomeGenBase[256];
            this.xPosition = p_i1972_2_;
            this.zPosition = p_i1972_3_;
            BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16);
            BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16, false);
        }
        
        public BiomeGenBase getBiomeGenAt(final int p_76885_1_, final int p_76885_2_) {
            return this.biomes[(p_76885_1_ & 0xF) | (p_76885_2_ & 0xF) << 4];
        }
    }
}
