// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;

public class SlowDownEvent extends Event
{
    private float forwardMult;
    private float strafeMult;
    
    public float getForwardMult() {
        return this.forwardMult;
    }
    
    public float getStrafeMult() {
        return this.strafeMult;
    }
    
    public void setForwardMult(final float forwardMult) {
        this.forwardMult = forwardMult;
    }
    
    public void setStrafeMult(final float strafeMult) {
        this.strafeMult = strafeMult;
    }
    
    public SlowDownEvent(final float forwardMult, final float strafeMult) {
        this.forwardMult = forwardMult;
        this.strafeMult = strafeMult;
    }
}
