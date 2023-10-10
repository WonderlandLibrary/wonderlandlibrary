/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ChunkPrimer {
    private final short[] data = new short[65536];
    private final IBlockState defaultState = Blocks.air.getDefaultState();

    public IBlockState getBlockState(int x, int y, int z) {
        int i = x << 12 | z << 8 | y;
        return this.getBlockState(i);
    }

    public IBlockState getBlockState(int index) {
        IBlockState iBlockState;
        if (index < 0) throw new IndexOutOfBoundsException("The coordinate is out of range");
        if (index >= this.data.length) throw new IndexOutOfBoundsException("The coordinate is out of range");
        IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(this.data[index]);
        if (iblockstate != null) {
            iBlockState = iblockstate;
            return iBlockState;
        }
        iBlockState = this.defaultState;
        return iBlockState;
    }

    public void setBlockState(int x, int y, int z, IBlockState state) {
        int i = x << 12 | z << 8 | y;
        this.setBlockState(i, state);
    }

    public void setBlockState(int index, IBlockState state) {
        if (index < 0) throw new IndexOutOfBoundsException("The coordinate is out of range");
        if (index >= this.data.length) throw new IndexOutOfBoundsException("The coordinate is out of range");
        this.data[index] = (short)Block.BLOCK_STATE_IDS.get(state);
    }
}

