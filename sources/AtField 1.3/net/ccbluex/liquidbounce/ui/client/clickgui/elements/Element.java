/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.elements;

import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public class Element
extends MinecraftInstance {
    private int y;
    private boolean visible;
    private int height;
    private int x;
    private int width;

    public boolean isVisible() {
        return this.visible;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public void drawScreen(int n, int n2, float f) {
    }

    public void setLocation(int n, int n2) {
        this.x = n;
        this.y = n2;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int n) {
        this.x = n;
    }

    public void mouseClicked(int n, int n2, int n3) {
    }

    public void mouseReleased(int n, int n2, int n3) {
    }

    public int getY() {
        return this.y;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int n) {
        this.height = n;
    }
}

