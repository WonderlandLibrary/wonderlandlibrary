// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
import java.nio.DoubleBuffer;
import java.io.Externalizable;

public class Matrix3d implements Externalizable, Cloneable, Matrix3dc
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m02;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m22;
    
    public Matrix3d() {
        this.m00 = 1.0;
        this.m11 = 1.0;
        this.m22 = 1.0;
    }
    
    public Matrix3d(final Matrix2dc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final Matrix2fc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final Matrix3dc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final Matrix3fc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final Matrix4fc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final Matrix4dc mat) {
        this.set(mat);
    }
    
    public Matrix3d(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22) {
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
    
    public Matrix3d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Matrix3d(final Vector3dc col0, final Vector3dc col1, final Vector3dc col2) {
        this.set(col0, col1, col2);
    }
    
    public double m00() {
        return this.m00;
    }
    
    public double m01() {
        return this.m01;
    }
    
    public double m02() {
        return this.m02;
    }
    
    public double m10() {
        return this.m10;
    }
    
    public double m11() {
        return this.m11;
    }
    
    public double m12() {
        return this.m12;
    }
    
    public double m20() {
        return this.m20;
    }
    
    public double m21() {
        return this.m21;
    }
    
    public double m22() {
        return this.m22;
    }
    
    public Matrix3d m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    public Matrix3d m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    public Matrix3d m02(final double m02) {
        this.m02 = m02;
        return this;
    }
    
    public Matrix3d m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    public Matrix3d m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    public Matrix3d m12(final double m12) {
        this.m12 = m12;
        return this;
    }
    
    public Matrix3d m20(final double m20) {
        this.m20 = m20;
        return this;
    }
    
    public Matrix3d m21(final double m21) {
        this.m21 = m21;
        return this;
    }
    
    public Matrix3d m22(final double m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix3d _m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix3d _m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix3d _m02(final double m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix3d _m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix3d _m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix3d _m12(final double m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix3d _m20(final double m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix3d _m21(final double m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix3d _m22(final double m22) {
        this.m22 = m22;
        return this;
    }
    
    public Matrix3d set(final Matrix3dc m) {
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
    
    public Matrix3d setTransposed(final Matrix3dc m) {
        final double nm10 = m.m01();
        final double nm11 = m.m21();
        final double nm12 = m.m02();
        final double nm13 = m.m12();
        return this._m00(m.m00())._m01(m.m10())._m02(m.m20())._m10(nm10)._m11(m.m11())._m12(nm11)._m20(nm12)._m21(nm13)._m22(m.m22());
    }
    
    public Matrix3d set(final Matrix3fc m) {
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
    
    public Matrix3d setTransposed(final Matrix3fc m) {
        final float nm10 = m.m01();
        final float nm11 = m.m21();
        final float nm12 = m.m02();
        final float nm13 = m.m12();
        return this._m00(m.m00())._m01(m.m10())._m02(m.m20())._m10(nm10)._m11(m.m11())._m12(nm11)._m20(nm12)._m21(nm13)._m22(m.m22());
    }
    
    public Matrix3d set(final Matrix4x3dc m) {
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
    
    public Matrix3d set(final Matrix4fc mat) {
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
    
    public Matrix3d set(final Matrix4dc mat) {
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
    
    public Matrix3d set(final Matrix2fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = 0.0;
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        return this;
    }
    
    public Matrix3d set(final Matrix2dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = 0.0;
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        return this;
    }
    
    public Matrix3d set(final AxisAngle4f axisAngle) {
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
        this.m00 = c + x * x * omc;
        this.m11 = c + y * y * omc;
        this.m22 = c + z * z * omc;
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
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
    
    public Matrix3d set(final AxisAngle4d axisAngle) {
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
        this.m00 = c + x * x * omc;
        this.m11 = c + y * y * omc;
        this.m22 = c + z * z * omc;
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
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
    
    public Matrix3d set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix3d set(final Quaterniondc q) {
        return this.rotation(q);
    }
    
    public Matrix3d mul(final Matrix3dc right) {
        return this.mul(right, this);
    }
    
    public Matrix3d mul(final Matrix3dc right, final Matrix3d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final double nm4 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final double nm5 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final double nm6 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final double nm7 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final double nm8 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final double nm9 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
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
    
    public Matrix3d mulLocal(final Matrix3dc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix3d mulLocal(final Matrix3dc left, final Matrix3d dest) {
        final double nm00 = left.m00() * this.m00 + left.m10() * this.m01 + left.m20() * this.m02;
        final double nm2 = left.m01() * this.m00 + left.m11() * this.m01 + left.m21() * this.m02;
        final double nm3 = left.m02() * this.m00 + left.m12() * this.m01 + left.m22() * this.m02;
        final double nm4 = left.m00() * this.m10 + left.m10() * this.m11 + left.m20() * this.m12;
        final double nm5 = left.m01() * this.m10 + left.m11() * this.m11 + left.m21() * this.m12;
        final double nm6 = left.m02() * this.m10 + left.m12() * this.m11 + left.m22() * this.m12;
        final double nm7 = left.m00() * this.m20 + left.m10() * this.m21 + left.m20() * this.m22;
        final double nm8 = left.m01() * this.m20 + left.m11() * this.m21 + left.m21() * this.m22;
        final double nm9 = left.m02() * this.m20 + left.m12() * this.m21 + left.m22() * this.m22;
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
    
    public Matrix3d mul(final Matrix3fc right) {
        return this.mul(right, this);
    }
    
    public Matrix3d mul(final Matrix3fc right, final Matrix3d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final double nm4 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final double nm5 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final double nm6 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final double nm7 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final double nm8 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final double nm9 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
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
    
    public Matrix3d set(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22) {
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
    
    public Matrix3d set(final double[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m10 = m[3];
        this.m11 = m[4];
        this.m12 = m[5];
        this.m20 = m[6];
        this.m21 = m[7];
        this.m22 = m[8];
        return this;
    }
    
    public Matrix3d set(final float[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m10 = m[3];
        this.m11 = m[4];
        this.m12 = m[5];
        this.m20 = m[6];
        this.m21 = m[7];
        this.m22 = m[8];
        return this;
    }
    
    public double determinant() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public Matrix3d invert() {
        return this.invert(this);
    }
    
    public Matrix3d invert(final Matrix3d dest) {
        final double a = Math.fma(this.m00, this.m11, -this.m01 * this.m10);
        final double b = Math.fma(this.m02, this.m10, -this.m00 * this.m12);
        final double c = Math.fma(this.m01, this.m12, -this.m02 * this.m11);
        final double d = Math.fma(a, this.m22, Math.fma(b, this.m21, c * this.m20));
        final double s = 1.0 / d;
        final double nm00 = Math.fma(this.m11, this.m22, -this.m21 * this.m12) * s;
        final double nm2 = Math.fma(this.m21, this.m02, -this.m01 * this.m22) * s;
        final double nm3 = c * s;
        final double nm4 = Math.fma(this.m20, this.m12, -this.m10 * this.m22) * s;
        final double nm5 = Math.fma(this.m00, this.m22, -this.m20 * this.m02) * s;
        final double nm6 = b * s;
        final double nm7 = Math.fma(this.m10, this.m21, -this.m20 * this.m11) * s;
        final double nm8 = Math.fma(this.m20, this.m01, -this.m00 * this.m21) * s;
        final double nm9 = a * s;
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
    
    public Matrix3d transpose() {
        return this.transpose(this);
    }
    
    public Matrix3d transpose(final Matrix3d dest) {
        dest.set(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
        return dest;
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
    
    public Matrix3d get(final Matrix3d dest) {
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
    
    public DoubleBuffer get(final DoubleBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public DoubleBuffer get(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get(final FloatBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.putf(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        return this.get(buffer.position(), buffer);
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getFloats(final ByteBuffer buffer) {
        return this.getFloats(buffer.position(), buffer);
    }
    
    public ByteBuffer getFloats(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putf(this, index, buffer);
        return buffer;
    }
    
    public Matrix3dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public double[] get(final double[] arr, final int offset) {
        arr[offset + 0] = this.m00;
        arr[offset + 1] = this.m01;
        arr[offset + 2] = this.m02;
        arr[offset + 3] = this.m10;
        arr[offset + 4] = this.m11;
        arr[offset + 5] = this.m12;
        arr[offset + 6] = this.m20;
        arr[offset + 7] = this.m21;
        arr[offset + 8] = this.m22;
        return arr;
    }
    
    public double[] get(final double[] arr) {
        return this.get(arr, 0);
    }
    
    public float[] get(final float[] arr, final int offset) {
        arr[offset + 0] = (float)this.m00;
        arr[offset + 1] = (float)this.m01;
        arr[offset + 2] = (float)this.m02;
        arr[offset + 3] = (float)this.m10;
        arr[offset + 4] = (float)this.m11;
        arr[offset + 5] = (float)this.m12;
        arr[offset + 6] = (float)this.m20;
        arr[offset + 7] = (float)this.m21;
        arr[offset + 8] = (float)this.m22;
        return arr;
    }
    
    public float[] get(final float[] arr) {
        return this.get(arr, 0);
    }
    
    public Matrix3d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3d set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3d setFloats(final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this;
    }
    
    public Matrix3d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3d set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this;
    }
    
    public Matrix3d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Matrix3d setFloats(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this;
    }
    
    public Matrix3d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Matrix3d set(final Vector3dc col0, final Vector3dc col1, final Vector3dc col2) {
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
    
    public Matrix3d zero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        return this;
    }
    
    public Matrix3d identity() {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        return this;
    }
    
    public Matrix3d scaling(final double factor) {
        this.m00 = factor;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = factor;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = factor;
        return this;
    }
    
    public Matrix3d scaling(final double x, final double y, final double z) {
        this.m00 = x;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = y;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = z;
        return this;
    }
    
    public Matrix3d scaling(final Vector3dc xyz) {
        this.m00 = xyz.x();
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = xyz.y();
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = xyz.z();
        return this;
    }
    
    public Matrix3d scale(final Vector3dc xyz, final Matrix3d dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix3d scale(final Vector3dc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix3d scale(final double x, final double y, final double z, final Matrix3d dest) {
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
    
    public Matrix3d scale(final double x, final double y, final double z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix3d scale(final double xyz, final Matrix3d dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix3d scale(final double xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix3d scaleLocal(final double x, final double y, final double z, final Matrix3d dest) {
        final double nm00 = x * this.m00;
        final double nm2 = y * this.m01;
        final double nm3 = z * this.m02;
        final double nm4 = x * this.m10;
        final double nm5 = y * this.m11;
        final double nm6 = z * this.m12;
        final double nm7 = x * this.m20;
        final double nm8 = y * this.m21;
        final double nm9 = z * this.m22;
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
    
    public Matrix3d scaleLocal(final double x, final double y, final double z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix3d rotation(final double angle, final Vector3dc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3d rotation(final double angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3d rotation(final AxisAngle4f axisAngle) {
        return this.rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3d rotation(final AxisAngle4d axisAngle) {
        return this.rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3d rotation(final double angle, final double x, final double y, final double z) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double C = 1.0 - cos;
        final double xy = x * y;
        final double xz = x * z;
        final double yz = y * z;
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
    
    public Matrix3d rotationX(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = cos;
        this.m12 = sin;
        this.m20 = 0.0;
        this.m21 = -sin;
        this.m22 = cos;
        return this;
    }
    
    public Matrix3d rotationY(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        this.m00 = cos;
        this.m01 = 0.0;
        this.m02 = -sin;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = sin;
        this.m21 = 0.0;
        this.m22 = cos;
        return this;
    }
    
    public Matrix3d rotationZ(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        this.m00 = cos;
        this.m01 = sin;
        this.m02 = 0.0;
        this.m10 = -sin;
        this.m11 = cos;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        return this;
    }
    
    public Matrix3d rotationXYZ(final double angleX, final double angleY, final double angleZ) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinX = -sinX;
        final double m_sinY = -sinY;
        final double m_sinZ = -sinZ;
        final double nm11 = cosX;
        final double nm12 = sinX;
        final double nm13 = m_sinX;
        final double nm14 = cosX;
        final double nm15 = cosY;
        final double nm16 = nm13 * m_sinY;
        final double nm17 = nm14 * m_sinY;
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
    
    public Matrix3d rotationZYX(final double angleZ, final double angleY, final double angleX) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinZ = -sinZ;
        final double m_sinY = -sinY;
        final double m_sinX = -sinX;
        final double nm00 = cosZ;
        final double nm2 = sinZ;
        final double nm3 = m_sinZ;
        final double nm4 = cosZ;
        final double nm5 = nm00 * sinY;
        final double nm6 = nm2 * sinY;
        final double nm7 = cosY;
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
    
    public Matrix3d rotationYXZ(final double angleY, final double angleX, final double angleZ) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinY = -sinY;
        final double m_sinX = -sinX;
        final double m_sinZ = -sinZ;
        final double nm00 = cosY;
        final double nm2 = m_sinY;
        final double nm3 = sinY;
        final double nm4 = cosY;
        final double nm5 = nm3 * sinX;
        final double nm6 = cosX;
        final double nm7 = nm4 * sinX;
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
    
    public Matrix3d rotation(final Quaterniondc quat) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
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
    
    public Matrix3d rotation(final Quaternionfc quat) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
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
    
    public Vector3d transform(final Vector3d v) {
        return v.mul(this);
    }
    
    public Vector3d transform(final Vector3dc v, final Vector3d dest) {
        v.mul(this, dest);
        return dest;
    }
    
    public Vector3f transform(final Vector3f v) {
        return v.mul(this);
    }
    
    public Vector3f transform(final Vector3fc v, final Vector3f dest) {
        return v.mul(this, dest);
    }
    
    public Vector3d transform(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(Math.fma(this.m00, x, Math.fma(this.m10, y, this.m20 * z)), Math.fma(this.m01, x, Math.fma(this.m11, y, this.m21 * z)), Math.fma(this.m02, x, Math.fma(this.m12, y, this.m22 * z)));
    }
    
    public Vector3d transformTranspose(final Vector3d v) {
        return v.mulTranspose(this);
    }
    
    public Vector3d transformTranspose(final Vector3dc v, final Vector3d dest) {
        return v.mulTranspose(this, dest);
    }
    
    public Vector3d transformTranspose(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(Math.fma(this.m00, x, Math.fma(this.m01, y, this.m02 * z)), Math.fma(this.m10, x, Math.fma(this.m11, y, this.m12 * z)), Math.fma(this.m20, x, Math.fma(this.m21, y, this.m22 * z)));
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.m00);
        out.writeDouble(this.m01);
        out.writeDouble(this.m02);
        out.writeDouble(this.m10);
        out.writeDouble(this.m11);
        out.writeDouble(this.m12);
        out.writeDouble(this.m20);
        out.writeDouble(this.m21);
        out.writeDouble(this.m22);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this.m00 = in.readDouble();
        this.m01 = in.readDouble();
        this.m02 = in.readDouble();
        this.m10 = in.readDouble();
        this.m11 = in.readDouble();
        this.m12 = in.readDouble();
        this.m20 = in.readDouble();
        this.m21 = in.readDouble();
        this.m22 = in.readDouble();
    }
    
    public Matrix3d rotateX(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double rm11;
        final double cos = rm11 = Math.cosFromSin(sin, ang);
        final double rm12 = -sin;
        final double rm13 = sin;
        final double rm14 = cos;
        final double nm10 = this.m10 * rm11 + this.m20 * rm13;
        final double nm11 = this.m11 * rm11 + this.m21 * rm13;
        final double nm12 = this.m12 * rm11 + this.m22 * rm13;
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
    
    public Matrix3d rotateX(final double ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix3d rotateY(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double rm00;
        final double cos = rm00 = Math.cosFromSin(sin, ang);
        final double rm2 = sin;
        final double rm3 = -sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m22 * rm3;
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
    
    public Matrix3d rotateY(final double ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix3d rotateZ(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double rm00;
        final double cos = rm00 = Math.cosFromSin(sin, ang);
        final double rm2 = -sin;
        final double rm3 = sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m10 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm3;
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
    
    public Matrix3d rotateZ(final double ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix3d rotateXYZ(final double angleX, final double angleY, final double angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix3d rotateXYZ(final double angleX, final double angleY, final double angleZ, final Matrix3d dest) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinX = -sinX;
        final double m_sinY = -sinY;
        final double m_sinZ = -sinZ;
        final double nm10 = this.m10 * cosX + this.m20 * sinX;
        final double nm11 = this.m11 * cosX + this.m21 * sinX;
        final double nm12 = this.m12 * cosX + this.m22 * sinX;
        final double nm13 = this.m10 * m_sinX + this.m20 * cosX;
        final double nm14 = this.m11 * m_sinX + this.m21 * cosX;
        final double nm15 = this.m12 * m_sinX + this.m22 * cosX;
        final double nm16 = this.m00 * cosY + nm13 * m_sinY;
        final double nm17 = this.m01 * cosY + nm14 * m_sinY;
        final double nm18 = this.m02 * cosY + nm15 * m_sinY;
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
    
    public Matrix3d rotateZYX(final double angleZ, final double angleY, final double angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix3d rotateZYX(final double angleZ, final double angleY, final double angleX, final Matrix3d dest) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinZ = -sinZ;
        final double m_sinY = -sinY;
        final double m_sinX = -sinX;
        final double nm00 = this.m00 * cosZ + this.m10 * sinZ;
        final double nm2 = this.m01 * cosZ + this.m11 * sinZ;
        final double nm3 = this.m02 * cosZ + this.m12 * sinZ;
        final double nm4 = this.m00 * m_sinZ + this.m10 * cosZ;
        final double nm5 = this.m01 * m_sinZ + this.m11 * cosZ;
        final double nm6 = this.m02 * m_sinZ + this.m12 * cosZ;
        final double nm7 = nm00 * sinY + this.m20 * cosY;
        final double nm8 = nm2 * sinY + this.m21 * cosY;
        final double nm9 = nm3 * sinY + this.m22 * cosY;
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
    
    public Matrix3d rotateYXZ(final Vector3d angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix3d rotateYXZ(final double angleY, final double angleX, final double angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix3d rotateYXZ(final double angleY, final double angleX, final double angleZ, final Matrix3d dest) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinY = -sinY;
        final double m_sinX = -sinX;
        final double m_sinZ = -sinZ;
        final double nm20 = this.m00 * sinY + this.m20 * cosY;
        final double nm21 = this.m01 * sinY + this.m21 * cosY;
        final double nm22 = this.m02 * sinY + this.m22 * cosY;
        final double nm23 = this.m00 * cosY + this.m20 * m_sinY;
        final double nm24 = this.m01 * cosY + this.m21 * m_sinY;
        final double nm25 = this.m02 * cosY + this.m22 * m_sinY;
        final double nm26 = this.m10 * cosX + nm20 * sinX;
        final double nm27 = this.m11 * cosX + nm21 * sinX;
        final double nm28 = this.m12 * cosX + nm22 * sinX;
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
    
    public Matrix3d rotate(final double ang, final double x, final double y, final double z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix3d rotate(final double ang, final double x, final double y, final double z, final Matrix3d dest) {
        final double s = Math.sin(ang);
        final double c = Math.cosFromSin(s, ang);
        final double C = 1.0 - c;
        final double xx = x * x;
        final double xy = x * y;
        final double xz = x * z;
        final double yy = y * y;
        final double yz = y * z;
        final double zz = z * z;
        final double rm00 = xx * C + c;
        final double rm2 = xy * C + z * s;
        final double rm3 = xz * C - y * s;
        final double rm4 = xy * C - z * s;
        final double rm5 = yy * C + c;
        final double rm6 = yz * C + x * s;
        final double rm7 = xz * C + y * s;
        final double rm8 = yz * C - x * s;
        final double rm9 = zz * C + c;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
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
    
    public Matrix3d rotateLocal(final double ang, final double x, final double y, final double z, final Matrix3d dest) {
        final double s = Math.sin(ang);
        final double c = Math.cosFromSin(s, ang);
        final double C = 1.0 - c;
        final double xx = x * x;
        final double xy = x * y;
        final double xz = x * z;
        final double yy = y * y;
        final double yz = y * z;
        final double zz = z * z;
        final double lm00 = xx * C + c;
        final double lm2 = xy * C + z * s;
        final double lm3 = xz * C - y * s;
        final double lm4 = xy * C - z * s;
        final double lm5 = yy * C + c;
        final double lm6 = yz * C + x * s;
        final double lm7 = xz * C + y * s;
        final double lm8 = yz * C - x * s;
        final double lm9 = zz * C + c;
        final double nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final double nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final double nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final double nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final double nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final double nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final double nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final double nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final double nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
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
    
    public Matrix3d rotateLocal(final double ang, final double x, final double y, final double z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix3d rotateLocalX(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm01 = cos * this.m01 - sin * this.m02;
        final double nm2 = sin * this.m01 + cos * this.m02;
        final double nm3 = cos * this.m11 - sin * this.m12;
        final double nm4 = sin * this.m11 + cos * this.m12;
        final double nm5 = cos * this.m21 - sin * this.m22;
        final double nm6 = sin * this.m21 + cos * this.m22;
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
    
    public Matrix3d rotateLocalX(final double ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix3d rotateLocalY(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm00 = cos * this.m00 + sin * this.m02;
        final double nm2 = -sin * this.m00 + cos * this.m02;
        final double nm3 = cos * this.m10 + sin * this.m12;
        final double nm4 = -sin * this.m10 + cos * this.m12;
        final double nm5 = cos * this.m20 + sin * this.m22;
        final double nm6 = -sin * this.m20 + cos * this.m22;
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
    
    public Matrix3d rotateLocalY(final double ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix3d rotateLocalZ(final double ang, final Matrix3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm00 = cos * this.m00 - sin * this.m01;
        final double nm2 = sin * this.m00 + cos * this.m01;
        final double nm3 = cos * this.m10 - sin * this.m11;
        final double nm4 = sin * this.m10 + cos * this.m11;
        final double nm5 = cos * this.m20 - sin * this.m21;
        final double nm6 = sin * this.m20 + cos * this.m21;
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
    
    public Matrix3d rotateLocalZ(final double ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public Matrix3d rotateLocal(final Quaterniondc quat, final Matrix3d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
        final double lm00 = w2 + x2 - z2 - y2;
        final double lm2 = dxy + dzw;
        final double lm3 = dxz - dyw;
        final double lm4 = dxy - dzw;
        final double lm5 = y2 - z2 + w2 - x2;
        final double lm6 = dyz + dxw;
        final double lm7 = dyw + dxz;
        final double lm8 = dyz - dxw;
        final double lm9 = z2 - y2 - x2 + w2;
        final double nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final double nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final double nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final double nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final double nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final double nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final double nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final double nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final double nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
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
    
    public Matrix3d rotateLocal(final Quaterniondc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix3d rotateLocal(final Quaternionfc quat, final Matrix3d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
        final double lm00 = w2 + x2 - z2 - y2;
        final double lm2 = dxy + dzw;
        final double lm3 = dxz - dyw;
        final double lm4 = dxy - dzw;
        final double lm5 = y2 - z2 + w2 - x2;
        final double lm6 = dyz + dxw;
        final double lm7 = dyw + dxz;
        final double lm8 = dyz - dxw;
        final double lm9 = z2 - y2 - x2 + w2;
        final double nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final double nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final double nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final double nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final double nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final double nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final double nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final double nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final double nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
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
    
    public Matrix3d rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix3d rotate(final Quaterniondc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix3d rotate(final Quaterniondc quat, final Matrix3d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
        final double rm00 = w2 + x2 - z2 - y2;
        final double rm2 = dxy + dzw;
        final double rm3 = dxz - dyw;
        final double rm4 = dxy - dzw;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
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
    
    public Matrix3d rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix3d rotate(final Quaternionfc quat, final Matrix3d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double dzw = zw + zw;
        final double xy = quat.x() * quat.y();
        final double dxy = xy + xy;
        final double xz = quat.x() * quat.z();
        final double dxz = xz + xz;
        final double yw = quat.y() * quat.w();
        final double dyw = yw + yw;
        final double yz = quat.y() * quat.z();
        final double dyz = yz + yz;
        final double xw = quat.x() * quat.w();
        final double dxw = xw + xw;
        final double rm00 = w2 + x2 - z2 - y2;
        final double rm2 = dxy + dzw;
        final double rm3 = dxz - dyw;
        final double rm4 = dxy - dzw;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
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
    
    public Matrix3d rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3d rotate(final AxisAngle4f axisAngle, final Matrix3d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix3d rotate(final AxisAngle4d axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix3d rotate(final AxisAngle4d axisAngle, final Matrix3d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix3d rotate(final double angle, final Vector3dc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3d rotate(final double angle, final Vector3dc axis, final Matrix3d dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Matrix3d rotate(final double angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix3d rotate(final double angle, final Vector3fc axis, final Matrix3d dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Vector3d getRow(final int row, final Vector3d dest) throws IndexOutOfBoundsException {
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
    
    public Matrix3d setRow(final int row, final Vector3dc src) throws IndexOutOfBoundsException {
        return this.setRow(row, src.x(), src.y(), src.z());
    }
    
    public Matrix3d setRow(final int row, final double x, final double y, final double z) throws IndexOutOfBoundsException {
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
    
    public Vector3d getColumn(final int column, final Vector3d dest) throws IndexOutOfBoundsException {
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
    
    public Matrix3d setColumn(final int column, final Vector3dc src) throws IndexOutOfBoundsException {
        return this.setColumn(column, src.x(), src.y(), src.z());
    }
    
    public Matrix3d setColumn(final int column, final double x, final double y, final double z) throws IndexOutOfBoundsException {
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
    
    public double get(final int column, final int row) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix3d set(final int column, final int row, final double value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public double getRowColumn(final int row, final int column) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix3d setRowColumn(final int row, final int column, final double value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public Matrix3d normal() {
        return this.normal(this);
    }
    
    public Matrix3d normal(final Matrix3d dest) {
        final double m00m11 = this.m00 * this.m11;
        final double m01m10 = this.m01 * this.m10;
        final double m02m10 = this.m02 * this.m10;
        final double m00m12 = this.m00 * this.m12;
        final double m01m11 = this.m01 * this.m12;
        final double m02m11 = this.m02 * this.m11;
        final double det = (m00m11 - m01m10) * this.m22 + (m02m10 - m00m12) * this.m21 + (m01m11 - m02m11) * this.m20;
        final double s = 1.0 / det;
        final double nm00 = (this.m11 * this.m22 - this.m21 * this.m12) * s;
        final double nm2 = (this.m20 * this.m12 - this.m10 * this.m22) * s;
        final double nm3 = (this.m10 * this.m21 - this.m20 * this.m11) * s;
        final double nm4 = (this.m21 * this.m02 - this.m01 * this.m22) * s;
        final double nm5 = (this.m00 * this.m22 - this.m20 * this.m02) * s;
        final double nm6 = (this.m20 * this.m01 - this.m00 * this.m21) * s;
        final double nm7 = (m01m11 - m02m11) * s;
        final double nm8 = (m02m10 - m00m12) * s;
        final double nm9 = (m00m11 - m01m10) * s;
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
    
    public Matrix3d cofactor() {
        return this.cofactor(this);
    }
    
    public Matrix3d cofactor(final Matrix3d dest) {
        final double nm00 = this.m11 * this.m22 - this.m21 * this.m12;
        final double nm2 = this.m20 * this.m12 - this.m10 * this.m22;
        final double nm3 = this.m10 * this.m21 - this.m20 * this.m11;
        final double nm4 = this.m21 * this.m02 - this.m01 * this.m22;
        final double nm5 = this.m00 * this.m22 - this.m20 * this.m02;
        final double nm6 = this.m20 * this.m01 - this.m00 * this.m21;
        final double nm7 = this.m01 * this.m12 - this.m11 * this.m02;
        final double nm8 = this.m02 * this.m10 - this.m12 * this.m00;
        final double nm9 = this.m00 * this.m11 - this.m10 * this.m01;
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
    
    public Matrix3d lookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix3d lookAlong(final Vector3dc dir, final Vector3dc up, final Matrix3d dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix3d lookAlong(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ, final Matrix3d dest) {
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= -invDirLength;
        dirY *= -invDirLength;
        dirZ *= -invDirLength;
        double leftX = upY * dirZ - upZ * dirY;
        double leftY = upZ * dirX - upX * dirZ;
        double leftZ = upX * dirY - upY * dirX;
        final double invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final double upnX = dirY * leftZ - dirZ * leftY;
        final double upnY = dirZ * leftX - dirX * leftZ;
        final double upnZ = dirX * leftY - dirY * leftX;
        final double rm00 = leftX;
        final double rm2 = upnX;
        final double rm3 = dirX;
        final double rm4 = leftY;
        final double rm5 = upnY;
        final double rm6 = dirY;
        final double rm7 = leftZ;
        final double rm8 = upnZ;
        final double rm9 = dirZ;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
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
    
    public Matrix3d lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix3d setLookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix3d setLookAlong(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ) {
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= -invDirLength;
        dirY *= -invDirLength;
        dirZ *= -invDirLength;
        double leftX = upY * dirZ - upZ * dirY;
        double leftY = upZ * dirX - upX * dirZ;
        double leftZ = upX * dirY - upY * dirX;
        final double invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final double upnX = dirY * leftZ - dirZ * leftY;
        final double upnY = dirZ * leftX - dirX * leftZ;
        final double upnZ = dirX * leftY - dirY * leftX;
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
    
    public Vector3d getScale(final Vector3d dest) {
        dest.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        dest.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        dest.z = Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
        return dest;
    }
    
    public Vector3d positiveZ(final Vector3d dir) {
        dir.x = this.m10 * this.m21 - this.m11 * this.m20;
        dir.y = this.m20 * this.m01 - this.m21 * this.m00;
        dir.z = this.m00 * this.m11 - this.m01 * this.m10;
        return dir.normalize(dir);
    }
    
    public Vector3d normalizedPositiveZ(final Vector3d dir) {
        dir.x = this.m02;
        dir.y = this.m12;
        dir.z = this.m22;
        return dir;
    }
    
    public Vector3d positiveX(final Vector3d dir) {
        dir.x = this.m11 * this.m22 - this.m12 * this.m21;
        dir.y = this.m02 * this.m21 - this.m01 * this.m22;
        dir.z = this.m01 * this.m12 - this.m02 * this.m11;
        return dir.normalize(dir);
    }
    
    public Vector3d normalizedPositiveX(final Vector3d dir) {
        dir.x = this.m00;
        dir.y = this.m10;
        dir.z = this.m20;
        return dir;
    }
    
    public Vector3d positiveY(final Vector3d dir) {
        dir.x = this.m12 * this.m20 - this.m10 * this.m22;
        dir.y = this.m00 * this.m22 - this.m02 * this.m20;
        dir.z = this.m02 * this.m10 - this.m00 * this.m12;
        return dir.normalize(dir);
    }
    
    public Vector3d normalizedPositiveY(final Vector3d dir) {
        dir.x = this.m01;
        dir.y = this.m11;
        dir.z = this.m21;
        return dir;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.m00);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m01);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m02);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m10);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m11);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m12);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m20);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m21);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m22);
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
        final Matrix3d other = (Matrix3d)obj;
        return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(other.m00) && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(other.m01) && Double.doubleToLongBits(this.m02) == Double.doubleToLongBits(other.m02) && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(other.m10) && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(other.m11) && Double.doubleToLongBits(this.m12) == Double.doubleToLongBits(other.m12) && Double.doubleToLongBits(this.m20) == Double.doubleToLongBits(other.m20) && Double.doubleToLongBits(this.m21) == Double.doubleToLongBits(other.m21) && Double.doubleToLongBits(this.m22) == Double.doubleToLongBits(other.m22);
    }
    
    public boolean equals(final Matrix3dc m, final double delta) {
        return this == m || (m != null && m instanceof Matrix3d && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m02, m.m02(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m12, m.m12(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta) && Runtime.equals(this.m22, m.m22(), delta));
    }
    
    public Matrix3d swap(final Matrix3d other) {
        double tmp = this.m00;
        this.m00 = other.m00;
        other.m00 = tmp;
        tmp = this.m01;
        this.m01 = other.m01;
        other.m01 = tmp;
        tmp = this.m02;
        this.m02 = other.m02;
        other.m02 = tmp;
        tmp = this.m10;
        this.m10 = other.m10;
        other.m10 = tmp;
        tmp = this.m11;
        this.m11 = other.m11;
        other.m11 = tmp;
        tmp = this.m12;
        this.m12 = other.m12;
        other.m12 = tmp;
        tmp = this.m20;
        this.m20 = other.m20;
        other.m20 = tmp;
        tmp = this.m21;
        this.m21 = other.m21;
        other.m21 = tmp;
        tmp = this.m22;
        this.m22 = other.m22;
        other.m22 = tmp;
        return this;
    }
    
    public Matrix3d add(final Matrix3dc other) {
        return this.add(other, this);
    }
    
    public Matrix3d add(final Matrix3dc other, final Matrix3d dest) {
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
    
    public Matrix3d sub(final Matrix3dc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix3d sub(final Matrix3dc subtrahend, final Matrix3d dest) {
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
    
    public Matrix3d mulComponentWise(final Matrix3dc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix3d mulComponentWise(final Matrix3dc other, final Matrix3d dest) {
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
    
    public Matrix3d setSkewSymmetric(final double a, final double b, final double c) {
        final double m00 = 0.0;
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
    
    public Matrix3d lerp(final Matrix3dc other, final double t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix3d lerp(final Matrix3dc other, final double t, final Matrix3d dest) {
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
    
    public Matrix3d rotateTowards(final Vector3dc direction, final Vector3dc up, final Matrix3d dest) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix3d rotateTowards(final Vector3dc direction, final Vector3dc up) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix3d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix3d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ, final Matrix3d dest) {
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final double ndirX = dirX * invDirLength;
        final double ndirY = dirY * invDirLength;
        final double ndirZ = dirZ * invDirLength;
        double leftX = upY * ndirZ - upZ * ndirY;
        double leftY = upZ * ndirX - upX * ndirZ;
        double leftZ = upX * ndirY - upY * ndirX;
        final double invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final double upnX = ndirY * leftZ - ndirZ * leftY;
        final double upnY = ndirZ * leftX - ndirX * leftZ;
        final double upnZ = ndirX * leftY - ndirY * leftX;
        final double rm00 = leftX;
        final double rm2 = leftY;
        final double rm3 = leftZ;
        final double rm4 = upnX;
        final double rm5 = upnY;
        final double rm6 = upnZ;
        final double rm7 = ndirX;
        final double rm8 = ndirY;
        final double rm9 = ndirZ;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
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
    
    public Matrix3d rotationTowards(final Vector3dc dir, final Vector3dc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix3d rotationTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        final double ndirX = dirX * invDirLength;
        final double ndirY = dirY * invDirLength;
        final double ndirZ = dirZ * invDirLength;
        double leftX = upY * ndirZ - upZ * ndirY;
        double leftY = upZ * ndirX - upX * ndirZ;
        double leftZ = upX * ndirY - upY * ndirX;
        final double invLeftLength = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLength;
        leftY *= invLeftLength;
        leftZ *= invLeftLength;
        final double upnX = ndirY * leftZ - ndirZ * leftY;
        final double upnY = ndirZ * leftX - ndirX * leftZ;
        final double upnZ = ndirX * leftY - ndirY * leftX;
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
    
    public Vector3d getEulerAnglesZYX(final Vector3d dest) {
        dest.x = Math.atan2(this.m12, this.m22);
        dest.y = Math.atan2(-this.m02, Math.sqrt(1.0 - this.m02 * this.m02));
        dest.z = Math.atan2(this.m01, this.m00);
        return dest;
    }
    
    public Vector3d getEulerAnglesXYZ(final Vector3d dest) {
        dest.x = Math.atan2(-this.m21, this.m22);
        dest.y = Math.atan2(this.m20, Math.sqrt(1.0 - this.m20 * this.m20));
        dest.z = Math.atan2(-this.m10, this.m00);
        return dest;
    }
    
    public Matrix3d obliqueZ(final double a, final double b) {
        this.m20 += this.m00 * a + this.m10 * b;
        this.m21 += this.m01 * a + this.m11 * b;
        this.m22 += this.m02 * a + this.m12 * b;
        return this;
    }
    
    public Matrix3d obliqueZ(final double a, final double b, final Matrix3d dest) {
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
    
    public Matrix3d reflect(final double nx, final double ny, final double nz, final Matrix3d dest) {
        final double da = nx + nx;
        final double db = ny + ny;
        final double dc = nz + nz;
        final double rm00 = 1.0 - da * nx;
        final double rm2 = -da * ny;
        final double rm3 = -da * nz;
        final double rm4 = -db * nx;
        final double rm5 = 1.0 - db * ny;
        final double rm6 = -db * nz;
        final double rm7 = -dc * nx;
        final double rm8 = -dc * ny;
        final double rm9 = 1.0 - dc * nz;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        return dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m10(nm4)._m11(nm5)._m12(nm6);
    }
    
    public Matrix3d reflect(final double nx, final double ny, final double nz) {
        return this.reflect(nx, ny, nz, this);
    }
    
    public Matrix3d reflect(final Vector3dc normal) {
        return this.reflect(normal.x(), normal.y(), normal.z());
    }
    
    public Matrix3d reflect(final Quaterniondc orientation) {
        return this.reflect(orientation, this);
    }
    
    public Matrix3d reflect(final Quaterniondc orientation, final Matrix3d dest) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflect(normalX, normalY, normalZ, dest);
    }
    
    public Matrix3d reflect(final Vector3dc normal, final Matrix3d dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), dest);
    }
    
    public Matrix3d reflection(final double nx, final double ny, final double nz) {
        final double da = nx + nx;
        final double db = ny + ny;
        final double dc = nz + nz;
        this._m00(1.0 - da * nx);
        this._m01(-da * ny);
        this._m02(-da * nz);
        this._m10(-db * nx);
        this._m11(1.0 - db * ny);
        this._m12(-db * nz);
        this._m20(-dc * nx);
        this._m21(-dc * ny);
        this._m22(1.0 - dc * nz);
        return this;
    }
    
    public Matrix3d reflection(final Vector3dc normal) {
        return this.reflection(normal.x(), normal.y(), normal.z());
    }
    
    public Matrix3d reflection(final Quaterniondc orientation) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflection(normalX, normalY, normalZ);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22);
    }
    
    public double quadraticFormProduct(final double x, final double y, final double z) {
        final double Axx = this.m00 * x + this.m10 * y + this.m20 * z;
        final double Axy = this.m01 * x + this.m11 * y + this.m21 * z;
        final double Axz = this.m02 * x + this.m12 * y + this.m22 * z;
        return x * Axx + y * Axy + z * Axz;
    }
    
    public double quadraticFormProduct(final Vector3dc v) {
        return this.quadraticFormProduct(v.x(), v.y(), v.z());
    }
    
    public double quadraticFormProduct(final Vector3fc v) {
        return this.quadraticFormProduct(v.x(), v.y(), v.z());
    }
    
    public Matrix3d mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix3d mapXZY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3d mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix3d mapXZnY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3d mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix3d mapXnYnZ(final Matrix3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix3d mapXnZY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3d mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix3d mapXnZnY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3d mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix3d mapYXZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix3d mapYXnZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix3d mapYZX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix3d mapYZnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix3d mapYnXZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix3d mapYnXnZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix3d mapYnZX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix3d mapYnZnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix3d mapZXY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3d mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix3d mapZXnY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3d mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix3d mapZYX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix3d mapZYnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix3d mapZnXY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3d mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix3d mapZnXnY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3d mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix3d mapZnYX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix3d mapZnYnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix3d mapnXYnZ(final Matrix3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix3d mapnXZY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3d mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix3d mapnXZnY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3d mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix3d mapnXnYZ(final Matrix3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix3d mapnXnYnZ(final Matrix3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix3d mapnXnZY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12);
    }
    
    public Matrix3d mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix3d mapnXnZnY(final Matrix3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12);
    }
    
    public Matrix3d mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix3d mapnYXZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix3d mapnYXnZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix3d mapnYZX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix3d mapnYZnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix3d mapnYnXZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix3d mapnYnXnZ(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix3d mapnYnZX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix3d mapnYnZnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix3d mapnZXY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3d mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix3d mapnZXnY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3d mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix3d mapnZYX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix3d mapnZYnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix3d mapnZnXY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6);
    }
    
    public Matrix3d mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix3d mapnZnXnY(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6);
    }
    
    public Matrix3d mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix3d mapnZnYX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3);
    }
    
    public Matrix3d mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix3d mapnZnYnX(final Matrix3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3);
    }
    
    public Matrix3d negateX() {
        return this._m00(-this.m00)._m01(-this.m01)._m02(-this.m02);
    }
    
    public Matrix3d negateX(final Matrix3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d negateY() {
        return this._m10(-this.m10)._m11(-this.m11)._m12(-this.m12);
    }
    
    public Matrix3d negateY(final Matrix3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22);
    }
    
    public Matrix3d negateZ() {
        return this._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Matrix3d negateZ(final Matrix3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
