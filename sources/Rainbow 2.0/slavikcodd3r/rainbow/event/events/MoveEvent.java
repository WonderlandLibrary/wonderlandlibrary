package slavikcodd3r.rainbow.event.events;

import slavikcodd3r.rainbow.event.Event;

public class MoveEvent extends Event
{
    public double x;
    public double y;
    public double z;
    
    public MoveEvent(final double x, final double y2, final double z) {
        this.x = x;
        this.y = y2;
        this.z = z;
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
    
    public void setY(final double y2) {
        this.y = y2;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setXYZ(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
