/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import org.jetbrains.annotations.Nullable;

public final class WVec3 {
    private final double xCoord;
    private final double yCoord;
    private final double zCoord;

    public final WVec3 addVector(double x, double y, double z) {
        int $i$f$addVector = 0;
        return new WVec3(this.getXCoord() + x, this.getYCoord() + y, this.getZCoord() + z);
    }

    public final double distanceTo(WVec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        double d = d0 * d0 + d1 * d1 + d2 * d2;
        boolean bl = false;
        return Math.sqrt(d);
    }

    public final double squareDistanceTo(WVec3 vec) {
        int $i$f$squareDistanceTo = 0;
        double d0 = vec.getXCoord() - this.getXCoord();
        double d1 = vec.getYCoord() - this.getYCoord();
        double d2 = vec.getZCoord() - this.getZCoord();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /*
     * WARNING - void declaration
     */
    public final WVec3 add(WVec3 vec) {
        void y$iv;
        void x$iv;
        void this_$iv;
        int $i$f$add = 0;
        WVec3 wVec3 = this;
        double d = vec.getXCoord();
        double d2 = vec.getYCoord();
        double z$iv = vec.getZCoord();
        boolean $i$f$addVector = false;
        return new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
    }

    public final WVec3 rotatePitch(float pitch) {
        boolean bl = false;
        float f = (float)Math.cos(pitch);
        boolean bl2 = false;
        float f1 = (float)Math.sin(pitch);
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.yCoord * (double)f1;
        return new WVec3(d0, d1, d2);
    }

    public final WVec3 rotateYaw(float yaw) {
        boolean bl = false;
        float f = (float)Math.cos(yaw);
        boolean bl2 = false;
        float f1 = (float)Math.sin(yaw);
        double d0 = this.xCoord * (double)f + this.zCoord * (double)f1;
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double)f - this.xCoord * (double)f1;
        return new WVec3(d0, d1, d2);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        Object object = other;
        if (this.getClass().equals(object != null ? object.getClass() : null) ^ true) {
            return false;
        }
        Object object2 = other;
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3");
        }
        WVec3 cfr_ignored_0 = (WVec3)object2;
        if (this.xCoord != ((WVec3)other).xCoord) {
            return false;
        }
        if (this.yCoord != ((WVec3)other).yCoord) {
            return false;
        }
        return this.zCoord == ((WVec3)other).zCoord;
    }

    public int hashCode() {
        int result = Double.hashCode(this.xCoord);
        result = 31 * result + Double.hashCode(this.yCoord);
        result = 31 * result + Double.hashCode(this.zCoord);
        return result;
    }

    public final double getXCoord() {
        return this.xCoord;
    }

    public final double getYCoord() {
        return this.yCoord;
    }

    public final double getZCoord() {
        return this.zCoord;
    }

    public WVec3(double xCoord, double yCoord, double zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    public WVec3(WVec3i blockPos) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}

