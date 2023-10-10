// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;

public interface Quaternionfc
{
    float x();
    
    float y();
    
    float z();
    
    float w();
    
    Quaternionf normalize(final Quaternionf p0);
    
    Quaternionf add(final float p0, final float p1, final float p2, final float p3, final Quaternionf p4);
    
    Quaternionf add(final Quaternionfc p0, final Quaternionf p1);
    
    float angle();
    
    Matrix3f get(final Matrix3f p0);
    
    Matrix3d get(final Matrix3d p0);
    
    Matrix4f get(final Matrix4f p0);
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix4x3f get(final Matrix4x3f p0);
    
    Matrix4x3d get(final Matrix4x3d p0);
    
    AxisAngle4f get(final AxisAngle4f p0);
    
    AxisAngle4d get(final AxisAngle4d p0);
    
    Quaterniond get(final Quaterniond p0);
    
    Quaternionf get(final Quaternionf p0);
    
    ByteBuffer getAsMatrix3f(final ByteBuffer p0);
    
    FloatBuffer getAsMatrix3f(final FloatBuffer p0);
    
    ByteBuffer getAsMatrix4f(final ByteBuffer p0);
    
    FloatBuffer getAsMatrix4f(final FloatBuffer p0);
    
    ByteBuffer getAsMatrix4x3f(final ByteBuffer p0);
    
    FloatBuffer getAsMatrix4x3f(final FloatBuffer p0);
    
    Quaternionf mul(final Quaternionfc p0, final Quaternionf p1);
    
    Quaternionf mul(final float p0, final float p1, final float p2, final float p3, final Quaternionf p4);
    
    Quaternionf premul(final Quaternionfc p0, final Quaternionf p1);
    
    Quaternionf premul(final float p0, final float p1, final float p2, final float p3, final Quaternionf p4);
    
    Vector3f transform(final Vector3f p0);
    
    Vector3f transformInverse(final Vector3f p0);
    
    Vector3f transformUnit(final Vector3f p0);
    
    Vector3f transformPositiveX(final Vector3f p0);
    
    Vector4f transformPositiveX(final Vector4f p0);
    
    Vector3f transformUnitPositiveX(final Vector3f p0);
    
    Vector4f transformUnitPositiveX(final Vector4f p0);
    
    Vector3f transformPositiveY(final Vector3f p0);
    
    Vector4f transformPositiveY(final Vector4f p0);
    
    Vector3f transformUnitPositiveY(final Vector3f p0);
    
    Vector4f transformUnitPositiveY(final Vector4f p0);
    
    Vector3f transformPositiveZ(final Vector3f p0);
    
    Vector4f transformPositiveZ(final Vector4f p0);
    
    Vector3f transformUnitPositiveZ(final Vector3f p0);
    
    Vector4f transformUnitPositiveZ(final Vector4f p0);
    
    Vector4f transform(final Vector4f p0);
    
    Vector4f transformInverse(final Vector4f p0);
    
    Vector3f transform(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformInverse(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transform(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3d transform(final float p0, final float p1, final float p2, final Vector3d p3);
    
    Vector3f transformInverse(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3d transformInverse(final float p0, final float p1, final float p2, final Vector3d p3);
    
    Vector3f transformInverseUnit(final Vector3f p0);
    
    Vector3f transformUnit(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformInverseUnit(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformUnit(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3d transformUnit(final float p0, final float p1, final float p2, final Vector3d p3);
    
    Vector3f transformInverseUnit(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3d transformInverseUnit(final float p0, final float p1, final float p2, final Vector3d p3);
    
    Vector4f transform(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformInverse(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transform(final float p0, final float p1, final float p2, final Vector4f p3);
    
    Vector4f transformInverse(final float p0, final float p1, final float p2, final Vector4f p3);
    
    Vector4f transformUnit(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformUnit(final Vector4f p0);
    
    Vector4f transformInverseUnit(final Vector4f p0);
    
    Vector4f transformInverseUnit(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformUnit(final float p0, final float p1, final float p2, final Vector4f p3);
    
    Vector4f transformInverseUnit(final float p0, final float p1, final float p2, final Vector4f p3);
    
    Vector3d transform(final Vector3d p0);
    
    Vector3d transformInverse(final Vector3d p0);
    
    Vector3d transformPositiveX(final Vector3d p0);
    
    Vector4d transformPositiveX(final Vector4d p0);
    
    Vector3d transformUnitPositiveX(final Vector3d p0);
    
    Vector4d transformUnitPositiveX(final Vector4d p0);
    
    Vector3d transformPositiveY(final Vector3d p0);
    
    Vector4d transformPositiveY(final Vector4d p0);
    
    Vector3d transformUnitPositiveY(final Vector3d p0);
    
    Vector4d transformUnitPositiveY(final Vector4d p0);
    
    Vector3d transformPositiveZ(final Vector3d p0);
    
    Vector4d transformPositiveZ(final Vector4d p0);
    
    Vector3d transformUnitPositiveZ(final Vector3d p0);
    
    Vector4d transformUnitPositiveZ(final Vector4d p0);
    
    Vector4d transform(final Vector4d p0);
    
    Vector4d transformInverse(final Vector4d p0);
    
    Vector3d transform(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformInverse(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transform(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d transformInverse(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector4d transform(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transformInverse(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transform(final double p0, final double p1, final double p2, final Vector4d p3);
    
    Vector4d transformInverse(final double p0, final double p1, final double p2, final Vector4d p3);
    
    Vector4d transformUnit(final Vector4d p0);
    
    Vector4d transformInverseUnit(final Vector4d p0);
    
    Vector3d transformUnit(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformInverseUnit(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformUnit(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d transformInverseUnit(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector4d transformUnit(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transformInverseUnit(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transformUnit(final double p0, final double p1, final double p2, final Vector4d p3);
    
    Vector4d transformInverseUnit(final double p0, final double p1, final double p2, final Vector4d p3);
    
    Quaternionf invert(final Quaternionf p0);
    
    Quaternionf div(final Quaternionfc p0, final Quaternionf p1);
    
    Quaternionf conjugate(final Quaternionf p0);
    
    Quaternionf rotateXYZ(final float p0, final float p1, final float p2, final Quaternionf p3);
    
    Quaternionf rotateZYX(final float p0, final float p1, final float p2, final Quaternionf p3);
    
    Quaternionf rotateYXZ(final float p0, final float p1, final float p2, final Quaternionf p3);
    
    Vector3f getEulerAnglesXYZ(final Vector3f p0);
    
    Vector3f getEulerAnglesZYX(final Vector3f p0);
    
    Vector3f getEulerAnglesZXY(final Vector3f p0);
    
    Vector3f getEulerAnglesYXZ(final Vector3f p0);
    
    float lengthSquared();
    
    Quaternionf slerp(final Quaternionfc p0, final float p1, final Quaternionf p2);
    
    Quaternionf scale(final float p0, final Quaternionf p1);
    
    Quaternionf integrate(final float p0, final float p1, final float p2, final float p3, final Quaternionf p4);
    
    Quaternionf nlerp(final Quaternionfc p0, final float p1, final Quaternionf p2);
    
    Quaternionf nlerpIterative(final Quaternionfc p0, final float p1, final float p2, final Quaternionf p3);
    
    Quaternionf lookAlong(final Vector3fc p0, final Vector3fc p1, final Quaternionf p2);
    
    Quaternionf lookAlong(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Quaternionf p6);
    
    Quaternionf rotateTo(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Quaternionf p6);
    
    Quaternionf rotateTo(final Vector3fc p0, final Vector3fc p1, final Quaternionf p2);
    
    Quaternionf rotateX(final float p0, final Quaternionf p1);
    
    Quaternionf rotateY(final float p0, final Quaternionf p1);
    
    Quaternionf rotateZ(final float p0, final Quaternionf p1);
    
    Quaternionf rotateLocalX(final float p0, final Quaternionf p1);
    
    Quaternionf rotateLocalY(final float p0, final Quaternionf p1);
    
    Quaternionf rotateLocalZ(final float p0, final Quaternionf p1);
    
    Quaternionf rotateAxis(final float p0, final float p1, final float p2, final float p3, final Quaternionf p4);
    
    Quaternionf rotateAxis(final float p0, final Vector3fc p1, final Quaternionf p2);
    
    Quaternionf difference(final Quaternionfc p0, final Quaternionf p1);
    
    Vector3f positiveX(final Vector3f p0);
    
    Vector3f normalizedPositiveX(final Vector3f p0);
    
    Vector3f positiveY(final Vector3f p0);
    
    Vector3f normalizedPositiveY(final Vector3f p0);
    
    Vector3f positiveZ(final Vector3f p0);
    
    Vector3f normalizedPositiveZ(final Vector3f p0);
    
    Quaternionf conjugateBy(final Quaternionfc p0, final Quaternionf p1);
    
    boolean isFinite();
    
    boolean equals(final Quaternionfc p0, final float p1);
    
    boolean equals(final float p0, final float p1, final float p2, final float p3);
}
