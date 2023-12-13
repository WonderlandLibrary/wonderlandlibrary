// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import java.util.Random;

public class MapGenBase
{
    protected int range;
    protected Random rand;
    protected World worldObj;
    private static final String __OBFID = "CL_00000394";
    
    public MapGenBase() {
        this.range = 8;
        this.rand = new Random();
    }
    
    public void func_175792_a(final IChunkProvider p_175792_1_, final World worldIn, final int p_175792_3_, final int p_175792_4_, final ChunkPrimer p_175792_5_) {
        final int var6 = this.range;
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        final long var7 = this.rand.nextLong();
        final long var8 = this.rand.nextLong();
        for (int var9 = p_175792_3_ - var6; var9 <= p_175792_3_ + var6; ++var9) {
            for (int var10 = p_175792_4_ - var6; var10 <= p_175792_4_ + var6; ++var10) {
                final long var11 = var9 * var7;
                final long var12 = var10 * var8;
                this.rand.setSeed(var11 ^ var12 ^ worldIn.getSeed());
                this.func_180701_a(worldIn, var9, var10, p_175792_3_, p_175792_4_, p_175792_5_);
            }
        }
    }
    
    protected void func_180701_a(final World worldIn, final int p_180701_2_, final int p_180701_3_, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer p_180701_6_) {
    }
}
