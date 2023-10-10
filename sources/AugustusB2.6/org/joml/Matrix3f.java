// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.nio.FloatBuffer;
import java.io.Externalizable;

public class Matrix3f implements Externalizable, Cloneable, Matrix3fc
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;
    
    public Matrix3f() {
        this.m00 = 1.0f;
        this.m11 = 1.0f;
        this.m22 = 1.0f;
    }
    
    public Matrix3f(final Matrix2fc mat) {
        this.set(mat);
    }
    
    public Matrix3f(final Matrix3fc mat) {
        this.set(mat);
    }
    
    public Matrix3f(final Matrix4fc mat) {
        this.set(mat);
    }
    
    public Matrix3f(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }
    
    public Matrix3f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Matrix3f(final Vector3fc col0, final Vector3fc col1, final Vector3fc col2) {
        this.set(col0, col1, col2);
    }
    
    public float m00() {
        return this.m00;
    }
    
    public float m01() {
        return this.m01;
    }
    
    public float m02() {
        return this.m02;
    }
    
    public float m10() {
        return this.m10;
    }
    
    public float m11() {
        return this.m11;
    }
    
    public float m12() {
        return this.m12;
    }
    
    public float m20() {
        return this.m20;
    }
    
    public float m21() {
        return this.m21;
    }
    
    public float m22() {
        return this.m22;
    }
    
    public Matrix3f m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    public Matrix3f m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    public Matrix3f m02(final float m02) {
        this.m02 = m02;
        return this;
    }
    
    public Matrix3f m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    public Matrix3f m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    public Matrix3f m12(final float m12) {
        this.m12 = m12;
        return this;
    }
    
    public Matrix3f m20(final float m20) {
        this.m20 = m20;
        return this;
    }
    
    public Matrix3f m21(final float m21) {
        this.m21 = m21;
        return this;
    }
    
    public Matrix3f m22(final float m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix3f _m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix3f _m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix3f _m02(final float m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix3f _m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix3f _m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix3f _m12(final float m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix3f _m20(final float m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix3f _m21(final float m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix3f _m22(final float m22) {
        this.m22 = m22;
        return this;
    }
    
    public Matrix3f set(final Matrix3fc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m10(m.m10())._m11(m.m11())._m12(m.m12())._m20(m.m20())._m21(m.m21())._m22(m.m22());
    }
    
    public Matrix3f setTransposed(final Matrix3fc m) {
        final float nm10 = m.m01();
        final float nm11 = m.m21();
        final float nm12 = m.m02();
        final float nm13 = m.m12();
        return this._m00(m.m00())._m01(m.m10())._m02(m.m20())._m10(nm10)._m11(m.m11())._m12(nm11)._m20(nm12)._m21(nm13)._m22(m.m22());
    }
    
    public Matrix3f set(final Matrix4x3fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m02 = m.m02();
        this.m10 = m.m10();
        this.m11 = m.m11();
        this.m12 = m.m12();
        this.m20 = m.m20();
        this.m21 = m.m21();
        this.m22 = m.m22();
        return this;
    }
    
    public Matrix3f set(final Matrix4fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        return this;
    }
    
    public Matrix3f set(final Matrix2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = 0.0f;
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        return this;
    }
    
    public Matrix3f set(final AxisAngle4f axisAngle) {
        float x = axisAngle.x;
        float y = axisAngle.y;
        float z = axisAngle.z;
        final float angle = axisAngle.angle;
        final float invLength = Math.invsqrt(x * x + y * y + z * z);
        x *= invLength;
        y *= invLength;
        z *= invLength;
        final float s = Math.sin(angle);
        final float c = Math.cosFromSin(s, angle);
        final float omc = 1.0f - c;
        this.m00 = c + x * x * omc;
        this.m11 = c + y * y * omc;
        this.m22 = c + z * z * omc;
        float tmp1 = x * y * omc;
        float tmp2 = z * s;
        this.m10 = tmp1 - tmp2;
        this.m01 = tmp1 + tmp2;
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this.m20 = tmp1 + tmp2;
        this.m02 = tmp1 - tmp2;
        tmp1 = y * z * omc;
        tmp2 = x * s;
        this.m21 = tmp1 - tmp2;
        this.m12 = tmp1 + tmp2;
        return this;
    }
    
    public Matrix3f set(final AxisAngle4d axisAngle) {
        double x = axisAngle.x;
        double y = axisAngle.y;
        double z = axisAngle.z;
        final double angle = axisAngle.angle;
        final double invLength = Math.invsqrt(x * x + y * y + z * z);
        x *= invLength;
        y *= invLength;
        z *= invLength;
        final double s = Math.sin(angle);
        final double c = Math.cosFromSin(s, angle);
        final double omc = 1.0 - c;
        this.m00 = (float)(c + x * x * omc);
        this.m11 = (float)(c + y * y * omc);
        this.m22 = (float)(c + z * z * omc);
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
        this.m10 = (float)(tmp1 - tmp2);
        this.m01 = (float)(tmp1 + tmp2);
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this.m20 = (float)(tmp1 + tmp2);
        this.m02 = (float)(tmp1 - tmp2);
        tmp1 = y * z * omc;
        tmp2 = x * s;
        this.m21 = (float)(tmp1 - tmp2);
        this.m12 = (float)(tmp1 + tmp2);
        return this;
    }
    
    public Matrix3f set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix3f set(final Quaterniondc q) {
        final double w2 = q.w() * q.w();
        final double x2 = q.x() * q.x();
        final double y2 = q.y() * q.y();
        final double z2 = q.z() * q.z();
        final double zw = q.z() * q.w();
        final double xy = q.x() * q.y();
        final double xz = q.x() * q.z();
        final double yw = q.y() * q.w();
        final double yz = q.y() * q.z();
        final double xw = q.x() * q.w();
        this.m00 = (float)(w2 + x2 - z2 - y2);
        this.m01 = (float)(xy + zw + zw + xy);
        this.m02 = (float)(xz - yw + xz - yw);
        this.m10 = (float)(-zw + xy - zw + xy);
        this.m11 = (float)(y2 - z2 + w2 - x2);
        this.m12 = (float)(yz + yz + xw + xw);
        this.m20 = (float)(yw + xz + xz + yw);
        this.m21 = (float)(yz + yz - xw - xw);
        this.m22 = (float)(z2 - y2 - x2 + w2);
        return this;
    }
    
    public Matrix3f mul(final Matrix3fc right) {
        return this.mul(right, this);
    }
    
    public Matrix3f mul(final Matrix3fc right, final Matrix3f dest) {
        final float nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final float nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final float nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final float nm4 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final float nm5 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final float nm6 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final float nm7 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final float nm8 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final float nm9 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f mulLocal(final Matrix3fc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix3f mulLocal(final Matrix3fc left, final Matrix3f dest) {
        final float nm00 = left.m00() * this.m00 + left.m10() * this.m01 + left.m20() * this.m02;
        final float nm2 = left.m01() * this.m00 + left.m11() * this.m01 + left.m21() * this.m02;
        final float nm3 = left.m02() * this.m00 + left.m12() * this.m01 + left.m22() * this.m02;
        final float nm4 = left.m00() * this.m10 + left.m10() * this.m11 + left.m20() * this.m12;
        final float nm5 = left.m01() * this.m10 + left.m11() * this.m11 + left.m21() * this.m12;
        final float nm6 = left.m02() * this.m10 + left.m12() * this.m11 + left.m22() * this.m12;
        final float nm7 = left.m00() * this.m20 + left.m10() * this.m21 + left.m20() * this.m22;
        final float nm8 = left.m01() * this.m20 + left.m11() * this.m21 + left.m21() * this.m22;
        final float nm9 = left.m02() * this.m20 + left.m12() * this.m21 + left.m22() * this.m22;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f set(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        return this;
    }
    
    public Matrix3f set(final float[] m) {
        MemUtil.INSTANCE.copy(m, 0, this);
        return this;
    }
    
    public Matrix3f set(final Vector3fc col0, final Vector3fc col1, final Vector3fc col2) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m02 = col0.z();
        this.m10 = col1.x();
        this.m11 = col1.y();
        this.m12 = col1.z();
        this.m20 = col2.x();
        this.m21 = col2.y();
        this.m22 = col2.z();
        return this;
    }
    
    public float determinant() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public Matrix3f invert() {
        return this.invert(this);
    }
    
    public Matrix3f invert(final Matrix3f dest) {
        final float a = Math.fma(this.m00, this.m11, -this.m01 * this.m10);
        final float b = Math.fma(this.m02, this.m10, -this.m00 * this.m12);
        final float c = Math.fma(this.m01, this.m12, -this.m02 * this.m11);
        final float d = Math.fma(a, this.m22, Math.fma(b, this.m21, c * this.m20));
        final float s = 1.0f / d;
        final float nm00 = Math.fma(this.m11, this.m22, -this.m21 * this.m12) * s;
        final float nm2 = Math.fma(this.m21, this.m02, -this.m01 * this.m22) * s;
        final float nm3 = c * s;
        final float nm4 = Math.fma(this.m20, this.m12, -this.m10 * this.m22) * s;
        final float nm5 = Math.fma(this.m00, this.m22, -this.m20 * this.m02) * s;
        final float nm6 = b * s;
        final float nm7 = Math.fma(this.m10, this.m21, -this.m20 * this.m11) * s;
        final float nm8 = Math.fma(this.m20, this.m01, -this.m00 * this.m21) * s;
        final float nm9 = a * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f transpose() {
        return this.transpose(this);
    }
    
    public Matrix3f transpose(final Matrix3f dest) {
        return dest.set(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
    }
    
    public String toString() {
        final String str = this.toString(Options.NUMBER_FORMAT);
        final StringBuffer res = new StringBuffer();
        int eIndex = Integer.MIN_VALUE;
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            if (c == 'E') {
                eIndex = i;
            }
            else {
                if (c == ' ' && eIndex == i - 1) {
                    res.append('+');
                    continue;
                }
                if (Character.isDigit(c) && eIndex == i - 1) {
                    res.append('+');
                }
            }
            res.append(c);
        }
        return res.toString();
    }
    
    public String toString(final NumberFormat formatter) {
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + " " + Runtime.format(this.m20, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + " " + Runtime.format(this.m21, formatter) + "\n" + Runtime.format(this.m02, formatter) + " " + Runtime.format(this.m12, formatter) + " " + Runtime.format(this.m22, formatter) + "\n";
    }
    
    public Matrix3f get(final Matrix3f dest) {
        return dest.set(this);
    }
    
    public Matrix4f get(final Matrix4f dest) {
        return dest.set(this);
    }
    
    public AxisAngle4f getRotation(final AxisAngle4f dest) {
        return dest.set(this);
    }
    
    public Quaternionf getUnnormalizedRotation(final Quaternionf dest) {
        return dest.setFromUnnormalized(this);
    }
    
    public Quaternionf getNormalizedRotation(final Quaternionf dest) {
        return dest.setFromNormalized(this);
    }
    
    public Quaterniond getUnnormalizedRotation(final Quaterniond dest) {
        return dest.setFromUnnormalized(this);
    }
    
    public Quaterniond getNormalizedRotation(final Quaterniond dest) {
        return dest.setFromNormalized(this);
    }
    
    public FloatBuffer get(final FloatBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get3x4(final FloatBuffer buffer) {
        return this.get3x4(buffer.position(), buffer);
    }
    
    public FloatBuffer get3x4(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get3x4(final ByteBuffer buffer) {
        return this.get3x4(buffer.position(), buffer);
    }
    
    public ByteBuffer get3x4(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer getTransposed(final FloatBuffer buffer) {
        return this.getTransposed(buffer.position(), buffer);
    }
    
    public FloatBuffer getTransposed(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getTransposed(final ByteBuffer buffer) {
        return this.getTransposed(buffer.position(), buffer);
    }
    
    public ByteBuffer getTransposed(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public Matrix3fc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public float[] get(final float[] arr, final int offset) {
        MemUtil.INSTANCE.copy(this, arr, offset);
        return arr;
    }
    
    public float[] get(final float[] arr) {
        return this.get(arr, 0);
    }
    
    public Matrix3f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix3f zero() {
        MemUtil.INSTANCE.zero(this);
        return this;
    }
    
    public Matrix3f identity() {
        MemUtil.INSTANCE.identity(this);
        return this;
    }
    
    public Matrix3f scale(final Vector3fc xyz, final Matrix3f dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix3f scale(final Vector3fc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix3f scale(final float x, final float y, final float z, final Matrix3f dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m02 = this.m02 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        dest.m12 = this.m12 * y;
        dest.m20 = this.m20 * z;
        dest.m21 = this.m21 * z;
        dest.m22 = this.m22 * z;
        return dest;
    }
    
    public Matrix3f scale(final float x, final float y, final float z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix3f scale(final float xyz, final Matrix3f dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix3f scale(final float xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix3f scaleLocal(final float x, final float y, final float z, final Matrix3f dest) {
        final float nm00 = x * this.m00;
        final float nm2 = y * this.m01;
        final float nm3 = z * this.m02;
        final float nm4 = x * this.m10;
        final float nm5 = y * this.m11;
        final float nm6 = z * this.m12;
        final float nm7 = x * this.m20;
        final float nm8 = y * this.m21;
        final float nm9 = z * this.m22;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f scaleLocal(final float x, final float y, final float z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix3f scaling(final float factor) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = factor;
        this.m11 = factor;
        this.m22 = factor;
        return this;
    }
    
    public Matrix3f scaling(final float x, final float y, final float z) {
        MemUtil.INSTANCE.zero(this);
        this.m00 = x;
        this.m11 = y;
        this.m22 = z;
        return this;
    }
    
    public Matrix3f scaling(final Vector3fc xyz) {
        return this.scaling(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Matrix3f rotation(final float angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3f rotation(final AxisAngle4f axisAngle) {
        return this.rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3f rotation(final float angle, final float x, final float y, final float z) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float C = 1.0f - cos;
        final float xy = x * y;
        final float xz = x * z;
        final float yz = y * z;
        this.m00 = cos + x * x * C;
        this.m10 = xy * C - z * sin;
        this.m20 = xz * C + y * sin;
        this.m01 = xy * C + z * sin;
        this.m11 = cos + y * y * C;
        this.m21 = yz * C - x * sin;
        this.m02 = xz * C - y * sin;
        this.m12 = yz * C + x * sin;
        this.m22 = cos + z * z * C;
        return this;
    }
    
    public Matrix3f rotationX(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = cos;
        this.m12 = sin;
        this.m20 = 0.0f;
        this.m21 = -sin;
        this.m22 = cos;
        return this;
    }
    
    public Matrix3f rotationY(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        this.m00 = cos;
        this.m01 = 0.0f;
        this.m02 = -sin;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = sin;
        this.m21 = 0.0f;
        this.m22 = cos;
        return this;
    }
    
    public Matrix3f rotationZ(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        this.m00 = cos;
        this.m01 = sin;
        this.m02 = 0.0f;
        this.m10 = -sin;
        this.m11 = cos;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        return this;
    }
    
    public Matrix3f rotationXYZ(final float angleX, final float angleY, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinX = -sinX;
        final float m_sinY = -sinY;
        final float m_sinZ = -sinZ;
        final float nm11 = cosX;
        final float nm12 = sinX;
        final float nm13 = m_sinX;
        final float nm14 = cosX;
        final float nm15 = cosY;
        final float nm16 = nm13 * m_sinY;
        final float nm17 = nm14 * m_sinY;
        this.m20 = sinY;
        this.m21 = nm13 * cosY;
        this.m22 = nm14 * cosY;
        this.m00 = nm15 * cosZ;
        this.m01 = nm16 * cosZ + nm11 * sinZ;
        this.m02 = nm17 * cosZ + nm12 * sinZ;
        this.m10 = nm15 * m_sinZ;
        this.m11 = nm16 * m_sinZ + nm11 * cosZ;
        this.m12 = nm17 * m_sinZ + nm12 * cosZ;
        return this;
    }
    
    public Matrix3f rotationZYX(final float angleZ, final float angleY, final float angleX) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinZ = -sinZ;
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float nm00 = cosZ;
        final float nm2 = sinZ;
        final float nm3 = m_sinZ;
        final float nm4 = cosZ;
        final float nm5 = nm00 * sinY;
        final float nm6 = nm2 * sinY;
        final float nm7 = cosY;
        this.m00 = nm00 * cosY;
        this.m01 = nm2 * cosY;
        this.m02 = m_sinY;
        this.m10 = nm3 * cosX + nm5 * sinX;
        this.m11 = nm4 * cosX + nm6 * sinX;
        this.m12 = nm7 * sinX;
        this.m20 = nm3 * m_sinX + nm5 * cosX;
        this.m21 = nm4 * m_sinX + nm6 * cosX;
        this.m22 = nm7 * cosX;
        return this;
    }
    
    public Matrix3f rotationYXZ(final float angleY, final float angleX, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float m_sinZ = -sinZ;
        final float nm00 = cosY;
        final float nm2 = m_sinY;
        final float nm3 = sinY;
        final float nm4 = cosY;
        final float nm5 = nm3 * sinX;
        final float nm6 = cosX;
        final float nm7 = nm4 * sinX;
        this.m20 = nm3 * cosX;
        this.m21 = m_sinX;
        this.m22 = nm4 * cosX;
        this.m00 = nm00 * cosZ + nm5 * sinZ;
        this.m01 = nm6 * sinZ;
        this.m02 = nm2 * cosZ + nm7 * sinZ;
        this.m10 = nm00 * m_sinZ + nm5 * cosZ;
        this.m11 = nm6 * cosZ;
        this.m12 = nm2 * m_sinZ + nm7 * cosZ;
        return this;
    }
    
    public Matrix3f rotation(final Quaternionfc quat) {
        final float w2 = quat.w() * quat.w();
        final float x2 = quat.x() * quat.x();
        final float y2 = quat.y() * quat.y();
        final float z2 = quat.z() * quat.z();
        final float zw = quat.z() * quat.w();
        final float dzw = zw + zw;
        final float xy = quat.x() * quat.y();
        final float dxy = xy + xy;
        final float xz = quat.x() * quat.z();
        final float dxz = xz + xz;
        final float yw = quat.y() * quat.w();
        final float dyw = yw + yw;
        final float yz = quat.y() * quat.z();
        final float dyz = yz + yz;
        final float xw = quat.x() * quat.w();
        final float dxw = xw + xw;
        this.m00 = w2 + x2 - z2 - y2;
        this.m01 = dxy + dzw;
        this.m02 = dxz - dyw;
        this.m10 = -dzw + dxy;
        this.m11 = y2 - z2 + w2 - x2;
        this.m12 = dyz + dxw;
        this.m20 = dyw + dxz;
        this.m21 = dyz - dxw;
        this.m22 = z2 - y2 - x2 + w2;
        return this;
    }
    
    public Vector3f transform(final Vector3f v) {
        return v.mul(this);
    }
    
    public Vector3f transform(final Vector3fc v, final Vector3f dest) {
        return v.mul(this, dest);
    }
    
    public Vector3f transform(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(Math.fma(this.m00, x, Math.fma(this.m10, y, this.m20 * z)), Math.fma(this.m01, x, Math.fma(this.m11, y, this.m21 * z)), Math.fma(this.m02, x, Math.fma(this.m12, y, this.m22 * z)));
    }
    
    public Vector3f transformTranspose(final Vector3f v) {
        return v.mulTranspose(this);
    }
    
    public Vector3f transformTranspose(final Vector3fc v, final Vector3f dest) {
        return v.mulTranspose(this, dest);
    }
    
    public Vector3f transformTranspose(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(Math.fma(this.m00, x, Math.fma(this.m01, y, this.m02 * z)), Math.fma(this.m10, x, Math.fma(this.m11, y, this.m12 * z)), Math.fma(this.m20, x, Math.fma(this.m21, y, this.m22 * z)));
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.m00);
        out.writeFloat(this.m01);
        out.writeFloat(this.m02);
        out.writeFloat(this.m10);
        out.writeFloat(this.m11);
        out.writeFloat(this.m12);
        out.writeFloat(this.m20);
        out.writeFloat(this.m21);
        out.writeFloat(this.m22);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readFloat();
        this.m01 = in.readFloat();
        this.m02 = in.readFloat();
        this.m10 = in.readFloat();
        this.m11 = in.readFloat();
        this.m12 = in.readFloat();
        this.m20 = in.readFloat();
        this.m21 = in.readFloat();
        this.m22 = in.readFloat();
    }
    
    public Matrix3f rotateX(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float rm11;
        final float cos = rm11 = Math.cosFromSin(sin, ang);
        final float rm12 = -sin;
        final float rm13 = sin;
        final float rm14 = cos;
        final float nm10 = this.m10 * rm11 + this.m20 * rm13;
        final float nm11 = this.m11 * rm11 + this.m21 * rm13;
        final float nm12 = this.m12 * rm11 + this.m22 * rm13;
        dest.m20 = this.m10 * rm12 + this.m20 * rm14;
        dest.m21 = this.m11 * rm12 + this.m21 * rm14;
        dest.m22 = this.m12 * rm12 + this.m22 * rm14;
        dest.m10 = nm10;
        dest.m11 = nm11;
        dest.m12 = nm12;
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        return dest;
    }
    
    public Matrix3f rotateX(final float ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix3f rotateY(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float rm00;
        final float cos = rm00 = Math.cosFromSin(sin, ang);
        final float rm2 = sin;
        final float rm3 = -sin;
        final float rm4 = cos;
        final float nm00 = this.m00 * rm00 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m22 * rm3;
        dest.m20 = this.m00 * rm2 + this.m20 * rm4;
        dest.m21 = this.m01 * rm2 + this.m21 * rm4;
        dest.m22 = this.m02 * rm2 + this.m22 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        return dest;
    }
    
    public Matrix3f rotateY(final float ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix3f rotateZ(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float rm00;
        final float cos = rm00 = Math.cosFromSin(sin, ang);
        final float rm2 = -sin;
        final float rm3 = sin;
        final float rm4 = cos;
        final float nm00 = this.m00 * rm00 + this.m10 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm3;
        dest.m10 = this.m00 * rm2 + this.m10 * rm4;
        dest.m11 = this.m01 * rm2 + this.m11 * rm4;
        dest.m12 = this.m02 * rm2 + this.m12 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        return dest;
    }
    
    public Matrix3f rotateZ(final float ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix3f rotateXYZ(final Vector3f angles) {
        return this.rotateXYZ(angles.x, angles.y, angles.z);
    }
    
    public Matrix3f rotateXYZ(final float angleX, final float angleY, final float angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix3f rotateXYZ(final float angleX, final float angleY, final float angleZ, final Matrix3f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinX = -sinX;
        final float m_sinY = -sinY;
        final float m_sinZ = -sinZ;
        final float nm10 = this.m10 * cosX + this.m20 * sinX;
        final float nm11 = this.m11 * cosX + this.m21 * sinX;
        final float nm12 = this.m12 * cosX + this.m22 * sinX;
        final float nm13 = this.m10 * m_sinX + this.m20 * cosX;
        final float nm14 = this.m11 * m_sinX + this.m21 * cosX;
        final float nm15 = this.m12 * m_sinX + this.m22 * cosX;
        final float nm16 = this.m00 * cosY + nm13 * m_sinY;
        final float nm17 = this.m01 * cosY + nm14 * m_sinY;
        final float nm18 = this.m02 * cosY + nm15 * m_sinY;
        dest.m20 = this.m00 * sinY + nm13 * cosY;
        dest.m21 = this.m01 * sinY + nm14 * cosY;
        dest.m22 = this.m02 * sinY + nm15 * cosY;
        dest.m00 = nm16 * cosZ + nm10 * sinZ;
        dest.m01 = nm17 * cosZ + nm11 * sinZ;
        dest.m02 = nm18 * cosZ + nm12 * sinZ;
        dest.m10 = nm16 * m_sinZ + nm10 * cosZ;
        dest.m11 = nm17 * m_sinZ + nm11 * cosZ;
        dest.m12 = nm18 * m_sinZ + nm12 * cosZ;
        return dest;
    }
    
    public Matrix3f rotateZYX(final Vector3f angles) {
        return this.rotateZYX(angles.z, angles.y, angles.x);
    }
    
    public Matrix3f rotateZYX(final float angleZ, final float angleY, final float angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix3f rotateZYX(final float angleZ, final float angleY, final float angleX, final Matrix3f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinZ = -sinZ;
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float nm00 = this.m00 * cosZ + this.m10 * sinZ;
        final float nm2 = this.m01 * cosZ + this.m11 * sinZ;
        final float nm3 = this.m02 * cosZ + this.m12 * sinZ;
        final float nm4 = this.m00 * m_sinZ + this.m10 * cosZ;
        final float nm5 = this.m01 * m_sinZ + this.m11 * cosZ;
        final float nm6 = this.m02 * m_sinZ + this.m12 * cosZ;
        final float nm7 = nm00 * sinY + this.m20 * cosY;
        final float nm8 = nm2 * sinY + this.m21 * cosY;
        final float nm9 = nm3 * sinY + this.m22 * cosY;
        dest.m00 = nm00 * cosY + this.m20 * m_sinY;
        dest.m01 = nm2 * cosY + this.m21 * m_sinY;
        dest.m02 = nm3 * cosY + this.m22 * m_sinY;
        dest.m10 = nm4 * cosX + nm7 * sinX;
        dest.m11 = nm5 * cosX + nm8 * sinX;
        dest.m12 = nm6 * cosX + nm9 * sinX;
        dest.m20 = nm4 * m_sinX + nm7 * cosX;
        dest.m21 = nm5 * m_sinX + nm8 * cosX;
        dest.m22 = nm6 * m_sinX + nm9 * cosX;
        return dest;
    }
    
    public Matrix3f rotateYXZ(final Vector3f angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix3f rotateYXZ(final float angleY, final float angleX, final float angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix3f rotateYXZ(final float angleY, final float angleX, final float angleZ, final Matrix3f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float m_sinZ = -sinZ;
        final float nm20 = this.m00 * sinY + this.m20 * cosY;
        final float nm21 = this.m01 * sinY + this.m21 * cosY;
        final float nm22 = this.m02 * sinY + this.m22 * cosY;
        final float nm23 = this.m00 * cosY + this.m20 * m_sinY;
        final float nm24 = this.m01 * cosY + this.m21 * m_sinY;
        final float nm25 = this.m02 * cosY + this.m22 * m_sinY;
        final float nm26 = this.m10 * cosX + nm20 * sinX;
        final float nm27 = this.m11 * cosX + nm21 * sinX;
        final float nm28 = this.m12 * cosX + nm22 * sinX;
        dest.m20 = this.m10 * m_sinX + nm20 * cosX;
        dest.m21 = this.m11 * m_sinX + nm21 * cosX;
        dest.m22 = this.m12 * m_sinX + nm22 * cosX;
        dest.m00 = nm23 * cosZ + nm26 * sinZ;
        dest.m01 = nm24 * cosZ + nm27 * sinZ;
        dest.m02 = nm25 * cosZ + nm28 * sinZ;
        dest.m10 = nm23 * m_sinZ + nm26 * cosZ;
        dest.m11 = nm24 * m_sinZ + nm27 * cosZ;
        dest.m12 = nm25 * m_sinZ + nm28 * cosZ;
        return dest;
    }
    
    public Matrix3f rotate(final float ang, final float x, final float y, final float z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix3f rotate(final float ang, final float x, final float y, final float z, final Matrix3f dest) {
        final float s = Math.sin(ang);
        final float c = Math.cosFromSin(s, ang);
        final float C = 1.0f - c;
        final float xx = x * x;
        final float xy = x * y;
        final float xz = x * z;
        final float yy = y * y;
        final float yz = y * z;
        final float zz = z * z;
        final float rm00 = xx * C + c;
        final float rm2 = xy * C + z * s;
        final float rm3 = xz * C - y * s;
        final float rm4 = xy * C - z * s;
        final float rm5 = yy * C + c;
        final float rm6 = yz * C + x * s;
        final float rm7 = xz * C + y * s;
        final float rm8 = yz * C - x * s;
        final float rm9 = zz * C + c;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest.m20 = this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9;
        dest.m21 = this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9;
        dest.m22 = this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        return dest;
    }
    
    public Matrix3f rotateLocal(final float ang, final float x, final float y, final float z, final Matrix3f dest) {
        final float s = Math.sin(ang);
        final float c = Math.cosFromSin(s, ang);
        final float C = 1.0f - c;
        final float xx = x * x;
        final float xy = x * y;
        final float xz = x * z;
        final float yy = y * y;
        final float yz = y * z;
        final float zz = z * z;
        final float lm00 = xx * C + c;
        final float lm2 = xy * C + z * s;
        final float lm3 = xz * C - y * s;
        final float lm4 = xy * C - z * s;
        final float lm5 = yy * C + c;
        final float lm6 = yz * C + x * s;
        final float lm7 = xz * C + y * s;
        final float lm8 = yz * C - x * s;
        final float lm9 = zz * C + c;
        final float nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final float nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final float nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final float nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final float nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final float nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final float nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final float nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final float nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f rotateLocal(final float ang, final float x, final float y, final float z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix3f rotateLocalX(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm01 = cos * this.m01 - sin * this.m02;
        final float nm2 = sin * this.m01 + cos * this.m02;
        final float nm3 = cos * this.m11 - sin * this.m12;
        final float nm4 = sin * this.m11 + cos * this.m12;
        final float nm5 = cos * this.m21 - sin * this.m22;
        final float nm6 = sin * this.m21 + cos * this.m22;
        dest.m00 = this.m00;
        dest.m01 = nm01;
        dest.m02 = nm2;
        dest.m10 = this.m10;
        dest.m11 = nm3;
        dest.m12 = nm4;
        dest.m20 = this.m20;
        dest.m21 = nm5;
        dest.m22 = nm6;
        return dest;
    }
    
    public Matrix3f rotateLocalX(final float ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix3f rotateLocalY(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = cos * this.m00 + sin * this.m02;
        final float nm2 = -sin * this.m00 + cos * this.m02;
        final float nm3 = cos * this.m10 + sin * this.m12;
        final float nm4 = -sin * this.m10 + cos * this.m12;
        final float nm5 = cos * this.m20 + sin * this.m22;
        final float nm6 = -sin * this.m20 + cos * this.m22;
        dest.m00 = nm00;
        dest.m01 = this.m01;
        dest.m02 = nm2;
        dest.m10 = nm3;
        dest.m11 = this.m11;
        dest.m12 = nm4;
        dest.m20 = nm5;
        dest.m21 = this.m21;
        dest.m22 = nm6;
        return dest;
    }
    
    public Matrix3f rotateLocalY(final float ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix3f rotateLocalZ(final float ang, final Matrix3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = cos * this.m00 - sin * this.m01;
        final float nm2 = sin * this.m00 + cos * this.m01;
        final float nm3 = cos * this.m10 - sin * this.m11;
        final float nm4 = sin * this.m10 + cos * this.m11;
        final float nm5 = cos * this.m20 - sin * this.m21;
        final float nm6 = sin * this.m20 + cos * this.m21;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = this.m02;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m12 = this.m12;
        dest.m20 = nm5;
        dest.m21 = nm6;
        dest.m22 = this.m22;
        return dest;
    }
    
    public Matrix3f rotateLocalZ(final float ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public Matrix3f rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix3f rotate(final Quaternionfc quat, final Matrix3f dest) {
        final float w2 = quat.w() * quat.w();
        final float x2 = quat.x() * quat.x();
        final float y2 = quat.y() * quat.y();
        final float z2 = quat.z() * quat.z();
        final float zw = quat.z() * quat.w();
        final float dzw = zw + zw;
        final float xy = quat.x() * quat.y();
        final float dxy = xy + xy;
        final float xz = quat.x() * quat.z();
        final float dxz = xz + xz;
        final float yw = quat.y() * quat.w();
        final float dyw = yw + yw;
        final float yz = quat.y() * quat.z();
        final float dyz = yz + yz;
        final float xw = quat.x() * quat.w();
        final float dxw = xw + xw;
        final float rm00 = w2 + x2 - z2 - y2;
        final float rm2 = dxy + dzw;
        final float rm3 = dxz - dyw;
        final float rm4 = dxy - dzw;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest.m20 = this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9;
        dest.m21 = this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9;
        dest.m22 = this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        return dest;
    }
    
    public Matrix3f rotateLocal(final Quaternionfc quat, final Matrix3f dest) {
        final float w2 = quat.w() * quat.w();
        final float x2 = quat.x() * quat.x();
        final float y2 = quat.y() * quat.y();
        final float z2 = quat.z() * quat.z();
        final float zw = quat.z() * quat.w();
        final float dzw = zw + zw;
        final float xy = quat.x() * quat.y();
        final float dxy = xy + xy;
        final float xz = quat.x() * quat.z();
        final float dxz = xz + xz;
        final float yw = quat.y() * quat.w();
        final float dyw = yw + yw;
        final float yz = quat.y() * quat.z();
        final float dyz = yz + yz;
        final float xw = quat.x() * quat.w();
        final float dxw = xw + xw;
        final float lm00 = w2 + x2 - z2 - y2;
        final float lm2 = dxy + dzw;
        final float lm3 = dxz - dyw;
        final float lm4 = dxy - dzw;
        final float lm5 = y2 - z2 + w2 - x2;
        final float lm6 = dyz + dxw;
        final float lm7 = dyw + dxz;
        final float lm8 = dyz - dxw;
        final float lm9 = z2 - y2 - x2 + w2;
        final float nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final float nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final float nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final float nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final float nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final float nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final float nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final float nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final float nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix3f rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3f rotate(final AxisAngle4f axisAngle, final Matrix3f dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix3f rotate(final float angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3f rotate(final float angle, final Vector3fc axis, final Matrix3f dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Matrix3f lookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix3f lookAlong(final Vector3fc dir, final Vector3fc up, final Matrix3f dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix3f lookAlong(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ, final Matrix3f dest) {
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= -invDirLength;
        dirY *= -invDirLength;
        dirZ *= -invDirLength;
        float leftX = upY * dirZ - upZ * dirY;
        float leftY = upZ * dirX - upX * dirZ;
        float leftZ = upX * dirY - upY * dirX;
        final float invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final float upnX = dirY * leftZ - dirZ * leftY;
        final float upnY = dirZ * leftX - dirX * leftZ;
        final float upnZ = dirX * leftY - dirY * leftX;
        final float rm00 = leftX;
        final float rm2 = upnX;
        final float rm3 = dirX;
        final float rm4 = leftY;
        final float rm5 = upnY;
        final float rm6 = dirY;
        final float rm7 = leftZ;
        final float rm8 = upnZ;
        final float rm9 = dirZ;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest.m20 = this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9;
        dest.m21 = this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9;
        dest.m22 = this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        return dest;
    }
    
    public Matrix3f lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix3f setLookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix3f setLookAlong(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ) {
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= -invDirLength;
        dirY *= -invDirLength;
        dirZ *= -invDirLength;
        float leftX = upY * dirZ - upZ * dirY;
        float leftY = upZ * dirX - upX * dirZ;
        float leftZ = upX * dirY - upY * dirX;
        final float invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final float upnX = dirY * leftZ - dirZ * leftY;
        final float upnY = dirZ * leftX - dirX * leftZ;
        final float upnZ = dirX * leftY - dirY * leftX;
        this.m00 = leftX;
        this.m01 = upnX;
        this.m02 = dirX;
        this.m10 = leftY;
        this.m11 = upnY;
        this.m12 = dirY;
        this.m20 = leftZ;
        this.m21 = upnZ;
        this.m22 = dirZ;
        return this;
    }
    
    public Vector3f getRow(final int row, final Vector3f dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                return dest.set(this.m00, this.m10, this.m20);
            }
            case 1: {
                return dest.set(this.m01, this.m11, this.m21);
            }
            case 2: {
                return dest.set(this.m02, this.m12, this.m22);
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Matrix3f setRow(final int row, final Vector3fc src) throws IndexOutOfBoundsException {
        return this.setRow(row, src.x(), src.y(), src.z());
    }
    
    public Matrix3f setRow(final int row, final float x, final float y, final float z) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                this.m00 = x;
                this.m10 = y;
                this.m20 = z;
                break;
            }
            case 1: {
                this.m01 = x;
                this.m11 = y;
                this.m21 = z;
                break;
            }
            case 2: {
                this.m02 = x;
                this.m12 = y;
                this.m22 = z;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return this;
    }
    
    public Vector3f getColumn(final int column, final Vector3f dest) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                return dest.set(this.m00, this.m01, this.m02);
            }
            case 1: {
                return dest.set(this.m10, this.m11, this.m12);
            }
            case 2: {
                return dest.set(this.m20, this.m21, this.m22);
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Matrix3f setColumn(final int column, final Vector3fc src) throws IndexOutOfBoundsException {
        return this.setColumn(column, src.x(), src.y(), src.z());
    }
    
    public Matrix3f setColumn(final int column, final float x, final float y, final float z) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                this.m00 = x;
                this.m01 = y;
                this.m02 = z;
                break;
            }
            case 1: {
                this.m10 = x;
                this.m11 = y;
                this.m12 = z;
                break;
            }
            case 2: {
                this.m20 = x;
                this.m21 = y;
                this.m22 = z;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return this;
    }
    
    public float get(final int column, final int row) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix3f set(final int column, final int row, final float value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public float getRowColumn(final int row, final int column) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix3f setRowColumn(final int row, final int column, final float value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public Matrix3f normal() {
        return this.normal(this);
    }
    
    public Matrix3f normal(final Matrix3f dest) {
        final float m00m11 = this.m00 * this.m11;
        final float m01m10 = this.m01 * this.m10;
        final float m02m10 = this.m02 * this.m10;
        final float m00m12 = this.m00 * this.m12;
        final float m01m11 = this.m01 * this.m12;
        final float m02m11 = this.m02 * this.m11;
        final float det = (m00m11 - m01m10) * this.m22 + (m02m10 - m00m12) * this.m21 + (m01m11 - m02m11) * this.m20;
        final float s = 1.0f / det;
        final float nm00 = (this.m11 * this.m22 - this.m21 * this.m12) * s;
        final float nm2 = (this.m20 * this.m12 - this.m10 * this.m22) * s;
        final float nm3 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final float nm4 = (this.m21 * this.m02 - this.m01 * this.m22) * s;
        final float nm5 = (this.m00 * this.m22 - this.m20 * this.m02) * s;
        final float nm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final float nm7 = (m01m11 - m02m11) * s;
        final float nm8 = (m02m10 - m00m12) * s;
        final float nm9 = (m00m11 - m01m10) * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Matrix3f cofactor() {
        return this.cofactor(this);
    }
    
    public Matrix3f cofactor(final Matrix3f dest) {
        final float nm00 = this.m11 * this.m22 - this.m21 * this.m12;
        final float nm2 = this.m20 * this.m12 - this.m10 * this.m22;
        final float nm3 = this.m10 * this.m21 - this.m20 * this.m11;
        final float nm4 = this.m21 * this.m02 - this.m01 * this.m22;
        final float nm5 = this.m00 * this.m22 - this.m20 * this.m02;
        final float nm6 = this.m20 * this.m01 - this.m00 * this.m21;
        final float nm7 = this.m01 * this.m12 - this.m11 * this.m02;
        final float nm8 = this.m02 * this.m10 - this.m12 * this.m00;
        final float nm9 = this.m00 * this.m11 - this.m10 * this.m01;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        return dest;
    }
    
    public Vector3f getScale(final Vector3f dest) {
        return dest.set(Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02), Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12), Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22));
    }
    
    public Vector3f positiveZ(final Vector3f dir) {
        dir.x = this.m10 * this.m21 - this.m11 * this.m20;
        dir.y = this.m20 * this.m01 - this.m21 * this.m00;
        dir.z = this.m00 * this.m11 - this.m01 * this.m10;
        return dir.normalize(dir);
    }
    
    public Vector3f normalizedPositiveZ(final Vector3f dir) {
        dir.x = this.m02;
        dir.y = this.m12;
        dir.z = this.m22;
        return dir;
    }
    
    public Vector3f positiveX(final Vector3f dir) {
        dir.x = this.m11 * this.m22 - this.m12 * this.m21;
        dir.y = this.m02 * this.m21 - this.m01 * this.m22;
        dir.z = this.m01 * this.m12 - this.m02 * this.m11;
        return dir.normalize(dir);
    }
    
    public Vector3f normalizedPositiveX(final Vector3f dir) {
        dir.x = this.m00;
        dir.y = this.m10;
        dir.z = this.m20;
        return dir;
    }
    
    public Vector3f positiveY(final Vector3f dir) {
        dir.x = this.m12 * this.m20 - this.m10 * this.m22;
        dir.y = this.m00 * this.m22 - this.m02 * this.m20;
        dir.z = this.m02 * this.m10 - this.m00 * this.m12;
        return dir.normalize(dir);
    }
    
    public Vector3f normalizedPositiveY(final Vector3f dir) {
        dir.x = this.m01;
        dir.y = this.m11;
        dir.z = this.m21;
        return dir;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.m00);
        result = 31 * result + Float.floatToIntBits(this.m01);
        result = 31 * result + Float.floatToIntBits(this.m02);
        result = 31 * result + Float.floatToIntBits(this.m10);
        result = 31 * result + Float.floatToIntBits(this.m11);
        result = 31 * result + Float.floatToIntBits(this.m12);
        result = 31 * result + Float.floatToIntBits(this.m20);
        result = 31 * result + Float.floatToIntBits(this.m21);
        result = 31 * result + Float.floatToIntBits(this.m22);
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
        final Matrix3f other = (Matrix3f)obj;
        return Float.floatToIntBits(this.m00) == Float.floatToIntBits(other.m00) && Float.floatToIntBits(this.m01) == Float.floatToIntBits(other.m01) && Float.floatToIntBits(this.m02) == Float.floatToIntBits(other.m02) && Float.floatToIntBits(this.m10) == Float.floatToIntBits(other.m10) && Float.floatToIntBits(this.m11) == Float.floatToIntBits(other.m11) && Float.floatToIntBits(this.m12) == Float.floatToIntBits(other.m12) && Float.floatToIntBits(this.m20) == Float.floatToIntBits(other.m20) && Float.floatToIntBits(this.m21) == Float.floatToIntBits(other.m21) && Float.floatToIntBits(this.m22) == Float.floatToIntBits(other.m22);
    }
    
    public boolean equals(final Matrix3fc m, final float delta) {
        return this == m || (m != null && m instanceof Matrix3f && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m02, m.m02(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m12, m.m12(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta) && Runtime.equals(this.m22, m.m22(), delta));
    }
    
    public Matrix3f swap(final Matrix3f other) {
        MemUtil.INSTANCE.swap(this, other);
        return this;
    }
    
    public Matrix3f add(final Matrix3fc other) {
        return this.add(other, this);
    }
    
    public Matrix3f add(final Matrix3fc other, final Matrix3f dest) {
        dest.m00 = this.m00 + other.m00();
        dest.m01 = this.m01 + other.m01();
        dest.m02 = this.m02 + other.m02();
        dest.m10 = this.m10 + other.m10();
        dest.m11 = this.m11 + other.m11();
        dest.m12 = this.m12 + other.m12();
        dest.m20 = this.m20 + other.m20();
        dest.m21 = this.m21 + other.m21();
        dest.m22 = this.m22 + other.m22();
        return dest;
    }
    
    public Matrix3f sub(final Matrix3fc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix3f sub(final Matrix3fc subtrahend, final Matrix3f dest) {
        dest.m00 = this.m00 - subtrahend.m00();
        dest.m01 = this.m01 - subtrahend.m01();
        dest.m02 = this.m02 - subtrahend.m02();
        dest.m10 = this.m10 - subtrahend.m10();
        dest.m11 = this.m11 - subtrahend.m11();
        dest.m12 = this.m12 - subtrahend.m12();
        dest.m20 = this.m20 - subtrahend.m20();
        dest.m21 = this.m21 - subtrahend.m21();
        dest.m22 = this.m22 - subtrahend.m22();
        return dest;
    }
    
    public Matrix3f mulComponentWise(final Matrix3fc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix3f mulComponentWise(final Matrix3fc other, final Matrix3f dest) {
        dest.m00 = this.m00 * other.m00();
        dest.m01 = this.m01 * other.m01();
        dest.m02 = this.m02 * other.m02();
        dest.m10 = this.m10 * other.m10();
        dest.m11 = this.m11 * other.m11();
        dest.m12 = this.m12 * other.m12();
        dest.m20 = this.m20 * other.m20();
        dest.m21 = this.m21 * other.m21();
        dest.m22 = this.m22 * other.m22();
        return dest;
    }
    
    public Matrix3f setSkewSymmetric(final float a, final float b, final float c) {
        final float m00 = 0.0f;
        this.m22 = m00;
        this.m11 = m00;
        this.m00 = m00;
        this.m01 = -a;
        this.m02 = b;
        this.m10 = a;
        this.m12 = -c;
        this.m20 = -b;
        this.m21 = c;
        return this;
    }
    
    public Matrix3f lerp(final Matrix3fc other, final float t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix3f lerp(final Matrix3fc other, final float t, final Matrix3f dest) {
        dest.m00 = Math.fma(other.m00() - this.m00, t, this.m00);
        dest.m01 = Math.fma(other.m01() - this.m01, t, this.m01);
        dest.m02 = Math.fma(other.m02() - this.m02, t, this.m02);
        dest.m10 = Math.fma(other.m10() - this.m10, t, this.m10);
        dest.m11 = Math.fma(other.m11() - this.m11, t, this.m11);
        dest.m12 = Math.fma(other.m12() - this.m12, t, this.m12);
        dest.m20 = Math.fma(other.m20() - this.m20, t, this.m20);
        dest.m21 = Math.fma(other.m21() - this.m21, t, this.m21);
        dest.m22 = Math.fma(other.m22() - this.m22, t, this.m22);
        return dest;
    }
    
    public Matrix3f rotateTowards(final Vector3fc direction, final Vector3fc up, final Matrix3f dest) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix3f rotateTowards(final Vector3fc direction, final Vector3fc up) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix3f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix3f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ, final Matrix3f dest) {
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final float ndirX = dirX * invDirLength;
        final float ndirY = dirY * invDirLength;
        final float ndirZ = dirZ * invDirLength;
        float leftX = upY * ndirZ - upZ * ndirY;
        float leftY = upZ * ndirX - upX * ndirZ;
        float leftZ = upX * ndirY - upY * ndirX;
        final float invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final float upnX = ndirY * leftZ - ndirZ * leftY;
        final float upnY = ndirZ * leftX - ndirX * leftZ;
        final float upnZ = ndirX * leftY - ndirY * leftX;
        final float rm00 = leftX;
        final float rm2 = leftY;
        final float rm3 = leftZ;
        final float rm4 = upnX;
        final float rm5 = upnY;
        final float rm6 = upnZ;
        final float rm7 = ndirX;
        final float rm8 = ndirY;
        final float rm9 = ndirZ;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest.m20 = this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9;
        dest.m21 = this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9;
        dest.m22 = this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        return dest;
    }
    
    public Matrix3f rotationTowards(final Vector3fc dir, final Vector3fc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix3f rotationTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final float ndirX = dirX * invDirLength;
        final float ndirY = dirY * invDirLength;
        final float ndirZ = dirZ * invDirLength;
        float leftX = upY * ndirZ - upZ * ndirY;
        float leftY = upZ * ndirX - upX * ndirZ;
        float leftZ = upX * ndirY - upY * ndirX;
        final float invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final float upnX = ndirY * leftZ - ndirZ * leftY;
        final float upnY = ndirZ * leftX - ndirX * leftZ;
        final float upnZ = ndirX * leftY - ndirY * leftX;
        this.m00 = leftX;
        this.m01 = leftY;
        this.m02 = leftZ;
        this.m10 = upnX;
        this.m11 = upnY;
        this.m12 = upnZ;
        this.m20 = ndirX;
        this.m21 = ndirY;
        this.m22 = ndirZ;
        return this;
    }
    
    public Vector3f getEulerAnglesZYX(final Vector3f dest) {
        dest.x = Math.atan2(this.m12, this.m22);
        dest.y = Math.atan2(-this.m02, Math.sqrt(1.0f - this.m02 * this.m02));
        dest.z = Math.atan2(this.m01, this.m00);
        return dest;
    }
    
    public Vector3f getEulerAnglesXYZ(final Vector3f dest) {
        dest.x = Math.atan2(-this.m21, this.m22);
        dest.y = Math.atan2(this.m20, Math.sqrt(1.0f - this.m20 * this.m20));
        dest.z = Math.atan2(-this.m10, this.m00);
        return dest;
    }
    
    public Matrix3f obliqueZ(final float a, final float b) {
        this.m20 += this.m00 * a + this.m10 * b;
        this.m21 += this.m01 * a + this.m11 * b;
        this.m22 += this.m02 * a + this.m12 * b;
        return this;
    }
    
    public Matrix3f obliqueZ(final float a, final float b, final Matrix3f dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m20 = this.m00 * a + this.m10 * b + this.m20;
        dest.m21 = this.m01 * a + this.m11 * b + this.m21;
        dest.m22 = this.m02 * a + this.m12 * b + this.m22;
        return dest;
    }
    
    public Matrix3f reflect(final float nx, final float ny, final float nz, final Matrix3f dest) {
        final float da = nx + nx;
        final float db = ny + ny;
        final float dc = nz + nz;
        final float rm00 = 1.0f - da * nx;
        final float rm2 = -da * ny;
        final float rm3 = -da * nz;
        final float rm4 = -db * nx;
        final float rm5 = 1.0f - db * ny;
        final float rm6 = -db * nz;
        final float rm7 = -dc * nx;
        final float rm8 = -dc * ny;
        final float rm9 = 1.0f - dc * nz;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        return dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m10(nm4)._m11(nm5)._m12(nm6);
    }
    
    public Matrix3f reflect(final float nx, final float ny, final float nz) {
        return this.reflect(nx, ny, nz, this);
    }
    
    public Matrix3f reflect(final Vector3fc normal) {
        return this.reflect(normal.x(), normal.y(), normal.z());
    }
    
    public Matrix3f reflect(final Quaternionfc orientation) {
        return this.reflect(orientation, this);
    }
    
    public Matrix3f reflect(final Quaternionfc orientation, final Matrix3f dest) {
        final float num1 = orientation.x() + orientation.x();
        final float num2 = orientation.y() + orientation.y();
        final float num3 = orientation.z() + orientation.z();
        final float normalX = orientation.x() * num3 + orientation.w() * num2;
        final float normalY = orientation.y() * num3 - orientation.w() * num1;
        final float normalZ = 1.0f - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflect(normalX, normalY, normalZ, dest);
    }
    
    public Matrix3f reflect(final Vector3fc normal, final Matrix3f dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), dest);
    }
    
    public Matrix3f reflection(final float nx, final float ny, final float nz) {
        final float da = nx + nx;
        final float db = ny + ny;
        final float dc = nz + nz;
        this._m00(1.0f - da * nx);
        this._m01(-da * ny);
        this._m02(-da * nz);
        this._m10(-db * nx);
        this._m11(1.0f - db * ny);
        this._m12(-db * nz);
        this._m20(-dc * nx);
        this._m21(-dc * ny);
        this._m22(1.0f - dc * nz);
        return this;
    }
    
    public Matrix3f reflection(final Vector3fc normal) {
        return this.reflection(normal.x(), normal.y(), normal.z());
    }
    
    public Matrix3f reflection(final Quaternionfc orientation) {
        final float num1 = orientation.x() + orientation.x();
        final float num2 = orientation.y() + orientation.y();
        final float num3 = orientation.z() + orientation.z();
        final float normalX = orientation.x() * num3 + orientation.w() * num2;
        final float normalY = orientation.y() * num3 - orientation.w() * num1;
        final float normalZ = 1.0f - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflection(normalX, normalY, normalZ);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22);
    }
    
    public float quadraticFormProduct(final float x, final float y, final float z) {
        final float Axx = this.m00 * x + this.m10 * y + this.m20 * z;
        final float Axy = this.m01 * x + this.m11 * y + this.m21 * z;
        final float Axz = this.m02 * x + this.m12 * y + this.m22 * z;
        return x * Axx + y * Axy + z * Axz;
    }
    
    public float quadraticFormProduct(final Vector3fc v) {
        return this.quadraticFormProduct(v.x(), v.y(), v.z());
    }
    
    public Matrix3f mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix3f mapXZY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3f mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix3f mapXZnY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3f mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix3f mapXnYnZ(final Matrix3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix3f mapXnZY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3f mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix3f mapXnZnY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3f mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix3f mapYXZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix3f mapYXnZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix3f mapYZX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix3f mapYZnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix3f mapYnXZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix3f mapYnXnZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix3f mapYnZX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix3f mapYnZnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix3f mapZXY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3f mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix3f mapZXnY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3f mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix3f mapZYX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix3f mapZYnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix3f mapZnXY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3f mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix3f mapZnXnY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3f mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix3f mapZnYX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix3f mapZnYnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix3f mapnXYnZ(final Matrix3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix3f mapnXZY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3f mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix3f mapnXZnY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3f mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix3f mapnXnYZ(final Matrix3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix3f mapnXnYnZ(final Matrix3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix3f mapnXnZY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3f mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix3f mapnXnZnY(final Matrix3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3f mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix3f mapnYXZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix3f mapnYXnZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix3f mapnYZX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix3f mapnYZnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix3f mapnYnXZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix3f mapnYnXnZ(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix3f mapnYnZX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix3f mapnYnZnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix3f mapnZXY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3f mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix3f mapnZXnY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3f mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix3f mapnZYX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix3f mapnZYnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix3f mapnZnXY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3f mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix3f mapnZnXnY(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3f mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix3f mapnZnYX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3f mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix3f mapnZnYnX(final Matrix3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3f negateX() {
        return this._m00(-this.m00)._m01(-this.m01)._m02(-this.m02);
    }
    
    public Matrix3f negateX(final Matrix3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f negateY() {
        return this._m10(-this.m10)._m11(-this.m11)._m12(-this.m12);
    }
    
    public Matrix3f negateY(final Matrix3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3f negateZ() {
        return this._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3f negateZ(final Matrix3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
