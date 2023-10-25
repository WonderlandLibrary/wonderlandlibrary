/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengles.GLContext
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.LinuxDisplay;
import org.lwjgl.opengl.LinuxPeerInfo;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengles.GLContext;

final class LinuxDisplayPeerInfo
extends LinuxPeerInfo {
    final boolean egl;

    LinuxDisplayPeerInfo() throws LWJGLException {
        this.egl = true;
        GLContext.loadOpenGLLibrary();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    LinuxDisplayPeerInfo(PixelFormat pixel_format) throws LWJGLException {
        this.egl = false;
        LinuxDisplay.lockAWT();
        try {
            org.lwjgl.opengl.GLContext.loadOpenGLLibrary();
            try {
                LinuxDisplay.incDisplay();
                try {
                    LinuxDisplayPeerInfo.initDefaultPeerInfo(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.getHandle(), pixel_format);
                }
                catch (LWJGLException e) {
                    LinuxDisplay.decDisplay();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                org.lwjgl.opengl.GLContext.unloadOpenGLLibrary();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void initDefaultPeerInfo(long var0, int var2, ByteBuffer var3, PixelFormat var4) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void doLockAndInitHandle() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplayPeerInfo.initDrawable(LinuxDisplay.getWindow(), this.getHandle());
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void initDrawable(long var0, ByteBuffer var2);

    protected void doUnlock() throws LWJGLException {
    }

    public void destroy() {
        super.destroy();
        if (this.egl) {
            GLContext.unloadOpenGLLibrary();
        } else {
            LinuxDisplay.lockAWT();
            LinuxDisplay.decDisplay();
            org.lwjgl.opengl.GLContext.unloadOpenGLLibrary();
            LinuxDisplay.unlockAWT();
        }
    }
}

