// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public interface Vector3dc
{
    double x();
    
    double y();
    
    double z();
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer getf(final ByteBuffer p0);
    
    ByteBuffer getf(final int p0, final ByteBuffer p1);
    
    Vector3dc getToAddress(final long p0);
    
    Vector3d sub(final Vector3dc p0, final Vector3d p1);
    
    Vector3d sub(final Vector3fc p0, final Vector3d p1);
    
    Vector3d sub(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d add(final Vector3dc p0, final Vector3d p1);
    
    Vector3d add(final Vector3fc p0, final Vector3d p1);
    
    Vector3d add(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d fma(final Vector3dc p0, final Vector3dc p1, final Vector3d p2);
    
    Vector3d fma(final double p0, final Vector3dc p1, final Vector3d p2);
    
    Vector3d fma(final Vector3dc p0, final Vector3fc p1, final Vector3d p2);
    
    Vector3d fma(final Vector3fc p0, final Vector3fc p1, final Vector3d p2);
    
    Vector3d fma(final double p0, final Vector3fc p1, final Vector3d p2);
    
    Vector3d mulAdd(final Vector3dc p0, final Vector3dc p1, final Vector3d p2);
    
    Vector3d mulAdd(final double p0, final Vector3dc p1, final Vector3d p2);
    
    Vector3d mulAdd(final Vector3fc p0, final Vector3dc p1, final Vector3d p2);
    
    Vector3d mul(final Vector3fc p0, final Vector3d p1);
    
    Vector3d mul(final Vector3dc p0, final Vector3d p1);
    
    Vector3d div(final Vector3fc p0, final Vector3d p1);
    
    Vector3d div(final Vector3dc p0, final Vector3d p1);
    
    Vector3d mulProject(final Matrix4dc p0, final double p1, final Vector3d p2);
    
    Vector3d mulProject(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulProject(final Matrix4fc p0, final Vector3d p1);
    
    Vector3d mul(final Matrix3dc p0, final Vector3d p1);
    
    Vector3f mul(final Matrix3dc p0, final Vector3f p1);
    
    Vector3d mul(final Matrix3fc p0, final Vector3d p1);
    
    Vector3d mul(final Matrix3x2dc p0, final Vector3d p1);
    
    Vector3d mul(final Matrix3x2fc p0, final Vector3d p1);
    
    Vector3d mulTranspose(final Matrix3dc p0, final Vector3d p1);
    
    Vector3d mulTranspose(final Matrix3fc p0, final Vector3d p1);
    
    Vector3d mulPosition(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulPosition(final Matrix4fc p0, final Vector3d p1);
    
    Vector3d mulPosition(final Matrix4x3dc p0, final Vector3d p1);
    
    Vector3d mulPosition(final Matrix4x3fc p0, final Vector3d p1);
    
    Vector3d mulTransposePosition(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulTransposePosition(final Matrix4fc p0, final Vector3d p1);
    
    double mulPositionW(final Matrix4fc p0, final Vector3d p1);
    
    double mulPositionW(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulDirection(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulDirection(final Matrix4fc p0, final Vector3d p1);
    
    Vector3d mulDirection(final Matrix4x3dc p0, final Vector3d p1);
    
    Vector3d mulDirection(final Matrix4x3fc p0, final Vector3d p1);
    
    Vector3d mulTransposeDirection(final Matrix4dc p0, final Vector3d p1);
    
    Vector3d mulTransposeDirection(final Matrix4fc p0, final Vector3d p1);
    
    Vector3d mul(final double p0, final Vector3d p1);
    
    Vector3d mul(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d rotate(final Quaterniondc p0, final Vector3d p1);
    
    Quaterniond rotationTo(final Vector3dc p0, final Quaterniond p1);
    
    Quaterniond rotationTo(final double p0, final double p1, final double p2, final Quaterniond p3);
    
    Vector3d rotateAxis(final double p0, final double p1, final double p2, final double p3, final Vector3d p4);
    
    Vector3d rotateX(final double p0, final Vector3d p1);
    
    Vector3d rotateY(final double p0, final Vector3d p1);
    
    Vector3d rotateZ(final double p0, final Vector3d p1);
    
    Vector3d div(final double p0, final Vector3d p1);
    
    Vector3d div(final double p0, final double p1, final double p2, final Vector3d p3);
    
    double lengthSquared();
    
    double length();
    
    Vector3d normalize(final Vector3d p0);
    
    Vector3d normalize(final double p0, final Vector3d p1);
    
    Vector3d cross(final Vector3dc p0, final Vector3d p1);
    
    Vector3d cross(final double p0, final double p1, final double p2, final Vector3d p3);
    
    double distance(final Vector3dc p0);
    
    double distance(final double p0, final double p1, final double p2);
    
    double distanceSquared(final Vector3dc p0);
    
    double distanceSquared(final double p0, final double p1, final double p2);
    
    double dot(final Vector3dc p0);
    
    double dot(final double p0, final double p1, final double p2);
    
    double angleCos(final Vector3dc p0);
    
    double angle(final Vector3dc p0);
    
    double angleSigned(final Vector3dc p0, final Vector3dc p1);
    
    double angleSigned(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5);
    
    Vector3d min(final Vector3dc p0, final Vector3d p1);
    
    Vector3d max(final Vector3dc p0, final Vector3d p1);
    
    Vector3d negate(final Vector3d p0);
    
    Vector3d absolute(final Vector3d p0);
    
    Vector3d reflect(final Vector3dc p0, final Vector3d p1);
    
    Vector3d reflect(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d half(final Vector3dc p0, final Vector3d p1);
    
    Vector3d half(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d smoothStep(final Vector3dc p0, final double p1, final Vector3d p2);
    
    Vector3d hermite(final Vector3dc p0, final Vector3dc p1, final Vector3dc p2, final double p3, final Vector3d p4);
    
    Vector3d lerp(final Vector3dc p0, final double p1, final Vector3d p2);
    
    double get(final int p0) throws IllegalArgumentException;
    
    Vector3i get(final int p0, final Vector3i p1);
    
    Vector3f get(final Vector3f p0);
    
    Vector3d get(final Vector3d p0);
    
    int maxComponent();
    
    int minComponent();
    
    Vector3d orthogonalize(final Vector3dc p0, final Vector3d p1);
    
    Vector3d orthogonalizeUnit(final Vector3dc p0, final Vector3d p1);
    
    Vector3d floor(final Vector3d p0);
    
    Vector3d ceil(final Vector3d p0);
    
    Vector3d round(final Vector3d p0);
    
    boolean isFinite();
    
    boolean equals(final Vector3dc p0, final double p1);
    
    boolean equals(final double p0, final double p1, final double p2);
}
