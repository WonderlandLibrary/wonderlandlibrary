// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.renderer;

public class DefaultLineStripRenderer implements LineStripRenderer
{
    private static SGL GL;
    
    public void end() {
        DefaultLineStripRenderer.GL.glEnd();
    }
    
    public void setAntiAlias(final boolean antialias) {
        if (antialias) {
            DefaultLineStripRenderer.GL.glEnable(2848);
        }
        else {
            DefaultLineStripRenderer.GL.glDisable(2848);
        }
    }
    
    public void setWidth(final float width) {
        DefaultLineStripRenderer.GL.glLineWidth(width);
    }
    
    public void start() {
        DefaultLineStripRenderer.GL.glBegin(3);
    }
    
    public void vertex(final float x, final float y) {
        DefaultLineStripRenderer.GL.glVertex2f(x, y);
    }
    
    public void color(final float r, final float g, final float b, final float a) {
        DefaultLineStripRenderer.GL.glColor4f(r, g, b, a);
    }
    
    public void setLineCaps(final boolean caps) {
    }
    
    public boolean applyGLLineFixes() {
        return true;
    }
    
    static {
        DefaultLineStripRenderer.GL = Renderer.get();
    }
}
