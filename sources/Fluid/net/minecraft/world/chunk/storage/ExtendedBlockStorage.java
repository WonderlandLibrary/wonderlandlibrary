// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.chunk.storage;

import net.optifine.reflect.Reflector;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage
{
    private int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private char[] data;
    private NibbleArray blocklightArray;
    private NibbleArray skylightArray;
    
    public ExtendedBlockStorage(final int y, final boolean storeSkylight) {
        this.yBase = y;
        this.data = new char[4096];
        this.blocklightArray = new NibbleArray();
        if (storeSkylight) {
            this.skylightArray = new NibbleArray();
        }
    }
    
    public IBlockState get(final int x, final int y, final int z) {
        final IBlockState iblockstate = Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
        return (iblockstate != null) ? iblockstate : Blocks.air.getDefaultState();
    }
    
    public void set(final int x, final int y, final int z, IBlockState state) {
        if (Reflector.IExtendedBlockState.isInstance(state)) {
            state = (IBlockState)Reflector.call(state, Reflector.IExtendedBlockState_getClean, new Object[0]);
        }
        final IBlockState iblockstate = this.get(x, y, z);
        final Block block = iblockstate.getBlock();
        final Block block2 = state.getBlock();
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
        this.data[y << 8 | z << 4 | x] = (char)Block.BLOCK_STATE_IDS.get(state);
    }
    
    public Block getBlockByExtId(final int x, final int y, final int z) {
        return this.get(x, y, z).getBlock();
    }
    
    public int getExtBlockMetadata(final int x, final int y, final int z) {
        final IBlockState iblockstate = this.get(x, y, z);
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
    
    public void setExtSkylightValue(final int x, final int y, final int z, final int value) {
        this.skylightArray.set(x, y, z, value);
    }
    
    public int getExtSkylightValue(final int x, final int y, final int z) {
        return this.skylightArray.get(x, y, z);
    }
    
    public void setExtBlocklightValue(final int x, final int y, final int z, final int value) {
        this.blocklightArray.set(x, y, z, value);
    }
    
    public int getExtBlocklightValue(final int x, final int y, final int z) {
        return this.blocklightArray.get(x, y, z);
    }
    
    public void removeInvalidBlocks() {
        final IBlockState iblockstate = Blocks.air.getDefaultState();
        int i = 0;
        int j = 0;
        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                for (int i2 = 0; i2 < 16; ++i2) {
                    final Block block = this.getBlockByExtId(i2, k, l);
                    if (block != Blocks.air) {
                        ++i;
                        if (block.getTickRandomly()) {
                            ++j;
                        }
                    }
                }
            }
        }
        this.blockRefCount = i;
        this.tickRefCount = j;
    }
    
    public char[] getData() {
        return this.data;
    }
    
    public void setData(final char[] dataArray) {
        this.data = dataArray;
    }
    
    public NibbleArray getBlocklightArray() {
        return this.blocklightArray;
    }
    
    public NibbleArray getSkylightArray() {
        return this.skylightArray;
    }
    
    public void setBlocklightArray(final NibbleArray newBlocklightArray) {
        this.blocklightArray = newBlocklightArray;
    }
    
    public void setSkylightArray(final NibbleArray newSkylightArray) {
        this.skylightArray = newSkylightArray;
    }
    
    public int getBlockRefCount() {
        return this.blockRefCount;
    }
}
