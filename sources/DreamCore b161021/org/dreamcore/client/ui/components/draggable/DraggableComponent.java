package org.dreamcore.client.ui.components.draggable;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class DraggableComponent {

    private final int width;
    private final int height;
    private int x;
    private int y;
    private int color;
    private int lastX;
    private int lastY;

    private boolean dragging;
    private boolean canRender = true;

    public DraggableComponent(int x, int y, int width, int height, int color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean isCanRender() {
        return canRender;
    }

    public void setCanRender(boolean canRender) {
        this.canRender = canRender;
    }

    public int getXPosition() {
        return this.x;
    }

    public void setXPosition(int x) {
        this.x = x;
    }

    public int getYPosition() {
        return this.y;
    }

    public void setYPosition(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(int mouseX, int mouseY) {
        if (canRender) {
            draggingFix(mouseX, mouseY);
            Gui.drawRect(this.getXPosition(), this.getYPosition() - this.getHeight() / 4, this.getXPosition() + this.getWidth(), this.getYPosition() + this.getHeight(), this.getColor());
            boolean mouseOverX = (mouseX >= this.getXPosition() && mouseX <= this.getXPosition() + this.getWidth());
            boolean mouseOverY = (mouseY >= this.getYPosition() - this.getHeight() / 4 && mouseY <= this.getYPosition() - this.getHeight() / 4 + this.getHeight());
            if (mouseOverX && mouseOverY) {
                if (Mouse.isButtonDown(0)) {
                    if (!this.dragging) {
                        this.lastX = x - mouseX;
                        this.lastY = y - mouseY;
                        this.dragging = true;
                    }
                }
            }
        }
    }

    private void draggingFix(int mouseX, int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) this.dragging = false;
        }
    }
}