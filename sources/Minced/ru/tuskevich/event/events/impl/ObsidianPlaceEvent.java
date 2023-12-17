// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockObsidian;
import ru.tuskevich.event.events.Event;

public class ObsidianPlaceEvent implements Event
{
    private final BlockObsidian block;
    private final BlockPos pos;
    
    public BlockObsidian getBlock() {
        return this.block;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public ObsidianPlaceEvent(final BlockObsidian block, final BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }
}
