// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

public class Scrollbar
{
    public float startX;
    public float startY;
    public float endY;
    public float currentScroll;
    public float maxScroll;
    public float minScroll;
    
    public void drawScrollBar(final int mouseX, final int mouseY) {
        this.maxScroll += this.currentScroll / 14.0f;
        final double current = (this.currentScroll - this.minScroll) / (this.maxScroll - this.minScroll);
        RenderUtils.relativeRect(this.startX, this.startY, this.startX + 2.0f, this.startY + this.endY, Integer.MIN_VALUE);
        RenderUtils.relativeRect(this.startX, (float)(this.startY + current * this.endY), this.startX + 2.0f, (float)(this.startY + 25.0f + current * this.endY), Integer.MAX_VALUE);
    }
    
    public void setInformation(final float startX, final float startY, final float endY, final float currentScroll, final float maxScroll, final float minScroll) {
        this.startX = startX;
        this.startY = startY;
        this.endY = endY;
        this.currentScroll = currentScroll;
        this.maxScroll = maxScroll;
        this.minScroll = minScroll;
    }
    
    public float getStartX() {
        return this.startX;
    }
    
    public float getStartY() {
        return this.startY;
    }
    
    public float getEndY() {
        return this.endY;
    }
    
    public float getCurrentScroll() {
        return this.currentScroll;
    }
    
    public float getMaxScroll() {
        return this.maxScroll;
    }
    
    public float getMinScroll() {
        return this.minScroll;
    }
}
