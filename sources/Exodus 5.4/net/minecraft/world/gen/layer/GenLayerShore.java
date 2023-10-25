/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerShore
extends GenLayer {
    public GenLayerShore(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }

    private boolean func_151631_c(int n) {
        return BiomeGenBase.getBiome(n) != null && BiomeGenBase.getBiome(n).getBiomeClass() == BiomeGenJungle.class ? true : n == BiomeGenBase.jungleEdge.biomeID || n == BiomeGenBase.jungle.biomeID || n == BiomeGenBase.jungleHills.biomeID || n == BiomeGenBase.forest.biomeID || n == BiomeGenBase.taiga.biomeID || GenLayerShore.isBiomeOceanic(n);
    }

    private void func_151632_a(int[] nArray, int[] nArray2, int n, int n2, int n3, int n4, int n5) {
        if (GenLayerShore.isBiomeOceanic(n4)) {
            nArray2[n + n2 * n3] = n4;
        } else {
            int n6 = nArray[n + 1 + (n2 + 1 - 1) * (n3 + 2)];
            int n7 = nArray[n + 1 + 1 + (n2 + 1) * (n3 + 2)];
            int n8 = nArray[n + 1 - 1 + (n2 + 1) * (n3 + 2)];
            int n9 = nArray[n + 1 + (n2 + 1 + 1) * (n3 + 2)];
            nArray2[n + n2 * n3] = !GenLayerShore.isBiomeOceanic(n6) && !GenLayerShore.isBiomeOceanic(n7) && !GenLayerShore.isBiomeOceanic(n8) && !GenLayerShore.isBiomeOceanic(n9) ? n4 : n5;
        }
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n - 1, n2 - 1, n3 + 2, n4 + 2);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                int n7;
                int n8;
                int n9;
                int n10;
                this.initChunkSeed(n6 + n, n5 + n2);
                int n11 = nArray[n6 + 1 + (n5 + 1) * (n3 + 2)];
                BiomeGenBase biomeGenBase = BiomeGenBase.getBiome(n11);
                if (n11 == BiomeGenBase.mushroomIsland.biomeID) {
                    n10 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                    n9 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                    n8 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                    n7 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                    nArray2[n6 + n5 * n3] = n10 != BiomeGenBase.ocean.biomeID && n9 != BiomeGenBase.ocean.biomeID && n8 != BiomeGenBase.ocean.biomeID && n7 != BiomeGenBase.ocean.biomeID ? n11 : BiomeGenBase.mushroomIslandShore.biomeID;
                } else if (biomeGenBase != null && biomeGenBase.getBiomeClass() == BiomeGenJungle.class) {
                    n10 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                    n9 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                    n8 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                    n7 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                    nArray2[n6 + n5 * n3] = this.func_151631_c(n10) && this.func_151631_c(n9) && this.func_151631_c(n8) && this.func_151631_c(n7) ? (!(GenLayerShore.isBiomeOceanic(n10) || GenLayerShore.isBiomeOceanic(n9) || GenLayerShore.isBiomeOceanic(n8) || GenLayerShore.isBiomeOceanic(n7)) ? n11 : BiomeGenBase.beach.biomeID) : BiomeGenBase.jungleEdge.biomeID;
                } else if (n11 != BiomeGenBase.extremeHills.biomeID && n11 != BiomeGenBase.extremeHillsPlus.biomeID && n11 != BiomeGenBase.extremeHillsEdge.biomeID) {
                    if (biomeGenBase != null && biomeGenBase.isSnowyBiome()) {
                        this.func_151632_a(nArray, nArray2, n6, n5, n3, n11, BiomeGenBase.coldBeach.biomeID);
                    } else if (n11 != BiomeGenBase.mesa.biomeID && n11 != BiomeGenBase.mesaPlateau_F.biomeID) {
                        if (n11 != BiomeGenBase.ocean.biomeID && n11 != BiomeGenBase.deepOcean.biomeID && n11 != BiomeGenBase.river.biomeID && n11 != BiomeGenBase.swampland.biomeID) {
                            n10 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                            n9 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                            n8 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                            n7 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                            nArray2[n6 + n5 * n3] = !(GenLayerShore.isBiomeOceanic(n10) || GenLayerShore.isBiomeOceanic(n9) || GenLayerShore.isBiomeOceanic(n8) || GenLayerShore.isBiomeOceanic(n7)) ? n11 : BiomeGenBase.beach.biomeID;
                        } else {
                            nArray2[n6 + n5 * n3] = n11;
                        }
                    } else {
                        n10 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                        n9 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                        n8 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                        n7 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                        nArray2[n6 + n5 * n3] = !(GenLayerShore.isBiomeOceanic(n10) || GenLayerShore.isBiomeOceanic(n9) || GenLayerShore.isBiomeOceanic(n8) || GenLayerShore.isBiomeOceanic(n7)) ? (this.func_151633_d(n10) && this.func_151633_d(n9) && this.func_151633_d(n8) && this.func_151633_d(n7) ? n11 : BiomeGenBase.desert.biomeID) : n11;
                    }
                } else {
                    this.func_151632_a(nArray, nArray2, n6, n5, n3, n11, BiomeGenBase.stoneBeach.biomeID);
                }
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }

    private boolean func_151633_d(int n) {
        return BiomeGenBase.getBiome(n) instanceof BiomeGenMesa;
    }
}

