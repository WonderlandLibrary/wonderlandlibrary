// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

public class Drag
{
    private float x;
    private float y;
    private float initialX;
    private float initialY;
    private float startX;
    private float startY;
    private boolean dragging;
    
    public Drag(final float initialXVal, final float initialYVal) {
        this.initialX = initialXVal;
        this.initialY = initialYVal;
        this.x = initialXVal;
        this.y = initialYVal;
    }
    
    public final void onDraw(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.x = mouseX - this.startX;
            this.y = mouseY - this.startY;
        }
    }
    
    public final void onClick(final int mouseX, final int mouseY, final int button, final boolean canDrag) {
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (float)(int)(mouseX - this.x);
            this.startY = (float)(int)(mouseY - this.y);
        }
    }
    
    public final void onRelease(final int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getInitialX() {
        return this.initialX;
    }
    
    public float getInitialY() {
        return this.initialY;
    }
    
    public float getStartX() {
        return this.startX;
    }
    
    public float getStartY() {
        return this.startY;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setInitialX(final float initialX) {
        this.initialX = initialX;
    }
    
    public void setInitialY(final float initialY) {
        this.initialY = initialY;
    }
    
    public void setStartX(final float startX) {
        this.startX = startX;
    }
    
    public void setStartY(final float startY) {
        this.startY = startY;
    }
    
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
}
