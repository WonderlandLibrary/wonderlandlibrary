/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.celestial.client.ui.components.draggable.DraggableComponent;

public class DraggableModule {
    public String name;
    public int x;
    public int y;
    public DraggableComponent drag;
    protected Minecraft mc = Minecraft.getMinecraft();

    public DraggableModule(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.drag = new DraggableComponent(this.x, this.y, this.getWidth(), this.getHeight(), new Color(255, 255, 255, 0).getRGB());
    }

    public void draw() {
    }

    public void render(int mouseX, int mouseY) {
        this.drag.draw(mouseX, mouseY);
    }

    public int getX() {
        return this.drag.getXPosition();
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getY() {
        return this.drag.getYPosition();
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX2() {
        return this.x;
    }

    public void setX2(int x) {
        this.drag.setXPosition(x);
    }

    public int getY2() {
        return this.y;
    }

    public void setY2(int y) {
        this.drag.setYPosition(y);
    }

    public int getWidth() {
        return 50;
    }

    public int getHeight() {
        return 50;
    }
}

