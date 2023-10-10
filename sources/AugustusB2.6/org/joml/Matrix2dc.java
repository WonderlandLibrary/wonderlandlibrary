// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public interface Matrix2dc
{
    double m00();
    
    double m01();
    
    double m10();
    
    double m11();
    
    Matrix2d mul(final Matrix2dc p0, final Matrix2d p1);
    
    Matrix2d mul(final Matrix2fc p0, final Matrix2d p1);
    
    Matrix2d mulLocal(final Matrix2dc p0, final Matrix2d p1);
    
    double determinant();
    
    Matrix2d invert(final Matrix2d p0);
    
    Matrix2d transpose(final Matrix2d p0);
    
    Matrix2d get(final Matrix2d p0);
    
    Matrix3x2d get(final Matrix3x2d p0);
    
    Matrix3d get(final Matrix3d p0);
    
    double getRotation();
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    DoubleBuffer getTransposed(final DoubleBuffer p0);
    
    DoubleBuffer getTransposed(final int p0, final DoubleBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    Matrix2dc getToAddress(final long p0);
    
    double[] get(final double[] p0, final int p1);
    
    double[] get(final double[] p0);
    
    Matrix2d scale(final Vector2dc p0, final Matrix2d p1);
    
    Matrix2d scale(final double p0, final double p1, final Matrix2d p2);
    
    Matrix2d scale(final double p0, final Matrix2d p1);
    
    Matrix2d scaleLocal(final double p0, final double p1, final Matrix2d p2);
    
    Vector2d transform(final Vector2d p0);
    
    Vector2d transform(final Vector2dc p0, final Vector2d p1);
    
    Vector2d transform(final double p0, final double p1, final Vector2d p2);
    
    Vector2d transformTranspose(final Vector2d p0);
    
    Vector2d transformTranspose(final Vector2dc p0, final Vector2d p1);
    
    Vector2d transformTranspose(final double p0, final double p1, final Vector2d p2);
    
    Matrix2d rotate(final double p0, final Matrix2d p1);
    
    Matrix2d rotateLocal(final double p0, final Matrix2d p1);
    
    Vector2d getRow(final int p0, final Vector2d p1) throws IndexOutOfBoundsException;
    
    Vector2d getColumn(final int p0, final Vector2d p1) throws IndexOutOfBoundsException;
    
    double get(final int p0, final int p1);
    
    Matrix2d normal(final Matrix2d p0);
    
    Vector2d getScale(final Vector2d p0);
    
    Vector2d positiveX(final Vector2d p0);
    
    Vector2d normalizedPositiveX(final Vector2d p0);
    
    Vector2d positiveY(final Vector2d p0);
    
    Vector2d normalizedPositiveY(final Vector2d p0);
    
    Matrix2d add(final Matrix2dc p0, final Matrix2d p1);
    
    Matrix2d sub(final Matrix2dc p0, final Matrix2d p1);
    
    Matrix2d mulComponentWise(final Matrix2dc p0, final Matrix2d p1);
    
    Matrix2d lerp(final Matrix2dc p0, final double p1, final Matrix2d p2);
    
    boolean equals(final Matrix2dc p0, final double p1);
    
    boolean isFinite();
}
