/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiverMix
extends GenLayer {
    private GenLayer riverPatternGeneratorChain;
    private GenLayer biomePatternGeneratorChain;

    @Override
    public void initWorldGenSeed(long l) {
        this.biomePatternGeneratorChain.initWorldGenSeed(l);
        this.riverPatternGeneratorChain.initWorldGenSeed(l);
        super.initWorldGenSeed(l);
    }

    public GenLayerRiverMix(long l, GenLayer genLayer, GenLayer genLayer2) {
        super(l);
        this.biomePatternGeneratorChain = genLayer;
        this.riverPatternGeneratorChain = genLayer2;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.biomePatternGeneratorChain.getInts(n, n2, n3, n4);
        int[] nArray2 = this.riverPatternGeneratorChain.getInts(n, n2, n3, n4);
        int[] nArray3 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n3 * n4) {
            nArray3[n5] = nArray[n5] != BiomeGenBase.ocean.biomeID && nArray[n5] != BiomeGenBase.deepOcean.biomeID ? (nArray2[n5] == BiomeGenBase.river.biomeID ? (nArray[n5] == BiomeGenBase.icePlains.biomeID ? BiomeGenBase.frozenRiver.biomeID : (nArray[n5] != BiomeGenBase.mushroomIsland.biomeID && nArray[n5] != BiomeGenBase.mushroomIslandShore.biomeID ? nArray2[n5] & 0xFF : BiomeGenBase.mushroomIslandShore.biomeID)) : nArray[n5]) : nArray[n5];
            ++n5;
        }
        return nArray3;
    }
}

