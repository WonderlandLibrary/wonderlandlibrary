// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

public class BiomeCache
{
    private final BiomeProvider provider;
    private long lastCleanupTime;
    private final Long2ObjectMap<Block> cacheMap;
    private final List<Block> cache;
    
    public BiomeCache(final BiomeProvider provider) {
        this.cacheMap = (Long2ObjectMap<Block>)new Long2ObjectOpenHashMap(4096);
        this.cache = (List<Block>)Lists.newArrayList();
        this.provider = provider;
    }
    
    public Block getEntry(int x, int z) {
        x >>= 4;
        z >>= 4;
        final long i = ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
        Block biomecache$block = (Block)this.cacheMap.get(i);
        if (biomecache$block == null) {
            biomecache$block = new Block(x, z);
            this.cacheMap.put(i, (Object)biomecache$block);
            this.cache.add(biomecache$block);
        }
        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }
    
    public Biome getBiome(final int x, final int z, final Biome defaultValue) {
        final Biome biome = this.getEntry(x, z).getBiome(x, z);
        return (biome == null) ? defaultValue : biome;
    }
    
    public void cleanupCache() {
        final long i = MinecraftServer.getCurrentTimeMillis();
        final long j = i - this.lastCleanupTime;
        if (j > 7500L || j < 0L) {
            this.lastCleanupTime = i;
            for (int k = 0; k < this.cache.size(); ++k) {
                final Block biomecache$block = this.cache.get(k);
                final long l = i - biomecache$block.lastAccessTime;
                if (l > 30000L || l < 0L) {
                    this.cache.remove(k--);
                    final long i2 = ((long)biomecache$block.x & 0xFFFFFFFFL) | ((long)biomecache$block.z & 0xFFFFFFFFL) << 32;
                    this.cacheMap.remove(i2);
                }
            }
        }
    }
    
    public Biome[] getCachedBiomes(final int x, final int z) {
        return this.getEntry(x, z).biomes;
    }
    
    public class Block
    {
        public Biome[] biomes;
        public int x;
        public int z;
        public long lastAccessTime;
        
        public Block(final int x, final int z) {
            this.biomes = new Biome[256];
            this.x = x;
            this.z = z;
            BiomeCache.this.provider.getBiomes(this.biomes, x << 4, z << 4, 16, 16, false);
        }
        
        public Biome getBiome(final int x, final int z) {
            return this.biomes[(x & 0xF) | (z & 0xF) << 4];
        }
    }
}
