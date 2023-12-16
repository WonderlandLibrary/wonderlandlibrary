// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import moonsense.event.SCEvent;

public class SCRenderWorldPassEvent extends SCEvent
{
    public final float partialTicks;
    
    public SCRenderWorldPassEvent(final float f) {
        this.partialTicks = f;
    }
}
