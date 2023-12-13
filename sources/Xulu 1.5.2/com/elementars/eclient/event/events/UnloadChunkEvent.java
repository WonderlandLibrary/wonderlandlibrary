// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.world.chunk.Chunk;
import com.elementars.eclient.event.Event;

public class UnloadChunkEvent extends Event
{
    private Chunk chunk;
    
    public UnloadChunkEvent(final Chunk chunk) {
        this.chunk = chunk;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
}
