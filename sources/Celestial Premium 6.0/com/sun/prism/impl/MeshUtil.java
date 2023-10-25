/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Quat4f;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.prism.impl.MeshTempState;

class MeshUtil {
    static final float NORMAL_WELD_COS = 0.9952f;
    static final float TANGENT_WELD_COS = 0.866f;
    static final float G_UV_PARALLEL = 0.9988f;
    static final float COS_1_DEGREE = 0.9998477f;
    static final float BIG_ENOUGH_NORMA2 = 0.0625f;
    static final double PI = Math.PI;
    static final float INV_SQRT2 = 0.70710677f;
    static final float DEAD_FACE = 9.094947E-13f;
    static final float MAGIC_SMALL = 1.0E-10f;
    static final float COS110 = -0.33333334f;

    private MeshUtil() {
    }

    static boolean isDeadFace(float f) {
        return f < 9.094947E-13f;
    }

    static boolean isDeadFace(int[] arrn) {
        return arrn[0] == arrn[1] || arrn[1] == arrn[2] || arrn[2] == arrn[0];
    }

    static boolean isNormalAlmostEqual(Vec3f vec3f, Vec3f vec3f2) {
        return vec3f.dot(vec3f2) >= 0.9998477f;
    }

    static boolean isTangentOk(Vec3f[] arrvec3f, Vec3f[] arrvec3f2) {
        return arrvec3f[0].dot(arrvec3f2[0]) >= 0.9952f && arrvec3f[1].dot(arrvec3f2[1]) >= 0.866f && arrvec3f[2].dot(arrvec3f2[2]) >= 0.866f;
    }

    static boolean isNormalOkAfterWeld(Vec3f vec3f) {
        return vec3f.dot(vec3f) > 0.0625f;
    }

    static boolean isTangentOK(Vec3f[] arrvec3f) {
        return MeshUtil.isTangentOk(arrvec3f, arrvec3f);
    }

    static boolean isOppositeLookingNormals(Vec3f[] arrvec3f, Vec3f[] arrvec3f2) {
        float f = arrvec3f[0].dot(arrvec3f2[0]);
        return f < -0.33333334f;
    }

    static float fabs(float f) {
        return f < 0.0f ? -f : f;
    }

    static void getOrt(Vec3f vec3f, Vec3f vec3f2) {
        vec3f2.cross(vec3f, vec3f2);
        vec3f2.cross(vec3f2, vec3f);
    }

    static void orthogonalizeTB(Vec3f[] arrvec3f) {
        MeshUtil.getOrt(arrvec3f[0], arrvec3f[1]);
        MeshUtil.getOrt(arrvec3f[0], arrvec3f[2]);
        arrvec3f[1].normalize();
        arrvec3f[2].normalize();
    }

    static void computeTBNNormalized(Vec3f vec3f, Vec3f vec3f2, Vec3f vec3f3, Vec2f vec2f, Vec2f vec2f2, Vec2f vec2f3, Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f4 = meshTempState.vec3f1;
        Vec3f vec3f5 = meshTempState.vec3f2;
        Vec3f vec3f6 = meshTempState.vec3f3;
        vec3f5.sub(vec3f2, vec3f);
        vec3f6.sub(vec3f3, vec3f);
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[0].set(vec3f4);
        arrvec3f[0].normalize();
        vec3f5.set(0.0f, vec2f2.x - vec2f.x, vec2f2.y - vec2f.y);
        vec3f6.set(0.0f, vec2f3.x - vec2f.x, vec2f3.y - vec2f.y);
        if (vec3f5.y * vec3f6.z == vec3f5.z * vec3f6.y) {
            MeshUtil.generateTB(vec3f, vec3f2, vec3f3, arrvec3f);
            return;
        }
        vec3f5.x = vec3f2.x - vec3f.x;
        vec3f6.x = vec3f3.x - vec3f.x;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].x = -vec3f4.y / vec3f4.x;
        arrvec3f[2].x = -vec3f4.z / vec3f4.x;
        vec3f5.x = vec3f2.y - vec3f.y;
        vec3f6.x = vec3f3.y - vec3f.y;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].y = -vec3f4.y / vec3f4.x;
        arrvec3f[2].y = -vec3f4.z / vec3f4.x;
        vec3f5.x = vec3f2.z - vec3f.z;
        vec3f6.x = vec3f3.z - vec3f.z;
        vec3f4.cross(vec3f5, vec3f6);
        arrvec3f[1].z = -vec3f4.y / vec3f4.x;
        arrvec3f[2].z = -vec3f4.z / vec3f4.x;
        arrvec3f[1].normalize();
        arrvec3f[2].normalize();
    }

    static void fixParallelTB(Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f = meshTempState.vec3f1;
        vec3f.add(arrvec3f[1], arrvec3f[2]);
        Vec3f vec3f2 = meshTempState.vec3f2;
        vec3f2.cross(arrvec3f[0], vec3f);
        vec3f.normalize();
        vec3f2.normalize();
        arrvec3f[1].add(vec3f, vec3f2);
        arrvec3f[1].mul(0.70710677f);
        arrvec3f[2].sub(vec3f, vec3f2);
        arrvec3f[2].mul(0.70710677f);
    }

    static void generateTB(Vec3f vec3f, Vec3f vec3f2, Vec3f vec3f3, Vec3f[] arrvec3f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f4 = meshTempState.vec3f1;
        vec3f4.sub(vec3f2, vec3f);
        Vec3f vec3f5 = meshTempState.vec3f2;
        vec3f5.sub(vec3f3, vec3f);
        if (vec3f4.dot(vec3f4) > vec3f5.dot(vec3f5)) {
            arrvec3f[1].set(vec3f4);
            arrvec3f[1].normalize();
            arrvec3f[2].cross(arrvec3f[0], arrvec3f[1]);
        } else {
            arrvec3f[2].set(vec3f5);
            arrvec3f[2].normalize();
            arrvec3f[1].cross(arrvec3f[2], arrvec3f[0]);
        }
    }

    static double clamp(double d, double d2, double d3) {
        return d < d3 ? (d > d2 ? d : d2) : d3;
    }

    static void fixTSpace(Vec3f[] arrvec3f) {
        float f = arrvec3f[0].length();
        MeshTempState meshTempState = MeshTempState.getInstance();
        Vec3f vec3f = meshTempState.vec3f1;
        vec3f.set(arrvec3f[1]);
        Vec3f vec3f2 = meshTempState.vec3f2;
        vec3f2.set(arrvec3f[2]);
        MeshUtil.getOrt(arrvec3f[0], vec3f);
        MeshUtil.getOrt(arrvec3f[0], vec3f2);
        float f2 = vec3f.length();
        float f3 = vec3f2.length();
        double d = vec3f.dot(vec3f2) / (f2 * f3);
        Vec3f vec3f3 = meshTempState.vec3f3;
        Vec3f vec3f4 = meshTempState.vec3f4;
        if ((double)MeshUtil.fabs((float)d) > 0.998) {
            Vec3f vec3f5 = meshTempState.vec3f5;
            vec3f5.cross(arrvec3f[0], vec3f);
            vec3f5.normalize();
            vec3f4.set(vec3f5);
            if (vec3f5.dot(vec3f2) < 0.0f) {
                vec3f4.mul(-1.0f);
            }
            vec3f3.set(vec3f);
            vec3f3.mul(1.0f / f2);
        } else {
            double d2 = Math.acos(MeshUtil.clamp(d, -1.0, 1.0));
            double d3 = (1.5707963267948966 - d2) * 0.5;
            Vec2f vec2f = meshTempState.vec2f1;
            vec2f.set((float)Math.sin(d3), (float)Math.cos(d3));
            Vec2f vec2f2 = meshTempState.vec2f2;
            vec2f2.set((float)Math.sin(d3 + d2), (float)Math.cos(d3 + d2));
            Vec3f vec3f6 = meshTempState.vec3f5;
            vec3f6.set(vec3f2);
            MeshUtil.getOrt(vec3f, vec3f6);
            float f4 = vec3f6.length();
            vec3f3.set(vec3f);
            vec3f3.mul(vec2f.y / f2);
            Vec3f vec3f7 = meshTempState.vec3f6;
            vec3f7.set(vec3f6);
            vec3f7.mul(vec2f.x / f4);
            vec3f3.sub(vec3f7);
            vec3f4.set(vec3f);
            vec3f4.mul(vec2f2.y / f2);
            vec3f7.set(vec3f6);
            vec3f7.mul(vec2f2.x / f4);
            vec3f4.add(vec3f7);
            float f5 = vec3f3.dot(vec3f);
            float f6 = vec3f4.dot(vec3f2);
        }
        arrvec3f[1].set(vec3f3);
        arrvec3f[2].set(vec3f4);
        arrvec3f[0].mul(1.0f / f);
    }

    static void buildQuat(Vec3f[] arrvec3f, Quat4f quat4f) {
        MeshTempState meshTempState = MeshTempState.getInstance();
        float[][] arrf = meshTempState.matrix;
        float[] arrf2 = meshTempState.vector;
        for (int i = 0; i < 3; ++i) {
            arrf[i][0] = arrvec3f[i].x;
            arrf[i][1] = arrvec3f[i].y;
            arrf[i][2] = arrvec3f[i].z;
        }
        float f = arrf[0][0] + arrf[1][1] + arrf[2][2];
        if (f > 0.0f) {
            float f2 = (float)Math.sqrt(f + 1.0f);
            float f3 = 0.5f / f2;
            quat4f.w = 0.5f * f2;
            quat4f.x = (arrf[1][2] - arrf[2][1]) * f3;
            quat4f.y = (arrf[2][0] - arrf[0][2]) * f3;
            quat4f.z = (arrf[0][1] - arrf[1][0]) * f3;
        } else {
            int[] arrn = new int[]{1, 2, 0};
            int n = 0;
            if (arrf[1][1] > arrf[0][0]) {
                n = 1;
            }
            if (arrf[2][2] > arrf[n][n]) {
                n = 2;
            }
            int n2 = arrn[n];
            int n3 = arrn[n2];
            float f4 = (float)Math.sqrt(arrf[n][n] - arrf[n2][n2] - arrf[n3][n3] + 1.0f);
            if (arrf[n2][n3] < arrf[n3][n2]) {
                f4 = -f4;
            }
            float f5 = 0.5f / f4;
            arrf2[n] = 0.5f * f4;
            quat4f.w = (arrf[n2][n3] - arrf[n3][n2]) * f5;
            arrf2[n2] = (arrf[n][n2] + arrf[n2][n]) * f5;
            arrf2[n3] = (arrf[n][n3] + arrf[n3][n]) * f5;
            quat4f.x = arrf2[0];
            quat4f.y = arrf2[1];
            quat4f.z = arrf2[2];
        }
    }
}

