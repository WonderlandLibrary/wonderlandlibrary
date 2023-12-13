/*
 * Decompiled with CFR 0.152.
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

    public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_) {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.field_151628_d = p_i45479_4_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i2 = 0; i2 < areaHeight; ++i2) {
            for (int j2 = 0; j2 < areaWidth; ++j2) {
                boolean flag;
                this.initChunkSeed(j2 + areaX, i2 + areaY);
                int k2 = aint[j2 + 1 + (i2 + 1) * (areaWidth + 2)];
                int l2 = aint1[j2 + 1 + (i2 + 1) * (areaWidth + 2)];
                boolean bl = flag = (l2 - 2) % 29 == 0;
                if (k2 > 255) {
                    logger.debug("old! " + k2);
                }
                if (k2 != 0 && l2 >= 2 && (l2 - 2) % 29 == 1 && k2 < 128) {
                    if (BiomeGenBase.getBiome(k2 + 128) != null) {
                        aint2[j2 + i2 * areaWidth] = k2 + 128;
                        continue;
                    }
                    aint2[j2 + i2 * areaWidth] = k2;
                    continue;
                }
                if (this.nextInt(3) != 0 && !flag) {
                    aint2[j2 + i2 * areaWidth] = k2;
                    continue;
                }
                int i1 = k2;
                if (k2 == BiomeGenBase.desert.biomeID) {
                    i1 = BiomeGenBase.desertHills.biomeID;
                } else if (k2 == BiomeGenBase.forest.biomeID) {
                    i1 = BiomeGenBase.forestHills.biomeID;
                } else if (k2 == BiomeGenBase.birchForest.biomeID) {
                    i1 = BiomeGenBase.birchForestHills.biomeID;
                } else if (k2 == BiomeGenBase.roofedForest.biomeID) {
                    i1 = BiomeGenBase.plains.biomeID;
                } else if (k2 == BiomeGenBase.taiga.biomeID) {
                    i1 = BiomeGenBase.taigaHills.biomeID;
                } else if (k2 == BiomeGenBase.megaTaiga.biomeID) {
                    i1 = BiomeGenBase.megaTaigaHills.biomeID;
                } else if (k2 == BiomeGenBase.coldTaiga.biomeID) {
                    i1 = BiomeGenBase.coldTaigaHills.biomeID;
                } else if (k2 == BiomeGenBase.plains.biomeID) {
                    i1 = this.nextInt(3) == 0 ? BiomeGenBase.forestHills.biomeID : BiomeGenBase.forest.biomeID;
                } else if (k2 == BiomeGenBase.icePlains.biomeID) {
                    i1 = BiomeGenBase.iceMountains.biomeID;
                } else if (k2 == BiomeGenBase.jungle.biomeID) {
                    i1 = BiomeGenBase.jungleHills.biomeID;
                } else if (k2 == BiomeGenBase.ocean.biomeID) {
                    i1 = BiomeGenBase.deepOcean.biomeID;
                } else if (k2 == BiomeGenBase.extremeHills.biomeID) {
                    i1 = BiomeGenBase.extremeHillsPlus.biomeID;
                } else if (k2 == BiomeGenBase.savanna.biomeID) {
                    i1 = BiomeGenBase.savannaPlateau.biomeID;
                } else if (GenLayerHills.biomesEqualOrMesaPlateau(k2, BiomeGenBase.mesaPlateau_F.biomeID)) {
                    i1 = BiomeGenBase.mesa.biomeID;
                } else if (k2 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0) {
                    int j1 = this.nextInt(2);
                    i1 = j1 == 0 ? BiomeGenBase.plains.biomeID : BiomeGenBase.forest.biomeID;
                }
                if (flag && i1 != k2) {
                    i1 = BiomeGenBase.getBiome(i1 + 128) != null ? (i1 += 128) : k2;
                }
                if (i1 == k2) {
                    aint2[j2 + i2 * areaWidth] = k2;
                    continue;
                }
                int k22 = aint[j2 + 1 + (i2 + 1 - 1) * (areaWidth + 2)];
                int k1 = aint[j2 + 1 + 1 + (i2 + 1) * (areaWidth + 2)];
                int l1 = aint[j2 + 1 - 1 + (i2 + 1) * (areaWidth + 2)];
                int i22 = aint[j2 + 1 + (i2 + 1 + 1) * (areaWidth + 2)];
                int j22 = 0;
                if (GenLayerHills.biomesEqualOrMesaPlateau(k22, k2)) {
                    ++j22;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(k1, k2)) {
                    ++j22;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(l1, k2)) {
                    ++j22;
                }
                if (GenLayerHills.biomesEqualOrMesaPlateau(i22, k2)) {
                    ++j22;
                }
                aint2[j2 + i2 * areaWidth] = j22 >= 3 ? i1 : k2;
            }
        }
        return aint2;
    }
}

