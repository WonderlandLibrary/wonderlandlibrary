/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.CanTransformVec3d;

public class TransformHelper {
    private TransformHelper() {
    }

    public static BaseBounds general3dBoundsTransform(CanTransformVec3d canTransformVec3d, BaseBounds baseBounds, BaseBounds baseBounds2, Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        double d = baseBounds.getMinX();
        double d2 = baseBounds.getMinY();
        double d3 = baseBounds.getMinZ();
        double d4 = baseBounds.getMaxX();
        double d5 = baseBounds.getMaxY();
        double d6 = baseBounds.getMaxZ();
        vec3d.set(d4, d5, d6);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        double d7 = vec3d.x;
        double d8 = vec3d.y;
        double d9 = vec3d.z;
        double d10 = vec3d.x;
        double d11 = vec3d.y;
        double d12 = vec3d.z;
        vec3d.set(d, d5, d6);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d, d2, d6);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d4, d2, d6);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d, d5, d3);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d4, d5, d3);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d, d2, d3);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        vec3d.set(d4, d2, d3);
        vec3d = canTransformVec3d.transform(vec3d, vec3d);
        if (vec3d.x > d10) {
            d10 = vec3d.x;
        }
        if (vec3d.y > d11) {
            d11 = vec3d.y;
        }
        if (vec3d.z > d12) {
            d12 = vec3d.z;
        }
        if (vec3d.x < d7) {
            d7 = vec3d.x;
        }
        if (vec3d.y < d8) {
            d8 = vec3d.y;
        }
        if (vec3d.z < d9) {
            d9 = vec3d.z;
        }
        return baseBounds2.deriveWithNewBounds((float)d7, (float)d8, (float)d9, (float)d10, (float)d11, (float)d12);
    }
}

