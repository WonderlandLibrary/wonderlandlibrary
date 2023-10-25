/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class MovementEvent
extends Event<MovementEvent> {
    private double motionX;
    private double motionY;
    private double motionZ;

    public double getMotionY() {
        return this.motionY;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public void setMotionY(double d) {
        this.motionY = d;
    }

    public MovementEvent(double d, double d2, double d3) {
        this.motionX = d;
        this.motionY = d2;
        this.motionZ = d3;
    }

    public void setMotionZ(double d) {
        this.motionZ = d;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionX(double d) {
        this.motionX = d;
    }
}

