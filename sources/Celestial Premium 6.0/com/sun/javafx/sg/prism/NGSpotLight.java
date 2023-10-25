/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point3D
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.sg.prism.NGPointLight;
import javafx.geometry.Point3D;

public class NGSpotLight
extends NGPointLight {
    private static final Point3D DEFAULT_DIRECTION = new Point3D(0.0, 0.0, 1.0);
    private static final float DEFAULT_INNER_ANGLE = 0.0f;
    private static final float DEFAULT_OUTER_ANGLE = 30.0f;
    private static final float DEFAULT_FALLOFF = 1.0f;
    private Point3D direction = DEFAULT_DIRECTION;
    private final Vec3d effectiveDir = new Vec3d();
    private float innerAngle = 0.0f;
    private float outerAngle = 30.0f;
    private float falloff = 1.0f;

    public static Point3D getDefaultDirection() {
        return DEFAULT_DIRECTION;
    }

    public static float getDefaultInnerAngle() {
        return 0.0f;
    }

    public static float getDefaultOuterAngle() {
        return 30.0f;
    }

    public static float getDefaultFalloff() {
        return 1.0f;
    }

    @Override
    public Point3D getDirection() {
        Vec3d vec3d = new Vec3d(this.direction.getX(), this.direction.getY(), this.direction.getZ());
        this.getWorldTransform().deltaTransform(vec3d, this.effectiveDir);
        return new Point3D(this.effectiveDir.x, this.effectiveDir.y, this.effectiveDir.z);
    }

    public void setDirection(Point3D point3D) {
        if (!this.direction.equals((Object)point3D)) {
            this.direction = point3D;
            this.visualsChanged();
        }
    }

    @Override
    public float getInnerAngle() {
        return this.innerAngle;
    }

    public void setInnerAngle(float f) {
        if (this.innerAngle != f) {
            this.innerAngle = f;
            this.visualsChanged();
        }
    }

    @Override
    public float getOuterAngle() {
        return this.outerAngle;
    }

    public void setOuterAngle(float f) {
        if (this.outerAngle != f) {
            this.outerAngle = f;
            this.visualsChanged();
        }
    }

    @Override
    public float getFalloff() {
        return this.falloff;
    }

    public void setFalloff(float f) {
        if (this.falloff != f) {
            this.falloff = f;
            this.visualsChanged();
        }
    }
}

