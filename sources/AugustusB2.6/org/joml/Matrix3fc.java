// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix3fc
{
    float m00();
    
    float m01();
    
    float m02();
    
    float m10();
    
    float m11();
    
    float m12();
    
    float m20();
    
    float m21();
    
    float m22();
    
    Matrix3f mul(final Matrix3fc p0, final Matrix3f p1);
    
    Matrix3f mulLocal(final Matrix3fc p0, final Matrix3f p1);
    
    float determinant();
    
    Matrix3f invert(final Matrix3f p0);
    
    Matrix3f transpose(final Matrix3f p0);
    
    Matrix3f get(final Matrix3f p0);
    
    Matrix4f get(final Matrix4f p0);
    
    AxisAngle4f getRotation(final AxisAngle4f p0);
    
    Quaternionf getUnnormalizedRotation(final Quaternionf p0);
    
    Quaternionf getNormalizedRotation(final Quaternionf p0);
    
    Quaterniond getUnnormalizedRotation(final Quaterniond p0);
    
    Quaterniond getNormalizedRotation(final Quaterniond p0);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    FloatBuffer get3x4(final FloatBuffer p0);
    
    FloatBuffer get3x4(final int p0, final FloatBuffer p1);
    
    ByteBuffer get3x4(final ByteBuffer p0);
    
    ByteBuffer get3x4(final int p0, final ByteBuffer p1);
    
    FloatBuffer getTransposed(final FloatBuffer p0);
    
    FloatBuffer getTransposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    Matrix3fc getToAddress(final long p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    Matrix3f scale(final Vector3fc p0, final Matrix3f p1);
    
    Matrix3f scale(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Matrix3f scale(final float p0, final Matrix3f p1);
    
    Matrix3f scaleLocal(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Vector3f transform(final Vector3f p0);
    
    Vector3f transform(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transform(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f transformTranspose(final Vector3f p0);
    
    Vector3f transformTranspose(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformTranspose(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Matrix3f rotateX(final float p0, final Matrix3f p1);
    
    Matrix3f rotateY(final float p0, final Matrix3f p1);
    
    Matrix3f rotateZ(final float p0, final Matrix3f p1);
    
    Matrix3f rotateXYZ(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Matrix3f rotateZYX(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Matrix3f rotateYXZ(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Matrix3f rotate(final float p0, final float p1, final float p2, final float p3, final Matrix3f p4);
    
    Matrix3f rotateLocal(final float p0, final float p1, final float p2, final float p3, final Matrix3f p4);
    
    Matrix3f rotateLocalX(final float p0, final Matrix3f p1);
    
    Matrix3f rotateLocalY(final float p0, final Matrix3f p1);
    
    Matrix3f rotateLocalZ(final float p0, final Matrix3f p1);
    
    Matrix3f rotate(final Quaternionfc p0, final Matrix3f p1);
    
    Matrix3f rotateLocal(final Quaternionfc p0, final Matrix3f p1);
    
    Matrix3f rotate(final AxisAngle4f p0, final Matrix3f p1);
    
    Matrix3f rotate(final float p0, final Vector3fc p1, final Matrix3f p2);
    
    Matrix3f lookAlong(final Vector3fc p0, final Vector3fc p1, final Matrix3f p2);
    
    Matrix3f lookAlong(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix3f p6);
    
    Vector3f getRow(final int p0, final Vector3f p1) throws IndexOutOfBoundsException;
    
    Vector3f getColumn(final int p0, final Vector3f p1) throws IndexOutOfBoundsException;
    
    float get(final int p0, final int p1);
    
    float getRowColumn(final int p0, final int p1);
    
    Matrix3f normal(final Matrix3f p0);
    
    Matrix3f cofactor(final Matrix3f p0);
    
    Vector3f getScale(final Vector3f p0);
    
    Vector3f positiveZ(final Vector3f p0);
    
    Vector3f normalizedPositiveZ(final Vector3f p0);
    
    Vector3f positiveX(final Vector3f p0);
    
    Vector3f normalizedPositiveX(final Vector3f p0);
    
    Vector3f positiveY(final Vector3f p0);
    
    Vector3f normalizedPositiveY(final Vector3f p0);
    
    Matrix3f add(final Matrix3fc p0, final Matrix3f p1);
    
    Matrix3f sub(final Matrix3fc p0, final Matrix3f p1);
    
    Matrix3f mulComponentWise(final Matrix3fc p0, final Matrix3f p1);
    
    Matrix3f lerp(final Matrix3fc p0, final float p1, final Matrix3f p2);
    
    Matrix3f rotateTowards(final Vector3fc p0, final Vector3fc p1, final Matrix3f p2);
    
    Matrix3f rotateTowards(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix3f p6);
    
    Vector3f getEulerAnglesXYZ(final Vector3f p0);
    
    Vector3f getEulerAnglesZYX(final Vector3f p0);
    
    Matrix3f obliqueZ(final float p0, final float p1, final Matrix3f p2);
    
    boolean equals(final Matrix3fc p0, final float p1);
    
    Matrix3f reflect(final float p0, final float p1, final float p2, final Matrix3f p3);
    
    Matrix3f reflect(final Quaternionfc p0, final Matrix3f p1);
    
    Matrix3f reflect(final Vector3fc p0, final Matrix3f p1);
    
    boolean isFinite();
    
    float quadraticFormProduct(final float p0, final float p1, final float p2);
    
    float quadraticFormProduct(final Vector3fc p0);
    
    Matrix3f mapXZY(final Matrix3f p0);
    
    Matrix3f mapXZnY(final Matrix3f p0);
    
    Matrix3f mapXnYnZ(final Matrix3f p0);
    
    Matrix3f mapXnZY(final Matrix3f p0);
    
    Matrix3f mapXnZnY(final Matrix3f p0);
    
    Matrix3f mapYXZ(final Matrix3f p0);
    
    Matrix3f mapYXnZ(final Matrix3f p0);
    
    Matrix3f mapYZX(final Matrix3f p0);
    
    Matrix3f mapYZnX(final Matrix3f p0);
    
    Matrix3f mapYnXZ(final Matrix3f p0);
    
    Matrix3f mapYnXnZ(final Matrix3f p0);
    
    Matrix3f mapYnZX(final Matrix3f p0);
    
    Matrix3f mapYnZnX(final Matrix3f p0);
    
    Matrix3f mapZXY(final Matrix3f p0);
    
    Matrix3f mapZXnY(final Matrix3f p0);
    
    Matrix3f mapZYX(final Matrix3f p0);
    
    Matrix3f mapZYnX(final Matrix3f p0);
    
    Matrix3f mapZnXY(final Matrix3f p0);
    
    Matrix3f mapZnXnY(final Matrix3f p0);
    
    Matrix3f mapZnYX(final Matrix3f p0);
    
    Matrix3f mapZnYnX(final Matrix3f p0);
    
    Matrix3f mapnXYnZ(final Matrix3f p0);
    
    Matrix3f mapnXZY(final Matrix3f p0);
    
    Matrix3f mapnXZnY(final Matrix3f p0);
    
    Matrix3f mapnXnYZ(final Matrix3f p0);
    
    Matrix3f mapnXnYnZ(final Matrix3f p0);
    
    Matrix3f mapnXnZY(final Matrix3f p0);
    
    Matrix3f mapnXnZnY(final Matrix3f p0);
    
    Matrix3f mapnYXZ(final Matrix3f p0);
    
    Matrix3f mapnYXnZ(final Matrix3f p0);
    
    Matrix3f mapnYZX(final Matrix3f p0);
    
    Matrix3f mapnYZnX(final Matrix3f p0);
    
    Matrix3f mapnYnXZ(final Matrix3f p0);
    
    Matrix3f mapnYnXnZ(final Matrix3f p0);
    
    Matrix3f mapnYnZX(final Matrix3f p0);
    
    Matrix3f mapnYnZnX(final Matrix3f p0);
    
    Matrix3f mapnZXY(final Matrix3f p0);
    
    Matrix3f mapnZXnY(final Matrix3f p0);
    
    Matrix3f mapnZYX(final Matrix3f p0);
    
    Matrix3f mapnZYnX(final Matrix3f p0);
    
    Matrix3f mapnZnXY(final Matrix3f p0);
    
    Matrix3f mapnZnXnY(final Matrix3f p0);
    
    Matrix3f mapnZnYX(final Matrix3f p0);
    
    Matrix3f mapnZnYnX(final Matrix3f p0);
    
    Matrix3f negateX(final Matrix3f p0);
    
    Matrix3f negateY(final Matrix3f p0);
    
    Matrix3f negateZ(final Matrix3f p0);
}
