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

    public IBlockState getBlockState(int x2, int y2, int z2) {
        int i2 = x2 << 12 | z2 << 8 | y2;
        return this.getBlockState(i2);
    }

    public IBlockState getBlockState(int index) {
        if (index >= 0 && index < this.data.length) {
            IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[index]);
            return iblockstate != null ? iblockstate : this.defaultState;
        }
        throw new IndexOutOfBoundsException("The coordinate is out of range");
    }

    public void setBlockState(int x2, int y2, int z2, IBlockState state) {
        int i2 = x2 << 12 | z2 << 8 | y2;
        this.setBlockState(i2, state);
    }

    public void setBlockState(int index, IBlockState state) {
        if (index < 0 || index >= this.data.length) {
            throw new IndexOutOfBoundsException("The coordinate is out of range");
        }
        this.data[index] = (short)Block.BLOCK_STATE_IDS.get(state);
    }
}

