package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData {

    private final BlockPos position;
    private final Block blockType;

    /**
     * Different for each blockID
     */
    private final int eventID;
    private final int eventParameter;

    public BlockEventData(BlockPos pos, Block blockType, int eventId, int p_i45756_4_) {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    /**
     * Get the Event ID (different for each BlockID)
     */
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
        } else {
            final BlockEventData blockEventData = (BlockEventData) p_equals_1_;
            return this.position.equals(blockEventData.position) && this.eventID == blockEventData.eventID && this.eventParameter == blockEventData.eventParameter && this.blockType == blockEventData.blockType;
        }
    }

    public String toString() {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }

}
