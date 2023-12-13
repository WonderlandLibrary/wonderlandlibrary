/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.WritableVector3f;

public class Vector3f
extends Vector
implements Serializable,
ReadableVector3f,
WritableVector3f {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;

    public Vector3f() {
    }

    public Vector3f(ReadableVector3f src) {
        this.set(src);
    }

    public Vector3f(float x2, float y2, float z2) {
        this.set(x2, y2, z2);
    }

    public void set(float x2, float y2) {
        this.x = x2;
        this.y = y2;
    }

    public void set(float x2, float y2, float z2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
    }

    public Vector3f set(ReadableVector3f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        return this;
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3f translate(float x2, float y2, float z2) {
        this.x += x2;
        this.y += y2;
        this.z += z2;
        return this;
    }

    public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) {
            return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
        }
        dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
        return dest;
    }

    public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) {
            return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
        }
        dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
        return dest;
    }

    public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }
        dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
        return dest;
    }

    public Vector negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3f negate(Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }

    public Vector3f normalise(Vector3f dest) {
        float l2 = this.length();
        if (dest == null) {
            dest = new Vector3f(this.x / l2, this.y / l2, this.z / l2);
        } else {
            dest.set(this.x / l2, this.y / l2, this.z / l2);
        }
        return dest;
    }

    public static float dot(Vector3f left, Vector3f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }

    public static float angle(Vector3f a2, Vector3f b2) {
        float dls = Vector3f.dot(a2, b2) / (a2.length() * b2.length());
        if (dls < -1.0f) {
            dls = -1.0f;
        } else if (dls > 1.0f) {
            dls = 1.0f;
        }
        return (float)Math.acos(dls);
    }

    public Vector load(FloatBuffer buf) {
        this.x = buf.get();
        this.y = buf.get();
        this.z = buf.get();
        return this;
    }

    public Vector scale(float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }

    public Vector store(FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Vector3f[");
        sb.append(this.x);
        sb.append(", ");
        sb.append(this.y);
        sb.append(", ");
        sb.append(this.z);
        sb.append(']');
        return sb.toString();
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final void setX(float x2) {
        this.x = x2;
    }

    public final void setY(float y2) {
        this.y = y2;
    }

    public void setZ(float z2) {
        this.z = z2;
    }

    public float getZ() {
        return this.z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Vector3f other = (Vector3f)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}

