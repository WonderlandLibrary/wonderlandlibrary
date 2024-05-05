// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class ScrollingText
{
    private final String text;
    private final float stringWidth;
    private final int width;
    private final int height;
    private final int backgroundColor;
    private final int transparentBackgroundColor;
    private final int textColor;
    private float scale;
    private ScrollingText parent;
    private ScrollingText child;
    private long lastTime;
    private long startOfWait;
    private float offset;
    private State state;
    
    public ScrollingText(final String text, final int width, final int height, final int backgroundColor, final int textColor) {
        this.scale = 1.0f;
        this.state = State.LEFT;
        this.text = text;
        this.stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) * (height / 10.0f);
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.transparentBackgroundColor = ((backgroundColor >> 16 & 0xFF) << 16 | (backgroundColor >> 8 & 0xFF) << 8 | (backgroundColor & 0xFF));
        this.textColor = textColor;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setParent(final ScrollingText parent) {
        this.parent = parent;
    }
    
    public void setChild(final ScrollingText child) {
        this.child = child;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public void setScale(final float scale) {
        this.scale = scale;
    }
    
    public void render(final float x, final float y) {
        if (this.stringWidth > this.width) {
            final double delta = (Minecraft.getSystemTime() - this.lastTime) / 50.0;
            this.lastTime = Minecraft.getSystemTime();
            switch (this.state) {
                case LEFT: {
                    if (this.startOfWait == 0L) {
                        this.startOfWait = this.lastTime;
                    }
                    if ((this.child == null || this.child.stringWidth <= this.child.width || (this.child.state == State.LEFT && this.child.startOfWait != 0L && this.child.lastTime - this.child.startOfWait > 4000L)) && (this.parent == null || this.parent.stringWidth <= this.parent.width || this.parent.state != State.LEFT) && this.lastTime - this.startOfWait > 4000L) {
                        this.startOfWait = 0L;
                        this.state = State.SCROLL_RIGHT;
                        break;
                    }
                    break;
                }
                case SCROLL_RIGHT: {
                    this.offset += (float)delta;
                    if (this.offset >= this.stringWidth - this.width) {
                        this.offset = this.stringWidth - this.width;
                        this.state = State.RIGHT;
                        break;
                    }
                    break;
                }
                case RIGHT: {
                    if (this.startOfWait == 0L) {
                        this.startOfWait = this.lastTime;
                    }
                    if ((this.child == null || this.child.stringWidth <= this.child.width || (this.child.state == State.RIGHT && this.child.startOfWait != 0L && this.child.lastTime - this.child.startOfWait > 2500L)) && (this.parent == null || this.parent.stringWidth <= this.parent.width || this.parent.state != State.RIGHT) && this.lastTime - this.startOfWait > 2500L) {
                        this.startOfWait = 0L;
                        this.state = State.SCROLL_LEFT;
                        break;
                    }
                    break;
                }
                case SCROLL_LEFT: {
                    this.offset -= (float)delta;
                    if (this.offset <= 0.0f) {
                        this.offset = 0.0f;
                        this.state = State.LEFT;
                        break;
                    }
                    break;
                }
            }
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final float scaleFactor = res.getScaleFactor() * this.scale;
        Depth.pre();
        Depth.mask();
        this.drawScaledString(this.text, x - this.offset, y, -1, this.height / 10.0f);
        Depth.render(518);
        RenderUtils.rectangle(x, y, x + this.width, y + this.height, Colors.getColor(175));
        Depth.post();
        if (this.offset > 0.0f) {
            GlStateManager.pushMatrix();
            RenderUtils.drawGradientSideways(x, y, x + 5.0f, y + this.height, this.backgroundColor, this.transparentBackgroundColor);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
            GlStateManager.popMatrix();
        }
        if (this.stringWidth > this.width && this.offset < this.stringWidth - this.width) {
            GlStateManager.pushMatrix();
            RenderUtils.drawGradientSideways(x + this.width - 5.0f, y, x + this.width, y + this.height, this.transparentBackgroundColor, this.backgroundColor);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
            GlStateManager.popMatrix();
        }
    }
    
    private void drawScaledString(final String string, final float x, final float y, final int color, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1.0f);
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRendererObj.drawString(string, 0, 0, color);
        GlStateManager.popMatrix();
    }
    
    private enum State
    {
        LEFT, 
        SCROLL_RIGHT, 
        RIGHT, 
        SCROLL_LEFT;
    }
}
