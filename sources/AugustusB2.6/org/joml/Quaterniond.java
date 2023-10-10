// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.io.Externalizable;

public class Quaterniond implements Externalizable, Cloneable, Quaterniondc
{
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;
    public double z;
    public double w;
    
    public Quaterniond() {
        this.w = 1.0;
    }
    
    public Quaterniond(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Quaterniond(final Quaterniondc source) {
        this.x = source.x();
        this.y = source.y();
        this.z = source.z();
        this.w = source.w();
    }
    
    public Quaterniond(final Quaternionfc source) {
        this.x = source.x();
        this.y = source.y();
        this.z = source.z();
        this.w = source.w();
    }
    
    public Quaterniond(final AxisAngle4f axisAngle) {
        final double s = Math.sin(axisAngle.angle * 0.5);
        this.x = axisAngle.x * s;
        this.y = axisAngle.y * s;
        this.z = axisAngle.z * s;
        this.w = Math.cosFromSin(s, axisAngle.angle * 0.5);
    }
    
    public Quaterniond(final AxisAngle4d axisAngle) {
        final double s = Math.sin(axisAngle.angle * 0.5);
        this.x = axisAngle.x * s;
        this.y = axisAngle.y * s;
        this.z = axisAngle.z * s;
        this.w = Math.cosFromSin(s, axisAngle.angle * 0.5);
    }
    
    public double x() {
        return this.x;
    }
    
    public double y() {
        return this.y;
    }
    
    public double z() {
        return this.z;
    }
    
    public double w() {
        return this.w;
    }
    
    public Quaterniond normalize() {
        final double invNorm = Math.invsqrt(this.lengthSquared());
        this.x *= invNorm;
        this.y *= invNorm;
        this.z *= invNorm;
        this.w *= invNorm;
        return this;
    }
    
    public Quaterniond normalize(final Quaterniond dest) {
        final double invNorm = Math.invsqrt(this.lengthSquared());
        dest.x = this.x * invNorm;
        dest.y = this.y * invNorm;
        dest.z = this.z * invNorm;
        dest.w = this.w * invNorm;
        return dest;
    }
    
    public Quaterniond add(final double x, final double y, final double z, final double w) {
        return this.add(x, y, z, w, this);
    }
    
    public Quaterniond add(final double x, final double y, final double z, final double w, final Quaterniond dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        dest.w = this.w + w;
        return dest;
    }
    
    public Quaterniond add(final Quaterniondc q2) {
        this.x += q2.x();
        this.y += q2.y();
        this.z += q2.z();
        this.w += q2.w();
        return this;
    }
    
    public Quaterniond add(final Quaterniondc q2, final Quaterniond dest) {
        dest.x = this.x + q2.x();
        dest.y = this.y + q2.y();
        dest.z = this.z + q2.z();
        dest.w = this.w + q2.w();
        return dest;
    }
    
    public double dot(final Quaterniondc otherQuat) {
        return this.x * otherQuat.x() + this.y * otherQuat.y() + this.z * otherQuat.z() + this.w * otherQuat.w();
    }
    
    public double angle() {
        return 2.0 * Math.safeAcos(this.w);
    }
    
    public Matrix3d get(final Matrix3d dest) {
        return dest.set(this);
    }
    
    public Matrix3f get(final Matrix3f dest) {
        return dest.set(this);
    }
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set(this);
    }
    
    public Matrix4f get(final Matrix4f dest) {
        return dest.set(this);
    }
    
    public AxisAngle4f get(final AxisAngle4f dest) {
        double x = this.x;
        double y = this.y;
        double z = this.z;
        double w = this.w;
        if (w > 1.0) {
            final double invNorm = Math.invsqrt(this.lengthSquared());
            x *= invNorm;
            y *= invNorm;
            z *= invNorm;
            w *= invNorm;
        }
        dest.angle = (float)(2.0 * Math.acos(w));
        double s = Math.sqrt(1.0 - w * w);
        if (s < 0.001) {
            dest.x = (float)x;
            dest.y = (float)y;
            dest.z = (float)z;
        }
        else {
            s = 1.0 / s;
            dest.x = (float)(x * s);
            dest.y = (float)(y * s);
            dest.z = (float)(z * s);
        }
        return dest;
    }
    
    public AxisAngle4d get(final AxisAngle4d dest) {
        double x = this.x;
        double y = this.y;
        double z = this.z;
        double w = this.w;
        if (w > 1.0) {
            final double invNorm = Math.invsqrt(this.lengthSquared());
            x *= invNorm;
            y *= invNorm;
            z *= invNorm;
            w *= invNorm;
        }
        dest.angle = 2.0 * Math.acos(w);
        double s = Math.sqrt(1.0 - w * w);
        if (s < 0.001) {
            dest.x = x;
            dest.y = y;
            dest.z = z;
        }
        else {
            s = 1.0 / s;
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
    
    public Quaterniond set(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Quaterniond set(final Quaterniondc q) {
        this.x = q.x();
        this.y = q.y();
        this.z = q.z();
        this.w = q.w();
        return this;
    }
    
    public Quaterniond set(final Quaternionfc q) {
        this.x = q.x();
        this.y = q.y();
        this.z = q.z();
        this.w = q.w();
        return this;
    }
    
    public Quaterniond set(final AxisAngle4f axisAngle) {
        return this.setAngleAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaterniond set(final AxisAngle4d axisAngle) {
        return this.setAngleAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaterniond setAngleAxis(final double angle, final double x, final double y, final double z) {
        final double s = Math.sin(angle * 0.5);
        this.x = x * s;
        this.y = y * s;
        this.z = z * s;
        this.w = Math.cosFromSin(s, angle * 0.5);
        return this;
    }
    
    public Quaterniond setAngleAxis(final double angle, final Vector3dc axis) {
        return this.setAngleAxis(angle, axis.x(), axis.y(), axis.z());
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
            this.w = t * 0.5;
            t = 0.5 / t;
            this.x = (m12 - m21) * t;
            this.y = (m20 - m02) * t;
            this.z = (m01 - m10) * t;
        }
        else if (m00 >= m11 && m00 >= m22) {
            double t = Math.sqrt(m00 - (m11 + m22) + 1.0);
            this.x = t * 0.5;
            t = 0.5 / t;
            this.y = (m10 + m01) * t;
            this.z = (m02 + m20) * t;
            this.w = (m12 - m21) * t;
        }
        else if (m11 > m22) {
            double t = Math.sqrt(m11 - (m22 + m00) + 1.0);
            this.y = t * 0.5;
            t = 0.5 / t;
            this.z = (m21 + m12) * t;
            this.x = (m10 + m01) * t;
            this.w = (m20 - m02) * t;
        }
        else {
            double t = Math.sqrt(m22 - (m00 + m11) + 1.0);
            this.z = t * 0.5;
            t = 0.5 / t;
            this.x = (m02 + m20) * t;
            this.y = (m21 + m12) * t;
            this.w = (m01 - m10) * t;
        }
    }
    
    public Quaterniond setFromUnnormalized(final Matrix4fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromUnnormalized(final Matrix4x3fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromUnnormalized(final Matrix4x3dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix4fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix4x3fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix4x3dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromUnnormalized(final Matrix4dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix4dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromUnnormalized(final Matrix3fc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix3fc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromUnnormalized(final Matrix3dc mat) {
        this.setFromUnnormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond setFromNormalized(final Matrix3dc mat) {
        this.setFromNormalized(mat.m00(), mat.m01(), mat.m02(), mat.m10(), mat.m11(), mat.m12(), mat.m20(), mat.m21(), mat.m22());
        return this;
    }
    
    public Quaterniond fromAxisAngleRad(final Vector3dc axis, final double angle) {
        return this.fromAxisAngleRad(axis.x(), axis.y(), axis.z(), angle);
    }
    
    public Quaterniond fromAxisAngleRad(final double axisX, final double axisY, final double axisZ, final double angle) {
        final double hangle = angle / 2.0;
        final double sinAngle = Math.sin(hangle);
        final double vLength = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        this.x = axisX / vLength * sinAngle;
        this.y = axisY / vLength * sinAngle;
        this.z = axisZ / vLength * sinAngle;
        this.w = Math.cosFromSin(sinAngle, hangle);
        return this;
    }
    
    public Quaterniond fromAxisAngleDeg(final Vector3dc axis, final double angle) {
        return this.fromAxisAngleRad(axis.x(), axis.y(), axis.z(), Math.toRadians(angle));
    }
    
    public Quaterniond fromAxisAngleDeg(final double axisX, final double axisY, final double axisZ, final double angle) {
        return this.fromAxisAngleRad(axisX, axisY, axisZ, Math.toRadians(angle));
    }
    
    public Quaterniond mul(final Quaterniondc q) {
        return this.mul(q, this);
    }
    
    public Quaterniond mul(final Quaterniondc q, final Quaterniond dest) {
        return this.mul(q.x(), q.y(), q.z(), q.w(), dest);
    }
    
    public Quaterniond mul(final double qx, final double qy, final double qz, final double qw) {
        return this.mul(qx, qy, qz, qw, this);
    }
    
    public Quaterniond mul(final double qx, final double qy, final double qz, final double qw, final Quaterniond dest) {
        return dest.set(Math.fma(this.w, qx, Math.fma(this.x, qw, Math.fma(this.y, qz, -this.z * qy))), Math.fma(this.w, qy, Math.fma(-this.x, qz, Math.fma(this.y, qw, this.z * qx))), Math.fma(this.w, qz, Math.fma(this.x, qy, Math.fma(-this.y, qx, this.z * qw))), Math.fma(this.w, qw, Math.fma(-this.x, qx, Math.fma(-this.y, qy, -this.z * qz))));
    }
    
    public Quaterniond premul(final Quaterniondc q) {
        return this.premul(q, this);
    }
    
    public Quaterniond premul(final Quaterniondc q, final Quaterniond dest) {
        return this.premul(q.x(), q.y(), q.z(), q.w(), dest);
    }
    
    public Quaterniond premul(final double qx, final double qy, final double qz, final double qw) {
        return this.premul(qx, qy, qz, qw, this);
    }
    
    public Quaterniond premul(final double qx, final double qy, final double qz, final double qw, final Quaterniond dest) {
        return dest.set(Math.fma(qw, this.x, Math.fma(qx, this.w, Math.fma(qy, this.z, -qz * this.y))), Math.fma(qw, this.y, Math.fma(-qx, this.z, Math.fma(qy, this.w, qz * this.x))), Math.fma(qw, this.z, Math.fma(qx, this.y, Math.fma(-qy, this.x, qz * this.w))), Math.fma(qw, this.w, Math.fma(-qx, this.x, Math.fma(-qy, this.y, -qz * this.z))));
    }
    
    public Vector3d transform(final Vector3d vec) {
        return this.transform(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3d transformInverse(final Vector3d vec) {
        return this.transformInverse(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3d transformUnit(final Vector3d vec) {
        return this.transformUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3d transformInverseUnit(final Vector3d vec) {
        return this.transformInverseUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3d transformPositiveX(final Vector3d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4d transformPositiveX(final Vector4d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        dest.x = ww + xx - zz - yy;
        dest.y = xy + zw + zw + xy;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3d transformUnitPositiveX(final Vector3d dest) {
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double zw = this.z * this.w;
        dest.x = 1.0 - yy - yy - zz - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector4d transformUnitPositiveX(final Vector4d dest) {
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double zw = this.z * this.w;
        dest.x = 1.0 - yy - yy - zz - zz;
        dest.y = xy + zw + xy + zw;
        dest.z = xz - yw + xz - yw;
        return dest;
    }
    
    public Vector3d transformPositiveY(final Vector3d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4d transformPositiveY(final Vector4d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = -zw + xy - zw + xy;
        dest.y = yy - zz + ww - xx;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector4d transformUnitPositiveY(final Vector4d dest) {
        final double xx = this.x * this.x;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0 - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3d transformUnitPositiveY(final Vector3d dest) {
        final double xx = this.x * this.x;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        dest.x = xy - zw + xy - zw;
        dest.y = 1.0 - xx - xx - zz - zz;
        dest.z = yz + yz + xw + xw;
        return dest;
    }
    
    public Vector3d transformPositiveZ(final Vector3d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4d transformPositiveZ(final Vector4d dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = yw + xz + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = zz - yy - xx + ww;
        return dest;
    }
    
    public Vector4d transformUnitPositiveZ(final Vector4d dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0 - xx - xx - yy - yy;
        return dest;
    }
    
    public Vector3d transformUnitPositiveZ(final Vector3d dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double yw = this.y * this.w;
        dest.x = xz + yw + xz + yw;
        dest.y = yz + yz - xw - xw;
        dest.z = 1.0 - xx - xx - yy - yy;
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
    
    public Vector3d transform(final double x, final double y, final double z, final Vector3d dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double ww = this.w * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        final double yw = this.y * this.w;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy - zw) * k, y, 2.0 * (xz + yw) * k * z)), Math.fma(2.0 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz - xw) * k * z)), Math.fma(2.0 * (xz - yw) * k, x, Math.fma(2.0 * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3d transformInverse(final double x, final double y, final double z, final Vector3d dest) {
        final double n = 1.0 / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final double qx = this.x * n;
        final double qy = this.y * n;
        final double qz = this.z * n;
        final double qw = this.w * n;
        final double xx = qx * qx;
        final double yy = qy * qy;
        final double zz = qz * qz;
        final double ww = qw * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yz = qy * qz;
        final double xw = qx * qw;
        final double zw = qz * qw;
        final double yw = qy * qw;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy + zw) * k, y, 2.0 * (xz - yw) * k * z)), Math.fma(2.0 * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz + xw) * k * z)), Math.fma(2.0 * (xz + yw) * k, x, Math.fma(2.0 * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4d transform(final Vector4dc vec, final Vector4d dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformInverse(final Vector4dc vec, final Vector4d dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transform(final double x, final double y, final double z, final Vector4d dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double ww = this.w * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        final double yw = this.y * this.w;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy - zw) * k, y, 2.0 * (xz + yw) * k * z)), Math.fma(2.0 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz - xw) * k * z)), Math.fma(2.0 * (xz - yw) * k, x, Math.fma(2.0 * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)), dest.w);
    }
    
    public Vector4d transformInverse(final double x, final double y, final double z, final Vector4d dest) {
        final double n = 1.0 / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final double qx = this.x * n;
        final double qy = this.y * n;
        final double qz = this.z * n;
        final double qw = this.w * n;
        final double xx = qx * qx;
        final double yy = qy * qy;
        final double zz = qz * qz;
        final double ww = qw * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yz = qy * qz;
        final double xw = qx * qw;
        final double zw = qz * qw;
        final double yw = qy * qw;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy + zw) * k, y, 2.0 * (xz - yw) * k * z)), Math.fma(2.0 * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz + xw) * k * z)), Math.fma(2.0 * (xz + yw) * k, x, Math.fma(2.0 * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3f transform(final Vector3f vec) {
        return this.transform(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformInverse(final Vector3f vec) {
        return this.transformInverse(vec.x, vec.y, vec.z, vec);
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
    
    public Vector3d transformUnit(final double x, final double y, final double z, final Vector3d dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy - zw), y, 2.0 * (xz + yw) * z)), Math.fma(2.0 * (xy + zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz - xw) * z)), Math.fma(2.0 * (xz - yw), x, Math.fma(2.0 * (yz + xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Vector3d transformInverseUnit(final double x, final double y, final double z, final Vector3d dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy + zw), y, 2.0 * (xz - yw) * z)), Math.fma(2.0 * (xy - zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz + xw) * z)), Math.fma(2.0 * (xz + yw), x, Math.fma(2.0 * (yz - xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Vector4d transformUnit(final Vector4dc vec, final Vector4d dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformInverseUnit(final Vector4dc vec, final Vector4d dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4d transformUnit(final double x, final double y, final double z, final Vector4d dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy - zw), y, 2.0 * (xz + yw) * z)), Math.fma(2.0 * (xy + zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz - xw) * z)), Math.fma(2.0 * (xz - yw), x, Math.fma(2.0 * (yz + xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)), dest.w);
    }
    
    public Vector4d transformInverseUnit(final double x, final double y, final double z, final Vector4d dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set(Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy + zw), y, 2.0 * (xz - yw) * z)), Math.fma(2.0 * (xy - zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz + xw) * z)), Math.fma(2.0 * (xz + yw), x, Math.fma(2.0 * (yz - xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)), dest.w);
    }
    
    public Vector3f transformUnit(final Vector3f vec) {
        return this.transformUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformInverseUnit(final Vector3f vec) {
        return this.transformInverseUnit(vec.x, vec.y, vec.z, vec);
    }
    
    public Vector3f transformPositiveX(final Vector3f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        dest.x = (float)(ww + xx - zz - yy);
        dest.y = (float)(xy + zw + zw + xy);
        dest.z = (float)(xz - yw + xz - yw);
        return dest;
    }
    
    public Vector4f transformPositiveX(final Vector4f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        dest.x = (float)(ww + xx - zz - yy);
        dest.y = (float)(xy + zw + zw + xy);
        dest.z = (float)(xz - yw + xz - yw);
        return dest;
    }
    
    public Vector3f transformUnitPositiveX(final Vector3f dest) {
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double zw = this.z * this.w;
        dest.x = (float)(1.0 - yy - yy - zz - zz);
        dest.y = (float)(xy + zw + xy + zw);
        dest.z = (float)(xz - yw + xz - yw);
        return dest;
    }
    
    public Vector4f transformUnitPositiveX(final Vector4f dest) {
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double zw = this.z * this.w;
        dest.x = (float)(1.0 - yy - yy - zz - zz);
        dest.y = (float)(xy + zw + xy + zw);
        dest.z = (float)(xz - yw + xz - yw);
        return dest;
    }
    
    public Vector3f transformPositiveY(final Vector3f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = (float)(-zw + xy - zw + xy);
        dest.y = (float)(yy - zz + ww - xx);
        dest.z = (float)(yz + yz + xw + xw);
        return dest;
    }
    
    public Vector4f transformPositiveY(final Vector4f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = (float)(-zw + xy - zw + xy);
        dest.y = (float)(yy - zz + ww - xx);
        dest.z = (float)(yz + yz + xw + xw);
        return dest;
    }
    
    public Vector4f transformUnitPositiveY(final Vector4f dest) {
        final double xx = this.x * this.x;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        dest.x = (float)(xy - zw + xy - zw);
        dest.y = (float)(1.0 - xx - xx - zz - zz);
        dest.z = (float)(yz + yz + xw + xw);
        return dest;
    }
    
    public Vector3f transformUnitPositiveY(final Vector3f dest) {
        final double xx = this.x * this.x;
        final double zz = this.z * this.z;
        final double xy = this.x * this.y;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        dest.x = (float)(xy - zw + xy - zw);
        dest.y = (float)(1.0 - xx - xx - zz - zz);
        dest.z = (float)(yz + yz + xw + xw);
        return dest;
    }
    
    public Vector3f transformPositiveZ(final Vector3f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = (float)(yw + xz + xz + yw);
        dest.y = (float)(yz + yz - xw - xw);
        dest.z = (float)(zz - yy - xx + ww);
        return dest;
    }
    
    public Vector4f transformPositiveZ(final Vector4f dest) {
        final double ww = this.w * this.w;
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double xz = this.x * this.z;
        final double yw = this.y * this.w;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        dest.x = (float)(yw + xz + xz + yw);
        dest.y = (float)(yz + yz - xw - xw);
        dest.z = (float)(zz - yy - xx + ww);
        return dest;
    }
    
    public Vector4f transformUnitPositiveZ(final Vector4f dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double yw = this.y * this.w;
        dest.x = (float)(xz + yw + xz + yw);
        dest.y = (float)(yz + yz - xw - xw);
        dest.z = (float)(1.0 - xx - xx - yy - yy);
        return dest;
    }
    
    public Vector3f transformUnitPositiveZ(final Vector3f dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double yw = this.y * this.w;
        dest.x = (float)(xz + yw + xz + yw);
        dest.y = (float)(yz + yz - xw - xw);
        dest.z = (float)(1.0 - xx - xx - yy - yy);
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
    
    public Vector3f transform(final double x, final double y, final double z, final Vector3f dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double ww = this.w * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        final double yw = this.y * this.w;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy - zw) * k, y, 2.0 * (xz + yw) * k * z)), Math.fma(2.0 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz - xw) * k * z)), Math.fma(2.0 * (xz - yw) * k, x, Math.fma(2.0 * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector3f transformInverse(final double x, final double y, final double z, final Vector3f dest) {
        final double n = 1.0 / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final double qx = this.x * n;
        final double qy = this.y * n;
        final double qz = this.z * n;
        final double qw = this.w * n;
        final double xx = qx * qx;
        final double yy = qy * qy;
        final double zz = qz * qz;
        final double ww = qw * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yz = qy * qz;
        final double xw = qx * qw;
        final double zw = qz * qw;
        final double yw = qy * qw;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy + zw) * k, y, 2.0 * (xz - yw) * k * z)), Math.fma(2.0 * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz + xw) * k * z)), Math.fma(2.0 * (xz + yw) * k, x, Math.fma(2.0 * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)));
    }
    
    public Vector4f transform(final Vector4fc vec, final Vector4f dest) {
        return this.transform(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformInverse(final Vector4fc vec, final Vector4f dest) {
        return this.transformInverse(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transform(final double x, final double y, final double z, final Vector4f dest) {
        final double xx = this.x * this.x;
        final double yy = this.y * this.y;
        final double zz = this.z * this.z;
        final double ww = this.w * this.w;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double yz = this.y * this.z;
        final double xw = this.x * this.w;
        final double zw = this.z * this.w;
        final double yw = this.y * this.w;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set((float)Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy - zw) * k, y, 2.0 * (xz + yw) * k * z)), (float)Math.fma(2.0 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz - xw) * k * z)), (float)Math.fma(2.0 * (xz - yw) * k, x, Math.fma(2.0 * (yz + xw) * k, y, (zz - xx - yy + ww) * k * z)), dest.w);
    }
    
    public Vector4f transformInverse(final double x, final double y, final double z, final Vector4f dest) {
        final double n = 1.0 / Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final double qx = this.x * n;
        final double qy = this.y * n;
        final double qz = this.z * n;
        final double qw = this.w * n;
        final double xx = qx * qx;
        final double yy = qy * qy;
        final double zz = qz * qz;
        final double ww = qw * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yz = qy * qz;
        final double xw = qx * qw;
        final double zw = qz * qw;
        final double yw = qy * qw;
        final double k = 1.0 / (xx + yy + zz + ww);
        return dest.set(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2.0 * (xy + zw) * k, y, 2.0 * (xz - yw) * k * z)), Math.fma(2.0 * (xy - zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, 2.0 * (yz + xw) * k * z)), Math.fma(2.0 * (xz + yw) * k, x, Math.fma(2.0 * (yz - xw) * k, y, (zz - xx - yy + ww) * k * z)), dest.w);
    }
    
    public Vector4f transformUnit(final Vector4f vec) {
        return this.transformUnit(vec, vec);
    }
    
    public Vector4f transformInverseUnit(final Vector4f vec) {
        return this.transformInverseUnit(vec, vec);
    }
    
    public Vector3f transformUnit(final Vector3fc vec, final Vector3f dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transformInverseUnit(final Vector3fc vec, final Vector3f dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector3f transformUnit(final double x, final double y, final double z, final Vector3f dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set((float)Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy - zw), y, 2.0 * (xz + yw) * z)), (float)Math.fma(2.0 * (xy + zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz - xw) * z)), (float)Math.fma(2.0 * (xz - yw), x, Math.fma(2.0 * (yz + xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Vector3f transformInverseUnit(final double x, final double y, final double z, final Vector3f dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set((float)Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy + zw), y, 2.0 * (xz - yw) * z)), (float)Math.fma(2.0 * (xy - zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz + xw) * z)), (float)Math.fma(2.0 * (xz + yw), x, Math.fma(2.0 * (yz - xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Vector4f transformUnit(final Vector4fc vec, final Vector4f dest) {
        return this.transformUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformInverseUnit(final Vector4fc vec, final Vector4f dest) {
        return this.transformInverseUnit(vec.x(), vec.y(), vec.z(), dest);
    }
    
    public Vector4f transformUnit(final double x, final double y, final double z, final Vector4f dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set((float)Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy - zw), y, 2.0 * (xz + yw) * z)), (float)Math.fma(2.0 * (xy + zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz - xw) * z)), (float)Math.fma(2.0 * (xz - yw), x, Math.fma(2.0 * (yz + xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Vector4f transformInverseUnit(final double x, final double y, final double z, final Vector4f dest) {
        final double xx = this.x * this.x;
        final double xy = this.x * this.y;
        final double xz = this.x * this.z;
        final double xw = this.x * this.w;
        final double yy = this.y * this.y;
        final double yz = this.y * this.z;
        final double yw = this.y * this.w;
        final double zz = this.z * this.z;
        final double zw = this.z * this.w;
        return dest.set((float)Math.fma(Math.fma(-2.0, yy + zz, 1.0), x, Math.fma(2.0 * (xy + zw), y, 2.0 * (xz - yw) * z)), (float)Math.fma(2.0 * (xy - zw), x, Math.fma(Math.fma(-2.0, xx + zz, 1.0), y, 2.0 * (yz + xw) * z)), (float)Math.fma(2.0 * (xz + yw), x, Math.fma(2.0 * (yz - xw), y, Math.fma(-2.0, xx + yy, 1.0) * z)));
    }
    
    public Quaterniond invert(final Quaterniond dest) {
        final double invNorm = 1.0 / this.lengthSquared();
        dest.x = -this.x * invNorm;
        dest.y = -this.y * invNorm;
        dest.z = -this.z * invNorm;
        dest.w = this.w * invNorm;
        return dest;
    }
    
    public Quaterniond invert() {
        return this.invert(this);
    }
    
    public Quaterniond div(final Quaterniondc b, final Quaterniond dest) {
        final double invNorm = 1.0 / Math.fma(b.x(), b.x(), Math.fma(b.y(), b.y(), Math.fma(b.z(), b.z(), b.w() * b.w())));
        final double x = -b.x() * invNorm;
        final double y = -b.y() * invNorm;
        final double z = -b.z() * invNorm;
        final double w = b.w() * invNorm;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaterniond div(final Quaterniondc b) {
        return this.div(b, this);
    }
    
    public Quaterniond conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    public Quaterniond conjugate(final Quaterniond dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = this.w;
        return dest;
    }
    
    public Quaterniond identity() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = 1.0;
        return this;
    }
    
    public double lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
    }
    
    public Quaterniond rotationXYZ(final double angleX, final double angleY, final double angleZ) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double cycz = cy * cz;
        final double sysz = sy * sz;
        final double sycz = sy * cz;
        final double cysz = cy * sz;
        this.w = cx * cycz - sx * sysz;
        this.x = sx * cycz + cx * sysz;
        this.y = cx * sycz - sx * cysz;
        this.z = cx * cysz + sx * sycz;
        return this;
    }
    
    public Quaterniond rotationZYX(final double angleZ, final double angleY, final double angleX) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double cycz = cy * cz;
        final double sysz = sy * sz;
        final double sycz = sy * cz;
        final double cysz = cy * sz;
        this.w = cx * cycz + sx * sysz;
        this.x = sx * cycz - cx * sysz;
        this.y = cx * sycz + sx * cysz;
        this.z = cx * cysz - sx * sycz;
        return this;
    }
    
    public Quaterniond rotationYXZ(final double angleY, final double angleX, final double angleZ) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double x = cy * sx;
        final double y = sy * cx;
        final double z = sy * sx;
        final double w = cy * cx;
        this.x = x * cz + y * sz;
        this.y = y * cz - x * sz;
        this.z = w * sz - z * cz;
        this.w = w * cz + z * sz;
        return this;
    }
    
    public Quaterniond slerp(final Quaterniondc target, final double alpha) {
        return this.slerp(target, alpha, this);
    }
    
    public Quaterniond slerp(final Quaterniondc target, final double alpha, final Quaterniond dest) {
        final double cosom = Math.fma(this.x, target.x(), Math.fma(this.y, target.y(), Math.fma(this.z, target.z(), this.w * target.w())));
        final double absCosom = Math.abs(cosom);
        double scale0;
        double scale2;
        if (1.0 - absCosom > 1.0E-6) {
            final double sinSqr = 1.0 - absCosom * absCosom;
            final double sinom = Math.invsqrt(sinSqr);
            final double omega = Math.atan2(sinSqr * sinom, absCosom);
            scale0 = Math.sin((1.0 - alpha) * omega) * sinom;
            scale2 = Math.sin(alpha * omega) * sinom;
        }
        else {
            scale0 = 1.0 - alpha;
            scale2 = alpha;
        }
        scale2 = ((cosom >= 0.0) ? scale2 : (-scale2));
        dest.x = Math.fma(scale0, this.x, scale2 * target.x());
        dest.y = Math.fma(scale0, this.y, scale2 * target.y());
        dest.z = Math.fma(scale0, this.z, scale2 * target.z());
        dest.w = Math.fma(scale0, this.w, scale2 * target.w());
        return dest;
    }
    
    public static Quaterniondc slerp(final Quaterniond[] qs, final double[] weights, final Quaterniond dest) {
        dest.set(qs[0]);
        double w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final double w2 = w;
            final double w3 = weights[i];
            final double rw1 = w3 / (w2 + w3);
            w += w3;
            dest.slerp(qs[i], rw1);
        }
        return dest;
    }
    
    public Quaterniond scale(final double factor) {
        return this.scale(factor, this);
    }
    
    public Quaterniond scale(final double factor, final Quaterniond dest) {
        final double sqrt = Math.sqrt(factor);
        dest.x = sqrt * this.x;
        dest.y = sqrt * this.y;
        dest.z = sqrt * this.z;
        dest.w = sqrt * this.w;
        return dest;
    }
    
    public Quaterniond scaling(final double factor) {
        final double sqrt = Math.sqrt(factor);
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = sqrt;
        return this;
    }
    
    public Quaterniond integrate(final double dt, final double vx, final double vy, final double vz) {
        return this.integrate(dt, vx, vy, vz, this);
    }
    
    public Quaterniond integrate(final double dt, final double vx, final double vy, final double vz, final Quaterniond dest) {
        final double thetaX = dt * vx * 0.5;
        final double thetaY = dt * vy * 0.5;
        final double thetaZ = dt * vz * 0.5;
        final double thetaMagSq = thetaX * thetaX + thetaY * thetaY + thetaZ * thetaZ;
        double dqW;
        double s;
        if (thetaMagSq * thetaMagSq / 24.0 < 1.0E-8) {
            dqW = 1.0 - thetaMagSq * 0.5;
            s = 1.0 - thetaMagSq / 6.0;
        }
        else {
            final double thetaMag = Math.sqrt(thetaMagSq);
            final double sin = Math.sin(thetaMag);
            s = sin / thetaMag;
            dqW = Math.cosFromSin(sin, thetaMag);
        }
        final double dqX = thetaX * s;
        final double dqY = thetaY * s;
        final double dqZ = thetaZ * s;
        return dest.set(Math.fma(dqW, this.x, Math.fma(dqX, this.w, Math.fma(dqY, this.z, -dqZ * this.y))), Math.fma(dqW, this.y, Math.fma(-dqX, this.z, Math.fma(dqY, this.w, dqZ * this.x))), Math.fma(dqW, this.z, Math.fma(dqX, this.y, Math.fma(-dqY, this.x, dqZ * this.w))), Math.fma(dqW, this.w, Math.fma(-dqX, this.x, Math.fma(-dqY, this.y, -dqZ * this.z))));
    }
    
    public Quaterniond nlerp(final Quaterniondc q, final double factor) {
        return this.nlerp(q, factor, this);
    }
    
    public Quaterniond nlerp(final Quaterniondc q, final double factor, final Quaterniond dest) {
        final double cosom = Math.fma(this.x, q.x(), Math.fma(this.y, q.y(), Math.fma(this.z, q.z(), this.w * q.w())));
        final double scale0 = 1.0 - factor;
        final double scale2 = (cosom >= 0.0) ? factor : (-factor);
        dest.x = Math.fma(scale0, this.x, scale2 * q.x());
        dest.y = Math.fma(scale0, this.y, scale2 * q.y());
        dest.z = Math.fma(scale0, this.z, scale2 * q.z());
        dest.w = Math.fma(scale0, this.w, scale2 * q.w());
        final double s = Math.invsqrt(Math.fma(dest.x, dest.x, Math.fma(dest.y, dest.y, Math.fma(dest.z, dest.z, dest.w * dest.w))));
        dest.x *= s;
        dest.y *= s;
        dest.z *= s;
        dest.w *= s;
        return dest;
    }
    
    public static Quaterniondc nlerp(final Quaterniond[] qs, final double[] weights, final Quaterniond dest) {
        dest.set(qs[0]);
        double w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final double w2 = w;
            final double w3 = weights[i];
            final double rw1 = w3 / (w2 + w3);
            w += w3;
            dest.nlerp(qs[i], rw1);
        }
        return dest;
    }
    
    public Quaterniond nlerpIterative(final Quaterniondc q, final double alpha, final double dotThreshold, final Quaterniond dest) {
        double q1x = this.x;
        double q1y = this.y;
        double q1z = this.z;
        double q1w = this.w;
        double q2x = q.x();
        double q2y = q.y();
        double q2z = q.z();
        double q2w = q.w();
        double dot = Math.fma(q1x, q2x, Math.fma(q1y, q2y, Math.fma(q1z, q2z, q1w * q2w)));
        double absDot = Math.abs(dot);
        if (0.999999 < absDot) {
            return dest.set(this);
        }
        double alphaN = alpha;
        while (absDot < dotThreshold) {
            final double scale0 = 0.5;
            final double scale2 = (dot >= 0.0) ? 0.5 : -0.5;
            if (alphaN < 0.5) {
                q2x = Math.fma(scale0, q2x, scale2 * q1x);
                q2y = Math.fma(scale0, q2y, scale2 * q1y);
                q2z = Math.fma(scale0, q2z, scale2 * q1z);
                q2w = Math.fma(scale0, q2w, scale2 * q1w);
                final float s = (float)Math.invsqrt(Math.fma(q2x, q2x, Math.fma(q2y, q2y, Math.fma(q2z, q2z, q2w * q2w))));
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
                final float s = (float)Math.invsqrt(Math.fma(q1x, q1x, Math.fma(q1y, q1y, Math.fma(q1z, q1z, q1w * q1w))));
                q1x *= s;
                q1y *= s;
                q1z *= s;
                q1w *= s;
                alphaN = alphaN + alphaN - 1.0;
            }
            dot = Math.fma(q1x, q2x, Math.fma(q1y, q2y, Math.fma(q1z, q2z, q1w * q2w)));
            absDot = Math.abs(dot);
        }
        final double scale0 = 1.0 - alphaN;
        final double scale2 = (dot >= 0.0) ? alphaN : (-alphaN);
        final double resX = Math.fma(scale0, q1x, scale2 * q2x);
        final double resY = Math.fma(scale0, q1y, scale2 * q2y);
        final double resZ = Math.fma(scale0, q1z, scale2 * q2z);
        final double resW = Math.fma(scale0, q1w, scale2 * q2w);
        final double s2 = Math.invsqrt(Math.fma(resX, resX, Math.fma(resY, resY, Math.fma(resZ, resZ, resW * resW))));
        dest.x = resX * s2;
        dest.y = resY * s2;
        dest.z = resZ * s2;
        dest.w = resW * s2;
        return dest;
    }
    
    public Quaterniond nlerpIterative(final Quaterniondc q, final double alpha, final double dotThreshold) {
        return this.nlerpIterative(q, alpha, dotThreshold, this);
    }
    
    public static Quaterniond nlerpIterative(final Quaterniondc[] qs, final double[] weights, final double dotThreshold, final Quaterniond dest) {
        dest.set(qs[0]);
        double w = weights[0];
        for (int i = 1; i < qs.length; ++i) {
            final double w2 = w;
            final double w3 = weights[i];
            final double rw1 = w3 / (w2 + w3);
            w += w3;
            dest.nlerpIterative(qs[i], rw1, dotThreshold);
        }
        return dest;
    }
    
    public Quaterniond lookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Quaterniond lookAlong(final Vector3dc dir, final Vector3dc up, final Quaterniond dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Quaterniond lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Quaterniond lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ, final Quaterniond dest) {
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final double dirnX = -dirX * invDirLength;
        final double dirnY = -dirY * invDirLength;
        final double dirnZ = -dirZ * invDirLength;
        double leftX = upY * dirnZ - upZ * dirnY;
        double leftY = upZ * dirnX - upX * dirnZ;
        double leftZ = upX * dirnY - upY * dirnX;
        final double invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final double upnX = dirnY * leftZ - dirnZ * leftY;
        final double upnY = dirnZ * leftX - dirnX * leftZ;
        final double upnZ = dirnX * leftY - dirnY * leftX;
        final double tr = leftX + upnY + dirnZ;
        double w;
        double x;
        double y;
        double z;
        if (tr >= 0.0) {
            double t = Math.sqrt(tr + 1.0);
            w = t * 0.5;
            t = 0.5 / t;
            x = (dirnY - upnZ) * t;
            y = (leftZ - dirnX) * t;
            z = (upnX - leftY) * t;
        }
        else if (leftX > upnY && leftX > dirnZ) {
            double t = Math.sqrt(1.0 + leftX - upnY - dirnZ);
            x = t * 0.5;
            t = 0.5 / t;
            y = (leftY + upnX) * t;
            z = (dirnX + leftZ) * t;
            w = (dirnY - upnZ) * t;
        }
        else if (upnY > dirnZ) {
            double t = Math.sqrt(1.0 + upnY - leftX - dirnZ);
            y = t * 0.5;
            t = 0.5 / t;
            x = (leftY + upnX) * t;
            z = (upnZ + dirnY) * t;
            w = (leftZ - dirnX) * t;
        }
        else {
            double t = Math.sqrt(1.0 + dirnZ - leftX - upnY);
            z = t * 0.5;
            t = 0.5 / t;
            x = (dirnX + leftZ) * t;
            y = (upnZ + dirnY) * t;
            w = (upnX - leftY) * t;
        }
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " " + Runtime.format(this.w, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeDouble(this.w);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.w = in.readDouble();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.w);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int)(temp ^ temp >>> 32);
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
        final Quaterniond other = (Quaterniond)obj;
        return Double.doubleToLongBits(this.w) == Double.doubleToLongBits(other.w) && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }
    
    public Quaterniond difference(final Quaterniondc other) {
        return this.difference(other, this);
    }
    
    public Quaterniond difference(final Quaterniondc other, final Quaterniond dest) {
        final double invNorm = 1.0 / this.lengthSquared();
        final double x = -this.x * invNorm;
        final double y = -this.y * invNorm;
        final double z = -this.z * invNorm;
        final double w = this.w * invNorm;
        dest.set(Math.fma(w, other.x(), Math.fma(x, other.w(), Math.fma(y, other.z(), -z * other.y()))), Math.fma(w, other.y(), Math.fma(-x, other.z(), Math.fma(y, other.w(), z * other.x()))), Math.fma(w, other.z(), Math.fma(x, other.y(), Math.fma(-y, other.x(), z * other.w()))), Math.fma(w, other.w(), Math.fma(-x, other.x(), Math.fma(-y, other.y(), -z * other.z()))));
        return dest;
    }
    
    public Quaterniond rotationTo(final double fromDirX, final double fromDirY, final double fromDirZ, final double toDirX, final double toDirY, final double toDirZ) {
        final double fn = Math.invsqrt(Math.fma(fromDirX, fromDirX, Math.fma(fromDirY, fromDirY, fromDirZ * fromDirZ)));
        final double tn = Math.invsqrt(Math.fma(toDirX, toDirX, Math.fma(toDirY, toDirY, toDirZ * toDirZ)));
        final double fx = fromDirX * fn;
        final double fy = fromDirY * fn;
        final double fz = fromDirZ * fn;
        final double tx = toDirX * tn;
        final double ty = toDirY * tn;
        final double tz = toDirZ * tn;
        final double dot = fx * tx + fy * ty + fz * tz;
        if (dot < -0.999999) {
            double x = fy;
            double y = -fx;
            double z = 0.0;
            double w = 0.0;
            if (x * x + y * y == 0.0) {
                x = 0.0;
                y = fz;
                z = -fy;
                w = 0.0;
            }
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = 0.0;
        }
        else {
            final double sd2 = Math.sqrt((1.0 + dot) * 2.0);
            final double isd2 = 1.0 / sd2;
            final double cx = fy * tz - fz * ty;
            final double cy = fz * tx - fx * tz;
            final double cz = fx * ty - fy * tx;
            final double x = cx * isd2;
            final double y = cy * isd2;
            final double z = cz * isd2;
            final double w = sd2 * 0.5;
            final double n2 = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            this.x = x * n2;
            this.y = y * n2;
            this.z = z * n2;
            this.w = w * n2;
        }
        return this;
    }
    
    public Quaterniond rotationTo(final Vector3dc fromDir, final Vector3dc toDir) {
        return this.rotationTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z());
    }
    
    public Quaterniond rotateTo(final double fromDirX, final double fromDirY, final double fromDirZ, final double toDirX, final double toDirY, final double toDirZ, final Quaterniond dest) {
        final double fn = Math.invsqrt(Math.fma(fromDirX, fromDirX, Math.fma(fromDirY, fromDirY, fromDirZ * fromDirZ)));
        final double tn = Math.invsqrt(Math.fma(toDirX, toDirX, Math.fma(toDirY, toDirY, toDirZ * toDirZ)));
        final double fx = fromDirX * fn;
        final double fy = fromDirY * fn;
        final double fz = fromDirZ * fn;
        final double tx = toDirX * tn;
        final double ty = toDirY * tn;
        final double tz = toDirZ * tn;
        final double dot = fx * tx + fy * ty + fz * tz;
        double x;
        double y;
        double z;
        double w;
        if (dot < -0.999999) {
            x = fy;
            y = -fx;
            z = 0.0;
            w = 0.0;
            if (x * x + y * y == 0.0) {
                x = 0.0;
                y = fz;
                z = -fy;
                w = 0.0;
            }
        }
        else {
            final double sd2 = Math.sqrt((1.0 + dot) * 2.0);
            final double isd2 = 1.0 / sd2;
            final double cx = fy * tz - fz * ty;
            final double cy = fz * tx - fx * tz;
            final double cz = fx * ty - fy * tx;
            x = cx * isd2;
            y = cy * isd2;
            z = cz * isd2;
            w = sd2 * 0.5;
            final double n2 = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
            x *= n2;
            y *= n2;
            z *= n2;
            w *= n2;
        }
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaterniond rotationAxis(final AxisAngle4f axisAngle) {
        return this.rotationAxis(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Quaterniond rotationAxis(final double angle, final double axisX, final double axisY, final double axisZ) {
        final double hangle = angle / 2.0;
        final double sinAngle = Math.sin(hangle);
        final double invVLength = Math.invsqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
        return this.set(axisX * invVLength * sinAngle, axisY * invVLength * sinAngle, axisZ * invVLength * sinAngle, Math.cosFromSin(sinAngle, hangle));
    }
    
    public Quaterniond rotationX(final double angle) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return this.set(sin, 0.0, cos, 0.0);
    }
    
    public Quaterniond rotationY(final double angle) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return this.set(0.0, sin, 0.0, cos);
    }
    
    public Quaterniond rotationZ(final double angle) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return this.set(0.0, 0.0, sin, cos);
    }
    
    public Quaterniond rotateTo(final double fromDirX, final double fromDirY, final double fromDirZ, final double toDirX, final double toDirY, final double toDirZ) {
        return this.rotateTo(fromDirX, fromDirY, fromDirZ, toDirX, toDirY, toDirZ, this);
    }
    
    public Quaterniond rotateTo(final Vector3dc fromDir, final Vector3dc toDir, final Quaterniond dest) {
        return this.rotateTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z(), dest);
    }
    
    public Quaterniond rotateTo(final Vector3dc fromDir, final Vector3dc toDir) {
        return this.rotateTo(fromDir.x(), fromDir.y(), fromDir.z(), toDir.x(), toDir.y(), toDir.z(), this);
    }
    
    public Quaterniond rotateX(final double angle) {
        return this.rotateX(angle, this);
    }
    
    public Quaterniond rotateX(final double angle, final Quaterniond dest) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return dest.set(this.w * sin + this.x * cos, this.y * cos + this.z * sin, this.z * cos - this.y * sin, this.w * cos - this.x * sin);
    }
    
    public Quaterniond rotateY(final double angle) {
        return this.rotateY(angle, this);
    }
    
    public Quaterniond rotateY(final double angle, final Quaterniond dest) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return dest.set(this.x * cos - this.z * sin, this.w * sin + this.y * cos, this.x * sin + this.z * cos, this.w * cos - this.y * sin);
    }
    
    public Quaterniond rotateZ(final double angle) {
        return this.rotateZ(angle, this);
    }
    
    public Quaterniond rotateZ(final double angle, final Quaterniond dest) {
        final double sin = Math.sin(angle * 0.5);
        final double cos = Math.cosFromSin(sin, angle * 0.5);
        return dest.set(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.w * sin + this.z * cos, this.w * cos - this.z * sin);
    }
    
    public Quaterniond rotateLocalX(final double angle) {
        return this.rotateLocalX(angle, this);
    }
    
    public Quaterniond rotateLocalX(final double angle, final Quaterniond dest) {
        final double hangle = angle * 0.5;
        final double s = Math.sin(hangle);
        final double c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x + s * this.w, c * this.y - s * this.z, c * this.z + s * this.y, c * this.w - s * this.x);
        return dest;
    }
    
    public Quaterniond rotateLocalY(final double angle) {
        return this.rotateLocalY(angle, this);
    }
    
    public Quaterniond rotateLocalY(final double angle, final Quaterniond dest) {
        final double hangle = angle * 0.5;
        final double s = Math.sin(hangle);
        final double c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x + s * this.z, c * this.y + s * this.w, c * this.z - s * this.x, c * this.w - s * this.y);
        return dest;
    }
    
    public Quaterniond rotateLocalZ(final double angle) {
        return this.rotateLocalZ(angle, this);
    }
    
    public Quaterniond rotateLocalZ(final double angle, final Quaterniond dest) {
        final double hangle = angle * 0.5;
        final double s = Math.sin(hangle);
        final double c = Math.cosFromSin(s, hangle);
        dest.set(c * this.x - s * this.y, c * this.y + s * this.x, c * this.z + s * this.w, c * this.w - s * this.z);
        return dest;
    }
    
    public Quaterniond rotateXYZ(final double angleX, final double angleY, final double angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Quaterniond rotateXYZ(final double angleX, final double angleY, final double angleZ, final Quaterniond dest) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double cycz = cy * cz;
        final double sysz = sy * sz;
        final double sycz = sy * cz;
        final double cysz = cy * sz;
        final double w = cx * cycz - sx * sysz;
        final double x = sx * cycz + cx * sysz;
        final double y = cx * sycz - sx * cysz;
        final double z = cx * cysz + sx * sycz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaterniond rotateZYX(final double angleZ, final double angleY, final double angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Quaterniond rotateZYX(final double angleZ, final double angleY, final double angleX, final Quaterniond dest) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double cycz = cy * cz;
        final double sysz = sy * sz;
        final double sycz = sy * cz;
        final double cysz = cy * sz;
        final double w = cx * cycz + sx * sysz;
        final double x = sx * cycz - cx * sysz;
        final double y = cx * sycz + sx * cysz;
        final double z = cx * cysz - sx * sycz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Quaterniond rotateYXZ(final double angleY, final double angleX, final double angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Quaterniond rotateYXZ(final double angleY, final double angleX, final double angleZ, final Quaterniond dest) {
        final double sx = Math.sin(angleX * 0.5);
        final double cx = Math.cosFromSin(sx, angleX * 0.5);
        final double sy = Math.sin(angleY * 0.5);
        final double cy = Math.cosFromSin(sy, angleY * 0.5);
        final double sz = Math.sin(angleZ * 0.5);
        final double cz = Math.cosFromSin(sz, angleZ * 0.5);
        final double yx = cy * sx;
        final double yy = sy * cx;
        final double yz = sy * sx;
        final double yw = cy * cx;
        final double x = yx * cz + yy * sz;
        final double y = yy * cz - yx * sz;
        final double z = yw * sz - yz * cz;
        final double w = yw * cz + yz * sz;
        return dest.set(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))), Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))), Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))), Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
    }
    
    public Vector3d getEulerAnglesXYZ(final Vector3d eulerAngles) {
        eulerAngles.x = Math.atan2(this.x * this.w - this.y * this.z, 0.5 - this.x * this.x - this.y * this.y);
        eulerAngles.y = Math.safeAsin(2.0 * (this.x * this.z + this.y * this.w));
        eulerAngles.z = Math.atan2(this.z * this.w - this.x * this.y, 0.5 - this.y * this.y - this.z * this.z);
        return eulerAngles;
    }
    
    public Vector3d getEulerAnglesZYX(final Vector3d eulerAngles) {
        eulerAngles.x = Math.atan2(this.y * this.z + this.w * this.x, 0.5 - this.x * this.x + this.y * this.y);
        eulerAngles.y = Math.safeAsin(-2.0 * (this.x * this.z - this.w * this.y));
        eulerAngles.z = Math.atan2(this.x * this.y + this.w * this.z, 0.5 - this.y * this.y - this.z * this.z);
        return eulerAngles;
    }
    
    public Vector3d getEulerAnglesZXY(final Vector3d eulerAngles) {
        eulerAngles.x = Math.safeAsin(2.0 * (this.w * this.x + this.y * this.z));
        eulerAngles.y = Math.atan2(this.w * this.y - this.x * this.z, 0.5 - this.y * this.y - this.x * this.x);
        eulerAngles.z = Math.atan2(this.w * this.z - this.x * this.y, 0.5 - this.z * this.z - this.x * this.x);
        return eulerAngles;
    }
    
    public Vector3d getEulerAnglesYXZ(final Vector3d eulerAngles) {
        eulerAngles.x = Math.safeAsin(-2.0 * (this.y * this.z - this.w * this.x));
        eulerAngles.y = Math.atan2(this.x * this.z + this.y * this.w, 0.5 - this.y * this.y - this.x * this.x);
        eulerAngles.z = Math.atan2(this.y * this.x + this.w * this.z, 0.5 - this.x * this.x - this.z * this.z);
        return eulerAngles;
    }
    
    public Quaterniond rotateAxis(final double angle, final double axisX, final double axisY, final double axisZ, final Quaterniond dest) {
        final double hangle = angle / 2.0;
        final double sinAngle = Math.sin(hangle);
        final double invVLength = Math.invsqrt(Math.fma(axisX, axisX, Math.fma(axisY, axisY, axisZ * axisZ)));
        final double rx = axisX * invVLength * sinAngle;
        final double ry = axisY * invVLength * sinAngle;
        final double rz = axisZ * invVLength * sinAngle;
        final double rw = Math.cosFromSin(sinAngle, hangle);
        return dest.set(Math.fma(this.w, rx, Math.fma(this.x, rw, Math.fma(this.y, rz, -this.z * ry))), Math.fma(this.w, ry, Math.fma(-this.x, rz, Math.fma(this.y, rw, this.z * rx))), Math.fma(this.w, rz, Math.fma(this.x, ry, Math.fma(-this.y, rx, this.z * rw))), Math.fma(this.w, rw, Math.fma(-this.x, rx, Math.fma(-this.y, ry, -this.z * rz))));
    }
    
    public Quaterniond rotateAxis(final double angle, final Vector3dc axis, final Quaterniond dest) {
        return this.rotateAxis(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Quaterniond rotateAxis(final double angle, final Vector3dc axis) {
        return this.rotateAxis(angle, axis.x(), axis.y(), axis.z(), this);
    }
    
    public Quaterniond rotateAxis(final double angle, final double axisX, final double axisY, final double axisZ) {
        return this.rotateAxis(angle, axisX, axisY, axisZ, this);
    }
    
    public Vector3d positiveX(final Vector3d dir) {
        final double invNorm = 1.0 / this.lengthSquared();
        final double nx = -this.x * invNorm;
        final double ny = -this.y * invNorm;
        final double nz = -this.z * invNorm;
        final double nw = this.w * invNorm;
        final double dy = ny + ny;
        final double dz = nz + nz;
        dir.x = -ny * dy - nz * dz + 1.0;
        dir.y = nx * dy + nw * dz;
        dir.z = nx * dz - nw * dy;
        return dir;
    }
    
    public Vector3d normalizedPositiveX(final Vector3d dir) {
        final double dy = this.y + this.y;
        final double dz = this.z + this.z;
        dir.x = -this.y * dy - this.z * dz + 1.0;
        dir.y = this.x * dy - this.w * dz;
        dir.z = this.x * dz + this.w * dy;
        return dir;
    }
    
    public Vector3d positiveY(final Vector3d dir) {
        final double invNorm = 1.0 / this.lengthSquared();
        final double nx = -this.x * invNorm;
        final double ny = -this.y * invNorm;
        final double nz = -this.z * invNorm;
        final double nw = this.w * invNorm;
        final double dx = nx + nx;
        final double dy = ny + ny;
        final double dz = nz + nz;
        dir.x = nx * dy - nw * dz;
        dir.y = -nx * dx - nz * dz + 1.0;
        dir.z = ny * dz + nw * dx;
        return dir;
    }
    
    public Vector3d normalizedPositiveY(final Vector3d dir) {
        final double dx = this.x + this.x;
        final double dy = this.y + this.y;
        final double dz = this.z + this.z;
        dir.x = this.x * dy + this.w * dz;
        dir.y = -this.x * dx - this.z * dz + 1.0;
        dir.z = this.y * dz - this.w * dx;
        return dir;
    }
    
    public Vector3d positiveZ(final Vector3d dir) {
        final double invNorm = 1.0 / this.lengthSquared();
        final double nx = -this.x * invNorm;
        final double ny = -this.y * invNorm;
        final double nz = -this.z * invNorm;
        final double nw = this.w * invNorm;
        final double dx = nx + nx;
        final double dy = ny + ny;
        final double dz = nz + nz;
        dir.x = nx * dz + nw * dy;
        dir.y = ny * dz - nw * dx;
        dir.z = -nx * dx - ny * dy + 1.0;
        return dir;
    }
    
    public Vector3d normalizedPositiveZ(final Vector3d dir) {
        final double dx = this.x + this.x;
        final double dy = this.y + this.y;
        final double dz = this.z + this.z;
        dir.x = this.x * dz - this.w * dy;
        dir.y = this.y * dz + this.w * dx;
        dir.z = -this.x * dx - this.y * dy + 1.0;
        return dir;
    }
    
    public Quaterniond conjugateBy(final Quaterniondc q) {
        return this.conjugateBy(q, this);
    }
    
    public Quaterniond conjugateBy(final Quaterniondc q, final Quaterniond dest) {
        final double invNorm = 1.0 / q.lengthSquared();
        final double qix = -q.x() * invNorm;
        final double qiy = -q.y() * invNorm;
        final double qiz = -q.z() * invNorm;
        final double qiw = q.w() * invNorm;
        final double qpx = Math.fma(q.w(), this.x, Math.fma(q.x(), this.w, Math.fma(q.y(), this.z, -q.z() * this.y)));
        final double qpy = Math.fma(q.w(), this.y, Math.fma(-q.x(), this.z, Math.fma(q.y(), this.w, q.z() * this.x)));
        final double qpz = Math.fma(q.w(), this.z, Math.fma(q.x(), this.y, Math.fma(-q.y(), this.x, q.z() * this.w)));
        final double qpw = Math.fma(q.w(), this.w, Math.fma(-q.x(), this.x, Math.fma(-q.y(), this.y, -q.z() * this.z)));
        return dest.set(Math.fma(qpw, qix, Math.fma(qpx, qiw, Math.fma(qpy, qiz, -qpz * qiy))), Math.fma(qpw, qiy, Math.fma(-qpx, qiz, Math.fma(qpy, qiw, qpz * qix))), Math.fma(qpw, qiz, Math.fma(qpx, qiy, Math.fma(-qpy, qix, qpz * qiw))), Math.fma(qpw, qiw, Math.fma(-qpx, qix, Math.fma(-qpy, qiy, -qpz * qiz))));
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
    }
    
    public boolean equals(final Quaterniondc q, final double delta) {
        return this == q || (q != null && q instanceof Quaterniondc && Runtime.equals(this.x, q.x(), delta) && Runtime.equals(this.y, q.y(), delta) && Runtime.equals(this.z, q.z(), delta) && Runtime.equals(this.w, q.w(), delta));
    }
    
    public boolean equals(final double x, final double y, final double z, final double w) {
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(z) && Double.doubleToLongBits(this.w) == Double.doubleToLongBits(w);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
