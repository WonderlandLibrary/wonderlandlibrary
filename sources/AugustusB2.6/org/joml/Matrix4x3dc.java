// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;

public interface Matrix4x3dc
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
    
    double m00();
    
    double m01();
    
    double m02();
    
    double m10();
    
    double m11();
    
    double m12();
    
    double m20();
    
    double m21();
    
    double m22();
    
    double m30();
    
    double m31();
    
    double m32();
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix4x3d mul(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d mul(final Matrix4x3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d mulTranslation(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d mulTranslation(final Matrix4x3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d mulOrtho(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d mul3x3(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4x3d p9);
    
    Matrix4x3d fma(final Matrix4x3dc p0, final double p1, final Matrix4x3d p2);
    
    Matrix4x3d fma(final Matrix4x3fc p0, final double p1, final Matrix4x3d p2);
    
    Matrix4x3d add(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d add(final Matrix4x3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d sub(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d sub(final Matrix4x3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d mulComponentWise(final Matrix4x3dc p0, final Matrix4x3d p1);
    
    double determinant();
    
    Matrix4x3d invert(final Matrix4x3d p0);
    
    Matrix4x3d invertOrtho(final Matrix4x3d p0);
    
    Matrix4x3d transpose3x3(final Matrix4x3d p0);
    
    Matrix3d transpose3x3(final Matrix3d p0);
    
    Vector3d getTranslation(final Vector3d p0);
    
    Vector3d getScale(final Vector3d p0);
    
    Matrix4x3d get(final Matrix4x3d p0);
    
    Quaternionf getUnnormalizedRotation(final Quaternionf p0);
    
    Quaternionf getNormalizedRotation(final Quaternionf p0);
    
    Quaterniond getUnnormalizedRotation(final Quaterniond p0);
    
    Quaterniond getNormalizedRotation(final Quaterniond p0);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    ByteBuffer getFloats(final ByteBuffer p0);
    
    ByteBuffer getFloats(final int p0, final ByteBuffer p1);
    
    Matrix4x3dc getToAddress(final long p0);
    
    double[] get(final double[] p0, final int p1);
    
    double[] get(final double[] p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    double[] get4x4(final double[] p0, final int p1);
    
    double[] get4x4(final double[] p0);
    
    float[] get4x4(final float[] p0, final int p1);
    
    float[] get4x4(final float[] p0);
    
    DoubleBuffer get4x4(final DoubleBuffer p0);
    
    DoubleBuffer get4x4(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get4x4(final ByteBuffer p0);
    
    ByteBuffer get4x4(final int p0, final ByteBuffer p1);
    
    DoubleBuffer getTransposed(final DoubleBuffer p0);
    
    DoubleBuffer getTransposed(final int p0, final DoubleBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    FloatBuffer getTransposed(final FloatBuffer p0);
    
    FloatBuffer getTransposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer getTransposedFloats(final ByteBuffer p0);
    
    ByteBuffer getTransposedFloats(final int p0, final ByteBuffer p1);
    
    double[] getTransposed(final double[] p0, final int p1);
    
    double[] getTransposed(final double[] p0);
    
    Vector4d transform(final Vector4d p0);
    
    Vector4d transform(final Vector4dc p0, final Vector4d p1);
    
    Vector3d transformPosition(final Vector3d p0);
    
    Vector3d transformPosition(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformDirection(final Vector3d p0);
    
    Vector3d transformDirection(final Vector3dc p0, final Vector3d p1);
    
    Matrix4x3d scale(final Vector3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d scale(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d scale(final double p0, final Matrix4x3d p1);
    
    Matrix4x3d scaleXY(final double p0, final double p1, final Matrix4x3d p2);
    
    Matrix4x3d scaleAround(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d scaleAround(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d scaleLocal(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d rotate(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d rotateTranslation(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d rotateAround(final Quaterniondc p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d rotateLocal(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d translate(final Vector3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d translate(final Vector3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d translate(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d translateLocal(final Vector3fc p0, final Matrix4x3d p1);
    
    Matrix4x3d translateLocal(final Vector3dc p0, final Matrix4x3d p1);
    
    Matrix4x3d translateLocal(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d rotateX(final double p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateY(final double p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateZ(final double p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateXYZ(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d rotateZYX(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d rotateYXZ(final double p0, final double p1, final double p2, final Matrix4x3d p3);
    
    Matrix4x3d rotate(final Quaterniondc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotate(final Quaternionfc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateTranslation(final Quaterniondc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateTranslation(final Quaternionfc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateLocal(final Quaterniondc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotateLocal(final Quaternionfc p0, final Matrix4x3d p1);
    
    Matrix4x3d rotate(final AxisAngle4f p0, final Matrix4x3d p1);
    
    Matrix4x3d rotate(final AxisAngle4d p0, final Matrix4x3d p1);
    
    Matrix4x3d rotate(final double p0, final Vector3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d rotate(final double p0, final Vector3fc p1, final Matrix4x3d p2);
    
    Vector4d getRow(final int p0, final Vector4d p1) throws IndexOutOfBoundsException;
    
    Vector3d getColumn(final int p0, final Vector3d p1) throws IndexOutOfBoundsException;
    
    Matrix4x3d normal(final Matrix4x3d p0);
    
    Matrix3d normal(final Matrix3d p0);
    
    Matrix3d cofactor3x3(final Matrix3d p0);
    
    Matrix4x3d cofactor3x3(final Matrix4x3d p0);
    
    Matrix4x3d normalize3x3(final Matrix4x3d p0);
    
    Matrix3d normalize3x3(final Matrix3d p0);
    
    Matrix4x3d reflect(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d reflect(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d reflect(final Quaterniondc p0, final Vector3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d reflect(final Vector3dc p0, final Vector3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d ortho(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4x3d p7);
    
    Matrix4x3d ortho(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d orthoLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4x3d p7);
    
    Matrix4x3d orthoLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d orthoSymmetric(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4x3d p5);
    
    Matrix4x3d orthoSymmetric(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d orthoSymmetricLH(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4x3d p5);
    
    Matrix4x3d orthoSymmetricLH(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d ortho2D(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d ortho2DLH(final double p0, final double p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d lookAlong(final Vector3dc p0, final Vector3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d lookAlong(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d lookAt(final Vector3dc p0, final Vector3dc p1, final Vector3dc p2, final Matrix4x3d p3);
    
    Matrix4x3d lookAt(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4x3d p9);
    
    Matrix4x3d lookAtLH(final Vector3dc p0, final Vector3dc p1, final Vector3dc p2, final Matrix4x3d p3);
    
    Matrix4x3d lookAtLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4x3d p9);
    
    Vector4d frustumPlane(final int p0, final Vector4d p1);
    
    Vector3d positiveZ(final Vector3d p0);
    
    Vector3d normalizedPositiveZ(final Vector3d p0);
    
    Vector3d positiveX(final Vector3d p0);
    
    Vector3d normalizedPositiveX(final Vector3d p0);
    
    Vector3d positiveY(final Vector3d p0);
    
    Vector3d normalizedPositiveY(final Vector3d p0);
    
    Vector3d origin(final Vector3d p0);
    
    Matrix4x3d shadow(final Vector4dc p0, final double p1, final double p2, final double p3, final double p4, final Matrix4x3d p5);
    
    Matrix4x3d shadow(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final Matrix4x3d p8);
    
    Matrix4x3d shadow(final Vector4dc p0, final Matrix4x3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d shadow(final double p0, final double p1, final double p2, final double p3, final Matrix4x3dc p4, final Matrix4x3d p5);
    
    Matrix4x3d pick(final double p0, final double p1, final double p2, final double p3, final int[] p4, final Matrix4x3d p5);
    
    Matrix4x3d arcball(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Matrix4x3d arcball(final double p0, final Vector3dc p1, final double p2, final double p3, final Matrix4x3d p4);
    
    Matrix4x3d transformAab(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Vector3d p6, final Vector3d p7);
    
    Matrix4x3d transformAab(final Vector3dc p0, final Vector3dc p1, final Vector3d p2, final Vector3d p3);
    
    Matrix4x3d lerp(final Matrix4x3dc p0, final double p1, final Matrix4x3d p2);
    
    Matrix4x3d rotateTowards(final Vector3dc p0, final Vector3dc p1, final Matrix4x3d p2);
    
    Matrix4x3d rotateTowards(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4x3d p6);
    
    Vector3d getEulerAnglesXYZ(final Vector3d p0);
    
    Vector3d getEulerAnglesZYX(final Vector3d p0);
    
    Matrix4x3d obliqueZ(final double p0, final double p1, final Matrix4x3d p2);
    
    Matrix4x3d mapXZY(final Matrix4x3d p0);
    
    Matrix4x3d mapXZnY(final Matrix4x3d p0);
    
    Matrix4x3d mapXnYnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapXnZY(final Matrix4x3d p0);
    
    Matrix4x3d mapXnZnY(final Matrix4x3d p0);
    
    Matrix4x3d mapYXZ(final Matrix4x3d p0);
    
    Matrix4x3d mapYXnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapYZX(final Matrix4x3d p0);
    
    Matrix4x3d mapYZnX(final Matrix4x3d p0);
    
    Matrix4x3d mapYnXZ(final Matrix4x3d p0);
    
    Matrix4x3d mapYnXnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapYnZX(final Matrix4x3d p0);
    
    Matrix4x3d mapYnZnX(final Matrix4x3d p0);
    
    Matrix4x3d mapZXY(final Matrix4x3d p0);
    
    Matrix4x3d mapZXnY(final Matrix4x3d p0);
    
    Matrix4x3d mapZYX(final Matrix4x3d p0);
    
    Matrix4x3d mapZYnX(final Matrix4x3d p0);
    
    Matrix4x3d mapZnXY(final Matrix4x3d p0);
    
    Matrix4x3d mapZnXnY(final Matrix4x3d p0);
    
    Matrix4x3d mapZnYX(final Matrix4x3d p0);
    
    Matrix4x3d mapZnYnX(final Matrix4x3d p0);
    
    Matrix4x3d mapnXYnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnXZY(final Matrix4x3d p0);
    
    Matrix4x3d mapnXZnY(final Matrix4x3d p0);
    
    Matrix4x3d mapnXnYZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnXnYnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnXnZY(final Matrix4x3d p0);
    
    Matrix4x3d mapnXnZnY(final Matrix4x3d p0);
    
    Matrix4x3d mapnYXZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnYXnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnYZX(final Matrix4x3d p0);
    
    Matrix4x3d mapnYZnX(final Matrix4x3d p0);
    
    Matrix4x3d mapnYnXZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnYnXnZ(final Matrix4x3d p0);
    
    Matrix4x3d mapnYnZX(final Matrix4x3d p0);
    
    Matrix4x3d mapnYnZnX(final Matrix4x3d p0);
    
    Matrix4x3d mapnZXY(final Matrix4x3d p0);
    
    Matrix4x3d mapnZXnY(final Matrix4x3d p0);
    
    Matrix4x3d mapnZYX(final Matrix4x3d p0);
    
    Matrix4x3d mapnZYnX(final Matrix4x3d p0);
    
    Matrix4x3d mapnZnXY(final Matrix4x3d p0);
    
    Matrix4x3d mapnZnXnY(final Matrix4x3d p0);
    
    Matrix4x3d mapnZnYX(final Matrix4x3d p0);
    
    Matrix4x3d mapnZnYnX(final Matrix4x3d p0);
    
    Matrix4x3d negateX(final Matrix4x3d p0);
    
    Matrix4x3d negateY(final Matrix4x3d p0);
    
    Matrix4x3d negateZ(final Matrix4x3d p0);
    
    boolean equals(final Matrix4x3dc p0, final double p1);
    
    boolean isFinite();
}
