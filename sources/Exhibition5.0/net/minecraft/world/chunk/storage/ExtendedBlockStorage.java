// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk.storage;

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
    private static final String __OBFID = "CL_00000375";
    
    public ExtendedBlockStorage(final int y, final boolean storeSkylight) {
        this.yBase = y;
        this.data = new char[4096];
        this.blocklightArray = new NibbleArray();
        if (storeSkylight) {
            this.skylightArray = new NibbleArray();
        }
    }
    
    public IBlockState get(final int x, final int y, final int z) {
        final IBlockState var4 = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
        return (var4 != null) ? var4 : Blocks.air.getDefaultState();
    }
    
    public void set(final int x, final int y, final int z, final IBlockState state) {
        final IBlockState var5 = this.get(x, y, z);
        final Block var6 = var5.getBlock();
        final Block var7 = state.getBlock();
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
    
    public Block getBlockByExtId(final int x, final int y, final int z) {
        return this.get(x, y, z).getBlock();
    }
    
    public int getExtBlockMetadata(final int x, final int y, final int z) {
        final IBlockState var4 = this.get(x, y, z);
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
        this.blockRefCount = 0;
        this.tickRefCount = 0;
        for (int var1 = 0; var1 < 16; ++var1) {
            for (int var2 = 0; var2 < 16; ++var2) {
                for (int var3 = 0; var3 < 16; ++var3) {
                    final Block var4 = this.getBlockByExtId(var1, var2, var3);
                    if (var4 != Blocks.air) {
                        ++this.blockRefCount;
                        if (var4.getTickRandomly()) {
                            ++this.tickRefCount;
                        }
                    }
                }
            }
        }
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
}
