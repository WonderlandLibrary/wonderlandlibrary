/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.vecmath;

import com.jhlabs.vecmath.AxisAngle4f;
import com.jhlabs.vecmath.Point3f;
import com.jhlabs.vecmath.Quat4f;
import com.jhlabs.vecmath.Vector3f;

public class Matrix4f {
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;

    public Matrix4f() {
        this.setIdentity();
    }

    public Matrix4f(Matrix4f m) {
        this.set(m);
    }

    public Matrix4f(float[] m) {
        this.set(m);
    }

    public void set(Matrix4f m) {
        this.m00 = m.m00;
        this.m01 = m.m01;
        this.m02 = m.m02;
        this.m03 = m.m03;
        this.m10 = m.m10;
        this.m11 = m.m11;
        this.m12 = m.m12;
        this.m13 = m.m13;
        this.m20 = m.m20;
        this.m21 = m.m21;
        this.m22 = m.m22;
        this.m23 = m.m23;
        this.m30 = m.m30;
        this.m31 = m.m31;
        this.m32 = m.m32;
        this.m33 = m.m33;
    }

    public void set(float[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m03 = m[3];
        this.m10 = m[4];
        this.m11 = m[5];
        this.m12 = m[6];
        this.m13 = m[7];
        this.m20 = m[8];
        this.m21 = m[9];
        this.m22 = m[10];
        this.m23 = m[11];
        this.m30 = m[12];
        this.m31 = m[13];
        this.m32 = m[14];
        this.m33 = m[15];
    }

    public void get(Matrix4f m) {
        m.m00 = this.m00;
        m.m01 = this.m01;
        m.m02 = this.m02;
        m.m03 = this.m03;
        m.m10 = this.m10;
        m.m11 = this.m11;
        m.m12 = this.m12;
        m.m13 = this.m13;
        m.m20 = this.m20;
        m.m21 = this.m21;
        m.m22 = this.m22;
        m.m23 = this.m23;
        m.m30 = this.m30;
        m.m31 = this.m31;
        m.m32 = this.m32;
        m.m33 = this.m33;
    }

    public void get(float[] m) {
        m[0] = this.m00;
        m[1] = this.m01;
        m[2] = this.m02;
        m[3] = this.m03;
        m[4] = this.m10;
        m[5] = this.m11;
        m[6] = this.m12;
        m[7] = this.m13;
        m[8] = this.m20;
        m[9] = this.m21;
        m[10] = this.m22;
        m[11] = this.m23;
        m[12] = this.m30;
        m[13] = this.m31;
        m[14] = this.m32;
        m[15] = this.m33;
    }

    public void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public void mul(Matrix4f m) {
        float tm00 = this.m00;
        float tm01 = this.m01;
        float tm02 = this.m02;
        float tm03 = this.m03;
        float tm10 = this.m10;
        float tm11 = this.m11;
        float tm12 = this.m12;
        float tm13 = this.m13;
        float tm20 = this.m20;
        float tm21 = this.m21;
        float tm22 = this.m22;
        float tm23 = this.m23;
        float tm30 = this.m30;
        float tm31 = this.m31;
        float tm32 = this.m32;
        float tm33 = this.m33;
        this.m00 = tm00 * m.m00 + tm10 * m.m01 + tm20 * m.m02 + tm30 * m.m03;
        this.m01 = tm01 * m.m00 + tm11 * m.m01 + tm21 * m.m02 + tm31 * m.m03;
        this.m02 = tm02 * m.m00 + tm12 * m.m01 + tm22 * m.m02 + tm32 * m.m03;
        this.m03 = tm03 * m.m00 + tm13 * m.m01 + tm23 * m.m02 + tm33 * m.m03;
        this.m10 = tm00 * m.m10 + tm10 * m.m11 + tm20 * m.m12 + tm30 * m.m13;
        this.m11 = tm01 * m.m10 + tm11 * m.m11 + tm21 * m.m12 + tm31 * m.m13;
        this.m12 = tm02 * m.m10 + tm12 * m.m11 + tm22 * m.m12 + tm32 * m.m13;
        this.m13 = tm03 * m.m10 + tm13 * m.m11 + tm23 * m.m12 + tm33 * m.m13;
        this.m20 = tm00 * m.m20 + tm10 * m.m21 + tm20 * m.m22 + tm30 * m.m23;
        this.m21 = tm01 * m.m20 + tm11 * m.m21 + tm21 * m.m22 + tm31 * m.m23;
        this.m22 = tm02 * m.m20 + tm12 * m.m21 + tm22 * m.m22 + tm32 * m.m23;
        this.m23 = tm03 * m.m20 + tm13 * m.m21 + tm23 * m.m22 + tm33 * m.m23;
        this.m30 = tm00 * m.m30 + tm10 * m.m31 + tm20 * m.m32 + tm30 * m.m33;
        this.m31 = tm01 * m.m30 + tm11 * m.m31 + tm21 * m.m32 + tm31 * m.m33;
        this.m32 = tm02 * m.m30 + tm12 * m.m31 + tm22 * m.m32 + tm32 * m.m33;
        this.m33 = tm03 * m.m30 + tm13 * m.m31 + tm23 * m.m32 + tm33 * m.m33;
    }

    public void invert() {
        Matrix4f t = new Matrix4f(this);
        this.invert(t);
    }

    public void invert(Matrix4f t) {
        this.m00 = t.m00;
        this.m01 = t.m10;
        this.m02 = t.m20;
        this.m03 = t.m03;
        this.m10 = t.m01;
        this.m11 = t.m11;
        this.m12 = t.m21;
        this.m13 = t.m13;
        this.m20 = t.m02;
        this.m21 = t.m12;
        this.m22 = t.m22;
        this.m23 = t.m23;
        this.m30 *= -1.0f;
        this.m31 *= -1.0f;
        this.m32 *= -1.0f;
        this.m33 = t.m33;
    }

    public void set(AxisAngle4f a) {
        float halfTheta = a.angle * 0.5f;
        float cosHalfTheta = (float)Math.cos(halfTheta);
        float sinHalfTheta = (float)Math.sin(halfTheta);
        this.set(new Quat4f(a.x * sinHalfTheta, a.y * sinHalfTheta, a.z * sinHalfTheta, cosHalfTheta));
    }

    public void set(Quat4f q) {
        float x2 = q.x + q.x;
        float y2 = q.y + q.y;
        float z2 = q.z + q.z;
        float xx = q.x * x2;
        float xy = q.x * y2;
        float xz = q.x * z2;
        float yy = q.y * y2;
        float yz = q.y * z2;
        float zz = q.z * z2;
        float wx = q.w * x2;
        float wy = q.w * y2;
        float wz = q.w * z2;
        this.m00 = 1.0f - (yy + zz);
        this.m01 = xy - wz;
        this.m02 = xz + wy;
        this.m03 = 0.0f;
        this.m10 = xy + wz;
        this.m11 = 1.0f - (xx + zz);
        this.m12 = yz - wx;
        this.m13 = 0.0f;
        this.m20 = xz - wy;
        this.m21 = yz + wx;
        this.m22 = 1.0f - (xx + yy);
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public void transform(Point3f v) {
        float x = v.x * this.m00 + v.y * this.m10 + v.z * this.m20 + this.m30;
        float y = v.x * this.m01 + v.y * this.m11 + v.z * this.m21 + this.m31;
        float z = v.x * this.m02 + v.y * this.m12 + v.z * this.m22 + this.m32;
        v.x = x;
        v.y = y;
        v.z = z;
    }

    public void transform(Vector3f v) {
        float x = v.x * this.m00 + v.y * this.m10 + v.z * this.m20;
        float y = v.x * this.m01 + v.y * this.m11 + v.z * this.m21;
        float z = v.x * this.m02 + v.y * this.m12 + v.z * this.m22;
        v.x = x;
        v.y = y;
        v.z = z;
    }

    public void setTranslation(Vector3f v) {
        this.m30 = v.x;
        this.m31 = v.y;
        this.m32 = v.z;
    }

    public void set(float scale) {
        this.m00 = scale;
        this.m11 = scale;
        this.m22 = scale;
    }

    public void rotX(float angle) {
        float s = (float)Math.sin(angle);
        float c = (float)Math.cos(angle);
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = c;
        this.m12 = s;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = -s;
        this.m22 = c;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public void rotY(float angle) {
        float c;
        float s = (float)Math.sin(angle);
        this.m00 = c = (float)Math.cos(angle);
        this.m01 = 0.0f;
        this.m02 = -s;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = s;
        this.m21 = 0.0f;
        this.m22 = c;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public void rotZ(float angle) {
        float c;
        float s = (float)Math.sin(angle);
        this.m00 = c = (float)Math.cos(angle);
        this.m01 = s;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = -s;
        this.m11 = c;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
}

