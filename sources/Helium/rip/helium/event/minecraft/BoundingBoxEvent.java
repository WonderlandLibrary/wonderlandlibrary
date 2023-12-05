package rip.helium.event.minecraft;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import rip.helium.event.CancellableEvent;

/**
 * @author antja03
 */
public class BoundingBoxEvent extends CancellableEvent {
    private Block block;
    private BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    private final boolean isCollided;

    public BoundingBoxEvent(Block block, BlockPos pos, AxisAlignedBB boundingBox, boolean b) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
        this.isCollided = b;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public boolean isCollided() {
        return isCollided;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
