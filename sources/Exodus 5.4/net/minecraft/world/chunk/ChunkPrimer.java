/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ChunkPrimer {
    private final IBlockState defaultState;
    private final short[] data = new short[65536];

    public void setBlockState(int n, int n2, int n3, IBlockState iBlockState) {
        int n4 = n << 12 | n3 << 8 | n2;
        this.setBlockState(n4, iBlockState);
    }

    public IBlockState getBlockState(int n, int n2, int n3) {
        int n4 = n << 12 | n3 << 8 | n2;
        return this.getBlockState(n4);
    }

    public ChunkPrimer() {
        this.defaultState = Blocks.air.getDefaultState();
    }

    public IBlockState getBlockState(int n) {
        if (n >= 0 && n < this.data.length) {
            IBlockState iBlockState = Block.BLOCK_STATE_IDS.getByValue(this.data[n]);
            return iBlockState != null ? iBlockState : this.defaultState;
        }
        throw new IndexOutOfBoundsException("The coordinate is out of range");
    }

    public void setBlockState(int n, IBlockState iBlockState) {
        if (n < 0 || n >= this.data.length) {
            throw new IndexOutOfBoundsException("The coordinate is out of range");
        }
        this.data[n] = (short)Block.BLOCK_STATE_IDS.get(iBlockState);
    }
}

