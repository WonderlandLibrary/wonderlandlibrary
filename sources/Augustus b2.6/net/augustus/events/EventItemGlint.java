// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventItemGlint extends Event
{
    private int color;
    private double speed;
    
    public EventItemGlint(final int color, final double speed) {
        this.color = color;
        this.speed = speed;
    }
    
    public double getSpeed() {
        return this.speed;
    }
    
    public void setSpeed(final double speed) {
        this.speed = speed;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
}
