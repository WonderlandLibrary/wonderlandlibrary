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
    private final double zCoord;
    private final double xCoord;
    private final double yCoord;

    public final WVec3 rotateYaw(float f) {
        boolean bl = false;
        float f2 = (float)Math.cos(f);
        boolean bl2 = false;
        float f3 = (float)Math.sin(f);
        double d = this.xCoord * (double)f2 + this.zCoord * (double)f3;
        double d2 = this.yCoord;
        double d3 = this.zCoord * (double)f2 - this.xCoord * (double)f3;
        return new WVec3(d, d2, d3);
    }

    public final WVec3 addVector(double d, double d2, double d3) {
        boolean bl = false;
        return new WVec3(this.getXCoord() + d, this.getYCoord() + d2, this.getZCoord() + d3);
    }

    public final double getYCoord() {
        return this.yCoord;
    }

    public WVec3(WVec3i wVec3i) {
        this(wVec3i.getX(), wVec3i.getY(), wVec3i.getZ());
    }

    public final double squareDistanceTo(WVec3 wVec3) {
        boolean bl = false;
        double d = wVec3.getXCoord() - this.getXCoord();
        double d2 = wVec3.getYCoord() - this.getYCoord();
        double d3 = wVec3.getZCoord() - this.getZCoord();
        return d * d + d2 * d2 + d3 * d3;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        Object object2 = object;
        if (this.getClass().equals(object2 != null ? object2.getClass() : null) ^ true) {
            return false;
        }
        Object object3 = object;
        if (object3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.WVec3");
        }
        WVec3 cfr_ignored_0 = (WVec3)object3;
        if (this.xCoord != ((WVec3)object).xCoord) {
            return false;
        }
        if (this.yCoord != ((WVec3)object).yCoord) {
            return false;
        }
        return this.zCoord == ((WVec3)object).zCoord;
    }

    public final WVec3 rotatePitch(float f) {
        boolean bl = false;
        float f2 = (float)Math.cos(f);
        boolean bl2 = false;
        float f3 = (float)Math.sin(f);
        double d = this.xCoord;
        double d2 = this.yCoord * (double)f2 + this.zCoord * (double)f3;
        double d3 = this.zCoord * (double)f2 - this.yCoord * (double)f3;
        return new WVec3(d, d2, d3);
    }

    public final double getXCoord() {
        return this.xCoord;
    }

    public final double getZCoord() {
        return this.zCoord;
    }

    public int hashCode() {
        int n = Double.hashCode(this.xCoord);
        n = 31 * n + Double.hashCode(this.yCoord);
        n = 31 * n + Double.hashCode(this.zCoord);
        return n;
    }

    public final WVec3 add(WVec3 wVec3) {
        boolean bl = false;
        WVec3 wVec32 = this;
        double d = wVec3.getXCoord();
        double d2 = wVec3.getYCoord();
        double d3 = wVec3.getZCoord();
        boolean bl2 = false;
        return new WVec3(wVec32.getXCoord() + d, wVec32.getYCoord() + d2, wVec32.getZCoord() + d3);
    }

    public WVec3(double d, double d2, double d3) {
        this.xCoord = d;
        this.yCoord = d2;
        this.zCoord = d3;
    }

    public final double distanceTo(WVec3 wVec3) {
        double d = wVec3.xCoord - this.xCoord;
        double d2 = wVec3.yCoord - this.yCoord;
        double d3 = wVec3.zCoord - this.zCoord;
        double d4 = d * d + d2 * d2 + d3 * d3;
        boolean bl = false;
        return Math.sqrt(d4);
    }
}

