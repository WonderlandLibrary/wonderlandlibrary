// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class ChunkPrimer
{
    private static final IBlockState DEFAULT_STATE;
    private final char[] data;
    
    public ChunkPrimer() {
        this.data = new char[65536];
    }
    
    public IBlockState getBlockState(final int x, final int y, final int z) {
        final IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(this.data[getBlockIndex(x, y, z)]);
        return (iblockstate == null) ? ChunkPrimer.DEFAULT_STATE : iblockstate;
    }
    
    public void setBlockState(final int x, final int y, final int z, final IBlockState state) {
        this.data[getBlockIndex(x, y, z)] = (char)Block.BLOCK_STATE_IDS.get(state);
    }
    
    private static int getBlockIndex(final int x, final int y, final int z) {
        return x << 12 | z << 8 | y;
    }
    
    public int findGroundBlockIdx(final int x, final int z) {
        final int i = (x << 12 | z << 8) + 256 - 1;
        for (int j = 255; j >= 0; --j) {
            final IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(this.data[i + j]);
            if (iblockstate != null && iblockstate != ChunkPrimer.DEFAULT_STATE) {
                return j;
            }
        }
        return 0;
    }
    
    static {
        DEFAULT_STATE = Blocks.AIR.getDefaultState();
    }
}
