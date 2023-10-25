/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengles.GLContext
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.WindowsPeerInfo;
import org.lwjgl.opengles.GLContext;

final class WindowsDisplayPeerInfo
extends WindowsPeerInfo {
    final boolean egl;

    WindowsDisplayPeerInfo(boolean egl) throws LWJGLException {
        this.egl = egl;
        if (egl) {
            GLContext.loadOpenGLLibrary();
        } else {
            org.lwjgl.opengl.GLContext.loadOpenGLLibrary();
        }
    }

    void initDC(long hwnd, long hdc) throws LWJGLException {
        WindowsDisplayPeerInfo.nInitDC(this.getHandle(), hwnd, hdc);
    }

    private static native void nInitDC(ByteBuffer var0, long var1, long var3);

    protected void doLockAndInitHandle() throws LWJGLException {
    }

    protected void doUnlock() throws LWJGLException {
    }

    public void destroy() {
        super.destroy();
        if (this.egl) {
            GLContext.unloadOpenGLLibrary();
        } else {
            org.lwjgl.opengl.GLContext.unloadOpenGLLibrary();
        }
    }
}

