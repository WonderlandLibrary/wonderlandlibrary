// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix4x3fc
{
    public static final int PLANE_NX = 0;
    public static final int PLANE_PX = 1;
    public static final int PLANE_NY = 2;
    public static final int PLANE_PY = 3;
    public static final int PLANE_NZ = 4;
    public static final int PLANE_PZ = 5;
    public static final byte PROPERTY_IDENTITY = 4;
    public static final byte PROPERTY_TRANSLATION = 8;
    public static final byte PROPERTY_ORTHONORMAL = 16;
    
    int properties();
    
    float m00();
    
    float m01();
    
    float m02();
    
    float m10();
    
    float m11();
    
    float m12();
    
    float m20();
    
    float m21();
    
    float m22();
    
    float m30();
    
    float m31();
    
    float m32();
    
    Matrix4f get(final Matrix4f p0);
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix4x3f mul(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f mulTranslation(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f mulOrtho(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f mul3x3(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4x3f p9);
    
    Matrix4x3f fma(final Matrix4x3fc p0, final float p1, final Matrix4x3f p2);
    
    Matrix4x3f add(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f sub(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f mulComponentWise(final Matrix4x3fc p0, final Matrix4x3f p1);
    
    float determinant();
    
    Matrix4x3f invert(final Matrix4x3f p0);
    
    Matrix4f invert(final Matrix4f p0);
    
    Matrix4x3f invertOrtho(final Matrix4x3f p0);
    
    Matrix4x3f transpose3x3(final Matrix4x3f p0);
    
    Matrix3f transpose3x3(final Matrix3f p0);
    
    Vector3f getTranslation(final Vector3f p0);
    
    Vector3f getScale(final Vector3f p0);
    
    Matrix4x3f get(final Matrix4x3f p0);
    
    Matrix4x3d get(final Matrix4x3d p0);
    
    AxisAngle4f getRotation(final AxisAngle4f p0);
    
    AxisAngle4d getRotation(final AxisAngle4d p0);
    
    Quaternionf getUnnormalizedRotation(final Quaternionf p0);
    
    Quaternionf getNormalizedRotation(final Quaternionf p0);
    
    Quaterniond getUnnormalizedRotation(final Quaterniond p0);
    
    Quaterniond getNormalizedRotation(final Quaterniond p0);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Matrix4x3fc getToAddress(final long p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    float[] get4x4(final float[] p0, final int p1);
    
    float[] get4x4(final float[] p0);
    
    FloatBuffer get4x4(final FloatBuffer p0);
    
    FloatBuffer get4x4(final int p0, final FloatBuffer p1);
    
    ByteBuffer get4x4(final ByteBuffer p0);
    
    ByteBuffer get4x4(final int p0, final ByteBuffer p1);
    
    FloatBuffer get3x4(final FloatBuffer p0);
    
    FloatBuffer get3x4(final int p0, final FloatBuffer p1);
    
    ByteBuffer get3x4(final ByteBuffer p0);
    
    ByteBuffer get3x4(final int p0, final ByteBuffer p1);
    
    FloatBuffer getTransposed(final FloatBuffer p0);
    
    FloatBuffer getTransposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    float[] getTransposed(final float[] p0, final int p1);
    
    float[] getTransposed(final float[] p0);
    
    Vector4f transform(final Vector4f p0);
    
    Vector4f transform(final Vector4fc p0, final Vector4f p1);
    
    Vector3f transformPosition(final Vector3f p0);
    
    Vector3f transformPosition(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformDirection(final Vector3f p0);
    
    Vector3f transformDirection(final Vector3fc p0, final Vector3f p1);
    
    Matrix4x3f scale(final Vector3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f scale(final float p0, final Matrix4x3f p1);
    
    Matrix4x3f scaleXY(final float p0, final float p1, final Matrix4x3f p2);
    
    Matrix4x3f scaleAround(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f scaleAround(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f scale(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f scaleLocal(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f rotateX(final float p0, final Matrix4x3f p1);
    
    Matrix4x3f rotateY(final float p0, final Matrix4x3f p1);
    
    Matrix4x3f rotateZ(final float p0, final Matrix4x3f p1);
    
    Matrix4x3f rotateXYZ(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f rotateZYX(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f rotateYXZ(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f rotate(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f rotateTranslation(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f rotateAround(final Quaternionfc p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f rotateLocal(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f translate(final Vector3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f translate(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f translateLocal(final Vector3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f translateLocal(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f ortho(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4x3f p7);
    
    Matrix4x3f ortho(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f orthoLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4x3f p7);
    
    Matrix4x3f orthoLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f orthoSymmetric(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4x3f p5);
    
    Matrix4x3f orthoSymmetric(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f orthoSymmetricLH(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4x3f p5);
    
    Matrix4x3f orthoSymmetricLH(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f ortho2D(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f ortho2DLH(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f lookAlong(final Vector3fc p0, final Vector3fc p1, final Matrix4x3f p2);
    
    Matrix4x3f lookAlong(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f lookAt(final Vector3fc p0, final Vector3fc p1, final Vector3fc p2, final Matrix4x3f p3);
    
    Matrix4x3f lookAt(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4x3f p9);
    
    Matrix4x3f lookAtLH(final Vector3fc p0, final Vector3fc p1, final Vector3fc p2, final Matrix4x3f p3);
    
    Matrix4x3f lookAtLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4x3f p9);
    
    Matrix4x3f rotate(final Quaternionfc p0, final Matrix4x3f p1);
    
    Matrix4x3f rotateTranslation(final Quaternionfc p0, final Matrix4x3f p1);
    
    Matrix4x3f rotateLocal(final Quaternionfc p0, final Matrix4x3f p1);
    
    Matrix4x3f rotate(final AxisAngle4f p0, final Matrix4x3f p1);
    
    Matrix4x3f rotate(final float p0, final Vector3fc p1, final Matrix4x3f p2);
    
    Matrix4x3f reflect(final float p0, final float p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f reflect(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f reflect(final Quaternionfc p0, final Vector3fc p1, final Matrix4x3f p2);
    
    Matrix4x3f reflect(final Vector3fc p0, final Vector3fc p1, final Matrix4x3f p2);
    
    Vector4f getRow(final int p0, final Vector4f p1) throws IndexOutOfBoundsException;
    
    Vector3f getColumn(final int p0, final Vector3f p1) throws IndexOutOfBoundsException;
    
    Matrix4x3f normal(final Matrix4x3f p0);
    
    Matrix3f normal(final Matrix3f p0);
    
    Matrix3f cofactor3x3(final Matrix3f p0);
    
    Matrix4x3f cofactor3x3(final Matrix4x3f p0);
    
    Matrix4x3f normalize3x3(final Matrix4x3f p0);
    
    Matrix3f normalize3x3(final Matrix3f p0);
    
    Vector4f frustumPlane(final int p0, final Vector4f p1);
    
    Vector3f positiveZ(final Vector3f p0);
    
    Vector3f normalizedPositiveZ(final Vector3f p0);
    
    Vector3f positiveX(final Vector3f p0);
    
    Vector3f normalizedPositiveX(final Vector3f p0);
    
    Vector3f positiveY(final Vector3f p0);
    
    Vector3f normalizedPositiveY(final Vector3f p0);
    
    Vector3f origin(final Vector3f p0);
    
    Matrix4x3f shadow(final Vector4fc p0, final float p1, final float p2, final float p3, final float p4, final Matrix4x3f p5);
    
    Matrix4x3f shadow(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final Matrix4x3f p8);
    
    Matrix4x3f shadow(final Vector4fc p0, final Matrix4x3fc p1, final Matrix4x3f p2);
    
    Matrix4x3f shadow(final float p0, final float p1, final float p2, final float p3, final Matrix4x3fc p4, final Matrix4x3f p5);
    
    Matrix4x3f pick(final float p0, final float p1, final float p2, final float p3, final int[] p4, final Matrix4x3f p5);
    
    Matrix4x3f arcball(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Matrix4x3f arcball(final float p0, final Vector3fc p1, final float p2, final float p3, final Matrix4x3f p4);
    
    Matrix4x3f transformAab(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Vector3f p6, final Vector3f p7);
    
    Matrix4x3f transformAab(final Vector3fc p0, final Vector3fc p1, final Vector3f p2, final Vector3f p3);
    
    Matrix4x3f lerp(final Matrix4x3fc p0, final float p1, final Matrix4x3f p2);
    
    Matrix4x3f rotateTowards(final Vector3fc p0, final Vector3fc p1, final Matrix4x3f p2);
    
    Matrix4x3f rotateTowards(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4x3f p6);
    
    Vector3f getEulerAnglesXYZ(final Vector3f p0);
    
    Vector3f getEulerAnglesZYX(final Vector3f p0);
    
    Matrix4x3f obliqueZ(final float p0, final float p1, final Matrix4x3f p2);
    
    Matrix4x3f withLookAtUp(final Vector3fc p0, final Matrix4x3f p1);
    
    Matrix4x3f withLookAtUp(final float p0, final float p1, final float p2, final Matrix4x3f p3);
    
    Matrix4x3f mapXZY(final Matrix4x3f p0);
    
    Matrix4x3f mapXZnY(final Matrix4x3f p0);
    
    Matrix4x3f mapXnYnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapXnZY(final Matrix4x3f p0);
    
    Matrix4x3f mapXnZnY(final Matrix4x3f p0);
    
    Matrix4x3f mapYXZ(final Matrix4x3f p0);
    
    Matrix4x3f mapYXnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapYZX(final Matrix4x3f p0);
    
    Matrix4x3f mapYZnX(final Matrix4x3f p0);
    
    Matrix4x3f mapYnXZ(final Matrix4x3f p0);
    
    Matrix4x3f mapYnXnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapYnZX(final Matrix4x3f p0);
    
    Matrix4x3f mapYnZnX(final Matrix4x3f p0);
    
    Matrix4x3f mapZXY(final Matrix4x3f p0);
    
    Matrix4x3f mapZXnY(final Matrix4x3f p0);
    
    Matrix4x3f mapZYX(final Matrix4x3f p0);
    
    Matrix4x3f mapZYnX(final Matrix4x3f p0);
    
    Matrix4x3f mapZnXY(final Matrix4x3f p0);
    
    Matrix4x3f mapZnXnY(final Matrix4x3f p0);
    
    Matrix4x3f mapZnYX(final Matrix4x3f p0);
    
    Matrix4x3f mapZnYnX(final Matrix4x3f p0);
    
    Matrix4x3f mapnXYnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnXZY(final Matrix4x3f p0);
    
    Matrix4x3f mapnXZnY(final Matrix4x3f p0);
    
    Matrix4x3f mapnXnYZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnXnYnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnXnZY(final Matrix4x3f p0);
    
    Matrix4x3f mapnXnZnY(final Matrix4x3f p0);
    
    Matrix4x3f mapnYXZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnYXnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnYZX(final Matrix4x3f p0);
    
    Matrix4x3f mapnYZnX(final Matrix4x3f p0);
    
    Matrix4x3f mapnYnXZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnYnXnZ(final Matrix4x3f p0);
    
    Matrix4x3f mapnYnZX(final Matrix4x3f p0);
    
    Matrix4x3f mapnYnZnX(final Matrix4x3f p0);
    
    Matrix4x3f mapnZXY(final Matrix4x3f p0);
    
    Matrix4x3f mapnZXnY(final Matrix4x3f p0);
    
    Matrix4x3f mapnZYX(final Matrix4x3f p0);
    
    Matrix4x3f mapnZYnX(final Matrix4x3f p0);
    
    Matrix4x3f mapnZnXY(final Matrix4x3f p0);
    
    Matrix4x3f mapnZnXnY(final Matrix4x3f p0);
    
    Matrix4x3f mapnZnYX(final Matrix4x3f p0);
    
    Matrix4x3f mapnZnYnX(final Matrix4x3f p0);
    
    Matrix4x3f negateX(final Matrix4x3f p0);
    
    Matrix4x3f negateY(final Matrix4x3f p0);
    
    Matrix4x3f negateZ(final Matrix4x3f p0);
    
    boolean equals(final Matrix4x3fc p0, final float p1);
    
    boolean isFinite();
}
