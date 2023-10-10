// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public interface Vector4dc
{
    double x();
    
    double y();
    
    double z();
    
    double w();
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer getf(final ByteBuffer p0);
    
    ByteBuffer getf(final int p0, final ByteBuffer p1);
    
    Vector4dc getToAddress(final long p0);
    
    Vector4d sub(final Vector4dc p0, final Vector4d p1);
    
    Vector4d sub(final Vector4fc p0, final Vector4d p1);
    
    Vector4d sub(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector4d add(final Vector4dc p0, final Vector4d p1);
    
    Vector4d add(final Vector4fc p0, final Vector4d p1);
    
    Vector4d add(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector4d fma(final Vector4dc p0, final Vector4dc p1, final Vector4d p2);
    
    Vector4d fma(final double p0, final Vector4dc p1, final Vector4d p2);
    
    Vector4d mul(final Vector4dc p0, final Vector4d p1);
    
    Vector4d mul(final Vector4fc p0, final Vector4d p1);
    
    Vector4d div(final Vector4dc p0, final Vector4d p1);
    
    Vector4d mul(final Matrix4dc p0, final Vector4d p1);
    
    Vector4d mul(final Matrix4x3dc p0, final Vector4d p1);
    
    Vector4d mul(final Matrix4x3fc p0, final Vector4d p1);
    
    Vector4d mul(final Matrix4fc p0, final Vector4d p1);
    
    Vector4d mulTranspose(final Matrix4dc p0, final Vector4d p1);
    
    Vector4d mulAffine(final Matrix4dc p0, final Vector4d p1);
    
    Vector4d mulAffineTranspose(final Matrix4dc p0, final Vector4d p1);
    
    Vector4d mulProject(final Matrix4dc p0, final Vector4d p1);
    
    Vector3d mulProject(final Matrix4dc p0, final Vector3d p1);
    
    Vector4d mulAdd(final Vector4dc p0, final Vector4dc p1, final Vector4d p2);
    
    Vector4d mulAdd(final double p0, final Vector4dc p1, final Vector4d p2);
    
    Vector4d mul(final double p0, final Vector4d p1);
    
    Vector4d div(final double p0, final Vector4d p1);
    
    Vector4d rotate(final Quaterniondc p0, final Vector4d p1);
    
    Vector4d rotateAxis(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector4d rotateX(final double p0, final Vector4d p1);
    
    Vector4d rotateY(final double p0, final Vector4d p1);
    
    Vector4d rotateZ(final double p0, final Vector4d p1);
    
    double lengthSquared();
    
    double length();
    
    Vector4d normalize(final Vector4d p0);
    
    Vector4d normalize(final double p0, final Vector4d p1);
    
    Vector4d normalize3(final Vector4d p0);
    
    double distance(final Vector4dc p0);
    
    double distance(final double p0, final double p1, final double p2, final double p3);
    
    double distanceSquared(final Vector4dc p0);
    
    double distanceSquared(final double p0, final double p1, final double p2, final double p3);
    
    double dot(final Vector4dc p0);
    
    double dot(final double p0, final double p1, final double p2, final double p3);
    
    double angleCos(final Vector4dc p0);
    
    double angle(final Vector4dc p0);
    
    Vector4d negate(final Vector4d p0);
    
    Vector4d min(final Vector4dc p0, final Vector4d p1);
    
    Vector4d max(final Vector4dc p0, final Vector4d p1);
    
    Vector4d smoothStep(final Vector4dc p0, final double p1, final Vector4d p2);
    
    Vector4d hermite(final Vector4dc p0, final Vector4dc p1, final Vector4dc p2, final double p3, final Vector4d p4);
    
    Vector4d lerp(final Vector4dc p0, final double p1, final Vector4d p2);
    
    double get(final int p0) throws IllegalArgumentException;
    
    Vector4i get(final int p0, final Vector4i p1);
    
    Vector4f get(final Vector4f p0);
    
    Vector4d get(final Vector4d p0);
    
    int maxComponent();
    
    int minComponent();
    
    Vector4d floor(final Vector4d p0);
    
    Vector4d ceil(final Vector4d p0);
    
    Vector4d round(final Vector4d p0);
    
    boolean isFinite();
    
    Vector4d absolute(final Vector4d p0);
    
    boolean equals(final Vector4dc p0, final double p1);
    
    boolean equals(final double p0, final double p1, final double p2, final double p3);
}
