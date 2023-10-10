// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix3x2fc
{
    float m00();
    
    float m01();
    
    float m10();
    
    float m11();
    
    float m20();
    
    float m21();
    
    Matrix3x2f mul(final Matrix3x2fc p0, final Matrix3x2f p1);
    
    Matrix3x2f mulLocal(final Matrix3x2fc p0, final Matrix3x2f p1);
    
    float determinant();
    
    Matrix3x2f invert(final Matrix3x2f p0);
    
    Matrix3x2f translate(final float p0, final float p1, final Matrix3x2f p2);
    
    Matrix3x2f translate(final Vector2fc p0, final Matrix3x2f p1);
    
    Matrix3x2f translateLocal(final Vector2fc p0, final Matrix3x2f p1);
    
    Matrix3x2f translateLocal(final float p0, final float p1, final Matrix3x2f p2);
    
    Matrix3x2f get(final Matrix3x2f p0);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    FloatBuffer get3x3(final FloatBuffer p0);
    
    FloatBuffer get3x3(final int p0, final FloatBuffer p1);
    
    ByteBuffer get3x3(final ByteBuffer p0);
    
    ByteBuffer get3x3(final int p0, final ByteBuffer p1);
    
    FloatBuffer get4x4(final FloatBuffer p0);
    
    FloatBuffer get4x4(final int p0, final FloatBuffer p1);
    
    ByteBuffer get4x4(final ByteBuffer p0);
    
    ByteBuffer get4x4(final int p0, final ByteBuffer p1);
    
    Matrix3x2fc getToAddress(final long p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    float[] get3x3(final float[] p0, final int p1);
    
    float[] get3x3(final float[] p0);
    
    float[] get4x4(final float[] p0, final int p1);
    
    float[] get4x4(final float[] p0);
    
    Matrix3x2f scale(final float p0, final float p1, final Matrix3x2f p2);
    
    Matrix3x2f scale(final Vector2fc p0, final Matrix3x2f p1);
    
    Matrix3x2f scaleAroundLocal(final float p0, final float p1, final float p2, final float p3, final Matrix3x2f p4);
    
    Matrix3x2f scaleAroundLocal(final float p0, final float p1, final float p2, final Matrix3x2f p3);
    
    Matrix3x2f scale(final float p0, final Matrix3x2f p1);
    
    Matrix3x2f scaleLocal(final float p0, final Matrix3x2f p1);
    
    Matrix3x2f scaleLocal(final float p0, final float p1, final Matrix3x2f p2);
    
    Matrix3x2f scaleAround(final float p0, final float p1, final float p2, final float p3, final Matrix3x2f p4);
    
    Matrix3x2f scaleAround(final float p0, final float p1, final float p2, final Matrix3x2f p3);
    
    Vector3f transform(final Vector3f p0);
    
    Vector3f transform(final Vector3f p0, final Vector3f p1);
    
    Vector3f transform(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector2f transformPosition(final Vector2f p0);
    
    Vector2f transformPosition(final Vector2fc p0, final Vector2f p1);
    
    Vector2f transformPosition(final float p0, final float p1, final Vector2f p2);
    
    Vector2f transformDirection(final Vector2f p0);
    
    Vector2f transformDirection(final Vector2fc p0, final Vector2f p1);
    
    Vector2f transformDirection(final float p0, final float p1, final Vector2f p2);
    
    Matrix3x2f rotate(final float p0, final Matrix3x2f p1);
    
    Matrix3x2f rotateLocal(final float p0, final Matrix3x2f p1);
    
    Matrix3x2f rotateAbout(final float p0, final float p1, final float p2, final Matrix3x2f p3);
    
    Matrix3x2f rotateTo(final Vector2fc p0, final Vector2fc p1, final Matrix3x2f p2);
    
    Matrix3x2f view(final float p0, final float p1, final float p2, final float p3, final Matrix3x2f p4);
    
    Vector2f origin(final Vector2f p0);
    
    float[] viewArea(final float[] p0);
    
    Vector2f positiveX(final Vector2f p0);
    
    Vector2f normalizedPositiveX(final Vector2f p0);
    
    Vector2f positiveY(final Vector2f p0);
    
    Vector2f normalizedPositiveY(final Vector2f p0);
    
    Vector2f unproject(final float p0, final float p1, final int[] p2, final Vector2f p3);
    
    Vector2f unprojectInv(final float p0, final float p1, final int[] p2, final Vector2f p3);
    
    boolean testPoint(final float p0, final float p1);
    
    boolean testCircle(final float p0, final float p1, final float p2);
    
    boolean testAar(final float p0, final float p1, final float p2, final float p3);
    
    boolean equals(final Matrix3x2fc p0, final float p1);
    
    boolean isFinite();
}
