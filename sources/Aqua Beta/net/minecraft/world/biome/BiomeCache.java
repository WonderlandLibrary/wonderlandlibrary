package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class BiomeCache {
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap<Block> cacheMap = new LongHashMap();
    private List<Block> cache = Lists.newArrayList();

    public BiomeCache(WorldChunkManager chunkManagerIn) {
        this.chunkManager = chunkManagerIn;
    }

    public Block getBiomeCacheBlock(int x, int z) {
        long i = (long)(x >>= 4) & 0xFFFFFFFFL | ((long)(z >>= 4) & 0xFFFFFFFFL) << 32;
        Block biomecache$block = (Block)this.cacheMap.getValueByKey(i);
        if (biomecache$block == null) {
            biomecache$block = new Block(this, x, z);
            this.cacheMap.add(i, (Object)biomecache$block);
            this.cache.add((Object)biomecache$block);
        }
        biomecache$block.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return biomecache$block;
    }

    public BiomeGenBase func_180284_a(int x, int z, BiomeGenBase p_180284_3_) {
        BiomeGenBase biomegenbase = this.getBiomeCacheBlock(x, z).getBiomeGenAt(x, z);
        return biomegenbase == null ? p_180284_3_ : biomegenbase;
    }

    public void cleanupCache() {
        long i = MinecraftServer.getCurrentTimeMillis();
        long j = i - this.lastCleanupTime;
        if (j > 7500L || j < 0L) {
            this.lastCleanupTime = i;
            for (int k = 0; k < this.cache.size(); ++k) {
                Block biomecache$block = (Block)this.cache.get(k);
                long l = i - biomecache$block.lastAccessTime;
                if (l <= 30000L && l >= 0L) continue;
                this.cache.remove(k--);
                long i1 = (long)biomecache$block.xPosition & 0xFFFFFFFFL | ((long)biomecache$block.zPosition & 0xFFFFFFFFL) << 32;
                this.cacheMap.remove(i1);
            }
        }
    }

    public BiomeGenBase[] getCachedBiomes(int x, int z) {
        return this.getBiomeCacheBlock((int)x, (int)z).biomes;
    }

    static /* synthetic */ WorldChunkManager access$000(BiomeCache x0) {
        return x0.chunkManager;
    }
}
