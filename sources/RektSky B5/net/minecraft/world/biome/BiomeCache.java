/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class BiomeCache {
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap cacheMap = new LongHashMap();
    private List<Block> cache = Lists.newArrayList();

    public BiomeCache(WorldChunkManager chunkManagerIn) {
        this.chunkManager = chunkManagerIn;
    }

    public Block getBiomeCacheBlock(int x2, int z2) {
        long i2 = (long)(x2 >>= 4) & 0xFFFFFFFFL | ((long)(z2 >>= 4) & 0xFFFFFFFFL) << 32;
        Block biomecache$block = (Block)this.cacheMap.getValueByKey(i2);
        if (biomecache$block == null) {
            biomecache$block = new Block(x2, z2);
            this.cacheMap.add(i2, biomecache$block);
            this.cache.add(biomecache$block);
        }
        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }

    public BiomeGenBase func_180284_a(int x2, int z2, BiomeGenBase p_180284_3_) {
        BiomeGenBase biomegenbase = this.getBiomeCacheBlock(x2, z2).getBiomeGenAt(x2, z2);
        return biomegenbase == null ? p_180284_3_ : biomegenbase;
    }

    public void cleanupCache() {
        long i2 = MinecraftServer.getCurrentTimeMillis();
        long j2 = i2 - this.lastCleanupTime;
        if (j2 > 7500L || j2 < 0L) {
            this.lastCleanupTime = i2;
            for (int k2 = 0; k2 < this.cache.size(); ++k2) {
                Block biomecache$block = this.cache.get(k2);
                long l2 = i2 - biomecache$block.lastAccessTime;
                if (l2 <= 30000L && l2 >= 0L) continue;
                this.cache.remove(k2--);
                long i1 = (long)biomecache$block.xPosition & 0xFFFFFFFFL | ((long)biomecache$block.zPosition & 0xFFFFFFFFL) << 32;
                this.cacheMap.remove(i1);
            }
        }
    }

    public BiomeGenBase[] getCachedBiomes(int x2, int z2) {
        return this.getBiomeCacheBlock((int)x2, (int)z2).biomes;
    }

    public class Block {
        public float[] rainfallValues = new float[256];
        public BiomeGenBase[] biomes = new BiomeGenBase[256];
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;

        public Block(int x2, int z2) {
            this.xPosition = x2;
            this.zPosition = z2;
            BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, x2 << 4, z2 << 4, 16, 16);
            BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, x2 << 4, z2 << 4, 16, 16, false);
        }

        public BiomeGenBase getBiomeGenAt(int x2, int z2) {
            return this.biomes[x2 & 0xF | (z2 & 0xF) << 4];
        }
    }
}

