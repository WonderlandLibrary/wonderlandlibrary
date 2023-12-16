// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.world.World;
import moonsense.event.SCEvent;

public class SCWorldUnloadedEvent extends SCEvent
{
    public final World worldObject;
    
    public SCWorldUnloadedEvent(final World world) {
        this.worldObject = world;
    }
}
