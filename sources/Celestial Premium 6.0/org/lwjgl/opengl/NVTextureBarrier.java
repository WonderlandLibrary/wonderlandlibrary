/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public final class NVTextureBarrier {
    private NVTextureBarrier() {
    }

    public static void glTextureBarrierNV() {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glTextureBarrierNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        NVTextureBarrier.nglTextureBarrierNV(function_pointer);
    }

    static native void nglTextureBarrierNV(long var0);
}

