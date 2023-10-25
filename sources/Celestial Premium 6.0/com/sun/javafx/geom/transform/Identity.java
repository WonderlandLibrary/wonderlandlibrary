/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;

public final class Identity
extends BaseTransform {
    @Override
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.IDENTITY;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public boolean isIdentity() {
        return true;
    }

    @Override
    public boolean isTranslateOrIdentity() {
        return true;
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public double getDeterminant() {
        return 1.0;
    }

    @Override
    public Point2D transform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Identity.makePoint(point2D, point2D2);
        }
        point2D2.setLocation(point2D);
        return point2D2;
    }

    @Override
    public Point2D inverseTransform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = Identity.makePoint(point2D, point2D2);
        }
        point2D2.setLocation(point2D);
        return point2D2;
    }

    @Override
    public Vec3d transform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d deltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d inverseTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public Vec3d inverseDeltaTransform(Vec3d vec3d, Vec3d vec3d2) {
        if (vec3d2 == null) {
            return new Vec3d(vec3d);
        }
        vec3d2.set(vec3d);
        return vec3d2;
    }

    @Override
    public void transform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void transform(double[] arrd, int n, double[] arrd2, int n2, int n3) {
        if (arrd != arrd2 || n != n2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
        }
    }

    @Override
    public void transform(float[] arrf, int n, double[] arrd, int n2, int n3) {
        for (int i = 0; i < n3; ++i) {
            arrd[n2++] = arrf[n++];
            arrd[n2++] = arrf[n++];
        }
    }

    @Override
    public void transform(double[] arrd, int n, float[] arrf, int n2, int n3) {
        for (int i = 0; i < n3; ++i) {
            arrf[n2++] = (float)arrd[n++];
            arrf[n2++] = (float)arrd[n++];
        }
    }

    @Override
    public void deltaTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void deltaTransform(double[] arrd, int n, double[] arrd2, int n2, int n3) {
        if (arrd != arrd2 || n != n2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
        }
    }

    @Override
    public void inverseTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void inverseDeltaTransform(float[] arrf, int n, float[] arrf2, int n2, int n3) {
        if (arrf != arrf2 || n != n2) {
            System.arraycopy(arrf, n, arrf2, n2, n3 * 2);
        }
    }

    @Override
    public void inverseTransform(double[] arrd, int n, double[] arrd2, int n2, int n3) {
        if (arrd != arrd2 || n != n2) {
            System.arraycopy(arrd, n, arrd2, n2, n3 * 2);
        }
    }

    @Override
    public BaseBounds transform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (baseBounds2 != baseBounds) {
            baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
        }
        return baseBounds2;
    }

    @Override
    public void transform(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public BaseBounds inverseTransform(BaseBounds baseBounds, BaseBounds baseBounds2) {
        if (baseBounds2 != baseBounds) {
            baseBounds2 = baseBounds2.deriveWithNewBounds(baseBounds);
        }
        return baseBounds2;
    }

    @Override
    public void inverseTransform(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2 != rectangle) {
            rectangle2.setBounds(rectangle);
        }
    }

    @Override
    public Shape createTransformedShape(Shape shape) {
        return new Path2D(shape);
    }

    @Override
    public void setToIdentity() {
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (!baseTransform.isIdentity()) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public void invert() {
    }

    @Override
    public void restoreTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        if (d != 1.0 || d2 != 0.0 || d3 != 0.0 || d4 != 1.0 || d5 != 0.0 || d6 != 0.0) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public void restoreTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        if (d != 1.0 || d2 != 0.0 || d3 != 0.0 || d4 != 0.0 || d5 != 0.0 || d6 != 1.0 || d7 != 0.0 || d8 != 0.0 || d9 != 0.0 || d10 != 0.0 || d11 != 1.0 || d12 != 0.0) {
            Identity.degreeError(BaseTransform.Degree.IDENTITY);
        }
    }

    @Override
    public BaseTransform deriveWithTranslation(double d, double d2) {
        return Translate2D.getInstance(d, d2);
    }

    @Override
    public BaseTransform deriveWithPreTranslation(double d, double d2) {
        return Translate2D.getInstance(d, d2);
    }

    @Override
    public BaseTransform deriveWithTranslation(double d, double d2, double d3) {
        if (d3 == 0.0) {
            if (d == 0.0 && d2 == 0.0) {
                return this;
            }
            return new Translate2D(d, d2);
        }
        Affine3D affine3D = new Affine3D();
        affine3D.translate(d, d2, d3);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithScale(double d, double d2, double d3) {
        if (d3 == 1.0) {
            if (d == 1.0 && d2 == 1.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            affine2D.scale(d, d2);
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.scale(d, d2, d3);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithRotation(double d, double d2, double d3, double d4) {
        if (d == 0.0) {
            return this;
        }
        if (Identity.almostZero(d2) && Identity.almostZero(d3)) {
            if (d4 == 0.0) {
                return this;
            }
            Affine2D affine2D = new Affine2D();
            if (d4 > 0.0) {
                affine2D.rotate(d);
            } else if (d4 < 0.0) {
                affine2D.rotate(-d);
            }
            return affine2D;
        }
        Affine3D affine3D = new Affine3D();
        affine3D.rotate(d, d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d, double d2, double d3, double d4, double d5, double d6) {
        return Identity.getInstance(d, d2, d3, d4, d5, d6);
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        return Identity.getInstance(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    @Override
    public BaseTransform deriveWithConcatenation(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform deriveWithPreConcatenation(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform deriveWithNewTransform(BaseTransform baseTransform) {
        return Identity.getInstance(baseTransform);
    }

    @Override
    public BaseTransform createInverse() {
        return this;
    }

    @Override
    public String toString() {
        return "Identity[]";
    }

    @Override
    public BaseTransform copy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof BaseTransform && ((BaseTransform)object).isIdentity();
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

