/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextGL;
import org.lwjgl.opengl.ContextImplementation;
import org.lwjgl.opengl.LinuxDisplay;
import org.lwjgl.opengl.PeerInfo;

final class LinuxContextImplementation
implements ContextImplementation {
    LinuxContextImplementation() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ByteBuffer create(PeerInfo peer_info, IntBuffer attribs, ByteBuffer shared_context_handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer byteBuffer;
            ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                byteBuffer = LinuxContextImplementation.nCreate(peer_handle, attribs, shared_context_handle);
            }
            catch (Throwable throwable) {
                peer_info.unlock();
                throw throwable;
            }
            peer_info.unlock();
            return byteBuffer;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native ByteBuffer nCreate(ByteBuffer var0, IntBuffer var1, ByteBuffer var2) throws LWJGLException;

    native long getGLXContext(ByteBuffer var1);

    native long getDisplay(ByteBuffer var1);

    public void releaseDrawable(ByteBuffer context_handle) throws LWJGLException {
    }

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
            LinuxDisplay.lockAWT();
            try {
                ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
                try {
                    LinuxContextImplementation.nSwapBuffers(peer_handle);
                }
                finally {
                    current_peer_info.unlock();
                }
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }

    private static native void nSwapBuffers(ByteBuffer var0) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseCurrentContext() throws LWJGLException {
        ContextGL current_context = ContextGL.getCurrentContext();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        ContextGL contextGL = current_context;
        synchronized (contextGL) {
            PeerInfo current_peer_info = current_context.getPeerInfo();
            LinuxDisplay.lockAWT();
            try {
                ByteBuffer peer_handle = current_peer_info.lockAndGetHandle();
                try {
                    LinuxContextImplementation.nReleaseCurrentContext(peer_handle);
                }
                finally {
                    current_peer_info.unlock();
                }
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }

    private static native void nReleaseCurrentContext(ByteBuffer var0) throws LWJGLException;

    public void update(ByteBuffer context_handle) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void makeCurrent(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                LinuxContextImplementation.nMakeCurrent(peer_handle, handle);
            }
            finally {
                peer_info.unlock();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nMakeCurrent(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isCurrent(ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            boolean result;
            boolean bl = result = LinuxContextImplementation.nIsCurrent(handle);
            return bl;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native boolean nIsCurrent(ByteBuffer var0) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setSwapInterval(int value) {
        ContextGL current_context = ContextGL.getCurrentContext();
        PeerInfo peer_info = current_context.getPeerInfo();
        if (current_context == null) {
            throw new IllegalStateException("No context is current");
        }
        ContextGL contextGL = current_context;
        synchronized (contextGL) {
            LinuxDisplay.lockAWT();
            try {
                ByteBuffer peer_handle = peer_info.lockAndGetHandle();
                try {
                    LinuxContextImplementation.nSetSwapInterval(peer_handle, current_context.getHandle(), value);
                }
                finally {
                    peer_info.unlock();
                }
            }
            catch (LWJGLException e) {
                e.printStackTrace();
            }
            finally {
                LinuxDisplay.unlockAWT();
            }
        }
    }

    private static native void nSetSwapInterval(ByteBuffer var0, ByteBuffer var1, int var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroy(PeerInfo peer_info, ByteBuffer handle) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer peer_handle = peer_info.lockAndGetHandle();
            try {
                LinuxContextImplementation.nDestroy(peer_handle, handle);
            }
            finally {
                peer_info.unlock();
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nDestroy(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;
}

