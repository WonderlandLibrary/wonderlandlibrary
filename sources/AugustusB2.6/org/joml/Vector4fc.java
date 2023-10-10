// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Vector4fc
{
    float x();
    
    float y();
    
    float z();
    
    float w();
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Vector4fc getToAddress(final long p0);
    
    Vector4f sub(final Vector4fc p0, final Vector4f p1);
    
    Vector4f sub(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f add(final Vector4fc p0, final Vector4f p1);
    
    Vector4f add(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f fma(final Vector4fc p0, final Vector4fc p1, final Vector4f p2);
    
    Vector4f fma(final float p0, final Vector4fc p1, final Vector4f p2);
    
    Vector4f mulAdd(final Vector4fc p0, final Vector4fc p1, final Vector4f p2);
    
    Vector4f mulAdd(final float p0, final Vector4fc p1, final Vector4f p2);
    
    Vector4f mul(final Vector4fc p0, final Vector4f p1);
    
    Vector4f div(final Vector4fc p0, final Vector4f p1);
    
    Vector4f mul(final Matrix4fc p0, final Vector4f p1);
    
    Vector4f mulTranspose(final Matrix4fc p0, final Vector4f p1);
    
    Vector4f mulAffine(final Matrix4fc p0, final Vector4f p1);
    
    Vector4f mulAffineTranspose(final Matrix4fc p0, final Vector4f p1);
    
    Vector4f mul(final Matrix4x3fc p0, final Vector4f p1);
    
    Vector4f mulProject(final Matrix4fc p0, final Vector4f p1);
    
    Vector3f mulProject(final Matrix4fc p0, final Vector3f p1);
    
    Vector4f mul(final float p0, final Vector4f p1);
    
    Vector4f mul(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f div(final float p0, final Vector4f p1);
    
    Vector4f div(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f rotate(final Quaternionfc p0, final Vector4f p1);
    
    Vector4f rotateAxis(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f rotateX(final float p0, final Vector4f p1);
    
    Vector4f rotateY(final float p0, final Vector4f p1);
    
    Vector4f rotateZ(final float p0, final Vector4f p1);
    
    float lengthSquared();
    
    float length();
    
    Vector4f normalize(final Vector4f p0);
    
    Vector4f normalize(final float p0, final Vector4f p1);
    
    Vector4f normalize3(final Vector4f p0);
    
    float distance(final Vector4fc p0);
    
    float distance(final float p0, final float p1, final float p2, final float p3);
    
    float distanceSquared(final Vector4fc p0);
    
    float distanceSquared(final float p0, final float p1, final float p2, final float p3);
    
    float dot(final Vector4fc p0);
    
    float dot(final float p0, final float p1, final float p2, final float p3);
    
    float angleCos(final Vector4fc p0);
    
    float angle(final Vector4fc p0);
    
    Vector4f negate(final Vector4f p0);
    
    Vector4f min(final Vector4fc p0, final Vector4f p1);
    
    Vector4f max(final Vector4fc p0, final Vector4f p1);
    
    Vector4f lerp(final Vector4fc p0, final float p1, final Vector4f p2);
    
    Vector4f smoothStep(final Vector4fc p0, final float p1, final Vector4f p2);
    
    Vector4f hermite(final Vector4fc p0, final Vector4fc p1, final Vector4fc p2, final float p3, final Vector4f p4);
    
    float get(final int p0) throws IllegalArgumentException;
    
    Vector4i get(final int p0, final Vector4i p1);
    
    Vector4f get(final Vector4f p0);
    
    Vector4d get(final Vector4d p0);
    
    int maxComponent();
    
    int minComponent();
    
    Vector4f floor(final Vector4f p0);
    
    Vector4f ceil(final Vector4f p0);
    
    Vector4f round(final Vector4f p0);
    
    boolean isFinite();
    
    Vector4f absolute(final Vector4f p0);
    
    boolean equals(final Vector4fc p0, final float p1);
    
    boolean equals(final float p0, final float p1, final float p2, final float p3);
}
