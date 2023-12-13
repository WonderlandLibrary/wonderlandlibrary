/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Util;

public class Project
extends Util {
    private static final float[] IDENTITY_MATRIX = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer tempMatrix = BufferUtils.createFloatBuffer(16);
    private static final float[] in = new float[4];
    private static final float[] out = new float[4];
    private static final float[] forward = new float[3];
    private static final float[] side = new float[3];
    private static final float[] up = new float[3];

    private static void __gluMakeIdentityf(FloatBuffer m2) {
        int oldPos = m2.position();
        m2.put(IDENTITY_MATRIX);
        m2.position(oldPos);
    }

    private static void __gluMultMatrixVecf(FloatBuffer m2, float[] in, float[] out) {
        for (int i2 = 0; i2 < 4; ++i2) {
            out[i2] = in[0] * m2.get(m2.position() + 0 + i2) + in[1] * m2.get(m2.position() + 4 + i2) + in[2] * m2.get(m2.position() + 8 + i2) + in[3] * m2.get(m2.position() + 12 + i2);
        }
    }

    private static boolean __gluInvertMatrixf(FloatBuffer src, FloatBuffer inverse) {
        int i2;
        FloatBuffer temp = tempMatrix;
        for (i2 = 0; i2 < 16; ++i2) {
            temp.put(i2, src.get(i2 + src.position()));
        }
        Project.__gluMakeIdentityf(inverse);
        for (i2 = 0; i2 < 4; ++i2) {
            float t2;
            int k2;
            int j2;
            int swap = i2;
            for (j2 = i2 + 1; j2 < 4; ++j2) {
                if (!(Math.abs(temp.get(j2 * 4 + i2)) > Math.abs(temp.get(i2 * 4 + i2)))) continue;
                swap = j2;
            }
            if (swap != i2) {
                for (k2 = 0; k2 < 4; ++k2) {
                    t2 = temp.get(i2 * 4 + k2);
                    temp.put(i2 * 4 + k2, temp.get(swap * 4 + k2));
                    temp.put(swap * 4 + k2, t2);
                    t2 = inverse.get(i2 * 4 + k2);
                    inverse.put(i2 * 4 + k2, inverse.get(swap * 4 + k2));
                    inverse.put(swap * 4 + k2, t2);
                }
            }
            if (temp.get(i2 * 4 + i2) == 0.0f) {
                return false;
            }
            t2 = temp.get(i2 * 4 + i2);
            for (k2 = 0; k2 < 4; ++k2) {
                temp.put(i2 * 4 + k2, temp.get(i2 * 4 + k2) / t2);
                inverse.put(i2 * 4 + k2, inverse.get(i2 * 4 + k2) / t2);
            }
            for (j2 = 0; j2 < 4; ++j2) {
                if (j2 == i2) continue;
                t2 = temp.get(j2 * 4 + i2);
                for (k2 = 0; k2 < 4; ++k2) {
                    temp.put(j2 * 4 + k2, temp.get(j2 * 4 + k2) - temp.get(i2 * 4 + k2) * t2);
                    inverse.put(j2 * 4 + k2, inverse.get(j2 * 4 + k2) - inverse.get(i2 * 4 + k2) * t2);
                }
            }
        }
        return true;
    }

    private static void __gluMultMatricesf(FloatBuffer a2, FloatBuffer b2, FloatBuffer r2) {
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int j2 = 0; j2 < 4; ++j2) {
                r2.put(r2.position() + i2 * 4 + j2, a2.get(a2.position() + i2 * 4 + 0) * b2.get(b2.position() + 0 + j2) + a2.get(a2.position() + i2 * 4 + 1) * b2.get(b2.position() + 4 + j2) + a2.get(a2.position() + i2 * 4 + 2) * b2.get(b2.position() + 8 + j2) + a2.get(a2.position() + i2 * 4 + 3) * b2.get(b2.position() + 12 + j2));
            }
        }
    }

    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        float radians = fovy / 2.0f * (float)Math.PI / 180.0f;
        float deltaZ = zFar - zNear;
        float sine = (float)Math.sin(radians);
        if (deltaZ == 0.0f || sine == 0.0f || aspect == 0.0f) {
            return;
        }
        float cotangent = (float)Math.cos(radians) / sine;
        Project.__gluMakeIdentityf(matrix);
        matrix.put(0, cotangent / aspect);
        matrix.put(5, cotangent);
        matrix.put(10, -(zFar + zNear) / deltaZ);
        matrix.put(11, -1.0f);
        matrix.put(14, -2.0f * zNear * zFar / deltaZ);
        matrix.put(15, 0.0f);
        GL11.glMultMatrix(matrix);
    }

    public static void gluLookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz) {
        float[] forward = Project.forward;
        float[] side = Project.side;
        float[] up = Project.up;
        forward[0] = centerx - eyex;
        forward[1] = centery - eyey;
        forward[2] = centerz - eyez;
        up[0] = upx;
        up[1] = upy;
        up[2] = upz;
        Project.normalize(forward);
        Project.cross(forward, up, side);
        Project.normalize(side);
        Project.cross(side, forward, up);
        Project.__gluMakeIdentityf(matrix);
        matrix.put(0, side[0]);
        matrix.put(4, side[1]);
        matrix.put(8, side[2]);
        matrix.put(1, up[0]);
        matrix.put(5, up[1]);
        matrix.put(9, up[2]);
        matrix.put(2, -forward[0]);
        matrix.put(6, -forward[1]);
        matrix.put(10, -forward[2]);
        GL11.glMultMatrix(matrix);
        GL11.glTranslatef(-eyex, -eyey, -eyez);
    }

    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in = Project.in;
        float[] out = Project.out;
        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0f;
        Project.__gluMultMatrixVecf(modelMatrix, in, out);
        Project.__gluMultMatrixVecf(projMatrix, out, in);
        if ((double)in[3] == 0.0) {
            return false;
        }
        in[3] = 1.0f / in[3] * 0.5f;
        in[0] = in[0] * in[3] + 0.5f;
        in[1] = in[1] * in[3] + 0.5f;
        in[2] = in[2] * in[3] + 0.5f;
        win_pos.put(0, in[0] * (float)viewport.get(viewport.position() + 2) + (float)viewport.get(viewport.position() + 0));
        win_pos.put(1, in[1] * (float)viewport.get(viewport.position() + 3) + (float)viewport.get(viewport.position() + 1));
        win_pos.put(2, in[2]);
        return true;
    }

    public static boolean gluUnProject(float winx, float winy, float winz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer obj_pos) {
        float[] in = Project.in;
        float[] out = Project.out;
        Project.__gluMultMatricesf(modelMatrix, projMatrix, finalMatrix);
        if (!Project.__gluInvertMatrixf(finalMatrix, finalMatrix)) {
            return false;
        }
        in[0] = winx;
        in[1] = winy;
        in[2] = winz;
        in[3] = 1.0f;
        in[0] = (in[0] - (float)viewport.get(viewport.position() + 0)) / (float)viewport.get(viewport.position() + 2);
        in[1] = (in[1] - (float)viewport.get(viewport.position() + 1)) / (float)viewport.get(viewport.position() + 3);
        in[0] = in[0] * 2.0f - 1.0f;
        in[1] = in[1] * 2.0f - 1.0f;
        in[2] = in[2] * 2.0f - 1.0f;
        Project.__gluMultMatrixVecf(finalMatrix, in, out);
        if ((double)out[3] == 0.0) {
            return false;
        }
        out[3] = 1.0f / out[3];
        obj_pos.put(obj_pos.position() + 0, out[0] * out[3]);
        obj_pos.put(obj_pos.position() + 1, out[1] * out[3]);
        obj_pos.put(obj_pos.position() + 2, out[2] * out[3]);
        return true;
    }

    public static void gluPickMatrix(float x2, float y2, float deltaX, float deltaY, IntBuffer viewport) {
        if (deltaX <= 0.0f || deltaY <= 0.0f) {
            return;
        }
        GL11.glTranslatef(((float)viewport.get(viewport.position() + 2) - 2.0f * (x2 - (float)viewport.get(viewport.position() + 0))) / deltaX, ((float)viewport.get(viewport.position() + 3) - 2.0f * (y2 - (float)viewport.get(viewport.position() + 1))) / deltaY, 0.0f);
        GL11.glScalef((float)viewport.get(viewport.position() + 2) / deltaX, (float)viewport.get(viewport.position() + 3) / deltaY, 1.0f);
    }
}

