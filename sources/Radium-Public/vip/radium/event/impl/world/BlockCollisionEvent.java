package vip.radium.event.impl.world;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import vip.radium.event.CancellableEvent;
import net.minecraft.block.Block;

public final class BlockCollisionEvent extends CancellableEvent {

    private final Block block;
    private final BlockPos blockPos;
    private AxisAlignedBB boundingBox;

    public BlockCollisionEvent(Block block, BlockPos blockPos, AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = blockPos;
        this.boundingBox = boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Block getBlock() {
        return block;
    }

}
