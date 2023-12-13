/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.ARBProgram;
import org.lwjgl.opengl.ARBVertexShader;

public final class ARBVertexProgram
extends ARBProgram {
    public static final int GL_VERTEX_PROGRAM_ARB = 34336;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 34370;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 34371;
    public static final int GL_COLOR_SUM_ARB = 33880;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 34338;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 34339;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 34340;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 34341;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 34922;
    public static final int GL_CURRENT_VERTEX_ATTRIB_ARB = 34342;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 34373;
    public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 34992;
    public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 34993;
    public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34994;
    public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 34995;
    public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 34921;

    private ARBVertexProgram() {
    }

    public static void glVertexAttrib1sARB(int index, short x2) {
        ARBVertexShader.glVertexAttrib1sARB(index, x2);
    }

    public static void glVertexAttrib1fARB(int index, float x2) {
        ARBVertexShader.glVertexAttrib1fARB(index, x2);
    }

    public static void glVertexAttrib1dARB(int index, double x2) {
        ARBVertexShader.glVertexAttrib1dARB(index, x2);
    }

    public static void glVertexAttrib2sARB(int index, short x2, short y2) {
        ARBVertexShader.glVertexAttrib2sARB(index, x2, y2);
    }

    public static void glVertexAttrib2fARB(int index, float x2, float y2) {
        ARBVertexShader.glVertexAttrib2fARB(index, x2, y2);
    }

    public static void glVertexAttrib2dARB(int index, double x2, double y2) {
        ARBVertexShader.glVertexAttrib2dARB(index, x2, y2);
    }

    public static void glVertexAttrib3sARB(int index, short x2, short y2, short z2) {
        ARBVertexShader.glVertexAttrib3sARB(index, x2, y2, z2);
    }

    public static void glVertexAttrib3fARB(int index, float x2, float y2, float z2) {
        ARBVertexShader.glVertexAttrib3fARB(index, x2, y2, z2);
    }

    public static void glVertexAttrib3dARB(int index, double x2, double y2, double z2) {
        ARBVertexShader.glVertexAttrib3dARB(index, x2, y2, z2);
    }

    public static void glVertexAttrib4sARB(int index, short x2, short y2, short z2, short w2) {
        ARBVertexShader.glVertexAttrib4sARB(index, x2, y2, z2, w2);
    }

    public static void glVertexAttrib4fARB(int index, float x2, float y2, float z2, float w2) {
        ARBVertexShader.glVertexAttrib4fARB(index, x2, y2, z2, w2);
    }

    public static void glVertexAttrib4dARB(int index, double x2, double y2, double z2, double w2) {
        ARBVertexShader.glVertexAttrib4dARB(index, x2, y2, z2, w2);
    }

    public static void glVertexAttrib4NubARB(int index, byte x2, byte y2, byte z2, byte w2) {
        ARBVertexShader.glVertexAttrib4NubARB(index, x2, y2, z2, w2);
    }

    public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, DoubleBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
    }

    public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
    }

    public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }

    public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }

    public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
    }

    public static void glVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
        ARBVertexShader.glVertexAttribPointerARB(index, size, type, normalized, stride, buffer_buffer_offset);
    }

    public static void glEnableVertexAttribArrayARB(int index) {
        ARBVertexShader.glEnableVertexAttribArrayARB(index);
    }

    public static void glDisableVertexAttribArrayARB(int index) {
        ARBVertexShader.glDisableVertexAttribArrayARB(index);
    }

    public static void glGetVertexAttribARB(int index, int pname, FloatBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }

    public static void glGetVertexAttribARB(int index, int pname, DoubleBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }

    public static void glGetVertexAttribARB(int index, int pname, IntBuffer params) {
        ARBVertexShader.glGetVertexAttribARB(index, pname, params);
    }

    public static ByteBuffer glGetVertexAttribPointerARB(int index, int pname, long result_size) {
        return ARBVertexShader.glGetVertexAttribPointerARB(index, pname, result_size);
    }
}

