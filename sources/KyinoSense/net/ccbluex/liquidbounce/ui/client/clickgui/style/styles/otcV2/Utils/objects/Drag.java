/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.objects;

public class Drag {
    private float xPos;
    private float yPos;
    private float startX;
    private float startY;
    private boolean dragging;

    public Drag(float initialXVal, float initialYVal) {
        this.xPos = initialXVal;
        this.yPos = initialYVal;
    }

    public float getX() {
        return this.xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return this.yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public final void onDraw(int mouseX, int mouseY) {
        if (this.dragging) {
            this.xPos = (float)mouseX - this.startX;
            this.yPos = (float)mouseY - this.startY;
        }
    }

    public final void onDrawNegX(int mouseX, int mouseY) {
        if (this.dragging) {
            this.xPos = -((float)mouseX - this.startX);
            this.yPos = (float)mouseY - this.startY;
        }
    }

    public final void onClick(int mouseX, int mouseY, int button, boolean canDrag) {
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (int)((float)mouseX - this.xPos);
            this.startY = (int)((float)mouseY - this.yPos);
        }
    }

    public final void onClickAddX(int mouseX, int mouseY, int button, boolean canDrag) {
        if (button == 0 && canDrag) {
            this.dragging = true;
            this.startX = (int)((float)mouseX + this.xPos);
            this.startY = (int)((float)mouseY - this.yPos);
        }
    }

    public final void onRelease(int button) {
        if (button == 0) {
            this.dragging = false;
        }
    }
}

