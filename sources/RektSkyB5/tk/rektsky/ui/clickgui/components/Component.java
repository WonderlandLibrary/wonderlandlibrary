/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.ui.clickgui.components;

public class Component {
    public int x;
    public int y;

    public Component(int x2, int y2) {
        this.x = x2;
        this.y = y2;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public void draw(int mouseX, int mouseY) {
    }

    public void onClick(int x2, int y2, int mouseBtn) {
    }

    public boolean isMouseOverObj(int x2, int y2) {
        if (this.x < x2 && this.y < y2) {
            return this.x > x2 + this.getWidth() && this.y > y2 + this.getHeight();
        }
        return false;
    }
}

