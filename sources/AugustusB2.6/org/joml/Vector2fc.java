// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;

public interface Vector2fc
{
    float x();
    
    float y();
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    Vector2fc getToAddress(final long p0);
    
    Vector2f sub(final Vector2fc p0, final Vector2f p1);
    
    Vector2f sub(final float p0, final float p1, final Vector2f p2);
    
    float dot(final Vector2fc p0);
    
    float angle(final Vector2fc p0);
    
    float lengthSquared();
    
    float length();
    
    float distance(final Vector2fc p0);
    
    float distanceSquared(final Vector2fc p0);
    
    float distance(final float p0, final float p1);
    
    float distanceSquared(final float p0, final float p1);
    
    Vector2f normalize(final Vector2f p0);
    
    Vector2f normalize(final float p0, final Vector2f p1);
    
    Vector2f add(final Vector2fc p0, final Vector2f p1);
    
    Vector2f add(final float p0, final float p1, final Vector2f p2);
    
    Vector2f negate(final Vector2f p0);
    
    Vector2f mul(final float p0, final Vector2f p1);
    
    Vector2f mul(final float p0, final float p1, final Vector2f p2);
    
    Vector2f mul(final Vector2fc p0, final Vector2f p1);
    
    Vector2f div(final float p0, final Vector2f p1);
    
    Vector2f div(final Vector2fc p0, final Vector2f p1);
    
    Vector2f div(final float p0, final float p1, final Vector2f p2);
    
    Vector2f mul(final Matrix2fc p0, final Vector2f p1);
    
    Vector2f mul(final Matrix2dc p0, final Vector2f p1);
    
    Vector2f mulTranspose(final Matrix2fc p0, final Vector2f p1);
    
    Vector2f mulPosition(final Matrix3x2fc p0, final Vector2f p1);
    
    Vector2f mulDirection(final Matrix3x2fc p0, final Vector2f p1);
    
    Vector2f lerp(final Vector2fc p0, final float p1, final Vector2f p2);
    
    Vector2f fma(final Vector2fc p0, final Vector2fc p1, final Vector2f p2);
    
    Vector2f fma(final float p0, final Vector2fc p1, final Vector2f p2);
    
    Vector2f min(final Vector2fc p0, final Vector2f p1);
    
    Vector2f max(final Vector2fc p0, final Vector2f p1);
    
    int maxComponent();
    
    int minComponent();
    
    float get(final int p0) throws IllegalArgumentException;
    
    Vector2i get(final int p0, final Vector2i p1);
    
    Vector2f get(final Vector2f p0);
    
    Vector2d get(final Vector2d p0);
    
    Vector2f floor(final Vector2f p0);
    
    Vector2f ceil(final Vector2f p0);
    
    Vector2f round(final Vector2f p0);
    
    boolean isFinite();
    
    Vector2f absolute(final Vector2f p0);
    
    boolean equals(final Vector2fc p0, final float p1);
    
    boolean equals(final float p0, final float p1);
}
