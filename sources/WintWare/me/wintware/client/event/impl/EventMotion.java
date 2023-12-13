/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;

public class EventMotion
implements Event {
    private final boolean isPre;
    private float yaw;
    private float pitch;
    private double x;
    private double y;
    private double z;
    private boolean onground;
    private boolean alwaysSend;
    private boolean sneaking;
    public static float YAW;
    public static float PITCH;
    public static float PREVYAW;
    public static float PREVPITCH;
    public static boolean SNEAKING;

    public EventMotion(double x, double y, double z, float yaw, float pitch, boolean sneaking, boolean ground) {
        this.y = y;
        this.x = x;
        this.z = z;
        this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onground = ground;
        this.sneaking = sneaking;
    }

    public EventMotion() {
        PREVYAW = YAW;
        PREVPITCH = PITCH;
        YAW = this.yaw;
        PITCH = this.pitch;
        SNEAKING = this.sneaking;
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

    public float getPitch() {
        return this.pitch;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setGround(boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }

    public void setOnGround(boolean onground) {
        this.onground = onground;
    }

    public boolean isRotating() {
        double yaw = this.yaw - this.yaw;
        double pitch = this.pitch - this.pitch;
        return Math.abs(yaw) != 0.0 || Math.abs(pitch) != 0.0;
    }
}

