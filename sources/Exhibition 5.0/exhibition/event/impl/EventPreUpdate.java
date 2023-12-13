// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventPreUpdate extends Event
{
    private float yaw;
    private float pitch;
    private double y;
    private boolean onground;
    
    public void fire(final double y, final float yaw, final float pitch, final boolean ground) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.onground = ground;
        super.fire();
    }
}
