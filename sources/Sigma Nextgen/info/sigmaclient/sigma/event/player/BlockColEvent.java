package info.sigmaclient.sigma.event.player;

import info.sigmaclient.sigma.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BlockColEvent extends Event {
    public BlockPos blockPos;
    public Block block;
    public AxisAlignedBB add = null;
    public BlockColEvent(BlockPos blockPos, Block block){
        this.eventID = 13;
        this.blockPos = blockPos;
        this.block = block;
    }
}
