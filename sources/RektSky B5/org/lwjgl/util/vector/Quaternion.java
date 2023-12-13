/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.vector;

import java.nio.FloatBuffer;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector4f;

public class Quaternion
extends Vector
implements ReadableVector4f {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion() {
        this.setIdentity();
    }

    public Quaternion(ReadableVector4f src) {
        this.set(src);
    }

    public Quaternion(float x2, float y2, float z2, float w2) {
        this.set(x2, y2, z2, w2);
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

    public void set(float x2, float y2, float z2, float w2) {
        this.x = x2;
        this.y = y2;
        this.z = z2;
        this.w = w2;
    }

    public Quaternion set(ReadableVector4f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        this.w = src.getW();
        return this;
    }

    public Quaternion setIdentity() {
        return Quaternion.setIdentity(this);
    }

    public static Quaternion setIdentity(Quaternion q2) {
        q2.x = 0.0f;
        q2.y = 0.0f;
        q2.z = 0.0f;
        q2.w = 1.0f;
        return q2;
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public static Quaternion normalise(Quaternion src, Quaternion dest) {
        float inv_l = 1.0f / src.length();
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set(src.x * inv_l, src.y * inv_l, src.z * inv_l, src.w * inv_l);
        return dest;
    }

    public Quaternion normalise(Quaternion dest) {
        return Quaternion.normalise(this, dest);
    }

    public static float dot(Quaternion left, Quaternion right) {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }

    public Quaternion negate(Quaternion dest) {
        return Quaternion.negate(this, dest);
    }

    public static Quaternion negate(Quaternion src, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = src.w;
        return dest;
    }

    public Vector negate() {
        return Quaternion.negate(this, this);
    }

    public Vector load(FloatBuffer buf) {
        this.x = buf.get();
        this.y = buf.get();
        this.z = buf.get();
        this.w = buf.get();
        return this;
    }

    public Vector scale(float scale) {
        return Quaternion.scale(scale, this, this);
    }

    public static Quaternion scale(float scale, Quaternion src, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.x = src.x * scale;
        dest.y = src.y * scale;
        dest.z = src.z * scale;
        dest.w = src.w * scale;
        return dest;
    }

    public Vector store(FloatBuffer buf) {
        buf.put(this.x);
        buf.put(this.y);
        buf.put(this.z);
        buf.put(this.w);
        return this;
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

    public void setW(float w2) {
        this.w = w2;
    }

    public float getW() {
        return this.w;
    }

    public String toString() {
        return "Quaternion: " + this.x + " " + this.y + " " + this.z + " " + this.w;
    }

    public static Quaternion mul(Quaternion left, Quaternion right, Quaternion dest) {
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set(left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y, left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z, left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x, left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z);
        return dest;
    }

    public static Quaternion mulInverse(Quaternion left, Quaternion right, Quaternion dest) {
        float n2 = right.lengthSquared();
        float f2 = n2 = (double)n2 == 0.0 ? n2 : 1.0f / n2;
        if (dest == null) {
            dest = new Quaternion();
        }
        dest.set((left.x * right.w - left.w * right.x - left.y * right.z + left.z * right.y) * n2, (left.y * right.w - left.w * right.y - left.z * right.x + left.x * right.z) * n2, (left.z * right.w - left.w * right.z - left.x * right.y + left.y * right.x) * n2, (left.w * right.w + left.x * right.x + left.y * right.y + left.z * right.z) * n2);
        return dest;
    }

    public final void setFromAxisAngle(Vector4f a1) {
        this.x = a1.x;
        this.y = a1.y;
        this.z = a1.z;
        float n2 = (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        float s2 = (float)(Math.sin(0.5 * (double)a1.w) / (double)n2);
        this.x *= s2;
        this.y *= s2;
        this.z *= s2;
        this.w = (float)Math.cos(0.5 * (double)a1.w);
    }

    public final Quaternion setFromMatrix(Matrix4f m2) {
        return Quaternion.setFromMatrix(m2, this);
    }

    public static Quaternion setFromMatrix(Matrix4f m2, Quaternion q2) {
        return q2.setFromMat(m2.m00, m2.m01, m2.m02, m2.m10, m2.m11, m2.m12, m2.m20, m2.m21, m2.m22);
    }

    public final Quaternion setFromMatrix(Matrix3f m2) {
        return Quaternion.setFromMatrix(m2, this);
    }

    public static Quaternion setFromMatrix(Matrix3f m2, Quaternion q2) {
        return q2.setFromMat(m2.m00, m2.m01, m2.m02, m2.m10, m2.m11, m2.m12, m2.m20, m2.m21, m2.m22);
    }

    private Quaternion setFromMat(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        float tr = m00 + m11 + m22;
        if ((double)tr >= 0.0) {
            float s2 = (float)Math.sqrt((double)tr + 1.0);
            this.w = s2 * 0.5f;
            s2 = 0.5f / s2;
            this.x = (m21 - m12) * s2;
            this.y = (m02 - m20) * s2;
            this.z = (m10 - m01) * s2;
        } else {
            float max = Math.max(Math.max(m00, m11), m22);
            if (max == m00) {
                float s3 = (float)Math.sqrt((double)(m00 - (m11 + m22)) + 1.0);
                this.x = s3 * 0.5f;
                s3 = 0.5f / s3;
                this.y = (m01 + m10) * s3;
                this.z = (m20 + m02) * s3;
                this.w = (m21 - m12) * s3;
            } else if (max == m11) {
                float s4 = (float)Math.sqrt((double)(m11 - (m22 + m00)) + 1.0);
                this.y = s4 * 0.5f;
                s4 = 0.5f / s4;
                this.z = (m12 + m21) * s4;
                this.x = (m01 + m10) * s4;
                this.w = (m02 - m20) * s4;
            } else {
                float s5 = (float)Math.sqrt((double)(m22 - (m00 + m11)) + 1.0);
                this.z = s5 * 0.5f;
                s5 = 0.5f / s5;
                this.x = (m20 + m02) * s5;
                this.y = (m12 + m21) * s5;
                this.w = (m10 - m01) * s5;
            }
        }
        return this;
    }
}

