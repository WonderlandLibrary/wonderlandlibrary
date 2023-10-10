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

public class Vector4d implements Externalizable, Cloneable, Vector4dc
{
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;
    public double z;
    public double w;
    
    public Vector4d() {
        this.w = 1.0;
    }
    
    public Vector4d(final Vector4dc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
    }
    
    public Vector4d(final Vector4ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
    }
    
    public Vector4d(final Vector3dc v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }
    
    public Vector4d(final Vector3ic v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }
    
    public Vector4d(final Vector2dc v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4d(final Vector2ic v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4d(final double d) {
        this.x = d;
        this.y = d;
        this.z = d;
        this.w = d;
    }
    
    public Vector4d(final Vector4fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
    }
    
    public Vector4d(final Vector3fc v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }
    
    public Vector4d(final Vector2fc v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4d(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4d(final float[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
    }
    
    public Vector4d(final double[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
    }
    
    public Vector4d(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4d(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector4d(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4d(final int index, final DoubleBuffer buffer) {
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
    
    public double w() {
        return this.w;
    }
    
    public Vector4d set(final Vector4dc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }
    
    public Vector4d set(final Vector4fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }
    
    public Vector4d set(final Vector4ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }
    
    public Vector4d set(final Vector3dc v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
        return this;
    }
    
    public Vector4d set(final Vector3ic v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
        return this;
    }
    
    public Vector4d set(final Vector3fc v, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
        return this;
    }
    
    public Vector4d set(final Vector2dc v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4d set(final Vector2ic v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4d set(final double d) {
        this.x = d;
        this.y = d;
        this.z = d;
        this.w = d;
        return this;
    }
    
    public Vector4d set(final Vector2fc v, final double z, final double w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4d set(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4d set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector4d set(final double[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
        return this;
    }
    
    public Vector4d set(final float[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
        return this;
    }
    
    public Vector4d set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4d set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4d set(final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4d set(final int index, final DoubleBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4d setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Vector4d setComponent(final int component, final double value) throws IllegalArgumentException {
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
            case 3: {
                this.w = value;
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
    
    public Vector4dc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector4d sub(final Vector4dc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }
    
    public Vector4d sub(final Vector4dc v, final Vector4d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        dest.w = this.w - v.w();
        return dest;
    }
    
    public Vector4d sub(final Vector4fc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }
    
    public Vector4d sub(final Vector4fc v, final Vector4d dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        dest.w = this.w - v.w();
        return dest;
    }
    
    public Vector4d sub(final double x, final double y, final double z, final double w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }
    
    public Vector4d sub(final double x, final double y, final double z, final double w, final Vector4d dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        dest.w = this.w - w;
        return dest;
    }
    
    public Vector4d add(final Vector4dc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }
    
    public Vector4d add(final Vector4dc v, final Vector4d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        dest.w = this.w + v.w();
        return dest;
    }
    
    public Vector4d add(final Vector4fc v, final Vector4d dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        dest.w = this.w + v.w();
        return dest;
    }
    
    public Vector4d add(final double x, final double y, final double z, final double w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }
    
    public Vector4d add(final double x, final double y, final double z, final double w, final Vector4d dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        dest.w = this.w + w;
        return dest;
    }
    
    public Vector4d add(final Vector4fc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }
    
    public Vector4d fma(final Vector4dc a, final Vector4dc b) {
        this.x = Math.fma(a.x(), b.x(), this.x);
        this.y = Math.fma(a.y(), b.y(), this.y);
        this.z = Math.fma(a.z(), b.z(), this.z);
        this.w = Math.fma(a.w(), b.w(), this.w);
        return this;
    }
    
    public Vector4d fma(final double a, final Vector4dc b) {
        this.x = Math.fma(a, b.x(), this.x);
        this.y = Math.fma(a, b.y(), this.y);
        this.z = Math.fma(a, b.z(), this.z);
        this.w = Math.fma(a, b.w(), this.w);
        return this;
    }
    
    public Vector4d fma(final Vector4dc a, final Vector4dc b, final Vector4d dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        dest.w = Math.fma(a.w(), b.w(), this.w);
        return dest;
    }
    
    public Vector4d fma(final double a, final Vector4dc b, final Vector4d dest) {
        dest.x = Math.fma(a, b.x(), this.x);
        dest.y = Math.fma(a, b.y(), this.y);
        dest.z = Math.fma(a, b.z(), this.z);
        dest.w = Math.fma(a, b.w(), this.w);
        return dest;
    }
    
    public Vector4d mulAdd(final Vector4dc a, final Vector4dc b) {
        this.x = Math.fma(this.x, a.x(), b.x());
        this.y = Math.fma(this.y, a.y(), b.y());
        this.z = Math.fma(this.z, a.z(), b.z());
        return this;
    }
    
    public Vector4d mulAdd(final double a, final Vector4dc b) {
        this.x = Math.fma(this.x, a, b.x());
        this.y = Math.fma(this.y, a, b.y());
        this.z = Math.fma(this.z, a, b.z());
        return this;
    }
    
    public Vector4d mulAdd(final Vector4dc a, final Vector4dc b, final Vector4d dest) {
        dest.x = Math.fma(this.x, a.x(), b.x());
        dest.y = Math.fma(this.y, a.y(), b.y());
        dest.z = Math.fma(this.z, a.z(), b.z());
        return dest;
    }
    
    public Vector4d mulAdd(final double a, final Vector4dc b, final Vector4d dest) {
        dest.x = Math.fma(this.x, a, b.x());
        dest.y = Math.fma(this.y, a, b.y());
        dest.z = Math.fma(this.z, a, b.z());
        return dest;
    }
    
    public Vector4d mul(final Vector4dc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        this.w *= v.w();
        return this;
    }
    
    public Vector4d mul(final Vector4dc v, final Vector4d dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        dest.w = this.w * v.w();
        return dest;
    }
    
    public Vector4d div(final Vector4dc v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        this.w /= v.w();
        return this;
    }
    
    public Vector4d div(final Vector4dc v, final Vector4d dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        dest.w = this.w / v.w();
        return dest;
    }
    
    public Vector4d mul(final Vector4fc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        this.w *= v.w();
        return this;
    }
    
    public Vector4d mul(final Vector4fc v, final Vector4d dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        dest.w = this.w * v.w();
        return dest;
    }
    
    public Vector4d mul(final Matrix4dc mat) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, this);
        }
        return this.mulGeneric(mat, this);
    }
    
    public Vector4d mul(final Matrix4dc mat, final Vector4d dest) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, dest);
        }
        return this.mulGeneric(mat, dest);
    }
    
    public Vector4d mulTranspose(final Matrix4dc mat) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffineTranspose(mat, this);
        }
        return this.mulGenericTranspose(mat, this);
    }
    
    public Vector4d mulTranspose(final Matrix4dc mat, final Vector4d dest) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffineTranspose(mat, dest);
        }
        return this.mulGenericTranspose(mat, dest);
    }
    
    public Vector4d mulAffine(final Matrix4dc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = this.w;
        return dest;
    }
    
    private Vector4d mulGeneric(final Matrix4dc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        final double rw = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = rw;
        return dest;
    }
    
    public Vector4d mulAffineTranspose(final Matrix4dc mat, final Vector4d dest) {
        final double x = this.x;
        final double y = this.y;
        final double z = this.z;
        final double w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        dest.w = Math.fma(mat.m30(), x, Math.fma(mat.m31(), y, mat.m32() * z + w));
        return dest;
    }
    
    private Vector4d mulGenericTranspose(final Matrix4dc mat, final Vector4d dest) {
        final double x = this.x;
        final double y = this.y;
        final double z = this.z;
        final double w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, Math.fma(mat.m02(), z, mat.m03() * w)));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, Math.fma(mat.m12(), z, mat.m13() * w)));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, Math.fma(mat.m22(), z, mat.m23() * w)));
        dest.w = Math.fma(mat.m30(), x, Math.fma(mat.m31(), y, Math.fma(mat.m32(), z, mat.m33() * w)));
        return dest;
    }
    
    public Vector4d mul(final Matrix4x3dc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector4d mul(final Matrix4x3dc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4d mul(final Matrix4x3fc mat) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector4d mul(final Matrix4x3fc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4d mul(final Matrix4fc mat) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, this);
        }
        return this.mulGeneric(mat, this);
    }
    
    public Vector4d mul(final Matrix4fc mat, final Vector4d dest) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, dest);
        }
        return this.mulGeneric(mat, dest);
    }
    
    private Vector4d mulAffine(final Matrix4fc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = this.w;
        return dest;
    }
    
    private Vector4d mulGeneric(final Matrix4fc mat, final Vector4d dest) {
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w)));
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w)));
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w)));
        final double rw = Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * this.w)));
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = rw;
        return dest;
    }
    
    public Vector4d mulProject(final Matrix4dc mat, final Vector4d dest) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * this.w)));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w))) * invW;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        dest.w = 1.0;
        return dest;
    }
    
    public Vector4d mulProject(final Matrix4dc mat) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * this.w)));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w))) * invW;
        this.x = rx;
        this.y = ry;
        this.z = rz;
        this.w = 1.0;
        return this;
    }
    
    public Vector3d mulProject(final Matrix4dc mat, final Vector3d dest) {
        final double invW = 1.0 / Math.fma(mat.m03(), this.x, Math.fma(mat.m13(), this.y, Math.fma(mat.m23(), this.z, mat.m33() * this.w)));
        final double rx = Math.fma(mat.m00(), this.x, Math.fma(mat.m10(), this.y, Math.fma(mat.m20(), this.z, mat.m30() * this.w))) * invW;
        final double ry = Math.fma(mat.m01(), this.x, Math.fma(mat.m11(), this.y, Math.fma(mat.m21(), this.z, mat.m31() * this.w))) * invW;
        final double rz = Math.fma(mat.m02(), this.x, Math.fma(mat.m12(), this.y, Math.fma(mat.m22(), this.z, mat.m32() * this.w))) * invW;
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector4d mul(final double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
    
    public Vector4d mul(final double scalar, final Vector4d dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        dest.w = this.w * scalar;
        return dest;
    }
    
    public Vector4d div(final double scalar) {
        final double inv = 1.0 / scalar;
        this.x *= inv;
        this.y *= inv;
        this.z *= inv;
        this.w *= inv;
        return this;
    }
    
    public Vector4d div(final double scalar, final Vector4d dest) {
        final double inv = 1.0 / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        dest.z = this.z * inv;
        dest.w = this.w * inv;
        return dest;
    }
    
    public Vector4d rotate(final Quaterniondc quat) {
        quat.transform(this, this);
        return this;
    }
    
    public Vector4d rotate(final Quaterniondc quat, final Vector4d dest) {
        quat.transform(this, dest);
        return dest;
    }
    
    public Vector4d rotateAxis(final double angle, final double x, final double y, final double z) {
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
    
    public Vector4d rotateAxis(final double angle, final double aX, final double aY, final double aZ, final Vector4d dest) {
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
    
    private Vector4d rotateAxisInternal(final double angle, final double aX, final double aY, final double aZ, final Vector4d dest) {
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
    
    public Vector4d rotateX(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double y = this.y * cos - this.z * sin;
        final double z = this.y * sin + this.z * cos;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector4d rotateX(final double angle, final Vector4d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double y = this.y * cos - this.z * sin;
        final double z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4d rotateY(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos + this.z * sin;
        final double z = -this.x * sin + this.z * cos;
        this.x = x;
        this.z = z;
        return this;
    }
    
    public Vector4d rotateY(final double angle, final Vector4d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos + this.z * sin;
        final double z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4d rotateZ(final double angle) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos - this.y * sin;
        final double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector4d rotateZ(final double angle, final Vector4d dest) {
        final double sin = Math.sin(angle);
        final double cos = Math.cosFromSin(sin, angle);
        final double x = this.x * cos - this.y * sin;
        final double y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        dest.w = this.w;
        return dest;
    }
    
    public double lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
    }
    
    public static double lengthSquared(final double x, final double y, final double z, final double w) {
        return Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w)));
    }
    
    public double length() {
        return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
    }
    
    public static double length(final double x, final double y, final double z, final double w) {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
    }
    
    public Vector4d normalize() {
        final double invLength = 1.0 / this.length();
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4d normalize(final Vector4d dest) {
        final double invLength = 1.0 / this.length();
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public Vector4d normalize(final double length) {
        final double invLength = 1.0 / this.length() * length;
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4d normalize(final double length, final Vector4d dest) {
        final double invLength = 1.0 / this.length() * length;
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public Vector4d normalize3() {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4d normalize3(final Vector4d dest) {
        final double invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public double distance(final Vector4dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        final double dz = this.z - v.z();
        final double dw = this.w - v.w();
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public double distance(final double x, final double y, final double z, final double w) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        final double dw = this.w - w;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public double distanceSquared(final Vector4dc v) {
        final double dx = this.x - v.x();
        final double dy = this.y - v.y();
        final double dz = this.z - v.z();
        final double dw = this.w - v.w();
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public double distanceSquared(final double x, final double y, final double z, final double w) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        final double dw = this.w - w;
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public static double distance(final double x1, final double y1, final double z1, final double w1, final double x2, final double y2, final double z2, final double w2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        final double dz = z1 - z2;
        final double dw = w1 - w2;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public static double distanceSquared(final double x1, final double y1, final double z1, final double w1, final double x2, final double y2, final double z2, final double w2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        final double dz = z1 - z2;
        final double dw = w1 - w2;
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public double dot(final Vector4dc v) {
        return Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), Math.fma(this.z, v.z(), this.w * v.w())));
    }
    
    public double dot(final double x, final double y, final double z, final double w) {
        return Math.fma(this.x, x, Math.fma(this.y, y, Math.fma(this.z, z, this.w * w)));
    }
    
    public double angleCos(final Vector4dc v) {
        final double length1Squared = Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
        final double length2Squared = Math.fma(v.x(), v.x(), Math.fma(v.y(), v.y(), Math.fma(v.z(), v.z(), v.w() * v.w())));
        final double dot = Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), Math.fma(this.z, v.z(), this.w * v.w())));
        return dot / Math.sqrt(length1Squared * length2Squared);
    }
    
    public double angle(final Vector4dc v) {
        double cos = this.angleCos(v);
        cos = ((cos < 1.0) ? cos : 1.0);
        cos = ((cos > -1.0) ? cos : -1.0);
        return Math.acos(cos);
    }
    
    public Vector4d zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.w = 0.0;
        return this;
    }
    
    public Vector4d negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    public Vector4d negate(final Vector4d dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = -this.w;
        return dest;
    }
    
    public Vector4d min(final Vector4dc v) {
        this.x = ((this.x < v.x()) ? this.x : v.x());
        this.y = ((this.y < v.y()) ? this.y : v.y());
        this.z = ((this.z < v.z()) ? this.z : v.z());
        this.w = ((this.w < v.w()) ? this.w : v.w());
        return this;
    }
    
    public Vector4d min(final Vector4dc v, final Vector4d dest) {
        dest.x = ((this.x < v.x()) ? this.x : v.x());
        dest.y = ((this.y < v.y()) ? this.y : v.y());
        dest.z = ((this.z < v.z()) ? this.z : v.z());
        dest.w = ((this.w < v.w()) ? this.w : v.w());
        return dest;
    }
    
    public Vector4d max(final Vector4dc v) {
        this.x = ((this.x > v.x()) ? this.x : v.x());
        this.y = ((this.y > v.y()) ? this.y : v.y());
        this.z = ((this.z > v.z()) ? this.z : v.z());
        this.w = ((this.w > v.w()) ? this.w : v.w());
        return this;
    }
    
    public Vector4d max(final Vector4dc v, final Vector4d dest) {
        dest.x = ((this.x > v.x()) ? this.x : v.x());
        dest.y = ((this.y > v.y()) ? this.y : v.y());
        dest.z = ((this.z > v.z()) ? this.z : v.z());
        dest.w = ((this.w > v.w()) ? this.w : v.w());
        return dest;
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
        final Vector4d other = (Vector4d)obj;
        return Double.doubleToLongBits(this.w) == Double.doubleToLongBits(other.w) && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }
    
    public boolean equals(final Vector4dc v, final double delta) {
        return this == v || (v != null && v instanceof Vector4dc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta) && Runtime.equals(this.z, v.z(), delta) && Runtime.equals(this.w, v.w(), delta));
    }
    
    public boolean equals(final double x, final double y, final double z, final double w) {
        return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(z) && Double.doubleToLongBits(this.w) == Double.doubleToLongBits(w);
    }
    
    public Vector4d smoothStep(final Vector4dc v, final double t, final Vector4d dest) {
        final double t2 = t * t;
        final double t3 = t2 * t;
        dest.x = (this.x + this.x - v.x() - v.x()) * t3 + (3.0 * v.x() - 3.0 * this.x) * t2 + this.x * t + this.x;
        dest.y = (this.y + this.y - v.y() - v.y()) * t3 + (3.0 * v.y() - 3.0 * this.y) * t2 + this.y * t + this.y;
        dest.z = (this.z + this.z - v.z() - v.z()) * t3 + (3.0 * v.z() - 3.0 * this.z) * t2 + this.z * t + this.z;
        dest.w = (this.w + this.w - v.w() - v.w()) * t3 + (3.0 * v.w() - 3.0 * this.w) * t2 + this.w * t + this.w;
        return dest;
    }
    
    public Vector4d hermite(final Vector4dc t0, final Vector4dc v1, final Vector4dc t1, final double t, final Vector4d dest) {
        final double t2 = t * t;
        final double t3 = t2 * t;
        dest.x = (this.x + this.x - v1.x() - v1.x() + t1.x() + t0.x()) * t3 + (3.0 * v1.x() - 3.0 * this.x - t0.x() - t0.x() - t1.x()) * t2 + this.x * t + this.x;
        dest.y = (this.y + this.y - v1.y() - v1.y() + t1.y() + t0.y()) * t3 + (3.0 * v1.y() - 3.0 * this.y - t0.y() - t0.y() - t1.y()) * t2 + this.y * t + this.y;
        dest.z = (this.z + this.z - v1.z() - v1.z() + t1.z() + t0.z()) * t3 + (3.0 * v1.z() - 3.0 * this.z - t0.z() - t0.z() - t1.z()) * t2 + this.z * t + this.z;
        dest.w = (this.w + this.w - v1.w() - v1.w() + t1.w() + t0.w()) * t3 + (3.0 * v1.w() - 3.0 * this.w - t0.w() - t0.w() - t1.w()) * t2 + this.w * t + this.w;
        return dest;
    }
    
    public Vector4d lerp(final Vector4dc other, final double t) {
        this.x = Math.fma(other.x() - this.x, t, this.x);
        this.y = Math.fma(other.y() - this.y, t, this.y);
        this.z = Math.fma(other.z() - this.z, t, this.z);
        this.w = Math.fma(other.w() - this.w, t, this.w);
        return this;
    }
    
    public Vector4d lerp(final Vector4dc other, final double t, final Vector4d dest) {
        dest.x = Math.fma(other.x() - this.x, t, this.x);
        dest.y = Math.fma(other.y() - this.y, t, this.y);
        dest.z = Math.fma(other.z() - this.z, t, this.z);
        dest.w = Math.fma(other.w() - this.w, t, this.w);
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
            case 3: {
                return this.w;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public Vector4i get(final int mode, final Vector4i dest) {
        dest.x = Math.roundUsing(this.x(), mode);
        dest.y = Math.roundUsing(this.y(), mode);
        dest.z = Math.roundUsing(this.z(), mode);
        dest.w = Math.roundUsing(this.w(), mode);
        return dest;
    }
    
    public Vector4f get(final Vector4f dest) {
        dest.x = (float)this.x();
        dest.y = (float)this.y();
        dest.z = (float)this.z();
        dest.w = (float)this.w();
        return dest;
    }
    
    public Vector4d get(final Vector4d dest) {
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        dest.w = this.w();
        return dest;
    }
    
    public int maxComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        final double absZ = Math.abs(this.z);
        final double absW = Math.abs(this.w);
        if (absX >= absY && absX >= absZ && absX >= absW) {
            return 0;
        }
        if (absY >= absZ && absY >= absW) {
            return 1;
        }
        if (absZ >= absW) {
            return 2;
        }
        return 3;
    }
    
    public int minComponent() {
        final double absX = Math.abs(this.x);
        final double absY = Math.abs(this.y);
        final double absZ = Math.abs(this.z);
        final double absW = Math.abs(this.w);
        if (absX < absY && absX < absZ && absX < absW) {
            return 0;
        }
        if (absY < absZ && absY < absW) {
            return 1;
        }
        if (absZ < absW) {
            return 2;
        }
        return 3;
    }
    
    public Vector4d floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
        this.w = Math.floor(this.w);
        return this;
    }
    
    public Vector4d floor(final Vector4d dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        dest.z = Math.floor(this.z);
        dest.w = Math.floor(this.w);
        return dest;
    }
    
    public Vector4d ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);
        this.w = Math.ceil(this.w);
        return this;
    }
    
    public Vector4d ceil(final Vector4d dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        dest.z = Math.ceil(this.z);
        dest.w = Math.ceil(this.w);
        return dest;
    }
    
    public Vector4d round() {
        this.x = (double)Math.round(this.x);
        this.y = (double)Math.round(this.y);
        this.z = (double)Math.round(this.z);
        this.w = (double)Math.round(this.w);
        return this;
    }
    
    public Vector4d round(final Vector4d dest) {
        dest.x = (double)Math.round(this.x);
        dest.y = (double)Math.round(this.y);
        dest.z = (double)Math.round(this.z);
        dest.w = (double)Math.round(this.w);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
    }
    
    public Vector4d absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
        return this;
    }
    
    public Vector4d absolute(final Vector4d dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        dest.w = Math.abs(this.w);
        return dest;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
