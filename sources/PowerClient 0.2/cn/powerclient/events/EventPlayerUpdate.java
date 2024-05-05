/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

import com.darkmagician6.eventapi.events.Event;

public class EventPlayerUpdate
implements Event {
    public float yaw;
    public float pitch;
    public double x;
    public double y;
    public double z;
    public boolean onGround;

    public EventPlayerUpdate(float yaw, float pitch, double x2, double y2, double z2, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y2;
        this.x = x2;
        this.z = z2;
        this.onGround = onGround;
    }
}

