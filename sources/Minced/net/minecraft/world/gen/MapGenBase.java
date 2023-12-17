// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import java.util.Random;

public class MapGenBase
{
    protected int range;
    protected Random rand;
    protected World world;
    
    public MapGenBase() {
        this.range = 8;
        this.rand = new Random();
    }
    
    public void generate(final World worldIn, final int x, final int z, final ChunkPrimer primer) {
        final int i = this.range;
        this.world = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        final long j = this.rand.nextLong();
        final long k = this.rand.nextLong();
        for (int l = x - i; l <= x + i; ++l) {
            for (int i2 = z - i; i2 <= z + i; ++i2) {
                final long j2 = l * j;
                final long k2 = i2 * k;
                this.rand.setSeed(j2 ^ k2 ^ worldIn.getSeed());
                this.recursiveGenerate(worldIn, l, i2, x, z, primer);
            }
        }
    }
    
    public static void setupChunkSeed(final long p_191068_0_, final Random p_191068_2_, final int p_191068_3_, final int p_191068_4_) {
        p_191068_2_.setSeed(p_191068_0_);
        final long i = p_191068_2_.nextLong();
        final long j = p_191068_2_.nextLong();
        final long k = p_191068_3_ * i;
        final long l = p_191068_4_ * j;
        p_191068_2_.setSeed(k ^ l ^ p_191068_0_);
    }
    
    protected void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int originalX, final int originalZ, final ChunkPrimer chunkPrimerIn) {
    }
}
