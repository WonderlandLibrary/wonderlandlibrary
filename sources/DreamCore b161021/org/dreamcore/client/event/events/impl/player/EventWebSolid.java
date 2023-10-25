package org.dreamcore.client.event.events.impl.player;

import net.minecraft.block.BlockWeb;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventWebSolid extends EventCancellable {

    private final BlockWeb blockWeb;
    private final BlockPos pos;

    public EventWebSolid(BlockWeb blockLiquid, BlockPos pos) {
        this.blockWeb = blockLiquid;
        this.pos = pos;
    }

    public BlockWeb getBlock() {
        return blockWeb;
    }

    public BlockPos getPos() {
        return pos;
    }
}
