/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable;

import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.lwjgl.input.Mouse;

public class DraggableComponent {
    private final int width;
    private final int height;
    private int x;
    private int y;
    private int color;
    private int lastX;
    private int lastY;
    public static ArrayList<DraggableModule> draggableModules = new ArrayList();
    private boolean dragging;
    private boolean canRender = true;

    public DraggableComponent(int x, int y, int width, int height, int color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public boolean isCanRender() {
        return this.canRender;
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
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.getXPosition() && mouseX <= this.getXPosition() + this.getWidth() && mouseY >= this.getYPosition() - this.getHeight() / 4 && mouseY <= this.getYPosition() - this.getHeight() / 4 + this.getHeight();
    }

    public void draw(int mouseX, int mouseY) {
        if (this.canRender) {
            boolean mouseOverY;
            this.draggingFix(mouseX, mouseY);
            Gui.drawRect(this.getXPosition(), this.getYPosition() - this.getHeight() / 4, this.getXPosition() + this.getWidth(), this.getYPosition() + this.getHeight(), this.getColor());
            boolean mouseOverX = mouseX >= this.getXPosition() && mouseX <= this.getXPosition() + this.getWidth();
            boolean bl = mouseOverY = mouseY >= this.getYPosition() - this.getHeight() / 4 && mouseY <= this.getYPosition() - this.getHeight() / 4 + this.getHeight();
            if (mouseOverX && mouseOverY) {
                if (Mouse.isButtonDown(0)) {
                    if (!this.dragging && draggableModules.size() <= 1) {
                        this.lastX = this.x - mouseX;
                        this.lastY = this.y - mouseY;
                        this.dragging = true;
                    }
                } else {
                    draggableModules.clear();
                }
            }
        }
    }

    private void draggingFix(int mouseX, int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
        }
    }
}

