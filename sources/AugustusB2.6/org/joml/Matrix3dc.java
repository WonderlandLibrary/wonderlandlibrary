// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;

public interface Matrix3dc
{
    double m00();
    
    double m01();
    
    double m02();
    
    double m10();
    
    double m11();
    
    double m12();
    
    double m20();
    
    double m21();
    
    double m22();
    
    Matrix3d mul(final Matrix3dc p0, final Matrix3d p1);
    
    Matrix3d mulLocal(final Matrix3dc p0, final Matrix3d p1);
    
    Matrix3d mul(final Matrix3fc p0, final Matrix3d p1);
    
    double determinant();
    
    Matrix3d invert(final Matrix3d p0);
    
    Matrix3d transpose(final Matrix3d p0);
    
    Matrix3d get(final Matrix3d p0);
    
    AxisAngle4f getRotation(final AxisAngle4f p0);
    
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
    
    Matrix3dc getToAddress(final long p0);
    
    double[] get(final double[] p0, final int p1);
    
    double[] get(final double[] p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    Matrix3d scale(final Vector3dc p0, final Matrix3d p1);
    
    Matrix3d scale(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d scale(final double p0, final Matrix3d p1);
    
    Matrix3d scaleLocal(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Vector3d transform(final Vector3d p0);
    
    Vector3d transform(final Vector3dc p0, final Vector3d p1);
    
    Vector3f transform(final Vector3f p0);
    
    Vector3f transform(final Vector3fc p0, final Vector3f p1);
    
    Vector3d transform(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d transformTranspose(final Vector3d p0);
    
    Vector3d transformTranspose(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformTranspose(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Matrix3d rotateX(final double p0, final Matrix3d p1);
    
    Matrix3d rotateY(final double p0, final Matrix3d p1);
    
    Matrix3d rotateZ(final double p0, final Matrix3d p1);
    
    Matrix3d rotateXYZ(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d rotateZYX(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d rotateYXZ(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d rotate(final double p0, final double p1, final double p2, final double p3, final Matrix3d p4);
    
    Matrix3d rotateLocal(final double p0, final double p1, final double p2, final double p3, final Matrix3d p4);
    
    Matrix3d rotateLocalX(final double p0, final Matrix3d p1);
    
    Matrix3d rotateLocalY(final double p0, final Matrix3d p1);
    
    Matrix3d rotateLocalZ(final double p0, final Matrix3d p1);
    
    Matrix3d rotateLocal(final Quaterniondc p0, final Matrix3d p1);
    
    Matrix3d rotateLocal(final Quaternionfc p0, final Matrix3d p1);
    
    Matrix3d rotate(final Quaterniondc p0, final Matrix3d p1);
    
    Matrix3d rotate(final Quaternionfc p0, final Matrix3d p1);
    
    Matrix3d rotate(final AxisAngle4f p0, final Matrix3d p1);
    
    Matrix3d rotate(final AxisAngle4d p0, final Matrix3d p1);
    
    Matrix3d rotate(final double p0, final Vector3dc p1, final Matrix3d p2);
    
    Matrix3d rotate(final double p0, final Vector3fc p1, final Matrix3d p2);
    
    Vector3d getRow(final int p0, final Vector3d p1) throws IndexOutOfBoundsException;
    
    Vector3d getColumn(final int p0, final Vector3d p1) throws IndexOutOfBoundsException;
    
    double get(final int p0, final int p1);
    
    double getRowColumn(final int p0, final int p1);
    
    Matrix3d normal(final Matrix3d p0);
    
    Matrix3d cofactor(final Matrix3d p0);
    
    Matrix3d lookAlong(final Vector3dc p0, final Vector3dc p1, final Matrix3d p2);
    
    Matrix3d lookAlong(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix3d p6);
    
    Vector3d getScale(final Vector3d p0);
    
    Vector3d positiveZ(final Vector3d p0);
    
    Vector3d normalizedPositiveZ(final Vector3d p0);
    
    Vector3d positiveX(final Vector3d p0);
    
    Vector3d normalizedPositiveX(final Vector3d p0);
    
    Vector3d positiveY(final Vector3d p0);
    
    Vector3d normalizedPositiveY(final Vector3d p0);
    
    Matrix3d add(final Matrix3dc p0, final Matrix3d p1);
    
    Matrix3d sub(final Matrix3dc p0, final Matrix3d p1);
    
    Matrix3d mulComponentWise(final Matrix3dc p0, final Matrix3d p1);
    
    Matrix3d lerp(final Matrix3dc p0, final double p1, final Matrix3d p2);
    
    Matrix3d rotateTowards(final Vector3dc p0, final Vector3dc p1, final Matrix3d p2);
    
    Matrix3d rotateTowards(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix3d p6);
    
    Vector3d getEulerAnglesXYZ(final Vector3d p0);
    
    Vector3d getEulerAnglesZYX(final Vector3d p0);
    
    Matrix3d obliqueZ(final double p0, final double p1, final Matrix3d p2);
    
    boolean equals(final Matrix3dc p0, final double p1);
    
    Matrix3d reflect(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d reflect(final Quaterniondc p0, final Matrix3d p1);
    
    Matrix3d reflect(final Vector3dc p0, final Matrix3d p1);
    
    boolean isFinite();
    
    double quadraticFormProduct(final double p0, final double p1, final double p2);
    
    double quadraticFormProduct(final Vector3dc p0);
    
    double quadraticFormProduct(final Vector3fc p0);
    
    Matrix3d mapXZY(final Matrix3d p0);
    
    Matrix3d mapXZnY(final Matrix3d p0);
    
    Matrix3d mapXnYnZ(final Matrix3d p0);
    
    Matrix3d mapXnZY(final Matrix3d p0);
    
    Matrix3d mapXnZnY(final Matrix3d p0);
    
    Matrix3d mapYXZ(final Matrix3d p0);
    
    Matrix3d mapYXnZ(final Matrix3d p0);
    
    Matrix3d mapYZX(final Matrix3d p0);
    
    Matrix3d mapYZnX(final Matrix3d p0);
    
    Matrix3d mapYnXZ(final Matrix3d p0);
    
    Matrix3d mapYnXnZ(final Matrix3d p0);
    
    Matrix3d mapYnZX(final Matrix3d p0);
    
    Matrix3d mapYnZnX(final Matrix3d p0);
    
    Matrix3d mapZXY(final Matrix3d p0);
    
    Matrix3d mapZXnY(final Matrix3d p0);
    
    Matrix3d mapZYX(final Matrix3d p0);
    
    Matrix3d mapZYnX(final Matrix3d p0);
    
    Matrix3d mapZnXY(final Matrix3d p0);
    
    Matrix3d mapZnXnY(final Matrix3d p0);
    
    Matrix3d mapZnYX(final Matrix3d p0);
    
    Matrix3d mapZnYnX(final Matrix3d p0);
    
    Matrix3d mapnXYnZ(final Matrix3d p0);
    
    Matrix3d mapnXZY(final Matrix3d p0);
    
    Matrix3d mapnXZnY(final Matrix3d p0);
    
    Matrix3d mapnXnYZ(final Matrix3d p0);
    
    Matrix3d mapnXnYnZ(final Matrix3d p0);
    
    Matrix3d mapnXnZY(final Matrix3d p0);
    
    Matrix3d mapnXnZnY(final Matrix3d p0);
    
    Matrix3d mapnYXZ(final Matrix3d p0);
    
    Matrix3d mapnYXnZ(final Matrix3d p0);
    
    Matrix3d mapnYZX(final Matrix3d p0);
    
    Matrix3d mapnYZnX(final Matrix3d p0);
    
    Matrix3d mapnYnXZ(final Matrix3d p0);
    
    Matrix3d mapnYnXnZ(final Matrix3d p0);
    
    Matrix3d mapnYnZX(final Matrix3d p0);
    
    Matrix3d mapnYnZnX(final Matrix3d p0);
    
    Matrix3d mapnZXY(final Matrix3d p0);
    
    Matrix3d mapnZXnY(final Matrix3d p0);
    
    Matrix3d mapnZYX(final Matrix3d p0);
    
    Matrix3d mapnZYnX(final Matrix3d p0);
    
    Matrix3d mapnZnXY(final Matrix3d p0);
    
    Matrix3d mapnZnXnY(final Matrix3d p0);
    
    Matrix3d mapnZnYX(final Matrix3d p0);
    
    Matrix3d mapnZnYnX(final Matrix3d p0);
    
    Matrix3d negateX(final Matrix3d p0);
    
    Matrix3d negateY(final Matrix3d p0);
    
    Matrix3d negateZ(final Matrix3d p0);
}
