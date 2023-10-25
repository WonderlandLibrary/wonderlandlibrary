/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3d;
import javax.vecmath.VecMathUtil;

public abstract class Tuple3f
implements Serializable,
Cloneable {
    static final long serialVersionUID = 5019834619484343712L;
    public float x;
    public float y;
    public float z;

    public Tuple3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Tuple3f(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public Tuple3f(Tuple3f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public Tuple3f(Tuple3d t1) {
        this.x = (float)t1.x;
        this.y = (float)t1.y;
        this.z = (float)t1.z;
    }

    public Tuple3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public final void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final void set(float[] t) {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public final void set(Tuple3f t1) {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public final void set(Tuple3d t1) {
        this.x = (float)t1.x;
        this.y = (float)t1.y;
        this.z = (float)t1.z;
    }

    public final void get(float[] t) {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public final void get(Tuple3f t) {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public final void add(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public final void add(Tuple3f t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    public final void sub(Tuple3f t1, Tuple3f t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }

    public final void sub(Tuple3f t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }

    public final void negate(Tuple3f t1) {
        this.x = -t1.x;
        this.y = -t1.y;
        this.z = -t1.z;
    }

    public final void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public final void scale(float s, Tuple3f t1) {
        this.x = s * t1.x;
        this.y = s * t1.y;
        this.z = s * t1.z;
    }

    public final void scale(float s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
    }

    public final void scaleAdd(float s, Tuple3f t1, Tuple3f t2) {
        this.x = s * t1.x + t2.x;
        this.y = s * t1.y + t2.y;
        this.z = s * t1.z + t2.z;
    }

    public final void scaleAdd(float s, Tuple3f t1) {
        this.x = s * this.x + t1.x;
        this.y = s * this.y + t1.y;
        this.z = s * this.z + t1.z;
    }

    public boolean equals(Tuple3f t1) {
        try {
            return this.x == t1.x && this.y == t1.y && this.z == t1.z;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean equals(Object t1) {
        try {
            Tuple3f t2 = (Tuple3f)t1;
            return this.x == t2.x && this.y == t2.y && this.z == t2.z;
        }
        catch (NullPointerException e2) {
            return false;
        }
        catch (ClassCastException e1) {
            return false;
        }
    }

    public boolean epsilonEquals(Tuple3f t1, float epsilon) {
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
        return !(f3 > epsilon);
    }

    public int hashCode() {
        long bits = 1L;
        bits = VecMathUtil.hashFloatBits(bits, this.x);
        bits = VecMathUtil.hashFloatBits(bits, this.y);
        bits = VecMathUtil.hashFloatBits(bits, this.z);
        return VecMathUtil.hashFinish(bits);
    }

    public final void clamp(float min, float max, Tuple3f t) {
        this.x = t.x > max ? max : (t.x < min ? min : t.x);
        this.y = t.y > max ? max : (t.y < min ? min : t.y);
        this.z = t.z > max ? max : (t.z < min ? min : t.z);
    }

    public final void clampMin(float min, Tuple3f t) {
        this.x = t.x < min ? min : t.x;
        this.y = t.y < min ? min : t.y;
        this.z = t.z < min ? min : t.z;
    }

    public final void clampMax(float max, Tuple3f t) {
        this.x = t.x > max ? max : t.x;
        this.y = t.y > max ? max : t.y;
        this.z = t.z > max ? max : t.z;
    }

    public final void absolute(Tuple3f t) {
        this.x = Math.abs(t.x);
        this.y = Math.abs(t.y);
        this.z = Math.abs(t.z);
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
    }

    public final void absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
    }

    public final void interpolate(Tuple3f t1, Tuple3f t2, float alpha) {
        this.x = (1.0f - alpha) * t1.x + alpha * t2.x;
        this.y = (1.0f - alpha) * t1.y + alpha * t2.y;
        this.z = (1.0f - alpha) * t1.z + alpha * t2.z;
    }

    public final void interpolate(Tuple3f t1, float alpha) {
        this.x = (1.0f - alpha) * this.x + alpha * t1.x;
        this.y = (1.0f - alpha) * this.y + alpha * t1.y;
        this.z = (1.0f - alpha) * this.z + alpha * t1.z;
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
}

