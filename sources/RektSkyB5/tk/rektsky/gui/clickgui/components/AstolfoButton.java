/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

public abstract class AstolfoButton {
    public float x;
    public float y;
    public float width;
    public float height;

    public AstolfoButton(float x2, float y2, float width, float height) {
        this.x = x2;
        this.y = y2;
        this.width = width;
        this.height = height;
    }

    public abstract void drawPanel(int var1, int var2);

    public abstract void mouseAction(int var1, int var2, boolean var3, int var4);

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.x && (float)mouseX <= this.x + this.width && (float)mouseY > this.y && (float)mouseY < this.y + this.height;
    }
}

