/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
    private int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private char[] data;
    private NibbleArray blocklightArray;
    private NibbleArray skylightArray;
    private static final String __OBFID = "CL_00000375";

    public ExtendedBlockStorage(int y2, boolean storeSkylight) {
        this.yBase = y2;
        this.data = new char[4096];
        this.blocklightArray = new NibbleArray();
        if (storeSkylight) {
            this.skylightArray = new NibbleArray();
        }
    }

    public IBlockState get(int x2, int y2, int z2) {
        IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[y2 << 8 | z2 << 4 | x2]);
        return iblockstate != null ? iblockstate : Blocks.air.getDefaultState();
    }

    public void set(int x2, int y2, int z2, IBlockState state) {
        IBlockState iblockstate = this.get(x2, y2, z2);
        Block block = iblockstate.getBlock();
        Block block1 = state.getBlock();
        if (block != Blocks.air) {
            --this.blockRefCount;
            if (block.getTickRandomly()) {
                --this.tickRefCount;
            }
        }
        if (block1 != Blocks.air) {
            ++this.blockRefCount;
            if (block1.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }
        this.data[y2 << 8 | z2 << 4 | x2] = (char)Block.BLOCK_STATE_IDS.get(state);
    }

    public Block getBlockByExtId(int x2, int y2, int z2) {
        return this.get(x2, y2, z2).getBlock();
    }

    public int getExtBlockMetadata(int x2, int y2, int z2) {
        IBlockState iblockstate = this.get(x2, y2, z2);
        return iblockstate.getBlock().getMetaFromState(iblockstate);
    }

    public boolean isEmpty() {
        return this.blockRefCount == 0;
    }

    public boolean getNeedsRandomTick() {
        return this.tickRefCount > 0;
    }

    public int getYLocation() {
        return this.yBase;
    }

    public void setExtSkylightValue(int x2, int y2, int z2, int value) {
        this.skylightArray.set(x2, y2, z2, value);
    }

    public int getExtSkylightValue(int x2, int y2, int z2) {
        return this.skylightArray.get(x2, y2, z2);
    }

    public void setExtBlocklightValue(int x2, int y2, int z2, int value) {
        this.blocklightArray.set(x2, y2, z2, value);
    }

    public int getExtBlocklightValue(int x2, int y2, int z2) {
        return this.blocklightArray.get(x2, y2, z2);
    }

    public void removeInvalidBlocks() {
        List list = Block.BLOCK_STATE_IDS.getObjectList();
        int i2 = list.size();
        int j2 = 0;
        int k2 = 0;
        for (int l2 = 0; l2 < 16; ++l2) {
            int i1 = l2 << 8;
            for (int j1 = 0; j1 < 16; ++j1) {
                int k1 = i1 | j1 << 4;
                for (int l1 = 0; l1 < 16; ++l1) {
                    Block block;
                    IBlockState iblockstate;
                    char i22 = this.data[k1 | l1];
                    if (i22 <= '\u0000') continue;
                    ++j2;
                    if (i22 >= i2 || (iblockstate = (IBlockState)list.get(i22)) == null || !(block = iblockstate.getBlock()).getTickRandomly()) continue;
                    ++k2;
                }
            }
        }
        this.blockRefCount = j2;
        this.tickRefCount = k2;
    }

    public char[] getData() {
        return this.data;
    }

    public void setData(char[] dataArray) {
        this.data = dataArray;
    }

    public NibbleArray getBlocklightArray() {
        return this.blocklightArray;
    }

    public NibbleArray getSkylightArray() {
        return this.skylightArray;
    }

    public void setBlocklightArray(NibbleArray newBlocklightArray) {
        this.blocklightArray = newBlocklightArray;
    }

    public void setSkylightArray(NibbleArray newSkylightArray) {
        this.skylightArray = newSkylightArray;
    }
}

