package wtf.evolution.event.events.impl;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import wtf.evolution.event.events.Event;
import wtf.evolution.event.events.callables.EventCancellable;


public class EventLight extends EventCancellable implements Event {

    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;

    public EventLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }

    public EnumSkyBlock getEnumSkyBlock() {
        return enumSkyBlock;
    }

    public BlockPos getPos() {
        return pos;
    }
}