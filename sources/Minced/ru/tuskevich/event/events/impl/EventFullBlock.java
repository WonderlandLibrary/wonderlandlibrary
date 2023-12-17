// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.block.Block;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventFullBlock extends EventCancellable
{
    private Block block;
    
    public EventFullBlock(final Block block) {
        this.block = block;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
