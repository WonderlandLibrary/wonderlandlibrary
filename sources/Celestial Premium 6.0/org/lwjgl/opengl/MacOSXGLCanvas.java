/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Graphics;

final class MacOSXGLCanvas
extends Canvas {
    private static final long serialVersionUID = 6916664741667434870L;
    private boolean canvas_painted;
    private boolean dirty;

    MacOSXGLCanvas() {
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void paint(Graphics g) {
        MacOSXGLCanvas macOSXGLCanvas = this;
        synchronized (macOSXGLCanvas) {
            this.dirty = true;
            this.canvas_painted = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean syncCanvasPainted() {
        boolean result;
        MacOSXGLCanvas macOSXGLCanvas = this;
        synchronized (macOSXGLCanvas) {
            result = this.canvas_painted;
            this.canvas_painted = false;
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean syncIsDirty() {
        boolean result;
        MacOSXGLCanvas macOSXGLCanvas = this;
        synchronized (macOSXGLCanvas) {
            result = this.dirty;
            this.dirty = false;
        }
        return result;
    }
}

