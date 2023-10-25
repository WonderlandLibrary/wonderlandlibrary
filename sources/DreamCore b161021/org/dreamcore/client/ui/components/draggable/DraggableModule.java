package org.dreamcore.client.ui.components.draggable;

import org.dreamcore.client.helpers.Helper;

import java.awt.*;

public class DraggableModule implements Helper {

    public String name;
    public int x, y;
    public DraggableComponent drag;

    public DraggableModule(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.drag = new DraggableComponent(this.x, this.y, this.getWidth(), this.getHeight(), new Color(255, 255, 255, 0).getRGB());
    }

    public void draw() {

    }

    public void render(int mouseX, int mouseY) {
        drag.draw(mouseX, mouseY);
    }

    public int getX() {
        return drag.getXPosition();
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getY() {
        return drag.getYPosition();
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
