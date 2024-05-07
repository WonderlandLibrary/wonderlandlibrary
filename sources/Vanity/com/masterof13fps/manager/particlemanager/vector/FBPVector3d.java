package com.masterof13fps.manager.particlemanager.vector;

import net.minecraft.util.*;

public class FBPVector3d extends Vector3d
{

    public double x, y, z;

    public FBPVector3d() {
    }

    public FBPVector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FBPVector3d(final FBPVector3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void copyFrom(final Vector3d vec) {
        this.x = vec.field_181059_a;
        this.y = vec.field_181060_b;
        this.z = vec.field_181061_c;
    }

    public void add(final Vector3d vec) {
        this.x += vec.field_181059_a;
        this.y += vec.field_181060_b;
        this.z += vec.field_181061_c;
    }

    public void zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public FBPVector3d partialVec(final FBPVector3d prevRot, final float partialTicks) {
        final FBPVector3d v = new FBPVector3d();
        v.x = prevRot.x + (this.x - prevRot.x) * partialTicks;
        v.y = prevRot.y + (this.y - prevRot.y) * partialTicks;
        v.z = prevRot.z + (this.z - prevRot.z) * partialTicks;
        return v;
    }

    public FBPVector3d multiply(final double d) {
        final FBPVector3d fbpVector3d;
        final FBPVector3d v = fbpVector3d = new FBPVector3d(this);
        fbpVector3d.x *= d;
        final FBPVector3d fbpVector3d2 = v;
        fbpVector3d2.y *= d;
        final FBPVector3d fbpVector3d3 = v;
        fbpVector3d3.z *= d;
        return v;
    }
}
