/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiome
extends GenLayer {
    private BiomeGenBase[] field_151620_f;
    private BiomeGenBase[] field_151622_e;
    private final ChunkProviderSettings field_175973_g;
    private BiomeGenBase[] field_151623_c = new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains};
    private BiomeGenBase[] field_151621_d = new BiomeGenBase[]{BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland};

    public GenLayerBiome(long l, GenLayer genLayer, WorldType worldType, String string) {
        super(l);
        this.field_151622_e = new BiomeGenBase[]{BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains};
        this.field_151620_f = new BiomeGenBase[]{BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga};
        this.parent = genLayer;
        if (worldType == WorldType.DEFAULT_1_1) {
            this.field_151623_c = new BiomeGenBase[]{BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga};
            this.field_175973_g = null;
        } else {
            this.field_175973_g = worldType == WorldType.CUSTOMIZED ? ChunkProviderSettings.Factory.jsonToFactory(string).func_177864_b() : null;
        }
    }

    @Override
    public int[] getInts(int n, int n2, int n3, int n4) {
        int[] nArray = this.parent.getInts(n, n2, n3, n4);
        int[] nArray2 = IntCache.getIntCache(n3 * n4);
        int n5 = 0;
        while (n5 < n4) {
            int n6 = 0;
            while (n6 < n3) {
                this.initChunkSeed(n6 + n, n5 + n2);
                int n7 = nArray[n6 + n5 * n3];
                int n8 = (n7 & 0xF00) >> 8;
                nArray2[n6 + n5 * n3] = this.field_175973_g != null && this.field_175973_g.fixedBiome >= 0 ? this.field_175973_g.fixedBiome : (GenLayerBiome.isBiomeOceanic(n7) ? n7 : (n7 == BiomeGenBase.mushroomIsland.biomeID ? n7 : (n7 == 1 ? (n8 > 0 ? (this.nextInt(3) == 0 ? BiomeGenBase.mesaPlateau.biomeID : BiomeGenBase.mesaPlateau_F.biomeID) : this.field_151623_c[this.nextInt((int)this.field_151623_c.length)].biomeID) : (n7 == 2 ? (n8 > 0 ? BiomeGenBase.jungle.biomeID : this.field_151621_d[this.nextInt((int)this.field_151621_d.length)].biomeID) : (n7 == 3 ? (n8 > 0 ? BiomeGenBase.megaTaiga.biomeID : this.field_151622_e[this.nextInt((int)this.field_151622_e.length)].biomeID) : ((n7 &= 0xFFFFF0FF) == 4 ? this.field_151620_f[this.nextInt((int)this.field_151620_f.length)].biomeID : BiomeGenBase.mushroomIsland.biomeID))))));
                ++n6;
            }
            ++n5;
        }
        return nArray2;
    }
}

