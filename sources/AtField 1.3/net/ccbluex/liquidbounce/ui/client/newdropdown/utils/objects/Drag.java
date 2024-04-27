/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.objects;

public class Drag {
    private float yPos;
    private boolean dragging;
    private float xPos;
    private float startX;
    private float startY;

    public float getY() {
        return this.yPos;
    }

    public void setY(float f) {
        this.yPos = f;
    }

    public float getX() {
        return this.xPos;
    }

    public final void onRelease(int n) {
        if (n == 0) {
            this.dragging = false;
        }
    }

    public final void onDraw(int n, int n2) {
        if (this.dragging) {
            this.xPos = (float)n - this.startX;
            this.yPos = (float)n2 - this.startY;
        }
    }

    public final void onClickAddX(int n, int n2, int n3, boolean bl) {
        if (n3 == 0 && bl) {
            this.dragging = true;
            this.startX = (int)((float)n + this.xPos);
            this.startY = (int)((float)n2 - this.yPos);
        }
    }

    public void setX(float f) {
        this.xPos = f;
    }

    public final void onDrawNegX(int n, int n2) {
        if (this.dragging) {
            this.xPos = -((float)n - this.startX);
            this.yPos = (float)n2 - this.startY;
        }
    }

    public final void onClick(int n, int n2, int n3, boolean bl) {
        if (n3 == 0 && bl) {
            this.dragging = true;
            this.startX = (int)((float)n - this.xPos);
            this.startY = (int)((float)n2 - this.yPos);
        }
    }

    public Drag(float f, float f2) {
        this.xPos = f;
        this.yPos = f2;
    }
}

