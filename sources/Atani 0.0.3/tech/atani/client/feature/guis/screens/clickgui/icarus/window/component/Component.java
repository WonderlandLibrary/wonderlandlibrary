package tech.atani.client.feature.guis.screens.clickgui.icarus.window.component;

import tech.atani.client.utility.interfaces.ColorPalette;

public abstract class Component implements ColorPalette {
    private float posX, posY;
    private float height;
    private float width;

    public Component(float posX, float posY, float height) {
        this.posX = posX;
        this.posY = posY;
        this.height = height;
    }

    public abstract float draw(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public float getFinalHeight() {
        return getHeight();
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
