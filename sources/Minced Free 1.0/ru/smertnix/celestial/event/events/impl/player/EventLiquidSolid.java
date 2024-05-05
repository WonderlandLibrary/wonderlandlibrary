package ru.smertnix.celestial.event.events.impl.player;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

public class EventLiquidSolid
        extends EventCancellable {
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;

    public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
    }

    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}
