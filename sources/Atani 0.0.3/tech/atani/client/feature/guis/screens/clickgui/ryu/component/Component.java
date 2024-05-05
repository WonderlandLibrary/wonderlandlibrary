package tech.atani.client.feature.guis.screens.clickgui.ryu.component;

import tech.atani.client.utility.interfaces.ColorPalette;

import java.util.ArrayList;

public abstract class Component implements ColorPalette {

    protected final ArrayList<Component> subComponents = new ArrayList<>();
    private float posX, posY, baseWidth, baseHeight;
    private float addY, addX;
    private boolean visible = true;

    public Component(float posX, float posY, float baseWidth, float baseHeight) {
        this.posX = posX;
        this.posY = posY;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    public float getAddX() {
        return addX;
    }

    public void setAddX(float addX) {
        this.addX = addX;
    }

    public abstract void drawScreen(int mouseX, int mouseY);

    public abstract void mouseClick(int mouseX, int mouseY, int mouseButton);

    public float getFinalHeight() {
        return this.baseHeight;
    }

    public float getFinalWidth() {
        return this.baseWidth;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getBaseWidth() {
        return baseWidth;
    }

    public float getBaseHeight() {
        return baseHeight;
    }

    public float getAddY() {
        return addY;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setBaseWidth(float baseWidth) {
        this.baseWidth = baseWidth;
    }

    public void setBaseHeight(float baseHeight) {
        this.baseHeight = baseHeight;
    }

    public void setAddY(float addY) {
        this.addY = addY;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
