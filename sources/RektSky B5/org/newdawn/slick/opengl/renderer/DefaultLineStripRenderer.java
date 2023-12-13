/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl.renderer;

import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class DefaultLineStripRenderer
implements LineStripRenderer {
    private SGL GL = Renderer.get();

    public void end() {
        this.GL.glEnd();
    }

    public void setAntiAlias(boolean antialias) {
        if (antialias) {
            this.GL.glEnable(2848);
        } else {
            this.GL.glDisable(2848);
        }
    }

    public void setWidth(float width) {
        this.GL.glLineWidth(width);
    }

    public void start() {
        this.GL.glBegin(3);
    }

    public void vertex(float x2, float y2) {
        this.GL.glVertex2f(x2, y2);
    }

    public void color(float r2, float g2, float b2, float a2) {
        this.GL.glColor4f(r2, g2, b2, a2);
    }

    public void setLineCaps(boolean caps) {
    }

    public boolean applyGLLineFixes() {
        return true;
    }
}

