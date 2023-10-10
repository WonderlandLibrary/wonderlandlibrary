// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public interface Matrix3x2dc
{
    double m00();
    
    double m01();
    
    double m10();
    
    double m11();
    
    double m20();
    
    double m21();
    
    Matrix3x2d mul(final Matrix3x2dc p0, final Matrix3x2d p1);
    
    Matrix3x2d mulLocal(final Matrix3x2dc p0, final Matrix3x2d p1);
    
    double determinant();
    
    Matrix3x2d invert(final Matrix3x2d p0);
    
    Matrix3x2d translate(final double p0, final double p1, final Matrix3x2d p2);
    
    Matrix3x2d translate(final Vector2dc p0, final Matrix3x2d p1);
    
    Matrix3x2d translateLocal(final Vector2dc p0, final Matrix3x2d p1);
    
    Matrix3x2d translateLocal(final double p0, final double p1, final Matrix3x2d p2);
    
    Matrix3x2d get(final Matrix3x2d p0);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get3x3(final DoubleBuffer p0);
    
    DoubleBuffer get3x3(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get3x3(final ByteBuffer p0);
    
    ByteBuffer get3x3(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get4x4(final DoubleBuffer p0);
    
    DoubleBuffer get4x4(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get4x4(final ByteBuffer p0);
    
    ByteBuffer get4x4(final int p0, final ByteBuffer p1);
    
    Matrix3x2dc getToAddress(final long p0);
    
    double[] get(final double[] p0, final int p1);
    
    double[] get(final double[] p0);
    
    double[] get3x3(final double[] p0, final int p1);
    
    double[] get3x3(final double[] p0);
    
    double[] get4x4(final double[] p0, final int p1);
    
    double[] get4x4(final double[] p0);
    
    Matrix3x2d scale(final double p0, final double p1, final Matrix3x2d p2);
    
    Matrix3x2d scale(final Vector2dc p0, final Matrix3x2d p1);
    
    Matrix3x2d scale(final Vector2fc p0, final Matrix3x2d p1);
    
    Matrix3x2d scaleLocal(final double p0, final Matrix3x2d p1);
    
    Matrix3x2d scaleLocal(final double p0, final double p1, final Matrix3x2d p2);
    
    Matrix3x2d scaleAroundLocal(final double p0, final double p1, final double p2, final double p3, final Matrix3x2d p4);
    
    Matrix3x2d scaleAroundLocal(final double p0, final double p1, final double p2, final Matrix3x2d p3);
    
    Matrix3x2d scale(final double p0, final Matrix3x2d p1);
    
    Matrix3x2d scaleAround(final double p0, final double p1, final double p2, final double p3, final Matrix3x2d p4);
    
    Matrix3x2d scaleAround(final double p0, final double p1, final double p2, final Matrix3x2d p3);
    
    Vector3d transform(final Vector3d p0);
    
    Vector3d transform(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transform(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector2d transformPosition(final Vector2d p0);
    
    Vector2d transformPosition(final Vector2dc p0, final Vector2d p1);
    
    Vector2d transformPosition(final double p0, final double p1, final Vector2d p2);
    
    Vector2d transformDirection(final Vector2d p0);
    
    Vector2d transformDirection(final Vector2dc p0, final Vector2d p1);
    
    Vector2d transformDirection(final double p0, final double p1, final Vector2d p2);
    
    Matrix3x2d rotate(final double p0, final Matrix3x2d p1);
    
    Matrix3x2d rotateLocal(final double p0, final Matrix3x2d p1);
    
    Matrix3x2d rotateAbout(final double p0, final double p1, final double p2, final Matrix3x2d p3);
    
    Matrix3x2d rotateTo(final Vector2dc p0, final Vector2dc p1, final Matrix3x2d p2);
    
    Matrix3x2d view(final double p0, final double p1, final double p2, final double p3, final Matrix3x2d p4);
    
    Vector2d origin(final Vector2d p0);
    
    double[] viewArea(final double[] p0);
    
    Vector2d positiveX(final Vector2d p0);
    
    Vector2d normalizedPositiveX(final Vector2d p0);
    
    Vector2d positiveY(final Vector2d p0);
    
    Vector2d normalizedPositiveY(final Vector2d p0);
    
    Vector2d unproject(final double p0, final double p1, final int[] p2, final Vector2d p3);
    
    Vector2d unprojectInv(final double p0, final double p1, final int[] p2, final Vector2d p3);
    
    boolean testPoint(final double p0, final double p1);
    
    boolean testCircle(final double p0, final double p1, final double p2);
    
    boolean testAar(final double p0, final double p1, final double p2, final double p3);
    
    boolean equals(final Matrix3x2dc p0, final double p1);
    
    boolean isFinite();
}
