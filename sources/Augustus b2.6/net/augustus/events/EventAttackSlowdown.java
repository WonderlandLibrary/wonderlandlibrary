// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventAttackSlowdown extends Event
{
    private boolean sprint;
    private double slowDown;
    
    public EventAttackSlowdown(final boolean sprint, final double slowDown) {
        this.slowDown = 0.6;
        this.sprint = sprint;
        this.slowDown = slowDown;
    }
    
    public boolean isSprint() {
        return this.sprint;
    }
    
    public void setSprint(final boolean sprint) {
        this.sprint = sprint;
    }
    
    public double getSlowDown() {
        return this.slowDown;
    }
    
    public void setSlowDown(final double slowDown) {
        this.slowDown = slowDown;
    }
}
