package de.tired.base.event.events;

import de.tired.base.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class EventBlockBB extends Event {
    private Block block;
    public AxisAlignedBB boundingBox;
    private int x;
    private int y;
    private int z;

    public void setBlock(Block block, AxisAlignedBB boundingBox, int x, int y, int z) {
        this.block = block;
        this.boundingBox = boundingBox;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock() {
        return this.block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}