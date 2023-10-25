/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.CanTransformVec3d;
import com.sun.javafx.geom.transform.Identity;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.geom.transform.Translate2D;

public abstract class BaseTransform
implements CanTransformVec3d {
    public static final BaseTransform IDENTITY_TRANSFORM = new Identity();
    protected static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_IDENTITY = 0;
    public static final int TYPE_TRANSLATION = 1;
    public static final int TYPE_UNIFORM_SCALE = 2;
    public static final int TYPE_GENERAL_SCALE = 4;
    public static final int TYPE_MASK_SCALE = 6;
    public static final int TYPE_FLIP = 64;
    public static final int TYPE_QUADRANT_ROTATION = 8;
    public static final int TYPE_GENERAL_ROTATION = 16;
    public static final int TYPE_MASK_ROTATION = 24;
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    public static final int TYPE_AFFINE2D_MASK = 127;
    public static final int TYPE_AFFINE_3D = 128;
    static final double EPSILON_ABSOLUTE = 1.0E-5;

    static void degreeError(Degree degree) {
        throw new InternalError("does not support higher than " + degree + " operations");
    }

    public static BaseTransform getInstance(BaseTransform baseTransform) {
        if (baseTransform.isIdentity()) {
            return IDENTITY_TRANSFORM;
        }
        if (baseTransform.isTranslateOrIdentity()) {
            return new Translate2D(baseTransform);
        }
        if (baseTransform.is2D()) {
            return new Affine2D(baseTransform);
        }
        return new Affine3D(baseTransform);
    }

    public static BaseTransform getInstance(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        if (d3 == 0.0 && d7 == 0.0 && d9 == 0.0 && d10 == 0.0 && d11 == 1.0 && d12 == 0.0) {
            return BaseTransform.getInstance(d, d5, d2, d6, d4, d8);
        }
        return new Affine3D(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static BaseTransform getInstance(double d, double d2, double d3, double d4, double d5, double d6) {
        if (d == 1.0 && d2 == 0.0 && d3 == 0.0 && d4 == 1.0) {
            return BaseTransform.getTranslateInstance(d5, d6);
        }
        return new Affine2D(d, d2, d3, d4, d5, d6);
    }

    public static BaseTransform getTranslateInstance(double d, double d2) {
        if (d == 0.0 && d2 == 0.0) {
            return IDENTITY_TRANSFORM;
        }
        return new Translate2D(d, d2);
    }

    public static BaseTransform getScaleInstance(double d, double d2) {
        return BaseTransform.getInstance(d, 0.0, 0.0, d2, 0.0, 0.0);
    }

    public static BaseTransform getRotateInstance(double d, double d2, double d3) {
        Affine2D affine2D = new Affine2D();
        affine2D.setToRotation(d, d2, d3);
        return affine2D;
    }

    public abstract Degree getDegree();

    public abstract int getType();

    public abstract boolean isIdentity();

    public abstract boolean isTranslateOrIdentity();

    public abstract boolean is2D();

    public abstract double getDeterminant();

    public double getMxx() {
        return 1.0;
    }

    public double getMxy() {
        return 0.0;
    }

    public double getMxz() {
        return 0.0;
    }

    public double getMxt() {
        return 0.0;
    }

    public double getMyx() {
        return 0.0;
    }

    public double getMyy() {
        return 1.0;
    }

    public double getMyz() {
        return 0.0;
    }

    public double getMyt() {
        return 0.0;
    }

    public double getMzx() {
        return 0.0;
    }

    public double getMzy() {
        return 0.0;
    }

    public double getMzz() {
        return 1.0;
    }

    public double getMzt() {
        return 0.0;
    }

    public abstract Point2D transform(Point2D var1, Point2D var2);

    public abstract Point2D inverseTransform(Point2D var1, Point2D var2) throws NoninvertibleTransformException;

    @Override
    public abstract Vec3d transform(Vec3d var1, Vec3d var2);

    public abstract Vec3d deltaTransform(Vec3d var1, Vec3d var2);

    public abstract Vec3d inverseTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException;

    public abstract Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException;

    public abstract void transform(float[] var1, int var2, float[] var3, int var4, int var5);

    public abstract void transform(double[] var1, int var2, double[] var3, int var4, int var5);

    public abstract void transform(float[] var1, int var2, double[] var3, int var4, int var5);

    public abstract void transform(double[] var1, int var2, float[] var3, int var4, int var5);

    public abstract void deltaTransform(float[] var1, int var2, float[] var3, int var4, int var5);

    public abstract void deltaTransform(double[] var1, int var2, double[] var3, int var4, int var5);

    public abstract void inverseTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException;

    public abstract void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException;

    public abstract void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) throws NoninvertibleTransformException;

    public abstract BaseBounds transform(BaseBounds var1, BaseBounds var2);

    public abstract void transform(Rectangle var1, Rectangle var2);

    public abstract BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) throws NoninvertibleTransformException;

    public abstract void inverseTransform(Rectangle var1, Rectangle var2) throws NoninvertibleTransformException;

    public abstract Shape createTransformedShape(Shape var1);

    public abstract void setToIdentity();

    public abstract void setTransform(BaseTransform var1);

    public abstract void invert() throws NoninvertibleTransformException;

    public abstract void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11);

    public abstract void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

    public abstract BaseTransform deriveWithTranslation(double var1, double var3);

    public abstract BaseTransform deriveWithTranslation(double var1, double var3, double var5);

    public abstract BaseTransform deriveWithScale(double var1, double var3, double var5);

    public abstract BaseTransform deriveWithRotation(double var1, double var3, double var5, double var7);

    public abstract BaseTransform deriveWithPreTranslation(double var1, double var3);

    public abstract BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11);

    public abstract BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

    public abstract BaseTransform deriveWithPreConcatenation(BaseTransform var1);

    public abstract BaseTransform deriveWithConcatenation(BaseTransform var1);

    public abstract BaseTransform deriveWithNewTransform(BaseTransform var1);

    public abstract BaseTransform createInverse() throws NoninvertibleTransformException;

    public abstract BaseTransform copy();

    public int hashCode() {
        if (this.isIdentity()) {
            return 0;
        }
        long l = 0L;
        l = l * 31L + Double.doubleToLongBits(this.getMzz());
        l = l * 31L + Double.doubleToLongBits(this.getMzy());
        l = l * 31L + Double.doubleToLongBits(this.getMzx());
        l = l * 31L + Double.doubleToLongBits(this.getMyz());
        l = l * 31L + Double.doubleToLongBits(this.getMxz());
        l = l * 31L + Double.doubleToLongBits(this.getMyy());
        l = l * 31L + Double.doubleToLongBits(this.getMyx());
        l = l * 31L + Double.doubleToLongBits(this.getMxy());
        l = l * 31L + Double.doubleToLongBits(this.getMxx());
        l = l * 31L + Double.doubleToLongBits(this.getMzt());
        l = l * 31L + Double.doubleToLongBits(this.getMyt());
        l = l * 31L + Double.doubleToLongBits(this.getMxt());
        return (int)l ^ (int)(l >> 32);
    }

    public boolean equals(Object object) {
        if (!(object instanceof BaseTransform)) {
            return false;
        }
        BaseTransform baseTransform = (BaseTransform)object;
        return this.getMxx() == baseTransform.getMxx() && this.getMxy() == baseTransform.getMxy() && this.getMxz() == baseTransform.getMxz() && this.getMxt() == baseTransform.getMxt() && this.getMyx() == baseTransform.getMyx() && this.getMyy() == baseTransform.getMyy() && this.getMyz() == baseTransform.getMyz() && this.getMyt() == baseTransform.getMyt() && this.getMzx() == baseTransform.getMzx() && this.getMzy() == baseTransform.getMzy() && this.getMzz() == baseTransform.getMzz() && this.getMzt() == baseTransform.getMzt();
    }

    static Point2D makePoint(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        return point2D2;
    }

    public static boolean almostZero(double d) {
        return d < 1.0E-5 && d > -1.0E-5;
    }

    public String toString() {
        return "Matrix: degree " + this.getDegree() + "\n" + this.getMxx() + ", " + this.getMxy() + ", " + this.getMxz() + ", " + this.getMxt() + "\n" + this.getMyx() + ", " + this.getMyy() + ", " + this.getMyz() + ", " + this.getMyt() + "\n" + this.getMzx() + ", " + this.getMzy() + ", " + this.getMzz() + ", " + this.getMzt() + "\n";
    }

    public static final class Degree
    extends Enum<Degree> {
        public static final /* enum */ Degree IDENTITY = new Degree();
        public static final /* enum */ Degree TRANSLATE_2D = new Degree();
        public static final /* enum */ Degree AFFINE_2D = new Degree();
        public static final /* enum */ Degree TRANSLATE_3D = new Degree();
        public static final /* enum */ Degree AFFINE_3D = new Degree();
        private static final /* synthetic */ Degree[] $VALUES;

        public static Degree[] values() {
            return (Degree[])$VALUES.clone();
        }

        public static Degree valueOf(String string) {
            return Enum.valueOf(Degree.class, string);
        }

        private static /* synthetic */ Degree[] $values() {
            return new Degree[]{IDENTITY, TRANSLATE_2D, AFFINE_2D, TRANSLATE_3D, AFFINE_3D};
        }

        static {
            $VALUES = Degree.$values();
        }
    }
}

