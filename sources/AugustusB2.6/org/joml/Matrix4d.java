// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.io.Externalizable;

public class Matrix4d implements Externalizable, Cloneable, Matrix4dc
{
    private static final long serialVersionUID = 1L;
    double m00;
    double m01;
    double m02;
    double m03;
    double m10;
    double m11;
    double m12;
    double m13;
    double m20;
    double m21;
    double m22;
    double m23;
    double m30;
    double m31;
    double m32;
    double m33;
    int properties;
    
    public Matrix4d() {
        this._m00(1.0)._m11(1.0)._m22(1.0)._m33(1.0).properties = 30;
    }
    
    public Matrix4d(final Matrix4dc mat) {
        this.set(mat);
    }
    
    public Matrix4d(final Matrix4fc mat) {
        this.set(mat);
    }
    
    public Matrix4d(final Matrix4x3dc mat) {
        this.set(mat);
    }
    
    public Matrix4d(final Matrix4x3fc mat) {
        this.set(mat);
    }
    
    public Matrix4d(final Matrix3dc mat) {
        this.set(mat);
    }
    
    public Matrix4d(final double m00, final double m01, final double m02, final double m03, final double m10, final double m11, final double m12, final double m13, final double m20, final double m21, final double m22, final double m23, final double m30, final double m31, final double m32, final double m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
        this.determineProperties();
    }
    
    public Matrix4d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        this.determineProperties();
    }
    
    public Matrix4d(final Vector4d col0, final Vector4d col1, final Vector4d col2, final Vector4d col3) {
        this.set(col0, col1, col2, col3);
    }
    
    public Matrix4d assume(final int properties) {
        this.properties = (byte)properties;
        return this;
    }
    
    public Matrix4d determineProperties() {
        int properties = 0;
        if (this.m03 == 0.0 && this.m13 == 0.0) {
            if (this.m23 == 0.0 && this.m33 == 1.0) {
                properties |= 0x2;
                if (this.m00 == 1.0 && this.m01 == 0.0 && this.m02 == 0.0 && this.m10 == 0.0 && this.m11 == 1.0 && this.m12 == 0.0 && this.m20 == 0.0 && this.m21 == 0.0 && this.m22 == 1.0) {
                    properties |= 0x18;
                    if (this.m30 == 0.0 && this.m31 == 0.0 && this.m32 == 0.0) {
                        properties |= 0x4;
                    }
                }
            }
            else if (this.m01 == 0.0 && this.m02 == 0.0 && this.m10 == 0.0 && this.m12 == 0.0 && this.m20 == 0.0 && this.m21 == 0.0 && this.m30 == 0.0 && this.m31 == 0.0 && this.m33 == 0.0) {
                properties |= 0x1;
            }
        }
        this.properties = properties;
        return this;
    }
    
    public int properties() {
        return this.properties;
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
    
    public double m03() {
        return this.m03;
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
    
    public double m13() {
        return this.m13;
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
    
    public double m23() {
        return this.m23;
    }
    
    public double m30() {
        return this.m30;
    }
    
    public double m31() {
        return this.m31;
    }
    
    public double m32() {
        return this.m32;
    }
    
    public double m33() {
        return this.m33;
    }
    
    public Matrix4d m00(final double m00) {
        this.m00 = m00;
        this.properties &= 0xFFFFFFEF;
        if (m00 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4d m01(final double m01) {
        this.m01 = m01;
        this.properties &= 0xFFFFFFEF;
        if (m01 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m02(final double m02) {
        this.m02 = m02;
        this.properties &= 0xFFFFFFEF;
        if (m02 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m03(final double m03) {
        this.m03 = m03;
        if (m03 != 0.0) {
            this.properties = 0;
        }
        return this;
    }
    
    public Matrix4d m10(final double m10) {
        this.m10 = m10;
        this.properties &= 0xFFFFFFEF;
        if (m10 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m11(final double m11) {
        this.m11 = m11;
        this.properties &= 0xFFFFFFEF;
        if (m11 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4d m12(final double m12) {
        this.m12 = m12;
        this.properties &= 0xFFFFFFEF;
        if (m12 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m13(final double m13) {
        this.m13 = m13;
        if (this.m03 != 0.0) {
            this.properties = 0;
        }
        return this;
    }
    
    public Matrix4d m20(final double m20) {
        this.m20 = m20;
        this.properties &= 0xFFFFFFEF;
        if (m20 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m21(final double m21) {
        this.m21 = m21;
        this.properties &= 0xFFFFFFEF;
        if (m21 != 0.0) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4d m22(final double m22) {
        this.m22 = m22;
        this.properties &= 0xFFFFFFEF;
        if (m22 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4d m23(final double m23) {
        this.m23 = m23;
        if (m23 != 0.0) {
            this.properties &= 0xFFFFFFE1;
        }
        return this;
    }
    
    public Matrix4d m30(final double m30) {
        this.m30 = m30;
        if (m30 != 0.0) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4d m31(final double m31) {
        this.m31 = m31;
        if (m31 != 0.0) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4d m32(final double m32) {
        this.m32 = m32;
        if (m32 != 0.0) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4d m33(final double m33) {
        this.m33 = m33;
        if (m33 != 0.0) {
            this.properties &= 0xFFFFFFFE;
        }
        if (m33 != 1.0) {
            this.properties &= 0xFFFFFFE1;
        }
        return this;
    }
    
    Matrix4d _properties(final int properties) {
        this.properties = properties;
        return this;
    }
    
    Matrix4d _m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix4d _m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix4d _m02(final double m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix4d _m03(final double m03) {
        this.m03 = m03;
        return this;
    }
    
    Matrix4d _m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix4d _m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix4d _m12(final double m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix4d _m13(final double m13) {
        this.m13 = m13;
        return this;
    }
    
    Matrix4d _m20(final double m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix4d _m21(final double m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix4d _m22(final double m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix4d _m23(final double m23) {
        this.m23 = m23;
        return this;
    }
    
    Matrix4d _m30(final double m30) {
        this.m30 = m30;
        return this;
    }
    
    Matrix4d _m31(final double m31) {
        this.m31 = m31;
        return this;
    }
    
    Matrix4d _m32(final double m32) {
        this.m32 = m32;
        return this;
    }
    
    Matrix4d _m33(final double m33) {
        this.m33 = m33;
        return this;
    }
    
    public Matrix4d identity() {
        if ((this.properties & 0x4) != 0x0) {
            return this;
        }
        this._identity();
        this.properties = 30;
        return this;
    }
    
    private void _identity() {
        this._m00(1.0)._m10(0.0)._m20(0.0)._m30(0.0)._m01(0.0)._m11(1.0)._m21(0.0)._m31(0.0)._m02(0.0)._m12(0.0)._m22(1.0)._m32(0.0)._m03(0.0)._m13(0.0)._m23(0.0)._m33(1.0);
    }
    
    public Matrix4d set(final Matrix4dc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(m.m03())._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(m.m13())._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(m.m23())._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(m.m33())._properties(m.properties());
    }
    
    public Matrix4d set(final Matrix4fc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(m.m03())._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(m.m13())._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(m.m23())._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(m.m33())._properties(m.properties());
    }
    
    public Matrix4d setTransposed(final Matrix4dc m) {
        if ((m.properties() & 0x4) != 0x0) {
            return this.identity();
        }
        return this.setTransposedInternal(m);
    }
    
    private Matrix4d setTransposedInternal(final Matrix4dc m) {
        final double nm10 = m.m01();
        final double nm11 = m.m21();
        final double nm12 = m.m31();
        final double nm13 = m.m02();
        final double nm14 = m.m12();
        final double nm15 = m.m03();
        final double nm16 = m.m13();
        final double nm17 = m.m23();
        return this._m00(m.m00())._m01(m.m10())._m02(m.m20())._m03(m.m30())._m10(nm10)._m11(m.m11())._m12(nm11)._m13(nm12)._m20(nm13)._m21(nm14)._m22(m.m22())._m23(m.m32())._m30(nm15)._m31(nm16)._m32(nm17)._m33(m.m33())._properties(m.properties() & 0x4);
    }
    
    public Matrix4d set(final Matrix4x3dc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(0.0)._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(0.0)._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(0.0)._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(1.0)._properties(m.properties() | 0x2);
    }
    
    public Matrix4d set(final Matrix4x3fc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(0.0)._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(0.0)._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(0.0)._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(1.0)._properties(m.properties() | 0x2);
    }
    
    public Matrix4d set(final Matrix3dc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m03(0.0)._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m13(0.0)._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0)._properties(2);
    }
    
    public Matrix4d set3x3(final Matrix4dc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._properties(this.properties & mat.properties() & 0xFFFFFFFE);
    }
    
    public Matrix4d set4x3(final Matrix4x3dc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m30(mat.m30())._m31(mat.m31())._m32(mat.m32())._properties(this.properties & mat.properties() & 0xFFFFFFFE);
    }
    
    public Matrix4d set4x3(final Matrix4x3fc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m30(mat.m30())._m31(mat.m31())._m32(mat.m32())._properties(this.properties & mat.properties() & 0xFFFFFFFE);
    }
    
    public Matrix4d set4x3(final Matrix4dc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m30(mat.m30())._m31(mat.m31())._m32(mat.m32())._properties(this.properties & mat.properties() & 0xFFFFFFFE);
    }
    
    public Matrix4d set(final AxisAngle4f axisAngle) {
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
        this._m00(c + x * x * omc)._m11(c + y * y * omc)._m22(c + z * z * omc);
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
        this._m10(tmp1 - tmp2)._m01(tmp1 + tmp2);
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this._m20(tmp1 + tmp2)._m02(tmp1 - tmp2);
        tmp1 = y * z * omc;
        tmp2 = x * s;
        this._m21(tmp1 - tmp2)._m12(tmp1 + tmp2)._m03(0.0)._m13(0.0)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d set(final AxisAngle4d axisAngle) {
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
        this._m00(c + x * x * omc)._m11(c + y * y * omc)._m22(c + z * z * omc);
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
        this._m10(tmp1 - tmp2)._m01(tmp1 + tmp2);
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this._m20(tmp1 + tmp2)._m02(tmp1 - tmp2);
        tmp1 = y * z * omc;
        tmp2 = x * s;
        this._m21(tmp1 - tmp2)._m12(tmp1 + tmp2)._m03(0.0)._m13(0.0)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix4d set(final Quaterniondc q) {
        return this.rotation(q);
    }
    
    public Matrix4d mul(final Matrix4dc right) {
        return this.mul(right, this);
    }
    
    public Matrix4d mul(final Matrix4dc right, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(right);
        }
        if ((right.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        if ((this.properties & 0x8) != 0x0 && (right.properties() & 0x2) != 0x0) {
            return this.mulTranslationAffine(right, dest);
        }
        if ((this.properties & 0x2) != 0x0 && (right.properties() & 0x2) != 0x0) {
            return this.mulAffine(right, dest);
        }
        if ((this.properties & 0x1) != 0x0 && (right.properties() & 0x2) != 0x0) {
            return this.mulPerspectiveAffine(right, dest);
        }
        if ((right.properties() & 0x2) != 0x0) {
            return this.mulAffineR(right, dest);
        }
        return this.mul0(right, dest);
    }
    
    public Matrix4d mul0(final Matrix4dc right) {
        return this.mul0(right, this);
    }
    
    public Matrix4d mul0(final Matrix4dc right, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), Math.fma(this.m20, right.m02(), this.m30 * right.m03())));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), Math.fma(this.m21, right.m02(), this.m31 * right.m03())));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), Math.fma(this.m22, right.m02(), this.m32 * right.m03())));
        final double nm4 = Math.fma(this.m03, right.m00(), Math.fma(this.m13, right.m01(), Math.fma(this.m23, right.m02(), this.m33 * right.m03())));
        final double nm5 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), Math.fma(this.m20, right.m12(), this.m30 * right.m13())));
        final double nm6 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), Math.fma(this.m21, right.m12(), this.m31 * right.m13())));
        final double nm7 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), Math.fma(this.m22, right.m12(), this.m32 * right.m13())));
        final double nm8 = Math.fma(this.m03, right.m10(), Math.fma(this.m13, right.m11(), Math.fma(this.m23, right.m12(), this.m33 * right.m13())));
        final double nm9 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), Math.fma(this.m20, right.m22(), this.m30 * right.m23())));
        final double nm10 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), Math.fma(this.m21, right.m22(), this.m31 * right.m23())));
        final double nm11 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), Math.fma(this.m22, right.m22(), this.m32 * right.m23())));
        final double nm12 = Math.fma(this.m03, right.m20(), Math.fma(this.m13, right.m21(), Math.fma(this.m23, right.m22(), this.m33 * right.m23())));
        final double nm13 = Math.fma(this.m00, right.m30(), Math.fma(this.m10, right.m31(), Math.fma(this.m20, right.m32(), this.m30 * right.m33())));
        final double nm14 = Math.fma(this.m01, right.m30(), Math.fma(this.m11, right.m31(), Math.fma(this.m21, right.m32(), this.m31 * right.m33())));
        final double nm15 = Math.fma(this.m02, right.m30(), Math.fma(this.m12, right.m31(), Math.fma(this.m22, right.m32(), this.m32 * right.m33())));
        final double nm16 = Math.fma(this.m03, right.m30(), Math.fma(this.m13, right.m31(), Math.fma(this.m23, right.m32(), this.m33 * right.m33())));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d mul(final double r00, final double r01, final double r02, final double r03, final double r10, final double r11, final double r12, final double r13, final double r20, final double r21, final double r22, final double r23, final double r30, final double r31, final double r32, final double r33) {
        return this.mul(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, this);
    }
    
    public Matrix4d mul(final double r00, final double r01, final double r02, final double r03, final double r10, final double r11, final double r12, final double r13, final double r20, final double r21, final double r22, final double r23, final double r30, final double r31, final double r32, final double r33, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.mulAffineL(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, dest);
        }
        return this.mulGeneric(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, dest);
    }
    
    private Matrix4d mulAffineL(final double r00, final double r01, final double r02, final double r03, final double r10, final double r11, final double r12, final double r13, final double r20, final double r21, final double r22, final double r23, final double r30, final double r31, final double r32, final double r33, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, r00, Math.fma(this.m10, r01, Math.fma(this.m20, r02, this.m30 * r03)));
        final double nm2 = Math.fma(this.m01, r00, Math.fma(this.m11, r01, Math.fma(this.m21, r02, this.m31 * r03)));
        final double nm3 = Math.fma(this.m02, r00, Math.fma(this.m12, r01, Math.fma(this.m22, r02, this.m32 * r03)));
        final double nm4 = r03;
        final double nm5 = Math.fma(this.m00, r10, Math.fma(this.m10, r11, Math.fma(this.m20, r12, this.m30 * r13)));
        final double nm6 = Math.fma(this.m01, r10, Math.fma(this.m11, r11, Math.fma(this.m21, r12, this.m31 * r13)));
        final double nm7 = Math.fma(this.m02, r10, Math.fma(this.m12, r11, Math.fma(this.m22, r12, this.m32 * r13)));
        final double nm8 = r13;
        final double nm9 = Math.fma(this.m00, r20, Math.fma(this.m10, r21, Math.fma(this.m20, r22, this.m30 * r23)));
        final double nm10 = Math.fma(this.m01, r20, Math.fma(this.m11, r21, Math.fma(this.m21, r22, this.m31 * r23)));
        final double nm11 = Math.fma(this.m02, r20, Math.fma(this.m12, r21, Math.fma(this.m22, r22, this.m32 * r23)));
        final double nm12 = r23;
        final double nm13 = Math.fma(this.m00, r30, Math.fma(this.m10, r31, Math.fma(this.m20, r32, this.m30 * r33)));
        final double nm14 = Math.fma(this.m01, r30, Math.fma(this.m11, r31, Math.fma(this.m21, r32, this.m31 * r33)));
        final double nm15 = Math.fma(this.m02, r30, Math.fma(this.m12, r31, Math.fma(this.m22, r32, this.m32 * r33)));
        final double nm16 = r33;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(2);
    }
    
    private Matrix4d mulGeneric(final double r00, final double r01, final double r02, final double r03, final double r10, final double r11, final double r12, final double r13, final double r20, final double r21, final double r22, final double r23, final double r30, final double r31, final double r32, final double r33, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, r00, Math.fma(this.m10, r01, Math.fma(this.m20, r02, this.m30 * r03)));
        final double nm2 = Math.fma(this.m01, r00, Math.fma(this.m11, r01, Math.fma(this.m21, r02, this.m31 * r03)));
        final double nm3 = Math.fma(this.m02, r00, Math.fma(this.m12, r01, Math.fma(this.m22, r02, this.m32 * r03)));
        final double nm4 = Math.fma(this.m03, r00, Math.fma(this.m13, r01, Math.fma(this.m23, r02, this.m33 * r03)));
        final double nm5 = Math.fma(this.m00, r10, Math.fma(this.m10, r11, Math.fma(this.m20, r12, this.m30 * r13)));
        final double nm6 = Math.fma(this.m01, r10, Math.fma(this.m11, r11, Math.fma(this.m21, r12, this.m31 * r13)));
        final double nm7 = Math.fma(this.m02, r10, Math.fma(this.m12, r11, Math.fma(this.m22, r12, this.m32 * r13)));
        final double nm8 = Math.fma(this.m03, r10, Math.fma(this.m13, r11, Math.fma(this.m23, r12, this.m33 * r13)));
        final double nm9 = Math.fma(this.m00, r20, Math.fma(this.m10, r21, Math.fma(this.m20, r22, this.m30 * r23)));
        final double nm10 = Math.fma(this.m01, r20, Math.fma(this.m11, r21, Math.fma(this.m21, r22, this.m31 * r23)));
        final double nm11 = Math.fma(this.m02, r20, Math.fma(this.m12, r21, Math.fma(this.m22, r22, this.m32 * r23)));
        final double nm12 = Math.fma(this.m03, r20, Math.fma(this.m13, r21, Math.fma(this.m23, r22, this.m33 * r23)));
        final double nm13 = Math.fma(this.m00, r30, Math.fma(this.m10, r31, Math.fma(this.m20, r32, this.m30 * r33)));
        final double nm14 = Math.fma(this.m01, r30, Math.fma(this.m11, r31, Math.fma(this.m21, r32, this.m31 * r33)));
        final double nm15 = Math.fma(this.m02, r30, Math.fma(this.m12, r31, Math.fma(this.m22, r32, this.m32 * r33)));
        final double nm16 = Math.fma(this.m03, r30, Math.fma(this.m13, r31, Math.fma(this.m23, r32, this.m33 * r33)));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d mul3x3(final double r00, final double r01, final double r02, final double r10, final double r11, final double r12, final double r20, final double r21, final double r22) {
        return this.mul3x3(r00, r01, r02, r10, r11, r12, r20, r21, r22, this);
    }
    
    public Matrix4d mul3x3(final double r00, final double r01, final double r02, final double r10, final double r11, final double r12, final double r20, final double r21, final double r22, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(r00, r01, r02, 0.0, r10, r11, r12, 0.0, r20, r21, r22, 0.0, 0.0, 0.0, 0.0, 1.0);
        }
        return this.mulGeneric3x3(r00, r01, r02, r10, r11, r12, r20, r21, r22, dest);
    }
    
    private Matrix4d mulGeneric3x3(final double r00, final double r01, final double r02, final double r10, final double r11, final double r12, final double r20, final double r21, final double r22, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, r00, Math.fma(this.m10, r01, this.m20 * r02));
        final double nm2 = Math.fma(this.m01, r00, Math.fma(this.m11, r01, this.m21 * r02));
        final double nm3 = Math.fma(this.m02, r00, Math.fma(this.m12, r01, this.m22 * r02));
        final double nm4 = Math.fma(this.m03, r00, Math.fma(this.m13, r01, this.m23 * r02));
        final double nm5 = Math.fma(this.m00, r10, Math.fma(this.m10, r11, this.m20 * r12));
        final double nm6 = Math.fma(this.m01, r10, Math.fma(this.m11, r11, this.m21 * r12));
        final double nm7 = Math.fma(this.m02, r10, Math.fma(this.m12, r11, this.m22 * r12));
        final double nm8 = Math.fma(this.m03, r10, Math.fma(this.m13, r11, this.m23 * r12));
        final double nm9 = Math.fma(this.m00, r20, Math.fma(this.m10, r21, this.m20 * r22));
        final double nm10 = Math.fma(this.m01, r20, Math.fma(this.m11, r21, this.m21 * r22));
        final double nm11 = Math.fma(this.m02, r20, Math.fma(this.m12, r21, this.m22 * r22));
        final double nm12 = Math.fma(this.m03, r20, Math.fma(this.m13, r21, this.m23 * r22));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x2);
    }
    
    public Matrix4d mulLocal(final Matrix4dc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix4d mulLocal(final Matrix4dc left, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(left);
        }
        if ((left.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        if ((this.properties & 0x2) != 0x0 && (left.properties() & 0x2) != 0x0) {
            return this.mulLocalAffine(left, dest);
        }
        return this.mulLocalGeneric(left, dest);
    }
    
    private Matrix4d mulLocalGeneric(final Matrix4dc left, final Matrix4d dest) {
        final double nm00 = Math.fma(left.m00(), this.m00, Math.fma(left.m10(), this.m01, Math.fma(left.m20(), this.m02, left.m30() * this.m03)));
        final double nm2 = Math.fma(left.m01(), this.m00, Math.fma(left.m11(), this.m01, Math.fma(left.m21(), this.m02, left.m31() * this.m03)));
        final double nm3 = Math.fma(left.m02(), this.m00, Math.fma(left.m12(), this.m01, Math.fma(left.m22(), this.m02, left.m32() * this.m03)));
        final double nm4 = Math.fma(left.m03(), this.m00, Math.fma(left.m13(), this.m01, Math.fma(left.m23(), this.m02, left.m33() * this.m03)));
        final double nm5 = Math.fma(left.m00(), this.m10, Math.fma(left.m10(), this.m11, Math.fma(left.m20(), this.m12, left.m30() * this.m13)));
        final double nm6 = Math.fma(left.m01(), this.m10, Math.fma(left.m11(), this.m11, Math.fma(left.m21(), this.m12, left.m31() * this.m13)));
        final double nm7 = Math.fma(left.m02(), this.m10, Math.fma(left.m12(), this.m11, Math.fma(left.m22(), this.m12, left.m32() * this.m13)));
        final double nm8 = Math.fma(left.m03(), this.m10, Math.fma(left.m13(), this.m11, Math.fma(left.m23(), this.m12, left.m33() * this.m13)));
        final double nm9 = Math.fma(left.m00(), this.m20, Math.fma(left.m10(), this.m21, Math.fma(left.m20(), this.m22, left.m30() * this.m23)));
        final double nm10 = Math.fma(left.m01(), this.m20, Math.fma(left.m11(), this.m21, Math.fma(left.m21(), this.m22, left.m31() * this.m23)));
        final double nm11 = Math.fma(left.m02(), this.m20, Math.fma(left.m12(), this.m21, Math.fma(left.m22(), this.m22, left.m32() * this.m23)));
        final double nm12 = Math.fma(left.m03(), this.m20, Math.fma(left.m13(), this.m21, Math.fma(left.m23(), this.m22, left.m33() * this.m23)));
        final double nm13 = Math.fma(left.m00(), this.m30, Math.fma(left.m10(), this.m31, Math.fma(left.m20(), this.m32, left.m30() * this.m33)));
        final double nm14 = Math.fma(left.m01(), this.m30, Math.fma(left.m11(), this.m31, Math.fma(left.m21(), this.m32, left.m31() * this.m33)));
        final double nm15 = Math.fma(left.m02(), this.m30, Math.fma(left.m12(), this.m31, Math.fma(left.m22(), this.m32, left.m32() * this.m33)));
        final double nm16 = Math.fma(left.m03(), this.m30, Math.fma(left.m13(), this.m31, Math.fma(left.m23(), this.m32, left.m33() * this.m33)));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d mulLocalAffine(final Matrix4dc left) {
        return this.mulLocalAffine(left, this);
    }
    
    public Matrix4d mulLocalAffine(final Matrix4dc left, final Matrix4d dest) {
        final double nm00 = left.m00() * this.m00 + left.m10() * this.m01 + left.m20() * this.m02;
        final double nm2 = left.m01() * this.m00 + left.m11() * this.m01 + left.m21() * this.m02;
        final double nm3 = left.m02() * this.m00 + left.m12() * this.m01 + left.m22() * this.m02;
        final double nm4 = left.m03();
        final double nm5 = left.m00() * this.m10 + left.m10() * this.m11 + left.m20() * this.m12;
        final double nm6 = left.m01() * this.m10 + left.m11() * this.m11 + left.m21() * this.m12;
        final double nm7 = left.m02() * this.m10 + left.m12() * this.m11 + left.m22() * this.m12;
        final double nm8 = left.m13();
        final double nm9 = left.m00() * this.m20 + left.m10() * this.m21 + left.m20() * this.m22;
        final double nm10 = left.m01() * this.m20 + left.m11() * this.m21 + left.m21() * this.m22;
        final double nm11 = left.m02() * this.m20 + left.m12() * this.m21 + left.m22() * this.m22;
        final double nm12 = left.m23();
        final double nm13 = left.m00() * this.m30 + left.m10() * this.m31 + left.m20() * this.m32 + left.m30();
        final double nm14 = left.m01() * this.m30 + left.m11() * this.m31 + left.m21() * this.m32 + left.m31();
        final double nm15 = left.m02() * this.m30 + left.m12() * this.m31 + left.m22() * this.m32 + left.m32();
        final double nm16 = left.m33();
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(2);
        return dest;
    }
    
    public Matrix4d mul(final Matrix4x3dc right) {
        return this.mul(right, this);
    }
    
    public Matrix4d mul(final Matrix4x3dc right, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(right);
        }
        if ((right.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.mulTranslation(right, dest);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.mulAffine(right, dest);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.mulPerspectiveAffine(right, dest);
        }
        return this.mulGeneric(right, dest);
    }
    
    private Matrix4d mulTranslation(final Matrix4x3dc right, final Matrix4d dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m03(this.m03)._m10(right.m10())._m11(right.m11())._m12(right.m12())._m13(this.m13)._m20(right.m20())._m21(right.m21())._m22(right.m22())._m23(this.m23)._m30(right.m30() + this.m30)._m31(right.m31() + this.m31)._m32(right.m32() + this.m32)._m33(this.m33)._properties(0x2 | (right.properties() & 0x10));
    }
    
    private Matrix4d mulAffine(final Matrix4x3dc right, final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        final double m7 = this.m20;
        final double m8 = this.m21;
        final double m9 = this.m22;
        final double rm00 = right.m00();
        final double rm2 = right.m01();
        final double rm3 = right.m02();
        final double rm4 = right.m10();
        final double rm5 = right.m11();
        final double rm6 = right.m12();
        final double rm7 = right.m20();
        final double rm8 = right.m21();
        final double rm9 = right.m22();
        final double rm10 = right.m30();
        final double rm11 = right.m31();
        final double rm12 = right.m32();
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m03(this.m03)._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m13(this.m13)._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m23(this.m23)._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32))))._m33(this.m33)._properties(0x2 | (this.properties & right.properties() & 0x10));
    }
    
    private Matrix4d mulGeneric(final Matrix4x3dc right, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final double nm4 = Math.fma(this.m03, right.m00(), Math.fma(this.m13, right.m01(), this.m23 * right.m02()));
        final double nm5 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final double nm6 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final double nm7 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final double nm8 = Math.fma(this.m03, right.m10(), Math.fma(this.m13, right.m11(), this.m23 * right.m12()));
        final double nm9 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final double nm10 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final double nm11 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
        final double nm12 = Math.fma(this.m03, right.m20(), Math.fma(this.m13, right.m21(), this.m23 * right.m22()));
        final double nm13 = Math.fma(this.m00, right.m30(), Math.fma(this.m10, right.m31(), Math.fma(this.m20, right.m32(), this.m30)));
        final double nm14 = Math.fma(this.m01, right.m30(), Math.fma(this.m11, right.m31(), Math.fma(this.m21, right.m32(), this.m31)));
        final double nm15 = Math.fma(this.m02, right.m30(), Math.fma(this.m12, right.m31(), Math.fma(this.m22, right.m32(), this.m32)));
        final double nm16 = Math.fma(this.m03, right.m30(), Math.fma(this.m13, right.m31(), Math.fma(this.m23, right.m32(), this.m33)));
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d mulPerspectiveAffine(final Matrix4x3dc view, final Matrix4d dest) {
        final double lm00 = this.m00;
        final double lm2 = this.m11;
        final double lm3 = this.m22;
        final double lm4 = this.m23;
        dest._m00(lm00 * view.m00())._m01(lm2 * view.m01())._m02(lm3 * view.m02())._m03(lm4 * view.m02())._m10(lm00 * view.m10())._m11(lm2 * view.m11())._m12(lm3 * view.m12())._m13(lm4 * view.m12())._m20(lm00 * view.m20())._m21(lm2 * view.m21())._m22(lm3 * view.m22())._m23(lm4 * view.m22())._m30(lm00 * view.m30())._m31(lm2 * view.m31())._m32(lm3 * view.m32() + this.m32)._m33(lm4 * view.m32())._properties(0);
        return dest;
    }
    
    public Matrix4d mul(final Matrix4x3fc right, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(right);
        }
        if ((right.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        return this.mulGeneric(right, dest);
    }
    
    private Matrix4d mulGeneric(final Matrix4x3fc right, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final double nm4 = Math.fma(this.m03, right.m00(), Math.fma(this.m13, right.m01(), this.m23 * right.m02()));
        final double nm5 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final double nm6 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final double nm7 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final double nm8 = Math.fma(this.m03, right.m10(), Math.fma(this.m13, right.m11(), this.m23 * right.m12()));
        final double nm9 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final double nm10 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final double nm11 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
        final double nm12 = Math.fma(this.m03, right.m20(), Math.fma(this.m13, right.m21(), this.m23 * right.m22()));
        final double nm13 = Math.fma(this.m00, right.m30(), Math.fma(this.m10, right.m31(), Math.fma(this.m20, right.m32(), this.m30)));
        final double nm14 = Math.fma(this.m01, right.m30(), Math.fma(this.m11, right.m31(), Math.fma(this.m21, right.m32(), this.m31)));
        final double nm15 = Math.fma(this.m02, right.m30(), Math.fma(this.m12, right.m31(), Math.fma(this.m22, right.m32(), this.m32)));
        final double nm16 = Math.fma(this.m03, right.m30(), Math.fma(this.m13, right.m31(), Math.fma(this.m23, right.m32(), this.m33)));
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d mul(final Matrix3x2dc right) {
        return this.mul(right, this);
    }
    
    public Matrix4d mul(final Matrix3x2dc right, final Matrix4d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final double nm3 = this.m02 * right.m00() + this.m12 * right.m01();
        final double nm4 = this.m03 * right.m00() + this.m13 * right.m01();
        final double nm5 = this.m00 * right.m10() + this.m10 * right.m11();
        final double nm6 = this.m01 * right.m10() + this.m11 * right.m11();
        final double nm7 = this.m02 * right.m10() + this.m12 * right.m11();
        final double nm8 = this.m03 * right.m10() + this.m13 * right.m11();
        final double nm9 = this.m00 * right.m20() + this.m10 * right.m21() + this.m30;
        final double nm10 = this.m01 * right.m20() + this.m11 * right.m21() + this.m31;
        final double nm11 = this.m02 * right.m20() + this.m12 * right.m21() + this.m32;
        final double nm12 = this.m03 * right.m20() + this.m13 * right.m21() + this.m33;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(nm9)._m31(nm10)._m32(nm11)._m33(nm12)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d mul(final Matrix3x2fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4d mul(final Matrix3x2fc right, final Matrix4d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01();
        final double nm3 = this.m02 * right.m00() + this.m12 * right.m01();
        final double nm4 = this.m03 * right.m00() + this.m13 * right.m01();
        final double nm5 = this.m00 * right.m10() + this.m10 * right.m11();
        final double nm6 = this.m01 * right.m10() + this.m11 * right.m11();
        final double nm7 = this.m02 * right.m10() + this.m12 * right.m11();
        final double nm8 = this.m03 * right.m10() + this.m13 * right.m11();
        final double nm9 = this.m00 * right.m20() + this.m10 * right.m21() + this.m30;
        final double nm10 = this.m01 * right.m20() + this.m11 * right.m21() + this.m31;
        final double nm11 = this.m02 * right.m20() + this.m12 * right.m21() + this.m32;
        final double nm12 = this.m03 * right.m20() + this.m13 * right.m21() + this.m33;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(nm9)._m31(nm10)._m32(nm11)._m33(nm12)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d mul(final Matrix4f right) {
        return this.mul(right, this);
    }
    
    public Matrix4d mul(final Matrix4fc right, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(right);
        }
        if ((right.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        return this.mulGeneric(right, dest);
    }
    
    private Matrix4d mulGeneric(final Matrix4fc right, final Matrix4d dest) {
        final double nm00 = this.m00 * right.m00() + this.m10 * right.m01() + this.m20 * right.m02() + this.m30 * right.m03();
        final double nm2 = this.m01 * right.m00() + this.m11 * right.m01() + this.m21 * right.m02() + this.m31 * right.m03();
        final double nm3 = this.m02 * right.m00() + this.m12 * right.m01() + this.m22 * right.m02() + this.m32 * right.m03();
        final double nm4 = this.m03 * right.m00() + this.m13 * right.m01() + this.m23 * right.m02() + this.m33 * right.m03();
        final double nm5 = this.m00 * right.m10() + this.m10 * right.m11() + this.m20 * right.m12() + this.m30 * right.m13();
        final double nm6 = this.m01 * right.m10() + this.m11 * right.m11() + this.m21 * right.m12() + this.m31 * right.m13();
        final double nm7 = this.m02 * right.m10() + this.m12 * right.m11() + this.m22 * right.m12() + this.m32 * right.m13();
        final double nm8 = this.m03 * right.m10() + this.m13 * right.m11() + this.m23 * right.m12() + this.m33 * right.m13();
        final double nm9 = this.m00 * right.m20() + this.m10 * right.m21() + this.m20 * right.m22() + this.m30 * right.m23();
        final double nm10 = this.m01 * right.m20() + this.m11 * right.m21() + this.m21 * right.m22() + this.m31 * right.m23();
        final double nm11 = this.m02 * right.m20() + this.m12 * right.m21() + this.m22 * right.m22() + this.m32 * right.m23();
        final double nm12 = this.m03 * right.m20() + this.m13 * right.m21() + this.m23 * right.m22() + this.m33 * right.m23();
        final double nm13 = this.m00 * right.m30() + this.m10 * right.m31() + this.m20 * right.m32() + this.m30 * right.m33();
        final double nm14 = this.m01 * right.m30() + this.m11 * right.m31() + this.m21 * right.m32() + this.m31 * right.m33();
        final double nm15 = this.m02 * right.m30() + this.m12 * right.m31() + this.m22 * right.m32() + this.m32 * right.m33();
        final double nm16 = this.m03 * right.m30() + this.m13 * right.m31() + this.m23 * right.m32() + this.m33 * right.m33();
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
        return dest;
    }
    
    public Matrix4d mulPerspectiveAffine(final Matrix4dc view) {
        return this.mulPerspectiveAffine(view, this);
    }
    
    public Matrix4d mulPerspectiveAffine(final Matrix4dc view, final Matrix4d dest) {
        final double nm00 = this.m00 * view.m00();
        final double nm2 = this.m11 * view.m01();
        final double nm3 = this.m22 * view.m02();
        final double nm4 = this.m23 * view.m02();
        final double nm5 = this.m00 * view.m10();
        final double nm6 = this.m11 * view.m11();
        final double nm7 = this.m22 * view.m12();
        final double nm8 = this.m23 * view.m12();
        final double nm9 = this.m00 * view.m20();
        final double nm10 = this.m11 * view.m21();
        final double nm11 = this.m22 * view.m22();
        final double nm12 = this.m23 * view.m22();
        final double nm13 = this.m00 * view.m30();
        final double nm14 = this.m11 * view.m31();
        final double nm15 = this.m22 * view.m32() + this.m32;
        final double nm16 = this.m23 * view.m32();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d mulAffineR(final Matrix4dc right) {
        return this.mulAffineR(right, this);
    }
    
    public Matrix4d mulAffineR(final Matrix4dc right, final Matrix4d dest) {
        final double nm00 = Math.fma(this.m00, right.m00(), Math.fma(this.m10, right.m01(), this.m20 * right.m02()));
        final double nm2 = Math.fma(this.m01, right.m00(), Math.fma(this.m11, right.m01(), this.m21 * right.m02()));
        final double nm3 = Math.fma(this.m02, right.m00(), Math.fma(this.m12, right.m01(), this.m22 * right.m02()));
        final double nm4 = Math.fma(this.m03, right.m00(), Math.fma(this.m13, right.m01(), this.m23 * right.m02()));
        final double nm5 = Math.fma(this.m00, right.m10(), Math.fma(this.m10, right.m11(), this.m20 * right.m12()));
        final double nm6 = Math.fma(this.m01, right.m10(), Math.fma(this.m11, right.m11(), this.m21 * right.m12()));
        final double nm7 = Math.fma(this.m02, right.m10(), Math.fma(this.m12, right.m11(), this.m22 * right.m12()));
        final double nm8 = Math.fma(this.m03, right.m10(), Math.fma(this.m13, right.m11(), this.m23 * right.m12()));
        final double nm9 = Math.fma(this.m00, right.m20(), Math.fma(this.m10, right.m21(), this.m20 * right.m22()));
        final double nm10 = Math.fma(this.m01, right.m20(), Math.fma(this.m11, right.m21(), this.m21 * right.m22()));
        final double nm11 = Math.fma(this.m02, right.m20(), Math.fma(this.m12, right.m21(), this.m22 * right.m22()));
        final double nm12 = Math.fma(this.m03, right.m20(), Math.fma(this.m13, right.m21(), this.m23 * right.m22()));
        final double nm13 = Math.fma(this.m00, right.m30(), Math.fma(this.m10, right.m31(), Math.fma(this.m20, right.m32(), this.m30)));
        final double nm14 = Math.fma(this.m01, right.m30(), Math.fma(this.m11, right.m31(), Math.fma(this.m21, right.m32(), this.m31)));
        final double nm15 = Math.fma(this.m02, right.m30(), Math.fma(this.m12, right.m31(), Math.fma(this.m22, right.m32(), this.m32)));
        final double nm16 = Math.fma(this.m03, right.m30(), Math.fma(this.m13, right.m31(), Math.fma(this.m23, right.m32(), this.m33)));
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d mulAffine(final Matrix4dc right) {
        return this.mulAffine(right, this);
    }
    
    public Matrix4d mulAffine(final Matrix4dc right, final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        final double m7 = this.m20;
        final double m8 = this.m21;
        final double m9 = this.m22;
        final double rm00 = right.m00();
        final double rm2 = right.m01();
        final double rm3 = right.m02();
        final double rm4 = right.m10();
        final double rm5 = right.m11();
        final double rm6 = right.m12();
        final double rm7 = right.m20();
        final double rm8 = right.m21();
        final double rm9 = right.m22();
        final double rm10 = right.m30();
        final double rm11 = right.m31();
        final double rm12 = right.m32();
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m03(this.m03)._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m13(this.m13)._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m23(this.m23)._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32))))._m33(this.m33)._properties(0x2 | (this.properties & right.properties() & 0x10));
    }
    
    public Matrix4d mulTranslationAffine(final Matrix4dc right, final Matrix4d dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m03(this.m03)._m10(right.m10())._m11(right.m11())._m12(right.m12())._m13(this.m13)._m20(right.m20())._m21(right.m21())._m22(right.m22())._m23(this.m23)._m30(right.m30() + this.m30)._m31(right.m31() + this.m31)._m32(right.m32() + this.m32)._m33(this.m33)._properties(0x2 | (right.properties() & 0x10));
    }
    
    public Matrix4d mulOrthoAffine(final Matrix4dc view) {
        return this.mulOrthoAffine(view, this);
    }
    
    public Matrix4d mulOrthoAffine(final Matrix4dc view, final Matrix4d dest) {
        final double nm00 = this.m00 * view.m00();
        final double nm2 = this.m11 * view.m01();
        final double nm3 = this.m22 * view.m02();
        final double nm4 = 0.0;
        final double nm5 = this.m00 * view.m10();
        final double nm6 = this.m11 * view.m11();
        final double nm7 = this.m22 * view.m12();
        final double nm8 = 0.0;
        final double nm9 = this.m00 * view.m20();
        final double nm10 = this.m11 * view.m21();
        final double nm11 = this.m22 * view.m22();
        final double nm12 = 0.0;
        final double nm13 = this.m00 * view.m30() + this.m30;
        final double nm14 = this.m11 * view.m31() + this.m31;
        final double nm15 = this.m22 * view.m32() + this.m32;
        final double nm16 = 1.0;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(2);
        return dest;
    }
    
    public Matrix4d fma4x3(final Matrix4dc other, final double otherFactor) {
        return this.fma4x3(other, otherFactor, this);
    }
    
    public Matrix4d fma4x3(final Matrix4dc other, final double otherFactor, final Matrix4d dest) {
        dest._m00(Math.fma(other.m00(), otherFactor, this.m00))._m01(Math.fma(other.m01(), otherFactor, this.m01))._m02(Math.fma(other.m02(), otherFactor, this.m02))._m03(this.m03)._m10(Math.fma(other.m10(), otherFactor, this.m10))._m11(Math.fma(other.m11(), otherFactor, this.m11))._m12(Math.fma(other.m12(), otherFactor, this.m12))._m13(this.m13)._m20(Math.fma(other.m20(), otherFactor, this.m20))._m21(Math.fma(other.m21(), otherFactor, this.m21))._m22(Math.fma(other.m22(), otherFactor, this.m22))._m23(this.m23)._m30(Math.fma(other.m30(), otherFactor, this.m30))._m31(Math.fma(other.m31(), otherFactor, this.m31))._m32(Math.fma(other.m32(), otherFactor, this.m32))._m33(this.m33)._properties(0);
        return dest;
    }
    
    public Matrix4d add(final Matrix4dc other) {
        return this.add(other, this);
    }
    
    public Matrix4d add(final Matrix4dc other, final Matrix4d dest) {
        dest._m00(this.m00 + other.m00())._m01(this.m01 + other.m01())._m02(this.m02 + other.m02())._m03(this.m03 + other.m03())._m10(this.m10 + other.m10())._m11(this.m11 + other.m11())._m12(this.m12 + other.m12())._m13(this.m13 + other.m13())._m20(this.m20 + other.m20())._m21(this.m21 + other.m21())._m22(this.m22 + other.m22())._m23(this.m23 + other.m23())._m30(this.m30 + other.m30())._m31(this.m31 + other.m31())._m32(this.m32 + other.m32())._m33(this.m33 + other.m33())._properties(0);
        return dest;
    }
    
    public Matrix4d sub(final Matrix4dc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix4d sub(final Matrix4dc subtrahend, final Matrix4d dest) {
        dest._m00(this.m00 - subtrahend.m00())._m01(this.m01 - subtrahend.m01())._m02(this.m02 - subtrahend.m02())._m03(this.m03 - subtrahend.m03())._m10(this.m10 - subtrahend.m10())._m11(this.m11 - subtrahend.m11())._m12(this.m12 - subtrahend.m12())._m13(this.m13 - subtrahend.m13())._m20(this.m20 - subtrahend.m20())._m21(this.m21 - subtrahend.m21())._m22(this.m22 - subtrahend.m22())._m23(this.m23 - subtrahend.m23())._m30(this.m30 - subtrahend.m30())._m31(this.m31 - subtrahend.m31())._m32(this.m32 - subtrahend.m32())._m33(this.m33 - subtrahend.m33())._properties(0);
        return dest;
    }
    
    public Matrix4d mulComponentWise(final Matrix4dc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix4d mulComponentWise(final Matrix4dc other, final Matrix4d dest) {
        dest._m00(this.m00 * other.m00())._m01(this.m01 * other.m01())._m02(this.m02 * other.m02())._m03(this.m03 * other.m03())._m10(this.m10 * other.m10())._m11(this.m11 * other.m11())._m12(this.m12 * other.m12())._m13(this.m13 * other.m13())._m20(this.m20 * other.m20())._m21(this.m21 * other.m21())._m22(this.m22 * other.m22())._m23(this.m23 * other.m23())._m30(this.m30 * other.m30())._m31(this.m31 * other.m31())._m32(this.m32 * other.m32())._m33(this.m33 * other.m33())._properties(0);
        return dest;
    }
    
    public Matrix4d add4x3(final Matrix4dc other) {
        return this.add4x3(other, this);
    }
    
    public Matrix4d add4x3(final Matrix4dc other, final Matrix4d dest) {
        dest._m00(this.m00 + other.m00())._m01(this.m01 + other.m01())._m02(this.m02 + other.m02())._m03(this.m03)._m10(this.m10 + other.m10())._m11(this.m11 + other.m11())._m12(this.m12 + other.m12())._m13(this.m13)._m20(this.m20 + other.m20())._m21(this.m21 + other.m21())._m22(this.m22 + other.m22())._m23(this.m23)._m30(this.m30 + other.m30())._m31(this.m31 + other.m31())._m32(this.m32 + other.m32())._m33(this.m33)._properties(0);
        return dest;
    }
    
    public Matrix4d add4x3(final Matrix4fc other) {
        return this.add4x3(other, this);
    }
    
    public Matrix4d add4x3(final Matrix4fc other, final Matrix4d dest) {
        dest._m00(this.m00 + other.m00())._m01(this.m01 + other.m01())._m02(this.m02 + other.m02())._m03(this.m03)._m10(this.m10 + other.m10())._m11(this.m11 + other.m11())._m12(this.m12 + other.m12())._m13(this.m13)._m20(this.m20 + other.m20())._m21(this.m21 + other.m21())._m22(this.m22 + other.m22())._m23(this.m23)._m30(this.m30 + other.m30())._m31(this.m31 + other.m31())._m32(this.m32 + other.m32())._m33(this.m33)._properties(0);
        return dest;
    }
    
    public Matrix4d sub4x3(final Matrix4dc subtrahend) {
        return this.sub4x3(subtrahend, this);
    }
    
    public Matrix4d sub4x3(final Matrix4dc subtrahend, final Matrix4d dest) {
        dest._m00(this.m00 - subtrahend.m00())._m01(this.m01 - subtrahend.m01())._m02(this.m02 - subtrahend.m02())._m03(this.m03)._m10(this.m10 - subtrahend.m10())._m11(this.m11 - subtrahend.m11())._m12(this.m12 - subtrahend.m12())._m13(this.m13)._m20(this.m20 - subtrahend.m20())._m21(this.m21 - subtrahend.m21())._m22(this.m22 - subtrahend.m22())._m23(this.m23)._m30(this.m30 - subtrahend.m30())._m31(this.m31 - subtrahend.m31())._m32(this.m32 - subtrahend.m32())._m33(this.m33)._properties(0);
        return dest;
    }
    
    public Matrix4d mul4x3ComponentWise(final Matrix4dc other) {
        return this.mul4x3ComponentWise(other, this);
    }
    
    public Matrix4d mul4x3ComponentWise(final Matrix4dc other, final Matrix4d dest) {
        dest._m00(this.m00 * other.m00())._m01(this.m01 * other.m01())._m02(this.m02 * other.m02())._m03(this.m03)._m10(this.m10 * other.m10())._m11(this.m11 * other.m11())._m12(this.m12 * other.m12())._m13(this.m13)._m20(this.m20 * other.m20())._m21(this.m21 * other.m21())._m22(this.m22 * other.m22())._m23(this.m23)._m30(this.m30 * other.m30())._m31(this.m31 * other.m31())._m32(this.m32 * other.m32())._m33(this.m33)._properties(0);
        return dest;
    }
    
    public Matrix4d set(final double m00, final double m01, final double m02, final double m03, final double m10, final double m11, final double m12, final double m13, final double m20, final double m21, final double m22, final double m23, final double m30, final double m31, final double m32, final double m33) {
        this.m00 = m00;
        this.m10 = m10;
        this.m20 = m20;
        this.m30 = m30;
        this.m01 = m01;
        this.m11 = m11;
        this.m21 = m21;
        this.m31 = m31;
        this.m02 = m02;
        this.m12 = m12;
        this.m22 = m22;
        this.m32 = m32;
        this.m03 = m03;
        this.m13 = m13;
        this.m23 = m23;
        this.m33 = m33;
        return this.determineProperties();
    }
    
    public Matrix4d set(final double[] m, final int off) {
        return this._m00(m[off + 0])._m01(m[off + 1])._m02(m[off + 2])._m03(m[off + 3])._m10(m[off + 4])._m11(m[off + 5])._m12(m[off + 6])._m13(m[off + 7])._m20(m[off + 8])._m21(m[off + 9])._m22(m[off + 10])._m23(m[off + 11])._m30(m[off + 12])._m31(m[off + 13])._m32(m[off + 14])._m33(m[off + 15]).determineProperties();
    }
    
    public Matrix4d set(final double[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4d set(final float[] m, final int off) {
        return this._m00(m[off + 0])._m01(m[off + 1])._m02(m[off + 2])._m03(m[off + 3])._m10(m[off + 4])._m11(m[off + 5])._m12(m[off + 6])._m13(m[off + 7])._m20(m[off + 8])._m21(m[off + 9])._m22(m[off + 10])._m23(m[off + 11])._m30(m[off + 12])._m31(m[off + 13])._m32(m[off + 14])._m33(m[off + 15]).determineProperties();
    }
    
    public Matrix4d set(final float[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4d set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4d set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4d setFloats(final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4d setFloats(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this.determineProperties();
    }
    
    public Matrix4d set(final Vector4d col0, final Vector4d col1, final Vector4d col2, final Vector4d col3) {
        return this._m00(col0.x())._m01(col0.y())._m02(col0.z())._m03(col0.w())._m10(col1.x())._m11(col1.y())._m12(col1.z())._m13(col1.w())._m20(col2.x())._m21(col2.y())._m22(col2.z())._m23(col2.w())._m30(col3.x())._m31(col3.y())._m32(col3.z())._m33(col3.w()).determineProperties();
    }
    
    public double determinant() {
        if ((this.properties & 0x2) != 0x0) {
            return this.determinantAffine();
        }
        return (this.m00 * this.m11 - this.m01 * this.m10) * (this.m22 * this.m33 - this.m23 * this.m32) + (this.m02 * this.m10 - this.m00 * this.m12) * (this.m21 * this.m33 - this.m23 * this.m31) + (this.m00 * this.m13 - this.m03 * this.m10) * (this.m21 * this.m32 - this.m22 * this.m31) + (this.m01 * this.m12 - this.m02 * this.m11) * (this.m20 * this.m33 - this.m23 * this.m30) + (this.m03 * this.m11 - this.m01 * this.m13) * (this.m20 * this.m32 - this.m22 * this.m30) + (this.m02 * this.m13 - this.m03 * this.m12) * (this.m20 * this.m31 - this.m21 * this.m30);
    }
    
    public double determinant3x3() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public double determinantAffine() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public Matrix4d invert() {
        return this.invert(this);
    }
    
    public Matrix4d invert(final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.invertTranslation(dest);
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.invertOrthonormal(dest);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.invertAffine(dest);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.invertPerspective(dest);
        }
        return this.invertGeneric(dest);
    }
    
    private Matrix4d invertTranslation(final Matrix4d dest) {
        if (dest != this) {
            dest.set(this);
        }
        dest._m30(-this.m30)._m31(-this.m31)._m32(-this.m32)._properties(26);
        return dest;
    }
    
    private Matrix4d invertOrthonormal(final Matrix4d dest) {
        final double nm30 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
        final double nm31 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
        final double nm32 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
        final double m01 = this.m01;
        final double m2 = this.m02;
        final double m3 = this.m12;
        dest._m00(this.m00)._m01(this.m10)._m02(this.m20)._m03(0.0)._m10(m01)._m11(this.m11)._m12(this.m21)._m13(0.0)._m20(m2)._m21(m3)._m22(this.m22)._m23(0.0)._m30(nm30)._m31(nm31)._m32(nm32)._m33(1.0)._properties(18);
        return dest;
    }
    
    private Matrix4d invertGeneric(final Matrix4d dest) {
        if (this != dest) {
            return this.invertGenericNonThis(dest);
        }
        return this.invertGenericThis(dest);
    }
    
    private Matrix4d invertGenericNonThis(final Matrix4d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        double det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0 / det;
        return dest._m00(Math.fma(this.m11, l, Math.fma(-this.m12, k, this.m13 * j)) * det)._m01(Math.fma(-this.m01, l, Math.fma(this.m02, k, -this.m03 * j)) * det)._m02(Math.fma(this.m31, f, Math.fma(-this.m32, e, this.m33 * d)) * det)._m03(Math.fma(-this.m21, f, Math.fma(this.m22, e, -this.m23 * d)) * det)._m10(Math.fma(-this.m10, l, Math.fma(this.m12, i, -this.m13 * h)) * det)._m11(Math.fma(this.m00, l, Math.fma(-this.m02, i, this.m03 * h)) * det)._m12(Math.fma(-this.m30, f, Math.fma(this.m32, c, -this.m33 * b)) * det)._m13(Math.fma(this.m20, f, Math.fma(-this.m22, c, this.m23 * b)) * det)._m20(Math.fma(this.m10, k, Math.fma(-this.m11, i, this.m13 * g)) * det)._m21(Math.fma(-this.m00, k, Math.fma(this.m01, i, -this.m03 * g)) * det)._m22(Math.fma(this.m30, e, Math.fma(-this.m31, c, this.m33 * a)) * det)._m23(Math.fma(-this.m20, e, Math.fma(this.m21, c, -this.m23 * a)) * det)._m30(Math.fma(-this.m10, j, Math.fma(this.m11, h, -this.m12 * g)) * det)._m31(Math.fma(this.m00, j, Math.fma(-this.m01, h, this.m02 * g)) * det)._m32(Math.fma(-this.m30, d, Math.fma(this.m31, b, -this.m32 * a)) * det)._m33(Math.fma(this.m20, d, Math.fma(-this.m21, b, this.m22 * a)) * det)._properties(0);
    }
    
    private Matrix4d invertGenericThis(final Matrix4d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        double det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0 / det;
        final double nm00 = Math.fma(this.m11, l, Math.fma(-this.m12, k, this.m13 * j)) * det;
        final double nm2 = Math.fma(-this.m01, l, Math.fma(this.m02, k, -this.m03 * j)) * det;
        final double nm3 = Math.fma(this.m31, f, Math.fma(-this.m32, e, this.m33 * d)) * det;
        final double nm4 = Math.fma(-this.m21, f, Math.fma(this.m22, e, -this.m23 * d)) * det;
        final double nm5 = Math.fma(-this.m10, l, Math.fma(this.m12, i, -this.m13 * h)) * det;
        final double nm6 = Math.fma(this.m00, l, Math.fma(-this.m02, i, this.m03 * h)) * det;
        final double nm7 = Math.fma(-this.m30, f, Math.fma(this.m32, c, -this.m33 * b)) * det;
        final double nm8 = Math.fma(this.m20, f, Math.fma(-this.m22, c, this.m23 * b)) * det;
        final double nm9 = Math.fma(this.m10, k, Math.fma(-this.m11, i, this.m13 * g)) * det;
        final double nm10 = Math.fma(-this.m00, k, Math.fma(this.m01, i, -this.m03 * g)) * det;
        final double nm11 = Math.fma(this.m30, e, Math.fma(-this.m31, c, this.m33 * a)) * det;
        final double nm12 = Math.fma(-this.m20, e, Math.fma(this.m21, c, -this.m23 * a)) * det;
        final double nm13 = Math.fma(-this.m10, j, Math.fma(this.m11, h, -this.m12 * g)) * det;
        final double nm14 = Math.fma(this.m00, j, Math.fma(-this.m01, h, this.m02 * g)) * det;
        final double nm15 = Math.fma(-this.m30, d, Math.fma(this.m31, b, -this.m32 * a)) * det;
        final double nm16 = Math.fma(this.m20, d, Math.fma(-this.m21, b, this.m22 * a)) * det;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d invertPerspective(final Matrix4d dest) {
        final double a = 1.0 / (this.m00 * this.m11);
        final double l = -1.0 / (this.m23 * this.m32);
        dest.set(this.m11 * a, 0.0, 0.0, 0.0, 0.0, this.m00 * a, 0.0, 0.0, 0.0, 0.0, 0.0, -this.m23 * l, 0.0, 0.0, -this.m32 * l, this.m22 * l);
        return dest;
    }
    
    public Matrix4d invertPerspective() {
        return this.invertPerspective(this);
    }
    
    public Matrix4d invertFrustum(final Matrix4d dest) {
        final double invM00 = 1.0 / this.m00;
        final double invM2 = 1.0 / this.m11;
        final double invM3 = 1.0 / this.m23;
        final double invM4 = 1.0 / this.m32;
        dest.set(invM00, 0.0, 0.0, 0.0, 0.0, invM2, 0.0, 0.0, 0.0, 0.0, 0.0, invM4, -this.m20 * invM00 * invM3, -this.m21 * invM2 * invM3, invM3, -this.m22 * invM3 * invM4);
        return dest;
    }
    
    public Matrix4d invertFrustum() {
        return this.invertFrustum(this);
    }
    
    public Matrix4d invertOrtho(final Matrix4d dest) {
        final double invM00 = 1.0 / this.m00;
        final double invM2 = 1.0 / this.m11;
        final double invM3 = 1.0 / this.m22;
        dest.set(invM00, 0.0, 0.0, 0.0, 0.0, invM2, 0.0, 0.0, 0.0, 0.0, invM3, 0.0, -this.m30 * invM00, -this.m31 * invM2, -this.m32 * invM3, 1.0)._properties(0x2 | (this.properties & 0x10));
        return dest;
    }
    
    public Matrix4d invertOrtho() {
        return this.invertOrtho(this);
    }
    
    public Matrix4d invertPerspectiveView(final Matrix4dc view, final Matrix4d dest) {
        final double a = 1.0 / (this.m00 * this.m11);
        final double l = -1.0 / (this.m23 * this.m32);
        final double pm00 = this.m11 * a;
        final double pm2 = this.m00 * a;
        final double pm3 = -this.m23 * l;
        final double pm4 = -this.m32 * l;
        final double pm5 = this.m22 * l;
        final double vm30 = -view.m00() * view.m30() - view.m01() * view.m31() - view.m02() * view.m32();
        final double vm31 = -view.m10() * view.m30() - view.m11() * view.m31() - view.m12() * view.m32();
        final double vm32 = -view.m20() * view.m30() - view.m21() * view.m31() - view.m22() * view.m32();
        final double nm10 = view.m01() * pm2;
        final double nm11 = view.m02() * pm4 + vm30 * pm5;
        final double nm12 = view.m12() * pm4 + vm31 * pm5;
        final double nm13 = view.m22() * pm4 + vm32 * pm5;
        return dest._m00(view.m00() * pm00)._m01(view.m10() * pm00)._m02(view.m20() * pm00)._m03(0.0)._m10(nm10)._m11(view.m11() * pm2)._m12(view.m21() * pm2)._m13(0.0)._m20(vm30 * pm3)._m21(vm31 * pm3)._m22(vm32 * pm3)._m23(pm3)._m30(nm11)._m31(nm12)._m32(nm13)._m33(pm5)._properties(0);
    }
    
    public Matrix4d invertPerspectiveView(final Matrix4x3dc view, final Matrix4d dest) {
        final double a = 1.0 / (this.m00 * this.m11);
        final double l = -1.0 / (this.m23 * this.m32);
        final double pm00 = this.m11 * a;
        final double pm2 = this.m00 * a;
        final double pm3 = -this.m23 * l;
        final double pm4 = -this.m32 * l;
        final double pm5 = this.m22 * l;
        final double vm30 = -view.m00() * view.m30() - view.m01() * view.m31() - view.m02() * view.m32();
        final double vm31 = -view.m10() * view.m30() - view.m11() * view.m31() - view.m12() * view.m32();
        final double vm32 = -view.m20() * view.m30() - view.m21() * view.m31() - view.m22() * view.m32();
        return dest._m00(view.m00() * pm00)._m01(view.m10() * pm00)._m02(view.m20() * pm00)._m03(0.0)._m10(view.m01() * pm2)._m11(view.m11() * pm2)._m12(view.m21() * pm2)._m13(0.0)._m20(vm30 * pm3)._m21(vm31 * pm3)._m22(vm32 * pm3)._m23(pm3)._m30(view.m02() * pm4 + vm30 * pm5)._m31(view.m12() * pm4 + vm31 * pm5)._m32(view.m22() * pm4 + vm32 * pm5)._m33(pm5)._properties(0);
    }
    
    public Matrix4d invertAffine(final Matrix4d dest) {
        final double m11m00 = this.m00 * this.m11;
        final double m10m01 = this.m01 * this.m10;
        final double m10m2 = this.m02 * this.m10;
        final double m12m00 = this.m00 * this.m12;
        final double m12m2 = this.m01 * this.m12;
        final double m11m2 = this.m02 * this.m11;
        final double s = 1.0 / ((m11m00 - m10m01) * this.m22 + (m10m2 - m12m00) * this.m21 + (m12m2 - m11m2) * this.m20);
        final double m10m3 = this.m10 * this.m22;
        final double m10m4 = this.m10 * this.m21;
        final double m11m3 = this.m11 * this.m22;
        final double m11m4 = this.m11 * this.m20;
        final double m12m3 = this.m12 * this.m21;
        final double m12m4 = this.m12 * this.m20;
        final double m20m02 = this.m20 * this.m02;
        final double m20m3 = this.m20 * this.m01;
        final double m21m02 = this.m21 * this.m02;
        final double m21m3 = this.m21 * this.m00;
        final double m22m01 = this.m22 * this.m01;
        final double m22m2 = this.m22 * this.m00;
        final double nm00 = (m11m3 - m12m3) * s;
        final double nm2 = (m21m02 - m22m01) * s;
        final double nm3 = (m12m2 - m11m2) * s;
        final double nm4 = (m12m4 - m10m3) * s;
        final double nm5 = (m22m2 - m20m02) * s;
        final double nm6 = (m10m2 - m12m00) * s;
        final double nm7 = (m10m4 - m11m4) * s;
        final double nm8 = (m20m3 - m21m3) * s;
        final double nm9 = (m11m00 - m10m01) * s;
        final double nm10 = (m10m3 * this.m31 - m10m4 * this.m32 + m11m4 * this.m32 - m11m3 * this.m30 + m12m3 * this.m30 - m12m4 * this.m31) * s;
        final double nm11 = (m20m02 * this.m31 - m20m3 * this.m32 + m21m3 * this.m32 - m21m02 * this.m30 + m22m01 * this.m30 - m22m2 * this.m31) * s;
        final double nm12 = (m11m2 * this.m30 - m12m2 * this.m30 + m12m00 * this.m31 - m10m2 * this.m31 + m10m01 * this.m32 - m11m00 * this.m32) * s;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m20(nm7)._m21(nm8)._m22(nm9)._m23(0.0)._m30(nm10)._m31(nm11)._m32(nm12)._m33(1.0)._properties(2);
        return dest;
    }
    
    public Matrix4d invertAffine() {
        return this.invertAffine(this);
    }
    
    public Matrix4d transpose() {
        return this.transpose(this);
    }
    
    public Matrix4d transpose(final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if (this != dest) {
            return this.transposeNonThisGeneric(dest);
        }
        return this.transposeThisGeneric(dest);
    }
    
    private Matrix4d transposeNonThisGeneric(final Matrix4d dest) {
        return dest._m00(this.m00)._m01(this.m10)._m02(this.m20)._m03(this.m30)._m10(this.m01)._m11(this.m11)._m12(this.m21)._m13(this.m31)._m20(this.m02)._m21(this.m12)._m22(this.m22)._m23(this.m32)._m30(this.m03)._m31(this.m13)._m32(this.m23)._m33(this.m33)._properties(0);
    }
    
    private Matrix4d transposeThisGeneric(final Matrix4d dest) {
        final double nm10 = this.m01;
        final double nm11 = this.m02;
        final double nm12 = this.m12;
        final double nm13 = this.m03;
        final double nm14 = this.m13;
        final double nm15 = this.m23;
        return dest._m01(this.m10)._m02(this.m20)._m03(this.m30)._m10(nm10)._m12(this.m21)._m13(this.m31)._m20(nm11)._m21(nm12)._m23(this.m32)._m30(nm13)._m31(nm14)._m32(nm15)._properties(0);
    }
    
    public Matrix4d transpose3x3() {
        return this.transpose3x3(this);
    }
    
    public Matrix4d transpose3x3(final Matrix4d dest) {
        final double nm10 = this.m01;
        final double nm11 = this.m02;
        final double nm12 = this.m12;
        return dest._m00(this.m00)._m01(this.m10)._m02(this.m20)._m10(nm10)._m11(this.m11)._m12(this.m21)._m20(nm11)._m21(nm12)._m22(this.m22)._properties(this.properties & 0x1E);
    }
    
    public Matrix3d transpose3x3(final Matrix3d dest) {
        return dest._m00(this.m00)._m01(this.m10)._m02(this.m20)._m10(this.m01)._m11(this.m11)._m12(this.m21)._m20(this.m02)._m21(this.m12)._m22(this.m22);
    }
    
    public Matrix4d translation(final double x, final double y, final double z) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        return this._m30(x)._m31(y)._m32(z)._m33(1.0)._properties(26);
    }
    
    public Matrix4d translation(final Vector3fc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d translation(final Vector3dc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d setTranslation(final double x, final double y, final double z) {
        final Matrix4d m32 = this._m30(x)._m31(y)._m32(z);
        m32.properties &= 0xFFFFFFFA;
        return this;
    }
    
    public Matrix4d setTranslation(final Vector3dc xyz) {
        return this.setTranslation(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Vector3d getTranslation(final Vector3d dest) {
        dest.x = this.m30;
        dest.y = this.m31;
        dest.z = this.m32;
        return dest;
    }
    
    public Vector3d getScale(final Vector3d dest) {
        dest.x = Math.sqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        dest.y = Math.sqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        dest.z = Math.sqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
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
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + " " + Runtime.format(this.m20, formatter) + " " + Runtime.format(this.m30, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + " " + Runtime.format(this.m21, formatter) + " " + Runtime.format(this.m31, formatter) + "\n" + Runtime.format(this.m02, formatter) + " " + Runtime.format(this.m12, formatter) + " " + Runtime.format(this.m22, formatter) + " " + Runtime.format(this.m32, formatter) + "\n" + Runtime.format(this.m03, formatter) + " " + Runtime.format(this.m13, formatter) + " " + Runtime.format(this.m23, formatter) + " " + Runtime.format(this.m33, formatter) + "\n";
    }
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set(this);
    }
    
    public Matrix4x3d get4x3(final Matrix4x3d dest) {
        return dest.set(this);
    }
    
    public Matrix3d get3x3(final Matrix3d dest) {
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
    
    public DoubleBuffer get(final DoubleBuffer dest) {
        MemUtil.INSTANCE.put(this, dest.position(), dest);
        return dest;
    }
    
    public DoubleBuffer get(final int index, final DoubleBuffer dest) {
        MemUtil.INSTANCE.put(this, index, dest);
        return dest;
    }
    
    public FloatBuffer get(final FloatBuffer dest) {
        MemUtil.INSTANCE.putf(this, dest.position(), dest);
        return dest;
    }
    
    public FloatBuffer get(final int index, final FloatBuffer dest) {
        MemUtil.INSTANCE.putf(this, index, dest);
        return dest;
    }
    
    public ByteBuffer get(final ByteBuffer dest) {
        MemUtil.INSTANCE.put(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer get(final int index, final ByteBuffer dest) {
        MemUtil.INSTANCE.put(this, index, dest);
        return dest;
    }
    
    public ByteBuffer getFloats(final ByteBuffer dest) {
        MemUtil.INSTANCE.putf(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer getFloats(final int index, final ByteBuffer dest) {
        MemUtil.INSTANCE.putf(this, index, dest);
        return dest;
    }
    
    public Matrix4dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public double[] get(final double[] dest, final int offset) {
        dest[offset + 0] = this.m00;
        dest[offset + 1] = this.m01;
        dest[offset + 2] = this.m02;
        dest[offset + 3] = this.m03;
        dest[offset + 4] = this.m10;
        dest[offset + 5] = this.m11;
        dest[offset + 6] = this.m12;
        dest[offset + 7] = this.m13;
        dest[offset + 8] = this.m20;
        dest[offset + 9] = this.m21;
        dest[offset + 10] = this.m22;
        dest[offset + 11] = this.m23;
        dest[offset + 12] = this.m30;
        dest[offset + 13] = this.m31;
        dest[offset + 14] = this.m32;
        dest[offset + 15] = this.m33;
        return dest;
    }
    
    public double[] get(final double[] dest) {
        return this.get(dest, 0);
    }
    
    public float[] get(final float[] dest, final int offset) {
        dest[offset + 0] = (float)this.m00;
        dest[offset + 1] = (float)this.m01;
        dest[offset + 2] = (float)this.m02;
        dest[offset + 3] = (float)this.m03;
        dest[offset + 4] = (float)this.m10;
        dest[offset + 5] = (float)this.m11;
        dest[offset + 6] = (float)this.m12;
        dest[offset + 7] = (float)this.m13;
        dest[offset + 8] = (float)this.m20;
        dest[offset + 9] = (float)this.m21;
        dest[offset + 10] = (float)this.m22;
        dest[offset + 11] = (float)this.m23;
        dest[offset + 12] = (float)this.m30;
        dest[offset + 13] = (float)this.m31;
        dest[offset + 14] = (float)this.m32;
        dest[offset + 15] = (float)this.m33;
        return dest;
    }
    
    public float[] get(final float[] dest) {
        return this.get(dest, 0);
    }
    
    public DoubleBuffer getTransposed(final DoubleBuffer dest) {
        MemUtil.INSTANCE.putTransposed(this, dest.position(), dest);
        return dest;
    }
    
    public DoubleBuffer getTransposed(final int index, final DoubleBuffer dest) {
        MemUtil.INSTANCE.putTransposed(this, index, dest);
        return dest;
    }
    
    public ByteBuffer getTransposed(final ByteBuffer dest) {
        MemUtil.INSTANCE.putTransposed(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer getTransposed(final int index, final ByteBuffer dest) {
        MemUtil.INSTANCE.putTransposed(this, index, dest);
        return dest;
    }
    
    public DoubleBuffer get4x3Transposed(final DoubleBuffer dest) {
        MemUtil.INSTANCE.put4x3Transposed(this, dest.position(), dest);
        return dest;
    }
    
    public DoubleBuffer get4x3Transposed(final int index, final DoubleBuffer dest) {
        MemUtil.INSTANCE.put4x3Transposed(this, index, dest);
        return dest;
    }
    
    public ByteBuffer get4x3Transposed(final ByteBuffer dest) {
        MemUtil.INSTANCE.put4x3Transposed(this, dest.position(), dest);
        return dest;
    }
    
    public ByteBuffer get4x3Transposed(final int index, final ByteBuffer dest) {
        MemUtil.INSTANCE.put4x3Transposed(this, index, dest);
        return dest;
    }
    
    public Matrix4d zero() {
        return this._m00(0.0)._m01(0.0)._m02(0.0)._m03(0.0)._m10(0.0)._m11(0.0)._m12(0.0)._m13(0.0)._m20(0.0)._m21(0.0)._m22(0.0)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(0.0)._properties(0);
    }
    
    public Matrix4d scaling(final double factor) {
        return this.scaling(factor, factor, factor);
    }
    
    public Matrix4d scaling(final double x, final double y, final double z) {
        if ((this.properties & 0x4) == 0x0) {
            this.identity();
        }
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        this._m00(x)._m11(y)._m22(z).properties = (0x2 | (one ? 16 : 0));
        return this;
    }
    
    public Matrix4d scaling(final Vector3dc xyz) {
        return this.scaling(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Matrix4d rotation(final double angle, final double x, final double y, final double z) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotationX(x * angle);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotationY(y * angle);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotationZ(z * angle);
        }
        return this.rotationInternal(angle, x, y, z);
    }
    
    private Matrix4d rotationInternal(final double angle, final double x, final double y, final double z) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double C = 1.0 - cos;
        final double xy = x * y;
        final double xz = x * z;
        final double yz = y * z;
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(cos + x * x * C)._m10(xy * C - z * sin)._m20(xz * C + y * sin)._m01(xy * C + z * sin)._m11(cos + y * y * C)._m21(yz * C - x * sin)._m02(xz * C - y * sin)._m12(yz * C + x * sin)._m22(cos + z * z * C).properties = 18;
        return this;
    }
    
    public Matrix4d rotationX(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m11(cos)._m12(sin)._m21(-sin)._m22(cos).properties = 18;
        return this;
    }
    
    public Matrix4d rotationY(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(cos)._m02(-sin)._m20(sin)._m22(cos).properties = 18;
        return this;
    }
    
    public Matrix4d rotationZ(final double ang) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(cos)._m01(sin)._m10(-sin)._m11(cos).properties = 18;
        return this;
    }
    
    public Matrix4d rotationTowardsXY(final double dirX, final double dirY) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this.m00 = dirY;
        this.m01 = dirX;
        this.m10 = -dirX;
        this.m11 = dirY;
        this.properties = 18;
        return this;
    }
    
    public Matrix4d rotationXYZ(final double angleX, final double angleY, final double angleZ) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinX = -sinX;
        final double m_sinY = -sinY;
        final double m_sinZ = -sinZ;
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        final double nm11 = cosX;
        final double nm12 = sinX;
        final double nm13 = m_sinX;
        final double nm14 = cosX;
        final double nm15 = cosY;
        final double nm16 = nm13 * m_sinY;
        final double nm17 = nm14 * m_sinY;
        this._m20(sinY)._m21(nm13 * cosY)._m22(nm14 * cosY)._m00(nm15 * cosZ)._m01(nm16 * cosZ + nm11 * sinZ)._m02(nm17 * cosZ + nm12 * sinZ)._m10(nm15 * m_sinZ)._m11(nm16 * m_sinZ + nm11 * cosZ)._m12(nm17 * m_sinZ + nm12 * cosZ).properties = 18;
        return this;
    }
    
    public Matrix4d rotationZYX(final double angleZ, final double angleY, final double angleX) {
        final double sinX = Math.sin(angleX);
        final double cosX = Math.cosFromSin(sinX, angleX);
        final double sinY = Math.sin(angleY);
        final double cosY = Math.cosFromSin(sinY, angleY);
        final double sinZ = Math.sin(angleZ);
        final double cosZ = Math.cosFromSin(sinZ, angleZ);
        final double m_sinZ = -sinZ;
        final double m_sinY = -sinY;
        final double m_sinX = -sinX;
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        final double nm00 = cosZ;
        final double nm2 = sinZ;
        final double nm3 = m_sinZ;
        final double nm4 = cosZ;
        final double nm5 = nm00 * sinY;
        final double nm6 = nm2 * sinY;
        final double nm7 = cosY;
        this._m00(nm00 * cosY)._m01(nm2 * cosY)._m02(m_sinY)._m10(nm3 * cosX + nm5 * sinX)._m11(nm4 * cosX + nm6 * sinX)._m12(nm7 * sinX)._m20(nm3 * m_sinX + nm5 * cosX)._m21(nm4 * m_sinX + nm6 * cosX)._m22(nm7 * cosX).properties = 18;
        return this;
    }
    
    public Matrix4d rotationYXZ(final double angleY, final double angleX, final double angleZ) {
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
        this._m20(nm3 * cosX)._m21(m_sinX)._m22(nm4 * cosX)._m23(0.0)._m00(nm00 * cosZ + nm5 * sinZ)._m01(nm6 * sinZ)._m02(nm2 * cosZ + nm7 * sinZ)._m03(0.0)._m10(nm00 * m_sinZ + nm5 * cosZ)._m11(nm6 * cosZ)._m12(nm2 * m_sinZ + nm7 * cosZ)._m13(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d setRotationXYZ(final double angleX, final double angleY, final double angleZ) {
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
        final Matrix4d m12 = this._m20(sinY)._m21(nm13 * cosY)._m22(nm14 * cosY)._m00(nm15 * cosZ)._m01(nm16 * cosZ + nm11 * sinZ)._m02(nm17 * cosZ + nm12 * sinZ)._m10(nm15 * m_sinZ)._m11(nm16 * m_sinZ + nm11 * cosZ)._m12(nm17 * m_sinZ + nm12 * cosZ);
        m12.properties &= 0xFFFFFFF2;
        return this;
    }
    
    public Matrix4d setRotationZYX(final double angleZ, final double angleY, final double angleX) {
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
        final Matrix4d m22 = this._m00(nm00 * cosY)._m01(nm2 * cosY)._m02(m_sinY)._m10(nm3 * cosX + nm5 * sinX)._m11(nm4 * cosX + nm6 * sinX)._m12(nm7 * sinX)._m20(nm3 * m_sinX + nm5 * cosX)._m21(nm4 * m_sinX + nm6 * cosX)._m22(nm7 * cosX);
        m22.properties &= 0xFFFFFFF2;
        return this;
    }
    
    public Matrix4d setRotationYXZ(final double angleY, final double angleX, final double angleZ) {
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
        final Matrix4d m12 = this._m20(nm3 * cosX)._m21(m_sinX)._m22(nm4 * cosX)._m00(nm00 * cosZ + nm5 * sinZ)._m01(nm6 * sinZ)._m02(nm2 * cosZ + nm7 * sinZ)._m10(nm00 * m_sinZ + nm5 * cosZ)._m11(nm6 * cosZ)._m12(nm2 * m_sinZ + nm7 * cosZ);
        m12.properties &= 0xFFFFFFF2;
        return this;
    }
    
    public Matrix4d rotation(final double angle, final Vector3dc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4d rotation(final double angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Vector4d transform(final Vector4d v) {
        return v.mul(this);
    }
    
    public Vector4d transform(final Vector4dc v, final Vector4d dest) {
        return v.mul(this, dest);
    }
    
    public Vector4d transform(final double x, final double y, final double z, final double w, final Vector4d dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20 * z + this.m30 * w, this.m01 * x + this.m11 * y + this.m21 * z + this.m31 * w, this.m02 * x + this.m12 * y + this.m22 * z + this.m32 * w, this.m03 * x + this.m13 * y + this.m23 * z + this.m33 * w);
    }
    
    public Vector4d transformTranspose(final Vector4d v) {
        return v.mulTranspose(this);
    }
    
    public Vector4d transformTranspose(final Vector4dc v, final Vector4d dest) {
        return v.mulTranspose(this, dest);
    }
    
    public Vector4d transformTranspose(final double x, final double y, final double z, final double w, final Vector4d dest) {
        return dest.set(x, y, z, w).mulTranspose(this);
    }
    
    public Vector4d transformProject(final Vector4d v) {
        return v.mulProject(this);
    }
    
    public Vector4d transformProject(final Vector4dc v, final Vector4d dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector4d transformProject(final double x, final double y, final double z, final double w, final Vector4d dest) {
        final double invW = 1.0 / (this.m03 * x + this.m13 * y + this.m23 * z + this.m33 * w);
        return dest.set((this.m00 * x + this.m10 * y + this.m20 * z + this.m30 * w) * invW, (this.m01 * x + this.m11 * y + this.m21 * z + this.m31 * w) * invW, (this.m02 * x + this.m12 * y + this.m22 * z + this.m32 * w) * invW, 1.0);
    }
    
    public Vector3d transformProject(final Vector3d v) {
        return v.mulProject(this);
    }
    
    public Vector3d transformProject(final Vector3dc v, final Vector3d dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector3d transformProject(final double x, final double y, final double z, final Vector3d dest) {
        final double invW = 1.0 / (this.m03 * x + this.m13 * y + this.m23 * z + this.m33);
        return dest.set((this.m00 * x + this.m10 * y + this.m20 * z + this.m30) * invW, (this.m01 * x + this.m11 * y + this.m21 * z + this.m31) * invW, (this.m02 * x + this.m12 * y + this.m22 * z + this.m32) * invW);
    }
    
    public Vector3d transformProject(final Vector4dc v, final Vector3d dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector3d transformProject(final double x, final double y, final double z, final double w, final Vector3d dest) {
        dest.x = x;
        dest.y = y;
        dest.z = z;
        return dest.mulProject(this, w, dest);
    }
    
    public Vector3d transformPosition(final Vector3d dest) {
        return dest.set(this.m00 * dest.x + this.m10 * dest.y + this.m20 * dest.z + this.m30, this.m01 * dest.x + this.m11 * dest.y + this.m21 * dest.z + this.m31, this.m02 * dest.x + this.m12 * dest.y + this.m22 * dest.z + this.m32);
    }
    
    public Vector3d transformPosition(final Vector3dc v, final Vector3d dest) {
        return this.transformPosition(v.x(), v.y(), v.z(), dest);
    }
    
    public Vector3d transformPosition(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20 * z + this.m30, this.m01 * x + this.m11 * y + this.m21 * z + this.m31, this.m02 * x + this.m12 * y + this.m22 * z + this.m32);
    }
    
    public Vector3d transformDirection(final Vector3d dest) {
        return dest.set(this.m00 * dest.x + this.m10 * dest.y + this.m20 * dest.z, this.m01 * dest.x + this.m11 * dest.y + this.m21 * dest.z, this.m02 * dest.x + this.m12 * dest.y + this.m22 * dest.z);
    }
    
    public Vector3d transformDirection(final Vector3dc v, final Vector3d dest) {
        return dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20 * v.z(), this.m01 * v.x() + this.m11 * v.y() + this.m21 * v.z(), this.m02 * v.x() + this.m12 * v.y() + this.m22 * v.z());
    }
    
    public Vector3d transformDirection(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(this.m00 * x + this.m10 * y + this.m20 * z, this.m01 * x + this.m11 * y + this.m21 * z, this.m02 * x + this.m12 * y + this.m22 * z);
    }
    
    public Vector3f transformDirection(final Vector3f dest) {
        return dest.mulDirection(this);
    }
    
    public Vector3f transformDirection(final Vector3fc v, final Vector3f dest) {
        return v.mulDirection(this, dest);
    }
    
    public Vector3f transformDirection(final double x, final double y, final double z, final Vector3f dest) {
        final float rx = (float)(this.m00 * x + this.m10 * y + this.m20 * z);
        final float ry = (float)(this.m01 * x + this.m11 * y + this.m21 * z);
        final float rz = (float)(this.m02 * x + this.m12 * y + this.m22 * z);
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector4d transformAffine(final Vector4d dest) {
        return dest.mulAffine(this, dest);
    }
    
    public Vector4d transformAffine(final Vector4dc v, final Vector4d dest) {
        return this.transformAffine(v.x(), v.y(), v.z(), v.w(), dest);
    }
    
    public Vector4d transformAffine(final double x, final double y, final double z, final double w, final Vector4d dest) {
        final double rx = this.m00 * x + this.m10 * y + this.m20 * z + this.m30 * w;
        final double ry = this.m01 * x + this.m11 * y + this.m21 * z + this.m31 * w;
        final double rz = this.m02 * x + this.m12 * y + this.m22 * z + this.m32 * w;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = w;
        return dest;
    }
    
    public Matrix4d set3x3(final Matrix3dc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4d scale(final Vector3dc xyz, final Matrix4d dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix4d scale(final Vector3dc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix4d scale(final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleGeneric(x, y, z, dest);
    }
    
    private Matrix4d scaleGeneric(final double x, final double y, final double z, final Matrix4d dest) {
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        dest._m00(this.m00 * x)._m01(this.m01 * x)._m02(this.m02 * x)._m03(this.m03 * x)._m10(this.m10 * y)._m11(this.m11 * y)._m12(this.m12 * y)._m13(this.m13 * y)._m20(this.m20 * z)._m21(this.m21 * z)._m22(this.m22 * z)._m23(this.m23 * z)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & ~(0xD | (one ? 0 : 16)));
        return dest;
    }
    
    public Matrix4d scale(final double x, final double y, final double z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix4d scale(final double xyz, final Matrix4d dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix4d scale(final double xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix4d scaleXY(final double x, final double y, final Matrix4d dest) {
        return this.scale(x, y, 1.0, dest);
    }
    
    public Matrix4d scaleXY(final double x, final double y) {
        return this.scale(x, y, 1.0);
    }
    
    public Matrix4d scaleAround(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz, final Matrix4d dest) {
        final double nm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final double nm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final double nm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final double nm33 = this.m03 * ox + this.m13 * oy + this.m23 * oz + this.m33;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return dest._m00(this.m00 * sx)._m01(this.m01 * sx)._m02(this.m02 * sx)._m03(this.m03 * sx)._m10(this.m10 * sy)._m11(this.m11 * sy)._m12(this.m12 * sy)._m13(this.m13 * sy)._m20(this.m20 * sz)._m21(this.m21 * sz)._m22(this.m22 * sz)._m23(this.m23 * sz)._m30(-dest.m00 * ox - dest.m10 * oy - dest.m20 * oz + nm30)._m31(-dest.m01 * ox - dest.m11 * oy - dest.m21 * oz + nm31)._m32(-dest.m02 * ox - dest.m12 * oy - dest.m22 * oz + nm32)._m33(-dest.m03 * ox - dest.m13 * oy - dest.m23 * oz + nm33)._properties(this.properties & ~(0xD | (one ? 0 : 16)));
    }
    
    public Matrix4d scaleAround(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz) {
        return this.scaleAround(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4d scaleAround(final double factor, final double ox, final double oy, final double oz) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4d scaleAround(final double factor, final double ox, final double oy, final double oz, final Matrix4d dest) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4d scaleLocal(final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleLocalGeneric(x, y, z, dest);
    }
    
    private Matrix4d scaleLocalGeneric(final double x, final double y, final double z, final Matrix4d dest) {
        final double nm00 = x * this.m00;
        final double nm2 = y * this.m01;
        final double nm3 = z * this.m02;
        final double nm4 = x * this.m10;
        final double nm5 = y * this.m11;
        final double nm6 = z * this.m12;
        final double nm7 = x * this.m20;
        final double nm8 = y * this.m21;
        final double nm9 = z * this.m22;
        final double nm10 = x * this.m30;
        final double nm11 = y * this.m31;
        final double nm12 = z * this.m32;
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03)._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13)._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23)._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33)._properties(this.properties & ~(0xD | (one ? 0 : 16)));
        return dest;
    }
    
    public Matrix4d scaleLocal(final double xyz, final Matrix4d dest) {
        return this.scaleLocal(xyz, xyz, xyz, dest);
    }
    
    public Matrix4d scaleLocal(final double xyz) {
        return this.scaleLocal(xyz, this);
    }
    
    public Matrix4d scaleLocal(final double x, final double y, final double z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix4d scaleAroundLocal(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz, final Matrix4d dest) {
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        dest._m00(sx * (this.m00 - ox * this.m03) + ox * this.m03)._m01(sy * (this.m01 - oy * this.m03) + oy * this.m03)._m02(sz * (this.m02 - oz * this.m03) + oz * this.m03)._m03(this.m03)._m10(sx * (this.m10 - ox * this.m13) + ox * this.m13)._m11(sy * (this.m11 - oy * this.m13) + oy * this.m13)._m12(sz * (this.m12 - oz * this.m13) + oz * this.m13)._m13(this.m13)._m20(sx * (this.m20 - ox * this.m23) + ox * this.m23)._m21(sy * (this.m21 - oy * this.m23) + oy * this.m23)._m22(sz * (this.m22 - oz * this.m23) + oz * this.m23)._m23(this.m23)._m30(sx * (this.m30 - ox * this.m33) + ox * this.m33)._m31(sy * (this.m31 - oy * this.m33) + oy * this.m33)._m32(sz * (this.m32 - oz * this.m33) + oz * this.m33)._m33(this.m33)._properties(this.properties & ~(0xD | (one ? 0 : 16)));
        return dest;
    }
    
    public Matrix4d scaleAroundLocal(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz) {
        return this.scaleAroundLocal(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4d scaleAroundLocal(final double factor, final double ox, final double oy, final double oz) {
        return this.scaleAroundLocal(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4d scaleAroundLocal(final double factor, final double ox, final double oy, final double oz, final Matrix4d dest) {
        return this.scaleAroundLocal(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4d rotate(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(ang, x, y, z);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(ang, x, y, z, dest);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.rotateAffine(ang, x, y, z, dest);
        }
        return this.rotateGeneric(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateGeneric(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotateX(x * ang, dest);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotateY(y * ang, dest);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotateZ(z * ang, dest);
        }
        return this.rotateGenericInternal(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateGenericInternal(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
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
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotate(final double ang, final double x, final double y, final double z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix4d rotateTranslation(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        final double tx = this.m30;
        final double ty = this.m31;
        final double tz = this.m32;
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return dest.rotationX(x * ang).setTranslation(tx, ty, tz);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return dest.rotationY(y * ang).setTranslation(tx, ty, tz);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return dest.rotationZ(z * ang).setTranslation(tx, ty, tz);
        }
        return this.rotateTranslationInternal(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateTranslationInternal(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
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
        return dest._m20(rm7)._m21(rm8)._m22(rm9)._m23(0.0)._m00(rm00)._m01(rm2)._m02(rm3)._m03(0.0)._m10(rm4)._m11(rm5)._m12(rm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(1.0)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4d rotateAffine(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotateX(x * ang, dest);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotateY(y * ang, dest);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotateZ(z * ang, dest);
        }
        return this.rotateAffineInternal(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateAffineInternal(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
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
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffine(final double ang, final double x, final double y, final double z) {
        return this.rotateAffine(ang, x, y, z, this);
    }
    
    public Matrix4d rotateAround(final Quaterniondc quat, final double ox, final double oy, final double oz) {
        return this.rotateAround(quat, ox, oy, oz, this);
    }
    
    public Matrix4d rotateAroundAffine(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4d dest) {
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
        final double rm4 = -dzw + dxy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double tm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final double tm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final double tm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m30(-nm00 * ox - nm4 * oy - this.m20 * oz + tm30)._m31(-nm2 * ox - nm5 * oy - this.m21 * oz + tm31)._m32(-nm3 * ox - nm6 * oy - this.m22 * oz + tm32)._m33(1.0)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAround(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.rotationAround(quat, ox, oy, oz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.rotateAroundAffine(quat, ox, oy, oz, this);
        }
        return this.rotateAroundGeneric(quat, ox, oy, oz, this);
    }
    
    private Matrix4d rotateAroundGeneric(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4d dest) {
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
        final double rm4 = -dzw + dxy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double tm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final double tm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final double tm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(-nm00 * ox - nm5 * oy - this.m20 * oz + tm30)._m31(-nm2 * ox - nm6 * oy - this.m21 * oz + tm31)._m32(-nm3 * ox - nm7 * oy - this.m22 * oz + tm32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotationAround(final Quaterniondc quat, final double ox, final double oy, final double oz) {
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
        this._m20(dyw + dxz);
        this._m21(dyz - dxw);
        this._m22(z2 - y2 - x2 + w2);
        this._m23(0.0);
        this._m00(w2 + x2 - z2 - y2);
        this._m01(dxy + dzw);
        this._m02(dxz - dyw);
        this._m03(0.0);
        this._m10(-dzw + dxy);
        this._m11(y2 - z2 + w2 - x2);
        this._m12(dyz + dxw);
        this._m13(0.0);
        this._m30(-this.m00 * ox - this.m10 * oy - this.m20 * oz + ox);
        this._m31(-this.m01 * ox - this.m11 * oy - this.m21 * oz + oy);
        this._m32(-this.m02 * ox - this.m12 * oy - this.m22 * oz + oz);
        this._m33(1.0);
        this.properties = 18;
        return this;
    }
    
    public Matrix4d rotateLocal(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(ang, x, y, z);
        }
        return this.rotateLocalGeneric(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateLocalGeneric(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotateLocalX(x * ang, dest);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotateLocalY(y * ang, dest);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotateLocalZ(z * ang, dest);
        }
        return this.rotateLocalGenericInternal(ang, x, y, z, dest);
    }
    
    private Matrix4d rotateLocalGenericInternal(final double ang, final double x, final double y, final double z, final Matrix4d dest) {
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
        final double nm10 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final double nm11 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final double nm12 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03)._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13)._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23)._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocal(final double ang, final double x, final double y, final double z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix4d rotateAroundLocal(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double xy = quat.x() * quat.y();
        final double xz = quat.x() * quat.z();
        final double yw = quat.y() * quat.w();
        final double yz = quat.y() * quat.z();
        final double xw = quat.x() * quat.w();
        final double lm00 = w2 + x2 - z2 - y2;
        final double lm2 = xy + zw + zw + xy;
        final double lm3 = xz - yw + xz - yw;
        final double lm4 = -zw + xy - zw + xy;
        final double lm5 = y2 - z2 + w2 - x2;
        final double lm6 = yz + yz + xw + xw;
        final double lm7 = yw + xz + xz + yw;
        final double lm8 = yz + yz - xw - xw;
        final double lm9 = z2 - y2 - x2 + w2;
        final double tm00 = this.m00 - ox * this.m03;
        final double tm2 = this.m01 - oy * this.m03;
        final double tm3 = this.m02 - oz * this.m03;
        final double tm4 = this.m10 - ox * this.m13;
        final double tm5 = this.m11 - oy * this.m13;
        final double tm6 = this.m12 - oz * this.m13;
        final double tm7 = this.m20 - ox * this.m23;
        final double tm8 = this.m21 - oy * this.m23;
        final double tm9 = this.m22 - oz * this.m23;
        final double tm10 = this.m30 - ox * this.m33;
        final double tm11 = this.m31 - oy * this.m33;
        final double tm12 = this.m32 - oz * this.m33;
        dest._m00(lm00 * tm00 + lm4 * tm2 + lm7 * tm3 + ox * this.m03)._m01(lm2 * tm00 + lm5 * tm2 + lm8 * tm3 + oy * this.m03)._m02(lm3 * tm00 + lm6 * tm2 + lm9 * tm3 + oz * this.m03)._m03(this.m03)._m10(lm00 * tm4 + lm4 * tm5 + lm7 * tm6 + ox * this.m13)._m11(lm2 * tm4 + lm5 * tm5 + lm8 * tm6 + oy * this.m13)._m12(lm3 * tm4 + lm6 * tm5 + lm9 * tm6 + oz * this.m13)._m13(this.m13)._m20(lm00 * tm7 + lm4 * tm8 + lm7 * tm9 + ox * this.m23)._m21(lm2 * tm7 + lm5 * tm8 + lm8 * tm9 + oy * this.m23)._m22(lm3 * tm7 + lm6 * tm8 + lm9 * tm9 + oz * this.m23)._m23(this.m23)._m30(lm00 * tm10 + lm4 * tm11 + lm7 * tm12 + ox * this.m33)._m31(lm2 * tm10 + lm5 * tm11 + lm8 * tm12 + oy * this.m33)._m32(lm3 * tm10 + lm6 * tm11 + lm9 * tm12 + oz * this.m33)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAroundLocal(final Quaterniondc quat, final double ox, final double oy, final double oz) {
        return this.rotateAroundLocal(quat, ox, oy, oz, this);
    }
    
    public Matrix4d translate(final Vector3dc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d translate(final Vector3dc offset, final Matrix4d dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4d translate(final Vector3fc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d translate(final Vector3fc offset, final Matrix4d dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4d translate(final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateGeneric(x, y, z, dest);
    }
    
    private Matrix4d translateGeneric(final double x, final double y, final double z, final Matrix4d dest) {
        dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(Math.fma(this.m00, x, Math.fma(this.m10, y, Math.fma(this.m20, z, this.m30))))._m31(Math.fma(this.m01, x, Math.fma(this.m11, y, Math.fma(this.m21, z, this.m31))))._m32(Math.fma(this.m02, x, Math.fma(this.m12, y, Math.fma(this.m22, z, this.m32))))._m33(Math.fma(this.m03, x, Math.fma(this.m13, y, Math.fma(this.m23, z, this.m33))))._properties(this.properties & 0xFFFFFFFA);
        return dest;
    }
    
    public Matrix4d translate(final double x, final double y, final double z) {
        if ((this.properties & 0x4) != 0x0) {
            return this.translation(x, y, z);
        }
        this._m30(Math.fma(this.m00, x, Math.fma(this.m10, y, Math.fma(this.m20, z, this.m30))));
        this._m31(Math.fma(this.m01, x, Math.fma(this.m11, y, Math.fma(this.m21, z, this.m31))));
        this._m32(Math.fma(this.m02, x, Math.fma(this.m12, y, Math.fma(this.m22, z, this.m32))));
        this._m33(Math.fma(this.m03, x, Math.fma(this.m13, y, Math.fma(this.m23, z, this.m33))));
        this.properties &= 0xFFFFFFFA;
        return this;
    }
    
    public Matrix4d translateLocal(final Vector3fc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d translateLocal(final Vector3fc offset, final Matrix4d dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4d translateLocal(final Vector3dc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4d translateLocal(final Vector3dc offset, final Matrix4d dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4d translateLocal(final double x, final double y, final double z, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateLocalGeneric(x, y, z, dest);
    }
    
    private Matrix4d translateLocalGeneric(final double x, final double y, final double z, final Matrix4d dest) {
        final double nm00 = this.m00 + x * this.m03;
        final double nm2 = this.m01 + y * this.m03;
        final double nm3 = this.m02 + z * this.m03;
        final double nm4 = this.m10 + x * this.m13;
        final double nm5 = this.m11 + y * this.m13;
        final double nm6 = this.m12 + z * this.m13;
        final double nm7 = this.m20 + x * this.m23;
        final double nm8 = this.m21 + y * this.m23;
        final double nm9 = this.m22 + z * this.m23;
        final double nm10 = this.m30 + x * this.m33;
        final double nm11 = this.m31 + y * this.m33;
        final double nm12 = this.m32 + z * this.m33;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03)._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13)._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23)._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33)._properties(this.properties & 0xFFFFFFFA);
    }
    
    public Matrix4d translateLocal(final double x, final double y, final double z) {
        return this.translateLocal(x, y, z, this);
    }
    
    public Matrix4d rotateLocalX(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm02 = sin * this.m01 + cos * this.m02;
        final double nm3 = sin * this.m11 + cos * this.m12;
        final double nm4 = sin * this.m21 + cos * this.m22;
        final double nm5 = sin * this.m31 + cos * this.m32;
        dest._m00(this.m00)._m01(cos * this.m01 - sin * this.m02)._m02(nm02)._m03(this.m03)._m10(this.m10)._m11(cos * this.m11 - sin * this.m12)._m12(nm3)._m13(this.m13)._m20(this.m20)._m21(cos * this.m21 - sin * this.m22)._m22(nm4)._m23(this.m23)._m30(this.m30)._m31(cos * this.m31 - sin * this.m32)._m32(nm5)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocalX(final double ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix4d rotateLocalY(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm02 = -sin * this.m00 + cos * this.m02;
        final double nm3 = -sin * this.m10 + cos * this.m12;
        final double nm4 = -sin * this.m20 + cos * this.m22;
        final double nm5 = -sin * this.m30 + cos * this.m32;
        dest._m00(cos * this.m00 + sin * this.m02)._m01(this.m01)._m02(nm02)._m03(this.m03)._m10(cos * this.m10 + sin * this.m12)._m11(this.m11)._m12(nm3)._m13(this.m13)._m20(cos * this.m20 + sin * this.m22)._m21(this.m21)._m22(nm4)._m23(this.m23)._m30(cos * this.m30 + sin * this.m32)._m31(this.m31)._m32(nm5)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocalY(final double ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix4d rotateLocalZ(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm01 = sin * this.m00 + cos * this.m01;
        final double nm2 = sin * this.m10 + cos * this.m11;
        final double nm3 = sin * this.m20 + cos * this.m21;
        final double nm4 = sin * this.m30 + cos * this.m31;
        dest._m00(cos * this.m00 - sin * this.m01)._m01(nm01)._m02(this.m02)._m03(this.m03)._m10(cos * this.m10 - sin * this.m11)._m11(nm2)._m12(this.m12)._m13(this.m13)._m20(cos * this.m20 - sin * this.m21)._m21(nm3)._m22(this.m22)._m23(this.m23)._m30(cos * this.m30 - sin * this.m31)._m31(nm4)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocalZ(final double ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.m00);
        out.writeDouble(this.m01);
        out.writeDouble(this.m02);
        out.writeDouble(this.m03);
        out.writeDouble(this.m10);
        out.writeDouble(this.m11);
        out.writeDouble(this.m12);
        out.writeDouble(this.m13);
        out.writeDouble(this.m20);
        out.writeDouble(this.m21);
        out.writeDouble(this.m22);
        out.writeDouble(this.m23);
        out.writeDouble(this.m30);
        out.writeDouble(this.m31);
        out.writeDouble(this.m32);
        out.writeDouble(this.m33);
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this._m00(in.readDouble())._m01(in.readDouble())._m02(in.readDouble())._m03(in.readDouble())._m10(in.readDouble())._m11(in.readDouble())._m12(in.readDouble())._m13(in.readDouble())._m20(in.readDouble())._m21(in.readDouble())._m22(in.readDouble())._m23(in.readDouble())._m30(in.readDouble())._m31(in.readDouble())._m32(in.readDouble())._m33(in.readDouble()).determineProperties();
    }
    
    public Matrix4d rotateX(final double ang, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationX(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double x = this.m30;
            final double y = this.m31;
            final double z = this.m32;
            return dest.rotationX(ang).setTranslation(x, y, z);
        }
        return this.rotateXInternal(ang, dest);
    }
    
    private Matrix4d rotateXInternal(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double rm11;
        final double cos = rm11 = Math.cosFromSin(sin, ang);
        final double rm12 = sin;
        final double rm13 = -sin;
        final double rm14 = cos;
        final double nm10 = this.m10 * rm11 + this.m20 * rm12;
        final double nm11 = this.m11 * rm11 + this.m21 * rm12;
        final double nm12 = this.m12 * rm11 + this.m22 * rm12;
        final double nm13 = this.m13 * rm11 + this.m23 * rm12;
        dest._m20(this.m10 * rm13 + this.m20 * rm14)._m21(this.m11 * rm13 + this.m21 * rm14)._m22(this.m12 * rm13 + this.m22 * rm14)._m23(this.m13 * rm13 + this.m23 * rm14)._m10(nm10)._m11(nm11)._m12(nm12)._m13(nm13)._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateX(final double ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix4d rotateY(final double ang, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationY(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double x = this.m30;
            final double y = this.m31;
            final double z = this.m32;
            return dest.rotationY(ang).setTranslation(x, y, z);
        }
        return this.rotateYInternal(ang, dest);
    }
    
    private Matrix4d rotateYInternal(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double rm00;
        final double cos = rm00 = Math.cosFromSin(sin, ang);
        final double rm2 = -sin;
        final double rm3 = sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m20 * rm2;
        final double nm2 = this.m01 * rm00 + this.m21 * rm2;
        final double nm3 = this.m02 * rm00 + this.m22 * rm2;
        final double nm4 = this.m03 * rm00 + this.m23 * rm2;
        dest._m20(this.m00 * rm3 + this.m20 * rm4)._m21(this.m01 * rm3 + this.m21 * rm4)._m22(this.m02 * rm3 + this.m22 * rm4)._m23(this.m03 * rm3 + this.m23 * rm4)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateY(final double ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix4d rotateZ(final double ang, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZ(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double x = this.m30;
            final double y = this.m31;
            final double z = this.m32;
            return dest.rotationZ(ang).setTranslation(x, y, z);
        }
        return this.rotateZInternal(ang, dest);
    }
    
    private Matrix4d rotateZInternal(final double ang, final Matrix4d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        return this.rotateTowardsXY(sin, cos, dest);
    }
    
    public Matrix4d rotateZ(final double ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix4d rotateTowardsXY(final double dirX, final double dirY) {
        return this.rotateTowardsXY(dirX, dirY, this);
    }
    
    public Matrix4d rotateTowardsXY(final double dirX, final double dirY, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationTowardsXY(dirX, dirY);
        }
        final double rm00 = dirY;
        final double rm2 = dirX;
        final double rm3 = -dirX;
        final double rm4 = dirY;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2;
        dest._m10(this.m00 * rm3 + this.m10 * rm4)._m11(this.m01 * rm3 + this.m11 * rm4)._m12(this.m02 * rm3 + this.m12 * rm4)._m13(this.m03 * rm3 + this.m13 * rm4)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateXYZ(final Vector3d angles) {
        return this.rotateXYZ(angles.x, angles.y, angles.z);
    }
    
    public Matrix4d rotateXYZ(final double angleX, final double angleY, final double angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4d rotateXYZ(final double angleX, final double angleY, final double angleZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineXYZ(angleX, angleY, angleZ);
        }
        return this.rotateXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4d rotateXYZInternal(final double angleX, final double angleY, final double angleZ, final Matrix4d dest) {
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
        final double nm13 = this.m13 * cosX + this.m23 * sinX;
        final double nm14 = this.m10 * m_sinX + this.m20 * cosX;
        final double nm15 = this.m11 * m_sinX + this.m21 * cosX;
        final double nm16 = this.m12 * m_sinX + this.m22 * cosX;
        final double nm17 = this.m13 * m_sinX + this.m23 * cosX;
        final double nm18 = this.m00 * cosY + nm14 * m_sinY;
        final double nm19 = this.m01 * cosY + nm15 * m_sinY;
        final double nm20 = this.m02 * cosY + nm16 * m_sinY;
        final double nm21 = this.m03 * cosY + nm17 * m_sinY;
        dest._m20(this.m00 * sinY + nm14 * cosY)._m21(this.m01 * sinY + nm15 * cosY)._m22(this.m02 * sinY + nm16 * cosY)._m23(this.m03 * sinY + nm17 * cosY)._m00(nm18 * cosZ + nm10 * sinZ)._m01(nm19 * cosZ + nm11 * sinZ)._m02(nm20 * cosZ + nm12 * sinZ)._m03(nm21 * cosZ + nm13 * sinZ)._m10(nm18 * m_sinZ + nm10 * cosZ)._m11(nm19 * m_sinZ + nm11 * cosZ)._m12(nm20 * m_sinZ + nm12 * cosZ)._m13(nm21 * m_sinZ + nm13 * cosZ)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffineXYZ(final double angleX, final double angleY, final double angleZ) {
        return this.rotateAffineXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4d rotateAffineXYZ(final double angleX, final double angleY, final double angleZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateAffineXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4d rotateAffineXYZInternal(final double angleX, final double angleY, final double angleZ, final Matrix4d dest) {
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
        dest._m20(this.m00 * sinY + nm13 * cosY)._m21(this.m01 * sinY + nm14 * cosY)._m22(this.m02 * sinY + nm15 * cosY)._m23(0.0)._m00(nm16 * cosZ + nm10 * sinZ)._m01(nm17 * cosZ + nm11 * sinZ)._m02(nm18 * cosZ + nm12 * sinZ)._m03(0.0)._m10(nm16 * m_sinZ + nm10 * cosZ)._m11(nm17 * m_sinZ + nm11 * cosZ)._m12(nm18 * m_sinZ + nm12 * cosZ)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateZYX(final Vector3d angles) {
        return this.rotateZYX(angles.z, angles.y, angles.x);
    }
    
    public Matrix4d rotateZYX(final double angleZ, final double angleY, final double angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4d rotateZYX(final double angleZ, final double angleY, final double angleX, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZYX(angleZ, angleY, angleX);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationZYX(angleZ, angleY, angleX).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineZYX(angleZ, angleY, angleX);
        }
        return this.rotateZYXInternal(angleZ, angleY, angleX, dest);
    }
    
    private Matrix4d rotateZYXInternal(final double angleZ, final double angleY, final double angleX, final Matrix4d dest) {
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
        final double nm4 = this.m03 * cosZ + this.m13 * sinZ;
        final double nm5 = this.m00 * m_sinZ + this.m10 * cosZ;
        final double nm6 = this.m01 * m_sinZ + this.m11 * cosZ;
        final double nm7 = this.m02 * m_sinZ + this.m12 * cosZ;
        final double nm8 = this.m03 * m_sinZ + this.m13 * cosZ;
        final double nm9 = nm00 * sinY + this.m20 * cosY;
        final double nm10 = nm2 * sinY + this.m21 * cosY;
        final double nm11 = nm3 * sinY + this.m22 * cosY;
        final double nm12 = nm4 * sinY + this.m23 * cosY;
        dest._m00(nm00 * cosY + this.m20 * m_sinY)._m01(nm2 * cosY + this.m21 * m_sinY)._m02(nm3 * cosY + this.m22 * m_sinY)._m03(nm4 * cosY + this.m23 * m_sinY)._m10(nm5 * cosX + nm9 * sinX)._m11(nm6 * cosX + nm10 * sinX)._m12(nm7 * cosX + nm11 * sinX)._m13(nm8 * cosX + nm12 * sinX)._m20(nm5 * m_sinX + nm9 * cosX)._m21(nm6 * m_sinX + nm10 * cosX)._m22(nm7 * m_sinX + nm11 * cosX)._m23(nm8 * m_sinX + nm12 * cosX)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffineZYX(final double angleZ, final double angleY, final double angleX) {
        return this.rotateAffineZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4d rotateAffineZYX(final double angleZ, final double angleY, final double angleX, final Matrix4d dest) {
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
        dest._m00(nm00 * cosY + this.m20 * m_sinY)._m01(nm2 * cosY + this.m21 * m_sinY)._m02(nm3 * cosY + this.m22 * m_sinY)._m03(0.0)._m10(nm4 * cosX + nm7 * sinX)._m11(nm5 * cosX + nm8 * sinX)._m12(nm6 * cosX + nm9 * sinX)._m13(0.0)._m20(nm4 * m_sinX + nm7 * cosX)._m21(nm5 * m_sinX + nm8 * cosX)._m22(nm6 * m_sinX + nm9 * cosX)._m23(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateYXZ(final Vector3d angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix4d rotateYXZ(final double angleY, final double angleX, final double angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4d rotateYXZ(final double angleY, final double angleX, final double angleZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationYXZ(angleY, angleX, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationYXZ(angleY, angleX, angleZ).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineYXZ(angleY, angleX, angleZ);
        }
        return this.rotateYXZInternal(angleY, angleX, angleZ, dest);
    }
    
    private Matrix4d rotateYXZInternal(final double angleY, final double angleX, final double angleZ, final Matrix4d dest) {
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
        final double nm23 = this.m03 * sinY + this.m23 * cosY;
        final double nm24 = this.m00 * cosY + this.m20 * m_sinY;
        final double nm25 = this.m01 * cosY + this.m21 * m_sinY;
        final double nm26 = this.m02 * cosY + this.m22 * m_sinY;
        final double nm27 = this.m03 * cosY + this.m23 * m_sinY;
        final double nm28 = this.m10 * cosX + nm20 * sinX;
        final double nm29 = this.m11 * cosX + nm21 * sinX;
        final double nm30 = this.m12 * cosX + nm22 * sinX;
        final double nm31 = this.m13 * cosX + nm23 * sinX;
        dest._m20(this.m10 * m_sinX + nm20 * cosX)._m21(this.m11 * m_sinX + nm21 * cosX)._m22(this.m12 * m_sinX + nm22 * cosX)._m23(this.m13 * m_sinX + nm23 * cosX)._m00(nm24 * cosZ + nm28 * sinZ)._m01(nm25 * cosZ + nm29 * sinZ)._m02(nm26 * cosZ + nm30 * sinZ)._m03(nm27 * cosZ + nm31 * sinZ)._m10(nm24 * m_sinZ + nm28 * cosZ)._m11(nm25 * m_sinZ + nm29 * cosZ)._m12(nm26 * m_sinZ + nm30 * cosZ)._m13(nm27 * m_sinZ + nm31 * cosZ)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffineYXZ(final double angleY, final double angleX, final double angleZ) {
        return this.rotateAffineYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4d rotateAffineYXZ(final double angleY, final double angleX, final double angleZ, final Matrix4d dest) {
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
        dest._m20(this.m10 * m_sinX + nm20 * cosX)._m21(this.m11 * m_sinX + nm21 * cosX)._m22(this.m12 * m_sinX + nm22 * cosX)._m23(0.0)._m00(nm23 * cosZ + nm26 * sinZ)._m01(nm24 * cosZ + nm27 * sinZ)._m02(nm25 * cosZ + nm28 * sinZ)._m03(0.0)._m10(nm23 * m_sinZ + nm26 * cosZ)._m11(nm24 * m_sinZ + nm27 * cosZ)._m12(nm25 * m_sinZ + nm28 * cosZ)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotation(final AxisAngle4f angleAxis) {
        return this.rotation(angleAxis.angle, angleAxis.x, angleAxis.y, angleAxis.z);
    }
    
    public Matrix4d rotation(final AxisAngle4d angleAxis) {
        return this.rotation(angleAxis.angle, angleAxis.x, angleAxis.y, angleAxis.z);
    }
    
    public Matrix4d rotation(final Quaterniondc quat) {
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
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(w2 + x2 - z2 - y2)._m01(dxy + dzw)._m02(dxz - dyw)._m10(-dzw + dxy)._m11(y2 - z2 + w2 - x2)._m12(dyz + dxw)._m20(dyw + dxz)._m21(dyz - dxw)._m22(z2 - y2 - x2 + w2)._properties(18);
        return this;
    }
    
    public Matrix4d rotation(final Quaternionfc quat) {
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
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(w2 + x2 - z2 - y2)._m01(dxy + dzw)._m02(dxz - dyw)._m10(-dzw + dxy)._m11(y2 - z2 + w2 - x2)._m12(dyz + dxw)._m20(dyw + dxz)._m21(dyz - dxw)._m22(z2 - y2 - x2 + w2)._properties(18);
        return this;
    }
    
    public Matrix4d translationRotateScale(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double sx, final double sy, final double sz) {
        final double dqx = qx + qx;
        final double dqy = qy + qy;
        final double dqz = qz + qz;
        final double q00 = dqx * qx;
        final double q2 = dqy * qy;
        final double q3 = dqz * qz;
        final double q4 = dqx * qy;
        final double q5 = dqx * qz;
        final double q6 = dqx * qw;
        final double q7 = dqy * qz;
        final double q8 = dqy * qw;
        final double q9 = dqz * qw;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        this._m00(sx - (q2 + q3) * sx)._m01((q4 + q9) * sx)._m02((q5 - q8) * sx)._m03(0.0)._m10((q4 - q9) * sy)._m11(sy - (q3 + q00) * sy)._m12((q7 + q6) * sy)._m13(0.0)._m20((q5 + q8) * sz)._m21((q7 - q6) * sz)._m22(sz - (q2 + q00) * sz)._m23(0.0)._m30(tx)._m31(ty)._m32(tz)._m33(1.0).properties = (0x2 | (one ? 16 : 0));
        return this;
    }
    
    public Matrix4d translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4d translationRotateScale(final Vector3dc translation, final Quaterniondc quat, final Vector3dc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4d translationRotateScale(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double scale) {
        return this.translationRotateScale(tx, ty, tz, qx, qy, qz, qw, scale, scale, scale);
    }
    
    public Matrix4d translationRotateScale(final Vector3dc translation, final Quaterniondc quat, final double scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4d translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final double scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4d translationRotateScaleInvert(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double sx, final double sy, final double sz) {
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        if (one) {
            return this.translationRotateInvert(tx, ty, tz, qx, qy, qz, qw);
        }
        final double nqx = -qx;
        final double nqy = -qy;
        final double nqz = -qz;
        final double dqx = nqx + nqx;
        final double dqy = nqy + nqy;
        final double dqz = nqz + nqz;
        final double q00 = dqx * nqx;
        final double q2 = dqy * nqy;
        final double q3 = dqz * nqz;
        final double q4 = dqx * nqy;
        final double q5 = dqx * nqz;
        final double q6 = dqx * qw;
        final double q7 = dqy * nqz;
        final double q8 = dqy * qw;
        final double q9 = dqz * qw;
        final double isx = 1.0 / sx;
        final double isy = 1.0 / sy;
        final double isz = 1.0 / sz;
        this._m00(isx * (1.0 - q2 - q3))._m01(isy * (q4 + q9))._m02(isz * (q5 - q8))._m03(0.0)._m10(isx * (q4 - q9))._m11(isy * (1.0 - q3 - q00))._m12(isz * (q7 + q6))._m13(0.0)._m20(isx * (q5 + q8))._m21(isy * (q7 - q6))._m22(isz * (1.0 - q2 - q00))._m23(0.0)._m30(-this.m00 * tx - this.m10 * ty - this.m20 * tz)._m31(-this.m01 * tx - this.m11 * ty - this.m21 * tz)._m32(-this.m02 * tx - this.m12 * ty - this.m22 * tz)._m33(1.0).properties = 2;
        return this;
    }
    
    public Matrix4d translationRotateScaleInvert(final Vector3dc translation, final Quaterniondc quat, final Vector3dc scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4d translationRotateScaleInvert(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4d translationRotateScaleInvert(final Vector3dc translation, final Quaterniondc quat, final double scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4d translationRotateScaleInvert(final Vector3fc translation, final Quaternionfc quat, final double scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4d translationRotateScaleMulAffine(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double sx, final double sy, final double sz, final Matrix4d m) {
        final double w2 = qw * qw;
        final double x2 = qx * qx;
        final double y2 = qy * qy;
        final double z2 = qz * qz;
        final double zw = qz * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yw = qy * qw;
        final double yz = qy * qz;
        final double xw = qx * qw;
        final double nm00 = w2 + x2 - z2 - y2;
        final double nm2 = xy + zw + zw + xy;
        final double nm3 = xz - yw + xz - yw;
        final double nm4 = -zw + xy - zw + xy;
        final double nm5 = y2 - z2 + w2 - x2;
        final double nm6 = yz + yz + xw + xw;
        final double nm7 = yw + xz + xz + yw;
        final double nm8 = yz + yz - xw - xw;
        final double nm9 = z2 - y2 - x2 + w2;
        final double m2 = nm00 * m.m00 + nm4 * m.m01 + nm7 * m.m02;
        final double m3 = nm2 * m.m00 + nm5 * m.m01 + nm8 * m.m02;
        this.m02 = nm3 * m.m00 + nm6 * m.m01 + nm9 * m.m02;
        this.m00 = m2;
        this.m01 = m3;
        this.m03 = 0.0;
        final double m4 = nm00 * m.m10 + nm4 * m.m11 + nm7 * m.m12;
        final double m5 = nm2 * m.m10 + nm5 * m.m11 + nm8 * m.m12;
        this.m12 = nm3 * m.m10 + nm6 * m.m11 + nm9 * m.m12;
        this.m10 = m4;
        this.m11 = m5;
        this.m13 = 0.0;
        final double m6 = nm00 * m.m20 + nm4 * m.m21 + nm7 * m.m22;
        final double m7 = nm2 * m.m20 + nm5 * m.m21 + nm8 * m.m22;
        this.m22 = nm3 * m.m20 + nm6 * m.m21 + nm9 * m.m22;
        this.m20 = m6;
        this.m21 = m7;
        this.m23 = 0.0;
        final double m8 = nm00 * m.m30 + nm4 * m.m31 + nm7 * m.m32 + tx;
        final double m9 = nm2 * m.m30 + nm5 * m.m31 + nm8 * m.m32 + ty;
        this.m32 = nm3 * m.m30 + nm6 * m.m31 + nm9 * m.m32 + tz;
        this.m30 = m8;
        this.m31 = m9;
        this.m33 = 1.0;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        this.properties = (0x2 | ((one && (m.properties & 0x10) != 0x0) ? 16 : 0));
        return this;
    }
    
    public Matrix4d translationRotateScaleMulAffine(final Vector3fc translation, final Quaterniondc quat, final Vector3fc scale, final Matrix4d m) {
        return this.translationRotateScaleMulAffine(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z(), m);
    }
    
    public Matrix4d translationRotate(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw) {
        final double w2 = qw * qw;
        final double x2 = qx * qx;
        final double y2 = qy * qy;
        final double z2 = qz * qz;
        final double zw = qz * qw;
        final double xy = qx * qy;
        final double xz = qx * qz;
        final double yw = qy * qw;
        final double yz = qy * qz;
        final double xw = qx * qw;
        this.m00 = w2 + x2 - z2 - y2;
        this.m01 = xy + zw + zw + xy;
        this.m02 = xz - yw + xz - yw;
        this.m10 = -zw + xy - zw + xy;
        this.m11 = y2 - z2 + w2 - x2;
        this.m12 = yz + yz + xw + xw;
        this.m20 = yw + xz + xz + yw;
        this.m21 = yz + yz - xw - xw;
        this.m22 = z2 - y2 - x2 + w2;
        this.m30 = tx;
        this.m31 = ty;
        this.m32 = tz;
        this.m33 = 1.0;
        this.properties = 18;
        return this;
    }
    
    public Matrix4d translationRotate(final double tx, final double ty, final double tz, final Quaterniondc quat) {
        return this.translationRotate(tx, ty, tz, quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4d translationRotate(final Vector3dc translation, final Quaterniondc quat) {
        return this.translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4d translationRotateInvert(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw) {
        final double nqx = -qx;
        final double nqy = -qy;
        final double nqz = -qz;
        final double dqx = nqx + nqx;
        final double dqy = nqy + nqy;
        final double dqz = nqz + nqz;
        final double q00 = dqx * nqx;
        final double q2 = dqy * nqy;
        final double q3 = dqz * nqz;
        final double q4 = dqx * nqy;
        final double q5 = dqx * nqz;
        final double q6 = dqx * qw;
        final double q7 = dqy * nqz;
        final double q8 = dqy * qw;
        final double q9 = dqz * qw;
        return this._m00(1.0 - q2 - q3)._m01(q4 + q9)._m02(q5 - q8)._m03(0.0)._m10(q4 - q9)._m11(1.0 - q3 - q00)._m12(q7 + q6)._m13(0.0)._m20(q5 + q8)._m21(q7 - q6)._m22(1.0 - q2 - q00)._m23(0.0)._m30(-this.m00 * tx - this.m10 * ty - this.m20 * tz)._m31(-this.m01 * tx - this.m11 * ty - this.m21 * tz)._m32(-this.m02 * tx - this.m12 * ty - this.m22 * tz)._m33(1.0)._properties(18);
    }
    
    public Matrix4d translationRotateInvert(final Vector3fc translation, final Quaternionfc quat) {
        return this.translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4d rotate(final Quaterniondc quat, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(quat);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(quat, dest);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.rotateAffine(quat, dest);
        }
        return this.rotateGeneric(quat, dest);
    }
    
    private Matrix4d rotateGeneric(final Quaterniondc quat, final Matrix4d dest) {
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
        final double rm4 = -dzw + dxy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotate(final Quaternionfc quat, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(quat);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(quat, dest);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.rotateAffine(quat, dest);
        }
        return this.rotateGeneric(quat, dest);
    }
    
    private Matrix4d rotateGeneric(final Quaternionfc quat, final Matrix4d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double xy = quat.x() * quat.y();
        final double xz = quat.x() * quat.z();
        final double yw = quat.y() * quat.w();
        final double yz = quat.y() * quat.z();
        final double xw = quat.x() * quat.w();
        final double rm00 = w2 + x2 - z2 - y2;
        final double rm2 = xy + zw + zw + xy;
        final double rm3 = xz - yw + xz - yw;
        final double rm4 = -zw + xy - zw + xy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = yz + yz + xw + xw;
        final double rm7 = yw + xz + xz + yw;
        final double rm8 = yz + yz - xw - xw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotate(final Quaterniondc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4d rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4d rotateAffine(final Quaterniondc quat, final Matrix4d dest) {
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
        final double rm4 = -dzw + dxy;
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
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffine(final Quaterniondc quat) {
        return this.rotateAffine(quat, this);
    }
    
    public Matrix4d rotateTranslation(final Quaterniondc quat, final Matrix4d dest) {
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
        final double rm4 = -dzw + dxy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = dyz + dxw;
        final double rm7 = dyw + dxz;
        final double rm8 = dyz - dxw;
        final double rm9 = z2 - y2 - x2 + w2;
        dest._m20(rm7)._m21(rm8)._m22(rm9)._m23(0.0)._m00(rm00)._m01(rm2)._m02(rm3)._m03(0.0)._m10(rm4)._m11(rm5)._m12(rm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(1.0)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateTranslation(final Quaternionfc quat, final Matrix4d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double xy = quat.x() * quat.y();
        final double xz = quat.x() * quat.z();
        final double yw = quat.y() * quat.w();
        final double yz = quat.y() * quat.z();
        final double xw = quat.x() * quat.w();
        final double rm00 = w2 + x2 - z2 - y2;
        final double rm2 = xy + zw + zw + xy;
        final double rm3 = xz - yw + xz - yw;
        final double rm4 = -zw + xy - zw + xy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = yz + yz + xw + xw;
        final double rm7 = yw + xz + xz + yw;
        final double rm8 = yz + yz - xw - xw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = rm00;
        final double nm2 = rm2;
        final double nm3 = rm3;
        final double nm4 = rm4;
        final double nm5 = rm5;
        final double nm6 = rm6;
        dest._m20(rm7)._m21(rm8)._m22(rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(1.0)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocal(final Quaterniondc quat, final Matrix4d dest) {
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
        final double lm4 = -dzw + dxy;
        final double lm5 = y2 - z2 + w2 - x2;
        final double lm6 = dyz + dxw;
        final double lm7 = dyw + dxz;
        final double lm8 = dyz - dxw;
        final double lm9 = z2 - y2 - x2 + w2;
        final double nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final double nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final double nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final double nm4 = this.m03;
        final double nm5 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final double nm6 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final double nm7 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final double nm8 = this.m13;
        final double nm9 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final double nm10 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final double nm11 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
        final double nm12 = this.m23;
        final double nm13 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final double nm14 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final double nm15 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocal(final Quaterniondc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4d rotateAffine(final Quaternionfc quat, final Matrix4d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double xy = quat.x() * quat.y();
        final double xz = quat.x() * quat.z();
        final double yw = quat.y() * quat.w();
        final double yz = quat.y() * quat.z();
        final double xw = quat.x() * quat.w();
        final double rm00 = w2 + x2 - z2 - y2;
        final double rm2 = xy + zw + zw + xy;
        final double rm3 = xz - yw + xz - yw;
        final double rm4 = -zw + xy - zw + xy;
        final double rm5 = y2 - z2 + w2 - x2;
        final double rm6 = yz + yz + xw + xw;
        final double rm7 = yw + xz + xz + yw;
        final double rm8 = yz + yz - xw - xw;
        final double rm9 = z2 - y2 - x2 + w2;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateAffine(final Quaternionfc quat) {
        return this.rotateAffine(quat, this);
    }
    
    public Matrix4d rotateLocal(final Quaternionfc quat, final Matrix4d dest) {
        final double w2 = quat.w() * quat.w();
        final double x2 = quat.x() * quat.x();
        final double y2 = quat.y() * quat.y();
        final double z2 = quat.z() * quat.z();
        final double zw = quat.z() * quat.w();
        final double xy = quat.x() * quat.y();
        final double xz = quat.x() * quat.z();
        final double yw = quat.y() * quat.w();
        final double yz = quat.y() * quat.z();
        final double xw = quat.x() * quat.w();
        final double lm00 = w2 + x2 - z2 - y2;
        final double lm2 = xy + zw + zw + xy;
        final double lm3 = xz - yw + xz - yw;
        final double lm4 = -zw + xy - zw + xy;
        final double lm5 = y2 - z2 + w2 - x2;
        final double lm6 = yz + yz + xw + xw;
        final double lm7 = yw + xz + xz + yw;
        final double lm8 = yz + yz - xw - xw;
        final double lm9 = z2 - y2 - x2 + w2;
        final double nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final double nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final double nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final double nm4 = this.m03;
        final double nm5 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final double nm6 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final double nm7 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final double nm8 = this.m13;
        final double nm9 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final double nm10 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final double nm11 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
        final double nm12 = this.m23;
        final double nm13 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final double nm14 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final double nm15 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4d rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4d rotate(final AxisAngle4f axisAngle, final Matrix4d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4d rotate(final AxisAngle4d axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4d rotate(final AxisAngle4d axisAngle, final Matrix4d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4d rotate(final double angle, final Vector3dc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4d rotate(final double angle, final Vector3dc axis, final Matrix4d dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Matrix4d rotate(final double angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4d rotate(final double angle, final Vector3fc axis, final Matrix4d dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Vector4d getRow(final int row, final Vector4d dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m10;
                dest.z = this.m20;
                dest.w = this.m30;
                break;
            }
            case 1: {
                dest.x = this.m01;
                dest.y = this.m11;
                dest.z = this.m21;
                dest.w = this.m31;
                break;
            }
            case 2: {
                dest.x = this.m02;
                dest.y = this.m12;
                dest.z = this.m22;
                dest.w = this.m32;
                break;
            }
            case 3: {
                dest.x = this.m03;
                dest.y = this.m13;
                dest.z = this.m23;
                dest.w = this.m33;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Vector3d getRow(final int row, final Vector3d dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m10;
                dest.z = this.m20;
                break;
            }
            case 1: {
                dest.x = this.m01;
                dest.y = this.m11;
                dest.z = this.m21;
                break;
            }
            case 2: {
                dest.x = this.m02;
                dest.y = this.m12;
                dest.z = this.m22;
                break;
            }
            case 3: {
                dest.x = this.m03;
                dest.y = this.m13;
                dest.z = this.m23;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix4d setRow(final int row, final Vector4dc src) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                return this._m00(src.x())._m10(src.y())._m20(src.z())._m30(src.w())._properties(0);
            }
            case 1: {
                return this._m01(src.x())._m11(src.y())._m21(src.z())._m31(src.w())._properties(0);
            }
            case 2: {
                return this._m02(src.x())._m12(src.y())._m22(src.z())._m32(src.w())._properties(0);
            }
            case 3: {
                return this._m03(src.x())._m13(src.y())._m23(src.z())._m33(src.w())._properties(0);
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Vector4d getColumn(final int column, final Vector4d dest) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m01;
                dest.z = this.m02;
                dest.w = this.m03;
                break;
            }
            case 1: {
                dest.x = this.m10;
                dest.y = this.m11;
                dest.z = this.m12;
                dest.w = this.m13;
                break;
            }
            case 2: {
                dest.x = this.m20;
                dest.y = this.m21;
                dest.z = this.m22;
                dest.w = this.m23;
                break;
            }
            case 3: {
                dest.x = this.m30;
                dest.y = this.m31;
                dest.z = this.m32;
                dest.w = this.m33;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Vector3d getColumn(final int column, final Vector3d dest) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                dest.x = this.m00;
                dest.y = this.m01;
                dest.z = this.m02;
                break;
            }
            case 1: {
                dest.x = this.m10;
                dest.y = this.m11;
                dest.z = this.m12;
                break;
            }
            case 2: {
                dest.x = this.m20;
                dest.y = this.m21;
                dest.z = this.m22;
                break;
            }
            case 3: {
                dest.x = this.m30;
                dest.y = this.m31;
                dest.z = this.m32;
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix4d setColumn(final int column, final Vector4dc src) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                return this._m00(src.x())._m01(src.y())._m02(src.z())._m03(src.w())._properties(0);
            }
            case 1: {
                return this._m10(src.x())._m11(src.y())._m12(src.z())._m13(src.w())._properties(0);
            }
            case 2: {
                return this._m20(src.x())._m21(src.y())._m22(src.z())._m23(src.w())._properties(0);
            }
            case 3: {
                return this._m30(src.x())._m31(src.y())._m32(src.z())._m33(src.w())._properties(0);
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public double get(final int column, final int row) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix4d set(final int column, final int row, final double value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public double getRowColumn(final int row, final int column) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix4d setRowColumn(final int row, final int column, final double value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public Matrix4d normal() {
        return this.normal(this);
    }
    
    public Matrix4d normal(final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix4d normalOrthonormal(final Matrix4d dest) {
        if (dest != this) {
            dest.set(this);
        }
        return dest._properties(18);
    }
    
    private Matrix4d normalGeneric(final Matrix4d dest) {
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
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._m20(nm7)._m21(nm8)._m22(nm9)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0)._properties((this.properties | 0x2) & 0xFFFFFFF6);
    }
    
    public Matrix3d normal(final Matrix3d dest) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix3d normalOrthonormal(final Matrix3d dest) {
        dest.set(this);
        return dest;
    }
    
    private Matrix3d normalGeneric(final Matrix3d dest) {
        final double m00m11 = this.m00 * this.m11;
        final double m01m10 = this.m01 * this.m10;
        final double m02m10 = this.m02 * this.m10;
        final double m00m12 = this.m00 * this.m12;
        final double m01m11 = this.m01 * this.m12;
        final double m02m11 = this.m02 * this.m11;
        final double det = (m00m11 - m01m10) * this.m22 + (m02m10 - m00m12) * this.m21 + (m01m11 - m02m11) * this.m20;
        final double s = 1.0 / det;
        return dest._m00((this.m11 * this.m22 - this.m21 * this.m12) * s)._m01((this.m20 * this.m12 - this.m10 * this.m22) * s)._m02((this.m10 * this.m21 - this.m20 * this.m11) * s)._m10((this.m21 * this.m02 - this.m01 * this.m22) * s)._m11((this.m00 * this.m22 - this.m20 * this.m02) * s)._m12((this.m20 * this.m01 - this.m00 * this.m21) * s)._m20((m01m11 - m02m11) * s)._m21((m02m10 - m00m12) * s)._m22((m00m11 - m01m10) * s);
    }
    
    public Matrix4d cofactor3x3() {
        return this.cofactor3x3(this);
    }
    
    public Matrix3d cofactor3x3(final Matrix3d dest) {
        return dest._m00(this.m11 * this.m22 - this.m21 * this.m12)._m01(this.m20 * this.m12 - this.m10 * this.m22)._m02(this.m10 * this.m21 - this.m20 * this.m11)._m10(this.m21 * this.m02 - this.m01 * this.m22)._m11(this.m00 * this.m22 - this.m20 * this.m02)._m12(this.m20 * this.m01 - this.m00 * this.m21)._m20(this.m01 * this.m12 - this.m02 * this.m11)._m21(this.m02 * this.m10 - this.m00 * this.m12)._m22(this.m00 * this.m11 - this.m01 * this.m10);
    }
    
    public Matrix4d cofactor3x3(final Matrix4d dest) {
        final double nm10 = this.m21 * this.m02 - this.m01 * this.m22;
        final double nm11 = this.m00 * this.m22 - this.m20 * this.m02;
        final double nm12 = this.m20 * this.m01 - this.m00 * this.m21;
        final double nm13 = this.m01 * this.m12 - this.m11 * this.m02;
        final double nm14 = this.m02 * this.m10 - this.m12 * this.m00;
        final double nm15 = this.m00 * this.m11 - this.m10 * this.m01;
        return dest._m00(this.m11 * this.m22 - this.m21 * this.m12)._m01(this.m20 * this.m12 - this.m10 * this.m22)._m02(this.m10 * this.m21 - this.m20 * this.m11)._m03(0.0)._m10(nm10)._m11(nm11)._m12(nm12)._m13(0.0)._m20(nm13)._m21(nm14)._m22(nm15)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0)._properties((this.properties | 0x2) & 0xFFFFFFF6);
    }
    
    public Matrix4d normalize3x3() {
        return this.normalize3x3(this);
    }
    
    public Matrix4d normalize3x3(final Matrix4d dest) {
        final double invXlen = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        final double invYlen = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        final double invZlen = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
        dest._m00(this.m00 * invXlen)._m01(this.m01 * invXlen)._m02(this.m02 * invXlen)._m10(this.m10 * invYlen)._m11(this.m11 * invYlen)._m12(this.m12 * invYlen)._m20(this.m20 * invZlen)._m21(this.m21 * invZlen)._m22(this.m22 * invZlen)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties);
        return dest;
    }
    
    public Matrix3d normalize3x3(final Matrix3d dest) {
        final double invXlen = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        final double invYlen = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        final double invZlen = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
        dest.m00(this.m00 * invXlen);
        dest.m01(this.m01 * invXlen);
        dest.m02(this.m02 * invXlen);
        dest.m10(this.m10 * invYlen);
        dest.m11(this.m11 * invYlen);
        dest.m12(this.m12 * invYlen);
        dest.m20(this.m20 * invZlen);
        dest.m21(this.m21 * invZlen);
        dest.m22(this.m22 * invZlen);
        return dest;
    }
    
    public Vector4d unproject(final double winX, final double winY, final double winZ, final int[] viewport, final Vector4d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        double det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0 / det;
        final double im00 = (this.m11 * l - this.m12 * k + this.m13 * j) * det;
        final double im2 = (-this.m01 * l + this.m02 * k - this.m03 * j) * det;
        final double im3 = (this.m31 * f - this.m32 * e + this.m33 * d) * det;
        final double im4 = (-this.m21 * f + this.m22 * e - this.m23 * d) * det;
        final double im5 = (-this.m10 * l + this.m12 * i - this.m13 * h) * det;
        final double im6 = (this.m00 * l - this.m02 * i + this.m03 * h) * det;
        final double im7 = (-this.m30 * f + this.m32 * c - this.m33 * b) * det;
        final double im8 = (this.m20 * f - this.m22 * c + this.m23 * b) * det;
        final double im9 = (this.m10 * k - this.m11 * i + this.m13 * g) * det;
        final double im10 = (-this.m00 * k + this.m01 * i - this.m03 * g) * det;
        final double im11 = (this.m30 * e - this.m31 * c + this.m33 * a) * det;
        final double im12 = (-this.m20 * e + this.m21 * c - this.m23 * a) * det;
        final double im13 = (-this.m10 * j + this.m11 * h - this.m12 * g) * det;
        final double im14 = (this.m00 * j - this.m01 * h + this.m02 * g) * det;
        final double im15 = (-this.m30 * d + this.m31 * b - this.m32 * a) * det;
        final double im16 = (this.m20 * d - this.m21 * b + this.m22 * a) * det;
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double ndcZ = winZ + winZ - 1.0;
        final double invW = 1.0 / (im4 * ndcX + im8 * ndcY + im12 * ndcZ + im16);
        dest.x = (im00 * ndcX + im5 * ndcY + im9 * ndcZ + im13) * invW;
        dest.y = (im2 * ndcX + im6 * ndcY + im10 * ndcZ + im14) * invW;
        dest.z = (im3 * ndcX + im7 * ndcY + im11 * ndcZ + im15) * invW;
        dest.w = 1.0;
        return dest;
    }
    
    public Vector3d unproject(final double winX, final double winY, final double winZ, final int[] viewport, final Vector3d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        double det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0 / det;
        final double im00 = (this.m11 * l - this.m12 * k + this.m13 * j) * det;
        final double im2 = (-this.m01 * l + this.m02 * k - this.m03 * j) * det;
        final double im3 = (this.m31 * f - this.m32 * e + this.m33 * d) * det;
        final double im4 = (-this.m21 * f + this.m22 * e - this.m23 * d) * det;
        final double im5 = (-this.m10 * l + this.m12 * i - this.m13 * h) * det;
        final double im6 = (this.m00 * l - this.m02 * i + this.m03 * h) * det;
        final double im7 = (-this.m30 * f + this.m32 * c - this.m33 * b) * det;
        final double im8 = (this.m20 * f - this.m22 * c + this.m23 * b) * det;
        final double im9 = (this.m10 * k - this.m11 * i + this.m13 * g) * det;
        final double im10 = (-this.m00 * k + this.m01 * i - this.m03 * g) * det;
        final double im11 = (this.m30 * e - this.m31 * c + this.m33 * a) * det;
        final double im12 = (-this.m20 * e + this.m21 * c - this.m23 * a) * det;
        final double im13 = (-this.m10 * j + this.m11 * h - this.m12 * g) * det;
        final double im14 = (this.m00 * j - this.m01 * h + this.m02 * g) * det;
        final double im15 = (-this.m30 * d + this.m31 * b - this.m32 * a) * det;
        final double im16 = (this.m20 * d - this.m21 * b + this.m22 * a) * det;
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double ndcZ = winZ + winZ - 1.0;
        final double invW = 1.0 / (im4 * ndcX + im8 * ndcY + im12 * ndcZ + im16);
        dest.x = (im00 * ndcX + im5 * ndcY + im9 * ndcZ + im13) * invW;
        dest.y = (im2 * ndcX + im6 * ndcY + im10 * ndcZ + im14) * invW;
        dest.z = (im3 * ndcX + im7 * ndcY + im11 * ndcZ + im15) * invW;
        return dest;
    }
    
    public Vector4d unproject(final Vector3dc winCoords, final int[] viewport, final Vector4d dest) {
        return this.unproject(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector3d unproject(final Vector3dc winCoords, final int[] viewport, final Vector3d dest) {
        return this.unproject(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Matrix4d unprojectRay(final double winX, final double winY, final int[] viewport, final Vector3d originDest, final Vector3d dirDest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        double det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0 / det;
        final double im00 = (this.m11 * l - this.m12 * k + this.m13 * j) * det;
        final double im2 = (-this.m01 * l + this.m02 * k - this.m03 * j) * det;
        final double im3 = (this.m31 * f - this.m32 * e + this.m33 * d) * det;
        final double im4 = (-this.m21 * f + this.m22 * e - this.m23 * d) * det;
        final double im5 = (-this.m10 * l + this.m12 * i - this.m13 * h) * det;
        final double im6 = (this.m00 * l - this.m02 * i + this.m03 * h) * det;
        final double im7 = (-this.m30 * f + this.m32 * c - this.m33 * b) * det;
        final double im8 = (this.m20 * f - this.m22 * c + this.m23 * b) * det;
        final double im9 = (this.m10 * k - this.m11 * i + this.m13 * g) * det;
        final double im10 = (-this.m00 * k + this.m01 * i - this.m03 * g) * det;
        final double im11 = (this.m30 * e - this.m31 * c + this.m33 * a) * det;
        final double im12 = (-this.m20 * e + this.m21 * c - this.m23 * a) * det;
        final double im13 = (-this.m10 * j + this.m11 * h - this.m12 * g) * det;
        final double im14 = (this.m00 * j - this.m01 * h + this.m02 * g) * det;
        final double im15 = (-this.m30 * d + this.m31 * b - this.m32 * a) * det;
        final double im16 = (this.m20 * d - this.m21 * b + this.m22 * a) * det;
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double px = im00 * ndcX + im5 * ndcY + im13;
        final double py = im2 * ndcX + im6 * ndcY + im14;
        final double pz = im3 * ndcX + im7 * ndcY + im15;
        final double invNearW = 1.0 / (im4 * ndcX + im8 * ndcY - im12 + im16);
        final double nearX = (px - im9) * invNearW;
        final double nearY = (py - im10) * invNearW;
        final double nearZ = (pz - im11) * invNearW;
        final double invW0 = 1.0 / (im4 * ndcX + im8 * ndcY + im16);
        final double x0 = px * invW0;
        final double y0 = py * invW0;
        final double z0 = pz * invW0;
        originDest.x = nearX;
        originDest.y = nearY;
        originDest.z = nearZ;
        dirDest.x = x0 - nearX;
        dirDest.y = y0 - nearY;
        dirDest.z = z0 - nearZ;
        return this;
    }
    
    public Matrix4d unprojectRay(final Vector2dc winCoords, final int[] viewport, final Vector3d originDest, final Vector3d dirDest) {
        return this.unprojectRay(winCoords.x(), winCoords.y(), viewport, originDest, dirDest);
    }
    
    public Vector4d unprojectInv(final Vector3dc winCoords, final int[] viewport, final Vector4d dest) {
        return this.unprojectInv(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector4d unprojectInv(final double winX, final double winY, final double winZ, final int[] viewport, final Vector4d dest) {
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double ndcZ = winZ + winZ - 1.0;
        final double invW = 1.0 / (this.m03 * ndcX + this.m13 * ndcY + this.m23 * ndcZ + this.m33);
        dest.x = (this.m00 * ndcX + this.m10 * ndcY + this.m20 * ndcZ + this.m30) * invW;
        dest.y = (this.m01 * ndcX + this.m11 * ndcY + this.m21 * ndcZ + this.m31) * invW;
        dest.z = (this.m02 * ndcX + this.m12 * ndcY + this.m22 * ndcZ + this.m32) * invW;
        dest.w = 1.0;
        return dest;
    }
    
    public Vector3d unprojectInv(final Vector3dc winCoords, final int[] viewport, final Vector3d dest) {
        return this.unprojectInv(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector3d unprojectInv(final double winX, final double winY, final double winZ, final int[] viewport, final Vector3d dest) {
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double ndcZ = winZ + winZ - 1.0;
        final double invW = 1.0 / (this.m03 * ndcX + this.m13 * ndcY + this.m23 * ndcZ + this.m33);
        dest.x = (this.m00 * ndcX + this.m10 * ndcY + this.m20 * ndcZ + this.m30) * invW;
        dest.y = (this.m01 * ndcX + this.m11 * ndcY + this.m21 * ndcZ + this.m31) * invW;
        dest.z = (this.m02 * ndcX + this.m12 * ndcY + this.m22 * ndcZ + this.m32) * invW;
        return dest;
    }
    
    public Matrix4d unprojectInvRay(final Vector2dc winCoords, final int[] viewport, final Vector3d originDest, final Vector3d dirDest) {
        return this.unprojectInvRay(winCoords.x(), winCoords.y(), viewport, originDest, dirDest);
    }
    
    public Matrix4d unprojectInvRay(final double winX, final double winY, final int[] viewport, final Vector3d originDest, final Vector3d dirDest) {
        final double ndcX = (winX - viewport[0]) / viewport[2] * 2.0 - 1.0;
        final double ndcY = (winY - viewport[1]) / viewport[3] * 2.0 - 1.0;
        final double px = this.m00 * ndcX + this.m10 * ndcY + this.m30;
        final double py = this.m01 * ndcX + this.m11 * ndcY + this.m31;
        final double pz = this.m02 * ndcX + this.m12 * ndcY + this.m32;
        final double invNearW = 1.0 / (this.m03 * ndcX + this.m13 * ndcY - this.m23 + this.m33);
        final double nearX = (px - this.m20) * invNearW;
        final double nearY = (py - this.m21) * invNearW;
        final double nearZ = (pz - this.m22) * invNearW;
        final double invW0 = 1.0 / (this.m03 * ndcX + this.m13 * ndcY + this.m33);
        final double x0 = px * invW0;
        final double y0 = py * invW0;
        final double z0 = pz * invW0;
        originDest.x = nearX;
        originDest.y = nearY;
        originDest.z = nearZ;
        dirDest.x = x0 - nearX;
        dirDest.y = y0 - nearY;
        dirDest.z = z0 - nearZ;
        return this;
    }
    
    public Vector4d project(final double x, final double y, final double z, final int[] viewport, final Vector4d winCoordsDest) {
        final double invW = 1.0 / Math.fma(this.m03, x, Math.fma(this.m13, y, Math.fma(this.m23, z, this.m33)));
        final double nx = Math.fma(this.m00, x, Math.fma(this.m10, y, Math.fma(this.m20, z, this.m30))) * invW;
        final double ny = Math.fma(this.m01, x, Math.fma(this.m11, y, Math.fma(this.m21, z, this.m31))) * invW;
        final double nz = Math.fma(this.m02, x, Math.fma(this.m12, y, Math.fma(this.m22, z, this.m32))) * invW;
        return winCoordsDest.set(Math.fma(Math.fma(nx, 0.5, 0.5), viewport[2], viewport[0]), Math.fma(Math.fma(ny, 0.5, 0.5), viewport[3], viewport[1]), Math.fma(0.5, nz, 0.5), 1.0);
    }
    
    public Vector3d project(final double x, final double y, final double z, final int[] viewport, final Vector3d winCoordsDest) {
        final double invW = 1.0 / Math.fma(this.m03, x, Math.fma(this.m13, y, Math.fma(this.m23, z, this.m33)));
        final double nx = Math.fma(this.m00, x, Math.fma(this.m10, y, Math.fma(this.m20, z, this.m30))) * invW;
        final double ny = Math.fma(this.m01, x, Math.fma(this.m11, y, Math.fma(this.m21, z, this.m31))) * invW;
        final double nz = Math.fma(this.m02, x, Math.fma(this.m12, y, Math.fma(this.m22, z, this.m32))) * invW;
        winCoordsDest.x = Math.fma(Math.fma(nx, 0.5, 0.5), viewport[2], viewport[0]);
        winCoordsDest.y = Math.fma(Math.fma(ny, 0.5, 0.5), viewport[3], viewport[1]);
        winCoordsDest.z = Math.fma(0.5, nz, 0.5);
        return winCoordsDest;
    }
    
    public Vector4d project(final Vector3dc position, final int[] viewport, final Vector4d dest) {
        return this.project(position.x(), position.y(), position.z(), viewport, dest);
    }
    
    public Vector3d project(final Vector3dc position, final int[] viewport, final Vector3d dest) {
        return this.project(position.x(), position.y(), position.z(), viewport, dest);
    }
    
    public Matrix4d reflect(final double a, final double b, final double c, final double d, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.reflection(a, b, c, d);
        }
        if ((this.properties & 0x4) != 0x0) {
            return dest.reflection(a, b, c, d);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.reflectAffine(a, b, c, d, dest);
        }
        return this.reflectGeneric(a, b, c, d, dest);
    }
    
    private Matrix4d reflectAffine(final double a, final double b, final double c, final double d, final Matrix4d dest) {
        final double da = a + a;
        final double db = b + b;
        final double dc = c + c;
        final double dd = d + d;
        final double rm00 = 1.0 - da * a;
        final double rm2 = -da * b;
        final double rm3 = -da * c;
        final double rm4 = -db * a;
        final double rm5 = 1.0 - db * b;
        final double rm6 = -db * c;
        final double rm7 = -dc * a;
        final double rm8 = -dc * b;
        final double rm9 = 1.0 - dc * c;
        final double rm10 = -dd * a;
        final double rm11 = -dd * b;
        final double rm12 = -dd * c;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest._m30(this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30)._m31(this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31)._m32(this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32)._m33(this.m33)._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(0.0)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    private Matrix4d reflectGeneric(final double a, final double b, final double c, final double d, final Matrix4d dest) {
        final double da = a + a;
        final double db = b + b;
        final double dc = c + c;
        final double dd = d + d;
        final double rm00 = 1.0 - da * a;
        final double rm2 = -da * b;
        final double rm3 = -da * c;
        final double rm4 = -db * a;
        final double rm5 = 1.0 - db * b;
        final double rm6 = -db * c;
        final double rm7 = -dc * a;
        final double rm8 = -dc * b;
        final double rm9 = 1.0 - dc * c;
        final double rm10 = -dd * a;
        final double rm11 = -dd * b;
        final double rm12 = -dd * c;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m30(this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30)._m31(this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31)._m32(this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32)._m33(this.m03 * rm10 + this.m13 * rm11 + this.m23 * rm12 + this.m33)._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d reflect(final double a, final double b, final double c, final double d) {
        return this.reflect(a, b, c, d, this);
    }
    
    public Matrix4d reflect(final double nx, final double ny, final double nz, final double px, final double py, final double pz) {
        return this.reflect(nx, ny, nz, px, py, pz, this);
    }
    
    public Matrix4d reflect(final double nx, final double ny, final double nz, final double px, final double py, final double pz, final Matrix4d dest) {
        final double invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final double nnx = nx * invLength;
        final double nny = ny * invLength;
        final double nnz = nz * invLength;
        return this.reflect(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz, dest);
    }
    
    public Matrix4d reflect(final Vector3dc normal, final Vector3dc point) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4d reflect(final Quaterniondc orientation, final Vector3dc point) {
        return this.reflect(orientation, point, this);
    }
    
    public Matrix4d reflect(final Quaterniondc orientation, final Vector3dc point, final Matrix4d dest) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflect(normalX, normalY, normalZ, point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4d reflect(final Vector3dc normal, final Vector3dc point, final Matrix4d dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4d reflection(final double a, final double b, final double c, final double d) {
        final double da = a + a;
        final double db = b + b;
        final double dc = c + c;
        final double dd = d + d;
        this._m00(1.0 - da * a)._m01(-da * b)._m02(-da * c)._m03(0.0)._m10(-db * a)._m11(1.0 - db * b)._m12(-db * c)._m13(0.0)._m20(-dc * a)._m21(-dc * b)._m22(1.0 - dc * c)._m23(0.0)._m30(-dd * a)._m31(-dd * b)._m32(-dd * c)._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d reflection(final double nx, final double ny, final double nz, final double px, final double py, final double pz) {
        final double invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final double nnx = nx * invLength;
        final double nny = ny * invLength;
        final double nnz = nz * invLength;
        return this.reflection(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz);
    }
    
    public Matrix4d reflection(final Vector3dc normal, final Vector3dc point) {
        return this.reflection(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4d reflection(final Quaterniondc orientation, final Vector3dc point) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflection(normalX, normalY, normalZ, point.x(), point.y(), point.z());
    }
    
    public Matrix4d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.orthoGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d orthoGeneric(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        final double rm4 = (left + right) / (left - right);
        final double rm5 = (top + bottom) / (bottom - top);
        final double rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6 + this.m30)._m31(this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6 + this.m31)._m32(this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6 + this.m32)._m33(this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(this.m20 * rm3)._m21(this.m21 * rm3)._m22(this.m22 * rm3)._m23(this.m23 * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4d dest) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.ortho(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.orthoLHGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d orthoLHGeneric(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        final double rm4 = (left + right) / (left - right);
        final double rm5 = (top + bottom) / (bottom - top);
        final double rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6 + this.m30)._m31(this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6 + this.m31)._m32(this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6 + this.m32)._m33(this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(this.m20 * rm3)._m21(this.m21 * rm3)._m22(this.m22 * rm3)._m23(this.m23 * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4d dest) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d setOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / (right - left))._m11(2.0 / (top - bottom))._m22((zZeroToOne ? 1.0 : 2.0) / (zNear - zFar))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar)).properties = 2;
        return this;
    }
    
    public Matrix4d setOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setOrtho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d setOrthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / (right - left))._m11(2.0 / (top - bottom))._m22((zZeroToOne ? 1.0 : 2.0) / (zFar - zNear))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar)).properties = 2;
        return this;
    }
    
    public Matrix4d setOrthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setOrthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoSymmetric(width, height, zNear, zFar, zZeroToOne);
        }
        return this.orthoSymmetricGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d orthoSymmetricGeneric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = 2.0 / width;
        final double rm2 = 2.0 / height;
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        final double rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m20 * rm4 + this.m30)._m31(this.m21 * rm4 + this.m31)._m32(this.m22 * rm4 + this.m32)._m33(this.m23 * rm4 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(this.m20 * rm3)._m21(this.m21 * rm3)._m22(this.m22 * rm3)._m23(this.m23 * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final Matrix4d dest) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoSymmetric(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d orthoSymmetric(final double width, final double height, final double zNear, final double zFar) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoSymmetricLH(width, height, zNear, zFar, zZeroToOne);
        }
        return this.orthoSymmetricLHGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d orthoSymmetricLHGeneric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = 2.0 / width;
        final double rm2 = 2.0 / height;
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        final double rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m20 * rm4 + this.m30)._m31(this.m21 * rm4 + this.m31)._m32(this.m22 * rm4 + this.m32)._m33(this.m23 * rm4 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(this.m20 * rm3)._m21(this.m21 * rm3)._m22(this.m22 * rm3)._m23(this.m23 * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final Matrix4d dest) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4d setOrthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / width)._m11(2.0 / height)._m22((zZeroToOne ? 1.0 : 2.0) / (zNear - zFar))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar)).properties = 2;
        return this;
    }
    
    public Matrix4d setOrthoSymmetric(final double width, final double height, final double zNear, final double zFar) {
        return this.setOrthoSymmetric(width, height, zNear, zFar, false);
    }
    
    public Matrix4d setOrthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / width)._m11(2.0 / height)._m22((zZeroToOne ? 1.0 : 2.0) / (zFar - zNear))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar)).properties = 2;
        return this;
    }
    
    public Matrix4d setOrthoSymmetricLH(final double width, final double height, final double zNear, final double zFar) {
        return this.setOrthoSymmetricLH(width, height, zNear, zFar, false);
    }
    
    public Matrix4d ortho2D(final double left, final double right, final double bottom, final double top, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho2D(left, right, bottom, top);
        }
        return this.ortho2DGeneric(left, right, bottom, top, dest);
    }
    
    private Matrix4d ortho2DGeneric(final double left, final double right, final double bottom, final double top, final Matrix4d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (right + left) / (left - right);
        final double rm4 = (top + bottom) / (bottom - top);
        dest._m30(this.m00 * rm3 + this.m10 * rm4 + this.m30)._m31(this.m01 * rm3 + this.m11 * rm4 + this.m31)._m32(this.m02 * rm3 + this.m12 * rm4 + this.m32)._m33(this.m03 * rm3 + this.m13 * rm4 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(-this.m23)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d ortho2D(final double left, final double right, final double bottom, final double top) {
        return this.ortho2D(left, right, bottom, top, this);
    }
    
    public Matrix4d ortho2DLH(final double left, final double right, final double bottom, final double top, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho2DLH(left, right, bottom, top);
        }
        return this.ortho2DLHGeneric(left, right, bottom, top, dest);
    }
    
    private Matrix4d ortho2DLHGeneric(final double left, final double right, final double bottom, final double top, final Matrix4d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (right + left) / (left - right);
        final double rm4 = (top + bottom) / (bottom - top);
        dest._m30(this.m00 * rm3 + this.m10 * rm4 + this.m30)._m31(this.m01 * rm3 + this.m11 * rm4 + this.m31)._m32(this.m02 * rm3 + this.m12 * rm4 + this.m32)._m33(this.m03 * rm3 + this.m13 * rm4 + this.m33)._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d ortho2DLH(final double left, final double right, final double bottom, final double top) {
        return this.ortho2DLH(left, right, bottom, top, this);
    }
    
    public Matrix4d setOrtho2D(final double left, final double right, final double bottom, final double top) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / (right - left))._m11(2.0 / (top - bottom))._m22(-1.0)._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top)).properties = 2;
        return this;
    }
    
    public Matrix4d setOrtho2DLH(final double left, final double right, final double bottom, final double top) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00(2.0 / (right - left))._m11(2.0 / (top - bottom))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top)).properties = 2;
        return this;
    }
    
    public Matrix4d lookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4d lookAlong(final Vector3dc dir, final Vector3dc up, final Matrix4d dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4d lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAlong(dirX, dirY, dirZ, upX, upY, upZ);
        }
        return this.lookAlongGeneric(dirX, dirY, dirZ, upX, upY, upZ, dest);
    }
    
    private Matrix4d lookAlongGeneric(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
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
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4d setLookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4d setLookAlong(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ) {
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
        this._m00(leftX)._m01(upnX)._m02(dirX)._m03(0.0)._m10(leftY)._m11(upnY)._m12(dirY)._m13(0.0)._m20(leftZ)._m21(upnZ)._m22(dirZ)._m23(0.0)._m30(0.0)._m31(0.0)._m32(0.0)._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d setLookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.setLookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4d setLookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        double dirX = eyeX - centerX;
        double dirY = eyeY - centerY;
        double dirZ = eyeZ - centerZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        return this._m00(leftX)._m01(upnX)._m02(dirX)._m03(0.0)._m10(leftY)._m11(upnY)._m12(dirY)._m13(0.0)._m20(leftZ)._m21(upnZ)._m22(dirZ)._m23(0.0)._m30(-(leftX * eyeX + leftY * eyeY + leftZ * eyeZ))._m31(-(upnX * eyeX + upnY * eyeY + upnZ * eyeZ))._m32(-(dirX * eyeX + dirY * eyeY + dirZ * eyeZ))._m33(1.0)._properties(18);
    }
    
    public Matrix4d lookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up, final Matrix4d dest) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4d lookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4d lookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.lookAtPerspective(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
        }
        return this.lookAtGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4d lookAtGeneric(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        double dirX = eyeX - centerX;
        double dirY = eyeY - centerY;
        double dirZ = eyeZ - centerZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final double rm10 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final double rm11 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final double rm12 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m30(this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30)._m31(this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31)._m32(this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32)._m33(this.m03 * rm10 + this.m13 * rm11 + this.m23 * rm12 + this.m33)._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d lookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        return this.lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4d lookAtPerspective(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        double dirX = eyeX - centerX;
        double dirY = eyeY - centerY;
        double dirZ = eyeZ - centerZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final double rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final double rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final double rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final double nm10 = this.m00 * leftY;
        final double nm11 = this.m00 * leftZ;
        final double nm12 = this.m11 * upnZ;
        final double nm13 = this.m00 * rm30;
        final double nm14 = this.m11 * rm31;
        final double nm15 = this.m22 * rm32 + this.m32;
        final double nm16 = this.m23 * rm32;
        return dest._m00(this.m00 * leftX)._m01(this.m11 * upnX)._m02(this.m22 * dirX)._m03(this.m23 * dirX)._m10(nm10)._m11(this.m11 * upnY)._m12(this.m22 * dirY)._m13(this.m23 * dirY)._m20(nm11)._m21(nm12)._m22(this.m22 * dirZ)._m23(this.m23 * dirZ)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4d setLookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.setLookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4d setLookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        double dirX = centerX - eyeX;
        double dirY = centerY - eyeY;
        double dirZ = centerZ - eyeZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        this._m00(leftX)._m01(upnX)._m02(dirX)._m03(0.0)._m10(leftY)._m11(upnY)._m12(dirY)._m13(0.0)._m20(leftZ)._m21(upnZ)._m22(dirZ)._m23(0.0)._m30(-(leftX * eyeX + leftY * eyeY + leftZ * eyeZ))._m31(-(upnX * eyeX + upnY * eyeY + upnZ * eyeZ))._m32(-(dirX * eyeX + dirY * eyeY + dirZ * eyeZ))._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d lookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up, final Matrix4d dest) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4d lookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4d lookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.lookAtPerspectiveLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
        }
        return this.lookAtLHGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4d lookAtLHGeneric(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        double dirX = centerX - eyeX;
        double dirY = centerY - eyeY;
        double dirZ = centerZ - eyeZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final double rm10 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final double rm11 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final double rm12 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m30(this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30)._m31(this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31)._m32(this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32)._m33(this.m03 * rm10 + this.m13 * rm11 + this.m23 * rm12 + this.m33)._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d lookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        return this.lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4d lookAtPerspectiveLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
        double dirX = centerX - eyeX;
        double dirY = centerY - eyeY;
        double dirZ = centerZ - eyeZ;
        final double invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final double rm10 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final double rm11 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final double rm12 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final double nm00 = this.m00 * rm00;
        final double nm2 = this.m11 * rm2;
        final double nm3 = this.m22 * rm3;
        final double nm4 = this.m23 * rm3;
        final double nm5 = this.m00 * rm4;
        final double nm6 = this.m11 * rm5;
        final double nm7 = this.m22 * rm6;
        final double nm8 = this.m23 * rm6;
        final double nm9 = this.m00 * rm7;
        final double nm10 = this.m11 * rm8;
        final double nm11 = this.m22 * rm9;
        final double nm12 = this.m23 * rm9;
        final double nm13 = this.m00 * rm10;
        final double nm14 = this.m11 * rm11;
        final double nm15 = this.m22 * rm12 + this.m32;
        final double nm16 = this.m23 * rm12;
        dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
        return dest;
    }
    
    public Matrix4d tile(final int x, final int y, final int w, final int h) {
        return this.tile(x, y, w, h, this);
    }
    
    public Matrix4d tile(final int x, final int y, final int w, final int h, final Matrix4d dest) {
        final float tx = (float)(w - 1 - (x << 1));
        final float ty = (float)(h - 1 - (y << 1));
        return dest._m30(Math.fma(this.m00, tx, Math.fma(this.m10, ty, this.m30)))._m31(Math.fma(this.m01, tx, Math.fma(this.m11, ty, this.m31)))._m32(Math.fma(this.m02, tx, Math.fma(this.m12, ty, this.m32)))._m33(Math.fma(this.m03, tx, Math.fma(this.m13, ty, this.m33)))._m00(this.m00 * w)._m01(this.m01 * w)._m02(this.m02 * w)._m03(this.m03 * w)._m10(this.m10 * h)._m11(this.m11 * h)._m12(this.m12 * h)._m13(this.m13 * h)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4d perspective(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspective(fovy, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveGeneric(fovy, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d perspectiveGeneric(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double h = Math.tan(fovy * 0.5);
        final double rm00 = 1.0 / (h * aspect);
        final double rm2 = 1.0 / h;
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm3;
        double rm4;
        if (farInf) {
            final double e = 1.0E-6;
            rm3 = e - 1.0;
            rm4 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            rm3 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm4 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m20 * rm3 - this.m30;
        final double nm21 = this.m21 * rm3 - this.m31;
        final double nm22 = this.m22 * rm3 - this.m32;
        final double nm23 = this.m23 * rm3 - this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm4)._m31(this.m21 * rm4)._m32(this.m22 * rm4)._m33(this.m23 * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4d perspective(final double fovy, final double aspect, final double zNear, final double zFar, final Matrix4d dest) {
        return this.perspective(fovy, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4d perspective(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspective(fovy, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspective(final double fovy, final double aspect, final double zNear, final double zFar) {
        return this.perspective(fovy, aspect, zNear, zFar, this);
    }
    
    public Matrix4d perspectiveRect(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveRect(width, height, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveRectGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d perspectiveRectGeneric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = (zNear + zNear) / width;
        final double rm2 = (zNear + zNear) / height;
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm3;
        double rm4;
        if (farInf) {
            final double e = 9.999999974752427E-7;
            rm3 = e - 1.0;
            rm4 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 9.999999974752427E-7;
            rm3 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm4 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m20 * rm3 - this.m30;
        final double nm21 = this.m21 * rm3 - this.m31;
        final double nm22 = this.m22 * rm3 - this.m32;
        final double nm23 = this.m23 * rm3 - this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm4)._m31(this.m21 * rm4)._m32(this.m22 * rm4)._m33(this.m23 * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4d perspectiveRect(final double width, final double height, final double zNear, final double zFar, final Matrix4d dest) {
        return this.perspectiveRect(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4d perspectiveRect(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspectiveRect(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspectiveRect(final double width, final double height, final double zNear, final double zFar) {
        return this.perspectiveRect(width, height, zNear, zFar, this);
    }
    
    public Matrix4d perspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveOffCenterGeneric(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d perspectiveOffCenterGeneric(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double h = Math.tan(fovy * 0.5);
        final double xScale = 1.0 / (h * aspect);
        final double yScale = 1.0 / h;
        final double rm00 = xScale;
        final double rm2 = yScale;
        final double offX = Math.tan(offAngleX);
        final double offY = Math.tan(offAngleY);
        final double rm3 = offX * xScale;
        final double rm4 = offY * yScale;
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm5;
        double rm6;
        if (farInf) {
            final double e = 1.0E-6;
            rm5 = e - 1.0;
            rm6 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            rm5 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm6 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm5 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm6 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m00 * rm3 + this.m10 * rm4 + this.m20 * rm5 - this.m30;
        final double nm21 = this.m01 * rm3 + this.m11 * rm4 + this.m21 * rm5 - this.m31;
        final double nm22 = this.m02 * rm3 + this.m12 * rm4 + this.m22 * rm5 - this.m32;
        final double nm23 = this.m03 * rm3 + this.m13 * rm4 + this.m23 * rm5 - this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm6)._m31(this.m21 * rm6)._m32(this.m22 * rm6)._m33(this.m23 * rm6)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & ~(0x1E | ((rm3 != 0.0 || rm4 != 0.0) ? 1 : 0)));
        return dest;
    }
    
    public Matrix4d perspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar, final Matrix4d dest) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4d perspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, this);
    }
    
    public Matrix4d perspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        return this.frustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne, dest);
    }
    
    public Matrix4d perspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar) {
        return this.perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, this);
    }
    
    public Matrix4d perspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final Matrix4d dest) {
        return this.frustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, dest);
    }
    
    public Matrix4d perspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        return this.frustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne, dest);
    }
    
    public Matrix4d perspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar) {
        return this.perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, this);
    }
    
    public Matrix4d perspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final Matrix4d dest) {
        return this.frustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, dest);
    }
    
    public Matrix4d setPerspective(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        final double h = Math.tan(fovy * 0.5);
        this._m00(1.0 / (h * aspect))._m01(0.0)._m02(0.0)._m03(0.0)._m10(0.0)._m11(1.0 / h)._m12(0.0)._m13(0.0)._m20(0.0)._m21(0.0);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(e - 1.0)._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e)._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0)._m30(0.0)._m31(0.0)._m33(0.0).properties = 1;
        return this;
    }
    
    public Matrix4d setPerspective(final double fovy, final double aspect, final double zNear, final double zFar) {
        return this.setPerspective(fovy, aspect, zNear, zFar, false);
    }
    
    public Matrix4d setPerspectiveRect(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.zero();
        this._m00((zNear + zNear) / width);
        this._m11((zNear + zNear) / height);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(e - 1.0);
            this._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 9.999999974752427E-7;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e);
            this._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar));
            this._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0);
        this.properties = 1;
        return this;
    }
    
    public Matrix4d setPerspectiveRect(final double width, final double height, final double zNear, final double zFar) {
        return this.setPerspectiveRect(width, height, zNear, zFar, false);
    }
    
    public Matrix4d setPerspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar) {
        return this.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, false);
    }
    
    public Matrix4d setPerspectiveOffCenter(final double fovy, final double offAngleX, final double offAngleY, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.zero();
        final double h = Math.tan(fovy * 0.5);
        final double xScale = 1.0 / (h * aspect);
        final double yScale = 1.0 / h;
        this._m00(xScale)._m11(yScale);
        final double offX = Math.tan(offAngleX);
        final double offY = Math.tan(offAngleY);
        this._m20(offX * xScale)._m21(offY * yScale);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(e - 1.0)._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e)._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0)._m30(0.0)._m31(0.0)._m33(0.0).properties = ((offAngleX == 0.0 && offAngleY == 0.0) ? 1 : 0);
        return this;
    }
    
    public Matrix4d setPerspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar) {
        return this.setPerspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }
    
    public Matrix4d setPerspectiveOffCenterFov(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.setFrustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne);
    }
    
    public Matrix4d setPerspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar) {
        return this.setPerspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }
    
    public Matrix4d setPerspectiveOffCenterFovLH(final double angleLeft, final double angleRight, final double angleDown, final double angleUp, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.setFrustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne);
    }
    
    public Matrix4d perspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveLH(fovy, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveLHGeneric(fovy, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d perspectiveLHGeneric(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double h = Math.tan(fovy * 0.5);
        final double rm00 = 1.0 / (h * aspect);
        final double rm2 = 1.0 / h;
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm3;
        double rm4;
        if (farInf) {
            final double e = 1.0E-6;
            rm3 = 1.0 - e;
            rm4 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            rm3 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm4 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m20 * rm3 + this.m30;
        final double nm21 = this.m21 * rm3 + this.m31;
        final double nm22 = this.m22 * rm3 + this.m32;
        final double nm23 = this.m23 * rm3 + this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm4)._m31(this.m21 * rm4)._m32(this.m22 * rm4)._m33(this.m23 * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4d perspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d perspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar, final Matrix4d dest) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4d perspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, this);
    }
    
    public Matrix4d setPerspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar, final boolean zZeroToOne) {
        final double h = Math.tan(fovy * 0.5);
        this._m00(1.0 / (h * aspect))._m01(0.0)._m02(0.0)._m03(0.0)._m10(0.0)._m11(1.0 / h)._m12(0.0)._m13(0.0)._m20(0.0)._m21(0.0);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(1.0 - e)._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e)._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(1.0)._m30(0.0)._m31(0.0)._m33(0.0).properties = 1;
        return this;
    }
    
    public Matrix4d setPerspectiveLH(final double fovy, final double aspect, final double zNear, final double zFar) {
        return this.setPerspectiveLH(fovy, aspect, zNear, zFar, false);
    }
    
    public Matrix4d frustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setFrustum(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.frustumGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d frustumGeneric(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = (zNear + zNear) / (right - left);
        final double rm2 = (zNear + zNear) / (top - bottom);
        final double rm3 = (right + left) / (right - left);
        final double rm4 = (top + bottom) / (top - bottom);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm5;
        double rm6;
        if (farInf) {
            final double e = 1.0E-6;
            rm5 = e - 1.0;
            rm6 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            rm5 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm6 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm5 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm6 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m00 * rm3 + this.m10 * rm4 + this.m20 * rm5 - this.m30;
        final double nm21 = this.m01 * rm3 + this.m11 * rm4 + this.m21 * rm5 - this.m31;
        final double nm22 = this.m02 * rm3 + this.m12 * rm4 + this.m22 * rm5 - this.m32;
        final double nm23 = this.m03 * rm3 + this.m13 * rm4 + this.m23 * rm5 - this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm6)._m31(this.m21 * rm6)._m32(this.m22 * rm6)._m33(this.m23 * rm6)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(0);
        return dest;
    }
    
    public Matrix4d frustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4d dest) {
        return this.frustum(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4d frustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.frustum(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d frustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.frustum(left, right, bottom, top, zNear, zFar, this);
    }
    
    public Matrix4d setFrustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00((zNear + zNear) / (right - left))._m11((zNear + zNear) / (top - bottom))._m20((right + left) / (right - left))._m21((top + bottom) / (top - bottom));
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(e - 1.0)._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e)._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0)._m33(0.0).properties = ((this.m20 == 0.0 && this.m21 == 0.0) ? 1 : 0);
        return this;
    }
    
    public Matrix4d setFrustum(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setFrustum(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d frustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setFrustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.frustumLHGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4d frustumLHGeneric(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4d dest) {
        final double rm00 = (zNear + zNear) / (right - left);
        final double rm2 = (zNear + zNear) / (top - bottom);
        final double rm3 = (right + left) / (right - left);
        final double rm4 = (top + bottom) / (top - bottom);
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        double rm5;
        double rm6;
        if (farInf) {
            final double e = 1.0E-6;
            rm5 = 1.0 - e;
            rm6 = (e - (zZeroToOne ? 1.0 : 2.0)) * zNear;
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            rm5 = (zZeroToOne ? 0.0 : 1.0) - e;
            rm6 = ((zZeroToOne ? 1.0 : 2.0) - e) * zFar;
        }
        else {
            rm5 = (zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear);
            rm6 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final double nm20 = this.m00 * rm3 + this.m10 * rm4 + this.m20 * rm5 + this.m30;
        final double nm21 = this.m01 * rm3 + this.m11 * rm4 + this.m21 * rm5 + this.m31;
        final double nm22 = this.m02 * rm3 + this.m12 * rm4 + this.m22 * rm5 + this.m32;
        final double nm23 = this.m03 * rm3 + this.m13 * rm4 + this.m23 * rm5 + this.m33;
        dest._m00(this.m00 * rm00)._m01(this.m01 * rm00)._m02(this.m02 * rm00)._m03(this.m03 * rm00)._m10(this.m10 * rm2)._m11(this.m11 * rm2)._m12(this.m12 * rm2)._m13(this.m13 * rm2)._m30(this.m20 * rm6)._m31(this.m21 * rm6)._m32(this.m22 * rm6)._m33(this.m23 * rm6)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(0);
        return dest;
    }
    
    public Matrix4d frustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4d frustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4d dest) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4d frustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, this);
    }
    
    public Matrix4d setFrustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this._m00((zNear + zNear) / (right - left))._m11((zNear + zNear) / (top - bottom))._m20((right + left) / (right - left))._m21((top + bottom) / (top - bottom));
        final boolean farInf = zFar > 0.0 && Double.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0 && Double.isInfinite(zNear);
        if (farInf) {
            final double e = 1.0E-6;
            this._m22(1.0 - e)._m32((e - (zZeroToOne ? 1.0 : 2.0)) * zNear);
        }
        else if (nearInf) {
            final double e = 1.0E-6;
            this._m22((zZeroToOne ? 0.0 : 1.0) - e)._m32(((zZeroToOne ? 1.0 : 2.0) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(1.0)._m33(0.0).properties = ((this.m20 == 0.0 && this.m21 == 0.0) ? 1 : 0);
        return this;
    }
    
    public Matrix4d setFrustumLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setFrustumLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4d setFromIntrinsic(final double alphaX, final double alphaY, final double gamma, final double u0, final double v0, final int imgWidth, final int imgHeight, final double near, final double far) {
        final double l00 = 2.0 / imgWidth;
        final double l2 = 2.0 / imgHeight;
        final double l3 = 2.0 / (near - far);
        this.m00 = l00 * alphaX;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = l00 * gamma;
        this.m11 = l2 * alphaY;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = l00 * u0 - 1.0;
        this.m21 = l2 * v0 - 1.0;
        this.m22 = l3 * -(near + far) + (far + near) / (near - far);
        this.m23 = -1.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = l3 * -near * far;
        this.m33 = 0.0;
        this.properties = 1;
        return this;
    }
    
    public Vector4d frustumPlane(final int plane, final Vector4d dest) {
        switch (plane) {
            case 0: {
                dest.set(this.m03 + this.m00, this.m13 + this.m10, this.m23 + this.m20, this.m33 + this.m30).normalize3();
                break;
            }
            case 1: {
                dest.set(this.m03 - this.m00, this.m13 - this.m10, this.m23 - this.m20, this.m33 - this.m30).normalize3();
                break;
            }
            case 2: {
                dest.set(this.m03 + this.m01, this.m13 + this.m11, this.m23 + this.m21, this.m33 + this.m31).normalize3();
                break;
            }
            case 3: {
                dest.set(this.m03 - this.m01, this.m13 - this.m11, this.m23 - this.m21, this.m33 - this.m31).normalize3();
                break;
            }
            case 4: {
                dest.set(this.m03 + this.m02, this.m13 + this.m12, this.m23 + this.m22, this.m33 + this.m32).normalize3();
                break;
            }
            case 5: {
                dest.set(this.m03 - this.m02, this.m13 - this.m12, this.m23 - this.m22, this.m33 - this.m32).normalize3();
                break;
            }
            default: {
                throw new IllegalArgumentException("dest");
            }
        }
        return dest;
    }
    
    public Vector3d frustumCorner(final int corner, final Vector3d dest) {
        double n1x = 0.0;
        double n1y = 0.0;
        double n1z = 0.0;
        double d1 = 0.0;
        double n2x = 0.0;
        double n2y = 0.0;
        double n2z = 0.0;
        double d2 = 0.0;
        double n3x = 0.0;
        double n3y = 0.0;
        double n3z = 0.0;
        double d3 = 0.0;
        switch (corner) {
            case 0: {
                n1x = this.m03 + this.m00;
                n1y = this.m13 + this.m10;
                n1z = this.m23 + this.m20;
                d1 = this.m33 + this.m30;
                n2x = this.m03 + this.m01;
                n2y = this.m13 + this.m11;
                n2z = this.m23 + this.m21;
                d2 = this.m33 + this.m31;
                n3x = this.m03 + this.m02;
                n3y = this.m13 + this.m12;
                n3z = this.m23 + this.m22;
                d3 = this.m33 + this.m32;
                break;
            }
            case 1: {
                n1x = this.m03 - this.m00;
                n1y = this.m13 - this.m10;
                n1z = this.m23 - this.m20;
                d1 = this.m33 - this.m30;
                n2x = this.m03 + this.m01;
                n2y = this.m13 + this.m11;
                n2z = this.m23 + this.m21;
                d2 = this.m33 + this.m31;
                n3x = this.m03 + this.m02;
                n3y = this.m13 + this.m12;
                n3z = this.m23 + this.m22;
                d3 = this.m33 + this.m32;
                break;
            }
            case 2: {
                n1x = this.m03 - this.m00;
                n1y = this.m13 - this.m10;
                n1z = this.m23 - this.m20;
                d1 = this.m33 - this.m30;
                n2x = this.m03 - this.m01;
                n2y = this.m13 - this.m11;
                n2z = this.m23 - this.m21;
                d2 = this.m33 - this.m31;
                n3x = this.m03 + this.m02;
                n3y = this.m13 + this.m12;
                n3z = this.m23 + this.m22;
                d3 = this.m33 + this.m32;
                break;
            }
            case 3: {
                n1x = this.m03 + this.m00;
                n1y = this.m13 + this.m10;
                n1z = this.m23 + this.m20;
                d1 = this.m33 + this.m30;
                n2x = this.m03 - this.m01;
                n2y = this.m13 - this.m11;
                n2z = this.m23 - this.m21;
                d2 = this.m33 - this.m31;
                n3x = this.m03 + this.m02;
                n3y = this.m13 + this.m12;
                n3z = this.m23 + this.m22;
                d3 = this.m33 + this.m32;
                break;
            }
            case 4: {
                n1x = this.m03 - this.m00;
                n1y = this.m13 - this.m10;
                n1z = this.m23 - this.m20;
                d1 = this.m33 - this.m30;
                n2x = this.m03 + this.m01;
                n2y = this.m13 + this.m11;
                n2z = this.m23 + this.m21;
                d2 = this.m33 + this.m31;
                n3x = this.m03 - this.m02;
                n3y = this.m13 - this.m12;
                n3z = this.m23 - this.m22;
                d3 = this.m33 - this.m32;
                break;
            }
            case 5: {
                n1x = this.m03 + this.m00;
                n1y = this.m13 + this.m10;
                n1z = this.m23 + this.m20;
                d1 = this.m33 + this.m30;
                n2x = this.m03 + this.m01;
                n2y = this.m13 + this.m11;
                n2z = this.m23 + this.m21;
                d2 = this.m33 + this.m31;
                n3x = this.m03 - this.m02;
                n3y = this.m13 - this.m12;
                n3z = this.m23 - this.m22;
                d3 = this.m33 - this.m32;
                break;
            }
            case 6: {
                n1x = this.m03 + this.m00;
                n1y = this.m13 + this.m10;
                n1z = this.m23 + this.m20;
                d1 = this.m33 + this.m30;
                n2x = this.m03 - this.m01;
                n2y = this.m13 - this.m11;
                n2z = this.m23 - this.m21;
                d2 = this.m33 - this.m31;
                n3x = this.m03 - this.m02;
                n3y = this.m13 - this.m12;
                n3z = this.m23 - this.m22;
                d3 = this.m33 - this.m32;
                break;
            }
            case 7: {
                n1x = this.m03 - this.m00;
                n1y = this.m13 - this.m10;
                n1z = this.m23 - this.m20;
                d1 = this.m33 - this.m30;
                n2x = this.m03 - this.m01;
                n2y = this.m13 - this.m11;
                n2z = this.m23 - this.m21;
                d2 = this.m33 - this.m31;
                n3x = this.m03 - this.m02;
                n3y = this.m13 - this.m12;
                n3z = this.m23 - this.m22;
                d3 = this.m33 - this.m32;
                break;
            }
            default: {
                throw new IllegalArgumentException("corner");
            }
        }
        final double c23x = n2y * n3z - n2z * n3y;
        final double c23y = n2z * n3x - n2x * n3z;
        final double c23z = n2x * n3y - n2y * n3x;
        final double c31x = n3y * n1z - n3z * n1y;
        final double c31y = n3z * n1x - n3x * n1z;
        final double c31z = n3x * n1y - n3y * n1x;
        final double c12x = n1y * n2z - n1z * n2y;
        final double c12y = n1z * n2x - n1x * n2z;
        final double c12z = n1x * n2y - n1y * n2x;
        final double invDot = 1.0 / (n1x * c23x + n1y * c23y + n1z * c23z);
        dest.x = (-c23x * d1 - c31x * d2 - c12x * d3) * invDot;
        dest.y = (-c23y * d1 - c31y * d2 - c12y * d3) * invDot;
        dest.z = (-c23z * d1 - c31z * d2 - c12z * d3) * invDot;
        return dest;
    }
    
    public Vector3d perspectiveOrigin(final Vector3d dest) {
        final double n1x = this.m03 + this.m00;
        final double n1y = this.m13 + this.m10;
        final double n1z = this.m23 + this.m20;
        final double d1 = this.m33 + this.m30;
        final double n2x = this.m03 - this.m00;
        final double n2y = this.m13 - this.m10;
        final double n2z = this.m23 - this.m20;
        final double d2 = this.m33 - this.m30;
        final double n3x = this.m03 - this.m01;
        final double n3y = this.m13 - this.m11;
        final double n3z = this.m23 - this.m21;
        final double d3 = this.m33 - this.m31;
        final double c23x = n2y * n3z - n2z * n3y;
        final double c23y = n2z * n3x - n2x * n3z;
        final double c23z = n2x * n3y - n2y * n3x;
        final double c31x = n3y * n1z - n3z * n1y;
        final double c31y = n3z * n1x - n3x * n1z;
        final double c31z = n3x * n1y - n3y * n1x;
        final double c12x = n1y * n2z - n1z * n2y;
        final double c12y = n1z * n2x - n1x * n2z;
        final double c12z = n1x * n2y - n1y * n2x;
        final double invDot = 1.0 / (n1x * c23x + n1y * c23y + n1z * c23z);
        dest.x = (-c23x * d1 - c31x * d2 - c12x * d3) * invDot;
        dest.y = (-c23y * d1 - c31y * d2 - c12y * d3) * invDot;
        dest.z = (-c23z * d1 - c31z * d2 - c12z * d3) * invDot;
        return dest;
    }
    
    public Vector3d perspectiveInvOrigin(final Vector3d dest) {
        final double invW = 1.0 / this.m23;
        dest.x = this.m20 * invW;
        dest.y = this.m21 * invW;
        dest.z = this.m22 * invW;
        return dest;
    }
    
    public double perspectiveFov() {
        final double n1x = this.m03 + this.m01;
        final double n1y = this.m13 + this.m11;
        final double n1z = this.m23 + this.m21;
        final double n2x = this.m01 - this.m03;
        final double n2y = this.m11 - this.m13;
        final double n2z = this.m21 - this.m23;
        final double n1len = Math.sqrt(n1x * n1x + n1y * n1y + n1z * n1z);
        final double n2len = Math.sqrt(n2x * n2x + n2y * n2y + n2z * n2z);
        return Math.acos((n1x * n2x + n1y * n2y + n1z * n2z) / (n1len * n2len));
    }
    
    public double perspectiveNear() {
        return this.m32 / (this.m23 + this.m22);
    }
    
    public double perspectiveFar() {
        return this.m32 / (this.m22 - this.m23);
    }
    
    public Vector3d frustumRayDir(final double x, final double y, final Vector3d dest) {
        final double a = this.m10 * this.m23;
        final double b = this.m13 * this.m21;
        final double c = this.m10 * this.m21;
        final double d = this.m11 * this.m23;
        final double e = this.m13 * this.m20;
        final double f = this.m11 * this.m20;
        final double g = this.m03 * this.m20;
        final double h = this.m01 * this.m23;
        final double i = this.m01 * this.m20;
        final double j = this.m03 * this.m21;
        final double k = this.m00 * this.m23;
        final double l = this.m00 * this.m21;
        final double m = this.m00 * this.m13;
        final double n = this.m03 * this.m11;
        final double o = this.m00 * this.m11;
        final double p = this.m01 * this.m13;
        final double q = this.m03 * this.m10;
        final double r = this.m01 * this.m10;
        final double m1x = (d + e + f - a - b - c) * (1.0 - y) + (a - b - c + d - e + f) * y;
        final double m1y = (j + k + l - g - h - i) * (1.0 - y) + (g - h - i + j - k + l) * y;
        final double m1z = (p + q + r - m - n - o) * (1.0 - y) + (m - n - o + p - q + r) * y;
        final double m2x = (b - c - d + e + f - a) * (1.0 - y) + (a + b - c - d - e + f) * y;
        final double m2y = (h - i - j + k + l - g) * (1.0 - y) + (g + h - i - j - k + l) * y;
        final double m2z = (n - o - p + q + r - m) * (1.0 - y) + (m + n - o - p - q + r) * y;
        dest.x = m1x * (1.0 - x) + m2x * x;
        dest.y = m1y * (1.0 - x) + m2y * x;
        dest.z = m1z * (1.0 - x) + m2z * x;
        return dest.normalize(dest);
    }
    
    public Vector3d positiveZ(final Vector3d dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveZ(dir);
        }
        return this.positiveZGeneric(dir);
    }
    
    private Vector3d positiveZGeneric(final Vector3d dir) {
        return dir.set(this.m10 * this.m21 - this.m11 * this.m20, this.m20 * this.m01 - this.m21 * this.m00, this.m00 * this.m11 - this.m01 * this.m10).normalize();
    }
    
    public Vector3d normalizedPositiveZ(final Vector3d dir) {
        return dir.set(this.m02, this.m12, this.m22);
    }
    
    public Vector3d positiveX(final Vector3d dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveX(dir);
        }
        return this.positiveXGeneric(dir);
    }
    
    private Vector3d positiveXGeneric(final Vector3d dir) {
        return dir.set(this.m11 * this.m22 - this.m12 * this.m21, this.m02 * this.m21 - this.m01 * this.m22, this.m01 * this.m12 - this.m02 * this.m11).normalize();
    }
    
    public Vector3d normalizedPositiveX(final Vector3d dir) {
        return dir.set(this.m00, this.m10, this.m20);
    }
    
    public Vector3d positiveY(final Vector3d dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveY(dir);
        }
        return this.positiveYGeneric(dir);
    }
    
    private Vector3d positiveYGeneric(final Vector3d dir) {
        return dir.set(this.m12 * this.m20 - this.m10 * this.m22, this.m00 * this.m22 - this.m02 * this.m20, this.m02 * this.m10 - this.m00 * this.m12).normalize();
    }
    
    public Vector3d normalizedPositiveY(final Vector3d dir) {
        return dir.set(this.m01, this.m11, this.m21);
    }
    
    public Vector3d originAffine(final Vector3d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        dest.x = -this.m10 * j + this.m11 * h - this.m12 * g;
        dest.y = this.m00 * j - this.m01 * h + this.m02 * g;
        dest.z = -this.m30 * d + this.m31 * b - this.m32 * a;
        return dest;
    }
    
    public Vector3d origin(final Vector3d dest) {
        if ((this.properties & 0x2) != 0x0) {
            return this.originAffine(dest);
        }
        return this.originGeneric(dest);
    }
    
    private Vector3d originGeneric(final Vector3d dest) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double c = this.m00 * this.m13 - this.m03 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double e = this.m01 * this.m13 - this.m03 * this.m11;
        final double f = this.m02 * this.m13 - this.m03 * this.m12;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double i = this.m20 * this.m33 - this.m23 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        final double k = this.m21 * this.m33 - this.m23 * this.m31;
        final double l = this.m22 * this.m33 - this.m23 * this.m32;
        final double det = a * l - b * k + c * j + d * i - e * h + f * g;
        final double invDet = 1.0 / det;
        final double nm30 = (-this.m10 * j + this.m11 * h - this.m12 * g) * invDet;
        final double nm31 = (this.m00 * j - this.m01 * h + this.m02 * g) * invDet;
        final double nm32 = (-this.m30 * d + this.m31 * b - this.m32 * a) * invDet;
        final double nm33 = det / (this.m20 * d - this.m21 * b + this.m22 * a);
        final double x = nm30 * nm33;
        final double y = nm31 * nm33;
        final double z = nm32 * nm33;
        return dest.set(x, y, z);
    }
    
    public Matrix4d shadow(final Vector4dc light, final double a, final double b, final double c, final double d) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, this);
    }
    
    public Matrix4d shadow(final Vector4dc light, final double a, final double b, final double c, final double d, final Matrix4d dest) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final double a, final double b, final double c, final double d) {
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, this);
    }
    
    public Matrix4d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final double a, final double b, final double c, final double d, final Matrix4d dest) {
        final double invPlaneLen = Math.invsqrt(a * a + b * b + c * c);
        final double an = a * invPlaneLen;
        final double bn = b * invPlaneLen;
        final double cn = c * invPlaneLen;
        final double dn = d * invPlaneLen;
        final double dot = an * lightX + bn * lightY + cn * lightZ + dn * lightW;
        final double rm00 = dot - an * lightX;
        final double rm2 = -an * lightY;
        final double rm3 = -an * lightZ;
        final double rm4 = -an * lightW;
        final double rm5 = -bn * lightX;
        final double rm6 = dot - bn * lightY;
        final double rm7 = -bn * lightZ;
        final double rm8 = -bn * lightW;
        final double rm9 = -cn * lightX;
        final double rm10 = -cn * lightY;
        final double rm11 = dot - cn * lightZ;
        final double rm12 = -cn * lightW;
        final double rm13 = -dn * lightX;
        final double rm14 = -dn * lightY;
        final double rm15 = -dn * lightZ;
        final double rm16 = dot - dn * lightW;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3 + this.m30 * rm4;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3 + this.m31 * rm4;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3 + this.m32 * rm4;
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3 + this.m33 * rm4;
        final double nm5 = this.m00 * rm5 + this.m10 * rm6 + this.m20 * rm7 + this.m30 * rm8;
        final double nm6 = this.m01 * rm5 + this.m11 * rm6 + this.m21 * rm7 + this.m31 * rm8;
        final double nm7 = this.m02 * rm5 + this.m12 * rm6 + this.m22 * rm7 + this.m32 * rm8;
        final double nm8 = this.m03 * rm5 + this.m13 * rm6 + this.m23 * rm7 + this.m33 * rm8;
        final double nm9 = this.m00 * rm9 + this.m10 * rm10 + this.m20 * rm11 + this.m30 * rm12;
        final double nm10 = this.m01 * rm9 + this.m11 * rm10 + this.m21 * rm11 + this.m31 * rm12;
        final double nm11 = this.m02 * rm9 + this.m12 * rm10 + this.m22 * rm11 + this.m32 * rm12;
        final double nm12 = this.m03 * rm9 + this.m13 * rm10 + this.m23 * rm11 + this.m33 * rm12;
        dest._m30(this.m00 * rm13 + this.m10 * rm14 + this.m20 * rm15 + this.m30 * rm16)._m31(this.m01 * rm13 + this.m11 * rm14 + this.m21 * rm15 + this.m31 * rm16)._m32(this.m02 * rm13 + this.m12 * rm14 + this.m22 * rm15 + this.m32 * rm16)._m33(this.m03 * rm13 + this.m13 * rm14 + this.m23 * rm15 + this.m33 * rm16)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4d shadow(final Vector4dc light, final Matrix4dc planeTransform, final Matrix4d dest) {
        final double a = planeTransform.m10();
        final double b = planeTransform.m11();
        final double c = planeTransform.m12();
        final double d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4d shadow(final Vector4d light, final Matrix4d planeTransform) {
        return this.shadow(light, planeTransform, this);
    }
    
    public Matrix4d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final Matrix4dc planeTransform, final Matrix4d dest) {
        final double a = planeTransform.m10();
        final double b = planeTransform.m11();
        final double c = planeTransform.m12();
        final double d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, dest);
    }
    
    public Matrix4d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final Matrix4dc planeTransform) {
        return this.shadow(lightX, lightY, lightZ, lightW, planeTransform, this);
    }
    
    public Matrix4d billboardCylindrical(final Vector3dc objPos, final Vector3dc targetPos, final Vector3dc up) {
        double dirX = targetPos.x() - objPos.x();
        double dirY = targetPos.y() - objPos.y();
        double dirZ = targetPos.z() - objPos.z();
        double leftX = up.y() * dirZ - up.z() * dirY;
        double leftY = up.z() * dirX - up.x() * dirZ;
        double leftZ = up.x() * dirY - up.y() * dirX;
        final double invLeftLen = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLen;
        leftY *= invLeftLen;
        leftZ *= invLeftLen;
        dirX = leftY * up.z() - leftZ * up.y();
        dirY = leftZ * up.x() - leftX * up.z();
        dirZ = leftX * up.y() - leftY * up.x();
        final double invDirLen = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLen;
        dirY *= invDirLen;
        dirZ *= invDirLen;
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m03(0.0)._m10(up.x())._m11(up.y())._m12(up.z())._m13(0.0)._m20(dirX)._m21(dirY)._m22(dirZ)._m23(0.0)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d billboardSpherical(final Vector3dc objPos, final Vector3dc targetPos, final Vector3dc up) {
        double dirX = targetPos.x() - objPos.x();
        double dirY = targetPos.y() - objPos.y();
        double dirZ = targetPos.z() - objPos.z();
        final double invDirLen = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLen;
        dirY *= invDirLen;
        dirZ *= invDirLen;
        double leftX = up.y() * dirZ - up.z() * dirY;
        double leftY = up.z() * dirX - up.x() * dirZ;
        double leftZ = up.x() * dirY - up.y() * dirX;
        final double invLeftLen = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLen;
        leftY *= invLeftLen;
        leftZ *= invLeftLen;
        final double upX = dirY * leftZ - dirZ * leftY;
        final double upY = dirZ * leftX - dirX * leftZ;
        final double upZ = dirX * leftY - dirY * leftX;
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m03(0.0)._m10(upX)._m11(upY)._m12(upZ)._m13(0.0)._m20(dirX)._m21(dirY)._m22(dirZ)._m23(0.0)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0).properties = 18;
        return this;
    }
    
    public Matrix4d billboardSpherical(final Vector3dc objPos, final Vector3dc targetPos) {
        final double toDirX = targetPos.x() - objPos.x();
        final double toDirY = targetPos.y() - objPos.y();
        final double toDirZ = targetPos.z() - objPos.z();
        double x = -toDirY;
        double y = toDirX;
        double w = Math.sqrt(toDirX * toDirX + toDirY * toDirY + toDirZ * toDirZ) + toDirZ;
        final double invNorm = Math.invsqrt(x * x + y * y + w * w);
        x *= invNorm;
        y *= invNorm;
        w *= invNorm;
        final double q00 = (x + x) * x;
        final double q2 = (y + y) * y;
        final double q3 = (x + x) * y;
        final double q4 = (x + x) * w;
        final double q5 = (y + y) * w;
        this._m00(1.0 - q2)._m01(q3)._m02(-q5)._m03(0.0)._m10(q3)._m11(1.0 - q00)._m12(q4)._m13(0.0)._m20(q5)._m21(-q4)._m22(1.0 - q2 - q00)._m23(0.0)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0).properties = 18;
        return this;
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
        temp = Double.doubleToLongBits(this.m03);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m10);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m11);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m12);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m13);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m20);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m21);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m22);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m23);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m30);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m31);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m32);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m33);
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
        if (!(obj instanceof Matrix4d)) {
            return false;
        }
        final Matrix4d other = (Matrix4d)obj;
        return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(other.m00) && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(other.m01) && Double.doubleToLongBits(this.m02) == Double.doubleToLongBits(other.m02) && Double.doubleToLongBits(this.m03) == Double.doubleToLongBits(other.m03) && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(other.m10) && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(other.m11) && Double.doubleToLongBits(this.m12) == Double.doubleToLongBits(other.m12) && Double.doubleToLongBits(this.m13) == Double.doubleToLongBits(other.m13) && Double.doubleToLongBits(this.m20) == Double.doubleToLongBits(other.m20) && Double.doubleToLongBits(this.m21) == Double.doubleToLongBits(other.m21) && Double.doubleToLongBits(this.m22) == Double.doubleToLongBits(other.m22) && Double.doubleToLongBits(this.m23) == Double.doubleToLongBits(other.m23) && Double.doubleToLongBits(this.m30) == Double.doubleToLongBits(other.m30) && Double.doubleToLongBits(this.m31) == Double.doubleToLongBits(other.m31) && Double.doubleToLongBits(this.m32) == Double.doubleToLongBits(other.m32) && Double.doubleToLongBits(this.m33) == Double.doubleToLongBits(other.m33);
    }
    
    public boolean equals(final Matrix4dc m, final double delta) {
        return this == m || (m != null && m instanceof Matrix4d && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m02, m.m02(), delta) && Runtime.equals(this.m03, m.m03(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m12, m.m12(), delta) && Runtime.equals(this.m13, m.m13(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta) && Runtime.equals(this.m22, m.m22(), delta) && Runtime.equals(this.m23, m.m23(), delta) && Runtime.equals(this.m30, m.m30(), delta) && Runtime.equals(this.m31, m.m31(), delta) && Runtime.equals(this.m32, m.m32(), delta) && Runtime.equals(this.m33, m.m33(), delta));
    }
    
    public Matrix4d pick(final double x, final double y, final double width, final double height, final int[] viewport, final Matrix4d dest) {
        final double sx = viewport[2] / width;
        final double sy = viewport[3] / height;
        final double tx = (viewport[2] + 2.0 * (viewport[0] - x)) / width;
        final double ty = (viewport[3] + 2.0 * (viewport[1] - y)) / height;
        dest._m30(this.m00 * tx + this.m10 * ty + this.m30)._m31(this.m01 * tx + this.m11 * ty + this.m31)._m32(this.m02 * tx + this.m12 * ty + this.m32)._m33(this.m03 * tx + this.m13 * ty + this.m33)._m00(this.m00 * sx)._m01(this.m01 * sx)._m02(this.m02 * sx)._m03(this.m03 * sx)._m10(this.m10 * sy)._m11(this.m11 * sy)._m12(this.m12 * sy)._m13(this.m13 * sy)._properties(0);
        return dest;
    }
    
    public Matrix4d pick(final double x, final double y, final double width, final double height, final int[] viewport) {
        return this.pick(x, y, width, height, viewport, this);
    }
    
    public boolean isAffine() {
        return this.m03 == 0.0 && this.m13 == 0.0 && this.m23 == 0.0 && this.m33 == 1.0;
    }
    
    public Matrix4d swap(final Matrix4d other) {
        double tmp = this.m00;
        this.m00 = other.m00;
        other.m00 = tmp;
        tmp = this.m01;
        this.m01 = other.m01;
        other.m01 = tmp;
        tmp = this.m02;
        this.m02 = other.m02;
        other.m02 = tmp;
        tmp = this.m03;
        this.m03 = other.m03;
        other.m03 = tmp;
        tmp = this.m10;
        this.m10 = other.m10;
        other.m10 = tmp;
        tmp = this.m11;
        this.m11 = other.m11;
        other.m11 = tmp;
        tmp = this.m12;
        this.m12 = other.m12;
        other.m12 = tmp;
        tmp = this.m13;
        this.m13 = other.m13;
        other.m13 = tmp;
        tmp = this.m20;
        this.m20 = other.m20;
        other.m20 = tmp;
        tmp = this.m21;
        this.m21 = other.m21;
        other.m21 = tmp;
        tmp = this.m22;
        this.m22 = other.m22;
        other.m22 = tmp;
        tmp = this.m23;
        this.m23 = other.m23;
        other.m23 = tmp;
        tmp = this.m30;
        this.m30 = other.m30;
        other.m30 = tmp;
        tmp = this.m31;
        this.m31 = other.m31;
        other.m31 = tmp;
        tmp = this.m32;
        this.m32 = other.m32;
        other.m32 = tmp;
        tmp = this.m33;
        this.m33 = other.m33;
        other.m33 = tmp;
        final int props = this.properties;
        this.properties = other.properties;
        other.properties = props;
        return this;
    }
    
    public Matrix4d arcball(final double radius, final double centerX, final double centerY, final double centerZ, final double angleX, final double angleY, final Matrix4d dest) {
        final double m30 = this.m20 * -radius + this.m30;
        final double m31 = this.m21 * -radius + this.m31;
        final double m32 = this.m22 * -radius + this.m32;
        final double m33 = this.m23 * -radius + this.m33;
        double sin = Math.sin(angleX);
        double cos = Math.cosFromSin(sin, angleX);
        final double nm10 = this.m10 * cos + this.m20 * sin;
        final double nm11 = this.m11 * cos + this.m21 * sin;
        final double nm12 = this.m12 * cos + this.m22 * sin;
        final double nm13 = this.m13 * cos + this.m23 * sin;
        final double m34 = this.m20 * cos - this.m10 * sin;
        final double m35 = this.m21 * cos - this.m11 * sin;
        final double m36 = this.m22 * cos - this.m12 * sin;
        final double m37 = this.m23 * cos - this.m13 * sin;
        sin = Math.sin(angleY);
        cos = Math.cosFromSin(sin, angleY);
        final double nm14 = this.m00 * cos - m34 * sin;
        final double nm15 = this.m01 * cos - m35 * sin;
        final double nm16 = this.m02 * cos - m36 * sin;
        final double nm17 = this.m03 * cos - m37 * sin;
        final double nm18 = this.m00 * sin + m34 * cos;
        final double nm19 = this.m01 * sin + m35 * cos;
        final double nm20 = this.m02 * sin + m36 * cos;
        final double nm21 = this.m03 * sin + m37 * cos;
        dest._m30(-nm14 * centerX - nm10 * centerY - nm18 * centerZ + m30)._m31(-nm15 * centerX - nm11 * centerY - nm19 * centerZ + m31)._m32(-nm16 * centerX - nm12 * centerY - nm20 * centerZ + m32)._m33(-nm17 * centerX - nm13 * centerY - nm21 * centerZ + m33)._m20(nm18)._m21(nm19)._m22(nm20)._m23(nm21)._m10(nm10)._m11(nm11)._m12(nm12)._m13(nm13)._m00(nm14)._m01(nm15)._m02(nm16)._m03(nm17)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d arcball(final double radius, final Vector3dc center, final double angleX, final double angleY, final Matrix4d dest) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, dest);
    }
    
    public Matrix4d arcball(final double radius, final double centerX, final double centerY, final double centerZ, final double angleX, final double angleY) {
        return this.arcball(radius, centerX, centerY, centerZ, angleX, angleY, this);
    }
    
    public Matrix4d arcball(final double radius, final Vector3dc center, final double angleX, final double angleY) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, this);
    }
    
    public Matrix4d frustumAabb(final Vector3d min, final Vector3d max) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        for (int t = 0; t < 8; ++t) {
            final double x = ((t & 0x1) << 1) - 1.0;
            final double y = ((t >>> 1 & 0x1) << 1) - 1.0;
            final double z = ((t >>> 2 & 0x1) << 1) - 1.0;
            final double invW = 1.0 / (this.m03 * x + this.m13 * y + this.m23 * z + this.m33);
            final double nx = (this.m00 * x + this.m10 * y + this.m20 * z + this.m30) * invW;
            final double ny = (this.m01 * x + this.m11 * y + this.m21 * z + this.m31) * invW;
            final double nz = (this.m02 * x + this.m12 * y + this.m22 * z + this.m32) * invW;
            minX = ((minX < nx) ? minX : nx);
            minY = ((minY < ny) ? minY : ny);
            minZ = ((minZ < nz) ? minZ : nz);
            maxX = ((maxX > nx) ? maxX : nx);
            maxY = ((maxY > ny) ? maxY : ny);
            maxZ = ((maxZ > nz) ? maxZ : nz);
        }
        min.x = minX;
        min.y = minY;
        min.z = minZ;
        max.x = maxX;
        max.y = maxY;
        max.z = maxZ;
        return this;
    }
    
    public Matrix4d projectedGridRange(final Matrix4dc projector, final double sLower, final double sUpper, final Matrix4d dest) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        boolean intersection = false;
        for (int t = 0; t < 12; ++t) {
            double c0X;
            double c1X;
            double c0Y;
            double c1Y;
            double c0Z;
            double c1Z;
            if (t < 4) {
                c0X = -1.0;
                c1X = 1.0;
                c1Y = (c0Y = ((t & 0x1) << 1) - 1.0);
                c1Z = (c0Z = ((t >>> 1 & 0x1) << 1) - 1.0);
            }
            else if (t < 8) {
                c0Y = -1.0;
                c1Y = 1.0;
                c1X = (c0X = ((t & 0x1) << 1) - 1.0);
                c1Z = (c0Z = ((t >>> 1 & 0x1) << 1) - 1.0);
            }
            else {
                c0Z = -1.0;
                c1Z = 1.0;
                c1X = (c0X = ((t & 0x1) << 1) - 1.0);
                c1Y = (c0Y = ((t >>> 1 & 0x1) << 1) - 1.0);
            }
            double invW = 1.0 / (this.m03 * c0X + this.m13 * c0Y + this.m23 * c0Z + this.m33);
            final double p0x = (this.m00 * c0X + this.m10 * c0Y + this.m20 * c0Z + this.m30) * invW;
            final double p0y = (this.m01 * c0X + this.m11 * c0Y + this.m21 * c0Z + this.m31) * invW;
            final double p0z = (this.m02 * c0X + this.m12 * c0Y + this.m22 * c0Z + this.m32) * invW;
            invW = 1.0 / (this.m03 * c1X + this.m13 * c1Y + this.m23 * c1Z + this.m33);
            final double p1x = (this.m00 * c1X + this.m10 * c1Y + this.m20 * c1Z + this.m30) * invW;
            final double p1y = (this.m01 * c1X + this.m11 * c1Y + this.m21 * c1Z + this.m31) * invW;
            final double p1z = (this.m02 * c1X + this.m12 * c1Y + this.m22 * c1Z + this.m32) * invW;
            final double dirX = p1x - p0x;
            final double dirY = p1y - p0y;
            final double dirZ = p1z - p0z;
            final double invDenom = 1.0 / dirY;
            for (int s = 0; s < 2; ++s) {
                final double isectT = -(p0y + ((s == 0) ? sLower : sUpper)) * invDenom;
                if (isectT >= 0.0 && isectT <= 1.0) {
                    intersection = true;
                    final double ix = p0x + isectT * dirX;
                    final double iz = p0z + isectT * dirZ;
                    invW = 1.0 / (projector.m03() * ix + projector.m23() * iz + projector.m33());
                    final double px = (projector.m00() * ix + projector.m20() * iz + projector.m30()) * invW;
                    final double py = (projector.m01() * ix + projector.m21() * iz + projector.m31()) * invW;
                    minX = ((minX < px) ? minX : px);
                    minY = ((minY < py) ? minY : py);
                    maxX = ((maxX > px) ? maxX : px);
                    maxY = ((maxY > py) ? maxY : py);
                }
            }
        }
        if (!intersection) {
            return null;
        }
        dest.set(maxX - minX, 0.0, 0.0, 0.0, 0.0, maxY - minY, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, minX, minY, 0.0, 1.0)._properties(2);
        return dest;
    }
    
    public Matrix4d perspectiveFrustumSlice(final double near, final double far, final Matrix4d dest) {
        final double invOldNear = (this.m23 + this.m22) / this.m32;
        final double invNearFar = 1.0 / (near - far);
        dest._m00(this.m00 * invOldNear * near)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11 * invOldNear * near)._m12(this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22((far + near) * invNearFar)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32((far + far) * near * invNearFar)._m33(this.m33)._properties(this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4d orthoCrop(final Matrix4dc view, final Matrix4d dest) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        for (int t = 0; t < 8; ++t) {
            final double x = ((t & 0x1) << 1) - 1.0;
            final double y = ((t >>> 1 & 0x1) << 1) - 1.0;
            final double z = ((t >>> 2 & 0x1) << 1) - 1.0;
            double invW = 1.0 / (this.m03 * x + this.m13 * y + this.m23 * z + this.m33);
            final double wx = (this.m00 * x + this.m10 * y + this.m20 * z + this.m30) * invW;
            final double wy = (this.m01 * x + this.m11 * y + this.m21 * z + this.m31) * invW;
            final double wz = (this.m02 * x + this.m12 * y + this.m22 * z + this.m32) * invW;
            invW = 1.0 / (view.m03() * wx + view.m13() * wy + view.m23() * wz + view.m33());
            final double vx = view.m00() * wx + view.m10() * wy + view.m20() * wz + view.m30();
            final double vy = view.m01() * wx + view.m11() * wy + view.m21() * wz + view.m31();
            final double vz = (view.m02() * wx + view.m12() * wy + view.m22() * wz + view.m32()) * invW;
            minX = ((minX < vx) ? minX : vx);
            maxX = ((maxX > vx) ? maxX : vx);
            minY = ((minY < vy) ? minY : vy);
            maxY = ((maxY > vy) ? maxY : vy);
            minZ = ((minZ < vz) ? minZ : vz);
            maxZ = ((maxZ > vz) ? maxZ : vz);
        }
        return dest.setOrtho(minX, maxX, minY, maxY, -maxZ, -minZ);
    }
    
    public Matrix4d trapezoidCrop(final double p0x, final double p0y, final double p1x, final double p1y, final double p2x, final double p2y, final double p3x, final double p3y) {
        final double aX = p1y - p0y;
        double nm00;
        final double aY = nm00 = p0x - p1x;
        double nm2 = -aX;
        double nm3 = aX * p0y - aY * p0x;
        double nm4 = aX;
        double nm5 = aY;
        double nm6 = -(aX * p0x + aY * p0y);
        final double c3x = nm00 * p3x + nm2 * p3y + nm3;
        final double c3y = nm4 * p3x + nm5 * p3y + nm6;
        final double s = -c3x / c3y;
        nm00 += s * nm4;
        nm2 += s * nm5;
        nm3 += s * nm6;
        final double d1x = nm00 * p1x + nm2 * p1y + nm3;
        final double d2x = nm00 * p2x + nm2 * p2y + nm3;
        final double d = d1x * c3y / (d2x - d1x);
        nm6 += d;
        final double sx = 2.0 / d2x;
        final double sy = 1.0 / (c3y + d);
        final double u = (sy + sy) * d / (1.0 - sy * d);
        final double m03 = nm4 * sy;
        final double m4 = nm5 * sy;
        final double m5 = nm6 * sy;
        nm4 = (u + 1.0) * m03;
        nm5 = (u + 1.0) * m4;
        nm6 = (u + 1.0) * m5 - u;
        nm00 = sx * nm00 - m03;
        nm2 = sx * nm2 - m4;
        nm3 = sx * nm3 - m5;
        this.set(nm00, nm4, 0.0, m03, nm2, nm5, 0.0, m4, 0.0, 0.0, 1.0, 0.0, nm3, nm6, 0.0, m5);
        this.properties = 0;
        return this;
    }
    
    public Matrix4d transformAab(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Vector3d outMin, final Vector3d outMax) {
        final double xax = this.m00 * minX;
        final double xay = this.m01 * minX;
        final double xaz = this.m02 * minX;
        final double xbx = this.m00 * maxX;
        final double xby = this.m01 * maxX;
        final double xbz = this.m02 * maxX;
        final double yax = this.m10 * minY;
        final double yay = this.m11 * minY;
        final double yaz = this.m12 * minY;
        final double ybx = this.m10 * maxY;
        final double yby = this.m11 * maxY;
        final double ybz = this.m12 * maxY;
        final double zax = this.m20 * minZ;
        final double zay = this.m21 * minZ;
        final double zaz = this.m22 * minZ;
        final double zbx = this.m20 * maxZ;
        final double zby = this.m21 * maxZ;
        final double zbz = this.m22 * maxZ;
        double xminx;
        double xmaxx;
        if (xax < xbx) {
            xminx = xax;
            xmaxx = xbx;
        }
        else {
            xminx = xbx;
            xmaxx = xax;
        }
        double xminy;
        double xmaxy;
        if (xay < xby) {
            xminy = xay;
            xmaxy = xby;
        }
        else {
            xminy = xby;
            xmaxy = xay;
        }
        double xminz;
        double xmaxz;
        if (xaz < xbz) {
            xminz = xaz;
            xmaxz = xbz;
        }
        else {
            xminz = xbz;
            xmaxz = xaz;
        }
        double yminx;
        double ymaxx;
        if (yax < ybx) {
            yminx = yax;
            ymaxx = ybx;
        }
        else {
            yminx = ybx;
            ymaxx = yax;
        }
        double yminy;
        double ymaxy;
        if (yay < yby) {
            yminy = yay;
            ymaxy = yby;
        }
        else {
            yminy = yby;
            ymaxy = yay;
        }
        double yminz;
        double ymaxz;
        if (yaz < ybz) {
            yminz = yaz;
            ymaxz = ybz;
        }
        else {
            yminz = ybz;
            ymaxz = yaz;
        }
        double zminx;
        double zmaxx;
        if (zax < zbx) {
            zminx = zax;
            zmaxx = zbx;
        }
        else {
            zminx = zbx;
            zmaxx = zax;
        }
        double zminy;
        double zmaxy;
        if (zay < zby) {
            zminy = zay;
            zmaxy = zby;
        }
        else {
            zminy = zby;
            zmaxy = zay;
        }
        double zminz;
        double zmaxz;
        if (zaz < zbz) {
            zminz = zaz;
            zmaxz = zbz;
        }
        else {
            zminz = zbz;
            zmaxz = zaz;
        }
        outMin.x = xminx + yminx + zminx + this.m30;
        outMin.y = xminy + yminy + zminy + this.m31;
        outMin.z = xminz + yminz + zminz + this.m32;
        outMax.x = xmaxx + ymaxx + zmaxx + this.m30;
        outMax.y = xmaxy + ymaxy + zmaxy + this.m31;
        outMax.z = xmaxz + ymaxz + zmaxz + this.m32;
        return this;
    }
    
    public Matrix4d transformAab(final Vector3dc min, final Vector3dc max, final Vector3d outMin, final Vector3d outMax) {
        return this.transformAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), outMin, outMax);
    }
    
    public Matrix4d lerp(final Matrix4dc other, final double t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix4d lerp(final Matrix4dc other, final double t, final Matrix4d dest) {
        dest._m00(Math.fma(other.m00() - this.m00, t, this.m00))._m01(Math.fma(other.m01() - this.m01, t, this.m01))._m02(Math.fma(other.m02() - this.m02, t, this.m02))._m03(Math.fma(other.m03() - this.m03, t, this.m03))._m10(Math.fma(other.m10() - this.m10, t, this.m10))._m11(Math.fma(other.m11() - this.m11, t, this.m11))._m12(Math.fma(other.m12() - this.m12, t, this.m12))._m13(Math.fma(other.m13() - this.m13, t, this.m13))._m20(Math.fma(other.m20() - this.m20, t, this.m20))._m21(Math.fma(other.m21() - this.m21, t, this.m21))._m22(Math.fma(other.m22() - this.m22, t, this.m22))._m23(Math.fma(other.m23() - this.m23, t, this.m23))._m30(Math.fma(other.m30() - this.m30, t, this.m30))._m31(Math.fma(other.m31() - this.m31, t, this.m31))._m32(Math.fma(other.m32() - this.m32, t, this.m32))._m33(Math.fma(other.m33() - this.m33, t, this.m33))._properties(this.properties & other.properties());
        return dest;
    }
    
    public Matrix4d rotateTowards(final Vector3dc direction, final Vector3dc up, final Matrix4d dest) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4d rotateTowards(final Vector3dc direction, final Vector3dc up) {
        return this.rotateTowards(direction.x(), direction.y(), direction.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ, final Matrix4d dest) {
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
        final double nm4 = this.m03 * rm00 + this.m13 * rm2 + this.m23 * rm3;
        final double nm5 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm6 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm7 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        final double nm8 = this.m03 * rm4 + this.m13 * rm5 + this.m23 * rm6;
        dest._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m23(this.m03 * rm7 + this.m13 * rm8 + this.m23 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d rotationTowards(final Vector3dc dir, final Vector3dc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4d rotationTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
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
        if ((this.properties & 0x4) == 0x0) {
            this._identity();
        }
        this.m00 = leftX;
        this.m01 = leftY;
        this.m02 = leftZ;
        this.m10 = upnX;
        this.m11 = upnY;
        this.m12 = upnZ;
        this.m20 = ndirX;
        this.m21 = ndirY;
        this.m22 = ndirZ;
        this.properties = 18;
        return this;
    }
    
    public Matrix4d translationRotateTowards(final Vector3dc pos, final Vector3dc dir, final Vector3dc up) {
        return this.translationRotateTowards(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4d translationRotateTowards(final double posX, final double posY, final double posZ, final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
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
        this.m03 = 0.0;
        this.m10 = upnX;
        this.m11 = upnY;
        this.m12 = upnZ;
        this.m13 = 0.0;
        this.m20 = ndirX;
        this.m21 = ndirY;
        this.m22 = ndirZ;
        this.m23 = 0.0;
        this.m30 = posX;
        this.m31 = posY;
        this.m32 = posZ;
        this.m33 = 1.0;
        this.properties = 18;
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
    
    public Matrix4d affineSpan(final Vector3d corner, final Vector3d xDir, final Vector3d yDir, final Vector3d zDir) {
        final double a = this.m10 * this.m22;
        final double b = this.m10 * this.m21;
        final double c = this.m10 * this.m02;
        final double d = this.m10 * this.m01;
        final double e = this.m11 * this.m22;
        final double f = this.m11 * this.m20;
        final double g = this.m11 * this.m02;
        final double h = this.m11 * this.m00;
        final double i = this.m12 * this.m21;
        final double j = this.m12 * this.m20;
        final double k = this.m12 * this.m01;
        final double l = this.m12 * this.m00;
        final double m = this.m20 * this.m02;
        final double n = this.m20 * this.m01;
        final double o = this.m21 * this.m02;
        final double p = this.m21 * this.m00;
        final double q = this.m22 * this.m01;
        final double r = this.m22 * this.m00;
        final double s = 1.0 / (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
        final double nm00 = (e - i) * s;
        final double nm2 = (o - q) * s;
        final double nm3 = (k - g) * s;
        final double nm4 = (j - a) * s;
        final double nm5 = (r - m) * s;
        final double nm6 = (c - l) * s;
        final double nm7 = (b - f) * s;
        final double nm8 = (n - p) * s;
        final double nm9 = (h - d) * s;
        corner.x = -nm00 - nm4 - nm7 + (a * this.m31 - b * this.m32 + f * this.m32 - e * this.m30 + i * this.m30 - j * this.m31) * s;
        corner.y = -nm2 - nm5 - nm8 + (m * this.m31 - n * this.m32 + p * this.m32 - o * this.m30 + q * this.m30 - r * this.m31) * s;
        corner.z = -nm3 - nm6 - nm9 + (g * this.m30 - k * this.m30 + l * this.m31 - c * this.m31 + d * this.m32 - h * this.m32) * s;
        xDir.x = 2.0 * nm00;
        xDir.y = 2.0 * nm2;
        xDir.z = 2.0 * nm3;
        yDir.x = 2.0 * nm4;
        yDir.y = 2.0 * nm5;
        yDir.z = 2.0 * nm6;
        zDir.x = 2.0 * nm7;
        zDir.y = 2.0 * nm8;
        zDir.z = 2.0 * nm9;
        return this;
    }
    
    public boolean testPoint(final double x, final double y, final double z) {
        final double nxX = this.m03 + this.m00;
        final double nxY = this.m13 + this.m10;
        final double nxZ = this.m23 + this.m20;
        final double nxW = this.m33 + this.m30;
        final double pxX = this.m03 - this.m00;
        final double pxY = this.m13 - this.m10;
        final double pxZ = this.m23 - this.m20;
        final double pxW = this.m33 - this.m30;
        final double nyX = this.m03 + this.m01;
        final double nyY = this.m13 + this.m11;
        final double nyZ = this.m23 + this.m21;
        final double nyW = this.m33 + this.m31;
        final double pyX = this.m03 - this.m01;
        final double pyY = this.m13 - this.m11;
        final double pyZ = this.m23 - this.m21;
        final double pyW = this.m33 - this.m31;
        final double nzX = this.m03 + this.m02;
        final double nzY = this.m13 + this.m12;
        final double nzZ = this.m23 + this.m22;
        final double nzW = this.m33 + this.m32;
        final double pzX = this.m03 - this.m02;
        final double pzY = this.m13 - this.m12;
        final double pzZ = this.m23 - this.m22;
        final double pzW = this.m33 - this.m32;
        return nxX * x + nxY * y + nxZ * z + nxW >= 0.0 && pxX * x + pxY * y + pxZ * z + pxW >= 0.0 && nyX * x + nyY * y + nyZ * z + nyW >= 0.0 && pyX * x + pyY * y + pyZ * z + pyW >= 0.0 && nzX * x + nzY * y + nzZ * z + nzW >= 0.0 && pzX * x + pzY * y + pzZ * z + pzW >= 0.0;
    }
    
    public boolean testSphere(final double x, final double y, final double z, final double r) {
        double nxX = this.m03 + this.m00;
        double nxY = this.m13 + this.m10;
        double nxZ = this.m23 + this.m20;
        double nxW = this.m33 + this.m30;
        double invl = Math.invsqrt(nxX * nxX + nxY * nxY + nxZ * nxZ);
        nxX *= invl;
        nxY *= invl;
        nxZ *= invl;
        nxW *= invl;
        double pxX = this.m03 - this.m00;
        double pxY = this.m13 - this.m10;
        double pxZ = this.m23 - this.m20;
        double pxW = this.m33 - this.m30;
        invl = Math.invsqrt(pxX * pxX + pxY * pxY + pxZ * pxZ);
        pxX *= invl;
        pxY *= invl;
        pxZ *= invl;
        pxW *= invl;
        double nyX = this.m03 + this.m01;
        double nyY = this.m13 + this.m11;
        double nyZ = this.m23 + this.m21;
        double nyW = this.m33 + this.m31;
        invl = Math.invsqrt(nyX * nyX + nyY * nyY + nyZ * nyZ);
        nyX *= invl;
        nyY *= invl;
        nyZ *= invl;
        nyW *= invl;
        double pyX = this.m03 - this.m01;
        double pyY = this.m13 - this.m11;
        double pyZ = this.m23 - this.m21;
        double pyW = this.m33 - this.m31;
        invl = Math.invsqrt(pyX * pyX + pyY * pyY + pyZ * pyZ);
        pyX *= invl;
        pyY *= invl;
        pyZ *= invl;
        pyW *= invl;
        double nzX = this.m03 + this.m02;
        double nzY = this.m13 + this.m12;
        double nzZ = this.m23 + this.m22;
        double nzW = this.m33 + this.m32;
        invl = Math.invsqrt(nzX * nzX + nzY * nzY + nzZ * nzZ);
        nzX *= invl;
        nzY *= invl;
        nzZ *= invl;
        nzW *= invl;
        double pzX = this.m03 - this.m02;
        double pzY = this.m13 - this.m12;
        double pzZ = this.m23 - this.m22;
        double pzW = this.m33 - this.m32;
        invl = Math.invsqrt(pzX * pzX + pzY * pzY + pzZ * pzZ);
        pzX *= invl;
        pzY *= invl;
        pzZ *= invl;
        pzW *= invl;
        return nxX * x + nxY * y + nxZ * z + nxW >= -r && pxX * x + pxY * y + pxZ * z + pxW >= -r && nyX * x + nyY * y + nyZ * z + nyW >= -r && pyX * x + pyY * y + pyZ * z + pyW >= -r && nzX * x + nzY * y + nzZ * z + nzW >= -r && pzX * x + pzY * y + pzZ * z + pzW >= -r;
    }
    
    public boolean testAab(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        final double nxX = this.m03 + this.m00;
        final double nxY = this.m13 + this.m10;
        final double nxZ = this.m23 + this.m20;
        final double nxW = this.m33 + this.m30;
        final double pxX = this.m03 - this.m00;
        final double pxY = this.m13 - this.m10;
        final double pxZ = this.m23 - this.m20;
        final double pxW = this.m33 - this.m30;
        final double nyX = this.m03 + this.m01;
        final double nyY = this.m13 + this.m11;
        final double nyZ = this.m23 + this.m21;
        final double nyW = this.m33 + this.m31;
        final double pyX = this.m03 - this.m01;
        final double pyY = this.m13 - this.m11;
        final double pyZ = this.m23 - this.m21;
        final double pyW = this.m33 - this.m31;
        final double nzX = this.m03 + this.m02;
        final double nzY = this.m13 + this.m12;
        final double nzZ = this.m23 + this.m22;
        final double nzW = this.m33 + this.m32;
        final double pzX = this.m03 - this.m02;
        final double pzY = this.m13 - this.m12;
        final double pzZ = this.m23 - this.m22;
        final double pzW = this.m33 - this.m32;
        return nxX * ((nxX < 0.0) ? minX : maxX) + nxY * ((nxY < 0.0) ? minY : maxY) + nxZ * ((nxZ < 0.0) ? minZ : maxZ) >= -nxW && pxX * ((pxX < 0.0) ? minX : maxX) + pxY * ((pxY < 0.0) ? minY : maxY) + pxZ * ((pxZ < 0.0) ? minZ : maxZ) >= -pxW && nyX * ((nyX < 0.0) ? minX : maxX) + nyY * ((nyY < 0.0) ? minY : maxY) + nyZ * ((nyZ < 0.0) ? minZ : maxZ) >= -nyW && pyX * ((pyX < 0.0) ? minX : maxX) + pyY * ((pyY < 0.0) ? minY : maxY) + pyZ * ((pyZ < 0.0) ? minZ : maxZ) >= -pyW && nzX * ((nzX < 0.0) ? minX : maxX) + nzY * ((nzY < 0.0) ? minY : maxY) + nzZ * ((nzZ < 0.0) ? minZ : maxZ) >= -nzW && pzX * ((pzX < 0.0) ? minX : maxX) + pzY * ((pzY < 0.0) ? minY : maxY) + pzZ * ((pzZ < 0.0) ? minZ : maxZ) >= -pzW;
    }
    
    public Matrix4d obliqueZ(final double a, final double b) {
        this.m20 += this.m00 * a + this.m10 * b;
        this.m21 += this.m01 * a + this.m11 * b;
        this.m22 += this.m02 * a + this.m12 * b;
        this.properties &= 0x2;
        return this;
    }
    
    public Matrix4d obliqueZ(final double a, final double b, final Matrix4d dest) {
        dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(this.m00 * a + this.m10 * b + this.m20)._m21(this.m01 * a + this.m11 * b + this.m21)._m22(this.m02 * a + this.m12 * b + this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x2);
        return dest;
    }
    
    public static void projViewFromRectangle(final Vector3d eye, final Vector3d p, final Vector3d x, final Vector3d y, final double nearFarDist, final boolean zeroToOne, final Matrix4d projDest, final Matrix4d viewDest) {
        double zx = y.y * x.z - y.z * x.y;
        double zy = y.z * x.x - y.x * x.z;
        double zz = y.x * x.y - y.y * x.x;
        double zd = zx * (p.x - eye.x) + zy * (p.y - eye.y) + zz * (p.z - eye.z);
        final double zs = (zd >= 0.0) ? 1.0 : -1.0;
        zx *= zs;
        zy *= zs;
        zz *= zs;
        zd *= zs;
        viewDest.setLookAt(eye.x, eye.y, eye.z, eye.x + zx, eye.y + zy, eye.z + zz, y.x, y.y, y.z);
        final double px = viewDest.m00 * p.x + viewDest.m10 * p.y + viewDest.m20 * p.z + viewDest.m30;
        final double py = viewDest.m01 * p.x + viewDest.m11 * p.y + viewDest.m21 * p.z + viewDest.m31;
        final double tx = viewDest.m00 * x.x + viewDest.m10 * x.y + viewDest.m20 * x.z;
        final double ty = viewDest.m01 * y.x + viewDest.m11 * y.y + viewDest.m21 * y.z;
        final double len = Math.sqrt(zx * zx + zy * zy + zz * zz);
        double near = zd / len;
        double far;
        if (Double.isInfinite(nearFarDist) && nearFarDist < 0.0) {
            far = near;
            near = Double.POSITIVE_INFINITY;
        }
        else if (Double.isInfinite(nearFarDist) && nearFarDist > 0.0) {
            far = Double.POSITIVE_INFINITY;
        }
        else if (nearFarDist < 0.0) {
            far = near;
            near += nearFarDist;
        }
        else {
            far = near + nearFarDist;
        }
        projDest.setFrustum(px, px + tx, py, py + ty, near, far, zeroToOne);
    }
    
    public Matrix4d withLookAtUp(final Vector3dc up) {
        return this.withLookAtUp(up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4d withLookAtUp(final Vector3dc up, final Matrix4d dest) {
        return this.withLookAtUp(up.x(), up.y(), up.z());
    }
    
    public Matrix4d withLookAtUp(final double upX, final double upY, final double upZ) {
        return this.withLookAtUp(upX, upY, upZ, this);
    }
    
    public Matrix4d withLookAtUp(final double upX, final double upY, final double upZ, final Matrix4d dest) {
        final double y = (upY * this.m21 - upZ * this.m11) * this.m02 + (upZ * this.m01 - upX * this.m21) * this.m12 + (upX * this.m11 - upY * this.m01) * this.m22;
        double x = upX * this.m01 + upY * this.m11 + upZ * this.m21;
        if ((this.properties & 0x10) == 0x0) {
            x *= Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
        }
        final double invsqrt = Math.invsqrt(y * y + x * x);
        final double c = x * invsqrt;
        final double s = y * invsqrt;
        final double nm00 = c * this.m00 - s * this.m01;
        final double nm2 = c * this.m10 - s * this.m11;
        final double nm3 = c * this.m20 - s * this.m21;
        final double nm4 = s * this.m30 + c * this.m31;
        final double nm5 = s * this.m00 + c * this.m01;
        final double nm6 = s * this.m10 + c * this.m11;
        final double nm7 = s * this.m20 + c * this.m21;
        final double nm8 = c * this.m30 - s * this.m31;
        dest._m00(nm00)._m10(nm2)._m20(nm3)._m30(nm8)._m01(nm5)._m11(nm6)._m21(nm7)._m31(nm4);
        if (dest != this) {
            dest._m02(this.m02)._m12(this.m12)._m22(this.m22)._m32(this.m32)._m03(this.m03)._m13(this.m13)._m23(this.m23)._m33(this.m33);
        }
        dest._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4d mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix4d mapXZY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(m10)._m21(m11)._m22(m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix4d mapXZnY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix4d mapXnYnZ(final Matrix4d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix4d mapXnZY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(m10)._m21(m11)._m22(m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix4d mapXnZnY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix4d mapYXZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix4d mapYXnZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix4d mapYZX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix4d mapYZnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix4d mapYnXZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix4d mapYnXnZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix4d mapYnZX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix4d mapYnZnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix4d mapZXY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(m4)._m21(m5)._m22(m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix4d mapZXnY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix4d mapZYX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix4d mapZYnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix4d mapZnXY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(m4)._m21(m5)._m22(m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix4d mapZnXnY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix4d mapZnYX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix4d mapZnYnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix4d mapnXYnZ(final Matrix4d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix4d mapnXZY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(m10)._m21(m11)._m22(m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix4d mapnXZnY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix4d mapnXnYZ(final Matrix4d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix4d mapnXnYnZ(final Matrix4d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix4d mapnXnZY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(m10)._m21(m11)._m22(m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix4d mapnXnZnY(final Matrix4d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix4d mapnYXZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix4d mapnYXnZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix4d mapnYZX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix4d mapnYZnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix4d mapnYnXZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix4d mapnYnXnZ(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix4d mapnYnZX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix4d mapnYnZnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m03(this.m03)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix4d mapnZXY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(m4)._m21(m5)._m22(m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix4d mapnZXnY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(m00)._m11(m2)._m12(m3)._m13(this.m13)._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix4d mapnZYX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix4d mapnZYnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix4d mapnZnXY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(m4)._m21(m5)._m22(m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix4d mapnZnXnY(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13)._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix4d mapnZnYX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(m00)._m21(m2)._m22(m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix4d mapnZnYnX(final Matrix4d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateX() {
        return this._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateX(final Matrix4d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateY() {
        return this._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateY(final Matrix4d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m13(this.m13)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateZ() {
        return this._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._properties(this.properties & 0x12);
    }
    
    public Matrix4d negateZ(final Matrix4d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m03(this.m03)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m13(this.m13)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m23(this.m23)._m30(this.m30)._m31(this.m31)._m32(this.m32)._m33(this.m33)._properties(this.properties & 0x12);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m03) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m13) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m23) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32) && Math.isFinite(this.m33);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
