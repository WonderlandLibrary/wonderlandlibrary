// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventStep extends Event
{
    private double stepHeight;
    private double realHeight;
    private boolean active;
    private boolean pre;
    
    public void fire(final boolean state, final double stepHeight, final double realHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = realHeight;
        super.fire();
    }
    
    public void fire(final boolean state, final double stepHeight) {
        this.pre = state;
        this.stepHeight = stepHeight;
        this.realHeight = this.realHeight;
        super.fire();
    }
    
    public boolean isPre() {
        return this.pre;
    }
    
    public double getStepHeight() {
        return this.stepHeight;
    }
    
    public boolean isActive() {
        return this.active;
    }
    
    public void setStepHeight(final double stepHeight) {
        this.stepHeight = stepHeight;
    }
    
    public void setActive(final boolean bypass) {
        this.active = bypass;
    }
    
    public double getRealHeight() {
        return this.realHeight;
    }
    
    public void setRealHeight(final double realHeight) {
        this.realHeight = realHeight;
    }
}
