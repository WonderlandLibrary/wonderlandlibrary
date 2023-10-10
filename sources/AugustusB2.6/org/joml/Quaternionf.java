// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Quaternionf implements Externalizable, Cloneable, Quaternionfc
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public Quaternionf() {
        this.w = 1.0f;
    }
    
    public Quaternionf(final double x, final double y, final double z, final double w) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        this.w = (float)w;
    }
    
    public Quaternionf(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Quaternionf(final Quaternionfc source) {
        this.set(source);
    }
    
    public Quaternionf(final Quaterniondc source) {
        this.set(source);
    }
    
    public Quaternionf(final AxisAngle4f axisAngle) {
        final float sin = Math.sin(axisAngle.angle * 0.5f);
        final float cos = Math.cosFromSin(sin, axisAngle.angle * 0.5f);
        this.x = axisAngle.x * sin;
        this.y = axisAngle.y * sin;
        this.z = axisAngle.z * sin;
        this.w = cos;
    }
    
    public Quaternionf(final AxisAngle4d axisAngle) {
        final double sin = Math.sin(axisAngle.angle * 0.5);
        final double cos = Math.cosFromSin(sin, axisAngle.angle * 0.5);
        this.x = (float)(axisAngle.x * sin);
        this.y = (float)(axisAngle.y * sin);
        this.z = (float)(axisAngle.z * sin);
        this.w = (float)cos;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    public float w() {
        return this.w;
    }
    
    public Quaternionf normalize() {
        return this.normalize(this);
    }
    
    public Quaternionf normalize(final Quaternionf dest) {
        final float invNorm = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
        dest.x = this.x * invNorm;
        dest.y = this.y * invNorm;
        dest.z = this.z * invNorm;
        dest.w = this.w * invNorm;
        return dest;
    }
    
    public Quaternionf add(final float x, final float y, final float z, final float w) {
        return this.add(x, y, z, w, this);
    }
    
    public Quaternionf add(final float x, final float y, final float z, final float w, final Quaternionf dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        dest.w = this.w + w;
        return dest;
    }
    
    public Quaternionf add(final Quaternionfc q2) {
        return this.add(q2, this);
    }
    
    public Quaternionf add(final Quaternionfc q2, final Quaternionf dest) {
        dest.x = this.x + q2.x();
        dest.y = this.y + q2.y();
        dest.z = this.z + q2.z();
        dest.w = this.w + q2.w();
        return dest;
    }
    
    public float dot(final Quaternionf otherQuat) {
        return this.x * otherQuat.x + this.y * otherQuat.y + this.z * otherQuat.z + this.w * otherQuat.w;
    }
    
    public float angle() {
        return (float)(2.0 * Math.safeAcos(this.w));
    }
    
    public Matrix3f get(final Matrix3f dest) {
        return dest.set(this);
    }
    
    public Matrix3d get(final Matrix3d dest) {
        return dest.set(this);
    }
    
    public Matrix4f get(final Matrix4f dest) {
        return dest.set(this);
    }
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set(this);
    }
    
    public Matrix4x3f get(final Matrix4x3f dest) {
        return dest.set(this);
    }
    
    public Matrix4x3d get(final Matrix4x3d dest) {
        return dest.set(this);
    }
    
    public AxisAngle4f get(final AxisAngle4f dest) {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float w = this.w;
        if (w > 1.0f) {
            final float invNorm = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            x *= invNorm;
            y *= invNorm;
            z *= invNorm;
            w *= invNorm;
        }
        dest.angle = 2.0f * Math.acos(w);
        float s = Math.sqrt(1.0f - w * w);
        if (s < 0.001f) {
            dest.x = x;
            dest.y = y;
            dest.z = z;
        }
        else {
            s = 1.0f / s;
            dest.x = x * s;
            dest.y = y * s;
            dest.z = z * s;
        }
        return dest;
    }
    
    public AxisAngle4d get(final AxisAngle4d dest) {
        float x = this.x;
        float y = this.y;
        float z = this.z;
        float w = this.w;
        if (w > 1.0f) {
            final float invNorm = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            x *= invNorm;
            y *= invNorm;
            z *= invNorm;
            w *= invNorm;
        }
        dest.angle = 2.0f * Math.acos(w);
        float s = Math.sqrt(1.0f - w * w);
        if (s < 0.001f) {
            dest.x = x;
            dest.y = y;
            dest.z = z;
        }
        else {
            s = 1.0f / s;
            dest.x = x * s;
            dest.y = y * s;
            dest.z = z * s;
        }
        return dest;
    }
    
    public Quaterniond get(final Quaterniond dest) {
        return dest.set(this);
    }
    
    public Quaternionf get(final Quaternionf dest) {
        return dest.set(this);
    }
    
    public ByteBuffer getAsMatrix3f(final ByteBuffer dest) {
        MemUtil.INSTANCE.putMatrix3f(this, dest.position(), dest);
        return dest;
    }
    
    public FloatBuffer getAsMatrix3f(final FloatBuffer dest) {
        MemUtil.INSTANCE.putMatrix3f(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer getAsMatrix4f(final ByteBuffer dest) {
        MemUtil.INSTANCE.putMatrix4f(this, dest.position(), dest);
        return dest;
    }
    
    public FloatBuffer getAsMatrix4f(final FloatBuffer dest) {
        MemUtil.INSTANCE.putMatrix4f(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer getAsMatrix4x3f(final ByteBuffer dest) {
        MemUtil.INSTANCE.putMatrix4x3f(this, dest.position(), dest);
        return dest;
    }
    
    public FloatBuffer getAsMatrix4x3f(final FloatBuffer dest) {
        MemUtil.INSTANCE.putMatrix4x3f(this, dest.position(), dest);
        return dest;
    }
    
    public Quaternionf set(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Quaternionf set(final Quaternionfc q) {
        this.x = q.x();
        this.y = q.y();
        this.z = q.z();
        this.w = q.w();
        return this;
    }
    
    public Quaternionf set(final Quaterniondc q) {
        this.x = (float)q.x();
        this.y = (float)q.y();
        this.z = (float)q.z();
        this.w = (float)q.w();
        return this;
    }
    
    public Quaternionf set(final AxisAngle4f axisAngle) {
        return this.setAngleAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaternionf set(final AxisAngle4d axisAngle) {
        return this.setAngleAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaternionf setAngleAxis(final float angle, final float x, final float y, final float z) {
        final float s = Math.sin(angle * 0.5f);
        this.x = x * s;
        this.y = y * s;
        this.z = z * s;
        this.w = Math.cosFromSin(s, angle * 0.5f);
        return this;
    }
    
    public Quaternionf setAngleAxis(final double angle, final double x, final double y, final double z) {
        final double s = Math.sin(angle * 0.5);
        this.x = (float)(x * s);
        this.y = (float)(y * s);
        this.z = (float)(z * s);
        this.w = (float)Math.cosFromSin(s, angle * 0.5);
        return this;
    }
    
    public Quaternionf rotationAxis(final AxisAngle4f axisAngle) {
        return this.rotationAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaternionf rotationAxis(final float angle, final float axisX, final float axisY, final float axisZ) {
        final float hangle = angle / 2.0f;
        final float sinAngle = Math.sin(hangle);
        final float invVLength = Math.invsqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        return this.set(axisX * invVLength * sinAngle, axisY * invVLength * sinAngle, axisZ * invVLength * sinAngle, Math.cosFromSin(sinAngle, hangle));
    }
    
    public Quaternionf rotationAxis(final float angle, final Vector3fc axis) {
        return this.rotationAxis(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Quaternionf rotationX(final float angle) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return this.set(sin, 0.0f, 0.0f, cos);
    }
    
    public Quaternionf rotationY(final float angle) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return this.set(0.0f, sin, 0.0f, cos);
    }
    
    public Quaternionf rotationZ(final float angle) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return this.set(0.0f, 0.0f, sin, cos);
    }
    
    private void setFromUnnormalized(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22) {
        float nm00 = m00;
        float nm2 = m01;
        float nm3 = m02;
        float nm4 = m10;
        float nm5 = m11;
        float nm6 = m12;
        float nm7 = m20;
        float nm8 = m21;
        float nm9 = m22;
        final float lenX = Math.invsqrt(m00 * m00 + m01 * m01 + m02 * m02);
        final float lenY = Math.invsqrt(m10 * m10 + m11 * m11 + m12 * m12);
        final float lenZ = Math.invsqrt(m20 * m20 + m21 * m21 + m22 * m22);
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        this.setFromNormalized(nm00, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9);
    }
    
    private void setFromNormalized(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22) {
        final float tr = m00 + m11 + m22;
        if (tr >= 0.0f) {
            float t = Math.sqrt(tr + 1.0f);
            this.w = t * 0.5f;
            t = 0.5f / t;
            this.x = (m12 - m21) * t;
            this.y = (m20 - m02) * t;
            this.z = (m01 - m10) * t;
        }
        else if (m00 >= m11 && m00 >= m22) {
            float t = Math.sqrt(m00 - (m11 + m22) + 1.0f);
            this.x = t * 0.5f;
            t = 0.5f / t;
            this.y = (m10 + m01) * t;
            this.z = (m02 + m20) * t;
            this.w = (m12 - m21) * t;
        }
        else if (m11 > m22) {
            float t = Math.sqrt(m11 - (m22 + m00) + 1.0f);
            this.y = t * 0.5f;
            t = 0.5f / t;
            this.z = (m21 + m12) * t;
            this.x = (m10 + m01) * t;
            this.w = (m20 - m02) * t;
        }
        else {
            float t = Math.sqrt(m22 - (m00 + m11) + 1.0f);
            this.z = t * 0.5f;
            t = 0.5f / t;
            this.x = (m02 + m20) * t;
            this.y = (m21 + m12) * t;
            this.w = (m01 - m10) * t;
        }
    }
    
    private void setFromUnnormalized(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22) {
        double nm00 = m00;
        double nm2 = m01;
        double nm3 = m02;
        double nm4 = m10;
        double nm5 = m11;
        double nm6 = m12;
        double nm7 = m20;
        double nm8 = m21;
        double nm9 = m22;
        final double lenX = Math.invsqrt(m00 * m00 + m01 * m01 + m02 * m02);
        final double lenY = Math.invsqrt(m10 * m10 + m11 * m11 + m12 * m12);
        final double lenZ = Math.invsqrt(m20 * m20 + m21 * m21 + m22 * m22);
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        this.setFromNormalized(nm00, nm2, nm3, nm4, nm5, nm6, nm7, nm8, nm9);
    }
    
    private void setFromNormalized(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22) {
        final double tr = m00 + m11 + m22;
        if (tr >= 0.0) {
            double t = Math.sqrt(tr + 1.0);
            this.w = (float)(t * 0.5);
            t = 0.5 / t;
            this.x = (float)((m12 - m21) * t);
            this.y = (float)((m20 - m02) * t);
            this.z = (float)((m01 - m10) * t);
        }
        else if (m00 >= m11 && m00 >= m22) {
            double t = Math.sqrt(m00 - (m11 + m22) + 1.0);
            this.x = (float)(t * 0.5);
            t = 0.5 / t;
            this.y = (float)((m10 + m01) * t);
            this.z = (float)((m02 + m20) * t);
            this.w = (float)((m12 - m21) * t);
        }
        else if (m11 > m22) {
            double t = Math.sqrt(m11 - (m22 + m00) + 1.0);
            this.y = (float)(t * 0.5);
            t = 0.5 / t;
            this.z = (float)((m21 + m12) * t);
            this.x = (float)((m10 + m01) * t);
            this.w = (float)((m20 - m02) * t);
        }
        else {
            double t = Math.sqrt(m22 - (m00 + m11) + 1.0);
            this.z = (float)(t * 0.5);
            t = 0.5 / t;
            this.x = (float)((m02 + m20) * t);
            this.y = (float)((m21 + m12) * t);
            this.w = (float)((m01 - m10) * t);
        }
    }
    
    public Quaternionf setFromUnnormalized(final Matrix4fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromUnnormalized(final Matrix4x3fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromUnnormalized(final Matrix4x3dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix4fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix4x3fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix4x3dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromUnnormalized(final Matrix4dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix4dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromUnnormalized(final Matrix3fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix3fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromUnnormalized(final Matrix3dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf setFromNormalized(final Matrix3dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaternionf fromAxisAngleRad(final Vector3fc axis, final float angle) {
        return this.fromAxisAngleRad(axis.x(), axis.y(), axis.z(), angle);
    }
    
    public Quaternionf fromAxisAngleRad(final float axisX, final float axisY, final float axisZ, final float angle) {
        final float hangle = angle / 2.0f;
        final float sinAngle = Math.sin(hangle);
        final float vLength = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        this.x = axisX / vLength * sinAngle;
        this.y = axisY / vLength * sinAngle;
        this.z = axisZ / vLength * sinAngle;
        this.w = Math.cosFromSin(sinAngle, hangle);
        return this;
    }
    
    public Quaternionf fromAxisAngleDeg(final Vector3fc axis, final float angle) {
        return this.fromAxisAngleRad(axis.x(), axis.y(), axis.z(), Math.toRadians(angle));
    }
    
    public Quaternionf fromAxisAngleDeg(final float axisX, final float axisY, final float axisZ, final float angle) {
        return this.fromAxisAngleRad(axisX, axisY, axisZ, Math.toRadians(angle));
    }
    
    public Quaternionf mul(final Quaternionfc q) {
        return this.mul(q, this);
    }
    
    public Quaternionf mul(final Quaternionfc q, final Quaternionf dest) {
        return dest.set(Math.fma(this.w, q.x(), Math.fma(this.x, q.w(), Math.fma(this.y, q.z(), -this.z * q.y()))), Math.fma(this.w, q.y(), Math.fma(-this.x, q.z(), Math.fma(this.y, q.w(), this.z * q.x()))), Math.fma(this.w, q.z(), Math.fma(this.x, q.y(), Math.fma(-this.y, q.x(), this.z * q.w()))), Math.fma(this.w, q.w(), Math.fma(-this.x, q.x(), Math.fma(-this.y, q.y(), -this.z * q.z()))));
    }
    
    public Quaternionf mul(final float qx, final float qy, final float qz, final float qw) {
        return this.mul(qx, qy, qz, qw, this);
    }
    
    public Quaternionf mul(final float qx, final float qy, final float qz, final float qw, final Quaternionf dest) {
        return dest.set(Math.fma(this.w, qx, Math.fma(this.x, qw, Math.fma(this.y, qz, -this.z * qy))), Math.fma(this.w, qy, Math.fma(-this.x, qz, Math.fma(this.y, qw, this.z * qx))), Math.fma(this.w, qz, Math.fma(this.x, qy, Math.fma(-this.y, qx, this.z * qw))), Math.fma(this.w, qw, Math.fma(-this.x, qx, Math.fma(-this.y, qy, -this.z * qz))));
    }
    
    public Quaternionf premul(final Quaternionfc q) {
        return this.premul(q, this);
    }
    
    public Quaternionf premul(final Quaternionfc q, final Quaternionf dest) {
        return dest.set(Math.fma(q.w(), this.x, Math.fma(q.x(), this.w, Math.fma(q.y(), this.z, -q.z() * this.y))), Math.fma(q.w(), this.y, Math.fma(-q.x(), this.z, Math.fma(q.y(), this.w, q.z() * this.x))), Math.fma(q.w(), this.z, Math.fma(q.x(), this.y, Math.fma(-q.y(), this.x, q.z() * this.w))), Math.fma(q.w(), this.w, Math.fma(-q.x(), this.x, Math.fma(-q.y(), this.y, -q.z() * this.z))));
    }
    
    public Quaternionf premul(final float qx, final float qy, final float qz, final float qw) {
        return this.premul(qx, qy, qz, qw, this);
    }
    
    public Quaternionf premul(final float qx, final float qy, final float qz, final float qw, final Quaternionf dest) {
        return dest.set(Math.fma(qw, this.x, Math.fma(qx, this.w, Math.fma(qy, this.z, -qz * this.y))), Math.fma(qw, this.y, Math.fma(-qx, this.z, Math.fma(qy, this.w, qz * this.x))), Math.fma(qw, this.z, Math.fma(qx, this.y, Math.fma(-qy, this.x, qz * this.w))), Math.fma(qw, this.w, Math.fma(-qx, this.x, Math.fma(-qy, this.y, -qz * this.z))));
    }
    
    public Vector3f transform(final Vector3f vec) {
        return this.transform(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformInverse(final Vector3f vec) {
        return this.transformInverse(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformPositiveX(final Vector3f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4f transformPositiveX(final Vector4f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3f transformUnitPositiveX(final Vector3f dest) {
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yy = this.y * this.y;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        dest.x = 1.0f - yy - zz - yy - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4f transformUnitPositiveX(final Vector4f dest) {
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float zw = this.z * this.w;
        dest.x = 1.0f - yy - yy - zz - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3f transformPositiveY(final Vector3f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4f transformPositiveY(final Vector4f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4f transformUnitPositiveY(final Vector4f dest) {
        final float xx = this.x * this.x;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0f - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3f transformUnitPositiveY(final Vector3f dest) {
        final float xx = this.x * this.x;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0f - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3f transformPositiveZ(final Vector3f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4f transformPositiveZ(final Vector4f dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4f transformUnitPositiveZ(final Vector4f dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0f - xx - xx - yy - yy;
        return dest;
    }
    
    public Vector3f transformUnitPositiveZ(final Vector3f dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0f - xx - xx - yy - yy;
        return dest;
    }
    
    public Vector4f transform(final Vector4f vec) {
        return this.transform(vec, vec);
    }
    
    public Vector4f transformInverse(final Vector4f vec) {
        return this.transformInverse(vec, vec);
    }
    
    public Vector3f transform(final Vector3fc vec, final Vector3f dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transformInverse(final Vector3fc vec, final Vector3f dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transform(final float x, final float y, final float z, final Vector3f dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float ww = this.w * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        final float yw = this.y * this.w;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy - zw) * k, y, 2.0f * (xz + yw) * k * z)), Math.fma(2.0f * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz - xw) * k * z)), Math.fma(2.0f * (xz - yw) * k, x, Math.fma(2.0f * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3f transformInverse(final float x, final float y, final float z, final Vector3f dest) {
        final float n = 1.0f / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final float qx = this.x * n;
        final float qy = this.y * n;
        final float qz = this.z * n;
        final float qw = this.w * n;
        final float xx = qx * qx;
        final float yy = qy * qy;
        final float zz = qz * qz;
        final float ww = qw * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yz = qy * qz;
        final float xw = qx * qw;
        final float zw = qz * qw;
        final float yw = qy * qw;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy + zw) * k, y, 2.0f * (xz - yw) * k * z)), Math.fma(2.0f * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz + xw) * k * z)), Math.fma(2.0f * (xz + yw) * k, x, Math.fma(2.0f * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3f transformUnit(final Vector3f vec) {
        return this.transformUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformInverseUnit(final Vector3f vec) {
        return this.transformInverseUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformUnit(final Vector3fc vec, final Vector3f dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transformInverseUnit(final Vector3fc vec, final Vector3f dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transformUnit(final float x, final float y, final float z, final Vector3f dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy - zw), y, 2.0f * (xz + yw) * z)), Math.fma(2.0f * (xy + zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz - xw) * z)), Math.fma(2.0f * (xz - yw), x, Math.fma(2.0f * (yz + xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector3f transformInverseUnit(final float x, final float y, final float z, final Vector3f dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy + zw), y, 2.0f * (xz - yw) * z)), Math.fma(2.0f * (xy - zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz + xw) * z)), Math.fma(2.0f * (xz + yw), x, Math.fma(2.0f * (yz - xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector4f transform(final Vector4fc vec, final Vector4f dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformInverse(final Vector4fc vec, final Vector4f dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transform(final float x, final float y, final float z, final Vector4f dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float ww = this.w * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        final float yw = this.y * this.w;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy - zw) * k, y, 2.0f * (xz + yw) * k * z)), Math.fma(2.0f * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz - xw) * k * z)), Math.fma(2.0f * (xz - yw) * k, x, Math.fma(2.0f * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4f transformInverse(final float x, final float y, final float z, final Vector4f dest) {
        final float n = 1.0f / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final float qx = this.x * n;
        final float qy = this.y * n;
        final float qz = this.z * n;
        final float qw = this.w * n;
        final float xx = qx * qx;
        final float yy = qy * qy;
        final float zz = qz * qz;
        final float ww = qw * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yz = qy * qz;
        final float xw = qx * qw;
        final float zw = qz * qw;
        final float yw = qy * qw;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy + zw) * k, y, 2.0f * (xz - yw) * k * z)), Math.fma(2.0f * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz + xw) * k * z)), Math.fma(2.0f * (xz + yw) * k, x, Math.fma(2.0f * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3d transform(final Vector3d vec) {
        return this.transform(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3d transformInverse(final Vector3d vec) {
        return this.transformInverse(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector4f transformUnit(final Vector4f vec) {
        return this.transformUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector4f transformInverseUnit(final Vector4f vec) {
        return this.transformInverseUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector4f transformUnit(final Vector4fc vec, final Vector4f dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformInverseUnit(final Vector4fc vec, final Vector4f dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformUnit(final float x, final float y, final float z, final Vector4f dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy - zw), y, 2.0f * (xz + yw) * z)), Math.fma(2.0f * (xy + zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz - xw) * z)), Math.fma(2.0f * (xz - yw), x, Math.fma(2.0f * (yz + xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector4f transformInverseUnit(final float x, final float y, final float z, final Vector4f dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy + zw), y, 2.0f * (xz - yw) * z)), Math.fma(2.0f * (xy - zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz + xw) * z)), Math.fma(2.0f * (xz + yw), x, Math.fma(2.0f * (yz - xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector3d transformPositiveX(final Vector3d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4d transformPositiveX(final Vector4d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3d transformUnitPositiveX(final Vector3d dest) {
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float zw = this.z * this.w;
        dest.x = 1.0f - yy - yy - zz - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4d transformUnitPositiveX(final Vector4d dest) {
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float zw = this.z * this.w;
        dest.x = 1.0f - yy - yy - zz - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3d transformPositiveY(final Vector3d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4d transformPositiveY(final Vector4d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4d transformUnitPositiveY(final Vector4d dest) {
        final float xx = this.x * this.x;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0f - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3d transformUnitPositiveY(final Vector3d dest) {
        final float xx = this.x * this.x;
        final float zz = this.z * this.z;
        final float xy = this.x * this.y;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0f - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3d transformPositiveZ(final Vector3d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4d transformPositiveZ(final Vector4d dest) {
        final float ww = this.w * this.w;
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float xz = this.x * this.z;
        final float yw = this.y * this.w;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4d transformUnitPositiveZ(final Vector4d dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0f - xx - xx - yy - yy;
        return dest;
    }
    
    public Vector3d transformUnitPositiveZ(final Vector3d dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0f - xx - xx - yy - yy;
        return dest;
    }
    
    public Vector4d transform(final Vector4d vec) {
        return this.transform(vec, vec);
    }
    
    public Vector4d transformInverse(final Vector4d vec) {
        return this.transformInverse(vec, vec);
    }
    
    public Vector3d transform(final Vector3dc vec, final Vector3d dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3d transformInverse(final Vector3dc vec, final Vector3d dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3d transform(final float x, final float y, final float z, final Vector3d dest) {
        return this.transform(x, y, (double)z, dest);
    }
    
    public Vector3d transformInverse(final float x, final float y, final float z, final Vector3d dest) {
        return this.transformInverse(x, y, (double)z, dest);
    }
    
    public Vector3d transform(final double x, final double y, final double z, final Vector3d dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float ww = this.w * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        final float yw = this.y * this.w;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy - zw) * k, y, 2.0f * (xz + yw) * k * z)), Math.fma(2.0f * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz - xw) * k * z)), Math.fma(2.0f * (xz - yw) * k, x, Math.fma(2.0f * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3d transformInverse(final double x, final double y, final double z, final Vector3d dest) {
        final float n = 1.0f / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final float qx = this.x * n;
        final float qy = this.y * n;
        final float qz = this.z * n;
        final float qw = this.w * n;
        final float xx = qx * qx;
        final float yy = qy * qy;
        final float zz = qz * qz;
        final float ww = qw * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yz = qy * qz;
        final float xw = qx * qw;
        final float zw = qz * qw;
        final float yw = qy * qw;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy + zw) * k, y, 2.0f * (xz - yw) * k * z)), Math.fma(2.0f * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz + xw) * k * z)), Math.fma(2.0f * (xz + yw) * k, x, Math.fma(2.0f * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4d transform(final Vector4dc vec, final Vector4d dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformInverse(final Vector4dc vec, final Vector4d dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transform(final double x, final double y, final double z, final Vector4d dest) {
        final float xx = this.x * this.x;
        final float yy = this.y * this.y;
        final float zz = this.z * this.z;
        final float ww = this.w * this.w;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float yz = this.y * this.z;
        final float xw = this.x * this.w;
        final float zw = this.z * this.w;
        final float yw = this.y * this.w;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy - zw) * k, y, 2.0f * (xz + yw) * k * z)), Math.fma(2.0f * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz - xw) * k * z)), Math.fma(2.0f * (xz - yw) * k, x, Math.fma(2.0f * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4d transformInverse(final double x, final double y, final double z, final Vector4d dest) {
        final float n = 1.0f / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final float qx = this.x * n;
        final float qy = this.y * n;
        final float qz = this.z * n;
        final float qw = this.w * n;
        final float xx = qx * qx;
        final float yy = qy * qy;
        final float zz = qz * qz;
        final float ww = qw * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yz = qy * qz;
        final float xw = qx * qw;
        final float zw = qz * qw;
        final float yw = qy * qw;
        final float k = 1.0f / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0f * (xy + zw) * k, y, 2.0f * (xz - yw) * k * z)), Math.fma(2.0f * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0f * (yz + xw) * k * z)), Math.fma(2.0f * (xz + yw) * k, x, Math.fma(2.0f * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4d transformUnit(final Vector4d vec) {
        return this.transformUnit(vec, vec);
    }
    
    public Vector4d transformInverseUnit(final Vector4d vec) {
        return this.transformInverseUnit(vec, vec);
    }
    
    public Vector3d transformUnit(final Vector3dc vec, final Vector3d dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3d transformInverseUnit(final Vector3dc vec, final Vector3d dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3d transformUnit(final float x, final float y, final float z, final Vector3d dest) {
        return this.transformUnit(x, y, (double)z, dest);
    }
    
    public Vector3d transformInverseUnit(final float x, final float y, final float z, final Vector3d dest) {
        return this.transformInverseUnit(x, y, (double)z, dest);
    }
    
    public Vector3d transformUnit(final double x, final double y, final double z, final Vector3d dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy - zw), y, 2.0f * (xz + yw) * z)), Math.fma(2.0f * (xy + zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz - xw) * z)), Math.fma(2.0f * (xz - yw), x, Math.fma(2.0f * (yz + xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector3d transformInverseUnit(final double x, final double y, final double z, final Vector3d dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy + zw), y, 2.0f * (xz - yw) * z)), Math.fma(2.0f * (xy - zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz + xw) * z)), Math.fma(2.0f * (xz + yw), x, Math.fma(2.0f * (yz - xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector4d transformUnit(final Vector4dc vec, final Vector4d dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformInverseUnit(final Vector4dc vec, final Vector4d dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformUnit(final double x, final double y, final double z, final Vector4d dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy - zw), y, 2.0f * (xz + yw) * z)), Math.fma(2.0f * (xy + zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz - xw) * z)), Math.fma(2.0f * (xz - yw), x, Math.fma(2.0f * (yz + xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Vector4d transformInverseUnit(final double x, final double y, final double z, final Vector4d dest) {
        final float xx = this.x * this.x;
        final float xy = this.x * this.y;
        final float xz = this.x * this.z;
        final float xw = this.x * this.w;
        final float yy = this.y * this.y;
        final float yz = this.y * this.z;
        final float yw = this.y * this.w;
        final float zz = this.z * this.z;
        final float zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0f, yy + zz, 1.0f), x, Math.fma(2.0f * (xy + zw), y, 2.0f * (xz - yw) * z)), Math.fma(2.0f * (xy - zw), x, Math.fma(Math.fma(-2.0f, xx + zz, 1.0f), y, 2.0f * (yz + xw) * z)), Math.fma(2.0f * (xz + yw), x, Math.fma(2.0f * (yz - xw), y, Math.fma(-2.0f, xx + yy, 1.0f) * z)));
    }
    
    public Quaternionf invert(final Quaternionf dest) {
        final float invNorm = 1.0f / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        dest.x = -this.x * invNorm;
        dest.y = -this.y * invNorm;
        dest.z = -this.z * invNorm;
        dest.w = this.w * invNorm;
        return dest;
    }
    
    public Quaternionf invert() {
        return this.invert(this);
    }
    
    public Quaternionf div(final Quaternionfc b, final Quaternionf dest) {
        final float invNorm = 1.0f / Math.fma(b.x(), b.x(), Math.fma(b.y(), b.y(), Math.fma(b.z(), b.z(), b.w() * b.w())));
        final float x = -b.x() * invNorm;
        final float y = -b.y() * invNorm;
        final float z = -b.z() * invNorm;
        final float w = b.w() * invNorm;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaternionf div(final Quaternionfc b) {
        return this.div(b, this);
    }
    
    public Quaternionf conjugate() {
        return this.conjugate(this);
    }
    
    public Quaternionf conjugate(final Quaternionf dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = this.w;
        return dest;
    }
    
    public Quaternionf identity() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 1.0f;
        return this;
    }
    
    public Quaternionf rotateXYZ(final float angleX, final float angleY, final float angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Quaternionf rotateXYZ(final float angleX, final float angleY, final float angleZ, final Quaternionf dest) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float cycz = cy * cz;
        final float sysz = sy * sz;
        final float sycz = sy * cz;
        final float cysz = cy * sz;
        final float w = cx * cycz - sx * sysz;
        final float x = sx * cycz + cx * sysz;
        final float y = cx * sycz - sx * cysz;
        final float z = cx * cysz + sx * sycz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaternionf rotateZYX(final float angleZ, final float angleY, final float angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Quaternionf rotateZYX(final float angleZ, final float angleY, final float angleX, final Quaternionf dest) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float cycz = cy * cz;
        final float sysz = sy * sz;
        final float sycz = sy * cz;
        final float cysz = cy * sz;
        final float w = cx * cycz + sx * sysz;
        final float x = sx * cycz - cx * sysz;
        final float y = cx * sycz + sx * cysz;
        final float z = cx * cysz - sx * sycz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaternionf rotateYXZ(final float angleY, final float angleX, final float angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Quaternionf rotateYXZ(final float angleY, final float angleX, final float angleZ, final Quaternionf dest) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float yx = cy * sx;
        final float yy = sy * cx;
        final float yz = sy * sx;
        final float yw = cy * cx;
        final float x = yx * cz + yy * sz;
        final float y = yy * cz - yx * sz;
        final float z = yw * sz - yz * cz;
        final float w = yw * cz + yz * sz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Vector3f getEulerAnglesXYZ(final Vector3f eulerAngles) {
        eulerAngles.x = Math.atan2(this.x * this.w - this.y * this.z, 0.5f - this.x * this.x - this.y * this.y);
        eulerAngles.y = Math.safeAsin(2.0f * (this.x * this.z + this.y * this.w));
        eulerAngles.z = Math.atan2(this.z * this.w - this.x * this.y, 0.5f - this.y * this.y - this.z * this.z);
        return eulerAngles;
    }
    
    public Vector3f getEulerAnglesZYX(final Vector3f eulerAngles) {
        eulerAngles.x = Math.atan2(this.y * this.z + this.w * this.x, 0.5f - this.x * this.x + this.y * this.y);
        eulerAngles.y = Math.safeAsin(-2.0f * (this.x * this.z - this.w * this.y));
        eulerAngles.z = Math.atan2(this.x * this.y + this.w * this.z, 0.5f - this.y * this.y - this.z * this.z);
        return eulerAngles;
    }
    
    public Vector3f getEulerAnglesZXY(final Vector3f eulerAngles) {
        eulerAngles.x = Math.safeAsin(2.0f * (this.w * this.x + this.y * this.z));
        eulerAngles.y = Math.atan2(this.w * this.y - this.x * this.z, 0.5f - this.y * this.y - this.x * this.x);
        eulerAngles.z = Math.atan2(this.w * this.z - this.x * this.y, 0.5f - this.z * this.z - this.x * this.x);
        return eulerAngles;
    }
    
    public Vector3f getEulerAnglesYXZ(final Vector3f eulerAngles) {
        eulerAngles.x = Math.safeAsin(-2.0f * (this.y * this.z - this.w * this.x));
        eulerAngles.y = Math.atan2(this.x * this.z + this.y * this.w, 0.5f - this.y * this.y - this.x * this.x);
        eulerAngles.z = Math.atan2(this.y * this.x + this.w * this.z, 0.5f - this.x * this.x - this.z * this.z);
        return eulerAngles;
    }
    
    public float lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
    }
    
    public Quaternionf rotationXYZ(final float angleX, final float angleY, final float angleZ) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float cycz = cy * cz;
        final float sysz = sy * sz;
        final float sycz = sy * cz;
        final float cysz = cy * sz;
        this.w = cx * cycz - sx * sysz;
        this.x = sx * cycz + cx * sysz;
        this.y = cx * sycz - sx * cysz;
        this.z = cx * cysz + sx * sycz;
        return this;
    }
    
    public Quaternionf rotationZYX(final float angleZ, final float angleY, final float angleX) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float cycz = cy * cz;
        final float sysz = sy * sz;
        final float sycz = sy * cz;
        final float cysz = cy * sz;
        this.w = cx * cycz + sx * sysz;
        this.x = sx * cycz - cx * sysz;
        this.y = cx * sycz + sx * cysz;
        this.z = cx * cysz - sx * sycz;
        return this;
    }
    
    public Quaternionf rotationYXZ(final float angleY, final float angleX, final float angleZ) {
        final float sx = Math.sin(angleX * 0.5f);
        final float cx = Math.cosFromSin(sx, angleX * 0.5f);
        final float sy = Math.sin(angleY * 0.5f);
        final float cy = Math.cosFromSin(sy, angleY * 0.5f);
        final float sz = Math.sin(angleZ * 0.5f);
        final float cz = Math.cosFromSin(sz, angleZ * 0.5f);
        final float x = cy * sx;
        final float y = sy * cx;
        final float z = sy * sx;
        final float w = cy * cx;
        this.x = x * cz + y * sz;
        this.y = y * cz - x * sz;
        this.z = w * sz - z * cz;
        this.w = w * cz + z * sz;
        return this;
    }
    
    public Quaternionf slerp(final Quaternionfc target, final float alpha) {
        return this.slerp(target, alpha, this);
    }
    
    public Quaternionf slerp(final Quaternionfc target, final float alpha, final Quaternionf dest) {
        final float cosom = Math.fma(this.x, target.x(), Math.fma(this.y, target.y(), Math.fma(this.z, target.z(), this.w * target.w())));
        final float absCosom = Math.abs(cosom);
        float scale0;
        float scale2;
        if (1.0f - absCosom > 1.0E-6f) {
            final float sinSqr = 1.0f - absCosom * absCosom;
            final float sinom = Math.invsqrt(sinSqr);
            final float omega = Math.atan2(sinSqr * sinom, absCosom);
            scale0 = (float)(Math.sin((1.0 - alpha) * omega) * sinom);
            scale2 = Math.sin(alpha * omega) * sinom;
        }
        else {
            scale0 = 1.0f - alpha;
            scale2 = alpha;
        }
        scale2 = ((cosom >= 0.0f) ? scale2 : (-scale2));
        dest.x = Math.fma(scale0, this.x, scale2 * target.x());
        dest.y = Math.fma(scale0, this.y, scale2 * target.y());
        dest.z = Math.fma(scale0, this.z, scale2 * target.z());
        dest.w = Math.fma(scale0, this.w, scale2 * target.w());
        return dest;
    }
    
    public static Quaternionfc slerp(final Quaternionf[] qs, final float[] weights, final Quaternionf dest) {
        dest.set(qs[0]);
        float w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final float w2 = w;
            final float w3 = weights[i];
            final float rw1 = w3 / (w2 + w3);
            w += w3;
            dest.slerp(qs[i], rw1);
        }
        return dest;
    }
    
    public Quaternionf scale(final float factor) {
        return this.scale(factor, this);
    }
    
    public Quaternionf scale(final float factor, final Quaternionf dest) {
        final float sqrt = Math.sqrt(factor);
        dest.x = sqrt * this.x;
        dest.y = sqrt * this.y;
        dest.z = sqrt * this.z;
        dest.w = sqrt * this.w;
        return dest;
    }
    
    public Quaternionf scaling(final float factor) {
        final float sqrt = Math.sqrt(factor);
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = sqrt;
        return this;
    }
    
    public Quaternionf integrate(final float dt, final float vx, final float vy, final float vz) {
        return this.integrate(dt, vx, vy, vz, this);
    }
    
    public Quaternionf integrate(final float dt, final float vx, final float vy, final float vz, final Quaternionf dest) {
        final float thetaX = dt * vx * 0.5f;
        final float thetaY = dt * vy * 0.5f;
        final float thetaZ = dt * vz * 0.5f;
        final float thetaMagSq = thetaX * thetaX + thetaY * thetaY + thetaZ * thetaZ;
        float dqW;
        float s;
        if (thetaMagSq * thetaMagSq / 24.0f < 1.0E-8f) {
            dqW = 1.0f - thetaMagSq * 0.5f;
            s = 1.0f - thetaMagSq / 6.0f;
        }
        else {
            final float thetaMag = Math.sqrt(thetaMagSq);
            final float sin = Math.sin(thetaMag);
            s = sin / thetaMag;
            dqW = Math.cosFromSin(sin, thetaMag);
        }
        final float dqX = thetaX * s;
        final float dqY = thetaY * s;
        final float dqZ = thetaZ * s;
        return dest.set(Math.fma(dqW, this.x, Math.fma(dqX, this.w, Math.fma(dqY, this.z, -dqZ * this.y))), Math.fma(dqW, this.y, Math.fma(-dqX, this.z, Math.fma(dqY, this.w, dqZ * this.x))), Math.fma(dqW, this.z, Math.fma(dqX, this.y, Math.fma(-dqY, this.x, dqZ * this.w))), Math.fma(dqW, this.w, Math.fma(-dqX, this.x, Math.fma(-dqY, this.y, -dqZ * this.z))));
    }
    
    public Quaternionf nlerp(final Quaternionfc q, final float factor) {
        return this.nlerp(q, factor, this);
    }
    
    public Quaternionf nlerp(final Quaternionfc q, final float factor, final Quaternionf dest) {
        final float cosom = Math.fma(this.x, q.x(), Math.fma(this.y, q.y(), Math.fma(this.z, q.z(), this.w * q.w())));
        final float scale0 = 1.0f - factor;
        final float scale2 = (cosom >= 0.0f) ? factor : (-factor);
        dest.x = Math.fma(scale0, this.x, scale2 * q.x());
        dest.y = Math.fma(scale0, this.y, scale2 * q.y());
        dest.z = Math.fma(scale0, this.z, scale2 * q.z());
        dest.w = Math.fma(scale0, this.w, scale2 * q.w());
        final float s = Math.invsqrt(Math.fma(dest.x, dest.x, Math.fma(dest.y, dest.y, Math.fma(dest.z, dest.z, dest.w * dest.w))));
        dest.x *= s;
        dest.y *= s;
        dest.z *= s;
        dest.w *= s;
        return dest;
    }
    
    public static Quaternionfc nlerp(final Quaternionfc[] qs, final float[] weights, final Quaternionf dest) {
        dest.set(qs[0]);
        float w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final float w2 = w;
            final float w3 = weights[i];
            final float rw1 = w3 / (w2 + w3);
            w += w3;
            dest.nlerp(qs[i], rw1);
        }
        return dest;
    }
    
    public Quaternionf nlerpIterative(final Quaternionfc q, final float alpha, final float dotThreshold, final Quaternionf dest) {
        float q1x = this.x;
        float q1y = this.y;
        float q1z = this.z;
        float q1w = this.w;
        float q2x = q.x();
        float q2y = q.y();
        float q2z = q.z();
        float q2w = q.w();
        float dot = Math.fma(q1x, q2x, Math.fma(q1y, q2y, Math.fma(q1z, q2z, q1w * q2w)));
        float absDot = Math.abs(dot);
        if (0.999999f < absDot) {
            return dest.set(this);
        }
        float alphaN = alpha;
        while (absDot < dotThreshold) {
            final float scale0 = 0.5f;
            final float scale2 = (dot >= 0.0f) ? 0.5f : -0.5f;
            if (alphaN < 0.5f) {
                q2x = Math.fma(scale0, q2x, scale2 * q1x);
                q2y = Math.fma(scale0, q2y, scale2 * q1y);
                q2z = Math.fma(scale0, q2z, scale2 * q1z);
                q2w = Math.fma(scale0, q2w, scale2 * q1w);
                final float s = Math.invsqrt(Math.fma(q2x, q2x, Math.fma(q2y, q2y, Math.fma(q2z, q2z, q2w * q2w))));
                q2x *= s;
                q2y *= s;
                q2z *= s;
                q2w *= s;
                alphaN += alphaN;
            }
            else {
                q1x = Math.fma(scale0, q1x, scale2 * q2x);
                q1y = Math.fma(scale0, q1y, scale2 * q2y);
                q1z = Math.fma(scale0, q1z, scale2 * q2z);
                q1w = Math.fma(scale0, q1w, scale2 * q2w);
                final float s = Math.invsqrt(Math.fma(q1x, q1x, Math.fma(q1y, q1y, Math.fma(q1z, q1z, q1w * q1w))));
                q1x *= s;
                q1y *= s;
                q1z *= s;
                q1w *= s;
                alphaN = alphaN + alphaN - 1.0f;
            }
            dot = Math.fma(q1x, q2x, Math.fma(q1y, q2y, Math.fma(q1z, q2z, q1w * q2w)));
            absDot = Math.abs(dot);
        }
        final float scale0 = 1.0f - alphaN;
        final float scale2 = (dot >= 0.0f) ? alphaN : (-alphaN);
        final float resX = Math.fma(scale0, q1x, scale2 * q2x);
        final float resY = Math.fma(scale0, q1y, scale2 * q2y);
        final float resZ = Math.fma(scale0, q1z, scale2 * q2z);
        final float resW = Math.fma(scale0, q1w, scale2 * q2w);
        final float s2 = Math.invsqrt(Math.fma(resX, resX, Math.fma(resY, resY, Math.fma(resZ, resZ, resW * resW))));
        dest.x = resX * s2;
        dest.y = resY * s2;
        dest.z = resZ * s2;
        dest.w = resW * s2;
        return dest;
    }
    
    public Quaternionf nlerpIterative(final Quaternionfc q, final float alpha, final float dotThreshold) {
        return this.nlerpIterative(q, alpha, dotThreshold, this);
    }
    
    public static Quaternionfc nlerpIterative(final Quaternionf[] qs, final float[] weights, final float dotThreshold, final Quaternionf dest) {
        dest.set(qs[0]);
        float w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final float w2 = w;
            final float w3 = weights[i];
            final float rw1 = w3 / (w2 + w3);
            w += w3;
            dest.nlerpIterative(qs[i], rw1, dotThreshold);
        }
        return dest;
    }
    
    public Quaternionf lookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Quaternionf lookAlong(final Vector3fc dir, final Vector3fc up, final Quaternionf dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Quaternionf lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Quaternionf lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ, final Quaternionf dest) {
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final float dirnX = -dirX * invDirLength;
        final float dirnY = -dirY * invDirLength;
        final float dirnZ = -dirZ * invDirLength;
        float leftX = upY * dirnZ - upZ * dirnY;
        float leftY = upZ * dirnX - upX * dirnZ;
        float leftZ = upX * dirnY - upY * dirnX;
        final float invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final float upnX = dirnY * leftZ - dirnZ * leftY;
        final float upnY = dirnZ * leftX - dirnX * leftZ;
        final float upnZ = dirnX * leftY - dirnY * leftX;
        final double tr = leftX + upnY + dirnZ;
        float w;
        float x;
        float y;
        float z;
        if (tr >= 0.0) {
            double t = Math.sqrt(tr + 1.0);
            w = (float)(t * 0.5);
            t = 0.5 / t;
            x = (float)((dirnY - upnZ) * t);
            y = (float)((leftZ - dirnX) * t);
            z = (float)((upnX - leftY) * t);
        }
        else if (leftX > upnY && leftX > dirnZ) {
            double t = Math.sqrt(1.0 + leftX - upnY - dirnZ);
            x = (float)(t * 0.5);
            t = 0.5 / t;
            y = (float)((leftY + upnX) * t);
            z = (float)((dirnX + leftZ) * t);
            w = (float)((dirnY - upnZ) * t);
        }
        else if (upnY > dirnZ) {
            double t = Math.sqrt(1.0 + upnY - leftX - dirnZ);
            y = (float)(t * 0.5);
            t = 0.5 / t;
            x = (float)((leftY + upnX) * t);
            z = (float)((upnZ + dirnY) * t);
            w = (float)((leftZ - dirnX) * t);
        }
        else {
            double t = Math.sqrt(1.0 + dirnZ - leftX - upnY);
            z = (float)(t * 0.5);
            t = 0.5 / t;
            x = (float)((dirnX + leftZ) * t);
            y = (float)((upnZ + dirnY) * t);
            w = (float)((upnX - leftY) * t);
        }
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaternionf rotationTo(final float fromDirX, final float fromDirY, final float fromDirZ, final float toDirX, final float toDirY, final float toDirZ) {
        final float fn = Math.invsqrt(Math.fma(fromDirX, fromDirX, Math.fma(fromDirY, fromDirY, fromDirZ * fromDirZ)));
        final float tn = Math.invsqrt(Math.fma(toDirX, toDirX, Math.fma(toDirY, toDirY, toDirZ * toDirZ)));
        final float fx = fromDirX * fn;
        final float fy = fromDirY * fn;
        final float fz = fromDirZ * fn;
        final float tx = toDirX * tn;
        final float ty = toDirY * tn;
        final float tz = toDirZ * tn;
        final float dot = fx * tx + fy * ty + fz * tz;
        if (dot < -0.999999f) {
            float x = fy;
            float y = -fx;
            float z = 0.0f;
            float w = 0.0f;
            if (x * x + y * y == 0.0f) {
                x = 0.0f;
                y = fz;
                z = -fy;
                w = 0.0f;
            }
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = 0.0f;
        }
        else {
            final float sd2 = Math.sqrt((1.0f + dot) * 2.0f);
            final float isd2 = 1.0f / sd2;
            final float cx = fy * tz - fz * ty;
            final float cy = fz * tx - fx * tz;
            final float cz = fx * ty - fy * tx;
            final float x = cx * isd2;
            final float y = cy * isd2;
            final float z = cz * isd2;
            final float w = sd2 * 0.5f;
            final float n2 = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            this.x = x * n2;
            this.y = y * n2;
            this.z = z * n2;
            this.w = w * n2;
        }
        return this;
    }
    
    public Quaternionf rotationTo(final Vector3fc fromDir, final Vector3fc toDir) {
        return this.rotationTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z());
    }
    
    public Quaternionf rotateTo(final float fromDirX, final float fromDirY, final float fromDirZ, final float toDirX, final float toDirY, final float toDirZ, final Quaternionf dest) {
        final float fn = Math.invsqrt(Math.fma(fromDirX, fromDirX, Math.fma(fromDirY, fromDirY, fromDirZ * fromDirZ)));
        final float tn = Math.invsqrt(Math.fma(toDirX, toDirX, Math.fma(toDirY, toDirY, toDirZ * toDirZ)));
        final float fx = fromDirX * fn;
        final float fy = fromDirY * fn;
        final float fz = fromDirZ * fn;
        final float tx = toDirX * tn;
        final float ty = toDirY * tn;
        final float tz = toDirZ * tn;
        final float dot = fx * tx + fy * ty + fz * tz;
        float x;
        float y;
        float z;
        float w;
        if (dot < -0.999999f) {
            x = fy;
            y = -fx;
            z = 0.0f;
            w = 0.0f;
            if (x * x + y * y == 0.0f) {
                x = 0.0f;
                y = fz;
                z = -fy;
                w = 0.0f;
            }
        }
        else {
            final float sd2 = Math.sqrt((1.0f + dot) * 2.0f);
            final float isd2 = 1.0f / sd2;
            final float cx = fy * tz - fz * ty;
            final float cy = fz * tx - fx * tz;
            final float cz = fx * ty - fy * tx;
            x = cx * isd2;
            y = cy * isd2;
            z = cz * isd2;
            w = sd2 * 0.5f;
            final float n2 = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            x *= n2;
            y *= n2;
            z *= n2;
            w *= n2;
        }
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaternionf rotateTo(final float fromDirX, final float fromDirY, final float fromDirZ, final float toDirX, final float toDirY, final float toDirZ) {
        return this.rotateTo(fromDirX, fromDirY, fromDirZ, toDirX, toDirY, toDirZ, this);
    }
    
    public Quaternionf rotateTo(final Vector3fc fromDir, final Vector3fc toDir, final Quaternionf dest) {
        return this.rotateTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z(), dest);
    }
    
    public Quaternionf rotateTo(final Vector3fc fromDir, final Vector3fc toDir) {
        return this.rotateTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z(), this);
    }
    
    public Quaternionf rotateX(final float angle) {
        return this.rotateX(angle, this);
    }
    
    public Quaternionf rotateX(final float angle, final Quaternionf dest) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return dest.set(this.w * sin + this.x * cos, this.y * cos + this.z * sin, this.z * cos - this.y * sin, this.w * cos - this.x * sin);
    }
    
    public Quaternionf rotateY(final float angle) {
        return this.rotateY(angle, this);
    }
    
    public Quaternionf rotateY(final float angle, final Quaternionf dest) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return dest.set(this.x * cos - this.z * sin, this.w * sin + this.y * cos, this.x * sin + this.z * cos, this.w * cos - this.y * sin);
    }
    
    public Quaternionf rotateZ(final float angle) {
        return this.rotateZ(angle, this);
    }
    
    public Quaternionf rotateZ(final float angle, final Quaternionf dest) {
        final float sin = Math.sin(angle * 0.5f);
        final float cos = Math.cosFromSin(sin, angle * 0.5f);
        return dest.set(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.w * sin + this.z * cos, this.w * cos - this.z * sin);
    }
    
    public Quaternionf rotateLocalX(final float angle) {
        return this.rotateLocalX(angle, this);
    }
    
    public Quaternionf rotateLocalX(final float angle, final Quaternionf dest) {
        final float hangle = angle * 0.5f;
        final float s = Math.sin(hangle);
        final float c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x + s * this.w, c * this.y - s * this.z, c * this.z + s * this.y, c * this.w - s * this.x);
        return dest;
    }
    
    public Quaternionf rotateLocalY(final float angle) {
        return this.rotateLocalY(angle, this);
    }
    
    public Quaternionf rotateLocalY(final float angle, final Quaternionf dest) {
        final float hangle = angle * 0.5f;
        final float s = Math.sin(hangle);
        final float c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x + s * this.z, c * this.y + s * this.w, c * this.z - s * this.x, c * this.w - s * this.y);
        return dest;
    }
    
    public Quaternionf rotateLocalZ(final float angle) {
        return this.rotateLocalZ(angle, this);
    }
    
    public Quaternionf rotateLocalZ(final float angle, final Quaternionf dest) {
        final float hangle = angle * 0.5f;
        final float s = Math.sin(hangle);
        final float c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x - s * this.y, c * this.y + s * this.x, c * this.z + s * this.w, c * this.w - s * this.z);
        return dest;
    }
    
    public Quaternionf rotateAxis(final float angle, final float axisX, final float axisY, final float axisZ, final Quaternionf dest) {
        final float hangle = angle / 2.0f;
        final float sinAngle = Math.sin(hangle);
        final float invVLength = Math.invsqrt(Math.fma(axisX, axisX, Math.fma(axisY, axisY, axisZ * axisZ)));
        final float rx = axisX * invVLength * sinAngle;
        final float ry = axisY * invVLength * sinAngle;
        final float rz = axisZ * invVLength * sinAngle;
        final float rw = Math.cosFromSin(sinAngle, hangle);
        return dest.set(Math.fma(this.w, rx, Math.fma(this.x, rw, Math.fma(this.y, rz, -this.z * ry))), Math.fma(this.w, ry, Math.fma(-this.x, rz, Math.fma(this.y, rw, this.z * rx))), Math.fma(this.w, rz, Math.fma(this.x, ry, Math.fma(-this.y, rx, this.z * rw))), Math.fma(this.w, rw, Math.fma(-this.x, rx, Math.fma(-this.y, ry, -this.z * rz))));
    }
    
    public Quaternionf rotateAxis(final float angle, final Vector3fc axis, final Quaternionf dest) {
        return this.rotateAxis(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Quaternionf rotateAxis(final float angle, final Vector3fc axis) {
        return this.rotateAxis(angle, axis.x(), axis.y(), axis.z(), this);
    }
    
    public Quaternionf rotateAxis(final float angle, final float axisX, final float axisY, final float axisZ) {
        return this.rotateAxis(angle, axisX, axisY, axisZ, this);
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " " + Runtime.format(this.w, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.w);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.w = in.readFloat();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.w);
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
        result = 31 * result + Float.floatToIntBits(this.z);
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Quaternionf other = (Quaternionf)obj;
        return Float.floatToIntBits(this.w) == Float.floatToIntBits(other.w) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public Quaternionf difference(final Quaternionf other) {
        return this.difference(other, this);
    }
    
    public Quaternionf difference(final Quaternionfc other, final Quaternionf dest) {
        final float invNorm = 1.0f / this.lengthSquared();
        final float x = -this.x * invNorm;
        final float y = -this.y * invNorm;
        final float z = -this.z * invNorm;
        final float w = this.w * invNorm;
        dest.set(Math.fma(w, other.x(), Math.fma(x, other.w(), Math.fma(y, other.z(), -z * other.y()))), Math.fma(w, other.y(), Math.fma(-x, other.z(), Math.fma(y, other.w(), z * other.x()))), Math.fma(w, other.z(), Math.fma(x, other.y(), Math.fma(-y, other.x(), z * other.w()))), Math.fma(w, other.w(), Math.fma(-x, other.x(), Math.fma(-y, other.y(), -z * other.z()))));
        return dest;
    }
    
    public Vector3f positiveX(final Vector3f dir) {
        final float invNorm = 1.0f / this.lengthSquared();
        final float nx = -this.x * invNorm;
        final float ny = -this.y * invNorm;
        final float nz = -this.z * invNorm;
        final float nw = this.w * invNorm;
        final float dy = ny + ny;
        final float dz = nz + nz;
        dir.x = -ny * dy - nz * dz + 1.0f;
        dir.y = nx * dy + nw * dz;
        dir.z = nx * dz - nw * dy;
        return dir;
    }
    
    public Vector3f normalizedPositiveX(final Vector3f dir) {
        final float dy = this.y + this.y;
        final float dz = this.z + this.z;
        dir.x = -this.y * dy - this.z * dz + 1.0f;
        dir.y = this.x * dy - this.w * dz;
        dir.z = this.x * dz + this.w * dy;
        return dir;
    }
    
    public Vector3f positiveY(final Vector3f dir) {
        final float invNorm = 1.0f / this.lengthSquared();
        final float nx = -this.x * invNorm;
        final float ny = -this.y * invNorm;
        final float nz = -this.z * invNorm;
        final float nw = this.w * invNorm;
        final float dx = nx + nx;
        final float dy = ny + ny;
        final float dz = nz + nz;
        dir.x = nx * dy - nw * dz;
        dir.y = -nx * dx - nz * dz + 1.0f;
        dir.z = ny * dz + nw * dx;
        return dir;
    }
    
    public Vector3f normalizedPositiveY(final Vector3f dir) {
        final float dx = this.x + this.x;
        final float dy = this.y + this.y;
        final float dz = this.z + this.z;
        dir.x = this.x * dy + this.w * dz;
        dir.y = -this.x * dx - this.z * dz + 1.0f;
        dir.z = this.y * dz - this.w * dx;
        return dir;
    }
    
    public Vector3f positiveZ(final Vector3f dir) {
        final float invNorm = 1.0f / this.lengthSquared();
        final float nx = -this.x * invNorm;
        final float ny = -this.y * invNorm;
        final float nz = -this.z * invNorm;
        final float nw = this.w * invNorm;
        final float dx = nx + nx;
        final float dy = ny + ny;
        final float dz = nz + nz;
        dir.x = nx * dz + nw * dy;
        dir.y = ny * dz - nw * dx;
        dir.z = -nx * dx - ny * dy + 1.0f;
        return dir;
    }
    
    public Vector3f normalizedPositiveZ(final Vector3f dir) {
        final float dx = this.x + this.x;
        final float dy = this.y + this.y;
        final float dz = this.z + this.z;
        dir.x = this.x * dz - this.w * dy;
        dir.y = this.y * dz + this.w * dx;
        dir.z = -this.x * dx - this.y * dy + 1.0f;
        return dir;
    }
    
    public Quaternionf conjugateBy(final Quaternionfc q) {
        return this.conjugateBy(q, this);
    }
    
    public Quaternionf conjugateBy(final Quaternionfc q, final Quaternionf dest) {
        final float invNorm = 1.0f / q.lengthSquared();
        final float qix = -q.x() * invNorm;
        final float qiy = -q.y() * invNorm;
        final float qiz = -q.z() * invNorm;
        final float qiw = q.w() * invNorm;
        final float qpx = Math.fma(q.w(), this.x, Math.fma(q.x(), this.w, Math.fma(q.y(), this.z, -q.z() * this.y)));
        final float qpy = Math.fma(q.w(), this.y, Math.fma(-q.x(), this.z, Math.fma(q.y(), this.w, q.z() * this.x)));
        final float qpz = Math.fma(q.w(), this.z, Math.fma(q.x(), this.y, Math.fma(-q.y(), this.x, q.z() * this.w)));
        final float qpw = Math.fma(q.w(), this.w, Math.fma(-q.x(), this.x, Math.fma(-q.y(), this.y, -q.z() * this.z)));
        return dest.set(Math.fma(qpw, qix, Math.fma(qpx, qiw, Math.fma(qpy, qiz, -qpz * qiy))), Math.fma(qpw, qiy, Math.fma(-qpx, qiz, Math.fma(qpy, qiw, qpz * qix))), Math.fma(qpw, qiz, Math.fma(qpx, qiy, Math.fma(-qpy, qix, qpz * qiw))), Math.fma(qpw, qiw, Math.fma(-qpx, qix, Math.fma(-qpy, qiy, -qpz * qiz))));
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
    }
    
    public boolean equals(final Quaternionfc q, final float delta) {
        return this == q || (q != null && q instanceof Quaternionfc && Runtime.equals(this.x, q.x(), delta) && Runtime.equals(this.y, q.y(), delta) && Runtime.equals(this.z, q.z(), delta) && Runtime.equals(this.w, q.w(), delta));
    }
    
    public boolean equals(final float x, final float y, final float z, final float w) {
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(z) && Float.floatToIntBits(this.w) == Float.floatToIntBits(w);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
