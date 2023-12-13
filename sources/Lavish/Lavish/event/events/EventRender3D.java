// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import net.minecraft.client.gui.ScaledResolution;
import Lavish.event.Event;

public class EventRender3D extends Event
{
    public final ScaledResolution scaledResolution;
    float partialTicks;
    
    public EventRender3D(final ScaledResolution scaledResolution, final float ticks) {
        this.partialTicks = ticks;
        this.scaledResolution = scaledResolution;
    }
    
    public float getTicks() {
        return this.partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
