/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.prism.PixelSource;

public abstract class PresentableState {
    protected Window window;
    protected View view;
    protected int nativeFrameBuffer;
    protected int windowX;
    protected int windowY;
    protected float windowAlpha;
    protected long nativeWindowHandle;
    protected long nativeView;
    protected int viewWidth;
    protected int viewHeight;
    protected float renderScaleX;
    protected float renderScaleY;
    protected int renderWidth;
    protected int renderHeight;
    protected float outputScaleX;
    protected float outputScaleY;
    protected int outputWidth;
    protected int outputHeight;
    protected int screenHeight;
    protected int screenWidth;
    protected boolean isWindowVisible;
    protected boolean isWindowMinimized;
    protected static final boolean hasWindowManager = Application.GetApplication().hasWindowManager();
    protected boolean isClosed;
    protected final int pixelFormat = Pixels.getNativeFormat();

    public int getWindowX() {
        return this.windowX;
    }

    public int getWindowY() {
        return this.windowY;
    }

    public int getWidth() {
        return this.viewWidth;
    }

    public int getHeight() {
        return this.viewHeight;
    }

    public int getRenderWidth() {
        return this.renderWidth;
    }

    public int getRenderHeight() {
        return this.renderHeight;
    }

    public int getOutputWidth() {
        return this.outputWidth;
    }

    public int getOutputHeight() {
        return this.outputHeight;
    }

    public float getRenderScaleX() {
        return this.renderScaleX;
    }

    public float getRenderScaleY() {
        return this.renderScaleY;
    }

    public float getOutputScaleX() {
        return this.outputScaleX;
    }

    public float getOutputScaleY() {
        return this.outputScaleY;
    }

    public float getAlpha() {
        return this.windowAlpha;
    }

    public long getNativeWindow() {
        return this.nativeWindowHandle;
    }

    public long getNativeView() {
        return this.nativeView;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public boolean isViewClosed() {
        return this.isClosed;
    }

    public boolean isWindowMinimized() {
        return this.isWindowMinimized;
    }

    public boolean isWindowVisible() {
        return this.isWindowVisible;
    }

    public boolean hasWindowManager() {
        return hasWindowManager;
    }

    public Window getWindow() {
        return this.window;
    }

    public boolean isMSAA() {
        return false;
    }

    public View getView() {
        return this.view;
    }

    public int getPixelFormat() {
        return this.pixelFormat;
    }

    public int getNativeFrameBuffer() {
        return this.nativeFrameBuffer;
    }

    public void lock() {
        if (this.view != null) {
            this.view.lock();
            this.nativeFrameBuffer = this.view.getNativeFrameBuffer();
        }
    }

    public void unlock() {
        if (this.view != null) {
            this.view.unlock();
        }
    }

    public void uploadPixels(PixelSource pixelSource) {
        Pixels pixels = pixelSource.getLatestPixels();
        if (pixels != null) {
            try {
                this.view.uploadPixels(pixels);
            }
            finally {
                pixelSource.doneWithPixels(pixels);
            }
        }
    }

    private int scale(int n, float f, float f2) {
        return f == f2 ? n : (int)Math.ceil((float)n * f2 / f);
    }

    protected void update(float f, float f2, float f3, float f4, float f5, float f6) {
        this.renderScaleX = f3;
        this.renderScaleY = f4;
        this.outputScaleX = f5;
        this.outputScaleY = f6;
        if (f3 == f && f4 == f2) {
            this.renderWidth = this.viewWidth;
            this.renderHeight = this.viewHeight;
        } else {
            this.renderWidth = this.scale(this.viewWidth, f, f3);
            this.renderHeight = this.scale(this.viewHeight, f2, f4);
        }
        if (f5 == f && f6 == f2) {
            this.outputWidth = this.viewWidth;
            this.outputHeight = this.viewHeight;
        } else if (f5 == f3 && f6 == f4) {
            this.outputWidth = this.renderWidth;
            this.outputHeight = this.renderHeight;
        } else {
            this.outputWidth = this.scale(this.viewWidth, f, f5);
            this.outputHeight = this.scale(this.viewHeight, f2, f6);
        }
    }

    public void update() {
        if (this.view != null) {
            this.viewWidth = this.view.getWidth();
            this.viewHeight = this.view.getHeight();
            this.window = this.view.getWindow();
        } else {
            this.viewHeight = -1;
            this.viewWidth = -1;
            this.window = null;
        }
        if (this.window != null) {
            this.windowX = this.window.getX();
            this.windowY = this.window.getY();
            this.windowAlpha = this.window.getAlpha();
            this.nativeView = this.view.getNativeView();
            this.nativeWindowHandle = this.window.getNativeWindow();
            this.isClosed = this.view.isClosed();
            this.isWindowVisible = this.window.isVisible();
            this.isWindowMinimized = this.window.isMinimized();
            this.update(this.window.getPlatformScaleX(), this.window.getPlatformScaleY(), this.window.getRenderScaleX(), this.window.getRenderScaleY(), this.window.getOutputScaleX(), this.window.getOutputScaleY());
            Screen screen = this.window.getScreen();
            if (screen != null) {
                this.screenHeight = screen.getHeight();
                this.screenWidth = screen.getWidth();
            }
        } else {
            this.nativeView = -1L;
            this.nativeWindowHandle = -1L;
            this.isClosed = true;
        }
    }
}

