/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiomeEdge
extends GenLayer {
    private boolean replaceBiomeEdgeIfNecessary(int[] nArray, int[] nArray2, int n, int n2, int n3, int n4, int n5, int n6) {
        if (!GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n4, n5)) {
            return false;
        }
        int n7 = nArray[n + 1 + (n2 + 1 - 1) * (n3 + 2)];
        int n8 = nArray[n + 1 + 1 + (n2 + 1) * (n3 + 2)];
        int n9 = nArray[n + 1 - 1 + (n2 + 1) * (n3 + 2)];
        int n10 = nArray[n + 1 + (n2 + 1 + 1) * (n3 + 2)];
        nArray2[n + n2 * n3] = this.canBiomesBeNeighbors(n7, n5) && this.canBiomesBeNeighbors(n8, n5) && this.canBiomesBeNeighbors(n9, n5) && this.canBiomesBeNeighbors(n10, n5) ? n4 : n6;
        return true;
    }

    private boolean replaceBiomeEdge(int[] nArray, int[] nArray2, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n4 != n5) {
            return false;
        }
        int n7 = nArray[n + 1 + (n2 + 1 - 1) * (n3 + 2)];
        int n8 = nArray[n + 1 + 1 + (n2 + 1) * (n3 + 2)];
        int n9 = nArray[n + 1 - 1 + (n2 + 1) * (n3 + 2)];
        int n10 = nArray[n + 1 + (n2 + 1 + 1) * (n3 + 2)];
        nArray2[n + n2 * n3] = GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n7, n5) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n8, n5) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n9, n5) && GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n10, n5) ? n4 : n6;
        return true;
    }

    private boolean canBiomesBeNeighbors(int n, int n2) {
        if (GenLayerBiomeEdge.biomesEqualOrMesaPlateau(n, n2)) {
            return true;
        }
        BiomeGenBase biomeGenBase = BiomeGenBase.getBiome(n);
        BiomeGenBase biomeGenBase2 = BiomeGenBase.getBiome(n2);
        if (biomeGenBase != null && biomeGenBase2 != null) {
            BiomeGenBase.TempCategory tempCategory;
            BiomeGenBase.TempCategory tempCategory2 = biomeGenBase.getTempCategory();
            return tempCategory2 == (tempCategory = biomeGenBase2.getTempCategory()) || tempCategory2 == BiomeGenBase.TempCategory.MEDIUM || tempCategory == BiomeGenBase.TempCategory.MEDIUM;
        }
        return false;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n - 1, n2 - 1, n3 + 2, n4 + 2);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n6 + n, n5 + n2);
                int n7 = nArray[n6 + 1 + (n5 + 1) * (n3 + 2)];
                if (!(this.replaceBiomeEdgeIfNecessary(nArray, nArray2, n6, n5, n3, n7, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) || this.replaceBiomeEdge(nArray, nArray2, n6, n5, n3, n7, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) || this.replaceBiomeEdge(nArray, nArray2, n6, n5, n3, n7, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) || this.replaceBiomeEdge(nArray, nArray2, n6, n5, n3, n7, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID))) {
                    int n8;
                    int n9;
                    int n10;
                    int n11;
                    if (n7 == BiomeGenBase.desert.biomeID) {
                        n11 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                        n10 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                        n9 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                        n8 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                        nArray2[n6 + n5 * n3] = n11 != BiomeGenBase.icePlains.biomeID && n10 != BiomeGenBase.icePlains.biomeID && n9 != BiomeGenBase.icePlains.biomeID && n8 != BiomeGenBase.icePlains.biomeID ? n7 : BiomeGenBase.extremeHillsPlus.biomeID;
                    } else if (n7 == BiomeGenBase.swampland.biomeID) {
                        n11 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                        n10 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                        n9 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                        n8 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                        nArray2[n6 + n5 * n3] = n11 != BiomeGenBase.desert.biomeID && n10 != BiomeGenBase.desert.biomeID && n9 != BiomeGenBase.desert.biomeID && n8 != BiomeGenBase.desert.biomeID && n11 != BiomeGenBase.coldTaiga.biomeID && n10 != BiomeGenBase.coldTaiga.biomeID && n9 != BiomeGenBase.coldTaiga.biomeID && n8 != BiomeGenBase.coldTaiga.biomeID && n11 != BiomeGenBase.icePlains.biomeID && n10 != BiomeGenBase.icePlains.biomeID && n9 != BiomeGenBase.icePlains.biomeID && n8 != BiomeGenBase.icePlains.biomeID ? (n11 != BiomeGenBase.jungle.biomeID && n8 != BiomeGenBase.jungle.biomeID && n10 != BiomeGenBase.jungle.biomeID && n9 != BiomeGenBase.jungle.biomeID ? n7 : BiomeGenBase.jungleEdge.biomeID) : BiomeGenBase.plains.biomeID;
                    } else {
                        nArray2[n6 + n5 * n3] = n7;
                    }
                }
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }

    public GenLayerBiomeEdge(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }
}

