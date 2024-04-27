package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockBBEvent extends Event
{
    private int x;
    private int y;
    private int z;
    private Block block;
    private AxisAlignedBB axisAlignedBB;

    public BlockBBEvent(BlockPos blockPos, Block block, AxisAlignedBB axisAlignedBB) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
        this.block = block;
        this.setBoundingBox(axisAlignedBB);
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

    public Block getBlock() {
        return this.block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.axisAlignedBB;
    }

    public void setBoundingBox(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
}

