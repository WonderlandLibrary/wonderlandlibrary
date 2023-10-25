/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public class PickRay {
    private Vec3d origin = new Vec3d();
    private Vec3d direction = new Vec3d();
    private double nearClip = 0.0;
    private double farClip = Double.POSITIVE_INFINITY;
    static final double EPS = (double)1.0E-5f;
    private static final double EPSILON_ABSOLUTE = 1.0E-5;

    public PickRay() {
    }

    public PickRay(Vec3d vec3d, Vec3d vec3d2, double d, double d2) {
        this.set(vec3d, vec3d2, d, d2);
    }

    public PickRay(double d, double d2, double d3, double d4, double d5) {
        this.set(d, d2, d3, d4, d5);
    }

    public static PickRay computePerspectivePickRay(double d, double d2, boolean bl, double d3, double d4, double d5, boolean bl2, Affine3D affine3D, double d6, double d7, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        Vec3d vec3d = pickRay.getDirectionNoClone();
        double d8 = d3 / 2.0;
        double d9 = d4 / 2.0;
        double d10 = bl2 ? d9 : d8;
        double d11 = d10 / Math.tan(d5 / 2.0);
        vec3d.x = d - d8;
        vec3d.y = d2 - d9;
        vec3d.z = d11;
        Vec3d vec3d2 = pickRay.getOriginNoClone();
        if (bl) {
            vec3d2.set(0.0, 0.0, 0.0);
        } else {
            vec3d2.set(d8, d9, -d11);
        }
        pickRay.nearClip = d6 * (vec3d.length() / (bl ? d11 : 1.0));
        pickRay.farClip = d7 * (vec3d.length() / (bl ? d11 : 1.0));
        pickRay.transform(affine3D);
        return pickRay;
    }

    public static PickRay computeParallelPickRay(double d, double d2, double d3, Affine3D affine3D, double d4, double d5, PickRay pickRay) {
        if (pickRay == null) {
            pickRay = new PickRay();
        }
        double d6 = d3 / 2.0 / Math.tan(Math.toRadians(15.0));
        pickRay.set(d, d2, d6, d4 * d6, d5 * d6);
        if (affine3D != null) {
            pickRay.transform(affine3D);
        }
        return pickRay;
    }

    public final void set(Vec3d vec3d, Vec3d vec3d2, double d, double d2) {
        this.setOrigin(vec3d);
        this.setDirection(vec3d2);
        this.nearClip = d;
        this.farClip = d2;
    }

    public final void set(double d, double d2, double d3, double d4, double d5) {
        this.setOrigin(d, d2, -d3);
        this.setDirection(0.0, 0.0, d3);
        this.nearClip = d4;
        this.farClip = d5;
    }

    public void setPickRay(PickRay pickRay) {
        this.setOrigin(pickRay.origin);
        this.setDirection(pickRay.direction);
        this.nearClip = pickRay.nearClip;
        this.farClip = pickRay.farClip;
    }

    public PickRay copy() {
        return new PickRay(this.origin, this.direction, this.nearClip, this.farClip);
    }

    public void setOrigin(Vec3d vec3d) {
        this.origin.set(vec3d);
    }

    public void setOrigin(double d, double d2, double d3) {
        this.origin.set(d, d2, d3);
    }

    public Vec3d getOrigin(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        vec3d.set(this.origin);
        return vec3d;
    }

    public Vec3d getOriginNoClone() {
        return this.origin;
    }

    public void setDirection(Vec3d vec3d) {
        this.direction.set(vec3d);
    }

    public void setDirection(double d, double d2, double d3) {
        this.direction.set(d, d2, d3);
    }

    public Vec3d getDirection(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        vec3d.set(this.direction);
        return vec3d;
    }

    public Vec3d getDirectionNoClone() {
        return this.direction;
    }

    public double getNearClip() {
        return this.nearClip;
    }

    public double getFarClip() {
        return this.farClip;
    }

    public double distance(Vec3d vec3d) {
        double d = vec3d.x - this.origin.x;
        double d2 = vec3d.y - this.origin.y;
        double d3 = vec3d.z - this.origin.z;
        return Math.sqrt(d * d + d2 * d2 + d3 * d3);
    }

    public Point2D projectToZeroPlane(BaseTransform baseTransform, boolean bl, Vec3d vec3d, Point2D point2D) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        baseTransform.transform(this.origin, vec3d);
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        vec3d.add(this.origin, this.direction);
        baseTransform.transform(vec3d, vec3d);
        double d4 = vec3d.x - d;
        double d5 = vec3d.y - d2;
        double d6 = vec3d.z - d3;
        if (PickRay.almostZero(d6)) {
            return null;
        }
        double d7 = -d3 / d6;
        if (bl && d7 < 0.0) {
            return null;
        }
        if (point2D == null) {
            point2D = new Point2D();
        }
        point2D.setLocation((float)(d + d4 * d7), (float)(d2 + d5 * d7));
        return point2D;
    }

    static boolean almostZero(double d) {
        return d < 1.0E-5 && d > -1.0E-5;
    }

    private static boolean isNonZero(double d) {
        return d > (double)1.0E-5f || d < (double)-1.0E-5f;
    }

    public void transform(BaseTransform baseTransform) {
        baseTransform.transform(this.origin, this.origin);
        baseTransform.deltaTransform(this.direction, this.direction);
    }

    public void inverseTransform(BaseTransform baseTransform) throws NoninvertibleTransformException {
        baseTransform.inverseTransform(this.origin, this.origin);
        baseTransform.inverseDeltaTransform(this.direction, this.direction);
    }

    public PickRay project(BaseTransform baseTransform, boolean bl, Vec3d vec3d, Point2D point2D) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        baseTransform.transform(this.origin, vec3d);
        double d = vec3d.x;
        double d2 = vec3d.y;
        double d3 = vec3d.z;
        vec3d.add(this.origin, this.direction);
        baseTransform.transform(vec3d, vec3d);
        double d4 = vec3d.x - d;
        double d5 = vec3d.y - d2;
        double d6 = vec3d.z - d3;
        PickRay pickRay = new PickRay();
        pickRay.origin.x = d;
        pickRay.origin.y = d2;
        pickRay.origin.z = d3;
        pickRay.direction.x = d4;
        pickRay.direction.y = d5;
        pickRay.direction.z = d6;
        return pickRay;
    }

    public String toString() {
        return "origin: " + this.origin + "  direction: " + this.direction;
    }
}

