// 
// Decompiled by Procyon v0.6.0
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
    
    Vector3d getEulerAnglesZYX(final Vector3d p0);
    
    Matrix3d obliqueZ(final double p0, final double p1, final Matrix3d p2);
    
    boolean equals(final Matrix3dc p0, final double p1);
    
    Matrix3d reflect(final double p0, final double p1, final double p2, final Matrix3d p3);
    
    Matrix3d reflect(final Quaterniondc p0, final Matrix3d p1);
    
    Matrix3d reflect(final Vector3dc p0, final Matrix3d p1);
    
    boolean isFinite();
}
