/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple4d;
import javax.vecmath.VecMathUtil;

public abstract class Tuple4f
implements Serializable,
Cloneable {
    static final long serialVersionUID = 7068460319248845763L;
    public float x;
    public float y;
    public float z;
    public float w;

    public Tuple4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Tuple4f(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public Tuple4f(Tuple4f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public Tuple4f(Tuple4d t1) {
        this.x = (float)t1.x;
        this.y = (float)t1.y;
        this.z = (float)t1.z;
        this.w = (float)t1.w;
    }

    public Tuple4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public final void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public final void set(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
        this.w = t[3];
    }

    public final void set(Tuple4f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
        this.w = t1.w;
    }

    public final void set(Tuple4d t1) {
        this.x = (float)t1.x;
        this.y = (float)t1.y;
        this.z = (float)t1.z;
        this.w = (float)t1.w;
    }

    public final void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
        t[3] = this.w;
    }

    public final void get(Tuple4f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
        t.w = this.w;
    }

    public final void add(Tuple4f t1, Tuple4f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
        this.w = t1.w + t2.w;
    }

    public final void add(Tuple4f t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
        this.w += t1.w;
    }

    public final void sub(Tuple4f t1, Tuple4f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
        this.w = t1.w - t2.w;
    }

    public final void sub(Tuple4f t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
        this.w -= t1.w;
    }

    public final void negate(Tuple4f t1) {
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

    public final void scale(float s, Tuple4f t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
        this.z = s * t1.z;
        this.w = s * t1.w;
    }

    public final void scale(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        this.w *= s;
    }

    public final void scaleAdd(float s, Tuple4f t1, Tuple4f t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
        this.w = s * t1.w + t2.w;
    }

    public final void scaleAdd(float s, Tuple4f t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
        this.z = s * this.z + t1.z;
        this.w = s * this.w + t1.w;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }

    public boolean equals(Tuple4f t1) {
        try {
            return this.x == t1.x && this.y == t1.y && this.z == t1.z && this.w == t1.w;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object t1) {
        try {
            Tuple4f t2 = (Tuple4f)t1;
            return this.x == t2.x && this.y == t2.y && this.z == t2.z && this.w == t2.w;
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e1) {
            return false;
        }
    }

    public boolean epsilonEquals(Tuple4f t1, float epsilon) {
        float diff = this.x - t1.x;
        if (Float.isNaN(diff)) {
            return false;
        }
        float f = diff < 0.0f ? -diff : diff;
        if (f > epsilon) {
            return false;
        }
        diff = this.y - t1.y;
        if (Float.isNaN(diff)) {
            return false;
        }
        float f2 = diff < 0.0f ? -diff : diff;
        if (f2 > epsilon) {
            return false;
        }
        diff = this.z - t1.z;
        if (Float.isNaN(diff)) {
            return false;
        }
        float f3 = diff < 0.0f ? -diff : diff;
        if (f3 > epsilon) {
            return false;
        }
        diff = this.w - t1.w;
        if (Float.isNaN(diff)) {
            return false;
        }
        float f4 = diff < 0.0f ? -diff : diff;
        return !(f4 > epsilon);
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashFloatBits(bits, this.x);
        bits = VecMathUtil.hashFloatBits(bits, this.y);
        bits = VecMathUtil.hashFloatBits(bits, this.z);
        bits = VecMathUtil.hashFloatBits(bits, this.w);
        return VecMathUtil.hashFinish(bits);
    }

    public final void clamp(float min, float max, Tuple4f t) {
        this.x = t.x > max ? max : (t.x < min ? min : t.x);
        this.y = t.y > max ? max : (t.y < min ? min : t.y);
        this.z = t.z > max ? max : (t.z < min ? min : t.z);
        this.w = t.w > max ? max : (t.w < min ? min : t.w);
    }

    public final void clampMin(float min, Tuple4f t) {
        this.x = t.x < min ? min : t.x;
        this.y = t.y < min ? min : t.y;
        this.z = t.z < min ? min : t.z;
        this.w = t.w < min ? min : t.w;
    }

    public final void clampMax(float max, Tuple4f t) {
        this.x = t.x > max ? max : t.x;
        this.y = t.y > max ? max : t.y;
        this.z = t.z > max ? max : t.z;
        this.w = t.w > max ? max : t.z;
    }

    public final void absolute(Tuple4f t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
        this.z = Math.abs(t.z);
        this.w = Math.abs(t.w);
    }

    public final void clamp(float min, float max) {
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

    public void interpolate(Tuple4f t1, Tuple4f t2, float alpha) {
        this.x = (1.0f - alpha) * t1.x + alpha * t2.x;
        this.y = (1.0f - alpha) * t1.y + alpha * t2.y;
        this.z = (1.0f - alpha) * t1.z + alpha * t2.z;
        this.w = (1.0f - alpha) * t1.w + alpha * t2.w;
    }

    public void interpolate(Tuple4f t1, float alpha) {
        this.x = (1.0f - alpha) * this.x + alpha * t1.x;
        this.y = (1.0f - alpha) * this.y + alpha * t1.y;
        this.z = (1.0f - alpha) * this.z + alpha * t1.z;
        this.w = (1.0f - alpha) * this.w + alpha * t1.w;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
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

    public final float getW() {
        return this.w;
    }

    public final void setW(float w) {
        this.w = w;
    }
}

