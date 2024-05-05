/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class EventMove
implements Event {
    public double x;
    public double y;
    public double z;

    public EventMove(double a2, double b2, double c2) {
        this.x = a2;
        this.y = b2;
        this.z = c2;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x2) {
        this.x = x2;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z2) {
        this.z = z2;
    }

    public C02PacketUseEntity getPacket() {
        return null;
    }
}

