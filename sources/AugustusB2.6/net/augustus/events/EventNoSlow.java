// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventNoSlow extends Event
{
    private float moveStrafe;
    private float moveForward;
    private boolean sprint;
    
    public EventNoSlow(final float moveStrafe, final float moveForward) {
        this.moveStrafe = moveStrafe;
        this.moveForward = moveForward;
    }
    
    public float getMoveStrafe() {
        return this.moveStrafe;
    }
    
    public void setMoveStrafe(final float moveStrafe) {
        this.moveStrafe = moveStrafe;
    }
    
    public float getMoveForward() {
        return this.moveForward;
    }
    
    public void setMoveForward(final float moveForward) {
        this.moveForward = moveForward;
    }
    
    public boolean isSprint() {
        return this.sprint;
    }
    
    public void setSprint(final boolean sprint) {
        this.sprint = sprint;
    }
}
