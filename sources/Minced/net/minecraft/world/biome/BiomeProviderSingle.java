// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Random;
import java.util.List;
import javax.annotation.Nullable;
import java.util.Arrays;
import net.minecraft.util.math.BlockPos;

public class BiomeProviderSingle extends BiomeProvider
{
    private final Biome biome;
    
    public BiomeProviderSingle(final Biome biomeIn) {
        this.biome = biomeIn;
    }
    
    @Override
    public Biome getBiome(final BlockPos pos) {
        return this.biome;
    }
    
    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, final int x, final int z, final int width, final int height) {
        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }
        Arrays.fill(biomes, 0, width * height, this.biome);
        return biomes;
    }
    
    @Override
    public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, final int x, final int z, final int width, final int depth) {
        if (oldBiomeList == null || oldBiomeList.length < width * depth) {
            oldBiomeList = new Biome[width * depth];
        }
        Arrays.fill(oldBiomeList, 0, width * depth, this.biome);
        return oldBiomeList;
    }
    
    @Override
    public Biome[] getBiomes(@Nullable final Biome[] listToReuse, final int x, final int z, final int width, final int length, final boolean cacheFlag) {
        return this.getBiomes(listToReuse, x, z, width, length);
    }
    
    @Nullable
    @Override
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List<Biome> biomes, final Random random) {
        return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }
    
    @Override
    public boolean areBiomesViable(final int x, final int z, final int radius, final List<Biome> allowed) {
        return allowed.contains(this.biome);
    }
    
    @Override
    public boolean isFixedBiome() {
        return true;
    }
    
    @Override
    public Biome getFixedBiome() {
        return this.biome;
    }
}
