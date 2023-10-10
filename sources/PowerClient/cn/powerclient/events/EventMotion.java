/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

import me.AveReborn.events.Event;

public class EventMotion
extends Event {
    private boolean isPre;
    private float yaw;
    private float pitch;
    private double y;
    private boolean onground;
    private boolean alwaysSend;

    public void fire(double y2, float yaw, float pitch, boolean ground) {
        this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y2;
        this.onground = ground;
        super.fire();
    }

    @Override
    public void fire() {
        this.isPre = false;
        super.fire();
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

    public float getPitch() {
        return this.pitch;
    }

    public double getY() {
        return this.y;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public boolean shouldAlwaysSend() {
        return this.alwaysSend;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setY(double y2) {
        this.y = y2;
    }

    public void setGround(boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }
}

