// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventRayCast extends Event
{
    private float partialTicks;
    
    public EventRayCast(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
