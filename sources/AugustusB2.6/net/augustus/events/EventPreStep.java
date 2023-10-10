// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventPreStep extends Event
{
    private float stepHeight;
    
    public EventPreStep(final float stepHeight) {
        this.stepHeight = 0.6f;
        this.stepHeight = stepHeight;
    }
    
    public float getStepHeight() {
        return this.stepHeight;
    }
    
    public void setStepHeight(final float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
