/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
    private NibbleArray skylightArray;
    private int tickRefCount;
    private NibbleArray blocklightArray;
    private char[] data;
    private int yBase;
    private int blockRefCount;

    public int getYLocation() {
        return this.yBase;
    }

    public void removeInvalidBlocks() {
        this.blockRefCount = 0;
        this.tickRefCount = 0;
        int n = 0;
        while (n < 16) {
            int n2 = 0;
            while (n2 < 16) {
                int n3 = 0;
                while (n3 < 16) {
                    Block block = this.getBlockByExtId(n, n2, n3);
                    if (block != Blocks.air) {
                        ++this.blockRefCount;
                        if (block.getTickRandomly()) {
                            ++this.tickRefCount;
                        }
                    }
                    ++n3;
                }
                ++n2;
            }
            ++n;
        }
    }

    public boolean isEmpty() {
        return this.blockRefCount == 0;
    }

    public ExtendedBlockStorage(int n, boolean bl) {
        this.yBase = n;
        this.data = new char[4096];
        this.blocklightArray = new NibbleArray();
        if (bl) {
            this.skylightArray = new NibbleArray();
        }
    }

    public int getExtSkylightValue(int n, int n2, int n3) {
        return this.skylightArray.get(n, n2, n3);
    }

    public Block getBlockByExtId(int n, int n2, int n3) {
        return this.get(n, n2, n3).getBlock();
    }

    public int getExtBlockMetadata(int n, int n2, int n3) {
        IBlockState iBlockState = this.get(n, n2, n3);
        return iBlockState.getBlock().getMetaFromState(iBlockState);
    }

    public NibbleArray getSkylightArray() {
        return this.skylightArray;
    }

    public boolean getNeedsRandomTick() {
        return this.tickRefCount > 0;
    }

    public void setData(char[] cArray) {
        this.data = cArray;
    }

    public void setSkylightArray(NibbleArray nibbleArray) {
        this.skylightArray = nibbleArray;
    }

    public void setExtBlocklightValue(int n, int n2, int n3, int n4) {
        this.blocklightArray.set(n, n2, n3, n4);
    }

    public void set(int n, int n2, int n3, IBlockState iBlockState) {
        IBlockState iBlockState2 = this.get(n, n2, n3);
        Block block = iBlockState2.getBlock();
        Block block2 = iBlockState.getBlock();
        if (block != Blocks.air) {
            --this.blockRefCount;
            if (block.getTickRandomly()) {
                --this.tickRefCount;
            }
        }
        if (block2 != Blocks.air) {
            ++this.blockRefCount;
            if (block2.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }
        this.data[n2 << 8 | n3 << 4 | n] = (char)Block.BLOCK_STATE_IDS.get(iBlockState);
    }

    public void setBlocklightArray(NibbleArray nibbleArray) {
        this.blocklightArray = nibbleArray;
    }

    public NibbleArray getBlocklightArray() {
        return this.blocklightArray;
    }

    public int getExtBlocklightValue(int n, int n2, int n3) {
        return this.blocklightArray.get(n, n2, n3);
    }

    public void setExtSkylightValue(int n, int n2, int n3, int n4) {
        this.skylightArray.set(n, n2, n3, n4);
    }

    public char[] getData() {
        return this.data;
    }

    public IBlockState get(int n, int n2, int n3) {
        IBlockState iBlockState = Block.BLOCK_STATE_IDS.getByValue(this.data[n2 << 8 | n3 << 4 | n]);
        return iBlockState != null ? iBlockState : Blocks.air.getDefaultState();
    }
}

