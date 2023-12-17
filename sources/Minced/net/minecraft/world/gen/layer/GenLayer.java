// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.WorldType;

public abstract class GenLayer
{
    private long worldGenSeed;
    protected GenLayer parent;
    private long chunkSeed;
    protected long baseSeed;
    
    public static GenLayer[] initializeAllBiomeGenerators(final long seed, final WorldType p_180781_2_, final ChunkGeneratorSettings p_180781_3_) {
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        final GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        final GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayer genlayeraddisland2 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland2 = new GenLayerAddIsland(50L, genlayeraddisland2);
        genlayeraddisland2 = new GenLayerAddIsland(70L, genlayeraddisland2);
        final GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland2);
        final GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        final GenLayer genlayeraddisland3 = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland3, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayerzoom2 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom2 = new GenLayerZoom(2003L, genlayerzoom2);
        final GenLayer genlayeraddisland4 = new GenLayerAddIsland(4L, genlayerzoom2);
        final GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland4);
        final GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        final GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        int j;
        int i = j = 4;
        if (p_180781_3_ != null) {
            i = p_180781_3_.biomeSize;
            j = p_180781_3_.riverSize;
        }
        if (p_180781_2_ == WorldType.LARGE_BIOMES) {
            i = 6;
        }
        final GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer2, 0);
        final GenLayer genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
        final GenLayer lvt_8_1_ = new GenLayerBiome(200L, genlayer2, p_180781_2_, p_180781_3_);
        final GenLayer genlayer3 = GenLayerZoom.magnify(1000L, lvt_8_1_, 2);
        final GenLayer genlayerbiomeedge = new GenLayerBiomeEdge(1000L, genlayer3);
        final GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_);
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer4 = GenLayerZoom.magnify(1000L, genlayer4, j);
        final GenLayer genlayerriver = new GenLayerRiver(1L, genlayer4);
        final GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);
        for (int k = 0; k < i; ++k) {
            genlayerhills = new GenLayerZoom(1000 + k, genlayerhills);
            if (k == 0) {
                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
            }
            if (k == 1 || i == 1) {
                genlayerhills = new GenLayerShore(1000L, genlayerhills);
            }
        }
        final GenLayer genlayersmooth2 = new GenLayerSmooth(1000L, genlayerhills);
        final GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth2, genlayersmooth);
        final GenLayer genlayer5 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer5.initWorldGenSeed(seed);
        return new GenLayer[] { genlayerrivermix, genlayer5, genlayerrivermix };
    }
    
    public GenLayer(final long p_i2125_1_) {
        this.baseSeed = p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
    }
    
    public void initWorldGenSeed(final long seed) {
        this.worldGenSeed = seed;
        if (this.parent != null) {
            this.parent.initWorldGenSeed(seed);
        }
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }
    
    public void initChunkSeed(final long p_75903_1_, final long p_75903_3_) {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
    }
    
    protected int nextInt(final int p_75902_1_) {
        int i = (int)((this.chunkSeed >> 24) % p_75902_1_);
        if (i < 0) {
            i += p_75902_1_;
        }
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }
    
    public abstract int[] getInts(final int p0, final int p1, final int p2, final int p3);
    
    protected static boolean biomesEqualOrMesaPlateau(final int biomeIDA, final int biomeIDB) {
        if (biomeIDA == biomeIDB) {
            return true;
        }
        final Biome biome = Biome.getBiome(biomeIDA);
        final Biome biome2 = Biome.getBiome(biomeIDB);
        if (biome == null || biome2 == null) {
            return false;
        }
        if (biome != Biomes.MESA_ROCK && biome != Biomes.MESA_CLEAR_ROCK) {
            return biome == biome2 || biome.getBiomeClass() == biome2.getBiomeClass();
        }
        return biome2 == Biomes.MESA_ROCK || biome2 == Biomes.MESA_CLEAR_ROCK;
    }
    
    protected static boolean isBiomeOceanic(final int p_151618_0_) {
        final Biome biome = Biome.getBiome(p_151618_0_);
        return biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN || biome == Biomes.FROZEN_OCEAN;
    }
    
    protected int selectRandom(final int... p_151619_1_) {
        return p_151619_1_[this.nextInt(p_151619_1_.length)];
    }
    
    protected int selectModeOrRandom(final int p_151617_1_, final int p_151617_2_, final int p_151617_3_, final int p_151617_4_) {
        if (p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_) {
            return p_151617_2_;
        }
        if (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_) {
            return p_151617_1_;
        }
        if (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_) {
            return p_151617_1_;
        }
        if (p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_) {
            return p_151617_1_;
        }
        if (p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_) {
            return p_151617_1_;
        }
        if (p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_) {
            return p_151617_1_;
        }
        if (p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_) {
            return p_151617_1_;
        }
        if (p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_) {
            return p_151617_2_;
        }
        if (p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_) {
            return p_151617_2_;
        }
        return (p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_) ? p_151617_3_ : this.selectRandom(p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_);
    }
}
