// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector3d implements Externalizable, Cloneable, Vector3dc
{
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;
    public double z;
    
    public Vector3d() {
    }
    
    public Vector3d(final double d) {
        this.x = d;
        this.y = d;
        this.z = d;
    }
    
    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3d(final Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    
    public Vector3d(final Vector3ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    
    public Vector3d(final Vector2fc v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }
    
    public Vector3d(final Vector2ic v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }
    
    public Vector3d(final Vector3dc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    
    public Vector3d(final Vector2dc v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }
    
    public Vector3d(final double[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }
    
    public Vector3d(final float[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }
    
    public Vector3d(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3d(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector3d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3d(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
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
    
    public Vector3d set(final Vector3dc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }
    
    public Vector3d set(final Vector3ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }
    
    public Vector3d set(final Vector2dc v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        return this;
    }
    
    public Vector3d set(final Vector2ic v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        return this;
    }
    
    public Vector3d set(final Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }
    
    public Vector3d set(final Vector2fc v, final double z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        return this;
    }
    
    public Vector3d set(final double d) {
        this.x = d;
        this.y = d;
        this.z = d;
        return this;
    }
    
    public Vector3d set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3d set(final double[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
        return this;
    }
    
    public Vector3d set(final float[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
        return this;
    }
    
    public Vector3d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Vector3d setComponent(final int component, final double value) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                this.x = value;
                break;
            }
            case 1: {
                this.y = value;
                break;
            }
            case 2: {
                this.z = value;
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return this;
    }
    
    public ByteBuffer get(final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public DoubleBuffer get(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public DoubleBuffer get(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public ByteBuffer getf(final ByteBuffer buffer) {
        MemUtil.INSTANCE.putf(this, buffer.position(), buffer);
        return buffer;
    }
    
    public ByteBuffer getf(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.putf(this, index, buffer);
        return buffer;
    }
    
    public FloatBuffer get(final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, buffer.position(), buffer);
        return buffer;
    }
    
    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.put(this, index, buffer);
        return buffer;
    }
    
    public Vector3dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector3d sub(final Vector3dc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }
    
    public Vector3d sub(final Vector3dc v, final Vector3d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        return dest;
    }
    
    public Vector3d sub(final Vector3fc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }
    
    public Vector3d sub(final Vector3fc v, final Vector3d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        return dest;
    }
    
    public Vector3d sub(final double x, final double y, final double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3d sub(final double x, final double y, final double z, final Vector3d dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        return dest;
    }
    
    public Vector3d add(final Vector3dc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        return this;
    }
    
    public Vector3d add(final Vector3dc v, final Vector3d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        return dest;
    }
    
    public Vector3d add(final Vector3fc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        return this;
    }
    
    public Vector3d add(final Vector3fc v, final Vector3d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        return dest;
    }
    
    public Vector3d add(final double x, final double y, final double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3d add(final double x, final double y, final double z, final Vector3d dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }
    
    public Vector3d fma(final Vector3dc a, final Vector3dc b) {
        this.x = Math.fma(a.x(), b.x(), this.x);
        this.y = Math.fma(a.y(), b.y(), this.y);
        this.z = Math.fma(a.z(), b.z(), this.z);
        return this;
    }
    
    public Vector3d fma(final double a, final Vector3dc b) {
        this.x = Math.fma(a, b.x(), this.x);
        this.y = Math.fma(a, b.y(), this.y);
        this.z = Math.fma(a, b.z(), this.z);
        return this;
    }
    
    public Vector3d fma(final Vector3fc a, final Vector3fc b) {
        this.x = Math.fma(a.x(), b.x(), this.x);
        this.y = Math.fma(a.y(), b.y(), this.y);
        this.z = Math.fma(a.z(), b.z(), this.z);
        return this;
    }
    
    public Vector3d fma(final Vector3fc a, final Vector3fc b, final Vector3d dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        return dest;
    }
    
    public Vector3d fma(final double a, final Vector3fc b) {
        this.x = Math.fma(a, b.x(), this.x);
        this.y = Math.fma(a, b.y(), this.y);
        this.z = Math.fma(a, b.z(), this.z);
        return this;
    }
    
    public Vector3d fma(final Vector3dc a, final Vector3dc b, final Vector3d dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        return dest;
    }
    
    public Vector3d fma(final double a, final Vector3dc b, final Vector3d dest) {
        dest.x = Math.fma(a, b.x(), this.x);
        dest.y = Math.fma(a, b.y(), this.y);
        dest.z = Math.fma(a, b.z(), this.z);
        return dest;
    }
    
    public Vector3d fma(final Vector3dc a, final Vector3fc b, final Vector3d dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        return dest;
    }
    
    public Vector3d fma(final double a, final Vector3fc b, final Vector3d dest) {
        dest.x = Math.fma(a, b.x(), this.x);
        dest.y = Math.fma(a, b.y(), this.y);
        dest.z = Math.fma(a, b.z(), this.z);
        return dest;
    }
    
    public Vector3d mulAdd(final Vector3dc a, final Vector3dc b) {
        this.x = Math.fma(this.x, a.x(), b.x());
        this.y = Math.fma(this.y, a.y(), b.y());
        this.z = Math.fma(this.z, a.z(), b.z());
        return this;
    }
    
    public Vector3d mulAdd(final double a, final Vector3dc b) {
        this.x = Math.fma(this.x, a, b.x());
        this.y = Math.fma(this.y, a, b.y());
        this.z = Math.fma(this.z, a, b.z());
        return this;
    }
    
    public Vector3d mulAdd(final Vector3dc a, final Vector3dc b, final Vector3d dest) {
        dest.x = Math.fma(this.x, a.x(), b.x());
        dest.y = Math.fma(this.y, a.y(), b.y());
        dest.z = Math.fma(this.z, a.z(), b.z());
        return dest;
    }
    
    public Vector3d mulAdd(final double a, final Vector3dc b, final Vector3d dest) {
        dest.x = Math.fma(this.x, a, b.x());
        dest.y = Math.fma(this.y, a, b.y());
        dest.z = Math.fma(this.z, a, b.z());
        return dest;
    }
    
    public Vector3d mulAdd(final Vector3fc a, final Vector3dc b, final Vector3d dest) {
        dest.x = Math.fma(this.x, a.x(), b.x());
        dest.y = Math.fma(this.y, a.y(), b.y());
        dest.z = Math.fma(this.z, a.z(), b.z());
        return dest;
    }
    
    public Vector3d mul(final Vector3dc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        return this;
    }
    
    public Vector3d mul(final Vector3fc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        return this;
    }
    
    public Vector3d mul(final Vector3fc v, final Vector3d dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        return dest;
    }
    
    public Vector3d mul(final Vector3dc v, final Vector3d dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        return dest;
    }
    
    public Vector3d div(final Vector3d v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        return this;
    }
    
    public Vector3d div(final Vector3fc v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        return this;
    }
    
    public Vector3d div(final Vector3fc v, final Vector3d dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        return dest;
    }
    
    public Vector3d div(final Vector3dc v, final Vector3d dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        return dest;
    }
    
    public Vector3d mulProject(final Matrix4dc mat, final double w, final Vector3d dest) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * w)));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * w))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * w))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * w))) * invW;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulProject(final Matrix4dc mat, final Vector3d dest) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30()))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31()))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32()))) * invW;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulProject(final Matrix4dc mat) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30()))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31()))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32()))) * invW;
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulProject(final Matrix4fc mat, final Vector3d dest) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = (mat.m00() * this.x + mat.m10() * this.y + mat.m20() * this.z + mat.m30()) * invW;
        final double ry = (mat.m01() * this.x + mat.m11() * this.y + mat.m21() * this.z + mat.m31()) * invW;
        final double rz = (mat.m02() * this.x + mat.m12() * this.y + mat.m22() * this.z + mat.m32()) * invW;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulProject(final Matrix4fc mat) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = (mat.m00() * this.x + mat.m10() * this.y + mat.m20() * this.z + mat.m30()) * invW;
        final double ry = (mat.m01() * this.x + mat.m11() * this.y + mat.m21() * this.z + mat.m31()) * invW;
        final double rz = (mat.m02() * this.x + mat.m12() * this.y + mat.m22() * this.z + mat.m32()) * invW;
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mul(final Matrix3fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mul(final Matrix3dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mul(final Matrix3dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3f mul(final Matrix3dc mat, final Vector3f dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = (float)rx;
        dest.y = (float)ry;
        dest.z = (float)rz;
        return dest;
    }
    
    public Vector3d mul(final Matrix3fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mul(final Matrix3x2dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector3d mul(final Matrix3x2dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = this.z;
        return dest;
    }
    
    public Vector3d mul(final Matrix3x2fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        this.x = rx;
        this.y = ry;
        return this;
    }
    
    public Vector3d mul(final Matrix3x2fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = this.z;
        return dest;
    }
    
    public Vector3d mulTranspose(final Matrix3dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTranspose(final Matrix3dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulTranspose(final Matrix3fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTranspose(final Matrix3fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulPosition(final Matrix4fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulPosition(final Matrix4dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulPosition(final Matrix4x3dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulPosition(final Matrix4x3fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulPosition(final Matrix4dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulPosition(final Matrix4fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulPosition(final Matrix4x3dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulPosition(final Matrix4x3fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulTransposePosition(final Matrix4dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, Math.fma(mat.m02(), this.z, mat.m03())));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m12(), this.z, mat.m13())));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, Math.fma(mat.m22(), this.z, mat.m23())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTransposePosition(final Matrix4dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, Math.fma(mat.m02(), this.z, mat.m03())));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m12(), this.z, mat.m13())));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, Math.fma(mat.m22(), this.z, mat.m23())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulTransposePosition(final Matrix4fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, Math.fma(mat.m02(), this.z, mat.m03())));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m12(), this.z, mat.m13())));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, Math.fma(mat.m22(), this.z, mat.m23())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTransposePosition(final Matrix4fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, Math.fma(mat.m02(), this.z, mat.m03())));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m12(), this.z, mat.m13())));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, Math.fma(mat.m22(), this.z, mat.m23())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public double mulPositionW(final Matrix4fc mat) {
        final double w = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return w;
    }
    
    public double mulPositionW(final Matrix4fc mat, final Vector3d dest) {
        final double w = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return w;
    }
    
    public double mulPositionW(final Matrix4dc mat) {
        final double w = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return w;
    }
    
    public double mulPositionW(final Matrix4dc mat, final Vector3d dest) {
        final double w = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33())));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30())));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31())));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32())));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return w;
    }
    
    public Vector3d mulDirection(final Matrix4fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulDirection(final Matrix4dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulDirection(final Matrix4x3dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulDirection(final Matrix4x3fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulDirection(final Matrix4dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulDirection(final Matrix4fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulDirection(final Matrix4x3dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulDirection(final Matrix4x3fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, mat.m20() * this.z));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, mat.m21() * this.z));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulTransposeDirection(final Matrix4dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTransposeDirection(final Matrix4dc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mulTransposeDirection(final Matrix4fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d mulTransposeDirection(final Matrix4fc mat, final Vector3d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m01(), this.y, mat.m02() * this.z));
        final double ry = Math.fma(mat.m10(), this.x, Math.fma(mat.m11(), this.y, mat.m12() * this.z));
        final double rz = Math.fma(mat.m20(), this.x, Math.fma(mat.m21(), this.y, mat.m22() * this.z));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d mul(final double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vector3d mul(final double scalar, final Vector3d dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }
    
    public Vector3d mul(final double x, final double y, final double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vector3d mul(final double x, final double y, final double z, final Vector3d dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        dest.z = this.z * z;
        return dest;
    }
    
    public Vector3d rotate(final Quaterniondc quat) {
        return quat.transform(this, this);
    }
    
    public Vector3d rotate(final Quaterniondc quat, final Vector3d dest) {
        return quat.transform(this, dest);
    }
    
    public Quaterniond rotationTo(final Vector3dc toDir, final Quaterniond dest) {
        return dest.rotationTo(this, toDir);
    }
    
    public Quaterniond rotationTo(final double toDirX, final double toDirY, final double toDirZ, final Quaterniond dest) {
        return dest.rotationTo(this.x, this.y, this.z, toDirX, toDirY, toDirZ);
    }
    
    public Vector3d rotateAxis(final double angle, final double x, final double y, final double z) {
        if (y == 0.0 && z == 0.0 && Math.absEqualsOne(x)) {
            return this.rotateX(x * angle, this);
        }
        if (x == 0.0 && z == 0.0 && Math.absEqualsOne(y)) {
            return this.rotateY(y * angle, this);
        }
        if (x == 0.0 && y == 0.0 && Math.absEqualsOne(z)) {
            return this.rotateZ(z * angle, this);
        }
        return this.rotateAxisInternal(angle, x, y, z, this);
    }
    
    public Vector3d rotateAxis(final double angle, final double aX, final double aY, final double aZ, final Vector3d dest) {
        if (aY == 0.0 && aZ == 0.0 && Math.absEqualsOne(aX)) {
            return this.rotateX(aX * angle, dest);
        }
        if (aX == 0.0 && aZ == 0.0 && Math.absEqualsOne(aY)) {
            return this.rotateY(aY * angle, dest);
        }
        if (aX == 0.0 && aY == 0.0 && Math.absEqualsOne(aZ)) {
            return this.rotateZ(aZ * angle, dest);
        }
        return this.rotateAxisInternal(angle, aX, aY, aZ, dest);
    }
    
    private Vector3d rotateAxisInternal(final double angle, final double aX, final double aY, final double aZ, final Vector3d dest) {
        final double hangle = angle * 0.5;
        final double sinAngle = Math.sin(hangle);
        final double qx = aX * sinAngle;
        final double qy = aY * sinAngle;
        final double qz = aZ * sinAngle;
        final double qw = Math.cosFromSin(sinAngle, hangle);
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
        final double nx = (w2 + x2 - z2 - y2) * this.x + (-zw + xy - zw + xy) * this.y + (yw + xz + xz + yw) * this.z;
        final double ny = (xy + zw + zw + xy) * this.x + (y2 - z2 + w2 - x2) * this.y + (yz + yz - xw - xw) * this.z;
        final double nz = (xz - yw + xz - yw) * this.x + (yz + yz + xw + xw) * this.y + (z2 - y2 - x2 + w2) * this.z;
        dest.x = nx;
        dest.y = ny;
        dest.z = nz;
        return dest;
    }
    
    public Vector3d rotateX(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double y = this.y * cos - this.z * sin;
        final double z = this.y * sin + this.z * cos;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3d rotateX(final double angle, final Vector3d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double y = this.y * cos - this.z * sin;
        final double z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        return dest;
    }
    
    public Vector3d rotateY(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos + this.z * sin;
        final double z = -this.x * sin + this.z * cos;
        this.x = x;
        this.z = z;
        return this;
    }
    
    public Vector3d rotateY(final double angle, final Vector3d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos + this.z * sin;
        final double z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        return dest;
    }
    
    public Vector3d rotateZ(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos - this.y * sin;
        final double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector3d rotateZ(final double angle, final Vector3d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos - this.y * sin;
        final double y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        return dest;
    }
    
    public Vector3d div(final double scalar) {
        final double inv = 1.0 / scalar;
        this.x *= inv;
        this.y *= inv;
        this.z *= inv;
        return this;
    }
    
    public Vector3d div(final double scalar, final Vector3d dest) {
        final double inv = 1.0 / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        dest.z = this.z * inv;
        return dest;
    }
    
    public Vector3d div(final double x, final double y, final double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    public Vector3d div(final double x, final double y, final double z, final Vector3d dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        dest.z = this.z / z;
        return dest;
    }
    
    public double lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
    }
    
    public static double lengthSquared(final double x, final double y, final double z) {
        return Math.fma(x, x, Math.fma(y, y, z * z));
    }
    
    public double length() {
        return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
    }
    
    public static double length(final double x, final double y, final double z) {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
    }
    
    public Vector3d normalize() {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        return this;
    }
    
    public Vector3d normalize(final Vector3d dest) {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        return dest;
    }
    
    public Vector3d normalize(final double length) {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * length;
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        return this;
    }
    
    public Vector3d normalize(final double length, final Vector3d dest) {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * length;
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        return dest;
    }
    
    public Vector3d cross(final Vector3dc v) {
        final double rx = Math.fma(this.y, v.z(), -this.z * v.y());
        final double ry = Math.fma(this.z, v.x(), -this.x * v.z());
        final double rz = Math.fma(this.x, v.y(), -this.y * v.x());
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d cross(final double x, final double y, final double z) {
        final double rx = Math.fma(this.y, z, -this.z * y);
        final double ry = Math.fma(this.z, x, -this.x * z);
        final double rz = Math.fma(this.x, y, -this.y * x);
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3d cross(final Vector3dc v, final Vector3d dest) {
        final double rx = Math.fma(this.y, v.z(), -this.z * v.y());
        final double ry = Math.fma(this.z, v.x(), -this.x * v.z());
        final double rz = Math.fma(this.x, v.y(), -this.y * v.x());
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3d cross(final double x, final double y, final double z, final Vector3d dest) {
        final double rx = Math.fma(this.y, z, -this.z * y);
        final double ry = Math.fma(this.z, x, -this.x * z);
        final double rz = Math.fma(this.x, y, -this.y * x);
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public double distance(final Vector3dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        final double dz = this.z - v.z();
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }
    
    public double distance(final double x, final double y, final double z) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }
    
    public double distanceSquared(final Vector3dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        final double dz = this.z - v.z();
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public double distanceSquared(final double x, final double y, final double z) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public static double distance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return Math.sqrt(distanceSquared(x1, y1, z1, x2, y2, z2));
    }
    
    public static double distanceSquared(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        final double dz = z1 - z2;
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public double dot(final Vector3dc v) {
        return Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), this.z * v.z()));
    }
    
    public double dot(final double x, final double y, final double z) {
        return Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
    }
    
    public double angleCos(final Vector3dc v) {
        final double length1Squared = Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
        final double length2Squared = Math.fma(v.x(), v.x(), Math.fma(v.y(), v.y(), v.z() * v.z()));
        final double dot = Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), this.z * v.z()));
        return dot / Math.sqrt(length1Squared * length2Squared);
    }
    
    public double angle(final Vector3dc v) {
        double cos = this.angleCos(v);
        cos = ((cos < 1.0) ? cos : 1.0);
        cos = ((cos > -1.0) ? cos : -1.0);
        return Math.acos(cos);
    }
    
    public double angleSigned(final Vector3dc v, final Vector3dc n) {
        final double x = v.x();
        final double y = v.y();
        final double z = v.z();
        return Math.atan2((this.y * z - this.z * y) * n.x() + (this.z * x - this.x * z) * n.y() + (this.x * y - this.y * x) * n.z(), this.x * x + this.y * y + this.z * z);
    }
    
    public double angleSigned(final double x, final double y, final double z, final double nx, final double ny, final double nz) {
        return Math.atan2((this.y * z - this.z * y) * nx + (this.z * x - this.x * z) * ny + (this.x * y - this.y * x) * nz, this.x * x + this.y * y + this.z * z);
    }
    
    public Vector3d min(final Vector3dc v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        this.z = ((this.z < v.z()) ? this.z : v.z());
        return this;
    }
    
    public Vector3d min(final Vector3dc v, final Vector3d dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        dest.z = ((this.z < v.z()) ? this.z : v.z());
        return dest;
    }
    
    public Vector3d max(final Vector3dc v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        this.z = ((this.z > v.z()) ? this.z : v.z());
        return this;
    }
    
    public Vector3d max(final Vector3dc v, final Vector3d dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        dest.z = ((this.z > v.z()) ? this.z : v.z());
        return dest;
    }
    
    public Vector3d zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
    }
    
    public Vector3d negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    public Vector3d negate(final Vector3d dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }
    
    public Vector3d absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }
    
    public Vector3d absolute(final Vector3d dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(this.x);
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
        final Vector3d other = (Vector3d)obj;
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }
    
    public boolean equals(final Vector3dc v, final double delta) {
        return this == v || (v != null && v instanceof Vector3dc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta) && Runtime.equals(this.z, v.z(), delta));
    }
    
    public boolean equals(final double x, final double y, final double z) {
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(z);
    }
    
    public Vector3d reflect(final Vector3dc normal) {
        final double x = normal.x();
        final double y = normal.y();
        final double z = normal.z();
        final double dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        this.x -= (dot + dot) * x;
        this.y -= (dot + dot) * y;
        this.z -= (dot + dot) * z;
        return this;
    }
    
    public Vector3d reflect(final double x, final double y, final double z) {
        final double dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        this.x -= (dot + dot) * x;
        this.y -= (dot + dot) * y;
        this.z -= (dot + dot) * z;
        return this;
    }
    
    public Vector3d reflect(final Vector3dc normal, final Vector3d dest) {
        final double x = normal.x();
        final double y = normal.y();
        final double z = normal.z();
        final double dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        dest.x = this.x - (dot + dot) * x;
        dest.y = this.y - (dot + dot) * y;
        dest.z = this.z - (dot + dot) * z;
        return dest;
    }
    
    public Vector3d reflect(final double x, final double y, final double z, final Vector3d dest) {
        final double dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        dest.x = this.x - (dot + dot) * x;
        dest.y = this.y - (dot + dot) * y;
        dest.z = this.z - (dot + dot) * z;
        return dest;
    }
    
    public Vector3d half(final Vector3dc other) {
        return this.set(this).add(other.x(), other.y(), other.z()).normalize();
    }
    
    public Vector3d half(final double x, final double y, final double z) {
        return this.set(this).add(x, y, z).normalize();
    }
    
    public Vector3d half(final Vector3dc other, final Vector3d dest) {
        return dest.set(this).add(other.x(), other.y(), other.z()).normalize();
    }
    
    public Vector3d half(final double x, final double y, final double z, final Vector3d dest) {
        return dest.set(this).add(x, y, z).normalize();
    }
    
    public Vector3d smoothStep(final Vector3dc v, final double t, final Vector3d dest) {
        final double t2 = t * t;
        final double t3 = t2 * t;
        dest.x = (this.x + this.x - v.x() - v.x()) * t3 + (3.0 * v.x() - 3.0 * this.x) * t2 + this.x * t + this.x;
        dest.y = (this.y + this.y - v.y() - v.y()) * t3 + (3.0 * v.y() - 3.0 * this.y) * t2 + this.y * t + this.y;
        dest.z = (this.z + this.z - v.z() - v.z()) * t3 + (3.0 * v.z() - 3.0 * this.z) * t2 + this.z * t + this.z;
        return dest;
    }
    
    public Vector3d hermite(final Vector3dc t0, final Vector3dc v1, final Vector3dc t1, final double t, final Vector3d dest) {
        final double t2 = t * t;
        final double t3 = t2 * t;
        dest.x = (this.x + this.x - v1.x() - v1.x() + t1.x() + t0.x()) * t3 + (3.0 * v1.x() - 3.0 * this.x - t0.x() - t0.x() - t1.x()) * t2 + this.x * t + this.x;
        dest.y = (this.y + this.y - v1.y() - v1.y() + t1.y() + t0.y()) * t3 + (3.0 * v1.y() - 3.0 * this.y - t0.y() - t0.y() - t1.y()) * t2 + this.y * t + this.y;
        dest.z = (this.z + this.z - v1.z() - v1.z() + t1.z() + t0.z()) * t3 + (3.0 * v1.z() - 3.0 * this.z - t0.z() - t0.z() - t1.z()) * t2 + this.z * t + this.z;
        return dest;
    }
    
    public Vector3d lerp(final Vector3dc other, final double t) {
        this.x = Math.fma(other.x() - this.x, t, this.x);
        this.y = Math.fma(other.y() - this.y, t, this.y);
        this.z = Math.fma(other.z() - this.z, t, this.z);
        return this;
    }
    
    public Vector3d lerp(final Vector3dc other, final double t, final Vector3d dest) {
        dest.x = Math.fma(other.x() - this.x, t, this.x);
        dest.y = Math.fma(other.y() - this.y, t, this.y);
        dest.z = Math.fma(other.z() - this.z, t, this.z);
        return dest;
    }
    
    public double get(final int component) throws IllegalArgumentException {
        switch (component) {
            case 0: {
                return this.x;
            }
            case 1: {
                return this.y;
            }
            case 2: {
                return this.z;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public Vector3i get(final int mode, final Vector3i dest) {
        dest.x = Math.roundUsing(this.x(), mode);
        dest.y = Math.roundUsing(this.y(), mode);
        dest.z = Math.roundUsing(this.z(), mode);
        return dest;
    }
    
    public Vector3f get(final Vector3f dest) {
        dest.x = (float)this.x();
        dest.y = (float)this.y();
        dest.z = (float)this.z();
        return dest;
    }
    
    public Vector3d get(final Vector3d dest) {
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        return dest;
    }
    
    public int maxComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        final double absZ = Math.abs(this.z);
        if (absX >= absY && absX >= absZ) {
            return 0;
        }
        if (absY >= absZ) {
            return 1;
        }
        return 2;
    }
    
    public int minComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        final double absZ = Math.abs(this.z);
        if (absX < absY && absX < absZ) {
            return 0;
        }
        if (absY < absZ) {
            return 1;
        }
        return 2;
    }
    
    public Vector3d orthogonalize(final Vector3dc v, final Vector3d dest) {
        double rx;
        double ry;
        double rz;
        if (Math.abs(v.x()) > Math.abs(v.z())) {
            rx = -v.y();
            ry = v.x();
            rz = 0.0;
        }
        else {
            rx = 0.0;
            ry = -v.z();
            rz = v.y();
        }
        final double invLen = Math.invsqrt(rx * rx + ry * ry + rz * rz);
        dest.x = rx * invLen;
        dest.y = ry * invLen;
        dest.z = rz * invLen;
        return dest;
    }
    
    public Vector3d orthogonalize(final Vector3dc v) {
        return this.orthogonalize(v, this);
    }
    
    public Vector3d orthogonalizeUnit(final Vector3dc v, final Vector3d dest) {
        return this.orthogonalize(v, dest);
    }
    
    public Vector3d orthogonalizeUnit(final Vector3dc v) {
        return this.orthogonalizeUnit(v, this);
    }
    
    public Vector3d floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
        return this;
    }
    
    public Vector3d floor(final Vector3d dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        dest.z = Math.floor(this.z);
        return dest;
    }
    
    public Vector3d ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);
        return this;
    }
    
    public Vector3d ceil(final Vector3d dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        dest.z = Math.ceil(this.z);
        return dest;
    }
    
    public Vector3d round() {
        this.x = (double)Math.round(this.x);
        this.y = (double)Math.round(this.y);
        this.z = (double)Math.round(this.z);
        return this;
    }
    
    public Vector3d round(final Vector3d dest) {
        dest.x = (double)Math.round(this.x);
        dest.y = (double)Math.round(this.y);
        dest.z = (double)Math.round(this.z);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
