// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import sun.misc.Unsafe;
import java.nio.IntBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

abstract class MemUtil
{
    public static final MemUtil INSTANCE;
    
    private static MemUtil createInstance() {
        MemUtil accessor;
        try {
            if (Options.NO_UNSAFE && Options.FORCE_UNSAFE) {
                throw new ConfigurationException("Cannot enable both -Djoml.nounsafe and -Djoml.forceUnsafe", null);
            }
            if (Options.NO_UNSAFE) {
                accessor = new MemUtilNIO();
            }
            else {
                accessor = new MemUtilUnsafe();
            }
        }
        catch (Throwable e) {
            if (Options.FORCE_UNSAFE) {
                throw new ConfigurationException("Unsafe is not supported but its use was forced via -Djoml.forceUnsafe", e);
            }
            accessor = new MemUtilNIO();
        }
        return accessor;
    }
    
    public abstract void put(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix4x3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Matrix4x3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x4(final Matrix4x3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put4x4(final Matrix4x3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x4(final Matrix4x3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put4x4(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x4(final Matrix3x2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put4x4(final Matrix3x2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x4(final Matrix3x2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put4x4(final Matrix3x2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put3x3(final Matrix3x2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put3x3(final Matrix3x2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put3x3(final Matrix3x2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put3x3(final Matrix3x2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x3(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put4x3(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put3x4(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put3x4(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put3x4(final Matrix4x3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put3x4(final Matrix4x3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put3x4(final Matrix3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put3x4(final Matrix3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void putTransposed(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x3Transposed(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put4x3Transposed(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix4x3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void putTransposed(final Matrix4x3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void putTransposed(final Matrix3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void putTransposed(final Matrix2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix4x3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Matrix4d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putf(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Matrix4x3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putf(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void putTransposed(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put4x3Transposed(final Matrix4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put4x3Transposed(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix4x3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void putTransposed(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putTransposed(final Matrix2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void putTransposed(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putfTransposed(final Matrix4d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putfTransposed(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putfTransposed(final Matrix4x3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putfTransposed(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putfTransposed(final Matrix2d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putfTransposed(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Matrix3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Matrix3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Matrix3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putf(final Matrix3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix3x2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Matrix3x2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix3x2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Matrix3x2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Matrix2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Matrix2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Matrix2d p0, final int p1, final FloatBuffer p2);
    
    public abstract void putf(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Vector4d p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Vector4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Vector4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Vector4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector4i p0, final int p1, final IntBuffer p2);
    
    public abstract void put(final Vector4i p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Vector3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Vector3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Vector3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void putf(final Vector3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector3i p0, final int p1, final IntBuffer p2);
    
    public abstract void put(final Vector3i p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void put(final Vector2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void put(final Vector2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void put(final Vector2i p0, final int p1, final IntBuffer p2);
    
    public abstract void put(final Vector2i p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void getTransposed(final Matrix4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void getTransposed(final Matrix4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix4x3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Matrix4x3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix4x3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void getf(final Matrix4d p0, final int p1, final FloatBuffer p2);
    
    public abstract void getf(final Matrix4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void getf(final Matrix4x3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void getf(final Matrix4x3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Matrix3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Matrix3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix3x2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Matrix3x2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix3x2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Matrix3x2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void getf(final Matrix3d p0, final int p1, final FloatBuffer p2);
    
    public abstract void getf(final Matrix3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Matrix2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Matrix2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void getf(final Matrix2d p0, final int p1, final FloatBuffer p2);
    
    public abstract void getf(final Matrix2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector4d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Vector4d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector4f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Vector4f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector4i p0, final int p1, final IntBuffer p2);
    
    public abstract void get(final Vector4i p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector3f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Vector3f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector3d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Vector3d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector3i p0, final int p1, final IntBuffer p2);
    
    public abstract void get(final Vector3i p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector2f p0, final int p1, final FloatBuffer p2);
    
    public abstract void get(final Vector2f p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector2d p0, final int p1, final DoubleBuffer p2);
    
    public abstract void get(final Vector2d p0, final int p1, final ByteBuffer p2);
    
    public abstract void get(final Vector2i p0, final int p1, final IntBuffer p2);
    
    public abstract void get(final Vector2i p0, final int p1, final ByteBuffer p2);
    
    public abstract void putMatrix3f(final Quaternionf p0, final int p1, final ByteBuffer p2);
    
    public abstract void putMatrix3f(final Quaternionf p0, final int p1, final FloatBuffer p2);
    
    public abstract void putMatrix4f(final Quaternionf p0, final int p1, final ByteBuffer p2);
    
    public abstract void putMatrix4f(final Quaternionf p0, final int p1, final FloatBuffer p2);
    
    public abstract void putMatrix4x3f(final Quaternionf p0, final int p1, final ByteBuffer p2);
    
    public abstract void putMatrix4x3f(final Quaternionf p0, final int p1, final FloatBuffer p2);
    
    public abstract float get(final Matrix4f p0, final int p1, final int p2);
    
    public abstract Matrix4f set(final Matrix4f p0, final int p1, final int p2, final float p3);
    
    public abstract double get(final Matrix4d p0, final int p1, final int p2);
    
    public abstract Matrix4d set(final Matrix4d p0, final int p1, final int p2, final double p3);
    
    public abstract float get(final Matrix3f p0, final int p1, final int p2);
    
    public abstract Matrix3f set(final Matrix3f p0, final int p1, final int p2, final float p3);
    
    public abstract double get(final Matrix3d p0, final int p1, final int p2);
    
    public abstract Matrix3d set(final Matrix3d p0, final int p1, final int p2, final double p3);
    
    public abstract Vector4f getColumn(final Matrix4f p0, final int p1, final Vector4f p2);
    
    public abstract Matrix4f setColumn(final Vector4f p0, final int p1, final Matrix4f p2);
    
    public abstract Matrix4f setColumn(final Vector4fc p0, final int p1, final Matrix4f p2);
    
    public abstract void copy(final Matrix4f p0, final Matrix4f p1);
    
    public abstract void copy(final Matrix4x3f p0, final Matrix4x3f p1);
    
    public abstract void copy(final Matrix4f p0, final Matrix4x3f p1);
    
    public abstract void copy(final Matrix4x3f p0, final Matrix4f p1);
    
    public abstract void copy(final Matrix3f p0, final Matrix3f p1);
    
    public abstract void copy(final Matrix3f p0, final Matrix4f p1);
    
    public abstract void copy(final Matrix4f p0, final Matrix3f p1);
    
    public abstract void copy(final Matrix3f p0, final Matrix4x3f p1);
    
    public abstract void copy(final Matrix3x2f p0, final Matrix3x2f p1);
    
    public abstract void copy(final Matrix3x2d p0, final Matrix3x2d p1);
    
    public abstract void copy(final Matrix2f p0, final Matrix2f p1);
    
    public abstract void copy(final Matrix2d p0, final Matrix2d p1);
    
    public abstract void copy(final Matrix2f p0, final Matrix3f p1);
    
    public abstract void copy(final Matrix3f p0, final Matrix2f p1);
    
    public abstract void copy(final Matrix2f p0, final Matrix3x2f p1);
    
    public abstract void copy(final Matrix3x2f p0, final Matrix2f p1);
    
    public abstract void copy(final Matrix2d p0, final Matrix3d p1);
    
    public abstract void copy(final Matrix3d p0, final Matrix2d p1);
    
    public abstract void copy(final Matrix2d p0, final Matrix3x2d p1);
    
    public abstract void copy(final Matrix3x2d p0, final Matrix2d p1);
    
    public abstract void copy3x3(final Matrix4f p0, final Matrix4f p1);
    
    public abstract void copy3x3(final Matrix4x3f p0, final Matrix4x3f p1);
    
    public abstract void copy3x3(final Matrix3f p0, final Matrix4x3f p1);
    
    public abstract void copy3x3(final Matrix3f p0, final Matrix4f p1);
    
    public abstract void copy4x3(final Matrix4f p0, final Matrix4f p1);
    
    public abstract void copy4x3(final Matrix4x3f p0, final Matrix4f p1);
    
    public abstract void copy(final float[] p0, final int p1, final Matrix4f p2);
    
    public abstract void copyTransposed(final float[] p0, final int p1, final Matrix4f p2);
    
    public abstract void copy(final float[] p0, final int p1, final Matrix3f p2);
    
    public abstract void copy(final float[] p0, final int p1, final Matrix4x3f p2);
    
    public abstract void copy(final float[] p0, final int p1, final Matrix3x2f p2);
    
    public abstract void copy(final double[] p0, final int p1, final Matrix3x2d p2);
    
    public abstract void copy(final float[] p0, final int p1, final Matrix2f p2);
    
    public abstract void copy(final double[] p0, final int p1, final Matrix2d p2);
    
    public abstract void copy(final Matrix4f p0, final float[] p1, final int p2);
    
    public abstract void copy(final Matrix3f p0, final float[] p1, final int p2);
    
    public abstract void copy(final Matrix4x3f p0, final float[] p1, final int p2);
    
    public abstract void copy(final Matrix3x2f p0, final float[] p1, final int p2);
    
    public abstract void copy(final Matrix3x2d p0, final double[] p1, final int p2);
    
    public abstract void copy(final Matrix2f p0, final float[] p1, final int p2);
    
    public abstract void copy(final Matrix2d p0, final double[] p1, final int p2);
    
    public abstract void copy4x4(final Matrix4x3f p0, final float[] p1, final int p2);
    
    public abstract void copy4x4(final Matrix4x3d p0, final float[] p1, final int p2);
    
    public abstract void copy4x4(final Matrix4x3d p0, final double[] p1, final int p2);
    
    public abstract void copy4x4(final Matrix3x2f p0, final float[] p1, final int p2);
    
    public abstract void copy4x4(final Matrix3x2d p0, final double[] p1, final int p2);
    
    public abstract void copy3x3(final Matrix3x2f p0, final float[] p1, final int p2);
    
    public abstract void copy3x3(final Matrix3x2d p0, final double[] p1, final int p2);
    
    public abstract void identity(final Matrix4f p0);
    
    public abstract void identity(final Matrix4x3f p0);
    
    public abstract void identity(final Matrix3f p0);
    
    public abstract void identity(final Matrix3x2f p0);
    
    public abstract void identity(final Matrix3x2d p0);
    
    public abstract void identity(final Matrix2f p0);
    
    public abstract void swap(final Matrix4f p0, final Matrix4f p1);
    
    public abstract void swap(final Matrix4x3f p0, final Matrix4x3f p1);
    
    public abstract void swap(final Matrix3f p0, final Matrix3f p1);
    
    public abstract void swap(final Matrix2f p0, final Matrix2f p1);
    
    public abstract void swap(final Matrix2d p0, final Matrix2d p1);
    
    public abstract void zero(final Matrix4f p0);
    
    public abstract void zero(final Matrix4x3f p0);
    
    public abstract void zero(final Matrix3f p0);
    
    public abstract void zero(final Matrix3x2f p0);
    
    public abstract void zero(final Matrix3x2d p0);
    
    public abstract void zero(final Matrix2f p0);
    
    public abstract void zero(final Matrix2d p0);
    
    static {
        INSTANCE = createInstance();
    }
    
    public static class MemUtilNIO extends MemUtil
    {
        public void put0(final Matrix4f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, m.m03()).put(4, m.m10()).put(5, m.m11()).put(6, m.m12()).put(7, m.m13()).put(8, m.m20()).put(9, m.m21()).put(10, m.m22()).put(11, m.m23()).put(12, m.m30()).put(13, m.m31()).put(14, m.m32()).put(15, m.m33());
        }
        
        public void putN(final Matrix4f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m03()).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, m.m13()).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, m.m23()).put(offset + 12, m.m30()).put(offset + 13, m.m31()).put(offset + 14, m.m32()).put(offset + 15, m.m33());
        }
        
        public void put(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put0(final Matrix4f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, m.m03()).putFloat(16, m.m10()).putFloat(20, m.m11()).putFloat(24, m.m12()).putFloat(28, m.m13()).putFloat(32, m.m20()).putFloat(36, m.m21()).putFloat(40, m.m22()).putFloat(44, m.m23()).putFloat(48, m.m30()).putFloat(52, m.m31()).putFloat(56, m.m32()).putFloat(60, m.m33());
        }
        
        private void putN(final Matrix4f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, m.m03()).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m12()).putFloat(offset + 28, m.m13()).putFloat(offset + 32, m.m20()).putFloat(offset + 36, m.m21()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, m.m23()).putFloat(offset + 48, m.m30()).putFloat(offset + 52, m.m31()).putFloat(offset + 56, m.m32()).putFloat(offset + 60, m.m33());
        }
        
        public void put(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put4x3_0(final Matrix4f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, m.m10()).put(4, m.m11()).put(5, m.m12()).put(6, m.m20()).put(7, m.m21()).put(8, m.m22()).put(9, m.m30()).put(10, m.m31()).put(11, m.m32());
        }
        
        public void put4x3_N(final Matrix4f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, m.m12()).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, m.m22()).put(offset + 9, m.m30()).put(offset + 10, m.m31()).put(offset + 11, m.m32());
        }
        
        public void put4x3(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put4x3_0(m, dest);
            }
            else {
                this.put4x3_N(m, offset, dest);
            }
        }
        
        public void put4x3_0(final Matrix4f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, m.m10()).putFloat(16, m.m11()).putFloat(20, m.m12()).putFloat(24, m.m20()).putFloat(28, m.m21()).putFloat(32, m.m22()).putFloat(36, m.m30()).putFloat(40, m.m31()).putFloat(44, m.m32());
        }
        
        private void put4x3_N(final Matrix4f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, m.m10()).putFloat(offset + 16, m.m11()).putFloat(offset + 20, m.m12()).putFloat(offset + 24, m.m20()).putFloat(offset + 28, m.m21()).putFloat(offset + 32, m.m22()).putFloat(offset + 36, m.m30()).putFloat(offset + 40, m.m31()).putFloat(offset + 44, m.m32());
        }
        
        public void put4x3(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put4x3_0(m, dest);
            }
            else {
                this.put4x3_N(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix4f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, m.m03()).putFloat(16, m.m10()).putFloat(20, m.m11()).putFloat(24, m.m12()).putFloat(28, m.m13()).putFloat(32, m.m20()).putFloat(36, m.m21()).putFloat(40, m.m22()).putFloat(44, m.m23());
        }
        
        private void put3x4_N(final Matrix4f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, m.m03()).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m12()).putFloat(offset + 28, m.m13()).putFloat(offset + 32, m.m20()).putFloat(offset + 36, m.m21()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, m.m23());
        }
        
        public void put3x4(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix4f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, m.m03()).put(4, m.m10()).put(5, m.m11()).put(6, m.m12()).put(7, m.m13()).put(8, m.m20()).put(9, m.m21()).put(10, m.m22()).put(11, m.m23());
        }
        
        public void put3x4_N(final Matrix4f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m03()).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, m.m13()).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, m.m23());
        }
        
        public void put3x4(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix4x3f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, 0.0f).putFloat(16, m.m10()).putFloat(20, m.m11()).putFloat(24, m.m12()).putFloat(28, 0.0f).putFloat(32, m.m20()).putFloat(36, m.m21()).putFloat(40, m.m22()).putFloat(44, 0.0f);
        }
        
        private void put3x4_N(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, 0.0f).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m12()).putFloat(offset + 28, 0.0f).putFloat(offset + 32, m.m20()).putFloat(offset + 36, m.m21()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, 0.0f);
        }
        
        public void put3x4(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix4x3f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, 0.0f).put(4, m.m10()).put(5, m.m11()).put(6, m.m12()).put(7, 0.0f).put(8, m.m20()).put(9, m.m21()).put(10, m.m22()).put(11, 0.0f);
        }
        
        public void put3x4_N(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, 0.0f).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, 0.0f).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, 0.0f);
        }
        
        public void put3x4(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put0(final Matrix4x3f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, m.m10()).put(4, m.m11()).put(5, m.m12()).put(6, m.m20()).put(7, m.m21()).put(8, m.m22()).put(9, m.m30()).put(10, m.m31()).put(11, m.m32());
        }
        
        public void putN(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, m.m12()).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, m.m22()).put(offset + 9, m.m30()).put(offset + 10, m.m31()).put(offset + 11, m.m32());
        }
        
        public void put(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put0(final Matrix4x3f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, m.m10()).putFloat(16, m.m11()).putFloat(20, m.m12()).putFloat(24, m.m20()).putFloat(28, m.m21()).putFloat(32, m.m22()).putFloat(36, m.m30()).putFloat(40, m.m31()).putFloat(44, m.m32());
        }
        
        public void putN(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, m.m10()).putFloat(offset + 16, m.m11()).putFloat(offset + 20, m.m12()).putFloat(offset + 24, m.m20()).putFloat(offset + 28, m.m21()).putFloat(offset + 32, m.m22()).putFloat(offset + 36, m.m30()).putFloat(offset + 40, m.m31()).putFloat(offset + 44, m.m32());
        }
        
        public void put(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put4x4(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, 0.0f).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, 0.0f).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, 0.0f).put(offset + 12, m.m30()).put(offset + 13, m.m31()).put(offset + 14, m.m32()).put(offset + 15, 1.0f);
        }
        
        public void put4x4(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, 0.0f).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m12()).putFloat(offset + 28, 0.0f).putFloat(offset + 32, m.m20()).putFloat(offset + 36, m.m21()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, 0.0f).putFloat(offset + 48, m.m30()).putFloat(offset + 52, m.m31()).putFloat(offset + 56, m.m32()).putFloat(offset + 60, 1.0f);
        }
        
        public void put4x4(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, 0.0).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, 0.0).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, 0.0).put(offset + 12, m.m30()).put(offset + 13, m.m31()).put(offset + 14, m.m32()).put(offset + 15, 1.0);
        }
        
        public void put4x4(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m02()).putDouble(offset + 24, 0.0).putDouble(offset + 32, m.m10()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, m.m12()).putDouble(offset + 56, 0.0).putDouble(offset + 64, m.m20()).putDouble(offset + 72, m.m21()).putDouble(offset + 80, m.m22()).putDouble(offset + 88, 0.0).putDouble(offset + 96, m.m30()).putDouble(offset + 104, m.m31()).putDouble(offset + 112, m.m32()).putDouble(offset + 120, 1.0);
        }
        
        public void put4x4(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, 0.0f).put(offset + 3, 0.0f).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, 0.0f).put(offset + 7, 0.0f).put(offset + 8, 0.0f).put(offset + 9, 0.0f).put(offset + 10, 1.0f).put(offset + 11, 0.0f).put(offset + 12, m.m20()).put(offset + 13, m.m21()).put(offset + 14, 0.0f).put(offset + 15, 1.0f);
        }
        
        public void put4x4(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, 0.0f).putFloat(offset + 12, 0.0f).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, 0.0f).putFloat(offset + 28, 0.0f).putFloat(offset + 32, 0.0f).putFloat(offset + 36, 0.0f).putFloat(offset + 40, 1.0f).putFloat(offset + 44, 0.0f).putFloat(offset + 48, m.m20()).putFloat(offset + 52, m.m21()).putFloat(offset + 56, 0.0f).putFloat(offset + 60, 1.0f);
        }
        
        public void put4x4(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, 0.0).put(offset + 3, 0.0).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, 0.0).put(offset + 7, 0.0).put(offset + 8, 0.0).put(offset + 9, 0.0).put(offset + 10, 1.0).put(offset + 11, 0.0).put(offset + 12, m.m20()).put(offset + 13, m.m21()).put(offset + 14, 0.0).put(offset + 15, 1.0);
        }
        
        public void put4x4(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, 0.0).putDouble(offset + 24, 0.0).putDouble(offset + 32, m.m10()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, 0.0).putDouble(offset + 56, 0.0).putDouble(offset + 64, 0.0).putDouble(offset + 72, 0.0).putDouble(offset + 80, 1.0).putDouble(offset + 88, 0.0).putDouble(offset + 96, m.m20()).putDouble(offset + 104, m.m21()).putDouble(offset + 112, 0.0).putDouble(offset + 120, 1.0);
        }
        
        public void put3x3(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, 0.0f).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, 0.0f).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, 1.0f);
        }
        
        public void put3x3(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, 0.0f).putFloat(offset + 12, m.m10()).putFloat(offset + 16, m.m11()).putFloat(offset + 20, 0.0f).putFloat(offset + 24, m.m20()).putFloat(offset + 28, m.m21()).putFloat(offset + 32, 1.0f);
        }
        
        public void put3x3(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, 0.0).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, 0.0).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, 1.0);
        }
        
        public void put3x3(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, 0.0).putDouble(offset + 24, m.m10()).putDouble(offset + 32, m.m11()).putDouble(offset + 40, 0.0).putDouble(offset + 48, m.m20()).putDouble(offset + 56, m.m21()).putDouble(offset + 64, 1.0);
        }
        
        private void putTransposedN(final Matrix4f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32()).put(offset + 12, m.m03()).put(offset + 13, m.m13()).put(offset + 14, m.m23()).put(offset + 15, m.m33());
        }
        
        private void putTransposed0(final Matrix4f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m10()).put(2, m.m20()).put(3, m.m30()).put(4, m.m01()).put(5, m.m11()).put(6, m.m21()).put(7, m.m31()).put(8, m.m02()).put(9, m.m12()).put(10, m.m22()).put(11, m.m32()).put(12, m.m03()).put(13, m.m13()).put(14, m.m23()).put(15, m.m33());
        }
        
        public void putTransposed(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.putTransposed0(m, dest);
            }
            else {
                this.putTransposedN(m, offset, dest);
            }
        }
        
        private void putTransposedN(final Matrix4f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m10()).putFloat(offset + 8, m.m20()).putFloat(offset + 12, m.m30()).putFloat(offset + 16, m.m01()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m21()).putFloat(offset + 28, m.m31()).putFloat(offset + 32, m.m02()).putFloat(offset + 36, m.m12()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, m.m32()).putFloat(offset + 48, m.m03()).putFloat(offset + 52, m.m13()).putFloat(offset + 56, m.m23()).putFloat(offset + 60, m.m33());
        }
        
        private void putTransposed0(final Matrix4f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m10()).putFloat(8, m.m20()).putFloat(12, m.m30()).putFloat(16, m.m01()).putFloat(20, m.m11()).putFloat(24, m.m21()).putFloat(28, m.m31()).putFloat(32, m.m02()).putFloat(36, m.m12()).putFloat(40, m.m22()).putFloat(44, m.m32()).putFloat(48, m.m03()).putFloat(52, m.m13()).putFloat(56, m.m23()).putFloat(60, m.m33());
        }
        
        public void putTransposed(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.putTransposed0(m, dest);
            }
            else {
                this.putTransposedN(m, offset, dest);
            }
        }
        
        public void put4x3Transposed(final Matrix4f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32());
        }
        
        public void put4x3Transposed(final Matrix4f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m10()).putFloat(offset + 8, m.m20()).putFloat(offset + 12, m.m30()).putFloat(offset + 16, m.m01()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m21()).putFloat(offset + 28, m.m31()).putFloat(offset + 32, m.m02()).putFloat(offset + 36, m.m12()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, m.m32());
        }
        
        public void putTransposed(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32());
        }
        
        public void putTransposed(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m10()).putFloat(offset + 8, m.m20()).putFloat(offset + 12, m.m30()).putFloat(offset + 16, m.m01()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m21()).putFloat(offset + 28, m.m31()).putFloat(offset + 32, m.m02()).putFloat(offset + 36, m.m12()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, m.m32());
        }
        
        public void putTransposed(final Matrix3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m01()).put(offset + 4, m.m11()).put(offset + 5, m.m21()).put(offset + 6, m.m02()).put(offset + 7, m.m12()).put(offset + 8, m.m22());
        }
        
        public void putTransposed(final Matrix3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m10()).putFloat(offset + 8, m.m20()).putFloat(offset + 12, m.m01()).putFloat(offset + 16, m.m11()).putFloat(offset + 20, m.m21()).putFloat(offset + 24, m.m02()).putFloat(offset + 28, m.m12()).putFloat(offset + 32, m.m22());
        }
        
        public void putTransposed(final Matrix2f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m01()).put(offset + 3, m.m11());
        }
        
        public void putTransposed(final Matrix2f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m10()).putFloat(offset + 8, m.m01()).putFloat(offset + 12, m.m11());
        }
        
        public void put(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m03()).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, m.m13()).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, m.m23()).put(offset + 12, m.m30()).put(offset + 13, m.m31()).put(offset + 14, m.m32()).put(offset + 15, m.m33());
        }
        
        public void put(final Matrix4d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m02()).putDouble(offset + 24, m.m03()).putDouble(offset + 32, m.m10()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, m.m12()).putDouble(offset + 56, m.m13()).putDouble(offset + 64, m.m20()).putDouble(offset + 72, m.m21()).putDouble(offset + 80, m.m22()).putDouble(offset + 88, m.m23()).putDouble(offset + 96, m.m30()).putDouble(offset + 104, m.m31()).putDouble(offset + 112, m.m32()).putDouble(offset + 120, m.m33());
        }
        
        public void put(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, m.m12()).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, m.m22()).put(offset + 9, m.m30()).put(offset + 10, m.m31()).put(offset + 11, m.m32());
        }
        
        public void put(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m02()).putDouble(offset + 24, m.m10()).putDouble(offset + 32, m.m11()).putDouble(offset + 40, m.m12()).putDouble(offset + 48, m.m20()).putDouble(offset + 56, m.m21()).putDouble(offset + 64, m.m22()).putDouble(offset + 72, m.m30()).putDouble(offset + 80, m.m31()).putDouble(offset + 88, m.m32());
        }
        
        public void putf(final Matrix4d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m01()).put(offset + 2, (float)m.m02()).put(offset + 3, (float)m.m03()).put(offset + 4, (float)m.m10()).put(offset + 5, (float)m.m11()).put(offset + 6, (float)m.m12()).put(offset + 7, (float)m.m13()).put(offset + 8, (float)m.m20()).put(offset + 9, (float)m.m21()).put(offset + 10, (float)m.m22()).put(offset + 11, (float)m.m23()).put(offset + 12, (float)m.m30()).put(offset + 13, (float)m.m31()).put(offset + 14, (float)m.m32()).put(offset + 15, (float)m.m33());
        }
        
        public void putf(final Matrix4d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m01()).putFloat(offset + 8, (float)m.m02()).putFloat(offset + 12, (float)m.m03()).putFloat(offset + 16, (float)m.m10()).putFloat(offset + 20, (float)m.m11()).putFloat(offset + 24, (float)m.m12()).putFloat(offset + 28, (float)m.m13()).putFloat(offset + 32, (float)m.m20()).putFloat(offset + 36, (float)m.m21()).putFloat(offset + 40, (float)m.m22()).putFloat(offset + 44, (float)m.m23()).putFloat(offset + 48, (float)m.m30()).putFloat(offset + 52, (float)m.m31()).putFloat(offset + 56, (float)m.m32()).putFloat(offset + 60, (float)m.m33());
        }
        
        public void putf(final Matrix4x3d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m01()).put(offset + 2, (float)m.m02()).put(offset + 3, (float)m.m10()).put(offset + 4, (float)m.m11()).put(offset + 5, (float)m.m12()).put(offset + 6, (float)m.m20()).put(offset + 7, (float)m.m21()).put(offset + 8, (float)m.m22()).put(offset + 9, (float)m.m30()).put(offset + 10, (float)m.m31()).put(offset + 11, (float)m.m32());
        }
        
        public void putf(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m01()).putFloat(offset + 8, (float)m.m02()).putFloat(offset + 12, (float)m.m10()).putFloat(offset + 16, (float)m.m11()).putFloat(offset + 20, (float)m.m12()).putFloat(offset + 24, (float)m.m20()).putFloat(offset + 28, (float)m.m21()).putFloat(offset + 32, (float)m.m22()).putFloat(offset + 36, (float)m.m30()).putFloat(offset + 40, (float)m.m31()).putFloat(offset + 44, (float)m.m32());
        }
        
        public void putTransposed(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32()).put(offset + 12, m.m03()).put(offset + 13, m.m13()).put(offset + 14, m.m23()).put(offset + 15, m.m33());
        }
        
        public void putTransposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m10()).putDouble(offset + 16, m.m20()).putDouble(offset + 24, m.m30()).putDouble(offset + 32, m.m01()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, m.m21()).putDouble(offset + 56, m.m31()).putDouble(offset + 64, m.m02()).putDouble(offset + 72, m.m12()).putDouble(offset + 80, m.m22()).putDouble(offset + 88, m.m32()).putDouble(offset + 96, m.m03()).putDouble(offset + 104, m.m13()).putDouble(offset + 112, m.m23()).putDouble(offset + 120, m.m33());
        }
        
        public void put4x3Transposed(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32());
        }
        
        public void put4x3Transposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m10()).putDouble(offset + 16, m.m20()).putDouble(offset + 24, m.m30()).putDouble(offset + 32, m.m01()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, m.m21()).putDouble(offset + 56, m.m31()).putDouble(offset + 64, m.m02()).putDouble(offset + 72, m.m12()).putDouble(offset + 80, m.m22()).putDouble(offset + 88, m.m32());
        }
        
        public void putTransposed(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m20()).put(offset + 3, m.m30()).put(offset + 4, m.m01()).put(offset + 5, m.m11()).put(offset + 6, m.m21()).put(offset + 7, m.m31()).put(offset + 8, m.m02()).put(offset + 9, m.m12()).put(offset + 10, m.m22()).put(offset + 11, m.m32());
        }
        
        public void putTransposed(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m10()).putDouble(offset + 16, m.m20()).putDouble(offset + 24, m.m30()).putDouble(offset + 32, m.m01()).putDouble(offset + 40, m.m11()).putDouble(offset + 48, m.m21()).putDouble(offset + 56, m.m31()).putDouble(offset + 64, m.m02()).putDouble(offset + 72, m.m12()).putDouble(offset + 80, m.m22()).putDouble(offset + 88, m.m32());
        }
        
        public void putTransposed(final Matrix2d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m10()).put(offset + 2, m.m01()).put(offset + 3, m.m11());
        }
        
        public void putTransposed(final Matrix2d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m10()).putDouble(offset + 16, m.m01()).putDouble(offset + 24, m.m11());
        }
        
        public void putfTransposed(final Matrix4x3d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m10()).put(offset + 2, (float)m.m20()).put(offset + 3, (float)m.m30()).put(offset + 4, (float)m.m01()).put(offset + 5, (float)m.m11()).put(offset + 6, (float)m.m21()).put(offset + 7, (float)m.m31()).put(offset + 8, (float)m.m02()).put(offset + 9, (float)m.m12()).put(offset + 10, (float)m.m22()).put(offset + 11, (float)m.m32());
        }
        
        public void putfTransposed(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m10()).putFloat(offset + 8, (float)m.m20()).putFloat(offset + 12, (float)m.m30()).putFloat(offset + 16, (float)m.m01()).putFloat(offset + 20, (float)m.m11()).putFloat(offset + 24, (float)m.m21()).putFloat(offset + 28, (float)m.m31()).putFloat(offset + 32, (float)m.m02()).putFloat(offset + 36, (float)m.m12()).putFloat(offset + 40, (float)m.m22()).putFloat(offset + 44, (float)m.m32());
        }
        
        public void putfTransposed(final Matrix2d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m10()).put(offset + 2, (float)m.m01()).put(offset + 3, (float)m.m11());
        }
        
        public void putfTransposed(final Matrix2d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m10()).putFloat(offset + 8, (float)m.m01()).putFloat(offset + 12, (float)m.m11());
        }
        
        public void putfTransposed(final Matrix4d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m10()).put(offset + 2, (float)m.m20()).put(offset + 3, (float)m.m30()).put(offset + 4, (float)m.m01()).put(offset + 5, (float)m.m11()).put(offset + 6, (float)m.m21()).put(offset + 7, (float)m.m31()).put(offset + 8, (float)m.m02()).put(offset + 9, (float)m.m12()).put(offset + 10, (float)m.m22()).put(offset + 11, (float)m.m32()).put(offset + 12, (float)m.m03()).put(offset + 13, (float)m.m13()).put(offset + 14, (float)m.m23()).put(offset + 15, (float)m.m33());
        }
        
        public void putfTransposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m10()).putFloat(offset + 8, (float)m.m20()).putFloat(offset + 12, (float)m.m30()).putFloat(offset + 16, (float)m.m01()).putFloat(offset + 20, (float)m.m11()).putFloat(offset + 24, (float)m.m21()).putFloat(offset + 28, (float)m.m31()).putFloat(offset + 32, (float)m.m02()).putFloat(offset + 36, (float)m.m12()).putFloat(offset + 40, (float)m.m22()).putFloat(offset + 44, (float)m.m32()).putFloat(offset + 48, (float)m.m03()).putFloat(offset + 52, (float)m.m13()).putFloat(offset + 56, (float)m.m23()).putFloat(offset + 60, (float)m.m33());
        }
        
        public void put0(final Matrix3f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, m.m10()).put(4, m.m11()).put(5, m.m12()).put(6, m.m20()).put(7, m.m21()).put(8, m.m22());
        }
        
        public void putN(final Matrix3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, m.m12()).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, m.m22());
        }
        
        public void put(final Matrix3f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put0(final Matrix3f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, m.m10()).putFloat(16, m.m11()).putFloat(20, m.m12()).putFloat(24, m.m20()).putFloat(28, m.m21()).putFloat(32, m.m22());
        }
        
        public void putN(final Matrix3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, m.m10()).putFloat(offset + 16, m.m11()).putFloat(offset + 20, m.m12()).putFloat(offset + 24, m.m20()).putFloat(offset + 28, m.m21()).putFloat(offset + 32, m.m22());
        }
        
        public void put(final Matrix3f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put0(m, dest);
            }
            else {
                this.putN(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix3f m, final ByteBuffer dest) {
            dest.putFloat(0, m.m00()).putFloat(4, m.m01()).putFloat(8, m.m02()).putFloat(12, 0.0f).putFloat(16, m.m10()).putFloat(20, m.m11()).putFloat(24, m.m12()).putFloat(28, 0.0f).putFloat(32, m.m20()).putFloat(36, m.m21()).putFloat(40, m.m22()).putFloat(44, 0.0f);
        }
        
        private void put3x4_N(final Matrix3f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m02()).putFloat(offset + 12, 0.0f).putFloat(offset + 16, m.m10()).putFloat(offset + 20, m.m11()).putFloat(offset + 24, m.m12()).putFloat(offset + 28, 0.0f).putFloat(offset + 32, m.m20()).putFloat(offset + 36, m.m21()).putFloat(offset + 40, m.m22()).putFloat(offset + 44, 0.0f);
        }
        
        public void put3x4(final Matrix3f m, final int offset, final ByteBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put3x4_0(final Matrix3f m, final FloatBuffer dest) {
            dest.put(0, m.m00()).put(1, m.m01()).put(2, m.m02()).put(3, 0.0f).put(4, m.m10()).put(5, m.m11()).put(6, m.m12()).put(7, 0.0f).put(8, m.m20()).put(9, m.m21()).put(10, m.m22()).put(11, 0.0f);
        }
        
        public void put3x4_N(final Matrix3f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, 0.0f).put(offset + 4, m.m10()).put(offset + 5, m.m11()).put(offset + 6, m.m12()).put(offset + 7, 0.0f).put(offset + 8, m.m20()).put(offset + 9, m.m21()).put(offset + 10, m.m22()).put(offset + 11, 0.0f);
        }
        
        public void put3x4(final Matrix3f m, final int offset, final FloatBuffer dest) {
            if (offset == 0) {
                this.put3x4_0(m, dest);
            }
            else {
                this.put3x4_N(m, offset, dest);
            }
        }
        
        public void put(final Matrix3d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m02()).put(offset + 3, m.m10()).put(offset + 4, m.m11()).put(offset + 5, m.m12()).put(offset + 6, m.m20()).put(offset + 7, m.m21()).put(offset + 8, m.m22());
        }
        
        public void put(final Matrix3d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m02()).putDouble(offset + 24, m.m10()).putDouble(offset + 32, m.m11()).putDouble(offset + 40, m.m12()).putDouble(offset + 48, m.m20()).putDouble(offset + 56, m.m21()).putDouble(offset + 64, m.m22());
        }
        
        public void put(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m10()).put(offset + 3, m.m11()).put(offset + 4, m.m20()).put(offset + 5, m.m21());
        }
        
        public void put(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m10()).putFloat(offset + 12, m.m11()).putFloat(offset + 16, m.m20()).putFloat(offset + 20, m.m21());
        }
        
        public void put(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m10()).put(offset + 3, m.m11()).put(offset + 4, m.m20()).put(offset + 5, m.m21());
        }
        
        public void put(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m10()).putDouble(offset + 24, m.m11()).putDouble(offset + 32, m.m20()).putDouble(offset + 40, m.m21());
        }
        
        public void putf(final Matrix3d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m01()).put(offset + 2, (float)m.m02()).put(offset + 3, (float)m.m10()).put(offset + 4, (float)m.m11()).put(offset + 5, (float)m.m12()).put(offset + 6, (float)m.m20()).put(offset + 7, (float)m.m21()).put(offset + 8, (float)m.m22());
        }
        
        public void put(final Matrix2f m, final int offset, final FloatBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m10()).put(offset + 3, m.m11());
        }
        
        public void put(final Matrix2f m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, m.m00()).putFloat(offset + 4, m.m01()).putFloat(offset + 8, m.m10()).putFloat(offset + 12, m.m11());
        }
        
        public void put(final Matrix2d m, final int offset, final DoubleBuffer dest) {
            dest.put(offset, m.m00()).put(offset + 1, m.m01()).put(offset + 2, m.m10()).put(offset + 3, m.m11());
        }
        
        public void put(final Matrix2d m, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, m.m00()).putDouble(offset + 8, m.m01()).putDouble(offset + 16, m.m10()).putDouble(offset + 24, m.m11());
        }
        
        public void putf(final Matrix2d m, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)m.m00()).put(offset + 1, (float)m.m01()).put(offset + 2, (float)m.m10()).put(offset + 3, (float)m.m11());
        }
        
        public void putf(final Matrix2d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m01()).putFloat(offset + 8, (float)m.m10()).putFloat(offset + 12, (float)m.m11());
        }
        
        public void putf(final Matrix3d m, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)m.m00()).putFloat(offset + 4, (float)m.m01()).putFloat(offset + 8, (float)m.m02()).putFloat(offset + 12, (float)m.m10()).putFloat(offset + 16, (float)m.m11()).putFloat(offset + 20, (float)m.m12()).putFloat(offset + 24, (float)m.m20()).putFloat(offset + 28, (float)m.m21()).putFloat(offset + 32, (float)m.m22());
        }
        
        public void put(final Vector4d src, final int offset, final DoubleBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z).put(offset + 3, src.w);
        }
        
        public void put(final Vector4d src, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)src.x).put(offset + 1, (float)src.y).put(offset + 2, (float)src.z).put(offset + 3, (float)src.w);
        }
        
        public void put(final Vector4d src, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, src.x).putDouble(offset + 8, src.y).putDouble(offset + 16, src.z).putDouble(offset + 24, src.w);
        }
        
        public void putf(final Vector4d src, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)src.x).putFloat(offset + 4, (float)src.y).putFloat(offset + 8, (float)src.z).putFloat(offset + 12, (float)src.w);
        }
        
        public void put(final Vector4f src, final int offset, final FloatBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z).put(offset + 3, src.w);
        }
        
        public void put(final Vector4f src, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, src.x).putFloat(offset + 4, src.y).putFloat(offset + 8, src.z).putFloat(offset + 12, src.w);
        }
        
        public void put(final Vector4i src, final int offset, final IntBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z).put(offset + 3, src.w);
        }
        
        public void put(final Vector4i src, final int offset, final ByteBuffer dest) {
            dest.putInt(offset, src.x).putInt(offset + 4, src.y).putInt(offset + 8, src.z).putInt(offset + 12, src.w);
        }
        
        public void put(final Vector3f src, final int offset, final FloatBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z);
        }
        
        public void put(final Vector3f src, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, src.x).putFloat(offset + 4, src.y).putFloat(offset + 8, src.z);
        }
        
        public void put(final Vector3d src, final int offset, final DoubleBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z);
        }
        
        public void put(final Vector3d src, final int offset, final FloatBuffer dest) {
            dest.put(offset, (float)src.x).put(offset + 1, (float)src.y).put(offset + 2, (float)src.z);
        }
        
        public void put(final Vector3d src, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, src.x).putDouble(offset + 8, src.y).putDouble(offset + 16, src.z);
        }
        
        public void putf(final Vector3d src, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, (float)src.x).putFloat(offset + 4, (float)src.y).putFloat(offset + 8, (float)src.z);
        }
        
        public void put(final Vector3i src, final int offset, final IntBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y).put(offset + 2, src.z);
        }
        
        public void put(final Vector3i src, final int offset, final ByteBuffer dest) {
            dest.putInt(offset, src.x).putInt(offset + 4, src.y).putInt(offset + 8, src.z);
        }
        
        public void put(final Vector2f src, final int offset, final FloatBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y);
        }
        
        public void put(final Vector2f src, final int offset, final ByteBuffer dest) {
            dest.putFloat(offset, src.x).putFloat(offset + 4, src.y);
        }
        
        public void put(final Vector2d src, final int offset, final DoubleBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y);
        }
        
        public void put(final Vector2d src, final int offset, final ByteBuffer dest) {
            dest.putDouble(offset, src.x).putDouble(offset + 8, src.y);
        }
        
        public void put(final Vector2i src, final int offset, final IntBuffer dest) {
            dest.put(offset, src.x).put(offset + 1, src.y);
        }
        
        public void put(final Vector2i src, final int offset, final ByteBuffer dest) {
            dest.putInt(offset, src.x).putInt(offset + 4, src.y);
        }
        
        public void get(final Matrix4f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m03(src.get(offset + 3))._m10(src.get(offset + 4))._m11(src.get(offset + 5))._m12(src.get(offset + 6))._m13(src.get(offset + 7))._m20(src.get(offset + 8))._m21(src.get(offset + 9))._m22(src.get(offset + 10))._m23(src.get(offset + 11))._m30(src.get(offset + 12))._m31(src.get(offset + 13))._m32(src.get(offset + 14))._m33(src.get(offset + 15));
        }
        
        public void get(final Matrix4f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m03(src.getFloat(offset + 12))._m10(src.getFloat(offset + 16))._m11(src.getFloat(offset + 20))._m12(src.getFloat(offset + 24))._m13(src.getFloat(offset + 28))._m20(src.getFloat(offset + 32))._m21(src.getFloat(offset + 36))._m22(src.getFloat(offset + 40))._m23(src.getFloat(offset + 44))._m30(src.getFloat(offset + 48))._m31(src.getFloat(offset + 52))._m32(src.getFloat(offset + 56))._m33(src.getFloat(offset + 60));
        }
        
        public void getTransposed(final Matrix4f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m10(src.get(offset + 1))._m20(src.get(offset + 2))._m30(src.get(offset + 3))._m01(src.get(offset + 4))._m11(src.get(offset + 5))._m21(src.get(offset + 6))._m31(src.get(offset + 7))._m02(src.get(offset + 8))._m12(src.get(offset + 9))._m22(src.get(offset + 10))._m32(src.get(offset + 11))._m03(src.get(offset + 12))._m13(src.get(offset + 13))._m23(src.get(offset + 14))._m33(src.get(offset + 15));
        }
        
        public void getTransposed(final Matrix4f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m10(src.getFloat(offset + 4))._m20(src.getFloat(offset + 8))._m30(src.getFloat(offset + 12))._m01(src.getFloat(offset + 16))._m11(src.getFloat(offset + 20))._m21(src.getFloat(offset + 24))._m31(src.getFloat(offset + 28))._m02(src.getFloat(offset + 32))._m12(src.getFloat(offset + 36))._m22(src.getFloat(offset + 40))._m32(src.getFloat(offset + 44))._m03(src.getFloat(offset + 48))._m13(src.getFloat(offset + 52))._m23(src.getFloat(offset + 56))._m33(src.getFloat(offset + 60));
        }
        
        public void get(final Matrix4x3f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8))._m30(src.get(offset + 9))._m31(src.get(offset + 10))._m32(src.get(offset + 11));
        }
        
        public void get(final Matrix4x3f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m10(src.getFloat(offset + 12))._m11(src.getFloat(offset + 16))._m12(src.getFloat(offset + 20))._m20(src.getFloat(offset + 24))._m21(src.getFloat(offset + 28))._m22(src.getFloat(offset + 32))._m30(src.getFloat(offset + 36))._m31(src.getFloat(offset + 40))._m32(src.getFloat(offset + 44));
        }
        
        public void get(final Matrix4d m, final int offset, final DoubleBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m03(src.get(offset + 3))._m10(src.get(offset + 4))._m11(src.get(offset + 5))._m12(src.get(offset + 6))._m13(src.get(offset + 7))._m20(src.get(offset + 8))._m21(src.get(offset + 9))._m22(src.get(offset + 10))._m23(src.get(offset + 11))._m30(src.get(offset + 12))._m31(src.get(offset + 13))._m32(src.get(offset + 14))._m33(src.get(offset + 15));
        }
        
        public void get(final Matrix4d m, final int offset, final ByteBuffer src) {
            m._m00(src.getDouble(offset))._m01(src.getDouble(offset + 8))._m02(src.getDouble(offset + 16))._m03(src.getDouble(offset + 24))._m10(src.getDouble(offset + 32))._m11(src.getDouble(offset + 40))._m12(src.getDouble(offset + 48))._m13(src.getDouble(offset + 56))._m20(src.getDouble(offset + 64))._m21(src.getDouble(offset + 72))._m22(src.getDouble(offset + 80))._m23(src.getDouble(offset + 88))._m30(src.getDouble(offset + 96))._m31(src.getDouble(offset + 104))._m32(src.getDouble(offset + 112))._m33(src.getDouble(offset + 120));
        }
        
        public void get(final Matrix4x3d m, final int offset, final DoubleBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8))._m30(src.get(offset + 9))._m31(src.get(offset + 10))._m32(src.get(offset + 11));
        }
        
        public void get(final Matrix4x3d m, final int offset, final ByteBuffer src) {
            m._m00(src.getDouble(offset))._m01(src.getDouble(offset + 8))._m02(src.getDouble(offset + 16))._m10(src.getDouble(offset + 24))._m11(src.getDouble(offset + 32))._m12(src.getDouble(offset + 40))._m20(src.getDouble(offset + 48))._m21(src.getDouble(offset + 56))._m22(src.getDouble(offset + 64))._m30(src.getDouble(offset + 72))._m31(src.getDouble(offset + 80))._m32(src.getDouble(offset + 88));
        }
        
        public void getf(final Matrix4d m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m03(src.get(offset + 3))._m10(src.get(offset + 4))._m11(src.get(offset + 5))._m12(src.get(offset + 6))._m13(src.get(offset + 7))._m20(src.get(offset + 8))._m21(src.get(offset + 9))._m22(src.get(offset + 10))._m23(src.get(offset + 11))._m30(src.get(offset + 12))._m31(src.get(offset + 13))._m32(src.get(offset + 14))._m33(src.get(offset + 15));
        }
        
        public void getf(final Matrix4d m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m03(src.getFloat(offset + 12))._m10(src.getFloat(offset + 16))._m11(src.getFloat(offset + 20))._m12(src.getFloat(offset + 24))._m13(src.getFloat(offset + 28))._m20(src.getFloat(offset + 32))._m21(src.getFloat(offset + 36))._m22(src.getFloat(offset + 40))._m23(src.getFloat(offset + 44))._m30(src.getFloat(offset + 48))._m31(src.getFloat(offset + 52))._m32(src.getFloat(offset + 56))._m33(src.getFloat(offset + 60));
        }
        
        public void getf(final Matrix4x3d m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8))._m30(src.get(offset + 9))._m31(src.get(offset + 10))._m32(src.get(offset + 11));
        }
        
        public void getf(final Matrix4x3d m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m10(src.getFloat(offset + 12))._m11(src.getFloat(offset + 16))._m12(src.getFloat(offset + 20))._m20(src.getFloat(offset + 24))._m21(src.getFloat(offset + 28))._m22(src.getFloat(offset + 32))._m30(src.getFloat(offset + 36))._m31(src.getFloat(offset + 40))._m32(src.getFloat(offset + 44));
        }
        
        public void get(final Matrix3f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8));
        }
        
        public void get(final Matrix3f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m10(src.getFloat(offset + 12))._m11(src.getFloat(offset + 16))._m12(src.getFloat(offset + 20))._m20(src.getFloat(offset + 24))._m21(src.getFloat(offset + 28))._m22(src.getFloat(offset + 32));
        }
        
        public void get(final Matrix3d m, final int offset, final DoubleBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8));
        }
        
        public void get(final Matrix3d m, final int offset, final ByteBuffer src) {
            m._m00(src.getDouble(offset))._m01(src.getDouble(offset + 8))._m02(src.getDouble(offset + 16))._m10(src.getDouble(offset + 24))._m11(src.getDouble(offset + 32))._m12(src.getDouble(offset + 40))._m20(src.getDouble(offset + 48))._m21(src.getDouble(offset + 56))._m22(src.getDouble(offset + 64));
        }
        
        public void get(final Matrix3x2f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m10(src.get(offset + 2))._m11(src.get(offset + 3))._m20(src.get(offset + 4))._m21(src.get(offset + 5));
        }
        
        public void get(final Matrix3x2f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m10(src.getFloat(offset + 8))._m11(src.getFloat(offset + 12))._m20(src.getFloat(offset + 16))._m21(src.getFloat(offset + 20));
        }
        
        public void get(final Matrix3x2d m, final int offset, final DoubleBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m10(src.get(offset + 2))._m11(src.get(offset + 3))._m20(src.get(offset + 4))._m21(src.get(offset + 5));
        }
        
        public void get(final Matrix3x2d m, final int offset, final ByteBuffer src) {
            m._m00(src.getDouble(offset))._m01(src.getDouble(offset + 8))._m10(src.getDouble(offset + 16))._m11(src.getDouble(offset + 24))._m20(src.getDouble(offset + 32))._m21(src.getDouble(offset + 40));
        }
        
        public void getf(final Matrix3d m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m02(src.get(offset + 2))._m10(src.get(offset + 3))._m11(src.get(offset + 4))._m12(src.get(offset + 5))._m20(src.get(offset + 6))._m21(src.get(offset + 7))._m22(src.get(offset + 8));
        }
        
        public void getf(final Matrix3d m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m02(src.getFloat(offset + 8))._m10(src.getFloat(offset + 12))._m11(src.getFloat(offset + 16))._m12(src.getFloat(offset + 20))._m20(src.getFloat(offset + 24))._m21(src.getFloat(offset + 28))._m22(src.getFloat(offset + 32));
        }
        
        public void get(final Matrix2f m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m10(src.get(offset + 2))._m11(src.get(offset + 3));
        }
        
        public void get(final Matrix2f m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m10(src.getFloat(offset + 8))._m11(src.getFloat(offset + 12));
        }
        
        public void get(final Matrix2d m, final int offset, final DoubleBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m10(src.get(offset + 2))._m11(src.get(offset + 3));
        }
        
        public void get(final Matrix2d m, final int offset, final ByteBuffer src) {
            m._m00(src.getDouble(offset))._m01(src.getDouble(offset + 8))._m10(src.getDouble(offset + 16))._m11(src.getDouble(offset + 24));
        }
        
        public void getf(final Matrix2d m, final int offset, final FloatBuffer src) {
            m._m00(src.get(offset))._m01(src.get(offset + 1))._m10(src.get(offset + 2))._m11(src.get(offset + 3));
        }
        
        public void getf(final Matrix2d m, final int offset, final ByteBuffer src) {
            m._m00(src.getFloat(offset))._m01(src.getFloat(offset + 4))._m10(src.getFloat(offset + 8))._m11(src.getFloat(offset + 12));
        }
        
        public void get(final Vector4d dst, final int offset, final DoubleBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
            dst.w = src.get(offset + 3);
        }
        
        public void get(final Vector4d dst, final int offset, final ByteBuffer src) {
            dst.x = src.getDouble(offset);
            dst.y = src.getDouble(offset + 8);
            dst.z = src.getDouble(offset + 16);
            dst.w = src.getDouble(offset + 24);
        }
        
        public void get(final Vector4f dst, final int offset, final FloatBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
            dst.w = src.get(offset + 3);
        }
        
        public void get(final Vector4f dst, final int offset, final ByteBuffer src) {
            dst.x = src.getFloat(offset);
            dst.y = src.getFloat(offset + 4);
            dst.z = src.getFloat(offset + 8);
            dst.w = src.getFloat(offset + 12);
        }
        
        public void get(final Vector4i dst, final int offset, final IntBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
            dst.w = src.get(offset + 3);
        }
        
        public void get(final Vector4i dst, final int offset, final ByteBuffer src) {
            dst.x = src.getInt(offset);
            dst.y = src.getInt(offset + 4);
            dst.z = src.getInt(offset + 8);
            dst.w = src.getInt(offset + 12);
        }
        
        public void get(final Vector3f dst, final int offset, final FloatBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
        }
        
        public void get(final Vector3f dst, final int offset, final ByteBuffer src) {
            dst.x = src.getFloat(offset);
            dst.y = src.getFloat(offset + 4);
            dst.z = src.getFloat(offset + 8);
        }
        
        public void get(final Vector3d dst, final int offset, final DoubleBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
        }
        
        public void get(final Vector3d dst, final int offset, final ByteBuffer src) {
            dst.x = src.getDouble(offset);
            dst.y = src.getDouble(offset + 8);
            dst.z = src.getDouble(offset + 16);
        }
        
        public void get(final Vector3i dst, final int offset, final IntBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
            dst.z = src.get(offset + 2);
        }
        
        public void get(final Vector3i dst, final int offset, final ByteBuffer src) {
            dst.x = src.getInt(offset);
            dst.y = src.getInt(offset + 4);
            dst.z = src.getInt(offset + 8);
        }
        
        public void get(final Vector2f dst, final int offset, final FloatBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
        }
        
        public void get(final Vector2f dst, final int offset, final ByteBuffer src) {
            dst.x = src.getFloat(offset);
            dst.y = src.getFloat(offset + 4);
        }
        
        public void get(final Vector2d dst, final int offset, final DoubleBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
        }
        
        public void get(final Vector2d dst, final int offset, final ByteBuffer src) {
            dst.x = src.getDouble(offset);
            dst.y = src.getDouble(offset + 8);
        }
        
        public void get(final Vector2i dst, final int offset, final IntBuffer src) {
            dst.x = src.get(offset);
            dst.y = src.get(offset + 1);
        }
        
        public void get(final Vector2i dst, final int offset, final ByteBuffer src) {
            dst.x = src.getInt(offset);
            dst.y = src.getInt(offset + 4);
        }
        
        public float get(final Matrix4f m, final int column, final int row) {
            Label_0255: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00();
                            }
                            case 1: {
                                return m.m01();
                            }
                            case 2: {
                                return m.m02();
                            }
                            case 3: {
                                return m.m03();
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10();
                            }
                            case 1: {
                                return m.m11();
                            }
                            case 2: {
                                return m.m12();
                            }
                            case 3: {
                                return m.m13();
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20();
                            }
                            case 1: {
                                return m.m21();
                            }
                            case 2: {
                                return m.m22();
                            }
                            case 3: {
                                return m.m23();
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (row) {
                            case 0: {
                                return m.m30();
                            }
                            case 1: {
                                return m.m31();
                            }
                            case 2: {
                                return m.m32();
                            }
                            case 3: {
                                return m.m33();
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public Matrix4f set(final Matrix4f m, final int column, final int row, final float value) {
            Label_0287: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00(value);
                            }
                            case 1: {
                                return m.m01(value);
                            }
                            case 2: {
                                return m.m02(value);
                            }
                            case 3: {
                                return m.m03(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10(value);
                            }
                            case 1: {
                                return m.m11(value);
                            }
                            case 2: {
                                return m.m12(value);
                            }
                            case 3: {
                                return m.m13(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20(value);
                            }
                            case 1: {
                                return m.m21(value);
                            }
                            case 2: {
                                return m.m22(value);
                            }
                            case 3: {
                                return m.m23(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (row) {
                            case 0: {
                                return m.m30(value);
                            }
                            case 1: {
                                return m.m31(value);
                            }
                            case 2: {
                                return m.m32(value);
                            }
                            case 3: {
                                return m.m33(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public double get(final Matrix4d m, final int column, final int row) {
            Label_0255: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00;
                            }
                            case 1: {
                                return m.m01;
                            }
                            case 2: {
                                return m.m02;
                            }
                            case 3: {
                                return m.m03;
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10;
                            }
                            case 1: {
                                return m.m11;
                            }
                            case 2: {
                                return m.m12;
                            }
                            case 3: {
                                return m.m13;
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20;
                            }
                            case 1: {
                                return m.m21;
                            }
                            case 2: {
                                return m.m22;
                            }
                            case 3: {
                                return m.m23;
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (row) {
                            case 0: {
                                return m.m30;
                            }
                            case 1: {
                                return m.m31;
                            }
                            case 2: {
                                return m.m32;
                            }
                            case 3: {
                                return m.m33;
                            }
                            default: {
                                break Label_0255;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public Matrix4d set(final Matrix4d m, final int column, final int row, final double value) {
            Label_0287: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00(value);
                            }
                            case 1: {
                                return m.m01(value);
                            }
                            case 2: {
                                return m.m02(value);
                            }
                            case 3: {
                                return m.m03(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10(value);
                            }
                            case 1: {
                                return m.m11(value);
                            }
                            case 2: {
                                return m.m12(value);
                            }
                            case 3: {
                                return m.m13(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20(value);
                            }
                            case 1: {
                                return m.m21(value);
                            }
                            case 2: {
                                return m.m22(value);
                            }
                            case 3: {
                                return m.m23(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                    case 3: {
                        switch (row) {
                            case 0: {
                                return m.m30(value);
                            }
                            case 1: {
                                return m.m31(value);
                            }
                            case 2: {
                                return m.m32(value);
                            }
                            case 3: {
                                return m.m33(value);
                            }
                            default: {
                                break Label_0287;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public float get(final Matrix3f m, final int column, final int row) {
            Label_0162: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00;
                            }
                            case 1: {
                                return m.m01;
                            }
                            case 2: {
                                return m.m02;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10;
                            }
                            case 1: {
                                return m.m11;
                            }
                            case 2: {
                                return m.m12;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20;
                            }
                            case 1: {
                                return m.m21;
                            }
                            case 2: {
                                return m.m22;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public Matrix3f set(final Matrix3f m, final int column, final int row, final float value) {
            Label_0184: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00(value);
                            }
                            case 1: {
                                return m.m01(value);
                            }
                            case 2: {
                                return m.m02(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10(value);
                            }
                            case 1: {
                                return m.m11(value);
                            }
                            case 2: {
                                return m.m12(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20(value);
                            }
                            case 1: {
                                return m.m21(value);
                            }
                            case 2: {
                                return m.m22(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public double get(final Matrix3d m, final int column, final int row) {
            Label_0162: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00;
                            }
                            case 1: {
                                return m.m01;
                            }
                            case 2: {
                                return m.m02;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10;
                            }
                            case 1: {
                                return m.m11;
                            }
                            case 2: {
                                return m.m12;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20;
                            }
                            case 1: {
                                return m.m21;
                            }
                            case 2: {
                                return m.m22;
                            }
                            default: {
                                break Label_0162;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public Matrix3d set(final Matrix3d m, final int column, final int row, final double value) {
            Label_0184: {
                switch (column) {
                    case 0: {
                        switch (row) {
                            case 0: {
                                return m.m00(value);
                            }
                            case 1: {
                                return m.m01(value);
                            }
                            case 2: {
                                return m.m02(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (row) {
                            case 0: {
                                return m.m10(value);
                            }
                            case 1: {
                                return m.m11(value);
                            }
                            case 2: {
                                return m.m12(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (row) {
                            case 0: {
                                return m.m20(value);
                            }
                            case 1: {
                                return m.m21(value);
                            }
                            case 2: {
                                return m.m22(value);
                            }
                            default: {
                                break Label_0184;
                            }
                        }
                        break;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
        
        public Vector4f getColumn(final Matrix4f m, final int column, final Vector4f dest) {
            switch (column) {
                case 0: {
                    return dest.set(m.m00(), m.m01(), m.m02(), m.m03());
                }
                case 1: {
                    return dest.set(m.m10(), m.m11(), m.m12(), m.m13());
                }
                case 2: {
                    return dest.set(m.m20(), m.m21(), m.m22(), m.m23());
                }
                case 3: {
                    return dest.set(m.m30(), m.m31(), m.m32(), m.m33());
                }
                default: {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
        
        public Matrix4f setColumn(final Vector4f v, final int column, final Matrix4f dest) {
            switch (column) {
                case 0: {
                    return dest._m00(v.x)._m01(v.y)._m02(v.z)._m03(v.w);
                }
                case 1: {
                    return dest._m10(v.x)._m11(v.y)._m12(v.z)._m13(v.w);
                }
                case 2: {
                    return dest._m20(v.x)._m21(v.y)._m22(v.z)._m23(v.w);
                }
                case 3: {
                    return dest._m30(v.x)._m31(v.y)._m32(v.z)._m33(v.w);
                }
                default: {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
        
        public Matrix4f setColumn(final Vector4fc v, final int column, final Matrix4f dest) {
            switch (column) {
                case 0: {
                    return dest._m00(v.x())._m01(v.y())._m02(v.z())._m03(v.w());
                }
                case 1: {
                    return dest._m10(v.x())._m11(v.y())._m12(v.z())._m13(v.w());
                }
                case 2: {
                    return dest._m20(v.x())._m21(v.y())._m22(v.z())._m23(v.w());
                }
                case 3: {
                    return dest._m30(v.x())._m31(v.y())._m32(v.z())._m33(v.w());
                }
                default: {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
        
        public void copy(final Matrix4f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m03(src.m03())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m13(src.m13())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m23(src.m23())._m30(src.m30())._m31(src.m31())._m32(src.m32())._m33(src.m33());
        }
        
        public void copy(final Matrix3f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m03(0.0f)._m10(src.m10())._m11(src.m11())._m12(src.m12())._m13(0.0f)._m20(src.m20())._m21(src.m21())._m22(src.m22())._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f);
        }
        
        public void copy(final Matrix4f src, final Matrix3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy(final Matrix3f src, final Matrix4x3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m30(0.0f)._m31(0.0f)._m32(0.0f);
        }
        
        public void copy(final Matrix3x2f src, final Matrix3x2f dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11())._m20(src.m20())._m21(src.m21());
        }
        
        public void copy(final Matrix3x2d src, final Matrix3x2d dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11())._m20(src.m20())._m21(src.m21());
        }
        
        public void copy(final Matrix2f src, final Matrix2f dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy(final Matrix2d src, final Matrix2d dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy(final Matrix2f src, final Matrix3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(0.0f)._m10(src.m10())._m11(src.m11())._m12(0.0f)._m20(0.0f)._m21(0.0f)._m22(1.0f);
        }
        
        public void copy(final Matrix3f src, final Matrix2f dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy(final Matrix2f src, final Matrix3x2f dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11())._m20(0.0f)._m21(0.0f);
        }
        
        public void copy(final Matrix3x2f src, final Matrix2f dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy(final Matrix2d src, final Matrix3d dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(0.0)._m10(src.m10())._m11(src.m11())._m12(0.0)._m20(0.0)._m21(0.0)._m22(1.0);
        }
        
        public void copy(final Matrix3d src, final Matrix2d dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy(final Matrix2d src, final Matrix3x2d dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11())._m20(0.0)._m21(0.0);
        }
        
        public void copy(final Matrix3x2d src, final Matrix2d dest) {
            dest._m00(src.m00())._m01(src.m01())._m10(src.m10())._m11(src.m11());
        }
        
        public void copy3x3(final Matrix4f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy3x3(final Matrix4x3f src, final Matrix4x3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy3x3(final Matrix3f src, final Matrix4x3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy3x3(final Matrix3f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy4x3(final Matrix4x3f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m30(src.m30())._m31(src.m31())._m32(src.m32());
        }
        
        public void copy4x3(final Matrix4f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m30(src.m30())._m31(src.m31())._m32(src.m32());
        }
        
        public void copy(final Matrix4f src, final Matrix4x3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m30(src.m30())._m31(src.m31())._m32(src.m32());
        }
        
        public void copy(final Matrix4x3f src, final Matrix4f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m03(0.0f)._m10(src.m10())._m11(src.m11())._m12(src.m12())._m13(0.0f)._m20(src.m20())._m21(src.m21())._m22(src.m22())._m23(0.0f)._m30(src.m30())._m31(src.m31())._m32(src.m32())._m33(1.0f);
        }
        
        public void copy(final Matrix4x3f src, final Matrix4x3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22())._m30(src.m30())._m31(src.m31())._m32(src.m32());
        }
        
        public void copy(final Matrix3f src, final Matrix3f dest) {
            dest._m00(src.m00())._m01(src.m01())._m02(src.m02())._m10(src.m10())._m11(src.m11())._m12(src.m12())._m20(src.m20())._m21(src.m21())._m22(src.m22());
        }
        
        public void copy(final float[] arr, final int off, final Matrix4f dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m02(arr[off + 2])._m03(arr[off + 3])._m10(arr[off + 4])._m11(arr[off + 5])._m12(arr[off + 6])._m13(arr[off + 7])._m20(arr[off + 8])._m21(arr[off + 9])._m22(arr[off + 10])._m23(arr[off + 11])._m30(arr[off + 12])._m31(arr[off + 13])._m32(arr[off + 14])._m33(arr[off + 15]);
        }
        
        public void copyTransposed(final float[] arr, final int off, final Matrix4f dest) {
            dest._m00(arr[off + 0])._m10(arr[off + 1])._m20(arr[off + 2])._m30(arr[off + 3])._m01(arr[off + 4])._m11(arr[off + 5])._m21(arr[off + 6])._m31(arr[off + 7])._m02(arr[off + 8])._m12(arr[off + 9])._m22(arr[off + 10])._m32(arr[off + 11])._m03(arr[off + 12])._m13(arr[off + 13])._m23(arr[off + 14])._m33(arr[off + 15]);
        }
        
        public void copy(final float[] arr, final int off, final Matrix3f dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m02(arr[off + 2])._m10(arr[off + 3])._m11(arr[off + 4])._m12(arr[off + 5])._m20(arr[off + 6])._m21(arr[off + 7])._m22(arr[off + 8]);
        }
        
        public void copy(final float[] arr, final int off, final Matrix4x3f dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m02(arr[off + 2])._m10(arr[off + 3])._m11(arr[off + 4])._m12(arr[off + 5])._m20(arr[off + 6])._m21(arr[off + 7])._m22(arr[off + 8])._m30(arr[off + 9])._m31(arr[off + 10])._m32(arr[off + 11]);
        }
        
        public void copy(final float[] arr, final int off, final Matrix3x2f dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m10(arr[off + 2])._m11(arr[off + 3])._m20(arr[off + 4])._m21(arr[off + 5]);
        }
        
        public void copy(final double[] arr, final int off, final Matrix3x2d dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m10(arr[off + 2])._m11(arr[off + 3])._m20(arr[off + 4])._m21(arr[off + 5]);
        }
        
        public void copy(final float[] arr, final int off, final Matrix2f dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m10(arr[off + 2])._m11(arr[off + 3]);
        }
        
        public void copy(final double[] arr, final int off, final Matrix2d dest) {
            dest._m00(arr[off + 0])._m01(arr[off + 1])._m10(arr[off + 2])._m11(arr[off + 3]);
        }
        
        public void copy(final Matrix4f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m02();
            dest[off + 3] = src.m03();
            dest[off + 4] = src.m10();
            dest[off + 5] = src.m11();
            dest[off + 6] = src.m12();
            dest[off + 7] = src.m13();
            dest[off + 8] = src.m20();
            dest[off + 9] = src.m21();
            dest[off + 10] = src.m22();
            dest[off + 11] = src.m23();
            dest[off + 12] = src.m30();
            dest[off + 13] = src.m31();
            dest[off + 14] = src.m32();
            dest[off + 15] = src.m33();
        }
        
        public void copy(final Matrix3f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m02();
            dest[off + 3] = src.m10();
            dest[off + 4] = src.m11();
            dest[off + 5] = src.m12();
            dest[off + 6] = src.m20();
            dest[off + 7] = src.m21();
            dest[off + 8] = src.m22();
        }
        
        public void copy(final Matrix4x3f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m02();
            dest[off + 3] = src.m10();
            dest[off + 4] = src.m11();
            dest[off + 5] = src.m12();
            dest[off + 6] = src.m20();
            dest[off + 7] = src.m21();
            dest[off + 8] = src.m22();
            dest[off + 9] = src.m30();
            dest[off + 10] = src.m31();
            dest[off + 11] = src.m32();
        }
        
        public void copy(final Matrix3x2f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m10();
            dest[off + 3] = src.m11();
            dest[off + 4] = src.m20();
            dest[off + 5] = src.m21();
        }
        
        public void copy(final Matrix3x2d src, final double[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m10();
            dest[off + 3] = src.m11();
            dest[off + 4] = src.m20();
            dest[off + 5] = src.m21();
        }
        
        public void copy(final Matrix2f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m10();
            dest[off + 3] = src.m11();
        }
        
        public void copy(final Matrix2d src, final double[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m10();
            dest[off + 3] = src.m11();
        }
        
        public void copy4x4(final Matrix4x3f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m02();
            dest[off + 3] = 0.0f;
            dest[off + 4] = src.m10();
            dest[off + 5] = src.m11();
            dest[off + 6] = src.m12();
            dest[off + 7] = 0.0f;
            dest[off + 8] = src.m20();
            dest[off + 9] = src.m21();
            dest[off + 10] = src.m22();
            dest[off + 11] = 0.0f;
            dest[off + 12] = src.m30();
            dest[off + 13] = src.m31();
            dest[off + 14] = src.m32();
            dest[off + 15] = 1.0f;
        }
        
        public void copy4x4(final Matrix4x3d src, final float[] dest, final int off) {
            dest[off + 0] = (float)src.m00();
            dest[off + 1] = (float)src.m01();
            dest[off + 2] = (float)src.m02();
            dest[off + 3] = 0.0f;
            dest[off + 4] = (float)src.m10();
            dest[off + 5] = (float)src.m11();
            dest[off + 6] = (float)src.m12();
            dest[off + 7] = 0.0f;
            dest[off + 8] = (float)src.m20();
            dest[off + 9] = (float)src.m21();
            dest[off + 10] = (float)src.m22();
            dest[off + 11] = 0.0f;
            dest[off + 12] = (float)src.m30();
            dest[off + 13] = (float)src.m31();
            dest[off + 14] = (float)src.m32();
            dest[off + 15] = 1.0f;
        }
        
        public void copy4x4(final Matrix4x3d src, final double[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = src.m02();
            dest[off + 3] = 0.0;
            dest[off + 4] = src.m10();
            dest[off + 5] = src.m11();
            dest[off + 6] = src.m12();
            dest[off + 7] = 0.0;
            dest[off + 8] = src.m20();
            dest[off + 9] = src.m21();
            dest[off + 10] = src.m22();
            dest[off + 11] = 0.0;
            dest[off + 12] = src.m30();
            dest[off + 13] = src.m31();
            dest[off + 14] = src.m32();
            dest[off + 15] = 1.0;
        }
        
        public void copy3x3(final Matrix3x2f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = 0.0f;
            dest[off + 3] = src.m10();
            dest[off + 4] = src.m11();
            dest[off + 5] = 0.0f;
            dest[off + 6] = src.m20();
            dest[off + 7] = src.m21();
            dest[off + 8] = 1.0f;
        }
        
        public void copy3x3(final Matrix3x2d src, final double[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 2] = 0.0;
            dest[off + 3] = src.m10();
            dest[off + 4] = src.m11();
            dest[off + 5] = 0.0;
            dest[off + 6] = src.m20();
            dest[off + 7] = src.m21();
            dest[off + 8] = 1.0;
        }
        
        public void copy4x4(final Matrix3x2f src, final float[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 3] = (dest[off + 2] = 0.0f);
            dest[off + 4] = src.m10();
            dest[off + 5] = src.m11();
            dest[off + 7] = (dest[off + 6] = 0.0f);
            dest[off + 9] = (dest[off + 8] = 0.0f);
            dest[off + 10] = 1.0f;
            dest[off + 11] = 0.0f;
            dest[off + 12] = src.m20();
            dest[off + 13] = src.m21();
            dest[off + 14] = 0.0f;
            dest[off + 15] = 1.0f;
        }
        
        public void copy4x4(final Matrix3x2d src, final double[] dest, final int off) {
            dest[off + 0] = src.m00();
            dest[off + 1] = src.m01();
            dest[off + 3] = (dest[off + 2] = 0.0);
            dest[off + 4] = src.m10();
            dest[off + 5] = src.m11();
            dest[off + 7] = (dest[off + 6] = 0.0);
            dest[off + 9] = (dest[off + 8] = 0.0);
            dest[off + 10] = 1.0;
            dest[off + 11] = 0.0;
            dest[off + 12] = src.m20();
            dest[off + 13] = src.m21();
            dest[off + 14] = 0.0;
            dest[off + 15] = 1.0;
        }
        
        public void identity(final Matrix4f dest) {
            dest._m00(1.0f)._m01(0.0f)._m02(0.0f)._m03(0.0f)._m10(0.0f)._m11(1.0f)._m12(0.0f)._m13(0.0f)._m20(0.0f)._m21(0.0f)._m22(1.0f)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(1.0f);
        }
        
        public void identity(final Matrix4x3f dest) {
            dest._m00(1.0f)._m01(0.0f)._m02(0.0f)._m10(0.0f)._m11(1.0f)._m12(0.0f)._m20(0.0f)._m21(0.0f)._m22(1.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f);
        }
        
        public void identity(final Matrix3f dest) {
            dest._m00(1.0f)._m01(0.0f)._m02(0.0f)._m10(0.0f)._m11(1.0f)._m12(0.0f)._m20(0.0f)._m21(0.0f)._m22(1.0f);
        }
        
        public void identity(final Matrix3x2f dest) {
            dest._m00(1.0f)._m01(0.0f)._m10(0.0f)._m11(1.0f)._m20(0.0f)._m21(0.0f);
        }
        
        public void identity(final Matrix3x2d dest) {
            dest._m00(1.0)._m01(0.0)._m10(0.0)._m11(1.0)._m20(0.0)._m21(0.0);
        }
        
        public void identity(final Matrix2f dest) {
            dest._m00(1.0f)._m01(0.0f)._m10(0.0f)._m11(1.0f);
        }
        
        public void swap(final Matrix4f m1, final Matrix4f m2) {
            float tmp = m1.m00();
            m1._m00(m2.m00());
            m2._m00(tmp);
            tmp = m1.m01();
            m1._m01(m2.m01());
            m2._m01(tmp);
            tmp = m1.m02();
            m1._m02(m2.m02());
            m2._m02(tmp);
            tmp = m1.m03();
            m1._m03(m2.m03());
            m2._m03(tmp);
            tmp = m1.m10();
            m1._m10(m2.m10());
            m2._m10(tmp);
            tmp = m1.m11();
            m1._m11(m2.m11());
            m2._m11(tmp);
            tmp = m1.m12();
            m1._m12(m2.m12());
            m2._m12(tmp);
            tmp = m1.m13();
            m1._m13(m2.m13());
            m2._m13(tmp);
            tmp = m1.m20();
            m1._m20(m2.m20());
            m2._m20(tmp);
            tmp = m1.m21();
            m1._m21(m2.m21());
            m2._m21(tmp);
            tmp = m1.m22();
            m1._m22(m2.m22());
            m2._m22(tmp);
            tmp = m1.m23();
            m1._m23(m2.m23());
            m2._m23(tmp);
            tmp = m1.m30();
            m1._m30(m2.m30());
            m2._m30(tmp);
            tmp = m1.m31();
            m1._m31(m2.m31());
            m2._m31(tmp);
            tmp = m1.m32();
            m1._m32(m2.m32());
            m2._m32(tmp);
            tmp = m1.m33();
            m1._m33(m2.m33());
            m2._m33(tmp);
        }
        
        public void swap(final Matrix4x3f m1, final Matrix4x3f m2) {
            float tmp = m1.m00();
            m1._m00(m2.m00());
            m2._m00(tmp);
            tmp = m1.m01();
            m1._m01(m2.m01());
            m2._m01(tmp);
            tmp = m1.m02();
            m1._m02(m2.m02());
            m2._m02(tmp);
            tmp = m1.m10();
            m1._m10(m2.m10());
            m2._m10(tmp);
            tmp = m1.m11();
            m1._m11(m2.m11());
            m2._m11(tmp);
            tmp = m1.m12();
            m1._m12(m2.m12());
            m2._m12(tmp);
            tmp = m1.m20();
            m1._m20(m2.m20());
            m2._m20(tmp);
            tmp = m1.m21();
            m1._m21(m2.m21());
            m2._m21(tmp);
            tmp = m1.m22();
            m1._m22(m2.m22());
            m2._m22(tmp);
            tmp = m1.m30();
            m1._m30(m2.m30());
            m2._m30(tmp);
            tmp = m1.m31();
            m1._m31(m2.m31());
            m2._m31(tmp);
            tmp = m1.m32();
            m1._m32(m2.m32());
            m2._m32(tmp);
        }
        
        public void swap(final Matrix3f m1, final Matrix3f m2) {
            float tmp = m1.m00();
            m1._m00(m2.m00());
            m2._m00(tmp);
            tmp = m1.m01();
            m1._m01(m2.m01());
            m2._m01(tmp);
            tmp = m1.m02();
            m1._m02(m2.m02());
            m2._m02(tmp);
            tmp = m1.m10();
            m1._m10(m2.m10());
            m2._m10(tmp);
            tmp = m1.m11();
            m1._m11(m2.m11());
            m2._m11(tmp);
            tmp = m1.m12();
            m1._m12(m2.m12());
            m2._m12(tmp);
            tmp = m1.m20();
            m1._m20(m2.m20());
            m2._m20(tmp);
            tmp = m1.m21();
            m1._m21(m2.m21());
            m2._m21(tmp);
            tmp = m1.m22();
            m1._m22(m2.m22());
            m2._m22(tmp);
        }
        
        public void swap(final Matrix2f m1, final Matrix2f m2) {
            float tmp = m1.m00();
            m1._m00(m2.m00());
            m2._m00(tmp);
            tmp = m1.m01();
            m1._m00(m2.m01());
            m2._m01(tmp);
            tmp = m1.m10();
            m1._m00(m2.m10());
            m2._m10(tmp);
            tmp = m1.m11();
            m1._m00(m2.m11());
            m2._m11(tmp);
        }
        
        public void swap(final Matrix2d m1, final Matrix2d m2) {
            double tmp = m1.m00();
            m1._m00(m2.m00());
            m2._m00(tmp);
            tmp = m1.m01();
            m1._m00(m2.m01());
            m2._m01(tmp);
            tmp = m1.m10();
            m1._m00(m2.m10());
            m2._m10(tmp);
            tmp = m1.m11();
            m1._m00(m2.m11());
            m2._m11(tmp);
        }
        
        public void zero(final Matrix4f dest) {
            dest._m00(0.0f)._m01(0.0f)._m02(0.0f)._m03(0.0f)._m10(0.0f)._m11(0.0f)._m12(0.0f)._m13(0.0f)._m20(0.0f)._m21(0.0f)._m22(0.0f)._m23(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f)._m33(0.0f);
        }
        
        public void zero(final Matrix4x3f dest) {
            dest._m00(0.0f)._m01(0.0f)._m02(0.0f)._m10(0.0f)._m11(0.0f)._m12(0.0f)._m20(0.0f)._m21(0.0f)._m22(0.0f)._m30(0.0f)._m31(0.0f)._m32(0.0f);
        }
        
        public void zero(final Matrix3f dest) {
            dest._m00(0.0f)._m01(0.0f)._m02(0.0f)._m10(0.0f)._m11(0.0f)._m12(0.0f)._m20(0.0f)._m21(0.0f)._m22(0.0f);
        }
        
        public void zero(final Matrix3x2f dest) {
            dest._m00(0.0f)._m01(0.0f)._m10(0.0f)._m11(0.0f)._m20(0.0f)._m21(0.0f);
        }
        
        public void zero(final Matrix3x2d dest) {
            dest._m00(0.0)._m01(0.0)._m10(0.0)._m11(0.0)._m20(0.0)._m21(0.0);
        }
        
        public void zero(final Matrix2f dest) {
            dest._m00(0.0f)._m01(0.0f)._m10(0.0f)._m11(0.0f);
        }
        
        public void zero(final Matrix2d dest) {
            dest._m00(0.0)._m01(0.0)._m10(0.0)._m11(0.0);
        }
        
        public void putMatrix3f(final Quaternionf q, final int position, final ByteBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.putFloat(position, w2 + x2 - z2 - y2).putFloat(position + 4, xy + zw + zw + xy).putFloat(position + 8, xz - yw + xz - yw).putFloat(position + 12, -zw + xy - zw + xy).putFloat(position + 16, y2 - z2 + w2 - x2).putFloat(position + 20, yz + yz + xw + xw).putFloat(position + 24, yw + xz + xz + yw).putFloat(position + 28, yz + yz - xw - xw).putFloat(position + 32, z2 - y2 - x2 + w2);
        }
        
        public void putMatrix3f(final Quaternionf q, final int position, final FloatBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.put(position, w2 + x2 - z2 - y2).put(position + 1, xy + zw + zw + xy).put(position + 2, xz - yw + xz - yw).put(position + 3, -zw + xy - zw + xy).put(position + 4, y2 - z2 + w2 - x2).put(position + 5, yz + yz + xw + xw).put(position + 6, yw + xz + xz + yw).put(position + 7, yz + yz - xw - xw).put(position + 8, z2 - y2 - x2 + w2);
        }
        
        public void putMatrix4f(final Quaternionf q, final int position, final ByteBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.putFloat(position, w2 + x2 - z2 - y2).putFloat(position + 4, xy + zw + zw + xy).putFloat(position + 8, xz - yw + xz - yw).putFloat(position + 12, 0.0f).putFloat(position + 16, -zw + xy - zw + xy).putFloat(position + 20, y2 - z2 + w2 - x2).putFloat(position + 24, yz + yz + xw + xw).putFloat(position + 28, 0.0f).putFloat(position + 32, yw + xz + xz + yw).putFloat(position + 36, yz + yz - xw - xw).putFloat(position + 40, z2 - y2 - x2 + w2).putFloat(position + 44, 0.0f).putLong(position + 48, 0L).putLong(position + 56, 4575657221408423936L);
        }
        
        public void putMatrix4f(final Quaternionf q, final int position, final FloatBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.put(position, w2 + x2 - z2 - y2).put(position + 1, xy + zw + zw + xy).put(position + 2, xz - yw + xz - yw).put(position + 3, 0.0f).put(position + 4, -zw + xy - zw + xy).put(position + 5, y2 - z2 + w2 - x2).put(position + 6, yz + yz + xw + xw).put(position + 7, 0.0f).put(position + 8, yw + xz + xz + yw).put(position + 9, yz + yz - xw - xw).put(position + 10, z2 - y2 - x2 + w2).put(position + 11, 0.0f).put(position + 12, 0.0f).put(position + 13, 0.0f).put(position + 14, 0.0f).put(position + 15, 1.0f);
        }
        
        public void putMatrix4x3f(final Quaternionf q, final int position, final ByteBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.putFloat(position, w2 + x2 - z2 - y2).putFloat(position + 4, xy + zw + zw + xy).putFloat(position + 8, xz - yw + xz - yw).putFloat(position + 12, -zw + xy - zw + xy).putFloat(position + 16, y2 - z2 + w2 - x2).putFloat(position + 20, yz + yz + xw + xw).putFloat(position + 24, yw + xz + xz + yw).putFloat(position + 28, yz + yz - xw - xw).putFloat(position + 32, z2 - y2 - x2 + w2).putLong(position + 36, 0L).putFloat(position + 44, 0.0f);
        }
        
        public void putMatrix4x3f(final Quaternionf q, final int position, final FloatBuffer dest) {
            final float w2 = q.w * q.w;
            final float x2 = q.x * q.x;
            final float y2 = q.y * q.y;
            final float z2 = q.z * q.z;
            final float zw = q.z * q.w;
            final float xy = q.x * q.y;
            final float xz = q.x * q.z;
            final float yw = q.y * q.w;
            final float yz = q.y * q.z;
            final float xw = q.x * q.w;
            dest.put(position, w2 + x2 - z2 - y2).put(position + 1, xy + zw + zw + xy).put(position + 2, xz - yw + xz - yw).put(position + 3, -zw + xy - zw + xy).put(position + 4, y2 - z2 + w2 - x2).put(position + 5, yz + yz + xw + xw).put(position + 6, yw + xz + xz + yw).put(position + 7, yz + yz - xw - xw).put(position + 8, z2 - y2 - x2 + w2).put(position + 9, 0.0f).put(position + 10, 0.0f).put(position + 11, 0.0f);
        }
    }
    
    public static class MemUtilUnsafe extends MemUtilNIO
    {
        public static final Unsafe UNSAFE;
        public static final long ADDRESS;
        public static final long Matrix2f_m00;
        public static final long Matrix3f_m00;
        public static final long Matrix3d_m00;
        public static final long Matrix4f_m00;
        public static final long Matrix4d_m00;
        public static final long Matrix4x3f_m00;
        public static final long Matrix3x2f_m00;
        public static final long Vector4f_x;
        public static final long Vector4i_x;
        public static final long Vector3f_x;
        public static final long Vector3i_x;
        public static final long Vector2f_x;
        public static final long Vector2i_x;
        public static final long Quaternionf_x;
        public static final long floatArrayOffset;
        private static /* synthetic */ Class class$0;
        private static /* synthetic */ Class class$1;
        private static /* synthetic */ Class class$2;
        private static /* synthetic */ Class class$3;
        private static /* synthetic */ Class class$4;
        private static /* synthetic */ Class class$5;
        private static /* synthetic */ Class class$6;
        private static /* synthetic */ Class class$7;
        private static /* synthetic */ Class class$8;
        private static /* synthetic */ Class class$9;
        private static /* synthetic */ Class class$10;
        private static /* synthetic */ Class class$11;
        private static /* synthetic */ Class class$12;
        private static /* synthetic */ Class class$13;
        private static /* synthetic */ Class class$14;
        private static /* synthetic */ Class class$15;
        private static /* synthetic */ Class class$16;
        private static /* synthetic */ Class class$17;
        
        private static long findBufferAddress() {
            try {
                return MemUtilUnsafe.UNSAFE.objectFieldOffset(getDeclaredField((MemUtilUnsafe.class$0 == null) ? (MemUtilUnsafe.class$0 = class$("java.nio.Buffer")) : MemUtilUnsafe.class$0, "address"));
            }
            catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
        
        private static /* synthetic */ Class class$(final String className) {
            try {
                return Class.forName(className);
            }
            catch (ClassNotFoundException cause) {
                throw new NoClassDefFoundError().initCause(cause);
            }
        }
        
        private static long checkMatrix4f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$1 == null) ? (MemUtilUnsafe.class$1 = class$("org.joml.Matrix4f")) : MemUtilUnsafe.class$1).getDeclaredField("m00");
            final long Matrix4f_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 16; ++i) {
                final int c = i >>> 2;
                final int r = i & 0x3;
                f = ((MemUtilUnsafe.class$1 == null) ? (MemUtilUnsafe.class$1 = class$("org.joml.Matrix4f")) : MemUtilUnsafe.class$1).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix4f_m00 + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Matrix4f element offset");
                }
            }
            return Matrix4f_m00;
        }
        
        private static long checkMatrix4d() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$2 == null) ? (MemUtilUnsafe.class$2 = class$("org.joml.Matrix4d")) : MemUtilUnsafe.class$2).getDeclaredField("m00");
            final long Matrix4d_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 16; ++i) {
                final int c = i >>> 2;
                final int r = i & 0x3;
                f = ((MemUtilUnsafe.class$2 == null) ? (MemUtilUnsafe.class$2 = class$("org.joml.Matrix4d")) : MemUtilUnsafe.class$2).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix4d_m00 + (i << 3)) {
                    throw new UnsupportedOperationException("Unexpected Matrix4d element offset");
                }
            }
            return Matrix4d_m00;
        }
        
        private static long checkMatrix4x3f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$3 == null) ? (MemUtilUnsafe.class$3 = class$("org.joml.Matrix4x3f")) : MemUtilUnsafe.class$3).getDeclaredField("m00");
            final long Matrix4x3f_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 12; ++i) {
                final int c = i / 3;
                final int r = i % 3;
                f = ((MemUtilUnsafe.class$3 == null) ? (MemUtilUnsafe.class$3 = class$("org.joml.Matrix4x3f")) : MemUtilUnsafe.class$3).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix4x3f_m00 + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Matrix4x3f element offset");
                }
            }
            return Matrix4x3f_m00;
        }
        
        private static long checkMatrix3f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$4 == null) ? (MemUtilUnsafe.class$4 = class$("org.joml.Matrix3f")) : MemUtilUnsafe.class$4).getDeclaredField("m00");
            final long Matrix3f_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 9; ++i) {
                final int c = i / 3;
                final int r = i % 3;
                f = ((MemUtilUnsafe.class$4 == null) ? (MemUtilUnsafe.class$4 = class$("org.joml.Matrix3f")) : MemUtilUnsafe.class$4).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix3f_m00 + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Matrix3f element offset");
                }
            }
            return Matrix3f_m00;
        }
        
        private static long checkMatrix3d() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$5 == null) ? (MemUtilUnsafe.class$5 = class$("org.joml.Matrix3d")) : MemUtilUnsafe.class$5).getDeclaredField("m00");
            final long Matrix3d_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 9; ++i) {
                final int c = i / 3;
                final int r = i % 3;
                f = ((MemUtilUnsafe.class$5 == null) ? (MemUtilUnsafe.class$5 = class$("org.joml.Matrix3d")) : MemUtilUnsafe.class$5).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix3d_m00 + (i << 3)) {
                    throw new UnsupportedOperationException("Unexpected Matrix3d element offset");
                }
            }
            return Matrix3d_m00;
        }
        
        private static long checkMatrix3x2f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$6 == null) ? (MemUtilUnsafe.class$6 = class$("org.joml.Matrix3x2f")) : MemUtilUnsafe.class$6).getDeclaredField("m00");
            final long Matrix3x2f_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 6; ++i) {
                final int c = i / 2;
                final int r = i % 2;
                f = ((MemUtilUnsafe.class$6 == null) ? (MemUtilUnsafe.class$6 = class$("org.joml.Matrix3x2f")) : MemUtilUnsafe.class$6).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix3x2f_m00 + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Matrix3x2f element offset");
                }
            }
            return Matrix3x2f_m00;
        }
        
        private static long checkMatrix2f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$7 == null) ? (MemUtilUnsafe.class$7 = class$("org.joml.Matrix2f")) : MemUtilUnsafe.class$7).getDeclaredField("m00");
            final long Matrix2f_m00 = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            for (int i = 1; i < 4; ++i) {
                final int c = i / 2;
                final int r = i % 2;
                f = ((MemUtilUnsafe.class$7 == null) ? (MemUtilUnsafe.class$7 = class$("org.joml.Matrix2f")) : MemUtilUnsafe.class$7).getDeclaredField("m" + c + r);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Matrix2f_m00 + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Matrix2f element offset");
                }
            }
            return Matrix2f_m00;
        }
        
        private static long checkVector4f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$8 == null) ? (MemUtilUnsafe.class$8 = class$("org.joml.Vector4f")) : MemUtilUnsafe.class$8).getDeclaredField("x");
            final long Vector4f_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            final String[] names = { "y", "z", "w" };
            for (int i = 1; i < 4; ++i) {
                f = ((MemUtilUnsafe.class$8 == null) ? (MemUtilUnsafe.class$8 = class$("org.joml.Vector4f")) : MemUtilUnsafe.class$8).getDeclaredField(names[i - 1]);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Vector4f_x + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Vector4f element offset");
                }
            }
            return Vector4f_x;
        }
        
        private static long checkVector4i() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$9 == null) ? (MemUtilUnsafe.class$9 = class$("org.joml.Vector4i")) : MemUtilUnsafe.class$9).getDeclaredField("x");
            final long Vector4i_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            final String[] names = { "y", "z", "w" };
            for (int i = 1; i < 4; ++i) {
                f = ((MemUtilUnsafe.class$9 == null) ? (MemUtilUnsafe.class$9 = class$("org.joml.Vector4i")) : MemUtilUnsafe.class$9).getDeclaredField(names[i - 1]);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Vector4i_x + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Vector4i element offset");
                }
            }
            return Vector4i_x;
        }
        
        private static long checkVector3f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$10 == null) ? (MemUtilUnsafe.class$10 = class$("org.joml.Vector3f")) : MemUtilUnsafe.class$10).getDeclaredField("x");
            final long Vector3f_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            final String[] names = { "y", "z" };
            for (int i = 1; i < 3; ++i) {
                f = ((MemUtilUnsafe.class$10 == null) ? (MemUtilUnsafe.class$10 = class$("org.joml.Vector3f")) : MemUtilUnsafe.class$10).getDeclaredField(names[i - 1]);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Vector3f_x + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Vector3f element offset");
                }
            }
            return Vector3f_x;
        }
        
        private static long checkVector3i() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$11 == null) ? (MemUtilUnsafe.class$11 = class$("org.joml.Vector3i")) : MemUtilUnsafe.class$11).getDeclaredField("x");
            final long Vector3i_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            final String[] names = { "y", "z" };
            for (int i = 1; i < 3; ++i) {
                f = ((MemUtilUnsafe.class$11 == null) ? (MemUtilUnsafe.class$11 = class$("org.joml.Vector3i")) : MemUtilUnsafe.class$11).getDeclaredField(names[i - 1]);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Vector3i_x + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Vector3i element offset");
                }
            }
            return Vector3i_x;
        }
        
        private static long checkVector2f() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$12 == null) ? (MemUtilUnsafe.class$12 = class$("org.joml.Vector2f")) : MemUtilUnsafe.class$12).getDeclaredField("x");
            final long Vector2f_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            f = ((MemUtilUnsafe.class$12 == null) ? (MemUtilUnsafe.class$12 = class$("org.joml.Vector2f")) : MemUtilUnsafe.class$12).getDeclaredField("y");
            final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            if (offset != Vector2f_x + 4L) {
                throw new UnsupportedOperationException("Unexpected Vector2f element offset");
            }
            return Vector2f_x;
        }
        
        private static long checkVector2i() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$13 == null) ? (MemUtilUnsafe.class$13 = class$("org.joml.Vector2i")) : MemUtilUnsafe.class$13).getDeclaredField("x");
            final long Vector2i_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            f = ((MemUtilUnsafe.class$13 == null) ? (MemUtilUnsafe.class$13 = class$("org.joml.Vector2i")) : MemUtilUnsafe.class$13).getDeclaredField("y");
            final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            if (offset != Vector2i_x + 4L) {
                throw new UnsupportedOperationException("Unexpected Vector2i element offset");
            }
            return Vector2i_x;
        }
        
        private static long checkQuaternionf() throws NoSuchFieldException, SecurityException {
            Field f = ((MemUtilUnsafe.class$14 == null) ? (MemUtilUnsafe.class$14 = class$("org.joml.Quaternionf")) : MemUtilUnsafe.class$14).getDeclaredField("x");
            final long Quaternionf_x = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
            final String[] names = { "y", "z", "w" };
            for (int i = 1; i < 4; ++i) {
                f = ((MemUtilUnsafe.class$14 == null) ? (MemUtilUnsafe.class$14 = class$("org.joml.Quaternionf")) : MemUtilUnsafe.class$14).getDeclaredField(names[i - 1]);
                final long offset = MemUtilUnsafe.UNSAFE.objectFieldOffset(f);
                if (offset != Quaternionf_x + (i << 2)) {
                    throw new UnsupportedOperationException("Unexpected Quaternionf element offset");
                }
            }
            return Quaternionf_x;
        }
        
        private static Field getDeclaredField(final Class root, final String fieldName) throws NoSuchFieldException {
            Class type = root;
            do {
                try {
                    final Field field = type.getDeclaredField(fieldName);
                    return field;
                }
                catch (NoSuchFieldException e) {
                    type = type.getSuperclass();
                }
                catch (SecurityException e2) {
                    type = type.getSuperclass();
                }
            } while (type != null);
            throw new NoSuchFieldException(fieldName + " does not exist in " + root.getName() + " or any of its superclasses.");
        }
        
        public static Unsafe getUnsafeInstance() throws SecurityException {
            final Field[] fields = ((MemUtilUnsafe.class$15 == null) ? (MemUtilUnsafe.class$15 = class$("sun.misc.Unsafe")) : MemUtilUnsafe.class$15).getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (field.getType().equals((MemUtilUnsafe.class$15 == null) ? (MemUtilUnsafe.class$15 = class$("sun.misc.Unsafe")) : MemUtilUnsafe.class$15)) {
                    final int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers)) {
                        if (Modifier.isFinal(modifiers)) {
                            field.setAccessible(true);
                            try {
                                return (Unsafe)field.get(null);
                            }
                            catch (IllegalAccessException ex) {
                                break;
                            }
                        }
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
        
        public static void put(final Matrix4f m, final long destAddr) {
            for (int i = 0; i < 8; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 3), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix4f_m00 + (i << 3)));
            }
        }
        
        public static void put4x3(final Matrix4f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            for (int i = 0; i < 4; ++i) {
                u.putLong(null, destAddr + 12 * i, u.getLong(m, MemUtilUnsafe.Matrix4f_m00 + (i << 4)));
            }
            u.putFloat(null, destAddr + 8L, m.m02());
            u.putFloat(null, destAddr + 20L, m.m12());
            u.putFloat(null, destAddr + 32L, m.m22());
            u.putFloat(null, destAddr + 44L, m.m32());
        }
        
        public static void put3x4(final Matrix4f m, final long destAddr) {
            for (int i = 0; i < 6; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 3), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix4f_m00 + (i << 3)));
            }
        }
        
        public static void put(final Matrix4x3f m, final long destAddr) {
            for (int i = 0; i < 6; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 3), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix4x3f_m00 + (i << 3)));
            }
        }
        
        public static void put4x4(final Matrix4x3f m, final long destAddr) {
            for (int i = 0; i < 4; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 4), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix4x3f_m00 + 12 * i));
                final long lng = (long)MemUtilUnsafe.UNSAFE.getInt(m, MemUtilUnsafe.Matrix4x3f_m00 + 8L + 12 * i) & 0xFFFFFFFFL;
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + 8L + (i << 4), lng);
            }
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 60L, 1.0f);
        }
        
        public static void put3x4(final Matrix4x3f m, final long destAddr) {
            for (int i = 0; i < 3; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 4), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix4x3f_m00 + 12 * i));
                MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + (i << 4) + 8L, MemUtilUnsafe.UNSAFE.getFloat(m, MemUtilUnsafe.Matrix4x3f_m00 + 8L + 12 * i));
                MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + (i << 4) + 12L, 0.0f);
            }
        }
        
        public static void put4x4(final Matrix4x3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, m.m02());
            u.putDouble(null, destAddr + 24L, 0.0);
            u.putDouble(null, destAddr + 32L, m.m10());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, m.m12());
            u.putDouble(null, destAddr + 56L, 0.0);
            u.putDouble(null, destAddr + 64L, m.m20());
            u.putDouble(null, destAddr + 72L, m.m21());
            u.putDouble(null, destAddr + 80L, m.m22());
            u.putDouble(null, destAddr + 88L, 0.0);
            u.putDouble(null, destAddr + 96L, m.m30());
            u.putDouble(null, destAddr + 104L, m.m31());
            u.putDouble(null, destAddr + 112L, m.m32());
            u.putDouble(null, destAddr + 120L, 1.0);
        }
        
        public static void put4x4(final Matrix3x2f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putLong(null, destAddr, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00));
            u.putLong(null, destAddr + 8L, 0L);
            u.putLong(null, destAddr + 16L, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00 + 8L));
            u.putLong(null, destAddr + 24L, 0L);
            u.putLong(null, destAddr + 32L, 0L);
            u.putLong(null, destAddr + 40L, 1065353216L);
            u.putLong(null, destAddr + 48L, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00 + 16L));
            u.putLong(null, destAddr + 56L, 4575657221408423936L);
        }
        
        public static void put4x4(final Matrix3x2d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, 0.0);
            u.putDouble(null, destAddr + 24L, 0.0);
            u.putDouble(null, destAddr + 32L, m.m10());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, 0.0);
            u.putDouble(null, destAddr + 56L, 0.0);
            u.putDouble(null, destAddr + 64L, 0.0);
            u.putDouble(null, destAddr + 72L, 0.0);
            u.putDouble(null, destAddr + 80L, 1.0);
            u.putDouble(null, destAddr + 88L, 0.0);
            u.putDouble(null, destAddr + 96L, m.m20());
            u.putDouble(null, destAddr + 104L, m.m21());
            u.putDouble(null, destAddr + 112L, 0.0);
            u.putDouble(null, destAddr + 120L, 1.0);
        }
        
        public static void put3x3(final Matrix3x2f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putLong(null, destAddr, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00));
            u.putInt(null, destAddr + 8L, 0);
            u.putLong(null, destAddr + 12L, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00 + 8L));
            u.putInt(null, destAddr + 20L, 0);
            u.putLong(null, destAddr + 24L, u.getLong(m, MemUtilUnsafe.Matrix3x2f_m00 + 16L));
            u.putFloat(null, destAddr + 32L, 1.0f);
        }
        
        public static void put3x3(final Matrix3x2d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, 0.0);
            u.putDouble(null, destAddr + 24L, m.m10());
            u.putDouble(null, destAddr + 32L, m.m11());
            u.putDouble(null, destAddr + 40L, 0.0);
            u.putDouble(null, destAddr + 48L, m.m20());
            u.putDouble(null, destAddr + 56L, m.m21());
            u.putDouble(null, destAddr + 64L, 1.0);
        }
        
        public static void putTransposed(final Matrix4f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, m.m00());
            u.putFloat(null, destAddr + 4L, m.m10());
            u.putFloat(null, destAddr + 8L, m.m20());
            u.putFloat(null, destAddr + 12L, m.m30());
            u.putFloat(null, destAddr + 16L, m.m01());
            u.putFloat(null, destAddr + 20L, m.m11());
            u.putFloat(null, destAddr + 24L, m.m21());
            u.putFloat(null, destAddr + 28L, m.m31());
            u.putFloat(null, destAddr + 32L, m.m02());
            u.putFloat(null, destAddr + 36L, m.m12());
            u.putFloat(null, destAddr + 40L, m.m22());
            u.putFloat(null, destAddr + 44L, m.m32());
            u.putFloat(null, destAddr + 48L, m.m03());
            u.putFloat(null, destAddr + 52L, m.m13());
            u.putFloat(null, destAddr + 56L, m.m23());
            u.putFloat(null, destAddr + 60L, m.m33());
        }
        
        public static void put4x3Transposed(final Matrix4f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, m.m00());
            u.putFloat(null, destAddr + 4L, m.m10());
            u.putFloat(null, destAddr + 8L, m.m20());
            u.putFloat(null, destAddr + 12L, m.m30());
            u.putFloat(null, destAddr + 16L, m.m01());
            u.putFloat(null, destAddr + 20L, m.m11());
            u.putFloat(null, destAddr + 24L, m.m21());
            u.putFloat(null, destAddr + 28L, m.m31());
            u.putFloat(null, destAddr + 32L, m.m02());
            u.putFloat(null, destAddr + 36L, m.m12());
            u.putFloat(null, destAddr + 40L, m.m22());
            u.putFloat(null, destAddr + 44L, m.m32());
        }
        
        public static void putTransposed(final Matrix4x3f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, m.m00());
            u.putFloat(null, destAddr + 4L, m.m10());
            u.putFloat(null, destAddr + 8L, m.m20());
            u.putFloat(null, destAddr + 12L, m.m30());
            u.putFloat(null, destAddr + 16L, m.m01());
            u.putFloat(null, destAddr + 20L, m.m11());
            u.putFloat(null, destAddr + 24L, m.m21());
            u.putFloat(null, destAddr + 28L, m.m31());
            u.putFloat(null, destAddr + 32L, m.m02());
            u.putFloat(null, destAddr + 36L, m.m12());
            u.putFloat(null, destAddr + 40L, m.m22());
            u.putFloat(null, destAddr + 44L, m.m32());
        }
        
        public static void putTransposed(final Matrix3f m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, m.m00());
            u.putFloat(null, destAddr + 4L, m.m10());
            u.putFloat(null, destAddr + 8L, m.m20());
            u.putFloat(null, destAddr + 12L, m.m01());
            u.putFloat(null, destAddr + 16L, m.m11());
            u.putFloat(null, destAddr + 20L, m.m21());
            u.putFloat(null, destAddr + 24L, m.m02());
            u.putFloat(null, destAddr + 28L, m.m12());
            u.putFloat(null, destAddr + 32L, m.m22());
        }
        
        public static void putTransposed(final Matrix2f m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr, m.m00());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 4L, m.m10());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, m.m01());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 12L, m.m11());
        }
        
        public static void put(final Matrix4d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, m.m02());
            u.putDouble(null, destAddr + 24L, m.m03());
            u.putDouble(null, destAddr + 32L, m.m10());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, m.m12());
            u.putDouble(null, destAddr + 56L, m.m13());
            u.putDouble(null, destAddr + 64L, m.m20());
            u.putDouble(null, destAddr + 72L, m.m21());
            u.putDouble(null, destAddr + 80L, m.m22());
            u.putDouble(null, destAddr + 88L, m.m23());
            u.putDouble(null, destAddr + 96L, m.m30());
            u.putDouble(null, destAddr + 104L, m.m31());
            u.putDouble(null, destAddr + 112L, m.m32());
            u.putDouble(null, destAddr + 120L, m.m33());
        }
        
        public static void put(final Matrix4x3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, m.m02());
            u.putDouble(null, destAddr + 24L, m.m10());
            u.putDouble(null, destAddr + 32L, m.m11());
            u.putDouble(null, destAddr + 40L, m.m12());
            u.putDouble(null, destAddr + 48L, m.m20());
            u.putDouble(null, destAddr + 56L, m.m21());
            u.putDouble(null, destAddr + 64L, m.m22());
            u.putDouble(null, destAddr + 72L, m.m30());
            u.putDouble(null, destAddr + 80L, m.m31());
            u.putDouble(null, destAddr + 88L, m.m32());
        }
        
        public static void putTransposed(final Matrix4d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m10());
            u.putDouble(null, destAddr + 16L, m.m20());
            u.putDouble(null, destAddr + 24L, m.m30());
            u.putDouble(null, destAddr + 32L, m.m01());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, m.m21());
            u.putDouble(null, destAddr + 56L, m.m31());
            u.putDouble(null, destAddr + 64L, m.m02());
            u.putDouble(null, destAddr + 72L, m.m12());
            u.putDouble(null, destAddr + 80L, m.m22());
            u.putDouble(null, destAddr + 88L, m.m32());
            u.putDouble(null, destAddr + 96L, m.m03());
            u.putDouble(null, destAddr + 104L, m.m13());
            u.putDouble(null, destAddr + 112L, m.m23());
            u.putDouble(null, destAddr + 120L, m.m33());
        }
        
        public static void putfTransposed(final Matrix4d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, (float)m.m00());
            u.putFloat(null, destAddr + 4L, (float)m.m10());
            u.putFloat(null, destAddr + 8L, (float)m.m20());
            u.putFloat(null, destAddr + 12L, (float)m.m30());
            u.putFloat(null, destAddr + 16L, (float)m.m01());
            u.putFloat(null, destAddr + 20L, (float)m.m11());
            u.putFloat(null, destAddr + 24L, (float)m.m21());
            u.putFloat(null, destAddr + 28L, (float)m.m31());
            u.putFloat(null, destAddr + 32L, (float)m.m02());
            u.putFloat(null, destAddr + 36L, (float)m.m12());
            u.putFloat(null, destAddr + 40L, (float)m.m22());
            u.putFloat(null, destAddr + 44L, (float)m.m32());
            u.putFloat(null, destAddr + 48L, (float)m.m03());
            u.putFloat(null, destAddr + 52L, (float)m.m13());
            u.putFloat(null, destAddr + 56L, (float)m.m23());
            u.putFloat(null, destAddr + 60L, (float)m.m33());
        }
        
        public static void put4x3Transposed(final Matrix4d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m10());
            u.putDouble(null, destAddr + 16L, m.m20());
            u.putDouble(null, destAddr + 24L, m.m30());
            u.putDouble(null, destAddr + 32L, m.m01());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, m.m21());
            u.putDouble(null, destAddr + 56L, m.m31());
            u.putDouble(null, destAddr + 64L, m.m02());
            u.putDouble(null, destAddr + 72L, m.m12());
            u.putDouble(null, destAddr + 80L, m.m22());
            u.putDouble(null, destAddr + 88L, m.m32());
        }
        
        public static void putTransposed(final Matrix4x3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m10());
            u.putDouble(null, destAddr + 16L, m.m20());
            u.putDouble(null, destAddr + 24L, m.m30());
            u.putDouble(null, destAddr + 32L, m.m01());
            u.putDouble(null, destAddr + 40L, m.m11());
            u.putDouble(null, destAddr + 48L, m.m21());
            u.putDouble(null, destAddr + 56L, m.m31());
            u.putDouble(null, destAddr + 64L, m.m02());
            u.putDouble(null, destAddr + 72L, m.m12());
            u.putDouble(null, destAddr + 80L, m.m22());
            u.putDouble(null, destAddr + 88L, m.m32());
        }
        
        public static void putTransposed(final Matrix2d m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr, m.m00());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 8L, m.m10());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 16L, m.m10());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 24L, m.m10());
        }
        
        public static void putfTransposed(final Matrix4x3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, (float)m.m00());
            u.putFloat(null, destAddr + 4L, (float)m.m10());
            u.putFloat(null, destAddr + 8L, (float)m.m20());
            u.putFloat(null, destAddr + 12L, (float)m.m30());
            u.putFloat(null, destAddr + 16L, (float)m.m01());
            u.putFloat(null, destAddr + 20L, (float)m.m11());
            u.putFloat(null, destAddr + 24L, (float)m.m21());
            u.putFloat(null, destAddr + 28L, (float)m.m31());
            u.putFloat(null, destAddr + 32L, (float)m.m02());
            u.putFloat(null, destAddr + 36L, (float)m.m12());
            u.putFloat(null, destAddr + 40L, (float)m.m22());
            u.putFloat(null, destAddr + 44L, (float)m.m32());
        }
        
        public static void putfTransposed(final Matrix2d m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr, (float)m.m00());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 4L, (float)m.m00());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, (float)m.m00());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 12L, (float)m.m00());
        }
        
        public static void putf(final Matrix4d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, (float)m.m00());
            u.putFloat(null, destAddr + 4L, (float)m.m01());
            u.putFloat(null, destAddr + 8L, (float)m.m02());
            u.putFloat(null, destAddr + 12L, (float)m.m03());
            u.putFloat(null, destAddr + 16L, (float)m.m10());
            u.putFloat(null, destAddr + 20L, (float)m.m11());
            u.putFloat(null, destAddr + 24L, (float)m.m12());
            u.putFloat(null, destAddr + 28L, (float)m.m13());
            u.putFloat(null, destAddr + 32L, (float)m.m20());
            u.putFloat(null, destAddr + 36L, (float)m.m21());
            u.putFloat(null, destAddr + 40L, (float)m.m22());
            u.putFloat(null, destAddr + 44L, (float)m.m23());
            u.putFloat(null, destAddr + 48L, (float)m.m30());
            u.putFloat(null, destAddr + 52L, (float)m.m31());
            u.putFloat(null, destAddr + 56L, (float)m.m32());
            u.putFloat(null, destAddr + 60L, (float)m.m33());
        }
        
        public static void putf(final Matrix4x3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, (float)m.m00());
            u.putFloat(null, destAddr + 4L, (float)m.m01());
            u.putFloat(null, destAddr + 8L, (float)m.m02());
            u.putFloat(null, destAddr + 12L, (float)m.m10());
            u.putFloat(null, destAddr + 16L, (float)m.m11());
            u.putFloat(null, destAddr + 20L, (float)m.m12());
            u.putFloat(null, destAddr + 24L, (float)m.m20());
            u.putFloat(null, destAddr + 28L, (float)m.m21());
            u.putFloat(null, destAddr + 32L, (float)m.m22());
            u.putFloat(null, destAddr + 36L, (float)m.m30());
            u.putFloat(null, destAddr + 40L, (float)m.m31());
            u.putFloat(null, destAddr + 44L, (float)m.m32());
        }
        
        public static void put(final Matrix3f m, final long destAddr) {
            for (int i = 0; i < 4; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 3), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix3f_m00 + (i << 3)));
            }
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 32L, m.m22());
        }
        
        public static void put3x4(final Matrix3f m, final long destAddr) {
            for (int i = 0; i < 3; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 4), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix3f_m00 + 12 * i));
                MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + (i << 4) + 8L, MemUtilUnsafe.UNSAFE.getFloat(m, MemUtilUnsafe.Matrix3f_m00 + 8L + 12 * i));
                MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 12 * i, 0.0f);
            }
        }
        
        public static void put(final Matrix3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, m.m02());
            u.putDouble(null, destAddr + 24L, m.m10());
            u.putDouble(null, destAddr + 32L, m.m11());
            u.putDouble(null, destAddr + 40L, m.m12());
            u.putDouble(null, destAddr + 48L, m.m20());
            u.putDouble(null, destAddr + 56L, m.m21());
            u.putDouble(null, destAddr + 64L, m.m22());
        }
        
        public static void put(final Matrix3x2f m, final long destAddr) {
            for (int i = 0; i < 3; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(null, destAddr + (i << 3), MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix3x2f_m00 + (i << 3)));
            }
        }
        
        public static void put(final Matrix3x2d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putDouble(null, destAddr, m.m00());
            u.putDouble(null, destAddr + 8L, m.m01());
            u.putDouble(null, destAddr + 16L, m.m10());
            u.putDouble(null, destAddr + 24L, m.m11());
            u.putDouble(null, destAddr + 32L, m.m20());
            u.putDouble(null, destAddr + 40L, m.m21());
        }
        
        public static void putf(final Matrix3d m, final long destAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, destAddr, (float)m.m00());
            u.putFloat(null, destAddr + 4L, (float)m.m01());
            u.putFloat(null, destAddr + 8L, (float)m.m02());
            u.putFloat(null, destAddr + 12L, (float)m.m10());
            u.putFloat(null, destAddr + 16L, (float)m.m11());
            u.putFloat(null, destAddr + 20L, (float)m.m12());
            u.putFloat(null, destAddr + 24L, (float)m.m20());
            u.putFloat(null, destAddr + 28L, (float)m.m21());
            u.putFloat(null, destAddr + 32L, (float)m.m22());
        }
        
        public static void put(final Matrix2f m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix2f_m00));
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr + 8L, MemUtilUnsafe.UNSAFE.getLong(m, MemUtilUnsafe.Matrix2f_m00 + 8L));
        }
        
        public static void put(final Matrix2d m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr, m.m00());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 8L, m.m01());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 16L, m.m10());
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 24L, m.m11());
        }
        
        public static void putf(final Matrix2d m, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr, (float)m.m00());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 4L, (float)m.m01());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, (float)m.m10());
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 12L, (float)m.m11());
        }
        
        public static void put(final Vector4d src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr, src.x);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 8L, src.y);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 16L, src.z);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 24L, src.w);
        }
        
        public static void putf(final Vector4d src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr, (float)src.x);
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 4L, (float)src.y);
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, (float)src.z);
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 12L, (float)src.w);
        }
        
        public static void put(final Vector4f src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector4f_x));
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr + 8L, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector4f_x + 8L));
        }
        
        public static void put(final Vector4i src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector4i_x));
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr + 8L, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector4i_x + 8L));
        }
        
        public static void put(final Vector3f src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector3f_x));
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, src.z);
        }
        
        public static void put(final Vector3d src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr, src.x);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 8L, src.y);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 16L, src.z);
        }
        
        public static void putf(final Vector3d src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr, (float)src.x);
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 4L, (float)src.y);
            MemUtilUnsafe.UNSAFE.putFloat(null, destAddr + 8L, (float)src.z);
        }
        
        public static void put(final Vector3i src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector3i_x));
            MemUtilUnsafe.UNSAFE.putInt(null, destAddr + 8L, src.z);
        }
        
        public static void put(final Vector2f src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector2f_x));
        }
        
        public static void put(final Vector2d src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr, src.x);
            MemUtilUnsafe.UNSAFE.putDouble(null, destAddr + 8L, src.y);
        }
        
        public static void put(final Vector2i src, final long destAddr) {
            MemUtilUnsafe.UNSAFE.putLong(null, destAddr, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.Vector2i_x));
        }
        
        public static void get(final Matrix4f m, final long srcAddr) {
            for (int i = 0; i < 8; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix4f_m00 + (i << 3), MemUtilUnsafe.UNSAFE.getLong(srcAddr + (i << 3)));
            }
        }
        
        public static void getTransposed(final Matrix4f m, final long srcAddr) {
            m._m00(MemUtilUnsafe.UNSAFE.getFloat(srcAddr))._m10(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 4L))._m20(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 8L))._m30(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 12L))._m01(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 16L))._m11(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 20L))._m21(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 24L))._m31(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 28L))._m02(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 32L))._m12(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 36L))._m22(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 40L))._m32(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 44L))._m03(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 48L))._m13(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 52L))._m23(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 56L))._m33(MemUtilUnsafe.UNSAFE.getFloat(srcAddr + 60L));
        }
        
        public static void get(final Matrix4x3f m, final long srcAddr) {
            for (int i = 0; i < 6; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix4x3f_m00 + (i << 3), MemUtilUnsafe.UNSAFE.getLong(srcAddr + (i << 3)));
            }
        }
        
        public static void get(final Matrix4d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getDouble(null, srcAddr))._m01(u.getDouble(null, srcAddr + 8L))._m02(u.getDouble(null, srcAddr + 16L))._m03(u.getDouble(null, srcAddr + 24L))._m10(u.getDouble(null, srcAddr + 32L))._m11(u.getDouble(null, srcAddr + 40L))._m12(u.getDouble(null, srcAddr + 48L))._m13(u.getDouble(null, srcAddr + 56L))._m20(u.getDouble(null, srcAddr + 64L))._m21(u.getDouble(null, srcAddr + 72L))._m22(u.getDouble(null, srcAddr + 80L))._m23(u.getDouble(null, srcAddr + 88L))._m30(u.getDouble(null, srcAddr + 96L))._m31(u.getDouble(null, srcAddr + 104L))._m32(u.getDouble(null, srcAddr + 112L))._m33(u.getDouble(null, srcAddr + 120L));
        }
        
        public static void get(final Matrix4x3d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getDouble(null, srcAddr))._m01(u.getDouble(null, srcAddr + 8L))._m02(u.getDouble(null, srcAddr + 16L))._m10(u.getDouble(null, srcAddr + 24L))._m11(u.getDouble(null, srcAddr + 32L))._m12(u.getDouble(null, srcAddr + 40L))._m20(u.getDouble(null, srcAddr + 48L))._m21(u.getDouble(null, srcAddr + 56L))._m22(u.getDouble(null, srcAddr + 64L))._m30(u.getDouble(null, srcAddr + 72L))._m31(u.getDouble(null, srcAddr + 80L))._m32(u.getDouble(null, srcAddr + 88L));
        }
        
        public static void getf(final Matrix4d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getFloat(null, srcAddr))._m01(u.getFloat(null, srcAddr + 4L))._m02(u.getFloat(null, srcAddr + 8L))._m03(u.getFloat(null, srcAddr + 12L))._m10(u.getFloat(null, srcAddr + 16L))._m11(u.getFloat(null, srcAddr + 20L))._m12(u.getFloat(null, srcAddr + 24L))._m13(u.getFloat(null, srcAddr + 28L))._m20(u.getFloat(null, srcAddr + 32L))._m21(u.getFloat(null, srcAddr + 36L))._m22(u.getFloat(null, srcAddr + 40L))._m23(u.getFloat(null, srcAddr + 44L))._m30(u.getFloat(null, srcAddr + 48L))._m31(u.getFloat(null, srcAddr + 52L))._m32(u.getFloat(null, srcAddr + 56L))._m33(u.getFloat(null, srcAddr + 60L));
        }
        
        public static void getf(final Matrix4x3d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getFloat(null, srcAddr))._m01(u.getFloat(null, srcAddr + 4L))._m02(u.getFloat(null, srcAddr + 8L))._m10(u.getFloat(null, srcAddr + 12L))._m11(u.getFloat(null, srcAddr + 16L))._m12(u.getFloat(null, srcAddr + 20L))._m20(u.getFloat(null, srcAddr + 24L))._m21(u.getFloat(null, srcAddr + 28L))._m22(u.getFloat(null, srcAddr + 32L))._m30(u.getFloat(null, srcAddr + 36L))._m31(u.getFloat(null, srcAddr + 40L))._m32(u.getFloat(null, srcAddr + 44L));
        }
        
        public static void get(final Matrix3f m, final long srcAddr) {
            for (int i = 0; i < 4; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix3f_m00 + (i << 3), MemUtilUnsafe.UNSAFE.getLong(null, srcAddr + (i << 3)));
            }
            m._m22(MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 32L));
        }
        
        public static void get(final Matrix3d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getDouble(null, srcAddr))._m01(u.getDouble(null, srcAddr + 8L))._m02(u.getDouble(null, srcAddr + 16L))._m10(u.getDouble(null, srcAddr + 24L))._m11(u.getDouble(null, srcAddr + 32L))._m12(u.getDouble(null, srcAddr + 40L))._m20(u.getDouble(null, srcAddr + 48L))._m21(u.getDouble(null, srcAddr + 56L))._m22(u.getDouble(null, srcAddr + 64L));
        }
        
        public static void get(final Matrix3x2f m, final long srcAddr) {
            for (int i = 0; i < 3; ++i) {
                MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix3x2f_m00 + (i << 3), MemUtilUnsafe.UNSAFE.getLong(null, srcAddr + (i << 3)));
            }
        }
        
        public static void get(final Matrix3x2d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getDouble(null, srcAddr))._m01(u.getDouble(null, srcAddr + 8L))._m10(u.getDouble(null, srcAddr + 16L))._m11(u.getDouble(null, srcAddr + 24L))._m20(u.getDouble(null, srcAddr + 32L))._m21(u.getDouble(null, srcAddr + 40L));
        }
        
        public static void getf(final Matrix3d m, final long srcAddr) {
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            m._m00(u.getFloat(null, srcAddr))._m01(u.getFloat(null, srcAddr + 4L))._m02(u.getFloat(null, srcAddr + 8L))._m10(u.getFloat(null, srcAddr + 12L))._m11(u.getFloat(null, srcAddr + 16L))._m12(u.getFloat(null, srcAddr + 20L))._m20(u.getFloat(null, srcAddr + 24L))._m21(u.getFloat(null, srcAddr + 28L))._m22(u.getFloat(null, srcAddr + 32L));
        }
        
        public static void get(final Matrix2f m, final long srcAddr) {
            MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix2f_m00, MemUtilUnsafe.UNSAFE.getLong(null, srcAddr));
            MemUtilUnsafe.UNSAFE.putLong(m, MemUtilUnsafe.Matrix2f_m00 + 8L, MemUtilUnsafe.UNSAFE.getLong(null, srcAddr + 8L));
        }
        
        public static void get(final Matrix2d m, final long srcAddr) {
            m._m00(MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr))._m01(MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 8L))._m10(MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 16L))._m11(MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 24L));
        }
        
        public static void getf(final Matrix2d m, final long srcAddr) {
            m._m00(MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr))._m01(MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 4L))._m10(MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 8L))._m11(MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 12L));
        }
        
        public static void get(final Vector4d dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 8L);
            dst.z = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 16L);
            dst.w = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 24L);
        }
        
        public static void get(final Vector4f dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 4L);
            dst.z = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 8L);
            dst.w = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 12L);
        }
        
        public static void get(final Vector4i dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 4L);
            dst.z = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 8L);
            dst.w = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 12L);
        }
        
        public static void get(final Vector3f dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 4L);
            dst.z = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 8L);
        }
        
        public static void get(final Vector3d dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 8L);
            dst.z = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 16L);
        }
        
        public static void get(final Vector3i dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 4L);
            dst.z = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 8L);
        }
        
        public static void get(final Vector2f dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getFloat(null, srcAddr + 4L);
        }
        
        public static void get(final Vector2d dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getDouble(null, srcAddr + 8L);
        }
        
        public static void get(final Vector2i dst, final long srcAddr) {
            dst.x = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr);
            dst.y = MemUtilUnsafe.UNSAFE.getInt(null, srcAddr + 4L);
        }
        
        public static void putMatrix3f(final Quaternionf q, final long addr) {
            final float dx = q.x + q.x;
            final float dy = q.y + q.y;
            final float dz = q.z + q.z;
            final float q2 = dx * q.x;
            final float q3 = dy * q.y;
            final float q4 = dz * q.z;
            final float q5 = dx * q.y;
            final float q6 = dx * q.z;
            final float q7 = dx * q.w;
            final float q8 = dy * q.z;
            final float q9 = dy * q.w;
            final float q10 = dz * q.w;
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, addr, 1.0f - q3 - q4);
            u.putFloat(null, addr + 4L, q5 + q10);
            u.putFloat(null, addr + 8L, q6 - q9);
            u.putFloat(null, addr + 12L, q5 - q10);
            u.putFloat(null, addr + 16L, 1.0f - q4 - q2);
            u.putFloat(null, addr + 20L, q8 + q7);
            u.putFloat(null, addr + 24L, q6 + q9);
            u.putFloat(null, addr + 28L, q8 - q7);
            u.putFloat(null, addr + 32L, 1.0f - q3 - q2);
        }
        
        public static void putMatrix4f(final Quaternionf q, final long addr) {
            final float dx = q.x + q.x;
            final float dy = q.y + q.y;
            final float dz = q.z + q.z;
            final float q2 = dx * q.x;
            final float q3 = dy * q.y;
            final float q4 = dz * q.z;
            final float q5 = dx * q.y;
            final float q6 = dx * q.z;
            final float q7 = dx * q.w;
            final float q8 = dy * q.z;
            final float q9 = dy * q.w;
            final float q10 = dz * q.w;
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, addr, 1.0f - q3 - q4);
            u.putFloat(null, addr + 4L, q5 + q10);
            u.putLong(null, addr + 8L, (long)Float.floatToRawIntBits(q6 - q9) & 0xFFFFFFFFL);
            u.putFloat(null, addr + 16L, q5 - q10);
            u.putFloat(null, addr + 20L, 1.0f - q4 - q2);
            u.putLong(null, addr + 24L, (long)Float.floatToRawIntBits(q8 + q7) & 0xFFFFFFFFL);
            u.putFloat(null, addr + 32L, q6 + q9);
            u.putFloat(null, addr + 36L, q8 - q7);
            u.putLong(null, addr + 40L, (long)Float.floatToRawIntBits(1.0f - q3 - q2) & 0xFFFFFFFFL);
            u.putLong(null, addr + 48L, 0L);
            u.putLong(null, addr + 56L, 4575657221408423936L);
        }
        
        public static void putMatrix4x3f(final Quaternionf q, final long addr) {
            final float dx = q.x + q.x;
            final float dy = q.y + q.y;
            final float dz = q.z + q.z;
            final float q2 = dx * q.x;
            final float q3 = dy * q.y;
            final float q4 = dz * q.z;
            final float q5 = dx * q.y;
            final float q6 = dx * q.z;
            final float q7 = dx * q.w;
            final float q8 = dy * q.z;
            final float q9 = dy * q.w;
            final float q10 = dz * q.w;
            final Unsafe u = MemUtilUnsafe.UNSAFE;
            u.putFloat(null, addr, 1.0f - q3 - q4);
            u.putFloat(null, addr + 4L, q5 + q10);
            u.putFloat(null, addr + 8L, q6 - q9);
            u.putFloat(null, addr + 12L, q5 - q10);
            u.putFloat(null, addr + 16L, 1.0f - q4 - q2);
            u.putFloat(null, addr + 20L, q8 + q7);
            u.putFloat(null, addr + 24L, q6 + q9);
            u.putFloat(null, addr + 28L, q8 - q7);
            u.putFloat(null, addr + 32L, 1.0f - q3 - q2);
            u.putLong(null, addr + 36L, 0L);
            u.putFloat(null, addr + 44L, 0.0f);
        }
        
        private static void throwNoDirectBufferException() {
            throw new IllegalArgumentException("Must use a direct buffer");
        }
        
        public void putMatrix3f(final Quaternionf q, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 36);
            }
            putMatrix3f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putMatrix3f(final Quaternionf q, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            putMatrix3f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        private static void checkPut(final int offset, final boolean direct, final int capacity, final int i) {
            if (!direct) {
                throwNoDirectBufferException();
            }
            if (capacity - offset < i) {
                throw new BufferOverflowException();
            }
        }
        
        public void putMatrix4f(final Quaternionf q, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            putMatrix4f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putMatrix4f(final Quaternionf q, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putMatrix4f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putMatrix4x3f(final Quaternionf q, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            putMatrix4x3f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putMatrix4x3f(final Quaternionf q, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putMatrix4x3f(q, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x3(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put4x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put4x3(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put4x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put3x4(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put3x4(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x4(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put4x4(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put3x4(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put3x4(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x4(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put4x4(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 128);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x4(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put4x4(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x4(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put4x4(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 128);
            }
            put4x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put3x3(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            put3x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put3x3(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 36);
            }
            put3x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put3x3(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            put3x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put3x3(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 72);
            }
            put3x3(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putTransposed(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x3Transposed(final Matrix4f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put4x3Transposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put4x3Transposed(final Matrix4f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put4x3Transposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix4x3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putTransposed(final Matrix4x3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putTransposed(final Matrix3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 36);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix2f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putTransposed(final Matrix2f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Matrix4d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 128);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 96);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Matrix4d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putf(final Matrix4d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Matrix4x3d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putf(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void putTransposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 128);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put4x3Transposed(final Matrix4d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put4x3Transposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put4x3Transposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 96);
            }
            put4x3Transposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix4x3d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void putTransposed(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 96);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putTransposed(final Matrix2d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void putTransposed(final Matrix2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 32);
            }
            putTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putfTransposed(final Matrix4d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putfTransposed(final Matrix4d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 64);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putfTransposed(final Matrix4x3d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putfTransposed(final Matrix4x3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putfTransposed(final Matrix2d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putfTransposed(final Matrix2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putfTransposed(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 36);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put3x4(final Matrix3f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put3x4(final Matrix3f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put3x4(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix3d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Matrix3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 72);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix3x2f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 6);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix3x2f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 24);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix3x2d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 6);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Matrix3x2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 48);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Matrix3d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 9);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putf(final Matrix3d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 36);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix2f m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Matrix2f m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Matrix2d m, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Matrix2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Matrix2d m, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void putf(final Matrix2d m, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putf(m, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector4d src, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Vector4d src, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            putf(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector4d src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 32);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Vector4d src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            putf(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector4f src, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector4f src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector4i src, final int offset, final IntBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 4);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector4i src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector3f src, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 3);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector3f src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector3d src, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 3);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Vector3d src, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 3);
            }
            putf(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector3d src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 24);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void putf(final Vector3d src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            putf(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector3i src, final int offset, final IntBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 3);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector3i src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 12);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector2f src, final int offset, final FloatBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 2);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector2f src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 8);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector2d src, final int offset, final DoubleBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 2);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void put(final Vector2d src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 16);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void put(final Vector2i src, final int offset, final IntBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 2);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void put(final Vector2i src, final int offset, final ByteBuffer dest) {
            if (Options.DEBUG) {
                checkPut(offset, dest.isDirect(), dest.capacity(), 8);
            }
            put(src, MemUtilUnsafe.UNSAFE.getLong(dest, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix4f m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Matrix4f m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 64);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public float get(final Matrix4f m, final int column, final int row) {
            return MemUtilUnsafe.UNSAFE.getFloat(m, MemUtilUnsafe.Matrix4f_m00 + (column << 4) + (row << 2));
        }
        
        public Matrix4f set(final Matrix4f m, final int column, final int row, final float value) {
            MemUtilUnsafe.UNSAFE.putFloat(m, MemUtilUnsafe.Matrix4f_m00 + (column << 4) + (row << 2), value);
            return m;
        }
        
        public double get(final Matrix4d m, final int column, final int row) {
            return MemUtilUnsafe.UNSAFE.getDouble(m, MemUtilUnsafe.Matrix4d_m00 + (column << 5) + (row << 3));
        }
        
        public Matrix4d set(final Matrix4d m, final int column, final int row, final double value) {
            MemUtilUnsafe.UNSAFE.putDouble(m, MemUtilUnsafe.Matrix4d_m00 + (column << 5) + (row << 3), value);
            return m;
        }
        
        public float get(final Matrix3f m, final int column, final int row) {
            return MemUtilUnsafe.UNSAFE.getFloat(m, MemUtilUnsafe.Matrix3f_m00 + column * 12 + (row << 2));
        }
        
        public Matrix3f set(final Matrix3f m, final int column, final int row, final float value) {
            MemUtilUnsafe.UNSAFE.putFloat(m, MemUtilUnsafe.Matrix3f_m00 + column * 12 + (row << 2), value);
            return m;
        }
        
        public double get(final Matrix3d m, final int column, final int row) {
            return MemUtilUnsafe.UNSAFE.getDouble(m, MemUtilUnsafe.Matrix3d_m00 + column * 24 + (row << 3));
        }
        
        public Matrix3d set(final Matrix3d m, final int column, final int row, final double value) {
            MemUtilUnsafe.UNSAFE.putDouble(m, MemUtilUnsafe.Matrix3d_m00 + column * 24 + (row << 3), value);
            return m;
        }
        
        public void get(final Matrix4x3f m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 12);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Matrix4x3f m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 48);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix4d m, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Matrix4d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 128);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix4x3d m, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 12);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Matrix4x3d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 96);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void getf(final Matrix4d m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void getf(final Matrix4d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 64);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void getf(final Matrix4x3d m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 12);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        private static void checkGet(final int offset, final boolean direct, final int capacity, final int i) {
            if (!direct) {
                throwNoDirectBufferException();
            }
            if (capacity - offset < i) {
                throw new BufferUnderflowException();
            }
        }
        
        public void getf(final Matrix4x3d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 48);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix3f m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 9);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Matrix3f m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 36);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix3d m, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 9);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Matrix3d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 72);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix3x2f m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 6);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Matrix3x2f m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 24);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix3x2d m, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 6);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Matrix3x2d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 48);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void getf(final Matrix3d m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 9);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void getf(final Matrix3d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 36);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix2f m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Matrix2f m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Matrix2d m, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Matrix2d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 32);
            }
            get(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void getf(final Matrix2d m, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void getf(final Matrix2d m, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            getf(m, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector4d dst, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Vector4d dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 32);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector4f dst, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector4f dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector4i dst, final int offset, final IntBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 4);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector4i dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector3f dst, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 3);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector3f dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 12);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector3d dst, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 3);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Vector3d dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 24);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector3i dst, final int offset, final IntBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 3);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector3i dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 12);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector2f dst, final int offset, final FloatBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 2);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector2f dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 8);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector2d dst, final int offset, final DoubleBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 2);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 3));
        }
        
        public void get(final Vector2d dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 16);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        public void get(final Vector2i dst, final int offset, final IntBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 2);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + (offset << 2));
        }
        
        public void get(final Vector2i dst, final int offset, final ByteBuffer src) {
            if (Options.DEBUG) {
                checkGet(offset, src.isDirect(), src.capacity(), 8);
            }
            get(dst, MemUtilUnsafe.UNSAFE.getLong(src, MemUtilUnsafe.ADDRESS) + offset);
        }
        
        static {
            UNSAFE = getUnsafeInstance();
            try {
                ADDRESS = findBufferAddress();
                Matrix4f_m00 = checkMatrix4f();
                Matrix4d_m00 = checkMatrix4d();
                Matrix4x3f_m00 = checkMatrix4x3f();
                Matrix3f_m00 = checkMatrix3f();
                Matrix3d_m00 = checkMatrix3d();
                Matrix3x2f_m00 = checkMatrix3x2f();
                Matrix2f_m00 = checkMatrix2f();
                Vector4f_x = checkVector4f();
                Vector4i_x = checkVector4i();
                Vector3f_x = checkVector3f();
                Vector3i_x = checkVector3i();
                Vector2f_x = checkVector2f();
                Vector2i_x = checkVector2i();
                Quaternionf_x = checkQuaternionf();
                floatArrayOffset = MemUtilUnsafe.UNSAFE.arrayBaseOffset((MemUtilUnsafe.class$16 == null) ? (MemUtilUnsafe.class$16 = class$("[F")) : MemUtilUnsafe.class$16);
                ((MemUtilUnsafe.class$15 == null) ? (MemUtilUnsafe.class$15 = class$("sun.misc.Unsafe")) : MemUtilUnsafe.class$15).getDeclaredMethod("getLong", (MemUtilUnsafe.class$17 == null) ? (MemUtilUnsafe.class$17 = class$("java.lang.Object")) : MemUtilUnsafe.class$17, Long.TYPE);
                ((MemUtilUnsafe.class$15 == null) ? (MemUtilUnsafe.class$15 = class$("sun.misc.Unsafe")) : MemUtilUnsafe.class$15).getDeclaredMethod("putLong", (MemUtilUnsafe.class$17 == null) ? (MemUtilUnsafe.class$17 = class$("java.lang.Object")) : MemUtilUnsafe.class$17, Long.TYPE, Long.TYPE);
            }
            catch (NoSuchFieldException e) {
                throw new UnsupportedOperationException(e);
            }
            catch (NoSuchMethodException e2) {
                throw new UnsupportedOperationException(e2);
            }
        }
    }
}
