/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;

public class EventVelocity
extends Event {
    private double x;
    private double y;
    private double z;

    public void fire(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        super.fire();
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
}

