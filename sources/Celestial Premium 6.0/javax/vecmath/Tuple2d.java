/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple2f;
import javax.vecmath.VecMathUtil;

public abstract class Tuple2d
implements Serializable,
Cloneable {
    static final long serialVersionUID = 6205762482756093838L;
    public double x;
    public double y;

    public Tuple2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Tuple2d(double[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public Tuple2d(Tuple2d t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public Tuple2d(Tuple2f t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public Tuple2d() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public final void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public final void set(double[] t) {
        this.x = t[0];
        this.y = t[1];
    }

    public final void set(Tuple2d t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public final void set(Tuple2f t1) {
        this.x = t1.x;
        this.y = t1.y;
    }

    public final void get(double[] t) {
        t[0] = this.x;
        t[1] = this.y;
    }

    public final void add(Tuple2d t1, Tuple2d t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
    }

    public final void add(Tuple2d t1) {
        this.x += t1.x;
        this.y += t1.y;
    }

    public final void sub(Tuple2d t1, Tuple2d t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
    }

    public final void sub(Tuple2d t1) {
        this.x -= t1.x;
        this.y -= t1.y;
    }

    public final void negate(Tuple2d t1) {
        this.x = -t1.x;
        this.y = -t1.y;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public final void scale(double s, Tuple2d t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
    }

    public final void scale(double s) {
        this.x *= s;
        this.y *= s;
    }

    public final void scaleAdd(double s, Tuple2d t1, Tuple2d t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
    }

    public final void scaleAdd(double s, Tuple2d t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashDoubleBits(bits, this.x);
        bits = VecMathUtil.hashDoubleBits(bits, this.y);
        return VecMathUtil.hashFinish(bits);
    }

    public boolean equals(Tuple2d t1) {
        try {
            return this.x == t1.x && this.y == t1.y;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object t1) {
        try {
            Tuple2d t2 = (Tuple2d)t1;
            return this.x == t2.x && this.y == t2.y;
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e1) {
            return false;
        }
    }

    public boolean epsilonEquals(Tuple2d t1, double epsilon) {
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
        return !(d2 > epsilon);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public final void clamp(double min, double max, Tuple2d t) {
        this.x = t.x > max ? max : (t.x < min ? min : t.x);
        this.y = t.y > max ? max : (t.y < min ? min : t.y);
    }

    public final void clampMin(double min, Tuple2d t) {
        this.x = t.x < min ? min : t.x;
        this.y = t.y < min ? min : t.y;
    }

    public final void clampMax(double max, Tuple2d t) {
        this.x = t.x > max ? max : t.x;
        this.y = t.y > max ? max : t.y;
    }

    public final void absolute(Tuple2d t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
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
    }

    public final void clampMin(double min) {
        if (this.x < min) {
            this.x = min;
        }
        if (this.y < min) {
            this.y = min;
        }
    }

    public final void clampMax(double max) {
        if (this.x > max) {
            this.x = max;
        }
        if (this.y > max) {
            this.y = max;
        }
    }

    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
    }

    public final void interpolate(Tuple2d t1, Tuple2d t2, double alpha) {
        this.x = (1.0 - alpha) * t1.x + alpha * t2.x;
        this.y = (1.0 - alpha) * t1.y + alpha * t2.y;
    }

    public final void interpolate(Tuple2d t1, double alpha) {
        this.x = (1.0 - alpha) * this.x + alpha * t1.x;
        this.y = (1.0 - alpha) * this.y + alpha * t1.y;
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
}

