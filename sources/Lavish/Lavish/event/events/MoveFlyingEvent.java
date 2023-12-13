// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import Lavish.event.Event;

public class MoveFlyingEvent extends Event<MoveFlyingEvent>
{
    public float yaw;
    
    public MoveFlyingEvent(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getYaw() {
        return this.yaw;
    }
}
