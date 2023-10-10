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

public class Matrix4x3d implements Externalizable, Cloneable, Matrix4x3dc
{
    private static final long serialVersionUID = 1L;
    double m00;
    double m01;
    double m02;
    double m10;
    double m11;
    double m12;
    double m20;
    double m21;
    double m22;
    double m30;
    double m31;
    double m32;
    int properties;
    
    public Matrix4x3d() {
        this.m00 = 1.0;
        this.m11 = 1.0;
        this.m22 = 1.0;
        this.properties = 28;
    }
    
    public Matrix4x3d(final Matrix4x3dc mat) {
        this.set(mat);
    }
    
    public Matrix4x3d(final Matrix4x3fc mat) {
        this.set(mat);
    }
    
    public Matrix4x3d(final Matrix3dc mat) {
        this.set(mat);
    }
    
    public Matrix4x3d(final Matrix3fc mat) {
        this.set(mat);
    }
    
    public Matrix4x3d(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22, final double m30, final double m31, final double m32) {
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
    
    public Matrix4x3d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        this.determineProperties();
    }
    
    public Matrix4x3d assume(final int properties) {
        this.properties = properties;
        return this;
    }
    
    public Matrix4x3d determineProperties() {
        int properties = 0;
        if (this.m00 == 1.0 && this.m01 == 0.0 && this.m02 == 0.0 && this.m10 == 0.0 && this.m11 == 1.0 && this.m12 == 0.0 && this.m20 == 0.0 && this.m21 == 0.0 && this.m22 == 1.0) {
            properties |= 0x18;
            if (this.m30 == 0.0 && this.m31 == 0.0 && this.m32 == 0.0) {
                properties |= 0x4;
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
    
    public double m30() {
        return this.m30;
    }
    
    public double m31() {
        return this.m31;
    }
    
    public double m32() {
        return this.m32;
    }
    
    Matrix4x3d _properties(final int properties) {
        this.properties = properties;
        return this;
    }
    
    Matrix4x3d _m00(final double m00) {
        this.m00 = m00;
        return this;
    }
    
    Matrix4x3d _m01(final double m01) {
        this.m01 = m01;
        return this;
    }
    
    Matrix4x3d _m02(final double m02) {
        this.m02 = m02;
        return this;
    }
    
    Matrix4x3d _m10(final double m10) {
        this.m10 = m10;
        return this;
    }
    
    Matrix4x3d _m11(final double m11) {
        this.m11 = m11;
        return this;
    }
    
    Matrix4x3d _m12(final double m12) {
        this.m12 = m12;
        return this;
    }
    
    Matrix4x3d _m20(final double m20) {
        this.m20 = m20;
        return this;
    }
    
    Matrix4x3d _m21(final double m21) {
        this.m21 = m21;
        return this;
    }
    
    Matrix4x3d _m22(final double m22) {
        this.m22 = m22;
        return this;
    }
    
    Matrix4x3d _m30(final double m30) {
        this.m30 = m30;
        return this;
    }
    
    Matrix4x3d _m31(final double m31) {
        this.m31 = m31;
        return this;
    }
    
    Matrix4x3d _m32(final double m32) {
        this.m32 = m32;
        return this;
    }
    
    public Matrix4x3d m00(final double m00) {
        this.m00 = m00;
        this.properties &= 0xFFFFFFEF;
        if (m00 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m01(final double m01) {
        this.m01 = m01;
        this.properties &= 0xFFFFFFEF;
        if (m01 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m02(final double m02) {
        this.m02 = m02;
        this.properties &= 0xFFFFFFEF;
        if (m02 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m10(final double m10) {
        this.m10 = m10;
        this.properties &= 0xFFFFFFEF;
        if (m10 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m11(final double m11) {
        this.m11 = m11;
        this.properties &= 0xFFFFFFEF;
        if (m11 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m12(final double m12) {
        this.m12 = m12;
        this.properties &= 0xFFFFFFEF;
        if (m12 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m20(final double m20) {
        this.m20 = m20;
        this.properties &= 0xFFFFFFEF;
        if (m20 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m21(final double m21) {
        this.m21 = m21;
        this.properties &= 0xFFFFFFEF;
        if (m21 != 0.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m22(final double m22) {
        this.m22 = m22;
        this.properties &= 0xFFFFFFEF;
        if (m22 != 1.0) {
            this.properties &= 0xFFFFFFF3;
        }
        return this;
    }
    
    public Matrix4x3d m30(final double m30) {
        this.m30 = m30;
        if (m30 != 0.0) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    public Matrix4x3d m31(final double m31) {
        this.m31 = m31;
        if (m31 != 0.0) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    public Matrix4x3d m32(final double m32) {
        this.m32 = m32;
        if (m32 != 0.0) {
            this.properties &= 0xFFFFFFFB;
        }
        return this;
    }
    
    public Matrix4x3d identity() {
        if ((this.properties & 0x4) != 0x0) {
            return this;
        }
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 28;
        return this;
    }
    
    public Matrix4x3d set(final Matrix4x3dc m) {
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
    
    public Matrix4x3d set(final Matrix4x3fc m) {
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
    
    public Matrix4x3d set(final Matrix4dc m) {
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
    
    public Matrix4d get(final Matrix4d dest) {
        return dest.set4x3(this);
    }
    
    public Matrix4x3d set(final Matrix3dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final Vector3dc col0, final Vector3dc col1, final Vector3dc col2, final Vector3dc col3) {
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
    
    public Matrix4x3d set3x3(final Matrix4x3dc mat) {
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
    
    public Matrix4x3d set(final AxisAngle4f axisAngle) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d set(final AxisAngle4d axisAngle) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d set(final Quaternionfc q) {
        return this.rotation(q);
    }
    
    public Matrix4x3d set(final Quaterniondc q) {
        return this.rotation(q);
    }
    
    public Matrix4x3d mul(final Matrix4x3dc right) {
        return this.mul(right, this);
    }
    
    public Matrix4x3d mul(final Matrix4x3dc right, final Matrix4x3d dest) {
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
    
    private Matrix4x3d mulGeneric(final Matrix4x3dc right, final Matrix4x3d dest) {
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
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32))))._properties(this.properties & right.properties() & 0x10);
    }
    
    public Matrix4x3d mul(final Matrix4x3fc right) {
        return this.mul(right, this);
    }
    
    public Matrix4x3d mul(final Matrix4x3fc right, final Matrix4x3d dest) {
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
    
    private Matrix4x3d mulGeneric(final Matrix4x3fc right, final Matrix4x3d dest) {
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
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm2, m7 * rm3)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm2, m8 * rm3)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm2, m9 * rm3)))._m10(Math.fma(m00, rm4, Math.fma(m4, rm5, m7 * rm6)))._m11(Math.fma(m2, rm4, Math.fma(m5, rm5, m8 * rm6)))._m12(Math.fma(m3, rm4, Math.fma(m6, rm5, m9 * rm6)))._m20(Math.fma(m00, rm7, Math.fma(m4, rm8, m7 * rm9)))._m21(Math.fma(m2, rm7, Math.fma(m5, rm8, m8 * rm9)))._m22(Math.fma(m3, rm7, Math.fma(m6, rm8, m9 * rm9)))._m30(Math.fma(m00, rm10, Math.fma(m4, rm11, Math.fma(m7, rm12, this.m30))))._m31(Math.fma(m2, rm10, Math.fma(m5, rm11, Math.fma(m8, rm12, this.m31))))._m32(Math.fma(m3, rm10, Math.fma(m6, rm11, Math.fma(m9, rm12, this.m32))))._properties(this.properties & right.properties() & 0x10);
    }
    
    public Matrix4x3d mulTranslation(final Matrix4x3dc right, final Matrix4x3d dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m10(right.m10())._m11(right.m11())._m12(right.m12())._m20(right.m20())._m21(right.m21())._m22(right.m22())._m30(right.m30() + this.m30)._m31(right.m31() + this.m31)._m32(right.m32() + this.m32)._properties(right.properties() & 0x10);
    }
    
    public Matrix4x3d mulTranslation(final Matrix4x3fc right, final Matrix4x3d dest) {
        return dest._m00(right.m00())._m01(right.m01())._m02(right.m02())._m10(right.m10())._m11(right.m11())._m12(right.m12())._m20(right.m20())._m21(right.m21())._m22(right.m22())._m30(right.m30() + this.m30)._m31(right.m31() + this.m31)._m32(right.m32() + this.m32)._properties(right.properties() & 0x10);
    }
    
    public Matrix4x3d mulOrtho(final Matrix4x3dc view) {
        return this.mulOrtho(view, this);
    }
    
    public Matrix4x3d mulOrtho(final Matrix4x3dc view, final Matrix4x3d dest) {
        final double nm00 = this.m00 * view.m00();
        final double nm2 = this.m11 * view.m01();
        final double nm3 = this.m22 * view.m02();
        final double nm4 = this.m00 * view.m10();
        final double nm5 = this.m11 * view.m11();
        final double nm6 = this.m22 * view.m12();
        final double nm7 = this.m00 * view.m20();
        final double nm8 = this.m11 * view.m21();
        final double nm9 = this.m22 * view.m22();
        final double nm10 = this.m00 * view.m30() + this.m30;
        final double nm11 = this.m11 * view.m31() + this.m31;
        final double nm12 = this.m22 * view.m32() + this.m32;
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
    
    public Matrix4x3d mul3x3(final double rm00, final double rm01, final double rm02, final double rm10, final double rm11, final double rm12, final double rm20, final double rm21, final double rm22) {
        return this.mul3x3(rm00, rm01, rm02, rm10, rm11, rm12, rm20, rm21, rm22, this);
    }
    
    public Matrix4x3d mul3x3(final double rm00, final double rm01, final double rm02, final double rm10, final double rm11, final double rm12, final double rm20, final double rm21, final double rm22, final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        final double m7 = this.m20;
        final double m8 = this.m21;
        final double m9 = this.m22;
        return dest._m00(Math.fma(m00, rm00, Math.fma(m4, rm01, m7 * rm02)))._m01(Math.fma(m2, rm00, Math.fma(m5, rm01, m8 * rm02)))._m02(Math.fma(m3, rm00, Math.fma(m6, rm01, m9 * rm02)))._m10(Math.fma(m00, rm10, Math.fma(m4, rm11, m7 * rm12)))._m11(Math.fma(m2, rm10, Math.fma(m5, rm11, m8 * rm12)))._m12(Math.fma(m3, rm10, Math.fma(m6, rm11, m9 * rm12)))._m20(Math.fma(m00, rm20, Math.fma(m4, rm21, m7 * rm22)))._m21(Math.fma(m2, rm20, Math.fma(m5, rm21, m8 * rm22)))._m22(Math.fma(m3, rm20, Math.fma(m6, rm21, m9 * rm22)))._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(0);
    }
    
    public Matrix4x3d fma(final Matrix4x3dc other, final double otherFactor) {
        return this.fma(other, otherFactor, this);
    }
    
    public Matrix4x3d fma(final Matrix4x3dc other, final double otherFactor, final Matrix4x3d dest) {
        dest._m00(Math.fma(other.m00(), otherFactor, this.m00))._m01(Math.fma(other.m01(), otherFactor, this.m01))._m02(Math.fma(other.m02(), otherFactor, this.m02))._m10(Math.fma(other.m10(), otherFactor, this.m10))._m11(Math.fma(other.m11(), otherFactor, this.m11))._m12(Math.fma(other.m12(), otherFactor, this.m12))._m20(Math.fma(other.m20(), otherFactor, this.m20))._m21(Math.fma(other.m21(), otherFactor, this.m21))._m22(Math.fma(other.m22(), otherFactor, this.m22))._m30(Math.fma(other.m30(), otherFactor, this.m30))._m31(Math.fma(other.m31(), otherFactor, this.m31))._m32(Math.fma(other.m32(), otherFactor, this.m32))._properties(0);
        return dest;
    }
    
    public Matrix4x3d fma(final Matrix4x3fc other, final double otherFactor) {
        return this.fma(other, otherFactor, this);
    }
    
    public Matrix4x3d fma(final Matrix4x3fc other, final double otherFactor, final Matrix4x3d dest) {
        dest._m00(Math.fma(other.m00(), otherFactor, this.m00))._m01(Math.fma(other.m01(), otherFactor, this.m01))._m02(Math.fma(other.m02(), otherFactor, this.m02))._m10(Math.fma(other.m10(), otherFactor, this.m10))._m11(Math.fma(other.m11(), otherFactor, this.m11))._m12(Math.fma(other.m12(), otherFactor, this.m12))._m20(Math.fma(other.m20(), otherFactor, this.m20))._m21(Math.fma(other.m21(), otherFactor, this.m21))._m22(Math.fma(other.m22(), otherFactor, this.m22))._m30(Math.fma(other.m30(), otherFactor, this.m30))._m31(Math.fma(other.m31(), otherFactor, this.m31))._m32(Math.fma(other.m32(), otherFactor, this.m32))._properties(0);
        return dest;
    }
    
    public Matrix4x3d add(final Matrix4x3dc other) {
        return this.add(other, this);
    }
    
    public Matrix4x3d add(final Matrix4x3dc other, final Matrix4x3d dest) {
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
    
    public Matrix4x3d add(final Matrix4x3fc other) {
        return this.add(other, this);
    }
    
    public Matrix4x3d add(final Matrix4x3fc other, final Matrix4x3d dest) {
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
    
    public Matrix4x3d sub(final Matrix4x3dc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix4x3d sub(final Matrix4x3dc subtrahend, final Matrix4x3d dest) {
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
    
    public Matrix4x3d sub(final Matrix4x3fc subtrahend) {
        return this.sub(subtrahend, this);
    }
    
    public Matrix4x3d sub(final Matrix4x3fc subtrahend, final Matrix4x3d dest) {
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
    
    public Matrix4x3d mulComponentWise(final Matrix4x3dc other) {
        return this.mulComponentWise(other, this);
    }
    
    public Matrix4x3d mulComponentWise(final Matrix4x3dc other, final Matrix4x3d dest) {
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
    
    public Matrix4x3d set(final double m00, final double m01, final double m02, final double m10, final double m11, final double m12, final double m20, final double m21, final double m22, final double m30, final double m31, final double m32) {
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
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final double[] m, final int off) {
        this.m00 = m[off + 0];
        this.m01 = m[off + 1];
        this.m02 = m[off + 2];
        this.m10 = m[off + 3];
        this.m11 = m[off + 4];
        this.m12 = m[off + 5];
        this.m20 = m[off + 6];
        this.m21 = m[off + 7];
        this.m22 = m[off + 8];
        this.m30 = m[off + 9];
        this.m31 = m[off + 10];
        this.m32 = m[off + 11];
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final double[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4x3d set(final float[] m, final int off) {
        this.m00 = m[off + 0];
        this.m01 = m[off + 1];
        this.m02 = m[off + 2];
        this.m10 = m[off + 3];
        this.m11 = m[off + 4];
        this.m12 = m[off + 5];
        this.m20 = m[off + 6];
        this.m21 = m[off + 7];
        this.m22 = m[off + 8];
        this.m30 = m[off + 9];
        this.m31 = m[off + 10];
        this.m32 = m[off + 11];
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final float[] m) {
        return this.set(m, 0);
    }
    
    public Matrix4x3d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d setFloats(final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, buffer.position(), buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d setFloats(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.getf(this, index, buffer);
        return this.determineProperties();
    }
    
    public Matrix4x3d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this.determineProperties();
    }
    
    public double determinant() {
        return (this.m00 * this.m11 - this.m01 * this.m10) * this.m22 + (this.m02 * this.m10 - this.m00 * this.m12) * this.m21 + (this.m01 * this.m12 - this.m02 * this.m11) * this.m20;
    }
    
    public Matrix4x3d invert() {
        return this.invert(this);
    }
    
    public Matrix4x3d invert(final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.invertOrthonormal(dest);
        }
        return this.invertGeneric(dest);
    }
    
    private Matrix4x3d invertGeneric(final Matrix4x3d dest) {
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
    
    private Matrix4x3d invertOrthonormal(final Matrix4x3d dest) {
        final double nm30 = -(this.m00 * this.m30 + this.m01 * this.m31 + this.m02 * this.m32);
        final double nm31 = -(this.m10 * this.m30 + this.m11 * this.m31 + this.m12 * this.m32);
        final double nm32 = -(this.m20 * this.m30 + this.m21 * this.m31 + this.m22 * this.m32);
        final double m01 = this.m01;
        final double m2 = this.m02;
        final double m3 = this.m12;
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
    
    public Matrix4x3d invertOrtho(final Matrix4x3d dest) {
        final double invM00 = 1.0 / this.m00;
        final double invM2 = 1.0 / this.m11;
        final double invM3 = 1.0 / this.m22;
        dest.set(invM00, 0.0, 0.0, 0.0, invM2, 0.0, 0.0, 0.0, invM3, -this.m30 * invM00, -this.m31 * invM2, -this.m32 * invM3);
        dest.properties = 0;
        return dest;
    }
    
    public Matrix4x3d invertOrtho() {
        return this.invertOrtho(this);
    }
    
    public Matrix4x3d transpose3x3() {
        return this.transpose3x3(this);
    }
    
    public Matrix4x3d transpose3x3(final Matrix4x3d dest) {
        final double nm00 = this.m00;
        final double nm2 = this.m10;
        final double nm3 = this.m20;
        final double nm4 = this.m01;
        final double nm5 = this.m11;
        final double nm6 = this.m21;
        final double nm7 = this.m02;
        final double nm8 = this.m12;
        final double nm9 = this.m22;
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
    
    public Matrix3d transpose3x3(final Matrix3d dest) {
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
    
    public Matrix4x3d translation(final double x, final double y, final double z) {
        if ((this.properties & 0x4) == 0x0) {
            this.identity();
        }
        this.m30 = x;
        this.m31 = y;
        this.m32 = z;
        this.properties = 24;
        return this;
    }
    
    public Matrix4x3d translation(final Vector3fc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d translation(final Vector3dc offset) {
        return this.translation(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d setTranslation(final double x, final double y, final double z) {
        this.m30 = x;
        this.m31 = y;
        this.m32 = z;
        this.properties &= 0xFFFFFFFB;
        return this;
    }
    
    public Matrix4x3d setTranslation(final Vector3dc xyz) {
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
        return Runtime.format(this.m00, formatter) + " " + Runtime.format(this.m10, formatter) + " " + Runtime.format(this.m20, formatter) + " " + Runtime.format(this.m30, formatter) + "\n" + Runtime.format(this.m01, formatter) + " " + Runtime.format(this.m11, formatter) + " " + Runtime.format(this.m21, formatter) + " " + Runtime.format(this.m31, formatter) + "\n" + Runtime.format(this.m02, formatter) + " " + Runtime.format(this.m12, formatter) + " " + Runtime.format(this.m22, formatter) + " " + Runtime.format(this.m32, formatter) + "\n";
    }
    
    public Matrix4x3d get(final Matrix4x3d dest) {
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
    
    public Matrix4x3dc getToAddress(final long address) {
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
        arr[offset + 9] = this.m30;
        arr[offset + 10] = this.m31;
        arr[offset + 11] = this.m32;
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
        arr[offset + 9] = (float)this.m30;
        arr[offset + 10] = (float)this.m31;
        arr[offset + 11] = (float)this.m32;
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
    
    public double[] get4x4(final double[] arr, final int offset) {
        MemUtil.INSTANCE.copy4x4(this, arr, offset);
        return arr;
    }
    
    public double[] get4x4(final double[] arr) {
        return this.get4x4(arr, 0);
    }
    
    public DoubleBuffer get4x4(final DoubleBuffer buffer) {
        return this.get4x4(buffer.position(), buffer);
    }
    
    public DoubleBuffer get4x4(final int index, final DoubleBuffer buffer) {
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
    
    public DoubleBuffer getTransposed(final DoubleBuffer buffer) {
        return this.getTransposed(buffer.position(), buffer);
    }
    
    public DoubleBuffer getTransposed(final int index, final DoubleBuffer buffer) {
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
    
    public FloatBuffer getTransposed(final FloatBuffer buffer) {
        return this.getTransposed(buffer.position(), buffer);
    }
    
    public FloatBuffer getTransposed(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.putfTransposed(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getTransposedFloats(final ByteBuffer buffer) {
        return this.getTransposed(buffer.position(), buffer);
    }
    
    public ByteBuffer getTransposedFloats(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putfTransposed(this, index, buffer);
        return buffer;
    }
    
    public double[] getTransposed(final double[] arr, final int offset) {
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
    
    public double[] getTransposed(final double[] arr) {
        return this.getTransposed(arr, 0);
    }
    
    public Matrix4x3d zero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d scaling(final double factor) {
        return this.scaling(factor, factor, factor);
    }
    
    public Matrix4x3d scaling(final double x, final double y, final double z) {
        if ((this.properties & 0x4) == 0x0) {
            this.identity();
        }
        this.m00 = x;
        this.m11 = y;
        this.m22 = z;
        final boolean one = Math.absEqualsOne(x) && Math.absEqualsOne(y) && Math.absEqualsOne(z);
        this.properties = (one ? 16 : 0);
        return this;
    }
    
    public Matrix4x3d scaling(final Vector3dc xyz) {
        return this.scaling(xyz.x(), xyz.y(), xyz.z());
    }
    
    public Matrix4x3d rotation(final double angle, final double x, final double y, final double z) {
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
    
    private Matrix4x3d rotationInternal(final double angle, final double x, final double y, final double z) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double C = 1.0 - cos;
        final double xy = x * y;
        final double xz = x * z;
        final double yz = y * z;
        this.m00 = cos + x * x * C;
        this.m01 = xy * C + z * sin;
        this.m02 = xz * C - y * sin;
        this.m10 = xy * C - z * sin;
        this.m11 = cos + y * y * C;
        this.m12 = yz * C + x * sin;
        this.m20 = xz * C + y * sin;
        this.m21 = yz * C - x * sin;
        this.m22 = cos + z * z * C;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationX(final double ang) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationY(final double ang) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationZ(final double ang) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationXYZ(final double angleX, final double angleY, final double angleZ) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationZYX(final double angleZ, final double angleY, final double angleX) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotationYXZ(final double angleY, final double angleX, final double angleZ) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d setRotationXYZ(final double angleX, final double angleY, final double angleZ) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3d setRotationZYX(final double angleZ, final double angleY, final double angleX) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3d setRotationYXZ(final double angleY, final double angleX, final double angleZ) {
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
        this.properties &= 0xFFFFFFF3;
        return this;
    }
    
    public Matrix4x3d rotation(final double angle, final Vector3dc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4x3d rotation(final double angle, final Vector3fc axis) {
        return this.rotation(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Vector4d transform(final Vector4d v) {
        return v.mul(this);
    }
    
    public Vector4d transform(final Vector4dc v, final Vector4d dest) {
        return v.mul(this, dest);
    }
    
    public Vector3d transformPosition(final Vector3d v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20 * v.z + this.m30, this.m01 * v.x + this.m11 * v.y + this.m21 * v.z + this.m31, this.m02 * v.x + this.m12 * v.y + this.m22 * v.z + this.m32);
        return v;
    }
    
    public Vector3d transformPosition(final Vector3dc v, final Vector3d dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20 * v.z() + this.m30, this.m01 * v.x() + this.m11 * v.y() + this.m21 * v.z() + this.m31, this.m02 * v.x() + this.m12 * v.y() + this.m22 * v.z() + this.m32);
        return dest;
    }
    
    public Vector3d transformDirection(final Vector3d v) {
        v.set(this.m00 * v.x + this.m10 * v.y + this.m20 * v.z, this.m01 * v.x + this.m11 * v.y + this.m21 * v.z, this.m02 * v.x + this.m12 * v.y + this.m22 * v.z);
        return v;
    }
    
    public Vector3d transformDirection(final Vector3dc v, final Vector3d dest) {
        dest.set(this.m00 * v.x() + this.m10 * v.y() + this.m20 * v.z(), this.m01 * v.x() + this.m11 * v.y() + this.m21 * v.z(), this.m02 * v.x() + this.m12 * v.y() + this.m22 * v.z());
        return dest;
    }
    
    public Matrix4x3d set3x3(final Matrix3dc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d set3x3(final Matrix3fc mat) {
        this.m00 = mat.m00();
        this.m01 = mat.m01();
        this.m02 = mat.m02();
        this.m10 = mat.m10();
        this.m11 = mat.m11();
        this.m12 = mat.m12();
        this.m20 = mat.m20();
        this.m21 = mat.m21();
        this.m22 = mat.m22();
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d scale(final Vector3dc xyz, final Matrix4x3d dest) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), dest);
    }
    
    public Matrix4x3d scale(final Vector3dc xyz) {
        return this.scale(xyz.x(), xyz.y(), xyz.z(), this);
    }
    
    public Matrix4x3d scale(final double x, final double y, final double z, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
        return this.scaleGeneric(x, y, z, dest);
    }
    
    private Matrix4x3d scaleGeneric(final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    public Matrix4x3d scale(final double x, final double y, final double z) {
        return this.scale(x, y, z, this);
    }
    
    public Matrix4x3d scale(final double xyz, final Matrix4x3d dest) {
        return this.scale(xyz, xyz, xyz, dest);
    }
    
    public Matrix4x3d scale(final double xyz) {
        return this.scale(xyz, xyz, xyz);
    }
    
    public Matrix4x3d scaleXY(final double x, final double y, final Matrix4x3d dest) {
        return this.scale(x, y, 1.0, dest);
    }
    
    public Matrix4x3d scaleXY(final double x, final double y) {
        return this.scale(x, y, 1.0);
    }
    
    public Matrix4x3d scaleAround(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz, final Matrix4x3d dest) {
        final double nm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final double nm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final double nm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final boolean one = Math.absEqualsOne(sx) && Math.absEqualsOne(sy) && Math.absEqualsOne(sz);
        return dest._m00(this.m00 * sx)._m01(this.m01 * sx)._m02(this.m02 * sx)._m10(this.m10 * sy)._m11(this.m11 * sy)._m12(this.m12 * sy)._m20(this.m20 * sz)._m21(this.m21 * sz)._m22(this.m22 * sz)._m30(-dest.m00 * ox - dest.m10 * oy - dest.m20 * oz + nm30)._m31(-dest.m01 * ox - dest.m11 * oy - dest.m21 * oz + nm31)._m32(-dest.m02 * ox - dest.m12 * oy - dest.m22 * oz + nm32)._properties(this.properties & ~(0xC | (one ? 0 : 16)));
    }
    
    public Matrix4x3d scaleAround(final double sx, final double sy, final double sz, final double ox, final double oy, final double oz) {
        return this.scaleAround(sx, sy, sz, ox, oy, oz, this);
    }
    
    public Matrix4x3d scaleAround(final double factor, final double ox, final double oy, final double oz) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, this);
    }
    
    public Matrix4x3d scaleAround(final double factor, final double ox, final double oy, final double oz, final Matrix4x3d dest) {
        return this.scaleAround(factor, factor, factor, ox, oy, oz, dest);
    }
    
    public Matrix4x3d scaleLocal(final double x, final double y, final double z, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.scaling(x, y, z);
        }
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
    
    public Matrix4x3d scaleLocal(final double x, final double y, final double z) {
        return this.scaleLocal(x, y, z, this);
    }
    
    public Matrix4x3d rotate(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(ang, x, y, z);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(ang, x, y, z, dest);
        }
        return this.rotateGeneric(ang, x, y, z, dest);
    }
    
    private Matrix4x3d rotateGeneric(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    private Matrix4x3d rotateGenericInternal(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotate(final double ang, final double x, final double y, final double z) {
        return this.rotate(ang, x, y, z, this);
    }
    
    public Matrix4x3d rotateTranslation(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    private Matrix4x3d rotateTranslationInternal(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    public Matrix4x3d rotateAround(final Quaterniondc quat, final double ox, final double oy, final double oz) {
        return this.rotateAround(quat, ox, oy, oz, this);
    }
    
    private Matrix4x3d rotateAroundAffine(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4x3d dest) {
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
        final double tm30 = this.m00 * ox + this.m10 * oy + this.m20 * oz + this.m30;
        final double tm31 = this.m01 * ox + this.m11 * oy + this.m21 * oz + this.m31;
        final double tm32 = this.m02 * ox + this.m12 * oy + this.m22 * oz + this.m32;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2 + this.m20 * rm3;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2 + this.m21 * rm3;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2 + this.m22 * rm3;
        final double nm4 = this.m00 * rm4 + this.m10 * rm5 + this.m20 * rm6;
        final double nm5 = this.m01 * rm4 + this.m11 * rm5 + this.m21 * rm6;
        final double nm6 = this.m02 * rm4 + this.m12 * rm5 + this.m22 * rm6;
        dest._m20(this.m00 * rm7 + this.m10 * rm8 + this.m20 * rm9)._m21(this.m01 * rm7 + this.m11 * rm8 + this.m21 * rm9)._m22(this.m02 * rm7 + this.m12 * rm8 + this.m22 * rm9)._m00(nm00)._m01(nm2)._m02(nm3)._m10(nm4)._m11(nm5)._m12(nm6)._m30(-nm00 * ox - nm4 * oy - this.m20 * oz + tm30)._m31(-nm2 * ox - nm5 * oy - this.m21 * oz + tm31)._m32(-nm3 * ox - nm6 * oy - this.m22 * oz + tm32)._properties(this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateAround(final Quaterniondc quat, final double ox, final double oy, final double oz, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.rotationAround(quat, ox, oy, oz);
        }
        return this.rotateAroundAffine(quat, ox, oy, oz, dest);
    }
    
    public Matrix4x3d rotationAround(final Quaterniondc quat, final double ox, final double oy, final double oz) {
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
    
    public Matrix4x3d rotateLocal(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotateLocalX(x * ang, dest);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotateLocalY(y * ang, dest);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotateLocalZ(z * ang, dest);
        }
        return this.rotateLocalInternal(ang, x, y, z, dest);
    }
    
    private Matrix4x3d rotateLocalInternal(final double ang, final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    public Matrix4x3d rotateLocal(final double ang, final double x, final double y, final double z) {
        return this.rotateLocal(ang, x, y, z, this);
    }
    
    public Matrix4x3d rotateLocalX(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm01 = cos * this.m01 - sin * this.m02;
        final double nm2 = sin * this.m01 + cos * this.m02;
        final double nm3 = cos * this.m11 - sin * this.m12;
        final double nm4 = sin * this.m11 + cos * this.m12;
        final double nm5 = cos * this.m21 - sin * this.m22;
        final double nm6 = sin * this.m21 + cos * this.m22;
        final double nm7 = cos * this.m31 - sin * this.m32;
        final double nm8 = sin * this.m31 + cos * this.m32;
        dest.m00 = this.m00;
        dest.m01 = nm01;
        dest.m02 = nm2;
        dest.m10 = this.m10;
        dest.m11 = nm3;
        dest.m12 = nm4;
        dest.m20 = this.m20;
        dest.m21 = nm5;
        dest.m22 = nm6;
        dest.m30 = this.m30;
        dest.m31 = nm7;
        dest.m32 = nm8;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateLocalX(final double ang) {
        return this.rotateLocalX(ang, this);
    }
    
    public Matrix4x3d rotateLocalY(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm00 = cos * this.m00 + sin * this.m02;
        final double nm2 = -sin * this.m00 + cos * this.m02;
        final double nm3 = cos * this.m10 + sin * this.m12;
        final double nm4 = -sin * this.m10 + cos * this.m12;
        final double nm5 = cos * this.m20 + sin * this.m22;
        final double nm6 = -sin * this.m20 + cos * this.m22;
        final double nm7 = cos * this.m30 + sin * this.m32;
        final double nm8 = -sin * this.m30 + cos * this.m32;
        dest.m00 = nm00;
        dest.m01 = this.m01;
        dest.m02 = nm2;
        dest.m10 = nm3;
        dest.m11 = this.m11;
        dest.m12 = nm4;
        dest.m20 = nm5;
        dest.m21 = this.m21;
        dest.m22 = nm6;
        dest.m30 = nm7;
        dest.m31 = this.m31;
        dest.m32 = nm8;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateLocalY(final double ang) {
        return this.rotateLocalY(ang, this);
    }
    
    public Matrix4x3d rotateLocalZ(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double cos = Math.cosFromSin(sin, ang);
        final double nm00 = cos * this.m00 - sin * this.m01;
        final double nm2 = sin * this.m00 + cos * this.m01;
        final double nm3 = cos * this.m10 - sin * this.m11;
        final double nm4 = sin * this.m10 + cos * this.m11;
        final double nm5 = cos * this.m20 - sin * this.m21;
        final double nm6 = sin * this.m20 + cos * this.m21;
        final double nm7 = cos * this.m30 - sin * this.m31;
        final double nm8 = sin * this.m30 + cos * this.m31;
        dest.m00 = nm00;
        dest.m01 = nm2;
        dest.m02 = this.m02;
        dest.m10 = nm3;
        dest.m11 = nm4;
        dest.m12 = this.m12;
        dest.m20 = nm5;
        dest.m21 = nm6;
        dest.m22 = this.m22;
        dest.m30 = nm7;
        dest.m31 = nm8;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateLocalZ(final double ang) {
        return this.rotateLocalZ(ang, this);
    }
    
    public Matrix4x3d translate(final Vector3dc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d translate(final Vector3dc offset, final Matrix4x3d dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3d translate(final Vector3fc offset) {
        return this.translate(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d translate(final Vector3fc offset, final Matrix4x3d dest) {
        return this.translate(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3d translate(final double x, final double y, final double z, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.translation(x, y, z);
        }
        return this.translateGeneric(x, y, z, dest);
    }
    
    private Matrix4x3d translateGeneric(final double x, final double y, final double z, final Matrix4x3d dest) {
        dest.m00 = this.m00;
        dest.m01 = this.m01;
        dest.m02 = this.m02;
        dest.m10 = this.m10;
        dest.m11 = this.m11;
        dest.m12 = this.m12;
        dest.m20 = this.m20;
        dest.m21 = this.m21;
        dest.m22 = this.m22;
        dest.m30 = this.m00 * x + this.m10 * y + this.m20 * z + this.m30;
        dest.m31 = this.m01 * x + this.m11 * y + this.m21 * z + this.m31;
        dest.m32 = this.m02 * x + this.m12 * y + this.m22 * z + this.m32;
        dest.properties = (this.properties & 0xFFFFFFFB);
        return dest;
    }
    
    public Matrix4x3d translate(final double x, final double y, final double z) {
        if ((this.properties & 0x4) != 0x0) {
            return this.translation(x, y, z);
        }
        final Matrix4x3d c = this;
        c.m30 += c.m00 * x + c.m10 * y + c.m20 * z;
        c.m31 += c.m01 * x + c.m11 * y + c.m21 * z;
        c.m32 += c.m02 * x + c.m12 * y + c.m22 * z;
        final Matrix4x3d matrix4x3d = c;
        matrix4x3d.properties &= 0xFFFFFFFB;
        return this;
    }
    
    public Matrix4x3d translateLocal(final Vector3fc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d translateLocal(final Vector3fc offset, final Matrix4x3d dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3d translateLocal(final Vector3dc offset) {
        return this.translateLocal(offset.x(), offset.y(), offset.z());
    }
    
    public Matrix4x3d translateLocal(final Vector3dc offset, final Matrix4x3d dest) {
        return this.translateLocal(offset.x(), offset.y(), offset.z(), dest);
    }
    
    public Matrix4x3d translateLocal(final double x, final double y, final double z, final Matrix4x3d dest) {
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
    
    public Matrix4x3d translateLocal(final double x, final double y, final double z) {
        return this.translateLocal(x, y, z, this);
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
        out.writeDouble(this.m30);
        out.writeDouble(this.m31);
        out.writeDouble(this.m32);
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
        this.m30 = in.readDouble();
        this.m31 = in.readDouble();
        this.m32 = in.readDouble();
        this.determineProperties();
    }
    
    public Matrix4x3d rotateX(final double ang, final Matrix4x3d dest) {
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
    
    private Matrix4x3d rotateXInternal(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double rm11;
        final double cos = rm11 = Math.cosFromSin(sin, ang);
        final double rm12 = sin;
        final double rm13 = -sin;
        final double rm14 = cos;
        final double nm10 = this.m10 * rm11 + this.m20 * rm12;
        final double nm11 = this.m11 * rm11 + this.m21 * rm12;
        final double nm12 = this.m12 * rm11 + this.m22 * rm12;
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
    
    public Matrix4x3d rotateX(final double ang) {
        return this.rotateX(ang, this);
    }
    
    public Matrix4x3d rotateY(final double ang, final Matrix4x3d dest) {
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
    
    private Matrix4x3d rotateYInternal(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double rm00;
        final double cos = rm00 = Math.cosFromSin(sin, ang);
        final double rm2 = -sin;
        final double rm3 = sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m20 * rm2;
        final double nm2 = this.m01 * rm00 + this.m21 * rm2;
        final double nm3 = this.m02 * rm00 + this.m22 * rm2;
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
    
    public Matrix4x3d rotateY(final double ang) {
        return this.rotateY(ang, this);
    }
    
    public Matrix4x3d rotateZ(final double ang, final Matrix4x3d dest) {
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
    
    private Matrix4x3d rotateZInternal(final double ang, final Matrix4x3d dest) {
        final double sin = Math.sin(ang);
        final double rm00;
        final double cos = rm00 = Math.cosFromSin(sin, ang);
        final double rm2 = sin;
        final double rm3 = -sin;
        final double rm4 = cos;
        final double nm00 = this.m00 * rm00 + this.m10 * rm2;
        final double nm2 = this.m01 * rm00 + this.m11 * rm2;
        final double nm3 = this.m02 * rm00 + this.m12 * rm2;
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
    
    public Matrix4x3d rotateZ(final double ang) {
        return this.rotateZ(ang, this);
    }
    
    public Matrix4x3d rotateXYZ(final Vector3d angles) {
        return this.rotateXYZ(angles.x, angles.y, angles.z);
    }
    
    public Matrix4x3d rotateXYZ(final double angleX, final double angleY, final double angleZ) {
        return this.rotateXYZ(angleX, angleY, angleZ, this);
    }
    
    public Matrix4x3d rotateXYZ(final double angleX, final double angleY, final double angleZ, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationXYZ(angleX, angleY, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationXYZ(angleX, angleY, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateXYZInternal(angleX, angleY, angleZ, dest);
    }
    
    private Matrix4x3d rotateXYZInternal(final double angleX, final double angleY, final double angleZ, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateZYX(final Vector3d angles) {
        return this.rotateZYX(angles.z, angles.y, angles.x);
    }
    
    public Matrix4x3d rotateZYX(final double angleZ, final double angleY, final double angleX) {
        return this.rotateZYX(angleZ, angleY, angleX, this);
    }
    
    public Matrix4x3d rotateZYX(final double angleZ, final double angleY, final double angleX, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationZYX(angleZ, angleY, angleX);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationZYX(angleZ, angleY, angleX).setTranslation(tx, ty, tz);
        }
        return this.rotateZYXInternal(angleZ, angleY, angleX, dest);
    }
    
    private Matrix4x3d rotateZYXInternal(final double angleZ, final double angleY, final double angleX, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotateYXZ(final Vector3d angles) {
        return this.rotateYXZ(angles.y, angles.x, angles.z);
    }
    
    public Matrix4x3d rotateYXZ(final double angleY, final double angleX, final double angleZ) {
        return this.rotateYXZ(angleY, angleX, angleZ, this);
    }
    
    public Matrix4x3d rotateYXZ(final double angleY, final double angleX, final double angleZ, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotationYXZ(angleY, angleX, angleZ);
        }
        if ((this.properties & 0x8) != 0x0) {
            final double tx = this.m30;
            final double ty = this.m31;
            final double tz = this.m32;
            return dest.rotationYXZ(angleY, angleX, angleZ).setTranslation(tx, ty, tz);
        }
        return this.rotateYXZInternal(angleY, angleX, angleZ, dest);
    }
    
    private Matrix4x3d rotateYXZInternal(final double angleY, final double angleX, final double angleZ, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotation(final AxisAngle4f angleAxis) {
        return this.rotation(angleAxis.angle, angleAxis.x, angleAxis.y, angleAxis.z);
    }
    
    public Matrix4x3d rotation(final AxisAngle4d angleAxis) {
        return this.rotation(angleAxis.angle, angleAxis.x, angleAxis.y, angleAxis.z);
    }
    
    public Matrix4x3d rotation(final Quaterniondc quat) {
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
        this._m00(w2 + x2 - z2 - y2);
        this._m01(dxy + dzw);
        this._m02(dxz - dyw);
        this._m10(dxy - dzw);
        this._m11(y2 - z2 + w2 - x2);
        this._m12(dyz + dxw);
        this._m20(dyw + dxz);
        this._m21(dyz - dxw);
        this._m22(z2 - y2 - x2 + w2);
        this._m30(0.0);
        this._m31(0.0);
        this._m32(0.0);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d rotation(final Quaternionfc quat) {
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
        this._m00(w2 + x2 - z2 - y2);
        this._m01(dxy + dzw);
        this._m02(dxz - dyw);
        this._m10(dxy - dzw);
        this._m11(y2 - z2 + w2 - x2);
        this._m12(dyz + dxw);
        this._m20(dyw + dxz);
        this._m21(dyz - dxw);
        this._m22(z2 - y2 - x2 + w2);
        this._m30(0.0);
        this._m31(0.0);
        this._m32(0.0);
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d translationRotateScale(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double sx, final double sy, final double sz) {
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
    
    public Matrix4x3d translationRotateScale(final Vector3fc translation, final Quaternionfc quat, final Vector3fc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4x3d translationRotateScale(final Vector3dc translation, final Quaterniondc quat, final Vector3dc scale) {
        return this.translationRotateScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z());
    }
    
    public Matrix4x3d translationRotateScaleMul(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final double sx, final double sy, final double sz, final Matrix4x3dc m) {
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
        final double nm00 = sx - (q2 + q3) * sx;
        final double nm2 = (q4 + q9) * sx;
        final double nm3 = (q5 - q8) * sx;
        final double nm4 = (q4 - q9) * sy;
        final double nm5 = sy - (q3 + q00) * sy;
        final double nm6 = (q7 + q6) * sy;
        final double nm7 = (q5 + q8) * sz;
        final double nm8 = (q7 - q6) * sz;
        final double nm9 = sz - (q2 + q00) * sz;
        final double m2 = nm00 * m.m00() + nm4 * m.m01() + nm7 * m.m02();
        final double m3 = nm2 * m.m00() + nm5 * m.m01() + nm8 * m.m02();
        this.m02 = nm3 * m.m00() + nm6 * m.m01() + nm9 * m.m02();
        this.m00 = m2;
        this.m01 = m3;
        final double m4 = nm00 * m.m10() + nm4 * m.m11() + nm7 * m.m12();
        final double m5 = nm2 * m.m10() + nm5 * m.m11() + nm8 * m.m12();
        this.m12 = nm3 * m.m10() + nm6 * m.m11() + nm9 * m.m12();
        this.m10 = m4;
        this.m11 = m5;
        final double m6 = nm00 * m.m20() + nm4 * m.m21() + nm7 * m.m22();
        final double m7 = nm2 * m.m20() + nm5 * m.m21() + nm8 * m.m22();
        this.m22 = nm3 * m.m20() + nm6 * m.m21() + nm9 * m.m22();
        this.m20 = m6;
        this.m21 = m7;
        final double m8 = nm00 * m.m30() + nm4 * m.m31() + nm7 * m.m32() + tx;
        final double m9 = nm2 * m.m30() + nm5 * m.m31() + nm8 * m.m32() + ty;
        this.m32 = nm3 * m.m30() + nm6 * m.m31() + nm9 * m.m32() + tz;
        this.m30 = m8;
        this.m31 = m9;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d translationRotateScaleMul(final Vector3dc translation, final Quaterniondc quat, final Vector3dc scale, final Matrix4x3dc m) {
        return this.translationRotateScaleMul(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w(), scale.x(), scale.y(), scale.z(), m);
    }
    
    public Matrix4x3d translationRotate(final double tx, final double ty, final double tz, final Quaterniondc quat) {
        final double dqx = quat.x() + quat.x();
        final double dqy = quat.y() + quat.y();
        final double dqz = quat.z() + quat.z();
        final double q00 = dqx * quat.x();
        final double q2 = dqy * quat.y();
        final double q3 = dqz * quat.z();
        final double q4 = dqx * quat.y();
        final double q5 = dqx * quat.z();
        final double q6 = dqx * quat.w();
        final double q7 = dqy * quat.z();
        final double q8 = dqy * quat.w();
        final double q9 = dqz * quat.w();
        this.m00 = 1.0 - (q2 + q3);
        this.m01 = q4 + q9;
        this.m02 = q5 - q8;
        this.m10 = q4 - q9;
        this.m11 = 1.0 - (q3 + q00);
        this.m12 = q7 + q6;
        this.m20 = q5 + q8;
        this.m21 = q7 - q6;
        this.m22 = 1.0 - (q2 + q00);
        this.m30 = tx;
        this.m31 = ty;
        this.m32 = tz;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d translationRotate(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw) {
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
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d translationRotate(final Vector3dc translation, final Quaterniondc quat) {
        return this.translationRotate(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4x3d translationRotateMul(final double tx, final double ty, final double tz, final Quaternionfc quat, final Matrix4x3dc mat) {
        return this.translationRotateMul(tx, ty, tz, quat.x(), quat.y(), quat.z(), quat.w(), mat);
    }
    
    public Matrix4x3d translationRotateMul(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw, final Matrix4x3dc mat) {
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
    
    public Matrix4x3d translationRotateInvert(final double tx, final double ty, final double tz, final double qx, final double qy, final double qz, final double qw) {
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
        return this._m00(1.0 - q2 - q3)._m01(q4 + q9)._m02(q5 - q8)._m10(q4 - q9)._m11(1.0 - q3 - q00)._m12(q7 + q6)._m20(q5 + q8)._m21(q7 - q6)._m22(1.0 - q2 - q00)._m30(-this.m00 * tx - this.m10 * ty - this.m20 * tz)._m31(-this.m01 * tx - this.m11 * ty - this.m21 * tz)._m32(-this.m02 * tx - this.m12 * ty - this.m22 * tz)._properties(16);
    }
    
    public Matrix4x3d translationRotateInvert(final Vector3dc translation, final Quaterniondc quat) {
        return this.translationRotateInvert(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(), quat.w());
    }
    
    public Matrix4x3d rotate(final Quaterniondc quat, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(quat);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(quat, dest);
        }
        return this.rotateGeneric(quat, dest);
    }
    
    private Matrix4x3d rotateGeneric(final Quaterniondc quat, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotate(final Quaternionfc quat, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.rotation(quat);
        }
        if ((this.properties & 0x8) != 0x0) {
            return this.rotateTranslation(quat, dest);
        }
        return this.rotateGeneric(quat, dest);
    }
    
    private Matrix4x3d rotateGeneric(final Quaternionfc quat, final Matrix4x3d dest) {
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
    
    public Matrix4x3d rotate(final Quaterniondc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4x3d rotate(final Quaternionfc quat) {
        return this.rotate(quat, this);
    }
    
    public Matrix4x3d rotateTranslation(final Quaterniondc quat, final Matrix4x3d dest) {
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
    
    public Matrix4x3d rotateTranslation(final Quaternionfc quat, final Matrix4x3d dest) {
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
        dest.m20 = rm7;
        dest.m21 = rm8;
        dest.m22 = rm9;
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
    
    public Matrix4x3d rotateLocal(final Quaterniondc quat, final Matrix4x3d dest) {
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
        final double nm10 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final double nm11 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final double nm12 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
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
    
    public Matrix4x3d rotateLocal(final Quaterniondc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4x3d rotateLocal(final Quaternionfc quat, final Matrix4x3d dest) {
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
        final double nm10 = lm00 * this.m30 + lm4 * this.m31 + lm7 * this.m32;
        final double nm11 = lm2 * this.m30 + lm5 * this.m31 + lm8 * this.m32;
        final double nm12 = lm3 * this.m30 + lm6 * this.m31 + lm9 * this.m32;
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
    
    public Matrix4x3d rotateLocal(final Quaternionfc quat) {
        return this.rotateLocal(quat, this);
    }
    
    public Matrix4x3d rotate(final AxisAngle4f axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4x3d rotate(final AxisAngle4f axisAngle, final Matrix4x3d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4x3d rotate(final AxisAngle4d axisAngle) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
    }
    
    public Matrix4x3d rotate(final AxisAngle4d axisAngle, final Matrix4x3d dest) {
        return this.rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
    }
    
    public Matrix4x3d rotate(final double angle, final Vector3dc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4x3d rotate(final double angle, final Vector3dc axis, final Matrix4x3d dest) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z(), dest);
    }
    
    public Matrix4x3d rotate(final double angle, final Vector3fc axis) {
        return this.rotate(angle, axis.x(), axis.y(), axis.z());
    }
    
    public Matrix4x3d rotate(final double angle, final Vector3fc axis, final Matrix4x3d dest) {
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
            default: {
                throw new IndexOutOfBoundsException();
            }
        }
        return dest;
    }
    
    public Matrix4x3d setRow(final int row, final Vector4dc src) throws IndexOutOfBoundsException {
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
    
    public Matrix4x3d setColumn(final int column, final Vector3dc src) throws IndexOutOfBoundsException {
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
    
    public Matrix4x3d normal() {
        return this.normal(this);
    }
    
    public Matrix4x3d normal(final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.identity();
        }
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix4x3d normalOrthonormal(final Matrix4x3d dest) {
        if (dest != this) {
            dest.set(this);
        }
        return dest._properties(16);
    }
    
    private Matrix4x3d normalGeneric(final Matrix4x3d dest) {
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
        dest.m30 = 0.0;
        dest.m31 = 0.0;
        dest.m32 = 0.0;
        dest.properties = (this.properties & 0xFFFFFFF7);
        return dest;
    }
    
    public Matrix3d normal(final Matrix3d dest) {
        if ((this.properties & 0x10) != 0x0) {
            return this.normalOrthonormal(dest);
        }
        return this.normalGeneric(dest);
    }
    
    private Matrix3d normalOrthonormal(final Matrix3d dest) {
        return dest.set(this);
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
    
    public Matrix4x3d cofactor3x3() {
        return this.cofactor3x3(this);
    }
    
    public Matrix3d cofactor3x3(final Matrix3d dest) {
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
    
    public Matrix4x3d cofactor3x3(final Matrix4x3d dest) {
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
        dest.m30 = 0.0;
        dest.m31 = 0.0;
        dest.m32 = 0.0;
        dest.properties = (this.properties & 0xFFFFFFF7);
        return dest;
    }
    
    public Matrix4x3d normalize3x3() {
        return this.normalize3x3(this);
    }
    
    public Matrix4x3d normalize3x3(final Matrix4x3d dest) {
        final double invXlen = Math.invsqrt(this.m00 * this.m00 + this.m01 * this.m01 + this.m02 * this.m02);
        final double invYlen = Math.invsqrt(this.m10 * this.m10 + this.m11 * this.m11 + this.m12 * this.m12);
        final double invZlen = Math.invsqrt(this.m20 * this.m20 + this.m21 * this.m21 + this.m22 * this.m22);
        dest.m00 = this.m00 * invXlen;
        dest.m01 = this.m01 * invXlen;
        dest.m02 = this.m02 * invXlen;
        dest.m10 = this.m10 * invYlen;
        dest.m11 = this.m11 * invYlen;
        dest.m12 = this.m12 * invYlen;
        dest.m20 = this.m20 * invZlen;
        dest.m21 = this.m21 * invZlen;
        dest.m22 = this.m22 * invZlen;
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
    
    public Matrix4x3d reflect(final double a, final double b, final double c, final double d, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.reflection(a, b, c, d);
        }
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
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d reflect(final double a, final double b, final double c, final double d) {
        return this.reflect(a, b, c, d, this);
    }
    
    public Matrix4x3d reflect(final double nx, final double ny, final double nz, final double px, final double py, final double pz) {
        return this.reflect(nx, ny, nz, px, py, pz, this);
    }
    
    public Matrix4x3d reflect(final double nx, final double ny, final double nz, final double px, final double py, final double pz, final Matrix4x3d dest) {
        final double invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final double nnx = nx * invLength;
        final double nny = ny * invLength;
        final double nnz = nz * invLength;
        return this.reflect(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz, dest);
    }
    
    public Matrix4x3d reflect(final Vector3dc normal, final Vector3dc point) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4x3d reflect(final Quaterniondc orientation, final Vector3dc point) {
        return this.reflect(orientation, point, this);
    }
    
    public Matrix4x3d reflect(final Quaterniondc orientation, final Vector3dc point, final Matrix4x3d dest) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflect(normalX, normalY, normalZ, point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4x3d reflect(final Vector3dc normal, final Vector3dc point, final Matrix4x3d dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z(), dest);
    }
    
    public Matrix4x3d reflection(final double a, final double b, final double c, final double d) {
        final double da = a + a;
        final double db = b + b;
        final double dc = c + c;
        final double dd = d + d;
        this.m00 = 1.0 - da * a;
        this.m01 = -da * b;
        this.m02 = -da * c;
        this.m10 = -db * a;
        this.m11 = 1.0 - db * b;
        this.m12 = -db * c;
        this.m20 = -dc * a;
        this.m21 = -dc * b;
        this.m22 = 1.0 - dc * c;
        this.m30 = -dd * a;
        this.m31 = -dd * b;
        this.m32 = -dd * c;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d reflection(final double nx, final double ny, final double nz, final double px, final double py, final double pz) {
        final double invLength = Math.invsqrt(nx * nx + ny * ny + nz * nz);
        final double nnx = nx * invLength;
        final double nny = ny * invLength;
        final double nnz = nz * invLength;
        return this.reflection(nnx, nny, nnz, -nnx * px - nny * py - nnz * pz);
    }
    
    public Matrix4x3d reflection(final Vector3dc normal, final Vector3dc point) {
        return this.reflection(normal.x(), normal.y(), normal.z(), point.x(), point.y(), point.z());
    }
    
    public Matrix4x3d reflection(final Quaterniondc orientation, final Vector3dc point) {
        final double num1 = orientation.x() + orientation.x();
        final double num2 = orientation.y() + orientation.y();
        final double num3 = orientation.z() + orientation.z();
        final double normalX = orientation.x() * num3 + orientation.w() * num2;
        final double normalY = orientation.y() * num3 - orientation.w() * num1;
        final double normalZ = 1.0 - (orientation.x() * num1 + orientation.y() * num2);
        return this.reflection(normalX, normalY, normalZ, point.x(), point.y(), point.z());
    }
    
    public Matrix4x3d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4x3d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        final double rm4 = (left + right) / (left - right);
        final double rm5 = (top + bottom) / (bottom - top);
        final double rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
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
    
    public Matrix4x3d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4x3d dest) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4x3d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.ortho(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3d ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.ortho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4x3d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        final double rm4 = (left + right) / (left - right);
        final double rm5 = (top + bottom) / (bottom - top);
        final double rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
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
    
    public Matrix4x3d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final Matrix4x3d dest) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false, dest);
    }
    
    public Matrix4x3d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3d orthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.orthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3d setOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.m00 = 2.0 / (right - left);
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / (top - bottom);
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        this.m30 = (right + left) / (left - right);
        this.m31 = (top + bottom) / (bottom - top);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d setOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setOrtho(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3d setOrthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.m00 = 2.0 / (right - left);
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / (top - bottom);
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        this.m30 = (right + left) / (left - right);
        this.m31 = (top + bottom) / (bottom - top);
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d setOrthoLH(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        return this.setOrthoLH(left, right, bottom, top, zNear, zFar, false);
    }
    
    public Matrix4x3d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4x3d dest) {
        final double rm00 = 2.0 / width;
        final double rm2 = 2.0 / height;
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        final double rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
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
    
    public Matrix4x3d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final Matrix4x3d dest) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4x3d orthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoSymmetric(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3d orthoSymmetric(final double width, final double height, final double zNear, final double zFar) {
        return this.orthoSymmetric(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4x3d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne, final Matrix4x3d dest) {
        final double rm00 = 2.0 / width;
        final double rm2 = 2.0 / height;
        final double rm3 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        final double rm4 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
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
    
    public Matrix4x3d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final Matrix4x3d dest) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, dest);
    }
    
    public Matrix4x3d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, zZeroToOne, this);
    }
    
    public Matrix4x3d orthoSymmetricLH(final double width, final double height, final double zNear, final double zFar) {
        return this.orthoSymmetricLH(width, height, zNear, zFar, false, this);
    }
    
    public Matrix4x3d setOrthoSymmetric(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.m00 = 2.0 / width;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / height;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = (zZeroToOne ? 1.0 : 2.0) / (zNear - zFar);
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d setOrthoSymmetric(final double width, final double height, final double zNear, final double zFar) {
        return this.setOrthoSymmetric(width, height, zNear, zFar, false);
    }
    
    public Matrix4x3d setOrthoSymmetricLH(final double width, final double height, final double zNear, final double zFar, final boolean zZeroToOne) {
        this.m00 = 2.0 / width;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / height;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = (zZeroToOne ? 1.0 : 2.0) / (zFar - zNear);
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d setOrthoSymmetricLH(final double width, final double height, final double zNear, final double zFar) {
        return this.setOrthoSymmetricLH(width, height, zNear, zFar, false);
    }
    
    public Matrix4x3d ortho2D(final double left, final double right, final double bottom, final double top, final Matrix4x3d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = -(right + left) / (right - left);
        final double rm4 = -(top + bottom) / (top - bottom);
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
    
    public Matrix4x3d ortho2D(final double left, final double right, final double bottom, final double top) {
        return this.ortho2D(left, right, bottom, top, this);
    }
    
    public Matrix4x3d ortho2DLH(final double left, final double right, final double bottom, final double top, final Matrix4x3d dest) {
        final double rm00 = 2.0 / (right - left);
        final double rm2 = 2.0 / (top - bottom);
        final double rm3 = -(right + left) / (right - left);
        final double rm4 = -(top + bottom) / (top - bottom);
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
    
    public Matrix4x3d ortho2DLH(final double left, final double right, final double bottom, final double top) {
        return this.ortho2DLH(left, right, bottom, top, this);
    }
    
    public Matrix4x3d setOrtho2D(final double left, final double right, final double bottom, final double top) {
        this.m00 = 2.0 / (right - left);
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / (top - bottom);
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = -1.0;
        this.m30 = -(right + left) / (right - left);
        this.m31 = -(top + bottom) / (top - bottom);
        this.m32 = 0.0;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d setOrtho2DLH(final double left, final double right, final double bottom, final double top) {
        this.m00 = 2.0 / (right - left);
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 2.0 / (top - bottom);
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m30 = -(right + left) / (right - left);
        this.m31 = -(top + bottom) / (top - bottom);
        this.m32 = 0.0;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d lookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3d lookAlong(final Vector3dc dir, final Vector3dc up, final Matrix4x3d dest) {
        return this.lookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3d lookAlong(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return this.setLookAlong(dirX, dirY, dirZ, upX, upY, upZ);
        }
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d lookAlong(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.lookAlong(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3d setLookAlong(final Vector3dc dir, final Vector3dc up) {
        return this.setLookAlong(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3d setLookAlong(double dirX, double dirY, double dirZ, final double upX, final double upY, final double upZ) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d setLookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.setLookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3d setLookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
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
    
    public Matrix4x3d lookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up, final Matrix4x3d dest) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3d lookAt(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.lookAt(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3d lookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        return this.lookAtGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4x3d lookAtGeneric(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
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
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d lookAt(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        return this.lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3d setLookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.setLookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3d setLookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
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
    
    public Matrix4x3d lookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up, final Matrix4x3d dest) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3d lookAtLH(final Vector3dc eye, final Vector3dc center, final Vector3dc up) {
        return this.lookAtLH(eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3d lookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
        if ((this.properties & 0x4) != 0x0) {
            return dest.setLookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        return this.lookAtLHGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest);
    }
    
    private Matrix4x3d lookAtLHGeneric(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
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
        dest.m30 = this.m00 * rm10 + this.m10 * rm11 + this.m20 * rm12 + this.m30;
        dest.m31 = this.m01 * rm10 + this.m11 * rm11 + this.m21 * rm12 + this.m31;
        dest.m32 = this.m02 * rm10 + this.m12 * rm11 + this.m22 * rm12 + this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d lookAtLH(final double eyeX, final double eyeY, final double eyeZ, final double centerX, final double centerY, final double centerZ, final double upX, final double upY, final double upZ) {
        return this.lookAtLH(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, this);
    }
    
    public Vector4d frustumPlane(final int which, final Vector4d dest) {
        switch (which) {
            case 0: {
                dest.set(this.m00, this.m10, this.m20, 1.0 + this.m30).normalize();
                break;
            }
            case 1: {
                dest.set(-this.m00, -this.m10, -this.m20, 1.0 - this.m30).normalize();
                break;
            }
            case 2: {
                dest.set(this.m01, this.m11, this.m21, 1.0 + this.m31).normalize();
                break;
            }
            case 3: {
                dest.set(-this.m01, -this.m11, -this.m21, 1.0 - this.m31).normalize();
                break;
            }
            case 4: {
                dest.set(this.m02, this.m12, this.m22, 1.0 + this.m32).normalize();
                break;
            }
            case 5: {
                dest.set(-this.m02, -this.m12, -this.m22, 1.0 - this.m32).normalize();
                break;
            }
            default: {
                throw new IllegalArgumentException("which");
            }
        }
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
    
    public Vector3d origin(final Vector3d origin) {
        final double a = this.m00 * this.m11 - this.m01 * this.m10;
        final double b = this.m00 * this.m12 - this.m02 * this.m10;
        final double d = this.m01 * this.m12 - this.m02 * this.m11;
        final double g = this.m20 * this.m31 - this.m21 * this.m30;
        final double h = this.m20 * this.m32 - this.m22 * this.m30;
        final double j = this.m21 * this.m32 - this.m22 * this.m31;
        origin.x = -this.m10 * j + this.m11 * h - this.m12 * g;
        origin.y = this.m00 * j - this.m01 * h + this.m02 * g;
        origin.z = -this.m30 * d + this.m31 * b - this.m32 * a;
        return origin;
    }
    
    public Matrix4x3d shadow(final Vector4dc light, final double a, final double b, final double c, final double d) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, this);
    }
    
    public Matrix4x3d shadow(final Vector4dc light, final double a, final double b, final double c, final double d, final Matrix4x3d dest) {
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4x3d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final double a, final double b, final double c, final double d) {
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, this);
    }
    
    public Matrix4x3d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final double a, final double b, final double c, final double d, final Matrix4x3d dest) {
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
        final double nm4 = this.m00 * rm5 + this.m10 * rm6 + this.m20 * rm7 + this.m30 * rm8;
        final double nm5 = this.m01 * rm5 + this.m11 * rm6 + this.m21 * rm7 + this.m31 * rm8;
        final double nm6 = this.m02 * rm5 + this.m12 * rm6 + this.m22 * rm7 + this.m32 * rm8;
        final double nm7 = this.m00 * rm9 + this.m10 * rm10 + this.m20 * rm11 + this.m30 * rm12;
        final double nm8 = this.m01 * rm9 + this.m11 * rm10 + this.m21 * rm11 + this.m31 * rm12;
        final double nm9 = this.m02 * rm9 + this.m12 * rm10 + this.m22 * rm11 + this.m32 * rm12;
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
    
    public Matrix4x3d shadow(final Vector4dc light, final Matrix4x3dc planeTransform, final Matrix4x3d dest) {
        final double a = planeTransform.m10();
        final double b = planeTransform.m11();
        final double c = planeTransform.m12();
        final double d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(light.x(), light.y(), light.z(), light.w(), a, b, c, d, dest);
    }
    
    public Matrix4x3d shadow(final Vector4dc light, final Matrix4x3dc planeTransform) {
        return this.shadow(light, planeTransform, this);
    }
    
    public Matrix4x3d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final Matrix4x3dc planeTransform, final Matrix4x3d dest) {
        final double a = planeTransform.m10();
        final double b = planeTransform.m11();
        final double c = planeTransform.m12();
        final double d = -a * planeTransform.m30() - b * planeTransform.m31() - c * planeTransform.m32();
        return this.shadow(lightX, lightY, lightZ, lightW, a, b, c, d, dest);
    }
    
    public Matrix4x3d shadow(final double lightX, final double lightY, final double lightZ, final double lightW, final Matrix4x3dc planeTransform) {
        return this.shadow(lightX, lightY, lightZ, lightW, planeTransform, this);
    }
    
    public Matrix4x3d billboardCylindrical(final Vector3dc objPos, final Vector3dc targetPos, final Vector3dc up) {
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
    
    public Matrix4x3d billboardSpherical(final Vector3dc objPos, final Vector3dc targetPos, final Vector3dc up) {
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
    
    public Matrix4x3d billboardSpherical(final Vector3dc objPos, final Vector3dc targetPos) {
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
        this.m00 = 1.0 - q2;
        this.m01 = q3;
        this.m02 = -q5;
        this.m10 = q3;
        this.m11 = 1.0 - q00;
        this.m12 = q4;
        this.m20 = q5;
        this.m21 = -q4;
        this.m22 = 1.0 - q2 - q00;
        this.m30 = objPos.x();
        this.m31 = objPos.y();
        this.m32 = objPos.z();
        this.properties = 16;
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
        temp = Double.doubleToLongBits(this.m30);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m31);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.m32);
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
        if (!(obj instanceof Matrix4x3d)) {
            return false;
        }
        final Matrix4x3d other = (Matrix4x3d)obj;
        return Double.doubleToLongBits(this.m00) == Double.doubleToLongBits(other.m00) && Double.doubleToLongBits(this.m01) == Double.doubleToLongBits(other.m01) && Double.doubleToLongBits(this.m02) == Double.doubleToLongBits(other.m02) && Double.doubleToLongBits(this.m10) == Double.doubleToLongBits(other.m10) && Double.doubleToLongBits(this.m11) == Double.doubleToLongBits(other.m11) && Double.doubleToLongBits(this.m12) == Double.doubleToLongBits(other.m12) && Double.doubleToLongBits(this.m20) == Double.doubleToLongBits(other.m20) && Double.doubleToLongBits(this.m21) == Double.doubleToLongBits(other.m21) && Double.doubleToLongBits(this.m22) == Double.doubleToLongBits(other.m22) && Double.doubleToLongBits(this.m30) == Double.doubleToLongBits(other.m30) && Double.doubleToLongBits(this.m31) == Double.doubleToLongBits(other.m31) && Double.doubleToLongBits(this.m32) == Double.doubleToLongBits(other.m32);
    }
    
    public boolean equals(final Matrix4x3dc m, final double delta) {
        return this == m || (m != null && m instanceof Matrix4x3d && Runtime.equals(this.m00, m.m00(), delta) && Runtime.equals(this.m01, m.m01(), delta) && Runtime.equals(this.m02, m.m02(), delta) && Runtime.equals(this.m10, m.m10(), delta) && Runtime.equals(this.m11, m.m11(), delta) && Runtime.equals(this.m12, m.m12(), delta) && Runtime.equals(this.m20, m.m20(), delta) && Runtime.equals(this.m21, m.m21(), delta) && Runtime.equals(this.m22, m.m22(), delta) && Runtime.equals(this.m30, m.m30(), delta) && Runtime.equals(this.m31, m.m31(), delta) && Runtime.equals(this.m32, m.m32(), delta));
    }
    
    public Matrix4x3d pick(final double x, final double y, final double width, final double height, final int[] viewport, final Matrix4x3d dest) {
        final double sx = viewport[2] / width;
        final double sy = viewport[3] / height;
        final double tx = (viewport[2] + 2.0 * (viewport[0] - x)) / width;
        final double ty = (viewport[3] + 2.0 * (viewport[1] - y)) / height;
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
    
    public Matrix4x3d pick(final double x, final double y, final double width, final double height, final int[] viewport) {
        return this.pick(x, y, width, height, viewport, this);
    }
    
    public Matrix4x3d swap(final Matrix4x3d other) {
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
        tmp = this.m30;
        this.m30 = other.m30;
        other.m30 = tmp;
        tmp = this.m31;
        this.m31 = other.m31;
        other.m31 = tmp;
        tmp = this.m32;
        this.m32 = other.m32;
        other.m32 = tmp;
        final int props = this.properties;
        this.properties = other.properties;
        other.properties = props;
        return this;
    }
    
    public Matrix4x3d arcball(final double radius, final double centerX, final double centerY, final double centerZ, final double angleX, final double angleY, final Matrix4x3d dest) {
        final double m30 = this.m20 * -radius + this.m30;
        final double m31 = this.m21 * -radius + this.m31;
        final double m32 = this.m22 * -radius + this.m32;
        double sin = Math.sin(angleX);
        double cos = Math.cosFromSin(sin, angleX);
        final double nm10 = this.m10 * cos + this.m20 * sin;
        final double nm11 = this.m11 * cos + this.m21 * sin;
        final double nm12 = this.m12 * cos + this.m22 * sin;
        final double m33 = this.m20 * cos - this.m10 * sin;
        final double m34 = this.m21 * cos - this.m11 * sin;
        final double m35 = this.m22 * cos - this.m12 * sin;
        sin = Math.sin(angleY);
        cos = Math.cosFromSin(sin, angleY);
        final double nm13 = this.m00 * cos - m33 * sin;
        final double nm14 = this.m01 * cos - m34 * sin;
        final double nm15 = this.m02 * cos - m35 * sin;
        final double nm16 = this.m00 * sin + m33 * cos;
        final double nm17 = this.m01 * sin + m34 * cos;
        final double nm18 = this.m02 * sin + m35 * cos;
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
    
    public Matrix4x3d arcball(final double radius, final Vector3dc center, final double angleX, final double angleY, final Matrix4x3d dest) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, dest);
    }
    
    public Matrix4x3d arcball(final double radius, final double centerX, final double centerY, final double centerZ, final double angleX, final double angleY) {
        return this.arcball(radius, centerX, centerY, centerZ, angleX, angleY, this);
    }
    
    public Matrix4x3d arcball(final double radius, final Vector3dc center, final double angleX, final double angleY) {
        return this.arcball(radius, center.x(), center.y(), center.z(), angleX, angleY, this);
    }
    
    public Matrix4x3d transformAab(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final Vector3d outMin, final Vector3d outMax) {
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
    
    public Matrix4x3d transformAab(final Vector3dc min, final Vector3dc max, final Vector3d outMin, final Vector3d outMax) {
        return this.transformAab(min.x(), min.y(), min.z(), max.x(), max.y(), max.z(), outMin, outMax);
    }
    
    public Matrix4x3d lerp(final Matrix4x3dc other, final double t) {
        return this.lerp(other, t, this);
    }
    
    public Matrix4x3d lerp(final Matrix4x3dc other, final double t, final Matrix4x3d dest) {
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
    
    public Matrix4x3d rotateTowards(final Vector3dc dir, final Vector3dc up, final Matrix4x3d dest) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), dest);
    }
    
    public Matrix4x3d rotateTowards(final Vector3dc dir, final Vector3dc up) {
        return this.rotateTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), this);
    }
    
    public Matrix4x3d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
        return this.rotateTowards(dirX, dirY, dirZ, upX, upY, upZ, this);
    }
    
    public Matrix4x3d rotateTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ, final Matrix4x3d dest) {
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
        dest.m30 = this.m30;
        dest.m31 = this.m31;
        dest.m32 = this.m32;
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
        dest.properties = (this.properties & 0xFFFFFFF3);
        return dest;
    }
    
    public Matrix4x3d rotationTowards(final Vector3dc dir, final Vector3dc up) {
        return this.rotationTowards(dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3d rotationTowards(final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
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
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.properties = 16;
        return this;
    }
    
    public Matrix4x3d translationRotateTowards(final Vector3dc pos, final Vector3dc dir, final Vector3dc up) {
        return this.translationRotateTowards(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z());
    }
    
    public Matrix4x3d translationRotateTowards(final double posX, final double posY, final double posZ, final double dirX, final double dirY, final double dirZ, final double upX, final double upY, final double upZ) {
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
        this.m30 = posX;
        this.m31 = posY;
        this.m32 = posZ;
        this.properties = 16;
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
    
    public Matrix4x3d obliqueZ(final double a, final double b) {
        this.m20 += this.m00 * a + this.m10 * b;
        this.m21 += this.m01 * a + this.m11 * b;
        this.m22 += this.m02 * a + this.m12 * b;
        this.properties = 0;
        return this;
    }
    
    public Matrix4x3d obliqueZ(final double a, final double b, final Matrix4x3d dest) {
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
    
    public Matrix4x3d mapXZY() {
        return this.mapXZY(this);
    }
    
    public Matrix4x3d mapXZY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapXZnY() {
        return this.mapXZnY(this);
    }
    
    public Matrix4x3d mapXZnY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapXnYnZ() {
        return this.mapXnYnZ(this);
    }
    
    public Matrix4x3d mapXnYnZ(final Matrix4x3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapXnZY() {
        return this.mapXnZY(this);
    }
    
    public Matrix4x3d mapXnZY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapXnZnY() {
        return this.mapXnZnY(this);
    }
    
    public Matrix4x3d mapXnZnY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYXZ() {
        return this.mapYXZ(this);
    }
    
    public Matrix4x3d mapYXZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYXnZ() {
        return this.mapYXnZ(this);
    }
    
    public Matrix4x3d mapYXnZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYZX() {
        return this.mapYZX(this);
    }
    
    public Matrix4x3d mapYZX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYZnX() {
        return this.mapYZnX(this);
    }
    
    public Matrix4x3d mapYZnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYnXZ() {
        return this.mapYnXZ(this);
    }
    
    public Matrix4x3d mapYnXZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYnXnZ() {
        return this.mapYnXnZ(this);
    }
    
    public Matrix4x3d mapYnXnZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYnZX() {
        return this.mapYnZX(this);
    }
    
    public Matrix4x3d mapYnZX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapYnZnX() {
        return this.mapYnZnX(this);
    }
    
    public Matrix4x3d mapYnZnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m10)._m01(this.m11)._m02(this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZXY() {
        return this.mapZXY(this);
    }
    
    public Matrix4x3d mapZXY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZXnY() {
        return this.mapZXnY(this);
    }
    
    public Matrix4x3d mapZXnY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZYX() {
        return this.mapZYX(this);
    }
    
    public Matrix4x3d mapZYX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZYnX() {
        return this.mapZYnX(this);
    }
    
    public Matrix4x3d mapZYnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZnXY() {
        return this.mapZnXY(this);
    }
    
    public Matrix4x3d mapZnXY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZnXnY() {
        return this.mapZnXnY(this);
    }
    
    public Matrix4x3d mapZnXnY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZnYX() {
        return this.mapZnYX(this);
    }
    
    public Matrix4x3d mapZnYX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapZnYnX() {
        return this.mapZnYnX(this);
    }
    
    public Matrix4x3d mapZnYnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(this.m20)._m01(this.m21)._m02(this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXYnZ() {
        return this.mapnXYnZ(this);
    }
    
    public Matrix4x3d mapnXYnZ(final Matrix4x3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXZY() {
        return this.mapnXZY(this);
    }
    
    public Matrix4x3d mapnXZY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXZnY() {
        return this.mapnXZnY(this);
    }
    
    public Matrix4x3d mapnXZnY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXnYZ() {
        return this.mapnXnYZ(this);
    }
    
    public Matrix4x3d mapnXnYZ(final Matrix4x3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXnYnZ() {
        return this.mapnXnYnZ(this);
    }
    
    public Matrix4x3d mapnXnYnZ(final Matrix4x3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXnZY() {
        return this.mapnXnZY(this);
    }
    
    public Matrix4x3d mapnXnZY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m10)._m21(m11)._m22(m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnXnZnY() {
        return this.mapnXnZnY(this);
    }
    
    public Matrix4x3d mapnXnZnY(final Matrix4x3d dest) {
        final double m10 = this.m10;
        final double m11 = this.m11;
        final double m12 = this.m12;
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m10)._m21(-m11)._m22(-m12)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYXZ() {
        return this.mapnYXZ(this);
    }
    
    public Matrix4x3d mapnYXZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYXnZ() {
        return this.mapnYXnZ(this);
    }
    
    public Matrix4x3d mapnYXnZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(m00)._m11(m2)._m12(m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYZX() {
        return this.mapnYZX(this);
    }
    
    public Matrix4x3d mapnYZX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYZnX() {
        return this.mapnYZnX(this);
    }
    
    public Matrix4x3d mapnYZnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(this.m20)._m11(this.m21)._m12(this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYnXZ() {
        return this.mapnYnXZ(this);
    }
    
    public Matrix4x3d mapnYnXZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYnXnZ() {
        return this.mapnYnXnZ(this);
    }
    
    public Matrix4x3d mapnYnXnZ(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYnZX() {
        return this.mapnYnZX(this);
    }
    
    public Matrix4x3d mapnYnZX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnYnZnX() {
        return this.mapnYnZnX(this);
    }
    
    public Matrix4x3d mapnYnZnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m10)._m01(-this.m11)._m02(-this.m12)._m10(-this.m20)._m11(-this.m21)._m12(-this.m22)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZXY() {
        return this.mapnZXY(this);
    }
    
    public Matrix4x3d mapnZXY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZXnY() {
        return this.mapnZXnY(this);
    }
    
    public Matrix4x3d mapnZXnY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(m00)._m11(m2)._m12(m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZYX() {
        return this.mapnZYX(this);
    }
    
    public Matrix4x3d mapnZYX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZYnX() {
        return this.mapnZYnX(this);
    }
    
    public Matrix4x3d mapnZYnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZnXY() {
        return this.mapnZnXY(this);
    }
    
    public Matrix4x3d mapnZnXY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(m4)._m21(m5)._m22(m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZnXnY() {
        return this.mapnZnXnY(this);
    }
    
    public Matrix4x3d mapnZnXnY(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        final double m4 = this.m10;
        final double m5 = this.m11;
        final double m6 = this.m12;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-m00)._m11(-m2)._m12(-m3)._m20(-m4)._m21(-m5)._m22(-m6)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZnYX() {
        return this.mapnZnYX(this);
    }
    
    public Matrix4x3d mapnZnYX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(m00)._m21(m2)._m22(m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d mapnZnYnX() {
        return this.mapnZnYnX(this);
    }
    
    public Matrix4x3d mapnZnYnX(final Matrix4x3d dest) {
        final double m00 = this.m00;
        final double m2 = this.m01;
        final double m3 = this.m02;
        return dest._m00(-this.m20)._m01(-this.m21)._m02(-this.m22)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(-m00)._m21(-m2)._m22(-m3)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateX() {
        return this._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateX(final Matrix4x3d dest) {
        return dest._m00(-this.m00)._m01(-this.m01)._m02(-this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateY() {
        return this._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateY(final Matrix4x3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(-this.m10)._m11(-this.m11)._m12(-this.m12)._m20(this.m20)._m21(this.m21)._m22(this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateZ() {
        return this._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._properties(this.properties & 0x10);
    }
    
    public Matrix4x3d negateZ(final Matrix4x3d dest) {
        return dest._m00(this.m00)._m01(this.m01)._m02(this.m02)._m10(this.m10)._m11(this.m11)._m12(this.m12)._m20(-this.m20)._m21(-this.m21)._m22(-this.m22)._m30(this.m30)._m31(this.m31)._m32(this.m32)._properties(this.properties & 0x10);
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.m00) && Math.isFinite(this.m01) && Math.isFinite(this.m02) && Math.isFinite(this.m10) && Math.isFinite(this.m11) && Math.isFinite(this.m12) && Math.isFinite(this.m20) && Math.isFinite(this.m21) && Math.isFinite(this.m22) && Math.isFinite(this.m30) && Math.isFinite(this.m31) && Math.isFinite(this.m32);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
