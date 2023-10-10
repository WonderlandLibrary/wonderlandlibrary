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

public class Matrix4x3f implements Externalizable, Cloneable, Matrix4x3fc
{
    private static final long serialVersionUID = 1L;
    float m00;
    float m01;
    float m02;
    float m10;
    float m11;
    float m12;
    float m20;
    float m21;
    float m22;
    float m30;
    float m31;
    float m32;
    int properties;
    
    public Matrix4x3f() {
        this.m00 = 1.0f;
        this.m11 = 1.0f;
        this.m22 = 1.0f;
        this.properties = 28;
    }
    
    public Matrix4x3f(final Matrix3fc mat) {
        this.set(mat);
    }
    
    public Matrix4x3f(final Matrix4x3fc mat) {
        this.set(mat);
    }
    
    public Matrix4x3f(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22, final float m30, final float m31, final float m32) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.determineProperties();
    }
    
    public Matrix4x3f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        this.determineProperties();
    }
    
    public Matrix4x3f(final Vector3fc col0, final Vector3fc col1, final Vector3fc col2, final Vector3fc col3) {
        this.set(col0, col1, col2, col3).determineProperties();
    }
    
    public Matrix4x3f assume(final int properties) {
        this.properties = properties;
        return this;
    }
    
    public Matrix4x3f determineProperties() {
        int properties = 0;
        if (this.m00 == 1.0f && this.m01 == 0.0f && this.m02 == 0.0f && this.m10 == 0.0f && this.m11 == 1.0f && this.m12 == 0.0f && this.m20 == 0.0f && this.m21 == 0.0f && this.m22 == 1.0f) {
            properties |= 0x18;
            if (this.m30 == 0.0f && this.m31 == 0.0f && this.m32 == 0.0f) {
                properties |= 0x4;
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
    
    public float m30() {
        return this.m30;
    }
    
    public float m31() {
        return this.m31;
    }
    
    public float m32() {
        return this.m32;
    }
    
    public Matrix4x3f m00(final float m00) {
        this.m00 = m00;
        this.properties &= 0xFFFFFFEF;
        if (m00 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m01(final float m01) {
        this.m01 = m01;
        this.properties &= 0xFFFFFFEF;
        if (m01 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m02(final float m02) {
        this.m02 = m02;
        this.properties &= 0xFFFFFFEF;
        if (m02 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m10(final float m10) {
        this.m10 = m10;
        this.properties &= 0xFFFFFFEF;
        if (m10 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m11(final float m11) {
        this.m11 = m11;
        this.properties &= 0xFFFFFFEF;
        if (m11 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m12(final float m12) {
        this.m12 = m12;
        this.properties &= 0xFFFFFFEF;
        if (m12 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m20(final float m20) {
        this.m20 = m20;
        this.properties &= 0xFFFFFFEF;
        if (m20 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m21(final float m21) {
        this.m21 = m21;
        this.properties &= 0xFFFFFFEF;
        if (m21 != 0.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m22(final float m22) {
        this.m22 = m22;
        this.properties &= 0xFFFFFFEF;
        if (m22 != 1.0f) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3f m30(final float m30) {
        this.m30 = m30;
        if (m30 != 0.0f) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    public Matrix4x3f m31(final float m31) {
        this.m31 = m31;
        if (m31 != 0.0f) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    public Matrix4x3f m32(final float m32) {
        this.m32 = m32;
        if (m32 != 0.0f) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    Matrix4x3f _properties(final int properties) {
        this.properties = properties;
        return this;
    }
    
    Matrix4x3f _m00(final float m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix4x3f _m01(final float m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix4x3f _m02(final float m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix4x3f _m10(final float m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix4x3f _m11(final float m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix4x3f _m12(final float m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix4x3f _m20(final float m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix4x3f _m21(final float m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix4x3f _m22(final float m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix4x3f _m30(final float m30) {
        this.m30 = m30;
        return this;
    }
    
    Matrix4x3f _m31(final float m31) {
        this.m31 = m31;
        return this;
    }
    
    Matrix4x3f _m32(final float m32) {
        this.m32 = m32;
        return this;
    }
    
    public Matrix4x3f identity() {
        if ((this.properties & 0x4) != 0x0) {
            return this;
        }
        MemUtil.INSTANCE.identity(this);
        this.properties = 28;
        return this;
    }
    
    public Matrix4x3f set(final Matrix4x3fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m02 = m.m02();
        this.m10 = m.m10();
        this.m11 = m.m11();
        this.m12 = m.m12();
        this.m20 = m.m20();
        this.m21 = m.m21();
        this.m22 = m.m22();
        this.m30 = m.m30();
        this.m31 = m.m31();
        this.m32 = m.m32();
        this.properties = m.properties();
        return this;
    }
    
    public Matrix4x3f set(final Matrix4fc m) {
        this.m00 = m.m00();
        this.m01 = m.m01();
        this.m02 = m.m02();
        this.m10 = m.m10();
        this.m11 = m.m11();
        this.m12 = m.m12();
        this.m20 = m.m20();
        this.m21 = m.m21();
        this.m22 = m.m22();
        this.m30 = m.m30();
        this.m31 = m.m31();
        this.m32 = m.m32();
        this.properties = (m.properties() & 0x1C);
        return this;
    }
    
    public Matrix4f get(final Matrix4f dest) {
        return dest.set4x3(this);
    }
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set4x3(this);
    }
    
    public Matrix4x3f set(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final AxisAngle4f axisAngle) {
        float x = axisAngle.x;
        float y = axisAngle.y;
        float z = axisAngle.z;
        final float angle = axisAngle.angle;
        float n = Math.sqrt(x * x + y * y + z * z);
        n = 1.0f / n;
        x *= n;
        y *= n;
        z *= n;
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f set(final AxisAngle4d axisAngle) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix4x3f set(final Quaterniondc q) {
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
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f set(final Vector3fc col0, final Vector3fc col1, final Vector3fc col2, final Vector3fc col3) {
        this.m00 = col0.x();
        this.m01 = col0.y();
        this.m02 = col0.z();
        this.m10 = col1.x();
        this.m11 = col1.y();
        this.m12 = col1.z();
        this.m20 = col2.x();
        this.m21 = col2.y();
        this.m22 = col2.z();
        this.m30 = col3.x();
        this.m31 = col3.y();
        this.m32 = col3.z();
        return this.determineProperties();
    }
    
    public Matrix4x3f set3x3(final Matrix4x3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.properties &= mat.properties();
        return this;
    }
    
    public Matrix4x3f mul(final Matrix4x3fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4x3f mul(final Matrix4x3fc right, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.set(right);
        }
        if ((right.properties() & 0x4) != 0x0) {
            return dest.set(this);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.mulTranslation(right, dest);
        }
        return this.mulGeneric(right, dest);
    }
    
    private Matrix4x3f mulGeneric(final Matrix4x3fc right, final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        final float m7 = this.m20;
        final float m8 = this.m21;
        final float m9 = this.m22;
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
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32))))._properties(this.properties & right.properties() & 0x10);
    }
    
    public Matrix4x3f mulTranslation(final Matrix4x3fc right, final Matrix4x3f dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m10(right.m10())._m11(right.m11())._m12(right.m12())._m20(right.m20())._m21(right.m21())._m22(right.m22())._m30(right.m30() + this.m30)._m31(right.m31() + this.m31)._m32(right.m32() + this.m32)._properties(right.properties() & 0x10);
    }
    
    public Matrix4x3f mulOrtho(final Matrix4x3fc view) {
        return this.mulOrtho(view, this);
    }
    
    public Matrix4x3f mulOrtho(final Matrix4x3fc view, final Matrix4x3f dest) {
        final float nm00 = this.m00 * view.m00();
        final float nm2 = this.m11 * view.m01();
        final float nm3 = this.m22 * view.m02();
        final float nm4 = this.m00 * view.m10();
        final float nm5 = this.m11 * view.m11();
        final float nm6 = this.m22 * view.m12();
        final float nm7 = this.m00 * view.m20();
        final float nm8 = this.m11 * view.m21();
        final float nm9 = this.m22 * view.m22();
        final float nm10 = this.m00 * view.m30() + this.m30;
        final float nm11 = this.m11 * view.m31() + this.m31;
        final float nm12 = this.m22 * view.m32() + this.m32;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.m30 = nm10;
        dest.m31 = nm11;
        dest.m32 = nm12;
        dest.properties = (this.properties & view.properties() & 0x10);
        return dest;
    }
    
    public Matrix4x3f mul3x3(final float rm00, final float rm01, final float rm02, final float rm10, final float rm11, final float rm12, final float rm20, final float rm21, final float rm22) {
        return this.mul3x3(rm00, rm01, rm02, rm10, rm11, rm12, rm20, rm21, rm22, this);
    }
    
    public Matrix4x3f mul3x3(final float rm00, final float rm01, final float rm02, final float rm10, final float rm11, final float rm12, final float rm20, final float rm21, final float rm22, final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        final float m7 = this.m20;
        final float m8 = this.m21;
        final float m9 = this.m22;
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm01, m7 * rm02)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm01, m8 * rm02)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm01, m9 * rm02)))._m10(Math.fma(m00, rm10, Math.fma(m4, rm11, m7 * rm12)))._m11(Math.fma(m2, rm10, Math.fma(m5, rm11, m8 * rm12)))._m12(Math.fma(m3, rm10, Math.fma(m6, rm11, m9 * rm12)))._m20(Math.fma(m00, rm20, Math.fma(m4, rm21, m7 * rm22)))._m21(Math.fma(m2, rm20, Math.fma(m5, rm21, m8 * rm22)))._m22(Math.fma(m3, rm20, Math.fma(m6, rm21, m9 * rm22)))._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(0);
    }
    
    public Matrix4x3f fma(final Matrix4x3fc other, final float otherFactor) {
        return this.fma(other, otherFactor, this);
    }
    
    public Matrix4x3f fma(final Matrix4x3fc other, final float otherFactor, final Matrix4x3f dest) {
        dest._m00(Math.fma(other.m00(), otherFactor, this.m00))._m01(Math.fma(other.m01(), otherFactor, this.m01))._m02(Math.fma(other.m02(), otherFactor, this.m02))._m10(Math.fma(other.m10(), otherFactor, this.m10))._m11(Math.fma(other.m11(), otherFactor, this.m11))._m12(Math.fma(other.m12(), otherFactor, this.m12))._m20(Math.fma(other.m20(), otherFactor, this.m20))._m21(Math.fma(other.m21(), otherFactor, this.m21))._m22(Math.fma(other.m22(), otherFactor, this.m22))._m30(Math.fma(other.m30(), otherFactor, this.m30))._m31(Math.fma(other.m31(), otherFactor, this.m31))._m32(Math.fma(other.m32(), otherFactor, this.m32))._properties(0);
        return dest;
    }
    
    public Matrix4x3f add(final Matrix4x3fc other) {
        return this.add(other, this);
    }
    
    public Matrix4x3f add(final Matrix4x3fc other, final Matrix4x3f dest) {
        dest.m00 = this.m00 + other.m00();
        dest.m01 = this.m01 + other.m01();
        dest.m02 = this.m02 + other.m02();
        dest.m10 = this.m10 + other.m10();
        dest.m11 = this.m11 + other.m11();
        dest.m12 = this.m12 + other.m12();
        dest.m20 = this.m20 + other.m20();
        dest.m21 = this.m21 + other.m21();
        dest.m22 = this.m22 + other.m22();
        dest.m30 = this.m30 + other.m30();
        dest.m31 = this.m31 + other.m31();
        dest.m32 = this.m32 + other.m32();
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f sub(final Matrix4x3fc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix4x3f sub(final Matrix4x3fc subtrahend, final Matrix4x3f dest) {
        dest.m00 = this.m00 - subtrahend.m00();
        dest.m01 = this.m01 - subtrahend.m01();
        dest.m02 = this.m02 - subtrahend.m02();
        dest.m10 = this.m10 - subtrahend.m10();
        dest.m11 = this.m11 - subtrahend.m11();
        dest.m12 = this.m12 - subtrahend.m12();
        dest.m20 = this.m20 - subtrahend.m20();
        dest.m21 = this.m21 - subtrahend.m21();
        dest.m22 = this.m22 - subtrahend.m22();
        dest.m30 = this.m30 - subtrahend.m30();
        dest.m31 = this.m31 - subtrahend.m31();
        dest.m32 = this.m32 - subtrahend.m32();
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f mulComponentWise(final Matrix4x3fc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix4x3f mulComponentWise(final Matrix4x3fc other, final Matrix4x3f dest) {
        dest.m00 = this.m00 * other.m00();
        dest.m01 = this.m01 * other.m01();
        dest.m02 = this.m02 * other.m02();
        dest.m10 = this.m10 * other.m10();
        dest.m11 = this.m11 * other.m11();
        dest.m12 = this.m12 * other.m12();
        dest.m20 = this.m20 * other.m20();
        dest.m21 = this.m21 * other.m21();
        dest.m22 = this.m22 * other.m22();
        dest.m30 = this.m30 * other.m30();
        dest.m31 = this.m31 * other.m31();
        dest.m32 = this.m32 * other.m32();
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f set(final float m00, final float m01, final float m02, final float m10, final float m11, final float m12, final float m20, final float m21, final float m22, final float m30, final float m31, final float m32) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final float[] m, final int off) {
        MemUtil.INSTANCE.copy(m, off, this);
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final float[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4x3f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this.determineProperties();
    }
    
    public float determinant() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public Matrix4x3f invert(final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.invertOrthonormal(dest);
        }
        return this.invertGeneric(dest);
    }
    
    private Matrix4x3f invertGeneric(final Matrix4x3f dest) {
        final float m11m00 = this.m00 * this.m11;
        final float m10m01 = this.m01 * this.m10;
        final float m10m2 = this.m02 * this.m10;
        final float m12m00 = this.m00 * this.m12;
        final float m12m2 = this.m01 * this.m12;
        final float m11m2 = this.m02 * this.m11;
        final float s = 1.0f / ((m11m00 - m10m01) * this.m22 + (m10m2 - m12m00) * this.m21 + (m12m2 - m11m2) * this.m20);
        final float m10m3 = this.m10 * this.m22;
        final float m10m4 = this.m10 * this.m21;
        final float m11m3 = this.m11 * this.m22;
        final float m11m4 = this.m11 * this.m20;
        final float m12m3 = this.m12 * this.m21;
        final float m12m4 = this.m12 * this.m20;
        final float m20m02 = this.m20 * this.m02;
        final float m20m3 = this.m20 * this.m01;
        final float m21m02 = this.m21 * this.m02;
        final float m21m3 = this.m21 * this.m00;
        final float m22m01 = this.m22 * this.m01;
        final float m22m2 = this.m22 * this.m00;
        final float nm00 = (m11m3 - m12m3) * s;
        final float nm2 = (m21m02 - m22m01) * s;
        final float nm3 = (m12m2 - m11m2) * s;
        final float nm4 = (m12m4 - m10m3) * s;
        final float nm5 = (m22m2 - m20m02) * s;
        final float nm6 = (m10m2 - m12m00) * s;
        final float nm7 = (m10m4 - m11m4) * s;
        final float nm8 = (m20m3 - m21m3) * s;
        final float nm9 = (m11m00 - m10m01) * s;
        final float nm10 = (m10m3 * this.m31 - m10m4 * this.m32 + m11m4 * this.m32 - m11m3 * this.m30 + m12m3 * this.m30 - m12m4 * this.m31) * s;
        final float nm11 = (m20m02 * this.m31 - m20m3 * this.m32 + m21m3 * this.m32 - m21m02 * this.m30 + m22m01 * this.m30 - m22m2 * this.m31) * s;
        final float nm12 = (m11m2 * this.m30 - m12m2 * this.m30 + m12m00 * this.m31 - m10m2 * this.m31 + m10m01 * this.m32 - m11m00 * this.m32) * s;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.m30 = nm10;
        dest.m31 = nm11;
        dest.m32 = nm12;
        dest.properties = 0;
        return dest;
    }
    
    private Matrix4x3f invertOrthonormal(final Matrix4x3f dest) {
        final float nm30 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
        final float nm31 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
        final float nm32 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
        final float m01 = this.m01;
        final float m2 = this.m02;
        final float m3 = this.m12;
        dest.m00 = this.m00;
        dest.m01 = this.m10;
        dest.m02 = this.m20;
        dest.m10 = m01;
        dest.m11 = this.m11;
        dest.m12 = this.m21;
        dest.m20 = m2;
        dest.m21 = m3;
        dest.m22 = this.m22;
        dest.m30 = nm30;
        dest.m31 = nm31;
        dest.m32 = nm32;
        dest.properties = 16;
        return dest;
    }
    
    public Matrix4f invert(final Matrix4f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.invertOrthonormal(dest);
        }
        return this.invertGeneric(dest);
    }
    
    private Matrix4f invertGeneric(final Matrix4f dest) {
        final float m11m00 = this.m00 * this.m11;
        final float m10m01 = this.m01 * this.m10;
        final float m10m2 = this.m02 * this.m10;
        final float m12m00 = this.m00 * this.m12;
        final float m12m2 = this.m01 * this.m12;
        final float m11m2 = this.m02 * this.m11;
        final float s = 1.0f / ((m11m00 - m10m01) * this.m22 + (m10m2 - m12m00) * this.m21 + (m12m2 - m11m2) * this.m20);
        final float m10m3 = this.m10 * this.m22;
        final float m10m4 = this.m10 * this.m21;
        final float m11m3 = this.m11 * this.m22;
        final float m11m4 = this.m11 * this.m20;
        final float m12m3 = this.m12 * this.m21;
        final float m12m4 = this.m12 * this.m20;
        final float m20m02 = this.m20 * this.m02;
        final float m20m3 = this.m20 * this.m01;
        final float m21m02 = this.m21 * this.m02;
        final float m21m3 = this.m21 * this.m00;
        final float m22m01 = this.m22 * this.m01;
        final float m22m2 = this.m22 * this.m00;
        final float nm00 = (m11m3 - m12m3) * s;
        final float nm2 = (m21m02 - m22m01) * s;
        final float nm3 = (m12m2 - m11m2) * s;
        final float nm4 = (m12m4 - m10m3) * s;
        final float nm5 = (m22m2 - m20m02) * s;
        final float nm6 = (m10m2 - m12m00) * s;
        final float nm7 = (m10m4 - m11m4) * s;
        final float nm8 = (m20m3 - m21m3) * s;
        final float nm9 = (m11m00 - m10m01) * s;
        final float nm10 = (m10m3 * this.m31 - m10m4 * this.m32 + m11m4 * this.m32 - m11m3 * this.m30 + m12m3 * this.m30 - m12m4 * this.m31) * s;
        final float nm11 = (m20m02 * this.m31 - m20m3 * this.m32 + m21m3 * this.m32 - m21m02 * this.m30 + m22m01 * this.m30 - m22m2 * this.m31) * s;
        final float nm12 = (m11m2 * this.m30 - m12m2 * this.m30 + m12m00 * this.m31 - m10m2 * this.m31 + m10m01 * this.m32 - m11m00 * this.m32) * s;
        return dest._m00(nm00)._m01(nm2)._m02(nm3)._m03(0.0f)._m10(nm4)._m11(nm5)._m12(nm6)._m13(0.0f)._m20(nm7)._m21(nm8)._m22(nm9)._m23(0.0f)._m30(nm10)._m31(nm11)._m32(nm12)._m33(1.0f)._properties(0);
    }
    
    private Matrix4f invertOrthonormal(final Matrix4f dest) {
        final float nm30 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
        final float nm31 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
        final float nm32 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
        final float m01 = this.m01;
        final float m2 = this.m02;
        final float m3 = this.m12;
        return dest._m00(this.m00)._m01(this.m10)._m02(this.m20)._m03(0.0f)._m10(m01)._m11(this.m11)._m12(this.m21)._m13(0.0f)._m20(m2)._m21(m3)._m22(this.m22)._m23(0.0f)._m30(nm30)._m31(nm31)._m32(nm32)._m33(1.0f)._properties(16);
    }
    
    public Matrix4x3f invert() {
        return this.invert(this);
    }
    
    public Matrix4x3f invertOrtho(final Matrix4x3f dest) {
        final float invM00 = 1.0f / this.m00;
        final float invM2 = 1.0f / this.m11;
        final float invM3 = 1.0f / this.m22;
        dest.set(invM00, 0.0f, 0.0f, 0.0f, invM2, 0.0f, 0.0f, 0.0f, invM3, -this.m30 * invM00, -this.m31 * invM2, -this.m32 * invM3);
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f invertOrtho() {
        return this.invertOrtho(this);
    }
    
    public Matrix4x3f transpose3x3() {
        return this.transpose3x3(this);
    }
    
    public Matrix4x3f transpose3x3(final Matrix4x3f dest) {
        final float nm00 = this.m00;
        final float nm2 = this.m10;
        final float nm3 = this.m20;
        final float nm4 = this.m01;
        final float nm5 = this.m11;
        final float nm6 = this.m21;
        final float nm7 = this.m02;
        final float nm8 = this.m12;
        final float nm9 = this.m22;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.properties = this.properties;
        return dest;
    }
    
    public Matrix3f transpose3x3(final Matrix3f dest) {
        dest.m00(this.m00);
        dest.m01(this.m10);
        dest.m02(this.m20);
        dest.m10(this.m01);
        dest.m11(this.m11);
        dest.m12(this.m21);
        dest.m20(this.m02);
        dest.m21(this.m12);
        dest.m22(this.m22);
        return dest;
    }
    
    public Matrix4x3f translation(final float x, final float y, final float z) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this.m30 = x;
        this.m31 = y;
        this.m32 = z;
        this.properties = 24;
        return this;
    }
    
    public Matrix4x3f translation(final Vector3fc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3f setTranslation(final float x, final float y, final float z) {
        this.m30 = x;
        this.m31 = y;
        this.m32 = z;
        this.properties &= 0xFFFFFFFB;
        return this;
    }
    
    public Matrix4x3f setTranslation(final Vector3fc xyz) {
        return this.setTranslation(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Vector3f getTranslation(final Vector3f dest) {
        dest.x = this.m30;
        dest.y = this.m31;
        dest.z = this.m32;
        return dest;
    }
    
    public Vector3f getScale(final Vector3f dest) {
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
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + " " + Runtime.format(this.m20, formatter) + " " + Runtime.format(this.m30, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + " " + Runtime.format(this.m21, formatter) + " " + Runtime.format(this.m31, formatter) + "\n" + Runtime.format(this.m02, formatter) + " " + Runtime.format(this.m12, formatter) + " " + Runtime.format(this.m22, formatter) + " " + Runtime.format(this.m32, formatter) + "\n";
    }
    
    public Matrix4x3f get(final Matrix4x3f dest) {
        return dest.set(this);
    }
    
    public Matrix4x3d get(final Matrix4x3d dest) {
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
    
    public Matrix4x3fc getToAddress(final long address) {
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
    
    public float[] get4x4(final float[] arr, final int offset) {
        MemUtil.INSTANCE.copy4x4(this, arr, offset);
        return arr;
    }
    
    public float[] get4x4(final float[] arr) {
        return this.get4x4(arr, 0);
    }
    
    public FloatBuffer get4x4(final FloatBuffer buffer) {
        return this.get4x4(buffer.position(), buffer);
    }
    
    public FloatBuffer get4x4(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer get4x4(final ByteBuffer buffer) {
        return this.get4x4(buffer.position(), buffer);
    }
    
    public ByteBuffer get4x4(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put4x4(this, index, buffer);
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
    
    public float[] getTransposed(final float[] arr, final int offset) {
        arr[offset + 0] = this.m00;
        arr[offset + 1] = this.m10;
        arr[offset + 2] = this.m20;
        arr[offset + 3] = this.m30;
        arr[offset + 4] = this.m01;
        arr[offset + 5] = this.m11;
        arr[offset + 6] = this.m21;
        arr[offset + 7] = this.m31;
        arr[offset + 8] = this.m02;
        arr[offset + 9] = this.m12;
        arr[offset + 10] = this.m22;
        arr[offset + 11] = this.m32;
        return arr;
    }
    
    public float[] getTransposed(final float[] arr) {
        return this.getTransposed(arr, 0);
    }
    
    public Matrix4x3f zero() {
        MemUtil.INSTANCE.zero(this);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f scaling(final float factor) {
        return this.scaling(factor, factor, factor);
    }
    
    public Matrix4x3f scaling(final float x, final float y, final float z) {
        if ((this.properties & 0x4) == 0x0) {
            MemUtil.INSTANCE.identity(this);
        }
        this.m00 = x;
        this.m11 = y;
        this.m22 = z;
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        this.properties = (one ? 16 : 0);
        return this;
    }
    
    public Matrix4x3f scaling(final Vector3fc xyz) {
        return this.scaling(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Matrix4x3f rotation(final float angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4x3f rotation(final AxisAngle4f axisAngle) {
        return this.rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4x3f rotation(final float angle, final float x, final float y, final float z) {
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
    
    private Matrix4x3f rotationInternal(final float angle, final float x, final float y, final float z) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float C = 1.0f - cos;
        final float xy = x * y;
        final float xz = x * z;
        final float yz = y * z;
        this.m00 = cos + x * x * C;
        this.m01 = xy * C + z * sin;
        this.m02 = xz * C - y * sin;
        this.m10 = xy * C - z * sin;
        this.m11 = cos + y * y * C;
        this.m12 = yz * C + x * sin;
        this.m20 = xz * C + y * sin;
        this.m21 = yz * C - x * sin;
        this.m22 = cos + z * z * C;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationX(final float ang) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationY(final float ang) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationZ(final float ang) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationXYZ(final float angleX, final float angleY, final float angleZ) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationZYX(final float angleZ, final float angleY, final float angleX) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotationYXZ(final float angleY, final float angleX, final float angleZ) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f setRotationXYZ(final float angleX, final float angleY, final float angleZ) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3f setRotationZYX(final float angleZ, final float angleY, final float angleX) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3f setRotationYXZ(final float angleY, final float angleX, final float angleZ) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3f rotation(final Quaternionfc quat) {
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
        this._m00(w2 + x2 - z2 - y2);
        this._m01(dxy + dzw);
        this._m02(dxz - dyw);
        this._m10(dxy - dzw);
        this._m11(y2 - z2 + w2 - x2);
        this._m12(dyz + dxw);
        this._m20(dyw + dxz);
        this._m21(dyz - dxw);
        this._m22(z2 - y2 - x2 + w2);
        this._m30(0.0f);
        this._m31(0.0f);
        this._m32(0.0f);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f translationRotateScale(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float sx, final float sy, final float sz) {
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
        this.m00 = sx - (q2 + q3) * sx;
        this.m01 = (q4 + q9) * sx;
        this.m02 = (q5 - q8) * sx;
        this.m10 = (q4 - q9) * sy;
        this.m11 = sy - (q3 + q00) * sy;
        this.m12 = (q7 + q6) * sy;
        this.m20 = (q5 + q8) * sz;
        this.m21 = (q7 - q6) * sz;
        this.m22 = sz - (q2 + q00) * sz;
        this.m30 = tx;
        this.m31 = ty;
        this.m32 = tz;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4x3f translationRotateScaleMul(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final float sx, final float sy, final float sz, final Matrix4x3f m) {
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
        final float nm00 = sx - (q2 + q3) * sx;
        final float nm2 = (q4 + q9) * sx;
        final float nm3 = (q5 - q8) * sx;
        final float nm4 = (q4 - q9) * sy;
        final float nm5 = sy - (q3 + q00) * sy;
        final float nm6 = (q7 + q6) * sy;
        final float nm7 = (q5 + q8) * sz;
        final float nm8 = (q7 - q6) * sz;
        final float nm9 = sz - (q2 + q00) * sz;
        final float m2 = nm00 * m.m00 + nm4 * m.m01 + nm7 * m.m02;
        final float m3 = nm2 * m.m00 + nm5 * m.m01 + nm8 * m.m02;
        this.m02 = nm3 * m.m00 + nm6 * m.m01 + nm9 * m.m02;
        this.m00 = m2;
        this.m01 = m3;
        final float m4 = nm00 * m.m10 + nm4 * m.m11 + nm7 * m.m12;
        final float m5 = nm2 * m.m10 + nm5 * m.m11 + nm8 * m.m12;
        this.m12 = nm3 * m.m10 + nm6 * m.m11 + nm9 * m.m12;
        this.m10 = m4;
        this.m11 = m5;
        final float m6 = nm00 * m.m20 + nm4 * m.m21 + nm7 * m.m22;
        final float m7 = nm2 * m.m20 + nm5 * m.m21 + nm8 * m.m22;
        this.m22 = nm3 * m.m20 + nm6 * m.m21 + nm9 * m.m22;
        this.m20 = m6;
        this.m21 = m7;
        final float m8 = nm00 * m.m30 + nm4 * m.m31 + nm7 * m.m32 + tx;
        final float m9 = nm2 * m.m30 + nm5 * m.m31 + nm8 * m.m32 + ty;
        this.m32 = nm3 * m.m30 + nm6 * m.m31 + nm9 * m.m32 + tz;
        this.m30 = m8;
        this.m31 = m9;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f translationRotateScaleMul(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale, final Matrix4x3f m) {
        return this.translationRotateScaleMul(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z(), m);
    }
    
    public Matrix4x3f translationRotate(final float tx, final float ty, final float tz, final Quaternionfc quat) {
        final float dqx = quat.x() + quat.x();
        final float dqy = quat.y() + quat.y();
        final float dqz = quat.z() + quat.z();
        final float q00 = dqx * quat.x();
        final float q2 = dqy * quat.y();
        final float q3 = dqz * quat.z();
        final float q4 = dqx * quat.y();
        final float q5 = dqx * quat.z();
        final float q6 = dqx * quat.w();
        final float q7 = dqy * quat.z();
        final float q8 = dqy * quat.w();
        final float q9 = dqz * quat.w();
        this.m00 = 1.0f - (q2 + q3);
        this.m01 = q4 + q9;
        this.m02 = q5 - q8;
        this.m10 = q4 - q9;
        this.m11 = 1.0f - (q3 + q00);
        this.m12 = q7 + q6;
        this.m20 = q5 + q8;
        this.m21 = q7 - q6;
        this.m22 = 1.0f - (q2 + q00);
        this.m30 = tx;
        this.m31 = ty;
        this.m32 = tz;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f translationRotate(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw) {
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
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f translationRotate(final Vector3fc translation, final Quaternionfc quat) {
        return this.translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4x3f translationRotateMul(final float tx, final float ty, final float tz, final Quaternionfc quat, final Matrix4x3fc mat) {
        return this.translationRotateMul(tx, ty, tz, quat.x(), quat.y(), quat.z(), quat.w(), mat);
    }
    
    public Matrix4x3f translationRotateMul(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw, final Matrix4x3fc mat) {
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
        this.m00 = nm00 * mat.m00() + nm4 * mat.m01() + nm7 * mat.m02();
        this.m01 = nm2 * mat.m00() + nm5 * mat.m01() + nm8 * mat.m02();
        this.m02 = nm3 * mat.m00() + nm6 * mat.m01() + nm9 * mat.m02();
        this.m10 = nm00 * mat.m10() + nm4 * mat.m11() + nm7 * mat.m12();
        this.m11 = nm2 * mat.m10() + nm5 * mat.m11() + nm8 * mat.m12();
        this.m12 = nm3 * mat.m10() + nm6 * mat.m11() + nm9 * mat.m12();
        this.m20 = nm00 * mat.m20() + nm4 * mat.m21() + nm7 * mat.m22();
        this.m21 = nm2 * mat.m20() + nm5 * mat.m21() + nm8 * mat.m22();
        this.m22 = nm3 * mat.m20() + nm6 * mat.m21() + nm9 * mat.m22();
        this.m30 = nm00 * mat.m30() + nm4 * mat.m31() + nm7 * mat.m32() + tx;
        this.m31 = nm2 * mat.m30() + nm5 * mat.m31() + nm8 * mat.m32() + ty;
        this.m32 = nm3 * mat.m30() + nm6 * mat.m31() + nm9 * mat.m32() + tz;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f translationRotateInvert(final float tx, final float ty, final float tz, final float qx, final float qy, final float qz, final float qw) {
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
        return this._m00(1.0f - q2 - q3)._m01(q4 + q9)._m02(q5 - q8)._m10(q4 - q9)._m11(1.0f - q3 - q00)._m12(q7 + q6)._m20(q5 + q8)._m21(q7 - q6)._m22(1.0f - q2 - q00)._m30(-this.m00 * tx - this.m10 * ty - this.m20 * tz)._m31(-this.m01 * tx - this.m11 * ty - this.m21 * tz)._m32(-this.m02 * tx - this.m12 * ty - this.m22 * tz)._properties(16);
    }
    
    public Matrix4x3f translationRotateInvert(final Vector3fc translation, final Quaternionfc quat) {
        return this.translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4x3f set3x3(final Matrix3fc mat) {
        if (mat instanceof Matrix3f) {
            MemUtil.INSTANCE.copy3x3((Matrix3f)mat, this);
        }
        else {
            this.set3x3Matrix3fc(mat);
        }
        this.properties = 0;
        return this;
    }
    
    private void set3x3Matrix3fc(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
    }
    
    public Vector4f transform(final Vector4f v) {
        return v.mul(this);
    }
    
    public Vector4f transform(final Vector4fc v, final Vector4f dest) {
        return v.mul(this, dest);
    }
    
    public Vector3f transformPosition(final Vector3f v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20 * v.z + this.m30, this.m01 * v.x + this.m11 * v.y + this.m21 * v.z + this.m31, this.m02 * v.x + this.m12 * v.y + this.m22 * v.z + this.m32);
        return v;
    }
    
    public Vector3f transformPosition(final Vector3fc v, final Vector3f dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20 * v.z() + this.m30, this.m01 * v.x() + this.m11 * v.y() + this.m21 * v.z() + this.m31, this.m02 * v.x() + this.m12 * v.y() + this.m22 * v.z() + this.m32);
        return dest;
    }
    
    public Vector3f transformDirection(final Vector3f v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20 * v.z, this.m01 * v.x + this.m11 * v.y + this.m21 * v.z, this.m02 * v.x + this.m12 * v.y + this.m22 * v.z);
        return v;
    }
    
    public Vector3f transformDirection(final Vector3fc v, final Vector3f dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20 * v.z(), this.m01 * v.x() + this.m11 * v.y() + this.m21 * v.z(), this.m02 * v.x() + this.m12 * v.y() + this.m22 * v.z());
        return dest;
    }
    
    public Matrix4x3f scale(final Vector3fc xyz, final Matrix4x3f dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix4x3f scale(final Vector3fc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix4x3f scale(final float xyz, final Matrix4x3f dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix4x3f scale(final float xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix4x3f scaleXY(final float x, final float y, final Matrix4x3f dest) {
        return this.scale(x, y, 1.0f, dest);
    }
    
    public Matrix4x3f scaleXY(final float x, final float y) {
        return this.scale(x, y, 1.0f);
    }
    
    public Matrix4x3f scale(final float x, final float y, final float z, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleGeneric(x, y, z, dest);
    }
    
    private Matrix4x3f scaleGeneric(final float x, final float y, final float z, final Matrix4x3f dest) {
        dest.m00 = this.m00 * x;
        dest.m01 = this.m01 * x;
        dest.m02 = this.m02 * x;
        dest.m10 = this.m10 * y;
        dest.m11 = this.m11 * y;
        dest.m12 = this.m12 * y;
        dest.m20 = this.m20 * z;
        dest.m21 = this.m21 * z;
        dest.m22 = this.m22 * z;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f scale(final float x, final float y, final float z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix4x3f scaleLocal(final float x, final float y, final float z, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        final float nm00 = x * this.m00;
        final float nm2 = y * this.m01;
        final float nm3 = z * this.m02;
        final float nm4 = x * this.m10;
        final float nm5 = y * this.m11;
        final float nm6 = z * this.m12;
        final float nm7 = x * this.m20;
        final float nm8 = y * this.m21;
        final float nm9 = z * this.m22;
        final float nm10 = x * this.m30;
        final float nm11 = y * this.m31;
        final float nm12 = z * this.m32;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.m30 = nm10;
        dest.m31 = nm11;
        dest.m32 = nm12;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f scaleAround(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz, final Matrix4x3f dest) {
        final float nm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final float nm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final float nm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return dest._m00(this.m00 * sx)._m01(this.m01 * sx)._m02(this.m02 * sx)._m10(this.m10 * sy)._m11(this.m11 * sy)._m12(this.m12 * sy)._m20(this.m20 * sz)._m21(this.m21 * sz)._m22(this.m22 * sz)._m30(-dest.m00 * ox - dest.m10 * oy - dest.m20 * oz + nm30)._m31(-dest.m01 * ox - dest.m11 * oy - dest.m21 * oz + nm31)._m32(-dest.m02 * ox - dest.m12 * oy - dest.m22 * oz + nm32)._properties(this.properties & ~(0xC | (one ? 0 : 16)));
    }
    
    public Matrix4x3f scaleAround(final float sx, final float sy, final float sz, final float ox, final float oy, final float oz) {
        return this.scaleAround(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4x3f scaleAround(final float factor, final float ox, final float oy, final float oz) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4x3f scaleAround(final float factor, final float ox, final float oy, final float oz, final Matrix4x3f dest) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4x3f scaleLocal(final float x, final float y, final float z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix4x3f rotateX(final float ang, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationX(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30;
            final float y = this.m31;
            final float z = this.m32;
            return dest.rotationX(ang).setTranslation(x, y, z);
        }
        return this.rotateXInternal(ang, dest);
    }
    
    private Matrix4x3f rotateXInternal(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float rm11;
        final float cos = rm11 = Math.cosFromSin(sin, ang);
        final float rm12 = sin;
        final float rm13 = -sin;
        final float rm14 = cos;
        final float nm10 = this.m10 * rm11 + this.m20 * rm12;
        final float nm11 = this.m11 * rm11 + this.m21 * rm12;
        final float nm12 = this.m12 * rm11 + this.m22 * rm12;
        dest.m20 = this.m10 * rm13 + this.m20 * rm14;
        dest.m21 = this.m11 * rm13 + this.m21 * rm14;
        dest.m22 = this.m12 * rm13 + this.m22 * rm14;
        dest.m10 = nm10;
        dest.m11 = nm11;
        dest.m12 = nm12;
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateX(final float ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix4x3f rotateY(final float ang, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationY(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30;
            final float y = this.m31;
            final float z = this.m32;
            return dest.rotationY(ang).setTranslation(x, y, z);
        }
        return this.rotateYInternal(ang, dest);
    }
    
    private Matrix4x3f rotateYInternal(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float rm00;
        final float cos = rm00 = Math.cosFromSin(sin, ang);
        final float rm2 = -sin;
        final float rm3 = sin;
        final float rm4 = cos;
        final float nm00 = this.m00 * rm00 + this.m20 * rm2;
        final float nm2 = this.m01 * rm00 + this.m21 * rm2;
        final float nm3 = this.m02 * rm00 + this.m22 * rm2;
        dest.m20 = this.m00 * rm3 + this.m20 * rm4;
        dest.m21 = this.m01 * rm3 + this.m21 * rm4;
        dest.m22 = this.m02 * rm3 + this.m22 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateY(final float ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix4x3f rotateZ(final float ang, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZ(ang);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float x = this.m30;
            final float y = this.m31;
            final float z = this.m32;
            return dest.rotationZ(ang).setTranslation(x, y, z);
        }
        return this.rotateZInternal(ang, dest);
    }
    
    private Matrix4x3f rotateZInternal(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float rm00;
        final float cos = rm00 = Math.cosFromSin(sin, ang);
        final float rm2 = sin;
        final float rm3 = -sin;
        final float rm4 = cos;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2;
        dest.m10 = this.m00 * rm3 + this.m10 * rm4;
        dest.m11 = this.m01 * rm3 + this.m11 * rm4;
        dest.m12 = this.m02 * rm3 + this.m12 * rm4;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateZ(final float ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix4x3f rotateXYZ(final Vector3f angles) {
        return this.rotateXYZ(angles.x, angles.y, angles.z);
    }
    
    public Matrix4x3f rotateXYZ(final float angleX, final float angleY, final float angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4x3f rotateXYZ(final float angleX, final float angleY, final float angleZ, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30;
            final float ty = this.m31;
            final float tz = this.m32;
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4x3f rotateXYZInternal(final float angleX, final float angleY, final float angleZ, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateZYX(final Vector3f angles) {
        return this.rotateZYX(angles.z, angles.y, angles.x);
    }
    
    public Matrix4x3f rotateZYX(final float angleZ, final float angleY, final float angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4x3f rotateZYX(final float angleZ, final float angleY, final float angleX, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZYX(angleZ, angleY, angleX);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30;
            final float ty = this.m31;
            final float tz = this.m32;
            return dest.rotationZYX(angleZ, angleY, angleX).setTranslation(tx, ty, tz);
        }
        return this.rotateZYXInternal(angleZ, angleY, angleX, dest);
    }
    
    private Matrix4x3f rotateZYXInternal(final float angleZ, final float angleY, final float angleX, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateYXZ(final Vector3f angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix4x3f rotateYXZ(final float angleY, final float angleX, final float angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4x3f rotateYXZ(final float angleY, final float angleX, final float angleZ, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationYXZ(angleY, angleX, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final float tx = this.m30;
            final float ty = this.m31;
            final float tz = this.m32;
            return dest.rotationYXZ(angleY, angleX, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateYXZInternal(angleY, angleX, angleZ, dest);
    }
    
    private Matrix4x3f rotateYXZInternal(final float angleY, final float angleX, final float angleZ, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotate(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(ang, x, y, z);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(ang, x, y, z, dest);
        }
        return this.rotateGeneric(ang, x, y, z, dest);
    }
    
    private Matrix4x3f rotateGeneric(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
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
    
    private Matrix4x3f rotateGenericInternal(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotate(final float ang, final float x, final float y, final float z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix4x3f rotateTranslation(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
        final float tx = this.m30;
        final float ty = this.m31;
        final float tz = this.m32;
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
    
    private Matrix4x3f rotateTranslationInternal(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
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
        dest.m20 = rm7;
        dest.m21 = rm8;
        dest.m22 = rm9;
        dest.m00 = rm00;
        dest.m01 = rm2;
        dest.m02 = rm3;
        dest.m10 = rm4;
        dest.m11 = rm5;
        dest.m12 = rm6;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateAround(final Quaternionfc quat, final float ox, final float oy, final float oz) {
        return this.rotateAround(quat, ox, oy, oz, this);
    }
    
    private Matrix4x3f rotateAroundAffine(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4x3f dest) {
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
        final float tm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final float tm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final float tm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final float nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final float nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final float nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m10(nm4)._m11(nm5)._m12(nm6)._m30(-nm00 * ox - nm4 * oy - this.m20 * oz + tm30)._m31(-nm2 * ox - nm5 * oy - this.m21 * oz + tm31)._m32(-nm3 * ox - nm6 * oy - this.m22 * oz + tm32)._properties(this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateAround(final Quaternionfc quat, final float ox, final float oy, final float oz, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.rotationAround(quat, ox, oy, oz);
        }
        return this.rotateAroundAffine(quat, ox, oy, oz, dest);
    }
    
    public Matrix4x3f rotationAround(final Quaternionfc quat, final float ox, final float oy, final float oz) {
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
        this._m20(dyw + dxz);
        this._m21(dyz - dxw);
        this._m22(z2 - y2 - x2 + w2);
        this._m00(w2 + x2 - z2 - y2);
        this._m01(dxy + dzw);
        this._m02(dxz - dyw);
        this._m10(dxy - dzw);
        this._m11(y2 - z2 + w2 - x2);
        this._m12(dyz + dxw);
        this._m30(-this.m00 * ox - this.m10 * oy - this.m20 * oz + ox);
        this._m31(-this.m01 * ox - this.m11 * oy - this.m21 * oz + oy);
        this._m32(-this.m02 * ox - this.m12 * oy - this.m22 * oz + oz);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f rotateLocal(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotateLocalX(x * ang, dest);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotateLocalY(y * ang, dest);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotateLocalZ(z * ang, dest);
        }
        return this.rotateLocalInternal(ang, x, y, z, dest);
    }
    
    private Matrix4x3f rotateLocalInternal(final float ang, final float x, final float y, final float z, final Matrix4x3f dest) {
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
        final float nm10 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final float nm11 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final float nm12 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.m30 = nm10;
        dest.m31 = nm11;
        dest.m32 = nm12;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocal(final float ang, final float x, final float y, final float z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix4x3f rotateLocalX(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm01 = cos * this.m01 - sin * this.m02;
        final float nm2 = sin * this.m01 + cos * this.m02;
        final float nm3 = cos * this.m11 - sin * this.m12;
        final float nm4 = sin * this.m11 + cos * this.m12;
        final float nm5 = cos * this.m21 - sin * this.m22;
        final float nm6 = sin * this.m21 + cos * this.m22;
        final float nm7 = cos * this.m31 - sin * this.m32;
        final float nm8 = sin * this.m31 + cos * this.m32;
        dest._m00(this.m00)._m01(nm01)._m02(nm2)._m10(this.m10)._m11(nm3)._m12(nm4)._m20(this.m20)._m21(nm5)._m22(nm6)._m30(this.m30)._m31(nm7)._m32(nm8)._properties(this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocalX(final float ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix4x3f rotateLocalY(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = cos * this.m00 + sin * this.m02;
        final float nm2 = -sin * this.m00 + cos * this.m02;
        final float nm3 = cos * this.m10 + sin * this.m12;
        final float nm4 = -sin * this.m10 + cos * this.m12;
        final float nm5 = cos * this.m20 + sin * this.m22;
        final float nm6 = -sin * this.m20 + cos * this.m22;
        final float nm7 = cos * this.m30 + sin * this.m32;
        final float nm8 = -sin * this.m30 + cos * this.m32;
        dest._m00(nm00)._m01(this.m01)._m02(nm2)._m10(nm3)._m11(this.m11)._m12(nm4)._m20(nm5)._m21(this.m21)._m22(nm6)._m30(nm7)._m31(this.m31)._m32(nm8)._properties(this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocalY(final float ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix4x3f rotateLocalZ(final float ang, final Matrix4x3f dest) {
        final float sin = Math.sin(ang);
        final float cos = Math.cosFromSin(sin, ang);
        final float nm00 = cos * this.m00 - sin * this.m01;
        final float nm2 = sin * this.m00 + cos * this.m01;
        final float nm3 = cos * this.m10 - sin * this.m11;
        final float nm4 = sin * this.m10 + cos * this.m11;
        final float nm5 = cos * this.m20 - sin * this.m21;
        final float nm6 = sin * this.m20 + cos * this.m21;
        final float nm7 = cos * this.m30 - sin * this.m31;
        final float nm8 = sin * this.m30 + cos * this.m31;
        dest._m00(nm00)._m01(nm2)._m02(this.m02)._m10(nm3)._m11(nm4)._m12(this.m12)._m20(nm5)._m21(nm6)._m22(this.m22)._m30(nm7)._m31(nm8)._m32(this.m32)._properties(this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocalZ(final float ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public Matrix4x3f translate(final Vector3fc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3f translate(final Vector3fc offset, final Matrix4x3f dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3f translate(final float x, final float y, final float z, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateGeneric(x, y, z, dest);
    }
    
    private Matrix4x3f translateGeneric(final float x, final float y, final float z, final Matrix4x3f dest) {
        MemUtil.INSTANCE.copy(this, dest);
        dest.m30 = this.m00 * x + this.m10 * y + this.m20 * z + this.m30;
        dest.m31 = this.m01 * x + this.m11 * y + this.m21 * z + this.m31;
        dest.m32 = this.m02 * x + this.m12 * y + this.m22 * z + this.m32;
        dest.properties = (this.properties & 0xFFFFFFFB);
        return dest;
    }
    
    public Matrix4x3f translate(final float x, final float y, final float z) {
        if ((this.properties & 0x4) != 0x0) {
            return this.translation(x, y, z);
        }
        final Matrix4x3f c = this;
        c.m30 += c.m00 * x + c.m10 * y + c.m20 * z;
        c.m31 += c.m01 * x + c.m11 * y + c.m21 * z;
        c.m32 += c.m02 * x + c.m12 * y + c.m22 * z;
        final Matrix4x3f matrix4x3f = c;
        matrix4x3f.properties &= 0xFFFFFFFB;
        return this;
    }
    
    public Matrix4x3f translateLocal(final Vector3fc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3f translateLocal(final Vector3fc offset, final Matrix4x3f dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3f translateLocal(final float x, final float y, final float z, final Matrix4x3f dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        dest.m30 = this.m30 + x;
        dest.m31 = this.m31 + y;
        dest.m32 = this.m32 + z;
        dest.properties = (this.properties & 0xFFFFFFFB);
        return dest;
    }
    
    public Matrix4x3f translateLocal(final float x, final float y, final float z) {
        return this.translateLocal(x, y, z, this);
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
        out.writeFloat(this.m30);
        out.writeFloat(this.m31);
        out.writeFloat(this.m32);
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
        this.m30 = in.readFloat();
        this.m31 = in.readFloat();
        this.m32 = in.readFloat();
        this.determineProperties();
    }
    
    public Matrix4x3f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4x3f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        final float rm4 = (left + right) / (left - right);
        final float rm5 = (top + bottom) / (bottom - top);
        final float rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest.m30 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6 + this.m30;
        dest.m31 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6 + this.m31;
        dest.m32 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = this.m20 * rm3;
        dest.m21 = this.m21 * rm3;
        dest.m22 = this.m22 * rm3;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4x3f dest) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4x3f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.ortho(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3f ortho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4x3f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        final float rm4 = (left + right) / (left - right);
        final float rm5 = (top + bottom) / (bottom - top);
        final float rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest.m30 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6 + this.m30;
        dest.m31 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6 + this.m31;
        dest.m32 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = this.m20 * rm3;
        dest.m21 = this.m21 * rm3;
        dest.m22 = this.m22 * rm3;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final Matrix4x3f dest) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4x3f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3f setOrtho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / (right - left);
        this.m11 = 2.0f / (top - bottom);
        this.m22 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        this.m30 = (right + left) / (left - right);
        this.m31 = (top + bottom) / (bottom - top);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f setOrtho(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setOrtho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3f setOrthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / (right - left);
        this.m11 = 2.0f / (top - bottom);
        this.m22 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        this.m30 = (right + left) / (left - right);
        this.m31 = (top + bottom) / (bottom - top);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f setOrthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
        return this.setOrthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4x3f dest) {
        final float rm00 = 2.0f / width;
        final float rm2 = 2.0f / height;
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        final float rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest.m30 = this.m20 * rm4 + this.m30;
        dest.m31 = this.m21 * rm4 + this.m31;
        dest.m32 = this.m22 * rm4 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = this.m20 * rm3;
        dest.m21 = this.m21 * rm3;
        dest.m22 = this.m22 * rm3;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final Matrix4x3f dest) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4x3f orthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoSymmetric(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3f orthoSymmetric(final float width, final float height, final float zNear, final float zFar) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4x3f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne, final Matrix4x3f dest) {
        final float rm00 = 2.0f / width;
        final float rm2 = 2.0f / height;
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        final float rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        dest.m30 = this.m20 * rm4 + this.m30;
        dest.m31 = this.m21 * rm4 + this.m31;
        dest.m32 = this.m22 * rm4 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = this.m20 * rm3;
        dest.m21 = this.m21 * rm3;
        dest.m22 = this.m22 * rm3;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final Matrix4x3f dest) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4x3f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3f orthoSymmetricLH(final float width, final float height, final float zNear, final float zFar) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4x3f setOrthoSymmetric(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / width;
        this.m11 = 2.0f / height;
        this.m22 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f setOrthoSymmetric(final float width, final float height, final float zNear, final float zFar) {
        return this.setOrthoSymmetric(width, height, zNear, zFar, false);
    }
    
    public Matrix4x3f setOrthoSymmetricLH(final float width, final float height, final float zNear, final float zFar, final boolean zZeroToOne) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / width;
        this.m11 = 2.0f / height;
        this.m22 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f setOrthoSymmetricLH(final float width, final float height, final float zNear, final float zFar) {
        return this.setOrthoSymmetricLH(width, height, zNear, zFar, false);
    }
    
    public Matrix4x3f ortho2D(final float left, final float right, final float bottom, final float top, final Matrix4x3f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = -(right + left) / (right - left);
        final float rm4 = -(top + bottom) / (top - bottom);
        dest.m30 = this.m00 * rm3 + this.m10 * rm4 + this.m30;
        dest.m31 = this.m01 * rm3 + this.m11 * rm4 + this.m31;
        dest.m32 = this.m02 * rm3 + this.m12 * rm4 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = -this.m20;
        dest.m21 = -this.m21;
        dest.m22 = -this.m22;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f ortho2D(final float left, final float right, final float bottom, final float top) {
        return this.ortho2D(left, right, bottom, top, this);
    }
    
    public Matrix4x3f ortho2DLH(final float left, final float right, final float bottom, final float top, final Matrix4x3f dest) {
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = -(right + left) / (right - left);
        final float rm4 = -(top + bottom) / (top - bottom);
        dest.m30 = this.m00 * rm3 + this.m10 * rm4 + this.m30;
        dest.m31 = this.m01 * rm3 + this.m11 * rm4 + this.m31;
        dest.m32 = this.m02 * rm3 + this.m12 * rm4 + this.m32;
        dest.m00 = this.m00 * rm00;
        dest.m01 = this.m01 * rm00;
        dest.m02 = this.m02 * rm00;
        dest.m10 = this.m10 * rm2;
        dest.m11 = this.m11 * rm2;
        dest.m12 = this.m12 * rm2;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f ortho2DLH(final float left, final float right, final float bottom, final float top) {
        return this.ortho2DLH(left, right, bottom, top, this);
    }
    
    public Matrix4x3f setOrtho2D(final float left, final float right, final float bottom, final float top) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / (right - left);
        this.m11 = 2.0f / (top - bottom);
        this.m22 = -1.0f;
        this.m30 = -(right + left) / (right - left);
        this.m31 = -(top + bottom) / (top - bottom);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f setOrtho2DLH(final float left, final float right, final float bottom, final float top) {
        MemUtil.INSTANCE.identity(this);
        this.m00 = 2.0f / (right - left);
        this.m11 = 2.0f / (top - bottom);
        this.m22 = 1.0f;
        this.m30 = -(right + left) / (right - left);
        this.m31 = -(top + bottom) / (top - bottom);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f lookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3f lookAlong(final Vector3fc dir, final Vector3fc up, final Matrix4x3f dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3f lookAlong(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.setLookAlong(dirX, dirY, dirZ, upX, upY, upZ);
        }
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f lookAlong(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3f setLookAlong(final Vector3fc dir, final Vector3fc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f setLookAlong(float dirX, float dirY, float dirZ, final float upX, final float upY, final float upZ) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f setLookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.setLookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f setLookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
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
        this.m00 = leftX;
        this.m01 = upnX;
        this.m02 = dirX;
        this.m10 = leftY;
        this.m11 = upnY;
        this.m12 = dirY;
        this.m20 = leftZ;
        this.m21 = upnZ;
        this.m22 = dirZ;
        this.m30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        this.m31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        this.m32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f lookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up, final Matrix4x3f dest) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3f lookAt(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3f lookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        return this.lookAtGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4x3f lookAtGeneric(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
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
        final float rm00 = leftX;
        final float rm2 = upnX;
        final float rm3 = dirX;
        final float rm4 = leftY;
        final float rm5 = upnY;
        final float rm6 = dirY;
        final float rm7 = leftZ;
        final float rm8 = upnZ;
        final float rm9 = dirZ;
        final float rm10 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm11 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm12 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f lookAt(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        return this.lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3f setLookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.setLookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f setLookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
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
        this.m00 = leftX;
        this.m01 = upnX;
        this.m02 = dirX;
        this.m10 = leftY;
        this.m11 = upnY;
        this.m12 = dirY;
        this.m20 = leftZ;
        this.m21 = upnZ;
        this.m22 = dirZ;
        this.m30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        this.m31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        this.m32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f lookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up, final Matrix4x3f dest) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3f lookAtLH(final Vector3fc eye, final Vector3fc center, final Vector3fc up) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3f lookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        return this.lookAtLHGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4x3f lookAtLHGeneric(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
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
        final float rm00 = leftX;
        final float rm2 = upnX;
        final float rm3 = dirX;
        final float rm4 = leftY;
        final float rm5 = upnY;
        final float rm6 = dirY;
        final float rm7 = leftZ;
        final float rm8 = upnZ;
        final float rm9 = dirZ;
        final float rm10 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ);
        final float rm11 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ);
        final float rm12 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ);
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f lookAtLH(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY, final float centerZ, final float upX, final float upY, final float upZ) {
        return this.lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3f rotate(final Quaternionfc quat, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(quat);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(quat, dest);
        }
        return this.rotateGeneric(quat, dest);
    }
    
    private Matrix4x3f rotateGeneric(final Quaternionfc quat, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4x3f rotateTranslation(final Quaternionfc quat, final Matrix4x3f dest) {
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
        dest.m20 = rm7;
        dest.m21 = rm8;
        dest.m22 = rm9;
        dest.m00 = rm00;
        dest.m01 = rm2;
        dest.m02 = rm3;
        dest.m10 = rm4;
        dest.m11 = rm5;
        dest.m12 = rm6;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocal(final Quaternionfc quat, final Matrix4x3f dest) {
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
        final float nm00 = lm00 * this.m00 + lm4 * this.m01 + lm7 * this.m02;
        final float nm2 = lm2 * this.m00 + lm5 * this.m01 + lm8 * this.m02;
        final float nm3 = lm3 * this.m00 + lm6 * this.m01 + lm9 * this.m02;
        final float nm4 = lm00 * this.m10 + lm4 * this.m11 + lm7 * this.m12;
        final float nm5 = lm2 * this.m10 + lm5 * this.m11 + lm8 * this.m12;
        final float nm6 = lm3 * this.m10 + lm6 * this.m11 + lm9 * this.m12;
        final float nm7 = lm00 * this.m20 + lm4 * this.m21 + lm7 * this.m22;
        final float nm8 = lm2 * this.m20 + lm5 * this.m21 + lm8 * this.m22;
        final float nm9 = lm3 * this.m20 + lm6 * this.m21 + lm9 * this.m22;
        final float nm10 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final float nm11 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final float nm12 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.m30 = nm10;
        dest.m31 = nm11;
        dest.m32 = nm12;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4x3f rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4x3f rotate(final AxisAngle4f axisAngle, final Matrix4x3f dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4x3f rotate(final float angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4x3f rotate(final float angle, final Vector3fc axis, final Matrix4x3f dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Matrix4x3f reflect(final float a, final float b, final float c, final float d, final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.reflection(a, b, c, d);
        }
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
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f reflect(final float a, final float b, final float c, final float d) {
        return this.reflect(a, b, c, d, this);
    }
    
    public Matrix4x3f reflect(final float nx, final float ny, final float nz, final float px, final float py, final float pz) {
        return this.reflect(nx, ny, nz, px, py, pz, this);
    }
    
    public Matrix4x3f reflect(final float nx, final float ny, final float nz, final float px, final float py, final float pz, final Matrix4x3f dest) {
        final float invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final float nnx = nx * invLength;
        final float nny = ny * invLength;
        final float nnz = nz * invLength;
        return this.reflect(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz, dest);
    }
    
    public Matrix4x3f reflect(final Vector3fc normal, final Vector3fc point) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4x3f reflect(final Quaternionfc orientation, final Vector3fc point) {
        return this.reflect(orientation, point, this);
    }
    
    public Matrix4x3f reflect(final Quaternionfc orientation, final Vector3fc point, final Matrix4x3f dest) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final float normalX = (float)(orientation.x() * num3 + orientation.w() * num2);
        final float normalY = (float)(orientation.y() * num3 - orientation.w() * num1);
        final float normalZ = (float)(1.0 - (orientation.x() * num1 + orientation.y() * num2));
        return this.reflect(normalX, normalY, normalZ, point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4x3f reflect(final Vector3fc normal, final Vector3fc point, final Matrix4x3f dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4x3f reflection(final float a, final float b, final float c, final float d) {
        final float da = a + a;
        final float db = b + b;
        final float dc = c + c;
        final float dd = d + d;
        this.m00 = 1.0f - da * a;
        this.m01 = -da * b;
        this.m02 = -da * c;
        this.m10 = -db * a;
        this.m11 = 1.0f - db * b;
        this.m12 = -db * c;
        this.m20 = -dc * a;
        this.m21 = -dc * b;
        this.m22 = 1.0f - dc * c;
        this.m30 = -dd * a;
        this.m31 = -dd * b;
        this.m32 = -dd * c;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f reflection(final float nx, final float ny, final float nz, final float px, final float py, final float pz) {
        final float invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final float nnx = nx * invLength;
        final float nny = ny * invLength;
        final float nnz = nz * invLength;
        return this.reflection(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz);
    }
    
    public Matrix4x3f reflection(final Vector3fc normal, final Vector3fc point) {
        return this.reflection(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4x3f reflection(final Quaternionfc orientation, final Vector3fc point) {
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
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix4x3f setRow(final int row, final Vector4fc src) throws IndexOutOfBoundsException {
        switch (row) {
            case 0: {
                this.m00 = src.x();
                this.m10 = src.y();
                this.m20 = src.z();
                this.m30 = src.w();
                break;
            }
            case 1: {
                this.m01 = src.x();
                this.m11 = src.y();
                this.m21 = src.z();
                this.m31 = src.w();
                break;
            }
            case 2: {
                this.m02 = src.x();
                this.m12 = src.y();
                this.m22 = src.z();
                this.m32 = src.w();
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        this.properties = 0;
        return this;
    }
    
    public Vector3f getColumn(final int column, final Vector3f dest) throws IndexOutOfBoundsException {
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
    
    public Matrix4x3f setColumn(final int column, final Vector3fc src) throws IndexOutOfBoundsException {
        switch (column) {
            case 0: {
                this.m00 = src.x();
                this.m01 = src.y();
                this.m02 = src.z();
                break;
            }
            case 1: {
                this.m10 = src.x();
                this.m11 = src.y();
                this.m12 = src.z();
                break;
            }
            case 2: {
                this.m20 = src.x();
                this.m21 = src.y();
                this.m22 = src.z();
                break;
            }
            case 3: {
                this.m30 = src.x();
                this.m31 = src.y();
                this.m32 = src.z();
                break;
            }
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f normal() {
        return this.normal(this);
    }
    
    public Matrix4x3f normal(final Matrix4x3f dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix4x3f normalOrthonormal(final Matrix4x3f dest) {
        if (dest != this) {
            dest.set(this);
        }
        return dest._properties(16);
    }
    
    private Matrix4x3f normalGeneric(final Matrix4x3f dest) {
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
        dest.m30 = 0.0f;
        dest.m31 = 0.0f;
        dest.m32 = 0.0f;
        dest.properties = (this.properties & 0xFFFFFFF7);
        return dest;
    }
    
    public Matrix3f normal(final Matrix3f dest) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix3f normalOrthonormal(final Matrix3f dest) {
        return dest.set(this);
    }
    
    private Matrix3f normalGeneric(final Matrix3f dest) {
        final float m00m11 = this.m00 * this.m11;
        final float m01m10 = this.m01 * this.m10;
        final float m02m10 = this.m02 * this.m10;
        final float m00m12 = this.m00 * this.m12;
        final float m01m11 = this.m01 * this.m12;
        final float m02m11 = this.m02 * this.m11;
        final float det = (m00m11 - m01m10) * this.m22 + (m02m10 - m00m12) * this.m21 + (m01m11 - m02m11) * this.m20;
        final float s = 1.0f / det;
        dest.m00((this.m11 * this.m22 - this.m21 * this.m12) * s);
        dest.m01((this.m20 * this.m12 - this.m10 * this.m22) * s);
        dest.m02((this.m10 * this.m21 - this.m20 * this.m11) * s);
        dest.m10((this.m21 * this.m02 - this.m01 * this.m22) * s);
        dest.m11((this.m00 * this.m22 - this.m20 * this.m02) * s);
        dest.m12((this.m20 * this.m01 - this.m00 * this.m21) * s);
        dest.m20((m01m11 - m02m11) * s);
        dest.m21((m02m10 - m00m12) * s);
        dest.m22((m00m11 - m01m10) * s);
        return dest;
    }
    
    public Matrix4x3f cofactor3x3() {
        return this.cofactor3x3(this);
    }
    
    public Matrix3f cofactor3x3(final Matrix3f dest) {
        dest.m00 = this.m11 * this.m22 - this.m21 * this.m12;
        dest.m01 = this.m20 * this.m12 - this.m10 * this.m22;
        dest.m02 = this.m10 * this.m21 - this.m20 * this.m11;
        dest.m10 = this.m21 * this.m02 - this.m01 * this.m22;
        dest.m11 = this.m00 * this.m22 - this.m20 * this.m02;
        dest.m12 = this.m20 * this.m01 - this.m00 * this.m21;
        dest.m20 = this.m01 * this.m12 - this.m02 * this.m11;
        dest.m21 = this.m02 * this.m10 - this.m00 * this.m12;
        dest.m22 = this.m00 * this.m11 - this.m01 * this.m10;
        return dest;
    }
    
    public Matrix4x3f cofactor3x3(final Matrix4x3f dest) {
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
        dest.m30 = 0.0f;
        dest.m31 = 0.0f;
        dest.m32 = 0.0f;
        dest.properties = (this.properties & 0xFFFFFFF7);
        return dest;
    }
    
    public Matrix4x3f normalize3x3() {
        return this.normalize3x3(this);
    }
    
    public Matrix4x3f normalize3x3(final Matrix4x3f dest) {
        final float invXlen = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        final float invYlen = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        final float invZlen = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
        dest.m00 = this.m00 * invXlen;
        dest.m01 = this.m01 * invXlen;
        dest.m02 = this.m02 * invXlen;
        dest.m10 = this.m10 * invYlen;
        dest.m11 = this.m11 * invYlen;
        dest.m12 = this.m12 * invYlen;
        dest.m20 = this.m20 * invZlen;
        dest.m21 = this.m21 * invZlen;
        dest.m22 = this.m22 * invZlen;
        dest.properties = this.properties;
        return dest;
    }
    
    public Matrix3f normalize3x3(final Matrix3f dest) {
        final float invXlen = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        final float invYlen = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        final float invZlen = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
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
    
    public Vector4f frustumPlane(final int which, final Vector4f dest) {
        switch (which) {
            case 0: {
                dest.set(this.m00, this.m10, this.m20, 1.0f + this.m30).normalize();
                break;
            }
            case 1: {
                dest.set(-this.m00, -this.m10, -this.m20, 1.0f - this.m30).normalize();
                break;
            }
            case 2: {
                dest.set(this.m01, this.m11, this.m21, 1.0f + this.m31).normalize();
                break;
            }
            case 3: {
                dest.set(-this.m01, -this.m11, -this.m21, 1.0f - this.m31).normalize();
                break;
            }
            case 4: {
                dest.set(this.m02, this.m12, this.m22, 1.0f + this.m32).normalize();
                break;
            }
            case 5: {
                dest.set(-this.m02, -this.m12, -this.m22, 1.0f - this.m32).normalize();
                break;
            }
            default: {
                throw new IllegalArgumentException("which");
            }
        }
        return dest;
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
    
    public Vector3f origin(final Vector3f origin) {
        final float a = this.m00 * this.m11 - this.m01 * this.m10;
        final float b = this.m00 * this.m12 - this.m02 * this.m10;
        final float d = this.m01 * this.m12 - this.m02 * this.m11;
        final float g = this.m20 * this.m31 - this.m21 * this.m30;
        final float h = this.m20 * this.m32 - this.m22 * this.m30;
        final float j = this.m21 * this.m32 - this.m22 * this.m31;
        origin.x = -this.m10 * j + this.m11 * h - this.m12 * g;
        origin.y = this.m00 * j - this.m01 * h + this.m02 * g;
        origin.z = -this.m30 * d + this.m31 * b - this.m32 * a;
        return origin;
    }
    
    public Matrix4x3f shadow(final Vector4fc light, final float a, final float b, final float c, final float d) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, this);
    }
    
    public Matrix4x3f shadow(final Vector4fc light, final float a, final float b, final float c, final float d, final Matrix4x3f dest) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4x3f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final float a, final float b, final float c, final float d) {
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, this);
    }
    
    public Matrix4x3f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final float a, final float b, final float c, final float d, final Matrix4x3f dest) {
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
        final float nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3 + this.m30 * rm4;
        final float nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3 + this.m31 * rm4;
        final float nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3 + this.m32 * rm4;
        final float nm4 = this.m00 * rm5 + this.m10 * rm6 + this.m20 * rm7 + this.m30 * rm8;
        final float nm5 = this.m01 * rm5 + this.m11 * rm6 + this.m21 * rm7 + this.m31 * rm8;
        final float nm6 = this.m02 * rm5 + this.m12 * rm6 + this.m22 * rm7 + this.m32 * rm8;
        final float nm7 = this.m00 * rm9 + this.m10 * rm10 + this.m20 * rm11 + this.m30 * rm12;
        final float nm8 = this.m01 * rm9 + this.m11 * rm10 + this.m21 * rm11 + this.m31 * rm12;
        final float nm9 = this.m02 * rm9 + this.m12 * rm10 + this.m22 * rm11 + this.m32 * rm12;
        dest.m30 = this.m00 * rm13 + this.m10 * rm14 + this.m20 * rm15 + this.m30 * rm16;
        dest.m31 = this.m01 * rm13 + this.m11 * rm14 + this.m21 * rm15 + this.m31 * rm16;
        dest.m32 = this.m02 * rm13 + this.m12 * rm14 + this.m22 * rm15 + this.m32 * rm16;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = nm3;
        dest.m10 = nm4;
        dest.m11 = nm5;
        dest.m12 = nm6;
        dest.m20 = nm7;
        dest.m21 = nm8;
        dest.m22 = nm9;
        dest.properties = (this.properties & 0xFFFFFFE3);
        return dest;
    }
    
    public Matrix4x3f shadow(final Vector4fc light, final Matrix4x3fc planeTransform, final Matrix4x3f dest) {
        final float a = planeTransform.m10();
        final float b = planeTransform.m11();
        final float c = planeTransform.m12();
        final float d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4x3f shadow(final Vector4fc light, final Matrix4x3fc planeTransform) {
        return this.shadow(light, planeTransform, this);
    }
    
    public Matrix4x3f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final Matrix4x3fc planeTransform, final Matrix4x3f dest) {
        final float a = planeTransform.m10();
        final float b = planeTransform.m11();
        final float c = planeTransform.m12();
        final float d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, dest);
    }
    
    public Matrix4x3f shadow(final float lightX, final float lightY, final float lightZ, final float lightW, final Matrix4x3f planeTransform) {
        return this.shadow(lightX, lightY, lightZ, lightW, planeTransform, this);
    }
    
    public Matrix4x3f billboardCylindrical(final Vector3fc objPos, final Vector3fc targetPos, final Vector3fc up) {
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
        this.m00 = leftX;
        this.m01 = leftY;
        this.m02 = leftZ;
        this.m10 = up.x();
        this.m11 = up.y();
        this.m12 = up.z();
        this.m20 = dirX;
        this.m21 = dirY;
        this.m22 = dirZ;
        this.m30 = objPos.x();
        this.m31 = objPos.y();
        this.m32 = objPos.z();
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f billboardSpherical(final Vector3fc objPos, final Vector3fc targetPos, final Vector3fc up) {
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
        this.m00 = leftX;
        this.m01 = leftY;
        this.m02 = leftZ;
        this.m10 = upX;
        this.m11 = upY;
        this.m12 = upZ;
        this.m20 = dirX;
        this.m21 = dirY;
        this.m22 = dirZ;
        this.m30 = objPos.x();
        this.m31 = objPos.y();
        this.m32 = objPos.z();
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f billboardSpherical(final Vector3fc objPos, final Vector3fc targetPos) {
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
        this.m00 = 1.0f - q2;
        this.m01 = q3;
        this.m02 = -q5;
        this.m10 = q3;
        this.m11 = 1.0f - q00;
        this.m12 = q4;
        this.m20 = q5;
        this.m21 = -q4;
        this.m22 = 1.0f - q2 - q00;
        this.m30 = objPos.x();
        this.m31 = objPos.y();
        this.m32 = objPos.z();
        this.properties = 16;
        return this;
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
        result = 31 * result + Float.floatToIntBits(this.m30);
        result = 31 * result + Float.floatToIntBits(this.m31);
        result = 31 * result + Float.floatToIntBits(this.m32);
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Matrix4x3f)) {
            return false;
        }
        final Matrix4x3f other = (Matrix4x3f)obj;
        return Float.floatToIntBits(this.m00) == Float.floatToIntBits(other.m00) && Float.floatToIntBits(this.m01) == Float.floatToIntBits(other.m01) && Float.floatToIntBits(this.m02) == Float.floatToIntBits(other.m02) && Float.floatToIntBits(this.m10) == Float.floatToIntBits(other.m10) && Float.floatToIntBits(this.m11) == Float.floatToIntBits(other.m11) && Float.floatToIntBits(this.m12) == Float.floatToIntBits(other.m12) && Float.floatToIntBits(this.m20) == Float.floatToIntBits(other.m20) && Float.floatToIntBits(this.m21) == Float.floatToIntBits(other.m21) && Float.floatToIntBits(this.m22) == Float.floatToIntBits(other.m22) && Float.floatToIntBits(this.m30) == Float.floatToIntBits(other.m30) && Float.floatToIntBits(this.m31) == Float.floatToIntBits(other.m31) && Float.floatToIntBits(this.m32) == Float.floatToIntBits(other.m32);
    }
    
    public boolean equals(final Matrix4x3fc m, final float delta) {
        return this == m || (m != null && m instanceof Matrix4x3f && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m02, m.m02(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m12, m.m12(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta) && Runtime.equals(this.m22, m.m22(), delta) && Runtime.equals(this.m30, m.m30(), delta) && Runtime.equals(this.m31, m.m31(), delta) && Runtime.equals(this.m32, m.m32(), delta));
    }
    
    public Matrix4x3f pick(final float x, final float y, final float width, final float height, final int[] viewport, final Matrix4x3f dest) {
        final float sx = viewport[2] / width;
        final float sy = viewport[3] / height;
        final float tx = (viewport[2] + 2.0f * (viewport[0] - x)) / width;
        final float ty = (viewport[3] + 2.0f * (viewport[1] - y)) / height;
        dest.m30 = this.m00 * tx + this.m10 * ty + this.m30;
        dest.m31 = this.m01 * tx + this.m11 * ty + this.m31;
        dest.m32 = this.m02 * tx + this.m12 * ty + this.m32;
        dest.m00 = this.m00 * sx;
        dest.m01 = this.m01 * sx;
        dest.m02 = this.m02 * sx;
        dest.m10 = this.m10 * sy;
        dest.m11 = this.m11 * sy;
        dest.m12 = this.m12 * sy;
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f pick(final float x, final float y, final float width, final float height, final int[] viewport) {
        return this.pick(x, y, width, height, viewport, this);
    }
    
    public Matrix4x3f swap(final Matrix4x3f other) {
        MemUtil.INSTANCE.swap(this, other);
        final int props = this.properties;
        this.properties = other.properties;
        other.properties = props;
        return this;
    }
    
    public Matrix4x3f arcball(final float radius, final float centerX, final float centerY, final float centerZ, final float angleX, final float angleY, final Matrix4x3f dest) {
        final float m30 = this.m20 * -radius + this.m30;
        final float m31 = this.m21 * -radius + this.m31;
        final float m32 = this.m22 * -radius + this.m32;
        float sin = Math.sin(angleX);
        float cos = Math.cosFromSin(sin, angleX);
        final float nm10 = this.m10 * cos + this.m20 * sin;
        final float nm11 = this.m11 * cos + this.m21 * sin;
        final float nm12 = this.m12 * cos + this.m22 * sin;
        final float m33 = this.m20 * cos - this.m10 * sin;
        final float m34 = this.m21 * cos - this.m11 * sin;
        final float m35 = this.m22 * cos - this.m12 * sin;
        sin = Math.sin(angleY);
        cos = Math.cosFromSin(sin, angleY);
        final float nm13 = this.m00 * cos - m33 * sin;
        final float nm14 = this.m01 * cos - m34 * sin;
        final float nm15 = this.m02 * cos - m35 * sin;
        final float nm16 = this.m00 * sin + m33 * cos;
        final float nm17 = this.m01 * sin + m34 * cos;
        final float nm18 = this.m02 * sin + m35 * cos;
        dest.m30 = -nm13 * centerX - nm10 * centerY - nm16 * centerZ + m30;
        dest.m31 = -nm14 * centerX - nm11 * centerY - nm17 * centerZ + m31;
        dest.m32 = -nm15 * centerX - nm12 * centerY - nm18 * centerZ + m32;
        dest.m20 = nm16;
        dest.m21 = nm17;
        dest.m22 = nm18;
        dest.m10 = nm10;
        dest.m11 = nm11;
        dest.m12 = nm12;
        dest.m00 = nm13;
        dest.m01 = nm14;
        dest.m02 = nm15;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f arcball(final float radius, final Vector3fc center, final float angleX, final float angleY, final Matrix4x3f dest) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, dest);
    }
    
    public Matrix4x3f arcball(final float radius, final float centerX, final float centerY, final float centerZ, final float angleX, final float angleY) {
        return this.arcball(radius, centerX, centerY, centerZ, angleX, angleY, this);
    }
    
    public Matrix4x3f arcball(final float radius, final Vector3fc center, final float angleX, final float angleY) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, this);
    }
    
    public Matrix4x3f transformAab(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final Vector3f outMin, final Vector3f outMax) {
        final float xax = this.m00 * minX;
        final float xay = this.m01 * minX;
        final float xaz = this.m02 * minX;
        final float xbx = this.m00 * maxX;
        final float xby = this.m01 * maxX;
        final float xbz = this.m02 * maxX;
        final float yax = this.m10 * minY;
        final float yay = this.m11 * minY;
        final float yaz = this.m12 * minY;
        final float ybx = this.m10 * maxY;
        final float yby = this.m11 * maxY;
        final float ybz = this.m12 * maxY;
        final float zax = this.m20 * minZ;
        final float zay = this.m21 * minZ;
        final float zaz = this.m22 * minZ;
        final float zbx = this.m20 * maxZ;
        final float zby = this.m21 * maxZ;
        final float zbz = this.m22 * maxZ;
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
        outMin.x = xminx + yminx + zminx + this.m30;
        outMin.y = xminy + yminy + zminy + this.m31;
        outMin.z = xminz + yminz + zminz + this.m32;
        outMax.x = xmaxx + ymaxx + zmaxx + this.m30;
        outMax.y = xmaxy + ymaxy + zmaxy + this.m31;
        outMax.z = xmaxz + ymaxz + zmaxz + this.m32;
        return this;
    }
    
    public Matrix4x3f transformAab(final Vector3fc min, final Vector3fc max, final Vector3f outMin, final Vector3f outMax) {
        return this.transformAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), outMin, outMax);
    }
    
    public Matrix4x3f lerp(final Matrix4x3fc other, final float t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix4x3f lerp(final Matrix4x3fc other, final float t, final Matrix4x3f dest) {
        dest.m00 = Math.fma(other.m00() - this.m00, t, this.m00);
        dest.m01 = Math.fma(other.m01() - this.m01, t, this.m01);
        dest.m02 = Math.fma(other.m02() - this.m02, t, this.m02);
        dest.m10 = Math.fma(other.m10() - this.m10, t, this.m10);
        dest.m11 = Math.fma(other.m11() - this.m11, t, this.m11);
        dest.m12 = Math.fma(other.m12() - this.m12, t, this.m12);
        dest.m20 = Math.fma(other.m20() - this.m20, t, this.m20);
        dest.m21 = Math.fma(other.m21() - this.m21, t, this.m21);
        dest.m22 = Math.fma(other.m22() - this.m22, t, this.m22);
        dest.m30 = Math.fma(other.m30() - this.m30, t, this.m30);
        dest.m31 = Math.fma(other.m31() - this.m31, t, this.m31);
        dest.m32 = Math.fma(other.m32() - this.m32, t, this.m32);
        dest.properties = (this.properties & other.properties());
        return dest;
    }
    
    public Matrix4x3f rotateTowards(final Vector3fc dir, final Vector3fc up, final Matrix4x3f dest) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3f rotateTowards(final Vector3fc dir, final Vector3fc up) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3f rotateTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f rotationTowards(final Vector3fc dir, final Vector3fc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f rotationTowards(final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3f translationRotateTowards(final Vector3fc pos, final Vector3fc dir, final Vector3fc up) {
        return this.translationRotateTowards(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f translationRotateTowards(final float posX, final float posY, final float posZ, final float dirX, final float dirY, final float dirZ, final float upX, final float upY, final float upZ) {
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
        this.m30 = posX;
        this.m31 = posY;
        this.m32 = posZ;
        this.properties = 16;
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
    
    public Matrix4x3f obliqueZ(final float a, final float b) {
        this.m20 += this.m00 * a + this.m10 * b;
        this.m21 += this.m01 * a + this.m11 * b;
        this.m22 += this.m02 * a + this.m12 * b;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3f obliqueZ(final float a, final float b, final Matrix4x3f dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m20 = this.m00 * a + this.m10 * b + this.m20;
        dest.m21 = this.m01 * a + this.m11 * b + this.m21;
        dest.m22 = this.m02 * a + this.m12 * b + this.m22;
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3f withLookAtUp(final Vector3fc up) {
        return this.withLookAtUp(up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3f withLookAtUp(final Vector3fc up, final Matrix4x3f dest) {
        return this.withLookAtUp(up.x(), up.y(), up.z());
    }
    
    public Matrix4x3f withLookAtUp(final float upX, final float upY, final float upZ) {
        return this.withLookAtUp(upX, upY, upZ, this);
    }
    
    public Matrix4x3f withLookAtUp(final float upX, final float upY, final float upZ, final Matrix4x3f dest) {
        final float y = (upY * this.m21 - upZ * this.m11) * this.m02 + (upZ * this.m01 - upX * this.m21) * this.m12 + (upX * this.m11 - upY * this.m01) * this.m22;
        float x = upX * this.m01 + upY * this.m11 + upZ * this.m21;
        if ((this.properties & 0x10) == 0x0) {
            x *= Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
        }
        final float invsqrt = Math.invsqrt(y * y + x * x);
        final float c = x * invsqrt;
        final float s = y * invsqrt;
        final float nm00 = c * this.m00 - s * this.m01;
        final float nm2 = c * this.m10 - s * this.m11;
        final float nm3 = c * this.m20 - s * this.m21;
        final float nm4 = s * this.m30 + c * this.m31;
        final float nm5 = s * this.m00 + c * this.m01;
        final float nm6 = s * this.m10 + c * this.m11;
        final float nm7 = s * this.m20 + c * this.m21;
        final float nm8 = c * this.m30 - s * this.m31;
        dest._m00(nm00)._m10(nm2)._m20(nm3)._m30(nm8)._m01(nm5)._m11(nm6)._m21(nm7)._m31(nm4);
        if (dest != this) {
            dest._m02(this.m02)._m12(this.m12)._m22(this.m22)._m32(this.m32);
        }
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3f mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix4x3f mapXZY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix4x3f mapXZnY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix4x3f mapXnYnZ(final Matrix4x3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix4x3f mapXnZY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix4x3f mapXnZnY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix4x3f mapYXZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix4x3f mapYXnZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix4x3f mapYZX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix4x3f mapYZnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix4x3f mapYnXZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix4x3f mapYnXnZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix4x3f mapYnZX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix4x3f mapYnZnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix4x3f mapZXY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix4x3f mapZXnY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix4x3f mapZYX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix4x3f mapZYnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix4x3f mapZnXY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix4x3f mapZnXnY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix4x3f mapZnYX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix4x3f mapZnYnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix4x3f mapnXYnZ(final Matrix4x3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix4x3f mapnXZY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix4x3f mapnXZnY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix4x3f mapnXnYZ(final Matrix4x3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix4x3f mapnXnYnZ(final Matrix4x3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix4x3f mapnXnZY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix4x3f mapnXnZnY(final Matrix4x3f dest) {
        final float m10 = this.m10;
        final float m11 = this.m11;
        final float m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix4x3f mapnYXZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix4x3f mapnYXnZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix4x3f mapnYZX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix4x3f mapnYZnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix4x3f mapnYnXZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix4x3f mapnYnXnZ(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix4x3f mapnYnZX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix4x3f mapnYnZnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix4x3f mapnZXY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix4x3f mapnZXnY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix4x3f mapnZYX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix4x3f mapnZYnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix4x3f mapnZnXY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix4x3f mapnZnXnY(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        final float m4 = this.m10;
        final float m5 = this.m11;
        final float m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix4x3f mapnZnYX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix4x3f mapnZnYnX(final Matrix4x3f dest) {
        final float m00 = this.m00;
        final float m2 = this.m01;
        final float m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateX() {
        return this._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateX(final Matrix4x3f dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateY() {
        return this._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateY(final Matrix4x3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateZ() {
        return this._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3f negateZ(final Matrix4x3f dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
