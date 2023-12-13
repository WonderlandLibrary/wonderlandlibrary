// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventPlayerStrafe extends Event<EventPlayerStrafe>
{
    private float moveStrafe;
    private float moveForward;
    private boolean jump;
    private boolean sneak;
    
    public EventPlayerStrafe(final float moveStrafe, final float moveForward, final boolean jump, final boolean sneak) {
        this.moveStrafe = moveStrafe;
        this.moveForward = moveForward;
        this.jump = jump;
        this.sneak = sneak;
    }
    
    public float moveStrafe() {
        return this.moveStrafe;
    }
    
    public void moveStrafe(final float moveStrafe) {
        this.moveStrafe = moveStrafe;
    }
    
    public float moveForward() {
        return this.moveForward;
    }
    
    public void moveForward(final float moveForward) {
        this.moveForward = moveForward;
    }
    
    public boolean jump() {
        return this.jump;
    }
    
    public void jump(final boolean jump) {
        this.jump = jump;
    }
    
    public boolean sneak() {
        return this.sneak;
    }
    
    public void sneak(final boolean sneak) {
        this.sneak = sneak;
    }
}
