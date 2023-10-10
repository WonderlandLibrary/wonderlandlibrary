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

public class Vector4f implements Externalizable, Cloneable, Vector4fc
{
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public Vector4f() {
        this.w = 1.0f;
    }
    
    public Vector4f(final Vector4fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
    }
    
    public Vector4f(final Vector4ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        this.w = (float)v.w();
    }
    
    public Vector4f(final Vector3fc v, final float w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
    }
    
    public Vector4f(final Vector3ic v, final float w) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        this.w = w;
    }
    
    public Vector4f(final Vector2fc v, final float z, final float w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4f(final Vector2ic v, final float z, final float w) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = z;
        this.w = w;
    }
    
    public Vector4f(final float d) {
        this.x = d;
        this.y = d;
        this.z = d;
        this.w = d;
    }
    
    public Vector4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4f(final float[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
    }
    
    public Vector4f(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4f(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
    }
    
    public Vector4f(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
    }
    
    public Vector4f(final int index, final FloatBuffer buffer) {
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
    
    public float w() {
        return this.w;
    }
    
    public Vector4f set(final Vector4fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = v.w();
        return this;
    }
    
    public Vector4f set(final Vector4ic v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        this.w = (float)v.w();
        return this;
    }
    
    public Vector4f set(final Vector4dc v) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        this.w = (float)v.w();
        return this;
    }
    
    public Vector4f set(final Vector3fc v, final float w) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
        this.w = w;
        return this;
    }
    
    public Vector4f set(final Vector3ic v, final float w) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = (float)v.z();
        this.w = w;
        return this;
    }
    
    public Vector4f set(final Vector2fc v, final float z, final float w) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4f set(final Vector2ic v, final float z, final float w) {
        this.x = (float)v.x();
        this.y = (float)v.y();
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4f set(final float d) {
        this.x = d;
        this.y = d;
        this.z = d;
        this.w = d;
        return this;
    }
    
    public Vector4f set(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }
    
    public Vector4f set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector4f set(final double d) {
        this.x = (float)d;
        this.y = (float)d;
        this.z = (float)d;
        this.w = (float)d;
        return this;
    }
    
    public Vector4f set(final double x, final double y, final double z, final double w) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        this.w = (float)w;
        return this;
    }
    
    public Vector4f set(final float[] xyzw) {
        this.x = xyzw[0];
        this.y = xyzw[1];
        this.z = xyzw[2];
        this.w = xyzw[3];
        return this;
    }
    
    public Vector4f set(final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4f set(final int index, final ByteBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4f set(final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, buffer.position(), buffer);
        return this;
    }
    
    public Vector4f set(final int index, final FloatBuffer buffer) {
        MemUtil.INSTANCE.get(this, index, buffer);
        return this;
    }
    
    public Vector4f setFromAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.get(this, address);
        return this;
    }
    
    public Vector4f setComponent(final int component, final float value) throws IllegalArgumentException {
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
    
    public Vector4fc getToAddress(final long address) {
        if (Options.NO_UNSAFE) {
            throw new UnsupportedOperationException("Not supported when using joml.nounsafe");
        }
        MemUtil.MemUtilUnsafe.put(this, address);
        return this;
    }
    
    public Vector4f sub(final Vector4fc v) {
        this.x -= v.x();
        this.y -= v.y();
        this.z -= v.z();
        this.w -= v.w();
        return this;
    }
    
    public Vector4f sub(final float x, final float y, final float z, final float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }
    
    public Vector4f sub(final Vector4fc v, final Vector4f dest) {
        dest.x = this.x - v.x();
        dest.y = this.y - v.y();
        dest.z = this.z - v.z();
        dest.w = this.w - v.w();
        return dest;
    }
    
    public Vector4f sub(final float x, final float y, final float z, final float w, final Vector4f dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        dest.w = this.w - w;
        return dest;
    }
    
    public Vector4f add(final Vector4fc v) {
        this.x += v.x();
        this.y += v.y();
        this.z += v.z();
        this.w += v.w();
        return this;
    }
    
    public Vector4f add(final Vector4fc v, final Vector4f dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        dest.w = this.w + v.w();
        return dest;
    }
    
    public Vector4f add(final float x, final float y, final float z, final float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }
    
    public Vector4f add(final float x, final float y, final float z, final float w, final Vector4f dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        dest.w = this.w + w;
        return dest;
    }
    
    public Vector4f fma(final Vector4fc a, final Vector4fc b) {
        this.x = Math.fma(a.x(), b.x(), this.x);
        this.y = Math.fma(a.y(), b.y(), this.y);
        this.z = Math.fma(a.z(), b.z(), this.z);
        this.w = Math.fma(a.w(), b.w(), this.w);
        return this;
    }
    
    public Vector4f fma(final float a, final Vector4fc b) {
        this.x = Math.fma(a, b.x(), this.x);
        this.y = Math.fma(a, b.y(), this.y);
        this.z = Math.fma(a, b.z(), this.z);
        this.w = Math.fma(a, b.w(), this.w);
        return this;
    }
    
    public Vector4f fma(final Vector4fc a, final Vector4fc b, final Vector4f dest) {
        dest.x = Math.fma(a.x(), b.x(), this.x);
        dest.y = Math.fma(a.y(), b.y(), this.y);
        dest.z = Math.fma(a.z(), b.z(), this.z);
        dest.w = Math.fma(a.w(), b.w(), this.w);
        return dest;
    }
    
    public Vector4f fma(final float a, final Vector4fc b, final Vector4f dest) {
        dest.x = Math.fma(a, b.x(), this.x);
        dest.y = Math.fma(a, b.y(), this.y);
        dest.z = Math.fma(a, b.z(), this.z);
        dest.w = Math.fma(a, b.w(), this.w);
        return dest;
    }
    
    public Vector4f mulAdd(final Vector4fc a, final Vector4fc b) {
        this.x = Math.fma(this.x, a.x(), b.x());
        this.y = Math.fma(this.y, a.y(), b.y());
        this.z = Math.fma(this.z, a.z(), b.z());
        return this;
    }
    
    public Vector4f mulAdd(final float a, final Vector4fc b) {
        this.x = Math.fma(this.x, a, b.x());
        this.y = Math.fma(this.y, a, b.y());
        this.z = Math.fma(this.z, a, b.z());
        return this;
    }
    
    public Vector4f mulAdd(final Vector4fc a, final Vector4fc b, final Vector4f dest) {
        dest.x = Math.fma(this.x, a.x(), b.x());
        dest.y = Math.fma(this.y, a.y(), b.y());
        dest.z = Math.fma(this.z, a.z(), b.z());
        return dest;
    }
    
    public Vector4f mulAdd(final float a, final Vector4fc b, final Vector4f dest) {
        dest.x = Math.fma(this.x, a, b.x());
        dest.y = Math.fma(this.y, a, b.y());
        dest.z = Math.fma(this.z, a, b.z());
        return dest;
    }
    
    public Vector4f mul(final Vector4fc v) {
        this.x *= v.x();
        this.y *= v.y();
        this.z *= v.z();
        this.w *= v.w();
        return this;
    }
    
    public Vector4f mul(final Vector4fc v, final Vector4f dest) {
        dest.x = this.x * v.x();
        dest.y = this.y * v.y();
        dest.z = this.z * v.z();
        dest.w = this.w * v.w();
        return dest;
    }
    
    public Vector4f div(final Vector4fc v) {
        this.x /= v.x();
        this.y /= v.y();
        this.z /= v.z();
        this.w /= v.w();
        return this;
    }
    
    public Vector4f div(final Vector4fc v, final Vector4f dest) {
        dest.x = this.x / v.x();
        dest.y = this.y / v.y();
        dest.z = this.z / v.z();
        dest.w = this.w / v.w();
        return dest;
    }
    
    public Vector4f mul(final Matrix4fc mat) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, this);
        }
        return this.mulGeneric(mat, this);
    }
    
    public Vector4f mul(final Matrix4fc mat, final Vector4f dest) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffine(mat, dest);
        }
        return this.mulGeneric(mat, dest);
    }
    
    public Vector4f mulTranspose(final Matrix4fc mat) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffineTranspose(mat, this);
        }
        return this.mulGenericTranspose(mat, this);
    }
    
    public Vector4f mulTranspose(final Matrix4fc mat, final Vector4f dest) {
        if ((mat.properties() & 0x2) != 0x0) {
            return this.mulAffineTranspose(mat, dest);
        }
        return this.mulGenericTranspose(mat, dest);
    }
    
    public Vector4f mulAffine(final Matrix4fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w)));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w)));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w)));
        dest.w = w;
        return dest;
    }
    
    private Vector4f mulGeneric(final Matrix4fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w)));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w)));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w)));
        dest.w = Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        return dest;
    }
    
    public Vector4f mulAffineTranspose(final Matrix4fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        dest.w = Math.fma(mat.m30(), x, Math.fma(mat.m31(), y, mat.m32() * z + w));
        return dest;
    }
    
    private Vector4f mulGenericTranspose(final Matrix4fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, Math.fma(mat.m02(), z, mat.m03() * w)));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, Math.fma(mat.m12(), z, mat.m13() * w)));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, Math.fma(mat.m22(), z, mat.m23() * w)));
        dest.w = Math.fma(mat.m30(), x, Math.fma(mat.m31(), y, Math.fma(mat.m32(), z, mat.m33() * w)));
        return dest;
    }
    
    public Vector4f mul(final Matrix4x3fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w)));
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w)));
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w)));
        this.w = w;
        return this;
    }
    
    public Vector4f mul(final Matrix4x3fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w)));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w)));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w)));
        dest.w = w;
        return dest;
    }
    
    public Vector4f mulProject(final Matrix4fc mat, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w))) * invW;
        dest.w = 1.0f;
        return dest;
    }
    
    public Vector4f mulProject(final Matrix4fc mat) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        this.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w))) * invW;
        this.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w))) * invW;
        this.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w))) * invW;
        this.w = 1.0f;
        return this;
    }
    
    public Vector3f mulProject(final Matrix4fc mat, final Vector3f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        final float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w))) * invW;
        return dest;
    }
    
    public Vector4f mul(final float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }
    
    public Vector4f mul(final float scalar, final Vector4f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        dest.w = this.w * scalar;
        return dest;
    }
    
    public Vector4f mul(final float x, final float y, final float z, final float w) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        this.w *= w;
        return this;
    }
    
    public Vector4f mul(final float x, final float y, final float z, final float w, final Vector4f dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        dest.z = this.z * z;
        dest.w = this.w * w;
        return dest;
    }
    
    public Vector4f div(final float scalar) {
        final float inv = 1.0f / scalar;
        this.x *= inv;
        this.y *= inv;
        this.z *= inv;
        this.w *= inv;
        return this;
    }
    
    public Vector4f div(final float scalar, final Vector4f dest) {
        final float inv = 1.0f / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        dest.z = this.z * inv;
        dest.w = this.w * inv;
        return dest;
    }
    
    public Vector4f div(final float x, final float y, final float z, final float w) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        this.w /= w;
        return this;
    }
    
    public Vector4f div(final float x, final float y, final float z, final float w, final Vector4f dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        dest.z = this.z / z;
        dest.w = this.w / w;
        return dest;
    }
    
    public Vector4f rotate(final Quaternionfc quat) {
        return quat.transform(this, this);
    }
    
    public Vector4f rotate(final Quaternionfc quat, final Vector4f dest) {
        return quat.transform(this, dest);
    }
    
    public Vector4f rotateAbout(final float angle, final float x, final float y, final float z) {
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
    
    public Vector4f rotateAxis(final float angle, final float aX, final float aY, final float aZ, final Vector4f dest) {
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
    
    private Vector4f rotateAxisInternal(final float angle, final float aX, final float aY, final float aZ, final Vector4f dest) {
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
    
    public Vector4f rotateX(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float y = this.y * cos - this.z * sin;
        final float z = this.y * sin + this.z * cos;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vector4f rotateX(final float angle, final Vector4f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float y = this.y * cos - this.z * sin;
        final float z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4f rotateY(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos + this.z * sin;
        final float z = -this.x * sin + this.z * cos;
        this.x = x;
        this.z = z;
        return this;
    }
    
    public Vector4f rotateY(final float angle, final Vector4f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos + this.z * sin;
        final float z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        dest.w = this.w;
        return dest;
    }
    
    public Vector4f rotateZ(final float angle) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos - this.y * sin;
        final float y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector4f rotateZ(final float angle, final Vector4f dest) {
        final float sin = Math.sin(angle);
        final float cos = Math.cosFromSin(sin, angle);
        final float x = this.x * cos - this.y * sin;
        final float y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        dest.w = this.w;
        return dest;
    }
    
    public float lengthSquared() {
        return Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w)));
    }
    
    public static float lengthSquared(final float x, final float y, final float z, final float w) {
        return Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w)));
    }
    
    public static float lengthSquared(final int x, final int y, final int z, final int w) {
        return Math.fma((float)x, (float)x, Math.fma((float)y, (float)y, Math.fma((float)z, (float)z, (float)(w * w))));
    }
    
    public float length() {
        return Math.sqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, Math.fma(this.z, this.z, this.w * this.w))));
    }
    
    public static float length(final float x, final float y, final float z, final float w) {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
    }
    
    public Vector4f normalize() {
        final float invLength = 1.0f / this.length();
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4f normalize(final Vector4f dest) {
        final float invLength = 1.0f / this.length();
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public Vector4f normalize(final float length) {
        final float invLength = 1.0f / this.length() * length;
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4f normalize(final float length, final Vector4f dest) {
        final float invLength = 1.0f / this.length() * length;
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public Vector4f normalize3() {
        final float invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
        return this;
    }
    
    public Vector4f normalize3(final Vector4f dest) {
        final float invLength = Math.invsqrt(Math.fma(this.x, this.x, Math.fma(this.y, this.y, this.z * this.z)));
        dest.x = this.x * invLength;
        dest.y = this.y * invLength;
        dest.z = this.z * invLength;
        dest.w = this.w * invLength;
        return dest;
    }
    
    public float distance(final Vector4fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        final float dz = this.z - v.z();
        final float dw = this.w - v.w();
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public float distance(final float x, final float y, final float z, final float w) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        final float dw = this.w - w;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public float distanceSquared(final Vector4fc v) {
        final float dx = this.x - v.x();
        final float dy = this.y - v.y();
        final float dz = this.z - v.z();
        final float dw = this.w - v.w();
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public float distanceSquared(final float x, final float y, final float z, final float w) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        final float dw = this.w - w;
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public static float distance(final float x1, final float y1, final float z1, final float w1, final float x2, final float y2, final float z2, final float w2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        final float dz = z1 - z2;
        final float dw = w1 - w2;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }
    
    public static float distanceSquared(final float x1, final float y1, final float z1, final float w1, final float x2, final float y2, final float z2, final float w2) {
        final float dx = x1 - x2;
        final float dy = y1 - y2;
        final float dz = z1 - z2;
        final float dw = w1 - w2;
        return Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw)));
    }
    
    public float dot(final Vector4fc v) {
        return Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), Math.fma(this.z, v.z(), this.w * v.w())));
    }
    
    public float dot(final float x, final float y, final float z, final float w) {
        return Math.fma(this.x, x, Math.fma(this.y, y, Math.fma(this.z, z, this.w * w)));
    }
    
    public float angleCos(final Vector4fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        final float length1Squared = Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w)));
        final float length2Squared = Math.fma(v.x(), v.x(), Math.fma(v.y(), v.y(), Math.fma(v.z(), v.z(), v.w() * v.w())));
        final float dot = Math.fma(x, v.x(), Math.fma(y, v.y(), Math.fma(z, v.z(), w * v.w())));
        return dot / Math.sqrt(length1Squared * length2Squared);
    }
    
    public float angle(final Vector4fc v) {
        float cos = this.angleCos(v);
        cos = ((cos < 1.0f) ? cos : 1.0f);
        cos = ((cos > -1.0f) ? cos : -1.0f);
        return Math.acos(cos);
    }
    
    public Vector4f zero() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
        return this;
    }
    
    public Vector4f negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    public Vector4f negate(final Vector4f dest) {
        dest.x = -this.x;
        dest.y = -this.y;
        dest.z = -this.z;
        dest.w = -this.w;
        return dest;
    }
    
    public String toString() {
        return Runtime.formatNumbers(this.toString(Options.NUMBER_FORMAT));
    }
    
    public String toString(final NumberFormat formatter) {
        return "(" + Runtime.format(this.x, formatter) + " " + Runtime.format(this.y, formatter) + " " + Runtime.format(this.z, formatter) + " " + Runtime.format(this.w, formatter) + ")";
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.w);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.set(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
    }
    
    public Vector4f min(final Vector4fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        this.x = ((x < v.x()) ? x : v.x());
        this.y = ((y < v.y()) ? y : v.y());
        this.z = ((z < v.z()) ? z : v.z());
        this.w = ((w < v.w()) ? w : v.w());
        return this;
    }
    
    public Vector4f min(final Vector4fc v, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = ((x < v.x()) ? x : v.x());
        dest.y = ((y < v.y()) ? y : v.y());
        dest.z = ((z < v.z()) ? z : v.z());
        dest.w = ((w < v.w()) ? w : v.w());
        return dest;
    }
    
    public Vector4f max(final Vector4fc v) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        this.x = ((x > v.x()) ? x : v.x());
        this.y = ((y > v.y()) ? y : v.y());
        this.z = ((z > v.z()) ? z : v.z());
        this.w = ((w > v.w()) ? w : v.w());
        return this;
    }
    
    public Vector4f max(final Vector4fc v, final Vector4f dest) {
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = ((x > v.x()) ? x : v.x());
        dest.y = ((y > v.y()) ? y : v.y());
        dest.z = ((z > v.z()) ? z : v.z());
        dest.w = ((w > v.w()) ? w : v.w());
        return dest;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.w);
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
        final Vector4f other = (Vector4f)obj;
        return Float.floatToIntBits(this.w) == Float.floatToIntBits(other.w) && Float.floatToIntBits(this.x) == Float.floatToIntBits(other.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }
    
    public boolean equals(final Vector4fc v, final float delta) {
        return this == v || (v != null && v instanceof Vector4fc && Runtime.equals(this.x, v.x(), delta) && Runtime.equals(this.y, v.y(), delta) && Runtime.equals(this.z, v.z(), delta) && Runtime.equals(this.w, v.w(), delta));
    }
    
    public boolean equals(final float x, final float y, final float z, final float w) {
        return Float.floatToIntBits(this.x) == Float.floatToIntBits(x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(z) && Float.floatToIntBits(this.w) == Float.floatToIntBits(w);
    }
    
    public Vector4f smoothStep(final Vector4fc v, final float t, final Vector4f dest) {
        final float t2 = t * t;
        final float t3 = t2 * t;
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = (x + x - v.x() - v.x()) * t3 + (3.0f * v.x() - 3.0f * x) * t2 + x * t + x;
        dest.y = (y + y - v.y() - v.y()) * t3 + (3.0f * v.y() - 3.0f * y) * t2 + y * t + y;
        dest.z = (z + z - v.z() - v.z()) * t3 + (3.0f * v.z() - 3.0f * z) * t2 + z * t + z;
        dest.w = (w + w - v.w() - v.w()) * t3 + (3.0f * v.w() - 3.0f * w) * t2 + w * t + w;
        return dest;
    }
    
    public Vector4f hermite(final Vector4fc t0, final Vector4fc v1, final Vector4fc t1, final float t, final Vector4f dest) {
        final float t2 = t * t;
        final float t3 = t2 * t;
        final float x = this.x;
        final float y = this.y;
        final float z = this.z;
        final float w = this.w;
        dest.x = (x + x - v1.x() - v1.x() + t1.x() + t0.x()) * t3 + (3.0f * v1.x() - 3.0f * x - t0.x() - t0.x() - t1.x()) * t2 + x * t + x;
        dest.y = (y + y - v1.y() - v1.y() + t1.y() + t0.y()) * t3 + (3.0f * v1.y() - 3.0f * y - t0.y() - t0.y() - t1.y()) * t2 + y * t + y;
        dest.z = (z + z - v1.z() - v1.z() + t1.z() + t0.z()) * t3 + (3.0f * v1.z() - 3.0f * z - t0.z() - t0.z() - t1.z()) * t2 + z * t + z;
        dest.w = (w + w - v1.w() - v1.w() + t1.w() + t0.w()) * t3 + (3.0f * v1.w() - 3.0f * w - t0.w() - t0.w() - t1.w()) * t2 + w * t + w;
        return dest;
    }
    
    public Vector4f lerp(final Vector4fc other, final float t) {
        this.x = Math.fma(other.x() - this.x, t, this.x);
        this.y = Math.fma(other.y() - this.y, t, this.y);
        this.z = Math.fma(other.z() - this.z, t, this.z);
        this.w = Math.fma(other.w() - this.w, t, this.w);
        return this;
    }
    
    public Vector4f lerp(final Vector4fc other, final float t, final Vector4f dest) {
        dest.x = Math.fma(other.x() - this.x, t, this.x);
        dest.y = Math.fma(other.y() - this.y, t, this.y);
        dest.z = Math.fma(other.z() - this.z, t, this.z);
        dest.w = Math.fma(other.w() - this.w, t, this.w);
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
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        dest.w = this.w();
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
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        final float absZ = Math.abs(this.z);
        final float absW = Math.abs(this.w);
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
        final float absX = Math.abs(this.x);
        final float absY = Math.abs(this.y);
        final float absZ = Math.abs(this.z);
        final float absW = Math.abs(this.w);
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
    
    public Vector4f floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
        this.w = Math.floor(this.w);
        return this;
    }
    
    public Vector4f floor(final Vector4f dest) {
        dest.x = Math.floor(this.x);
        dest.y = Math.floor(this.y);
        dest.z = Math.floor(this.z);
        dest.w = Math.floor(this.w);
        return dest;
    }
    
    public Vector4f ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);
        this.w = Math.ceil(this.w);
        return this;
    }
    
    public Vector4f ceil(final Vector4f dest) {
        dest.x = Math.ceil(this.x);
        dest.y = Math.ceil(this.y);
        dest.z = Math.ceil(this.z);
        dest.w = Math.ceil(this.w);
        return dest;
    }
    
    public Vector4f round() {
        this.x = (float)Math.round(this.x);
        this.y = (float)Math.round(this.y);
        this.z = (float)Math.round(this.z);
        this.w = (float)Math.round(this.w);
        return this;
    }
    
    public Vector4f round(final Vector4f dest) {
        dest.x = (float)Math.round(this.x);
        dest.y = (float)Math.round(this.y);
        dest.z = (float)Math.round(this.z);
        dest.w = (float)Math.round(this.w);
        return dest;
    }
    
    public boolean isFinite() {
        return Math.isFinite(this.x) && Math.isFinite(this.y) && Math.isFinite(this.z) && Math.isFinite(this.w);
    }
    
    public Vector4f absolute() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        this.w = Math.abs(this.w);
        return this;
    }
    
    public Vector4f absolute(final Vector4f dest) {
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
