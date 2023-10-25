/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple4f;
import javax.vecmath.VecMathUtil;

public abstract class Tuple4d
implements Serializable,
Cloneable {
    static final long serialVersionUID = -4748953690425311052L;
    public double x;
    public double y;
    public double z;
    public double w;

    public Tuple4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Tuple4d(double[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public Tuple4d(Tuple4d t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public Tuple4d(Tuple4f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public Tuple4d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = 0.0;
    }

    public final void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public final void set(double[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public final void set(Tuple4d t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public final void set(Tuple4f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public final void get(double[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
        t[3] = this.w;
    }

    public final void get(Tuple4d t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
        t.w = this.w;
    }

    public final void add(Tuple4d t1, Tuple4d t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
        this.w = t1.w + t2.w;
    }

    public final void add(Tuple4d t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
        this.w += t1.w;
    }

    public final void sub(Tuple4d t1, Tuple4d t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
        this.w = t1.w - t2.w;
    }

    public final void sub(Tuple4d t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
        this.w -= t1.w;
    }

    public final void negate(Tuple4d t1) {
        this.x = -t1.x;
        this.y = -t1.y;
        this.z = -t1.z;
        this.w = -t1.w;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }

    public final void scale(double s, Tuple4d t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
        this.z = s * t1.z;
        this.w = s * t1.w;
    }

    public final void scale(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
    }

    public final void scaleAdd(double s, Tuple4d t1, Tuple4d t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
        this.w = s * t1.w + t2.w;
    }

    public final void scaleAdd(float s, Tuple4d t1) {
        this.scaleAdd((double)s, t1);
    }

    public final void scaleAdd(double s, Tuple4d t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
        this.z = s * this.z + t1.z;
        this.w = s * this.w + t1.w;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }

    public boolean equals(Tuple4d t1) {
        try {
            return this.x == t1.x && this.y == t1.y && this.z == t1.z && this.w == t1.w;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object t1) {
        try {
            Tuple4d t2 = (Tuple4d)t1;
            return this.x == t2.x && this.y == t2.y && this.z == t2.z && this.w == t2.w;
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e1) {
            return false;
        }
    }

    public boolean epsilonEquals(Tuple4d t1, double epsilon) {
        double diff = this.x - t1.x;
        if (Double.isNaN(diff)) {
            return false;
        }
        double d = diff < 0.0 ? -diff : diff;
        if (d > epsilon) {
            return false;
        }
        diff = this.y - t1.y;
        if (Double.isNaN(diff)) {
            return false;
        }
        double d2 = diff < 0.0 ? -diff : diff;
        if (d2 > epsilon) {
            return false;
        }
        diff = this.z - t1.z;
        if (Double.isNaN(diff)) {
            return false;
        }
        double d3 = diff < 0.0 ? -diff : diff;
        if (d3 > epsilon) {
            return false;
        }
        diff = this.w - t1.w;
        if (Double.isNaN(diff)) {
            return false;
        }
        double d4 = diff < 0.0 ? -diff : diff;
        return !(d4 > epsilon);
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashDoubleBits(bits, this.x);
        bits = VecMathUtil.hashDoubleBits(bits, this.y);
        bits = VecMathUtil.hashDoubleBits(bits, this.z);
        bits = VecMathUtil.hashDoubleBits(bits, this.w);
        return VecMathUtil.hashFinish(bits);
    }

    public final void clamp(float min, float max, Tuple4d t) {
        this.clamp((double)min, (double)max, t);
    }

    public final void clamp(double min, double max, Tuple4d t) {
        this.x = t.x > max ? max : (t.x < min ? min : t.x);
        this.y = t.y > max ? max : (t.y < min ? min : t.y);
        this.z = t.z > max ? max : (t.z < min ? min : t.z);
        this.w = t.w > max ? max : (t.w < min ? min : t.w);
    }

    public final void clampMin(float min, Tuple4d t) {
        this.clampMin((double)min, t);
    }

    public final void clampMin(double min, Tuple4d t) {
        this.x = t.x < min ? min : t.x;
        this.y = t.y < min ? min : t.y;
        this.z = t.z < min ? min : t.z;
        this.w = t.w < min ? min : t.w;
    }

    public final void clampMax(float max, Tuple4d t) {
        this.clampMax((double)max, t);
    }

    public final void clampMax(double max, Tuple4d t) {
        this.x = t.x > max ? max : t.x;
        this.y = t.y > max ? max : t.y;
        this.z = t.z > max ? max : t.z;
        this.w = t.w > max ? max : t.z;
    }

    public final void absolute(Tuple4d t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
        this.z = Math.abs(t.z);
        this.w = Math.abs(t.w);
    }

    public final void clamp(float min, float max) {
        this.clamp((double)min, (double)max);
    }

    public final void clamp(double min, double max) {
        if (this.x > max) {
            this.x = max;
        } else if (this.x < min) {
            this.x = min;
        }
        if (this.y > max) {
            this.y = max;
        } else if (this.y < min) {
            this.y = min;
        }
        if (this.z > max) {
            this.z = max;
        } else if (this.z < min) {
            this.z = min;
        }
        if (this.w > max) {
            this.w = max;
        } else if (this.w < min) {
            this.w = min;
        }
    }

    public final void clampMin(float min) {
        this.clampMin((double)min);
    }

    public final void clampMin(double min) {
        if (this.x < min) {
            this.x = min;
        }
        if (this.y < min) {
            this.y = min;
        }
        if (this.z < min) {
            this.z = min;
        }
        if (this.w < min) {
            this.w = min;
        }
    }

    public final void clampMax(float max) {
        this.clampMax((double)max);
    }

    public final void clampMax(double max) {
        if (this.x > max) {
            this.x = max;
        }
        if (this.y > max) {
            this.y = max;
        }
        if (this.z > max) {
            this.z = max;
        }
        if (this.w > max) {
            this.w = max;
        }
    }

    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
    }

    public void interpolate(Tuple4d t1, Tuple4d t2, float alpha) {
        this.interpolate(t1, t2, (double)alpha);
    }

    public void interpolate(Tuple4d t1, Tuple4d t2, double alpha) {
        this.x = (1.0 - alpha) * t1.x + alpha * t2.x;
        this.y = (1.0 - alpha) * t1.y + alpha * t2.y;
        this.z = (1.0 - alpha) * t1.z + alpha * t2.z;
        this.w = (1.0 - alpha) * t1.w + alpha * t2.w;
    }

    public void interpolate(Tuple4d t1, float alpha) {
        this.interpolate(t1, (double)alpha);
    }

    public void interpolate(Tuple4d t1, double alpha) {
        this.x = (1.0 - alpha) * this.x + alpha * t1.x;
        this.y = (1.0 - alpha) * this.y + alpha * t1.y;
        this.z = (1.0 - alpha) * this.z + alpha * t1.z;
        this.w = (1.0 - alpha) * this.w + alpha * t1.w;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public final double getX() {
        return this.x;
    }

    public final void setX(double x) {
        this.x = x;
    }

    public final double getY() {
        return this.y;
    }

    public final void setY(double y) {
        this.y = y;
    }

    public final double getZ() {
        return this.z;
    }

    public final void setZ(double z) {
        this.z = z;
    }

    public final double getW() {
        return this.w;
    }

    public final void setW(double w) {
        this.w = w;
    }
}

