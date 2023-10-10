// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix4fc
{
    public static final int PLANE_NX = 0;
    public static final int PLANE_PX = 1;
    public static final int PLANE_NY = 2;
    public static final int PLANE_PY = 3;
    public static final int PLANE_NZ = 4;
    public static final int PLANE_PZ = 5;
    public static final int CORNER_NXNYNZ = 0;
    public static final int CORNER_PXNYNZ = 1;
    public static final int CORNER_PXPYNZ = 2;
    public static final int CORNER_NXPYNZ = 3;
    public static final int CORNER_PXNYPZ = 4;
    public static final int CORNER_NXNYPZ = 5;
    public static final int CORNER_NXPYPZ = 6;
    public static final int CORNER_PXPYPZ = 7;
    public static final byte PROPERTY_PERSPECTIVE = 1;
    public static final byte PROPERTY_AFFINE = 2;
    public static final byte PROPERTY_IDENTITY = 4;
    public static final byte PROPERTY_TRANSLATION = 8;
    public static final byte PROPERTY_ORTHONORMAL = 16;
    
    int properties();
    
    float m00();
    
    float m01();
    
    float m02();
    
    float m03();
    
    float m10();
    
    float m11();
    
    float m12();
    
    float m13();
    
    float m20();
    
    float m21();
    
    float m22();
    
    float m23();
    
    float m30();
    
    float m31();
    
    float m32();
    
    float m33();
    
    Matrix4f mul(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mul0(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mul(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final float p11, final float p12, final float p13, final float p14, final float p15, final Matrix4f p16);
    
    Matrix4f mul3x3(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4f p9);
    
    Matrix4f mulLocal(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulLocalAffine(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mul(final Matrix3x2fc p0, final Matrix4f p1);
    
    Matrix4f mul(final Matrix4x3fc p0, final Matrix4f p1);
    
    Matrix4f mulPerspectiveAffine(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulPerspectiveAffine(final Matrix4x3fc p0, final Matrix4f p1);
    
    Matrix4f mulAffineR(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulAffine(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulTranslationAffine(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulOrthoAffine(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f fma4x3(final Matrix4fc p0, final float p1, final Matrix4f p2);
    
    Matrix4f add(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f sub(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mulComponentWise(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f add4x3(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f sub4x3(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f mul4x3ComponentWise(final Matrix4fc p0, final Matrix4f p1);
    
    float determinant();
    
    float determinant3x3();
    
    float determinantAffine();
    
    Matrix4f invert(final Matrix4f p0);
    
    Matrix4f invertPerspective(final Matrix4f p0);
    
    Matrix4f invertFrustum(final Matrix4f p0);
    
    Matrix4f invertOrtho(final Matrix4f p0);
    
    Matrix4f invertPerspectiveView(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f invertPerspectiveView(final Matrix4x3fc p0, final Matrix4f p1);
    
    Matrix4f invertAffine(final Matrix4f p0);
    
    Matrix4f transpose(final Matrix4f p0);
    
    Matrix4f transpose3x3(final Matrix4f p0);
    
    Matrix3f transpose3x3(final Matrix3f p0);
    
    Vector3f getTranslation(final Vector3f p0);
    
    Vector3f getScale(final Vector3f p0);
    
    Matrix4f get(final Matrix4f p0);
    
    Matrix4x3f get4x3(final Matrix4x3f p0);
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix3f get3x3(final Matrix3f p0);
    
    Matrix3d get3x3(final Matrix3d p0);
    
    AxisAngle4f getRotation(final AxisAngle4f p0);
    
    AxisAngle4d getRotation(final AxisAngle4d p0);
    
    Quaternionf getUnnormalizedRotation(final Quaternionf p0);
    
    Quaternionf getNormalizedRotation(final Quaternionf p0);
    
    Quaterniond getUnnormalizedRotation(final Quaterniond p0);
    
    Quaterniond getNormalizedRotation(final Quaterniond p0);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    FloatBuffer get4x3(final FloatBuffer p0);
    
    FloatBuffer get4x3(final int p0, final FloatBuffer p1);
    
    ByteBuffer get4x3(final ByteBuffer p0);
    
    ByteBuffer get4x3(final int p0, final ByteBuffer p1);
    
    FloatBuffer get3x4(final FloatBuffer p0);
    
    FloatBuffer get3x4(final int p0, final FloatBuffer p1);
    
    ByteBuffer get3x4(final ByteBuffer p0);
    
    ByteBuffer get3x4(final int p0, final ByteBuffer p1);
    
    FloatBuffer getTransposed(final FloatBuffer p0);
    
    FloatBuffer getTransposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    FloatBuffer get4x3Transposed(final FloatBuffer p0);
    
    FloatBuffer get4x3Transposed(final int p0, final FloatBuffer p1);
    
    ByteBuffer get4x3Transposed(final ByteBuffer p0);
    
    ByteBuffer get4x3Transposed(final int p0, final ByteBuffer p1);
    
    Matrix4fc getToAddress(final long p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    Vector4f transform(final Vector4f p0);
    
    Vector4f transform(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transform(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f transformTranspose(final Vector4f p0);
    
    Vector4f transformTranspose(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformTranspose(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector4f transformProject(final Vector4f p0);
    
    Vector4f transformProject(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformProject(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Vector3f transformProject(final Vector3f p0);
    
    Vector3f transformProject(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformProject(final Vector4fc p0, final Vector3f p1);
    
    Vector3f transformProject(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f transformProject(final float p0, final float p1, final float p2, final float p3, final Vector3f p4);
    
    Vector3f transformPosition(final Vector3f p0);
    
    Vector3f transformPosition(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformPosition(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector3f transformDirection(final Vector3f p0);
    
    Vector3f transformDirection(final Vector3fc p0, final Vector3f p1);
    
    Vector3f transformDirection(final float p0, final float p1, final float p2, final Vector3f p3);
    
    Vector4f transformAffine(final Vector4f p0);
    
    Vector4f transformAffine(final Vector4fc p0, final Vector4f p1);
    
    Vector4f transformAffine(final float p0, final float p1, final float p2, final float p3, final Vector4f p4);
    
    Matrix4f scale(final Vector3fc p0, final Matrix4f p1);
    
    Matrix4f scale(final float p0, final Matrix4f p1);
    
    Matrix4f scaleXY(final float p0, final float p1, final Matrix4f p2);
    
    Matrix4f scale(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f scaleAround(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f scaleAround(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f scaleLocal(final float p0, final Matrix4f p1);
    
    Matrix4f scaleLocal(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f scaleAroundLocal(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f scaleAroundLocal(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateX(final float p0, final Matrix4f p1);
    
    Matrix4f rotateY(final float p0, final Matrix4f p1);
    
    Matrix4f rotateZ(final float p0, final Matrix4f p1);
    
    Matrix4f rotateTowardsXY(final float p0, final float p1, final Matrix4f p2);
    
    Matrix4f rotateXYZ(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotateAffineXYZ(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotateZYX(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotateAffineZYX(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotateYXZ(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotateAffineYXZ(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f rotate(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateTranslation(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateAffine(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateLocal(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateLocalX(final float p0, final Matrix4f p1);
    
    Matrix4f rotateLocalY(final float p0, final Matrix4f p1);
    
    Matrix4f rotateLocalZ(final float p0, final Matrix4f p1);
    
    Matrix4f translate(final Vector3fc p0, final Matrix4f p1);
    
    Matrix4f translate(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f translateLocal(final Vector3fc p0, final Matrix4f p1);
    
    Matrix4f translateLocal(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f ortho(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f ortho(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f orthoLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f orthoLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f orthoSymmetric(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4f p5);
    
    Matrix4f orthoSymmetric(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f orthoSymmetricLH(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4f p5);
    
    Matrix4f orthoSymmetricLH(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f ortho2D(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f ortho2DLH(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f lookAlong(final Vector3fc p0, final Vector3fc p1, final Matrix4f p2);
    
    Matrix4f lookAlong(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f lookAt(final Vector3fc p0, final Vector3fc p1, final Vector3fc p2, final Matrix4f p3);
    
    Matrix4f lookAt(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4f p9);
    
    Matrix4f lookAtPerspective(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4f p9);
    
    Matrix4f lookAtLH(final Vector3fc p0, final Vector3fc p1, final Vector3fc p2, final Matrix4f p3);
    
    Matrix4f lookAtLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4f p9);
    
    Matrix4f lookAtPerspectiveLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final Matrix4f p9);
    
    Matrix4f tile(final int p0, final int p1, final int p2, final int p3, final Matrix4f p4);
    
    Matrix4f perspective(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4f p5);
    
    Matrix4f perspective(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f perspectiveRect(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4f p5);
    
    Matrix4f perspectiveRect(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f perspectiveOffCenter(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f perspectiveOffCenter(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f perspectiveOffCenterFov(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f perspectiveOffCenterFov(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f perspectiveOffCenterFovLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f perspectiveOffCenterFovLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f perspectiveLH(final float p0, final float p1, final float p2, final float p3, final boolean p4, final Matrix4f p5);
    
    Matrix4f perspectiveLH(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f frustum(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f frustum(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f frustumLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final boolean p6, final Matrix4f p7);
    
    Matrix4f frustumLH(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f rotate(final Quaternionfc p0, final Matrix4f p1);
    
    Matrix4f rotateAffine(final Quaternionfc p0, final Matrix4f p1);
    
    Matrix4f rotateTranslation(final Quaternionfc p0, final Matrix4f p1);
    
    Matrix4f rotateAroundAffine(final Quaternionfc p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateAround(final Quaternionfc p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotateLocal(final Quaternionfc p0, final Matrix4f p1);
    
    Matrix4f rotateAroundLocal(final Quaternionfc p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f rotate(final AxisAngle4f p0, final Matrix4f p1);
    
    Matrix4f rotate(final float p0, final Vector3fc p1, final Matrix4f p2);
    
    Vector4f unproject(final float p0, final float p1, final float p2, final int[] p3, final Vector4f p4);
    
    Vector3f unproject(final float p0, final float p1, final float p2, final int[] p3, final Vector3f p4);
    
    Vector4f unproject(final Vector3fc p0, final int[] p1, final Vector4f p2);
    
    Vector3f unproject(final Vector3fc p0, final int[] p1, final Vector3f p2);
    
    Matrix4f unprojectRay(final float p0, final float p1, final int[] p2, final Vector3f p3, final Vector3f p4);
    
    Matrix4f unprojectRay(final Vector2fc p0, final int[] p1, final Vector3f p2, final Vector3f p3);
    
    Vector4f unprojectInv(final Vector3fc p0, final int[] p1, final Vector4f p2);
    
    Vector4f unprojectInv(final float p0, final float p1, final float p2, final int[] p3, final Vector4f p4);
    
    Matrix4f unprojectInvRay(final Vector2fc p0, final int[] p1, final Vector3f p2, final Vector3f p3);
    
    Matrix4f unprojectInvRay(final float p0, final float p1, final int[] p2, final Vector3f p3, final Vector3f p4);
    
    Vector3f unprojectInv(final Vector3fc p0, final int[] p1, final Vector3f p2);
    
    Vector3f unprojectInv(final float p0, final float p1, final float p2, final int[] p3, final Vector3f p4);
    
    Vector4f project(final float p0, final float p1, final float p2, final int[] p3, final Vector4f p4);
    
    Vector3f project(final float p0, final float p1, final float p2, final int[] p3, final Vector3f p4);
    
    Vector4f project(final Vector3fc p0, final int[] p1, final Vector4f p2);
    
    Vector3f project(final Vector3fc p0, final int[] p1, final Vector3f p2);
    
    Matrix4f reflect(final float p0, final float p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f reflect(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f reflect(final Quaternionfc p0, final Vector3fc p1, final Matrix4f p2);
    
    Matrix4f reflect(final Vector3fc p0, final Vector3fc p1, final Matrix4f p2);
    
    Vector4f getRow(final int p0, final Vector4f p1) throws IndexOutOfBoundsException;
    
    Vector3f getRow(final int p0, final Vector3f p1) throws IndexOutOfBoundsException;
    
    Vector4f getColumn(final int p0, final Vector4f p1) throws IndexOutOfBoundsException;
    
    Vector3f getColumn(final int p0, final Vector3f p1) throws IndexOutOfBoundsException;
    
    float get(final int p0, final int p1);
    
    float getRowColumn(final int p0, final int p1);
    
    Matrix4f normal(final Matrix4f p0);
    
    Matrix3f normal(final Matrix3f p0);
    
    Matrix3f cofactor3x3(final Matrix3f p0);
    
    Matrix4f cofactor3x3(final Matrix4f p0);
    
    Matrix4f normalize3x3(final Matrix4f p0);
    
    Matrix3f normalize3x3(final Matrix3f p0);
    
    Vector4f frustumPlane(final int p0, final Vector4f p1);
    
    Vector3f frustumCorner(final int p0, final Vector3f p1);
    
    Vector3f perspectiveOrigin(final Vector3f p0);
    
    Vector3f perspectiveInvOrigin(final Vector3f p0);
    
    float perspectiveFov();
    
    float perspectiveNear();
    
    float perspectiveFar();
    
    Vector3f frustumRayDir(final float p0, final float p1, final Vector3f p2);
    
    Vector3f positiveZ(final Vector3f p0);
    
    Vector3f normalizedPositiveZ(final Vector3f p0);
    
    Vector3f positiveX(final Vector3f p0);
    
    Vector3f normalizedPositiveX(final Vector3f p0);
    
    Vector3f positiveY(final Vector3f p0);
    
    Vector3f normalizedPositiveY(final Vector3f p0);
    
    Vector3f originAffine(final Vector3f p0);
    
    Vector3f origin(final Vector3f p0);
    
    Matrix4f shadow(final Vector4f p0, final float p1, final float p2, final float p3, final float p4, final Matrix4f p5);
    
    Matrix4f shadow(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final Matrix4f p8);
    
    Matrix4f shadow(final Vector4f p0, final Matrix4fc p1, final Matrix4f p2);
    
    Matrix4f shadow(final float p0, final float p1, final float p2, final float p3, final Matrix4fc p4, final Matrix4f p5);
    
    Matrix4f pick(final float p0, final float p1, final float p2, final float p3, final int[] p4, final Matrix4f p5);
    
    boolean isAffine();
    
    Matrix4f arcball(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Matrix4f arcball(final float p0, final Vector3fc p1, final float p2, final float p3, final Matrix4f p4);
    
    Matrix4f frustumAabb(final Vector3f p0, final Vector3f p1);
    
    Matrix4f projectedGridRange(final Matrix4fc p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f perspectiveFrustumSlice(final float p0, final float p1, final Matrix4f p2);
    
    Matrix4f orthoCrop(final Matrix4fc p0, final Matrix4f p1);
    
    Matrix4f transformAab(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Vector3f p6, final Vector3f p7);
    
    Matrix4f transformAab(final Vector3fc p0, final Vector3fc p1, final Vector3f p2, final Vector3f p3);
    
    Matrix4f lerp(final Matrix4fc p0, final float p1, final Matrix4f p2);
    
    Matrix4f rotateTowards(final Vector3fc p0, final Vector3fc p1, final Matrix4f p2);
    
    Matrix4f rotateTowards(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final Matrix4f p6);
    
    Vector3f getEulerAnglesXYZ(final Vector3f p0);
    
    Vector3f getEulerAnglesZYX(final Vector3f p0);
    
    boolean testPoint(final float p0, final float p1, final float p2);
    
    boolean testSphere(final float p0, final float p1, final float p2, final float p3);
    
    boolean testAab(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    Matrix4f obliqueZ(final float p0, final float p1, final Matrix4f p2);
    
    Matrix4f withLookAtUp(final Vector3fc p0, final Matrix4f p1);
    
    Matrix4f withLookAtUp(final float p0, final float p1, final float p2, final Matrix4f p3);
    
    Matrix4f mapXZY(final Matrix4f p0);
    
    Matrix4f mapXZnY(final Matrix4f p0);
    
    Matrix4f mapXnYnZ(final Matrix4f p0);
    
    Matrix4f mapXnZY(final Matrix4f p0);
    
    Matrix4f mapXnZnY(final Matrix4f p0);
    
    Matrix4f mapYXZ(final Matrix4f p0);
    
    Matrix4f mapYXnZ(final Matrix4f p0);
    
    Matrix4f mapYZX(final Matrix4f p0);
    
    Matrix4f mapYZnX(final Matrix4f p0);
    
    Matrix4f mapYnXZ(final Matrix4f p0);
    
    Matrix4f mapYnXnZ(final Matrix4f p0);
    
    Matrix4f mapYnZX(final Matrix4f p0);
    
    Matrix4f mapYnZnX(final Matrix4f p0);
    
    Matrix4f mapZXY(final Matrix4f p0);
    
    Matrix4f mapZXnY(final Matrix4f p0);
    
    Matrix4f mapZYX(final Matrix4f p0);
    
    Matrix4f mapZYnX(final Matrix4f p0);
    
    Matrix4f mapZnXY(final Matrix4f p0);
    
    Matrix4f mapZnXnY(final Matrix4f p0);
    
    Matrix4f mapZnYX(final Matrix4f p0);
    
    Matrix4f mapZnYnX(final Matrix4f p0);
    
    Matrix4f mapnXYnZ(final Matrix4f p0);
    
    Matrix4f mapnXZY(final Matrix4f p0);
    
    Matrix4f mapnXZnY(final Matrix4f p0);
    
    Matrix4f mapnXnYZ(final Matrix4f p0);
    
    Matrix4f mapnXnYnZ(final Matrix4f p0);
    
    Matrix4f mapnXnZY(final Matrix4f p0);
    
    Matrix4f mapnXnZnY(final Matrix4f p0);
    
    Matrix4f mapnYXZ(final Matrix4f p0);
    
    Matrix4f mapnYXnZ(final Matrix4f p0);
    
    Matrix4f mapnYZX(final Matrix4f p0);
    
    Matrix4f mapnYZnX(final Matrix4f p0);
    
    Matrix4f mapnYnXZ(final Matrix4f p0);
    
    Matrix4f mapnYnXnZ(final Matrix4f p0);
    
    Matrix4f mapnYnZX(final Matrix4f p0);
    
    Matrix4f mapnYnZnX(final Matrix4f p0);
    
    Matrix4f mapnZXY(final Matrix4f p0);
    
    Matrix4f mapnZXnY(final Matrix4f p0);
    
    Matrix4f mapnZYX(final Matrix4f p0);
    
    Matrix4f mapnZYnX(final Matrix4f p0);
    
    Matrix4f mapnZnXY(final Matrix4f p0);
    
    Matrix4f mapnZnXnY(final Matrix4f p0);
    
    Matrix4f mapnZnYX(final Matrix4f p0);
    
    Matrix4f mapnZnYnX(final Matrix4f p0);
    
    Matrix4f negateX(final Matrix4f p0);
    
    Matrix4f negateY(final Matrix4f p0);
    
    Matrix4f negateZ(final Matrix4f p0);
    
    boolean equals(final Matrix4fc p0, final float p1);
    
    boolean isFinite();
}
