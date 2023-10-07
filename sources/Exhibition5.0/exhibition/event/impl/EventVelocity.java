// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventVelocity extends Event
{
    private double x;
    private double y;
    private double z;
    
    public void fire(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        super.fire();
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
}
