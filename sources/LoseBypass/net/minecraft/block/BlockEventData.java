/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockEventData {
    private BlockPos position;
    private Block blockType;
    private int eventID;
    private int eventParameter;

    public BlockEventData(BlockPos pos, Block blockType, int eventId, int p_i45756_4_) {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public int getEventID() {
        return this.eventID;
    }

    public int getEventParameter() {
        return this.eventParameter;
    }

    public Block getBlock() {
        return this.blockType;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof BlockEventData)) {
            return false;
        }
        BlockEventData blockeventdata = (BlockEventData)p_equals_1_;
        if (!this.position.equals(blockeventdata.position)) return false;
        if (this.eventID != blockeventdata.eventID) return false;
        if (this.eventParameter != blockeventdata.eventParameter) return false;
        if (this.blockType != blockeventdata.blockType) return false;
        return true;
    }

    public String toString() {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}

