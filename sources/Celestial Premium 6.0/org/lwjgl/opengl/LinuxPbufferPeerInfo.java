/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.LinuxDisplay;
import org.lwjgl.opengl.LinuxPeerInfo;
import org.lwjgl.opengl.PixelFormat;

final class LinuxPbufferPeerInfo
extends LinuxPeerInfo {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    LinuxPbufferPeerInfo(int width, int height, PixelFormat pixel_format) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            GLContext.loadOpenGLLibrary();
            try {
                LinuxDisplay.incDisplay();
                try {
                    LinuxPbufferPeerInfo.nInitHandle(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), width, height, pixel_format);
                }
                catch (LWJGLException e) {
                    LinuxDisplay.decDisplay();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                GLContext.unloadOpenGLLibrary();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nInitHandle(long var0, int var2, ByteBuffer var3, int var4, int var5, PixelFormat var6) throws LWJGLException;

    public void destroy() {
        LinuxDisplay.lockAWT();
        LinuxPbufferPeerInfo.nDestroy(this.getHandle());
        LinuxDisplay.decDisplay();
        GLContext.unloadOpenGLLibrary();
        LinuxDisplay.unlockAWT();
    }

    private static native void nDestroy(ByteBuffer var0);

    protected void doLockAndInitHandle() throws LWJGLException {
    }

    protected void doUnlock() throws LWJGLException {
    }
}

