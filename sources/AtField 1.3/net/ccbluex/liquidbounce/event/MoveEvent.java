/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class MoveEvent
extends CancellableEvent {
    private double z;
    private double y;
    private boolean isSafeWalk;
    private double x;

    public final void setSafeWalk(boolean bl) {
        this.isSafeWalk = bl;
    }

    public final void setY(double d) {
        this.y = d;
    }

    public final void zeroXZ() {
        this.x = 0.0;
        this.z = 0.0;
    }

    public final double getX() {
        return this.x;
    }

    public final void setZ(double d) {
        this.z = d;
    }

    public MoveEvent(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public final double getZ() {
        return this.z;
    }

    public final double getY() {
        return this.y;
    }

    public final void zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public final void setX(double d) {
        this.x = d;
    }

    public final boolean isSafeWalk() {
        return this.isSafeWalk;
    }
}

