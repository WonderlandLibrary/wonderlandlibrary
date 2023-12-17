// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import com.google.common.cache.LoadingCache;

public class BiomeEndDecorator extends BiomeDecorator
{
    private static final LoadingCache<Long, WorldGenSpikes.EndSpike[]> SPIKE_CACHE;
    private final WorldGenSpikes spikeGen;
    
    public BiomeEndDecorator() {
        this.spikeGen = new WorldGenSpikes();
    }
    
    @Override
    protected void genDecorations(final Biome biomeIn, final World worldIn, final Random random) {
        this.generateOres(worldIn, random);
        final WorldGenSpikes.EndSpike[] spikesForWorld;
        final WorldGenSpikes.EndSpike[] aworldgenspikes$endspike = spikesForWorld = getSpikesForWorld(worldIn);
        for (final WorldGenSpikes.EndSpike worldgenspikes$endspike : spikesForWorld) {
            if (worldgenspikes$endspike.doesStartInChunk(this.chunkPos)) {
                this.spikeGen.setSpike(worldgenspikes$endspike);
                this.spikeGen.generate(worldIn, random, new BlockPos(worldgenspikes$endspike.getCenterX(), 45, worldgenspikes$endspike.getCenterZ()));
            }
        }
    }
    
    public static WorldGenSpikes.EndSpike[] getSpikesForWorld(final World p_185426_0_) {
        final Random random = new Random(p_185426_0_.getSeed());
        final long i = random.nextLong() & 0xFFFFL;
        return (WorldGenSpikes.EndSpike[])BiomeEndDecorator.SPIKE_CACHE.getUnchecked((Object)i);
    }
    
    static {
        SPIKE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build((CacheLoader)new SpikeCacheLoader());
    }
    
    static class SpikeCacheLoader extends CacheLoader<Long, WorldGenSpikes.EndSpike[]>
    {
        private SpikeCacheLoader() {
        }
        
        public WorldGenSpikes.EndSpike[] load(final Long p_load_1_) throws Exception {
            final List<Integer> list = (List<Integer>)Lists.newArrayList((Iterable)ContiguousSet.create(Range.closedOpen((Comparable)0, (Comparable)10), DiscreteDomain.integers()));
            Collections.shuffle(list, new Random(p_load_1_));
            final WorldGenSpikes.EndSpike[] aworldgenspikes$endspike = new WorldGenSpikes.EndSpike[10];
            for (int i = 0; i < 10; ++i) {
                final int j = (int)(42.0 * Math.cos(2.0 * (-3.141592653589793 + 0.3141592653589793 * i)));
                final int k = (int)(42.0 * Math.sin(2.0 * (-3.141592653589793 + 0.3141592653589793 * i)));
                final int l = list.get(i);
                final int i2 = 2 + l / 3;
                final int j2 = 76 + l * 3;
                final boolean flag = l == 1 || l == 2;
                aworldgenspikes$endspike[i] = new WorldGenSpikes.EndSpike(j, k, i2, j2, flag);
            }
            return aworldgenspikes$endspike;
        }
    }
}
