// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.io.Externalizable;

public class Vector3f implements Externalizable, Cloneable, Vector3fc
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    
    public Vector3f() {
    }
    
    public Vector3f(final float d) {
        this.x = d;
        this.y = d;
        this.z = d;
    }
    
    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(final Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }
    
    public Vector3f(final Vector3ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
    }
    
    public Vector3f(final Vector2fc v, final float z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }
    
    public Vector3f(final Vector2ic v, final float z) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = z;
    }
    
    public Vector3f(final float[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }
    
    public Vector3f(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3f(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector3f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector3f(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    public Vector3f set(final Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        return this;
    }
    
    public Vector3f set(final Vector3dc v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        return this;
    }
    
    public Vector3f set(final Vector3ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        return this;
    }
    
    public Vector3f set(final Vector2fc v, final float z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        return this;
    }
    
    public Vector3f set(final Vector2dc v, final float z) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = z;
        return this;
    }
    
    public Vector3f set(final Vector2ic v, final float z) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = z;
        return this;
    }
    
    public Vector3f set(final float d) {
        this.x = d;
        this.y = d;
        this.z = d;
        return this;
    }
    
    public Vector3f set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3f set(final double d) {
        this.x = (float)d;
        this.y = (float)d;
        this.z = (float)d;
        return this;
    }
    
    public Vector3f set(final double x, final double y, final double z) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        return this;
    }
    
    public Vector3f set(final float[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
        return this;
    }
    
    public Vector3f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector3f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector3f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Vector3f setComponent(final int component, final float value) throws IllegalArgumentException {
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
    
    public Vector3fc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector3f sub(final Vector3fc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        return this;
    }
    
    public Vector3f sub(final Vector3fc v, final Vector3f dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        return dest;
    }
    
    public Vector3f sub(final float x, final float y, final float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3f sub(final float x, final float y, final float z, final Vector3f dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        return dest;
    }
    
    public Vector3f add(final Vector3fc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        return this;
    }
    
    public Vector3f add(final Vector3fc v, final Vector3f dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        return dest;
    }
    
    public Vector3f add(final float x, final float y, final float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3f add(final float x, final float y, final float z, final Vector3f dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }
    
    public Vector3f fma(final Vector3fc a, final Vector3fc b) {
        this.x = Math.fma(a.x(), b.x(), this.x);
        this.y = Math.fma(a.y(), b.y(), this.y);
        this.z = Math.fma(a.z(), b.z(), this.z);
        return this;
    }
    
    public Vector3f fma(final float a, final Vector3fc b) {
        this.x = Math.fma(a, b.x(), this.x);
        this.y = Math.fma(a, b.y(), this.y);
        this.z = Math.fma(a, b.z(), this.z);
        return this;
    }
    
    public Vector3f fma(final Vector3fc a, final Vector3fc b, final Vector3f dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        return dest;
    }
    
    public Vector3f fma(final float a, final Vector3fc b, final Vector3f dest) {
        dest.x = Math.fma(a, b.x(), this.x);
        dest.y = Math.fma(a, b.y(), this.y);
        dest.z = Math.fma(a, b.z(), this.z);
        return dest;
    }
    
    public Vector3f mulAdd(final Vector3fc a, final Vector3fc b) {
        this.x = Math.fma(this.x, a.x(), b.x());
        this.y = Math.fma(this.y, a.y(), b.y());
        this.z = Math.fma(this.z, a.z(), b.z());
        return this;
    }
    
    public Vector3f mulAdd(final float a, final Vector3fc b) {
        this.x = Math.fma(this.x, a, b.x());
        this.y = Math.fma(this.y, a, b.y());
        this.z = Math.fma(this.z, a, b.z());
        return this;
    }
    
    public Vector3f mulAdd(final Vector3fc a, final Vector3fc b, final Vector3f dest) {
        dest.x = Math.fma(this.x, a.x(), b.x());
        dest.y = Math.fma(this.y, a.y(), b.y());
        dest.z = Math.fma(this.z, a.z(), b.z());
        return dest;
    }
    
    public Vector3f mulAdd(final float a, final Vector3fc b, final Vector3f dest) {
        dest.x = Math.fma(this.x, a, b.x());
        dest.y = Math.fma(this.y, a, b.y());
        dest.z = Math.fma(this.z, a, b.z());
        return dest;
    }
    
    public Vector3f mul(final Vector3fc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        return this;
    }
    
    public Vector3f mul(final Vector3fc v, final Vector3f dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        return dest;
    }
    
    public Vector3f div(final Vector3fc v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        return this;
    }
    
    public Vector3f div(final Vector3fc v, final Vector3f dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        return dest;
    }
    
    public Vector3f mulProject(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30()))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31()))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32()))) * invW;
        return dest;
    }
    
    public Vector3f mulProject(final Matrix4fc mat, final float w, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w))) * invW;
        return dest;
    }
    
    public Vector3f mulProject(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30()))) * invW;
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31()))) * invW;
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32()))) * invW;
        return this;
    }
    
    public Vector3f mul(final Matrix3fc mat) {
        final float lx = this.x;
        final float ly = this.y;
        final float lz = this.z;
        this.x = Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        this.y = Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        this.z = Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return this;
    }
    
    public Vector3f mul(final Matrix3fc mat, final Vector3f dest) {
        final float lx = this.x;
        final float ly = this.y;
        final float lz = this.z;
        dest.x = Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        dest.y = Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        dest.z = Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return dest;
    }
    
    public Vector3f mul(final Matrix3dc mat) {
        final float lx = this.x;
        final float ly = this.y;
        final float lz = this.z;
        this.x = (float)Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        this.y = (float)Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        this.z = (float)Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return this;
    }
    
    public Vector3f mul(final Matrix3dc mat, final Vector3f dest) {
        final float lx = this.x;
        final float ly = this.y;
        final float lz = this.z;
        dest.x = (float)Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        dest.y = (float)Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        dest.z = (float)Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return dest;
    }
    
    public Vector3f mul(final Matrix3x2fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        this.z = z;
        return this;
    }
    
    public Vector3f mul(final Matrix3x2fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = z;
        return dest;
    }
    
    public Vector3f mulTranspose(final Matrix3fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        this.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        this.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return this;
    }
    
    public Vector3f mulTranspose(final Matrix3fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return dest;
    }
    
    public Vector3f mulPosition(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return this;
    }
    
    public Vector3f mulPosition(final Matrix4x3fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return this;
    }
    
    public Vector3f mulPosition(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return dest;
    }
    
    public Vector3f mulPosition(final Matrix4x3fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return dest;
    }
    
    public Vector3f mulTransposePosition(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, Math.fma(mat.m02(), z, mat.m03())));
        this.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, Math.fma(mat.m12(), z, mat.m13())));
        this.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, Math.fma(mat.m22(), z, mat.m23())));
        return this;
    }
    
    public Vector3f mulTransposePosition(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, Math.fma(mat.m02(), z, mat.m03())));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, Math.fma(mat.m12(), z, mat.m13())));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, Math.fma(mat.m22(), z, mat.m23())));
        return dest;
    }
    
    public float mulPositionW(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return w;
    }
    
    public float mulPositionW(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return w;
    }
    
    public Vector3f mulDirection(final Matrix4dc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = (float)Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        this.y = (float)Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        this.z = (float)Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return this;
    }
    
    public Vector3f mulDirection(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return this;
    }
    
    public Vector3f mulDirection(final Matrix4x3fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return this;
    }
    
    public Vector3f mulDirection(final Matrix4dc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = (float)Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = (float)Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = (float)Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }
    
    public Vector3f mulDirection(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }
    
    public Vector3f mulDirection(final Matrix4x3fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }
    
    public Vector3f mulTransposeDirection(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        this.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        this.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return this;
    }
    
    public Vector3f mulTransposeDirection(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return dest;
    }
    
    public Vector3f mul(final float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vector3f mul(final float scalar, final Vector3f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }
    
    public Vector3f mul(final float x, final float y, final float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vector3f mul(final float x, final float y, final float z, final Vector3f dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        dest.z = this.z * z;
        return dest;
    }
    
    public Vector3f div(final float scalar) {
        final float inv = 1.0f / scalar;
        this.x *= inv;
        this.y *= inv;
        this.z *= inv;
        return this;
    }
    
    public Vector3f div(final float scalar, final Vector3f dest) {
        final float inv = 1.0f / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        dest.z = this.z * inv;
        return dest;
    }
    
    public Vector3f div(final float x, final float y, final float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    public Vector3f div(final float x, final float y, final float z, final Vector3f dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        dest.z = this.z / z;
        return dest;
    }
    
    public Vector3f rotate(final Quaternionfc quat) {
        return quat.transform(this, this);
    }
    
    public Vector3f rotate(final Quaternionfc quat, final Vector3f dest) {
        return quat.transform(this, dest);
    }
    
    public Quaternionf rotationTo(final Vector3fc toDir, final Quaternionf dest) {
        return dest.rotationTo(this, toDir);
    }
    
    public Quaternionf rotationTo(final float toDirX, final float toDirY, final float toDirZ, final Quaternionf dest) {
        return dest.rotationTo(this.x, this.y, this.z, toDirX, toDirY, toDirZ);
    }
    
    public Vector3f rotateAxis(final float angle, final float x, final float y, final float z) {
        if (y == 0.0f && z == 0.0f && Math.absEqualsOne(x)) {
            return this.rotateX(x * angle, this);
        }
        if (x == 0.0f && z == 0.0f && Math.absEqualsOne(y)) {
            return this.rotateY(y * angle, this);
        }
        if (x == 0.0f && y == 0.0f && Math.absEqualsOne(z)) {
            return this.rotateZ(z * angle, this);
        }
        return this.rotateAxisInternal(angle, x, y, z, this);
    }
    
    public Vector3f rotateAxis(final float angle, final float aX, final float aY, final float aZ, final Vector3f dest) {
        if (aY == 0.0f && aZ == 0.0f && Math.absEqualsOne(aX)) {
            return this.rotateX(aX * angle, dest);
        }
        if (aX == 0.0f && aZ == 0.0f && Math.absEqualsOne(aY)) {
            return this.rotateY(aY * angle, dest);
        }
        if (aX == 0.0f && aY == 0.0f && Math.absEqualsOne(aZ)) {
            return this.rotateZ(aZ * angle, dest);
        }
        return this.rotateAxisInternal(angle, aX, aY, aZ, dest);
    }
    
    private Vector3f rotateAxisInternal(final float angle, final float aX, final float aY, final float aZ, final Vector3f dest) {
        final float hangle = angle * 0.5f;
        final float sinAngle = Math.sin(hangle);
        final float qx = aX * sinAngle;
        final float qy = aY * sinAngle;
        final float qz = aZ * sinAngle;
        final float qw = Math.cosFromSin(sinAngle, hangle);
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
        final float x3 = this.x;
        final float y3 = this.y;
        final float z3 = this.z;
        dest.x = (w2 + x2 - z2 - y2) * x3 + (-zw + xy - zw + xy) * y3 + (yw + xz + xz + yw) * z3;
        dest.y = (xy + zw + zw + xy) * x3 + (y2 - z2 + w2 - x2) * y3 + (yz + yz - xw - xw) * z3;
        dest.z = (xz - yw + xz - yw) * x3 + (yz + yz + xw + xw) * y3 + (z2 - y2 - x2 + w2) * z3;
        return dest;
    }
    
    public Vector3f rotateX(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float y = this.y * cos - this.z * sin;
        final float z = this.y * sin + this.z * cos;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector3f rotateX(final float angle, final Vector3f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float y = this.y * cos - this.z * sin;
        final float z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        return dest;
    }
    
    public Vector3f rotateY(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos + this.z * sin;
        final float z = -this.x * sin + this.z * cos;
        this.x = x;
        this.z = z;
        return this;
    }
    
    public Vector3f rotateY(final float angle, final Vector3f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos + this.z * sin;
        final float z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        return dest;
    }
    
    public Vector3f rotateZ(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos - this.y * sin;
        final float y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector3f rotateZ(final float angle, final Vector3f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos - this.y * sin;
        final float y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        return dest;
    }
    
    public float lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z));
    }
    
    public static float lengthSquared(final float x, final float y, final float z) {
        return Math.fma(x, x, Math.fma(y, y, z * z));
    }
    
    public float length() {
        return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
    }
    
    public static float length(final float x, final float y, final float z) {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
    }
    
    public Vector3f normalize() {
        final float scalar = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vector3f normalize(final Vector3f dest) {
        final float scalar = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }
    
    public Vector3f normalize(final float length) {
        final float scalar = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * length;
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    
    public Vector3f normalize(final float length, final Vector3f dest) {
        final float scalar = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z))) * length;
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }
    
    public Vector3f cross(final Vector3fc v) {
        final float rx = Math.fma(this.y, v.z(), -this.z * v.y());
        final float ry = Math.fma(this.z, v.x(), -this.x * v.z());
        final float rz = Math.fma(this.x, v.y(), -this.y * v.x());
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3f cross(final float x, final float y, final float z) {
        final float rx = Math.fma(this.y, z, -this.z * y);
        final float ry = Math.fma(this.z, x, -this.x * z);
        final float rz = Math.fma(this.x, y, -this.y * x);
        this.x = rx;
        this.y = ry;
        this.z = rz;
        return this;
    }
    
    public Vector3f cross(final Vector3fc v, final Vector3f dest) {
        final float rx = Math.fma(this.y, v.z(), -this.z * v.y());
        final float ry = Math.fma(this.z, v.x(), -this.x * v.z());
        final float rz = Math.fma(this.x, v.y(), -this.y * v.x());
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public Vector3f cross(final float x, final float y, final float z, final Vector3f dest) {
        final float rx = Math.fma(this.y, z, -this.z * y);
        final float ry = Math.fma(this.z, x, -this.x * z);
        final float rz = Math.fma(this.x, y, -this.y * x);
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }
    
    public float distance(final Vector3fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        final float dz = this.z - v.z();
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }
    
    public float distance(final float x, final float y, final float z) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }
    
    public float distanceSquared(final Vector3fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        final float dz = this.z - v.z();
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public float distanceSquared(final float x, final float y, final float z) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public static float distance(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        return Math.sqrt(distanceSquared(x1, y1, z1, x2, y2, z2));
    }
    
    public static float distanceSquared(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        final float dz = z1 - z2;
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }
    
    public float dot(final Vector3fc v) {
        return Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), this.z * v.z()));
    }
    
    public float dot(final float x, final float y, final float z) {
        return Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
    }
    
    public float angleCos(final Vector3fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float length1Squared = Math.fma(x, x, Math.fma(y, y, z * z));
        final float length2Squared = Math.fma(v.x(), v.x(), Math.fma(v.y(), v.y(), v.z() * v.z()));
        final float dot = Math.fma(x, v.x(), Math.fma(y, v.y(), z * v.z()));
        return dot / Math.sqrt(length1Squared * length2Squared);
    }
    
    public float angle(final Vector3fc v) {
        float cos = this.angleCos(v);
        cos = ((cos < 1.0f) ? cos : 1.0f);
        cos = ((cos > -1.0f) ? cos : -1.0f);
        return Math.acos(cos);
    }
    
    public float angleSigned(final Vector3fc v, final Vector3fc n) {
        return this.angleSigned(v.x(), v.y(), v.z(), n.x(), n.y(), n.z());
    }
    
    public float angleSigned(final float x, final float y, final float z, final float nx, final float ny, final float nz) {
        final float tx = this.x;
        final float ty = this.y;
        final float tz = this.z;
        return Math.atan2((ty * z - tz * y) * nx + (tz * x - tx * z) * ny + (tx * y - ty * x) * nz, tx * x + ty * y + tz * z);
    }
    
    public Vector3f min(final Vector3fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = ((x < v.x()) ? x : v.x());
        this.y = ((y < v.y()) ? y : v.y());
        this.z = ((z < v.z()) ? z : v.z());
        return this;
    }
    
    public Vector3f min(final Vector3fc v, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = ((x < v.x()) ? x : v.x());
        dest.y = ((y < v.y()) ? y : v.y());
        dest.z = ((z < v.z()) ? z : v.z());
        return dest;
    }
    
    public Vector3f max(final Vector3fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        this.x = ((x > v.x()) ? x : v.x());
        this.y = ((y > v.y()) ? y : v.y());
        this.z = ((z > v.z()) ? z : v.z());
        return this;
    }
    
    public Vector3f max(final Vector3fc v, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        dest.x = ((x > v.x()) ? x : v.x());
        dest.y = ((y > v.y()) ? y : v.y());
        dest.z = ((z > v.z()) ? z : v.z());
        return dest;
    }
    
    public Vector3f zero() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        return this;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.set(in.readFloat(), in.readFloat(), in.readFloat());
    }
    
    public Vector3f negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    public Vector3f negate(final Vector3f dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        return dest;
    }
    
    public Vector3f absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }
    
    public Vector3f absolute(final Vector3f dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final Vector3f other = (Vector3f)obj;
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public boolean equals(final Vector3fc v, final float delta) {
        return this == v || (v != null && v instanceof Vector3fc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta) && Runtime.equals(this.z, v.z(), delta));
    }
    
    public boolean equals(final float x, final float y, final float z) {
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(z);
    }
    
    public Vector3f reflect(final Vector3fc normal) {
        final float x = normal.x();
        final float y = normal.y();
        final float z = normal.z();
        final float dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        this.x -= (dot + dot) * x;
        this.y -= (dot + dot) * y;
        this.z -= (dot + dot) * z;
        return this;
    }
    
    public Vector3f reflect(final float x, final float y, final float z) {
        final float dot = Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
        this.x -= (dot + dot) * x;
        this.y -= (dot + dot) * y;
        this.z -= (dot + dot) * z;
        return this;
    }
    
    public Vector3f reflect(final Vector3fc normal, final Vector3f dest) {
        return this.reflect(normal.x(), normal.y(), normal.z(), dest);
    }
    
    public Vector3f reflect(final float x, final float y, final float z, final Vector3f dest) {
        final float dot = this.dot(x, y, z);
        dest.x = this.x - (dot + dot) * x;
        dest.y = this.y - (dot + dot) * y;
        dest.z = this.z - (dot + dot) * z;
        return dest;
    }
    
    public Vector3f half(final Vector3fc other) {
        return this.set(this).add(other.x(), other.y(), other.z()).normalize();
    }
    
    public Vector3f half(final float x, final float y, final float z) {
        return this.half(x, y, z, this);
    }
    
    public Vector3f half(final Vector3fc other, final Vector3f dest) {
        return this.half(other.x(), other.y(), other.z(), dest);
    }
    
    public Vector3f half(final float x, final float y, final float z, final Vector3f dest) {
        return dest.set(this).add(x, y, z).normalize();
    }
    
    public Vector3f smoothStep(final Vector3fc v, final float t, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float t2 = t * t;
        final float t3 = t2 * t;
        dest.x = (x + x - v.x() - v.x()) * t3 + (3.0f * v.x() - 3.0f * x) * t2 + x * t + x;
        dest.y = (y + y - v.y() - v.y()) * t3 + (3.0f * v.y() - 3.0f * y) * t2 + y * t + y;
        dest.z = (z + z - v.z() - v.z()) * t3 + (3.0f * v.z() - 3.0f * z) * t2 + z * t + z;
        return dest;
    }
    
    public Vector3f hermite(final Vector3fc t0, final Vector3fc v1, final Vector3fc t1, final float t, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float t2 = t * t;
        final float t3 = t2 * t;
        dest.x = (x + x - v1.x() - v1.x() + t1.x() + t0.x()) * t3 + (3.0f * v1.x() - 3.0f * x - t0.x() - t0.x() - t1.x()) * t2 + x * t + x;
        dest.y = (y + y - v1.y() - v1.y() + t1.y() + t0.y()) * t3 + (3.0f * v1.y() - 3.0f * y - t0.y() - t0.y() - t1.y()) * t2 + y * t + y;
        dest.z = (z + z - v1.z() - v1.z() + t1.z() + t0.z()) * t3 + (3.0f * v1.z() - 3.0f * z - t0.z() - t0.z() - t1.z()) * t2 + z * t + z;
        return dest;
    }
    
    public Vector3f lerp(final Vector3fc other, final float t) {
        return this.lerp(other, t, this);
    }
    
    public Vector3f lerp(final Vector3fc other, final float t, final Vector3f dest) {
        dest.x = Math.fma(other.x() - this.x, t, this.x);
        dest.y = Math.fma(other.y() - this.y, t, this.y);
        dest.z = Math.fma(other.z() - this.z, t, this.z);
        return dest;
    }
    
    public float get(final int component) throws IllegalArgumentException {
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
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        return dest;
    }
    
    public Vector3d get(final Vector3d dest) {
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        return dest;
    }
    
    public int maxComponent() {
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        final float absZ = Math.abs(this.z);
        if (absX >= absY && absX >= absZ) {
            return 0;
        }
        if (absY >= absZ) {
            return 1;
        }
        return 2;
    }
    
    public int minComponent() {
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        final float absZ = Math.abs(this.z);
        if (absX < absY && absX < absZ) {
            return 0;
        }
        if (absY < absZ) {
            return 1;
        }
        return 2;
    }
    
    public Vector3f orthogonalize(final Vector3fc v, final Vector3f dest) {
        float rx;
        float ry;
        float rz;
        if (Math.abs(v.x()) > Math.abs(v.z())) {
            rx = -v.y();
            ry = v.x();
            rz = 0.0f;
        }
        else {
            rx = 0.0f;
            ry = -v.z();
            rz = v.y();
        }
        final float invLen = Math.invsqrt(rx * rx + ry * ry + rz * rz);
        dest.x = rx * invLen;
        dest.y = ry * invLen;
        dest.z = rz * invLen;
        return dest;
    }
    
    public Vector3f orthogonalize(final Vector3fc v) {
        return this.orthogonalize(v, this);
    }
    
    public Vector3f orthogonalizeUnit(final Vector3fc v, final Vector3f dest) {
        return this.orthogonalize(v, dest);
    }
    
    public Vector3f orthogonalizeUnit(final Vector3fc v) {
        return this.orthogonalizeUnit(v, this);
    }
    
    public Vector3f floor() {
        return this.floor(this);
    }
    
    public Vector3f floor(final Vector3f dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        dest.z = Math.floor(this.z);
        return dest;
    }
    
    public Vector3f ceil() {
        return this.ceil(this);
    }
    
    public Vector3f ceil(final Vector3f dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        dest.z = Math.ceil(this.z);
        return dest;
    }
    
    public Vector3f round() {
        return this.round(this);
    }
    
    public Vector3f round(final Vector3f dest) {
        dest.x = (float)Math.round(this.x);
        dest.y = (float)Math.round(this.y);
        dest.z = (float)Math.round(this.z);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z);
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
