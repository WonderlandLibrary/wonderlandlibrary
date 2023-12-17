// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.drag;

public class Drag
{
    private float xPos;
    private float yPos;
    private float startX;
    private float startY;
    private boolean dragging;
    
    public Drag(final float initialXVal, final float initialYVal) {
        this.xPos = initialXVal;
        this.yPos = initialYVal;
    }
    
    public float getX() {
        return this.xPos;
    }
    
    public void setX(final float x) {
        this.xPos = x;
    }
    
    public float getY() {
        return this.yPos;
    }
    
    public void setY(final float y) {
        this.yPos = y;
    }
    
    public final void onDraw(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.xPos = mouseX - this.startX;
            this.yPos = mouseY - this.startY;
        }
    }
    
    public final void onDrawNegX(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.xPos = -(mouseX - this.startX);
            this.yPos = mouseY - this.startY;
        }
    }
    
    public final void onClick(final int mouseX, final int mouseY, final int button, final boolean canDrag) {
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (float)(int)(mouseX - this.xPos);
            this.startY = (float)(int)(mouseY - this.yPos);
        }
    }
    
    public final void onClickAddX(final int mouseX, final int mouseY, final int button, final boolean canDrag) {
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (float)(int)(mouseX + this.xPos);
            this.startY = (float)(int)(mouseY - this.yPos);
        }
    }
    
    public final void onRelease(final int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
}
