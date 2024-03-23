package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class AxisAlignedBBEvent extends Event {
    private Block block;
    private BlockPos blockPos;
    private AxisAlignedBB axisAlignedBB;

    public AxisAlignedBBEvent(Block block, BlockPos blockPos, AxisAlignedBB axisAlignedBB) {
        this.block = block;
        this.blockPos = blockPos;
        this.axisAlignedBB = axisAlignedBB;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public AxisAlignedBB getAxisAlignedBB() {
        return axisAlignedBB;
    }

    public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
        this.axisAlignedBB = axisAlignedBB;
    }
}
