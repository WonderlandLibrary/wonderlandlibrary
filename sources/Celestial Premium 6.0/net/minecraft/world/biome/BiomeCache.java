/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class BiomeCache {
    private final BiomeProvider chunkManager;
    private long lastCleanupTime;
    private final Long2ObjectMap<Block> cacheMap = new Long2ObjectOpenHashMap(4096);
    private final List<Block> cache = Lists.newArrayList();

    public BiomeCache(BiomeProvider chunkManagerIn) {
        this.chunkManager = chunkManagerIn;
    }

    public Block getBiomeCacheBlock(int x, int z) {
        long i = (long)(x >>= 4) & 0xFFFFFFFFL | ((long)(z >>= 4) & 0xFFFFFFFFL) << 32;
        Block biomecache$block = (Block)this.cacheMap.get(i);
        if (biomecache$block == null) {
            biomecache$block = new Block(x, z);
            this.cacheMap.put(i, (Object)biomecache$block);
            this.cache.add(biomecache$block);
        }
        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }

    public Biome getBiome(int x, int z, Biome defaultValue) {
        Biome biome = this.getBiomeCacheBlock(x, z).getBiome(x, z);
        return biome == null ? defaultValue : biome;
    }

    public void cleanupCache() {
        long i = MinecraftServer.getCurrentTimeMillis();
        long j = i - this.lastCleanupTime;
        if (j > 7500L || j < 0L) {
            this.lastCleanupTime = i;
            for (int k = 0; k < this.cache.size(); ++k) {
                Block biomecache$block = this.cache.get(k);
                long l = i - biomecache$block.lastAccessTime;
                if (l <= 30000L && l >= 0L) continue;
                this.cache.remove(k--);
                long i1 = (long)biomecache$block.xPosition & 0xFFFFFFFFL | ((long)biomecache$block.zPosition & 0xFFFFFFFFL) << 32;
                this.cacheMap.remove(i1);
            }
        }
    }

    public Biome[] getCachedBiomes(int x, int z) {
        return this.getBiomeCacheBlock((int)x, (int)z).biomes;
    }

    public class Block {
        public Biome[] biomes = new Biome[256];
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;

        public Block(int x, int z) {
            this.xPosition = x;
            this.zPosition = z;
            BiomeCache.this.chunkManager.getBiomes(this.biomes, x << 4, z << 4, 16, 16, false);
        }

        public Biome getBiome(int x, int z) {
            return this.biomes[x & 0xF | (z & 0xF) << 4];
        }
    }
}

