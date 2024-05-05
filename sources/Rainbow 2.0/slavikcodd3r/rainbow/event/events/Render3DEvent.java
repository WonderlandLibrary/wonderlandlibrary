// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.event.events;

import slavikcodd3r.rainbow.event.Event;

public class Render3DEvent extends Event
{
    public float partialTicks;
    
    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
