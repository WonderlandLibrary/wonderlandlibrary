// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public interface Quaterniondc
{
    double x();
    
    double y();
    
    double z();
    
    double w();
    
    Quaterniond normalize(final Quaterniond p0);
    
    Quaterniond add(final double p0, final double p1, final double p2, final double p3, final Quaterniond p4);
    
    Quaterniond add(final Quaterniondc p0, final Quaterniond p1);
    
    double dot(final Quaterniondc p0);
    
    double angle();
    
    Matrix3d get(final Matrix3d p0);
    
    Matrix3f get(final Matrix3f p0);
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix4f get(final Matrix4f p0);
    
    AxisAngle4f get(final AxisAngle4f p0);
    
    AxisAngle4d get(final AxisAngle4d p0);
    
    Quaterniond get(final Quaterniond p0);
    
    Quaternionf get(final Quaternionf p0);
    
    Quaterniond mul(final Quaterniondc p0, final Quaterniond p1);
    
    Quaterniond mul(final double p0, final double p1, final double p2, final double p3, final Quaterniond p4);
    
    Quaterniond premul(final Quaterniondc p0, final Quaterniond p1);
    
    Quaterniond premul(final double p0, final double p1, final double p2, final double p3, final Quaterniond p4);
    
    Vector3d transform(final Vector3d p0);
    
    Vector3d transformInverse(final Vector3d p0);
    
    Vector3d transformUnit(final Vector3d p0);
    
    Vector3d transformInverseUnit(final Vector3d p0);
    
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
    
    Vector3f transform(final Vector3f p0);
    
    Vector3f transformInverse(final Vector3f p0);
    
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
    
    Vector3f transformUnit(final Vector3f p0);
    
    Vector3f transformInverseUnit(final Vector3f p0);
    
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
    
    Vector3f transform(final double p0, final double p1, final double p2, final Vector3f p3);
    
    Vector3f transformInverse(final double p0, final double p1, final double p2, final Vector3f p3);
    
    Vector4f transform(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformInverse(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transform(final double p0, final double p1, final double p2, final Vector4f p3);
    
    Vector4f transformInverse(final double p0, final double p1, final double p2, final Vector4f p3);
    
    Vector4f transformUnit(final Vector4f p0);
    
    Vector4f transformInverseUnit(final Vector4f p0);
    
    Vector3f transformUnit(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformInverseUnit(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformUnit(final double p0, final double p1, final double p2, final Vector3f p3);
    
    Vector3f transformInverseUnit(final double p0, final double p1, final double p2, final Vector3f p3);
    
    Vector4f transformUnit(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformInverseUnit(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformUnit(final double p0, final double p1, final double p2, final Vector4f p3);
    
    Vector4f transformInverseUnit(final double p0, final double p1, final double p2, final Vector4f p3);
    
    Quaterniond invert(final Quaterniond p0);
    
    Quaterniond div(final Quaterniondc p0, final Quaterniond p1);
    
    Quaterniond conjugate(final Quaterniond p0);
    
    double lengthSquared();
    
    Quaterniond slerp(final Quaterniondc p0, final double p1, final Quaterniond p2);
    
    Quaterniond scale(final double p0, final Quaterniond p1);
    
    Quaterniond integrate(final double p0, final double p1, final double p2, final double p3, final Quaterniond p4);
    
    Quaterniond nlerp(final Quaterniondc p0, final double p1, final Quaterniond p2);
    
    Quaterniond nlerpIterative(final Quaterniondc p0, final double p1, final double p2, final Quaterniond p3);
    
    Quaterniond lookAlong(final Vector3dc p0, final Vector3dc p1, final Quaterniond p2);
    
    Quaterniond lookAlong(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Quaterniond p6);
    
    Quaterniond difference(final Quaterniondc p0, final Quaterniond p1);
    
    Quaterniond rotateTo(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Quaterniond p6);
    
    Quaterniond rotateTo(final Vector3dc p0, final Vector3dc p1, final Quaterniond p2);
    
    Quaterniond rotateX(final double p0, final Quaterniond p1);
    
    Quaterniond rotateY(final double p0, final Quaterniond p1);
    
    Quaterniond rotateZ(final double p0, final Quaterniond p1);
    
    Quaterniond rotateLocalX(final double p0, final Quaterniond p1);
    
    Quaterniond rotateLocalY(final double p0, final Quaterniond p1);
    
    Quaterniond rotateLocalZ(final double p0, final Quaterniond p1);
    
    Quaterniond rotateXYZ(final double p0, final double p1, final double p2, final Quaterniond p3);
    
    Quaterniond rotateZYX(final double p0, final double p1, final double p2, final Quaterniond p3);
    
    Quaterniond rotateYXZ(final double p0, final double p1, final double p2, final Quaterniond p3);
    
    Vector3d getEulerAnglesXYZ(final Vector3d p0);
    
    Vector3d getEulerAnglesZYX(final Vector3d p0);
    
    Vector3d getEulerAnglesZXY(final Vector3d p0);
    
    Vector3d getEulerAnglesYXZ(final Vector3d p0);
    
    Quaterniond rotateAxis(final double p0, final double p1, final double p2, final double p3, final Quaterniond p4);
    
    Quaterniond rotateAxis(final double p0, final Vector3dc p1, final Quaterniond p2);
    
    Vector3d positiveX(final Vector3d p0);
    
    Vector3d normalizedPositiveX(final Vector3d p0);
    
    Vector3d positiveY(final Vector3d p0);
    
    Vector3d normalizedPositiveY(final Vector3d p0);
    
    Vector3d positiveZ(final Vector3d p0);
    
    Vector3d normalizedPositiveZ(final Vector3d p0);
    
    Quaterniond conjugateBy(final Quaterniondc p0, final Quaterniond p1);
    
    boolean isFinite();
    
    boolean equals(final Quaterniondc p0, final double p1);
    
    boolean equals(final double p0, final double p1, final double p2, final double p3);
}
