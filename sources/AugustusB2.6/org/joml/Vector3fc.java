// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Vector3fc
{
    float x();
    
    float y();
    
    float z();
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Vector3fc getToAddress(final long p0);
    
    Vector3f sub(final Vector3fc p0, final Vector3f p1);
    
    Vector3f sub(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f add(final Vector3fc p0, final Vector3f p1);
    
    Vector3f add(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f fma(final Vector3fc p0, final Vector3fc p1, final Vector3f p2);
    
    Vector3f fma(final float p0, final Vector3fc p1, final Vector3f p2);
    
    Vector3f mulAdd(final Vector3fc p0, final Vector3fc p1, final Vector3f p2);
    
    Vector3f mulAdd(final float p0, final Vector3fc p1, final Vector3f p2);
    
    Vector3f mul(final Vector3fc p0, final Vector3f p1);
    
    Vector3f div(final Vector3fc p0, final Vector3f p1);
    
    Vector3f mulProject(final Matrix4fc p0, final Vector3f p1);
    
    Vector3f mulProject(final Matrix4fc p0, final float p1, final Vector3f p2);
    
    Vector3f mul(final Matrix3fc p0, final Vector3f p1);
    
    Vector3f mul(final Matrix3dc p0, final Vector3f p1);
    
    Vector3f mul(final Matrix3x2fc p0, final Vector3f p1);
    
    Vector3f mulTranspose(final Matrix3fc p0, final Vector3f p1);
    
    Vector3f mulPosition(final Matrix4fc p0, final Vector3f p1);
    
    Vector3f mulPosition(final Matrix4x3fc p0, final Vector3f p1);
    
    Vector3f mulTransposePosition(final Matrix4fc p0, final Vector3f p1);
    
    float mulPositionW(final Matrix4fc p0, final Vector3f p1);
    
    Vector3f mulDirection(final Matrix4dc p0, final Vector3f p1);
    
    Vector3f mulDirection(final Matrix4fc p0, final Vector3f p1);
    
    Vector3f mulDirection(final Matrix4x3fc p0, final Vector3f p1);
    
    Vector3f mulTransposeDirection(final Matrix4fc p0, final Vector3f p1);
    
    Vector3f mul(final float p0, final Vector3f p1);
    
    Vector3f mul(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f div(final float p0, final Vector3f p1);
    
    Vector3f div(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f rotate(final Quaternionfc p0, final Vector3f p1);
    
    Quaternionf rotationTo(final Vector3fc p0, final Quaternionf p1);
    
    Quaternionf rotationTo(final float p0, final float p1, final float p2, final Quaternionf p3);
    
    Vector3f rotateAxis(final float p0, final float p1, final float p2, final float p3, final Vector3f p4);
    
    Vector3f rotateX(final float p0, final Vector3f p1);
    
    Vector3f rotateY(final float p0, final Vector3f p1);
    
    Vector3f rotateZ(final float p0, final Vector3f p1);
    
    float lengthSquared();
    
    float length();
    
    Vector3f normalize(final Vector3f p0);
    
    Vector3f normalize(final float p0, final Vector3f p1);
    
    Vector3f cross(final Vector3fc p0, final Vector3f p1);
    
    Vector3f cross(final float p0, final float p1, final float p2, final Vector3f p3);
    
    float distance(final Vector3fc p0);
    
    float distance(final float p0, final float p1, final float p2);
    
    float distanceSquared(final Vector3fc p0);
    
    float distanceSquared(final float p0, final float p1, final float p2);
    
    float dot(final Vector3fc p0);
    
    float dot(final float p0, final float p1, final float p2);
    
    float angleCos(final Vector3fc p0);
    
    float angle(final Vector3fc p0);
    
    float angleSigned(final Vector3fc p0, final Vector3fc p1);
    
    float angleSigned(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    Vector3f min(final Vector3fc p0, final Vector3f p1);
    
    Vector3f max(final Vector3fc p0, final Vector3f p1);
    
    Vector3f negate(final Vector3f p0);
    
    Vector3f absolute(final Vector3f p0);
    
    Vector3f reflect(final Vector3fc p0, final Vector3f p1);
    
    Vector3f reflect(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f half(final Vector3fc p0, final Vector3f p1);
    
    Vector3f half(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f smoothStep(final Vector3fc p0, final float p1, final Vector3f p2);
    
    Vector3f hermite(final Vector3fc p0, final Vector3fc p1, final Vector3fc p2, final float p3, final Vector3f p4);
    
    Vector3f lerp(final Vector3fc p0, final float p1, final Vector3f p2);
    
    float get(final int p0) throws IllegalArgumentException;
    
    Vector3i get(final int p0, final Vector3i p1);
    
    Vector3f get(final Vector3f p0);
    
    Vector3d get(final Vector3d p0);
    
    int maxComponent();
    
    int minComponent();
    
    Vector3f orthogonalize(final Vector3fc p0, final Vector3f p1);
    
    Vector3f orthogonalizeUnit(final Vector3fc p0, final Vector3f p1);
    
    Vector3f floor(final Vector3f p0);
    
    Vector3f ceil(final Vector3f p0);
    
    Vector3f round(final Vector3f p0);
    
    boolean isFinite();
    
    boolean equals(final Vector3fc p0, final float p1);
    
    boolean equals(final float p0, final float p1, final float p2);
}
