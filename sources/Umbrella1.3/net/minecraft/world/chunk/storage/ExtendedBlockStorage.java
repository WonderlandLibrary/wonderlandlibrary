/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk.storage;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;
import optifine.Reflector;

public class ExtendedBlockStorage {
    private int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private char[] data;
    private NibbleArray blocklightArray;
    private NibbleArray skylightArray;
    private static final String __OBFID = "CL_00000375";

    public ExtendedBlockStorage(int y, boolean storeSkylight) {
        this.yBase = y;
        this.data = new char[4096];
        this.blocklightArray = new NibbleArray();
        if (storeSkylight) {
            this.skylightArray = new NibbleArray();
        }
    }

    public IBlockState get(int x, int y, int z) {
        IBlockState var4 = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
        return var4 != null ? var4 : Blocks.air.getDefaultState();
    }

    public void set(int x, int y, int z, IBlockState state) {
        if (Reflector.IExtendedBlockState.isInstance(state)) {
            state = (IBlockState)Reflector.call(state, Reflector.IExtendedBlockState_getClean, new Object[0]);
        }
        IBlockState var5 = this.get(x, y, z);
        Block var6 = var5.getBlock();
        Block var7 = state.getBlock();
        if (var6 != Blocks.air) {
            --this.blockRefCount;
            if (var6.getTickRandomly()) {
                --this.tickRefCount;
            }
        }
        if (var7 != Blocks.air) {
            ++this.blockRefCount;
            if (var7.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }
        this.data[y << 8 | z << 4 | x] = (char)Block.BLOCK_STATE_IDS.get(state);
    }

    public Block getBlockByExtId(int x, int y, int z) {
        return this.get(x, y, z).getBlock();
    }

    public int getExtBlockMetadata(int x, int y, int z) {
        IBlockState var4 = this.get(x, y, z);
        return var4.getBlock().getMetaFromState(var4);
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

    public void setExtSkylightValue(int x, int y, int z, int value) {
        this.skylightArray.set(x, y, z, value);
    }

    public int getExtSkylightValue(int x, int y, int z) {
        return this.skylightArray.get(x, y, z);
    }

    public void setExtBlocklightValue(int x, int y, int z, int value) {
        this.blocklightArray.set(x, y, z, value);
    }

    public int getExtBlocklightValue(int x, int y, int z) {
        return this.blocklightArray.get(x, y, z);
    }

    public void removeInvalidBlocks() {
        List blockStates = Block.BLOCK_STATE_IDS.getObjectList();
        int maxStateId = blockStates.size();
        int localBlockRefCount = 0;
        int localTickRefCount = 0;
        for (int y = 0; y < 16; ++y) {
            int by = y << 8;
            for (int z = 0; z < 16; ++z) {
                int byz = by | z << 4;
                for (int x = 0; x < 16; ++x) {
                    Block var4;
                    IBlockState bs;
                    char stateId = this.data[byz | x];
                    if (stateId <= '\u0000') continue;
                    ++localBlockRefCount;
                    if (stateId >= maxStateId || (bs = (IBlockState)blockStates.get(stateId)) == null || !(var4 = bs.getBlock()).getTickRandomly()) continue;
                    ++localTickRefCount;
                }
            }
        }
        this.blockRefCount = localBlockRefCount;
        this.tickRefCount = localTickRefCount;
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

