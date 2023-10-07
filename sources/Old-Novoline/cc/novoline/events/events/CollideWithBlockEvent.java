package cc.novoline.events.events;

import cc.novoline.events.events.callables.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class CollideWithBlockEvent extends CancellableEvent {

    public AxisAlignedBB boundingBox;
    private Block block;
    private BlockPos blockPos;

    public CollideWithBlockEvent(Block block, BlockPos pos, AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockPos getPos() {
        return this.blockPos;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

}