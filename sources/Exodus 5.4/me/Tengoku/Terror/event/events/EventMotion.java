/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventType;

public class EventMotion
extends Event {
    private static double x;
    private EventType stage;
    private static double lastZ;
    private static double lastY;
    private static float pitch;
    private static double z;
    private static double y;
    private static float yaw;
    public boolean onGround;
    private float lastYaw;
    private float lastPitch;
    private static double lastX;

    public static double getLastY() {
        return lastY;
    }

    public static double getZ() {
        return z;
    }

    public static double getY() {
        return y;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    @Override
    public boolean isPost() {
        return !this.isPre();
    }

    public static float getYaw() {
        return yaw;
    }

    public void setZ(double d) {
        z = d;
    }

    public void setX(double d) {
        x = d;
    }

    @Override
    public boolean isPre() {
        return this.stage.equals((Object)EventType.PRE);
    }

    public static double getX() {
        return x;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public void setLastYaw(float f) {
        this.lastYaw = f;
    }

    public static void setLastZ(double d) {
        lastZ = d;
    }

    public void setYaw(float f) {
        yaw = f;
    }

    public static float getPitch() {
        return pitch;
    }

    public static double getLastZ() {
        return lastZ;
    }

    public EventMotion(double d, double d2, double d3, boolean bl, float f, float f2, float f3, float f4, EventType eventType) {
        x = d;
        y = d2;
        z = d3;
        this.onGround = bl;
        pitch = f2;
        yaw = f;
        this.lastPitch = f4;
        this.lastYaw = f3;
        this.stage = eventType;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setLastPitch(float f) {
        this.lastPitch = f;
    }

    public void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public static double getLastX() {
        return lastX;
    }

    public void setPitch(float f) {
        pitch = f;
    }

    public static void setLastX(double d) {
        lastX = d;
    }

    public static void setLastY(double d) {
        lastY = d;
    }

    public void setY(double d) {
        y = d;
    }
}

