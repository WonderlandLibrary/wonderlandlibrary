/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.injection.backend.MovingObjectPositionImpl;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class AxisAlignedBBImpl
implements IAxisAlignedBB {
    private final AxisAlignedBB wrapped;

    @Override
    public double getMinZ() {
        return this.wrapped.field_72339_c;
    }

    public AxisAlignedBBImpl(AxisAlignedBB axisAlignedBB) {
        this.wrapped = axisAlignedBB;
    }

    @Override
    public double getMinY() {
        return this.wrapped.field_72338_b;
    }

    public final AxisAlignedBB getWrapped() {
        return this.wrapped;
    }

    @Override
    public double getMaxZ() {
        return this.wrapped.field_72334_f;
    }

    @Override
    public IAxisAlignedBB offset(double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72317_d(d, d2, d3);
        boolean bl = false;
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public boolean intersectsWith(IAxisAlignedBB iAxisAlignedBB) {
        IAxisAlignedBB iAxisAlignedBB2 = iAxisAlignedBB;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean bl = false;
        AxisAlignedBB axisAlignedBB2 = ((AxisAlignedBBImpl)iAxisAlignedBB2).getWrapped();
        return axisAlignedBB.func_72326_a(axisAlignedBB2);
    }

    @Override
    public IMovingObjectPosition calculateIntercept(WVec3 wVec3, WVec3 wVec32) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 wVec33 = wVec3;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean bl = false;
        Vec3d vec3d = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        wVec33 = wVec32;
        bl = false;
        Vec3d vec3d2 = new Vec3d(wVec33.getXCoord(), wVec33.getYCoord(), wVec33.getZCoord());
        RayTraceResult rayTraceResult = axisAlignedBB.func_72327_a(vec3d, vec3d2);
        if (rayTraceResult != null) {
            wVec33 = rayTraceResult;
            bl = false;
            iMovingObjectPosition = new MovingObjectPositionImpl((RayTraceResult)wVec33);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    @Override
    public boolean isVecInside(WVec3 wVec3) {
        WVec3 wVec32 = wVec3;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean bl = false;
        Vec3d vec3d = new Vec3d(wVec32.getXCoord(), wVec32.getYCoord(), wVec32.getZCoord());
        return axisAlignedBB.func_72318_a(vec3d);
    }

    @Override
    public IAxisAlignedBB expand(double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72314_b(d, d2, d3);
        boolean bl = false;
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public IAxisAlignedBB addCoord(double d, double d2, double d3) {
        AxisAlignedBB axisAlignedBB = this.wrapped.func_72321_a(d, d2, d3);
        boolean bl = false;
        return new AxisAlignedBBImpl(axisAlignedBB);
    }

    @Override
    public double getMaxX() {
        return this.wrapped.field_72336_d;
    }

    @Override
    public double getMinX() {
        return this.wrapped.field_72340_a;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof AxisAlignedBBImpl && ((AxisAlignedBBImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public double getMaxY() {
        return this.wrapped.field_72337_e;
    }
}

