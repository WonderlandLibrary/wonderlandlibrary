/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLAllocation {
    public static FloatBuffer createDirectFloatBuffer(int n) {
        return GLAllocation.createDirectByteBuffer(n << 2).asFloatBuffer();
    }

    public static synchronized ByteBuffer createDirectByteBuffer(int n) {
        return ByteBuffer.allocateDirect(n).order(ByteOrder.nativeOrder());
    }

    public static synchronized void deleteDisplayLists(int n, int n2) {
        GL11.glDeleteLists((int)n, (int)n2);
    }

    public static synchronized void deleteDisplayLists(int n) {
        GL11.glDeleteLists((int)n, (int)1);
    }

    public static IntBuffer createDirectIntBuffer(int n) {
        return GLAllocation.createDirectByteBuffer(n << 2).asIntBuffer();
    }

    public static synchronized int generateDisplayLists(int n) {
        int n2 = GL11.glGenLists((int)n);
        if (n2 == 0) {
            int n3 = GL11.glGetError();
            String string = "No error code reported";
            if (n3 != 0) {
                string = GLU.gluErrorString((int)n3);
            }
            throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + n + ", GL error (" + n3 + "): " + string);
        }
        return n2;
    }
}

