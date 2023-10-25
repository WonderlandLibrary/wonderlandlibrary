/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextGL;
import org.lwjgl.opengl.ContextImplementation;
import org.lwjgl.opengl.PeerInfo;
import org.lwjgl.opengl.Util;

final class WindowsContextImplementation
implements ContextImplementation {
    WindowsContextImplementation() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ByteBuffer create(PeerInfo peer_info, IntBuffer attribs, ByteBuffer shared_context_handle) throws LWJGLException {
        ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            ByteBuffer byteBuffer = WindowsContextImplementation.nCreate(peer_handle, attribs, shared_context_handle);
            return byteBuffer;
        }
        finally {
            peer_info.unlock();
        }
    }

    private static native ByteBuffer nCreate(ByteBuffer var0, IntBuffer var1, ByteBuffer var2) throws LWJGLException;

    native long getHGLRC(ByteBuffer var1);

    native long getHDC(ByteBuffer var1);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void swapBuffers() throws LWJGLException {
        ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        ContextGL contextGL = current_context;
        synchronized (contextGL) {
            PeerInfo current_peer_info = current_context.getPeerInfo();
            ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
            try {
                WindowsContextImplementation.nSwapBuffers(peer_handle);
            }
            finally {
                current_peer_info.unlock();
            }
        }
    }

    private static native void nSwapBuffers(ByteBuffer var0) throws LWJGLException;

    public void releaseDrawable(ByteBuffer context_handle) throws LWJGLException {
    }

    public void update(ByteBuffer context_handle) {
    }

    public void releaseCurrentContext() throws LWJGLException {
        WindowsContextImplementation.nReleaseCurrentContext();
    }

    private static native void nReleaseCurrentContext() throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void makeCurrent(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
        ByteBuffer peer_handle = peer_info.lockAndGetHandle();
        try {
            WindowsContextImplementation.nMakeCurrent(peer_handle, handle);
        }
        finally {
            peer_info.unlock();
        }
    }

    private static native void nMakeCurrent(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

    public boolean isCurrent(ByteBuffer handle) throws LWJGLException {
        boolean result = WindowsContextImplementation.nIsCurrent(handle);
        return result;
    }

    private static native boolean nIsCurrent(ByteBuffer var0) throws LWJGLException;

    public void setSwapInterval(int value) {
        boolean success = WindowsContextImplementation.nSetSwapInterval(value);
        if (!success) {
            LWJGLUtil.log("Failed to set swap interval");
        }
        Util.checkGLError();
    }

    private static native boolean nSetSwapInterval(int var0);

    public void destroy(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
        WindowsContextImplementation.nDestroy(handle);
    }

    private static native void nDestroy(ByteBuffer var0) throws LWJGLException;
}

