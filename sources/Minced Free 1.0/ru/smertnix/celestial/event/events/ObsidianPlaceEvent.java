package ru.smertnix.celestial.event.events;

import net.minecraft.block.BlockObsidian;
import net.minecraft.util.math.BlockPos;

public class ObsidianPlaceEvent implements Event {

    public BlockObsidian getBlock() {
        return block;
    }
    private final BlockObsidian block;
    public BlockPos getPos() {
        return pos;
    }
    private final BlockPos pos;

    public ObsidianPlaceEvent(final BlockObsidian block, final BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

}