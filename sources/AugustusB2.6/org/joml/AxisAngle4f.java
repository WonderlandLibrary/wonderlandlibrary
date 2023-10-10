// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.text.NumberFormat;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.Externalizable;

public class AxisAngle4f implements Externalizable, Cloneable
{
    private static final long serialVersionUID = 1L;
    public float angle;
    public float x;
    public float y;
    public float z;
    
    public AxisAngle4f() {
        this.z = 1.0f;
    }
    
    public AxisAngle4f(final AxisAngle4f a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.angle = (float)(((a.angle < 0.0) ? (6.283185307179586 + a.angle % 6.283185307179586) : a.angle) % 6.283185307179586);
    }
    
    public AxisAngle4f(final Quaternionfc q) {
        final float acos = Math.safeAcos(q.w());
        final float invSqrt = Math.invsqrt(1.0f - q.w() * q.w());
        if (Float.isInfinite(invSqrt)) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
        }
        else {
            this.x = q.x() * invSqrt;
            this.y = q.y() * invSqrt;
            this.z = q.z() * invSqrt;
        }
        this.angle = acos + acos;
    }
    
    public AxisAngle4f(final float angle, final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = (float)(((angle < 0.0) ? (6.283185307179586 + angle % 6.283185307179586) : angle) % 6.283185307179586);
    }
    
    public AxisAngle4f(final float angle, final Vector3fc v) {
        this(angle, v.x(), v.y(), v.z());
    }
    
    public AxisAngle4f set(final AxisAngle4f a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.angle = a.angle;
        this.angle = (float)(((this.angle < 0.0) ? (6.283185307179586 + this.angle % 6.283185307179586) : this.angle) % 6.283185307179586);
        return this;
    }
    
    public AxisAngle4f set(final AxisAngle4d a) {
        this.x = (float)a.x;
        this.y = (float)a.y;
        this.z = (float)a.z;
        this.angle = (float)a.angle;
        this.angle = (float)(((this.angle < 0.0) ? (6.283185307179586 + this.angle % 6.283185307179586) : this.angle) % 6.283185307179586);
        return this;
    }
    
    public AxisAngle4f set(final float angle, final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = (float)(((angle < 0.0) ? (6.283185307179586 + angle % 6.283185307179586) : angle) % 6.283185307179586);
        return this;
    }
    
    public AxisAngle4f set(final float angle, final Vector3fc v) {
        return this.set(angle, v.x(), v.y(), v.z());
    }
    
    public AxisAngle4f set(final Quaternionfc q) {
        final float acos = Math.safeAcos(q.w());
        final float invSqrt = Math.invsqrt(1.0f - q.w() * q.w());
        if (Float.isInfinite(invSqrt)) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
        }
        else {
            this.x = q.x() * invSqrt;
            this.y = q.y() * invSqrt;
            this.z = q.z() * invSqrt;
        }
        this.angle = acos + acos;
        return this;
    }
    
    public AxisAngle4f set(final Quaterniondc q) {
        final double acos = Math.safeAcos(q.w());
        final double invSqrt = Math.invsqrt(1.0 - q.w() * q.w());
        if (Double.isInfinite(invSqrt)) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
        }
        else {
            this.x = (float)(q.x() * invSqrt);
            this.y = (float)(q.y() * invSqrt);
            this.z = (float)(q.z() * invSqrt);
        }
        this.angle = (float)(acos + acos);
        return this;
    }
    
    public AxisAngle4f set(final Matrix3fc m) {
        float nm00 = m.m00();
        float nm2 = m.m01();
        float nm3 = m.m02();
        float nm4 = m.m10();
        float nm5 = m.m11();
        float nm6 = m.m12();
        float nm7 = m.m20();
        float nm8 = m.m21();
        float nm9 = m.m22();
        final float lenX = Math.invsqrt(m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02());
        final float lenY = Math.invsqrt(m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12());
        final float lenZ = Math.invsqrt(m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22());
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        final float epsilon = 1.0E-4f;
        final float epsilon2 = 0.001f;
        if (Math.abs(nm4 - nm2) >= epsilon || Math.abs(nm7 - nm3) >= epsilon || Math.abs(nm8 - nm6) >= epsilon) {
            final float s = Math.sqrt((nm6 - nm8) * (nm6 - nm8) + (nm7 - nm3) * (nm7 - nm3) + (nm2 - nm4) * (nm2 - nm4));
            this.angle = Math.safeAcos((nm00 + nm5 + nm9 - 1.0f) / 2.0f);
            this.x = (nm6 - nm8) / s;
            this.y = (nm7 - nm3) / s;
            this.z = (nm2 - nm4) / s;
            return this;
        }
        if (Math.abs(nm4 + nm2) < epsilon2 && Math.abs(nm7 + nm3) < epsilon2 && Math.abs(nm8 + nm6) < epsilon2 && Math.abs(nm00 + nm5 + nm9 - 3.0f) < epsilon2) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            this.angle = 0.0f;
            return this;
        }
        this.angle = 3.1415927f;
        final float xx = (nm00 + 1.0f) / 2.0f;
        final float yy = (nm5 + 1.0f) / 2.0f;
        final float zz = (nm9 + 1.0f) / 2.0f;
        final float xy = (nm4 + nm2) / 4.0f;
        final float xz = (nm7 + nm3) / 4.0f;
        final float yz = (nm8 + nm6) / 4.0f;
        if (xx > yy && xx > zz) {
            this.x = Math.sqrt(xx);
            this.y = xy / this.x;
            this.z = xz / this.x;
        }
        else if (yy > zz) {
            this.y = Math.sqrt(yy);
            this.x = xy / this.y;
            this.z = yz / this.y;
        }
        else {
            this.z = Math.sqrt(zz);
            this.x = xz / this.z;
            this.y = yz / this.z;
        }
        return this;
    }
    
    public AxisAngle4f set(final Matrix3dc m) {
        double nm00 = m.m00();
        double nm2 = m.m01();
        double nm3 = m.m02();
        double nm4 = m.m10();
        double nm5 = m.m11();
        double nm6 = m.m12();
        double nm7 = m.m20();
        double nm8 = m.m21();
        double nm9 = m.m22();
        final double lenX = Math.invsqrt(m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02());
        final double lenY = Math.invsqrt(m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12());
        final double lenZ = Math.invsqrt(m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22());
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        final double epsilon = 1.0E-4;
        final double epsilon2 = 0.001;
        if (Math.abs(nm4 - nm2) >= epsilon || Math.abs(nm7 - nm3) >= epsilon || Math.abs(nm8 - nm6) >= epsilon) {
            final double s = Math.sqrt((nm6 - nm8) * (nm6 - nm8) + (nm7 - nm3) * (nm7 - nm3) + (nm2 - nm4) * (nm2 - nm4));
            this.angle = (float)Math.safeAcos((nm00 + nm5 + nm9 - 1.0) / 2.0);
            this.x = (float)((nm6 - nm8) / s);
            this.y = (float)((nm7 - nm3) / s);
            this.z = (float)((nm2 - nm4) / s);
            return this;
        }
        if (Math.abs(nm4 + nm2) < epsilon2 && Math.abs(nm7 + nm3) < epsilon2 && Math.abs(nm8 + nm6) < epsilon2 && Math.abs(nm00 + nm5 + nm9 - 3.0) < epsilon2) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            this.angle = 0.0f;
            return this;
        }
        this.angle = 3.1415927f;
        final double xx = (nm00 + 1.0) / 2.0;
        final double yy = (nm5 + 1.0) / 2.0;
        final double zz = (nm9 + 1.0) / 2.0;
        final double xy = (nm4 + nm2) / 4.0;
        final double xz = (nm7 + nm3) / 4.0;
        final double yz = (nm8 + nm6) / 4.0;
        if (xx > yy && xx > zz) {
            this.x = (float)Math.sqrt(xx);
            this.y = (float)(xy / this.x);
            this.z = (float)(xz / this.x);
        }
        else if (yy > zz) {
            this.y = (float)Math.sqrt(yy);
            this.x = (float)(xy / this.y);
            this.z = (float)(yz / this.y);
        }
        else {
            this.z = (float)Math.sqrt(zz);
            this.x = (float)(xz / this.z);
            this.y = (float)(yz / this.z);
        }
        return this;
    }
    
    public AxisAngle4f set(final Matrix4fc m) {
        float nm00 = m.m00();
        float nm2 = m.m01();
        float nm3 = m.m02();
        float nm4 = m.m10();
        float nm5 = m.m11();
        float nm6 = m.m12();
        float nm7 = m.m20();
        float nm8 = m.m21();
        float nm9 = m.m22();
        final float lenX = Math.invsqrt(m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02());
        final float lenY = Math.invsqrt(m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12());
        final float lenZ = Math.invsqrt(m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22());
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        final float epsilon = 1.0E-4f;
        final float epsilon2 = 0.001f;
        if (Math.abs(nm4 - nm2) >= epsilon || Math.abs(nm7 - nm3) >= epsilon || Math.abs(nm8 - nm6) >= epsilon) {
            final float s = Math.sqrt((nm6 - nm8) * (nm6 - nm8) + (nm7 - nm3) * (nm7 - nm3) + (nm2 - nm4) * (nm2 - nm4));
            this.angle = Math.safeAcos((nm00 + nm5 + nm9 - 1.0f) / 2.0f);
            this.x = (nm6 - nm8) / s;
            this.y = (nm7 - nm3) / s;
            this.z = (nm2 - nm4) / s;
            return this;
        }
        if (Math.abs(nm4 + nm2) < epsilon2 && Math.abs(nm7 + nm3) < epsilon2 && Math.abs(nm8 + nm6) < epsilon2 && Math.abs(nm00 + nm5 + nm9 - 3.0f) < epsilon2) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            this.angle = 0.0f;
            return this;
        }
        this.angle = 3.1415927f;
        final float xx = (nm00 + 1.0f) / 2.0f;
        final float yy = (nm5 + 1.0f) / 2.0f;
        final float zz = (nm9 + 1.0f) / 2.0f;
        final float xy = (nm4 + nm2) / 4.0f;
        final float xz = (nm7 + nm3) / 4.0f;
        final float yz = (nm8 + nm6) / 4.0f;
        if (xx > yy && xx > zz) {
            this.x = Math.sqrt(xx);
            this.y = xy / this.x;
            this.z = xz / this.x;
        }
        else if (yy > zz) {
            this.y = Math.sqrt(yy);
            this.x = xy / this.y;
            this.z = yz / this.y;
        }
        else {
            this.z = Math.sqrt(zz);
            this.x = xz / this.z;
            this.y = yz / this.z;
        }
        return this;
    }
    
    public AxisAngle4f set(final Matrix4x3fc m) {
        float nm00 = m.m00();
        float nm2 = m.m01();
        float nm3 = m.m02();
        float nm4 = m.m10();
        float nm5 = m.m11();
        float nm6 = m.m12();
        float nm7 = m.m20();
        float nm8 = m.m21();
        float nm9 = m.m22();
        final float lenX = Math.invsqrt(m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02());
        final float lenY = Math.invsqrt(m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12());
        final float lenZ = Math.invsqrt(m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22());
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        final float epsilon = 1.0E-4f;
        final float epsilon2 = 0.001f;
        if (Math.abs(nm4 - nm2) >= epsilon || Math.abs(nm7 - nm3) >= epsilon || Math.abs(nm8 - nm6) >= epsilon) {
            final float s = Math.sqrt((nm6 - nm8) * (nm6 - nm8) + (nm7 - nm3) * (nm7 - nm3) + (nm2 - nm4) * (nm2 - nm4));
            this.angle = Math.safeAcos((nm00 + nm5 + nm9 - 1.0f) / 2.0f);
            this.x = (nm6 - nm8) / s;
            this.y = (nm7 - nm3) / s;
            this.z = (nm2 - nm4) / s;
            return this;
        }
        if (Math.abs(nm4 + nm2) < epsilon2 && Math.abs(nm7 + nm3) < epsilon2 && Math.abs(nm8 + nm6) < epsilon2 && Math.abs(nm00 + nm5 + nm9 - 3.0f) < epsilon2) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            this.angle = 0.0f;
            return this;
        }
        this.angle = 3.1415927f;
        final float xx = (nm00 + 1.0f) / 2.0f;
        final float yy = (nm5 + 1.0f) / 2.0f;
        final float zz = (nm9 + 1.0f) / 2.0f;
        final float xy = (nm4 + nm2) / 4.0f;
        final float xz = (nm7 + nm3) / 4.0f;
        final float yz = (nm8 + nm6) / 4.0f;
        if (xx > yy && xx > zz) {
            this.x = Math.sqrt(xx);
            this.y = xy / this.x;
            this.z = xz / this.x;
        }
        else if (yy > zz) {
            this.y = Math.sqrt(yy);
            this.x = xy / this.y;
            this.z = yz / this.y;
        }
        else {
            this.z = Math.sqrt(zz);
            this.x = xz / this.z;
            this.y = yz / this.z;
        }
        return this;
    }
    
    public AxisAngle4f set(final Matrix4dc m) {
        double nm00 = m.m00();
        double nm2 = m.m01();
        double nm3 = m.m02();
        double nm4 = m.m10();
        double nm5 = m.m11();
        double nm6 = m.m12();
        double nm7 = m.m20();
        double nm8 = m.m21();
        double nm9 = m.m22();
        final double lenX = Math.invsqrt(m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02());
        final double lenY = Math.invsqrt(m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12());
        final double lenZ = Math.invsqrt(m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22());
        nm00 *= lenX;
        nm2 *= lenX;
        nm3 *= lenX;
        nm4 *= lenY;
        nm5 *= lenY;
        nm6 *= lenY;
        nm7 *= lenZ;
        nm8 *= lenZ;
        nm9 *= lenZ;
        final double epsilon = 1.0E-4;
        final double epsilon2 = 0.001;
        if (Math.abs(nm4 - nm2) >= epsilon || Math.abs(nm7 - nm3) >= epsilon || Math.abs(nm8 - nm6) >= epsilon) {
            final double s = Math.sqrt((nm6 - nm8) * (nm6 - nm8) + (nm7 - nm3) * (nm7 - nm3) + (nm2 - nm4) * (nm2 - nm4));
            this.angle = (float)Math.safeAcos((nm00 + nm5 + nm9 - 1.0) / 2.0);
            this.x = (float)((nm6 - nm8) / s);
            this.y = (float)((nm7 - nm3) / s);
            this.z = (float)((nm2 - nm4) / s);
            return this;
        }
        if (Math.abs(nm4 + nm2) < epsilon2 && Math.abs(nm7 + nm3) < epsilon2 && Math.abs(nm8 + nm6) < epsilon2 && Math.abs(nm00 + nm5 + nm9 - 3.0) < epsilon2) {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            this.angle = 0.0f;
            return this;
        }
        this.angle = 3.1415927f;
        final double xx = (nm00 + 1.0) / 2.0;
        final double yy = (nm5 + 1.0) / 2.0;
        final double zz = (nm9 + 1.0) / 2.0;
        final double xy = (nm4 + nm2) / 4.0;
        final double xz = (nm7 + nm3) / 4.0;
        final double yz = (nm8 + nm6) / 4.0;
        if (xx > yy && xx > zz) {
            this.x = (float)Math.sqrt(xx);
            this.y = (float)(xy / this.x);
            this.z = (float)(xz / this.x);
        }
        else if (yy > zz) {
            this.y = (float)Math.sqrt(yy);
            this.x = (float)(xy / this.y);
            this.z = (float)(yz / this.y);
        }
        else {
            this.z = (float)Math.sqrt(zz);
            this.x = (float)(xz / this.z);
            this.y = (float)(yz / this.z);
        }
        return this;
    }
    
    public Quaternionf get(final Quaternionf q) {
        return q.set(this);
    }
    
    public Quaterniond get(final Quaterniond q) {
        return q.set(this);
    }
    
    public Matrix4f get(final Matrix4f m) {
        return m.set(this);
    }
    
    public Matrix3f get(final Matrix3f m) {
        return m.set(this);
    }
    
    public Matrix4d get(final Matrix4d m) {
        return m.set(this);
    }
    
    public Matrix3d get(final Matrix3d m) {
        return m.set(this);
    }
    
    public AxisAngle4d get(final AxisAngle4d dest) {
        return dest.set(this);
    }
    
    public AxisAngle4f get(final AxisAngle4f dest) {
        return dest.set(this);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.angle);
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.angle = in.readFloat();
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
    }
    
    public AxisAngle4f normalize() {
        final float invLength = Math.invsqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        return this;
    }
    
    public AxisAngle4f rotate(final float ang) {
        this.angle += ang;
        this.angle = (float)(((this.angle < 0.0) ? (6.283185307179586 + this.angle % 6.283185307179586) : this.angle) % 6.283185307179586);
        return this;
    }
    
    public Vector3f transform(final Vector3f v) {
        return this.transform(v, v);
    }
    
    public Vector3f transform(final Vector3fc v, final Vector3f dest) {
        final double sin = Math.sin(this.angle);
        final double cos = Math.cosFromSin(sin, this.angle);
        final float dot = this.x * v.x() + this.y * v.y() + this.z * v.z();
        dest.set((float)(v.x() * cos + sin * (this.y * v.z() - this.z * v.y()) + (1.0 - cos) * dot * this.x), (float)(v.y() * cos + sin * (this.z * v.x() - this.x * v.z()) + (1.0 - cos) * dot * this.y), (float)(v.z() * cos + sin * (this.x * v.y() - this.y * v.x()) + (1.0 - cos) * dot * this.z));
        return dest;
    }
    
    public Vector4f transform(final Vector4f v) {
        return this.transform(v, v);
    }
    
    public Vector4f transform(final Vector4fc v, final Vector4f dest) {
        final double sin = Math.sin(this.angle);
        final double cos = Math.cosFromSin(sin, this.angle);
        final float dot = this.x * v.x() + this.y * v.y() + this.z * v.z();
        dest.set((float)(v.x() * cos + sin * (this.y * v.z() - this.z * v.y()) + (1.0 - cos) * dot * this.x), (float)(v.y() * cos + sin * (this.z * v.x() - this.x * v.z()) + (1.0 - cos) * dot * this.y), (float)(v.z() * cos + sin * (this.x * v.y() - this.y * v.x()) + (1.0 - cos) * dot * this.z), dest.w);
        return dest;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " <| " + Runtime.format(this.angle, formatter) + ")";
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final float nangle = (float)(((this.angle < 0.0) ? (6.283185307179586 + this.angle % 6.283185307179586) : this.angle) % 6.283185307179586);
        result = 31 * result + Float.floatToIntBits(nangle);
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
        final AxisAngle4f other = (AxisAngle4f)obj;
        final float nangle = (float)(((this.angle < 0.0) ? (6.283185307179586 + this.angle % 6.283185307179586) : this.angle) % 6.283185307179586);
        final float nangleOther = (float)(((other.angle < 0.0) ? (6.283185307179586 + other.angle % 6.283185307179586) : other.angle) % 6.283185307179586);
        return Float.floatToIntBits(nangle) == Float.floatToIntBits(nangleOther) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
