/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills
extends GenLayer {
    private static final Logger logger = LogManager.getLogger();
    private GenLayer field_151628_d;

    public GenLayerHills(long l, GenLayer genLayer, GenLayer genLayer2) {
        super(l);
        this.parent = genLayer;
        this.field_151628_d = genLayer2;
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n - 1, n2 - 1, n3 + 2, n4 + 2);
        int[] nArray2 = this.field_151628_d.getInts(n - 1, n2 - 1, n3 + 2, n4 + 2);
        int[] nArray3 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                boolean bl;
                this.initChunkSeed(n6 + n, n5 + n2);
                int n7 = nArray[n6 + 1 + (n5 + 1) * (n3 + 2)];
                int n8 = nArray2[n6 + 1 + (n5 + 1) * (n3 + 2)];
                boolean bl2 = bl = (n8 - 2) % 29 == 0;
                if (n7 > 255) {
                    logger.debug("old! " + n7);
                }
                if (n7 != 0 && n8 >= 2 && (n8 - 2) % 29 == 1 && n7 < 128) {
                    nArray3[n6 + n5 * n3] = BiomeGenBase.getBiome(n7 + 128) != null ? n7 + 128 : n7;
                } else if (this.nextInt(3) != 0 && !bl) {
                    nArray3[n6 + n5 * n3] = n7;
                } else {
                    int n9;
                    int n10 = n7;
                    if (n7 == BiomeGenBase.desert.biomeID) {
                        n10 = BiomeGenBase.desertHills.biomeID;
                    } else if (n7 == BiomeGenBase.forest.biomeID) {
                        n10 = BiomeGenBase.forestHills.biomeID;
                    } else if (n7 == BiomeGenBase.birchForest.biomeID) {
                        n10 = BiomeGenBase.birchForestHills.biomeID;
                    } else if (n7 == BiomeGenBase.roofedForest.biomeID) {
                        n10 = BiomeGenBase.plains.biomeID;
                    } else if (n7 == BiomeGenBase.taiga.biomeID) {
                        n10 = BiomeGenBase.taigaHills.biomeID;
                    } else if (n7 == BiomeGenBase.megaTaiga.biomeID) {
                        n10 = BiomeGenBase.megaTaigaHills.biomeID;
                    } else if (n7 == BiomeGenBase.coldTaiga.biomeID) {
                        n10 = BiomeGenBase.coldTaigaHills.biomeID;
                    } else if (n7 == BiomeGenBase.plains.biomeID) {
                        n10 = this.nextInt(3) == 0 ? BiomeGenBase.forestHills.biomeID : BiomeGenBase.forest.biomeID;
                    } else if (n7 == BiomeGenBase.icePlains.biomeID) {
                        n10 = BiomeGenBase.iceMountains.biomeID;
                    } else if (n7 == BiomeGenBase.jungle.biomeID) {
                        n10 = BiomeGenBase.jungleHills.biomeID;
                    } else if (n7 == BiomeGenBase.ocean.biomeID) {
                        n10 = BiomeGenBase.deepOcean.biomeID;
                    } else if (n7 == BiomeGenBase.extremeHills.biomeID) {
                        n10 = BiomeGenBase.extremeHillsPlus.biomeID;
                    } else if (n7 == BiomeGenBase.savanna.biomeID) {
                        n10 = BiomeGenBase.savannaPlateau.biomeID;
                    } else if (GenLayerHills.biomesEqualOrMesaPlateau(n7, BiomeGenBase.mesaPlateau_F.biomeID)) {
                        n10 = BiomeGenBase.mesa.biomeID;
                    } else if (n7 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0) {
                        n9 = this.nextInt(2);
                        n10 = n9 == 0 ? BiomeGenBase.plains.biomeID : BiomeGenBase.forest.biomeID;
                    }
                    if (bl && n10 != n7) {
                        n10 = BiomeGenBase.getBiome(n10 + 128) != null ? (n10 += 128) : n7;
                    }
                    if (n10 == n7) {
                        nArray3[n6 + n5 * n3] = n7;
                    } else {
                        n9 = nArray[n6 + 1 + (n5 + 1 - 1) * (n3 + 2)];
                        int n11 = nArray[n6 + 1 + 1 + (n5 + 1) * (n3 + 2)];
                        int n12 = nArray[n6 + 1 - 1 + (n5 + 1) * (n3 + 2)];
                        int n13 = nArray[n6 + 1 + (n5 + 1 + 1) * (n3 + 2)];
                        int n14 = 0;
                        if (GenLayerHills.biomesEqualOrMesaPlateau(n9, n7)) {
                            ++n14;
                        }
                        if (GenLayerHills.biomesEqualOrMesaPlateau(n11, n7)) {
                            ++n14;
                        }
                        if (GenLayerHills.biomesEqualOrMesaPlateau(n12, n7)) {
                            ++n14;
                        }
                        if (GenLayerHills.biomesEqualOrMesaPlateau(n13, n7)) {
                            ++n14;
                        }
                        nArray3[n6 + n5 * n3] = n14 >= 3 ? n10 : n7;
                    }
                }
                ++n6;
            }
            ++n5;
        }
        return nArray3;
    }
}

