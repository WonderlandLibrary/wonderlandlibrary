package wtf.evolution.editor;

import wtf.evolution.module.Module;

public class Drag {

    public final Module module;
    public final String name;

    private float width, height;
    public boolean dragging;
    public float startX;
    public float startY;

    public float xPos, yPos;

    public Drag(Module module, String name, float initialXVal, float initialYVal) {
        this.module = module;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public void onClick(int mouseX, int mouseY) {
        if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
            dragging = true;
            startX = mouseX - xPos;
            startY = mouseY - yPos;
        }
    }

    public void onRelease(int mouseX, int mouseY) {
        dragging = false;
    }



}
