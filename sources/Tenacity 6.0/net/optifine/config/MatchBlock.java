// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.minecraft.src.Config;
import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId;
    private int[] metadatas;
    
    public MatchBlock(final int blockId) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = blockId;
    }
    
    public MatchBlock(final int blockId, final int metadata) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = blockId;
        if (metadata >= 0 && metadata <= 15) {
            this.metadatas = new int[] { metadata };
        }
    }
    
    public MatchBlock(final int blockId, final int[] metadatas) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = blockId;
        this.metadatas = metadatas;
    }
    
    public int getBlockId() {
        return this.blockId;
    }
    
    public int[] getMetadatas() {
        return this.metadatas;
    }
    
    public boolean matches(final BlockStateBase blockState) {
        return blockState.getBlockId() == this.blockId && Matches.metadata(blockState.getMetadata(), this.metadatas);
    }
    
    public boolean matches(final int id, final int metadata) {
        return id == this.blockId && Matches.metadata(metadata, this.metadatas);
    }
    
    public void addMetadata(final int metadata) {
        if (this.metadatas != null && metadata >= 0 && metadata <= 15) {
            for (int i = 0; i < this.metadatas.length; ++i) {
                if (this.metadatas[i] == metadata) {
                    return;
                }
            }
            this.metadatas = Config.addIntToArray(this.metadatas, metadata);
        }
    }
    
    @Override
    public String toString() {
        return "" + this.blockId + ":" + Config.arrayToString(this.metadatas);
    }
}
