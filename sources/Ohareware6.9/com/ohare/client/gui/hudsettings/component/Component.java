package com.ohare.client.gui.hudsettings.component;

/**
 * made by Xen for OhareWare
 *
 * @since 7/26/2019
 **/
public class Component {
    private String label;
    public float posX, posY,height;

    public Component(String label, float posX, float posY) {
        this.label = label;
        this.posX = posX;
        this.posY = posY;
    }

    public void init() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

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

    public void keyTyped(char typedChar, int key) {
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }
}
