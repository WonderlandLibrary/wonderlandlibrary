// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;

public interface Vector2dc
{
    double x();
    
    double y();
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    Vector2dc getToAddress(final long p0);
    
    Vector2d sub(final double p0, final double p1, final Vector2d p2);
    
    Vector2d sub(final Vector2dc p0, final Vector2d p1);
    
    Vector2d sub(final Vector2fc p0, final Vector2d p1);
    
    Vector2d mul(final double p0, final Vector2d p1);
    
    Vector2d mul(final double p0, final double p1, final Vector2d p2);
    
    Vector2d mul(final Vector2dc p0, final Vector2d p1);
    
    Vector2d div(final double p0, final Vector2d p1);
    
    Vector2d div(final double p0, final double p1, final Vector2d p2);
    
    Vector2d div(final Vector2fc p0, final Vector2d p1);
    
    Vector2d div(final Vector2dc p0, final Vector2d p1);
    
    Vector2d mul(final Matrix2dc p0, final Vector2d p1);
    
    Vector2d mul(final Matrix2fc p0, final Vector2d p1);
    
    Vector2d mulTranspose(final Matrix2dc p0, final Vector2d p1);
    
    Vector2d mulTranspose(final Matrix2fc p0, final Vector2d p1);
    
    Vector2d mulPosition(final Matrix3x2dc p0, final Vector2d p1);
    
    Vector2d mulDirection(final Matrix3x2dc p0, final Vector2d p1);
    
    double dot(final Vector2dc p0);
    
    double angle(final Vector2dc p0);
    
    double lengthSquared();
    
    double length();
    
    double distance(final Vector2dc p0);
    
    double distanceSquared(final Vector2dc p0);
    
    double distance(final Vector2fc p0);
    
    double distanceSquared(final Vector2fc p0);
    
    double distance(final double p0, final double p1);
    
    double distanceSquared(final double p0, final double p1);
    
    Vector2d normalize(final Vector2d p0);
    
    Vector2d normalize(final double p0, final Vector2d p1);
    
    Vector2d add(final double p0, final double p1, final Vector2d p2);
    
    Vector2d add(final Vector2dc p0, final Vector2d p1);
    
    Vector2d add(final Vector2fc p0, final Vector2d p1);
    
    Vector2d negate(final Vector2d p0);
    
    Vector2d lerp(final Vector2dc p0, final double p1, final Vector2d p2);
    
    Vector2d fma(final Vector2dc p0, final Vector2dc p1, final Vector2d p2);
    
    Vector2d fma(final double p0, final Vector2dc p1, final Vector2d p2);
    
    Vector2d min(final Vector2dc p0, final Vector2d p1);
    
    Vector2d max(final Vector2dc p0, final Vector2d p1);
    
    int maxComponent();
    
    int minComponent();
    
    double get(final int p0) throws IllegalArgumentException;
    
    Vector2i get(final int p0, final Vector2i p1);
    
    Vector2f get(final Vector2f p0);
    
    Vector2d get(final Vector2d p0);
    
    Vector2d floor(final Vector2d p0);
    
    Vector2d ceil(final Vector2d p0);
    
    Vector2d round(final Vector2d p0);
    
    boolean isFinite();
    
    Vector2d absolute(final Vector2d p0);
    
    boolean equals(final Vector2dc p0, final double p1);
    
    boolean equals(final double p0, final double p1);
}
