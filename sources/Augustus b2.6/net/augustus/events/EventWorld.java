// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

import net.minecraft.client.multiplayer.WorldClient;

public class EventWorld extends Event
{
    private final WorldClient worldClient;
    
    public EventWorld(final WorldClient worldClient) {
        this.worldClient = worldClient;
    }
    
    public WorldClient getWorldClient() {
        return this.worldClient;
    }
}
