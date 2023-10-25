/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public final class ATIElementArray {
    public static final int GL_ELEMENT_ARRAY_ATI = 34664;
    public static final int GL_ELEMENT_ARRAY_TYPE_ATI = 34665;
    public static final int GL_ELEMENT_ARRAY_POINTER_ATI = 34666;

    private ATIElementArray() {
    }

    public static void glElementPointerATI(ByteBuffer pPointer) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        ATIElementArray.nglElementPointerATI(5121, MemoryUtil.getAddress(pPointer), function_pointer);
    }

    public static void glElementPointerATI(IntBuffer pPointer) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        ATIElementArray.nglElementPointerATI(5125, MemoryUtil.getAddress(pPointer), function_pointer);
    }

    public static void glElementPointerATI(ShortBuffer pPointer) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glElementPointerATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pPointer);
        ATIElementArray.nglElementPointerATI(5123, MemoryUtil.getAddress(pPointer), function_pointer);
    }

    static native void nglElementPointerATI(int var0, long var1, long var3);

    public static void glDrawElementArrayATI(int mode, int count) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glDrawElementArrayATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        ATIElementArray.nglDrawElementArrayATI(mode, count, function_pointer);
    }

    static native void nglDrawElementArrayATI(int var0, int var1, long var2);

    public static void glDrawRangeElementArrayATI(int mode, int start, int end, int count) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glDrawRangeElementArrayATI;
        BufferChecks.checkFunctionAddress(function_pointer);
        ATIElementArray.nglDrawRangeElementArrayATI(mode, start, end, count, function_pointer);
    }

    static native void nglDrawRangeElementArrayATI(int var0, int var1, int var2, int var3, long var4);
}

