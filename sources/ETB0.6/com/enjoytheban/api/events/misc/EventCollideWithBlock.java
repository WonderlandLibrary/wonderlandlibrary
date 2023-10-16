package com.enjoytheban.api.events.misc;

import com.enjoytheban.api.Event;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

/**
 * An event that is called when we collide with blocks
 * @author Purity
 * @called Block addCollisionBoxesToList
 */

public class EventCollideWithBlock extends Event {

    //variables
    private Block block;
    private BlockPos blockPos;
    public AxisAlignedBB boundingBox;

    public EventCollideWithBlock(Block block, BlockPos pos, AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
    }

    //getters and setters
    public Block getBlock() {
        return this.block;
    }

    public BlockPos getPos() {
        return blockPos;
    }
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }

    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}