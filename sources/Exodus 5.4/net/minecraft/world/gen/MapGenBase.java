/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenBase {
    protected Random rand = new Random();
    protected int range = 8;
    protected World worldObj;

    protected void recursiveGenerate(World world, int n, int n2, int n3, int n4, ChunkPrimer chunkPrimer) {
    }

    public void generate(IChunkProvider iChunkProvider, World world, int n, int n2, ChunkPrimer chunkPrimer) {
        int n3 = this.range;
        this.worldObj = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long l2 = this.rand.nextLong();
        int n4 = n - n3;
        while (n4 <= n + n3) {
            int n5 = n2 - n3;
            while (n5 <= n2 + n3) {
                long l3 = (long)n4 * l;
                long l4 = (long)n5 * l2;
                this.rand.setSeed(l3 ^ l4 ^ world.getSeed());
                this.recursiveGenerate(world, n4, n5, n, n2, chunkPrimer);
                ++n5;
            }
            ++n4;
        }
    }
}

