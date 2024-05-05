package tech.atani.client.feature.guis.screens.clickgui.oldaugustus.window.component;

public abstract class Component {
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
