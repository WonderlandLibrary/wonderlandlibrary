package org.dreamcore.client.event.events.impl.player;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventLiquidSolid extends EventCancellable {

    private final BlockLiquid blockLiquid;
    private final BlockPos pos;

    public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
    }

    public BlockLiquid getBlock() {
        return blockLiquid;
    }

    public BlockPos getPos() {
        return pos;
    }
}