/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.VecMathUtil;
import javax.vecmath.Vector3f;

public class AxisAngle4f
implements Serializable,
Cloneable {
    static final long serialVersionUID = -163246355858070601L;
    public float x;
    public float y;
    public float z;
    public float angle;
    static final double EPS = 1.0E-6;

    public AxisAngle4f(float x, float y, float z, float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    public AxisAngle4f(float[] a) {
        this.x = a[0];
        this.y = a[1];
        this.z = a[2];
        this.angle = a[3];
    }

    public AxisAngle4f(AxisAngle4f a1) {
        this.x = a1.x;
        this.y = a1.y;
        this.z = a1.z;
        this.angle = a1.angle;
    }

    public AxisAngle4f(AxisAngle4d a1) {
        this.x = (float)a1.x;
        this.y = (float)a1.y;
        this.z = (float)a1.z;
        this.angle = (float)a1.angle;
    }

    public AxisAngle4f(Vector3f axis, float angle) {
        this.x = axis.x;
        this.y = axis.y;
        this.z = axis.z;
        this.angle = angle;
    }

    public AxisAngle4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 1.0f;
        this.angle = 0.0f;
    }

    public final void set(float x, float y, float z, float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    public final void set(float[] a) {
        this.x = a[0];
        this.y = a[1];
        this.z = a[2];
        this.angle = a[3];
    }

    public final void set(AxisAngle4f a1) {
        this.x = a1.x;
        this.y = a1.y;
        this.z = a1.z;
        this.angle = a1.angle;
    }

    public final void set(AxisAngle4d a1) {
        this.x = (float)a1.x;
        this.y = (float)a1.y;
        this.z = (float)a1.z;
        this.angle = (float)a1.angle;
    }

    public final void set(Vector3f axis, float angle) {
        this.x = axis.x;
        this.y = axis.y;
        this.z = axis.z;
        this.angle = angle;
    }

    public final void get(float[] a) {
        a[0] = this.x;
        a[1] = this.y;
        a[2] = this.z;
        a[3] = this.angle;
    }

    public final void set(Quat4f q1) {
        double mag = q1.x * q1.x + q1.y * q1.y + q1.z * q1.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double invMag = 1.0 / mag;
            this.x = (float)((double)q1.x * invMag);
            this.y = (float)((double)q1.y * invMag);
            this.z = (float)((double)q1.z * invMag);
            this.angle = (float)(2.0 * Math.atan2(mag, q1.w));
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public final void set(Quat4d q1) {
        double mag = q1.x * q1.x + q1.y * q1.y + q1.z * q1.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double invMag = 1.0 / mag;
            this.x = (float)(q1.x * invMag);
            this.y = (float)(q1.y * invMag);
            this.z = (float)(q1.z * invMag);
            this.angle = (float)(2.0 * Math.atan2(mag, q1.w));
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public final void set(Matrix4f m1) {
        Matrix3f m3f = new Matrix3f();
        m1.get(m3f);
        this.x = m3f.m21 - m3f.m12;
        this.y = m3f.m02 - m3f.m20;
        this.z = m3f.m10 - m3f.m01;
        double mag = this.x * this.x + this.y * this.y + this.z * this.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double sin = 0.5 * mag;
            double cos = 0.5 * ((double)(m3f.m00 + m3f.m11 + m3f.m22) - 1.0);
            this.angle = (float)Math.atan2(sin, cos);
            double invMag = 1.0 / mag;
            this.x = (float)((double)this.x * invMag);
            this.y = (float)((double)this.y * invMag);
            this.z = (float)((double)this.z * invMag);
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public final void set(Matrix4d m1) {
        Matrix3d m3d = new Matrix3d();
        m1.get(m3d);
        this.x = (float)(m3d.m21 - m3d.m12);
        this.y = (float)(m3d.m02 - m3d.m20);
        this.z = (float)(m3d.m10 - m3d.m01);
        double mag = this.x * this.x + this.y * this.y + this.z * this.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double sin = 0.5 * mag;
            double cos = 0.5 * (m3d.m00 + m3d.m11 + m3d.m22 - 1.0);
            this.angle = (float)Math.atan2(sin, cos);
            double invMag = 1.0 / mag;
            this.x = (float)((double)this.x * invMag);
            this.y = (float)((double)this.y * invMag);
            this.z = (float)((double)this.z * invMag);
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public final void set(Matrix3f m1) {
        this.x = m1.m21 - m1.m12;
        this.y = m1.m02 - m1.m20;
        this.z = m1.m10 - m1.m01;
        double mag = this.x * this.x + this.y * this.y + this.z * this.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double sin = 0.5 * mag;
            double cos = 0.5 * ((double)(m1.m00 + m1.m11 + m1.m22) - 1.0);
            this.angle = (float)Math.atan2(sin, cos);
            double invMag = 1.0 / mag;
            this.x = (float)((double)this.x * invMag);
            this.y = (float)((double)this.y * invMag);
            this.z = (float)((double)this.z * invMag);
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public final void set(Matrix3d m1) {
        this.x = (float)(m1.m21 - m1.m12);
        this.y = (float)(m1.m02 - m1.m20);
        this.z = (float)(m1.m10 - m1.m01);
        double mag = this.x * this.x + this.y * this.y + this.z * this.z;
        if (mag > 1.0E-6) {
            mag = Math.sqrt(mag);
            double sin = 0.5 * mag;
            double cos = 0.5 * (m1.m00 + m1.m11 + m1.m22 - 1.0);
            this.angle = (float)Math.atan2(sin, cos);
            double invMag = 1.0 / mag;
            this.x = (float)((double)this.x * invMag);
            this.y = (float)((double)this.y * invMag);
            this.z = (float)((double)this.z * invMag);
        } else {
            this.x = 0.0f;
            this.y = 1.0f;
            this.z = 0.0f;
            this.angle = 0.0f;
        }
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.angle + ")";
    }

    public boolean equals(AxisAngle4f a1) {
        try {
            return this.x == a1.x && this.y == a1.y && this.z == a1.z && this.angle == a1.angle;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object o1) {
        try {
            AxisAngle4f a2 = (AxisAngle4f)o1;
            return this.x == a2.x && this.y == a2.y && this.z == a2.z && this.angle == a2.angle;
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e1) {
            return false;
        }
    }

    public boolean epsilonEquals(AxisAngle4f a1, float epsilon) {
        float diff = this.x - a1.x;
        float f = diff < 0.0f ? -diff : diff;
        if (f > epsilon) {
            return false;
        }
        diff = this.y - a1.y;
        float f2 = diff < 0.0f ? -diff : diff;
        if (f2 > epsilon) {
            return false;
        }
        diff = this.z - a1.z;
        float f3 = diff < 0.0f ? -diff : diff;
        if (f3 > epsilon) {
            return false;
        }
        diff = this.angle - a1.angle;
        float f4 = diff < 0.0f ? -diff : diff;
        return !(f4 > epsilon);
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashFloatBits(bits, this.x);
        bits = VecMathUtil.hashFloatBits(bits, this.y);
        bits = VecMathUtil.hashFloatBits(bits, this.z);
        bits = VecMathUtil.hashFloatBits(bits, this.angle);
        return VecMathUtil.hashFinish(bits);
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public final float getAngle() {
        return this.angle;
    }

    public final void setAngle(float angle) {
        this.angle = angle;
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final float getY() {
        return this.y;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public final float getZ() {
        return this.z;
    }

    public final void setZ(float z) {
        this.z = z;
    }
}

