/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengles.EGL
 *  org.lwjgl.opengles.EGLConfig
 *  org.lwjgl.opengles.EGLDisplay
 *  org.lwjgl.opengles.EGLSurface
 *  org.lwjgl.opengles.GLES20
 *  org.lwjgl.opengles.PixelFormat
 *  org.lwjgl.opengles.PowerManagementEventException
 *  org.lwjgl.opengles.Util
 */
package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.Context;
import org.lwjgl.opengl.ContextGLES;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.DrawableLWJGL;
import org.lwjgl.opengl.GlobalLock;
import org.lwjgl.opengl.PixelFormatLWJGL;
import org.lwjgl.opengles.ContextAttribs;
import org.lwjgl.opengles.EGL;
import org.lwjgl.opengles.EGLConfig;
import org.lwjgl.opengles.EGLDisplay;
import org.lwjgl.opengles.EGLSurface;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.PixelFormat;
import org.lwjgl.opengles.PowerManagementEventException;
import org.lwjgl.opengles.Util;

abstract class DrawableGLES
implements DrawableLWJGL {
    protected PixelFormat pixel_format;
    protected EGLDisplay eglDisplay;
    protected EGLConfig eglConfig;
    protected EGLSurface eglSurface;
    protected ContextGLES context;
    protected Drawable shared_drawable;

    protected DrawableGLES() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setPixelFormat(PixelFormatLWJGL pf) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.pixel_format = (PixelFormat)pf;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public PixelFormatLWJGL getPixelFormat() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.pixel_format;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void initialize(long window, long display_id, int eglSurfaceType, PixelFormat pf) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (this.eglSurface != null) {
                this.eglSurface.destroy();
                this.eglSurface = null;
            }
            if (this.eglDisplay != null) {
                this.eglDisplay.terminate();
                this.eglDisplay = null;
            }
            EGLDisplay eglDisplay = EGL.eglGetDisplay((long)((int)display_id));
            int[] attribs = new int[]{12329, 0, 12352, 4, 12333, 0};
            EGLConfig[] configs = eglDisplay.chooseConfig(pf.getAttribBuffer(eglDisplay, eglSurfaceType, attribs), null, BufferUtils.createIntBuffer(1));
            if (configs.length == 0) {
                throw new LWJGLException("No EGLConfigs found for the specified PixelFormat.");
            }
            EGLConfig eglConfig = pf.getBestMatch(configs);
            EGLSurface eglSurface = eglDisplay.createWindowSurface(eglConfig, window, null);
            pf.setSurfaceAttribs(eglSurface);
            this.eglDisplay = eglDisplay;
            this.eglConfig = eglConfig;
            this.eglSurface = eglSurface;
            if (this.context != null) {
                this.context.getEGLContext().setDisplay(eglDisplay);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void createContext(ContextAttribs attribs, Drawable shared_drawable) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.context = new ContextGLES(this, attribs, shared_drawable != null ? ((DrawableGLES)shared_drawable).getContext() : null);
            this.shared_drawable = shared_drawable;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Drawable getSharedDrawable() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.shared_drawable;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public EGLDisplay getEGLDisplay() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.eglDisplay;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public EGLConfig getEGLConfig() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.eglConfig;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public EGLSurface getEGLSurface() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.eglSurface;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ContextGLES getContext() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return this.context;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Context createSharedContext() throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.checkDestroyed();
            return new ContextGLES(this, this.context.getContextAttribs(), this.context);
        }
    }

    public void checkGLError() {
        Util.checkGLError();
    }

    public void setSwapInterval(int swap_interval) {
        ContextGLES.setSwapInterval(swap_interval);
    }

    public void swapBuffers() throws LWJGLException {
        ContextGLES.swapBuffers();
    }

    public void initContext(float r, float g, float b) {
        GLES20.glClearColor((float)r, (float)g, (float)b, (float)0.0f);
        GLES20.glClear((int)16384);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isCurrent() throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.checkDestroyed();
            return this.context.isCurrent();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void makeCurrent() throws LWJGLException, PowerManagementEventException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.checkDestroyed();
            this.context.makeCurrent();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseContext() throws LWJGLException, PowerManagementEventException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            this.checkDestroyed();
            if (this.context.isCurrent()) {
                this.context.releaseCurrent();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroy() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            try {
                if (this.context != null) {
                    try {
                        this.releaseContext();
                    }
                    catch (PowerManagementEventException e) {
                        // empty catch block
                    }
                    this.context.forceDestroy();
                    this.context = null;
                }
                if (this.eglSurface != null) {
                    this.eglSurface.destroy();
                    this.eglSurface = null;
                }
                if (this.eglDisplay != null) {
                    this.eglDisplay.terminate();
                    this.eglDisplay = null;
                }
                this.pixel_format = null;
                this.shared_drawable = null;
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
            }
        }
    }

    protected void checkDestroyed() {
        if (this.context == null) {
            throw new IllegalStateException("The Drawable has no context available.");
        }
    }

    public void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
        throw new UnsupportedOperationException();
    }
}

