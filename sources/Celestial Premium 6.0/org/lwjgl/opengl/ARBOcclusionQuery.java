/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.APIUtil;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public final class ARBOcclusionQuery {
    public static final int GL_SAMPLES_PASSED_ARB = 35092;
    public static final int GL_QUERY_COUNTER_BITS_ARB = 34916;
    public static final int GL_CURRENT_QUERY_ARB = 34917;
    public static final int GL_QUERY_RESULT_ARB = 34918;
    public static final int GL_QUERY_RESULT_AVAILABLE_ARB = 34919;

    private ARBOcclusionQuery() {
    }

    public static void glGenQueriesARB(IntBuffer ids) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGenQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        ARBOcclusionQuery.nglGenQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }

    static native void nglGenQueriesARB(int var0, long var1, long var3);

    public static int glGenQueriesARB() {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGenQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        IntBuffer ids = APIUtil.getBufferInt(caps);
        ARBOcclusionQuery.nglGenQueriesARB(1, MemoryUtil.getAddress(ids), function_pointer);
        return ids.get(0);
    }

    public static void glDeleteQueriesARB(IntBuffer ids) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glDeleteQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(ids);
        ARBOcclusionQuery.nglDeleteQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
    }

    static native void nglDeleteQueriesARB(int var0, long var1, long var3);

    public static void glDeleteQueriesARB(int id) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glDeleteQueriesARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBOcclusionQuery.nglDeleteQueriesARB(1, APIUtil.getInt(caps, id), function_pointer);
    }

    public static boolean glIsQueryARB(int id) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glIsQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        boolean __result = ARBOcclusionQuery.nglIsQueryARB(id, function_pointer);
        return __result;
    }

    static native boolean nglIsQueryARB(int var0, long var1);

    public static void glBeginQueryARB(int target, int id) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glBeginQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBOcclusionQuery.nglBeginQueryARB(target, id, function_pointer);
    }

    static native void nglBeginQueryARB(int var0, int var1, long var2);

    public static void glEndQueryARB(int target) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glEndQueryARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        ARBOcclusionQuery.nglEndQueryARB(target, function_pointer);
    }

    static native void nglEndQueryARB(int var0, long var1);

    public static void glGetQueryARB(int target, int pname, IntBuffer params) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        ARBOcclusionQuery.nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
    }

    static native void nglGetQueryivARB(int var0, int var1, long var2, long var4);

    @Deprecated
    public static int glGetQueryARB(int target, int pname) {
        return ARBOcclusionQuery.glGetQueryiARB(target, pname);
    }

    public static int glGetQueryiARB(int target, int pname) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        IntBuffer params = APIUtil.getBufferInt(caps);
        ARBOcclusionQuery.nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }

    public static void glGetQueryObjectARB(int id, int pname, IntBuffer params) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryObjectivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        ARBOcclusionQuery.nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }

    static native void nglGetQueryObjectivARB(int var0, int var1, long var2, long var4);

    public static int glGetQueryObjectiARB(int id, int pname) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryObjectivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        IntBuffer params = APIUtil.getBufferInt(caps);
        ARBOcclusionQuery.nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }

    public static void glGetQueryObjectuARB(int id, int pname, IntBuffer params) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryObjectuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(params, 1);
        ARBOcclusionQuery.nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    }

    static native void nglGetQueryObjectuivARB(int var0, int var1, long var2, long var4);

    public static int glGetQueryObjectuiARB(int id, int pname) {
        ContextCapabilities caps = GLContext.getCapabilities();
        long function_pointer = caps.glGetQueryObjectuivARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        IntBuffer params = APIUtil.getBufferInt(caps);
        ARBOcclusionQuery.nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
        return params.get(0);
    }
}

