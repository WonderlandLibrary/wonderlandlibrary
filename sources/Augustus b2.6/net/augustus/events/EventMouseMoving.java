// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventMouseMoving extends Event
{
    private int deltaX;
    private int deltaY;
    
    public EventMouseMoving(final int deltaX, final int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    
    public int getDeltaX() {
        return this.deltaX;
    }
    
    public void setDeltaX(final int deltaX) {
        this.deltaX = deltaX;
    }
    
    public int getDeltaY() {
        return this.deltaY;
    }
    
    public void setDeltaY(final int deltaY) {
        this.deltaY = deltaY;
    }
}
