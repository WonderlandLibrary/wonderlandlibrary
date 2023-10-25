/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerHell
extends WorldChunkManager {
    private float rainfall;
    private BiomeGenBase biomeGenerator;

    @Override
    public boolean areBiomesViable(int n, int n2, int n3, List<BiomeGenBase> list) {
        return list.contains(this.biomeGenerator);
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4, boolean bl) {
        return this.loadBlockGeneratorData(biomeGenBaseArray, n, n2, n3, n4);
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4) {
        if (biomeGenBaseArray == null || biomeGenBaseArray.length < n3 * n4) {
            biomeGenBaseArray = new BiomeGenBase[n3 * n4];
        }
        Arrays.fill(biomeGenBaseArray, 0, n3 * n4, this.biomeGenerator);
        return biomeGenBaseArray;
    }

    @Override
    public BlockPos findBiomePosition(int n, int n2, int n3, List<BiomeGenBase> list, Random random) {
        return list.contains(this.biomeGenerator) ? new BlockPos(n - n3 + random.nextInt(n3 * 2 + 1), 0, n2 - n3 + random.nextInt(n3 * 2 + 1)) : null;
    }

    @Override
    public float[] getRainfall(float[] fArray, int n, int n2, int n3, int n4) {
        if (fArray == null || fArray.length < n3 * n4) {
            fArray = new float[n3 * n4];
        }
        Arrays.fill(fArray, 0, n3 * n4, this.rainfall);
        return fArray;
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4) {
        if (biomeGenBaseArray == null || biomeGenBaseArray.length < n3 * n4) {
            biomeGenBaseArray = new BiomeGenBase[n3 * n4];
        }
        Arrays.fill(biomeGenBaseArray, 0, n3 * n4, this.biomeGenerator);
        return biomeGenBaseArray;
    }

    public WorldChunkManagerHell(BiomeGenBase biomeGenBase, float f) {
        this.biomeGenerator = biomeGenBase;
        this.rainfall = f;
    }

    @Override
    public BiomeGenBase getBiomeGenerator(BlockPos blockPos) {
        return this.biomeGenerator;
    }
}

