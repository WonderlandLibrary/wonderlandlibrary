package com.ohare.client.gui.click.component;

/**
 * made by oHare for oHareWare
 *
 * @since 6/4/2019
 **/

public class Component {
    private String label;
    private float posX, posY, width, height;
    public Component(String label, float posX, float posY, float width, float height) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) { }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) { }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    public String getLabel() {
        return label;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void keyTyped(char typedChar, int key) { }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));  }
}
