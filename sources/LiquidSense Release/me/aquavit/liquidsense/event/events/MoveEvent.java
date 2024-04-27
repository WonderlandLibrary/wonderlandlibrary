package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;

public class MoveEvent extends CancellableEvent {
    public double x;
    public double y;
    public double z;
    public boolean safeWalk;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void zero() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public void zeroXZ() {
        this.x = 0.0D;
        this.z = 0.0D;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setSafeWalk(boolean safeWalk) {
        this.safeWalk = safeWalk;
    }

    public boolean isSafeWalk() {
        return this.safeWalk;
    }
}
