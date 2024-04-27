/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector2f
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
 */
package net.ccbluex.liquidbounce.utils.render;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class WorldToScreen {
    public static Vector2f worldToScreen(Vector3f vector3f, Matrix4f matrix4f, Matrix4f matrix4f2, int n, int n2) {
        Vector4f vector4f = WorldToScreen.multiply(WorldToScreen.multiply(new Vector4f(vector3f.x, vector3f.y, vector3f.z, 1.0f), matrix4f), matrix4f2);
        Vector3f vector3f2 = new Vector3f(vector4f.x / vector4f.w, vector4f.y / vector4f.w, vector4f.z / vector4f.w);
        float f = (vector3f2.x + 1.0f) / 2.0f * (float)n;
        float f2 = (1.0f - vector3f2.y) / 2.0f * (float)n2;
        if ((double)vector3f2.z < -1.0 || (double)vector3f2.z > 1.0) {
            return null;
        }
        return new Vector2f(f, f2);
    }

    public static Matrix4f getMatrix(int n) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)n, (FloatBuffer)floatBuffer);
        return (Matrix4f)new Matrix4f().load(floatBuffer);
    }

    public static Vector4f multiply(Vector4f vector4f, Matrix4f matrix4f) {
        return new Vector4f(vector4f.x * matrix4f.m00 + vector4f.y * matrix4f.m10 + vector4f.z * matrix4f.m20 + vector4f.w * matrix4f.m30, vector4f.x * matrix4f.m01 + vector4f.y * matrix4f.m11 + vector4f.z * matrix4f.m21 + vector4f.w * matrix4f.m31, vector4f.x * matrix4f.m02 + vector4f.y * matrix4f.m12 + vector4f.z * matrix4f.m22 + vector4f.w * matrix4f.m32, vector4f.x * matrix4f.m03 + vector4f.y * matrix4f.m13 + vector4f.z * matrix4f.m23 + vector4f.w * matrix4f.m33);
    }
}

