/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTSurfaceLock;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.WindowsPeerInfo;

final class WindowsAWTGLCanvasPeerInfo
extends WindowsPeerInfo {
    private final Canvas component;
    private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
    private final PixelFormat pixel_format;
    private boolean has_pixel_format;

    WindowsAWTGLCanvasPeerInfo(Canvas component, PixelFormat pixel_format) {
        this.component = component;
        this.pixel_format = pixel_format;
    }

    protected void doLockAndInitHandle() throws LWJGLException {
        WindowsAWTGLCanvasPeerInfo.nInitHandle(this.awt_surface.lockAndGetHandle(this.component), this.getHandle());
        if (!this.has_pixel_format && this.pixel_format != null) {
            int format = WindowsAWTGLCanvasPeerInfo.choosePixelFormat(this.getHdc(), this.component.getX(), this.component.getY(), this.pixel_format, null, true, true, false, true);
            WindowsAWTGLCanvasPeerInfo.setPixelFormat(this.getHdc(), format);
            this.has_pixel_format = true;
        }
    }

    private static native void nInitHandle(ByteBuffer var0, ByteBuffer var1) throws LWJGLException;

    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }
}

