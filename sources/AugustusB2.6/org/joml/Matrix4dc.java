// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;

public interface Matrix4dc
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
    
    double m00();
    
    double m01();
    
    double m02();
    
    double m03();
    
    double m10();
    
    double m11();
    
    double m12();
    
    double m13();
    
    double m20();
    
    double m21();
    
    double m22();
    
    double m23();
    
    double m30();
    
    double m31();
    
    double m32();
    
    double m33();
    
    Matrix4d mul(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mul0(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mul(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final double p9, final double p10, final double p11, final double p12, final double p13, final double p14, final double p15, final Matrix4d p16);
    
    Matrix4d mul3x3(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4d p9);
    
    Matrix4d mulLocal(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulLocalAffine(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mul(final Matrix3x2dc p0, final Matrix4d p1);
    
    Matrix4d mul(final Matrix3x2fc p0, final Matrix4d p1);
    
    Matrix4d mul(final Matrix4x3dc p0, final Matrix4d p1);
    
    Matrix4d mul(final Matrix4x3fc p0, final Matrix4d p1);
    
    Matrix4d mul(final Matrix4fc p0, final Matrix4d p1);
    
    Matrix4d mulPerspectiveAffine(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulPerspectiveAffine(final Matrix4x3dc p0, final Matrix4d p1);
    
    Matrix4d mulAffineR(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulAffine(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulTranslationAffine(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulOrthoAffine(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d fma4x3(final Matrix4dc p0, final double p1, final Matrix4d p2);
    
    Matrix4d add(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d sub(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mulComponentWise(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d add4x3(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d add4x3(final Matrix4fc p0, final Matrix4d p1);
    
    Matrix4d sub4x3(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d mul4x3ComponentWise(final Matrix4dc p0, final Matrix4d p1);
    
    double determinant();
    
    double determinant3x3();
    
    double determinantAffine();
    
    Matrix4d invert(final Matrix4d p0);
    
    Matrix4d invertPerspective(final Matrix4d p0);
    
    Matrix4d invertFrustum(final Matrix4d p0);
    
    Matrix4d invertOrtho(final Matrix4d p0);
    
    Matrix4d invertPerspectiveView(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d invertPerspectiveView(final Matrix4x3dc p0, final Matrix4d p1);
    
    Matrix4d invertAffine(final Matrix4d p0);
    
    Matrix4d transpose(final Matrix4d p0);
    
    Matrix4d transpose3x3(final Matrix4d p0);
    
    Matrix3d transpose3x3(final Matrix3d p0);
    
    Vector3d getTranslation(final Vector3d p0);
    
    Vector3d getScale(final Vector3d p0);
    
    Matrix4d get(final Matrix4d p0);
    
    Matrix4x3d get4x3(final Matrix4x3d p0);
    
    Matrix3d get3x3(final Matrix3d p0);
    
    Quaternionf getUnnormalizedRotation(final Quaternionf p0);
    
    Quaternionf getNormalizedRotation(final Quaternionf p0);
    
    Quaterniond getUnnormalizedRotation(final Quaterniond p0);
    
    Quaterniond getNormalizedRotation(final Quaterniond p0);
    
    DoubleBuffer get(final DoubleBuffer p0);
    
    DoubleBuffer get(final int p0, final DoubleBuffer p1);
    
    FloatBuffer get(final FloatBuffer p0);
    
    FloatBuffer get(final int p0, final FloatBuffer p1);
    
    ByteBuffer get(final ByteBuffer p0);
    
    ByteBuffer get(final int p0, final ByteBuffer p1);
    
    Matrix4dc getToAddress(final long p0);
    
    ByteBuffer getFloats(final ByteBuffer p0);
    
    ByteBuffer getFloats(final int p0, final ByteBuffer p1);
    
    double[] get(final double[] p0, final int p1);
    
    double[] get(final double[] p0);
    
    float[] get(final float[] p0, final int p1);
    
    float[] get(final float[] p0);
    
    DoubleBuffer getTransposed(final DoubleBuffer p0);
    
    DoubleBuffer getTransposed(final int p0, final DoubleBuffer p1);
    
    ByteBuffer getTransposed(final ByteBuffer p0);
    
    ByteBuffer getTransposed(final int p0, final ByteBuffer p1);
    
    DoubleBuffer get4x3Transposed(final DoubleBuffer p0);
    
    DoubleBuffer get4x3Transposed(final int p0, final DoubleBuffer p1);
    
    ByteBuffer get4x3Transposed(final ByteBuffer p0);
    
    ByteBuffer get4x3Transposed(final int p0, final ByteBuffer p1);
    
    Vector4d transform(final Vector4d p0);
    
    Vector4d transform(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transform(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector4d transformTranspose(final Vector4d p0);
    
    Vector4d transformTranspose(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transformTranspose(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector4d transformProject(final Vector4d p0);
    
    Vector4d transformProject(final Vector4dc p0, final Vector4d p1);
    
    Vector3d transformProject(final Vector4dc p0, final Vector3d p1);
    
    Vector4d transformProject(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Vector3d transformProject(final Vector3d p0);
    
    Vector3d transformProject(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformProject(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d transformProject(final double p0, final double p1, final double p2, final double p3, final Vector3d p4);
    
    Vector3d transformPosition(final Vector3d p0);
    
    Vector3d transformPosition(final Vector3dc p0, final Vector3d p1);
    
    Vector3d transformPosition(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3d transformDirection(final Vector3d p0);
    
    Vector3d transformDirection(final Vector3dc p0, final Vector3d p1);
    
    Vector3f transformDirection(final Vector3f p0);
    
    Vector3f transformDirection(final Vector3fc p0, final Vector3f p1);
    
    Vector3d transformDirection(final double p0, final double p1, final double p2, final Vector3d p3);
    
    Vector3f transformDirection(final double p0, final double p1, final double p2, final Vector3f p3);
    
    Vector4d transformAffine(final Vector4d p0);
    
    Vector4d transformAffine(final Vector4dc p0, final Vector4d p1);
    
    Vector4d transformAffine(final double p0, final double p1, final double p2, final double p3, final Vector4d p4);
    
    Matrix4d scale(final Vector3dc p0, final Matrix4d p1);
    
    Matrix4d scale(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d scale(final double p0, final Matrix4d p1);
    
    Matrix4d scaleXY(final double p0, final double p1, final Matrix4d p2);
    
    Matrix4d scaleAround(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d scaleAround(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d scaleLocal(final double p0, final Matrix4d p1);
    
    Matrix4d scaleLocal(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d scaleAroundLocal(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d scaleAroundLocal(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotate(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateTranslation(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateAffine(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateAroundAffine(final Quaterniondc p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateAround(final Quaterniondc p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateLocal(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d rotateLocalX(final double p0, final Matrix4d p1);
    
    Matrix4d rotateLocalY(final double p0, final Matrix4d p1);
    
    Matrix4d rotateLocalZ(final double p0, final Matrix4d p1);
    
    Matrix4d rotateAroundLocal(final Quaterniondc p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d translate(final Vector3dc p0, final Matrix4d p1);
    
    Matrix4d translate(final Vector3fc p0, final Matrix4d p1);
    
    Matrix4d translate(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d translateLocal(final Vector3fc p0, final Matrix4d p1);
    
    Matrix4d translateLocal(final Vector3dc p0, final Matrix4d p1);
    
    Matrix4d translateLocal(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateX(final double p0, final Matrix4d p1);
    
    Matrix4d rotateY(final double p0, final Matrix4d p1);
    
    Matrix4d rotateZ(final double p0, final Matrix4d p1);
    
    Matrix4d rotateTowardsXY(final double p0, final double p1, final Matrix4d p2);
    
    Matrix4d rotateXYZ(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateAffineXYZ(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateZYX(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateAffineZYX(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateYXZ(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotateAffineYXZ(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d rotate(final Quaterniondc p0, final Matrix4d p1);
    
    Matrix4d rotate(final Quaternionfc p0, final Matrix4d p1);
    
    Matrix4d rotateAffine(final Quaterniondc p0, final Matrix4d p1);
    
    Matrix4d rotateTranslation(final Quaterniondc p0, final Matrix4d p1);
    
    Matrix4d rotateTranslation(final Quaternionfc p0, final Matrix4d p1);
    
    Matrix4d rotateLocal(final Quaterniondc p0, final Matrix4d p1);
    
    Matrix4d rotateAffine(final Quaternionfc p0, final Matrix4d p1);
    
    Matrix4d rotateLocal(final Quaternionfc p0, final Matrix4d p1);
    
    Matrix4d rotate(final AxisAngle4f p0, final Matrix4d p1);
    
    Matrix4d rotate(final AxisAngle4d p0, final Matrix4d p1);
    
    Matrix4d rotate(final double p0, final Vector3dc p1, final Matrix4d p2);
    
    Matrix4d rotate(final double p0, final Vector3fc p1, final Matrix4d p2);
    
    Vector4d getRow(final int p0, final Vector4d p1) throws IndexOutOfBoundsException;
    
    Vector3d getRow(final int p0, final Vector3d p1) throws IndexOutOfBoundsException;
    
    Vector4d getColumn(final int p0, final Vector4d p1) throws IndexOutOfBoundsException;
    
    Vector3d getColumn(final int p0, final Vector3d p1) throws IndexOutOfBoundsException;
    
    double get(final int p0, final int p1);
    
    double getRowColumn(final int p0, final int p1);
    
    Matrix4d normal(final Matrix4d p0);
    
    Matrix3d normal(final Matrix3d p0);
    
    Matrix3d cofactor3x3(final Matrix3d p0);
    
    Matrix4d cofactor3x3(final Matrix4d p0);
    
    Matrix4d normalize3x3(final Matrix4d p0);
    
    Matrix3d normalize3x3(final Matrix3d p0);
    
    Vector4d unproject(final double p0, final double p1, final double p2, final int[] p3, final Vector4d p4);
    
    Vector3d unproject(final double p0, final double p1, final double p2, final int[] p3, final Vector3d p4);
    
    Vector4d unproject(final Vector3dc p0, final int[] p1, final Vector4d p2);
    
    Vector3d unproject(final Vector3dc p0, final int[] p1, final Vector3d p2);
    
    Matrix4d unprojectRay(final double p0, final double p1, final int[] p2, final Vector3d p3, final Vector3d p4);
    
    Matrix4d unprojectRay(final Vector2dc p0, final int[] p1, final Vector3d p2, final Vector3d p3);
    
    Vector4d unprojectInv(final Vector3dc p0, final int[] p1, final Vector4d p2);
    
    Vector4d unprojectInv(final double p0, final double p1, final double p2, final int[] p3, final Vector4d p4);
    
    Vector3d unprojectInv(final Vector3dc p0, final int[] p1, final Vector3d p2);
    
    Vector3d unprojectInv(final double p0, final double p1, final double p2, final int[] p3, final Vector3d p4);
    
    Matrix4d unprojectInvRay(final Vector2dc p0, final int[] p1, final Vector3d p2, final Vector3d p3);
    
    Matrix4d unprojectInvRay(final double p0, final double p1, final int[] p2, final Vector3d p3, final Vector3d p4);
    
    Vector4d project(final double p0, final double p1, final double p2, final int[] p3, final Vector4d p4);
    
    Vector3d project(final double p0, final double p1, final double p2, final int[] p3, final Vector3d p4);
    
    Vector4d project(final Vector3dc p0, final int[] p1, final Vector4d p2);
    
    Vector3d project(final Vector3dc p0, final int[] p1, final Vector3d p2);
    
    Matrix4d reflect(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d reflect(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d reflect(final Quaterniondc p0, final Vector3dc p1, final Matrix4d p2);
    
    Matrix4d reflect(final Vector3dc p0, final Vector3dc p1, final Matrix4d p2);
    
    Matrix4d ortho(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d ortho(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d orthoLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d orthoLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d orthoSymmetric(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4d p5);
    
    Matrix4d orthoSymmetric(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d orthoSymmetricLH(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4d p5);
    
    Matrix4d orthoSymmetricLH(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d ortho2D(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d ortho2DLH(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d lookAlong(final Vector3dc p0, final Vector3dc p1, final Matrix4d p2);
    
    Matrix4d lookAlong(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d lookAt(final Vector3dc p0, final Vector3dc p1, final Vector3dc p2, final Matrix4d p3);
    
    Matrix4d lookAt(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4d p9);
    
    Matrix4d lookAtPerspective(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4d p9);
    
    Matrix4d lookAtLH(final Vector3dc p0, final Vector3dc p1, final Vector3dc p2, final Matrix4d p3);
    
    Matrix4d lookAtLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4d p9);
    
    Matrix4d lookAtPerspectiveLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final double p8, final Matrix4d p9);
    
    Matrix4d tile(final int p0, final int p1, final int p2, final int p3, final Matrix4d p4);
    
    Matrix4d perspective(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4d p5);
    
    Matrix4d perspective(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d perspectiveRect(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4d p5);
    
    Matrix4d perspectiveRect(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d perspectiveOffCenter(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d perspectiveOffCenter(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d perspectiveOffCenterFov(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d perspectiveOffCenterFov(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d perspectiveOffCenterFovLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d perspectiveOffCenterFovLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d perspectiveLH(final double p0, final double p1, final double p2, final double p3, final boolean p4, final Matrix4d p5);
    
    Matrix4d perspectiveLH(final double p0, final double p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d frustum(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d frustum(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d frustumLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final boolean p6, final Matrix4d p7);
    
    Matrix4d frustumLH(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Vector4d frustumPlane(final int p0, final Vector4d p1);
    
    Vector3d frustumCorner(final int p0, final Vector3d p1);
    
    Vector3d perspectiveOrigin(final Vector3d p0);
    
    Vector3d perspectiveInvOrigin(final Vector3d p0);
    
    double perspectiveFov();
    
    double perspectiveNear();
    
    double perspectiveFar();
    
    Vector3d frustumRayDir(final double p0, final double p1, final Vector3d p2);
    
    Vector3d positiveZ(final Vector3d p0);
    
    Vector3d normalizedPositiveZ(final Vector3d p0);
    
    Vector3d positiveX(final Vector3d p0);
    
    Vector3d normalizedPositiveX(final Vector3d p0);
    
    Vector3d positiveY(final Vector3d p0);
    
    Vector3d normalizedPositiveY(final Vector3d p0);
    
    Vector3d originAffine(final Vector3d p0);
    
    Vector3d origin(final Vector3d p0);
    
    Matrix4d shadow(final Vector4dc p0, final double p1, final double p2, final double p3, final double p4, final Matrix4d p5);
    
    Matrix4d shadow(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final double p7, final Matrix4d p8);
    
    Matrix4d shadow(final Vector4dc p0, final Matrix4dc p1, final Matrix4d p2);
    
    Matrix4d shadow(final double p0, final double p1, final double p2, final double p3, final Matrix4dc p4, final Matrix4d p5);
    
    Matrix4d pick(final double p0, final double p1, final double p2, final double p3, final int[] p4, final Matrix4d p5);
    
    boolean isAffine();
    
    Matrix4d arcball(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Matrix4d arcball(final double p0, final Vector3dc p1, final double p2, final double p3, final Matrix4d p4);
    
    Matrix4d projectedGridRange(final Matrix4dc p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d perspectiveFrustumSlice(final double p0, final double p1, final Matrix4d p2);
    
    Matrix4d orthoCrop(final Matrix4dc p0, final Matrix4d p1);
    
    Matrix4d transformAab(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Vector3d p6, final Vector3d p7);
    
    Matrix4d transformAab(final Vector3dc p0, final Vector3dc p1, final Vector3d p2, final Vector3d p3);
    
    Matrix4d lerp(final Matrix4dc p0, final double p1, final Matrix4d p2);
    
    Matrix4d rotateTowards(final Vector3dc p0, final Vector3dc p1, final Matrix4d p2);
    
    Matrix4d rotateTowards(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5, final Matrix4d p6);
    
    Vector3d getEulerAnglesXYZ(final Vector3d p0);
    
    Vector3d getEulerAnglesZYX(final Vector3d p0);
    
    boolean testPoint(final double p0, final double p1, final double p2);
    
    boolean testSphere(final double p0, final double p1, final double p2, final double p3);
    
    boolean testAab(final double p0, final double p1, final double p2, final double p3, final double p4, final double p5);
    
    Matrix4d obliqueZ(final double p0, final double p1, final Matrix4d p2);
    
    Matrix4d withLookAtUp(final Vector3dc p0, final Matrix4d p1);
    
    Matrix4d withLookAtUp(final double p0, final double p1, final double p2, final Matrix4d p3);
    
    Matrix4d mapXZY(final Matrix4d p0);
    
    Matrix4d mapXZnY(final Matrix4d p0);
    
    Matrix4d mapXnYnZ(final Matrix4d p0);
    
    Matrix4d mapXnZY(final Matrix4d p0);
    
    Matrix4d mapXnZnY(final Matrix4d p0);
    
    Matrix4d mapYXZ(final Matrix4d p0);
    
    Matrix4d mapYXnZ(final Matrix4d p0);
    
    Matrix4d mapYZX(final Matrix4d p0);
    
    Matrix4d mapYZnX(final Matrix4d p0);
    
    Matrix4d mapYnXZ(final Matrix4d p0);
    
    Matrix4d mapYnXnZ(final Matrix4d p0);
    
    Matrix4d mapYnZX(final Matrix4d p0);
    
    Matrix4d mapYnZnX(final Matrix4d p0);
    
    Matrix4d mapZXY(final Matrix4d p0);
    
    Matrix4d mapZXnY(final Matrix4d p0);
    
    Matrix4d mapZYX(final Matrix4d p0);
    
    Matrix4d mapZYnX(final Matrix4d p0);
    
    Matrix4d mapZnXY(final Matrix4d p0);
    
    Matrix4d mapZnXnY(final Matrix4d p0);
    
    Matrix4d mapZnYX(final Matrix4d p0);
    
    Matrix4d mapZnYnX(final Matrix4d p0);
    
    Matrix4d mapnXYnZ(final Matrix4d p0);
    
    Matrix4d mapnXZY(final Matrix4d p0);
    
    Matrix4d mapnXZnY(final Matrix4d p0);
    
    Matrix4d mapnXnYZ(final Matrix4d p0);
    
    Matrix4d mapnXnYnZ(final Matrix4d p0);
    
    Matrix4d mapnXnZY(final Matrix4d p0);
    
    Matrix4d mapnXnZnY(final Matrix4d p0);
    
    Matrix4d mapnYXZ(final Matrix4d p0);
    
    Matrix4d mapnYXnZ(final Matrix4d p0);
    
    Matrix4d mapnYZX(final Matrix4d p0);
    
    Matrix4d mapnYZnX(final Matrix4d p0);
    
    Matrix4d mapnYnXZ(final Matrix4d p0);
    
    Matrix4d mapnYnXnZ(final Matrix4d p0);
    
    Matrix4d mapnYnZX(final Matrix4d p0);
    
    Matrix4d mapnYnZnX(final Matrix4d p0);
    
    Matrix4d mapnZXY(final Matrix4d p0);
    
    Matrix4d mapnZXnY(final Matrix4d p0);
    
    Matrix4d mapnZYX(final Matrix4d p0);
    
    Matrix4d mapnZYnX(final Matrix4d p0);
    
    Matrix4d mapnZnXY(final Matrix4d p0);
    
    Matrix4d mapnZnXnY(final Matrix4d p0);
    
    Matrix4d mapnZnYX(final Matrix4d p0);
    
    Matrix4d mapnZnYnX(final Matrix4d p0);
    
    Matrix4d negateX(final Matrix4d p0);
    
    Matrix4d negateY(final Matrix4d p0);
    
    Matrix4d negateZ(final Matrix4d p0);
    
    boolean equals(final Matrix4dc p0, final double p1);
    
    boolean isFinite();
}
