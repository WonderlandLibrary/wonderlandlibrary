/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import me.wintware.client.utils.movement.Location;
import net.minecraft.network.play.client.CPacketPlayer;

public class EventPreMotionNew
implements Event {
    private boolean cancel;
    public float yaw;
    public float pitch;
    public double y;
    private CPacketPlayer.Rotation rotation;
    private final Location location;

    public EventPreMotionNew(float yaw, float pitch, double y, Location location) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.location = location;
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public CPacketPlayer.Rotation getRotation() {
        return this.rotation;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public EventPreMotionNew getLocation() {
        return null;
    }

    public double getLegitMotion() {
        return 0.42f;
    }
}

