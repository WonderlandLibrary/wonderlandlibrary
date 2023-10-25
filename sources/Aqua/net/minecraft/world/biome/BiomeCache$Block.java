package net.minecraft.world.biome;

import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;

/*
 * Exception performing whole class analysis ignored.
 */
public class BiomeCache.Block {
    public float[] rainfallValues = new float[256];
    public BiomeGenBase[] biomes = new BiomeGenBase[256];
    public int xPosition;
    public int zPosition;
    public long lastAccessTime;

    public BiomeCache.Block(int x, int z) {
        this.xPosition = x;
        this.zPosition = z;
        BiomeCache.access$000((BiomeCache)BiomeCache.this).getRainfall(this.rainfallValues, x << 4, z << 4, 16, 16);
        BiomeCache.access$000((BiomeCache)BiomeCache.this).getBiomeGenAt(this.biomes, x << 4, z << 4, 16, 16, false);
    }

    public BiomeGenBase getBiomeGenAt(int x, int z) {
        return this.biomes[x & 0xF | (z & 0xF) << 4];
    }
}
