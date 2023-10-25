/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.player;

import org.celestial.client.event.events.callables.EventCancellable;

public class EventPlayerState
extends EventCancellable {
    private final boolean isPre;
    private float yaw;
    private float pitch;
    private double x;
    private double y;
    private double z;
    private boolean onground;

    public EventPlayerState(double x, double y, double z, float yaw, float pitch, boolean ground) {
        this.y = y;
        this.x = x;
        this.z = z;
        this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onground = ground;
    }

    public EventPlayerState() {
        this.isPre = false;
    }

    public boolean isPre() {
        return this.isPre;
    }

    public boolean isPost() {
        return !this.isPre;
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

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isOnGround() {
        return this.onground;
    }

    public void setOnGround(boolean onground) {
        this.onground = onground;
    }
}

