// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix2fc
{
    float m00();
    
    float m01();
    
    float m10();
    
    float m11();
    
    Matrix2f mul(final Matrix2fc p0, final Matrix2f p1);
    
    Matrix2f mulLocal(final Matrix2fc p0, final Matrix2f p1);
    
    float determinant();
    
    Matrix2f invert(final Matrix2f p0);
    
    Matrix2f transpose(final Matrix2f p0);
    
    Matrix2f get(final Matrix2f p0);
    
    Matrix3x2f get(final Matrix3x2f p0);
    
    Matrix3f get(final Matrix3f p0);
    
    float getRotation();
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    FloatBuffer getTransposed(final FloatBuffer p0);
    
    FloatBuffer getTransposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    Matrix2fc getToAddress(final long p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    Matrix2f scale(final Vector2fc p0, final Matrix2f p1);
    
    Matrix2f scale(final float p0, final float p1, final Matrix2f p2);
    
    Matrix2f scale(final float p0, final Matrix2f p1);
    
    Matrix2f scaleLocal(final float p0, final float p1, final Matrix2f p2);
    
    Vector2f transform(final Vector2f p0);
    
    Vector2f transform(final Vector2fc p0, final Vector2f p1);
    
    Vector2f transform(final float p0, final float p1, final Vector2f p2);
    
    Vector2f transformTranspose(final Vector2f p0);
    
    Vector2f transformTranspose(final Vector2fc p0, final Vector2f p1);
    
    Vector2f transformTranspose(final float p0, final float p1, final Vector2f p2);
    
    Matrix2f rotate(final float p0, final Matrix2f p1);
    
    Matrix2f rotateLocal(final float p0, final Matrix2f p1);
    
    Vector2f getRow(final int p0, final Vector2f p1) throws IndexOutOfBoundsException;
    
    Vector2f getColumn(final int p0, final Vector2f p1) throws IndexOutOfBoundsException;
    
    float get(final int p0, final int p1);
    
    Matrix2f normal(final Matrix2f p0);
    
    Vector2f getScale(final Vector2f p0);
    
    Vector2f positiveX(final Vector2f p0);
    
    Vector2f normalizedPositiveX(final Vector2f p0);
    
    Vector2f positiveY(final Vector2f p0);
    
    Vector2f normalizedPositiveY(final Vector2f p0);
    
    Matrix2f add(final Matrix2fc p0, final Matrix2f p1);
    
    Matrix2f sub(final Matrix2fc p0, final Matrix2f p1);
    
    Matrix2f mulComponentWise(final Matrix2fc p0, final Matrix2f p1);
    
    Matrix2f lerp(final Matrix2fc p0, final float p1, final Matrix2f p2);
    
    boolean equals(final Matrix2fc p0, final float p1);
    
    boolean isFinite();
}
