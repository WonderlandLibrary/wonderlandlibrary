/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import tk.rektsky.event.Event;

public class BlockBBEvent
extends Event {
    BlockPos pos;
    Block block;
    AxisAlignedBB axisalignedbb;

    public BlockBBEvent(BlockPos pos, Block block, AxisAlignedBB axisalignedbb) {
        this.pos = pos;
        this.block = block;
        this.axisalignedbb = axisalignedbb;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.axisalignedbb;
    }

    public void setBoundingBox(AxisAlignedBB axisalignedbb) {
        this.axisalignedbb = axisalignedbb;
    }

    public double getX() {
        return this.pos.getX();
    }

    public double getY() {
        return this.pos.getY();
    }

    public double getZ() {
        return this.pos.getZ();
    }
}

