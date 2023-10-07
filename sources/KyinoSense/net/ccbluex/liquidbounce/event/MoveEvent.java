/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import net.ccbluex.liquidbounce.event.CancellableEvent;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\u0015\u0010\u000e\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0015J\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\b\u0016J\u0006\u0010\u0017\u001a\u00020\u0014J\u0006\u0010\u0018\u001a\u00020\u0014R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000f\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/event/MoveEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "x", "", "y", "z", "(DDD)V", "isSafeWalk", "", "()Z", "setSafeWalk", "(Z)V", "getX", "()D", "setX", "(D)V", "getY", "setY", "getZ", "setZ", "", "setX1", "setZ1", "zero", "zeroXZ", "KyinoClient"})
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

    @JvmName(name="setX1")
    public final void setX1(double x) {
        this.x = x;
    }

    @JvmName(name="setZ1")
    public final void setZ1(double z) {
        this.z = z;
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

