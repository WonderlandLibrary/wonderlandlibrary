package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.CancellableEvent;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\u0000\n\n\b\n\n\b\f\n\n\b\u000020B000¢J0J0R0\bX¢\n\u0000\b\t\"\b\nR0X¢\n\u0000\b\f\r\"\bR0X¢\n\u0000\b\r\"\bR0X¢\n\u0000\b\r\"\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/MoveEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "x", "", "y", "z", "(DDD)V", "isSafeWalk", "", "()Z", "setSafeWalk", "(Z)V", "getX", "()D", "setX", "(D)V", "getY", "setY", "getZ", "setZ", "zero", "", "zeroXZ", "Pride"})
public final class MoveEvent
extends CancellableEvent {
    private boolean isSafeWalk;
    private double x;
    private double y;
    private double z;

    public final boolean isSafeWalk() {
        return this.isSafeWalk;
    }

    public final void setSafeWalk(boolean bl) {
        this.isSafeWalk = bl;
    }

    public final void zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public final void zeroXZ() {
        this.x = 0.0;
        this.z = 0.0;
    }

    public final double getX() {
        return this.x;
    }

    public final void setX(double d) {
        this.x = d;
    }

    public final double getY() {
        return this.y;
    }

    public final void setY(double d) {
        this.y = d;
    }

    public final double getZ() {
        return this.z;
    }

    public final void setZ(double d) {
        this.z = d;
    }

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
