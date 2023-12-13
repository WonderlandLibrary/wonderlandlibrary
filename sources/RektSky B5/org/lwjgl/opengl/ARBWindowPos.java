/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public final class ARBWindowPos {
    private ARBWindowPos() {
    }

    public static void glWindowPos2fARB(float x2, float y2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos2fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos2fARB(x2, y2, function_pointer);
    }

    static native void nglWindowPos2fARB(float var0, float var1, long var2);

    public static void glWindowPos2dARB(double x2, double y2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos2dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos2dARB(x2, y2, function_pointer);
    }

    static native void nglWindowPos2dARB(double var0, double var2, long var4);

    public static void glWindowPos2iARB(int x2, int y2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos2iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos2iARB(x2, y2, function_pointer);
    }

    static native void nglWindowPos2iARB(int var0, int var1, long var2);

    public static void glWindowPos2sARB(short x2, short y2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos2sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos2sARB(x2, y2, function_pointer);
    }

    static native void nglWindowPos2sARB(short var0, short var1, long var2);

    public static void glWindowPos3fARB(float x2, float y2, float z2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos3fARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos3fARB(x2, y2, z2, function_pointer);
    }

    static native void nglWindowPos3fARB(float var0, float var1, float var2, long var3);

    public static void glWindowPos3dARB(double x2, double y2, double z2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos3dARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos3dARB(x2, y2, z2, function_pointer);
    }

    static native void nglWindowPos3dARB(double var0, double var2, double var4, long var6);

    public static void glWindowPos3iARB(int x2, int y2, int z2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos3iARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos3iARB(x2, y2, z2, function_pointer);
    }

    static native void nglWindowPos3iARB(int var0, int var1, int var2, long var3);

    public static void glWindowPos3sARB(short x2, short y2, short z2) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glWindowPos3sARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBWindowPos.nglWindowPos3sARB(x2, y2, z2, function_pointer);
    }

    static native void nglWindowPos3sARB(short var0, short var1, short var2, long var3);
}

