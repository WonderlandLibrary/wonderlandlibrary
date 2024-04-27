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
    public IAxisAlignedBB addCoord(double x, double y, double z) {
        AxisAlignedBB $this$wrap$iv = this.wrapped.func_72321_a(x, y, z);
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    @Override
    public IAxisAlignedBB expand(double x, double y, double z) {
        AxisAlignedBB $this$wrap$iv = this.wrapped.func_72314_b(x, y, z);
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    @Override
    public IMovingObjectPosition calculateIntercept(WVec3 from, WVec3 to) {
        IMovingObjectPosition iMovingObjectPosition;
        WVec3 $this$unwrap$iv;
        WVec3 wVec3 = from;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        $this$unwrap$iv = to;
        $i$f$unwrap = false;
        Vec3d vec3d2 = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        RayTraceResult rayTraceResult = axisAlignedBB.func_72327_a(vec3d, vec3d2);
        if (rayTraceResult != null) {
            RayTraceResult $this$wrap$iv = rayTraceResult;
            boolean $i$f$wrap = false;
            iMovingObjectPosition = new MovingObjectPositionImpl($this$wrap$iv);
        } else {
            iMovingObjectPosition = null;
        }
        return iMovingObjectPosition;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean isVecInside(WVec3 vec) {
        void $this$unwrap$iv;
        WVec3 wVec3 = vec;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        Vec3d vec3d = new Vec3d($this$unwrap$iv.getXCoord(), $this$unwrap$iv.getYCoord(), $this$unwrap$iv.getZCoord());
        return axisAlignedBB.func_72318_a(vec3d);
    }

    @Override
    public IAxisAlignedBB offset(double sx, double sy, double sz) {
        AxisAlignedBB $this$wrap$iv = this.wrapped.func_72317_d(sx, sy, sz);
        boolean $i$f$wrap = false;
        return new AxisAlignedBBImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean intersectsWith(IAxisAlignedBB boundingBox) {
        void $this$unwrap$iv;
        IAxisAlignedBB iAxisAlignedBB = boundingBox;
        AxisAlignedBB axisAlignedBB = this.wrapped;
        boolean $i$f$unwrap = false;
        AxisAlignedBB axisAlignedBB2 = ((AxisAlignedBBImpl)$this$unwrap$iv).getWrapped();
        return axisAlignedBB.func_72326_a(axisAlignedBB2);
    }

    @Override
    public double getMinX() {
        return this.wrapped.field_72340_a;
    }

    @Override
    public double getMinY() {
        return this.wrapped.field_72338_b;
    }

    @Override
    public double getMinZ() {
        return this.wrapped.field_72339_c;
    }

    @Override
    public double getMaxX() {
        return this.wrapped.field_72336_d;
    }

    @Override
    public double getMaxY() {
        return this.wrapped.field_72337_e;
    }

    @Override
    public double getMaxZ() {
        return this.wrapped.field_72334_f;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof AxisAlignedBBImpl && ((AxisAlignedBBImpl)other).wrapped.equals(this.wrapped);
    }

    public final AxisAlignedBB getWrapped() {
        return this.wrapped;
    }

    public AxisAlignedBBImpl(AxisAlignedBB wrapped) {
        this.wrapped = wrapped;
    }
}

