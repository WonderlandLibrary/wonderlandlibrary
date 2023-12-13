// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventRayCastPost extends Event
{
    private float partialTicks;
    
    public EventRayCastPost(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
