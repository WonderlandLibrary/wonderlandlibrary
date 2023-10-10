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
import java.io.Externalizable;

public class Matrix4f implements Externalizable, Cloneable, Matrix4fc
{
    private static final long serialVersionUID = 1L;
    float m00;
    float m01;
    float m02;
    float m03;
    float m10;
    float m11;
    float m12;
    float m13;
    float m20;
    float m21;
    float m22;
    float m23;
    float m30;
    float m31;
    float m32;
    float m33;
    int properties;
    
    public Matrix4f() {
        this._m00(1.0f)._m11(1.0f)._m22(1.0f)._m33(1.0f)._properties(30);
    }
    
    public Matrix4f(final Matrix3fc mat) {
        this.set(mat);
    }
    
    public Matrix4f(final Matrix4fc mat) {
        this.set(mat);
    }
    
    public Matrix4f(final Matrix4x3fc mat) {
        this.set(mat);
    }
    
    public Matrix4f(final Matrix4dc mat) {
        this.set(mat);
    }
    
    public Matrix4f(final float m00, final float m01, final float m02, final float m03, final float m10, final float m11, final float m12, final float m13, final float m20, final float m21, final float m22, final float m23, final float m30, final float m31, final float m32, final float m33) {
        this._m00(m00)._m01(m01)._m02(m02)._m03(m03)._m10(m10)._m11(m11)._m12(m12)._m13(m13)._m20(m20)._m21(m21)._m22(m22)._m23(m23)._m30(m30)._m31(m31)._m32(m32)._m33(m33).determineProperties();
    }
    
    public Matrix4f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        this.determineProperties();
    }
    
    public Matrix4f(final Vector4fc col0, final Vector4fc col1, final Vector4fc col2, final Vector4fc col3) {
        this.set(col0, col1, col2, col3);
    }
    
    Matrix4f _properties(final int properties) {
        this.properties = properties;
        return this;
    }
    
    public Matrix4f assume(final int properties) {
        this._properties(properties);
        return this;
    }
    
    public Matrix4f determineProperties() {
        int properties = 0;
        if (this.m03() == 0.0f && this.m13() == 0.0f) {
            if (this.m23() == 0.0f && this.m33() == 1.0f) {
                properties |= 0x2;
                if (this.m00() == 1.0f && this.m01() == 0.0f && this.m02() == 0.0f && this.m10() == 0.0f && this.m11() == 1.0f && this.m12() == 0.0f && this.m20() == 0.0f && this.m21() == 0.0f && this.m22() == 1.0f) {
                    properties |= 0x18;
                    if (this.m30() == 0.0f && this.m31() == 0.0f && this.m32() == 0.0f) {
                        properties |= 0x4;
                    }
                }
            }
            else if (this.m01() == 0.0f && this.m02() == 0.0f && this.m10() == 0.0f && this.m12() == 0.0f && this.m20() == 0.0f && this.m21() == 0.0f && this.m30() == 0.0f && this.m31() == 0.0f && this.m33() == 0.0f) {
                properties |= 0x1;
            }
        }
        this.properties = properties;
        return this;
    }
    
    public int properties() {
        return this.properties;
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
    
    public float m03() {
        return this.m03;
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
    
    public float m13() {
        return this.m13;
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
    
    public float m23() {
        return this.m23;
    }
    
    public float m30() {
        return this.m30;
    }
    
    public float m31() {
        return this.m31;
    }
    
    public float m32() {
        return this.m32;
    }
    
    public float m33() {
        return this.m33;
    }
    
    public Matrix4f m00(final float m00) {
        this.m00 = m00;
        this.properties &= 0xFFFFFFEF;
        if (m00 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4f m01(final float m01) {
        this.m01 = m01;
        this.properties &= 0xFFFFFFEF;
        if (m01 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m02(final float m02) {
        this.m02 = m02;
        this.properties &= 0xFFFFFFEF;
        if (m02 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m03(final float m03) {
        this.m03 = m03;
        if (m03 != 0.0f) {
            this.properties = 0;
        }
        return this;
    }
    
    public Matrix4f m10(final float m10) {
        this.m10 = m10;
        this.properties &= 0xFFFFFFEF;
        if (m10 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m11(final float m11) {
        this.m11 = m11;
        this.properties &= 0xFFFFFFEF;
        if (m11 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4f m12(final float m12) {
        this.m12 = m12;
        this.properties &= 0xFFFFFFEF;
        if (m12 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m13(final float m13) {
        this.m13 = m13;
        if (m13 != 0.0f) {
            this.properties = 0;
        }
        return this;
    }
    
    public Matrix4f m20(final float m20) {
        this.m20 = m20;
        this.properties &= 0xFFFFFFEF;
        if (m20 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m21(final float m21) {
        this.m21 = m21;
        this.properties &= 0xFFFFFFEF;
        if (m21 != 0.0f) {
            this.properties &= 0xFFFFFFF2;
        }
        return this;
    }
    
    public Matrix4f m22(final float m22) {
        this.m22 = m22;
        this.properties &= 0xFFFFFFEF;
        if (m22 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4f m23(final float m23) {
        this.m23 = m23;
        if (m23 != 0.0f) {
            this.properties &= 0xFFFFFFE1;
        }
        return this;
    }
    
    public Matrix4f m30(final float m30) {
        this.m30 = m30;
        if (m30 != 0.0f) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4f m31(final float m31) {
        this.m31 = m31;
        if (m31 != 0.0f) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4f m32(final float m32) {
        this.m32 = m32;
        if (m32 != 0.0f) {
            this.properties &= 0xFFFFFFFA;
        }
        return this;
    }
    
    public Matrix4f m33(final float m33) {
        this.m33 = m33;
        if (m33 != 0.0f) {
            this.properties &= 0xFFFFFFFE;
        }
        if (m33 != 1.0f) {
            this.properties &= 0xFFFFFFE1;
        }
        return this;
    }
    
    Matrix4f _m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix4f _m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix4f _m02(final float m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix4f _m03(final float m03) {
        this.m03 = m03;
        return this;
    }
    
    Matrix4f _m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix4f _m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix4f _m12(final float m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix4f _m13(final float m13) {
        this.m13 = m13;
        return this;
    }
    
    Matrix4f _m20(final float m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix4f _m21(final float m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix4f _m22(final float m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix4f _m23(final float m23) {
        this.m23 = m23;
        return this;
    }
    
    Matrix4f _m30(final float m30) {
        this.m30 = m30;
        return this;
    }
    
    Matrix4f _m31(final float m31) {
        this.m31 = m31;
        return this;
    }
    
    Matrix4f _m32(final float m32) {
        this.m32 = m32;
        return this;
    }
    
    Matrix4f _m33(final float m33) {
        this.m33 = m33;
        return this;
    }
    
    public Matrix4f identity() {
        if ((this.properties & 0x4) != 0x0) {
            return this;
        }
        return this._m00(1.0f)._m01(0.0f)._m02(0.0f)._m03(0.0f)._m10(0.0f)._m11(1.0f)._m12(0.0f)._m13(0.0f)._m20(0.0f)._m21(0.0f)._m22(1.0f)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(30);
    }
    
    public Matrix4f set(final Matrix4fc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(m.m03())._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(m.m13())._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(m.m23())._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(m.m33())._properties(m.properties());
    }
    
    public Matrix4f setTransposed(final Matrix4fc m) {
        if ((m.properties() & 0x4) != 0x0) {
            return this.identity();
        }
        return this.setTransposedInternal(m);
    }
    
    private Matrix4f setTransposedInternal(final Matrix4fc m) {
        final float nm10 = m.m01();
        final float nm11 = m.m21();
        final float nm12 = m.m31();
        final float nm13 = m.m02();
        final float nm14 = m.m12();
        final float nm15 = m.m03();
        final float nm16 = m.m13();
        final float nm17 = m.m23();
        return this._m00(m.m00())._m01(m.m10())._m02(m.m20())._m03(m.m30())._m10(nm10)._m11(m.m11())._m12(nm11)._m13(nm12)._m20(nm13)._m21(nm14)._m22(m.m22())._m23(m.m32())._m30(nm15)._m31(nm16)._m32(nm17)._m33(m.m33())._properties(m.properties() & 0x4);
    }
    
    public Matrix4f set(final Matrix4x3fc m) {
        return this._m00(m.m00())._m01(m.m01())._m02(m.m02())._m03(0.0f)._m10(m.m10())._m11(m.m11())._m12(m.m12())._m13(0.0f)._m20(m.m20())._m21(m.m21())._m22(m.m22())._m23(0.0f)._m30(m.m30())._m31(m.m31())._m32(m.m32())._m33(1.0f)._properties(m.properties() | 0x2);
    }
    
    public Matrix4f set(final Matrix4dc m) {
        return this._m00((float)m.m00())._m01((float)m.m01())._m02((float)m.m02())._m03((float)m.m03())._m10((float)m.m10())._m11((float)m.m11())._m12((float)m.m12())._m13((float)m.m13())._m20((float)m.m20())._m21((float)m.m21())._m22((float)m.m22())._m23((float)m.m23())._m30((float)m.m30())._m31((float)m.m31())._m32((float)m.m32())._m33((float)m.m33())._properties(m.properties());
    }
    
    public Matrix4f set(final Matrix3fc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m03(0.0f)._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m13(0.0f)._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(2);
    }
    
    public Matrix4f set(final AxisAngle4f axisAngle) {
        float x = axisAngle.x;
        float y = axisAngle.y;
        float z = axisAngle.z;
        final float angle = axisAngle.angle;
        double n = Math.sqrt(x * x + y * y + z * z);
        n = 1.0 / n;
        x *= (float)n;
        y *= (float)n;
        z *= (float)n;
        final float s = Math.sin(angle);
        final float c = Math.cosFromSin(s, angle);
        final float omc = 1.0f - c;
        this._m00(c + x * x * omc)._m11(c + y * y * omc)._m22(c + z * z * omc);
        float tmp1 = x * y * omc;
        float tmp2 = z * s;
        this._m10(tmp1 - tmp2)._m01(tmp1 + tmp2);
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this._m20(tmp1 + tmp2)._m02(tmp1 - tmp2);
        tmp1 = y * z * omc;
        tmp2 = x * s;
        return this._m21(tmp1 - tmp2)._m12(tmp1 + tmp2)._m03(0.0f)._m13(0.0f)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f set(final AxisAngle4d axisAngle) {
        double x = axisAngle.x;
        double y = axisAngle.y;
        double z = axisAngle.z;
        final double angle = axisAngle.angle;
        double n = Math.sqrt(x * x + y * y + z * z);
        n = 1.0 / n;
        x *= n;
        y *= n;
        z *= n;
        final double s = Math.sin(angle);
        final double c = Math.cosFromSin(s, angle);
        final double omc = 1.0 - c;
        this._m00((float)(c + x * x * omc))._m11((float)(c + y * y * omc))._m22((float)(c + z * z * omc));
        double tmp1 = x * y * omc;
        double tmp2 = z * s;
        this._m10((float)(tmp1 - tmp2))._m01((float)(tmp1 + tmp2));
        tmp1 = x * z * omc;
        tmp2 = y * s;
        this._m20((float)(tmp1 + tmp2))._m02((float)(tmp1 - tmp2));
        tmp1 = y * z * omc;
        tmp2 = x * s;
        return this._m21((float)(tmp1 - tmp2))._m12((float)(tmp1 + tmp2))._m03(0.0f)._m13(0.0f)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix4f set(final Quaterniondc q) {
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
        return this._m00((float)(w2 + x2 - z2 - y2))._m01((float)(xy + zw + zw + xy))._m02((float)(xz - yw + xz - yw))._m03(0.0f)._m10((float)(-zw + xy - zw + xy))._m11((float)(y2 - z2 + w2 - x2))._m12((float)(yz + yz + xw + xw))._m13(0.0f)._m20((float)(yw + xz + xz + yw))._m21((float)(yz + yz - xw - xw))._m22((float)(z2 - y2 - x2 + w2))._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f set3x3(final Matrix4f mat) {
        MemUtil.INSTANCE.copy3x3(mat, this);
        return this._properties(this.properties & mat.properties & 0xFFFFFFFE);
    }
    
    public Matrix4f set4x3(final Matrix4x3fc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22())._m30(mat.m30())._m31(mat.m31())._m32(mat.m32())._properties(this.properties & mat.properties() & 0xFFFFFFFE);
    }
    
    public Matrix4f set4x3(final Matrix4f mat) {
        MemUtil.INSTANCE.copy4x3(mat, this);
        return this._properties(this.properties & mat.properties & 0xFFFFFFFE);
    }
    
    public Matrix4f mul(final Matrix4fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4f mul(final Matrix4fc right, final Matrix4f dest) {
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
    
    public Matrix4f mul0(final Matrix4fc right) {
        return this.mul0(right, this);
    }
    
    public Matrix4f mul0(final Matrix4fc right, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), right.m00(), Math.fma(this.m10(), right.m01(), Math.fma(this.m20(), right.m02(), this.m30() * right.m03())));
        final float nm2 = Math.fma(this.m01(), right.m00(), Math.fma(this.m11(), right.m01(), Math.fma(this.m21(), right.m02(), this.m31() * right.m03())));
        final float nm3 = Math.fma(this.m02(), right.m00(), Math.fma(this.m12(), right.m01(), Math.fma(this.m22(), right.m02(), this.m32() * right.m03())));
        final float nm4 = Math.fma(this.m03(), right.m00(), Math.fma(this.m13(), right.m01(), Math.fma(this.m23(), right.m02(), this.m33() * right.m03())));
        final float nm5 = Math.fma(this.m00(), right.m10(), Math.fma(this.m10(), right.m11(), Math.fma(this.m20(), right.m12(), this.m30() * right.m13())));
        final float nm6 = Math.fma(this.m01(), right.m10(), Math.fma(this.m11(), right.m11(), Math.fma(this.m21(), right.m12(), this.m31() * right.m13())));
        final float nm7 = Math.fma(this.m02(), right.m10(), Math.fma(this.m12(), right.m11(), Math.fma(this.m22(), right.m12(), this.m32() * right.m13())));
        final float nm8 = Math.fma(this.m03(), right.m10(), Math.fma(this.m13(), right.m11(), Math.fma(this.m23(), right.m12(), this.m33() * right.m13())));
        final float nm9 = Math.fma(this.m00(), right.m20(), Math.fma(this.m10(), right.m21(), Math.fma(this.m20(), right.m22(), this.m30() * right.m23())));
        final float nm10 = Math.fma(this.m01(), right.m20(), Math.fma(this.m11(), right.m21(), Math.fma(this.m21(), right.m22(), this.m31() * right.m23())));
        final float nm11 = Math.fma(this.m02(), right.m20(), Math.fma(this.m12(), right.m21(), Math.fma(this.m22(), right.m22(), this.m32() * right.m23())));
        final float nm12 = Math.fma(this.m03(), right.m20(), Math.fma(this.m13(), right.m21(), Math.fma(this.m23(), right.m22(), this.m33() * right.m23())));
        final float nm13 = Math.fma(this.m00(), right.m30(), Math.fma(this.m10(), right.m31(), Math.fma(this.m20(), right.m32(), this.m30() * right.m33())));
        final float nm14 = Math.fma(this.m01(), right.m30(), Math.fma(this.m11(), right.m31(), Math.fma(this.m21(), right.m32(), this.m31() * right.m33())));
        final float nm15 = Math.fma(this.m02(), right.m30(), Math.fma(this.m12(), right.m31(), Math.fma(this.m22(), right.m32(), this.m32() * right.m33())));
        final float nm16 = Math.fma(this.m03(), right.m30(), Math.fma(this.m13(), right.m31(), Math.fma(this.m23(), right.m32(), this.m33() * right.m33())));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f mul(final float r00, final float r01, final float r02, final float r03, final float r10, final float r11, final float r12, final float r13, final float r20, final float r21, final float r22, final float r23, final float r30, final float r31, final float r32, final float r33) {
        return this.mul(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, this);
    }
    
    public Matrix4f mul(final float r00, final float r01, final float r02, final float r03, final float r10, final float r11, final float r12, final float r13, final float r20, final float r21, final float r22, final float r23, final float r30, final float r31, final float r32, final float r33, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.mulAffineL(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, dest);
        }
        return this.mulGeneric(r00, r01, r02, r03, r10, r11, r12, r13, r20, r21, r22, r23, r30, r31, r32, r33, dest);
    }
    
    private Matrix4f mulAffineL(final float r00, final float r01, final float r02, final float r03, final float r10, final float r11, final float r12, final float r13, final float r20, final float r21, final float r22, final float r23, final float r30, final float r31, final float r32, final float r33, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), r00, Math.fma(this.m10(), r01, Math.fma(this.m20(), r02, this.m30() * r03)));
        final float nm2 = Math.fma(this.m01(), r00, Math.fma(this.m11(), r01, Math.fma(this.m21(), r02, this.m31() * r03)));
        final float nm3 = Math.fma(this.m02(), r00, Math.fma(this.m12(), r01, Math.fma(this.m22(), r02, this.m32() * r03)));
        final float nm4 = r03;
        final float nm5 = Math.fma(this.m00(), r10, Math.fma(this.m10(), r11, Math.fma(this.m20(), r12, this.m30() * r13)));
        final float nm6 = Math.fma(this.m01(), r10, Math.fma(this.m11(), r11, Math.fma(this.m21(), r12, this.m31() * r13)));
        final float nm7 = Math.fma(this.m02(), r10, Math.fma(this.m12(), r11, Math.fma(this.m22(), r12, this.m32() * r13)));
        final float nm8 = r13;
        final float nm9 = Math.fma(this.m00(), r20, Math.fma(this.m10(), r21, Math.fma(this.m20(), r22, this.m30() * r23)));
        final float nm10 = Math.fma(this.m01(), r20, Math.fma(this.m11(), r21, Math.fma(this.m21(), r22, this.m31() * r23)));
        final float nm11 = Math.fma(this.m02(), r20, Math.fma(this.m12(), r21, Math.fma(this.m22(), r22, this.m32() * r23)));
        final float nm12 = r23;
        final float nm13 = Math.fma(this.m00(), r30, Math.fma(this.m10(), r31, Math.fma(this.m20(), r32, this.m30() * r33)));
        final float nm14 = Math.fma(this.m01(), r30, Math.fma(this.m11(), r31, Math.fma(this.m21(), r32, this.m31() * r33)));
        final float nm15 = Math.fma(this.m02(), r30, Math.fma(this.m12(), r31, Math.fma(this.m22(), r32, this.m32() * r33)));
        final float nm16 = r33;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(2);
    }
    
    private Matrix4f mulGeneric(final float r00, final float r01, final float r02, final float r03, final float r10, final float r11, final float r12, final float r13, final float r20, final float r21, final float r22, final float r23, final float r30, final float r31, final float r32, final float r33, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), r00, Math.fma(this.m10(), r01, Math.fma(this.m20(), r02, this.m30() * r03)));
        final float nm2 = Math.fma(this.m01(), r00, Math.fma(this.m11(), r01, Math.fma(this.m21(), r02, this.m31() * r03)));
        final float nm3 = Math.fma(this.m02(), r00, Math.fma(this.m12(), r01, Math.fma(this.m22(), r02, this.m32() * r03)));
        final float nm4 = Math.fma(this.m03(), r00, Math.fma(this.m13(), r01, Math.fma(this.m23(), r02, this.m33() * r03)));
        final float nm5 = Math.fma(this.m00(), r10, Math.fma(this.m10(), r11, Math.fma(this.m20(), r12, this.m30() * r13)));
        final float nm6 = Math.fma(this.m01(), r10, Math.fma(this.m11(), r11, Math.fma(this.m21(), r12, this.m31() * r13)));
        final float nm7 = Math.fma(this.m02(), r10, Math.fma(this.m12(), r11, Math.fma(this.m22(), r12, this.m32() * r13)));
        final float nm8 = Math.fma(this.m03(), r10, Math.fma(this.m13(), r11, Math.fma(this.m23(), r12, this.m33() * r13)));
        final float nm9 = Math.fma(this.m00(), r20, Math.fma(this.m10(), r21, Math.fma(this.m20(), r22, this.m30() * r23)));
        final float nm10 = Math.fma(this.m01(), r20, Math.fma(this.m11(), r21, Math.fma(this.m21(), r22, this.m31() * r23)));
        final float nm11 = Math.fma(this.m02(), r20, Math.fma(this.m12(), r21, Math.fma(this.m22(), r22, this.m32() * r23)));
        final float nm12 = Math.fma(this.m03(), r20, Math.fma(this.m13(), r21, Math.fma(this.m23(), r22, this.m33() * r23)));
        final float nm13 = Math.fma(this.m00(), r30, Math.fma(this.m10(), r31, Math.fma(this.m20(), r32, this.m30() * r33)));
        final float nm14 = Math.fma(this.m01(), r30, Math.fma(this.m11(), r31, Math.fma(this.m21(), r32, this.m31() * r33)));
        final float nm15 = Math.fma(this.m02(), r30, Math.fma(this.m12(), r31, Math.fma(this.m22(), r32, this.m32() * r33)));
        final float nm16 = Math.fma(this.m03(), r30, Math.fma(this.m13(), r31, Math.fma(this.m23(), r32, this.m33() * r33)));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f mul3x3(final float r00, final float r01, final float r02, final float r10, final float r11, final float r12, final float r20, final float r21, final float r22) {
        return this.mul3x3(r00, r01, r02, r10, r11, r12, r20, r21, r22, this);
    }
    
    public Matrix4f mul3x3(final float r00, final float r01, final float r02, final float r10, final float r11, final float r12, final float r20, final float r21, final float r22, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(r00, r01, r02, 0.0f, r10, r11, r12, 0.0f, r20, r21, r22, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        }
        return this.mulGeneric3x3(r00, r01, r02, r10, r11, r12, r20, r21, r22, dest);
    }
    
    private Matrix4f mulGeneric3x3(final float r00, final float r01, final float r02, final float r10, final float r11, final float r12, final float r20, final float r21, final float r22, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), r00, Math.fma(this.m10(), r01, this.m20() * r02));
        final float nm2 = Math.fma(this.m01(), r00, Math.fma(this.m11(), r01, this.m21() * r02));
        final float nm3 = Math.fma(this.m02(), r00, Math.fma(this.m12(), r01, this.m22() * r02));
        final float nm4 = Math.fma(this.m03(), r00, Math.fma(this.m13(), r01, this.m23() * r02));
        final float nm5 = Math.fma(this.m00(), r10, Math.fma(this.m10(), r11, this.m20() * r12));
        final float nm6 = Math.fma(this.m01(), r10, Math.fma(this.m11(), r11, this.m21() * r12));
        final float nm7 = Math.fma(this.m02(), r10, Math.fma(this.m12(), r11, this.m22() * r12));
        final float nm8 = Math.fma(this.m03(), r10, Math.fma(this.m13(), r11, this.m23() * r12));
        final float nm9 = Math.fma(this.m00(), r20, Math.fma(this.m10(), r21, this.m20() * r22));
        final float nm10 = Math.fma(this.m01(), r20, Math.fma(this.m11(), r21, this.m21() * r22));
        final float nm11 = Math.fma(this.m02(), r20, Math.fma(this.m12(), r21, this.m22() * r22));
        final float nm12 = Math.fma(this.m03(), r20, Math.fma(this.m13(), r21, this.m23() * r22));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x2);
    }
    
    public Matrix4f mulLocal(final Matrix4fc left) {
        return this.mulLocal(left, this);
    }
    
    public Matrix4f mulLocal(final Matrix4fc left, final Matrix4f dest) {
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
    
    private Matrix4f mulLocalGeneric(final Matrix4fc left, final Matrix4f dest) {
        final float nm00 = Math.fma(left.m00(), this.m00(), Math.fma(left.m10(), this.m01(), Math.fma(left.m20(), this.m02(), left.m30() * this.m03())));
        final float nm2 = Math.fma(left.m01(), this.m00(), Math.fma(left.m11(), this.m01(), Math.fma(left.m21(), this.m02(), left.m31() * this.m03())));
        final float nm3 = Math.fma(left.m02(), this.m00(), Math.fma(left.m12(), this.m01(), Math.fma(left.m22(), this.m02(), left.m32() * this.m03())));
        final float nm4 = Math.fma(left.m03(), this.m00(), Math.fma(left.m13(), this.m01(), Math.fma(left.m23(), this.m02(), left.m33() * this.m03())));
        final float nm5 = Math.fma(left.m00(), this.m10(), Math.fma(left.m10(), this.m11(), Math.fma(left.m20(), this.m12(), left.m30() * this.m13())));
        final float nm6 = Math.fma(left.m01(), this.m10(), Math.fma(left.m11(), this.m11(), Math.fma(left.m21(), this.m12(), left.m31() * this.m13())));
        final float nm7 = Math.fma(left.m02(), this.m10(), Math.fma(left.m12(), this.m11(), Math.fma(left.m22(), this.m12(), left.m32() * this.m13())));
        final float nm8 = Math.fma(left.m03(), this.m10(), Math.fma(left.m13(), this.m11(), Math.fma(left.m23(), this.m12(), left.m33() * this.m13())));
        final float nm9 = Math.fma(left.m00(), this.m20(), Math.fma(left.m10(), this.m21(), Math.fma(left.m20(), this.m22(), left.m30() * this.m23())));
        final float nm10 = Math.fma(left.m01(), this.m20(), Math.fma(left.m11(), this.m21(), Math.fma(left.m21(), this.m22(), left.m31() * this.m23())));
        final float nm11 = Math.fma(left.m02(), this.m20(), Math.fma(left.m12(), this.m21(), Math.fma(left.m22(), this.m22(), left.m32() * this.m23())));
        final float nm12 = Math.fma(left.m03(), this.m20(), Math.fma(left.m13(), this.m21(), Math.fma(left.m23(), this.m22(), left.m33() * this.m23())));
        final float nm13 = Math.fma(left.m00(), this.m30(), Math.fma(left.m10(), this.m31(), Math.fma(left.m20(), this.m32(), left.m30() * this.m33())));
        final float nm14 = Math.fma(left.m01(), this.m30(), Math.fma(left.m11(), this.m31(), Math.fma(left.m21(), this.m32(), left.m31() * this.m33())));
        final float nm15 = Math.fma(left.m02(), this.m30(), Math.fma(left.m12(), this.m31(), Math.fma(left.m22(), this.m32(), left.m32() * this.m33())));
        final float nm16 = Math.fma(left.m03(), this.m30(), Math.fma(left.m13(), this.m31(), Math.fma(left.m23(), this.m32(), left.m33() * this.m33())));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f mulLocalAffine(final Matrix4fc left) {
        return this.mulLocalAffine(left, this);
    }
    
    public Matrix4f mulLocalAffine(final Matrix4fc left, final Matrix4f dest) {
        final float nm00 = left.m00() * this.m00() + left.m10() * this.m01() + left.m20() * this.m02();
        final float nm2 = left.m01() * this.m00() + left.m11() * this.m01() + left.m21() * this.m02();
        final float nm3 = left.m02() * this.m00() + left.m12() * this.m01() + left.m22() * this.m02();
        final float nm4 = left.m03();
        final float nm5 = left.m00() * this.m10() + left.m10() * this.m11() + left.m20() * this.m12();
        final float nm6 = left.m01() * this.m10() + left.m11() * this.m11() + left.m21() * this.m12();
        final float nm7 = left.m02() * this.m10() + left.m12() * this.m11() + left.m22() * this.m12();
        final float nm8 = left.m13();
        final float nm9 = left.m00() * this.m20() + left.m10() * this.m21() + left.m20() * this.m22();
        final float nm10 = left.m01() * this.m20() + left.m11() * this.m21() + left.m21() * this.m22();
        final float nm11 = left.m02() * this.m20() + left.m12() * this.m21() + left.m22() * this.m22();
        final float nm12 = left.m23();
        final float nm13 = left.m00() * this.m30() + left.m10() * this.m31() + left.m20() * this.m32() + left.m30();
        final float nm14 = left.m01() * this.m30() + left.m11() * this.m31() + left.m21() * this.m32() + left.m31();
        final float nm15 = left.m02() * this.m30() + left.m12() * this.m31() + left.m22() * this.m32() + left.m32();
        final float nm16 = left.m33();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0x2 | (this.properties() & left.properties() & 0x10));
    }
    
    public Matrix4f mul(final Matrix4x3fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4f mul(final Matrix4x3fc right, final Matrix4f dest) {
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
    
    private Matrix4f mulTranslation(final Matrix4x3fc right, final Matrix4f dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m03(this.m03())._m10(right.m10())._m11(right.m11())._m12(right.m12())._m13(this.m13())._m20(right.m20())._m21(right.m21())._m22(right.m22())._m23(this.m23())._m30(right.m30() + this.m30())._m31(right.m31() + this.m31())._m32(right.m32() + this.m32())._m33(this.m33())._properties(0x2 | (right.properties() & 0x10));
    }
    
    private Matrix4f mulAffine(final Matrix4x3fc right, final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        final float m7 = this.m20();
        final float m8 = this.m21();
        final float m9 = this.m22();
        final float rm00 = right.m00();
        final float rm2 = right.m01();
        final float rm3 = right.m02();
        final float rm4 = right.m10();
        final float rm5 = right.m11();
        final float rm6 = right.m12();
        final float rm7 = right.m20();
        final float rm8 = right.m21();
        final float rm9 = right.m22();
        final float rm10 = right.m30();
        final float rm11 = right.m31();
        final float rm12 = right.m32();
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m03(this.m03())._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m13(this.m13())._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m23(this.m23())._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30()))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31()))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32()))))._m33(this.m33())._properties(0x2 | (this.properties & right.properties() & 0x10));
    }
    
    private Matrix4f mulGeneric(final Matrix4x3fc right, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), right.m00(), Math.fma(this.m10(), right.m01(), this.m20() * right.m02()));
        final float nm2 = Math.fma(this.m01(), right.m00(), Math.fma(this.m11(), right.m01(), this.m21() * right.m02()));
        final float nm3 = Math.fma(this.m02(), right.m00(), Math.fma(this.m12(), right.m01(), this.m22() * right.m02()));
        final float nm4 = Math.fma(this.m03(), right.m00(), Math.fma(this.m13(), right.m01(), this.m23() * right.m02()));
        final float nm5 = Math.fma(this.m00(), right.m10(), Math.fma(this.m10(), right.m11(), this.m20() * right.m12()));
        final float nm6 = Math.fma(this.m01(), right.m10(), Math.fma(this.m11(), right.m11(), this.m21() * right.m12()));
        final float nm7 = Math.fma(this.m02(), right.m10(), Math.fma(this.m12(), right.m11(), this.m22() * right.m12()));
        final float nm8 = Math.fma(this.m03(), right.m10(), Math.fma(this.m13(), right.m11(), this.m23() * right.m12()));
        final float nm9 = Math.fma(this.m00(), right.m20(), Math.fma(this.m10(), right.m21(), this.m20() * right.m22()));
        final float nm10 = Math.fma(this.m01(), right.m20(), Math.fma(this.m11(), right.m21(), this.m21() * right.m22()));
        final float nm11 = Math.fma(this.m02(), right.m20(), Math.fma(this.m12(), right.m21(), this.m22() * right.m22()));
        final float nm12 = Math.fma(this.m03(), right.m20(), Math.fma(this.m13(), right.m21(), this.m23() * right.m22()));
        final float nm13 = Math.fma(this.m00(), right.m30(), Math.fma(this.m10(), right.m31(), Math.fma(this.m20(), right.m32(), this.m30())));
        final float nm14 = Math.fma(this.m01(), right.m30(), Math.fma(this.m11(), right.m31(), Math.fma(this.m21(), right.m32(), this.m31())));
        final float nm15 = Math.fma(this.m02(), right.m30(), Math.fma(this.m12(), right.m31(), Math.fma(this.m22(), right.m32(), this.m32())));
        final float nm16 = Math.fma(this.m03(), right.m30(), Math.fma(this.m13(), right.m31(), Math.fma(this.m23(), right.m32(), this.m33())));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4f mul(final Matrix3x2fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4f mul(final Matrix3x2fc right, final Matrix4f dest) {
        final float nm00 = this.m00() * right.m00() + this.m10() * right.m01();
        final float nm2 = this.m01() * right.m00() + this.m11() * right.m01();
        final float nm3 = this.m02() * right.m00() + this.m12() * right.m01();
        final float nm4 = this.m03() * right.m00() + this.m13() * right.m01();
        final float nm5 = this.m00() * right.m10() + this.m10() * right.m11();
        final float nm6 = this.m01() * right.m10() + this.m11() * right.m11();
        final float nm7 = this.m02() * right.m10() + this.m12() * right.m11();
        final float nm8 = this.m03() * right.m10() + this.m13() * right.m11();
        final float nm9 = this.m00() * right.m20() + this.m10() * right.m21() + this.m30();
        final float nm10 = this.m01() * right.m20() + this.m11() * right.m21() + this.m31();
        final float nm11 = this.m02() * right.m20() + this.m12() * right.m21() + this.m32();
        final float nm12 = this.m03() * right.m20() + this.m13() * right.m21() + this.m33();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(nm9)._m31(nm10)._m32(nm11)._m33(nm12)._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4f mulPerspectiveAffine(final Matrix4fc view) {
        return this.mulPerspectiveAffine(view, this);
    }
    
    public Matrix4f mulPerspectiveAffine(final Matrix4fc view, final Matrix4f dest) {
        final float nm00 = this.m00() * view.m00();
        final float nm2 = this.m11() * view.m01();
        final float nm3 = this.m22() * view.m02();
        final float nm4 = this.m23() * view.m02();
        final float nm5 = this.m00() * view.m10();
        final float nm6 = this.m11() * view.m11();
        final float nm7 = this.m22() * view.m12();
        final float nm8 = this.m23() * view.m12();
        final float nm9 = this.m00() * view.m20();
        final float nm10 = this.m11() * view.m21();
        final float nm11 = this.m22() * view.m22();
        final float nm12 = this.m23() * view.m22();
        final float nm13 = this.m00() * view.m30();
        final float nm14 = this.m11() * view.m31();
        final float nm15 = this.m22() * view.m32() + this.m32();
        final float nm16 = this.m23() * view.m32();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f mulPerspectiveAffine(final Matrix4x3fc view) {
        return this.mulPerspectiveAffine(view, this);
    }
    
    public Matrix4f mulPerspectiveAffine(final Matrix4x3fc view, final Matrix4f dest) {
        final float lm00 = this.m00();
        final float lm2 = this.m11();
        final float lm3 = this.m22();
        final float lm4 = this.m23();
        return dest._m00(lm00 * view.m00())._m01(lm2 * view.m01())._m02(lm3 * view.m02())._m03(lm4 * view.m02())._m10(lm00 * view.m10())._m11(lm2 * view.m11())._m12(lm3 * view.m12())._m13(lm4 * view.m12())._m20(lm00 * view.m20())._m21(lm2 * view.m21())._m22(lm3 * view.m22())._m23(lm4 * view.m22())._m30(lm00 * view.m30())._m31(lm2 * view.m31())._m32(lm3 * view.m32() + this.m32())._m33(lm4 * view.m32())._properties(0);
    }
    
    public Matrix4f mulAffineR(final Matrix4fc right) {
        return this.mulAffineR(right, this);
    }
    
    public Matrix4f mulAffineR(final Matrix4fc right, final Matrix4f dest) {
        final float nm00 = Math.fma(this.m00(), right.m00(), Math.fma(this.m10(), right.m01(), this.m20() * right.m02()));
        final float nm2 = Math.fma(this.m01(), right.m00(), Math.fma(this.m11(), right.m01(), this.m21() * right.m02()));
        final float nm3 = Math.fma(this.m02(), right.m00(), Math.fma(this.m12(), right.m01(), this.m22() * right.m02()));
        final float nm4 = Math.fma(this.m03(), right.m00(), Math.fma(this.m13(), right.m01(), this.m23() * right.m02()));
        final float nm5 = Math.fma(this.m00(), right.m10(), Math.fma(this.m10(), right.m11(), this.m20() * right.m12()));
        final float nm6 = Math.fma(this.m01(), right.m10(), Math.fma(this.m11(), right.m11(), this.m21() * right.m12()));
        final float nm7 = Math.fma(this.m02(), right.m10(), Math.fma(this.m12(), right.m11(), this.m22() * right.m12()));
        final float nm8 = Math.fma(this.m03(), right.m10(), Math.fma(this.m13(), right.m11(), this.m23() * right.m12()));
        final float nm9 = Math.fma(this.m00(), right.m20(), Math.fma(this.m10(), right.m21(), this.m20() * right.m22()));
        final float nm10 = Math.fma(this.m01(), right.m20(), Math.fma(this.m11(), right.m21(), this.m21() * right.m22()));
        final float nm11 = Math.fma(this.m02(), right.m20(), Math.fma(this.m12(), right.m21(), this.m22() * right.m22()));
        final float nm12 = Math.fma(this.m03(), right.m20(), Math.fma(this.m13(), right.m21(), this.m23() * right.m22()));
        final float nm13 = Math.fma(this.m00(), right.m30(), Math.fma(this.m10(), right.m31(), Math.fma(this.m20(), right.m32(), this.m30())));
        final float nm14 = Math.fma(this.m01(), right.m30(), Math.fma(this.m11(), right.m31(), Math.fma(this.m21(), right.m32(), this.m31())));
        final float nm15 = Math.fma(this.m02(), right.m30(), Math.fma(this.m12(), right.m31(), Math.fma(this.m22(), right.m32(), this.m32())));
        final float nm16 = Math.fma(this.m03(), right.m30(), Math.fma(this.m13(), right.m31(), Math.fma(this.m23(), right.m32(), this.m33())));
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4f mulAffine(final Matrix4fc right) {
        return this.mulAffine(right, this);
    }
    
    public Matrix4f mulAffine(final Matrix4fc right, final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        final float m7 = this.m20();
        final float m8 = this.m21();
        final float m9 = this.m22();
        final float rm00 = right.m00();
        final float rm2 = right.m01();
        final float rm3 = right.m02();
        final float rm4 = right.m10();
        final float rm5 = right.m11();
        final float rm6 = right.m12();
        final float rm7 = right.m20();
        final float rm8 = right.m21();
        final float rm9 = right.m22();
        final float rm10 = right.m30();
        final float rm11 = right.m31();
        final float rm12 = right.m32();
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m03(this.m03())._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m13(this.m13())._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m23(this.m23())._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30()))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31()))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32()))))._m33(this.m33())._properties(0x2 | (this.properties & right.properties() & 0x10));
    }
    
    public Matrix4f mulTranslationAffine(final Matrix4fc right, final Matrix4f dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m03(this.m03())._m10(right.m10())._m11(right.m11())._m12(right.m12())._m13(this.m13())._m20(right.m20())._m21(right.m21())._m22(right.m22())._m23(this.m23())._m30(right.m30() + this.m30())._m31(right.m31() + this.m31())._m32(right.m32() + this.m32())._m33(this.m33())._properties(0x2 | (right.properties() & 0x10));
    }
    
    public Matrix4f mulOrthoAffine(final Matrix4fc view) {
        return this.mulOrthoAffine(view, this);
    }
    
    public Matrix4f mulOrthoAffine(final Matrix4fc view, final Matrix4f dest) {
        final float nm00 = this.m00() * view.m00();
        final float nm2 = this.m11() * view.m01();
        final float nm3 = this.m22() * view.m02();
        final float nm4 = this.m00() * view.m10();
        final float nm5 = this.m11() * view.m11();
        final float nm6 = this.m22() * view.m12();
        final float nm7 = this.m00() * view.m20();
        final float nm8 = this.m11() * view.m21();
        final float nm9 = this.m22() * view.m22();
        final float nm10 = this.m00() * view.m30() + this.m30();
        final float nm11 = this.m11() * view.m31() + this.m31();
        final float nm12 = this.m22() * view.m32() + this.m32();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m20(nm7)._m21(nm8)._m22(nm9)._m23(0.0f)._m30(nm10)._m31(nm11)._m32(nm12)._m33(1.0f)._properties(2);
    }
    
    public Matrix4f fma4x3(final Matrix4fc other, final float otherFactor) {
        return this.fma4x3(other, otherFactor, this);
    }
    
    public Matrix4f fma4x3(final Matrix4fc other, final float otherFactor, final Matrix4f dest) {
        dest._m00(Math.fma(other.m00(), otherFactor, this.m00()))._m01(Math.fma(other.m01(), otherFactor, this.m01()))._m02(Math.fma(other.m02(), otherFactor, this.m02()))._m03(this.m03())._m10(Math.fma(other.m10(), otherFactor, this.m10()))._m11(Math.fma(other.m11(), otherFactor, this.m11()))._m12(Math.fma(other.m12(), otherFactor, this.m12()))._m13(this.m13())._m20(Math.fma(other.m20(), otherFactor, this.m20()))._m21(Math.fma(other.m21(), otherFactor, this.m21()))._m22(Math.fma(other.m22(), otherFactor, this.m22()))._m23(this.m23())._m30(Math.fma(other.m30(), otherFactor, this.m30()))._m31(Math.fma(other.m31(), otherFactor, this.m31()))._m32(Math.fma(other.m32(), otherFactor, this.m32()))._m33(this.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f add(final Matrix4fc other) {
        return this.add(other, this);
    }
    
    public Matrix4f add(final Matrix4fc other, final Matrix4f dest) {
        dest._m00(this.m00() + other.m00())._m01(this.m01() + other.m01())._m02(this.m02() + other.m02())._m03(this.m03() + other.m03())._m10(this.m10() + other.m10())._m11(this.m11() + other.m11())._m12(this.m12() + other.m12())._m13(this.m13() + other.m13())._m20(this.m20() + other.m20())._m21(this.m21() + other.m21())._m22(this.m22() + other.m22())._m23(this.m23() + other.m23())._m30(this.m30() + other.m30())._m31(this.m31() + other.m31())._m32(this.m32() + other.m32())._m33(this.m33() + other.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f sub(final Matrix4fc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix4f sub(final Matrix4fc subtrahend, final Matrix4f dest) {
        dest._m00(this.m00() - subtrahend.m00())._m01(this.m01() - subtrahend.m01())._m02(this.m02() - subtrahend.m02())._m03(this.m03() - subtrahend.m03())._m10(this.m10() - subtrahend.m10())._m11(this.m11() - subtrahend.m11())._m12(this.m12() - subtrahend.m12())._m13(this.m13() - subtrahend.m13())._m20(this.m20() - subtrahend.m20())._m21(this.m21() - subtrahend.m21())._m22(this.m22() - subtrahend.m22())._m23(this.m23() - subtrahend.m23())._m30(this.m30() - subtrahend.m30())._m31(this.m31() - subtrahend.m31())._m32(this.m32() - subtrahend.m32())._m33(this.m33() - subtrahend.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f mulComponentWise(final Matrix4fc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix4f mulComponentWise(final Matrix4fc other, final Matrix4f dest) {
        dest._m00(this.m00() * other.m00())._m01(this.m01() * other.m01())._m02(this.m02() * other.m02())._m03(this.m03() * other.m03())._m10(this.m10() * other.m10())._m11(this.m11() * other.m11())._m12(this.m12() * other.m12())._m13(this.m13() * other.m13())._m20(this.m20() * other.m20())._m21(this.m21() * other.m21())._m22(this.m22() * other.m22())._m23(this.m23() * other.m23())._m30(this.m30() * other.m30())._m31(this.m31() * other.m31())._m32(this.m32() * other.m32())._m33(this.m33() * other.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f add4x3(final Matrix4fc other) {
        return this.add4x3(other, this);
    }
    
    public Matrix4f add4x3(final Matrix4fc other, final Matrix4f dest) {
        dest._m00(this.m00() + other.m00())._m01(this.m01() + other.m01())._m02(this.m02() + other.m02())._m03(this.m03())._m10(this.m10() + other.m10())._m11(this.m11() + other.m11())._m12(this.m12() + other.m12())._m13(this.m13())._m20(this.m20() + other.m20())._m21(this.m21() + other.m21())._m22(this.m22() + other.m22())._m23(this.m23())._m30(this.m30() + other.m30())._m31(this.m31() + other.m31())._m32(this.m32() + other.m32())._m33(this.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f sub4x3(final Matrix4f subtrahend) {
        return this.sub4x3(subtrahend, this);
    }
    
    public Matrix4f sub4x3(final Matrix4fc subtrahend, final Matrix4f dest) {
        dest._m00(this.m00() - subtrahend.m00())._m01(this.m01() - subtrahend.m01())._m02(this.m02() - subtrahend.m02())._m03(this.m03())._m10(this.m10() - subtrahend.m10())._m11(this.m11() - subtrahend.m11())._m12(this.m12() - subtrahend.m12())._m13(this.m13())._m20(this.m20() - subtrahend.m20())._m21(this.m21() - subtrahend.m21())._m22(this.m22() - subtrahend.m22())._m23(this.m23())._m30(this.m30() - subtrahend.m30())._m31(this.m31() - subtrahend.m31())._m32(this.m32() - subtrahend.m32())._m33(this.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f mul4x3ComponentWise(final Matrix4fc other) {
        return this.mul4x3ComponentWise(other, this);
    }
    
    public Matrix4f mul4x3ComponentWise(final Matrix4fc other, final Matrix4f dest) {
        dest._m00(this.m00() * other.m00())._m01(this.m01() * other.m01())._m02(this.m02() * other.m02())._m03(this.m03())._m10(this.m10() * other.m10())._m11(this.m11() * other.m11())._m12(this.m12() * other.m12())._m13(this.m13())._m20(this.m20() * other.m20())._m21(this.m21() * other.m21())._m22(this.m22() * other.m22())._m23(this.m23())._m30(this.m30() * other.m30())._m31(this.m31() * other.m31())._m32(this.m32() * other.m32())._m33(this.m33())._properties(0);
        return dest;
    }
    
    public Matrix4f set(final float m00, final float m01, final float m02, final float m03, final float m10, final float m11, final float m12, final float m13, final float m20, final float m21, final float m22, final float m23, final float m30, final float m31, final float m32, final float m33) {
        return this._m00(m00)._m10(m10)._m20(m20)._m30(m30)._m01(m01)._m11(m11)._m21(m21)._m31(m31)._m02(m02)._m12(m12)._m22(m22)._m32(m32)._m03(m03)._m13(m13)._m23(m23)._m33(m33).determineProperties();
    }
    
    public Matrix4f set(final float[] m, final int off) {
        MemUtil.INSTANCE.copy(m, off, this);
        return this.determineProperties();
    }
    
    public Matrix4f set(final float[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4f setTransposed(final float[] m, final int off) {
        MemUtil.INSTANCE.copyTransposed(m, off, this);
        return this.determineProperties();
    }
    
    public Matrix4f setTransposed(final float[] m) {
        return this.setTransposed(m, 0);
    }
    
    public Matrix4f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4f setTransposed(final FloatBuffer buffer) {
        MemUtil.INSTANCE.getTransposed(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4f setTransposed(final ByteBuffer buffer) {
        MemUtil.INSTANCE.getTransposed(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this.determineProperties();
    }
    
    public Matrix4f setTransposedFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.getTransposed(this, address);
        return this.determineProperties();
    }
    
    public Matrix4f set(final Vector4fc col0, final Vector4fc col1, final Vector4fc col2, final Vector4fc col3) {
        return this._m00(col0.x())._m01(col0.y())._m02(col0.z())._m03(col0.w())._m10(col1.x())._m11(col1.y())._m12(col1.z())._m13(col1.w())._m20(col2.x())._m21(col2.y())._m22(col2.z())._m23(col2.w())._m30(col3.x())._m31(col3.y())._m32(col3.z())._m33(col3.w()).determineProperties();
    }
    
    public float determinant() {
        if ((this.properties & 0x2) != 0x0) {
            return this.determinantAffine();
        }
        return (this.m00() * this.m11() - this.m01() * this.m10()) * (this.m22() * this.m33() - this.m23() * this.m32()) + (this.m02() * this.m10() - this.m00() * this.m12()) * (this.m21() * this.m33() - this.m23() * this.m31()) + (this.m00() * this.m13() - this.m03() * this.m10()) * (this.m21() * this.m32() - this.m22() * this.m31()) + (this.m01() * this.m12() - this.m02() * this.m11()) * (this.m20() * this.m33() - this.m23() * this.m30()) + (this.m03() * this.m11() - this.m01() * this.m13()) * (this.m20() * this.m32() - this.m22() * this.m30()) + (this.m02() * this.m13() - this.m03() * this.m12()) * (this.m20() * this.m31() - this.m21() * this.m30());
    }
    
    public float determinant3x3() {
        return (this.m00() * this.m11() - this.m01() * this.m10()) * this.m22() + (this.m02() * this.m10() - this.m00() * this.m12()) * this.m21() + (this.m01() * this.m12() - this.m02() * this.m11()) * this.m20();
    }
    
    public float determinantAffine() {
        return (this.m00() * this.m11() - this.m01() * this.m10()) * this.m22() + (this.m02() * this.m10() - this.m00() * this.m12()) * this.m21() + (this.m01() * this.m12() - this.m02() * this.m11()) * this.m20();
    }
    
    public Matrix4f invert(final Matrix4f dest) {
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
    
    private Matrix4f invertTranslation(final Matrix4f dest) {
        if (dest != this) {
            dest.set(this);
        }
        return dest._m30(-this.m30())._m31(-this.m31())._m32(-this.m32())._properties(26);
    }
    
    private Matrix4f invertOrthonormal(final Matrix4f dest) {
        final float nm30 = -(this.m00() * this.m30() + this.m01() * this.m31() + this.m02() * this.m32());
        final float nm31 = -(this.m10() * this.m30() + this.m11() * this.m31() + this.m12() * this.m32());
        final float nm32 = -(this.m20() * this.m30() + this.m21() * this.m31() + this.m22() * this.m32());
        final float m01 = this.m01();
        final float m2 = this.m02();
        final float m3 = this.m12();
        return dest._m00(this.m00())._m01(this.m10())._m02(this.m20())._m03(0.0f)._m10(m01)._m11(this.m11())._m12(this.m21())._m13(0.0f)._m20(m2)._m21(m3)._m22(this.m22())._m23(0.0f)._m30(nm30)._m31(nm31)._m32(nm32)._m33(1.0f)._properties(18);
    }
    
    private Matrix4f invertGeneric(final Matrix4f dest) {
        if (this != dest) {
            return this.invertGenericNonThis(dest);
        }
        return this.invertGenericThis(dest);
    }
    
    private Matrix4f invertGenericNonThis(final Matrix4f dest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        return dest._m00(Math.fma(this.m11(), l, Math.fma(-this.m12(), k, this.m13() * j)) * det)._m01(Math.fma(-this.m01(), l, Math.fma(this.m02(), k, -this.m03() * j)) * det)._m02(Math.fma(this.m31(), f, Math.fma(-this.m32(), e, this.m33() * d)) * det)._m03(Math.fma(-this.m21(), f, Math.fma(this.m22(), e, -this.m23() * d)) * det)._m10(Math.fma(-this.m10(), l, Math.fma(this.m12(), i, -this.m13() * h)) * det)._m11(Math.fma(this.m00(), l, Math.fma(-this.m02(), i, this.m03() * h)) * det)._m12(Math.fma(-this.m30(), f, Math.fma(this.m32(), c, -this.m33() * b)) * det)._m13(Math.fma(this.m20(), f, Math.fma(-this.m22(), c, this.m23() * b)) * det)._m20(Math.fma(this.m10(), k, Math.fma(-this.m11(), i, this.m13() * g)) * det)._m21(Math.fma(-this.m00(), k, Math.fma(this.m01(), i, -this.m03() * g)) * det)._m22(Math.fma(this.m30(), e, Math.fma(-this.m31(), c, this.m33() * a)) * det)._m23(Math.fma(-this.m20(), e, Math.fma(this.m21(), c, -this.m23() * a)) * det)._m30(Math.fma(-this.m10(), j, Math.fma(this.m11(), h, -this.m12() * g)) * det)._m31(Math.fma(this.m00(), j, Math.fma(-this.m01(), h, this.m02() * g)) * det)._m32(Math.fma(-this.m30(), d, Math.fma(this.m31(), b, -this.m32() * a)) * det)._m33(Math.fma(this.m20(), d, Math.fma(-this.m21(), b, this.m22() * a)) * det)._properties(0);
    }
    
    private Matrix4f invertGenericThis(final Matrix4f dest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        final float nm00 = Math.fma(this.m11(), l, Math.fma(-this.m12(), k, this.m13() * j)) * det;
        final float nm2 = Math.fma(-this.m01(), l, Math.fma(this.m02(), k, -this.m03() * j)) * det;
        final float nm3 = Math.fma(this.m31(), f, Math.fma(-this.m32(), e, this.m33() * d)) * det;
        final float nm4 = Math.fma(-this.m21(), f, Math.fma(this.m22(), e, -this.m23() * d)) * det;
        final float nm5 = Math.fma(-this.m10(), l, Math.fma(this.m12(), i, -this.m13() * h)) * det;
        final float nm6 = Math.fma(this.m00(), l, Math.fma(-this.m02(), i, this.m03() * h)) * det;
        final float nm7 = Math.fma(-this.m30(), f, Math.fma(this.m32(), c, -this.m33() * b)) * det;
        final float nm8 = Math.fma(this.m20(), f, Math.fma(-this.m22(), c, this.m23() * b)) * det;
        final float nm9 = Math.fma(this.m10(), k, Math.fma(-this.m11(), i, this.m13() * g)) * det;
        final float nm10 = Math.fma(-this.m00(), k, Math.fma(this.m01(), i, -this.m03() * g)) * det;
        final float nm11 = Math.fma(this.m30(), e, Math.fma(-this.m31(), c, this.m33() * a)) * det;
        final float nm12 = Math.fma(-this.m20(), e, Math.fma(this.m21(), c, -this.m23() * a)) * det;
        final float nm13 = Math.fma(-this.m10(), j, Math.fma(this.m11(), h, -this.m12() * g)) * det;
        final float nm14 = Math.fma(this.m00(), j, Math.fma(-this.m01(), h, this.m02() * g)) * det;
        final float nm15 = Math.fma(-this.m30(), d, Math.fma(this.m31(), b, -this.m32() * a)) * det;
        final float nm16 = Math.fma(this.m20(), d, Math.fma(-this.m21(), b, this.m22() * a)) * det;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f invert() {
        return this.invert(this);
    }
    
    public Matrix4f invertPerspective(final Matrix4f dest) {
        final float a = 1.0f / (this.m00() * this.m11());
        final float l = -1.0f / (this.m23() * this.m32());
        return dest.set(this.m11() * a, 0.0f, 0.0f, 0.0f, 0.0f, this.m00() * a, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -this.m23() * l, 0.0f, 0.0f, -this.m32() * l, this.m22() * l)._properties(0);
    }
    
    public Matrix4f invertPerspective() {
        return this.invertPerspective(this);
    }
    
    public Matrix4f invertFrustum(final Matrix4f dest) {
        final float invM00 = 1.0f / this.m00();
        final float invM2 = 1.0f / this.m11();
        final float invM3 = 1.0f / this.m23();
        final float invM4 = 1.0f / this.m32();
        return dest.set(invM00, 0.0f, 0.0f, 0.0f, 0.0f, invM2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, invM4, -this.m20() * invM00 * invM3, -this.m21() * invM2 * invM3, invM3, -this.m22() * invM3 * invM4);
    }
    
    public Matrix4f invertFrustum() {
        return this.invertFrustum(this);
    }
    
    public Matrix4f invertOrtho(final Matrix4f dest) {
        final float invM00 = 1.0f / this.m00();
        final float invM2 = 1.0f / this.m11();
        final float invM3 = 1.0f / this.m22();
        return dest.set(invM00, 0.0f, 0.0f, 0.0f, 0.0f, invM2, 0.0f, 0.0f, 0.0f, 0.0f, invM3, 0.0f, -this.m30() * invM00, -this.m31() * invM2, -this.m32() * invM3, 1.0f)._properties(0x2 | (this.properties & 0x10));
    }
    
    public Matrix4f invertOrtho() {
        return this.invertOrtho(this);
    }
    
    public Matrix4f invertPerspectiveView(final Matrix4fc view, final Matrix4f dest) {
        final float a = 1.0f / (this.m00() * this.m11());
        final float l = -1.0f / (this.m23() * this.m32());
        final float pm00 = this.m11() * a;
        final float pm2 = this.m00() * a;
        final float pm3 = -this.m23() * l;
        final float pm4 = -this.m32() * l;
        final float pm5 = this.m22() * l;
        final float vm30 = -view.m00() * view.m30() - view.m01() * view.m31() - view.m02() * view.m32();
        final float vm31 = -view.m10() * view.m30() - view.m11() * view.m31() - view.m12() * view.m32();
        final float vm32 = -view.m20() * view.m30() - view.m21() * view.m31() - view.m22() * view.m32();
        final float nm10 = view.m01() * pm2;
        final float nm11 = view.m02() * pm4 + vm30 * pm5;
        final float nm12 = view.m12() * pm4 + vm31 * pm5;
        final float nm13 = view.m22() * pm4 + vm32 * pm5;
        return dest._m00(view.m00() * pm00)._m01(view.m10() * pm00)._m02(view.m20() * pm00)._m03(0.0f)._m10(nm10)._m11(view.m11() * pm2)._m12(view.m21() * pm2)._m13(0.0f)._m20(vm30 * pm3)._m21(vm31 * pm3)._m22(vm32 * pm3)._m23(pm3)._m30(nm11)._m31(nm12)._m32(nm13)._m33(pm5)._properties(0);
    }
    
    public Matrix4f invertPerspectiveView(final Matrix4x3fc view, final Matrix4f dest) {
        final float a = 1.0f / (this.m00() * this.m11());
        final float l = -1.0f / (this.m23() * this.m32());
        final float pm00 = this.m11() * a;
        final float pm2 = this.m00() * a;
        final float pm3 = -this.m23() * l;
        final float pm4 = -this.m32() * l;
        final float pm5 = this.m22() * l;
        final float vm30 = -view.m00() * view.m30() - view.m01() * view.m31() - view.m02() * view.m32();
        final float vm31 = -view.m10() * view.m30() - view.m11() * view.m31() - view.m12() * view.m32();
        final float vm32 = -view.m20() * view.m30() - view.m21() * view.m31() - view.m22() * view.m32();
        return dest._m00(view.m00() * pm00)._m01(view.m10() * pm00)._m02(view.m20() * pm00)._m03(0.0f)._m10(view.m01() * pm2)._m11(view.m11() * pm2)._m12(view.m21() * pm2)._m13(0.0f)._m20(vm30 * pm3)._m21(vm31 * pm3)._m22(vm32 * pm3)._m23(pm3)._m30(view.m02() * pm4 + vm30 * pm5)._m31(view.m12() * pm4 + vm31 * pm5)._m32(view.m22() * pm4 + vm32 * pm5)._m33(pm5)._properties(0);
    }
    
    public Matrix4f invertAffine(final Matrix4f dest) {
        final float m11m00 = this.m00() * this.m11();
        final float m10m01 = this.m01() * this.m10();
        final float m10m2 = this.m02() * this.m10();
        final float m12m00 = this.m00() * this.m12();
        final float m12m2 = this.m01() * this.m12();
        final float m11m2 = this.m02() * this.m11();
        final float det = (m11m00 - m10m01) * this.m22() + (m10m2 - m12m00) * this.m21() + (m12m2 - m11m2) * this.m20();
        final float s = 1.0f / det;
        final float m10m3 = this.m10() * this.m22();
        final float m10m4 = this.m10() * this.m21();
        final float m11m3 = this.m11() * this.m22();
        final float m11m4 = this.m11() * this.m20();
        final float m12m3 = this.m12() * this.m21();
        final float m12m4 = this.m12() * this.m20();
        final float m20m02 = this.m20() * this.m02();
        final float m20m3 = this.m20() * this.m01();
        final float m21m02 = this.m21() * this.m02();
        final float m21m3 = this.m21() * this.m00();
        final float m22m01 = this.m22() * this.m01();
        final float m22m2 = this.m22() * this.m00();
        final float nm31 = (m20m02 * this.m31() - m20m3 * this.m32() + m21m3 * this.m32() - m21m02 * this.m30() + m22m01 * this.m30() - m22m2 * this.m31()) * s;
        final float nm32 = (m11m2 * this.m30() - m12m2 * this.m30() + m12m00 * this.m31() - m10m2 * this.m31() + m10m01 * this.m32() - m11m00 * this.m32()) * s;
        return dest._m00((m11m3 - m12m3) * s)._m01((m21m02 - m22m01) * s)._m02((m12m2 - m11m2) * s)._m03(0.0f)._m10((m12m4 - m10m3) * s)._m11((m22m2 - m20m02) * s)._m12((m10m2 - m12m00) * s)._m13(0.0f)._m20((m10m4 - m11m4) * s)._m21((m20m3 - m21m3) * s)._m22((m11m00 - m10m01) * s)._m23(0.0f)._m30((m10m3 * this.m31() - m10m4 * this.m32() + m11m4 * this.m32() - m11m3 * this.m30() + m12m3 * this.m30() - m12m4 * this.m31()) * s)._m31(nm31)._m32(nm32)._m33(1.0f)._properties(2);
    }
    
    public Matrix4f invertAffine() {
        return this.invertAffine(this);
    }
    
    public Matrix4f transpose(final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if (this != dest) {
            return this.transposeNonThisGeneric(dest);
        }
        return this.transposeThisGeneric(dest);
    }
    
    private Matrix4f transposeNonThisGeneric(final Matrix4f dest) {
        return dest._m00(this.m00())._m01(this.m10())._m02(this.m20())._m03(this.m30())._m10(this.m01())._m11(this.m11())._m12(this.m21())._m13(this.m31())._m20(this.m02())._m21(this.m12())._m22(this.m22())._m23(this.m32())._m30(this.m03())._m31(this.m13())._m32(this.m23())._m33(this.m33())._properties(0);
    }
    
    private Matrix4f transposeThisGeneric(final Matrix4f dest) {
        final float nm10 = this.m01();
        final float nm11 = this.m02();
        final float nm12 = this.m12();
        final float nm13 = this.m03();
        final float nm14 = this.m13();
        final float nm15 = this.m23();
        return dest._m01(this.m10())._m02(this.m20())._m03(this.m30())._m10(nm10)._m12(this.m21())._m13(this.m31())._m20(nm11)._m21(nm12)._m23(this.m32())._m30(nm13)._m31(nm14)._m32(nm15)._properties(0);
    }
    
    public Matrix4f transpose3x3() {
        return this.transpose3x3(this);
    }
    
    public Matrix4f transpose3x3(final Matrix4f dest) {
        final float nm10 = this.m01();
        final float nm11 = this.m02();
        final float nm12 = this.m12();
        return dest._m00(this.m00())._m01(this.m10())._m02(this.m20())._m10(nm10)._m11(this.m11())._m12(this.m21())._m20(nm11)._m21(nm12)._m22(this.m22())._properties(this.properties & 0x1E);
    }
    
    public Matrix3f transpose3x3(final Matrix3f dest) {
        return dest._m00(this.m00())._m01(this.m10())._m02(this.m20())._m10(this.m01())._m11(this.m11())._m12(this.m21())._m20(this.m02())._m21(this.m12())._m22(this.m22());
    }
    
    public Matrix4f transpose() {
        return this.transpose(this);
    }
    
    public Matrix4f translation(final float x, final float y, final float z) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        return this._m30(x)._m31(y)._m32(z)._properties(26);
    }
    
    public Matrix4f translation(final Vector3fc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4f setTranslation(final float x, final float y, final float z) {
        return this._m30(x)._m31(y)._m32(z)._properties(this.properties & 0xFFFFFFFA);
    }
    
    public Matrix4f setTranslation(final Vector3fc xyz) {
        return this.setTranslation(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Vector3f getTranslation(final Vector3f dest) {
        dest.x = this.m30();
        dest.y = this.m31();
        dest.z = this.m32();
        return dest;
    }
    
    public Vector3f getScale(final Vector3f dest) {
        dest.x = Math.sqrt(this.m00() * this.m00() + this.m01() * this.m01() + this.m02() * this.m02());
        dest.y = Math.sqrt(this.m10() * this.m10() + this.m11() * this.m11() + this.m12() * this.m12());
        dest.z = Math.sqrt(this.m20() * this.m20() + this.m21() * this.m21() + this.m22() * this.m22());
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
        return Runtime.format(this.m00(), formatter) + " " + Runtime.format(this.m10(), formatter) + " " + Runtime.format(this.m20(), formatter) + " " + Runtime.format(this.m30(), formatter) + "\n" + Runtime.format(this.m01(), formatter) + " " + Runtime.format(this.m11(), formatter) + " " + Runtime.format(this.m21(), formatter) + " " + Runtime.format(this.m31(), formatter) + "\n" + Runtime.format(this.m02(), formatter) + " " + Runtime.format(this.m12(), formatter) + " " + Runtime.format(this.m22(), formatter) + " " + Runtime.format(this.m32(), formatter) + "\n" + Runtime.format(this.m03(), formatter) + " " + Runtime.format(this.m13(), formatter) + " " + Runtime.format(this.m23(), formatter) + " " + Runtime.format(this.m33(), formatter) + "\n";
    }
    
    public Matrix4f get(final Matrix4f dest) {
        return dest.set(this);
    }
    
    public Matrix4x3f get4x3(final Matrix4x3f dest) {
        return dest.set(this);
    }
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set(this);
    }
    
    public Matrix3f get3x3(final Matrix3f dest) {
        return dest.set(this);
    }
    
    public Matrix3d get3x3(final Matrix3d dest) {
        return dest.set(this);
    }
    
    public AxisAngle4f getRotation(final AxisAngle4f dest) {
        return dest.set(this);
    }
    
    public AxisAngle4d getRotation(final AxisAngle4d dest) {
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
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get4x3(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x3(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get4x3(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x3(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get4x3(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x3(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get4x3(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x3(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get3x4(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get3x4(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get3x4(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get3x4(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put3x4(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer getTransposed(final FloatBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer getTransposed(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getTransposed(final ByteBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer getTransposed(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putTransposed(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get4x3Transposed(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x3Transposed(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get4x3Transposed(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x3Transposed(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get4x3Transposed(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x3Transposed(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get4x3Transposed(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x3Transposed(this, index, buffer);
        return buffer;
    }
    
    public Matrix4fc getToAddress(final long address) {
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
        MemUtil.INSTANCE.copy(this, arr, 0);
        return arr;
    }
    
    public Matrix4f zero() {
        MemUtil.INSTANCE.zero(this);
        return this._properties(0);
    }
    
    public Matrix4f scaling(final float factor) {
        return this.scaling(factor, factor, factor);
    }
    
    public Matrix4f scaling(final float x, final float y, final float z) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        return this._m00(x)._m11(y)._m22(z)._properties(0x2 | (one ? 16 : 0));
    }
    
    public Matrix4f scaling(final Vector3fc xyz) {
        return this.scaling(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Matrix4f rotation(final float angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4f rotation(final AxisAngle4f axisAngle) {
        return this.rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4f rotation(final float angle, final float x, final float y, final float z) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotationX(x * angle);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotationY(y * angle);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotationZ(z * angle);
        }
        return this.rotationInternal(angle, x, y, z);
    }
    
    private Matrix4f rotationInternal(final float angle, final float x, final float y, final float z) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float C = 1.0f - cos;
        final float xy = x * y;
        final float xz = x * z;
        final float yz = y * z;
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        return this._m00(cos + x * x * C)._m10(xy * C - z * sin)._m20(xz * C + y * sin)._m01(xy * C + z * sin)._m11(cos + y * y * C)._m21(yz * C - x * sin)._m02(xz * C - y * sin)._m12(yz * C + x * sin)._m22(cos + z * z * C)._properties(18);
    }
    
    public Matrix4f rotationX(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m11(cos)._m12(sin)._m21(-sin)._m22(cos)._properties(18);
        return this;
    }
    
    public Matrix4f rotationY(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(cos)._m02(-sin)._m20(sin)._m22(cos)._properties(18);
        return this;
    }
    
    public Matrix4f rotationZ(final float ang) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        return this._m00(cos)._m01(sin)._m10(-sin)._m11(cos)._properties(18);
    }
    
    public Matrix4f rotationTowardsXY(final float dirX, final float dirY) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        return this._m00(dirY)._m01(dirX)._m10(-dirX)._m11(dirY)._properties(18);
    }
    
    public Matrix4f rotationXYZ(final float angleX, final float angleY, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        final float nm01 = -sinX * -sinY;
        final float nm2 = cosX * -sinY;
        return this._m20(sinY)._m21(-sinX * cosY)._m22(cosX * cosY)._m00(cosY * cosZ)._m01(nm01 * cosZ + cosX * sinZ)._m02(nm2 * cosZ + sinX * sinZ)._m10(cosY * -sinZ)._m11(nm01 * -sinZ + cosX * cosZ)._m12(nm2 * -sinZ + sinX * cosZ)._properties(18);
    }
    
    public Matrix4f rotationZYX(final float angleZ, final float angleY, final float angleX) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float nm20 = cosZ * sinY;
        final float nm21 = sinZ * sinY;
        return this._m00(cosZ * cosY)._m01(sinZ * cosY)._m02(-sinY)._m03(0.0f)._m10(-sinZ * cosX + nm20 * sinX)._m11(cosZ * cosX + nm21 * sinX)._m12(cosY * sinX)._m13(0.0f)._m20(-sinZ * -sinX + nm20 * cosX)._m21(cosZ * -sinX + nm21 * cosX)._m22(cosY * cosX)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f rotationYXZ(final float angleY, final float angleX, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float nm10 = sinY * sinX;
        final float nm11 = cosY * sinX;
        return this._m20(sinY * cosX)._m21(-sinX)._m22(cosY * cosX)._m23(0.0f)._m00(cosY * cosZ + nm10 * sinZ)._m01(cosX * sinZ)._m02(-sinY * cosZ + nm11 * sinZ)._m03(0.0f)._m10(cosY * -sinZ + nm10 * cosZ)._m11(cosX * cosZ)._m12(-sinY * -sinZ + nm11 * cosZ)._m13(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f setRotationXYZ(final float angleX, final float angleY, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float nm01 = -sinX * -sinY;
        final float nm2 = cosX * -sinY;
        return this._m20(sinY)._m21(-sinX * cosY)._m22(cosX * cosY)._m00(cosY * cosZ)._m01(nm01 * cosZ + cosX * sinZ)._m02(nm2 * cosZ + sinX * sinZ)._m10(cosY * -sinZ)._m11(nm01 * -sinZ + cosX * cosZ)._m12(nm2 * -sinZ + sinX * cosZ)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f setRotationZYX(final float angleZ, final float angleY, final float angleX) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float nm20 = cosZ * sinY;
        final float nm21 = sinZ * sinY;
        return this._m00(cosZ * cosY)._m01(sinZ * cosY)._m02(-sinY)._m10(-sinZ * cosX + nm20 * sinX)._m11(cosZ * cosX + nm21 * sinX)._m12(cosY * sinX)._m20(-sinZ * -sinX + nm20 * cosX)._m21(cosZ * -sinX + nm21 * cosX)._m22(cosY * cosX)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f setRotationYXZ(final float angleY, final float angleX, final float angleZ) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float nm10 = sinY * sinX;
        final float nm11 = cosY * sinX;
        return this._m20(sinY * cosX)._m21(-sinX)._m22(cosY * cosX)._m00(cosY * cosZ + nm10 * sinZ)._m01(cosX * sinZ)._m02(-sinY * cosZ + nm11 * sinZ)._m10(cosY * -sinZ + nm10 * cosZ)._m11(cosX * cosZ)._m12(-sinY * -sinZ + nm11 * cosZ)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotation(final Quaternionfc quat) {
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
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        return this._m00(w2 + x2 - z2 - y2)._m01(dxy + dzw)._m02(dxz - dyw)._m10(-dzw + dxy)._m11(y2 - z2 + w2 - x2)._m12(dyz + dxw)._m20(dyw + dxz)._m21(dyz - dxw)._m22(z2 - y2 - x2 + w2)._properties(18);
    }
    
    public Matrix4f translationRotateScale(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float sx, final float sy, final float sz) {
        final float dqx = qx + qx;
        final float dqy = qy + qy;
        final float dqz = qz + qz;
        final float q00 = dqx * qx;
        final float q2 = dqy * qy;
        final float q3 = dqz * qz;
        final float q4 = dqx * qy;
        final float q5 = dqx * qz;
        final float q6 = dqx * qw;
        final float q7 = dqy * qz;
        final float q8 = dqy * qw;
        final float q9 = dqz * qw;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return this._m00(sx - (q2 + q3) * sx)._m01((q4 + q9) * sx)._m02((q5 - q8) * sx)._m03(0.0f)._m10((q4 - q9) * sy)._m11(sy - (q3 + q00) * sy)._m12((q7 + q6) * sy)._m13(0.0f)._m20((q5 + q8) * sz)._m21((q7 - q6) * sz)._m22(sz - (q2 + q00) * sz)._m23(0.0f)._m30(tx)._m31(ty)._m32(tz)._m33(1.0f)._properties(0x2 | (one ? 16 : 0));
    }
    
    public Matrix4f translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4f translationRotateScale(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float scale) {
        return this.translationRotateScale(tx, ty, tz, qx, qy, qz, qw, scale, scale, scale);
    }
    
    public Matrix4f translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final float scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4f translationRotateScaleInvert(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float sx, final float sy, final float sz) {
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        if (one) {
            return this.translationRotateInvert(tx, ty, tz, qx, qy, qz, qw);
        }
        final float nqx = -qx;
        final float nqy = -qy;
        final float nqz = -qz;
        final float dqx = nqx + nqx;
        final float dqy = nqy + nqy;
        final float dqz = nqz + nqz;
        final float q00 = dqx * nqx;
        final float q2 = dqy * nqy;
        final float q3 = dqz * nqz;
        final float q4 = dqx * nqy;
        final float q5 = dqx * nqz;
        final float q6 = dqx * qw;
        final float q7 = dqy * nqz;
        final float q8 = dqy * qw;
        final float q9 = dqz * qw;
        final float isx = 1.0f / sx;
        final float isy = 1.0f / sy;
        final float isz = 1.0f / sz;
        return this._m00(isx * (1.0f - q2 - q3))._m01(isy * (q4 + q9))._m02(isz * (q5 - q8))._m03(0.0f)._m10(isx * (q4 - q9))._m11(isy * (1.0f - q3 - q00))._m12(isz * (q7 + q6))._m13(0.0f)._m20(isx * (q5 + q8))._m21(isy * (q7 - q6))._m22(isz * (1.0f - q2 - q00))._m23(0.0f)._m30(-this.m00() * tx - this.m10() * ty - this.m20() * tz)._m31(-this.m01() * tx - this.m11() * ty - this.m21() * tz)._m32(-this.m02() * tx - this.m12() * ty - this.m22() * tz)._m33(1.0f)._properties(2);
    }
    
    public Matrix4f translationRotateScaleInvert(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4f translationRotateScaleInvert(final Vector3fc translation, final Quaternionfc quat, final float scale) {
        return this.translationRotateScaleInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale, scale, scale);
    }
    
    public Matrix4f translationRotateScaleMulAffine(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float sx, final float sy, final float sz, final Matrix4f m) {
        final float w2 = qw * qw;
        final float x2 = qx * qx;
        final float y2 = qy * qy;
        final float z2 = qz * qz;
        final float zw = qz * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yw = qy * qw;
        final float yz = qy * qz;
        final float xw = qx * qw;
        final float nm00 = w2 + x2 - z2 - y2;
        final float nm2 = xy + zw + zw + xy;
        final float nm3 = xz - yw + xz - yw;
        final float nm4 = -zw + xy - zw + xy;
        final float nm5 = y2 - z2 + w2 - x2;
        final float nm6 = yz + yz + xw + xw;
        final float nm7 = yw + xz + xz + yw;
        final float nm8 = yz + yz - xw - xw;
        final float nm9 = z2 - y2 - x2 + w2;
        final float m2 = nm00 * m.m00() + nm4 * m.m01() + nm7 * m.m02();
        final float m3 = nm2 * m.m00() + nm5 * m.m01() + nm8 * m.m02();
        this._m02(nm3 * m.m00() + nm6 * m.m01() + nm9 * m.m02())._m00(m2)._m01(m3)._m03(0.0f);
        final float m4 = nm00 * m.m10() + nm4 * m.m11() + nm7 * m.m12();
        final float m5 = nm2 * m.m10() + nm5 * m.m11() + nm8 * m.m12();
        this._m12(nm3 * m.m10() + nm6 * m.m11() + nm9 * m.m12())._m10(m4)._m11(m5)._m13(0.0f);
        final float m6 = nm00 * m.m20() + nm4 * m.m21() + nm7 * m.m22();
        final float m7 = nm2 * m.m20() + nm5 * m.m21() + nm8 * m.m22();
        this._m22(nm3 * m.m20() + nm6 * m.m21() + nm9 * m.m22())._m20(m6)._m21(m7)._m23(0.0f);
        final float m8 = nm00 * m.m30() + nm4 * m.m31() + nm7 * m.m32() + tx;
        final float m9 = nm2 * m.m30() + nm5 * m.m31() + nm8 * m.m32() + ty;
        this._m32(nm3 * m.m30() + nm6 * m.m31() + nm9 * m.m32() + tz)._m30(m8)._m31(m9)._m33(1.0f);
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return this._properties(0x2 | ((one && (m.properties & 0x10) != 0x0) ? 16 : 0));
    }
    
    public Matrix4f translationRotateScaleMulAffine(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale, final Matrix4f m) {
        return this.translationRotateScaleMulAffine(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z(), m);
    }
    
    public Matrix4f translationRotate(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw) {
        final float w2 = qw * qw;
        final float x2 = qx * qx;
        final float y2 = qy * qy;
        final float z2 = qz * qz;
        final float zw = qz * qw;
        final float xy = qx * qy;
        final float xz = qx * qz;
        final float yw = qy * qw;
        final float yz = qy * qz;
        final float xw = qx * qw;
        return this._m00(w2 + x2 - z2 - y2)._m01(xy + zw + zw + xy)._m02(xz - yw + xz - yw)._m10(-zw + xy - zw + xy)._m11(y2 - z2 + w2 - x2)._m12(yz + yz + xw + xw)._m20(yw + xz + xz + yw)._m21(yz + yz - xw - xw)._m22(z2 - y2 - x2 + w2)._m30(tx)._m31(ty)._m32(tz)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f translationRotate(final float tx, final float ty, final float tz, final Quaternionfc quat) {
        return this.translationRotate(tx, ty, tz, quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4f translationRotate(final Vector3fc translation, final Quaternionfc quat) {
        return this.translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4f translationRotateInvert(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw) {
        final float nqx = -qx;
        final float nqy = -qy;
        final float nqz = -qz;
        final float dqx = nqx + nqx;
        final float dqy = nqy + nqy;
        final float dqz = nqz + nqz;
        final float q00 = dqx * nqx;
        final float q2 = dqy * nqy;
        final float q3 = dqz * nqz;
        final float q4 = dqx * nqy;
        final float q5 = dqx * nqz;
        final float q6 = dqx * qw;
        final float q7 = dqy * nqz;
        final float q8 = dqy * qw;
        final float q9 = dqz * qw;
        return this._m00(1.0f - q2 - q3)._m01(q4 + q9)._m02(q5 - q8)._m03(0.0f)._m10(q4 - q9)._m11(1.0f - q3 - q00)._m12(q7 + q6)._m13(0.0f)._m20(q5 + q8)._m21(q7 - q6)._m22(1.0f - q2 - q00)._m23(0.0f)._m30(-this.m00() * tx - this.m10() * ty - this.m20() * tz)._m31(-this.m01() * tx - this.m11() * ty - this.m21() * tz)._m32(-this.m02() * tx - this.m12() * ty - this.m22() * tz)._m33(1.0f)._properties(18);
    }
    
    public Matrix4f translationRotateInvert(final Vector3fc translation, final Quaternionfc quat) {
        return this.translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4f set3x3(final Matrix3fc mat) {
        return this.set3x3Matrix3fc(mat)._properties(this.properties & 0xFFFFFFE2);
    }
    
    private Matrix4f set3x3Matrix3fc(final Matrix3fc mat) {
        return this._m00(mat.m00())._m01(mat.m01())._m02(mat.m02())._m10(mat.m10())._m11(mat.m11())._m12(mat.m12())._m20(mat.m20())._m21(mat.m21())._m22(mat.m22());
    }
    
    public Vector4f transform(final Vector4f v) {
        return v.mul(this);
    }
    
    public Vector4f transform(final Vector4fc v, final Vector4f dest) {
        return v.mul(this, dest);
    }
    
    public Vector4f transform(final float x, final float y, final float z, final float w, final Vector4f dest) {
        return dest.set(x, y, z, w).mul(this);
    }
    
    public Vector4f transformTranspose(final Vector4f v) {
        return v.mulTranspose(this);
    }
    
    public Vector4f transformTranspose(final Vector4fc v, final Vector4f dest) {
        return v.mulTranspose(this, dest);
    }
    
    public Vector4f transformTranspose(final float x, final float y, final float z, final float w, final Vector4f dest) {
        return dest.set(x, y, z, w).mulTranspose(this);
    }
    
    public Vector4f transformProject(final Vector4f v) {
        return v.mulProject(this);
    }
    
    public Vector4f transformProject(final Vector4fc v, final Vector4f dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector4f transformProject(final float x, final float y, final float z, final float w, final Vector4f dest) {
        return dest.set(x, y, z, w).mulProject(this);
    }
    
    public Vector3f transformProject(final Vector4fc v, final Vector3f dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector3f transformProject(final float x, final float y, final float z, final float w, final Vector3f dest) {
        return dest.set(x, y, z).mulProject(this, w, dest);
    }
    
    public Vector3f transformProject(final Vector3f v) {
        return v.mulProject(this);
    }
    
    public Vector3f transformProject(final Vector3fc v, final Vector3f dest) {
        return v.mulProject(this, dest);
    }
    
    public Vector3f transformProject(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(x, y, z).mulProject(this);
    }
    
    public Vector3f transformPosition(final Vector3f v) {
        return v.mulPosition(this);
    }
    
    public Vector3f transformPosition(final Vector3fc v, final Vector3f dest) {
        return this.transformPosition(v.x(), v.y(), v.z(), dest);
    }
    
    public Vector3f transformPosition(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(x, y, z).mulPosition(this);
    }
    
    public Vector3f transformDirection(final Vector3f v) {
        return this.transformDirection(v.x, v.y, v.z, v);
    }
    
    public Vector3f transformDirection(final Vector3fc v, final Vector3f dest) {
        return this.transformDirection(v.x(), v.y(), v.z(), dest);
    }
    
    public Vector3f transformDirection(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(x, y, z).mulDirection(this);
    }
    
    public Vector4f transformAffine(final Vector4f v) {
        return v.mulAffine(this, v);
    }
    
    public Vector4f transformAffine(final Vector4fc v, final Vector4f dest) {
        return this.transformAffine(v.x(), v.y(), v.z(), v.w(), dest);
    }
    
    public Vector4f transformAffine(final float x, final float y, final float z, final float w, final Vector4f dest) {
        return dest.set(x, y, z, w).mulAffine(this, dest);
    }
    
    public Matrix4f scale(final Vector3fc xyz, final Matrix4f dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix4f scale(final Vector3fc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix4f scale(final float xyz, final Matrix4f dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix4f scale(final float xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix4f scaleXY(final float x, final float y, final Matrix4f dest) {
        return this.scale(x, y, 1.0f, dest);
    }
    
    public Matrix4f scaleXY(final float x, final float y) {
        return this.scale(x, y, 1.0f);
    }
    
    public Matrix4f scale(final float x, final float y, final float z, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleGeneric(x, y, z, dest);
    }
    
    private Matrix4f scaleGeneric(final float x, final float y, final float z, final Matrix4f dest) {
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        return dest._m00(this.m00() * x)._m01(this.m01() * x)._m02(this.m02() * x)._m03(this.m03() * x)._m10(this.m10() * y)._m11(this.m11() * y)._m12(this.m12() * y)._m13(this.m13() * y)._m20(this.m20() * z)._m21(this.m21() * z)._m22(this.m22() * z)._m23(this.m23() * z)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & ~(0xD | (one ? 0 : 16)));
    }
    
    public Matrix4f scale(final float x, final float y, final float z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix4f scaleAround(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz, final Matrix4f dest) {
        final float nm30 = this.m00() * ox + this.m10() * oy + this.m20() * oz + this.m30();
        final float nm31 = this.m01() * ox + this.m11() * oy + this.m21() * oz + this.m31();
        final float nm32 = this.m02() * ox + this.m12() * oy + this.m22() * oz + this.m32();
        final float nm33 = this.m03() * ox + this.m13() * oy + this.m23() * oz + this.m33();
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return dest._m00(this.m00() * sx)._m01(this.m01() * sx)._m02(this.m02() * sx)._m03(this.m03() * sx)._m10(this.m10() * sy)._m11(this.m11() * sy)._m12(this.m12() * sy)._m13(this.m13() * sy)._m20(this.m20() * sz)._m21(this.m21() * sz)._m22(this.m22() * sz)._m23(this.m23() * sz)._m30(-dest.m00() * ox - dest.m10() * oy - dest.m20() * oz + nm30)._m31(-dest.m01() * ox - dest.m11() * oy - dest.m21() * oz + nm31)._m32(-dest.m02() * ox - dest.m12() * oy - dest.m22() * oz + nm32)._m33(-dest.m03() * ox - dest.m13() * oy - dest.m23() * oz + nm33)._properties(this.properties & ~(0xD | (one ? 0 : 16)));
    }
    
    public Matrix4f scaleAround(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz) {
        return this.scaleAround(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4f scaleAround(final float factor, final float ox, final float oy, final float oz) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4f scaleAround(final float factor, final float ox, final float oy, final float oz, final Matrix4f dest) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4f scaleLocal(final float x, final float y, final float z, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleLocalGeneric(x, y, z, dest);
    }
    
    private Matrix4f scaleLocalGeneric(final float x, final float y, final float z, final Matrix4f dest) {
        final float nm00 = x * this.m00();
        final float nm2 = y * this.m01();
        final float nm3 = z * this.m02();
        final float nm4 = x * this.m10();
        final float nm5 = y * this.m11();
        final float nm6 = z * this.m12();
        final float nm7 = x * this.m20();
        final float nm8 = y * this.m21();
        final float nm9 = z * this.m22();
        final float nm10 = x * this.m30();
        final float nm11 = y * this.m31();
        final float nm12 = z * this.m32();
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03())._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13())._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23())._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33())._properties(this.properties & ~(0xD | (one ? 0 : 16)));
    }
    
    public Matrix4f scaleLocal(final float xyz, final Matrix4f dest) {
        return this.scaleLocal(xyz, xyz, xyz, dest);
    }
    
    public Matrix4f scaleLocal(final float xyz) {
        return this.scaleLocal(xyz, this);
    }
    
    public Matrix4f scaleLocal(final float x, final float y, final float z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix4f scaleAroundLocal(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz, final Matrix4f dest) {
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return dest._m00(sx * (this.m00() - ox * this.m03()) + ox * this.m03())._m01(sy * (this.m01() - oy * this.m03()) + oy * this.m03())._m02(sz * (this.m02() - oz * this.m03()) + oz * this.m03())._m03(this.m03())._m10(sx * (this.m10() - ox * this.m13()) + ox * this.m13())._m11(sy * (this.m11() - oy * this.m13()) + oy * this.m13())._m12(sz * (this.m12() - oz * this.m13()) + oz * this.m13())._m13(this.m13())._m20(sx * (this.m20() - ox * this.m23()) + ox * this.m23())._m21(sy * (this.m21() - oy * this.m23()) + oy * this.m23())._m22(sz * (this.m22() - oz * this.m23()) + oz * this.m23())._m23(this.m23())._m30(sx * (this.m30() - ox * this.m33()) + ox * this.m33())._m31(sy * (this.m31() - oy * this.m33()) + oy * this.m33())._m32(sz * (this.m32() - oz * this.m33()) + oz * this.m33())._m33(this.m33())._properties(this.properties & ~(0xD | (one ? 0 : 16)));
    }
    
    public Matrix4f scaleAroundLocal(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz) {
        return this.scaleAroundLocal(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4f scaleAroundLocal(final float factor, final float ox, final float oy, final float oz) {
        return this.scaleAroundLocal(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4f scaleAroundLocal(final float factor, final float ox, final float oy, final float oz, final Matrix4f dest) {
        return this.scaleAroundLocal(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4f rotateX(final float ang, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationX(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30();
            final float y = this.m31();
            final float z = this.m32();
            return dest.rotationX(ang).setTranslation(x, y, z);
        }
        return this.rotateXInternal(ang, dest);
    }
    
    private Matrix4f rotateXInternal(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float lm10 = this.m10();
        final float lm11 = this.m11();
        final float lm12 = this.m12();
        final float lm13 = this.m13();
        final float lm14 = this.m20();
        final float lm15 = this.m21();
        final float lm16 = this.m22();
        final float lm17 = this.m23();
        return dest._m20(Math.fma(lm10, -sin, lm14 * cos))._m21(Math.fma(lm11, -sin, lm15 * cos))._m22(Math.fma(lm12, -sin, lm16 * cos))._m23(Math.fma(lm13, -sin, lm17 * cos))._m10(Math.fma(lm10, cos, lm14 * sin))._m11(Math.fma(lm11, cos, lm15 * sin))._m12(Math.fma(lm12, cos, lm16 * sin))._m13(Math.fma(lm13, cos, lm17 * sin))._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateX(final float ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix4f rotateY(final float ang, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationY(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30();
            final float y = this.m31();
            final float z = this.m32();
            return dest.rotationY(ang).setTranslation(x, y, z);
        }
        return this.rotateYInternal(ang, dest);
    }
    
    private Matrix4f rotateYInternal(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = Math.fma(this.m00(), cos, this.m20() * -sin);
        final float nm2 = Math.fma(this.m01(), cos, this.m21() * -sin);
        final float nm3 = Math.fma(this.m02(), cos, this.m22() * -sin);
        final float nm4 = Math.fma(this.m03(), cos, this.m23() * -sin);
        return dest._m20(Math.fma(this.m00(), sin, this.m20() * cos))._m21(Math.fma(this.m01(), sin, this.m21() * cos))._m22(Math.fma(this.m02(), sin, this.m22() * cos))._m23(Math.fma(this.m03(), sin, this.m23() * cos))._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateY(final float ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix4f rotateZ(final float ang, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZ(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30();
            final float y = this.m31();
            final float z = this.m32();
            return dest.rotationZ(ang).setTranslation(x, y, z);
        }
        return this.rotateZInternal(ang, dest);
    }
    
    private Matrix4f rotateZInternal(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        return this.rotateTowardsXY(sin, cos, dest);
    }
    
    public Matrix4f rotateZ(final float ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix4f rotateTowardsXY(final float dirX, final float dirY) {
        return this.rotateTowardsXY(dirX, dirY, this);
    }
    
    public Matrix4f rotateTowardsXY(final float dirX, final float dirY, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationTowardsXY(dirX, dirY);
        }
        final float nm00 = Math.fma(this.m00(), dirY, this.m10() * dirX);
        final float nm2 = Math.fma(this.m01(), dirY, this.m11() * dirX);
        final float nm3 = Math.fma(this.m02(), dirY, this.m12() * dirX);
        final float nm4 = Math.fma(this.m03(), dirY, this.m13() * dirX);
        return dest._m10(Math.fma(this.m00(), -dirX, this.m10() * dirY))._m11(Math.fma(this.m01(), -dirX, this.m11() * dirY))._m12(Math.fma(this.m02(), -dirX, this.m12() * dirY))._m13(Math.fma(this.m03(), -dirX, this.m13() * dirY))._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateXYZ(final Vector3fc angles) {
        return this.rotateXYZ(angles.x(), angles.y(), angles.z());
    }
    
    public Matrix4f rotateXYZ(final float angleX, final float angleY, final float angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4f rotateXYZ(final float angleX, final float angleY, final float angleZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30();
            final float ty = this.m31();
            final float tz = this.m32();
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineXYZ(angleX, angleY, angleZ);
        }
        return this.rotateXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4f rotateXYZInternal(final float angleX, final float angleY, final float angleZ, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinX = -sinX;
        final float m_sinY = -sinY;
        final float m_sinZ = -sinZ;
        final float nm10 = Math.fma(this.m10(), cosX, this.m20() * sinX);
        final float nm11 = Math.fma(this.m11(), cosX, this.m21() * sinX);
        final float nm12 = Math.fma(this.m12(), cosX, this.m22() * sinX);
        final float nm13 = Math.fma(this.m13(), cosX, this.m23() * sinX);
        final float nm14 = Math.fma(this.m10(), m_sinX, this.m20() * cosX);
        final float nm15 = Math.fma(this.m11(), m_sinX, this.m21() * cosX);
        final float nm16 = Math.fma(this.m12(), m_sinX, this.m22() * cosX);
        final float nm17 = Math.fma(this.m13(), m_sinX, this.m23() * cosX);
        final float nm18 = Math.fma(this.m00(), cosY, nm14 * m_sinY);
        final float nm19 = Math.fma(this.m01(), cosY, nm15 * m_sinY);
        final float nm20 = Math.fma(this.m02(), cosY, nm16 * m_sinY);
        final float nm21 = Math.fma(this.m03(), cosY, nm17 * m_sinY);
        return dest._m20(Math.fma(this.m00(), sinY, nm14 * cosY))._m21(Math.fma(this.m01(), sinY, nm15 * cosY))._m22(Math.fma(this.m02(), sinY, nm16 * cosY))._m23(Math.fma(this.m03(), sinY, nm17 * cosY))._m00(Math.fma(nm18, cosZ, nm10 * sinZ))._m01(Math.fma(nm19, cosZ, nm11 * sinZ))._m02(Math.fma(nm20, cosZ, nm12 * sinZ))._m03(Math.fma(nm21, cosZ, nm13 * sinZ))._m10(Math.fma(nm18, m_sinZ, nm10 * cosZ))._m11(Math.fma(nm19, m_sinZ, nm11 * cosZ))._m12(Math.fma(nm20, m_sinZ, nm12 * cosZ))._m13(Math.fma(nm21, m_sinZ, nm13 * cosZ))._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffineXYZ(final float angleX, final float angleY, final float angleZ) {
        return this.rotateAffineXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4f rotateAffineXYZ(final float angleX, final float angleY, final float angleZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30();
            final float ty = this.m31();
            final float tz = this.m32();
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateAffineXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4f rotateAffineXYZInternal(final float angleX, final float angleY, final float angleZ, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinX = -sinX;
        final float m_sinY = -sinY;
        final float m_sinZ = -sinZ;
        final float nm10 = Math.fma(this.m10(), cosX, this.m20() * sinX);
        final float nm11 = Math.fma(this.m11(), cosX, this.m21() * sinX);
        final float nm12 = Math.fma(this.m12(), cosX, this.m22() * sinX);
        final float nm13 = Math.fma(this.m10(), m_sinX, this.m20() * cosX);
        final float nm14 = Math.fma(this.m11(), m_sinX, this.m21() * cosX);
        final float nm15 = Math.fma(this.m12(), m_sinX, this.m22() * cosX);
        final float nm16 = Math.fma(this.m00(), cosY, nm13 * m_sinY);
        final float nm17 = Math.fma(this.m01(), cosY, nm14 * m_sinY);
        final float nm18 = Math.fma(this.m02(), cosY, nm15 * m_sinY);
        return dest._m20(Math.fma(this.m00(), sinY, nm13 * cosY))._m21(Math.fma(this.m01(), sinY, nm14 * cosY))._m22(Math.fma(this.m02(), sinY, nm15 * cosY))._m23(0.0f)._m00(Math.fma(nm16, cosZ, nm10 * sinZ))._m01(Math.fma(nm17, cosZ, nm11 * sinZ))._m02(Math.fma(nm18, cosZ, nm12 * sinZ))._m03(0.0f)._m10(Math.fma(nm16, m_sinZ, nm10 * cosZ))._m11(Math.fma(nm17, m_sinZ, nm11 * cosZ))._m12(Math.fma(nm18, m_sinZ, nm12 * cosZ))._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateZYX(final Vector3f angles) {
        return this.rotateZYX(angles.z, angles.y, angles.x);
    }
    
    public Matrix4f rotateZYX(final float angleZ, final float angleY, final float angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4f rotateZYX(final float angleZ, final float angleY, final float angleX, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZYX(angleZ, angleY, angleX);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30();
            final float ty = this.m31();
            final float tz = this.m32();
            return dest.rotationZYX(angleZ, angleY, angleX).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineZYX(angleZ, angleY, angleX);
        }
        return this.rotateZYXInternal(angleZ, angleY, angleX, dest);
    }
    
    private Matrix4f rotateZYXInternal(final float angleZ, final float angleY, final float angleX, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinZ = -sinZ;
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float nm00 = this.m00() * cosZ + this.m10() * sinZ;
        final float nm2 = this.m01() * cosZ + this.m11() * sinZ;
        final float nm3 = this.m02() * cosZ + this.m12() * sinZ;
        final float nm4 = this.m03() * cosZ + this.m13() * sinZ;
        final float nm5 = this.m00() * m_sinZ + this.m10() * cosZ;
        final float nm6 = this.m01() * m_sinZ + this.m11() * cosZ;
        final float nm7 = this.m02() * m_sinZ + this.m12() * cosZ;
        final float nm8 = this.m03() * m_sinZ + this.m13() * cosZ;
        final float nm9 = nm00 * sinY + this.m20() * cosY;
        final float nm10 = nm2 * sinY + this.m21() * cosY;
        final float nm11 = nm3 * sinY + this.m22() * cosY;
        final float nm12 = nm4 * sinY + this.m23() * cosY;
        return dest._m00(nm00 * cosY + this.m20() * m_sinY)._m01(nm2 * cosY + this.m21() * m_sinY)._m02(nm3 * cosY + this.m22() * m_sinY)._m03(nm4 * cosY + this.m23() * m_sinY)._m10(nm5 * cosX + nm9 * sinX)._m11(nm6 * cosX + nm10 * sinX)._m12(nm7 * cosX + nm11 * sinX)._m13(nm8 * cosX + nm12 * sinX)._m20(nm5 * m_sinX + nm9 * cosX)._m21(nm6 * m_sinX + nm10 * cosX)._m22(nm7 * m_sinX + nm11 * cosX)._m23(nm8 * m_sinX + nm12 * cosX)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffineZYX(final float angleZ, final float angleY, final float angleX) {
        return this.rotateAffineZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4f rotateAffineZYX(final float angleZ, final float angleY, final float angleX, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinZ = -sinZ;
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float nm00 = this.m00() * cosZ + this.m10() * sinZ;
        final float nm2 = this.m01() * cosZ + this.m11() * sinZ;
        final float nm3 = this.m02() * cosZ + this.m12() * sinZ;
        final float nm4 = this.m00() * m_sinZ + this.m10() * cosZ;
        final float nm5 = this.m01() * m_sinZ + this.m11() * cosZ;
        final float nm6 = this.m02() * m_sinZ + this.m12() * cosZ;
        final float nm7 = nm00 * sinY + this.m20() * cosY;
        final float nm8 = nm2 * sinY + this.m21() * cosY;
        final float nm9 = nm3 * sinY + this.m22() * cosY;
        return dest._m00(nm00 * cosY + this.m20() * m_sinY)._m01(nm2 * cosY + this.m21() * m_sinY)._m02(nm3 * cosY + this.m22() * m_sinY)._m03(0.0f)._m10(nm4 * cosX + nm7 * sinX)._m11(nm5 * cosX + nm8 * sinX)._m12(nm6 * cosX + nm9 * sinX)._m13(0.0f)._m20(nm4 * m_sinX + nm7 * cosX)._m21(nm5 * m_sinX + nm8 * cosX)._m22(nm6 * m_sinX + nm9 * cosX)._m23(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateYXZ(final Vector3f angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix4f rotateYXZ(final float angleY, final float angleX, final float angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4f rotateYXZ(final float angleY, final float angleX, final float angleZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationYXZ(angleY, angleX, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30();
            final float ty = this.m31();
            final float tz = this.m32();
            return dest.rotationYXZ(angleY, angleX, angleZ).setTranslation(tx, ty, tz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return dest.rotateAffineYXZ(angleY, angleX, angleZ);
        }
        return this.rotateYXZInternal(angleY, angleX, angleZ, dest);
    }
    
    private Matrix4f rotateYXZInternal(final float angleY, final float angleX, final float angleZ, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float m_sinZ = -sinZ;
        final float nm20 = this.m00() * sinY + this.m20() * cosY;
        final float nm21 = this.m01() * sinY + this.m21() * cosY;
        final float nm22 = this.m02() * sinY + this.m22() * cosY;
        final float nm23 = this.m03() * sinY + this.m23() * cosY;
        final float nm24 = this.m00() * cosY + this.m20() * m_sinY;
        final float nm25 = this.m01() * cosY + this.m21() * m_sinY;
        final float nm26 = this.m02() * cosY + this.m22() * m_sinY;
        final float nm27 = this.m03() * cosY + this.m23() * m_sinY;
        final float nm28 = this.m10() * cosX + nm20 * sinX;
        final float nm29 = this.m11() * cosX + nm21 * sinX;
        final float nm30 = this.m12() * cosX + nm22 * sinX;
        final float nm31 = this.m13() * cosX + nm23 * sinX;
        return dest._m20(this.m10() * m_sinX + nm20 * cosX)._m21(this.m11() * m_sinX + nm21 * cosX)._m22(this.m12() * m_sinX + nm22 * cosX)._m23(this.m13() * m_sinX + nm23 * cosX)._m00(nm24 * cosZ + nm28 * sinZ)._m01(nm25 * cosZ + nm29 * sinZ)._m02(nm26 * cosZ + nm30 * sinZ)._m03(nm27 * cosZ + nm31 * sinZ)._m10(nm24 * m_sinZ + nm28 * cosZ)._m11(nm25 * m_sinZ + nm29 * cosZ)._m12(nm26 * m_sinZ + nm30 * cosZ)._m13(nm27 * m_sinZ + nm31 * cosZ)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffineYXZ(final float angleY, final float angleX, final float angleZ) {
        return this.rotateAffineYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4f rotateAffineYXZ(final float angleY, final float angleX, final float angleZ, final Matrix4f dest) {
        final float sinX = Math.sin(angleX);
        final float cosX = Math.cosFromSin(sinX, angleX);
        final float sinY = Math.sin(angleY);
        final float cosY = Math.cosFromSin(sinY, angleY);
        final float sinZ = Math.sin(angleZ);
        final float cosZ = Math.cosFromSin(sinZ, angleZ);
        final float m_sinY = -sinY;
        final float m_sinX = -sinX;
        final float m_sinZ = -sinZ;
        final float nm20 = this.m00() * sinY + this.m20() * cosY;
        final float nm21 = this.m01() * sinY + this.m21() * cosY;
        final float nm22 = this.m02() * sinY + this.m22() * cosY;
        final float nm23 = this.m00() * cosY + this.m20() * m_sinY;
        final float nm24 = this.m01() * cosY + this.m21() * m_sinY;
        final float nm25 = this.m02() * cosY + this.m22() * m_sinY;
        final float nm26 = this.m10() * cosX + nm20 * sinX;
        final float nm27 = this.m11() * cosX + nm21 * sinX;
        final float nm28 = this.m12() * cosX + nm22 * sinX;
        return dest._m20(this.m10() * m_sinX + nm20 * cosX)._m21(this.m11() * m_sinX + nm21 * cosX)._m22(this.m12() * m_sinX + nm22 * cosX)._m23(0.0f)._m00(nm23 * cosZ + nm26 * sinZ)._m01(nm24 * cosZ + nm27 * sinZ)._m02(nm25 * cosZ + nm28 * sinZ)._m03(0.0f)._m10(nm23 * m_sinZ + nm26 * cosZ)._m11(nm24 * m_sinZ + nm27 * cosZ)._m12(nm25 * m_sinZ + nm28 * cosZ)._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotate(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
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
    
    private Matrix4f rotateGeneric(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotateX(x * ang, dest);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotateY(y * ang, dest);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotateZ(z * ang, dest);
        }
        return this.rotateGenericInternal(ang, x, y, z, dest);
    }
    
    private Matrix4f rotateGenericInternal(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
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
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3;
        final float nm5 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm6 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm7 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        final float nm8 = this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6;
        return dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(this.m03() * rm7 + this.m13() * rm8 + this.m23() * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotate(final float ang, final float x, final float y, final float z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix4f rotateTranslation(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
        final float tx = this.m30();
        final float ty = this.m31();
        final float tz = this.m32();
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return dest.rotationX(x * ang).setTranslation(tx, ty, tz);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return dest.rotationY(y * ang).setTranslation(tx, ty, tz);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return dest.rotationZ(z * ang).setTranslation(tx, ty, tz);
        }
        return this.rotateTranslationInternal(ang, x, y, z, dest);
    }
    
    private Matrix4f rotateTranslationInternal(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
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
        return dest._m20(rm7)._m21(rm8)._m22(rm9)._m23(0.0f)._m00(rm00)._m01(rm2)._m02(rm3)._m03(0.0f)._m10(rm4)._m11(rm5)._m12(rm6)._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(1.0f)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffine(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotateX(x * ang, dest);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotateY(y * ang, dest);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotateZ(z * ang, dest);
        }
        return this.rotateAffineInternal(ang, x, y, z, dest);
    }
    
    private Matrix4f rotateAffineInternal(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
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
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm5 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm6 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        return dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(0.0f)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(1.0f)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffine(final float ang, final float x, final float y, final float z) {
        return this.rotateAffine(ang, x, y, z, this);
    }
    
    public Matrix4f rotateLocal(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(ang, x, y, z);
        }
        return this.rotateLocalGeneric(ang, x, y, z, dest);
    }
    
    private Matrix4f rotateLocalGeneric(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotateLocalX(x * ang, dest);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotateLocalY(y * ang, dest);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotateLocalZ(z * ang, dest);
        }
        return this.rotateLocalGenericInternal(ang, x, y, z, dest);
    }
    
    private Matrix4f rotateLocalGenericInternal(final float ang, final float x, final float y, final float z, final Matrix4f dest) {
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
        final float nm00 = lm00 * this.m00() + lm4 * this.m01() + lm7 * this.m02();
        final float nm2 = lm2 * this.m00() + lm5 * this.m01() + lm8 * this.m02();
        final float nm3 = lm3 * this.m00() + lm6 * this.m01() + lm9 * this.m02();
        final float nm4 = lm00 * this.m10() + lm4 * this.m11() + lm7 * this.m12();
        final float nm5 = lm2 * this.m10() + lm5 * this.m11() + lm8 * this.m12();
        final float nm6 = lm3 * this.m10() + lm6 * this.m11() + lm9 * this.m12();
        final float nm7 = lm00 * this.m20() + lm4 * this.m21() + lm7 * this.m22();
        final float nm8 = lm2 * this.m20() + lm5 * this.m21() + lm8 * this.m22();
        final float nm9 = lm3 * this.m20() + lm6 * this.m21() + lm9 * this.m22();
        final float nm10 = lm00 * this.m30() + lm4 * this.m31() + lm7 * this.m32();
        final float nm11 = lm2 * this.m30() + lm5 * this.m31() + lm8 * this.m32();
        final float nm12 = lm3 * this.m30() + lm6 * this.m31() + lm9 * this.m32();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03())._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13())._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23())._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateLocal(final float ang, final float x, final float y, final float z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix4f rotateLocalX(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm02 = sin * this.m01() + cos * this.m02();
        final float nm3 = sin * this.m11() + cos * this.m12();
        final float nm4 = sin * this.m21() + cos * this.m22();
        final float nm5 = sin * this.m31() + cos * this.m32();
        return dest._m00(this.m00())._m01(cos * this.m01() - sin * this.m02())._m02(nm02)._m03(this.m03())._m10(this.m10())._m11(cos * this.m11() - sin * this.m12())._m12(nm3)._m13(this.m13())._m20(this.m20())._m21(cos * this.m21() - sin * this.m22())._m22(nm4)._m23(this.m23())._m30(this.m30())._m31(cos * this.m31() - sin * this.m32())._m32(nm5)._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateLocalX(final float ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix4f rotateLocalY(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm02 = -sin * this.m00() + cos * this.m02();
        final float nm3 = -sin * this.m10() + cos * this.m12();
        final float nm4 = -sin * this.m20() + cos * this.m22();
        final float nm5 = -sin * this.m30() + cos * this.m32();
        return dest._m00(cos * this.m00() + sin * this.m02())._m01(this.m01())._m02(nm02)._m03(this.m03())._m10(cos * this.m10() + sin * this.m12())._m11(this.m11())._m12(nm3)._m13(this.m13())._m20(cos * this.m20() + sin * this.m22())._m21(this.m21())._m22(nm4)._m23(this.m23())._m30(cos * this.m30() + sin * this.m32())._m31(this.m31())._m32(nm5)._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateLocalY(final float ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix4f rotateLocalZ(final float ang, final Matrix4f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm01 = sin * this.m00() + cos * this.m01();
        final float nm2 = sin * this.m10() + cos * this.m11();
        final float nm3 = sin * this.m20() + cos * this.m21();
        final float nm4 = sin * this.m30() + cos * this.m31();
        return dest._m00(cos * this.m00() - sin * this.m01())._m01(nm01)._m02(this.m02())._m03(this.m03())._m10(cos * this.m10() - sin * this.m11())._m11(nm2)._m12(this.m12())._m13(this.m13())._m20(cos * this.m20() - sin * this.m21())._m21(nm3)._m22(this.m22())._m23(this.m23())._m30(cos * this.m30() - sin * this.m31())._m31(nm4)._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateLocalZ(final float ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public Matrix4f translate(final Vector3fc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4f translate(final Vector3fc offset, final Matrix4f dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4f translate(final float x, final float y, final float z, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateGeneric(x, y, z, dest);
    }
    
    private Matrix4f translateGeneric(final float x, final float y, final float z, final Matrix4f dest) {
        MemUtil.INSTANCE.copy(this, dest);
        return dest._m30(Math.fma(this.m00(), x, Math.fma(this.m10(), y, Math.fma(this.m20(), z, this.m30()))))._m31(Math.fma(this.m01(), x, Math.fma(this.m11(), y, Math.fma(this.m21(), z, this.m31()))))._m32(Math.fma(this.m02(), x, Math.fma(this.m12(), y, Math.fma(this.m22(), z, this.m32()))))._m33(Math.fma(this.m03(), x, Math.fma(this.m13(), y, Math.fma(this.m23(), z, this.m33()))))._properties(this.properties & 0xFFFFFFFA);
    }
    
    public Matrix4f translate(final float x, final float y, final float z) {
        if ((this.properties & 0x4) != 0x0) {
            return this.translation(x, y, z);
        }
        return this.translateGeneric(x, y, z);
    }
    
    private Matrix4f translateGeneric(final float x, final float y, final float z) {
        return this._m30(Math.fma(this.m00(), x, Math.fma(this.m10(), y, Math.fma(this.m20(), z, this.m30()))))._m31(Math.fma(this.m01(), x, Math.fma(this.m11(), y, Math.fma(this.m21(), z, this.m31()))))._m32(Math.fma(this.m02(), x, Math.fma(this.m12(), y, Math.fma(this.m22(), z, this.m32()))))._m33(Math.fma(this.m03(), x, Math.fma(this.m13(), y, Math.fma(this.m23(), z, this.m33()))))._properties(this.properties & 0xFFFFFFFA);
    }
    
    public Matrix4f translateLocal(final Vector3fc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4f translateLocal(final Vector3fc offset, final Matrix4f dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4f translateLocal(final float x, final float y, final float z, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateLocalGeneric(x, y, z, dest);
    }
    
    private Matrix4f translateLocalGeneric(final float x, final float y, final float z, final Matrix4f dest) {
        final float nm00 = this.m00() + x * this.m03();
        final float nm2 = this.m01() + y * this.m03();
        final float nm3 = this.m02() + z * this.m03();
        final float nm4 = this.m10() + x * this.m13();
        final float nm5 = this.m11() + y * this.m13();
        final float nm6 = this.m12() + z * this.m13();
        final float nm7 = this.m20() + x * this.m23();
        final float nm8 = this.m21() + y * this.m23();
        final float nm9 = this.m22() + z * this.m23();
        final float nm10 = this.m30() + x * this.m33();
        final float nm11 = this.m31() + y * this.m33();
        final float nm12 = this.m32() + z * this.m33();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03())._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13())._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23())._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33())._properties(this.properties & 0xFFFFFFFA);
    }
    
    public Matrix4f translateLocal(final float x, final float y, final float z) {
        return this.translateLocal(x, y, z, this);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.m00());
        out.writeFloat(this.m01());
        out.writeFloat(this.m02());
        out.writeFloat(this.m03());
        out.writeFloat(this.m10());
        out.writeFloat(this.m11());
        out.writeFloat(this.m12());
        out.writeFloat(this.m13());
        out.writeFloat(this.m20());
        out.writeFloat(this.m21());
        out.writeFloat(this.m22());
        out.writeFloat(this.m23());
        out.writeFloat(this.m30());
        out.writeFloat(this.m31());
        out.writeFloat(this.m32());
        out.writeFloat(this.m33());
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        this._m00(in.readFloat())._m01(in.readFloat())._m02(in.readFloat())._m03(in.readFloat())._m10(in.readFloat())._m11(in.readFloat())._m12(in.readFloat())._m13(in.readFloat())._m20(in.readFloat())._m21(in.readFloat())._m22(in.readFloat())._m23(in.readFloat())._m30(in.readFloat())._m31(in.readFloat())._m32(in.readFloat())._m33(in.readFloat()).determineProperties();
    }
    
    public Matrix4f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.orthoGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f orthoGeneric(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        final float rm4 = (left + right) / (left - right);
        final float rm5 = (top + bottom) / (bottom - top);
        final float rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6 + this.m30())._m31(this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6 + this.m31())._m32(this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6 + this.m32())._m33(this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(this.m20() * rm3)._m21(this.m21() * rm3)._m22(this.m22() * rm3)._m23(this.m23() * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4f dest) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.ortho(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.orthoLHGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f orthoLHGeneric(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        final float rm4 = (left + right) / (left - right);
        final float rm5 = (top + bottom) / (bottom - top);
        final float rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6 + this.m30())._m31(this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6 + this.m31())._m32(this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6 + this.m32())._m33(this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(this.m20() * rm3)._m21(this.m21() * rm3)._m22(this.m22() * rm3)._m23(this.m23() * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4f dest) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f setOrtho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / (right - left))._m11(2.0f / (top - bottom))._m22((zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar))._properties(2);
        return this;
    }
    
    public Matrix4f setOrtho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setOrtho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f setOrthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / (right - left))._m11(2.0f / (top - bottom))._m22((zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar))._properties(2);
        return this;
    }
    
    public Matrix4f setOrthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setOrthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoSymmetric(width, height, zNear, zFar, zZeroToOne);
        }
        return this.orthoSymmetricGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f orthoSymmetricGeneric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = 2.0f / width;
        final float rm2 = 2.0f / height;
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        final float rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m20() * rm4 + this.m30())._m31(this.m21() * rm4 + this.m31())._m32(this.m22() * rm4 + this.m32())._m33(this.m23() * rm4 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(this.m20() * rm3)._m21(this.m21() * rm3)._m22(this.m22() * rm3)._m23(this.m23() * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final Matrix4f dest) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoSymmetric(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f orthoSymmetric(final float width, final float height, final float zNear, final float zFar) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrthoSymmetricLH(width, height, zNear, zFar, zZeroToOne);
        }
        return this.orthoSymmetricLHGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f orthoSymmetricLHGeneric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = 2.0f / width;
        final float rm2 = 2.0f / height;
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        final float rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest._m30(this.m20() * rm4 + this.m30())._m31(this.m21() * rm4 + this.m31())._m32(this.m22() * rm4 + this.m32())._m33(this.m23() * rm4 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(this.m20() * rm3)._m21(this.m21() * rm3)._m22(this.m22() * rm3)._m23(this.m23() * rm3)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final Matrix4f dest) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4f setOrthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / width)._m11(2.0f / height)._m22((zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar))._properties(2);
        return this;
    }
    
    public Matrix4f setOrthoSymmetric(final float width, final float height, final float zNear, final float zFar) {
        return this.setOrthoSymmetric(width, height, zNear, zFar, false);
    }
    
    public Matrix4f setOrthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / width)._m11(2.0f / height)._m22((zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear))._m32((zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar))._properties(2);
        return this;
    }
    
    public Matrix4f setOrthoSymmetricLH(final float width, final float height, final float zNear, final float zFar) {
        return this.setOrthoSymmetricLH(width, height, zNear, zFar, false);
    }
    
    public Matrix4f ortho2D(final float left, final float right, final float bottom, final float top, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho2D(left, right, bottom, top);
        }
        return this.ortho2DGeneric(left, right, bottom, top, dest);
    }
    
    private Matrix4f ortho2DGeneric(final float left, final float right, final float bottom, final float top, final Matrix4f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (right + left) / (left - right);
        final float rm4 = (top + bottom) / (bottom - top);
        dest._m30(this.m00() * rm3 + this.m10() * rm4 + this.m30())._m31(this.m01() * rm3 + this.m11() * rm4 + this.m31())._m32(this.m02() * rm3 + this.m12() * rm4 + this.m32())._m33(this.m03() * rm3 + this.m13() * rm4 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(-this.m23())._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f ortho2D(final float left, final float right, final float bottom, final float top) {
        return this.ortho2D(left, right, bottom, top, this);
    }
    
    public Matrix4f ortho2DLH(final float left, final float right, final float bottom, final float top, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setOrtho2DLH(left, right, bottom, top);
        }
        return this.ortho2DLHGeneric(left, right, bottom, top, dest);
    }
    
    private Matrix4f ortho2DLHGeneric(final float left, final float right, final float bottom, final float top, final Matrix4f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (right + left) / (left - right);
        final float rm4 = (top + bottom) / (bottom - top);
        dest._m30(this.m00() * rm3 + this.m10() * rm4 + this.m30())._m31(this.m01() * rm3 + this.m11() * rm4 + this.m31())._m32(this.m02() * rm3 + this.m12() * rm4 + this.m32())._m33(this.m03() * rm3 + this.m13() * rm4 + this.m33())._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f ortho2DLH(final float left, final float right, final float bottom, final float top) {
        return this.ortho2DLH(left, right, bottom, top, this);
    }
    
    public Matrix4f setOrtho2D(final float left, final float right, final float bottom, final float top) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / (right - left))._m11(2.0f / (top - bottom))._m22(-1.0f)._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._properties(2);
        return this;
    }
    
    public Matrix4f setOrtho2DLH(final float left, final float right, final float bottom, final float top) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(2.0f / (right - left))._m11(2.0f / (top - bottom))._m30((right + left) / (left - right))._m31((top + bottom) / (bottom - top))._properties(2);
        return this;
    }
    
    public Matrix4f lookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4f lookAlong(final Vector3fc dir, final Vector3fc up, final Matrix4f dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4f lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAlong(dirX, dirY, dirZ, upX, upY, upZ);
        }
        return this.lookAlongGeneric(dirX, dirY, dirZ, upX, upY, upZ, dest);
    }
    
    private Matrix4f lookAlongGeneric(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
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
        final float nm00 = this.m00() * leftX + this.m10() * upnX + this.m20() * dirX;
        final float nm2 = this.m01() * leftX + this.m11() * upnX + this.m21() * dirX;
        final float nm3 = this.m02() * leftX + this.m12() * upnX + this.m22() * dirX;
        final float nm4 = this.m03() * leftX + this.m13() * upnX + this.m23() * dirX;
        final float nm5 = this.m00() * leftY + this.m10() * upnY + this.m20() * dirY;
        final float nm6 = this.m01() * leftY + this.m11() * upnY + this.m21() * dirY;
        final float nm7 = this.m02() * leftY + this.m12() * upnY + this.m22() * dirY;
        final float nm8 = this.m03() * leftY + this.m13() * upnY + this.m23() * dirY;
        return dest._m20(this.m00() * leftZ + this.m10() * upnZ + this.m20() * dirZ)._m21(this.m01() * leftZ + this.m11() * upnZ + this.m21() * dirZ)._m22(this.m02() * leftZ + this.m12() * upnZ + this.m22() * dirZ)._m23(this.m03() * leftZ + this.m13() * upnZ + this.m23() * dirZ)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4f setLookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4f setLookAlong(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ) {
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
        this._m00(leftX)._m01(dirY * leftZ - dirZ * leftY)._m02(dirX)._m03(0.0f)._m10(leftY)._m11(dirZ * leftX - dirX * leftZ)._m12(dirY)._m13(0.0f)._m20(leftZ)._m21(dirX * leftY - dirY * leftX)._m22(dirZ)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f setLookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.setLookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4f setLookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        float dirX = eyeX - centerX;
        float dirY = eyeY - centerY;
        float dirZ = eyeZ - centerZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        return this._m00(leftX)._m01(upnX)._m02(dirX)._m03(0.0f)._m10(leftY)._m11(upnY)._m12(dirY)._m13(0.0f)._m20(leftZ)._m21(upnZ)._m22(dirZ)._m23(0.0f)._m30(-(leftX * eyeX + leftY * eyeY + leftZ * eyeZ))._m31(-(upnX * eyeX + upnY * eyeY + upnZ * eyeZ))._m32(-(dirX * eyeX + dirY * eyeY + dirZ * eyeZ))._m33(1.0f)._properties(18);
    }
    
    public Matrix4f lookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up, final Matrix4f dest) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4f lookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4f lookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.lookAtPerspective(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
        }
        return this.lookAtGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4f lookAtGeneric(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        float dirX = eyeX - centerX;
        float dirY = eyeY - centerY;
        float dirZ = eyeZ - centerZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final float rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final float nm00 = this.m00() * leftX + this.m10() * upnX + this.m20() * dirX;
        final float nm2 = this.m01() * leftX + this.m11() * upnX + this.m21() * dirX;
        final float nm3 = this.m02() * leftX + this.m12() * upnX + this.m22() * dirX;
        final float nm4 = this.m03() * leftX + this.m13() * upnX + this.m23() * dirX;
        final float nm5 = this.m00() * leftY + this.m10() * upnY + this.m20() * dirY;
        final float nm6 = this.m01() * leftY + this.m11() * upnY + this.m21() * dirY;
        final float nm7 = this.m02() * leftY + this.m12() * upnY + this.m22() * dirY;
        final float nm8 = this.m03() * leftY + this.m13() * upnY + this.m23() * dirY;
        return dest._m30(this.m00() * rm30 + this.m10() * rm31 + this.m20() * rm32 + this.m30())._m31(this.m01() * rm30 + this.m11() * rm31 + this.m21() * rm32 + this.m31())._m32(this.m02() * rm30 + this.m12() * rm31 + this.m22() * rm32 + this.m32())._m33(this.m03() * rm30 + this.m13() * rm31 + this.m23() * rm32 + this.m33())._m20(this.m00() * leftZ + this.m10() * upnZ + this.m20() * dirZ)._m21(this.m01() * leftZ + this.m11() * upnZ + this.m21() * dirZ)._m22(this.m02() * leftZ + this.m12() * upnZ + this.m22() * dirZ)._m23(this.m03() * leftZ + this.m13() * upnZ + this.m23() * dirZ)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f lookAtPerspective(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        float dirX = eyeX - centerX;
        float dirY = eyeY - centerY;
        float dirZ = eyeZ - centerZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final float rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final float nm10 = this.m00() * leftY;
        final float nm11 = this.m00() * leftZ;
        final float nm12 = this.m11() * upnZ;
        final float nm13 = this.m00() * rm30;
        final float nm14 = this.m11() * rm31;
        final float nm15 = this.m22() * rm32 + this.m32();
        final float nm16 = this.m23() * rm32;
        return dest._m00(this.m00() * leftX)._m01(this.m11() * upnX)._m02(this.m22() * dirX)._m03(this.m23() * dirX)._m10(nm10)._m11(this.m11() * upnY)._m12(this.m22() * dirY)._m13(this.m23() * dirY)._m20(nm11)._m21(nm12)._m22(this.m22() * dirZ)._m23(this.m23() * dirZ)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f lookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        return this.lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4f setLookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.setLookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4f setLookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        float dirX = centerX - eyeX;
        float dirY = centerY - eyeY;
        float dirZ = centerZ - eyeZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        this._m00(leftX)._m01(upnX)._m02(dirX)._m03(0.0f)._m10(leftY)._m11(upnY)._m12(dirY)._m13(0.0f)._m20(leftZ)._m21(upnZ)._m22(dirZ)._m23(0.0f)._m30(-(leftX * eyeX + leftY * eyeY + leftZ * eyeZ))._m31(-(upnX * eyeX + upnY * eyeY + upnZ * eyeZ))._m32(-(dirX * eyeX + dirY * eyeY + dirZ * eyeZ))._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f lookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up, final Matrix4f dest) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4f lookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4f lookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        if ((this.properties & 0x1) != 0x0) {
            return this.lookAtPerspectiveLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
        }
        return this.lookAtLHGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4f lookAtLHGeneric(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        float dirX = centerX - eyeX;
        float dirY = centerY - eyeY;
        float dirZ = centerZ - eyeZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final float rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final float nm00 = this.m00() * leftX + this.m10() * upnX + this.m20() * dirX;
        final float nm2 = this.m01() * leftX + this.m11() * upnX + this.m21() * dirX;
        final float nm3 = this.m02() * leftX + this.m12() * upnX + this.m22() * dirX;
        final float nm4 = this.m03() * leftX + this.m13() * upnX + this.m23() * dirX;
        final float nm5 = this.m00() * leftY + this.m10() * upnY + this.m20() * dirY;
        final float nm6 = this.m01() * leftY + this.m11() * upnY + this.m21() * dirY;
        final float nm7 = this.m02() * leftY + this.m12() * upnY + this.m22() * dirY;
        final float nm8 = this.m03() * leftY + this.m13() * upnY + this.m23() * dirY;
        return dest._m30(this.m00() * rm30 + this.m10() * rm31 + this.m20() * rm32 + this.m30())._m31(this.m01() * rm30 + this.m11() * rm31 + this.m21() * rm32 + this.m31())._m32(this.m02() * rm30 + this.m12() * rm31 + this.m22() * rm32 + this.m32())._m33(this.m03() * rm30 + this.m13() * rm31 + this.m23() * rm32 + this.m33())._m20(this.m00() * leftZ + this.m10() * upnZ + this.m20() * dirZ)._m21(this.m01() * leftZ + this.m11() * upnZ + this.m21() * dirZ)._m22(this.m02() * leftZ + this.m12() * upnZ + this.m22() * dirZ)._m23(this.m03() * leftZ + this.m13() * upnZ + this.m23() * dirZ)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f lookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        return this.lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4f lookAtPerspectiveLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
        float dirX = centerX - eyeX;
        float dirY = centerY - eyeY;
        float dirZ = centerZ - eyeZ;
        final float invDirLength = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLength;
        dirY *= invDirLength;
        dirZ *= invDirLength;
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
        final float rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        final float nm00 = this.m00() * leftX;
        final float nm2 = this.m11() * upnX;
        final float nm3 = this.m22() * dirX;
        final float nm4 = this.m23() * dirX;
        final float nm5 = this.m00() * leftY;
        final float nm6 = this.m11() * upnY;
        final float nm7 = this.m22() * dirY;
        final float nm8 = this.m23() * dirY;
        final float nm9 = this.m00() * leftZ;
        final float nm10 = this.m11() * upnZ;
        final float nm11 = this.m22() * dirZ;
        final float nm12 = this.m23() * dirZ;
        final float nm13 = this.m00() * rm30;
        final float nm14 = this.m11() * rm31;
        final float nm15 = this.m22() * rm32 + this.m32();
        final float nm16 = this.m23() * rm32;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._m30(nm13)._m31(nm14)._m32(nm15)._m33(nm16)._properties(0);
    }
    
    public Matrix4f tile(final int x, final int y, final int w, final int h) {
        return this.tile(x, y, w, h, this);
    }
    
    public Matrix4f tile(final int x, final int y, final int w, final int h, final Matrix4f dest) {
        final float tx = (float)(w - 1 - (x << 1));
        final float ty = (float)(h - 1 - (y << 1));
        return dest._m30(Math.fma(this.m00(), tx, Math.fma(this.m10(), ty, this.m30())))._m31(Math.fma(this.m01(), tx, Math.fma(this.m11(), ty, this.m31())))._m32(Math.fma(this.m02(), tx, Math.fma(this.m12(), ty, this.m32())))._m33(Math.fma(this.m03(), tx, Math.fma(this.m13(), ty, this.m33())))._m00(this.m00() * w)._m01(this.m01() * w)._m02(this.m02() * w)._m03(this.m03() * w)._m10(this.m10() * h)._m11(this.m11() * h)._m12(this.m12() * h)._m13(this.m13() * h)._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._properties(this.properties & 0xFFFFFFE2);
    }
    
    public Matrix4f perspective(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspective(fovy, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveGeneric(fovy, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f perspectiveGeneric(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float h = Math.tan(fovy * 0.5f);
        final float rm00 = 1.0f / (h * aspect);
        final float rm2 = 1.0f / h;
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm3;
        float rm4;
        if (farInf) {
            final float e = 1.0E-6f;
            rm3 = e - 1.0f;
            rm4 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm3 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm4 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m20() * rm3 - this.m30();
        final float nm21 = this.m21() * rm3 - this.m31();
        final float nm22 = this.m22() * rm3 - this.m32();
        final float nm23 = this.m23() * rm3 - this.m33();
        dest._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m30(this.m20() * rm4)._m31(this.m21() * rm4)._m32(this.m22() * rm4)._m33(this.m23() * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4f perspective(final float fovy, final float aspect, final float zNear, final float zFar, final Matrix4f dest) {
        return this.perspective(fovy, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4f perspective(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspective(fovy, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspective(final float fovy, final float aspect, final float zNear, final float zFar) {
        return this.perspective(fovy, aspect, zNear, zFar, this);
    }
    
    public Matrix4f perspectiveRect(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveRect(width, height, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveRectGeneric(width, height, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f perspectiveRectGeneric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = (zNear + zNear) / width;
        final float rm2 = (zNear + zNear) / height;
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm3;
        float rm4;
        if (farInf) {
            final float e = 1.0E-6f;
            rm3 = e - 1.0f;
            rm4 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm3 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm4 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m20() * rm3 - this.m30();
        final float nm21 = this.m21() * rm3 - this.m31();
        final float nm22 = this.m22() * rm3 - this.m32();
        final float nm23 = this.m23() * rm3 - this.m33();
        dest._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m30(this.m20() * rm4)._m31(this.m21() * rm4)._m32(this.m22() * rm4)._m33(this.m23() * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4f perspectiveRect(final float width, final float height, final float zNear, final float zFar, final Matrix4f dest) {
        return this.perspectiveRect(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4f perspectiveRect(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspectiveRect(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspectiveRect(final float width, final float height, final float zNear, final float zFar) {
        return this.perspectiveRect(width, height, zNear, zFar, this);
    }
    
    public Matrix4f perspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveOffCenterGeneric(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f perspectiveOffCenterGeneric(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float h = Math.tan(fovy * 0.5f);
        final float xScale = 1.0f / (h * aspect);
        final float yScale = 1.0f / h;
        final float offX = Math.tan(offAngleX);
        final float offY = Math.tan(offAngleY);
        final float rm20 = offX * xScale;
        final float rm21 = offY * yScale;
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm22;
        float rm23;
        if (farInf) {
            final float e = 1.0E-6f;
            rm22 = e - 1.0f;
            rm23 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm22 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm23 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm22 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm23 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m00() * rm20 + this.m10() * rm21 + this.m20() * rm22 - this.m30();
        final float nm21 = this.m01() * rm20 + this.m11() * rm21 + this.m21() * rm22 - this.m31();
        final float nm22 = this.m02() * rm20 + this.m12() * rm21 + this.m22() * rm22 - this.m32();
        final float nm23 = this.m03() * rm20 + this.m13() * rm21 + this.m23() * rm22 - this.m33();
        dest._m00(this.m00() * xScale)._m01(this.m01() * xScale)._m02(this.m02() * xScale)._m03(this.m03() * xScale)._m10(this.m10() * yScale)._m11(this.m11() * yScale)._m12(this.m12() * yScale)._m13(this.m13() * yScale)._m30(this.m20() * rm23)._m31(this.m21() * rm23)._m32(this.m22() * rm23)._m33(this.m23() * rm23)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & ~(0x1E | ((rm20 != 0.0f || rm21 != 0.0f) ? 1 : 0)));
        return dest;
    }
    
    public Matrix4f perspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar, final Matrix4f dest) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4f perspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar) {
        return this.perspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, this);
    }
    
    public Matrix4f perspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        return this.frustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne, dest);
    }
    
    public Matrix4f perspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar) {
        return this.perspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, this);
    }
    
    public Matrix4f perspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final Matrix4f dest) {
        return this.frustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, dest);
    }
    
    public Matrix4f perspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        return this.frustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne, dest);
    }
    
    public Matrix4f perspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar) {
        return this.perspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, this);
    }
    
    public Matrix4f perspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final Matrix4f dest) {
        return this.frustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, dest);
    }
    
    public Matrix4f setPerspective(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.zero(this);
        final float h = Math.tan(fovy * 0.5f);
        this._m00(1.0f / (h * aspect))._m11(1.0f / h);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(e - 1.0f)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        return this._m23(-1.0f)._properties(1);
    }
    
    public Matrix4f setPerspective(final float fovy, final float aspect, final float zNear, final float zFar) {
        return this.setPerspective(fovy, aspect, zNear, zFar, false);
    }
    
    public Matrix4f setPerspectiveRect(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.zero(this);
        this._m00((zNear + zNear) / width)._m11((zNear + zNear) / height);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(e - 1.0f)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0f)._properties(1);
        return this;
    }
    
    public Matrix4f setPerspectiveRect(final float width, final float height, final float zNear, final float zFar) {
        return this.setPerspectiveRect(width, height, zNear, zFar, false);
    }
    
    public Matrix4f setPerspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar) {
        return this.setPerspectiveOffCenter(fovy, offAngleX, offAngleY, aspect, zNear, zFar, false);
    }
    
    public Matrix4f setPerspectiveOffCenter(final float fovy, final float offAngleX, final float offAngleY, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.zero(this);
        final float h = Math.tan(fovy * 0.5f);
        final float xScale = 1.0f / (h * aspect);
        final float yScale = 1.0f / h;
        final float offX = Math.tan(offAngleX);
        final float offY = Math.tan(offAngleY);
        this._m00(xScale)._m11(yScale)._m20(offX * xScale)._m21(offY * yScale);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(e - 1.0f)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0f)._properties((offAngleX == 0.0f && offAngleY == 0.0f) ? 1 : 0);
        return this;
    }
    
    public Matrix4f setPerspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar) {
        return this.setPerspectiveOffCenterFov(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }
    
    public Matrix4f setPerspectiveOffCenterFov(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.setFrustum(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne);
    }
    
    public Matrix4f setPerspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar) {
        return this.setPerspectiveOffCenterFovLH(angleLeft, angleRight, angleDown, angleUp, zNear, zFar, false);
    }
    
    public Matrix4f setPerspectiveOffCenterFovLH(final float angleLeft, final float angleRight, final float angleDown, final float angleUp, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.setFrustumLH(Math.tan(angleLeft) * zNear, Math.tan(angleRight) * zNear, Math.tan(angleDown) * zNear, Math.tan(angleUp) * zNear, zNear, zFar, zZeroToOne);
    }
    
    public Matrix4f perspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setPerspectiveLH(fovy, aspect, zNear, zFar, zZeroToOne);
        }
        return this.perspectiveLHGeneric(fovy, aspect, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f perspectiveLHGeneric(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float h = Math.tan(fovy * 0.5f);
        final float rm00 = 1.0f / (h * aspect);
        final float rm2 = 1.0f / h;
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm3;
        float rm4;
        if (farInf) {
            final float e = 1.0E-6f;
            rm3 = 1.0f - e;
            rm4 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm3 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm4 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm3 = (zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear);
            rm4 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m20() * rm3 + this.m30();
        final float nm21 = this.m21() * rm3 + this.m31();
        final float nm22 = this.m22() * rm3 + this.m32();
        final float nm23 = this.m23() * rm3 + this.m33();
        dest._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m30(this.m20() * rm4)._m31(this.m21() * rm4)._m32(this.m22() * rm4)._m33(this.m23() * rm4)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(this.properties & 0xFFFFFFE1);
        return dest;
    }
    
    public Matrix4f perspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f perspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar, final Matrix4f dest) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, false, dest);
    }
    
    public Matrix4f perspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar) {
        return this.perspectiveLH(fovy, aspect, zNear, zFar, this);
    }
    
    public Matrix4f setPerspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.zero(this);
        final float h = Math.tan(fovy * 0.5f);
        this._m00(1.0f / (h * aspect))._m11(1.0f / h);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(1.0f - e)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(1.0f)._properties(1);
        return this;
    }
    
    public Matrix4f setPerspectiveLH(final float fovy, final float aspect, final float zNear, final float zFar) {
        return this.setPerspectiveLH(fovy, aspect, zNear, zFar, false);
    }
    
    public Matrix4f frustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setFrustum(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.frustumGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f frustumGeneric(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = (zNear + zNear) / (right - left);
        final float rm2 = (zNear + zNear) / (top - bottom);
        final float rm3 = (right + left) / (right - left);
        final float rm4 = (top + bottom) / (top - bottom);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm5;
        float rm6;
        if (farInf) {
            final float e = 1.0E-6f;
            rm5 = e - 1.0f;
            rm6 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm5 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm6 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm5 = (zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar);
            rm6 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m00() * rm3 + this.m10() * rm4 + this.m20() * rm5 - this.m30();
        final float nm21 = this.m01() * rm3 + this.m11() * rm4 + this.m21() * rm5 - this.m31();
        final float nm22 = this.m02() * rm3 + this.m12() * rm4 + this.m22() * rm5 - this.m32();
        final float nm23 = this.m03() * rm3 + this.m13() * rm4 + this.m23() * rm5 - this.m33();
        dest._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m30(this.m20() * rm6)._m31(this.m21() * rm6)._m32(this.m22() * rm6)._m33(this.m23() * rm6)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(0);
        return dest;
    }
    
    public Matrix4f frustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4f dest) {
        return this.frustum(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4f frustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.frustum(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f frustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.frustum(left, right, bottom, top, zNear, zFar, this);
    }
    
    public Matrix4f setFrustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00((zNear + zNear) / (right - left))._m11((zNear + zNear) / (top - bottom))._m20((right + left) / (right - left))._m21((top + bottom) / (top - bottom));
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(e - 1.0f)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zNear - zFar))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        this._m23(-1.0f)._m33(0.0f)._properties((this.m20() == 0.0f && this.m21() == 0.0f) ? 1 : 0);
        return this;
    }
    
    public Matrix4f setFrustum(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setFrustum(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f frustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setFrustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne);
        }
        return this.frustumLHGeneric(left, right, bottom, top, zNear, zFar, zZeroToOne, dest);
    }
    
    private Matrix4f frustumLHGeneric(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4f dest) {
        final float rm00 = (zNear + zNear) / (right - left);
        final float rm2 = (zNear + zNear) / (top - bottom);
        final float rm3 = (right + left) / (right - left);
        final float rm4 = (top + bottom) / (top - bottom);
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        float rm5;
        float rm6;
        if (farInf) {
            final float e = 1.0E-6f;
            rm5 = 1.0f - e;
            rm6 = (e - (zZeroToOne ? 1.0f : 2.0f)) * zNear;
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            rm5 = (zZeroToOne ? 0.0f : 1.0f) - e;
            rm6 = ((zZeroToOne ? 1.0f : 2.0f) - e) * zFar;
        }
        else {
            rm5 = (zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear);
            rm6 = (zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar);
        }
        final float nm20 = this.m00() * rm3 + this.m10() * rm4 + this.m20() * rm5 + this.m30();
        final float nm21 = this.m01() * rm3 + this.m11() * rm4 + this.m21() * rm5 + this.m31();
        final float nm22 = this.m02() * rm3 + this.m12() * rm4 + this.m22() * rm5 + this.m32();
        final float nm23 = this.m03() * rm3 + this.m13() * rm4 + this.m23() * rm5 + this.m33();
        dest._m00(this.m00() * rm00)._m01(this.m01() * rm00)._m02(this.m02() * rm00)._m03(this.m03() * rm00)._m10(this.m10() * rm2)._m11(this.m11() * rm2)._m12(this.m12() * rm2)._m13(this.m13() * rm2)._m30(this.m20() * rm6)._m31(this.m21() * rm6)._m32(this.m22() * rm6)._m33(this.m23() * rm6)._m20(nm20)._m21(nm21)._m22(nm22)._m23(nm23)._properties(0);
        return dest;
    }
    
    public Matrix4f frustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4f frustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4f dest) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4f frustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.frustumLH(left, right, bottom, top, zNear, zFar, this);
    }
    
    public Matrix4f setFrustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00((zNear + zNear) / (right - left))._m11((zNear + zNear) / (top - bottom))._m20((right + left) / (right - left))._m21((top + bottom) / (top - bottom));
        final boolean farInf = zFar > 0.0f && Float.isInfinite(zFar);
        final boolean nearInf = zNear > 0.0f && Float.isInfinite(zNear);
        if (farInf) {
            final float e = 1.0E-6f;
            this._m22(1.0f - e)._m32((e - (zZeroToOne ? 1.0f : 2.0f)) * zNear);
        }
        else if (nearInf) {
            final float e = 1.0E-6f;
            this._m22((zZeroToOne ? 0.0f : 1.0f) - e)._m32(((zZeroToOne ? 1.0f : 2.0f) - e) * zFar);
        }
        else {
            this._m22((zZeroToOne ? zFar : (zFar + zNear)) / (zFar - zNear))._m32((zZeroToOne ? zFar : (zFar + zFar)) * zNear / (zNear - zFar));
        }
        return this._m23(1.0f)._m33(0.0f)._properties(0);
    }
    
    public Matrix4f setFrustumLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setFrustumLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4f setFromIntrinsic(final float alphaX, final float alphaY, final float gamma, final float u0, final float v0, final int imgWidth, final int imgHeight, final float near, final float far) {
        final float l00 = 2.0f / imgWidth;
        final float l2 = 2.0f / imgHeight;
        final float l3 = 2.0f / (near - far);
        return this._m00(l00 * alphaX)._m01(0.0f)._m02(0.0f)._m03(0.0f)._m10(l00 * gamma)._m11(l2 * alphaY)._m12(0.0f)._m13(0.0f)._m20(l00 * u0 - 1.0f)._m21(l2 * v0 - 1.0f)._m22(l3 * -(near + far) + (far + near) / (near - far))._m23(-1.0f)._m30(0.0f)._m31(0.0f)._m32(l3 * -near * far)._m33(0.0f)._properties(1);
    }
    
    public Matrix4f rotate(final Quaternionfc quat, final Matrix4f dest) {
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
    
    private Matrix4f rotateGeneric(final Quaternionfc quat, final Matrix4f dest) {
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
        final float rm4 = -dzw + dxy;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3;
        final float nm5 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm6 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm7 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        final float nm8 = this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6;
        return dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(this.m03() * rm7 + this.m13() * rm8 + this.m23() * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4f rotateAffine(final Quaternionfc quat, final Matrix4f dest) {
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
        final float rm4 = -dzw + dxy;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm5 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm6 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        return dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(0.0f)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAffine(final Quaternionfc quat) {
        return this.rotateAffine(quat, this);
    }
    
    public Matrix4f rotateTranslation(final Quaternionfc quat, final Matrix4f dest) {
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
        final float rm4 = -dzw + dxy;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        return dest._m20(rm7)._m21(rm8)._m22(rm9)._m23(0.0f)._m00(rm00)._m01(rm2)._m02(rm3)._m03(0.0f)._m10(rm4)._m11(rm5)._m12(rm6)._m13(0.0f)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateAround(final Quaternionfc quat, final float ox, final float oy, final float oz) {
        return this.rotateAround(quat, ox, oy, oz, this);
    }
    
    public Matrix4f rotateAroundAffine(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4f dest) {
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
        final float rm4 = -dzw + dxy;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        final float tm30 = this.m00() * ox + this.m10() * oy + this.m20() * oz + this.m30();
        final float tm31 = this.m01() * ox + this.m11() * oy + this.m21() * oz + this.m31();
        final float tm32 = this.m02() * ox + this.m12() * oy + this.m22() * oz + this.m32();
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm5 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm6 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(0.0f)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m30(-nm00 * ox - nm4 * oy - this.m20() * oz + tm30)._m31(-nm2 * ox - nm5 * oy - this.m21() * oz + tm31)._m32(-nm3 * ox - nm6 * oy - this.m22() * oz + tm32)._m33(1.0f)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f rotateAround(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.rotationAround(quat, ox, oy, oz);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.rotateAroundAffine(quat, ox, oy, oz, dest);
        }
        return this.rotateAroundGeneric(quat, ox, oy, oz, dest);
    }
    
    private Matrix4f rotateAroundGeneric(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4f dest) {
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
        final float rm4 = -dzw + dxy;
        final float rm5 = y2 - z2 + w2 - x2;
        final float rm6 = dyz + dxw;
        final float rm7 = dyw + dxz;
        final float rm8 = dyz - dxw;
        final float rm9 = z2 - y2 - x2 + w2;
        final float tm30 = this.m00() * ox + this.m10() * oy + this.m20() * oz + this.m30();
        final float tm31 = this.m01() * ox + this.m11() * oy + this.m21() * oz + this.m31();
        final float tm32 = this.m02() * ox + this.m12() * oy + this.m22() * oz + this.m32();
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3;
        final float nm5 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm6 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm7 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        final float nm8 = this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6;
        dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(this.m03() * rm7 + this.m13() * rm8 + this.m23() * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m30(-nm00 * ox - nm5 * oy - this.m20() * oz + tm30)._m31(-nm2 * ox - nm6 * oy - this.m21() * oz + tm31)._m32(-nm3 * ox - nm7 * oy - this.m22() * oz + tm32)._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f rotationAround(final Quaternionfc quat, final float ox, final float oy, final float oz) {
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
        this._m20(dyw + dxz)._m21(dyz - dxw)._m22(z2 - y2 - x2 + w2)._m23(0.0f)._m00(w2 + x2 - z2 - y2)._m01(dxy + dzw)._m02(dxz - dyw)._m03(0.0f)._m10(-dzw + dxy)._m11(y2 - z2 + w2 - x2)._m12(dyz + dxw)._m13(0.0f)._m30(-this.m00() * ox - this.m10() * oy - this.m20() * oz + ox)._m31(-this.m01() * ox - this.m11() * oy - this.m21() * oz + oy)._m32(-this.m02() * ox - this.m12() * oy - this.m22() * oz + oz)._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f rotateLocal(final Quaternionfc quat, final Matrix4f dest) {
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
        final float lm4 = -dzw + dxy;
        final float lm5 = y2 - z2 + w2 - x2;
        final float lm6 = dyz + dxw;
        final float lm7 = dyw + dxz;
        final float lm8 = dyz - dxw;
        final float lm9 = z2 - y2 - x2 + w2;
        final float nm00 = lm00 * this.m00() + lm4 * this.m01() + lm7 * this.m02();
        final float nm2 = lm2 * this.m00() + lm5 * this.m01() + lm8 * this.m02();
        final float nm3 = lm3 * this.m00() + lm6 * this.m01() + lm9 * this.m02();
        final float nm4 = lm00 * this.m10() + lm4 * this.m11() + lm7 * this.m12();
        final float nm5 = lm2 * this.m10() + lm5 * this.m11() + lm8 * this.m12();
        final float nm6 = lm3 * this.m10() + lm6 * this.m11() + lm9 * this.m12();
        final float nm7 = lm00 * this.m20() + lm4 * this.m21() + lm7 * this.m22();
        final float nm8 = lm2 * this.m20() + lm5 * this.m21() + lm8 * this.m22();
        final float nm9 = lm3 * this.m20() + lm6 * this.m21() + lm9 * this.m22();
        final float nm10 = lm00 * this.m30() + lm4 * this.m31() + lm7 * this.m32();
        final float nm11 = lm2 * this.m30() + lm5 * this.m31() + lm8 * this.m32();
        final float nm12 = lm3 * this.m30() + lm6 * this.m31() + lm9 * this.m32();
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(this.m03())._m10(nm4)._m11(nm5)._m12(nm6)._m13(this.m13())._m20(nm7)._m21(nm8)._m22(nm9)._m23(this.m23())._m30(nm10)._m31(nm11)._m32(nm12)._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
    }
    
    public Matrix4f rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4f rotateAroundLocal(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4f dest) {
        final float w2 = quat.w() * quat.w();
        final float x2 = quat.x() * quat.x();
        final float y2 = quat.y() * quat.y();
        final float z2 = quat.z() * quat.z();
        final float zw = quat.z() * quat.w();
        final float xy = quat.x() * quat.y();
        final float xz = quat.x() * quat.z();
        final float yw = quat.y() * quat.w();
        final float yz = quat.y() * quat.z();
        final float xw = quat.x() * quat.w();
        final float lm00 = w2 + x2 - z2 - y2;
        final float lm2 = xy + zw + zw + xy;
        final float lm3 = xz - yw + xz - yw;
        final float lm4 = -zw + xy - zw + xy;
        final float lm5 = y2 - z2 + w2 - x2;
        final float lm6 = yz + yz + xw + xw;
        final float lm7 = yw + xz + xz + yw;
        final float lm8 = yz + yz - xw - xw;
        final float lm9 = z2 - y2 - x2 + w2;
        final float tm00 = this.m00() - ox * this.m03();
        final float tm2 = this.m01() - oy * this.m03();
        final float tm3 = this.m02() - oz * this.m03();
        final float tm4 = this.m10() - ox * this.m13();
        final float tm5 = this.m11() - oy * this.m13();
        final float tm6 = this.m12() - oz * this.m13();
        final float tm7 = this.m20() - ox * this.m23();
        final float tm8 = this.m21() - oy * this.m23();
        final float tm9 = this.m22() - oz * this.m23();
        final float tm10 = this.m30() - ox * this.m33();
        final float tm11 = this.m31() - oy * this.m33();
        final float tm12 = this.m32() - oz * this.m33();
        dest._m00(lm00 * tm00 + lm4 * tm2 + lm7 * tm3 + ox * this.m03())._m01(lm2 * tm00 + lm5 * tm2 + lm8 * tm3 + oy * this.m03())._m02(lm3 * tm00 + lm6 * tm2 + lm9 * tm3 + oz * this.m03())._m03(this.m03())._m10(lm00 * tm4 + lm4 * tm5 + lm7 * tm6 + ox * this.m13())._m11(lm2 * tm4 + lm5 * tm5 + lm8 * tm6 + oy * this.m13())._m12(lm3 * tm4 + lm6 * tm5 + lm9 * tm6 + oz * this.m13())._m13(this.m13())._m20(lm00 * tm7 + lm4 * tm8 + lm7 * tm9 + ox * this.m23())._m21(lm2 * tm7 + lm5 * tm8 + lm8 * tm9 + oy * this.m23())._m22(lm3 * tm7 + lm6 * tm8 + lm9 * tm9 + oz * this.m23())._m23(this.m23())._m30(lm00 * tm10 + lm4 * tm11 + lm7 * tm12 + ox * this.m33())._m31(lm2 * tm10 + lm5 * tm11 + lm8 * tm12 + oy * this.m33())._m32(lm3 * tm10 + lm6 * tm11 + lm9 * tm12 + oz * this.m33())._m33(this.m33())._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f rotateAroundLocal(final Quaternionfc quat, final float ox, final float oy, final float oz) {
        return this.rotateAroundLocal(quat, ox, oy, oz, this);
    }
    
    public Matrix4f rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4f rotate(final AxisAngle4f axisAngle, final Matrix4f dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4f rotate(final float angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4f rotate(final float angle, final Vector3fc axis, final Matrix4f dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Vector4f unproject(final float winX, final float winY, final float winZ, final int[] viewport, final Vector4f dest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        final float im00 = (this.m11() * l - this.m12() * k + this.m13() * j) * det;
        final float im2 = (-this.m01() * l + this.m02() * k - this.m03() * j) * det;
        final float im3 = (this.m31() * f - this.m32() * e + this.m33() * d) * det;
        final float im4 = (-this.m21() * f + this.m22() * e - this.m23() * d) * det;
        final float im5 = (-this.m10() * l + this.m12() * i - this.m13() * h) * det;
        final float im6 = (this.m00() * l - this.m02() * i + this.m03() * h) * det;
        final float im7 = (-this.m30() * f + this.m32() * c - this.m33() * b) * det;
        final float im8 = (this.m20() * f - this.m22() * c + this.m23() * b) * det;
        final float im9 = (this.m10() * k - this.m11() * i + this.m13() * g) * det;
        final float im10 = (-this.m00() * k + this.m01() * i - this.m03() * g) * det;
        final float im11 = (this.m30() * e - this.m31() * c + this.m33() * a) * det;
        final float im12 = (-this.m20() * e + this.m21() * c - this.m23() * a) * det;
        final float im13 = (-this.m10() * j + this.m11() * h - this.m12() * g) * det;
        final float im14 = (this.m00() * j - this.m01() * h + this.m02() * g) * det;
        final float im15 = (-this.m30() * d + this.m31() * b - this.m32() * a) * det;
        final float im16 = (this.m20() * d - this.m21() * b + this.m22() * a) * det;
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float ndcZ = winZ + winZ - 1.0f;
        final float invW = 1.0f / (im4 * ndcX + im8 * ndcY + im12 * ndcZ + im16);
        return dest.set((im00 * ndcX + im5 * ndcY + im9 * ndcZ + im13) * invW, (im2 * ndcX + im6 * ndcY + im10 * ndcZ + im14) * invW, (im3 * ndcX + im7 * ndcY + im11 * ndcZ + im15) * invW, 1.0f);
    }
    
    public Vector3f unproject(final float winX, final float winY, final float winZ, final int[] viewport, final Vector3f dest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        final float im00 = (this.m11() * l - this.m12() * k + this.m13() * j) * det;
        final float im2 = (-this.m01() * l + this.m02() * k - this.m03() * j) * det;
        final float im3 = (this.m31() * f - this.m32() * e + this.m33() * d) * det;
        final float im4 = (-this.m21() * f + this.m22() * e - this.m23() * d) * det;
        final float im5 = (-this.m10() * l + this.m12() * i - this.m13() * h) * det;
        final float im6 = (this.m00() * l - this.m02() * i + this.m03() * h) * det;
        final float im7 = (-this.m30() * f + this.m32() * c - this.m33() * b) * det;
        final float im8 = (this.m20() * f - this.m22() * c + this.m23() * b) * det;
        final float im9 = (this.m10() * k - this.m11() * i + this.m13() * g) * det;
        final float im10 = (-this.m00() * k + this.m01() * i - this.m03() * g) * det;
        final float im11 = (this.m30() * e - this.m31() * c + this.m33() * a) * det;
        final float im12 = (-this.m20() * e + this.m21() * c - this.m23() * a) * det;
        final float im13 = (-this.m10() * j + this.m11() * h - this.m12() * g) * det;
        final float im14 = (this.m00() * j - this.m01() * h + this.m02() * g) * det;
        final float im15 = (-this.m30() * d + this.m31() * b - this.m32() * a) * det;
        final float im16 = (this.m20() * d - this.m21() * b + this.m22() * a) * det;
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float ndcZ = winZ + winZ - 1.0f;
        final float invW = 1.0f / (im4 * ndcX + im8 * ndcY + im12 * ndcZ + im16);
        return dest.set((im00 * ndcX + im5 * ndcY + im9 * ndcZ + im13) * invW, (im2 * ndcX + im6 * ndcY + im10 * ndcZ + im14) * invW, (im3 * ndcX + im7 * ndcY + im11 * ndcZ + im15) * invW);
    }
    
    public Vector4f unproject(final Vector3fc winCoords, final int[] viewport, final Vector4f dest) {
        return this.unproject(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector3f unproject(final Vector3fc winCoords, final int[] viewport, final Vector3f dest) {
        return this.unproject(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Matrix4f unprojectRay(final float winX, final float winY, final int[] viewport, final Vector3f originDest, final Vector3f dirDest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        final float im00 = (this.m11() * l - this.m12() * k + this.m13() * j) * det;
        final float im2 = (-this.m01() * l + this.m02() * k - this.m03() * j) * det;
        final float im3 = (this.m31() * f - this.m32() * e + this.m33() * d) * det;
        final float im4 = (-this.m21() * f + this.m22() * e - this.m23() * d) * det;
        final float im5 = (-this.m10() * l + this.m12() * i - this.m13() * h) * det;
        final float im6 = (this.m00() * l - this.m02() * i + this.m03() * h) * det;
        final float im7 = (-this.m30() * f + this.m32() * c - this.m33() * b) * det;
        final float im8 = (this.m20() * f - this.m22() * c + this.m23() * b) * det;
        final float im9 = (this.m10() * k - this.m11() * i + this.m13() * g) * det;
        final float im10 = (-this.m00() * k + this.m01() * i - this.m03() * g) * det;
        final float im11 = (this.m30() * e - this.m31() * c + this.m33() * a) * det;
        final float im12 = (-this.m20() * e + this.m21() * c - this.m23() * a) * det;
        final float im13 = (-this.m10() * j + this.m11() * h - this.m12() * g) * det;
        final float im14 = (this.m00() * j - this.m01() * h + this.m02() * g) * det;
        final float im15 = (-this.m30() * d + this.m31() * b - this.m32() * a) * det;
        final float im16 = (this.m20() * d - this.m21() * b + this.m22() * a) * det;
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float px = im00 * ndcX + im5 * ndcY + im13;
        final float py = im2 * ndcX + im6 * ndcY + im14;
        final float pz = im3 * ndcX + im7 * ndcY + im15;
        final float invNearW = 1.0f / (im4 * ndcX + im8 * ndcY - im12 + im16);
        final float nearX = (px - im9) * invNearW;
        final float nearY = (py - im10) * invNearW;
        final float nearZ = (pz - im11) * invNearW;
        final float invW0 = 1.0f / (im4 * ndcX + im8 * ndcY + im16);
        final float x0 = px * invW0;
        final float y0 = py * invW0;
        final float z0 = pz * invW0;
        originDest.x = nearX;
        originDest.y = nearY;
        originDest.z = nearZ;
        dirDest.x = x0 - nearX;
        dirDest.y = y0 - nearY;
        dirDest.z = z0 - nearZ;
        return this;
    }
    
    public Matrix4f unprojectRay(final Vector2fc winCoords, final int[] viewport, final Vector3f originDest, final Vector3f dirDest) {
        return this.unprojectRay(winCoords.x(), winCoords.y(), viewport, originDest, dirDest);
    }
    
    public Vector4f unprojectInv(final Vector3fc winCoords, final int[] viewport, final Vector4f dest) {
        return this.unprojectInv(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector4f unprojectInv(final float winX, final float winY, final float winZ, final int[] viewport, final Vector4f dest) {
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float ndcZ = winZ + winZ - 1.0f;
        final float invW = 1.0f / (this.m03() * ndcX + this.m13() * ndcY + this.m23() * ndcZ + this.m33());
        return dest.set((this.m00() * ndcX + this.m10() * ndcY + this.m20() * ndcZ + this.m30()) * invW, (this.m01() * ndcX + this.m11() * ndcY + this.m21() * ndcZ + this.m31()) * invW, (this.m02() * ndcX + this.m12() * ndcY + this.m22() * ndcZ + this.m32()) * invW, 1.0f);
    }
    
    public Matrix4f unprojectInvRay(final Vector2fc winCoords, final int[] viewport, final Vector3f originDest, final Vector3f dirDest) {
        return this.unprojectInvRay(winCoords.x(), winCoords.y(), viewport, originDest, dirDest);
    }
    
    public Matrix4f unprojectInvRay(final float winX, final float winY, final int[] viewport, final Vector3f originDest, final Vector3f dirDest) {
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float px = this.m00() * ndcX + this.m10() * ndcY + this.m30();
        final float py = this.m01() * ndcX + this.m11() * ndcY + this.m31();
        final float pz = this.m02() * ndcX + this.m12() * ndcY + this.m32();
        final float invNearW = 1.0f / (this.m03() * ndcX + this.m13() * ndcY - this.m23() + this.m33());
        final float nearX = (px - this.m20()) * invNearW;
        final float nearY = (py - this.m21()) * invNearW;
        final float nearZ = (pz - this.m22()) * invNearW;
        final float invW0 = 1.0f / (this.m03() * ndcX + this.m13() * ndcY + this.m33());
        final float x0 = px * invW0;
        final float y0 = py * invW0;
        final float z0 = pz * invW0;
        originDest.x = nearX;
        originDest.y = nearY;
        originDest.z = nearZ;
        dirDest.x = x0 - nearX;
        dirDest.y = y0 - nearY;
        dirDest.z = z0 - nearZ;
        return this;
    }
    
    public Vector3f unprojectInv(final Vector3fc winCoords, final int[] viewport, final Vector3f dest) {
        return this.unprojectInv(winCoords.x(), winCoords.y(), winCoords.z(), viewport, dest);
    }
    
    public Vector3f unprojectInv(final float winX, final float winY, final float winZ, final int[] viewport, final Vector3f dest) {
        final float ndcX = (winX - viewport[0]) / viewport[2] * 2.0f - 1.0f;
        final float ndcY = (winY - viewport[1]) / viewport[3] * 2.0f - 1.0f;
        final float ndcZ = winZ + winZ - 1.0f;
        final float invW = 1.0f / (this.m03() * ndcX + this.m13() * ndcY + this.m23() * ndcZ + this.m33());
        return dest.set((this.m00() * ndcX + this.m10() * ndcY + this.m20() * ndcZ + this.m30()) * invW, (this.m01() * ndcX + this.m11() * ndcY + this.m21() * ndcZ + this.m31()) * invW, (this.m02() * ndcX + this.m12() * ndcY + this.m22() * ndcZ + this.m32()) * invW);
    }
    
    public Vector4f project(final float x, final float y, final float z, final int[] viewport, final Vector4f winCoordsDest) {
        final float invW = 1.0f / Math.fma(this.m03(), x, Math.fma(this.m13(), y, Math.fma(this.m23(), z, this.m33())));
        final float nx = Math.fma(this.m00(), x, Math.fma(this.m10(), y, Math.fma(this.m20(), z, this.m30()))) * invW;
        final float ny = Math.fma(this.m01(), x, Math.fma(this.m11(), y, Math.fma(this.m21(), z, this.m31()))) * invW;
        final float nz = Math.fma(this.m02(), x, Math.fma(this.m12(), y, Math.fma(this.m22(), z, this.m32()))) * invW;
        return winCoordsDest.set(Math.fma(Math.fma(nx, 0.5f, 0.5f), (float)viewport[2], (float)viewport[0]), Math.fma(Math.fma(ny, 0.5f, 0.5f), (float)viewport[3], (float)viewport[1]), Math.fma(0.5f, nz, 0.5f), 1.0f);
    }
    
    public Vector3f project(final float x, final float y, final float z, final int[] viewport, final Vector3f winCoordsDest) {
        final float invW = 1.0f / Math.fma(this.m03(), x, Math.fma(this.m13(), y, Math.fma(this.m23(), z, this.m33())));
        final float nx = Math.fma(this.m00(), x, Math.fma(this.m10(), y, Math.fma(this.m20(), z, this.m30()))) * invW;
        final float ny = Math.fma(this.m01(), x, Math.fma(this.m11(), y, Math.fma(this.m21(), z, this.m31()))) * invW;
        final float nz = Math.fma(this.m02(), x, Math.fma(this.m12(), y, Math.fma(this.m22(), z, this.m32()))) * invW;
        winCoordsDest.x = Math.fma(Math.fma(nx, 0.5f, 0.5f), (float)viewport[2], (float)viewport[0]);
        winCoordsDest.y = Math.fma(Math.fma(ny, 0.5f, 0.5f), (float)viewport[3], (float)viewport[1]);
        winCoordsDest.z = Math.fma(0.5f, nz, 0.5f);
        return winCoordsDest;
    }
    
    public Vector4f project(final Vector3fc position, final int[] viewport, final Vector4f winCoordsDest) {
        return this.project(position.x(), position.y(), position.z(), viewport, winCoordsDest);
    }
    
    public Vector3f project(final Vector3fc position, final int[] viewport, final Vector3f winCoordsDest) {
        return this.project(position.x(), position.y(), position.z(), viewport, winCoordsDest);
    }
    
    public Matrix4f reflect(final float a, final float b, final float c, final float d, final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.reflection(a, b, c, d);
        }
        if ((this.properties & 0x2) != 0x0) {
            return this.reflectAffine(a, b, c, d, dest);
        }
        return this.reflectGeneric(a, b, c, d, dest);
    }
    
    private Matrix4f reflectAffine(final float a, final float b, final float c, final float d, final Matrix4f dest) {
        final float da = a + a;
        final float db = b + b;
        final float dc = c + c;
        final float dd = d + d;
        final float rm00 = 1.0f - da * a;
        final float rm2 = -da * b;
        final float rm3 = -da * c;
        final float rm4 = -db * a;
        final float rm5 = 1.0f - db * b;
        final float rm6 = -db * c;
        final float rm7 = -dc * a;
        final float rm8 = -dc * b;
        final float rm9 = 1.0f - dc * c;
        final float rm10 = -dd * a;
        final float rm11 = -dd * b;
        final float rm12 = -dd * c;
        dest._m30(this.m00() * rm10 + this.m10() * rm11 + this.m20() * rm12 + this.m30())._m31(this.m01() * rm10 + this.m11() * rm11 + this.m21() * rm12 + this.m31())._m32(this.m02() * rm10 + this.m12() * rm11 + this.m22() * rm12 + this.m32())._m33(this.m33());
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm5 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm6 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(0.0f)._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    private Matrix4f reflectGeneric(final float a, final float b, final float c, final float d, final Matrix4f dest) {
        final float da = a + a;
        final float db = b + b;
        final float dc = c + c;
        final float dd = d + d;
        final float rm00 = 1.0f - da * a;
        final float rm2 = -da * b;
        final float rm3 = -da * c;
        final float rm4 = -db * a;
        final float rm5 = 1.0f - db * b;
        final float rm6 = -db * c;
        final float rm7 = -dc * a;
        final float rm8 = -dc * b;
        final float rm9 = 1.0f - dc * c;
        final float rm10 = -dd * a;
        final float rm11 = -dd * b;
        final float rm12 = -dd * c;
        dest._m30(this.m00() * rm10 + this.m10() * rm11 + this.m20() * rm12 + this.m30())._m31(this.m01() * rm10 + this.m11() * rm11 + this.m21() * rm12 + this.m31())._m32(this.m02() * rm10 + this.m12() * rm11 + this.m22() * rm12 + this.m32())._m33(this.m03() * rm10 + this.m13() * rm11 + this.m23() * rm12 + this.m33());
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3;
        final float nm5 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm6 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm7 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        final float nm8 = this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6;
        dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(this.m03() * rm7 + this.m13() * rm8 + this.m23() * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f reflect(final float a, final float b, final float c, final float d) {
        return this.reflect(a, b, c, d, this);
    }
    
    public Matrix4f reflect(final float nx, final float ny, final float nz, final float px, final float py, final float pz) {
        return this.reflect(nx, ny, nz, px, py, pz, this);
    }
    
    public Matrix4f reflect(final float nx, final float ny, final float nz, final float px, final float py, final float pz, final Matrix4f dest) {
        final float invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final float nnx = nx * invLength;
        final float nny = ny * invLength;
        final float nnz = nz * invLength;
        return this.reflect(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz, dest);
    }
    
    public Matrix4f reflect(final Vector3fc normal, final Vector3fc point) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4f reflect(final Quaternionfc orientation, final Vector3fc point) {
        return this.reflect(orientation, point, this);
    }
    
    public Matrix4f reflect(final Quaternionfc orientation, final Vector3fc point, final Matrix4f dest) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final float normalX = (float)(orientation.x() * num3 + orientation.w() * num2);
        final float normalY = (float)(orientation.y() * num3 - orientation.w() * num1);
        final float normalZ = (float)(1.0 - (orientation.x() * num1 + orientation.y() * num2));
        return this.reflect(normalX, normalY, normalZ, point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4f reflect(final Vector3fc normal, final Vector3fc point, final Matrix4f dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4f reflection(final float a, final float b, final float c, final float d) {
        final float da = a + a;
        final float db = b + b;
        final float dc = c + c;
        final float dd = d + d;
        this._m00(1.0f - da * a)._m01(-da * b)._m02(-da * c)._m03(0.0f)._m10(-db * a)._m11(1.0f - db * b)._m12(-db * c)._m13(0.0f)._m20(-dc * a)._m21(-dc * b)._m22(1.0f - dc * c)._m23(0.0f)._m30(-dd * a)._m31(-dd * b)._m32(-dd * c)._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f reflection(final float nx, final float ny, final float nz, final float px, final float py, final float pz) {
        final float invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final float nnx = nx * invLength;
        final float nny = ny * invLength;
        final float nnz = nz * invLength;
        return this.reflection(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz);
    }
    
    public Matrix4f reflection(final Vector3fc normal, final Vector3fc point) {
        return this.reflection(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4f reflection(final Quaternionfc orientation, final Vector3fc point) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final float normalX = (float)(orientation.x() * num3 + orientation.w() * num2);
        final float normalY = (float)(orientation.y() * num3 - orientation.w() * num1);
        final float normalZ = (float)(1.0 - (orientation.x() * num1 + orientation.y() * num2));
        return this.reflection(normalX, normalY, normalZ, point.x(), point.y(), point.z());
    }
    
    public Vector4f getRow(final int row, final Vector4f dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                return dest.set(this.m00(), this.m10(), this.m20(), this.m30());
            }
            case 1: {
                return dest.set(this.m01(), this.m11(), this.m21(), this.m31());
            }
            case 2: {
                return dest.set(this.m02(), this.m12(), this.m22(), this.m32());
            }
            case 3: {
                return dest.set(this.m03(), this.m13(), this.m23(), this.m33());
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Vector3f getRow(final int row, final Vector3f dest) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                return dest.set(this.m00(), this.m10(), this.m20());
            }
            case 1: {
                return dest.set(this.m01(), this.m11(), this.m21());
            }
            case 2: {
                return dest.set(this.m02(), this.m12(), this.m22());
            }
            case 3: {
                return dest.set(this.m03(), this.m13(), this.m23());
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Matrix4f setRow(final int row, final Vector4fc src) throws IndexOutOfBoundsException {
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
    
    public Vector4f getColumn(final int column, final Vector4f dest) throws IndexOutOfBoundsException {
        return MemUtil.INSTANCE.getColumn(this, column, dest);
    }
    
    public Vector3f getColumn(final int column, final Vector3f dest) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                return dest.set(this.m00(), this.m01(), this.m02());
            }
            case 1: {
                return dest.set(this.m10(), this.m11(), this.m12());
            }
            case 2: {
                return dest.set(this.m20(), this.m21(), this.m22());
            }
            case 3: {
                return dest.set(this.m30(), this.m31(), this.m32());
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
    }
    
    public Matrix4f setColumn(final int column, final Vector4fc src) throws IndexOutOfBoundsException {
        if (src instanceof Vector4f) {
            return MemUtil.INSTANCE.setColumn((Vector4f)src, column, this)._properties(0);
        }
        return MemUtil.INSTANCE.setColumn(src, column, this)._properties(0);
    }
    
    public float get(final int column, final int row) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix4f set(final int column, final int row, final float value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public float getRowColumn(final int row, final int column) {
        return MemUtil.INSTANCE.get(this, column, row);
    }
    
    public Matrix4f setRowColumn(final int row, final int column, final float value) {
        return MemUtil.INSTANCE.set(this, column, row, value);
    }
    
    public Matrix4f normal() {
        return this.normal(this);
    }
    
    public Matrix4f normal(final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix4f normalOrthonormal(final Matrix4f dest) {
        if (dest != this) {
            dest.set(this);
        }
        return dest._properties(18);
    }
    
    private Matrix4f normalGeneric(final Matrix4f dest) {
        final float m00m11 = this.m00() * this.m11();
        final float m01m10 = this.m01() * this.m10();
        final float m02m10 = this.m02() * this.m10();
        final float m00m12 = this.m00() * this.m12();
        final float m01m11 = this.m01() * this.m12();
        final float m02m11 = this.m02() * this.m11();
        final float det = (m00m11 - m01m10) * this.m22() + (m02m10 - m00m12) * this.m21() + (m01m11 - m02m11) * this.m20();
        final float s = 1.0f / det;
        final float nm00 = (this.m11() * this.m22() - this.m21() * this.m12()) * s;
        final float nm2 = (this.m20() * this.m12() - this.m10() * this.m22()) * s;
        final float nm3 = (this.m10() * this.m21() - this.m20() * this.m11()) * s;
        final float nm4 = (this.m21() * this.m02() - this.m01() * this.m22()) * s;
        final float nm5 = (this.m00() * this.m22() - this.m20() * this.m02()) * s;
        final float nm6 = (this.m20() * this.m01() - this.m00() * this.m21()) * s;
        final float nm7 = (m01m11 - m02m11) * s;
        final float nm8 = (m02m10 - m00m12) * s;
        final float nm9 = (m00m11 - m01m10) * s;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m20(nm7)._m21(nm8)._m22(nm9)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties((this.properties | 0x2) & 0xFFFFFFF6);
    }
    
    public Matrix3f normal(final Matrix3f dest) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix3f normalOrthonormal(final Matrix3f dest) {
        dest.set(this);
        return dest;
    }
    
    private Matrix3f normalGeneric(final Matrix3f dest) {
        final float det = (this.m00() * this.m11() - this.m01() * this.m10()) * this.m22() + (this.m02() * this.m10() - this.m00() * this.m12()) * this.m21() + (this.m01() * this.m12() - this.m02() * this.m11()) * this.m20();
        final float s = 1.0f / det;
        return dest._m00((this.m11() * this.m22() - this.m21() * this.m12()) * s)._m01((this.m20() * this.m12() - this.m10() * this.m22()) * s)._m02((this.m10() * this.m21() - this.m20() * this.m11()) * s)._m10((this.m21() * this.m02() - this.m01() * this.m22()) * s)._m11((this.m00() * this.m22() - this.m20() * this.m02()) * s)._m12((this.m20() * this.m01() - this.m00() * this.m21()) * s)._m20((this.m01() * this.m12() - this.m02() * this.m11()) * s)._m21((this.m02() * this.m10() - this.m00() * this.m12()) * s)._m22((this.m00() * this.m11() - this.m01() * this.m10()) * s);
    }
    
    public Matrix4f cofactor3x3() {
        return this.cofactor3x3(this);
    }
    
    public Matrix3f cofactor3x3(final Matrix3f dest) {
        return dest._m00(this.m11() * this.m22() - this.m21() * this.m12())._m01(this.m20() * this.m12() - this.m10() * this.m22())._m02(this.m10() * this.m21() - this.m20() * this.m11())._m10(this.m21() * this.m02() - this.m01() * this.m22())._m11(this.m00() * this.m22() - this.m20() * this.m02())._m12(this.m20() * this.m01() - this.m00() * this.m21())._m20(this.m01() * this.m12() - this.m02() * this.m11())._m21(this.m02() * this.m10() - this.m00() * this.m12())._m22(this.m00() * this.m11() - this.m01() * this.m10());
    }
    
    public Matrix4f cofactor3x3(final Matrix4f dest) {
        final float nm10 = this.m21() * this.m02() - this.m01() * this.m22();
        final float nm11 = this.m00() * this.m22() - this.m20() * this.m02();
        final float nm12 = this.m20() * this.m01() - this.m00() * this.m21();
        final float nm13 = this.m01() * this.m12() - this.m11() * this.m02();
        final float nm14 = this.m02() * this.m10() - this.m12() * this.m00();
        final float nm15 = this.m00() * this.m11() - this.m10() * this.m01();
        return dest._m00(this.m11() * this.m22() - this.m21() * this.m12())._m01(this.m20() * this.m12() - this.m10() * this.m22())._m02(this.m10() * this.m21() - this.m20() * this.m11())._m03(0.0f)._m10(nm10)._m11(nm11)._m12(nm12)._m13(0.0f)._m20(nm13)._m21(nm14)._m22(nm15)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f)._properties((this.properties | 0x2) & 0xFFFFFFF6);
    }
    
    public Matrix4f normalize3x3() {
        return this.normalize3x3(this);
    }
    
    public Matrix4f normalize3x3(final Matrix4f dest) {
        final float invXlen = Math.invsqrt(this.m00() * this.m00() + this.m01() * this.m01() + this.m02() * this.m02());
        final float invYlen = Math.invsqrt(this.m10() * this.m10() + this.m11() * this.m11() + this.m12() * this.m12());
        final float invZlen = Math.invsqrt(this.m20() * this.m20() + this.m21() * this.m21() + this.m22() * this.m22());
        return dest._m00(this.m00() * invXlen)._m01(this.m01() * invXlen)._m02(this.m02() * invXlen)._m10(this.m10() * invYlen)._m11(this.m11() * invYlen)._m12(this.m12() * invYlen)._m20(this.m20() * invZlen)._m21(this.m21() * invZlen)._m22(this.m22() * invZlen)._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties);
    }
    
    public Matrix3f normalize3x3(final Matrix3f dest) {
        final float invXlen = Math.invsqrt(this.m00() * this.m00() + this.m01() * this.m01() + this.m02() * this.m02());
        final float invYlen = Math.invsqrt(this.m10() * this.m10() + this.m11() * this.m11() + this.m12() * this.m12());
        final float invZlen = Math.invsqrt(this.m20() * this.m20() + this.m21() * this.m21() + this.m22() * this.m22());
        return dest._m00(this.m00() * invXlen)._m01(this.m01() * invXlen)._m02(this.m02() * invXlen)._m10(this.m10() * invYlen)._m11(this.m11() * invYlen)._m12(this.m12() * invYlen)._m20(this.m20() * invZlen)._m21(this.m21() * invZlen)._m22(this.m22() * invZlen);
    }
    
    public Vector4f frustumPlane(final int plane, final Vector4f dest) {
        switch (plane) {
            case 0: {
                dest.set(this.m03() + this.m00(), this.m13() + this.m10(), this.m23() + this.m20(), this.m33() + this.m30()).normalize3();
                break;
            }
            case 1: {
                dest.set(this.m03() - this.m00(), this.m13() - this.m10(), this.m23() - this.m20(), this.m33() - this.m30()).normalize3();
                break;
            }
            case 2: {
                dest.set(this.m03() + this.m01(), this.m13() + this.m11(), this.m23() + this.m21(), this.m33() + this.m31()).normalize3();
                break;
            }
            case 3: {
                dest.set(this.m03() - this.m01(), this.m13() - this.m11(), this.m23() - this.m21(), this.m33() - this.m31()).normalize3();
                break;
            }
            case 4: {
                dest.set(this.m03() + this.m02(), this.m13() + this.m12(), this.m23() + this.m22(), this.m33() + this.m32()).normalize3();
                break;
            }
            case 5: {
                dest.set(this.m03() - this.m02(), this.m13() - this.m12(), this.m23() - this.m22(), this.m33() - this.m32()).normalize3();
                break;
            }
            default: {
                throw new IllegalArgumentException("dest");
            }
        }
        return dest;
    }
    
    public Vector3f frustumCorner(final int corner, final Vector3f point) {
        float n1x = 0.0f;
        float n1y = 0.0f;
        float n1z = 0.0f;
        float d1 = 0.0f;
        float n2x = 0.0f;
        float n2y = 0.0f;
        float n2z = 0.0f;
        float d2 = 0.0f;
        float n3x = 0.0f;
        float n3y = 0.0f;
        float n3z = 0.0f;
        float d3 = 0.0f;
        switch (corner) {
            case 0: {
                n1x = this.m03() + this.m00();
                n1y = this.m13() + this.m10();
                n1z = this.m23() + this.m20();
                d1 = this.m33() + this.m30();
                n2x = this.m03() + this.m01();
                n2y = this.m13() + this.m11();
                n2z = this.m23() + this.m21();
                d2 = this.m33() + this.m31();
                n3x = this.m03() + this.m02();
                n3y = this.m13() + this.m12();
                n3z = this.m23() + this.m22();
                d3 = this.m33() + this.m32();
                break;
            }
            case 1: {
                n1x = this.m03() - this.m00();
                n1y = this.m13() - this.m10();
                n1z = this.m23() - this.m20();
                d1 = this.m33() - this.m30();
                n2x = this.m03() + this.m01();
                n2y = this.m13() + this.m11();
                n2z = this.m23() + this.m21();
                d2 = this.m33() + this.m31();
                n3x = this.m03() + this.m02();
                n3y = this.m13() + this.m12();
                n3z = this.m23() + this.m22();
                d3 = this.m33() + this.m32();
                break;
            }
            case 2: {
                n1x = this.m03() - this.m00();
                n1y = this.m13() - this.m10();
                n1z = this.m23() - this.m20();
                d1 = this.m33() - this.m30();
                n2x = this.m03() - this.m01();
                n2y = this.m13() - this.m11();
                n2z = this.m23() - this.m21();
                d2 = this.m33() - this.m31();
                n3x = this.m03() + this.m02();
                n3y = this.m13() + this.m12();
                n3z = this.m23() + this.m22();
                d3 = this.m33() + this.m32();
                break;
            }
            case 3: {
                n1x = this.m03() + this.m00();
                n1y = this.m13() + this.m10();
                n1z = this.m23() + this.m20();
                d1 = this.m33() + this.m30();
                n2x = this.m03() - this.m01();
                n2y = this.m13() - this.m11();
                n2z = this.m23() - this.m21();
                d2 = this.m33() - this.m31();
                n3x = this.m03() + this.m02();
                n3y = this.m13() + this.m12();
                n3z = this.m23() + this.m22();
                d3 = this.m33() + this.m32();
                break;
            }
            case 4: {
                n1x = this.m03() - this.m00();
                n1y = this.m13() - this.m10();
                n1z = this.m23() - this.m20();
                d1 = this.m33() - this.m30();
                n2x = this.m03() + this.m01();
                n2y = this.m13() + this.m11();
                n2z = this.m23() + this.m21();
                d2 = this.m33() + this.m31();
                n3x = this.m03() - this.m02();
                n3y = this.m13() - this.m12();
                n3z = this.m23() - this.m22();
                d3 = this.m33() - this.m32();
                break;
            }
            case 5: {
                n1x = this.m03() + this.m00();
                n1y = this.m13() + this.m10();
                n1z = this.m23() + this.m20();
                d1 = this.m33() + this.m30();
                n2x = this.m03() + this.m01();
                n2y = this.m13() + this.m11();
                n2z = this.m23() + this.m21();
                d2 = this.m33() + this.m31();
                n3x = this.m03() - this.m02();
                n3y = this.m13() - this.m12();
                n3z = this.m23() - this.m22();
                d3 = this.m33() - this.m32();
                break;
            }
            case 6: {
                n1x = this.m03() + this.m00();
                n1y = this.m13() + this.m10();
                n1z = this.m23() + this.m20();
                d1 = this.m33() + this.m30();
                n2x = this.m03() - this.m01();
                n2y = this.m13() - this.m11();
                n2z = this.m23() - this.m21();
                d2 = this.m33() - this.m31();
                n3x = this.m03() - this.m02();
                n3y = this.m13() - this.m12();
                n3z = this.m23() - this.m22();
                d3 = this.m33() - this.m32();
                break;
            }
            case 7: {
                n1x = this.m03() - this.m00();
                n1y = this.m13() - this.m10();
                n1z = this.m23() - this.m20();
                d1 = this.m33() - this.m30();
                n2x = this.m03() - this.m01();
                n2y = this.m13() - this.m11();
                n2z = this.m23() - this.m21();
                d2 = this.m33() - this.m31();
                n3x = this.m03() - this.m02();
                n3y = this.m13() - this.m12();
                n3z = this.m23() - this.m22();
                d3 = this.m33() - this.m32();
                break;
            }
            default: {
                throw new IllegalArgumentException("corner");
            }
        }
        final float c23x = n2y * n3z - n2z * n3y;
        final float c23y = n2z * n3x - n2x * n3z;
        final float c23z = n2x * n3y - n2y * n3x;
        final float c31x = n3y * n1z - n3z * n1y;
        final float c31y = n3z * n1x - n3x * n1z;
        final float c31z = n3x * n1y - n3y * n1x;
        final float c12x = n1y * n2z - n1z * n2y;
        final float c12y = n1z * n2x - n1x * n2z;
        final float c12z = n1x * n2y - n1y * n2x;
        final float invDot = 1.0f / (n1x * c23x + n1y * c23y + n1z * c23z);
        point.x = (-c23x * d1 - c31x * d2 - c12x * d3) * invDot;
        point.y = (-c23y * d1 - c31y * d2 - c12y * d3) * invDot;
        point.z = (-c23z * d1 - c31z * d2 - c12z * d3) * invDot;
        return point;
    }
    
    public Vector3f perspectiveOrigin(final Vector3f origin) {
        final float n1x = this.m03() + this.m00();
        final float n1y = this.m13() + this.m10();
        final float n1z = this.m23() + this.m20();
        final float d1 = this.m33() + this.m30();
        final float n2x = this.m03() - this.m00();
        final float n2y = this.m13() - this.m10();
        final float n2z = this.m23() - this.m20();
        final float d2 = this.m33() - this.m30();
        final float n3x = this.m03() - this.m01();
        final float n3y = this.m13() - this.m11();
        final float n3z = this.m23() - this.m21();
        final float d3 = this.m33() - this.m31();
        final float c23x = n2y * n3z - n2z * n3y;
        final float c23y = n2z * n3x - n2x * n3z;
        final float c23z = n2x * n3y - n2y * n3x;
        final float c31x = n3y * n1z - n3z * n1y;
        final float c31y = n3z * n1x - n3x * n1z;
        final float c31z = n3x * n1y - n3y * n1x;
        final float c12x = n1y * n2z - n1z * n2y;
        final float c12y = n1z * n2x - n1x * n2z;
        final float c12z = n1x * n2y - n1y * n2x;
        final float invDot = 1.0f / (n1x * c23x + n1y * c23y + n1z * c23z);
        origin.x = (-c23x * d1 - c31x * d2 - c12x * d3) * invDot;
        origin.y = (-c23y * d1 - c31y * d2 - c12y * d3) * invDot;
        origin.z = (-c23z * d1 - c31z * d2 - c12z * d3) * invDot;
        return origin;
    }
    
    public Vector3f perspectiveInvOrigin(final Vector3f dest) {
        final float invW = 1.0f / this.m23();
        dest.x = this.m20() * invW;
        dest.y = this.m21() * invW;
        dest.z = this.m22() * invW;
        return dest;
    }
    
    public float perspectiveFov() {
        final float n1x = this.m03() + this.m01();
        final float n1y = this.m13() + this.m11();
        final float n1z = this.m23() + this.m21();
        final float n2x = this.m01() - this.m03();
        final float n2y = this.m11() - this.m13();
        final float n2z = this.m21() - this.m23();
        final float n1len = Math.sqrt(n1x * n1x + n1y * n1y + n1z * n1z);
        final float n2len = Math.sqrt(n2x * n2x + n2y * n2y + n2z * n2z);
        return Math.acos((n1x * n2x + n1y * n2y + n1z * n2z) / (n1len * n2len));
    }
    
    public float perspectiveNear() {
        return this.m32() / (this.m23() + this.m22());
    }
    
    public float perspectiveFar() {
        return this.m32() / (this.m22() - this.m23());
    }
    
    public Vector3f frustumRayDir(final float x, final float y, final Vector3f dir) {
        final float a = this.m10() * this.m23();
        final float b = this.m13() * this.m21();
        final float c = this.m10() * this.m21();
        final float d = this.m11() * this.m23();
        final float e = this.m13() * this.m20();
        final float f = this.m11() * this.m20();
        final float g = this.m03() * this.m20();
        final float h = this.m01() * this.m23();
        final float i = this.m01() * this.m20();
        final float j = this.m03() * this.m21();
        final float k = this.m00() * this.m23();
        final float l = this.m00() * this.m21();
        final float m = this.m00() * this.m13();
        final float n = this.m03() * this.m11();
        final float o = this.m00() * this.m11();
        final float p = this.m01() * this.m13();
        final float q = this.m03() * this.m10();
        final float r = this.m01() * this.m10();
        final float m1x = (d + e + f - a - b - c) * (1.0f - y) + (a - b - c + d - e + f) * y;
        final float m1y = (j + k + l - g - h - i) * (1.0f - y) + (g - h - i + j - k + l) * y;
        final float m1z = (p + q + r - m - n - o) * (1.0f - y) + (m - n - o + p - q + r) * y;
        final float m2x = (b - c - d + e + f - a) * (1.0f - y) + (a + b - c - d - e + f) * y;
        final float m2y = (h - i - j + k + l - g) * (1.0f - y) + (g + h - i - j - k + l) * y;
        final float m2z = (n - o - p + q + r - m) * (1.0f - y) + (m + n - o - p - q + r) * y;
        dir.x = m1x + (m2x - m1x) * x;
        dir.y = m1y + (m2y - m1y) * x;
        dir.z = m1z + (m2z - m1z) * x;
        return dir.normalize(dir);
    }
    
    public Vector3f positiveZ(final Vector3f dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveZ(dir);
        }
        return this.positiveZGeneric(dir);
    }
    
    private Vector3f positiveZGeneric(final Vector3f dir) {
        return dir.set(this.m10() * this.m21() - this.m11() * this.m20(), this.m20() * this.m01() - this.m21() * this.m00(), this.m00() * this.m11() - this.m01() * this.m10()).normalize();
    }
    
    public Vector3f normalizedPositiveZ(final Vector3f dir) {
        return dir.set(this.m02(), this.m12(), this.m22());
    }
    
    public Vector3f positiveX(final Vector3f dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveX(dir);
        }
        return this.positiveXGeneric(dir);
    }
    
    private Vector3f positiveXGeneric(final Vector3f dir) {
        return dir.set(this.m11() * this.m22() - this.m12() * this.m21(), this.m02() * this.m21() - this.m01() * this.m22(), this.m01() * this.m12() - this.m02() * this.m11()).normalize();
    }
    
    public Vector3f normalizedPositiveX(final Vector3f dir) {
        return dir.set(this.m00(), this.m10(), this.m20());
    }
    
    public Vector3f positiveY(final Vector3f dir) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalizedPositiveY(dir);
        }
        return this.positiveYGeneric(dir);
    }
    
    private Vector3f positiveYGeneric(final Vector3f dir) {
        return dir.set(this.m12() * this.m20() - this.m10() * this.m22(), this.m00() * this.m22() - this.m02() * this.m20(), this.m02() * this.m10() - this.m00() * this.m12()).normalize();
    }
    
    public Vector3f normalizedPositiveY(final Vector3f dir) {
        return dir.set(this.m01(), this.m11(), this.m21());
    }
    
    public Vector3f originAffine(final Vector3f origin) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        return origin.set(-this.m10() * j + this.m11() * h - this.m12() * g, this.m00() * j - this.m01() * h + this.m02() * g, -this.m30() * d + this.m31() * b - this.m32() * a);
    }
    
    public Vector3f origin(final Vector3f dest) {
        if ((this.properties & 0x2) != 0x0) {
            return this.originAffine(dest);
        }
        return this.originGeneric(dest);
    }
    
    private Vector3f originGeneric(final Vector3f dest) {
        final float a = this.m00() * this.m11() - this.m01() * this.m10();
        final float b = this.m00() * this.m12() - this.m02() * this.m10();
        final float c = this.m00() * this.m13() - this.m03() * this.m10();
        final float d = this.m01() * this.m12() - this.m02() * this.m11();
        final float e = this.m01() * this.m13() - this.m03() * this.m11();
        final float f = this.m02() * this.m13() - this.m03() * this.m12();
        final float g = this.m20() * this.m31() - this.m21() * this.m30();
        final float h = this.m20() * this.m32() - this.m22() * this.m30();
        final float i = this.m20() * this.m33() - this.m23() * this.m30();
        final float j = this.m21() * this.m32() - this.m22() * this.m31();
        final float k = this.m21() * this.m33() - this.m23() * this.m31();
        final float l = this.m22() * this.m33() - this.m23() * this.m32();
        final float det = a * l - b * k + c * j + d * i - e * h + f * g;
        final float invDet = 1.0f / det;
        final float nm30 = (-this.m10() * j + this.m11() * h - this.m12() * g) * invDet;
        final float nm31 = (this.m00() * j - this.m01() * h + this.m02() * g) * invDet;
        final float nm32 = (-this.m30() * d + this.m31() * b - this.m32() * a) * invDet;
        final float nm33 = det / (this.m20() * d - this.m21() * b + this.m22() * a);
        return dest.set(nm30 * nm33, nm31 * nm33, nm32 * nm33);
    }
    
    public Matrix4f shadow(final Vector4f light, final float a, final float b, final float c, final float d) {
        return this.shadow(light.x, light.y, light.z, light.w, a, b, c, d, this);
    }
    
    public Matrix4f shadow(final Vector4f light, final float a, final float b, final float c, final float d, final Matrix4f dest) {
        return this.shadow(light.x, light.y, light.z, light.w, a, b, c, d, dest);
    }
    
    public Matrix4f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final float a, final float b, final float c, final float d) {
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, this);
    }
    
    public Matrix4f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final float a, final float b, final float c, final float d, final Matrix4f dest) {
        final float invPlaneLen = Math.invsqrt(a * a + b * b + c * c);
        final float an = a * invPlaneLen;
        final float bn = b * invPlaneLen;
        final float cn = c * invPlaneLen;
        final float dn = d * invPlaneLen;
        final float dot = an * lightX + bn * lightY + cn * lightZ + dn * lightW;
        final float rm00 = dot - an * lightX;
        final float rm2 = -an * lightY;
        final float rm3 = -an * lightZ;
        final float rm4 = -an * lightW;
        final float rm5 = -bn * lightX;
        final float rm6 = dot - bn * lightY;
        final float rm7 = -bn * lightZ;
        final float rm8 = -bn * lightW;
        final float rm9 = -cn * lightX;
        final float rm10 = -cn * lightY;
        final float rm11 = dot - cn * lightZ;
        final float rm12 = -cn * lightW;
        final float rm13 = -dn * lightX;
        final float rm14 = -dn * lightY;
        final float rm15 = -dn * lightZ;
        final float rm16 = dot - dn * lightW;
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3 + this.m30() * rm4;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3 + this.m31() * rm4;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3 + this.m32() * rm4;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3 + this.m33() * rm4;
        final float nm5 = this.m00() * rm5 + this.m10() * rm6 + this.m20() * rm7 + this.m30() * rm8;
        final float nm6 = this.m01() * rm5 + this.m11() * rm6 + this.m21() * rm7 + this.m31() * rm8;
        final float nm7 = this.m02() * rm5 + this.m12() * rm6 + this.m22() * rm7 + this.m32() * rm8;
        final float nm8 = this.m03() * rm5 + this.m13() * rm6 + this.m23() * rm7 + this.m33() * rm8;
        final float nm9 = this.m00() * rm9 + this.m10() * rm10 + this.m20() * rm11 + this.m30() * rm12;
        final float nm10 = this.m01() * rm9 + this.m11() * rm10 + this.m21() * rm11 + this.m31() * rm12;
        final float nm11 = this.m02() * rm9 + this.m12() * rm10 + this.m22() * rm11 + this.m32() * rm12;
        final float nm12 = this.m03() * rm9 + this.m13() * rm10 + this.m23() * rm11 + this.m33() * rm12;
        dest._m30(this.m00() * rm13 + this.m10() * rm14 + this.m20() * rm15 + this.m30() * rm16)._m31(this.m01() * rm13 + this.m11() * rm14 + this.m21() * rm15 + this.m31() * rm16)._m32(this.m02() * rm13 + this.m12() * rm14 + this.m22() * rm15 + this.m32() * rm16)._m33(this.m03() * rm13 + this.m13() * rm14 + this.m23() * rm15 + this.m33() * rm16)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._m20(nm9)._m21(nm10)._m22(nm11)._m23(nm12)._properties(this.properties & 0xFFFFFFE2);
        return dest;
    }
    
    public Matrix4f shadow(final Vector4f light, final Matrix4fc planeTransform, final Matrix4f dest) {
        final float a = planeTransform.m10();
        final float b = planeTransform.m11();
        final float c = planeTransform.m12();
        final float d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(light.x, light.y, light.z, light.w, a, b, c, d, dest);
    }
    
    public Matrix4f shadow(final Vector4f light, final Matrix4f planeTransform) {
        return this.shadow(light, planeTransform, this);
    }
    
    public Matrix4f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final Matrix4fc planeTransform, final Matrix4f dest) {
        final float a = planeTransform.m10();
        final float b = planeTransform.m11();
        final float c = planeTransform.m12();
        final float d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, dest);
    }
    
    public Matrix4f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final Matrix4f planeTransform) {
        return this.shadow(lightX, lightY, lightZ, lightW, planeTransform, this);
    }
    
    public Matrix4f billboardCylindrical(final Vector3fc objPos, final Vector3fc targetPos, final Vector3fc up) {
        float dirX = targetPos.x() - objPos.x();
        float dirY = targetPos.y() - objPos.y();
        float dirZ = targetPos.z() - objPos.z();
        float leftX = up.y() * dirZ - up.z() * dirY;
        float leftY = up.z() * dirX - up.x() * dirZ;
        float leftZ = up.x() * dirY - up.y() * dirX;
        final float invLeftLen = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLen;
        leftY *= invLeftLen;
        leftZ *= invLeftLen;
        dirX = leftY * up.z() - leftZ * up.y();
        dirY = leftZ * up.x() - leftX * up.z();
        dirZ = leftX * up.y() - leftY * up.x();
        final float invDirLen = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLen;
        dirY *= invDirLen;
        dirZ *= invDirLen;
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m03(0.0f)._m10(up.x())._m11(up.y())._m12(up.z())._m13(0.0f)._m20(dirX)._m21(dirY)._m22(dirZ)._m23(0.0f)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f billboardSpherical(final Vector3fc objPos, final Vector3fc targetPos, final Vector3fc up) {
        float dirX = targetPos.x() - objPos.x();
        float dirY = targetPos.y() - objPos.y();
        float dirZ = targetPos.z() - objPos.z();
        final float invDirLen = Math.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX *= invDirLen;
        dirY *= invDirLen;
        dirZ *= invDirLen;
        float leftX = up.y() * dirZ - up.z() * dirY;
        float leftY = up.z() * dirX - up.x() * dirZ;
        float leftZ = up.x() * dirY - up.y() * dirX;
        final float invLeftLen = Math.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ);
        leftX *= invLeftLen;
        leftY *= invLeftLen;
        leftZ *= invLeftLen;
        final float upX = dirY * leftZ - dirZ * leftY;
        final float upY = dirZ * leftX - dirX * leftZ;
        final float upZ = dirX * leftY - dirY * leftX;
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m03(0.0f)._m10(upX)._m11(upY)._m12(upZ)._m13(0.0f)._m20(dirX)._m21(dirY)._m22(dirZ)._m23(0.0f)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0f)._properties(18);
        return this;
    }
    
    public Matrix4f billboardSpherical(final Vector3fc objPos, final Vector3fc targetPos) {
        final float toDirX = targetPos.x() - objPos.x();
        final float toDirY = targetPos.y() - objPos.y();
        final float toDirZ = targetPos.z() - objPos.z();
        float x = -toDirY;
        float y = toDirX;
        float w = Math.sqrt(toDirX * toDirX + toDirY * toDirY + toDirZ * toDirZ) + toDirZ;
        final float invNorm = Math.invsqrt(x * x + y * y + w * w);
        x *= invNorm;
        y *= invNorm;
        w *= invNorm;
        final float q00 = (x + x) * x;
        final float q2 = (y + y) * y;
        final float q3 = (x + x) * y;
        final float q4 = (x + x) * w;
        final float q5 = (y + y) * w;
        this._m00(1.0f - q2)._m01(q3)._m02(-q5)._m03(0.0f)._m10(q3)._m11(1.0f - q00)._m12(q4)._m13(0.0f)._m20(q5)._m21(-q4)._m22(1.0f - q2 - q00)._m23(0.0f)._m30(objPos.x())._m31(objPos.y())._m32(objPos.z())._m33(1.0f)._properties(18);
        return this;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.m00());
        result = 31 * result + Float.floatToIntBits(this.m01());
        result = 31 * result + Float.floatToIntBits(this.m02());
        result = 31 * result + Float.floatToIntBits(this.m03());
        result = 31 * result + Float.floatToIntBits(this.m10());
        result = 31 * result + Float.floatToIntBits(this.m11());
        result = 31 * result + Float.floatToIntBits(this.m12());
        result = 31 * result + Float.floatToIntBits(this.m13());
        result = 31 * result + Float.floatToIntBits(this.m20());
        result = 31 * result + Float.floatToIntBits(this.m21());
        result = 31 * result + Float.floatToIntBits(this.m22());
        result = 31 * result + Float.floatToIntBits(this.m23());
        result = 31 * result + Float.floatToIntBits(this.m30());
        result = 31 * result + Float.floatToIntBits(this.m31());
        result = 31 * result + Float.floatToIntBits(this.m32());
        result = 31 * result + Float.floatToIntBits(this.m33());
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Matrix4f)) {
            return false;
        }
        final Matrix4fc other = (Matrix4fc)obj;
        return Float.floatToIntBits(this.m00()) == Float.floatToIntBits(other.m00()) && Float.floatToIntBits(this.m01()) == Float.floatToIntBits(other.m01()) && Float.floatToIntBits(this.m02()) == Float.floatToIntBits(other.m02()) && Float.floatToIntBits(this.m03()) == Float.floatToIntBits(other.m03()) && Float.floatToIntBits(this.m10()) == Float.floatToIntBits(other.m10()) && Float.floatToIntBits(this.m11()) == Float.floatToIntBits(other.m11()) && Float.floatToIntBits(this.m12()) == Float.floatToIntBits(other.m12()) && Float.floatToIntBits(this.m13()) == Float.floatToIntBits(other.m13()) && Float.floatToIntBits(this.m20()) == Float.floatToIntBits(other.m20()) && Float.floatToIntBits(this.m21()) == Float.floatToIntBits(other.m21()) && Float.floatToIntBits(this.m22()) == Float.floatToIntBits(other.m22()) && Float.floatToIntBits(this.m23()) == Float.floatToIntBits(other.m23()) && Float.floatToIntBits(this.m30()) == Float.floatToIntBits(other.m30()) && Float.floatToIntBits(this.m31()) == Float.floatToIntBits(other.m31()) && Float.floatToIntBits(this.m32()) == Float.floatToIntBits(other.m32()) && Float.floatToIntBits(this.m33()) == Float.floatToIntBits(other.m33());
    }
    
    public boolean equals(final Matrix4fc m, final float delta) {
        return this == m || (m != null && m instanceof Matrix4f && Runtime.equals(this.m00(), m.m00(), delta) && Runtime.equals(this.m01(), m.m01(), delta) && Runtime.equals(this.m02(), m.m02(), delta) && Runtime.equals(this.m03(), m.m03(), delta) && Runtime.equals(this.m10(), m.m10(), delta) && Runtime.equals(this.m11(), m.m11(), delta) && Runtime.equals(this.m12(), m.m12(), delta) && Runtime.equals(this.m13(), m.m13(), delta) && Runtime.equals(this.m20(), m.m20(), delta) && Runtime.equals(this.m21(), m.m21(), delta) && Runtime.equals(this.m22(), m.m22(), delta) && Runtime.equals(this.m23(), m.m23(), delta) && Runtime.equals(this.m30(), m.m30(), delta) && Runtime.equals(this.m31(), m.m31(), delta) && Runtime.equals(this.m32(), m.m32(), delta) && Runtime.equals(this.m33(), m.m33(), delta));
    }
    
    public Matrix4f pick(final float x, final float y, final float width, final float height, final int[] viewport, final Matrix4f dest) {
        final float sx = viewport[2] / width;
        final float sy = viewport[3] / height;
        final float tx = (viewport[2] + 2.0f * (viewport[0] - x)) / width;
        final float ty = (viewport[3] + 2.0f * (viewport[1] - y)) / height;
        dest._m30(this.m00() * tx + this.m10() * ty + this.m30())._m31(this.m01() * tx + this.m11() * ty + this.m31())._m32(this.m02() * tx + this.m12() * ty + this.m32())._m33(this.m03() * tx + this.m13() * ty + this.m33())._m00(this.m00() * sx)._m01(this.m01() * sx)._m02(this.m02() * sx)._m03(this.m03() * sx)._m10(this.m10() * sy)._m11(this.m11() * sy)._m12(this.m12() * sy)._m13(this.m13() * sy)._properties(0);
        return dest;
    }
    
    public Matrix4f pick(final float x, final float y, final float width, final float height, final int[] viewport) {
        return this.pick(x, y, width, height, viewport, this);
    }
    
    public boolean isAffine() {
        return this.m03() == 0.0f && this.m13() == 0.0f && this.m23() == 0.0f && this.m33() == 1.0f;
    }
    
    public Matrix4f swap(final Matrix4f other) {
        MemUtil.INSTANCE.swap(this, other);
        final int props = this.properties;
        this.properties = other.properties();
        other.properties = props;
        return this;
    }
    
    public Matrix4f arcball(final float radius, final float centerX, final float centerY, final float centerZ, final float angleX, final float angleY, final Matrix4f dest) {
        final float m30 = this.m20() * -radius + this.m30();
        final float m31 = this.m21() * -radius + this.m31();
        final float m32 = this.m22() * -radius + this.m32();
        final float m33 = this.m23() * -radius + this.m33();
        float sin = Math.sin(angleX);
        float cos = Math.cosFromSin(sin, angleX);
        final float nm10 = this.m10() * cos + this.m20() * sin;
        final float nm11 = this.m11() * cos + this.m21() * sin;
        final float nm12 = this.m12() * cos + this.m22() * sin;
        final float nm13 = this.m13() * cos + this.m23() * sin;
        final float m34 = this.m20() * cos - this.m10() * sin;
        final float m35 = this.m21() * cos - this.m11() * sin;
        final float m36 = this.m22() * cos - this.m12() * sin;
        final float m37 = this.m23() * cos - this.m13() * sin;
        sin = Math.sin(angleY);
        cos = Math.cosFromSin(sin, angleY);
        final float nm14 = this.m00() * cos - m34 * sin;
        final float nm15 = this.m01() * cos - m35 * sin;
        final float nm16 = this.m02() * cos - m36 * sin;
        final float nm17 = this.m03() * cos - m37 * sin;
        final float nm18 = this.m00() * sin + m34 * cos;
        final float nm19 = this.m01() * sin + m35 * cos;
        final float nm20 = this.m02() * sin + m36 * cos;
        final float nm21 = this.m03() * sin + m37 * cos;
        dest._m30(-nm14 * centerX - nm10 * centerY - nm18 * centerZ + m30)._m31(-nm15 * centerX - nm11 * centerY - nm19 * centerZ + m31)._m32(-nm16 * centerX - nm12 * centerY - nm20 * centerZ + m32)._m33(-nm17 * centerX - nm13 * centerY - nm21 * centerZ + m33)._m20(nm18)._m21(nm19)._m22(nm20)._m23(nm21)._m10(nm10)._m11(nm11)._m12(nm12)._m13(nm13)._m00(nm14)._m01(nm15)._m02(nm16)._m03(nm17)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f arcball(final float radius, final Vector3fc center, final float angleX, final float angleY, final Matrix4f dest) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, dest);
    }
    
    public Matrix4f arcball(final float radius, final float centerX, final float centerY, final float centerZ, final float angleX, final float angleY) {
        return this.arcball(radius, centerX, centerY, centerZ, angleX, angleY, this);
    }
    
    public Matrix4f arcball(final float radius, final Vector3fc center, final float angleX, final float angleY) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, this);
    }
    
    public Matrix4f frustumAabb(final Vector3f min, final Vector3f max) {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;
        for (int t = 0; t < 8; ++t) {
            final float x = ((t & 0x1) << 1) - 1.0f;
            final float y = ((t >>> 1 & 0x1) << 1) - 1.0f;
            final float z = ((t >>> 2 & 0x1) << 1) - 1.0f;
            final float invW = 1.0f / (this.m03() * x + this.m13() * y + this.m23() * z + this.m33());
            final float nx = (this.m00() * x + this.m10() * y + this.m20() * z + this.m30()) * invW;
            final float ny = (this.m01() * x + this.m11() * y + this.m21() * z + this.m31()) * invW;
            final float nz = (this.m02() * x + this.m12() * y + this.m22() * z + this.m32()) * invW;
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
    
    public Matrix4f projectedGridRange(final Matrix4fc projector, final float sLower, final float sUpper, final Matrix4f dest) {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        boolean intersection = false;
        for (int t = 0; t < 12; ++t) {
            float c0X;
            float c1X;
            float c0Y;
            float c1Y;
            float c0Z;
            float c1Z;
            if (t < 4) {
                c0X = -1.0f;
                c1X = 1.0f;
                c1Y = (c0Y = ((t & 0x1) << 1) - 1.0f);
                c1Z = (c0Z = ((t >>> 1 & 0x1) << 1) - 1.0f);
            }
            else if (t < 8) {
                c0Y = -1.0f;
                c1Y = 1.0f;
                c1X = (c0X = ((t & 0x1) << 1) - 1.0f);
                c1Z = (c0Z = ((t >>> 1 & 0x1) << 1) - 1.0f);
            }
            else {
                c0Z = -1.0f;
                c1Z = 1.0f;
                c1X = (c0X = ((t & 0x1) << 1) - 1.0f);
                c1Y = (c0Y = ((t >>> 1 & 0x1) << 1) - 1.0f);
            }
            float invW = 1.0f / (this.m03() * c0X + this.m13() * c0Y + this.m23() * c0Z + this.m33());
            final float p0x = (this.m00() * c0X + this.m10() * c0Y + this.m20() * c0Z + this.m30()) * invW;
            final float p0y = (this.m01() * c0X + this.m11() * c0Y + this.m21() * c0Z + this.m31()) * invW;
            final float p0z = (this.m02() * c0X + this.m12() * c0Y + this.m22() * c0Z + this.m32()) * invW;
            invW = 1.0f / (this.m03() * c1X + this.m13() * c1Y + this.m23() * c1Z + this.m33());
            final float p1x = (this.m00() * c1X + this.m10() * c1Y + this.m20() * c1Z + this.m30()) * invW;
            final float p1y = (this.m01() * c1X + this.m11() * c1Y + this.m21() * c1Z + this.m31()) * invW;
            final float p1z = (this.m02() * c1X + this.m12() * c1Y + this.m22() * c1Z + this.m32()) * invW;
            final float dirX = p1x - p0x;
            final float dirY = p1y - p0y;
            final float dirZ = p1z - p0z;
            final float invDenom = 1.0f / dirY;
            for (int s = 0; s < 2; ++s) {
                final float isectT = -(p0y + ((s == 0) ? sLower : sUpper)) * invDenom;
                if (isectT >= 0.0f && isectT <= 1.0f) {
                    intersection = true;
                    final float ix = p0x + isectT * dirX;
                    final float iz = p0z + isectT * dirZ;
                    invW = 1.0f / (projector.m03() * ix + projector.m23() * iz + projector.m33());
                    final float px = (projector.m00() * ix + projector.m20() * iz + projector.m30()) * invW;
                    final float py = (projector.m01() * ix + projector.m21() * iz + projector.m31()) * invW;
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
        dest.set(maxX - minX, 0.0f, 0.0f, 0.0f, 0.0f, maxY - minY, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, minX, minY, 0.0f, 1.0f);
        dest._properties(2);
        return dest;
    }
    
    public Matrix4f perspectiveFrustumSlice(final float near, final float far, final Matrix4f dest) {
        final float invOldNear = (this.m23() + this.m22()) / this.m32();
        final float invNearFar = 1.0f / (near - far);
        dest._m00(this.m00() * invOldNear * near)._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(this.m10())._m11(this.m11() * invOldNear * near)._m12(this.m12())._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22((far + near) * invNearFar)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32((far + far) * near * invNearFar)._m33(this.m33())._properties(this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4f orthoCrop(final Matrix4fc view, final Matrix4f dest) {
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;
        for (int t = 0; t < 8; ++t) {
            final float x = ((t & 0x1) << 1) - 1.0f;
            final float y = ((t >>> 1 & 0x1) << 1) - 1.0f;
            final float z = ((t >>> 2 & 0x1) << 1) - 1.0f;
            float invW = 1.0f / (this.m03() * x + this.m13() * y + this.m23() * z + this.m33());
            final float wx = (this.m00() * x + this.m10() * y + this.m20() * z + this.m30()) * invW;
            final float wy = (this.m01() * x + this.m11() * y + this.m21() * z + this.m31()) * invW;
            final float wz = (this.m02() * x + this.m12() * y + this.m22() * z + this.m32()) * invW;
            invW = 1.0f / (view.m03() * wx + view.m13() * wy + view.m23() * wz + view.m33());
            final float vx = view.m00() * wx + view.m10() * wy + view.m20() * wz + view.m30();
            final float vy = view.m01() * wx + view.m11() * wy + view.m21() * wz + view.m31();
            final float vz = (view.m02() * wx + view.m12() * wy + view.m22() * wz + view.m32()) * invW;
            minX = ((minX < vx) ? minX : vx);
            maxX = ((maxX > vx) ? maxX : vx);
            minY = ((minY < vy) ? minY : vy);
            maxY = ((maxY > vy) ? maxY : vy);
            minZ = ((minZ < vz) ? minZ : vz);
            maxZ = ((maxZ > vz) ? maxZ : vz);
        }
        return dest.setOrtho(minX, maxX, minY, maxY, -maxZ, -minZ);
    }
    
    public Matrix4f trapezoidCrop(final float p0x, final float p0y, final float p1x, final float p1y, final float p2x, final float p2y, final float p3x, final float p3y) {
        final float aX = p1y - p0y;
        float nm00;
        final float aY = nm00 = p0x - p1x;
        float nm2 = -aX;
        float nm3 = aX * p0y - aY * p0x;
        float nm4 = aX;
        float nm5 = aY;
        float nm6 = -(aX * p0x + aY * p0y);
        final float c3x = nm00 * p3x + nm2 * p3y + nm3;
        final float c3y = nm4 * p3x + nm5 * p3y + nm6;
        final float s = -c3x / c3y;
        nm00 += s * nm4;
        nm2 += s * nm5;
        nm3 += s * nm6;
        final float d1x = nm00 * p1x + nm2 * p1y + nm3;
        final float d2x = nm00 * p2x + nm2 * p2y + nm3;
        final float d = d1x * c3y / (d2x - d1x);
        nm6 += d;
        final float sx = 2.0f / d2x;
        final float sy = 1.0f / (c3y + d);
        final float u = (sy + sy) * d / (1.0f - sy * d);
        final float m03 = nm4 * sy;
        final float m4 = nm5 * sy;
        final float m5 = nm6 * sy;
        nm4 = (u + 1.0f) * m03;
        nm5 = (u + 1.0f) * m4;
        nm6 = (u + 1.0f) * m5 - u;
        nm00 = sx * nm00 - m03;
        nm2 = sx * nm2 - m4;
        nm3 = sx * nm3 - m5;
        this.set(nm00, nm4, 0.0f, m03, nm2, nm5, 0.0f, m4, 0.0f, 0.0f, 1.0f, 0.0f, nm3, nm6, 0.0f, m5);
        this._properties(0);
        return this;
    }
    
    public Matrix4f transformAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final Vector3f outMin, final Vector3f outMax) {
        final float xax = this.m00() * minX;
        final float xay = this.m01() * minX;
        final float xaz = this.m02() * minX;
        final float xbx = this.m00() * maxX;
        final float xby = this.m01() * maxX;
        final float xbz = this.m02() * maxX;
        final float yax = this.m10() * minY;
        final float yay = this.m11() * minY;
        final float yaz = this.m12() * minY;
        final float ybx = this.m10() * maxY;
        final float yby = this.m11() * maxY;
        final float ybz = this.m12() * maxY;
        final float zax = this.m20() * minZ;
        final float zay = this.m21() * minZ;
        final float zaz = this.m22() * minZ;
        final float zbx = this.m20() * maxZ;
        final float zby = this.m21() * maxZ;
        final float zbz = this.m22() * maxZ;
        float xminx;
        float xmaxx;
        if (xax < xbx) {
            xminx = xax;
            xmaxx = xbx;
        }
        else {
            xminx = xbx;
            xmaxx = xax;
        }
        float xminy;
        float xmaxy;
        if (xay < xby) {
            xminy = xay;
            xmaxy = xby;
        }
        else {
            xminy = xby;
            xmaxy = xay;
        }
        float xminz;
        float xmaxz;
        if (xaz < xbz) {
            xminz = xaz;
            xmaxz = xbz;
        }
        else {
            xminz = xbz;
            xmaxz = xaz;
        }
        float yminx;
        float ymaxx;
        if (yax < ybx) {
            yminx = yax;
            ymaxx = ybx;
        }
        else {
            yminx = ybx;
            ymaxx = yax;
        }
        float yminy;
        float ymaxy;
        if (yay < yby) {
            yminy = yay;
            ymaxy = yby;
        }
        else {
            yminy = yby;
            ymaxy = yay;
        }
        float yminz;
        float ymaxz;
        if (yaz < ybz) {
            yminz = yaz;
            ymaxz = ybz;
        }
        else {
            yminz = ybz;
            ymaxz = yaz;
        }
        float zminx;
        float zmaxx;
        if (zax < zbx) {
            zminx = zax;
            zmaxx = zbx;
        }
        else {
            zminx = zbx;
            zmaxx = zax;
        }
        float zminy;
        float zmaxy;
        if (zay < zby) {
            zminy = zay;
            zmaxy = zby;
        }
        else {
            zminy = zby;
            zmaxy = zay;
        }
        float zminz;
        float zmaxz;
        if (zaz < zbz) {
            zminz = zaz;
            zmaxz = zbz;
        }
        else {
            zminz = zbz;
            zmaxz = zaz;
        }
        outMin.x = xminx + yminx + zminx + this.m30();
        outMin.y = xminy + yminy + zminy + this.m31();
        outMin.z = xminz + yminz + zminz + this.m32();
        outMax.x = xmaxx + ymaxx + zmaxx + this.m30();
        outMax.y = xmaxy + ymaxy + zmaxy + this.m31();
        outMax.z = xmaxz + ymaxz + zmaxz + this.m32();
        return this;
    }
    
    public Matrix4f transformAab(final Vector3fc min, final Vector3fc max, final Vector3f outMin, final Vector3f outMax) {
        return this.transformAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), outMin, outMax);
    }
    
    public Matrix4f lerp(final Matrix4fc other, final float t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix4f lerp(final Matrix4fc other, final float t, final Matrix4f dest) {
        dest._m00(Math.fma(other.m00() - this.m00(), t, this.m00()))._m01(Math.fma(other.m01() - this.m01(), t, this.m01()))._m02(Math.fma(other.m02() - this.m02(), t, this.m02()))._m03(Math.fma(other.m03() - this.m03(), t, this.m03()))._m10(Math.fma(other.m10() - this.m10(), t, this.m10()))._m11(Math.fma(other.m11() - this.m11(), t, this.m11()))._m12(Math.fma(other.m12() - this.m12(), t, this.m12()))._m13(Math.fma(other.m13() - this.m13(), t, this.m13()))._m20(Math.fma(other.m20() - this.m20(), t, this.m20()))._m21(Math.fma(other.m21() - this.m21(), t, this.m21()))._m22(Math.fma(other.m22() - this.m22(), t, this.m22()))._m23(Math.fma(other.m23() - this.m23(), t, this.m23()))._m30(Math.fma(other.m30() - this.m30(), t, this.m30()))._m31(Math.fma(other.m31() - this.m31(), t, this.m31()))._m32(Math.fma(other.m32() - this.m32(), t, this.m32()))._m33(Math.fma(other.m33() - this.m33(), t, this.m33()))._properties(this.properties & other.properties());
        return dest;
    }
    
    public Matrix4f rotateTowards(final Vector3fc dir, final Vector3fc up, final Matrix4f dest) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4f rotateTowards(final Vector3fc dir, final Vector3fc up) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ, final Matrix4f dest) {
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
        dest._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33());
        final float nm00 = this.m00() * rm00 + this.m10() * rm2 + this.m20() * rm3;
        final float nm2 = this.m01() * rm00 + this.m11() * rm2 + this.m21() * rm3;
        final float nm3 = this.m02() * rm00 + this.m12() * rm2 + this.m22() * rm3;
        final float nm4 = this.m03() * rm00 + this.m13() * rm2 + this.m23() * rm3;
        final float nm5 = this.m00() * rm4 + this.m10() * rm5 + this.m20() * rm6;
        final float nm6 = this.m01() * rm4 + this.m11() * rm5 + this.m21() * rm6;
        final float nm7 = this.m02() * rm4 + this.m12() * rm5 + this.m22() * rm6;
        final float nm8 = this.m03() * rm4 + this.m13() * rm5 + this.m23() * rm6;
        dest._m20(this.m00() * rm7 + this.m10() * rm8 + this.m20() * rm9)._m21(this.m01() * rm7 + this.m11() * rm8 + this.m21() * rm9)._m22(this.m02() * rm7 + this.m12() * rm8 + this.m22() * rm9)._m23(this.m03() * rm7 + this.m13() * rm8 + this.m23() * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m03(nm4)._m10(nm5)._m11(nm6)._m12(nm7)._m13(nm8)._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f rotationTowards(final Vector3fc dir, final Vector3fc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4f rotationTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
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
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m10(upnX)._m11(upnY)._m12(upnZ)._m20(ndirX)._m21(ndirY)._m22(ndirZ)._properties(18);
        return this;
    }
    
    public Matrix4f translationRotateTowards(final Vector3fc pos, final Vector3fc dir, final Vector3fc up) {
        return this.translationRotateTowards(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4f translationRotateTowards(final float posX, final float posY, final float posZ, final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
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
        this._m00(leftX)._m01(leftY)._m02(leftZ)._m03(0.0f)._m10(upnX)._m11(upnY)._m12(upnZ)._m13(0.0f)._m20(ndirX)._m21(ndirY)._m22(ndirZ)._m23(0.0f)._m30(posX)._m31(posY)._m32(posZ)._m33(1.0f)._properties(18);
        return this;
    }
    
    public Vector3f getEulerAnglesZYX(final Vector3f dest) {
        dest.x = Math.atan2(this.m12(), this.m22());
        dest.y = Math.atan2(-this.m02(), Math.sqrt(1.0f - this.m02() * this.m02()));
        dest.z = Math.atan2(this.m01(), this.m00());
        return dest;
    }
    
    public Vector3f getEulerAnglesXYZ(final Vector3f dest) {
        dest.x = Math.atan2(-this.m21(), this.m22());
        dest.y = Math.atan2(this.m20(), Math.sqrt(1.0f - this.m20() * this.m20()));
        dest.z = Math.atan2(-this.m10(), this.m00());
        return dest;
    }
    
    public Matrix4f affineSpan(final Vector3f corner, final Vector3f xDir, final Vector3f yDir, final Vector3f zDir) {
        final float a = this.m10() * this.m22();
        final float b = this.m10() * this.m21();
        final float c = this.m10() * this.m02();
        final float d = this.m10() * this.m01();
        final float e = this.m11() * this.m22();
        final float f = this.m11() * this.m20();
        final float g = this.m11() * this.m02();
        final float h = this.m11() * this.m00();
        final float i = this.m12() * this.m21();
        final float j = this.m12() * this.m20();
        final float k = this.m12() * this.m01();
        final float l = this.m12() * this.m00();
        final float m = this.m20() * this.m02();
        final float n = this.m20() * this.m01();
        final float o = this.m21() * this.m02();
        final float p = this.m21() * this.m00();
        final float q = this.m22() * this.m01();
        final float r = this.m22() * this.m00();
        final float s = 1.0f / (this.m00() * this.m11() - this.m01() * this.m10()) * this.m22() + (this.m02() * this.m10() - this.m00() * this.m12()) * this.m21() + (this.m01() * this.m12() - this.m02() * this.m11()) * this.m20();
        final float nm00 = (e - i) * s;
        final float nm2 = (o - q) * s;
        final float nm3 = (k - g) * s;
        final float nm4 = (j - a) * s;
        final float nm5 = (r - m) * s;
        final float nm6 = (c - l) * s;
        final float nm7 = (b - f) * s;
        final float nm8 = (n - p) * s;
        final float nm9 = (h - d) * s;
        corner.x = -nm00 - nm4 - nm7 + (a * this.m31() - b * this.m32() + f * this.m32() - e * this.m30() + i * this.m30() - j * this.m31()) * s;
        corner.y = -nm2 - nm5 - nm8 + (m * this.m31() - n * this.m32() + p * this.m32() - o * this.m30() + q * this.m30() - r * this.m31()) * s;
        corner.z = -nm3 - nm6 - nm9 + (g * this.m30() - k * this.m30() + l * this.m31() - c * this.m31() + d * this.m32() - h * this.m32()) * s;
        xDir.x = 2.0f * nm00;
        xDir.y = 2.0f * nm2;
        xDir.z = 2.0f * nm3;
        yDir.x = 2.0f * nm4;
        yDir.y = 2.0f * nm5;
        yDir.z = 2.0f * nm6;
        zDir.x = 2.0f * nm7;
        zDir.y = 2.0f * nm8;
        zDir.z = 2.0f * nm9;
        return this;
    }
    
    public boolean testPoint(final float x, final float y, final float z) {
        final float nxX = this.m03() + this.m00();
        final float nxY = this.m13() + this.m10();
        final float nxZ = this.m23() + this.m20();
        final float nxW = this.m33() + this.m30();
        final float pxX = this.m03() - this.m00();
        final float pxY = this.m13() - this.m10();
        final float pxZ = this.m23() - this.m20();
        final float pxW = this.m33() - this.m30();
        final float nyX = this.m03() + this.m01();
        final float nyY = this.m13() + this.m11();
        final float nyZ = this.m23() + this.m21();
        final float nyW = this.m33() + this.m31();
        final float pyX = this.m03() - this.m01();
        final float pyY = this.m13() - this.m11();
        final float pyZ = this.m23() - this.m21();
        final float pyW = this.m33() - this.m31();
        final float nzX = this.m03() + this.m02();
        final float nzY = this.m13() + this.m12();
        final float nzZ = this.m23() + this.m22();
        final float nzW = this.m33() + this.m32();
        final float pzX = this.m03() - this.m02();
        final float pzY = this.m13() - this.m12();
        final float pzZ = this.m23() - this.m22();
        final float pzW = this.m33() - this.m32();
        return nxX * x + nxY * y + nxZ * z + nxW >= 0.0f && pxX * x + pxY * y + pxZ * z + pxW >= 0.0f && nyX * x + nyY * y + nyZ * z + nyW >= 0.0f && pyX * x + pyY * y + pyZ * z + pyW >= 0.0f && nzX * x + nzY * y + nzZ * z + nzW >= 0.0f && pzX * x + pzY * y + pzZ * z + pzW >= 0.0f;
    }
    
    public boolean testSphere(final float x, final float y, final float z, final float r) {
        float nxX = this.m03() + this.m00();
        float nxY = this.m13() + this.m10();
        float nxZ = this.m23() + this.m20();
        float nxW = this.m33() + this.m30();
        float invl = Math.invsqrt(nxX * nxX + nxY * nxY + nxZ * nxZ);
        nxX *= invl;
        nxY *= invl;
        nxZ *= invl;
        nxW *= invl;
        float pxX = this.m03() - this.m00();
        float pxY = this.m13() - this.m10();
        float pxZ = this.m23() - this.m20();
        float pxW = this.m33() - this.m30();
        invl = Math.invsqrt(pxX * pxX + pxY * pxY + pxZ * pxZ);
        pxX *= invl;
        pxY *= invl;
        pxZ *= invl;
        pxW *= invl;
        float nyX = this.m03() + this.m01();
        float nyY = this.m13() + this.m11();
        float nyZ = this.m23() + this.m21();
        float nyW = this.m33() + this.m31();
        invl = Math.invsqrt(nyX * nyX + nyY * nyY + nyZ * nyZ);
        nyX *= invl;
        nyY *= invl;
        nyZ *= invl;
        nyW *= invl;
        float pyX = this.m03() - this.m01();
        float pyY = this.m13() - this.m11();
        float pyZ = this.m23() - this.m21();
        float pyW = this.m33() - this.m31();
        invl = Math.invsqrt(pyX * pyX + pyY * pyY + pyZ * pyZ);
        pyX *= invl;
        pyY *= invl;
        pyZ *= invl;
        pyW *= invl;
        float nzX = this.m03() + this.m02();
        float nzY = this.m13() + this.m12();
        float nzZ = this.m23() + this.m22();
        float nzW = this.m33() + this.m32();
        invl = Math.invsqrt(nzX * nzX + nzY * nzY + nzZ * nzZ);
        nzX *= invl;
        nzY *= invl;
        nzZ *= invl;
        nzW *= invl;
        float pzX = this.m03() - this.m02();
        float pzY = this.m13() - this.m12();
        float pzZ = this.m23() - this.m22();
        float pzW = this.m33() - this.m32();
        invl = Math.invsqrt(pzX * pzX + pzY * pzY + pzZ * pzZ);
        pzX *= invl;
        pzY *= invl;
        pzZ *= invl;
        pzW *= invl;
        return nxX * x + nxY * y + nxZ * z + nxW >= -r && pxX * x + pxY * y + pxZ * z + pxW >= -r && nyX * x + nyY * y + nyZ * z + nyW >= -r && pyX * x + pyY * y + pyZ * z + pyW >= -r && nzX * x + nzY * y + nzZ * z + nzW >= -r && pzX * x + pzY * y + pzZ * z + pzW >= -r;
    }
    
    public boolean testAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        final float nxX = this.m03() + this.m00();
        final float nxY = this.m13() + this.m10();
        final float nxZ = this.m23() + this.m20();
        final float nxW = this.m33() + this.m30();
        final float pxX = this.m03() - this.m00();
        final float pxY = this.m13() - this.m10();
        final float pxZ = this.m23() - this.m20();
        final float pxW = this.m33() - this.m30();
        final float nyX = this.m03() + this.m01();
        final float nyY = this.m13() + this.m11();
        final float nyZ = this.m23() + this.m21();
        final float nyW = this.m33() + this.m31();
        final float pyX = this.m03() - this.m01();
        final float pyY = this.m13() - this.m11();
        final float pyZ = this.m23() - this.m21();
        final float pyW = this.m33() - this.m31();
        final float nzX = this.m03() + this.m02();
        final float nzY = this.m13() + this.m12();
        final float nzZ = this.m23() + this.m22();
        final float nzW = this.m33() + this.m32();
        final float pzX = this.m03() - this.m02();
        final float pzY = this.m13() - this.m12();
        final float pzZ = this.m23() - this.m22();
        final float pzW = this.m33() - this.m32();
        return nxX * ((nxX < 0.0f) ? minX : maxX) + nxY * ((nxY < 0.0f) ? minY : maxY) + nxZ * ((nxZ < 0.0f) ? minZ : maxZ) >= -nxW && pxX * ((pxX < 0.0f) ? minX : maxX) + pxY * ((pxY < 0.0f) ? minY : maxY) + pxZ * ((pxZ < 0.0f) ? minZ : maxZ) >= -pxW && nyX * ((nyX < 0.0f) ? minX : maxX) + nyY * ((nyY < 0.0f) ? minY : maxY) + nyZ * ((nyZ < 0.0f) ? minZ : maxZ) >= -nyW && pyX * ((pyX < 0.0f) ? minX : maxX) + pyY * ((pyY < 0.0f) ? minY : maxY) + pyZ * ((pyZ < 0.0f) ? minZ : maxZ) >= -pyW && nzX * ((nzX < 0.0f) ? minX : maxX) + nzY * ((nzY < 0.0f) ? minY : maxY) + nzZ * ((nzZ < 0.0f) ? minZ : maxZ) >= -nzW && pzX * ((pzX < 0.0f) ? minX : maxX) + pzY * ((pzY < 0.0f) ? minY : maxY) + pzZ * ((pzZ < 0.0f) ? minZ : maxZ) >= -pzW;
    }
    
    public Matrix4f obliqueZ(final float a, final float b) {
        return this._m20(this.m00() * a + this.m10() * b + this.m20())._m21(this.m01() * a + this.m11() * b + this.m21())._m22(this.m02() * a + this.m12() * b + this.m22())._properties(this.properties & 0x2);
    }
    
    public Matrix4f obliqueZ(final float a, final float b, final Matrix4f dest) {
        dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(this.m00() * a + this.m10() * b + this.m20())._m21(this.m01() * a + this.m11() * b + this.m21())._m22(this.m02() * a + this.m12() * b + this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x2);
        return dest;
    }
    
    public static void projViewFromRectangle(final Vector3f eye, final Vector3f p, final Vector3f x, final Vector3f y, final float nearFarDist, final boolean zeroToOne, final Matrix4f projDest, final Matrix4f viewDest) {
        float zx = y.y * x.z - y.z * x.y;
        float zy = y.z * x.x - y.x * x.z;
        float zz = y.x * x.y - y.y * x.x;
        float zd = zx * (p.x - eye.x) + zy * (p.y - eye.y) + zz * (p.z - eye.z);
        final float zs = (zd >= 0.0f) ? 1.0f : -1.0f;
        zx *= zs;
        zy *= zs;
        zz *= zs;
        zd *= zs;
        viewDest.setLookAt(eye.x, eye.y, eye.z, eye.x + zx, eye.y + zy, eye.z + zz, y.x, y.y, y.z);
        final float px = viewDest.m00() * p.x + viewDest.m10() * p.y + viewDest.m20() * p.z + viewDest.m30();
        final float py = viewDest.m01() * p.x + viewDest.m11() * p.y + viewDest.m21() * p.z + viewDest.m31();
        final float tx = viewDest.m00() * x.x + viewDest.m10() * x.y + viewDest.m20() * x.z;
        final float ty = viewDest.m01() * y.x + viewDest.m11() * y.y + viewDest.m21() * y.z;
        final float len = Math.sqrt(zx * zx + zy * zy + zz * zz);
        float near = zd / len;
        float far;
        if (Float.isInfinite(nearFarDist) && nearFarDist < 0.0f) {
            far = near;
            near = Float.POSITIVE_INFINITY;
        }
        else if (Float.isInfinite(nearFarDist) && nearFarDist > 0.0f) {
            far = Float.POSITIVE_INFINITY;
        }
        else if (nearFarDist < 0.0f) {
            far = near;
            near += nearFarDist;
        }
        else {
            far = near + nearFarDist;
        }
        projDest.setFrustum(px, px + tx, py, py + ty, near, far, zeroToOne);
    }
    
    public Matrix4f withLookAtUp(final Vector3fc up) {
        return this.withLookAtUp(up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4f withLookAtUp(final Vector3fc up, final Matrix4f dest) {
        return this.withLookAtUp(up.x(), up.y(), up.z());
    }
    
    public Matrix4f withLookAtUp(final float upX, final float upY, final float upZ) {
        return this.withLookAtUp(upX, upY, upZ, this);
    }
    
    public Matrix4f withLookAtUp(final float upX, final float upY, final float upZ, final Matrix4f dest) {
        final float y = (upY * this.m21() - upZ * this.m11()) * this.m02() + (upZ * this.m01() - upX * this.m21()) * this.m12() + (upX * this.m11() - upY * this.m01()) * this.m22();
        float x = upX * this.m01() + upY * this.m11() + upZ * this.m21();
        if ((this.properties & 0x10) == 0x0) {
            x *= Math.sqrt(this.m01() * this.m01() + this.m11() * this.m11() + this.m21() * this.m21());
        }
        final float invsqrt = Math.invsqrt(y * y + x * x);
        final float c = x * invsqrt;
        final float s = y * invsqrt;
        final float nm00 = c * this.m00() - s * this.m01();
        final float nm2 = c * this.m10() - s * this.m11();
        final float nm3 = c * this.m20() - s * this.m21();
        final float nm4 = s * this.m30() + c * this.m31();
        final float nm5 = s * this.m00() + c * this.m01();
        final float nm6 = s * this.m10() + c * this.m11();
        final float nm7 = s * this.m20() + c * this.m21();
        final float nm8 = c * this.m30() - s * this.m31();
        dest._m00(nm00)._m10(nm2)._m20(nm3)._m30(nm8)._m01(nm5)._m11(nm6)._m21(nm7)._m31(nm4);
        if (dest != this) {
            dest._m02(this.m02())._m12(this.m12())._m22(this.m22())._m32(this.m32())._m03(this.m03())._m13(this.m13())._m23(this.m23())._m33(this.m33());
        }
        dest._properties(this.properties & 0xFFFFFFF2);
        return dest;
    }
    
    public Matrix4f mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix4f mapXZY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(m10)._m21(m11)._m22(m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix4f mapXZnY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix4f mapXnYnZ(final Matrix4f dest) {
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix4f mapXnZY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(m10)._m21(m11)._m22(m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix4f mapXnZnY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix4f mapYXZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix4f mapYXnZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix4f mapYZX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix4f mapYZnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix4f mapYnXZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix4f mapYnXnZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix4f mapYnZX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix4f mapYnZnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m10())._m01(this.m11())._m02(this.m12())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix4f mapZXY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(m4)._m21(m5)._m22(m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix4f mapZXnY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix4f mapZYX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix4f mapZYnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix4f mapZnXY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(m4)._m21(m5)._m22(m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix4f mapZnXnY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix4f mapZnYX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix4f mapZnYnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(this.m20())._m01(this.m21())._m02(this.m22())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix4f mapnXYnZ(final Matrix4f dest) {
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix4f mapnXZY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(m10)._m21(m11)._m22(m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix4f mapnXZnY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix4f mapnXnYZ(final Matrix4f dest) {
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix4f mapnXnYnZ(final Matrix4f dest) {
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix4f mapnXnZY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(m10)._m21(m11)._m22(m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix4f mapnXnZnY(final Matrix4f dest) {
        final float m10 = this.m10();
        final float m11 = this.m11();
        final float m12 = this.m12();
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(-m10)._m21(-m11)._m22(-m12)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix4f mapnYXZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix4f mapnYXnZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix4f mapnYZX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix4f mapnYZnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(this.m20())._m11(this.m21())._m12(this.m22())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix4f mapnYnXZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix4f mapnYnXnZ(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix4f mapnYnZX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix4f mapnYnZnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m10())._m01(-this.m11())._m02(-this.m12())._m03(this.m03())._m10(-this.m20())._m11(-this.m21())._m12(-this.m22())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix4f mapnZXY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(m4)._m21(m5)._m22(m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix4f mapnZXnY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(m00)._m11(m2)._m12(m3)._m13(this.m13())._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix4f mapnZYX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix4f mapnZYnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix4f mapnZnXY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(m4)._m21(m5)._m22(m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix4f mapnZnXnY(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        final float m4 = this.m10();
        final float m5 = this.m11();
        final float m6 = this.m12();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(-m00)._m11(-m2)._m12(-m3)._m13(this.m13())._m20(-m4)._m21(-m5)._m22(-m6)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix4f mapnZnYX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(m00)._m21(m2)._m22(m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix4f mapnZnYnX(final Matrix4f dest) {
        final float m00 = this.m00();
        final float m2 = this.m01();
        final float m3 = this.m02();
        return dest._m00(-this.m20())._m01(-this.m21())._m02(-this.m22())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(-m00)._m21(-m2)._m22(-m3)._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateX() {
        return this._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateX(final Matrix4f dest) {
        return dest._m00(-this.m00())._m01(-this.m01())._m02(-this.m02())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateY() {
        return this._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateY(final Matrix4f dest) {
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(-this.m10())._m11(-this.m11())._m12(-this.m12())._m13(this.m13())._m20(this.m20())._m21(this.m21())._m22(this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateZ() {
        return this._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._properties(this.properties & 0x12);
    }
    
    public Matrix4f negateZ(final Matrix4f dest) {
        return dest._m00(this.m00())._m01(this.m01())._m02(this.m02())._m03(this.m03())._m10(this.m10())._m11(this.m11())._m12(this.m12())._m13(this.m13())._m20(-this.m20())._m21(-this.m21())._m22(-this.m22())._m23(this.m23())._m30(this.m30())._m31(this.m31())._m32(this.m32())._m33(this.m33())._properties(this.properties & 0x12);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00()) && Math.isFinite(this.m01()) && Math.isFinite(this.m02()) && Math.isFinite(this.m03()) && Math.isFinite(this.m10()) && Math.isFinite(this.m11()) && Math.isFinite(this.m12()) && Math.isFinite(this.m13()) && Math.isFinite(this.m20()) && Math.isFinite(this.m21()) && Math.isFinite(this.m22()) && Math.isFinite(this.m23()) && Math.isFinite(this.m30()) && Math.isFinite(this.m31()) && Math.isFinite(this.m32()) && Math.isFinite(this.m33());
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
